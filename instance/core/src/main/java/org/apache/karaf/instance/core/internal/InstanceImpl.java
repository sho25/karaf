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
name|instance
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
name|BufferedReader
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
name|FilenameFilter
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
name|InputStreamReader
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
name|Socket
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
name|net
operator|.
name|UnknownHostException
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
name|instance
operator|.
name|core
operator|.
name|Instance
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
name|jpm
operator|.
name|Process
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
name|jpm
operator|.
name|ProcessBuilderFactory
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
name|jpm
operator|.
name|impl
operator|.
name|ProcessBuilderFactoryImpl
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
name|jpm
operator|.
name|impl
operator|.
name|ScriptUtils
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
name|InstanceImpl
implements|implements
name|Instance
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|InstanceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONFIG_PROPERTIES_FILE_NAME
init|=
literal|"config.properties"
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
name|DEFAULT_SHUTDOWN_COMMAND
init|=
literal|"SHUTDOWN"
decl_stmt|;
specifier|private
name|InstanceServiceImpl
name|service
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|location
decl_stmt|;
specifier|private
name|String
name|javaOpts
decl_stmt|;
specifier|private
name|Process
name|process
decl_stmt|;
specifier|private
name|boolean
name|root
decl_stmt|;
specifier|private
specifier|final
name|ProcessBuilderFactory
name|processBuilderFactory
decl_stmt|;
specifier|public
name|InstanceImpl
parameter_list|(
name|InstanceServiceImpl
name|service
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|location
parameter_list|,
name|String
name|javaOpts
parameter_list|)
block|{
name|this
argument_list|(
name|service
argument_list|,
name|name
argument_list|,
name|location
argument_list|,
name|javaOpts
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|InstanceImpl
parameter_list|(
name|InstanceServiceImpl
name|service
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|location
parameter_list|,
name|String
name|javaOpts
parameter_list|,
name|boolean
name|root
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
name|this
operator|.
name|javaOpts
operator|=
name|javaOpts
expr_stmt|;
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
name|this
operator|.
name|processBuilderFactory
operator|=
operator|new
name|ProcessBuilderFactoryImpl
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|attach
parameter_list|(
name|int
name|pid
parameter_list|)
throws|throws
name|IOException
block|{
name|checkProcess
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|process
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Instance already started"
argument_list|)
throw|;
block|}
name|this
operator|.
name|process
operator|=
name|processBuilderFactory
operator|.
name|newBuilder
argument_list|()
operator|.
name|attach
argument_list|(
name|pid
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRoot
parameter_list|()
block|{
return|return
name|root
return|;
block|}
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|location
return|;
block|}
specifier|public
name|void
name|setLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
block|}
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
return|return
operator|new
name|File
argument_list|(
name|location
argument_list|)
operator|.
name|isDirectory
argument_list|()
return|;
block|}
specifier|public
name|int
name|getPid
parameter_list|()
block|{
name|checkProcess
argument_list|()
expr_stmt|;
return|return
name|this
operator|.
name|process
operator|!=
literal|null
condition|?
name|this
operator|.
name|process
operator|.
name|getPid
argument_list|()
else|:
literal|0
return|;
block|}
specifier|public
name|int
name|getSshPort
parameter_list|()
block|{
try|try
block|{
name|String
name|loc
init|=
name|this
operator|.
name|getConfiguration
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|,
literal|"etc/org.apache.karaf.shell.cfg"
argument_list|)
argument_list|,
literal|"sshPort"
argument_list|)
decl_stmt|;
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|loc
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
literal|0
return|;
block|}
block|}
specifier|public
name|void
name|changeSshPort
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|checkProcess
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|process
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Instance not stopped"
argument_list|)
throw|;
block|}
name|this
operator|.
name|changeConfiguration
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|,
literal|"etc/org.apache.karaf.shell.cfg"
argument_list|)
argument_list|,
literal|"sshPort"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getRmiRegistryPort
parameter_list|()
block|{
try|try
block|{
name|String
name|loc
init|=
name|this
operator|.
name|getConfiguration
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|,
literal|"etc/org.apache.karaf.management.cfg"
argument_list|)
argument_list|,
literal|"rmiRegistryPort"
argument_list|)
decl_stmt|;
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|loc
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
literal|0
return|;
block|}
block|}
specifier|public
name|void
name|changeRmiRegistryPort
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|checkProcess
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|process
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Instance not stopped"
argument_list|)
throw|;
block|}
name|this
operator|.
name|changeConfiguration
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|,
literal|"etc/org.apache.karaf.management.cfg"
argument_list|)
argument_list|,
literal|"rmiRegistryPort"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getRmiServerPort
parameter_list|()
block|{
try|try
block|{
name|String
name|loc
init|=
name|this
operator|.
name|getConfiguration
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|,
literal|"etc/org.apache.karaf.management.cfg"
argument_list|)
argument_list|,
literal|"rmiServerPort"
argument_list|)
decl_stmt|;
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|loc
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
literal|0
return|;
block|}
block|}
specifier|public
name|void
name|changeRmiServerPort
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|checkProcess
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|process
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Instance not stopped"
argument_list|)
throw|;
block|}
name|this
operator|.
name|changeConfiguration
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|,
literal|"etc/org.apache.karaf.management.cfg"
argument_list|)
argument_list|,
literal|"rmiServerPort"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Change a configuration property in a given configuration file.      *      * @param configurationFile the configuration file where to update the configuration property.      * @param propertyName the property name.      * @param propertyValue the property value.      * @throws Exception if a failure occurs.      */
specifier|private
name|void
name|changeConfiguration
parameter_list|(
name|File
name|configurationFile
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|propertyValue
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|configurationFile
argument_list|)
decl_stmt|;
try|try
block|{
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|props
operator|.
name|put
argument_list|(
name|propertyName
argument_list|,
name|propertyValue
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|configurationFile
argument_list|)
decl_stmt|;
try|try
block|{
name|props
operator|.
name|save
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Read a given configuration file to get the value of a given property.      *      * @param configurationFile the configuration file where to lookup property.      * @param propertyName the property name to look for.      * @return the property value.      * @throws Exception in case of read failure.      */
specifier|private
name|String
name|getConfiguration
parameter_list|(
name|File
name|configurationFile
parameter_list|,
name|String
name|propertyName
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
name|loadPropertiesFile
argument_list|(
name|configurationFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|props
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
specifier|public
name|String
name|getJavaOpts
parameter_list|()
block|{
return|return
name|javaOpts
return|;
block|}
specifier|public
name|void
name|changeJavaOpts
parameter_list|(
name|String
name|javaOpts
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|javaOpts
operator|=
name|javaOpts
expr_stmt|;
name|this
operator|.
name|service
operator|.
name|saveState
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|start
parameter_list|(
name|String
name|javaOpts
parameter_list|)
throws|throws
name|Exception
block|{
name|checkProcess
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|process
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Instance already started"
argument_list|)
throw|;
block|}
if|if
condition|(
name|javaOpts
operator|==
literal|null
operator|||
name|javaOpts
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|javaOpts
operator|=
name|this
operator|.
name|javaOpts
expr_stmt|;
block|}
if|if
condition|(
name|javaOpts
operator|==
literal|null
operator|||
name|javaOpts
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|javaOpts
operator|=
literal|"-server -Xmx512M -Dcom.sun.management.jmxremote"
expr_stmt|;
block|}
name|String
name|karafOpts
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.opts"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|File
name|libDir
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.home"
argument_list|)
argument_list|,
literal|"lib"
argument_list|)
decl_stmt|;
name|String
name|classpath
init|=
name|createClasspathFromAllJars
argument_list|(
name|libDir
argument_list|)
decl_stmt|;
name|String
name|endorsedDirs
init|=
name|getSubDirs
argument_list|(
name|libDir
argument_list|,
literal|"endorsed"
argument_list|)
decl_stmt|;
name|String
name|extDirs
init|=
name|getSubDirs
argument_list|(
name|libDir
argument_list|,
literal|"ext"
argument_list|)
decl_stmt|;
name|String
name|command
init|=
literal|"\""
operator|+
name|ScriptUtils
operator|.
name|getJavaCommandPath
argument_list|()
operator|+
literal|"\" "
operator|+
name|javaOpts
operator|+
literal|" "
operator|+
name|karafOpts
operator|+
literal|" -Djava.util.logging.config.file=\""
operator|+
operator|new
name|File
argument_list|(
name|location
argument_list|,
literal|"etc/java.util.logging.properties"
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
operator|+
literal|"\""
operator|+
literal|" -Djava.endorsed.dirs=\""
operator|+
name|endorsedDirs
operator|+
literal|"\""
operator|+
literal|" -Djava.ext.dirs=\""
operator|+
name|extDirs
operator|+
literal|"\""
operator|+
literal|" -Dkaraf.home=\""
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.home"
argument_list|)
operator|+
literal|"\""
operator|+
literal|" -Dkaraf.base=\""
operator|+
operator|new
name|File
argument_list|(
name|location
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
operator|+
literal|"\""
operator|+
literal|" -Dkaraf.startLocalConsole=false"
operator|+
literal|" -Dkaraf.startRemoteShell=true"
operator|+
literal|" -classpath "
operator|+
name|classpath
operator|.
name|toString
argument_list|()
operator|+
literal|" org.apache.karaf.main.Main"
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting instance "
operator|+
name|name
operator|+
literal|" with command: "
operator|+
name|command
argument_list|)
expr_stmt|;
name|this
operator|.
name|process
operator|=
name|processBuilderFactory
operator|.
name|newBuilder
argument_list|()
operator|.
name|directory
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|)
argument_list|)
operator|.
name|command
argument_list|(
name|command
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|this
operator|.
name|service
operator|.
name|saveState
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|getSubDirs
parameter_list|(
name|File
name|libDir
parameter_list|,
name|String
name|subDir
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|jreLibDir
init|=
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
literal|"java.home"
argument_list|)
argument_list|,
literal|"jre"
argument_list|)
argument_list|,
literal|"lib"
argument_list|)
decl_stmt|;
name|File
name|javaLibDir
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
argument_list|,
literal|"lib"
argument_list|)
decl_stmt|;
name|String
name|sep
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"path.separator"
argument_list|)
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|jreLibDir
argument_list|,
name|subDir
argument_list|)
operator|+
name|sep
operator|+
operator|new
name|File
argument_list|(
name|javaLibDir
argument_list|,
name|subDir
argument_list|)
operator|+
name|sep
operator|+
operator|new
name|File
argument_list|(
name|libDir
argument_list|,
name|subDir
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
return|;
block|}
specifier|private
name|String
name|createClasspathFromAllJars
parameter_list|(
name|File
name|libDir
parameter_list|)
throws|throws
name|IOException
block|{
name|File
index|[]
name|jars
init|=
name|libDir
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|StringBuilder
name|classpath
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|jar
range|:
name|jars
control|)
block|{
if|if
condition|(
name|classpath
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|classpath
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"path.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|classpath
operator|.
name|append
argument_list|(
name|jar
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|classpath
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|checkProcess
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|process
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Instance not started"
argument_list|)
throw|;
block|}
comment|// Try a clean shutdown
name|cleanShutdown
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|process
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|process
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|checkProcess
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|process
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Instance not stopped"
argument_list|)
throw|;
block|}
name|deleteFile
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|service
operator|.
name|forget
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|service
operator|.
name|saveState
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|String
name|getState
parameter_list|()
block|{
name|int
name|port
init|=
name|getSshPort
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|exists
argument_list|()
operator|||
name|port
operator|<=
literal|0
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|checkProcess
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|process
operator|==
literal|null
condition|)
block|{
return|return
name|STOPPED
return|;
block|}
else|else
block|{
try|try
block|{
name|Socket
name|s
init|=
operator|new
name|Socket
argument_list|(
literal|"localhost"
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|STARTED
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|STARTING
return|;
block|}
block|}
specifier|protected
name|void
name|checkProcess
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|process
operator|!=
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|process
operator|.
name|isRunning
argument_list|()
condition|)
block|{
name|this
operator|.
name|process
operator|=
literal|null
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{             }
block|}
block|}
specifier|protected
name|void
name|cleanShutdown
parameter_list|()
block|{
try|try
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|,
literal|"etc"
argument_list|)
argument_list|,
name|CONFIG_PROPERTIES_FILE_NAME
argument_list|)
decl_stmt|;
name|URL
name|configPropURL
init|=
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|Properties
name|props
init|=
name|loadPropertiesFile
argument_list|(
name|configPropURL
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_SHUTDOWN_HOST
argument_list|,
literal|"localhost"
argument_list|)
decl_stmt|;
name|String
name|shutdown
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_SHUTDOWN_COMMAND
argument_list|,
name|DEFAULT_SHUTDOWN_COMMAND
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|getShutDownPort
argument_list|(
name|props
argument_list|)
decl_stmt|;
comment|// We found the port, try to send the command
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|tryShutDownAndWait
argument_list|(
name|host
argument_list|,
name|shutdown
argument_list|,
name|port
argument_list|,
name|service
operator|.
name|getStopTimeout
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Unable to cleanly shutdown instance"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|tryShutDownAndWait
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|shutdownCommand
parameter_list|,
name|int
name|port
parameter_list|,
name|long
name|stopTimeout
parameter_list|)
throws|throws
name|UnknownHostException
throws|,
name|IOException
throws|,
name|InterruptedException
block|{
name|Socket
name|s
init|=
operator|new
name|Socket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|s
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
name|shutdownCommand
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
name|long
name|t
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
name|stopTimeout
decl_stmt|;
do|do
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|checkProcess
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|<
name|t
operator|&&
name|process
operator|!=
literal|null
condition|)
do|;
block|}
specifier|private
name|int
name|getShutDownPort
parameter_list|(
name|Properties
name|props
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|IOException
block|{
name|int
name|port
init|=
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
decl_stmt|;
comment|// Try to get port from port file
name|String
name|portFile
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|KARAF_SHUTDOWN_PORT_FILE
argument_list|)
decl_stmt|;
if|if
condition|(
name|port
operator|==
literal|0
operator|&&
name|portFile
operator|!=
literal|null
condition|)
block|{
name|BufferedReader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|portFile
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|portStr
init|=
name|r
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|portStr
argument_list|)
expr_stmt|;
name|r
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|port
return|;
block|}
specifier|protected
specifier|static
name|boolean
name|deleteFile
parameter_list|(
name|File
name|fileToDelete
parameter_list|)
block|{
if|if
condition|(
name|fileToDelete
operator|==
literal|null
operator|||
operator|!
name|fileToDelete
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|boolean
name|result
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|fileToDelete
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|files
init|=
name|fileToDelete
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|==
literal|null
condition|)
block|{
name|result
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|File
name|file
init|=
name|files
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"."
argument_list|)
operator|||
name|file
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|".."
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|result
operator|&=
name|deleteFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|&=
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
name|result
operator|&=
name|fileToDelete
operator|.
name|delete
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|protected
name|Properties
name|loadPropertiesFile
parameter_list|(
name|URL
name|configPropURL
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Read the properties file.
name|Properties
name|configProps
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|configProps
operator|.
name|put
argument_list|(
literal|"karaf.base"
argument_list|,
operator|new
name|File
argument_list|(
name|location
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|configProps
operator|.
name|put
argument_list|(
literal|"karaf.home"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.home"
argument_list|)
argument_list|)
expr_stmt|;
name|configProps
operator|.
name|put
argument_list|(
literal|"karaf.data"
argument_list|,
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|)
argument_list|,
literal|"data"
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|is
operator|=
name|configPropURL
operator|.
name|openConnection
argument_list|()
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|configProps
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
return|return
name|configProps
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error loading config properties from "
operator|+
name|configPropURL
argument_list|,
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
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
block|}
catch|catch
parameter_list|(
name|IOException
name|ex2
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|ex2
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isAttached
parameter_list|()
block|{
name|checkProcess
argument_list|()
expr_stmt|;
return|return
operator|(
name|process
operator|!=
literal|null
operator|)
return|;
block|}
block|}
end_class

end_unit

