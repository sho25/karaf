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

begin_comment
comment|/**  * Represents an exclusive lock on a database,  * used to avoid multiple Karaf instances attempting  * to become master.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultJDBCLock
implements|implements
name|Lock
block|{
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
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_URL
init|=
literal|"karaf.lock.jdbc.url"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_JDBC_DRIVER
init|=
literal|"karaf.lock.jdbc.driver"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_JDBC_USER
init|=
literal|"karaf.lock.jdbc.user"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_JDBC_PASSWORD
init|=
literal|"karaf.lock.jdbc.password"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_JDBC_TABLE
init|=
literal|"karaf.lock.jdbc.table"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_JDBC_CLUSTERNAME
init|=
literal|"karaf.lock.jdbc.clustername"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_JDBC_TIMEOUT
init|=
literal|"karaf.lock.jdbc.timeout"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_PASSWORD
init|=
literal|""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_USER
init|=
literal|""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_TABLE
init|=
literal|"KARAF_LOCK"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CLUSTERNAME
init|=
literal|"karaf"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_TIMEOUT
init|=
literal|"10"
decl_stmt|;
comment|// in seconds
specifier|final
name|Statements
name|statements
decl_stmt|;
name|Connection
name|lockConnection
decl_stmt|;
name|String
name|url
decl_stmt|;
name|String
name|driver
decl_stmt|;
name|String
name|user
decl_stmt|;
name|String
name|password
decl_stmt|;
name|String
name|table
decl_stmt|;
name|String
name|clusterName
decl_stmt|;
name|int
name|timeout
decl_stmt|;
specifier|public
name|DefaultJDBCLock
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
name|LOG
operator|.
name|addHandler
argument_list|(
name|BootstrapLogManager
operator|.
name|getDefaultHandler
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_URL
argument_list|)
expr_stmt|;
name|this
operator|.
name|driver
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_JDBC_DRIVER
argument_list|)
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_JDBC_USER
argument_list|,
name|DEFAULT_USER
argument_list|)
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_JDBC_PASSWORD
argument_list|,
name|DEFAULT_PASSWORD
argument_list|)
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_JDBC_TABLE
argument_list|,
name|DEFAULT_TABLE
argument_list|)
expr_stmt|;
name|this
operator|.
name|clusterName
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_JDBC_CLUSTERNAME
argument_list|,
name|DEFAULT_CLUSTERNAME
argument_list|)
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_JDBC_TIMEOUT
argument_list|,
name|DEFAULT_TIMEOUT
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|statements
operator|=
name|createStatements
argument_list|()
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
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
return|return
name|statements
return|;
block|}
name|void
name|init
parameter_list|()
block|{
try|try
block|{
name|createDatabase
argument_list|()
expr_stmt|;
name|createSchema
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
literal|"Error occured while attempting to obtain connection: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|createDatabase
parameter_list|()
block|{
comment|// do nothing in the default implementation
block|}
name|void
name|createSchema
parameter_list|()
block|{
if|if
condition|(
name|schemaExists
argument_list|()
condition|)
block|{
return|return;
block|}
name|String
index|[]
name|createStatments
init|=
name|this
operator|.
name|statements
operator|.
name|getLockCreateSchemaStatements
argument_list|(
name|getCurrentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|Statement
name|statement
init|=
literal|null
decl_stmt|;
try|try
block|{
name|statement
operator|=
name|getConnection
argument_list|()
operator|.
name|createStatement
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|stmt
range|:
name|createStatments
control|)
block|{
name|statement
operator|.
name|execute
argument_list|(
name|stmt
argument_list|)
expr_stmt|;
block|}
name|getConnection
argument_list|()
operator|.
name|commit
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
literal|"Could not create schema: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|closeSafely
argument_list|(
name|statement
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|schemaExists
parameter_list|()
block|{
name|ResultSet
name|rs
init|=
literal|null
decl_stmt|;
name|boolean
name|schemaExists
init|=
literal|false
decl_stmt|;
try|try
block|{
name|rs
operator|=
name|getConnection
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|getTables
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|statements
operator|.
name|getFullLockTableName
argument_list|()
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"TABLE"
block|}
argument_list|)
expr_stmt|;
name|schemaExists
operator|=
name|rs
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Error testing for db table: "
operator|+
name|ignore
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|closeSafely
argument_list|(
name|rs
argument_list|)
expr_stmt|;
block|}
return|return
name|schemaExists
return|;
block|}
comment|/*      * (non-Javadoc)      * @see org.apache.karaf.main.Lock#lock()      */
specifier|public
name|boolean
name|lock
parameter_list|()
block|{
name|boolean
name|result
init|=
name|aquireLock
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
name|result
operator|=
name|updateLock
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
name|boolean
name|aquireLock
parameter_list|()
block|{
name|String
name|lockCreateStatement
init|=
name|statements
operator|.
name|getLockCreateStatement
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
name|lockCreateStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|lockAquired
operator|=
name|preparedStatement
operator|.
name|execute
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
name|boolean
name|updateLock
parameter_list|()
block|{
name|String
name|lockUpdateStatement
init|=
name|statements
operator|.
name|getLockUpdateStatement
argument_list|(
name|getCurrentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|PreparedStatement
name|preparedStatement
init|=
literal|null
decl_stmt|;
name|boolean
name|lockUpdated
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
name|lockUpdateStatement
argument_list|)
expr_stmt|;
name|preparedStatement
operator|.
name|setQueryTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|int
name|rows
init|=
name|preparedStatement
operator|.
name|executeUpdate
argument_list|()
decl_stmt|;
name|lockUpdated
operator|=
operator|(
name|rows
operator|==
literal|1
operator|)
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
name|warning
argument_list|(
literal|"Failed to update database lock: "
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
name|lockUpdated
return|;
block|}
comment|/*      * (non-Javadoc)      * @see org.apache.karaf.main.Lock#release()      */
specifier|public
name|void
name|release
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isConnected
argument_list|()
condition|)
block|{
try|try
block|{
name|getConnection
argument_list|()
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Exception while rollbacking the connection on release: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|getConnection
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ignored
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Exception while closing connection on release: "
operator|+
name|ignored
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|lockConnection
operator|=
literal|null
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      * @see org.apache.karaf.main.Lock#isAlive()      */
specifier|public
name|boolean
name|isAlive
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|isConnected
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Lost lock!"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
name|updateLock
argument_list|()
return|;
block|}
name|boolean
name|isConnected
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|lockConnection
operator|!=
literal|null
operator|&&
operator|!
name|lockConnection
operator|.
name|isClosed
argument_list|()
return|;
block|}
name|void
name|closeSafely
parameter_list|(
name|Statement
name|preparedStatement
parameter_list|)
block|{
if|if
condition|(
name|preparedStatement
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|preparedStatement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Failed to close statement: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|closeSafely
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
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
name|SQLException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Error occured while releasing ResultSet: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|isConnected
argument_list|()
condition|)
block|{
name|lockConnection
operator|=
name|createConnection
argument_list|(
name|driver
argument_list|,
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|lockConnection
operator|.
name|setAutoCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|lockConnection
return|;
block|}
comment|/**      * Create a new jdbc connection.      *       * @param driver      * @param url      * @param username      * @param password      * @return a new jdbc connection      * @throws Exception       */
name|Connection
name|createConnection
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
throws|throws
name|Exception
block|{
if|if
condition|(
name|url
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"jdbc:derby"
argument_list|)
condition|)
block|{
name|url
operator|=
operator|(
name|url
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"create=true"
argument_list|)
operator|)
condition|?
name|url
else|:
name|url
operator|+
literal|";create=true"
expr_stmt|;
block|}
try|try
block|{
return|return
name|doCreateConnection
argument_list|(
name|driver
argument_list|,
name|url
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
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
literal|"Error occured while setting up JDBC connection: "
operator|+
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
comment|/**      * This method could be used to inject a mock jdbc connection for testing purposes.      *       * @param driver      * @param url      * @param username      * @param password      * @return      * @throws ClassNotFoundException      * @throws SQLException      */
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
comment|// results in a closed connection in Derby if the update lock table request timed out
comment|// DriverManager.setLoginTimeout(timeout);
return|return
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
name|long
name|getCurrentTimeMillis
parameter_list|()
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
return|;
block|}
block|}
end_class

end_unit

