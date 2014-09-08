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
name|modules
operator|.
name|impl
package|;
end_package

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
name|Hashtable
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
name|jaas
operator|.
name|config
operator|.
name|JaasRealm
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
name|modules
operator|.
name|BackingEngineFactory
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
name|modules
operator|.
name|EncryptionService
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
name|modules
operator|.
name|encryption
operator|.
name|BasicEncryptionService
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
name|modules
operator|.
name|ldap
operator|.
name|LDAPCache
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
name|modules
operator|.
name|properties
operator|.
name|AutoEncryptionSupport
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
name|modules
operator|.
name|properties
operator|.
name|PropertiesBackingEngineFactory
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
name|modules
operator|.
name|publickey
operator|.
name|PublickeyBackingEngineFactory
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
name|tracker
operator|.
name|BaseActivator
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
name|tracker
operator|.
name|Managed
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
name|tracker
operator|.
name|ProvideService
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
name|tracker
operator|.
name|Services
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
name|service
operator|.
name|cm
operator|.
name|ManagedService
import|;
end_import

begin_class
annotation|@
name|Managed
argument_list|(
literal|"org.apache.karaf.jaas"
argument_list|)
annotation|@
name|Services
argument_list|(
name|provides
operator|=
block|{
annotation|@
name|ProvideService
argument_list|(
name|JaasRealm
operator|.
name|class
argument_list|)
block|,
annotation|@
name|ProvideService
argument_list|(
name|BackingEngineFactory
operator|.
name|class
argument_list|)
block|,
annotation|@
name|ProvideService
argument_list|(
name|EncryptionService
operator|.
name|class
argument_list|)
block|}
argument_list|)
specifier|public
class|class
name|Activator
extends|extends
name|BaseActivator
implements|implements
name|ManagedService
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ENCRYPTION_NAME
init|=
literal|"encryption.name"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCRYPTION_ENABLED
init|=
literal|"encryption.enabled"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCRYPTION_PREFIX
init|=
literal|"encryption.prefix"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCRYPTION_SUFFIX
init|=
literal|"encryption.suffix"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCRYPTION_ALGORITHM
init|=
literal|"encryption.algorithm"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCRYPTION_ENCODING
init|=
literal|"encryption.encoding"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EVENTADMIN_ENABLED
init|=
literal|"eventadmin.enabled"
decl_stmt|;
specifier|private
name|KarafRealm
name|karafRealm
decl_stmt|;
specifier|private
name|AutoEncryptionSupport
name|autoEncryptionSupport
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|doOpen
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doOpen
argument_list|()
expr_stmt|;
name|register
argument_list|(
name|BackingEngineFactory
operator|.
name|class
argument_list|,
operator|new
name|PropertiesBackingEngineFactory
argument_list|()
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|BackingEngineFactory
operator|.
name|class
argument_list|,
operator|new
name|PublickeyBackingEngineFactory
argument_list|()
argument_list|)
expr_stmt|;
name|Hashtable
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
name|Constants
operator|.
name|SERVICE_RANKING
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"basic"
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|EncryptionService
operator|.
name|class
argument_list|,
operator|new
name|BasicEncryptionService
argument_list|()
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
name|getConfig
argument_list|()
decl_stmt|;
name|karafRealm
operator|=
operator|new
name|KarafRealm
argument_list|(
name|bundleContext
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|JaasRealm
operator|.
name|class
argument_list|,
name|karafRealm
argument_list|)
expr_stmt|;
name|autoEncryptionSupport
operator|=
operator|new
name|AutoEncryptionSupport
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
if|if
condition|(
name|autoEncryptionSupport
operator|!=
literal|null
condition|)
block|{
name|autoEncryptionSupport
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|LDAPCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|reconfigure
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
name|getConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|karafRealm
operator|!=
literal|null
condition|)
block|{
name|karafRealm
operator|.
name|updated
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|autoEncryptionSupport
operator|!=
literal|null
condition|)
block|{
name|autoEncryptionSupport
operator|.
name|updated
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getConfig
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
literal|"detailed.login.exception"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
name|ENCRYPTION_NAME
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
name|ENCRYPTION_ENABLED
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
name|ENCRYPTION_PREFIX
argument_list|,
literal|"{CRYPT}"
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
name|ENCRYPTION_SUFFIX
argument_list|,
literal|"{CRYPT}"
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
name|ENCRYPTION_ALGORITHM
argument_list|,
literal|"MD5"
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
name|ENCRYPTION_ENCODING
argument_list|,
literal|"hexadecimal"
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
name|EVENTADMIN_ENABLED
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
literal|"audit.file.enabled"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
literal|"audit.file.file"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.data"
argument_list|)
operator|+
literal|"/security/audit.log"
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
literal|"audit.eventadmin.enabled"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|config
argument_list|,
literal|"audit.eventadmin.topic"
argument_list|,
literal|"org/apache/karaf/login"
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
name|BundleContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|bundleContext
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
specifier|private
name|void
name|populate
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|def
parameter_list|)
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|getString
argument_list|(
name|key
argument_list|,
name|def
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

