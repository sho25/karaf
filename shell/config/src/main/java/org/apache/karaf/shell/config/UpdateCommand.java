begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|config
package|;
end_package

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
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|gogo
operator|.
name|commands
operator|.
name|Option
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
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
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
name|gogo
operator|.
name|commands
operator|.
name|Command
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"config"
argument_list|,
name|name
operator|=
literal|"update"
argument_list|,
name|description
operator|=
literal|"Saves and propagates changes from the configuration being edited."
argument_list|)
specifier|public
class|class
name|UpdateCommand
extends|extends
name|ConfigCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-b"
argument_list|,
name|aliases
operator|=
block|{
literal|"--bypass-storage"
block|}
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|description
operator|=
literal|"Do not store the configuration in a properties file, but feed it directly to ConfigAdmin"
argument_list|)
specifier|private
name|boolean
name|bypassStorage
decl_stmt|;
specifier|private
name|File
name|storage
decl_stmt|;
specifier|public
name|File
name|getStorage
parameter_list|()
block|{
return|return
name|storage
return|;
block|}
specifier|public
name|void
name|setStorage
parameter_list|(
name|File
name|storage
parameter_list|)
block|{
name|this
operator|.
name|storage
operator|=
name|storage
expr_stmt|;
block|}
specifier|protected
name|void
name|doExecute
parameter_list|(
name|ConfigurationAdmin
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
name|Dictionary
name|props
init|=
name|getEditedProps
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"No configuration is being edited. Run the edit command first"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|bypassStorage
operator|&&
name|storage
operator|!=
literal|null
condition|)
block|{
name|String
name|pid
init|=
operator|(
name|String
operator|)
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|PROPERTY_CONFIG_PID
argument_list|)
decl_stmt|;
name|File
name|storageFile
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|,
name|pid
operator|+
literal|".cfg"
argument_list|)
decl_stmt|;
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|(
name|storageFile
argument_list|)
decl_stmt|;
for|for
control|(
name|Enumeration
name|keys
init|=
name|props
operator|.
name|keys
argument_list|()
init|;
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|Object
name|key
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"service.pid"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|&&
operator|!
literal|"felix.fileinstall.filename"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|p
operator|.
name|put
argument_list|(
operator|(
name|String
operator|)
name|key
argument_list|,
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|storage
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|p
operator|.
name|save
argument_list|()
expr_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|PROPERTY_CONFIG_PID
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|PROPERTY_CONFIG_PROPS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|pid
init|=
operator|(
name|String
operator|)
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|PROPERTY_CONFIG_PID
argument_list|)
decl_stmt|;
name|Configuration
name|cfg
init|=
name|admin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|update
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|PROPERTY_CONFIG_PID
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|PROPERTY_CONFIG_PROPS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

