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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
comment|/**  *<p>  * Unit tests on<code>Properties</code>.  *</p>  *   * @author jbonofre  */
end_comment

begin_class
specifier|public
class|class
name|PropertiesTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|final
specifier|static
name|String
name|TEST_PROPERTIES_FILE
init|=
literal|"test.properties"
decl_stmt|;
specifier|private
name|Properties
name|properties
decl_stmt|;
comment|/*      * (non-Javadoc)      * @see junit.framework.TestCase#setUp()      */
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_PROPERTIES_FILE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      *<p>      * Test getting property.      *</p>      *       * @throws Exception      */
specifier|public
name|void
name|testGettingProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLoadSave
parameter_list|()
throws|throws
name|IOException
block|{
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
decl_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"# "
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"# The Main  "
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"# "
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"# Comment "
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"# "
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"# Another comment"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"# A value comment"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"key1 = val1"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"# Another value comment"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"key2 = ${key1}/foo"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"# A third comment"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"key3 = val3"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
operator|new
name|StringReader
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|save
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"====="
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
name|props
operator|.
name|get
argument_list|(
literal|"key2"
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"key3"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|props
operator|.
name|save
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"====="
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

