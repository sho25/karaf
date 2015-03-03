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
name|client
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
name|IOException
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
name|slf4j
operator|.
name|impl
operator|.
name|SimpleLogger
import|;
end_import

begin_class
specifier|public
class|class
name|ClientConfig
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ROLE_DELIMITER
init|=
literal|","
decl_stmt|;
specifier|private
name|String
name|host
decl_stmt|;
specifier|private
name|int
name|port
decl_stmt|;
specifier|private
name|String
name|user
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|int
name|level
decl_stmt|;
specifier|private
name|int
name|retryAttempts
decl_stmt|;
specifier|private
name|int
name|retryDelay
decl_stmt|;
specifier|private
name|boolean
name|batch
decl_stmt|;
specifier|private
name|String
name|file
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|keyFile
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|command
decl_stmt|;
specifier|public
name|ClientConfig
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|shellCfg
init|=
name|loadProps
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
argument_list|,
literal|"org.apache.karaf.shell.cfg"
argument_list|)
argument_list|)
decl_stmt|;
name|host
operator|=
name|shellCfg
operator|.
name|getProperty
argument_list|(
literal|"sshHost"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
if|if
condition|(
name|host
operator|.
name|contains
argument_list|(
literal|"${"
argument_list|)
condition|)
block|{
comment|// if sshHost property contains a reference to another property (coming from etc/config.properties
comment|// or etc/custom.properties), we fall back to "localhost" default value
name|host
operator|=
literal|"localhost"
expr_stmt|;
block|}
name|String
name|portString
init|=
name|shellCfg
operator|.
name|getProperty
argument_list|(
literal|"sshPort"
argument_list|,
literal|"8101"
argument_list|)
decl_stmt|;
if|if
condition|(
name|portString
operator|.
name|contains
argument_list|(
literal|"${"
argument_list|)
condition|)
block|{
comment|// if sshPort property contains a reference to another property (coming from etc/config.properties
comment|// or etc/custom.properties), we fall back to "8101" default value
name|portString
operator|=
literal|"8101"
expr_stmt|;
block|}
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|portString
argument_list|)
expr_stmt|;
name|level
operator|=
name|SimpleLogger
operator|.
name|WARN
expr_stmt|;
name|retryAttempts
operator|=
literal|0
expr_stmt|;
name|retryDelay
operator|=
literal|2
expr_stmt|;
name|batch
operator|=
literal|false
expr_stmt|;
name|file
operator|=
literal|null
expr_stmt|;
name|user
operator|=
literal|null
expr_stmt|;
name|password
operator|=
literal|null
expr_stmt|;
name|StringBuilder
name|commandBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'-'
condition|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-a"
argument_list|)
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<=
operator|++
name|i
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"miss the port"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-h"
argument_list|)
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<=
operator|++
name|i
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"miss the host"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|host
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-u"
argument_list|)
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<=
operator|++
name|i
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"miss the user"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|user
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-v"
argument_list|)
condition|)
block|{
name|level
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-l"
argument_list|)
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<=
operator|++
name|i
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"miss the log level"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|levelValue
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|levelValue
argument_list|<
literal|0
operator|||
name|levelValue
argument_list|>
literal|4
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"log level can only be 0, 1, 2, 3, or 4"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|level
operator|=
name|levelValue
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-r"
argument_list|)
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<=
operator|++
name|i
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"miss the attempts"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retryAttempts
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-d"
argument_list|)
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<=
operator|++
name|i
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"miss the delay in seconds"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retryDelay
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-b"
argument_list|)
condition|)
block|{
name|batch
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-f"
argument_list|)
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<=
operator|++
name|i
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"miss the commands file"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|file
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-k"
argument_list|)
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<=
operator|++
name|i
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"miss the key file"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyFile
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"--help"
argument_list|)
condition|)
block|{
name|showHelp
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unknown option: "
operator|+
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Run with --help for usage"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|commandBuilder
operator|.
name|append
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|commandBuilder
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
name|command
operator|=
name|commandBuilder
operator|.
name|toString
argument_list|()
expr_stmt|;
name|Properties
name|usersCfg
init|=
name|loadProps
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
operator|+
literal|"/users.properties"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|usersCfg
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
name|user
operator|=
operator|(
name|String
operator|)
name|usersCfg
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|password
operator|=
operator|(
name|String
operator|)
name|usersCfg
operator|.
name|getProperty
argument_list|(
name|user
argument_list|)
expr_stmt|;
if|if
condition|(
name|password
operator|!=
literal|null
operator|&&
name|password
operator|.
name|contains
argument_list|(
name|ROLE_DELIMITER
argument_list|)
condition|)
block|{
name|password
operator|=
name|password
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|password
operator|.
name|indexOf
argument_list|(
name|ROLE_DELIMITER
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|showHelp
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Apache Karaf client"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -a [port]     specify the port to connect to"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -h [host]     specify the host to connect to"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -u [user]     specify the user name"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  --help        shows this help message"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -v            raise verbosity"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -l            set client logging level. Set to 0 for ERROR logging and up to 4 for TRACE."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -r [attempts] retry connection establishment (up to attempts times)"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -d [delay]    intra-retry delay (defaults to 2 seconds)"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -b            batch mode, specify multiple commands via standard input"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -f [file]     read commands from the specified file"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -k [keyFile]    specify the private keyFile location when using key login, need have BouncyCastle registered as security provider using this flag"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  [commands]    commands to run"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"If no commands are specified, the client will be put in an interactive mode"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Properties
name|loadProps
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|FileInputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Warning: could not load properties from: "
operator|+
name|file
operator|+
literal|", Reason: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
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
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
return|return
name|props
return|;
block|}
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|int
name|getLevel
parameter_list|()
block|{
return|return
name|level
return|;
block|}
specifier|public
name|int
name|getRetryAttempts
parameter_list|()
block|{
return|return
name|retryAttempts
return|;
block|}
specifier|public
name|int
name|getRetryDelay
parameter_list|()
block|{
return|return
name|retryDelay
return|;
block|}
specifier|public
name|String
name|getCommand
parameter_list|()
block|{
return|return
name|command
return|;
block|}
specifier|public
name|void
name|setCommand
parameter_list|(
name|String
name|command
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
block|}
specifier|public
name|boolean
name|isBatch
parameter_list|()
block|{
return|return
name|batch
return|;
block|}
specifier|public
name|String
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
specifier|public
name|String
name|getKeyFile
parameter_list|()
block|{
return|return
name|keyFile
return|;
block|}
block|}
end_class

end_unit

