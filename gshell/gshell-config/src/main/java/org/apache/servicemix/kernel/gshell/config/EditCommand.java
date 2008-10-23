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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|config
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
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|clp
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
name|geronimo
operator|.
name|gshell
operator|.
name|clp
operator|.
name|Option
import|;
end_import

begin_class
specifier|public
class|class
name|EditCommand
extends|extends
name|ConfigCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"PID of the configuration"
argument_list|)
name|String
name|pid
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--force"
argument_list|,
name|description
operator|=
literal|"Force the edition of this config, even if another one was under edition"
argument_list|)
name|boolean
name|force
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|ConfigurationAdmin
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|oldPid
init|=
operator|(
name|String
operator|)
name|this
operator|.
name|variables
operator|.
name|get
argument_list|(
name|PROPERTY_CONFIG_PID
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldPid
operator|!=
literal|null
operator|&&
operator|!
name|oldPid
operator|.
name|equals
argument_list|(
name|pid
argument_list|)
operator|&&
operator|!
name|force
condition|)
block|{
name|io
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Another config is being edited.  Cancel / update first, or use the --force option"
argument_list|)
expr_stmt|;
return|return;
block|}
name|Dictionary
name|props
init|=
name|admin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|)
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|this
operator|.
name|variables
operator|.
name|parent
argument_list|()
operator|.
name|set
argument_list|(
name|PROPERTY_CONFIG_PID
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|this
operator|.
name|variables
operator|.
name|parent
argument_list|()
operator|.
name|set
argument_list|(
name|PROPERTY_CONFIG_PROPS
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

