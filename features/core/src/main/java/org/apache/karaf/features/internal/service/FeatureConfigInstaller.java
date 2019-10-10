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
operator|.
name|service
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
name|io
operator|.
name|StringReader
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
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
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
name|InterpolationHelper
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
name|ConfigInfo
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
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|StreamUtils
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
specifier|static
specifier|final
name|String
name|FILEINSTALL_FILE_NAME
init|=
literal|"felix.fileinstall.filename"
decl_stmt|;
specifier|private
specifier|final
name|ConfigurationAdmin
name|configAdmin
decl_stmt|;
specifier|private
name|File
name|storage
decl_stmt|;
specifier|private
name|boolean
name|configCfgStore
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
name|this
operator|.
name|storage
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|configCfgStore
operator|=
name|FeaturesServiceImpl
operator|.
name|DEFAULT_CONFIG_CFG_STORE
expr_stmt|;
block|}
specifier|public
name|FeatureConfigInstaller
parameter_list|(
name|ConfigurationAdmin
name|configAdmin
parameter_list|,
name|boolean
name|configCfgStore
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
expr_stmt|;
name|this
operator|.
name|configCfgStore
operator|=
name|configCfgStore
expr_stmt|;
block|}
specifier|private
name|ConfigId
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
name|ConfigId
name|cid
init|=
operator|new
name|ConfigId
argument_list|()
decl_stmt|;
name|cid
operator|.
name|fullPid
operator|=
name|pid
expr_stmt|;
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|cid
operator|.
name|factoryPid
operator|=
name|pid
operator|.
name|substring
argument_list|(
name|n
operator|+
literal|1
argument_list|)
expr_stmt|;
name|cid
operator|.
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
block|}
else|else
block|{
name|cid
operator|.
name|pid
operator|=
name|pid
expr_stmt|;
block|}
return|return
name|cid
return|;
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
name|pid
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
name|ConfigId
name|cid
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
name|cid
operator|.
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
name|cid
operator|.
name|pid
operator|+
literal|")"
expr_stmt|;
block|}
else|else
block|{
name|filter
operator|=
literal|"("
operator|+
name|CONFIG_KEY
operator|+
literal|"="
operator|+
name|cid
operator|.
name|fullPid
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
return|return
operator|(
name|configurations
operator|!=
literal|null
operator|&&
name|configurations
operator|.
name|length
operator|>
literal|0
operator|)
condition|?
name|configurations
index|[
literal|0
index|]
else|:
literal|null
return|;
block|}
specifier|public
name|void
name|installFeatureConfigs
parameter_list|(
name|Feature
name|feature
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
for|for
control|(
name|ConfigInfo
name|config
range|:
name|feature
operator|.
name|getConfigurations
argument_list|()
control|)
block|{
name|TypedProperties
name|props
init|=
operator|new
name|TypedProperties
argument_list|()
decl_stmt|;
comment|// trim lines
name|String
name|val
init|=
name|config
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|isExternal
argument_list|()
condition|)
block|{
try|try
block|{
name|props
operator|.
name|load
argument_list|(
operator|new
name|URL
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|net
operator|.
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to load config info from URL ["
operator|+
name|val
operator|+
literal|"] for feature ["
operator|+
name|feature
operator|.
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|feature
operator|.
name|getVersion
argument_list|()
operator|+
literal|"]."
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|props
operator|.
name|load
argument_list|(
operator|new
name|StringReader
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ConfigId
name|cid
init|=
name|parsePid
argument_list|(
name|config
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|cfg
init|=
name|findExistingConfiguration
argument_list|(
name|configAdmin
argument_list|,
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfg
operator|==
literal|null
operator|||
name|config
operator|.
name|isOverride
argument_list|()
condition|)
block|{
name|File
name|cfgFile
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|storage
operator|!=
literal|null
condition|)
block|{
name|cfgFile
operator|=
operator|new
name|File
argument_list|(
name|storage
argument_list|,
name|cid
operator|.
name|fullPid
operator|+
literal|".cfg"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|cfgFile
operator|.
name|exists
argument_list|()
operator|||
name|config
operator|.
name|isOverride
argument_list|()
condition|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cfgProps
init|=
name|convertToDict
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|cfg
operator|=
name|createConfiguration
argument_list|(
name|configAdmin
argument_list|,
name|cid
operator|.
name|pid
argument_list|,
name|cid
operator|.
name|factoryPid
argument_list|)
expr_stmt|;
name|cfgProps
operator|.
name|put
argument_list|(
name|CONFIG_KEY
argument_list|,
name|cid
operator|.
name|fullPid
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONFIG_KEY
argument_list|,
name|cid
operator|.
name|fullPid
argument_list|)
expr_stmt|;
if|if
condition|(
name|storage
operator|!=
literal|null
operator|&&
name|configCfgStore
condition|)
block|{
name|cfgProps
operator|.
name|put
argument_list|(
name|FILEINSTALL_FILE_NAME
argument_list|,
name|cfgFile
operator|.
name|getAbsoluteFile
argument_list|()
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
name|cfgProps
argument_list|)
expr_stmt|;
try|try
block|{
name|updateStorage
argument_list|(
name|cid
argument_list|,
name|props
argument_list|,
literal|false
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
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Skipping configuration {} - file already exists"
argument_list|,
name|cfgFile
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|isAppend
argument_list|()
condition|)
block|{
name|boolean
name|update
init|=
literal|false
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|cfg
operator|.
name|getProperties
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|props
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|==
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
argument_list|)
expr_stmt|;
name|update
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|update
condition|)
block|{
name|cfg
operator|.
name|update
argument_list|(
name|properties
argument_list|)
expr_stmt|;
try|try
block|{
name|updateStorage
argument_list|(
name|cid
argument_list|,
name|props
argument_list|,
literal|true
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
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|uninstallFeatureConfigs
parameter_list|(
name|Feature
name|feature
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
if|if
condition|(
name|feature
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|feature
operator|.
name|getConfigurations
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ConfigInfo
name|configInfo
range|:
name|feature
operator|.
name|getConfigurations
argument_list|()
control|)
block|{
name|ConfigId
name|configId
init|=
name|parsePid
argument_list|(
name|configInfo
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|findExistingConfiguration
argument_list|(
name|configAdmin
argument_list|,
name|configId
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|File
name|cfgFile
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|storage
operator|!=
literal|null
condition|)
block|{
name|cfgFile
operator|=
operator|new
name|File
argument_list|(
name|storage
argument_list|,
name|configId
operator|.
name|fullPid
operator|+
literal|".cfg"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cfgFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|cfgFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|feature
operator|.
name|getConfigurationFiles
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ConfigFileInfo
name|configFileInfo
range|:
name|feature
operator|.
name|getConfigurationFiles
argument_list|()
control|)
block|{
name|String
name|finalname
init|=
name|substFinalName
argument_list|(
name|configFileInfo
operator|.
name|getFinalname
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|cfgFile
init|=
operator|new
name|File
argument_list|(
name|finalname
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfgFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|cfgFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|convertToDict
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cfgProps
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|e
range|:
name|props
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|cfgProps
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|cfgProps
return|;
block|}
comment|/**      * Substitute variables in the final name and append prefix if necessary.      *      *<ol>      *<li>If the final name does not start with '${' it is prefixed with      * karaf.base (+ file separator).</li>      *<li>It substitute also all variables (scheme ${...}) with the respective      * configuration values and system properties.</li>      *<li>All unknown variables kept unchanged.</li>      *<li>If the substituted string starts with an variable that could not be      * substituted, it will be prefixed with karaf.base (+ file separator), too.      *</li>      *</ol>      *       * @param finalname      *            The final name that should be processed.      * @return the location in the file system that should be accesses.      */
specifier|protected
specifier|static
name|String
name|substFinalName
parameter_list|(
name|String
name|finalname
parameter_list|)
block|{
specifier|final
name|String
name|markerVarBeg
init|=
literal|"${"
decl_stmt|;
specifier|final
name|String
name|markerVarEnd
init|=
literal|"}"
decl_stmt|;
name|boolean
name|startsWithVariable
init|=
name|finalname
operator|.
name|startsWith
argument_list|(
name|markerVarBeg
argument_list|)
operator|&&
name|finalname
operator|.
name|contains
argument_list|(
name|markerVarEnd
argument_list|)
decl_stmt|;
comment|// Substitute all variables, but keep unknown ones.
specifier|final
name|String
name|dummyKey
init|=
literal|""
decl_stmt|;
try|try
block|{
name|finalname
operator|=
name|InterpolationHelper
operator|.
name|substVars
argument_list|(
name|finalname
argument_list|,
name|dummyKey
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Substitution failed. Skip substitution of variables of configuration final name ({})."
argument_list|,
name|finalname
argument_list|)
expr_stmt|;
block|}
comment|// Prefix with karaf base if the initial final name does not start with
comment|// a variable or the first variable was not substituted.
if|if
condition|(
operator|!
name|startsWithVariable
operator|||
name|finalname
operator|.
name|startsWith
argument_list|(
name|markerVarBeg
argument_list|)
condition|)
block|{
specifier|final
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
block|}
comment|// Remove all unknown variables.
while|while
condition|(
name|finalname
operator|.
name|contains
argument_list|(
name|markerVarBeg
argument_list|)
operator|&&
name|finalname
operator|.
name|contains
argument_list|(
name|markerVarEnd
argument_list|)
condition|)
block|{
name|int
name|beg
init|=
name|finalname
operator|.
name|indexOf
argument_list|(
name|markerVarBeg
argument_list|)
decl_stmt|;
name|int
name|end
init|=
name|finalname
operator|.
name|indexOf
argument_list|(
name|markerVarEnd
argument_list|)
decl_stmt|;
specifier|final
name|String
name|rem
init|=
name|finalname
operator|.
name|substring
argument_list|(
name|beg
argument_list|,
name|end
operator|+
name|markerVarEnd
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|finalname
operator|=
name|finalname
operator|.
name|replace
argument_list|(
name|rem
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
return|return
name|finalname
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
parameter_list|)
throws|throws
name|IOException
block|{
name|finalname
operator|=
name|substFinalName
argument_list|(
name|finalname
argument_list|)
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
condition|)
block|{
if|if
condition|(
operator|!
name|override
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Configuration file {} already exist, don't override it"
argument_list|,
name|finalname
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Configuration file {} already exist, overriding it"
argument_list|,
name|finalname
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Creating configuration file {}"
argument_list|,
name|finalname
argument_list|)
expr_stmt|;
block|}
comment|// TODO: use download manager to download the configuration
try|try
init|(
name|InputStream
name|is
init|=
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
init|)
block|{
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
block|{
name|parentFile
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|file
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|FileOutputStream
name|fop
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
name|StreamUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|fop
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
decl||
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
block|}
specifier|protected
name|void
name|updateStorage
parameter_list|(
name|ConfigId
name|cid
parameter_list|,
name|TypedProperties
name|props
parameter_list|,
name|boolean
name|append
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|storage
operator|!=
literal|null
operator|&&
name|configCfgStore
condition|)
block|{
name|File
name|cfgFile
init|=
name|getConfigFile
argument_list|(
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cfgFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|props
operator|.
name|save
argument_list|(
name|cfgFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updateExistingConfig
argument_list|(
name|props
argument_list|,
name|append
argument_list|,
name|cfgFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|File
name|getConfigFile
parameter_list|(
name|ConfigId
name|cid
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
name|Configuration
name|cfg
init|=
name|findExistingConfiguration
argument_list|(
name|configAdmin
argument_list|,
name|cid
argument_list|)
decl_stmt|;
comment|// update the cfg file depending of the configuration
name|File
name|cfgFile
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|,
name|cid
operator|.
name|fullPid
operator|+
literal|".cfg"
argument_list|)
decl_stmt|;
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
operator|new
name|IOException
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
return|return
name|cfgFile
return|;
block|}
specifier|private
name|void
name|updateExistingConfig
parameter_list|(
name|TypedProperties
name|props
parameter_list|,
name|boolean
name|append
parameter_list|,
name|File
name|cfgFile
parameter_list|)
throws|throws
name|IOException
block|{
name|TypedProperties
name|properties
init|=
operator|new
name|TypedProperties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|cfgFile
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|props
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|isInternalKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|comments
init|=
name|props
operator|.
name|getComments
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|properties
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|comments
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|append
condition|)
block|{
if|if
condition|(
name|comments
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|comments
operator|=
name|properties
operator|.
name|getComments
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|comments
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|append
condition|)
block|{
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
operator|!
name|props
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
operator|!
name|isInternalKey
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
block|}
name|storage
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|properties
operator|.
name|save
argument_list|(
name|cfgFile
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|isInternalKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|Constants
operator|.
name|SERVICE_PID
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|ConfigurationAdmin
operator|.
name|SERVICE_FACTORYPID
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|FILEINSTALL_FILE_NAME
operator|.
name|equals
argument_list|(
name|key
argument_list|)
return|;
block|}
class|class
name|ConfigId
block|{
name|String
name|fullPid
decl_stmt|;
name|String
name|pid
decl_stmt|;
name|String
name|factoryPid
decl_stmt|;
block|}
block|}
end_class

end_unit

