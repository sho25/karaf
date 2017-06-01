begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/* * Licensed to the Apache Software Foundation (ASF) under one or more * contributor license agreements.  See the NOTICE file distributed with * this work for additional information regarding copyright ownership. * The ASF licenses this file to You under the Apache License, Version 2.0 * (the "License"); you may not use this file except in compliance with * the License.  You may obtain a copy of the License at * *      http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|system
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
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
name|system
operator|.
name|FrameworkType
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
name|system
operator|.
name|SystemService
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
name|BundleContext
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
name|startlevel
operator|.
name|FrameworkStartLevel
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

begin_comment
comment|/**  * Implementation of the system service.  */
end_comment

begin_class
specifier|public
class|class
name|SystemServiceImpl
implements|implements
name|SystemService
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
name|SystemServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|this
operator|.
name|bundleContext
return|;
block|}
specifier|public
name|void
name|halt
parameter_list|()
throws|throws
name|Exception
block|{
name|halt
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|halt
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|Exception
block|{
name|shutdown
argument_list|(
name|timeToSleep
argument_list|(
name|time
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|reboot
parameter_list|()
throws|throws
name|Exception
block|{
name|reboot
argument_list|(
literal|null
argument_list|,
name|Swipe
operator|.
name|NONE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|reboot
parameter_list|(
name|String
name|time
parameter_list|,
name|Swipe
name|cleanup
parameter_list|)
throws|throws
name|Exception
block|{
name|reboot
argument_list|(
name|timeToSleep
argument_list|(
name|time
argument_list|)
argument_list|,
name|cleanup
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|shutdown
parameter_list|(
specifier|final
name|long
name|sleep
parameter_list|)
block|{
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|sleepWithMsg
argument_list|(
name|sleep
argument_list|,
literal|"Shutdown in "
operator|+
name|sleep
operator|/
literal|1000
operator|/
literal|60
operator|+
literal|" minute(s)"
argument_list|)
expr_stmt|;
name|getBundleContext
argument_list|()
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|stop
argument_list|()
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
name|error
argument_list|(
literal|"Halt error"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|reboot
parameter_list|(
specifier|final
name|long
name|sleep
parameter_list|,
specifier|final
name|Swipe
name|clean
parameter_list|)
block|{
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|sleepWithMsg
argument_list|(
name|sleep
argument_list|,
literal|"Reboot in "
operator|+
name|sleep
operator|/
literal|1000
operator|/
literal|60
operator|+
literal|" minute(s)"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.restart"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
if|if
condition|(
name|clean
operator|.
name|equals
argument_list|(
name|Swipe
operator|.
name|ALL
argument_list|)
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.clean.all"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clean
operator|.
name|equals
argument_list|(
name|Swipe
operator|.
name|CACHE
argument_list|)
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.clean.cache"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|stop
argument_list|()
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
name|error
argument_list|(
literal|"Reboot error"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|sleepWithMsg
parameter_list|(
specifier|final
name|long
name|sleep
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|sleep
operator|>
literal|0
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
name|sleep
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setStartLevel
parameter_list|(
name|int
name|startLevel
parameter_list|)
throws|throws
name|Exception
block|{
name|getBundleContext
argument_list|()
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
operator|.
name|setStartLevel
argument_list|(
name|startLevel
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getStartLevel
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getBundleContext
argument_list|()
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
operator|.
name|getStartLevel
argument_list|()
return|;
block|}
comment|/**      * Convert a time string to sleep period (in millisecond).      *      * @param time the time string.      * @return the corresponding sleep period in millisecond.      */
specifier|private
name|long
name|timeToSleep
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|sleep
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|time
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|time
operator|.
name|equals
argument_list|(
literal|"now"
argument_list|)
condition|)
block|{
if|if
condition|(
name|time
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
comment|// try to parse the date in hh:mm
name|String
index|[]
name|strings
init|=
name|time
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|strings
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Time "
operator|+
name|time
operator|+
literal|" is not valid (not in hour:minute format)"
argument_list|)
throw|;
block|}
name|int
name|hour
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|strings
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|int
name|minute
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|strings
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|hour
argument_list|<
literal|0
operator|||
name|hour
argument_list|>
literal|23
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Time "
operator|+
name|time
operator|+
literal|" is not valid (hour "
operator|+
name|hour
operator|+
literal|" is not between 0 and 23)"
argument_list|)
throw|;
block|}
if|if
condition|(
name|minute
argument_list|<
literal|0
operator|||
name|minute
argument_list|>
literal|59
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Time "
operator|+
name|time
operator|+
literal|" is not valid (minute "
operator|+
name|minute
operator|+
literal|" is not between 0 and 59)"
argument_list|)
throw|;
block|}
name|GregorianCalendar
name|currentDate
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|GregorianCalendar
name|shutdownDate
init|=
operator|new
name|GregorianCalendar
argument_list|(
name|currentDate
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
argument_list|,
name|currentDate
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MONTH
argument_list|)
argument_list|,
name|currentDate
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|)
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|strings
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|strings
index|[
literal|1
index|]
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|shutdownDate
operator|.
name|before
argument_list|(
name|currentDate
argument_list|)
condition|)
block|{
name|shutdownDate
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|DATE
argument_list|,
name|shutdownDate
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DATE
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|sleep
operator|=
name|shutdownDate
operator|.
name|getTimeInMillis
argument_list|()
operator|-
name|currentDate
operator|.
name|getTimeInMillis
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|time
operator|.
name|startsWith
argument_list|(
literal|"+"
argument_list|)
condition|)
block|{
name|time
operator|=
name|time
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|sleep
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|time
argument_list|)
operator|*
literal|60
operator|*
literal|1000
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Time "
operator|+
name|time
operator|+
literal|" is not valid"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
return|return
name|sleep
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.version"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|bundleContext
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|String
name|karafEtc
init|=
name|bundleContext
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
decl_stmt|;
name|File
name|etcDir
init|=
operator|new
name|File
argument_list|(
name|karafEtc
argument_list|)
decl_stmt|;
name|File
name|syspropsFile
init|=
operator|new
name|File
argument_list|(
name|etcDir
argument_list|,
literal|"system.properties"
argument_list|)
decl_stmt|;
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|syspropsFile
argument_list|)
decl_stmt|;
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
name|fis
argument_list|)
expr_stmt|;
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"karaf.name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|syspropsFile
argument_list|)
decl_stmt|;
name|props
operator|.
name|store
argument_list|(
name|fos
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
specifier|public
name|FrameworkType
name|getFramework
parameter_list|()
block|{
if|if
condition|(
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|getSymbolicName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"felix"
argument_list|)
condition|)
block|{
return|return
name|FrameworkType
operator|.
name|felix
return|;
block|}
else|else
block|{
return|return
name|FrameworkType
operator|.
name|equinox
return|;
block|}
block|}
specifier|private
name|Properties
name|loadProps
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|Properties
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
literal|"config.properties"
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setFramework
parameter_list|(
name|FrameworkType
name|framework
parameter_list|)
block|{
if|if
condition|(
name|framework
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|Properties
name|properties
init|=
name|loadProps
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"karaf.framework"
argument_list|,
name|framework
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|save
argument_list|()
expr_stmt|;
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
literal|"Error setting framework: "
operator|+
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
specifier|public
name|void
name|setFrameworkDebug
parameter_list|(
name|boolean
name|debug
parameter_list|)
block|{
try|try
block|{
name|Properties
name|properties
init|=
name|loadProps
argument_list|()
decl_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"felix.log.level"
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"osgi.debug"
argument_list|,
literal|"etc/equinox-debug.properties"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|properties
operator|.
name|remove
argument_list|(
literal|"felix.log.level"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|remove
argument_list|(
literal|"osgi.debug"
argument_list|)
expr_stmt|;
block|}
comment|// TODO populate the equinox-debug.properties file with the one provided in shell/dev module
name|properties
operator|.
name|save
argument_list|()
expr_stmt|;
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
literal|"Error setting framework debugging: "
operator|+
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
annotation|@
name|Override
specifier|public
name|String
name|setSystemProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|persist
parameter_list|)
block|{
if|if
condition|(
name|persist
condition|)
block|{
try|try
block|{
name|String
name|etc
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|(
operator|new
name|File
argument_list|(
name|etc
argument_list|,
literal|"system.properties"
argument_list|)
argument_list|)
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|props
operator|.
name|save
argument_list|()
expr_stmt|;
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
literal|"Error persisting system property"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|System
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

