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
name|io
operator|.
name|File
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|AppConfigurationEntry
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
name|boot
operator|.
name|ProxyLoginModule
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
name|KarafRealm
implements|implements
name|JaasRealm
block|{
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_ETC
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REALM
init|=
literal|"karaf"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTIES_MODULE
init|=
literal|"org.apache.karaf.jaas.modules.properties.PropertiesLoginModule"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PUBLIC_KEY_MODULE
init|=
literal|"org.apache.karaf.jaas.modules.publickey.PublickeyLoginModule"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FILE_AUDIT_MODULE
init|=
literal|"org.apache.karaf.jaas.modules.audit.FileAuditLoginModule"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOG_AUDIT_MODULE
init|=
literal|"org.apache.karaf.jaas.modules.audit.LogAuditLoginModule"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EVENTADMIN_AUDIT_MODULE
init|=
literal|"org.apache.karaf.jaas.modules.audit.EventAdminAuditLoginModule"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MODULE
init|=
literal|"org.apache.karaf.jaas.module"
decl_stmt|;
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
specifier|volatile
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
specifier|public
name|KarafRealm
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|updated
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|updated
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|REALM
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getRank
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|AppConfigurationEntry
index|[]
name|getEntries
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|propertiesOptions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|propertiesOptions
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
name|propertiesOptions
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_MODULE
argument_list|,
name|PROPERTIES_MODULE
argument_list|)
expr_stmt|;
name|propertiesOptions
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_BUNDLE
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesOptions
operator|.
name|put
argument_list|(
literal|"users"
argument_list|,
name|KARAF_ETC
operator|+
name|File
operator|.
name|separatorChar
operator|+
literal|"users.properties"
argument_list|)
expr_stmt|;
name|propertiesOptions
operator|.
name|put
argument_list|(
literal|"detailed.login.exception"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"detailed.login.exception"
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesOptions
operator|.
name|put
argument_list|(
literal|"encryption.name"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"encryption.name"
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesOptions
operator|.
name|put
argument_list|(
literal|"encryption.enabled"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"encryption.enabled"
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesOptions
operator|.
name|put
argument_list|(
literal|"encryption.prefix"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"encryption.prefix"
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesOptions
operator|.
name|put
argument_list|(
literal|"encryption.suffix"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"encryption.suffix"
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesOptions
operator|.
name|put
argument_list|(
literal|"encryption.algorithm"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"encryption.algorithm"
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesOptions
operator|.
name|put
argument_list|(
literal|"encryption.encoding"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"encryption.encoding"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|publicKeyOptions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|publicKeyOptions
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
name|publicKeyOptions
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_MODULE
argument_list|,
name|PUBLIC_KEY_MODULE
argument_list|)
expr_stmt|;
name|publicKeyOptions
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_BUNDLE
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|publicKeyOptions
operator|.
name|put
argument_list|(
literal|"users"
argument_list|,
name|KARAF_ETC
operator|+
name|File
operator|.
name|separatorChar
operator|+
literal|"keys.properties"
argument_list|)
expr_stmt|;
name|publicKeyOptions
operator|.
name|put
argument_list|(
literal|"detailed.login.exception"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"detailed.login.exception"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|fileOptions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|fileOptions
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
name|fileOptions
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_MODULE
argument_list|,
name|FILE_AUDIT_MODULE
argument_list|)
expr_stmt|;
name|fileOptions
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_BUNDLE
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|fileOptions
operator|.
name|put
argument_list|(
literal|"enabled"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"audit.file.enabled"
argument_list|)
argument_list|)
expr_stmt|;
name|fileOptions
operator|.
name|put
argument_list|(
literal|"file"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"audit.file.file"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|logOptions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|logOptions
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
name|logOptions
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_MODULE
argument_list|,
name|LOG_AUDIT_MODULE
argument_list|)
expr_stmt|;
name|logOptions
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_BUNDLE
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|logOptions
operator|.
name|put
argument_list|(
literal|"enabled"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"audit.log.enabled"
argument_list|)
argument_list|)
expr_stmt|;
name|logOptions
operator|.
name|put
argument_list|(
literal|"logger"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"audit.log.logger"
argument_list|)
argument_list|)
expr_stmt|;
name|logOptions
operator|.
name|put
argument_list|(
literal|"level"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"audit.log.level"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|eventadminOptions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|eventadminOptions
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|eventadminOptions
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
name|eventadminOptions
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_MODULE
argument_list|,
name|EVENTADMIN_AUDIT_MODULE
argument_list|)
expr_stmt|;
name|eventadminOptions
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_BUNDLE
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|eventadminOptions
operator|.
name|put
argument_list|(
literal|"enabled"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"audit.eventadmin.enabled"
argument_list|)
argument_list|)
expr_stmt|;
name|eventadminOptions
operator|.
name|put
argument_list|(
literal|"topic"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"audit.eventadmin.topic"
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|AppConfigurationEntry
index|[]
block|{
operator|new
name|AppConfigurationEntry
argument_list|(
name|ProxyLoginModule
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|AppConfigurationEntry
operator|.
name|LoginModuleControlFlag
operator|.
name|OPTIONAL
argument_list|,
name|propertiesOptions
argument_list|)
block|,
operator|new
name|AppConfigurationEntry
argument_list|(
name|ProxyLoginModule
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|AppConfigurationEntry
operator|.
name|LoginModuleControlFlag
operator|.
name|OPTIONAL
argument_list|,
name|publicKeyOptions
argument_list|)
block|,
operator|new
name|AppConfigurationEntry
argument_list|(
name|ProxyLoginModule
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|AppConfigurationEntry
operator|.
name|LoginModuleControlFlag
operator|.
name|OPTIONAL
argument_list|,
name|fileOptions
argument_list|)
block|,
operator|new
name|AppConfigurationEntry
argument_list|(
name|ProxyLoginModule
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|AppConfigurationEntry
operator|.
name|LoginModuleControlFlag
operator|.
name|OPTIONAL
argument_list|,
name|logOptions
argument_list|)
block|,
operator|new
name|AppConfigurationEntry
argument_list|(
name|ProxyLoginModule
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|AppConfigurationEntry
operator|.
name|LoginModuleControlFlag
operator|.
name|OPTIONAL
argument_list|,
name|eventadminOptions
argument_list|)
block|}
return|;
block|}
block|}
end_class

end_unit

