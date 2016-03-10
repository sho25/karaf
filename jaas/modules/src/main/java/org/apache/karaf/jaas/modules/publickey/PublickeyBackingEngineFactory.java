begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|publickey
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|BackingEngine
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
name|jaas
operator|.
name|modules
operator|.
name|BackingEngineFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|PublickeyBackingEngineFactory
implements|implements
name|BackingEngineFactory
block|{
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PublickeyBackingEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USER_FILE
init|=
literal|"users"
decl_stmt|;
specifier|public
name|BackingEngine
name|build
parameter_list|(
name|Map
name|options
parameter_list|)
block|{
name|PublickeyBackingEngine
name|engine
init|=
literal|null
decl_stmt|;
name|String
name|usersFile
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|USER_FILE
argument_list|)
decl_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|usersFile
argument_list|)
decl_stmt|;
name|Properties
name|users
decl_stmt|;
try|try
block|{
name|users
operator|=
operator|new
name|Properties
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|engine
operator|=
operator|new
name|PublickeyBackingEngine
argument_list|(
name|users
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Cannot open keys file:"
operator|+
name|usersFile
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
return|return
name|engine
return|;
block|}
block|}
specifier|public
name|String
name|getModuleClass
parameter_list|()
block|{
return|return
name|PublickeyLoginModule
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

