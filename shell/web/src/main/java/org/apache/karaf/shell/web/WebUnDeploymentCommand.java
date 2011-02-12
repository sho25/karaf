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
name|web
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
literal|"web"
argument_list|,
name|name
operator|=
literal|"undeploy"
argument_list|,
name|description
operator|=
literal|"Stops/undeploys the web context of the given bundle id."
argument_list|)
specifier|public
class|class
name|WebUnDeploymentCommand
extends|extends
name|WebManagerCommand
block|{
annotation|@
name|Override
specifier|protected
name|void
name|doExecute
parameter_list|(
name|List
argument_list|<
name|Long
argument_list|>
name|bundleIds
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Long
name|bundleId
range|:
name|bundleIds
control|)
block|{
name|getWarManager
argument_list|()
operator|.
name|stop
argument_list|(
name|bundleId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

