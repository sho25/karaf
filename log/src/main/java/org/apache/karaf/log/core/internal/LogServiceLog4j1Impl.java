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
name|log
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
name|apache
operator|.
name|karaf
operator|.
name|log
operator|.
name|core
operator|.
name|Level
import|;
end_import

begin_class
specifier|public
class|class
name|LogServiceLog4j1Impl
implements|implements
name|LogServiceInternal
block|{
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
specifier|private
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
decl_stmt|;
specifier|public
name|LogServiceLog4j1Impl
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getLevel
parameter_list|(
name|String
name|logger
parameter_list|)
block|{
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
argument_list|<>
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
name|getLevelFromProperty
argument_list|(
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|ROOT_LOGGER_PREFIX
argument_list|)
argument_list|)
decl_stmt|;
name|loggers
operator|.
name|put
argument_list|(
name|ROOT_LOGGER
argument_list|,
name|root
argument_list|)
expr_stmt|;
for|for
control|(
name|Enumeration
name|e
init|=
name|config
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
name|getLevelFromProperty
argument_list|(
operator|(
name|String
operator|)
name|config
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
return|return
name|loggers
return|;
block|}
name|String
name|l
init|=
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
name|l
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
name|l
expr_stmt|;
block|}
name|val
operator|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|val
operator|=
name|getLevelFromProperty
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
name|l
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|int
name|idx
init|=
name|l
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
name|l
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|l
operator|=
name|l
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
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
name|logger
operator|=
name|ROOT_LOGGER
expr_stmt|;
name|loggers
operator|.
name|put
argument_list|(
name|logger
argument_list|,
name|val
argument_list|)
expr_stmt|;
return|return
name|loggers
return|;
block|}
specifier|public
name|void
name|setLevel
parameter_list|(
name|String
name|logger
parameter_list|,
name|String
name|level
parameter_list|)
block|{
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
operator|||
name|LogServiceInternal
operator|.
name|ROOT_LOGGER
operator|.
name|equalsIgnoreCase
argument_list|(
name|logger
argument_list|)
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
name|config
operator|.
name|get
argument_list|(
name|prop
argument_list|)
expr_stmt|;
if|if
condition|(
name|Level
operator|.
name|isDefault
argument_list|(
name|level
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
name|config
operator|.
name|remove
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|config
operator|.
name|put
argument_list|(
name|prop
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getLevelFromProperty
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
block|}
end_class

end_unit

