begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
operator|.
name|bundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

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
name|Configuration
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
name|CoreOptions
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
name|Option
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
name|PerClass
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
name|Bundle
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
name|PerClass
operator|.
name|class
argument_list|)
specifier|public
class|class
name|ImportServiceTest
extends|extends
name|BaseTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE2_NAME
init|=
literal|"testbundle.require.service"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE1_NAME
init|=
literal|"testbundle.import.service"
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|config
parameter_list|()
block|{
name|List
argument_list|<
name|Option
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|super
operator|.
name|config
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|InputStream
name|testBundleImportService
init|=
name|bundle
argument_list|()
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|IMPORT_SERVICE
argument_list|,
literal|"FooService"
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
name|BUNDLE1_NAME
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|,
literal|"1.0.0"
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_MANIFESTVERSION
argument_list|,
literal|"2"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|options
operator|.
name|add
argument_list|(
name|CoreOptions
operator|.
name|streamBundle
argument_list|(
name|testBundleImportService
argument_list|)
argument_list|)
expr_stmt|;
name|InputStream
name|testBundleRequireService
init|=
name|bundle
argument_list|()
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|REQUIRE_CAPABILITY
argument_list|,
literal|"osgi.service;effective:=active;filter:=\"(objectClass=FooService)\""
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
name|BUNDLE2_NAME
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|,
literal|"1.0.0"
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_MANIFESTVERSION
argument_list|,
literal|"2"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|options
operator|.
name|add
argument_list|(
name|CoreOptions
operator|.
name|streamBundle
argument_list|(
name|testBundleRequireService
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|options
operator|.
name|toArray
argument_list|(
operator|new
name|Option
index|[]
block|{}
argument_list|)
return|;
block|}
comment|/**      * Checks that the resolver does not mandate specified required services to be present.      * This is done for backwards compatibility as not all bundles define capabilities for services they start.      */
annotation|@
name|Test
specifier|public
name|void
name|checkBundleStarted
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|waitBundleState
argument_list|(
name|BUNDLE1_NAME
argument_list|,
name|Bundle
operator|.
name|ACTIVE
argument_list|)
expr_stmt|;
name|waitBundleState
argument_list|(
name|BUNDLE2_NAME
argument_list|,
name|Bundle
operator|.
name|ACTIVE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

