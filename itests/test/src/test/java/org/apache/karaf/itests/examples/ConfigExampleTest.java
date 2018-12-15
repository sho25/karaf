begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|itests
operator|.
name|examples
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
name|itests
operator|.
name|KarafTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|ExamReactorStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|PerMethod
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
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|Hashtable
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
annotation|@
name|ExamReactorStrategy
argument_list|(
name|PerMethod
operator|.
name|class
argument_list|)
specifier|public
class|class
name|ConfigExampleTest
extends|extends
name|KarafTestSupport
block|{
specifier|private
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|private
name|PrintStream
name|originStream
decl_stmt|;
specifier|private
name|PrintStream
name|outStream
init|=
operator|new
name|PrintStream
argument_list|(
name|byteArrayOutputStream
argument_list|)
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|originStream
operator|=
name|System
operator|.
name|out
expr_stmt|;
name|System
operator|.
name|setOut
argument_list|(
name|outStream
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addFeaturesRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|addFeaturesRepository
argument_list|(
literal|"mvn:org.apache.karaf.examples/karaf-config-example-features/"
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.version"
argument_list|)
operator|+
literal|"/xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStatic
parameter_list|()
throws|throws
name|Exception
block|{
name|addFeaturesRepository
argument_list|()
expr_stmt|;
name|installAndAssertFeature
argument_list|(
literal|"karaf-config-example-static"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|System
operator|.
name|setOut
argument_list|(
name|originStream
argument_list|)
expr_stmt|;
name|String
name|output
init|=
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"foo = bar"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"hello = world"
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testManaged
parameter_list|()
throws|throws
name|Exception
block|{
name|addFeaturesRepository
argument_list|()
expr_stmt|;
name|installAndAssertFeature
argument_list|(
literal|"karaf-config-example-managed"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertContains
argument_list|(
literal|"Configuration changed"
argument_list|,
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
literal|"org.apache.karaf.example.config"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|properties
operator|.
name|put
argument_list|(
literal|"exam"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|update
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"exam = test"
argument_list|,
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testListener
parameter_list|()
throws|throws
name|Exception
block|{
name|addFeaturesRepository
argument_list|()
expr_stmt|;
name|installAndAssertFeature
argument_list|(
literal|"karaf-config-example-listener"
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
literal|"org.apache.karaf.example.config"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"exam"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|update
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertContains
argument_list|(
literal|"Configuration org.apache.karaf.example.config has been updated"
argument_list|,
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBlueprint
parameter_list|()
throws|throws
name|Exception
block|{
name|addFeaturesRepository
argument_list|()
expr_stmt|;
name|installAndAssertFeature
argument_list|(
literal|"karaf-config-example-blueprint"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertContains
argument_list|(
literal|"hello = world"
argument_list|,
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
literal|"org.apache.karaf.example.config"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"hello"
argument_list|,
literal|"exam"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|update
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertContains
argument_list|(
literal|"hello = exam"
argument_list|,
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testScr
parameter_list|()
throws|throws
name|Exception
block|{
name|addFeaturesRepository
argument_list|()
expr_stmt|;
name|installAndAssertFeature
argument_list|(
literal|"karaf-config-example-scr"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertContainsNot
argument_list|(
literal|"hello = exam"
argument_list|,
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
literal|"org.apache.karaf.example.config"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"hello"
argument_list|,
literal|"exam"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|update
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertContains
argument_list|(
literal|"hello = exam"
argument_list|,
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
