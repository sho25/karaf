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
name|scr
operator|.
name|examples
operator|.
name|component
operator|.
name|factories
operator|.
name|component
package|;
end_package

begin_import
import|import
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Activate
import|;
end_import

begin_import
import|import
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Deactivate
import|;
end_import

begin_import
import|import
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Reference
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
name|scr
operator|.
name|examples
operator|.
name|component
operator|.
name|factories
operator|.
name|GreeterServiceComponentFactory
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
name|component
operator|.
name|ComponentFactory
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
name|component
operator|.
name|ComponentInstance
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
name|Properties
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
name|locks
operator|.
name|ReadWriteLock
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
name|locks
operator|.
name|ReentrantReadWriteLock
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
name|name
operator|=
name|GreeterServiceFactoryManager
operator|.
name|COMPONENT_NAME
argument_list|)
specifier|public
class|class
name|GreeterServiceFactoryManager
block|{
specifier|public
specifier|static
specifier|final
name|String
name|COMPONENT_NAME
init|=
literal|"GreeterServiceFactoryManager"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMPONENT_LABEL
init|=
literal|"Greeter Service Factory Manager"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GreeterServiceFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ComponentFactory
name|factory
decl_stmt|;
specifier|private
name|ComponentInstance
name|instance
decl_stmt|;
specifier|private
name|GreeterServiceComponentFactory
name|greeterService
decl_stmt|;
specifier|private
name|ReadWriteLock
name|lock
init|=
operator|new
name|ReentrantReadWriteLock
argument_list|()
decl_stmt|;
comment|/**      * Called when all of the SCR Components required dependencies have been satisfied.      */
annotation|@
name|Activate
specifier|public
name|void
name|activate
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Activating the {}"
argument_list|,
name|COMPONENT_LABEL
argument_list|)
expr_stmt|;
try|try
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"salutation"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"name"
argument_list|,
literal|"User"
argument_list|)
expr_stmt|;
name|instance
operator|=
name|factory
operator|.
name|newInstance
argument_list|(
operator|(
name|Dictionary
operator|)
name|props
argument_list|)
expr_stmt|;
name|greeterService
operator|=
operator|(
name|GreeterServiceComponentFactory
operator|)
name|instance
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|greeterService
operator|.
name|startGreeter
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Callend when any of the SCR Components required dependencies become unsatisfied.      */
annotation|@
name|Deactivate
specifier|public
name|void
name|deactivate
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Deactivating the {}"
argument_list|,
name|COMPONENT_LABEL
argument_list|)
expr_stmt|;
try|try
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
name|greeterService
operator|.
name|stopGreeter
argument_list|()
expr_stmt|;
name|instance
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Reference
argument_list|(
name|target
operator|=
literal|"(component.factory=greeter.factory.provider)"
argument_list|)
specifier|public
name|void
name|setFactory
parameter_list|(
specifier|final
name|ComponentFactory
name|factory
parameter_list|)
block|{
try|try
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|unsetFactory
parameter_list|()
block|{
try|try
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
name|this
operator|.
name|factory
operator|=
literal|null
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

