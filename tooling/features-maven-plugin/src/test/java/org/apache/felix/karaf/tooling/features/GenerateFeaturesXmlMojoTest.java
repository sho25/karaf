begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|tooling
operator|.
name|features
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jmock
operator|.
name|Expectations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jmock
operator|.
name|Mockery
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Test cases for {@link GenerateFeaturesXmlMojo}  */
end_comment

begin_class
specifier|public
class|class
name|GenerateFeaturesXmlMojoTest
extends|extends
name|TestCase
block|{
specifier|private
name|Mockery
name|mockery
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|mockery
operator|=
operator|new
name|Mockery
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testToString
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Artifact
name|artifact
init|=
name|mockery
operator|.
name|mock
argument_list|(
name|Artifact
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockery
operator|.
name|checking
argument_list|(
operator|new
name|Expectations
argument_list|()
block|{
block|{
name|allowing
argument_list|(
name|artifact
argument_list|)
operator|.
name|getGroupId
argument_list|()
expr_stmt|;
name|will
argument_list|(
name|returnValue
argument_list|(
literal|"org.apache.servicemix.test"
argument_list|)
argument_list|)
expr_stmt|;
name|allowing
argument_list|(
name|artifact
argument_list|)
operator|.
name|getArtifactId
argument_list|()
expr_stmt|;
name|will
argument_list|(
name|returnValue
argument_list|(
literal|"test-artifact"
argument_list|)
argument_list|)
expr_stmt|;
name|allowing
argument_list|(
name|artifact
argument_list|)
operator|.
name|getVersion
argument_list|()
expr_stmt|;
name|will
argument_list|(
name|returnValue
argument_list|(
literal|"1.2.3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.servicemix.test/test-artifact/1.2.3"
argument_list|,
name|GenerateFeaturesXmlMojo
operator|.
name|toString
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

