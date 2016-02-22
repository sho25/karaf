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
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|ConsoleHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|LogRecord
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|StreamHandler
import|;
end_import

begin_comment
comment|/**  * Convenience class for configuring java.util.logging to append to  * the configured log4j log.  This could be used for bootstrap logging  * prior to start of the framework.  *   */
end_comment

begin_class
specifier|public
class|class
name|BootstrapLogManager
block|{
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_BOOTSTRAP_LOG
init|=
literal|"karaf.bootstrap.log"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOG4J_APPENDER_FILE
init|=
literal|"log4j.appender.out.file"
decl_stmt|;
specifier|private
specifier|static
name|BootstrapLogManager
name|instance
decl_stmt|;
specifier|private
name|Handler
name|handler
decl_stmt|;
specifier|private
name|Properties
name|configProps
decl_stmt|;
specifier|private
name|String
name|log4jConfigPath
decl_stmt|;
specifier|public
name|BootstrapLogManager
parameter_list|(
name|Properties
name|configProps
parameter_list|,
name|String
name|log4jConfigPath
parameter_list|)
block|{
name|this
operator|.
name|configProps
operator|=
name|configProps
expr_stmt|;
name|this
operator|.
name|log4jConfigPath
operator|=
name|log4jConfigPath
expr_stmt|;
name|this
operator|.
name|handler
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|Handler
name|getDefaultHandler
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Properties must be set before calling getDefaultHandler"
argument_list|)
throw|;
block|}
return|return
name|instance
operator|.
name|getDefaultHandlerInternal
argument_list|()
return|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|List
argument_list|<
name|Handler
argument_list|>
name|getDefaultHandlers
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Properties must be set before calling getDefaultHandler"
argument_list|)
throw|;
block|}
return|return
name|instance
operator|.
name|getDefaultHandlersInternal
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|configureLogger
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
try|try
block|{
for|for
control|(
name|Handler
name|handler
range|:
name|getDefaultHandlers
argument_list|()
control|)
block|{
name|logger
operator|.
name|addHandler
argument_list|(
name|handler
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
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|setProperties
parameter_list|(
name|Properties
name|configProps
parameter_list|)
block|{
name|setProperties
argument_list|(
name|configProps
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|setProperties
parameter_list|(
name|Properties
name|configProps
parameter_list|,
name|String
name|log4jConfigPath
parameter_list|)
block|{
name|instance
operator|=
operator|new
name|BootstrapLogManager
argument_list|(
name|configProps
argument_list|,
name|log4jConfigPath
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Handler
name|getDefaultHandlerInternal
parameter_list|()
block|{
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
return|return
name|handler
return|;
block|}
name|String
name|filename
init|=
name|getLogFilePath
argument_list|()
decl_stmt|;
name|filename
operator|=
name|InterpolationHelper
operator|.
name|substVars
argument_list|(
name|filename
argument_list|,
name|LOG4J_APPENDER_FILE
argument_list|,
literal|null
argument_list|,
name|configProps
argument_list|)
expr_stmt|;
name|File
name|logFile
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|new
name|SimpleFileHandler
argument_list|(
name|logFile
argument_list|)
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
specifier|private
name|List
argument_list|<
name|Handler
argument_list|>
name|getDefaultHandlersInternal
parameter_list|()
block|{
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"karaf.log.console"
argument_list|)
condition|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ConsoleHandler
argument_list|()
argument_list|,
name|getDefaultHandlerInternal
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|getDefaultHandlerInternal
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|private
name|Properties
name|loadPaxLoggingConfig
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|log4jConfigPath
argument_list|)
expr_stmt|;
name|props
operator|.
name|load
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
finally|finally
block|{
name|close
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
return|return
name|props
return|;
block|}
specifier|private
specifier|static
name|void
name|close
parameter_list|(
name|Closeable
name|closeable
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|closeable
operator|!=
literal|null
condition|)
block|{
name|closeable
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
name|String
name|getLogFilePath
parameter_list|()
block|{
name|String
name|filename
init|=
name|configProps
operator|==
literal|null
condition|?
literal|null
else|:
name|configProps
operator|.
name|getProperty
argument_list|(
name|KARAF_BOOTSTRAP_LOG
argument_list|)
decl_stmt|;
if|if
condition|(
name|filename
operator|!=
literal|null
condition|)
block|{
return|return
name|filename
return|;
block|}
name|Properties
name|props
init|=
name|loadPaxLoggingConfig
argument_list|()
decl_stmt|;
comment|// Make a best effort to log to the default file appender configured for log4j
return|return
name|props
operator|.
name|getProperty
argument_list|(
name|LOG4J_APPENDER_FILE
argument_list|,
literal|"${karaf.data}/log/karaf.log"
argument_list|)
return|;
block|}
comment|/**      * Implementation of java.util.logging.Handler that does simple appending      * to a named file.  Should be able to use this for bootstrap logging      * via java.util.logging prior to startup of pax logging.      */
specifier|public
specifier|static
class|class
name|SimpleFileHandler
extends|extends
name|StreamHandler
block|{
specifier|public
name|SimpleFileHandler
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|open
argument_list|(
name|file
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|open
parameter_list|(
name|File
name|logfile
parameter_list|,
name|boolean
name|append
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|logfile
operator|.
name|getParentFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|logfile
operator|.
name|getParentFile
argument_list|()
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
name|IOException
argument_list|(
name|se
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|FileOutputStream
name|fout
init|=
operator|new
name|FileOutputStream
argument_list|(
name|logfile
argument_list|,
name|append
argument_list|)
decl_stmt|;
name|BufferedOutputStream
name|out
init|=
operator|new
name|BufferedOutputStream
argument_list|(
name|fout
argument_list|)
decl_stmt|;
name|setOutputStream
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|publish
parameter_list|(
name|LogRecord
name|record
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isLoggable
argument_list|(
name|record
argument_list|)
condition|)
block|{
return|return;
block|}
name|super
operator|.
name|publish
argument_list|(
name|record
argument_list|)
expr_stmt|;
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

