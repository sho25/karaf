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
name|felix
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
name|BufferedOutputStream
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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|Handler
name|handler
decl_stmt|;
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
name|Properties
name|configProps
decl_stmt|;
specifier|public
specifier|static
name|Handler
name|getDefaultHandler
parameter_list|()
block|{
name|String
name|filename
decl_stmt|;
name|File
name|log
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|filename
operator|=
name|configProps
operator|.
name|getProperty
argument_list|(
name|KARAF_BOOTSTRAP_LOG
argument_list|)
expr_stmt|;
if|if
condition|(
name|filename
operator|!=
literal|null
condition|)
block|{
name|log
operator|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Make a best effort to log to the default file appender configured for log4j
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
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
operator|+
literal|"/etc/org.ops4j.pax.logging.cfg"
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
name|IOException
name|e
parameter_list|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
literal|"log4j.appender.out.file"
argument_list|,
literal|"${karaf.base}/data/log/karaf.log"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|fis
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|ioe
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|filename
operator|=
name|Main
operator|.
name|substVars
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
literal|"log4j.appender.out.file"
argument_list|)
argument_list|,
literal|"log4j.appender.out.file"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|log
operator|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|handler
operator|=
operator|new
name|BootstrapLogManager
operator|.
name|SimpleFileHandler
argument_list|(
name|log
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|handler
return|;
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
name|BootstrapLogManager
operator|.
name|configProps
operator|=
name|configProps
expr_stmt|;
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

