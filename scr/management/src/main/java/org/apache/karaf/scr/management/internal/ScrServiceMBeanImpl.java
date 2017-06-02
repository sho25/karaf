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
name|management
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

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|*
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|felix
operator|.
name|scr
operator|.
name|Component
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
name|scr
operator|.
name|ScrService
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
name|management
operator|.
name|ScrServiceMBean
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
name|management
operator|.
name|codec
operator|.
name|JmxComponent
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

begin_class
annotation|@
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Component
argument_list|(
name|name
operator|=
name|ScrServiceMBeanImpl
operator|.
name|COMPONENT_NAME
argument_list|,
name|enabled
operator|=
literal|true
argument_list|,
name|immediate
operator|=
literal|true
argument_list|,
name|properties
operator|=
block|{
literal|"hidden.component=true"
block|}
argument_list|)
specifier|public
class|class
name|ScrServiceMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|ScrServiceMBean
block|{
specifier|public
specifier|static
specifier|final
name|String
name|OBJECT_NAME
init|=
literal|"org.apache.karaf:type=scr,name="
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|,
literal|"root"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMPONENT_NAME
init|=
literal|"ScrServiceMBean"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMPONENT_LABEL
init|=
literal|"Apache Karaf SCR Service MBean"
decl_stmt|;
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
name|ScrServiceMBeanImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|MBeanServer
name|mBeanServer
decl_stmt|;
specifier|private
name|ScrService
name|scrService
decl_stmt|;
specifier|private
name|ReadWriteLock
name|lock
init|=
operator|new
name|ReentrantReadWriteLock
argument_list|()
decl_stmt|;
comment|/**      * Creates new Declarative Services MBean.      *      * @throws NotCompliantMBeanException If the MBean is not a valid MBean.      */
specifier|public
name|ScrServiceMBeanImpl
parameter_list|()
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|ScrServiceMBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Service component activation call back.  Called when all dependencies are satisfied.      *      * @throws Exception If the activation fails.      */
annotation|@
name|Activate
specifier|public
name|void
name|activate
parameter_list|()
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Activating the "
operator|+
name|COMPONENT_LABEL
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|mbeans
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|mbeans
operator|.
name|put
argument_list|(
name|this
argument_list|,
literal|"org.apache.karaf:type=scr,name=${karaf.name}"
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|mBeanServer
operator|!=
literal|null
condition|)
block|{
name|mBeanServer
operator|.
name|registerMBean
argument_list|(
name|this
argument_list|,
operator|new
name|ObjectName
argument_list|(
name|OBJECT_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"Exception registering the SCR Management MBean: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|e
argument_list|)
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
comment|/**      * Service component deactivation call back.  Called after the component is in an active      * state when any dependencies become unsatisfied.      *      * @throws Exception If the deactivation fails.      */
annotation|@
name|Deactivate
specifier|public
name|void
name|deactivate
parameter_list|()
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Deactivating the "
operator|+
name|COMPONENT_LABEL
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|mBeanServer
operator|!=
literal|null
condition|)
block|{
name|mBeanServer
operator|.
name|unregisterMBean
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|OBJECT_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|Override
specifier|public
name|TabularData
name|getComponents
parameter_list|()
block|{
try|try
block|{
return|return
name|JmxComponent
operator|.
name|tableFrom
argument_list|(
name|safe
argument_list|(
name|scrService
operator|.
name|getComponents
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|String
index|[]
name|listComponents
parameter_list|()
block|{
name|Component
index|[]
name|components
init|=
name|safe
argument_list|(
name|scrService
operator|.
name|getComponents
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|componentNames
init|=
operator|new
name|String
index|[
name|components
operator|.
name|length
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
name|componentNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|componentNames
index|[
name|i
index|]
operator|=
name|components
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|componentNames
return|;
block|}
specifier|public
name|boolean
name|isComponentActive
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
return|return
name|componentState
argument_list|(
name|componentName
argument_list|)
operator|==
name|Component
operator|.
name|STATE_ACTIVE
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|int
name|componentState
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
name|int
name|state
init|=
operator|-
literal|1
decl_stmt|;
specifier|final
name|Component
name|component
init|=
name|findComponent
argument_list|(
name|componentName
argument_list|)
decl_stmt|;
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
name|state
operator|=
name|component
operator|.
name|getState
argument_list|()
expr_stmt|;
else|else
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"No component found for name: "
operator|+
name|componentName
argument_list|)
expr_stmt|;
return|return
name|state
return|;
block|}
specifier|public
name|void
name|activateComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
specifier|final
name|Component
name|component
init|=
name|findComponent
argument_list|(
name|componentName
argument_list|)
decl_stmt|;
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
name|component
operator|.
name|enable
argument_list|()
expr_stmt|;
else|else
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"No component found for name: "
operator|+
name|componentName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deactivateComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
specifier|final
name|Component
name|component
init|=
name|findComponent
argument_list|(
name|componentName
argument_list|)
decl_stmt|;
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
name|component
operator|.
name|disable
argument_list|()
expr_stmt|;
else|else
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"No component found for name: "
operator|+
name|componentName
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Component
name|findComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
name|Component
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|scrService
operator|.
name|getComponents
argument_list|(
name|componentName
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Component
index|[]
name|components
init|=
name|scrService
operator|.
name|getComponents
argument_list|(
name|componentName
argument_list|)
decl_stmt|;
for|for
control|(
name|Component
name|component
range|:
name|safe
argument_list|(
name|components
argument_list|)
control|)
block|{
name|answer
operator|=
name|component
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
specifier|private
name|Component
index|[]
name|safe
parameter_list|(
name|Component
index|[]
name|components
parameter_list|)
block|{
return|return
name|components
operator|==
literal|null
condition|?
operator|new
name|Component
index|[
literal|0
index|]
else|:
name|components
return|;
block|}
annotation|@
name|Reference
specifier|public
name|void
name|setmBeanServer
parameter_list|(
name|MBeanServer
name|mBeanServer
parameter_list|)
block|{
name|this
operator|.
name|mBeanServer
operator|=
name|mBeanServer
expr_stmt|;
block|}
specifier|public
name|void
name|unsetmBeanServer
parameter_list|(
name|MBeanServer
name|mBeanServer
parameter_list|)
block|{
name|this
operator|.
name|mBeanServer
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Reference
specifier|public
name|void
name|setScrService
parameter_list|(
name|ScrService
name|scrService
parameter_list|)
block|{
name|this
operator|.
name|scrService
operator|=
name|scrService
expr_stmt|;
block|}
specifier|public
name|void
name|unsetScrService
parameter_list|(
name|ScrService
name|scrService
parameter_list|)
block|{
name|this
operator|.
name|scrService
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

