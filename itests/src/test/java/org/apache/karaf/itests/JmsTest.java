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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|both
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|containsString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|hasItem
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
name|assertThat
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
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|composite
import|;
end_import

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
name|CoreOptions
operator|.
name|maven
import|;
end_import

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
name|features
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
name|UnknownHostException
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
name|AttributeNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|InstanceNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanException
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
name|javax
operator|.
name|management
operator|.
name|ReflectionException
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
name|options
operator|.
name|MavenArtifactUrlReference
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
name|String
name|JMX_CF_NAME
init|=
literal|"testMBean"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JMX_QUEUE_NAME
init|=
literal|"queueMBean"
decl_stmt|;
specifier|private
name|MBeanServer
name|mbeanServer
decl_stmt|;
specifier|private
name|ObjectName
name|objName
decl_stmt|;
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|config
parameter_list|()
block|{
name|MavenArtifactUrlReference
name|activeMqUrl
init|=
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.activemq"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"activemq-karaf"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
operator|.
name|type
argument_list|(
literal|"xml"
argument_list|)
operator|.
name|classifier
argument_list|(
literal|"features"
argument_list|)
decl_stmt|;
return|return
operator|new
name|Option
index|[]
comment|//
block|{
name|composite
argument_list|(
name|super
operator|.
name|config
argument_list|()
argument_list|)
block|,
comment|//
name|features
argument_list|(
name|activeMqUrl
argument_list|,
literal|"jms"
argument_list|,
literal|"activemq-broker-noweb"
argument_list|)
block|}
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|await
argument_list|(
literal|"ActiveMQ transport up"
argument_list|)
operator|.
name|atMost
argument_list|(
literal|30
argument_list|,
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
name|jmsTransportPresent
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
expr_stmt|;
name|objName
operator|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=jms,name=root"
argument_list|)
expr_stmt|;
block|}
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
name|execute
argument_list|(
literal|"jms:create -t ActiveMQ -u karaf -p karaf --url tcp://localhost:61616 test"
argument_list|)
expr_stmt|;
name|waitForConnectionFactory
argument_list|(
literal|"name=test"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|execute
argument_list|(
literal|"jms:connectionfactories"
argument_list|)
argument_list|,
name|containsString
argument_list|(
literal|"jms/test"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|execute
argument_list|(
literal|"jms:info test"
argument_list|)
argument_list|,
name|both
argument_list|(
name|containsString
argument_list|(
literal|"ActiveMQ"
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|containsString
argument_list|(
literal|"5.14.4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|execute
argument_list|(
literal|"jms:send test queue message"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|execute
argument_list|(
literal|"jms:count test queue"
argument_list|)
argument_list|,
name|containsString
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|execute
argument_list|(
literal|"jms:consume test queue"
argument_list|)
argument_list|,
name|containsString
argument_list|(
literal|"1 message"
argument_list|)
argument_list|)
expr_stmt|;
name|execute
argument_list|(
literal|"jms:send test queue message"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|execute
argument_list|(
literal|"jms:move test queue other"
argument_list|)
argument_list|,
name|containsString
argument_list|(
literal|"1 message"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|execute
argument_list|(
literal|"jms:queues test"
argument_list|)
argument_list|,
name|both
argument_list|(
name|containsString
argument_list|(
literal|"queue"
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|containsString
argument_list|(
literal|"other"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|execute
argument_list|(
literal|"jms:browse test other"
argument_list|)
argument_list|,
name|both
argument_list|(
name|containsString
argument_list|(
literal|"queue"
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|containsString
argument_list|(
literal|"queue://other"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|execute
argument_list|(
literal|"jms:consume test other"
argument_list|)
expr_stmt|;
name|execute
argument_list|(
literal|"jms:delete test"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|60000
argument_list|)
specifier|public
name|void
name|testMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|checkJMXCreateConnectionFactory
argument_list|()
expr_stmt|;
name|invoke
argument_list|(
literal|"send"
argument_list|,
name|JMX_CF_NAME
argument_list|,
name|JMX_QUEUE_NAME
argument_list|,
literal|"message"
argument_list|,
literal|null
argument_list|,
literal|"karaf"
argument_list|,
literal|"karaf"
argument_list|)
expr_stmt|;
name|Integer
name|count
init|=
name|invoke
argument_list|(
literal|"count"
argument_list|,
name|JMX_CF_NAME
argument_list|,
name|JMX_QUEUE_NAME
argument_list|,
literal|"karaf"
argument_list|,
literal|"karaf"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Queue size> 0"
argument_list|,
name|count
operator|>
literal|0
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|queues
init|=
name|invoke
argument_list|(
literal|"queues"
argument_list|,
name|JMX_CF_NAME
argument_list|,
literal|"karaf"
argument_list|,
literal|"karaf"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|queues
argument_list|,
name|hasItem
argument_list|(
name|JMX_QUEUE_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|invoke
argument_list|(
literal|"delete"
argument_list|,
name|JMX_CF_NAME
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|jmsTransportPresent
parameter_list|()
throws|throws
name|UnknownHostException
throws|,
name|IOException
block|{
try|try
init|(
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
init|)
block|{
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|String
name|execute
parameter_list|(
name|String
name|command
parameter_list|)
block|{
name|String
name|output
init|=
name|executeCommand
argument_list|(
name|command
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
return|return
name|output
return|;
block|}
specifier|private
name|void
name|checkJMXCreateConnectionFactory
parameter_list|()
throws|throws
name|Exception
throws|,
name|AttributeNotFoundException
throws|,
name|MBeanException
throws|,
name|InstanceNotFoundException
throws|,
name|ReflectionException
block|{
name|invoke
argument_list|(
literal|"create"
argument_list|,
name|JMX_CF_NAME
argument_list|,
literal|"activemq"
argument_list|,
literal|"tcp://localhost:61616"
argument_list|,
literal|"karaf"
argument_list|,
literal|"karaf"
argument_list|)
expr_stmt|;
name|waitForConnectionFactory
argument_list|(
literal|"name="
operator|+
name|JMX_CF_NAME
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|objName
argument_list|,
literal|"Connectionfactories"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|connectionFactories
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|invoke
parameter_list|(
name|String
name|operationName
parameter_list|,
name|String
modifier|...
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|types
init|=
operator|new
name|String
index|[
name|parameters
operator|.
name|length
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|types
argument_list|,
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking jmx call "
operator|+
name|operationName
argument_list|)
expr_stmt|;
return|return
operator|(
name|T
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|objName
argument_list|,
name|operationName
argument_list|,
name|parameters
argument_list|,
name|types
argument_list|)
return|;
block|}
comment|/**      * Give fileinstall some time to load the blueprint file by looking for the connection factory OSGi      * service      */
specifier|private
name|ConnectionFactory
name|waitForConnectionFactory
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|ConnectionFactory
operator|.
name|class
argument_list|,
name|filter
argument_list|,
literal|30000
argument_list|)
return|;
block|}
block|}
end_class

end_unit

