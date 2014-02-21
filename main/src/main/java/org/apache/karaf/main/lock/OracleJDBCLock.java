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
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|util
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
name|OracleJDBCLock
extends|extends
name|DefaultJDBCLock
block|{
specifier|private
specifier|static
specifier|final
name|String
name|MOMENT_COLUMN_DATA_TYPE
init|=
literal|"NUMBER(20)"
decl_stmt|;
specifier|public
name|OracleJDBCLock
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
name|Statements
name|createStatements
parameter_list|()
block|{
name|Statements
name|statements
init|=
operator|new
name|Statements
argument_list|()
decl_stmt|;
name|statements
operator|.
name|setTableName
argument_list|(
name|table
argument_list|)
expr_stmt|;
name|statements
operator|.
name|setNodeName
argument_list|(
name|clusterName
argument_list|)
expr_stmt|;
name|statements
operator|.
name|setMomentColumnDataType
argument_list|(
name|MOMENT_COLUMN_DATA_TYPE
argument_list|)
expr_stmt|;
return|return
name|statements
return|;
block|}
comment|/**      * When we perform an update on a long lived locked table, Oracle will save      * a copy of the transaction in it's UNDO table space. Eventually this can      * cause the UNDO table to become full, disrupting all locks in the DB instance.      * A select query just touches the table, ensuring we can still read the DB but      * doesn't add to the UNDO.       */
annotation|@
name|Override
specifier|public
name|boolean
name|lock
parameter_list|()
block|{
return|return
name|aquireLock
argument_list|()
return|;
block|}
comment|/**      * When we perform an update on a long lived locked table, Oracle will save      * a copy of the transaction in it's UNDO table space. Eventually this can      * cause the UNDO table to become full, disrupting all locks in the DB instance.      * A select query just touches the table, ensuring we can still read the DB but      * doesn't add to the UNDO.       */
annotation|@
name|Override
name|boolean
name|updateLock
parameter_list|()
block|{
return|return
name|aquireLock
argument_list|()
return|;
block|}
comment|/**      * A SELECT FOR UPDATE does not create a database lock when the SELECT FOR UPDATE is performed      * on an empty selection. So a succesfull call to {@link DefaultJDBCLock#aquireLock()} is not sufficient to       * ensure that we are the only one who have acquired the lock.      */
annotation|@
name|Override
name|boolean
name|aquireLock
parameter_list|()
block|{
return|return
name|super
operator|.
name|aquireLock
argument_list|()
operator|&&
name|lockAcquiredOnNonEmptySelection
argument_list|()
return|;
block|}
comment|//Verify that we have a non empty record set.
specifier|private
name|boolean
name|lockAcquiredOnNonEmptySelection
parameter_list|()
block|{
name|String
name|verifySelectionNotEmpytStatement
init|=
name|statements
operator|.
name|getLockVerifySelectionNotEmptyStatement
argument_list|()
decl_stmt|;
name|PreparedStatement
name|preparedStatement
init|=
literal|null
decl_stmt|;
name|boolean
name|lockAquired
init|=
literal|false
decl_stmt|;
try|try
block|{
name|preparedStatement
operator|=
name|getConnection
argument_list|()
operator|.
name|prepareStatement
argument_list|(
name|verifySelectionNotEmpytStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|ResultSet
name|rs
init|=
name|preparedStatement
operator|.
name|executeQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|lockAquired
operator|=
name|rs
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
operator|>
literal|0
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Failed to acquire database lock. Missing database lock record."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Failed to acquire database lock: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|closeSafely
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
block|}
return|return
name|lockAquired
return|;
block|}
block|}
end_class

end_unit

