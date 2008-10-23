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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|config
package|;
end_package

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
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_class
specifier|public
class|class
name|PropListCommand
extends|extends
name|ConfigCommandSupport
block|{
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
else|else
block|{
for|for
control|(
name|Enumeration
name|e
init|=
name|props
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|Object
name|key
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|key
operator|+
literal|" = "
operator|+
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
block|}
block|}
end_class

end_unit

