begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|bundle
operator|.
name|core
operator|.
name|internal
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
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLInputFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

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

begin_class
specifier|public
class|class
name|MavenConfigService
block|{
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MavenConfigService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ConfigurationAdmin
name|configurationAdmin
decl_stmt|;
specifier|public
name|MavenConfigService
parameter_list|(
name|ConfigurationAdmin
name|configurationAdmin
parameter_list|)
block|{
name|this
operator|.
name|configurationAdmin
operator|=
name|configurationAdmin
expr_stmt|;
block|}
specifier|public
name|File
name|getLocalRepository
parameter_list|()
block|{
name|String
name|path
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
literal|"org.ops4j.pax.url.mvn"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dict
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|path
operator|=
name|getLocalRepoFromConfig
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Error retrieving maven configuration"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|path
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
literal|".m2"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"repository"
expr_stmt|;
block|}
name|int
name|index
init|=
name|path
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
name|path
operator|.
name|substring
argument_list|(
name|index
argument_list|)
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|File
argument_list|(
name|path
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
return|;
block|}
block|}
specifier|static
name|String
name|getLocalRepoFromConfig
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dict
parameter_list|)
throws|throws
name|XMLStreamException
throws|,
name|FileNotFoundException
block|{
name|String
name|path
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dict
operator|!=
literal|null
condition|)
block|{
name|path
operator|=
operator|(
name|String
operator|)
name|dict
operator|.
name|get
argument_list|(
literal|"org.ops4j.pax.url.mvn.localRepository"
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|String
name|settings
init|=
operator|(
name|String
operator|)
name|dict
operator|.
name|get
argument_list|(
literal|"org.ops4j.pax.url.mvn.settings"
argument_list|)
decl_stmt|;
if|if
condition|(
name|settings
operator|!=
literal|null
condition|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|settings
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|XMLInputFactory
operator|.
name|newFactory
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|event
decl_stmt|;
name|String
name|elementName
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|event
operator|=
name|reader
operator|.
name|next
argument_list|()
operator|)
operator|!=
name|XMLStreamConstants
operator|.
name|END_DOCUMENT
condition|)
block|{
if|if
condition|(
name|event
operator|==
name|XMLStreamConstants
operator|.
name|START_ELEMENT
condition|)
block|{
name|elementName
operator|=
name|reader
operator|.
name|getLocalName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|==
name|XMLStreamConstants
operator|.
name|END_ELEMENT
condition|)
block|{
name|elementName
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|==
name|XMLStreamConstants
operator|.
name|CHARACTERS
operator|&&
literal|"localRepository"
operator|.
name|equals
argument_list|(
name|elementName
argument_list|)
condition|)
block|{
name|path
operator|=
name|reader
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

