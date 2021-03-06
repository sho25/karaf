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
name|profile
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|profile
operator|.
name|Profile
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
name|profile
operator|.
name|ProfileService
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
name|Action
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
name|Option
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
name|Reference
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
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|table
operator|.
name|ShellTable
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|profile
operator|.
name|impl
operator|.
name|Utils
operator|.
name|join
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|name
operator|=
literal|"list"
argument_list|,
name|scope
operator|=
literal|"profile"
argument_list|,
name|description
operator|=
literal|"Lists all profiles"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|ProfileList
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--hidden"
argument_list|,
name|description
operator|=
literal|"Display hidden profiles"
argument_list|)
specifier|private
name|boolean
name|hidden
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|ProfileService
name|profileService
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ids
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|profileService
operator|.
name|getProfiles
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|ids
argument_list|)
expr_stmt|;
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"parents"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|id
range|:
name|ids
control|)
block|{
name|Profile
name|profile
init|=
name|profileService
operator|.
name|getProfile
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|profile
operator|!=
literal|null
operator|&&
operator|(
name|hidden
operator|||
operator|!
name|profile
operator|.
name|isHidden
argument_list|()
operator|)
condition|)
block|{
name|String
name|parents
init|=
name|join
argument_list|(
literal|" "
argument_list|,
name|profile
operator|.
name|getParentIds
argument_list|()
argument_list|)
decl_stmt|;
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|id
argument_list|,
name|parents
argument_list|)
expr_stmt|;
block|}
block|}
name|table
operator|.
name|print
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
end_class

end_unit

