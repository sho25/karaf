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
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|DriverManager
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
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|main
operator|.
name|util
operator|.
name|BootstrapLogManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|BaseJDBCLockIntegrationTest
block|{
specifier|private
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultJDBCLock
name|lock
decl_stmt|;
name|Properties
name|props
decl_stmt|;
name|String
name|user
init|=
literal|"root"
decl_stmt|;
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|driver
decl_stmt|;
name|String
name|url
decl_stmt|;
name|String
name|tableName
init|=
literal|"LOCK_TABLE"
decl_stmt|;
name|String
name|clustername
init|=
literal|"karaf_cluster"
decl_stmt|;
name|int
name|timeout
init|=
literal|10
decl_stmt|;
name|String
name|momentDatatype
init|=
literal|"BIGINT"
decl_stmt|;
name|String
name|nodeDatatype
init|=
literal|"VARCHAR(20)"
decl_stmt|;
specifier|abstract
name|DefaultJDBCLock
name|createLock
parameter_list|(
name|Properties
name|props
parameter_list|)
function_decl|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setUpTestSuite
parameter_list|()
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"karaf.bootstrap.log"
argument_list|,
literal|"target/karaf.log"
argument_list|)
expr_stmt|;
name|BootstrapLogManager
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|props
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"karaf.lock.jdbc.url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"karaf.lock.jdbc.driver"
argument_list|,
name|driver
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"karaf.lock.jdbc.user"
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"karaf.lock.jdbc.password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"karaf.lock.jdbc.table"
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"karaf.lock.jdbc.clustername"
argument_list|,
name|clustername
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"karaf.lock.jdbc.timeout"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|timeout
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|executeStatement
argument_list|(
literal|"DROP TABLE "
operator|+
name|tableName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected if the table dosn't exist
block|}
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|lock
operator|!=
literal|null
condition|)
block|{
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
annotation|@
name|Ignore
specifier|public
name|void
name|lockShouldRestoreTheLockAfterADbFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|Lock
name|lock1
init|=
name|createLock
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|lock1
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock1
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
comment|// shut down the database
name|assertFalse
argument_list|(
name|lock1
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
comment|// start the database
name|assertTrue
argument_list|(
name|lock1
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock1
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|initShouldCreateTheSchemaIfItNotExists
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|long
name|moment
init|=
name|queryDatabaseSingleResult
argument_list|(
literal|"SELECT MOMENT FROM "
operator|+
name|tableName
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|moment
operator|>=
name|start
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|moment
operator|<=
name|end
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|initShouldNotCreateTheSchemaIfItAlreadyExists
parameter_list|()
throws|throws
name|Exception
block|{
name|executeStatement
argument_list|(
literal|"CREATE TABLE "
operator|+
name|tableName
operator|+
literal|" (MOMENT "
operator|+
name|momentDatatype
operator|+
literal|", NODE "
operator|+
name|nodeDatatype
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|executeStatement
argument_list|(
literal|"INSERT INTO "
operator|+
name|tableName
operator|+
literal|" (MOMENT, NODE) VALUES (1, '"
operator|+
name|clustername
operator|+
literal|"')"
argument_list|)
expr_stmt|;
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|long
name|moment
init|=
name|queryDatabaseSingleResult
argument_list|(
literal|"SELECT MOMENT FROM "
operator|+
name|tableName
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|moment
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|lockShouldReturnTrueItTheTableIsNotLocked
parameter_list|()
throws|throws
name|Exception
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|assertTableIsLocked
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|lockShouldReturnFalseIfAnotherRowIsLocked
parameter_list|()
throws|throws
name|Exception
block|{
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|executeStatement
argument_list|(
literal|"INSERT INTO "
operator|+
name|tableName
operator|+
literal|" (MOMENT, NODE) VALUES (1, '"
operator|+
name|clustername
operator|+
literal|"_2')"
argument_list|)
expr_stmt|;
name|connection
operator|=
name|lock
argument_list|(
name|tableName
argument_list|,
name|clustername
operator|+
literal|"_2"
argument_list|)
expr_stmt|;
comment|// we can't lock only one row for the cluster
name|assertFalse
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|lockShouldReturnFalseIfTheRowIsAlreadyLocked
parameter_list|()
throws|throws
name|Exception
block|{
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|connection
operator|=
name|lock
argument_list|(
name|tableName
argument_list|,
name|clustername
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|lockShouldReturnFalseIfTheTableIsEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|truncateTable
argument_list|()
expr_stmt|;
comment|//Empty the table
name|connection
operator|=
name|lock
argument_list|(
name|tableName
argument_list|,
name|clustername
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|release
parameter_list|()
throws|throws
name|Exception
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|lock
operator|.
name|lockConnection
argument_list|)
expr_stmt|;
name|assertTableIsUnlocked
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|releaseShouldSucceedForAnAlreadyClosedConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|lock
operator|.
name|lockConnection
operator|.
name|rollback
argument_list|()
expr_stmt|;
comment|// release the lock
name|lock
operator|.
name|lockConnection
operator|.
name|close
argument_list|()
expr_stmt|;
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
name|assertTableIsUnlocked
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|releaseShouldSucceedForANullConnectionReference
parameter_list|()
throws|throws
name|Exception
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|lock
operator|.
name|lockConnection
operator|.
name|rollback
argument_list|()
expr_stmt|;
comment|// release the lock
name|lock
operator|.
name|lockConnection
operator|.
name|close
argument_list|()
expr_stmt|;
name|lock
operator|.
name|lockConnection
operator|=
literal|null
expr_stmt|;
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
name|assertTableIsUnlocked
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|isAliveShouldReturnTrueIfItHoldsTheLock
parameter_list|()
throws|throws
name|Exception
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|isAliveShouldReturnFalseIfTheConnectionIsClosed
parameter_list|()
throws|throws
name|Exception
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|lock
operator|.
name|lockConnection
operator|.
name|rollback
argument_list|()
expr_stmt|;
comment|// release the lock
name|lock
operator|.
name|lockConnection
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|lock
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|isAliveShouldReturnFalseIfTheConnectionIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|lock
operator|.
name|lockConnection
operator|.
name|rollback
argument_list|()
expr_stmt|;
comment|// release the lock
name|lock
operator|.
name|lockConnection
operator|.
name|close
argument_list|()
expr_stmt|;
name|lock
operator|.
name|lockConnection
operator|=
literal|null
expr_stmt|;
name|assertFalse
argument_list|(
name|lock
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|isAliveShouldReturnFalseIfItNotHoldsTheLock
parameter_list|()
throws|throws
name|Exception
block|{
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|lock
operator|.
name|lockConnection
operator|.
name|rollback
argument_list|()
expr_stmt|;
comment|// release the lock
name|connection
operator|=
name|lock
argument_list|(
name|tableName
argument_list|,
name|clustername
argument_list|)
expr_stmt|;
comment|// another connection locks the table
name|assertFalse
argument_list|(
name|lock
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
name|Connection
name|getConnection
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|user
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|SQLException
block|{
name|Class
operator|.
name|forName
argument_list|(
name|driver
argument_list|)
expr_stmt|;
name|Connection
name|connection
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|connection
operator|.
name|setAutoCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|connection
return|;
block|}
name|void
name|executeStatement
parameter_list|(
name|String
name|stmt
parameter_list|)
throws|throws
name|SQLException
throws|,
name|ClassNotFoundException
block|{
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
name|Statement
name|statement
init|=
literal|null
decl_stmt|;
try|try
block|{
name|connection
operator|=
name|getConnection
argument_list|(
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|statement
operator|=
name|connection
operator|.
name|createStatement
argument_list|()
expr_stmt|;
name|statement
operator|.
name|setQueryTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|statement
operator|.
name|execute
argument_list|(
name|stmt
argument_list|)
expr_stmt|;
name|connection
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|close
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
name|Long
name|queryDatabaseSingleResult
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|SQLException
block|{
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
name|Statement
name|statement
init|=
literal|null
decl_stmt|;
name|ResultSet
name|rs
init|=
literal|null
decl_stmt|;
try|try
block|{
name|connection
operator|=
name|getConnection
argument_list|(
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|statement
operator|=
name|connection
operator|.
name|createStatement
argument_list|()
expr_stmt|;
name|rs
operator|=
name|statement
operator|.
name|executeQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|rs
operator|.
name|next
argument_list|()
expr_stmt|;
return|return
name|rs
operator|.
name|getLong
argument_list|(
literal|1
argument_list|)
return|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|rs
argument_list|)
expr_stmt|;
name|close
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|close
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|assertTableIsLocked
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|SQLException
block|{
try|try
block|{
name|executeStatement
argument_list|(
literal|"UPDATE "
operator|+
name|tableName
operator|+
literal|" SET MOMENT = "
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"SQLException for timeout expected because the table should be already locked"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|sqle
parameter_list|)
block|{
comment|// expected
block|}
block|}
name|void
name|assertTableIsUnlocked
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|SQLException
block|{
name|executeStatement
argument_list|(
literal|"UPDATE "
operator|+
name|tableName
operator|+
literal|" SET MOMENT = "
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|void
name|truncateTable
parameter_list|()
throws|throws
name|SQLException
throws|,
name|ClassNotFoundException
block|{
name|executeStatement
argument_list|(
literal|"TRUNCATE TABLE "
operator|+
name|tableName
argument_list|)
expr_stmt|;
block|}
name|Connection
name|lock
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|node
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|SQLException
block|{
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
name|Statement
name|statement
init|=
literal|null
decl_stmt|;
try|try
block|{
name|connection
operator|=
name|getConnection
argument_list|(
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|statement
operator|=
name|connection
operator|.
name|createStatement
argument_list|()
expr_stmt|;
comment|//statement.execute("SELECT * FROM " + table + " WHERE NODE = '" + node + "' FOR UPDATE");
comment|//statement.execute("UPDATE " + table + " SET MOMENT = " + System.currentTimeMillis() + " WHERE NODE = '" + node + "'");
name|statement
operator|.
name|execute
argument_list|(
literal|"SELECT * FROM "
operator|+
name|table
operator|+
literal|" FOR UPDATE"
argument_list|)
expr_stmt|;
name|statement
operator|.
name|execute
argument_list|(
literal|"UPDATE "
operator|+
name|table
operator|+
literal|" SET MOMENT = "
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|statement
argument_list|)
expr_stmt|;
comment|// connection must not be closed!
block|}
return|return
name|connection
return|;
block|}
name|void
name|close
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|rs
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{             }
block|}
block|}
name|void
name|close
parameter_list|(
name|Statement
name|statement
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|statement
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Can't close the statement: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|close
parameter_list|(
name|Connection
name|connection
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|connection
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Can't rollback the connection: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Can't close the connection: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

