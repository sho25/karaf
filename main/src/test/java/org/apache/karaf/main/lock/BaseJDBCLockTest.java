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
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|*
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
name|assertFalse
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
name|assertTrue
import|;
end_import

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
name|DatabaseMetaData
import|;
end_import

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
name|easymock
operator|.
name|EasyMock
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
name|Test
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|BaseJDBCLockTest
block|{
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
name|String
name|createTableStmtSuffix
init|=
literal|""
decl_stmt|;
name|Connection
name|connection
decl_stmt|;
name|DatabaseMetaData
name|metaData
decl_stmt|;
name|ResultSet
name|resultSet
decl_stmt|;
name|PreparedStatement
name|preparedStatement
decl_stmt|;
name|Statement
name|statement
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
name|connection
operator|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
expr_stmt|;
name|metaData
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|DatabaseMetaData
operator|.
name|class
argument_list|)
expr_stmt|;
name|resultSet
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ResultSet
operator|.
name|class
argument_list|)
expr_stmt|;
name|preparedStatement
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|PreparedStatement
operator|.
name|class
argument_list|)
expr_stmt|;
name|statement
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Statement
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|Integer
operator|.
name|toString
argument_list|(
name|timeout
argument_list|)
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
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setAutoCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|getMetaData
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|metaData
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|metaData
operator|.
name|getTables
argument_list|(
name|isNull
argument_list|()
argument_list|,
name|isNull
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|,
name|aryEq
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"TABLE"
block|}
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|resultSet
operator|.
name|next
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|resultSet
operator|.
name|close
argument_list|()
expr_stmt|;
name|expectLastCall
argument_list|()
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|createStatement
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|statement
operator|.
name|execute
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
operator|+
name|createTableStmtSuffix
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|statement
operator|.
name|execute
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
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
name|connection
operator|.
name|commit
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
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
name|connection
operator|.
name|setAutoCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|getMetaData
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|metaData
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|metaData
operator|.
name|getTables
argument_list|(
name|isNull
argument_list|()
argument_list|,
name|isNull
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|,
name|aryEq
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"TABLE"
block|}
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|resultSet
operator|.
name|next
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resultSet
operator|.
name|close
argument_list|()
expr_stmt|;
name|expectLastCall
argument_list|()
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
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
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"SELECT * FROM "
operator|+
name|tableName
operator|+
literal|" FOR UPDATE"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preparedStatement
operator|.
name|execute
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"UPDATE "
operator|+
name|tableName
operator|+
literal|" SET MOMENT = 1"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preparedStatement
operator|.
name|executeUpdate
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|boolean
name|lockAcquired
init|=
name|lock
operator|.
name|lock
argument_list|()
decl_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lockAcquired
argument_list|)
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
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"SELECT * FROM "
operator|+
name|tableName
operator|+
literal|" FOR UPDATE"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preparedStatement
operator|.
name|execute
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"UPDATE "
operator|+
name|tableName
operator|+
literal|" SET MOMENT = 1"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preparedStatement
operator|.
name|executeUpdate
argument_list|()
argument_list|)
operator|.
name|andThrow
argument_list|(
operator|new
name|SQLException
argument_list|()
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|boolean
name|lockAcquired
init|=
name|lock
operator|.
name|lock
argument_list|()
decl_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|lockAcquired
argument_list|)
expr_stmt|;
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
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"SELECT * FROM "
operator|+
name|tableName
operator|+
literal|" FOR UPDATE"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preparedStatement
operator|.
name|execute
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"UPDATE "
operator|+
name|tableName
operator|+
literal|" SET MOMENT = 1"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preparedStatement
operator|.
name|executeUpdate
argument_list|()
argument_list|)
operator|.
name|andThrow
argument_list|(
operator|new
name|SQLException
argument_list|()
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|boolean
name|lockAcquired
init|=
name|lock
operator|.
name|lock
argument_list|()
decl_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|lockAcquired
argument_list|)
expr_stmt|;
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
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|connection
operator|.
name|rollback
argument_list|()
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
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
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
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
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|lock
operator|.
name|lockConnection
operator|=
literal|null
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
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
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"UPDATE "
operator|+
name|tableName
operator|+
literal|" SET MOMENT = 1"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preparedStatement
operator|.
name|executeUpdate
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|boolean
name|alive
init|=
name|lock
operator|.
name|isAlive
argument_list|()
decl_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|alive
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
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|boolean
name|alive
init|=
name|lock
operator|.
name|isAlive
argument_list|()
decl_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|alive
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
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|lock
operator|.
name|lockConnection
operator|=
literal|null
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|boolean
name|alive
init|=
name|lock
operator|.
name|isAlive
argument_list|()
decl_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|alive
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
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"UPDATE "
operator|+
name|tableName
operator|+
literal|" SET MOMENT = 1"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preparedStatement
operator|.
name|executeUpdate
argument_list|()
argument_list|)
operator|.
name|andThrow
argument_list|(
operator|new
name|SQLException
argument_list|()
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|boolean
name|alive
init|=
name|lock
operator|.
name|isAlive
argument_list|()
decl_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|alive
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|lockShouldReturnFalseIfTableIsEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|initShouldNotCreateTheSchemaIfItAlreadyExists
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"SELECT * FROM "
operator|+
name|tableName
operator|+
literal|" FOR UPDATE"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preparedStatement
operator|.
name|execute
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|connection
operator|.
name|prepareStatement
argument_list|(
literal|"UPDATE "
operator|+
name|tableName
operator|+
literal|" SET MOMENT = 1"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|preparedStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preparedStatement
operator|.
name|executeUpdate
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|boolean
name|lockAcquired
init|=
name|lock
operator|.
name|lock
argument_list|()
decl_stmt|;
name|verify
argument_list|(
name|connection
argument_list|,
name|metaData
argument_list|,
name|statement
argument_list|,
name|preparedStatement
argument_list|,
name|resultSet
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|lockAcquired
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

