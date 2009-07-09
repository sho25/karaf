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
name|features
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
name|features
operator|.
name|FeaturesService
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
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|FeaturesCommandSupport
extends|extends
name|OsgiCommandSupport
block|{
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Get repository admin service.
name|ServiceReference
name|ref
init|=
name|getBundleContext
argument_list|()
operator|.
name|getServiceReference
argument_list|(
name|FeaturesService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"FeaturesService service is unavailable."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
try|try
block|{
name|FeaturesService
name|admin
init|=
operator|(
name|FeaturesService
operator|)
name|getBundleContext
argument_list|()
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|admin
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"FeaturesService service is unavailable."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|doExecute
argument_list|(
name|admin
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|getBundleContext
argument_list|()
operator|.
name|ungetService
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
specifier|abstract
name|void
name|doExecute
parameter_list|(
name|FeaturesService
name|admin
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

