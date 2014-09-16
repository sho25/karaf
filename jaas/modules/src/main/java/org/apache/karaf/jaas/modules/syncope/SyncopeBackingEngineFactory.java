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
name|syncope
package|;
end_package

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
name|util
operator|.
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|SyncopeBackingEngineFactory
implements|implements
name|BackingEngineFactory
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SyncopeBackingEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|BackingEngine
name|build
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
parameter_list|)
block|{
name|SyncopeBackingEngine
name|instance
init|=
literal|null
decl_stmt|;
name|String
name|address
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SyncopeLoginModule
operator|.
name|ADDRESS
argument_list|)
decl_stmt|;
name|String
name|adminUser
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SyncopeLoginModule
operator|.
name|ADMIN_USER
argument_list|)
decl_stmt|;
name|String
name|adminPassword
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SyncopeLoginModule
operator|.
name|ADMIN_PASSWORD
argument_list|)
decl_stmt|;
try|try
block|{
name|instance
operator|=
operator|new
name|SyncopeBackingEngine
argument_list|(
name|address
argument_list|,
name|adminUser
argument_list|,
name|adminPassword
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error creating the Syncope backing engine"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
comment|/**      * Returns the login module class, that this factory can build.      */
specifier|public
name|String
name|getModuleClass
parameter_list|()
block|{
return|return
name|SyncopeLoginModule
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
