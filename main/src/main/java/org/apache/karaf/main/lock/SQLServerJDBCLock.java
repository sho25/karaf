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
name|java
operator|.
name|util
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
name|SQLServerJDBCLock
extends|extends
name|DefaultJDBCLock
block|{
specifier|public
name|SQLServerJDBCLock
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|super
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
name|Statements
name|createStatements
parameter_list|()
block|{
name|Statements
name|statements
init|=
operator|new
name|MsSQLServerStatements
argument_list|()
decl_stmt|;
return|return
name|statements
return|;
block|}
specifier|private
class|class
name|MsSQLServerStatements
extends|extends
name|Statements
block|{
annotation|@
name|Override
specifier|public
name|String
name|getLockCreateStatement
parameter_list|()
block|{
return|return
literal|"SELECT * FROM "
operator|+
name|getFullLockTableName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

