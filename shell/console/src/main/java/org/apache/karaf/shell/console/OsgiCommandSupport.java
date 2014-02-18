begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|List
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
name|service
operator|.
name|command
operator|.
name|CommandSession
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
name|shell
operator|.
name|commands
operator|.
name|Action
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

begin_class
specifier|public
specifier|abstract
class|class
name|OsgiCommandSupport
extends|extends
name|AbstractAction
implements|implements
name|Action
implements|,
name|BundleContextAware
block|{
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
name|usedReferences
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|(
name|CommandSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
return|return
name|super
operator|.
name|execute
argument_list|(
name|session
argument_list|)
return|;
block|}
finally|finally
block|{
name|ungetServices
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
name|Bundle
name|framework
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|framework
operator|==
literal|null
condition|?
name|bundleContext
else|:
name|framework
operator|.
name|getBundleContext
argument_list|()
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
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|getAllServices
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
try|try
block|{
return|return
name|getAllServices
argument_list|(
name|clazz
argument_list|,
literal|null
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
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|getAllServices
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|String
name|filter
parameter_list|)
throws|throws
name|InvalidSyntaxException
block|{
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|T
argument_list|>
argument_list|>
name|references
init|=
name|getBundleContext
argument_list|()
operator|.
name|getServiceReferences
argument_list|(
name|clazz
argument_list|,
name|filter
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|T
argument_list|>
name|services
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
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
argument_list|<
name|T
argument_list|>
name|ref
range|:
name|references
control|)
block|{
name|T
name|t
init|=
name|getService
argument_list|(
name|clazz
argument_list|,
name|ref
argument_list|)
decl_stmt|;
name|services
operator|.
name|add
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|services
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
name|ServiceReference
argument_list|<
name|T
argument_list|>
name|sr
init|=
name|getBundleContext
argument_list|()
operator|.
name|getServiceReference
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|sr
operator|!=
literal|null
condition|)
block|{
return|return
name|getService
argument_list|(
name|clazz
argument_list|,
name|sr
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|ServiceReference
argument_list|<
name|T
argument_list|>
name|reference
parameter_list|)
block|{
name|T
name|t
init|=
name|getBundleContext
argument_list|()
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|usedReferences
operator|==
literal|null
condition|)
block|{
name|usedReferences
operator|=
operator|new
name|ArrayList
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|usedReferences
operator|.
name|add
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
return|return
name|t
return|;
block|}
specifier|protected
name|void
name|ungetServices
parameter_list|()
block|{
if|if
condition|(
name|usedReferences
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|ref
range|:
name|usedReferences
control|)
block|{
name|getBundleContext
argument_list|()
operator|.
name|ungetService
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

