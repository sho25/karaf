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
name|system
operator|.
name|management
operator|.
name|internal
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
name|system
operator|.
name|FrameworkType
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
name|system
operator|.
name|SystemService
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
name|system
operator|.
name|management
operator|.
name|SystemMBean
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
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StandardMBean
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
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * System MBean implementation.  */
end_comment

begin_class
specifier|public
class|class
name|SystemMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|SystemMBean
block|{
specifier|private
name|SystemService
name|systemService
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
specifier|public
name|SystemMBeanImpl
parameter_list|()
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|SystemMBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSystemService
parameter_list|(
name|SystemService
name|systemService
parameter_list|)
block|{
name|this
operator|.
name|systemService
operator|=
name|systemService
expr_stmt|;
block|}
specifier|public
name|SystemService
name|getSystemService
parameter_list|()
block|{
return|return
name|this
operator|.
name|systemService
return|;
block|}
specifier|public
name|void
name|halt
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
name|systemService
operator|.
name|halt
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|halt
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|systemService
operator|.
name|halt
argument_list|(
name|time
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|reboot
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
name|systemService
operator|.
name|reboot
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|reboot
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|systemService
operator|.
name|reboot
argument_list|(
name|time
argument_list|,
name|SystemService
operator|.
name|Swipe
operator|.
name|NONE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|rebootCleanCache
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|systemService
operator|.
name|reboot
argument_list|(
name|time
argument_list|,
name|SystemService
operator|.
name|Swipe
operator|.
name|CACHE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|rebootCleanAll
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|systemService
operator|.
name|reboot
argument_list|(
name|time
argument_list|,
name|SystemService
operator|.
name|Swipe
operator|.
name|ALL
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setStartLevel
parameter_list|(
name|int
name|startLevel
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|systemService
operator|.
name|setStartLevel
argument_list|(
name|startLevel
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|int
name|getStartLevel
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
return|return
name|systemService
operator|.
name|getStartLevel
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getFramework
parameter_list|()
block|{
return|return
name|this
operator|.
name|systemService
operator|.
name|getFramework
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setFramework
parameter_list|(
name|String
name|framework
parameter_list|)
block|{
name|this
operator|.
name|systemService
operator|.
name|setFramework
argument_list|(
name|FrameworkType
operator|.
name|valueOf
argument_list|(
name|framework
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setFrameworkDebug
parameter_list|(
name|boolean
name|debug
parameter_list|)
block|{
name|this
operator|.
name|systemService
operator|.
name|setFrameworkDebug
argument_list|(
name|debug
argument_list|)
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
name|this
operator|.
name|systemService
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instance name can't be null or empty"
argument_list|)
throw|;
block|}
name|this
operator|.
name|systemService
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|this
operator|.
name|systemService
operator|.
name|getVersion
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|(
name|boolean
name|unset
parameter_list|,
name|boolean
name|dumpToFile
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Properties
name|props
init|=
operator|(
name|Properties
operator|)
name|java
operator|.
name|lang
operator|.
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|clone
argument_list|()
decl_stmt|;
name|String
name|def
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|unset
condition|)
block|{
name|def
operator|=
literal|"unset"
expr_stmt|;
block|}
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_BEGINNING_STARTLEVEL
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_BOOTDELEGATION
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_BUNDLE_PARENT
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_BUNDLE_PARENT_APP
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_BUNDLE_PARENT_BOOT
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_BUNDLE_PARENT_EXT
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_BUNDLE_PARENT_FRAMEWORK
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_EXECPERMISSION
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_EXECUTIONENVIRONMENT
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_LANGUAGE
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_LIBRARY_EXTENSIONS
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_OS_NAME
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_OS_VERSION
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_PROCESSOR
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_SECURITY
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_SECURITY_OSGI
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_STORAGE
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_SYSTEMPACKAGES
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_SYSTEMPACKAGES_EXTRA
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_VENDOR
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_VERSION
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|FRAMEWORK_WINDOWSYSTEM
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|SUPPORTS_BOOTCLASSPATH_EXTENSION
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|SUPPORTS_FRAMEWORK_EXTENSION
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|SUPPORTS_FRAMEWORK_FRAGMENT
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|props
argument_list|,
name|Constants
operator|.
name|SUPPORTS_FRAMEWORK_REQUIREBUNDLE
argument_list|,
name|def
argument_list|)
expr_stmt|;
if|if
condition|(
name|dumpToFile
condition|)
block|{
name|PrintStream
name|ps
init|=
operator|new
name|PrintStream
argument_list|(
operator|new
name|File
argument_list|(
name|bundleContext
operator|.
name|getProperty
argument_list|(
literal|"karaf.data"
argument_list|)
argument_list|,
literal|"dump-properties-"
operator|+
name|java
operator|.
name|lang
operator|.
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|".properties"
argument_list|)
argument_list|)
decl_stmt|;
name|ps
operator|.
name|println
argument_list|(
literal|"#Dump of the System and OSGi properties"
argument_list|)
expr_stmt|;
name|ps
operator|.
name|println
argument_list|(
literal|"#Dump executed at "
operator|+
operator|new
name|SimpleDateFormat
argument_list|()
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|printOrderedProperties
argument_list|(
name|props
argument_list|,
name|ps
argument_list|)
expr_stmt|;
name|ps
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ps
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|printOrderedProperties
argument_list|(
name|props
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|printOrderedProperties
parameter_list|(
name|Properties
name|props
parameter_list|,
name|PrintStream
name|out
parameter_list|)
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|keys
init|=
name|props
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|order
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|keys
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|str
range|:
name|keys
control|)
block|{
name|order
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|str
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|order
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|order
control|)
block|{
name|out
operator|.
name|println
argument_list|(
name|key
operator|+
literal|"="
operator|+
name|props
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|printOrderedProperties
parameter_list|(
name|Properties
name|props
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
parameter_list|)
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|keys
init|=
name|props
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|order
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|keys
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|str
range|:
name|keys
control|)
block|{
name|order
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|str
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|order
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|order
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|props
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setProperty
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|def
parameter_list|)
block|{
name|String
name|val
init|=
name|bundleContext
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
operator|&&
name|def
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|java
operator|.
name|lang
operator|.
name|System
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|persistent
parameter_list|)
block|{
name|systemService
operator|.
name|setSystemProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|persistent
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

