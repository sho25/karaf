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
name|features
operator|.
name|internal
operator|.
name|model
package|;
end_package

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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|ConfigTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testTrim
parameter_list|()
block|{
name|Config
name|config
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|config
operator|.
name|setName
argument_list|(
literal|"my.config"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setValue
argument_list|(
literal|"    # my comment\n"
operator|+
literal|"    my.key = my.value\n"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"my.value"
argument_list|,
name|config
operator|.
name|getProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"my.key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInterpolation
parameter_list|()
block|{
name|Config
name|config
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|config
operator|.
name|setName
argument_list|(
literal|"my.config"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setValue
argument_list|(
literal|"    # my comment\n"
operator|+
literal|"    my.nb = 2\n"
operator|+
literal|"    my.key.1 = my.value.1\n"
operator|+
literal|"    my.key.2 = my.value.2\n"
operator|+
literal|"    my.key.3 = ab${my.key.${my.nb}}"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abmy.value.2"
argument_list|,
name|config
operator|.
name|getProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"my.key.3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

