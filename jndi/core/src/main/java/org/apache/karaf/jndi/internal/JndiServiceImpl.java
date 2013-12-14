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
name|jndi
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
name|aries
operator|.
name|proxy
operator|.
name|ProxyManager
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
name|jndi
operator|.
name|JndiService
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
name|ServiceReference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|*
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

begin_comment
comment|/**  * Implementation of the JNDI Service.  */
end_comment

begin_class
specifier|public
class|class
name|JndiServiceImpl
implements|implements
name|JndiService
block|{
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|ProxyManager
name|proxyManager
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|OSGI_JNDI_CONTEXT_PREFIX
init|=
literal|"osgi:service"
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|OSGI_JNDI_SERVICE_PROPERTY
init|=
literal|"osgi.jndi.service.name"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|names
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
name|names
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|result
operator|.
name|putAll
argument_list|(
name|names
argument_list|(
name|OSGI_JNDI_CONTEXT_PREFIX
operator|+
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
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
name|names
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|OSGI_JNDI_CONTEXT_PREFIX
argument_list|)
condition|)
block|{
comment|// OSGi service binding
comment|// make a lookup using directly the OSGi service
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|services
init|=
name|bundle
operator|.
name|getRegisteredServices
argument_list|()
decl_stmt|;
if|if
condition|(
name|services
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|service
range|:
name|services
control|)
block|{
if|if
condition|(
name|service
operator|.
name|getProperty
argument_list|(
name|OSGI_JNDI_SERVICE_PROPERTY
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Object
name|actualService
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|service
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxyManager
operator|.
name|isProxy
argument_list|(
name|actualService
argument_list|)
condition|)
block|{
name|actualService
operator|=
name|proxyManager
operator|.
name|unwrap
argument_list|(
name|actualService
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
name|OSGI_JNDI_CONTEXT_PREFIX
operator|+
name|service
operator|.
name|getProperty
argument_list|(
name|OSGI_JNDI_SERVICE_PROPERTY
argument_list|)
argument_list|,
name|actualService
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
else|else
block|{
comment|// "real" JNDI lookup
name|Context
name|context
init|=
operator|new
name|InitialContext
argument_list|()
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|NameClassPair
argument_list|>
name|pairs
init|=
name|context
operator|.
name|list
argument_list|(
name|name
argument_list|)
decl_stmt|;
while|while
condition|(
name|pairs
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|NameClassPair
name|pair
init|=
name|pairs
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Object
name|o
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|o
operator|=
name|context
operator|.
name|lookup
argument_list|(
name|name
operator|+
literal|"/"
operator|+
name|pair
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|o
operator|=
name|context
operator|.
name|lookup
argument_list|(
name|pair
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|instanceof
name|Context
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|pair
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|names
argument_list|(
operator|(
name|Context
operator|)
name|o
argument_list|,
name|sb
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
name|pair
operator|.
name|getName
argument_list|()
argument_list|,
name|pair
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|map
return|;
block|}
comment|/**      * Recursively list a context      *      * @param ctx the startup context.      * @param sb the string builder where to construct the full qualified name.      * @param map the final map containing name/class name pairs.      * @throws Exception      */
specifier|private
specifier|static
specifier|final
name|void
name|names
parameter_list|(
name|Context
name|ctx
parameter_list|,
name|StringBuilder
name|sb
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|NamingEnumeration
name|list
init|=
name|ctx
operator|.
name|listBindings
argument_list|(
literal|""
argument_list|)
decl_stmt|;
while|while
condition|(
name|list
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|Binding
name|item
init|=
operator|(
name|Binding
operator|)
name|list
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|className
init|=
name|item
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|item
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Object
name|o
init|=
name|item
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Context
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|names
argument_list|(
operator|(
name|Context
operator|)
name|o
argument_list|,
name|sb
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
operator|+
literal|"/"
operator|+
name|name
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|create
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
operator|new
name|InitialContext
argument_list|()
decl_stmt|;
name|String
index|[]
name|splitted
init|=
name|name
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitted
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|splitted
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|context
operator|.
name|createSubcontext
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|context
operator|=
operator|(
name|Context
operator|)
name|context
operator|.
name|lookup
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|context
operator|.
name|createSubcontext
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|delete
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
operator|new
name|InitialContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|destroySubcontext
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bind
parameter_list|(
name|long
name|serviceId
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
operator|new
name|InitialContext
argument_list|()
decl_stmt|;
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|services
init|=
name|bundle
operator|.
name|getRegisteredServices
argument_list|()
decl_stmt|;
if|if
condition|(
name|services
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|service
range|:
name|services
control|)
block|{
if|if
condition|(
name|service
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
operator|!=
literal|null
operator|&&
operator|(
operator|(
name|Long
operator|)
name|service
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
operator|)
operator|==
name|serviceId
condition|)
block|{
name|Object
name|actualService
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|service
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxyManager
operator|.
name|isProxy
argument_list|(
name|actualService
argument_list|)
condition|)
block|{
name|actualService
operator|=
name|proxyManager
operator|.
name|unwrap
argument_list|(
name|actualService
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|String
index|[]
name|splitted
init|=
name|name
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitted
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|splitted
operator|.
name|length
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|Object
name|o
init|=
name|context
operator|.
name|lookup
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Context
operator|)
condition|)
block|{
throw|throw
operator|new
name|NamingException
argument_list|(
literal|"Name "
operator|+
name|splitted
index|[
name|i
index|]
operator|+
literal|" already exists"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|NameNotFoundException
name|nnfe
parameter_list|)
block|{
name|context
operator|.
name|createSubcontext
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|context
operator|=
operator|(
name|Context
operator|)
name|context
operator|.
name|lookup
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|name
operator|=
name|splitted
index|[
name|splitted
operator|.
name|length
operator|-
literal|1
index|]
expr_stmt|;
block|}
name|context
operator|.
name|bind
argument_list|(
name|name
argument_list|,
name|actualService
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|alias
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|alias
parameter_list|)
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
operator|new
name|InitialContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|OSGI_JNDI_CONTEXT_PREFIX
argument_list|)
condition|)
block|{
comment|// get the object
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|services
init|=
name|bundle
operator|.
name|getRegisteredServices
argument_list|()
decl_stmt|;
if|if
condition|(
name|services
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|service
range|:
name|services
control|)
block|{
if|if
condition|(
name|service
operator|.
name|getProperty
argument_list|(
name|OSGI_JNDI_SERVICE_PROPERTY
argument_list|)
operator|!=
literal|null
operator|&&
operator|(
operator|(
name|String
operator|)
name|service
operator|.
name|getProperty
argument_list|(
name|OSGI_JNDI_SERVICE_PROPERTY
argument_list|)
operator|)
operator|.
name|equals
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|OSGI_JNDI_CONTEXT_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|Object
name|actualService
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|service
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|proxyManager
operator|.
name|isProxy
argument_list|(
name|actualService
argument_list|)
condition|)
block|{
name|actualService
operator|=
name|proxyManager
operator|.
name|unwrap
argument_list|(
name|actualService
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
name|String
index|[]
name|splitted
init|=
name|alias
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitted
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|splitted
operator|.
name|length
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|Object
name|o
init|=
name|context
operator|.
name|lookup
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Context
operator|)
condition|)
block|{
throw|throw
operator|new
name|NamingException
argument_list|(
literal|"Name "
operator|+
name|splitted
index|[
name|i
index|]
operator|+
literal|" already exists"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|NameNotFoundException
name|nnfe
parameter_list|)
block|{
name|context
operator|.
name|createSubcontext
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|context
operator|=
operator|(
name|Context
operator|)
name|context
operator|.
name|lookup
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|alias
operator|=
name|splitted
index|[
name|splitted
operator|.
name|length
operator|-
literal|1
index|]
expr_stmt|;
block|}
name|context
operator|.
name|bind
argument_list|(
name|alias
argument_list|,
name|actualService
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
else|else
block|{
name|Object
name|object
init|=
name|context
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|String
index|[]
name|splitted
init|=
name|alias
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitted
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|splitted
operator|.
name|length
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|Object
name|o
init|=
name|context
operator|.
name|lookup
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Context
operator|)
condition|)
block|{
throw|throw
operator|new
name|NamingException
argument_list|(
literal|"Name "
operator|+
name|splitted
index|[
name|i
index|]
operator|+
literal|" already exists"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|NameNotFoundException
name|nnfe
parameter_list|)
block|{
name|context
operator|.
name|createSubcontext
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|context
operator|=
operator|(
name|Context
operator|)
name|context
operator|.
name|lookup
argument_list|(
name|splitted
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|alias
operator|=
name|splitted
index|[
name|splitted
operator|.
name|length
operator|-
literal|1
index|]
expr_stmt|;
block|}
name|context
operator|.
name|bind
argument_list|(
name|alias
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|unbind
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|InitialContext
name|context
init|=
operator|new
name|InitialContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|OSGI_JNDI_CONTEXT_PREFIX
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You can't unbind a name from the "
operator|+
name|OSGI_JNDI_CONTEXT_PREFIX
operator|+
literal|" JNDI context."
argument_list|)
throw|;
block|}
name|context
operator|.
name|unbind
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
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
name|ProxyManager
name|getProxyManager
parameter_list|()
block|{
return|return
name|proxyManager
return|;
block|}
specifier|public
name|void
name|setProxyManager
parameter_list|(
name|ProxyManager
name|proxyManager
parameter_list|)
block|{
name|this
operator|.
name|proxyManager
operator|=
name|proxyManager
expr_stmt|;
block|}
block|}
end_class

end_unit

