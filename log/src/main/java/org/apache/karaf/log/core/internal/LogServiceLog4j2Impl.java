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
name|Collections
import|;
end_import

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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|collections
operator|.
name|DictionaryAsMap
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
name|LogServiceLog4j2Impl
implements|implements
name|LogServiceInternal
block|{
specifier|static
specifier|final
name|String
name|ROOT_LOGGER_LEVEL
init|=
literal|"log4j2.rootLogger.level"
decl_stmt|;
specifier|static
specifier|final
name|String
name|LOGGER_PREFIX
init|=
literal|"log4j2.logger."
decl_stmt|;
specifier|static
specifier|final
name|String
name|NAME_SUFFIX
init|=
literal|".name"
decl_stmt|;
specifier|static
specifier|final
name|String
name|LEVEL_SUFFIX
init|=
literal|".level"
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
decl_stmt|;
specifier|private
name|Pattern
name|namePattern
decl_stmt|;
specifier|private
name|Pattern
name|levelPattern
decl_stmt|;
specifier|public
name|LogServiceLog4j2Impl
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
operator|new
name|DictionaryAsMap
argument_list|<>
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|namePattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"log4j2\\.logger\\.([a-zA-Z_]+)\\.name"
argument_list|)
expr_stmt|;
name|levelPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"log4j2\\.logger\\.([a-zA-Z_]+)\\.level"
argument_list|)
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
name|String
name|root
init|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|ROOT_LOGGER_LEVEL
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
if|if
condition|(
name|ROOT_LOGGER
operator|.
name|equals
argument_list|(
name|logger
argument_list|)
condition|)
block|{
return|return
name|loggers
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|names
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|levels
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|config
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|loggerName
init|=
name|getMatching
argument_list|(
name|namePattern
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|loggerName
operator|!=
literal|null
condition|)
block|{
name|names
operator|.
name|put
argument_list|(
name|loggerName
argument_list|,
name|config
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|loggerName
operator|=
name|getMatching
argument_list|(
name|levelPattern
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|loggerName
operator|!=
literal|null
condition|)
block|{
name|levels
operator|.
name|put
argument_list|(
name|loggerName
argument_list|,
name|config
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|names
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|loggers
operator|.
name|put
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
name|levels
operator|.
name|get
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
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
name|val
operator|=
name|loggers
operator|.
name|get
argument_list|(
name|l
operator|!=
literal|null
condition|?
name|l
else|:
name|ROOT_LOGGER
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
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
name|logger
argument_list|,
name|val
argument_list|)
return|;
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
block|}
specifier|private
name|String
name|getMatching
parameter_list|(
name|Pattern
name|pattern
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|matcher
operator|.
name|matches
argument_list|()
operator|)
condition|?
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
else|:
literal|null
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
name|config
operator|.
name|put
argument_list|(
name|ROOT_LOGGER_LEVEL
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|loggerKey
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|config
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Matcher
name|matcher
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\Q"
operator|+
name|LOGGER_PREFIX
operator|+
literal|"\\E([a-zA-Z_]+)\\Q"
operator|+
name|NAME_SUFFIX
operator|+
literal|"\\E"
argument_list|)
operator|.
name|matcher
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|config
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|matches
argument_list|(
name|logger
argument_list|)
condition|)
block|{
name|loggerKey
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|loggerKey
operator|!=
literal|null
condition|)
block|{
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
name|config
operator|.
name|remove
argument_list|(
name|level
argument_list|(
name|loggerKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|config
operator|.
name|put
argument_list|(
name|level
argument_list|(
name|loggerKey
argument_list|)
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|loggerKey
operator|=
name|logger
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'_'
argument_list|)
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
name|name
argument_list|(
name|loggerKey
argument_list|)
argument_list|,
name|logger
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
name|level
argument_list|(
name|loggerKey
argument_list|)
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|name
parameter_list|(
name|String
name|logger
parameter_list|)
block|{
return|return
name|LOGGER_PREFIX
operator|+
name|logger
operator|+
name|NAME_SUFFIX
return|;
block|}
specifier|private
name|String
name|level
parameter_list|(
name|String
name|logger
parameter_list|)
block|{
return|return
name|LOGGER_PREFIX
operator|+
name|logger
operator|+
name|LEVEL_SUFFIX
return|;
block|}
block|}
end_class

end_unit

