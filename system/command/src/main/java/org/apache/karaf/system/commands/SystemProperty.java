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
name|system
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
name|karaf
operator|.
name|shell
operator|.
name|commands
operator|.
name|Argument
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
name|commands
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
name|commands
operator|.
name|Option
import|;
end_import

begin_comment
comment|/**  * Command that allow access to system properties easily.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"system"
argument_list|,
name|name
operator|=
literal|"property"
argument_list|,
name|description
operator|=
literal|"Get or set a system property."
argument_list|)
specifier|public
class|class
name|SystemProperty
extends|extends
name|AbstractSystemAction
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
block|{
literal|"--persistent"
block|}
argument_list|,
name|description
operator|=
literal|"Persist the new value to the etc/system.properties file"
argument_list|)
name|boolean
name|persistent
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"key"
argument_list|,
name|description
operator|=
literal|"The system property name"
argument_list|)
name|String
name|key
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"value"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|description
operator|=
literal|"New value for the system property"
argument_list|)
name|String
name|value
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|systemService
operator|.
name|setSystemProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|persistent
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

