begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|deployer
operator|.
name|wrap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|fileinstall
operator|.
name|ArtifactUrlTransformer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|DeployerUtils
import|;
end_import

begin_comment
comment|/**  * A deployment listener that listens for non OSGi jar deployements.  */
end_comment

begin_class
specifier|public
class|class
name|WrapDeploymentListener
implements|implements
name|ArtifactUrlTransformer
block|{
specifier|public
name|boolean
name|canHandle
parameter_list|(
name|File
name|artifact
parameter_list|)
block|{
try|try
block|{
comment|// only handle .jar files
if|if
condition|(
operator|!
name|artifact
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
init|(
name|JarFile
name|jar
init|=
operator|new
name|JarFile
argument_list|(
name|artifact
argument_list|)
init|)
block|{
comment|// only handle non OSGi jar
name|Manifest
name|manifest
init|=
name|jar
operator|.
name|getManifest
argument_list|()
decl_stmt|;
return|return
name|manifest
operator|==
literal|null
operator|||
name|manifest
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|getValue
argument_list|(
operator|new
name|Attributes
operator|.
name|Name
argument_list|(
literal|"Bundle-SymbolicName"
argument_list|)
argument_list|)
operator|==
literal|null
operator|||
name|manifest
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|getValue
argument_list|(
operator|new
name|Attributes
operator|.
name|Name
argument_list|(
literal|"Bundle-Version"
argument_list|)
argument_list|)
operator|==
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|URL
name|transform
parameter_list|(
name|URL
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|path
init|=
name|artifact
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|path
operator|.
name|substring
argument_list|(
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|String
index|[]
name|nv
init|=
name|DeployerUtils
operator|.
name|extractNameVersionType
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|URL
argument_list|(
literal|"wrap"
argument_list|,
literal|null
argument_list|,
name|artifact
operator|.
name|toExternalForm
argument_list|()
operator|+
literal|"$Bundle-SymbolicName="
operator|+
name|nv
index|[
literal|0
index|]
operator|+
literal|"&Bundle-Version="
operator|+
name|nv
index|[
literal|1
index|]
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
operator|new
name|URL
argument_list|(
literal|"wrap"
argument_list|,
literal|null
argument_list|,
name|artifact
operator|.
name|toExternalForm
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

