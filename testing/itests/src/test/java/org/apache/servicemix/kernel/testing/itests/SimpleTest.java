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
name|servicemix
operator|.
name|kernel
operator|.
name|testing
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
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLInputFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|kernel
operator|.
name|testing
operator|.
name|support
operator|.
name|AbstractIntegrationTest
import|;
end_import

begin_class
specifier|public
class|class
name|SimpleTest
extends|extends
name|AbstractIntegrationTest
block|{
specifier|private
name|Properties
name|dependencies
decl_stmt|;
comment|/** 	 * The manifest to use for the "virtual bundle" created 	 * out of the test classes and resources in this project 	 * 	 * This is actually the boilerplate manifest with one additional 	 * import-package added. We should provide a simpler customization 	 * point for such use cases that doesn't require duplication 	 * of the entire manifest... 	 */
specifier|protected
name|String
name|getManifestLocation
parameter_list|()
block|{
return|return
literal|"classpath:org/apache/servicemix/MANIFEST.MF"
return|;
block|}
comment|/** 	 * The location of the packaged OSGi bundles to be installed 	 * for this test. Values are Spring resource paths. The bundles 	 * we want to use are part of the same multi-project maven 	 * build as this project is. Hence we use the localMavenArtifact 	 * helper method to find the bundles produced by the package 	 * phase of the maven build (these tests will run after the 	 * packaging phase, in the integration-test phase). 	 * 	 * JUnit, commons-logging, spring-core and the spring OSGi 	 * test bundle are automatically included so do not need 	 * to be specified here. 	 */
specifier|protected
name|String
index|[]
name|getTestBundlesNames
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
name|getBundle
argument_list|(
literal|"org.apache.servicemix.specs"
argument_list|,
literal|"org.apache.servicemix.specs.stax-api-1.0"
argument_list|)
block|, 		}
return|;
block|}
specifier|public
name|void
name|testWoodstox
parameter_list|()
throws|throws
name|Exception
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|XMLInputFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|XMLInputFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|XMLInputFactory
name|factory
init|=
literal|null
decl_stmt|;
try|try
block|{
name|factory
operator|=
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Factory should not have been found"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertNull
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|installBundle
argument_list|(
literal|"org.apache.servicemix.bundles"
argument_list|,
literal|"org.apache.servicemix.bundles.woodstox"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

