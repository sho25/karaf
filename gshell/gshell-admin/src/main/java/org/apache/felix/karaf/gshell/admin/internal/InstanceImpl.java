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
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|admin
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
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|admin
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
name|felix
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
name|felix
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
name|felix
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
name|AdminServiceImpl
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
name|Process
name|process
decl_stmt|;
specifier|public
name|InstanceImpl
parameter_list|(
name|AdminServiceImpl
name|service
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|location
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
name|ProcessBuilderFactory
operator|.
name|newInstance
argument_list|()
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
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|location
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
name|getPort
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|location
argument_list|,
literal|"etc/org.apache.felix.karaf.shell.cfg"
argument_list|)
decl_stmt|;
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|loc
init|=
name|props
operator|.
name|getProperty
argument_list|(
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
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|changePort
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
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|location
argument_list|,
literal|"etc/org.apache.felix.karaf.shell.cfg"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|f
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
name|setProperty
argument_list|(
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
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
try|try
block|{
name|props
operator|.
name|store
argument_list|(
name|os
argument_list|,
literal|null
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
condition|)
block|{
name|javaOpts
operator|=
literal|"-server -Xmx512M -Dcom.sun.management.jmxremote"
expr_stmt|;
block|}
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
name|String
name|command
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
name|ScriptUtils
operator|.
name|isWindows
argument_list|()
condition|?
literal|"bin\\java.exe"
else|:
literal|"bin/java"
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
operator|+
literal|" "
operator|+
name|javaOpts
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
literal|" org.apache.felix.karaf.main.Bootstrap"
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting instance with command: "
operator|+
name|command
argument_list|)
expr_stmt|;
name|this
operator|.
name|process
operator|=
name|ProcessBuilderFactory
operator|.
name|newInstance
argument_list|()
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
name|this
operator|.
name|process
operator|.
name|destroy
argument_list|()
expr_stmt|;
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
name|int
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
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
block|}
end_class

end_unit

