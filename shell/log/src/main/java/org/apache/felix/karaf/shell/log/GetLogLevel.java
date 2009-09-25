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
name|shell
operator|.
name|log
package|;
end_package

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
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|OsgiCommandSupport
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
name|gogo
operator|.
name|commands
operator|.
name|Argument
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
name|gogo
operator|.
name|commands
operator|.
name|Command
import|;
end_import

begin_comment
comment|/**  * Get the log level for a given logger  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"log"
argument_list|,
name|name
operator|=
literal|"get"
argument_list|,
name|description
operator|=
literal|"Shows the currently set log level."
argument_list|)
specifier|public
class|class
name|GetLogLevel
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"logger"
argument_list|,
name|description
operator|=
literal|"The name of the logger, ALL or ROOT (default)"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|logger
decl_stmt|;
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
name|ALL_LOGGER
init|=
literal|"ALL"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ROOT_LOGGER
init|=
literal|"ROOT"
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
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
name|this
operator|.
name|logger
argument_list|)
condition|)
block|{
name|this
operator|.
name|logger
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|ALL_LOGGER
operator|.
name|equalsIgnoreCase
argument_list|(
name|logger
argument_list|)
condition|)
block|{
name|String
name|root
init|=
name|getLevel
argument_list|(
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|ROOT_LOGGER_PREFIX
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|loggers
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Enumeration
name|e
init|=
name|props
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|prop
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|prop
operator|.
name|startsWith
argument_list|(
name|LOGGER_PREFIX
argument_list|)
condition|)
block|{
name|String
name|val
init|=
name|getLevel
argument_list|(
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|prop
argument_list|)
argument_list|)
decl_stmt|;
name|loggers
operator|.
name|put
argument_list|(
name|prop
operator|.
name|substring
argument_list|(
name|LOGGER_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"ROOT: "
operator|+
name|root
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|logger
range|:
name|loggers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|logger
operator|+
literal|": "
operator|+
name|loggers
operator|.
name|get
argument_list|(
name|logger
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|logger
init|=
name|this
operator|.
name|logger
decl_stmt|;
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
if|if
condition|(
name|logger
operator|!=
name|this
operator|.
name|logger
condition|)
block|{
name|st
operator|+=
literal|" (inherited from "
operator|+
operator|(
name|logger
operator|!=
literal|null
condition|?
name|logger
else|:
literal|"ROOT"
operator|)
operator|+
literal|")"
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|st
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
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
specifier|protected
name|ConfigurationAdmin
name|getConfigAdmin
parameter_list|()
block|{
name|ServiceReference
name|ref
init|=
name|getBundleContext
argument_list|()
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
return|return
name|getService
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|,
name|ref
argument_list|)
return|;
block|}
block|}
end_class

end_unit

