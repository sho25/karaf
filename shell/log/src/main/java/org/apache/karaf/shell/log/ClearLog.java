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
name|shell
operator|.
name|log
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
name|console
operator|.
name|OsgiCommandSupport
import|;
end_import

begin_comment
comment|/**  * Clear the last log entries.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"log"
argument_list|,
name|name
operator|=
literal|"clear"
argument_list|,
name|description
operator|=
literal|"Clear log entries."
argument_list|)
specifier|public
class|class
name|ClearLog
extends|extends
name|OsgiCommandSupport
block|{
specifier|protected
name|LruList
name|events
decl_stmt|;
specifier|public
name|LruList
name|getEvents
parameter_list|()
block|{
return|return
name|events
return|;
block|}
specifier|public
name|void
name|setEvents
parameter_list|(
name|LruList
name|events
parameter_list|)
block|{
name|this
operator|.
name|events
operator|=
name|events
expr_stmt|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|events
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

