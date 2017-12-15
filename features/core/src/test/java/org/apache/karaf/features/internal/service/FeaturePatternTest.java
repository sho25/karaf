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
name|features
operator|.
name|internal
operator|.
name|service
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
name|features
operator|.
name|FeaturePattern
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|Version
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|FeaturePatternTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|matchingFeatureIds
parameter_list|()
block|{
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"spring"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"spring"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"spring"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"spring"
argument_list|,
literal|"0.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"spring"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"spring"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"spring"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"springish"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"commons/1"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"commons"
argument_list|,
literal|"0.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"commons/1"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"commons"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"commons/1"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"commons"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"commons/1"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"commons"
argument_list|,
literal|"1.0.0.1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"space/[3,4]"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"space"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"space/[3,4]"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"space"
argument_list|,
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"space/[3,4]"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"space"
argument_list|,
literal|"3.1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"space/[3,4]"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"x-space"
argument_list|,
literal|"3.1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"space/[3,4]"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"space"
argument_list|,
literal|"4.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"space/[3,4]"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"space"
argument_list|,
literal|"4.0.0.0"
argument_list|)
argument_list|)
expr_stmt|;
comment|// last ".0" is qualifier
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"space/[3,4]"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"space"
argument_list|,
literal|"4.0.1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"special;range=1"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"special"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"special;range=1"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"special"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"special;range=1"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"special"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"special;range=1"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"special2"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"special;range=1"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"special"
argument_list|,
literal|"1.0.1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"universal;range=[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"universal"
argument_list|,
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"universal;range=[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"universal"
argument_list|,
literal|"3.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"universal;range=[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"universal"
argument_list|,
literal|"3.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"universal;range=[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"universal"
argument_list|,
literal|"3.4.2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"universal;range=[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"universal"
argument_list|,
literal|"3.9.9.GA"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"universal;range=[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"universalis"
argument_list|,
literal|"3.9.9.GA"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"universal;range=[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"universal"
argument_list|,
literal|"4.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"a*"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"alphabet"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"a*"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"alphabet"
argument_list|,
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"a*"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"alphabet"
argument_list|,
literal|"999"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"a*"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"_alphabet"
argument_list|,
literal|"999"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"*b/[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"b"
argument_list|,
literal|"3.5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"*b/[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"cb"
argument_list|,
literal|"3.5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"*b/[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"bc"
argument_list|,
literal|"3.5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"*b/[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"cb"
argument_list|,
literal|"4.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"*b/[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"cb"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"*b/[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"cb"
argument_list|,
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"*b/[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"cb"
argument_list|,
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|internal
operator|.
name|model
operator|.
name|Feature
operator|.
name|DEFAULT_VERSION
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
literal|"*b/[3,4)"
argument_list|)
operator|.
name|matches
argument_list|(
literal|"cb"
argument_list|,
name|Version
operator|.
name|emptyVersion
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
