begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|features
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|utils
operator|.
name|ManifestUtils
operator|.
name|matches
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
name|manifest
operator|.
name|Attribute
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
name|manifest
operator|.
name|Clause
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
name|manifest
operator|.
name|Directive
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
name|tooling
operator|.
name|utils
operator|.
name|ManifestUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_comment
comment|/**  * Test cased for {@link org.apache.karaf.tooling.utils.ManifestUtils}  */
end_comment

begin_class
specifier|public
class|class
name|ManifestUtilsTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testIsOptional
parameter_list|()
block|{
name|Directive
index|[]
name|directive
init|=
operator|new
name|Directive
index|[
literal|0
index|]
decl_stmt|;
name|Attribute
index|[]
name|attribute
init|=
operator|new
name|Attribute
index|[
literal|0
index|]
decl_stmt|;
name|Clause
name|clause
init|=
operator|new
name|Clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
name|directive
argument_list|,
name|attribute
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|ManifestUtils
operator|.
name|isOptional
argument_list|(
name|clause
argument_list|)
argument_list|)
expr_stmt|;
name|directive
operator|=
operator|new
name|Directive
index|[
literal|1
index|]
expr_stmt|;
name|directive
index|[
literal|0
index|]
operator|=
operator|new
name|Directive
argument_list|(
literal|"resolution"
argument_list|,
literal|"mandatory"
argument_list|)
expr_stmt|;
name|clause
operator|=
operator|new
name|Clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
name|directive
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ManifestUtils
operator|.
name|isOptional
argument_list|(
name|clause
argument_list|)
argument_list|)
expr_stmt|;
name|directive
index|[
literal|0
index|]
operator|=
operator|new
name|Directive
argument_list|(
literal|"resolution"
argument_list|,
literal|"optional"
argument_list|)
expr_stmt|;
name|clause
operator|=
operator|new
name|Clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
name|directive
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ManifestUtils
operator|.
name|isOptional
argument_list|(
name|clause
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatches
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.dev"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"1.2.0"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.1.0]"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"1.1.0"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.1.0]"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// a single version means>= 1.0.0, so 1.1.O should be a match
name|assertTrue
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"1.1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.2.0)"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.0.0, 1.0.0]"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.2.0)"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.2.0, 1.2.0]"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.2.0)"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.1.0]"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.2.0)"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.1, 1.1.1]"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.1.0]"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.1.0]"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.1.0]"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"1.1.1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matches
argument_list|(
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"[1.1.0, 1.1.0]"
argument_list|)
argument_list|,
name|clause
argument_list|(
literal|"org.apache.karaf.test"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Clause
name|clause
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|Clause
argument_list|(
name|name
argument_list|,
operator|new
name|Directive
index|[
literal|0
index|]
argument_list|,
operator|new
name|Attribute
index|[
literal|0
index|]
argument_list|)
return|;
block|}
specifier|private
name|Clause
name|clause
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|Attribute
index|[]
name|attribute
init|=
block|{
operator|new
name|Attribute
argument_list|(
name|Constants
operator|.
name|VERSION_ATTRIBUTE
argument_list|,
name|version
argument_list|)
block|}
decl_stmt|;
return|return
operator|new
name|Clause
argument_list|(
name|name
argument_list|,
operator|new
name|Directive
index|[
literal|0
index|]
argument_list|,
name|attribute
argument_list|)
return|;
block|}
block|}
end_class

end_unit

