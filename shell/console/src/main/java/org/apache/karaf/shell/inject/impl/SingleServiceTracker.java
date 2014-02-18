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
name|shell
operator|.
name|inject
operator|.
name|impl
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
name|atomic
operator|.
name|AtomicBoolean
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
name|AtomicReference
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
name|Constants
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
name|Filter
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
name|InvalidSyntaxException
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
name|ServiceEvent
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
name|ServiceListener
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

begin_comment
comment|/**  * Track a single service by its type.  *  * @param<T>  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SingleServiceTracker
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
specifier|final
name|BundleContext
name|ctx
decl_stmt|;
specifier|private
specifier|final
name|String
name|className
decl_stmt|;
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|T
argument_list|>
name|service
init|=
operator|new
name|AtomicReference
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|ServiceReference
argument_list|>
name|ref
init|=
operator|new
name|AtomicReference
argument_list|<
name|ServiceReference
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|AtomicBoolean
name|open
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Satisfiable
name|serviceListener
decl_stmt|;
specifier|private
name|Filter
name|filter
decl_stmt|;
specifier|private
specifier|final
name|ServiceListener
name|listener
init|=
operator|new
name|ServiceListener
argument_list|()
block|{
specifier|public
name|void
name|serviceChanged
parameter_list|(
name|ServiceEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|open
operator|.
name|get
argument_list|()
condition|)
block|{
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|ServiceEvent
operator|.
name|UNREGISTERING
condition|)
block|{
name|ServiceReference
name|deadRef
init|=
name|event
operator|.
name|getServiceReference
argument_list|()
decl_stmt|;
if|if
condition|(
name|deadRef
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
name|findMatchingReference
argument_list|(
name|deadRef
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|ServiceEvent
operator|.
name|REGISTERED
operator|&&
name|ref
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|findMatchingReference
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
decl_stmt|;
specifier|public
name|SingleServiceTracker
parameter_list|(
name|BundleContext
name|context
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Satisfiable
name|sl
parameter_list|)
block|{
name|ctx
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|className
operator|=
name|clazz
operator|.
name|getName
argument_list|()
expr_stmt|;
name|serviceListener
operator|=
name|sl
expr_stmt|;
block|}
specifier|public
name|T
name|getService
parameter_list|()
block|{
return|return
name|service
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
name|ServiceReference
name|getServiceReference
parameter_list|()
block|{
return|return
name|ref
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
name|void
name|open
parameter_list|()
block|{
if|if
condition|(
name|open
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
try|try
block|{
name|String
name|filterString
init|=
literal|'('
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|'='
operator|+
name|className
operator|+
literal|')'
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
name|filterString
operator|=
literal|"(&"
operator|+
name|filterString
operator|+
name|filter
operator|+
literal|')'
expr_stmt|;
name|ctx
operator|.
name|addServiceListener
argument_list|(
name|listener
argument_list|,
name|filterString
argument_list|)
expr_stmt|;
name|findMatchingReference
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
comment|// this can never happen. (famous last words :)
block|}
block|}
block|}
specifier|private
name|void
name|findMatchingReference
parameter_list|(
name|ServiceReference
name|original
parameter_list|)
block|{
name|boolean
name|clear
init|=
literal|true
decl_stmt|;
name|ServiceReference
name|ref
init|=
name|ctx
operator|.
name|getServiceReference
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
operator|&&
operator|(
name|filter
operator|==
literal|null
operator|||
name|filter
operator|.
name|match
argument_list|(
name|ref
argument_list|)
operator|)
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
name|service
init|=
operator|(
name|T
operator|)
name|ctx
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|!=
literal|null
condition|)
block|{
name|clear
operator|=
literal|false
expr_stmt|;
comment|// We do the unget out of the lock so we don't exit this class while holding a lock.
if|if
condition|(
operator|!
operator|!
operator|!
name|update
argument_list|(
name|original
argument_list|,
name|ref
argument_list|,
name|service
argument_list|)
condition|)
block|{
name|ctx
operator|.
name|ungetService
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|original
operator|==
literal|null
condition|)
block|{
name|clear
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|clear
condition|)
block|{
name|update
argument_list|(
name|original
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|update
parameter_list|(
name|ServiceReference
name|deadRef
parameter_list|,
name|ServiceReference
name|newRef
parameter_list|,
name|T
name|service
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
name|int
name|foundLostReplaced
init|=
operator|-
literal|1
decl_stmt|;
comment|// Make sure we don't try to get a lock on null
name|Object
name|lock
decl_stmt|;
comment|// we have to choose our lock.
if|if
condition|(
name|newRef
operator|!=
literal|null
condition|)
name|lock
operator|=
name|newRef
expr_stmt|;
elseif|else
if|if
condition|(
name|deadRef
operator|!=
literal|null
condition|)
name|lock
operator|=
name|deadRef
expr_stmt|;
else|else
name|lock
operator|=
name|this
expr_stmt|;
comment|// This lock is here to ensure that no two threads can set the ref and service
comment|// at the same time.
synchronized|synchronized
init|(
name|lock
init|)
block|{
if|if
condition|(
name|open
operator|.
name|get
argument_list|()
condition|)
block|{
name|result
operator|=
name|this
operator|.
name|ref
operator|.
name|compareAndSet
argument_list|(
name|deadRef
argument_list|,
name|newRef
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
condition|)
block|{
name|this
operator|.
name|service
operator|.
name|set
argument_list|(
name|service
argument_list|)
expr_stmt|;
if|if
condition|(
name|deadRef
operator|==
literal|null
operator|&&
name|newRef
operator|!=
literal|null
condition|)
name|foundLostReplaced
operator|=
literal|0
expr_stmt|;
if|if
condition|(
name|deadRef
operator|!=
literal|null
operator|&&
name|newRef
operator|==
literal|null
condition|)
name|foundLostReplaced
operator|=
literal|1
expr_stmt|;
if|if
condition|(
name|deadRef
operator|!=
literal|null
operator|&&
name|newRef
operator|!=
literal|null
condition|)
name|foundLostReplaced
operator|=
literal|2
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|serviceListener
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|foundLostReplaced
operator|==
literal|0
condition|)
name|serviceListener
operator|.
name|found
argument_list|()
expr_stmt|;
elseif|else
if|if
condition|(
name|foundLostReplaced
operator|==
literal|1
condition|)
name|serviceListener
operator|.
name|lost
argument_list|()
expr_stmt|;
elseif|else
if|if
condition|(
name|foundLostReplaced
operator|==
literal|2
condition|)
name|serviceListener
operator|.
name|updated
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|open
operator|.
name|compareAndSet
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|ctx
operator|.
name|removeServiceListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|ServiceReference
name|deadRef
init|=
name|ref
operator|.
name|getAndSet
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|service
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|deadRef
operator|!=
literal|null
condition|)
name|ctx
operator|.
name|ungetService
argument_list|(
name|deadRef
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

