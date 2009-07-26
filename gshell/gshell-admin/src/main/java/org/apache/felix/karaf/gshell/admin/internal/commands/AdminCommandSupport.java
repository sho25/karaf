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
name|gshell
operator|.
name|admin
operator|.
name|internal
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|admin
operator|.
name|AdminService
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
name|karaf
operator|.
name|gshell
operator|.
name|admin
operator|.
name|Instance
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
name|karaf
operator|.
name|gshell
operator|.
name|console
operator|.
name|OsgiCommandSupport
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
name|gogo
operator|.
name|commands
operator|.
name|Command
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AdminCommandSupport
extends|extends
name|OsgiCommandSupport
block|{
specifier|private
name|AdminService
name|adminService
decl_stmt|;
specifier|public
name|AdminService
name|getAdminService
parameter_list|()
block|{
return|return
name|adminService
return|;
block|}
specifier|public
name|void
name|setAdminService
parameter_list|(
name|AdminService
name|adminService
parameter_list|)
block|{
name|this
operator|.
name|adminService
operator|=
name|adminService
expr_stmt|;
block|}
specifier|protected
name|Instance
name|getExistingInstance
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Instance
name|i
init|=
name|adminService
operator|.
name|getInstance
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instance '"
operator|+
name|name
operator|+
literal|"' does not exist"
argument_list|)
throw|;
block|}
return|return
name|i
return|;
block|}
block|}
end_class

end_unit

