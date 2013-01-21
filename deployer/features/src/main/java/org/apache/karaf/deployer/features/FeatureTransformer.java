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
name|features
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|JarOutputStream
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
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_comment
comment|/**  * Transform a feature descriptor into an bundles bundle  */
end_comment

begin_class
specifier|public
class|class
name|FeatureTransformer
block|{
specifier|public
specifier|static
name|void
name|transform
parameter_list|(
name|URL
name|url
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Heuristicly retrieve name and version
name|String
name|name
init|=
name|getPath
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|str
init|=
name|DeployerUtils
operator|.
name|extractNameVersionType
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// Create manifest
name|Manifest
name|m
init|=
operator|new
name|Manifest
argument_list|()
decl_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
literal|"Manifest-Version"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_MANIFESTVERSION
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
name|str
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|,
name|str
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
comment|// Put content
name|JarOutputStream
name|out
init|=
operator|new
name|JarOutputStream
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|ZipEntry
name|e
init|=
operator|new
name|ZipEntry
argument_list|(
name|JarFile
operator|.
name|MANIFEST_NAME
argument_list|)
decl_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|m
operator|.
name|write
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|e
operator|=
operator|new
name|ZipEntry
argument_list|(
literal|"META-INF/"
argument_list|)
expr_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|e
operator|=
operator|new
name|ZipEntry
argument_list|(
literal|"META-INF/"
operator|+
name|FeatureDeploymentListener
operator|.
name|FEATURE_PATH
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|e
operator|=
operator|new
name|ZipEntry
argument_list|(
literal|"META-INF/"
operator|+
name|FeatureDeploymentListener
operator|.
name|FEATURE_PATH
operator|+
literal|"/"
operator|+
name|name
argument_list|)
expr_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|InputStream
name|fis
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|copyInputStream
argument_list|(
name|fis
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getPath
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
if|if
condition|(
name|url
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
literal|"mvn"
argument_list|)
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|url
operator|.
name|toExternalForm
argument_list|()
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|groupId
decl_stmt|;
name|String
name|artifactId
decl_stmt|;
name|String
name|version
decl_stmt|;
name|String
name|type
decl_stmt|;
name|String
name|qualifier
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
argument_list|<
literal|3
operator|||
name|parts
operator|.
name|length
argument_list|>
literal|5
condition|)
block|{
return|return
name|url
operator|.
name|getPath
argument_list|()
return|;
block|}
name|groupId
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|artifactId
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
name|version
operator|=
name|parts
index|[
literal|2
index|]
expr_stmt|;
name|type
operator|=
operator|(
name|parts
operator|.
name|length
operator|>=
literal|4
operator|)
condition|?
literal|"."
operator|+
name|parts
index|[
literal|3
index|]
else|:
literal|".jar"
expr_stmt|;
name|qualifier
operator|=
operator|(
name|parts
operator|.
name|length
operator|>=
literal|5
operator|)
condition|?
literal|"-"
operator|+
name|parts
index|[
literal|4
index|]
else|:
literal|""
expr_stmt|;
return|return
name|groupId
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"/"
operator|+
name|version
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"-"
operator|+
name|version
operator|+
name|qualifier
operator|+
name|type
return|;
block|}
return|return
name|url
operator|.
name|getPath
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|copyInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|int
name|len
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
while|while
condition|(
name|len
operator|>=
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|len
operator|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

