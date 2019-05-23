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
name|config
operator|.
name|core
operator|.
name|impl
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|utils
operator|.
name|properties
operator|.
name|TypedProperties
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
name|config
operator|.
name|core
operator|.
name|ConfigRepository
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
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
name|ConfigRepositoryImpl
implements|implements
name|ConfigRepository
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ConfigRepositoryImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FILEINSTALL_FILE_NAME
init|=
literal|"felix.fileinstall.filename"
decl_stmt|;
specifier|private
name|ConfigurationAdmin
name|configAdmin
decl_stmt|;
specifier|public
name|ConfigRepositoryImpl
parameter_list|(
name|ConfigurationAdmin
name|configAdmin
parameter_list|)
block|{
name|this
operator|.
name|configAdmin
operator|=
name|configAdmin
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.karaf.shell.config.impl.ConfigRepository#update(java.lang.String, java.util.Dictionary, boolean)      */
annotation|@
name|Override
specifier|public
name|void
name|update
parameter_list|(
name|String
name|pid
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Updating configuration {}"
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|Configuration
name|cfg
init|=
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|,
literal|"?"
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dict
init|=
name|cfg
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|TypedProperties
name|props
init|=
operator|new
name|TypedProperties
argument_list|()
decl_stmt|;
name|File
name|file
init|=
name|getCfgFileFromProperties
argument_list|(
name|dict
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|load
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|props
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|props
operator|.
name|keySet
argument_list|()
operator|.
name|retainAll
argument_list|(
name|properties
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|props
operator|.
name|clear
argument_list|()
expr_stmt|;
name|props
operator|.
name|load
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|FILEINSTALL_FILE_NAME
argument_list|,
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|,
name|pid
operator|+
literal|".cfg"
argument_list|)
expr_stmt|;
name|props
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|props
operator|.
name|keySet
argument_list|()
operator|.
name|retainAll
argument_list|(
name|properties
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|FILEINSTALL_FILE_NAME
argument_list|,
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|cfg
operator|.
name|update
argument_list|(
operator|new
name|Hashtable
argument_list|<>
argument_list|(
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error updating config"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/* (non-Javadoc)      * @see org.apache.karaf.shell.config.impl.ConfigRepository#delete(java.lang.String)      */
annotation|@
name|Override
specifier|public
name|void
name|delete
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Deleting configuration {}"
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|exists
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|Exception
block|{
name|Configuration
index|[]
name|configurations
init|=
name|configAdmin
operator|.
name|listConfigurations
argument_list|(
literal|"(service.pid="
operator|+
name|pid
operator|+
literal|")"
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurations
operator|==
literal|null
operator|||
name|configurations
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|File
name|getCfgFileFromProperties
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|MalformedURLException
block|{
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|Object
name|val
init|=
name|properties
operator|.
name|get
argument_list|(
name|FILEINSTALL_FILE_NAME
argument_list|)
decl_stmt|;
return|return
name|getCfgFileFromProperty
argument_list|(
name|val
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|File
name|getCfgFileFromProperty
parameter_list|(
name|Object
name|val
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|MalformedURLException
block|{
if|if
condition|(
name|val
operator|instanceof
name|URL
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
operator|(
operator|(
name|URL
operator|)
name|val
operator|)
operator|.
name|toURI
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|val
operator|instanceof
name|URI
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
operator|(
name|URI
operator|)
name|val
argument_list|)
return|;
block|}
if|if
condition|(
name|val
operator|instanceof
name|String
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
operator|new
name|URL
argument_list|(
operator|(
name|String
operator|)
name|val
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|TypedProperties
name|getConfig
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
if|if
condition|(
name|pid
operator|!=
literal|null
operator|&&
name|configAdmin
operator|!=
literal|null
condition|)
block|{
name|Configuration
name|configuration
init|=
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
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
name|TypedProperties
name|tp
init|=
operator|new
name|TypedProperties
argument_list|()
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
name|File
name|file
decl_stmt|;
try|try
block|{
name|file
operator|=
name|getCfgFileFromProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|tp
operator|.
name|load
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|e
init|=
name|props
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Object
name|val
init|=
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|tp
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
name|tp
operator|.
name|remove
argument_list|(
name|Constants
operator|.
name|SERVICE_PID
argument_list|)
expr_stmt|;
name|tp
operator|.
name|remove
argument_list|(
name|ConfigurationAdmin
operator|.
name|SERVICE_FACTORYPID
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|tp
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|createFactoryConfiguration
parameter_list|(
name|String
name|factoryPid
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createFactoryConfiguration
argument_list|(
name|factoryPid
argument_list|,
literal|null
argument_list|,
name|properties
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|createFactoryConfiguration
parameter_list|(
name|String
name|factoryPid
parameter_list|,
name|String
name|alias
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|IOException
block|{
name|Configuration
name|config
init|=
name|configAdmin
operator|.
name|createFactoryConfiguration
argument_list|(
name|factoryPid
argument_list|,
literal|"?"
argument_list|)
decl_stmt|;
name|TypedProperties
name|props
init|=
operator|new
name|TypedProperties
argument_list|()
decl_stmt|;
name|File
name|file
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|alias
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|alias
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|)
argument_list|,
name|factoryPid
operator|+
literal|"-"
operator|+
name|alias
operator|+
literal|".cfg"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|file
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
name|factoryPid
operator|+
literal|"-"
argument_list|,
literal|".cfg"
argument_list|,
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|props
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|props
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|FILEINSTALL_FILE_NAME
argument_list|,
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|update
argument_list|(
operator|new
name|Hashtable
argument_list|<>
argument_list|(
name|props
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|config
operator|.
name|getPid
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationAdmin
name|getConfigAdmin
parameter_list|()
block|{
return|return
name|configAdmin
return|;
block|}
block|}
end_class

end_unit

