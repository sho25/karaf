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
name|features
operator|.
name|internal
operator|.
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ListenerNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanNotificationInfo
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
name|Notification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationBroadcasterSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationEmitter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationListener
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

begin_class
specifier|public
class|class
name|StandardEmitterMBean
extends|extends
name|StandardMBean
implements|implements
name|NotificationEmitter
block|{
specifier|private
specifier|final
name|NotificationBroadcasterSupport
name|emitter
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|StandardEmitterMBean
parameter_list|(
name|Class
name|mbeanInterface
parameter_list|)
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|mbeanInterface
argument_list|)
expr_stmt|;
name|this
operator|.
name|emitter
operator|=
operator|new
name|NotificationBroadcasterSupport
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|MBeanNotificationInfo
index|[]
name|getNotificationInfo
parameter_list|()
block|{
return|return
name|StandardEmitterMBean
operator|.
name|this
operator|.
name|getNotificationInfo
argument_list|()
return|;
block|}
block|}
expr_stmt|;
block|}
specifier|public
name|void
name|sendNotification
parameter_list|(
name|Notification
name|notification
parameter_list|)
block|{
name|emitter
operator|.
name|sendNotification
argument_list|(
name|notification
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeNotificationListener
parameter_list|(
name|NotificationListener
name|listener
parameter_list|,
name|NotificationFilter
name|filter
parameter_list|,
name|Object
name|handback
parameter_list|)
throws|throws
name|ListenerNotFoundException
block|{
name|emitter
operator|.
name|removeNotificationListener
argument_list|(
name|listener
argument_list|,
name|filter
argument_list|,
name|handback
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addNotificationListener
parameter_list|(
name|NotificationListener
name|listener
parameter_list|,
name|NotificationFilter
name|filter
parameter_list|,
name|Object
name|handback
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|emitter
operator|.
name|addNotificationListener
argument_list|(
name|listener
argument_list|,
name|filter
argument_list|,
name|handback
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeNotificationListener
parameter_list|(
name|NotificationListener
name|listener
parameter_list|)
throws|throws
name|ListenerNotFoundException
block|{
name|emitter
operator|.
name|removeNotificationListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MBeanNotificationInfo
index|[]
name|getNotificationInfo
parameter_list|()
block|{
return|return
operator|new
name|MBeanNotificationInfo
index|[
literal|0
index|]
return|;
block|}
annotation|@
name|Override
specifier|public
name|MBeanInfo
name|getMBeanInfo
parameter_list|()
block|{
name|MBeanInfo
name|mbeanInfo
init|=
name|super
operator|.
name|getMBeanInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|mbeanInfo
operator|!=
literal|null
condition|)
block|{
name|MBeanNotificationInfo
index|[]
name|notificationInfo
init|=
name|getNotificationInfo
argument_list|()
decl_stmt|;
name|mbeanInfo
operator|=
operator|new
name|MBeanInfo
argument_list|(
name|mbeanInfo
operator|.
name|getClassName
argument_list|()
argument_list|,
name|mbeanInfo
operator|.
name|getDescription
argument_list|()
argument_list|,
name|mbeanInfo
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|mbeanInfo
operator|.
name|getConstructors
argument_list|()
argument_list|,
name|mbeanInfo
operator|.
name|getOperations
argument_list|()
argument_list|,
name|notificationInfo
argument_list|)
expr_stmt|;
block|}
return|return
name|mbeanInfo
return|;
block|}
block|}
end_class

end_unit

