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
name|PerClass
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

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|net
operator|.
name|Socket
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
name|util
operator|.
name|List
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
annotation|@
name|Before
specifier|public
name|void
name|installJmsFeatureAndActiveMQBroker
parameter_list|()
throws|throws
name|Exception
block|{
name|installAndAssertFeature
argument_list|(
literal|"jms"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|addRepository
argument_list|(
operator|new
name|URI
argument_list|(
literal|"mvn:org.apache.activemq/activemq-karaf/5.10.0/xml/features"
argument_list|)
argument_list|)
expr_stmt|;
name|installAndAssertFeature
argument_list|(
literal|"activemq-broker-noweb"
argument_list|)
expr_stmt|;
comment|// check if ActiveMQ is completely started
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Waiting for the ActiveMQ transport connector on 61616 ..."
argument_list|)
expr_stmt|;
name|boolean
name|bound
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|bound
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|Socket
name|socket
init|=
operator|new
name|Socket
argument_list|(
literal|"localhost"
argument_list|,
literal|61616
argument_list|)
decl_stmt|;
name|bound
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// wait the connection
block|}
block|}
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|120000
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
literal|"===>testCommands"
argument_list|)
expr_stmt|;
comment|// jms:create command
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"jms:create -t ActiveMQ -u karaf -p karaf --url tcp://localhost:61616 test"
argument_list|)
argument_list|)
expr_stmt|;
comment|// give time to fileinstall to load the blueprint file by looking for the connection factory OSGi service
name|getOsgiService
argument_list|(
name|ConnectionFactory
operator|.
name|class
argument_list|,
literal|"name=test"
argument_list|,
literal|30000
argument_list|)
expr_stmt|;
comment|// jms:connectionfactories command
name|String
name|connectionFactories
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
name|connectionFactories
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"jms/test"
argument_list|,
name|connectionFactories
argument_list|)
expr_stmt|;
comment|// jms:info command
name|String
name|info
init|=
name|executeCommand
argument_list|(
literal|"jms:info test"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"ActiveMQ"
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"5.10.0"
argument_list|,
name|info
argument_list|)
expr_stmt|;
comment|// jms:send command
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"jms:send test queue message"
argument_list|)
argument_list|)
expr_stmt|;
comment|// jms:count command
name|String
name|count
init|=
name|executeCommand
argument_list|(
literal|"jms:count test queue"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"1"
argument_list|,
name|count
argument_list|)
expr_stmt|;
comment|// jms:consume command
name|String
name|consumed
init|=
name|executeCommand
argument_list|(
literal|"jms:consume test queue"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|consumed
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"1 message"
argument_list|,
name|consumed
argument_list|)
expr_stmt|;
comment|// jms:send& jms:move commands
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|executeCommand
argument_list|(
literal|"jms:send test queue message"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|move
init|=
name|executeCommand
argument_list|(
literal|"jms:move test queue other"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|move
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"1 message"
argument_list|,
name|move
argument_list|)
expr_stmt|;
comment|// jms:queues command
name|String
name|queues
init|=
name|executeCommand
argument_list|(
literal|"jms:queues test"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|queues
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"queue"
argument_list|,
name|queues
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"other"
argument_list|,
name|queues
argument_list|)
expr_stmt|;
comment|// jms:browse command
name|String
name|browse
init|=
name|executeCommand
argument_list|(
literal|"jms:browse test other"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|browse
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"message"
argument_list|,
name|browse
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"queue://other"
argument_list|,
name|browse
argument_list|)
expr_stmt|;
comment|// jms:consume command
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
comment|// jms:delete command
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
comment|// jms:connectionfactories command
name|connectionFactories
operator|=
name|executeCommand
argument_list|(
literal|"jms:connectionfactories"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|connectionFactories
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|120000
argument_list|)
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
literal|"===>testMBean"
argument_list|)
expr_stmt|;
name|MBeanServer
name|mbeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=jms,name=root"
argument_list|)
decl_stmt|;
comment|// create operation
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"JMS MBean create operation invocation"
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
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
block|}
argument_list|)
expr_stmt|;
comment|// give time to fileinstall to load the blueprint file by looking for the connection factory OSGi service
name|getOsgiService
argument_list|(
name|ConnectionFactory
operator|.
name|class
argument_list|,
literal|"name=testMBean"
argument_list|,
literal|30000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|connectionFactories
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"Connectionfactories"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|connectionFactories
operator|.
name|size
argument_list|()
operator|>=
literal|1
argument_list|)
expr_stmt|;
comment|// send operation
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"JMS MBean send operation invocation"
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"send"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"testMBean"
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
comment|// count operation
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"JMS MBean count operation invocation"
argument_list|)
expr_stmt|;
name|Integer
name|count
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"count"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"testMBean"
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// queues operation
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"JMS MBean queues operation invocation: "
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
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"queues"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"testMBean"
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|queues
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queues
operator|.
name|size
argument_list|()
operator|>=
literal|1
argument_list|)
expr_stmt|;
comment|// delete operation
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"JMS MBean delete operation invocation"
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
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

