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
name|features
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
name|BufferedInputStream
import|;
end_import

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
name|FileOutputStream
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
name|io
operator|.
name|InputStream
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
name|Hashtable
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
name|features
operator|.
name|ConfigFileInfo
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
name|features
operator|.
name|Feature
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
name|FeatureConfigInstaller
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
name|FeaturesServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONFIG_KEY
init|=
literal|"org.apache.karaf.features.configKey"
decl_stmt|;
specifier|private
specifier|final
name|ConfigurationAdmin
name|configAdmin
decl_stmt|;
specifier|public
name|FeatureConfigInstaller
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
specifier|private
name|String
index|[]
name|parsePid
parameter_list|(
name|String
name|pid
parameter_list|)
block|{
name|int
name|n
init|=
name|pid
operator|.
name|indexOf
argument_list|(
literal|'-'
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|String
name|factoryPid
init|=
name|pid
operator|.
name|substring
argument_list|(
name|n
operator|+
literal|1
argument_list|)
decl_stmt|;
name|pid
operator|=
name|pid
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{
name|pid
block|,
name|factoryPid
block|}
return|;
block|}
else|else
block|{
return|return
operator|new
name|String
index|[]
block|{
name|pid
block|,
literal|null
block|}
return|;
block|}
block|}
specifier|private
name|Configuration
name|createConfiguration
parameter_list|(
name|ConfigurationAdmin
name|configurationAdmin
parameter_list|,
name|String
name|pid
parameter_list|,
name|String
name|factoryPid
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
if|if
condition|(
name|factoryPid
operator|!=
literal|null
condition|)
block|{
return|return
name|configurationAdmin
operator|.
name|createFactoryConfiguration
argument_list|(
name|factoryPid
argument_list|,
literal|null
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
specifier|private
name|Configuration
name|findExistingConfiguration
parameter_list|(
name|ConfigurationAdmin
name|configurationAdmin
parameter_list|,
name|String
name|pid
parameter_list|,
name|String
name|factoryPid
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
name|String
name|filter
decl_stmt|;
if|if
condition|(
name|factoryPid
operator|==
literal|null
condition|)
block|{
name|filter
operator|=
literal|"("
operator|+
name|Constants
operator|.
name|SERVICE_PID
operator|+
literal|"="
operator|+
name|pid
operator|+
literal|")"
expr_stmt|;
block|}
else|else
block|{
name|String
name|key
init|=
name|createConfigurationKey
argument_list|(
name|pid
argument_list|,
name|factoryPid
argument_list|)
decl_stmt|;
name|filter
operator|=
literal|"("
operator|+
name|CONFIG_KEY
operator|+
literal|"="
operator|+
name|key
operator|+
literal|")"
expr_stmt|;
block|}
name|Configuration
index|[]
name|configurations
init|=
name|configurationAdmin
operator|.
name|listConfigurations
argument_list|(
name|filter
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurations
operator|!=
literal|null
operator|&&
name|configurations
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|configurations
index|[
literal|0
index|]
return|;
block|}
return|return
literal|null
return|;
block|}
name|void
name|installFeatureConfigs
parameter_list|(
name|Feature
name|feature
parameter_list|,
name|boolean
name|verbose
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
for|for
control|(
name|String
name|config
range|:
name|feature
operator|.
name|getConfigurations
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|feature
operator|.
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
name|config
argument_list|)
argument_list|)
decl_stmt|;
name|String
index|[]
name|pid
init|=
name|parsePid
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|Configuration
name|cfg
init|=
name|findExistingConfiguration
argument_list|(
name|configAdmin
argument_list|,
name|pid
index|[
literal|0
index|]
argument_list|,
name|pid
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfg
operator|==
literal|null
condition|)
block|{
name|cfg
operator|=
name|createConfiguration
argument_list|(
name|configAdmin
argument_list|,
name|pid
index|[
literal|0
index|]
argument_list|,
name|pid
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|createConfigurationKey
argument_list|(
name|pid
index|[
literal|0
index|]
argument_list|,
name|pid
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONFIG_KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|cfg
operator|.
name|getBundleLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cfg
operator|.
name|setBundleLocation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|cfg
operator|.
name|update
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|ConfigFileInfo
name|configFile
range|:
name|feature
operator|.
name|getConfigurationFiles
argument_list|()
control|)
block|{
name|installConfigurationFile
argument_list|(
name|configFile
operator|.
name|getLocation
argument_list|()
argument_list|,
name|configFile
operator|.
name|getFinalname
argument_list|()
argument_list|,
name|configFile
operator|.
name|isOverride
argument_list|()
argument_list|,
name|verbose
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|createConfigurationKey
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|factoryPid
parameter_list|)
block|{
return|return
name|factoryPid
operator|==
literal|null
condition|?
name|pid
else|:
name|pid
operator|+
literal|"-"
operator|+
name|factoryPid
return|;
block|}
specifier|private
name|void
name|installConfigurationFile
parameter_list|(
name|String
name|fileLocation
parameter_list|,
name|String
name|finalname
parameter_list|,
name|boolean
name|override
parameter_list|,
name|boolean
name|verbose
parameter_list|)
throws|throws
name|IOException
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Checking configuration file "
operator|+
name|fileLocation
argument_list|)
expr_stmt|;
if|if
condition|(
name|verbose
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Checking configuration file "
operator|+
name|fileLocation
argument_list|)
expr_stmt|;
block|}
name|String
name|basePath
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
decl_stmt|;
if|if
condition|(
name|finalname
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
comment|//remove any placeholder or variable part, this is not valid.
name|int
name|marker
init|=
name|finalname
operator|.
name|indexOf
argument_list|(
literal|"}"
argument_list|)
decl_stmt|;
name|finalname
operator|=
name|finalname
operator|.
name|substring
argument_list|(
name|marker
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|finalname
operator|=
name|basePath
operator|+
name|File
operator|.
name|separator
operator|+
name|finalname
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|finalname
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|override
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"configFile already exist, don't override it"
argument_list|)
expr_stmt|;
return|return;
block|}
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|FileOutputStream
name|fop
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|URL
argument_list|(
name|fileLocation
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|File
name|parentFile
init|=
name|file
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentFile
operator|!=
literal|null
condition|)
name|parentFile
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|file
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
name|fop
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|int
name|bytesRead
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
while|while
condition|(
operator|(
name|bytesRead
operator|=
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|fop
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|bytesRead
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|fop
operator|!=
literal|null
condition|)
block|{
name|fop
operator|.
name|flush
argument_list|()
expr_stmt|;
name|fop
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

