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
name|testing
operator|.
name|support
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractIntegrationTestTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSnapshotVersion
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|AbstractIntegrationTest
operator|.
name|isTimestamped
argument_list|(
literal|"0.9.0-20070713.230317-1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|AbstractIntegrationTest
operator|.
name|isSnapshot
argument_list|(
literal|"0.9.0-SNAPSHOT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|AbstractIntegrationTest
operator|.
name|isSnapshot
argument_list|(
literal|"0.9.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0.9.0-SNAPSHOT"
argument_list|,
name|AbstractIntegrationTest
operator|.
name|getSnapshot
argument_list|(
literal|"0.9.0-20070713.230317-1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0.9.0"
argument_list|,
literal|"0.9.0"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

