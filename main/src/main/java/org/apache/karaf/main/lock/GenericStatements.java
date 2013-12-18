begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_comment
comment|/**  * This class is used to create the sql statements for the karaf lock tables that are used  * for clustering of karaf instances.  *   * It will generate sql statement to create two separate tables, a lock table and a lock id table  *   *   CREATE TABLE LOCK ( ID INTEGER DEFAULT 0, STATE INTEGER DEFAULT 0, LOCK_DELAY INTEGER DEFAULT 0 )  *     *   CREATE TABLE LOCK_ID ( ID INTEGER DEFAULT 0 )  *     * @author Claudio Corsi  *   */
end_comment

begin_class
specifier|public
class|class
name|GenericStatements
block|{
specifier|private
specifier|final
name|String
name|lockTableName
decl_stmt|;
specifier|private
specifier|final
name|String
name|lockIdTableName
decl_stmt|;
specifier|private
specifier|final
name|String
name|clusterName
decl_stmt|;
comment|/** 	 * This constructor is used to determine the name of the karaf lock table, the karaf lock id 	 * table and the name of the clustered instances. 	 * 	 * @param lockTableName The name of the karaf lock table 	 * @param lockIdTableName The name of the karaf lock id table 	 * @param clusterName the name of the cluster being used 	 */
specifier|public
name|GenericStatements
parameter_list|(
name|String
name|lockTableName
parameter_list|,
name|String
name|lockIdTableName
parameter_list|,
name|String
name|clusterName
parameter_list|)
block|{
name|this
operator|.
name|lockTableName
operator|=
name|lockTableName
expr_stmt|;
name|this
operator|.
name|lockIdTableName
operator|=
name|lockIdTableName
expr_stmt|;
name|this
operator|.
name|clusterName
operator|=
name|clusterName
expr_stmt|;
block|}
comment|/** 	 * This method will return the name of the cluster that the instances are using to compete for the 	 * master lock. 	 * 	 * @return cluster node name 	 */
specifier|public
specifier|final
name|String
name|getNodeName
parameter_list|()
block|{
return|return
name|this
operator|.
name|clusterName
return|;
block|}
comment|/** 	 * This method will return the name of the karaf lock table. 	 * 	 * @return name of the karaf lock table 	 */
specifier|public
specifier|final
name|String
name|getLockTableName
parameter_list|()
block|{
return|return
name|lockTableName
return|;
block|}
comment|/** 	 * This method will return the insert statement used to create a row in the Lock table and will 	 * generate the following sql statement. 	 * 	 * INSERT INTO KARAF_LOCK (ID, STATE, LOCK_DELAY) VALUES (0, 0, 0) 	 *  	 * @return sql insert statement 	 */
specifier|private
name|String
name|getLockTableInitialInsertStatement
parameter_list|()
block|{
return|return
literal|"INSERT INTO "
operator|+
name|this
operator|.
name|getLockTableName
argument_list|()
operator|+
literal|"(ID, STATE, LOCK_DELAY) VALUES (0, 0, 0)"
return|;
block|}
comment|/** 	 * This will be called when trying to acquire the lock and will generate the following sql statemnt. 	 * 	 *  UPDATE KARAF_LOCK SET ID = ?, STATE = ?, LOCK_DELAY = ? WHERE ID = 0 OR ID = ? 	 *  	 * You are then expected to assign the values associated with the sql statement. 	 * 	 * @return sql update statement 	 */
specifier|public
name|String
name|getLockUpdateIdStatement
parameter_list|(
name|int
name|id
parameter_list|,
name|int
name|state
parameter_list|,
name|int
name|lock_delay
parameter_list|,
name|int
name|curId
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"UPDATE %s SET ID = %d, STATE = %d, LOCK_DELAY = %d WHERE ID = 0 OR ID = %d"
argument_list|,
name|this
operator|.
name|getLockTableName
argument_list|()
argument_list|,
name|id
argument_list|,
name|state
argument_list|,
name|lock_delay
argument_list|,
name|curId
argument_list|)
return|;
block|}
comment|/** 	 * This will be called when trying to steal the lock and will generate the following sql statemnt. 	 * 	 *  UPDATE KARAF_LOCK SET ID = ?, STATE = ?, LOCK_DELAY = ? WHERE ( ID = 0 OR ID = ? ) AND STATE = ? 	 * 	 * You are then responsible to assign the values of the different fields using standard jdbc statement 	 * calls. 	 *  	 * @return sql update statement 	 */
specifier|public
name|String
name|getLockUpdateIdStatementToStealLock
parameter_list|(
name|int
name|id
parameter_list|,
name|int
name|state
parameter_list|,
name|int
name|lock_delay
parameter_list|,
name|int
name|curId
parameter_list|,
name|int
name|curState
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"UPDATE %s SET ID = %d, STATE = %d, LOCK_DELAY = %d WHERE ( ID = 0 OR ID = %d ) AND STATE = %d"
argument_list|,
name|this
operator|.
name|getLockTableName
argument_list|()
argument_list|,
name|id
argument_list|,
name|state
argument_list|,
name|lock_delay
argument_list|,
name|curId
argument_list|,
name|curState
argument_list|)
return|;
block|}
comment|/** 	 * This method is called only when we are releasing the lock and will generate the following sql 	 * statement. 	 * 	 *  UPDATE KARAF_LOCK SET ID = 0 WHERE ID = ? 	 *  	 * @return sql update statement 	 */
specifier|public
name|String
name|getLockResetIdStatement
parameter_list|(
name|int
name|id
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"UPDATE %s SET ID = 0 WHERE ID = %d"
argument_list|,
name|this
operator|.
name|getLockTableName
argument_list|()
argument_list|,
name|id
argument_list|)
return|;
block|}
comment|/** 	 * This will be called to determine the current master instance for the lock table and will  	 * generate the following sql statement. 	 * 	 * SELECT ID, STATE, LOCK_DELAY FROM KARAF_LOCK 	 * 	 * @return sql select statement 	 */
specifier|public
name|String
name|getLockSelectStatement
parameter_list|()
block|{
return|return
literal|"SELECT ID, STATE, LOCK_DELAY FROM "
operator|+
name|this
operator|.
name|getLockTableName
argument_list|()
return|;
block|}
specifier|public
name|int
name|getIdFromLockSelectStatement
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
return|;
block|}
specifier|public
name|int
name|getStateFromLockSelectStatement
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getInt
argument_list|(
literal|2
argument_list|)
return|;
block|}
specifier|public
name|int
name|getLockDelayFromLockSelectStatement
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getInt
argument_list|(
literal|3
argument_list|)
return|;
block|}
comment|/** 	 * This method should only be called during the creation of the KARAF_LOCK table and will 	 * generate the following sql statement. 	 * 	 * CREATE TABLE KARAF_LOCK (ID INTEGER DEFAULT 0, STATE INTEGER DEFAULT 0, LOCK_DELAY INTEGER DEFAULT 0) 	 *  	 * @return sql create table statement 	 */
specifier|private
name|String
name|getLockTableCreateStatement
parameter_list|()
block|{
return|return
literal|"CREATE TABLE "
operator|+
name|this
operator|.
name|getLockTableName
argument_list|()
operator|+
literal|" ( ID INTEGER DEFAULT 0, STATE INTEGER DEFAULT 0 , LOCK_DELAY INTEGER DEFAULT 0 )"
return|;
block|}
comment|//  ==================  LOCK ID TABLE ========================
comment|/** 	 * This method will generate the create table sql statement to create the karaf id table and will 	 * generate the following sql statement. 	 * 	 * CREATE TABLE KARAF_ID ( ID INTEGER DEFAULT 0 ) 	 * 	 * @return sql create table statement 	 */
specifier|private
name|String
name|getLockIdTableCreateStatement
parameter_list|()
block|{
return|return
literal|"CREATE TABLE "
operator|+
name|this
operator|.
name|getLockIdTableName
argument_list|()
operator|+
literal|" ( ID INTEGER DEFAULT 0 )"
return|;
block|}
comment|/** 	 * This method will return the sql statement to retreive the id of the lock id table and will 	 * generate the following sql statement. 	 * 	 * SELECT ID FROM KARAF_ID 	 * 	 * @return sql select statement 	 */
specifier|public
name|String
name|getLockIdSelectStatement
parameter_list|()
block|{
return|return
literal|"SELECT ID FROM "
operator|+
name|this
operator|.
name|getLockIdTableName
argument_list|()
return|;
block|}
specifier|public
name|int
name|getIdFromLockIdSelectStatement
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
return|;
block|}
comment|/** 	 * This method will return the update statement for the lock id table and will generate the 	 * following sql statement. 	 * 	 * UPDATE KARAF_ID SET ID = ? WHERE ID = ? 	 * 	 * @return sql update statement 	 */
specifier|public
name|String
name|getLockIdUpdateIdStatement
parameter_list|(
name|int
name|id
parameter_list|,
name|int
name|curId
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"UPDATE %s SET ID = %d WHERE ID = %d"
argument_list|,
name|this
operator|.
name|getLockIdTableName
argument_list|()
argument_list|,
name|id
argument_list|,
name|curId
argument_list|)
return|;
block|}
comment|/** 	 * This method will return the name of the karaf lock id table. 	 * 	 * @return name of the karaf lock id table 	 */
specifier|public
specifier|final
name|String
name|getLockIdTableName
parameter_list|()
block|{
return|return
name|lockIdTableName
return|;
block|}
comment|/** 	 * This method will return the required sql statements to initialize the lock database. 	 * 	 * @return array of sql statements 	 */
specifier|public
name|String
index|[]
name|getLockCreateSchemaStatements
parameter_list|(
name|long
name|moment
parameter_list|)
block|{
return|return
operator|new
name|String
index|[]
block|{
name|getLockTableCreateStatement
argument_list|()
block|,
name|getLockIdTableCreateStatement
argument_list|()
block|,
name|getLockTableInitialInsertStatement
argument_list|()
block|,
name|getLockIdTableInitialInsertStatement
argument_list|()
block|, 		}
return|;
block|}
comment|/** 	 * This method will return the insert statement to insert a row in the lock id table and will 	 * generate the following sql statement. 	 * 	 * INSERT INTO KARAF_ID (ID) VALUES (0) 	 * 	 * @return sql insert statement 	 */
specifier|private
name|String
name|getLockIdTableInitialInsertStatement
parameter_list|()
block|{
return|return
literal|"INSERT INTO "
operator|+
name|this
operator|.
name|getLockIdTableName
argument_list|()
operator|+
literal|"(ID) VALUES (0)"
return|;
block|}
block|}
end_class

end_unit

