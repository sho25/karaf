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
name|itests
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
name|FeaturesService
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
name|itests
operator|.
name|util
operator|.
name|RunIfRule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|MavenUtils
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
name|replaceConfigurationFile
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
name|XATest
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
name|Rule
specifier|public
name|RunIfRule
name|rule
init|=
operator|new
name|RunIfRule
argument_list|()
decl_stmt|;
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|config
parameter_list|()
block|{
name|String
name|version
init|=
name|MavenUtils
operator|.
name|getArtifactVersion
argument_list|(
literal|"org.apache.karaf"
argument_list|,
literal|"apache-karaf"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Option
argument_list|>
name|result
init|=
operator|new
name|LinkedList
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
name|result
operator|.
name|add
argument_list|(
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.features.cfg"
argument_list|,
literal|"featuresRepositories"
argument_list|,
literal|"mvn:org.apache.karaf.features/framework/"
operator|+
name|version
operator|+
literal|"/xml/features, "
operator|+
literal|"mvn:org.apache.karaf.features/enterprise/"
operator|+
name|version
operator|+
literal|"/xml/features, "
operator|+
literal|"mvn:org.apache.karaf.features/spring-legacy/"
operator|+
name|version
operator|+
literal|"/xml/features, "
operator|+
literal|"mvn:org.apache.karaf.features/standard/"
operator|+
name|version
operator|+
literal|"/xml/features, "
operator|+
literal|"mvn:org.apache.activemq/artemis-features/2.6.0/xml/features, "
operator|+
literal|"mvn:org.apache.camel.karaf/apache-camel/2.20.1/xml/features"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.features.cfg"
argument_list|,
literal|"featuresBoot"
argument_list|,
literal|"instance,package,log,ssh,framework,system,eventadmin,feature,shell,management,service,jaas,deployer,diagnostic,wrap,bundle,config,kar,aries-blueprint,artemis,jms,pax-jms-artemis"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|replaceConfigurationFile
argument_list|(
literal|"etc/org.ops4j.connectionfactory-artemis.cfg"
argument_list|,
name|getConfigFile
argument_list|(
literal|"/org/apache/karaf/itests/features/org.ops4j.connectionfactory-artemis.cfg"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|replaceConfigurationFile
argument_list|(
literal|"etc/org.ops4j.datasource-derby.cfg"
argument_list|,
name|getConfigFile
argument_list|(
literal|"/org/apache/karaf/itests/features/org.ops4j.datasource-derby.cfg"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|replaceConfigurationFile
argument_list|(
literal|"etc/xa-test-camel.xml"
argument_list|,
name|getConfigFile
argument_list|(
literal|"/org/apache/karaf/itests/features/xa-test-camel.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|Option
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
comment|//wait and until artemis server up
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"jms:info artemis"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Installing Derby"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"jdbc"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"pax-jdbc-derby"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"pax-jdbc-pool-transx"
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
literal|" "
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
literal|"jdbc:ds-list"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Install Camel route"
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"camel-blueprint"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"camel-sql"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"camel-jms"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"transaction-manager-narayana"
argument_list|,
name|NO_AUTO_REFRESH
argument_list|)
expr_stmt|;
name|Bundle
name|bundle
init|=
name|bundleContext
operator|.
name|installBundle
argument_list|(
literal|"blueprint:file:etc/xa-test-camel.xml"
argument_list|)
decl_stmt|;
name|bundle
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
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
literal|"camel:route-list"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Creating tables in Derby"
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
literal|"jdbc:execute derby CREATE TABLE messages (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY, message VARCHAR(1024) NOT NULL, CONSTRAINT primary_key PRIMARY KEY (id))"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"== Sending a message in Artemis broker that should be consumed by Camel route and inserted into the Derby database"
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
literal|"jms:send artemis MyQueue 'the-message'"
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|String
name|output
init|=
name|executeCommand
argument_list|(
literal|"jdbc:query derby select * from messages"
argument_list|)
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"the-message"
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

