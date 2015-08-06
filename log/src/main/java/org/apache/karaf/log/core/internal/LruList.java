begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|ArrayList
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
name|List
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
name|PaxAppender
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

begin_comment
comment|/**  * A list that only keep the last N elements added  */
end_comment

begin_class
specifier|public
class|class
name|LruList
implements|implements
name|PaxAppender
block|{
specifier|private
name|PaxLoggingEvent
index|[]
name|elements
decl_stmt|;
specifier|private
specifier|transient
name|int
name|start
init|=
literal|0
decl_stmt|;
specifier|private
specifier|transient
name|int
name|end
init|=
literal|0
decl_stmt|;
specifier|private
specifier|transient
name|boolean
name|full
init|=
literal|false
decl_stmt|;
specifier|private
specifier|final
name|int
name|maxElements
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|PaxAppender
argument_list|>
name|appenders
decl_stmt|;
specifier|public
name|LruList
parameter_list|(
name|int
name|size
parameter_list|)
block|{
if|if
condition|(
name|size
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The size must be greater than 0"
argument_list|)
throw|;
block|}
name|elements
operator|=
operator|new
name|PaxLoggingEvent
index|[
name|size
index|]
expr_stmt|;
name|maxElements
operator|=
name|elements
operator|.
name|length
expr_stmt|;
name|appenders
operator|=
operator|new
name|ArrayList
argument_list|<
name|PaxAppender
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|int
name|size
parameter_list|()
block|{
name|int
name|size
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|end
operator|<
name|start
condition|)
block|{
name|size
operator|=
name|maxElements
operator|-
name|start
operator|+
name|end
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|end
operator|==
name|start
condition|)
block|{
name|size
operator|=
operator|(
name|full
condition|?
name|maxElements
else|:
literal|0
operator|)
expr_stmt|;
block|}
else|else
block|{
name|size
operator|=
name|end
operator|-
name|start
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|clear
parameter_list|()
block|{
name|start
operator|=
literal|0
expr_stmt|;
name|end
operator|=
literal|0
expr_stmt|;
name|full
operator|=
literal|false
expr_stmt|;
name|elements
operator|=
operator|new
name|PaxLoggingEvent
index|[
name|maxElements
index|]
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|add
parameter_list|(
name|PaxLoggingEvent
name|element
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|element
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Attempted to add null object to buffer"
argument_list|)
throw|;
block|}
if|if
condition|(
name|size
argument_list|()
operator|==
name|maxElements
condition|)
block|{
name|Object
name|e
init|=
name|elements
index|[
name|start
index|]
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|e
condition|)
block|{
name|elements
index|[
name|start
operator|++
index|]
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|start
operator|>=
name|maxElements
condition|)
block|{
name|start
operator|=
literal|0
expr_stmt|;
block|}
name|full
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|elements
index|[
name|end
operator|++
index|]
operator|=
name|element
expr_stmt|;
if|if
condition|(
name|end
operator|>=
name|maxElements
condition|)
block|{
name|end
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|end
operator|==
name|start
condition|)
block|{
name|full
operator|=
literal|true
expr_stmt|;
block|}
for|for
control|(
name|PaxAppender
name|appender
range|:
name|appenders
control|)
block|{
try|try
block|{
name|appender
operator|.
name|doAppend
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
specifier|public
specifier|synchronized
name|Iterable
argument_list|<
name|PaxLoggingEvent
argument_list|>
name|getElements
parameter_list|()
block|{
return|return
name|getElements
argument_list|(
name|size
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|Iterable
argument_list|<
name|PaxLoggingEvent
argument_list|>
name|getElements
parameter_list|(
name|int
name|nb
parameter_list|)
block|{
name|int
name|s
init|=
name|size
argument_list|()
decl_stmt|;
name|nb
operator|=
name|Math
operator|.
name|min
argument_list|(
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|nb
argument_list|)
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|PaxLoggingEvent
index|[]
name|e
init|=
operator|new
name|PaxLoggingEvent
index|[
name|nb
index|]
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
name|nb
condition|;
name|i
operator|++
control|)
block|{
name|e
index|[
name|i
index|]
operator|=
name|elements
index|[
operator|(
name|i
operator|+
name|s
operator|-
name|nb
operator|+
name|start
operator|)
operator|%
name|maxElements
index|]
expr_stmt|;
block|}
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|e
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|addAppender
parameter_list|(
name|PaxAppender
name|appender
parameter_list|)
block|{
name|this
operator|.
name|appenders
operator|.
name|add
argument_list|(
name|appender
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|removeAppender
parameter_list|(
name|PaxAppender
name|appender
parameter_list|)
block|{
name|this
operator|.
name|appenders
operator|.
name|remove
argument_list|(
name|appender
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|doAppend
parameter_list|(
name|PaxLoggingEvent
name|event
parameter_list|)
block|{
name|event
operator|.
name|getProperties
argument_list|()
expr_stmt|;
comment|// ensure MDC properties are copied
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

