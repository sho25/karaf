begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|impl
operator|.
name|console
package|;
end_package

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Branding
block|{
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Branding
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Branding
parameter_list|()
block|{ }
specifier|public
specifier|static
name|Properties
name|loadBrandingProperties
parameter_list|(
name|boolean
name|ssh
parameter_list|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|ssh
condition|?
literal|"branding-ssh.properties"
else|:
literal|"branding.properties"
decl_stmt|;
name|loadPropsFromResource
argument_list|(
name|props
argument_list|,
literal|"org/apache/karaf/shell/console/"
operator|+
name|name
argument_list|)
expr_stmt|;
name|loadPropsFromResource
argument_list|(
name|props
argument_list|,
literal|"org/apache/karaf/branding/"
operator|+
name|name
argument_list|)
expr_stmt|;
name|loadPropsFromFile
argument_list|(
name|props
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
operator|+
literal|"/"
operator|+
name|name
argument_list|)
expr_stmt|;
return|return
name|props
return|;
block|}
specifier|private
specifier|static
name|void
name|loadPropsFromFile
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|fileName
parameter_list|)
block|{
try|try
init|(
name|FileInputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|fileName
argument_list|)
init|)
block|{
name|loadProps
argument_list|(
name|props
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Could not load branding."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|loadPropsFromResource
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|resource
parameter_list|)
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|Branding
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
init|)
block|{
name|loadProps
argument_list|(
name|props
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Could not load branding."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|loadProps
parameter_list|(
name|Properties
name|props
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

