begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|specs
operator|.
name|activator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|Map
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
name|Callable
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
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|specs
operator|.
name|locator
operator|.
name|OsgiLocator
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
name|BundleActivator
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
name|BundleEvent
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
name|SynchronousBundleListener
import|;
end_import

begin_class
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
implements|,
name|SynchronousBundleListener
block|{
specifier|private
specifier|static
name|boolean
name|debug
init|=
literal|false
decl_stmt|;
specifier|private
name|ConcurrentMap
argument_list|<
name|Long
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Callable
argument_list|<
name|Class
argument_list|>
argument_list|>
argument_list|>
name|factories
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
static|static
block|{
try|try
block|{
name|String
name|prop
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.karaf.specs.debug"
argument_list|)
decl_stmt|;
name|debug
operator|=
name|prop
operator|!=
literal|null
operator|&&
operator|!
literal|"false"
operator|.
name|equals
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{ }
block|}
comment|/**      *<p>Output debugging messages.</p>      *      * @param msg<code>String</code> to print to<code>stderr</code>.      */
specifier|protected
name|void
name|debugPrintln
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Spec("
operator|+
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
operator|+
literal|"): "
operator|+
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|start
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|debugPrintln
argument_list|(
literal|"activating"
argument_list|)
expr_stmt|;
name|debugPrintln
argument_list|(
literal|"adding bundle listener"
argument_list|)
expr_stmt|;
name|bundleContext
operator|.
name|addBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|debugPrintln
argument_list|(
literal|"checking existing bundles"
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|RESOLVED
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|STARTING
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|ACTIVE
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|STOPPING
condition|)
block|{
name|register
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
name|debugPrintln
argument_list|(
literal|"activated"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|stop
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|Exception
block|{
name|debugPrintln
argument_list|(
literal|"deactivating"
argument_list|)
expr_stmt|;
if|if
condition|(
name|bundleContext
operator|!=
literal|null
condition|)
block|{
name|bundleContext
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
operator|!
name|factories
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|unregister
argument_list|(
name|factories
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|debugPrintln
argument_list|(
literal|"deactivated"
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|bundleChanged
parameter_list|(
name|BundleEvent
name|event
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|bundleContext
operator|==
literal|null
condition|)
block|{
return|return;
block|}
block|}
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|RESOLVED
condition|)
block|{
name|register
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|UNRESOLVED
operator|||
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|UNINSTALLED
condition|)
block|{
name|unregister
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|register
parameter_list|(
specifier|final
name|Bundle
name|bundle
parameter_list|)
block|{
name|debugPrintln
argument_list|(
literal|"checking bundle "
operator|+
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Callable
argument_list|<
name|Class
argument_list|>
argument_list|>
name|map
init|=
name|factories
operator|.
name|get
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
decl_stmt|;
name|Enumeration
name|e
init|=
name|bundle
operator|.
name|findEntries
argument_list|(
literal|"META-INF/services/"
argument_list|,
literal|"*"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
while|while
condition|(
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
specifier|final
name|URL
name|u
init|=
operator|(
name|URL
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
specifier|final
name|String
name|url
init|=
name|u
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
continue|continue;
block|}
specifier|final
name|String
name|factoryId
init|=
name|url
operator|.
name|substring
argument_list|(
name|url
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
name|factoryId
argument_list|,
operator|new
name|BundleFactoryLoader
argument_list|(
name|factoryId
argument_list|,
name|u
argument_list|,
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Callable
argument_list|<
name|Class
argument_list|>
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|debugPrintln
argument_list|(
literal|"registering service for key "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" with value "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|OsgiLocator
operator|.
name|register
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|unregister
parameter_list|(
name|long
name|bundleId
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Callable
argument_list|<
name|Class
argument_list|>
argument_list|>
name|map
init|=
name|factories
operator|.
name|remove
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Callable
argument_list|<
name|Class
argument_list|>
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|debugPrintln
argument_list|(
literal|"unregistering service for key "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" with value "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|OsgiLocator
operator|.
name|unregister
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
class|class
name|BundleFactoryLoader
implements|implements
name|Callable
argument_list|<
name|Class
argument_list|>
block|{
specifier|private
specifier|final
name|String
name|factoryId
decl_stmt|;
specifier|private
specifier|final
name|URL
name|u
decl_stmt|;
specifier|private
specifier|final
name|Bundle
name|bundle
decl_stmt|;
specifier|private
specifier|volatile
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
decl_stmt|;
specifier|public
name|BundleFactoryLoader
parameter_list|(
name|String
name|factoryId
parameter_list|,
name|URL
name|u
parameter_list|,
name|Bundle
name|bundle
parameter_list|)
block|{
name|this
operator|.
name|factoryId
operator|=
name|factoryId
expr_stmt|;
name|this
operator|.
name|u
operator|=
name|u
expr_stmt|;
name|this
operator|.
name|bundle
operator|=
name|bundle
expr_stmt|;
block|}
specifier|public
name|Class
name|call
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|debugPrintln
argument_list|(
literal|"loading factory for key: "
operator|+
name|factoryId
argument_list|)
expr_stmt|;
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
name|debugPrintln
argument_list|(
literal|"creating factory for key: "
operator|+
name|factoryId
argument_list|)
expr_stmt|;
try|try
init|(
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|u
operator|.
name|openStream
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
init|)
block|{
name|String
name|factoryClassName
init|=
name|br
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|factoryClassName
operator|!=
literal|null
condition|)
block|{
name|factoryClassName
operator|=
name|factoryClassName
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|factoryClassName
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|!=
literal|'#'
condition|)
block|{
name|debugPrintln
argument_list|(
literal|"factory implementation: "
operator|+
name|factoryClassName
argument_list|)
expr_stmt|;
name|clazz
operator|=
name|bundle
operator|.
name|loadClass
argument_list|(
name|factoryClassName
argument_list|)
expr_stmt|;
return|return
name|clazz
return|;
block|}
name|factoryClassName
operator|=
name|br
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|clazz
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|debugPrintln
argument_list|(
literal|"exception caught while creating factory: "
operator|+
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|debugPrintln
argument_list|(
literal|"error caught while creating factory: "
operator|+
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|u
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|u
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|BundleFactoryLoader
condition|)
block|{
return|return
name|u
operator|.
name|toExternalForm
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|BundleFactoryLoader
operator|)
name|obj
operator|)
operator|.
name|u
operator|.
name|toExternalForm
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

