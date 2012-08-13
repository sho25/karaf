begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|main
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
name|FileNotFoundException
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
name|util
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
name|main
operator|.
name|lock
operator|.
name|SimpleFileLock
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
name|main
operator|.
name|util
operator|.
name|Utils
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

begin_class
specifier|public
class|class
name|ConfigProperties
block|{
comment|/**      * The system property for specifying the Karaf home directory.  The home directory      * hold the binary install of Karaf.      */
specifier|public
specifier|static
specifier|final
name|String
name|PROP_KARAF_HOME
init|=
literal|"karaf.home"
decl_stmt|;
comment|/**      * The environment variable for specifying the Karaf home directory.  The home directory      * hold the binary install of Karaf.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENV_KARAF_HOME
init|=
literal|"KARAF_HOME"
decl_stmt|;
comment|/**      * The system property for specifying the Karaf base directory.  The base directory      * holds the configuration and data for a Karaf instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|PROP_KARAF_BASE
init|=
literal|"karaf.base"
decl_stmt|;
comment|/**      * The environment variable for specifying the Karaf base directory.  The base directory      * holds the configuration and data for a Karaf instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENV_KARAF_BASE
init|=
literal|"KARAF_BASE"
decl_stmt|;
comment|/**      * The system property for specifying the Karaf data directory. The data directory      * holds the bundles data and cache for a Karaf instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|PROP_KARAF_DATA
init|=
literal|"karaf.data"
decl_stmt|;
comment|/**      * The environment variable for specifying the Karaf data directory. The data directory      * holds the bundles data and cache for a Karaf instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENV_KARAF_DATA
init|=
literal|"KARAF_DATA"
decl_stmt|;
comment|/**      * The system property for specifying the Karaf data directory. The data directory      * holds the bundles data and cache for a Karaf instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|PROP_KARAF_INSTANCES
init|=
literal|"karaf.instances"
decl_stmt|;
comment|/**      * The system property for specifying the Karaf data directory. The data directory      * holds the bundles data and cache for a Karaf instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENV_KARAF_INSTANCES
init|=
literal|"KARAF_INSTANCES"
decl_stmt|;
comment|/**      * The system property for hosting the current Karaf version.      */
specifier|public
specifier|static
specifier|final
name|String
name|PROP_KARAF_VERSION
init|=
literal|"karaf.version"
decl_stmt|;
comment|/**      * The default name used for the configuration properties file.      */
specifier|private
specifier|static
specifier|final
name|String
name|CONFIG_PROPERTIES_FILE_NAME
init|=
literal|"config.properties"
decl_stmt|;
comment|/**      * The default name used for the system properties file.      */
specifier|public
specifier|static
specifier|final
name|String
name|SYSTEM_PROPERTIES_FILE_NAME
init|=
literal|"system.properties"
decl_stmt|;
comment|/**      * Config property which identifies directories which contain bundles to be loaded by SMX      */
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE_LOCATIONS
init|=
literal|"bundle.locations"
decl_stmt|;
comment|/**      * The lock implementation      */
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_CLASS
init|=
literal|"karaf.lock.class"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_DELAY
init|=
literal|"karaf.lock.delay"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_LEVEL
init|=
literal|"karaf.lock.level"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_REPO
init|=
literal|"karaf.default.repository"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_FRAMEWORK
init|=
literal|"karaf.framework"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_FRAMEWORK_FACTORY
init|=
literal|"karaf.framework.factory"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_SHUTDOWN_TIMEOUT
init|=
literal|"karaf.shutdown.timeout"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_SHUTDOWN_PORT
init|=
literal|"karaf.shutdown.port"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_SHUTDOWN_HOST
init|=
literal|"karaf.shutdown.host"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_SHUTDOWN_PORT_FILE
init|=
literal|"karaf.shutdown.port.file"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_SHUTDOWN_COMMAND
init|=
literal|"karaf.shutdown.command"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_SHUTDOWN_PID_FILE
init|=
literal|"karaf.shutdown.pid.file"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_STARTUP_MESSAGE
init|=
literal|"karaf.startup.message"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_DELAY_CONSOLE
init|=
literal|"karaf.delay.console"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_SHUTDOWN_COMMAND
init|=
literal|"SHUTDOWN"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_CLASS_DEFAULT
init|=
name|SimpleFileLock
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SECURITY_PROVIDERS
init|=
literal|"org.apache.karaf.security.providers"
decl_stmt|;
comment|/**      * If a lock should be used before starting the runtime      */
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_USE_LOCK
init|=
literal|"karaf.lock"
decl_stmt|;
name|File
name|karafHome
decl_stmt|;
name|File
name|karafBase
decl_stmt|;
name|File
name|karafData
decl_stmt|;
name|File
name|karafInstances
decl_stmt|;
name|Properties
name|props
decl_stmt|;
name|String
index|[]
name|securityProviders
decl_stmt|;
name|int
name|defaultStartLevel
init|=
literal|100
decl_stmt|;
name|int
name|lockStartLevel
init|=
literal|1
decl_stmt|;
name|int
name|lockDelay
init|=
literal|1000
decl_stmt|;
name|int
name|shutdownTimeout
init|=
literal|5
operator|*
literal|60
operator|*
literal|1000
decl_stmt|;
name|boolean
name|useLock
decl_stmt|;
name|String
name|lockClass
decl_stmt|;
name|String
name|frameworkFactoryClass
decl_stmt|;
name|URI
name|frameworkBundle
decl_stmt|;
name|String
name|defaultRepo
decl_stmt|;
name|String
name|bundleLocations
decl_stmt|;
name|int
name|defaultBundleStartlevel
decl_stmt|;
name|String
name|pidFile
decl_stmt|;
name|int
name|shutdownPort
decl_stmt|;
name|String
name|shutdownHost
decl_stmt|;
name|String
name|portFile
decl_stmt|;
name|String
name|shutdownCommand
decl_stmt|;
name|String
name|includes
decl_stmt|;
name|String
name|optionals
decl_stmt|;
name|File
name|etcFolder
decl_stmt|;
name|String
name|startupMessage
decl_stmt|;
name|boolean
name|delayConsoleStart
decl_stmt|;
specifier|public
name|ConfigProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|karafHome
operator|=
name|Utils
operator|.
name|getKarafHome
argument_list|(
name|ConfigProperties
operator|.
name|class
argument_list|,
name|PROP_KARAF_HOME
argument_list|,
name|ENV_KARAF_HOME
argument_list|)
expr_stmt|;
name|this
operator|.
name|karafBase
operator|=
name|Utils
operator|.
name|getKarafDirectory
argument_list|(
name|PROP_KARAF_BASE
argument_list|,
name|ENV_KARAF_BASE
argument_list|,
name|karafHome
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|karafData
operator|=
name|Utils
operator|.
name|getKarafDirectory
argument_list|(
name|PROP_KARAF_DATA
argument_list|,
name|ENV_KARAF_DATA
argument_list|,
operator|new
name|File
argument_list|(
name|karafBase
argument_list|,
literal|"data"
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"karaf.restart.clean"
argument_list|)
condition|)
block|{
name|Utils
operator|.
name|deleteDirectory
argument_list|(
name|this
operator|.
name|karafData
argument_list|)
expr_stmt|;
name|this
operator|.
name|karafData
operator|=
name|Utils
operator|.
name|getKarafDirectory
argument_list|(
name|PROP_KARAF_DATA
argument_list|,
name|ENV_KARAF_DATA
argument_list|,
operator|new
name|File
argument_list|(
name|karafBase
argument_list|,
literal|"data"
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|karafInstances
operator|=
name|Utils
operator|.
name|getKarafDirectory
argument_list|(
name|PROP_KARAF_INSTANCES
argument_list|,
name|ENV_KARAF_INSTANCES
argument_list|,
operator|new
name|File
argument_list|(
name|karafHome
argument_list|,
literal|"instances"
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Package
name|p
init|=
name|Package
operator|.
name|getPackage
argument_list|(
literal|"org.apache.karaf.main"
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
operator|&&
name|p
operator|.
name|getImplementationVersion
argument_list|()
operator|!=
literal|null
condition|)
name|System
operator|.
name|setProperty
argument_list|(
name|PROP_KARAF_VERSION
argument_list|,
name|p
operator|.
name|getImplementationVersion
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|PROP_KARAF_HOME
argument_list|,
name|karafHome
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|PROP_KARAF_BASE
argument_list|,
name|karafBase
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|PROP_KARAF_DATA
argument_list|,
name|karafData
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|PROP_KARAF_INSTANCES
argument_list|,
name|karafInstances
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|etcFolder
operator|=
operator|new
name|File
argument_list|(
name|karafBase
argument_list|,
literal|"etc"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|etcFolder
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"etc folder not found: "
operator|+
name|etcFolder
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|PropertiesLoader
operator|.
name|loadSystemProperties
argument_list|(
operator|new
name|File
argument_list|(
name|etcFolder
argument_list|,
name|SYSTEM_PROPERTIES_FILE_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|etcFolder
argument_list|,
name|CONFIG_PROPERTIES_FILE_NAME
argument_list|)
decl_stmt|;
name|this
operator|.
name|props
operator|=
name|PropertiesLoader
operator|.
name|loadConfigProperties
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|String
name|prop
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|SECURITY_PROVIDERS
argument_list|)
decl_stmt|;
name|this
operator|.
name|securityProviders
operator|=
operator|(
name|prop
operator|!=
literal|null
operator|)
condition|?
name|prop
operator|.
name|split
argument_list|(
literal|","
argument_list|)
else|:
operator|new
name|String
index|[]
block|{}
expr_stmt|;
name|this
operator|.
name|defaultStartLevel
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|FRAMEWORK_BEGINNING_STARTLEVEL
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|Constants
operator|.
name|FRAMEWORK_BEGINNING_STARTLEVEL
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|this
operator|.
name|defaultStartLevel
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|lockStartLevel
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_LEVEL
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|lockStartLevel
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|lockDelay
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_DELAY
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|lockDelay
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|props
operator|.
name|setProperty
argument_list|(
name|Constants
operator|.
name|FRAMEWORK_BEGINNING_STARTLEVEL
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|lockStartLevel
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|shutdownTimeout
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_SHUTDOWN_TIMEOUT
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|shutdownTimeout
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|useLock
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_USE_LOCK
argument_list|,
literal|"true"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|lockClass
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_CLASS
argument_list|,
name|PROPERTY_LOCK_CLASS_DEFAULT
argument_list|)
expr_stmt|;
name|initFrameworkStorage
argument_list|(
name|karafData
argument_list|)
expr_stmt|;
name|this
operator|.
name|frameworkFactoryClass
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_FRAMEWORK_FACTORY
argument_list|)
expr_stmt|;
name|this
operator|.
name|frameworkBundle
operator|=
name|getFramework
argument_list|()
expr_stmt|;
name|this
operator|.
name|defaultRepo
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|DEFAULT_REPO
argument_list|,
literal|"system"
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleLocations
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|BUNDLE_LOCATIONS
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultBundleStartlevel
operator|=
name|getDefaultBundleStartLevel
argument_list|(
literal|60
argument_list|)
expr_stmt|;
name|this
operator|.
name|pidFile
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_SHUTDOWN_PID_FILE
argument_list|)
expr_stmt|;
name|this
operator|.
name|shutdownPort
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_SHUTDOWN_PORT
argument_list|,
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|shutdownHost
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_SHUTDOWN_HOST
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
name|this
operator|.
name|portFile
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_SHUTDOWN_PORT_FILE
argument_list|)
expr_stmt|;
name|this
operator|.
name|shutdownCommand
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_SHUTDOWN_COMMAND
argument_list|,
name|DEFAULT_SHUTDOWN_COMMAND
argument_list|)
expr_stmt|;
name|this
operator|.
name|startupMessage
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_STARTUP_MESSAGE
argument_list|,
literal|"Apache Karaf starting up. Press Enter to open the shell now..."
argument_list|)
expr_stmt|;
name|this
operator|.
name|delayConsoleStart
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_DELAY_CONSOLE
argument_list|,
literal|"false"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|KARAF_DELAY_CONSOLE
argument_list|,
operator|new
name|Boolean
argument_list|(
name|this
operator|.
name|delayConsoleStart
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getProperyOrFail
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|String
name|value
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Property "
operator|+
name|propertyName
operator|+
literal|" must be set in the etc/"
operator|+
name|CONFIG_PROPERTIES_FILE_NAME
operator|+
literal|" configuration file"
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
specifier|private
name|URI
name|getFramework
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|String
name|framework
init|=
name|getProperyOrFail
argument_list|(
name|KARAF_FRAMEWORK
argument_list|)
decl_stmt|;
name|String
name|frameworkBundleUri
init|=
name|getProperyOrFail
argument_list|(
name|KARAF_FRAMEWORK
operator|+
literal|"."
operator|+
name|framework
argument_list|)
decl_stmt|;
return|return
operator|new
name|URI
argument_list|(
name|frameworkBundleUri
argument_list|)
return|;
block|}
specifier|private
name|void
name|initFrameworkStorage
parameter_list|(
name|File
name|karafData
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|frameworkStoragePath
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|FRAMEWORK_STORAGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|frameworkStoragePath
operator|==
literal|null
condition|)
block|{
name|File
name|storage
init|=
operator|new
name|File
argument_list|(
name|karafData
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"cache"
argument_list|)
decl_stmt|;
try|try
block|{
name|storage
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|se
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|se
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|props
operator|.
name|setProperty
argument_list|(
name|Constants
operator|.
name|FRAMEWORK_STORAGE
argument_list|,
name|storage
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|int
name|getDefaultBundleStartLevel
parameter_list|(
name|int
name|ibsl
parameter_list|)
block|{
try|try
block|{
name|String
name|str
init|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"karaf.startlevel.bundle"
argument_list|)
decl_stmt|;
if|if
condition|(
name|str
operator|!=
literal|null
condition|)
block|{
name|ibsl
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{         }
return|return
name|ibsl
return|;
block|}
block|}
end_class

end_unit

