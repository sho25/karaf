begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|main
operator|.
name|lock
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
name|utils
operator|.
name|properties
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * Represents an exclusive lock on a database,  * used to avoid multiple Karaf instances attempting  * to become master.  */
end_comment

begin_class
specifier|public
class|class
name|PostgreSQLJDBCLock
extends|extends
name|DefaultJDBCLock
block|{
specifier|public
name|PostgreSQLJDBCLock
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
name|super
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
name|boolean
name|acquireLock
parameter_list|()
block|{
name|this
operator|.
name|timeout
operator|=
literal|0
expr_stmt|;
return|return
name|super
operator|.
name|acquireLock
argument_list|()
return|;
block|}
block|}
end_class

end_unit

