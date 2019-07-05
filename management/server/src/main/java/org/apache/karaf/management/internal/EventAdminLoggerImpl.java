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
name|management
operator|.
name|internal
package|;
end_package

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
name|service
operator|.
name|event
operator|.
name|Event
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
name|event
operator|.
name|EventAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
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
name|Locale
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

begin_class
specifier|public
class|class
name|EventAdminLoggerImpl
implements|implements
name|EventAdminLogger
block|{
specifier|private
specifier|final
name|ServiceTracker
argument_list|<
name|EventAdmin
argument_list|,
name|EventAdmin
argument_list|>
name|tracker
decl_stmt|;
specifier|public
name|EventAdminLoggerImpl
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|<>
argument_list|(
name|context
argument_list|,
name|EventAdmin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
name|this
operator|.
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|log
parameter_list|(
name|String
name|methodName
parameter_list|,
name|String
index|[]
name|signature
parameter_list|,
name|Object
name|result
parameter_list|,
name|Throwable
name|error
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
block|{
name|EventAdmin
name|admin
init|=
name|tracker
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|admin
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"method"
argument_list|,
name|methodName
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"signature"
argument_list|,
name|signature
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"params"
argument_list|,
name|params
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"error"
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
name|Event
name|event
init|=
operator|new
name|Event
argument_list|(
literal|"javax/management/MBeanServer/"
operator|+
name|methodName
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|admin
operator|.
name|postEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

