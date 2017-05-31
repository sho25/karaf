begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|blueprint
operator|.
name|jasypt
operator|.
name|handler
package|;
end_package

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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarInputStream
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|connect
operator|.
name|PojoServiceRegistryFactoryImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|connect
operator|.
name|launch
operator|.
name|BundleDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|connect
operator|.
name|launch
operator|.
name|ClasspathScanner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|connect
operator|.
name|launch
operator|.
name|PojoServiceRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|connect
operator|.
name|launch
operator|.
name|PojoServiceRegistryFactory
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
name|jaas
operator|.
name|blueprint
operator|.
name|jasypt
operator|.
name|handler
operator|.
name|beans
operator|.
name|Bean
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
name|util
operator|.
name|StreamUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jasypt
operator|.
name|encryption
operator|.
name|pbe
operator|.
name|StandardPBEStringEncryptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jasypt
operator|.
name|encryption
operator|.
name|pbe
operator|.
name|config
operator|.
name|EnvironmentStringPBEConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundle
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Filter
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
name|FrameworkUtil
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
name|InvalidSyntaxException
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
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
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
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
operator|.
name|bundle
import|;
end_import

begin_class
specifier|public
class|class
name|EncryptablePropertyPlaceholderTest
extends|extends
name|TestCase
block|{
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_TIMEOUT
init|=
literal|30000
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|StandardPBEStringEncryptor
name|enc
init|=
operator|new
name|StandardPBEStringEncryptor
argument_list|()
decl_stmt|;
name|EnvironmentStringPBEConfig
name|env
init|=
operator|new
name|EnvironmentStringPBEConfig
argument_list|()
decl_stmt|;
name|env
operator|.
name|setAlgorithm
argument_list|(
literal|"PBEWithMD5AndDES"
argument_list|)
expr_stmt|;
name|env
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|enc
operator|.
name|setConfig
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|String
name|val
init|=
name|enc
operator|.
name|encrypt
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"foo"
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.bundles.framework.storage"
argument_list|,
literal|"target/bundles/"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.name"
argument_list|,
literal|"root"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|BundleDescriptor
argument_list|>
name|bundles
init|=
operator|new
name|ClasspathScanner
argument_list|()
operator|.
name|scanForBundles
argument_list|(
literal|"(Bundle-SymbolicName=*)"
argument_list|)
decl_stmt|;
name|bundles
operator|.
name|add
argument_list|(
name|getBundleDescriptor
argument_list|(
literal|"target/jasypt.jar"
argument_list|,
name|bundle
argument_list|()
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/karaf-jaas-jasypt.xml"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/OSGI-INF/blueprint/karaf-jaas-jasypt.xml"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
literal|"Manifest-Version"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-ManifestVersion"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-SymbolicName"
argument_list|,
literal|"jasypt"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-Version"
argument_list|,
literal|"0.0.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bundles
operator|.
name|add
argument_list|(
name|getBundleDescriptor
argument_list|(
literal|"target/test.jar"
argument_list|,
name|bundle
argument_list|()
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/test.xml"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test.xml"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
literal|"Manifest-Version"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-ManifestVersion"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-SymbolicName"
argument_list|,
literal|"test"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-Version"
argument_list|,
literal|"0.0.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Map
name|config
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
name|PojoServiceRegistryFactory
operator|.
name|BUNDLE_DESCRIPTORS
argument_list|,
name|bundles
argument_list|)
expr_stmt|;
name|PojoServiceRegistry
name|reg
init|=
operator|new
name|PojoServiceRegistryFactoryImpl
argument_list|()
operator|.
name|newPojoServiceRegistry
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|bundleContext
operator|=
name|reg
operator|.
name|getBundleContext
argument_list|()
expr_stmt|;
block|}
specifier|private
name|BundleDescriptor
name|getBundleDescriptor
parameter_list|(
name|String
name|path
parameter_list|,
name|TinyBundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|StreamUtils
operator|.
name|copy
argument_list|(
name|bundle
operator|.
name|build
argument_list|()
argument_list|,
name|fos
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|JarInputStream
name|jis
init|=
operator|new
name|JarInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
name|entry
range|:
name|jis
operator|.
name|getManifest
argument_list|()
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|BundleDescriptor
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
literal|"jar:"
operator|+
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"!/"
argument_list|,
name|headers
argument_list|)
return|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPlaceholder
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|" / "
operator|+
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Bean
name|encoded
init|=
name|getOsgiService
argument_list|(
name|Bean
operator|.
name|class
argument_list|,
literal|"(encoded=*)"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|encoded
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|timeout
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|DEFAULT_TIMEOUT
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|filter
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|type
argument_list|,
name|filter
argument_list|,
name|DEFAULT_TIMEOUT
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|filter
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|ServiceTracker
name|tracker
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|flt
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|filter
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
condition|)
block|{
name|flt
operator|=
literal|"(&("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")"
operator|+
name|filter
operator|+
literal|")"
expr_stmt|;
block|}
else|else
block|{
name|flt
operator|=
literal|"(&("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")("
operator|+
name|filter
operator|+
literal|"))"
expr_stmt|;
block|}
block|}
else|else
block|{
name|flt
operator|=
literal|"("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
name|Filter
name|osgiFilter
init|=
name|FrameworkUtil
operator|.
name|createFilter
argument_list|(
name|flt
argument_list|)
decl_stmt|;
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|(
name|bundleContext
argument_list|,
name|osgiFilter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|open
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Note that the tracker is not closed to keep the reference
comment|// This is buggy, as the service reference may change i think
name|Object
name|svc
init|=
name|type
operator|.
name|cast
argument_list|(
name|tracker
operator|.
name|waitForService
argument_list|(
name|timeout
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|svc
operator|==
literal|null
condition|)
block|{
name|Dictionary
name|dic
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Test bundle headers: "
operator|+
name|explode
argument_list|(
name|dic
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ServiceReference
name|ref
range|:
name|asCollection
argument_list|(
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"ServiceReference: "
operator|+
name|ref
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ServiceReference
name|ref
range|:
name|asCollection
argument_list|(
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
literal|null
argument_list|,
name|flt
argument_list|)
argument_list|)
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Filtered ServiceReference: "
operator|+
name|ref
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Gave up waiting for service "
operator|+
name|flt
argument_list|)
throw|;
block|}
return|return
name|type
operator|.
name|cast
argument_list|(
name|svc
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid filter"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/*      * Explode the dictionary into a ,-delimited list of key=value pairs      */
specifier|private
specifier|static
name|String
name|explode
parameter_list|(
name|Dictionary
name|dictionary
parameter_list|)
block|{
name|Enumeration
name|keys
init|=
name|dictionary
operator|.
name|keys
argument_list|()
decl_stmt|;
name|StringBuffer
name|result
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Object
name|key
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s=%s"
argument_list|,
name|key
argument_list|,
name|dictionary
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/*      * Provides an iterable collection of references, even if the original array is null      */
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|ServiceReference
argument_list|>
name|asCollection
parameter_list|(
name|ServiceReference
index|[]
name|references
parameter_list|)
block|{
name|List
argument_list|<
name|ServiceReference
argument_list|>
name|result
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|references
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|reference
range|:
name|references
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

