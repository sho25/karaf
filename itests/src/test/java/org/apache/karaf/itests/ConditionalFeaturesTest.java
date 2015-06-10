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
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|ConditionalFeaturesTest
extends|extends
name|KarafTestSupport
block|{
comment|/*     @Inject     private FeaturesService featuresService;      @Inject     private BundleContext bundleContext;          @Inject     BootFinished bootFinished;       @ProbeBuilder     public TestProbeBuilder probeConfiguration(TestProbeBuilder probe) {         probe.setHeader(Constants.DYNAMICIMPORT_PACKAGE, "*,org.apache.felix.service.*;status=provisional");         return probe;     }       @Configuration     public Option[] config() {                  MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").type("zip").versionAsInProject();         return new Option[]{             karafDistributionConfiguration().frameworkUrl(karafUrl),             KarafDistributionOption.editConfigurationFilePut("etc/org.ops4j.pax.web.cfg", "org.osgi.service.http.port", KarafTestSupport.HTTP_PORT)         };     }     */
annotation|@
name|Test
specifier|public
name|void
name|testScr
parameter_list|()
throws|throws
name|Exception
block|{
comment|//Remove management and install scr
name|featureService
operator|.
name|uninstallFeature
argument_list|(
literal|"management"
argument_list|,
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
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"scr"
argument_list|,
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
argument_list|)
expr_stmt|;
name|assertBundleNotInstalled
argument_list|(
literal|"org.apache.karaf.scr.management"
argument_list|)
expr_stmt|;
comment|//Add management back
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"management"
argument_list|,
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
argument_list|)
expr_stmt|;
name|assertBundleInstalled
argument_list|(
literal|"org.apache.karaf.scr.management"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWebconsole
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|featureService
operator|.
name|uninstallFeature
argument_list|(
literal|"scr"
argument_list|,
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
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{         }
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"eventadmin"
argument_list|,
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
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
literal|"webconsole"
argument_list|,
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
argument_list|)
expr_stmt|;
name|assertBundleInstalled
argument_list|(
literal|"org.apache.karaf.webconsole.features"
argument_list|)
expr_stmt|;
name|assertBundleInstalled
argument_list|(
literal|"org.apache.karaf.webconsole.instance"
argument_list|)
expr_stmt|;
name|assertBundleInstalled
argument_list|(
literal|"org.apache.karaf.webconsole.gogo"
argument_list|)
expr_stmt|;
name|assertBundleInstalled
argument_list|(
literal|"org.apache.karaf.webconsole.http"
argument_list|)
expr_stmt|;
name|assertBundleInstalled
argument_list|(
literal|"org.apache.felix.webconsole.plugins.event"
argument_list|)
expr_stmt|;
comment|// add eventadmin
name|featureService
operator|.
name|uninstallFeature
argument_list|(
literal|"eventadmin"
argument_list|,
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
argument_list|)
expr_stmt|;
name|assertBundleNotInstalled
argument_list|(
literal|"org.apache.felix.webconsole.plugins.event"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

