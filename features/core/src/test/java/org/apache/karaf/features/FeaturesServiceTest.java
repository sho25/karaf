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
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|reset
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
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
name|assertNull
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
name|fail
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|net
operator|.
name|MalformedURLException
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
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
name|internal
operator|.
name|service
operator|.
name|FeaturesServiceImpl
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
name|internal
operator|.
name|service
operator|.
name|StateStorage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
name|BundleContext
import|;
end_import

begin_class
specifier|public
class|class
name|FeaturesServiceTest
extends|extends
name|TestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FEATURE_WITH_INVALID_BUNDLE
init|=
literal|"<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"
operator|+
literal|"<feature name='f1'><bundle>%s</bundle><bundle>zfs:unknown</bundle></feature>"
operator|+
literal|"<feature name='f2'><bundle>%s</bundle></feature>"
operator|+
literal|"</features>"
decl_stmt|;
name|File
name|dataFile
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|IOException
block|{
name|dataFile
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"features"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|URI
name|createTempRepo
parameter_list|(
name|String
name|repoContent
parameter_list|,
name|Object
modifier|...
name|variables
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|tmp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"karaf"
argument_list|,
literal|".feature"
argument_list|)
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|tmp
argument_list|)
argument_list|)
decl_stmt|;
name|pw
operator|.
name|printf
argument_list|(
name|repoContent
argument_list|,
name|variables
argument_list|)
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|tmp
operator|.
name|toURI
argument_list|()
return|;
block|}
comment|/*        TODO: migrate those tests      @Test     public void testInstallFeature() throws Exception {         URI uri = createTempRepo(                 "<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"                  + "<feature name='f1'><bundle start='true'>bundle-f1</bundle></feature>"                 + "</features>");          BundleManager bundleManager = EasyMock.createMock(BundleManager.class);         Bundle installedBundle = createDummyBundle(12345L, "bundle-f1", headers());         FeaturesServiceImpl svc = testAddRepository("bundle-f1", uri, bundleManager, installedBundle);                  reset(bundleManager);                  expect(bundleManager.installBundleIfNeeded(eq("bundle-f1"), eq(0), eq((String)null))).andReturn(new BundleInstallerResult(installedBundle, true));         expect(bundleManager.getDataFile(EasyMock.anyObject(String.class))).andReturn(dataFile);         ignoreRefreshes(bundleManager);         replay(bundleManager);         svc.installFeature("f1", org.apache.karaf.features.internal.model.Feature.DEFAULT_VERSION, EnumSet.of(FeaturesService.Option.NoAutoRefreshBundles));         verify(bundleManager);                  Feature[] installed = svc.listInstalledFeatures();         assertEquals(1, installed.length);         assertEquals("f1", installed[0].getName());     }      private FeaturesServiceImpl testAddRepository(String name, URI uri, BundleManager bundleManager,             Bundle installedBundle) throws IOException, BundleException, Exception {         expect(bundleManager.getDataFile(EasyMock.anyObject(String.class))).andReturn(dataFile);         expect(bundleManager.installBundleIfNeeded(eq(name), eq(0), eq((String)null))).andReturn(new BundleInstallerResult(installedBundle, true)).anyTimes();          replay(bundleManager);         FeaturesServiceImpl svc = new FeaturesServiceImpl(bundleManager);         svc.addRepository(uri);         Repository[] repositories = svc.listRepositories();         verify(bundleManager);          assertNotNull(repositories);         assertEquals(1, repositories.length);         assertNotNull(repositories[0]);         Feature[] features = repositories[0].getFeatures();         assertNotNull(features);         assertEquals(1, features.length);         assertNotNull(features[0]);         assertEquals("f1", features[0].getName());         assertNotNull(features[0].getDependencies());         assertEquals(0, features[0].getDependencies().size());         assertNotNull(features[0].getBundles());         assertEquals(1, features[0].getBundles().size());         assertEquals(name, features[0].getBundles().get(0).getLocation());         assertTrue(features[0].getBundles().get(0).isStart());         return svc;     }      @SuppressWarnings("unchecked")     @Test     public void testUninstallFeatureWithTwoVersions() throws Exception {         URI uri  = createTempRepo("<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"                 + "<feature name='f1' version='0.1'><bundle>bundle-0.1</bundle></feature>"                 + "<feature name='f1' version='0.2'><bundle>bundle-0.1</bundle></feature>"                  + "</features>");          Bundle bundlef101 = createDummyBundle(12345L, "bundle-0.1", headers());          BundleManager bundleManager = EasyMock.createMock(BundleManager.class);         BundleContext bundleContext = EasyMock.createMock(BundleContext.class);         expect(bundleManager.getDataFile(EasyMock.anyObject(String.class))).andReturn(dataFile).anyTimes();         expect(bundleManager.installBundleIfNeeded("bundle-0.1", 0, null)).andReturn(new BundleInstallerResult(bundlef101, true));         expect(bundleManager.installBundleIfNeeded("bundle-0.1", 0, null)).andReturn(new BundleInstallerResult(bundlef101, false));         expect(bundleManager.getBundleContext()).andReturn(bundleContext);         ignoreRefreshes(bundleManager);         bundleManager.uninstall(Collections.EMPTY_LIST, true);         EasyMock.expectLastCall().times(2);                           replay(bundleManager);         FeaturesServiceImpl svc = new FeaturesServiceImpl(bundleManager);         svc.addRepository(uri);          try {             svc.uninstallFeature("f1");             fail("Uninstall should have failed as feature is not installed");         } catch (Exception e) {             // ok         }          svc.installFeature("f1", "0.1", EnumSet.of(FeaturesService.Option.NoAutoRefreshBundles));         svc.installFeature("f1", "0.2", EnumSet.of(FeaturesService.Option.NoAutoRefreshBundles));          try {             svc.uninstallFeature("f1");             fail("Uninstall should have failed as feature is installed in multiple versions");         } catch (Exception e) {             // ok         }          svc.uninstallFeature("f1", "0.1");         svc.uninstallFeature("f1");         verify(bundleManager);     }              @Test     public void testAddAndRemoveRepository() throws Exception {         URI uri = createTempRepo("<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"                 + "<feature name='f1' version='0.1'><bundle>bundle-f1-0.1</bundle></feature>"                 + "</features>");          BundleManager bundleManager = EasyMock.createMock(BundleManager.class);         expect(bundleManager.getDataFile(EasyMock.<String>anyObject())).andReturn(dataFile).anyTimes();          replay(bundleManager);         FeaturesServiceImpl svc = new FeaturesServiceImpl(bundleManager);         EasyMock.verify(bundleManager);          svc.addRepository(uri);                                                              svc.removeRepository(uri);         verify(bundleManager);     }      // Tests install of a Repository that includes a feature     // with a feature dependency     // The dependant feature is in the same repository     // Tests uninstall of features     @SuppressWarnings("unchecked")     @Test     public void testInstallFeatureWithDependantFeatures() throws Exception {         URI uri = createTempRepo("<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"                                  + "<feature name='f1' version='0.1'><feature version='0.1'>f2</feature><bundle>bundle-f1-0.1</bundle></feature>"                                  + "<feature name='f2' version='0.1'><bundle>bundle-f2-0.1</bundle></feature>"                                  + "</features>");          BundleManager bundleManager = EasyMock.createMock(BundleManager.class);         BundleContext bundleContext = EasyMock.createMock(BundleContext.class);         Bundle bundlef101 = createDummyBundle(12345L, "bundle-f1-0.1", headers());         Bundle bundlef201 = createDummyBundle(54321L, "bundle-f2-0.1", headers());         expect(bundleManager.getDataFile(EasyMock.<String> anyObject())).andReturn(dataFile).anyTimes();         expect(bundleManager.installBundleIfNeeded("bundle-f1-0.1", 0, null))             .andReturn(new BundleInstallerResult(bundlef101, true));         expect(bundleManager.installBundleIfNeeded("bundle-f2-0.1", 0, null))             .andReturn(new BundleInstallerResult(bundlef201, true));         expect(bundleManager.getBundleContext()).andReturn(bundleContext).anyTimes();         expect(bundleContext.getBundle(12345)).andReturn(bundlef101).anyTimes();         ignoreRefreshes(bundleManager);         bundleManager.uninstall(Collections.EMPTY_LIST, true);                 EasyMock.expectLastCall().anyTimes();         replay(bundleManager);                  FeaturesServiceImpl svc = new FeaturesServiceImpl(bundleManager);         svc.addRepository(uri);         svc.installFeature("f1", "0.1");         svc.uninstallFeature("f1", "0.1");         verify(bundleManager);      }      @SuppressWarnings("unchecked")     private BundleManager prepareBundleManagerForInstallUninstall(String bundleUri, String bundlename) throws Exception {         BundleManager bundleManager = EasyMock.createMock(BundleManager.class);         BundleContext bundleContext = EasyMock.createMock(BundleContext.class);         Bundle installedBundle = createDummyBundle(12345L, bundlename, headers());         expect(bundleManager.getDataFile(EasyMock.<String>anyObject())).andReturn(dataFile).anyTimes();         expect(bundleManager.installBundleIfNeeded(bundleUri, 0, null)).andReturn(new BundleInstallerResult(installedBundle, true));         expect(bundleManager.getBundleContext()).andReturn(bundleContext);         ignoreRefreshes(bundleManager);         bundleManager.uninstall(Collections.EMPTY_LIST, true);         EasyMock.expectLastCall().times(2);         return bundleManager;     }      @Test     public void testInstallFeatureWithDependantFeaturesAndVersionWithoutPreinstall() throws Exception {         URI uri = createTempRepo("<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"                 + "<feature name='f1' version='0.1'><feature version='0.1'>f2</feature></feature>"                 + "<feature name='f2' version='0.1'><bundle>bundle-0.1</bundle></feature>"                 + "<feature name='f2' version='0.2'><bundle>bundle-0.2</bundle></feature>"                 + "</features>");          BundleManager bundleManager = prepareBundleManagerForInstallUninstall("bundle-0.1", "bundle-0.1");          replay(bundleManager);         FeaturesServiceImpl svc = new FeaturesServiceImpl(bundleManager);         svc.addRepository(uri);         svc.installFeature("f1", "0.1");         svc.uninstallFeature("f1", "0.1");         svc.uninstallFeature("f2", "0.1");         verify(bundleManager);     }      @Test     public void testInstallFeatureWithDependantFeaturesAndNoVersionWithoutPreinstall() throws Exception {         URI uri = createTempRepo("<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"                 + "<feature name='f1' version='0.1'><feature>f2</feature></feature>"                 + "<feature name='f2' version='0.1'><bundle>bundle-0.1</bundle></feature>"                 + "<feature name='f2' version='0.2'><bundle>bundle-0.2</bundle></feature>"                 + "</features>");          BundleManager bundleManager = prepareBundleManagerForInstallUninstall("bundle-0.2", "bundle-0.2");          replay(bundleManager);         FeaturesServiceImpl svc = new FeaturesServiceImpl(bundleManager);         svc.addRepository(uri);         svc.installFeature("f1", "0.1");         svc.uninstallFeature("f1", "0.1");         svc.uninstallFeature("f2", "0.2");         verify(bundleManager);     }      @SuppressWarnings("unchecked")     @Test     public void testInstallFeatureWithDependantFeaturesAndRangeWithoutPreinstall() throws Exception {         URI uri = createTempRepo("<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"                 + "<feature name='f1' version='0.1'><feature version='[0.1,0.3)'>f2</feature></feature>"                 + "<feature name='f2' version='0.1'><bundle>bundle-0.1</bundle></feature>"                 + "<feature name='f2' version='0.2'><bundle>bundle-0.2</bundle></feature>"                 + "</features>");          BundleManager bundleManager = EasyMock.createMock(BundleManager.class);         BundleContext bundleContext = EasyMock.createMock(BundleContext.class);         Bundle bundleVer02 = createDummyBundle(54321L, "bundleVer02", headers());         expect(bundleManager.getDataFile(EasyMock.<String>anyObject())).andReturn(dataFile).anyTimes();         expect(bundleManager.installBundleIfNeeded("bundle-0.2", 0, null)).andReturn(new BundleInstallerResult(bundleVer02, true));         expect(bundleManager.getBundleContext()).andReturn(bundleContext);         ignoreRefreshes(bundleManager);         bundleManager.uninstall(Collections.EMPTY_LIST, true);          EasyMock.expectLastCall().times(2);          replay(bundleManager);         FeaturesServiceImpl svc = new FeaturesServiceImpl(bundleManager);         svc.addRepository(uri);         svc.installFeature("f1", "0.1");         svc.uninstallFeature("f1", "0.1");         svc.uninstallFeature("f2", "0.2");         verify(bundleManager);     }      @SuppressWarnings("unchecked")     @Test     public void testInstallFeatureWithDependantFeaturesAndRangeWithPreinstall() throws Exception {         String bundleVer01Uri = "bundle-0.1";         String bundleVer02Uri = "bundle-0.2";          URI uri = createTempRepo("<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"                 + "<feature name='f1' version='0.1'><feature version='[0.1,0.3)'>f2</feature></feature>"                 + "<feature name='f2' version='0.1'><bundle>%s</bundle></feature>"                 + "<feature name='f2' version='0.2'><bundle>%s</bundle></feature>"                 + "</features>", bundleVer01Uri, bundleVer02Uri);                  BundleContext bundleContext = EasyMock.createMock(BundleContext.class);         expect(bundleContext.getBundles()).andReturn(new Bundle[0]);         replay(bundleContext);          FeaturesServiceImpl svc = new FeaturesServiceImpl(null, bundleContext, new Storage(), null, null, null, null);         svc.addRepository(uri);         svc.installFeature("f2", "0.1");         svc.installFeature("f1", "0.1");         svc.uninstallFeature("f1", "0.1");         svc.uninstallFeature("f2", "0.1");          verify(bundleContext);     }     */
annotation|@
name|Test
specifier|public
name|void
name|testGetFeaturesShouldHandleDifferentVersionPatterns
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
name|createTempRepo
argument_list|(
literal|"<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"
operator|+
literal|"<feature name='f1' version='0.1'><feature version='[0.1,0.3)'>f2</feature></feature>"
operator|+
literal|"<feature name='f2' version='0.1'><bundle>bundle1</bundle></feature>"
operator|+
literal|"<feature name='f2' version='0.2'><bundle>bundle2</bundle></feature>"
operator|+
literal|"</features>"
argument_list|)
decl_stmt|;
name|FeaturesServiceImpl
name|svc
init|=
operator|new
name|FeaturesServiceImpl
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|Storage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|svc
operator|.
name|addRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|feature
argument_list|(
literal|"f2"
argument_list|,
literal|"0.2"
argument_list|)
argument_list|,
name|svc
operator|.
name|getFeature
argument_list|(
literal|"f2"
argument_list|,
literal|"[0.1,0.3)"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|feature
argument_list|(
literal|"f2"
argument_list|,
literal|"0.2"
argument_list|)
argument_list|,
name|svc
operator|.
name|getFeature
argument_list|(
literal|"f2"
argument_list|,
literal|"0.0.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|feature
argument_list|(
literal|"f2"
argument_list|,
literal|"0.2"
argument_list|)
argument_list|,
name|svc
operator|.
name|getFeature
argument_list|(
literal|"f2"
argument_list|,
literal|"0.2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|svc
operator|.
name|getFeature
argument_list|(
literal|"f2"
argument_list|,
literal|"0.3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInstallBatchFeatureWithFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|bundle1Uri
init|=
literal|"file:bundle1"
decl_stmt|;
name|String
name|bundle2Uri
init|=
literal|"file:bundle2"
decl_stmt|;
name|URI
name|uri
init|=
name|createTempRepo
argument_list|(
name|FEATURE_WITH_INVALID_BUNDLE
argument_list|,
name|bundle1Uri
argument_list|,
name|bundle2Uri
argument_list|)
decl_stmt|;
name|BundleContext
name|bundleContext
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|bundleContext
operator|.
name|getBundles
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Bundle
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|FeaturesServiceImpl
name|svc
init|=
operator|new
name|FeaturesServiceImpl
argument_list|(
literal|null
argument_list|,
name|bundleContext
argument_list|,
operator|new
name|Storage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|svc
operator|.
name|addRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
try|try
block|{
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|svc
operator|.
name|listFeatures
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|svc
operator|.
name|installFeatures
argument_list|(
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|Feature
argument_list|>
argument_list|(
name|features
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Call should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{         }
name|verify
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
block|}
comment|/**      * This test checks schema validation of submited uri.      */
annotation|@
name|Test
specifier|public
name|void
name|testSchemaValidation
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
name|createTempRepo
argument_list|(
literal|"<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"
operator|+
literal|"<featur><bundle>somebundle</bundle></featur></features>"
argument_list|)
decl_stmt|;
name|FeaturesServiceImpl
name|svc
init|=
operator|new
name|FeaturesServiceImpl
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|Storage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|svc
operator|.
name|addRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"exception expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unable to validate"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This test checks feature service behavior with old, non namespaced descriptor.      */
annotation|@
name|Test
specifier|public
name|void
name|testLoadOldFeatureFile
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
name|createTempRepo
argument_list|(
literal|"<features name='test' xmlns='http://karaf.apache.org/xmlns/features/v1.0.0'>"
operator|+
literal|"<feature name='f1'><bundle>file:bundle1</bundle><bundle>file:bundle2</bundle></feature>"
operator|+
literal|"</features>"
argument_list|)
decl_stmt|;
name|FeaturesServiceImpl
name|svc
init|=
operator|new
name|FeaturesServiceImpl
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|Storage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|svc
operator|.
name|addRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|Feature
name|feature
init|=
name|svc
operator|.
name|getFeature
argument_list|(
literal|"f1"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"No feature named fi found"
argument_list|,
name|feature
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|bundles
init|=
name|feature
operator|.
name|getBundles
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bundles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|Storage
extends|extends
name|StateStorage
block|{
annotation|@
name|Override
specifier|protected
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|OutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

