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
name|management
operator|.
name|mbeans
operator|.
name|log
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|management
operator|.
name|mbeans
operator|.
name|log
operator|.
name|LogMBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|logging
operator|.
name|spi
operator|.
name|PaxLoggingEvent
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
name|ServiceReference
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
name|javax
operator|.
name|management
operator|.
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StandardMBean
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
name|*
import|;
end_import

begin_comment
comment|/**  * Implementation of the Log MBean.  */
end_comment

begin_class
specifier|public
class|class
name|LogMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|LogMBean
block|{
specifier|static
specifier|final
name|String
name|CONFIGURATION_PID
init|=
literal|"org.ops4j.pax.logging"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ROOT_LOGGER_PREFIX
init|=
literal|"log4j.rootLogger"
decl_stmt|;
specifier|static
specifier|final
name|String
name|LOGGER_PREFIX
init|=
literal|"log4j.logger."
decl_stmt|;
specifier|static
specifier|final
name|String
name|ROOT_LOGGER
init|=
literal|"ROOT"
decl_stmt|;
specifier|private
name|LruList
name|events
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|public
name|LogMBeanImpl
parameter_list|()
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|LogMBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|display
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|display
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|display
parameter_list|(
name|String
name|logger
parameter_list|)
throws|throws
name|Exception
block|{
name|Iterable
argument_list|<
name|PaxLoggingEvent
argument_list|>
name|le
init|=
name|events
operator|.
name|getElements
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|out
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PaxLoggingEvent
name|event
range|:
name|le
control|)
block|{
if|if
condition|(
operator|(
name|logger
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|event
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|checkIfFromRequestedLog
argument_list|(
name|event
argument_list|,
name|logger
argument_list|)
operator|)
condition|)
block|{
name|out
operator|.
name|add
argument_list|(
name|render
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|event
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|logger
operator|==
literal|null
operator|)
condition|)
block|{
name|out
operator|.
name|add
argument_list|(
name|render
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|out
return|;
block|}
specifier|public
name|void
name|set
parameter_list|(
name|String
name|level
parameter_list|)
throws|throws
name|Exception
block|{
name|set
argument_list|(
name|level
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|set
parameter_list|(
name|String
name|level
parameter_list|,
name|String
name|logger
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|ROOT_LOGGER
operator|.
name|equalsIgnoreCase
argument_list|(
name|logger
argument_list|)
condition|)
block|{
name|logger
operator|=
literal|null
expr_stmt|;
block|}
comment|// make sure both uppercase and lowercase levels are supported
name|level
operator|=
name|level
operator|.
name|toUpperCase
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|level
operator|.
name|equals
argument_list|(
literal|"TRACE"
argument_list|)
operator|&&
operator|!
name|level
operator|.
name|equals
argument_list|(
literal|"DEBUG"
argument_list|)
operator|&&
operator|!
name|level
operator|.
name|equals
argument_list|(
literal|"INFO"
argument_list|)
operator|&&
operator|!
name|level
operator|.
name|equals
argument_list|(
literal|"WARN"
argument_list|)
operator|&&
operator|!
name|level
operator|.
name|equals
argument_list|(
literal|"ERROR"
argument_list|)
operator|&&
operator|!
name|level
operator|.
name|equals
argument_list|(
literal|"DEFAULT"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"level must be set to TRACE, DEBUG, INFO, WARN or ERROR (or DEFAULT to unset it)"
argument_list|)
throw|;
block|}
name|Configuration
name|cfg
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
name|Dictionary
name|props
init|=
name|cfg
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|String
name|val
decl_stmt|;
name|String
name|prop
decl_stmt|;
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|prop
operator|=
name|ROOT_LOGGER_PREFIX
expr_stmt|;
block|}
else|else
block|{
name|prop
operator|=
name|LOGGER_PREFIX
operator|+
name|logger
expr_stmt|;
block|}
name|val
operator|=
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|prop
argument_list|)
expr_stmt|;
if|if
condition|(
name|level
operator|.
name|equals
argument_list|(
literal|"DEFAULT"
argument_list|)
condition|)
block|{
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|val
operator|=
name|val
operator|.
name|trim
argument_list|()
expr_stmt|;
name|int
name|idx
init|=
name|val
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|val
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|val
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
name|level
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|val
operator|.
name|trim
argument_list|()
expr_stmt|;
name|int
name|idx
init|=
name|val
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|val
operator|=
name|level
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|level
operator|+
name|val
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|props
operator|.
name|remove
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|props
operator|.
name|put
argument_list|(
name|prop
argument_list|,
name|val
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
specifier|public
name|String
name|get
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|get
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|public
name|String
name|get
parameter_list|(
name|String
name|logger
parameter_list|)
throws|throws
name|Exception
block|{
name|ConfigurationAdmin
name|cfgAdmin
init|=
name|getConfigAdmin
argument_list|()
decl_stmt|;
name|Configuration
name|cfg
init|=
name|cfgAdmin
operator|.
name|getConfiguration
argument_list|(
name|CONFIGURATION_PID
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Dictionary
name|props
init|=
name|cfg
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|ROOT_LOGGER
operator|.
name|equalsIgnoreCase
argument_list|(
name|logger
argument_list|)
condition|)
block|{
name|logger
operator|=
literal|null
expr_stmt|;
block|}
name|String
name|val
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|String
name|prop
decl_stmt|;
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|prop
operator|=
name|ROOT_LOGGER_PREFIX
expr_stmt|;
block|}
else|else
block|{
name|prop
operator|=
name|LOGGER_PREFIX
operator|+
name|logger
expr_stmt|;
block|}
name|val
operator|=
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|val
operator|=
name|getLevel
argument_list|(
name|val
argument_list|)
expr_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
operator|||
name|logger
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|int
name|idx
init|=
name|logger
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|logger
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|=
name|logger
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|st
init|=
literal|"Level: "
operator|+
name|val
decl_stmt|;
return|return
name|st
return|;
block|}
specifier|private
name|boolean
name|checkIfFromRequestedLog
parameter_list|(
name|PaxLoggingEvent
name|event
parameter_list|,
name|String
name|logger
parameter_list|)
block|{
return|return
operator|(
name|event
operator|.
name|getLoggerName
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
name|logger
argument_list|)
operator|>=
literal|0
operator|)
condition|?
literal|true
else|:
literal|false
return|;
block|}
specifier|private
name|String
name|render
parameter_list|(
name|PaxLoggingEvent
name|event
parameter_list|)
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|sb
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|event
operator|.
name|getThrowableStrRep
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|r
range|:
name|event
operator|.
name|getThrowableStrRep
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|r
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|getLevel
parameter_list|(
name|String
name|prop
parameter_list|)
block|{
if|if
condition|(
name|prop
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|String
name|val
init|=
name|prop
operator|.
name|trim
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|val
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|==
literal|0
condition|)
block|{
name|val
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|val
operator|=
name|val
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
return|return
name|val
return|;
block|}
block|}
specifier|public
name|LruList
name|getEvents
parameter_list|()
block|{
return|return
name|this
operator|.
name|events
return|;
block|}
specifier|public
name|void
name|setEvents
parameter_list|(
name|LruList
name|events
parameter_list|)
block|{
name|this
operator|.
name|events
operator|=
name|events
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
specifier|protected
name|Configuration
name|getConfiguration
parameter_list|()
throws|throws
name|IOException
block|{
name|Configuration
name|cfg
init|=
name|getConfigAdmin
argument_list|()
operator|.
name|getConfiguration
argument_list|(
name|CONFIGURATION_PID
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|cfg
return|;
block|}
specifier|protected
name|ConfigurationAdmin
name|getConfigAdmin
parameter_list|()
block|{
name|ServiceReference
name|ref
init|=
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|ConfigurationAdmin
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

