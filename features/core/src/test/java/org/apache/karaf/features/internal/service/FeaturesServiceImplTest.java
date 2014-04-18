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
operator|.
name|internal
operator|.
name|service
package|;
end_package

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
name|assertSame
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
name|util
operator|.
name|Map
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
name|Feature
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
name|TestBase
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

begin_comment
comment|/**  * Test cases for {@link org.apache.karaf.features.internal.service.FeaturesServiceImpl}  */
end_comment

begin_class
specifier|public
class|class
name|FeaturesServiceImplTest
extends|extends
name|TestBase
block|{
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
annotation|@
name|Test
specifier|public
name|void
name|testGetFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|Feature
name|transactionFeature
init|=
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|>
name|features
init|=
name|features
argument_list|(
name|transactionFeature
argument_list|)
decl_stmt|;
specifier|final
name|FeaturesServiceImpl
name|impl
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
literal|""
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|>
name|getFeatures
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|features
return|;
block|}
block|}
decl_stmt|;
name|assertNotNull
argument_list|(
name|impl
operator|.
name|getFeature
argument_list|(
literal|"transaction"
argument_list|,
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
name|model
operator|.
name|Feature
operator|.
name|DEFAULT_VERSION
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|transactionFeature
argument_list|,
name|impl
operator|.
name|getFeature
argument_list|(
literal|"transaction"
argument_list|,
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
name|model
operator|.
name|Feature
operator|.
name|DEFAULT_VERSION
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFeatureStripVersion
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|FeaturesServiceImpl
name|impl
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
literal|""
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|>
name|getFeatures
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|features
argument_list|(
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Feature
name|feature
init|=
name|impl
operator|.
name|getFeature
argument_list|(
literal|"transaction"
argument_list|,
literal|"  1.0.0  "
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"transaction"
argument_list|,
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFeatureNotAvailable
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|FeaturesServiceImpl
name|impl
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
literal|""
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|>
name|getFeatures
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|features
argument_list|(
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|assertNull
argument_list|(
name|impl
operator|.
name|getFeature
argument_list|(
literal|"activemq"
argument_list|,
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
name|model
operator|.
name|Feature
operator|.
name|DEFAULT_VERSION
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFeatureHighestAvailable
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|>
name|features
init|=
name|features
argument_list|(
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|,
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"2.0.0"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|FeaturesServiceImpl
name|impl
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
literal|""
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|>
name|getFeatures
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|features
return|;
block|}
block|}
decl_stmt|;
name|assertNotNull
argument_list|(
name|impl
operator|.
name|getFeature
argument_list|(
literal|"transaction"
argument_list|,
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
name|model
operator|.
name|Feature
operator|.
name|DEFAULT_VERSION
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"2.0.0"
argument_list|,
name|impl
operator|.
name|getFeature
argument_list|(
literal|"transaction"
argument_list|,
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
name|model
operator|.
name|Feature
operator|.
name|DEFAULT_VERSION
argument_list|)
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This test ensures that every feature get installed only once, even if it appears multiple times in the list      * of transitive feature dependencies (KARAF-1600)      */
comment|/*     @Test     @SuppressWarnings("unchecked")     public void testNoDuplicateFeaturesInstallation() throws Exception {         final List<Feature> installed = new LinkedList<Feature>();         BundleManager bundleManager = EasyMock.createMock(BundleManager.class);         expect(bundleManager.installBundleIfNeeded(EasyMock.anyObject(String.class), EasyMock.anyInt(), EasyMock.anyObject(String.class)))             .andReturn(new BundleInstallerResult(createDummyBundle(1l, "", headers()), true)).anyTimes();         bundleManager.refreshBundles(EasyMock.anyObject(Set.class), EasyMock.anyObject(Set.class), EasyMock.anyObject(EnumSet.class));         EasyMock.expectLastCall();         final FeaturesServiceImpl impl = new FeaturesServiceImpl(bundleManager, null) {             // override methods which refers to bundle context to avoid mocking everything             @Override             protected boolean loadState() {                 return true;             }              @Override             protected void saveState() {              }              @Override             protected void doInstallFeature(InstallationState state, Feature feature, boolean verbose) throws Exception {                 installed.add(feature);                  super.doInstallFeature(state, feature, verbose);             }          };         replay(bundleManager);         impl.addRepository(getClass().getResource("repo2.xml").toURI());         impl.installFeature("all");          // copying the features to a set to filter out the duplicates         Set<Feature> noduplicates = new HashSet<Feature>();         noduplicates.addAll(installed);          assertEquals("Every feature should only have been installed once", installed.size(), noduplicates.size());     }      @Test     public void testGetOptionalImportsOnly() {         BundleManager bundleManager = new BundleManager(null, 0l);          List<Clause> result = bundleManager.getOptionalImports("org.apache.karaf,org.apache.karaf.optional;resolution:=optional");         assertEquals("One optional import expected", 1, result.size());         assertEquals("org.apache.karaf.optional", result.get(0).getName());          result = bundleManager.getOptionalImports(null);         assertNotNull(result);         assertEquals("No optional imports expected", 0, result.size());     }     */
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

