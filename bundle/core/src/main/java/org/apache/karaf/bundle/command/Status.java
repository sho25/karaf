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
name|bundle
operator|.
name|command
package|;
end_package

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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"bundle"
argument_list|,
name|name
operator|=
literal|"status"
argument_list|,
name|description
operator|=
literal|"Get the bundle current status"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|Status
extends|extends
name|BundleCommand
block|{
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|getState
argument_list|(
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|/**      * Return a String representation of a bundle state      */
specifier|private
name|String
name|getState
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
switch|switch
condition|(
name|bundle
operator|.
name|getState
argument_list|()
condition|)
block|{
case|case
name|Bundle
operator|.
name|UNINSTALLED
case|:
return|return
literal|"Uninstalled"
return|;
case|case
name|Bundle
operator|.
name|INSTALLED
case|:
return|return
literal|"Installed"
return|;
case|case
name|Bundle
operator|.
name|RESOLVED
case|:
return|return
literal|"Resolved"
return|;
case|case
name|Bundle
operator|.
name|STARTING
case|:
return|return
literal|"Starting"
return|;
case|case
name|Bundle
operator|.
name|STOPPING
case|:
return|return
literal|"Stopping"
return|;
case|case
name|Bundle
operator|.
name|ACTIVE
case|:
return|return
literal|"Active"
return|;
default|default:
return|return
literal|"Unknown"
return|;
block|}
block|}
block|}
end_class

end_unit

