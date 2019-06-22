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
name|PerClass
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
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|StandardOpenOption
operator|.
name|*
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
name|*
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
name|OverrideConfigTest
extends|extends
name|KarafTestSupport
block|{
annotation|@
name|Test
specifier|public
name|void
name|testOverrideConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|dir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
argument_list|,
literal|"system/org/foo/bar/1.0-SNAPSHOT"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|dir
argument_list|)
expr_stmt|;
name|writeTo
argument_list|(
name|dir
operator|.
name|resolve
argument_list|(
literal|"bar-1.0-SNAPSHOT-features.xml"
argument_list|)
argument_list|,
comment|//
literal|"<features name='org.foo'>\n"
operator|+
literal|"<feature name='bar' version='1.0-SNAPSHOT'>\n"
operator|+
literal|"<config name='org.foo' override='true'>\n"
operator|+
literal|"      foo=bar"
operator|+
literal|"</config>\n"
operator|+
literal|"</feature>\n"
operator|+
literal|"</features>\n"
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
literal|"org.foo"
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|update
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|Configuration
index|[]
name|cfgs
init|=
name|configurationAdmin
operator|.
name|listConfigurations
argument_list|(
literal|"(service.pid=org.foo)"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cfgs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cfgs
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|cfgs
index|[
literal|0
index|]
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|addRepository
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"mvn:org.foo/bar/1.0-SNAPSHOT/xml/features"
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|cfgs
operator|=
name|configurationAdmin
operator|.
name|listConfigurations
argument_list|(
literal|"(service.pid=org.foo)"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cfgs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cfgs
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|cfgs
index|[
literal|0
index|]
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeTo
parameter_list|(
name|Path
name|file
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|BufferedWriter
name|w
init|=
name|Files
operator|.
name|newBufferedWriter
argument_list|(
name|file
argument_list|,
name|CREATE
argument_list|,
name|TRUNCATE_EXISTING
argument_list|,
name|WRITE
argument_list|)
init|)
block|{
name|w
operator|.
name|write
argument_list|(
name|content
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
