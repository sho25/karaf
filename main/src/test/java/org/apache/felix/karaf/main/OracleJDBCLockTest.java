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
name|felix
operator|.
name|karaf
operator|.
name|main
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
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|reset
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
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
name|SQLException
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|OracleJDBCLockTest
extends|extends
name|BaseJDBCLockTest
block|{
annotation|@
name|Before
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|password
operator|=
literal|"root"
expr_stmt|;
name|driver
operator|=
literal|"oracle.jdbc.driver.OracleDriver"
expr_stmt|;
name|url
operator|=
literal|"jdbc:oracle:thin:@172.16.16.132:1521:XE"
expr_stmt|;
name|momentDatatype
operator|=
literal|"NUMBER(20)"
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
name|OracleJDBCLock
name|createLock
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
return|return
operator|new
name|OracleJDBCLock
argument_list|(
name|props
argument_list|)
block|{
annotation|@
name|Override
name|Connection
name|doCreateConnection
parameter_list|(
name|String
name|driver
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|this
operator|.
name|driver
argument_list|,
name|driver
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|url
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|user
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|password
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|connection
return|;
block|}
annotation|@
name|Override
name|long
name|getCurrentTimeMillis
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
annotation|@
name|Override
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
name|lockAquired
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
name|lockAquired
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Override
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
name|lockAquired
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
name|lockAquired
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Override
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
name|lockAquired
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
name|lockAquired
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
block|}
end_class

end_unit

