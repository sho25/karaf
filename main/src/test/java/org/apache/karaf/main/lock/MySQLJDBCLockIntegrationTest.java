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
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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

begin_class
annotation|@
name|Ignore
specifier|public
class|class
name|MySQLJDBCLockIntegrationTest
extends|extends
name|BaseJDBCLockIntegrationTest
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|driver
operator|=
literal|"com.mysql.jdbc.Driver"
expr_stmt|;
name|url
operator|=
literal|"jdbc:mysql://127.0.0.1:3306/test"
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
name|MySQLJDBCLock
name|createLock
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
return|return
operator|new
name|MySQLJDBCLock
argument_list|(
name|props
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|initShouldCreateTheDatabaseIfItNotExists
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|database
init|=
literal|"test"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
try|try
block|{
name|executeStatement
argument_list|(
literal|"DROP DATABASE "
operator|+
name|database
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected if table dosn't exist
block|}
name|url
operator|=
literal|"jdbc:mysql://127.0.0.1:3306/"
operator|+
name|database
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
name|lock
operator|=
name|createLock
argument_list|(
name|props
argument_list|)
expr_stmt|;
comment|// should throw an exeption, if the database doesn't exists
name|Connection
name|connection
init|=
name|getConnection
argument_list|(
literal|"jdbc:mysql://127.0.0.1:3306/"
operator|+
name|database
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|connection
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
name|executeStatement
argument_list|(
literal|"DROP DATABASE "
operator|+
name|database
argument_list|)
expr_stmt|;
name|close
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

