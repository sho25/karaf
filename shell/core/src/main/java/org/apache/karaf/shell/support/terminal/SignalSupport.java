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
name|shell
operator|.
name|support
operator|.
name|terminal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
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
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Signal
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
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|SignalListener
import|;
end_import

begin_class
specifier|public
class|class
name|SignalSupport
block|{
specifier|protected
specifier|final
name|ConcurrentMap
argument_list|<
name|Signal
argument_list|,
name|Set
argument_list|<
name|SignalListener
argument_list|>
argument_list|>
name|listeners
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|public
name|void
name|addSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|,
name|Signal
modifier|...
name|signals
parameter_list|)
block|{
if|if
condition|(
name|signals
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"signals may not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"listener may not be null"
argument_list|)
throw|;
block|}
for|for
control|(
name|Signal
name|s
range|:
name|signals
control|)
block|{
name|getSignalListeners
argument_list|(
name|s
argument_list|,
literal|true
argument_list|)
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|)
block|{
name|addSignalListener
argument_list|(
name|listener
argument_list|,
name|EnumSet
operator|.
name|allOf
argument_list|(
name|Signal
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|,
name|EnumSet
argument_list|<
name|Signal
argument_list|>
name|signals
parameter_list|)
block|{
if|if
condition|(
name|signals
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"signals may not be null"
argument_list|)
throw|;
block|}
name|addSignalListener
argument_list|(
name|listener
argument_list|,
name|signals
operator|.
name|toArray
argument_list|(
operator|new
name|Signal
index|[
name|signals
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"listener may not be null"
argument_list|)
throw|;
block|}
for|for
control|(
name|Signal
name|s
range|:
name|EnumSet
operator|.
name|allOf
argument_list|(
name|Signal
operator|.
name|class
argument_list|)
control|)
block|{
specifier|final
name|Set
argument_list|<
name|SignalListener
argument_list|>
name|ls
init|=
name|getSignalListeners
argument_list|(
name|s
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|ls
operator|!=
literal|null
condition|)
block|{
name|ls
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|signal
parameter_list|(
name|Signal
name|signal
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|SignalListener
argument_list|>
name|ls
init|=
name|getSignalListeners
argument_list|(
name|signal
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|ls
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|SignalListener
name|l
range|:
name|ls
control|)
block|{
name|l
operator|.
name|signal
argument_list|(
name|signal
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Set
argument_list|<
name|SignalListener
argument_list|>
name|getSignalListeners
parameter_list|(
name|Signal
name|signal
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
return|return
name|listeners
operator|.
name|compute
argument_list|(
name|signal
argument_list|,
operator|(
name|sig
operator|,
name|lst
operator|)
operator|->
name|lst
operator|!=
literal|null
condition|?
name|lst
else|:
name|create
condition|?
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
else|:
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

