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
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|editConfigurationFilePut
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
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
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|PerClass
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|JmsTest
extends|extends
name|KarafTestSupport
block|{
specifier|private
specifier|static
specifier|final
name|EnumSet
argument_list|<
name|FeaturesService
operator|.
name|Option
argument_list|>
name|NO_AUTO_REFRESH
init|=
name|EnumSet
operator|.
name|of
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|)
decl_stmt|;
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|60000
argument_list|)
specifier|public
name|void
name|testCommands
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Add features repositories"
argument_list|)
expr_stmt|;
name|executeCommand
argument_list|(
literal|"feature:repo-add spring-legacy"
argument_list|)
expr_stmt|;
name|executeCommand
argument_list|(
literal|"feature:repo-add activemq 5.15.3"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"aries-blueprint"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Installing JMS feature"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"jms"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"pax-jms-activemq"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Installing ActiveMQ"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"shell-compat"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"activemq-broker-noweb"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Creating JMS ConnectionFactory"
argument_list|)
expr_stmt|;
name|executeCommand
argument_list|(
literal|"jms:create test"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|String
name|output
init|=
name|executeCommand
argument_list|(
literal|"jms:connectionfactories"
argument_list|)
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
literal|"jms/test"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|output
operator|=
name|executeCommand
argument_list|(
literal|"jms:info jms/test"
argument_list|)
expr_stmt|;
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
literal|"ActiveMQ"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|executeCommand
argument_list|(
literal|"jms:send jms/test queue message"
argument_list|)
expr_stmt|;
name|output
operator|=
name|executeCommand
argument_list|(
literal|"jms:count jms/test queue"
argument_list|)
expr_stmt|;
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
literal|"1"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|output
operator|=
name|executeCommand
argument_list|(
literal|"jms:consume jms/test queue"
argument_list|)
expr_stmt|;
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
literal|"1 message"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|executeCommand
argument_list|(
literal|"jms:send test queue message"
argument_list|)
expr_stmt|;
name|output
operator|=
name|executeCommand
argument_list|(
literal|"jms:move test queue other"
argument_list|)
expr_stmt|;
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
literal|"1 message"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|output
operator|=
name|executeCommand
argument_list|(
literal|"jms:queues test"
argument_list|)
expr_stmt|;
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
literal|"queue"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"other"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|output
operator|=
name|executeCommand
argument_list|(
literal|"jms:browse test other"
argument_list|)
expr_stmt|;
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
literal|"queue"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"queue://other"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"jms:consume test other"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"jms:delete test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Add features repositories"
argument_list|)
expr_stmt|;
name|executeCommand
argument_list|(
literal|"feature:repo-add spring-legacy"
argument_list|)
expr_stmt|;
name|executeCommand
argument_list|(
literal|"feature:repo-add activemq 5.15.3"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"aries-blueprint"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Installing JMS feature"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"jms"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"pax-jms-activemq"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Installing ActiveMQ"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"shell-compat"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"activemq-broker-noweb"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|MBeanServer
name|mBeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|objectName
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=jms,name=root"
argument_list|)
decl_stmt|;
name|mBeanServer
operator|.
name|invoke
argument_list|(
name|objectName
argument_list|,
literal|"create"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"testMBean"
block|,
literal|"activemq"
block|,
literal|"tcp://localhost:61616"
block|,
literal|"karaf"
block|,
literal|"karaf"
block|,
literal|"transx"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|mBeanServer
operator|.
name|invoke
argument_list|(
name|objectName
argument_list|,
literal|"send"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"jms/testMBean"
block|,
literal|"queueMBean"
block|,
literal|"message"
block|,
literal|null
block|,
literal|"karaf"
block|,
literal|"karaf"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|Integer
name|count
init|=
operator|(
name|Integer
operator|)
name|mBeanServer
operator|.
name|invoke
argument_list|(
name|objectName
argument_list|,
literal|"count"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"jms/testMBean"
block|,
literal|"queueMBean"
block|,
literal|"karaf"
block|,
literal|"karaf"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|}
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|1
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|queues
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|mBeanServer
operator|.
name|invoke
argument_list|(
name|objectName
argument_list|,
literal|"queues"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"jms/testMBean"
block|,
literal|"karaf"
block|,
literal|"karaf"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|}
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|queues
operator|.
name|contains
argument_list|(
literal|"queueMBean"
argument_list|)
argument_list|)
expr_stmt|;
name|mBeanServer
operator|.
name|invoke
argument_list|(
name|objectName
argument_list|,
literal|"delete"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"testMBean"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

