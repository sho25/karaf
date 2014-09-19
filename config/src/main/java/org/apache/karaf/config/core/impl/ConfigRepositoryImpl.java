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
name|URI
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
name|ArrayList
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
name|Hashtable
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
name|Properties
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
specifier|private
name|File
name|storage
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
specifier|public
name|ConfigRepositoryImpl
parameter_list|(
name|ConfigurationAdmin
name|configAdmin
parameter_list|,
name|File
name|storage
parameter_list|)
block|{
name|this
operator|.
name|configAdmin
operator|=
name|configAdmin
expr_stmt|;
name|this
operator|.
name|storage
operator|=
name|storage
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.karaf.shell.config.impl.ConfigRepository#update(java.lang.String, java.util.Dictionary, boolean)      */
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|public
name|void
name|update
parameter_list|(
name|String
name|pid
parameter_list|,
name|Dictionary
name|props
parameter_list|)
throws|throws
name|IOException
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Update configuration {}"
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|Configuration
name|cfg
init|=
name|this
operator|.
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|update
argument_list|(
name|props
argument_list|)
expr_stmt|;
try|try
block|{
name|updateStorage
argument_list|(
name|pid
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Can't update cfg file"
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
literal|"Delete configuration {}"
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|this
operator|.
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|delete
argument_list|()
expr_stmt|;
try|try
block|{
name|deleteStorage
argument_list|(
name|pid
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Can't delete cfg file"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|deleteStorage
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|storage
operator|!=
literal|null
condition|)
block|{
name|File
name|cfgFile
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|,
name|pid
operator|+
literal|".cfg"
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Delete {}"
argument_list|,
name|cfgFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cfgFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|updateStorage
parameter_list|(
name|String
name|pid
parameter_list|,
name|Dictionary
name|props
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|storage
operator|!=
literal|null
condition|)
block|{
comment|// get the cfg file
name|File
name|cfgFile
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|,
name|pid
operator|+
literal|".cfg"
argument_list|)
decl_stmt|;
name|Configuration
name|cfg
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
comment|// update the cfg file depending of the configuration
if|if
condition|(
name|cfg
operator|!=
literal|null
operator|&&
name|cfg
operator|.
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|val
init|=
name|cfg
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|FILEINSTALL_FILE_NAME
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|val
operator|instanceof
name|URL
condition|)
block|{
name|cfgFile
operator|=
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
expr_stmt|;
block|}
if|if
condition|(
name|val
operator|instanceof
name|URI
condition|)
block|{
name|cfgFile
operator|=
operator|new
name|File
argument_list|(
operator|(
name|URI
operator|)
name|val
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|val
operator|instanceof
name|String
condition|)
block|{
name|cfgFile
operator|=
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
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|IOException
operator|)
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Update {}"
argument_list|,
name|cfgFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// update the cfg file
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|(
name|cfgFile
argument_list|)
decl_stmt|;
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|props
operator|.
name|keys
argument_list|()
init|;
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Constants
operator|.
name|SERVICE_PID
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|&&
operator|!
name|ConfigurationAdmin
operator|.
name|SERVICE_FACTORYPID
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|&&
operator|!
name|FILEINSTALL_FILE_NAME
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
if|if
condition|(
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// remove "removed" properties from the cfg file
name|ArrayList
argument_list|<
name|String
argument_list|>
name|propertiesToRemove
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|properties
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|==
literal|null
operator|&&
operator|!
name|Constants
operator|.
name|SERVICE_PID
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|&&
operator|!
name|ConfigurationAdmin
operator|.
name|SERVICE_FACTORYPID
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|&&
operator|!
name|FILEINSTALL_FILE_NAME
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|propertiesToRemove
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|key
range|:
name|propertiesToRemove
control|)
block|{
name|properties
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
comment|// save the cfg file
name|storage
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|properties
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
block|}
comment|/* (non-Javadoc)      * @see org.apache.karaf.shell.config.impl.ConfigRepository#getConfigProperties(java.lang.String)      */
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|Dictionary
name|getConfigProperties
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
name|this
operator|.
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
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
name|props
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
return|return
operator|(
name|props
operator|!=
literal|null
operator|)
condition|?
name|props
else|:
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|ConfigurationAdmin
name|getConfigAdmin
parameter_list|()
block|{
return|return
name|this
operator|.
name|configAdmin
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
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
block|{
try|try
block|{
name|Configuration
name|config
init|=
name|configAdmin
operator|.
name|createFactoryConfiguration
argument_list|(
name|factoryPid
argument_list|)
decl_stmt|;
name|config
operator|.
name|update
argument_list|(
name|properties
argument_list|)
expr_stmt|;
return|return
name|config
operator|.
name|getPid
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

