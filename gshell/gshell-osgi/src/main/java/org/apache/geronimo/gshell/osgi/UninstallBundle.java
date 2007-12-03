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
name|geronimo
operator|.
name|gshell
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|annotation
operator|.
name|CommandComponent
import|;
end_import

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: gnodet  * Date: Oct 3, 2007  * Time: 12:37:30 PM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
annotation|@
name|CommandComponent
argument_list|(
name|id
operator|=
literal|"osgi:uninstall"
argument_list|,
name|description
operator|=
literal|"Uninstall bundle"
argument_list|)
specifier|public
class|class
name|UninstallBundle
extends|extends
name|BundleCommand
block|{
specifier|protected
name|void
name|doExecute
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
name|bundle
operator|.
name|uninstall
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

