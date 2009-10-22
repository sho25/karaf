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
name|admin
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_class
specifier|public
class|class
name|MBeanRegistrer
block|{
specifier|private
name|MBeanServer
name|mbeanServer
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|mbeans
decl_stmt|;
specifier|public
name|void
name|setMbeans
parameter_list|(
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|mbeans
parameter_list|)
block|{
name|this
operator|.
name|mbeans
operator|=
name|mbeans
expr_stmt|;
block|}
specifier|public
name|void
name|registerMBeanServer
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|)
throws|throws
name|JMException
block|{
if|if
condition|(
name|this
operator|.
name|mbeanServer
operator|!=
name|mbeanServer
condition|)
block|{
name|unregisterMBeans
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|mbeanServer
operator|=
name|mbeanServer
expr_stmt|;
name|registerMBeans
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|unregisterMBeanServer
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|)
throws|throws
name|JMException
block|{
name|unregisterMBeans
argument_list|()
expr_stmt|;
name|this
operator|.
name|mbeanServer
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|registerMBeans
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|registerMBeans
parameter_list|()
throws|throws
name|JMException
block|{
if|if
condition|(
name|mbeanServer
operator|!=
literal|null
operator|&&
name|mbeans
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|mbeans
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|mbeanServer
operator|.
name|registerMBean
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
operator|new
name|ObjectName
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|unregisterMBeans
parameter_list|()
throws|throws
name|JMException
block|{
if|if
condition|(
name|mbeanServer
operator|!=
literal|null
operator|&&
name|mbeans
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|mbeans
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|mbeanServer
operator|.
name|unregisterMBean
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

