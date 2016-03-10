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
name|config
operator|.
name|command
package|;
end_package

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
name|Enumeration
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
name|action
operator|.
name|Command
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
name|action
operator|.
name|lifecycle
operator|.
name|Service
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"config"
argument_list|,
name|name
operator|=
literal|"property-list"
argument_list|,
name|description
operator|=
literal|"Lists properties from the currently edited configuration."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|PropListCommand
extends|extends
name|ConfigPropertyCommandSupport
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|public
name|void
name|propertyAction
parameter_list|(
name|Dictionary
name|props
parameter_list|)
block|{
for|for
control|(
name|Enumeration
name|e
init|=
name|props
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|Object
name|key
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|key
operator|+
literal|" = "
operator|+
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Check if a configuration (identified by PID) requires an update or not.      *      * @param pid the configuration PID.      * @return true if the configuration requires an update, false else (always returns false).      */
annotation|@
name|Override
specifier|protected
name|boolean
name|requiresUpdate
parameter_list|(
name|String
name|pid
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

