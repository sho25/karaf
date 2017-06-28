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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactory
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_class
specifier|public
class|class
name|ThreadUtils
block|{
comment|/**      * Constructs threads with names<code>&lt;prefix&gt;-&lt;pool number&gt;-thread-&lt;thread number&gt;</code>.      * @param prefix prefix to be used for thread names created by this {@link ThreadFactory}      * @return      */
specifier|public
specifier|static
name|ThreadFactory
name|namedThreadFactory
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
return|return
operator|new
name|NamedThreadFactory
argument_list|(
name|prefix
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|NamedThreadFactory
implements|implements
name|ThreadFactory
block|{
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|poolNumber
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ThreadGroup
name|group
decl_stmt|;
specifier|private
specifier|final
name|AtomicInteger
name|threadNumber
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
name|namePrefix
decl_stmt|;
specifier|public
name|NamedThreadFactory
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|SecurityManager
name|s
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
name|group
operator|=
operator|(
name|s
operator|!=
literal|null
operator|)
condition|?
name|s
operator|.
name|getThreadGroup
argument_list|()
else|:
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getThreadGroup
argument_list|()
expr_stmt|;
name|namePrefix
operator|=
name|prefix
operator|+
literal|"-"
operator|+
name|poolNumber
operator|.
name|getAndIncrement
argument_list|()
operator|+
literal|"-thread-"
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
name|Thread
name|t
init|=
operator|new
name|Thread
argument_list|(
name|group
argument_list|,
name|r
argument_list|,
name|namePrefix
operator|+
name|threadNumber
operator|.
name|getAndIncrement
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|isDaemon
argument_list|()
condition|)
name|t
operator|.
name|setDaemon
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|t
operator|.
name|getPriority
argument_list|()
operator|!=
name|Thread
operator|.
name|NORM_PRIORITY
condition|)
name|t
operator|.
name|setPriority
argument_list|(
name|Thread
operator|.
name|NORM_PRIORITY
argument_list|)
expr_stmt|;
return|return
name|t
return|;
block|}
block|}
block|}
end_class

end_unit
