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
name|service
operator|.
name|guard
operator|.
name|impl
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
name|Collections
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
name|Hashtable
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
name|easymock
operator|.
name|IAnswer
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
name|ServiceListener
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
name|service
operator|.
name|cm
operator|.
name|Configuration
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
name|ConfigurationAdmin
import|;
end_import

begin_class
specifier|public
class|class
name|GuardingFindHookTest
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testFindHook
parameter_list|()
throws|throws
name|Exception
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"service.guard"
argument_list|,
literal|"(|(moo=foo)(foo=*))"
argument_list|)
expr_stmt|;
name|BundleContext
name|hookBC
init|=
name|mockConfigAdminBundleContext
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|GuardProxyCatalog
name|gpc
init|=
operator|new
name|GuardProxyCatalog
argument_list|(
name|hookBC
argument_list|)
decl_stmt|;
name|Filter
name|serviceFilter
init|=
name|FrameworkUtil
operator|.
name|createFilter
argument_list|(
literal|"(foo=*)"
argument_list|)
decl_stmt|;
name|GuardingFindHook
name|gfh
init|=
operator|new
name|GuardingFindHook
argument_list|(
name|hookBC
argument_list|,
name|gpc
argument_list|,
name|serviceFilter
argument_list|)
decl_stmt|;
name|BundleContext
name|clientBC
init|=
name|mockBundleContext
argument_list|(
literal|31L
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|,
literal|16L
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"moo"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|sref
init|=
name|mockServiceReference
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
name|refs
init|=
operator|new
name|ArrayList
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|refs
operator|.
name|add
argument_list|(
name|sref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition"
argument_list|,
literal|0
argument_list|,
name|gpc
operator|.
name|proxyMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|gfh
operator|.
name|find
argument_list|(
name|clientBC
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|refs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The service doesn't match the filter so should have no effect"
argument_list|,
literal|0
argument_list|,
name|gpc
operator|.
name|proxyMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The service doesn't match the filter so should be presented to the client"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|sref
argument_list|)
argument_list|,
name|refs
argument_list|)
expr_stmt|;
name|long
name|service2ID
init|=
literal|17L
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props2
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props2
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|,
name|service2ID
argument_list|)
expr_stmt|;
name|props2
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|sref2
init|=
name|mockServiceReference
argument_list|(
name|props2
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
name|refs2
init|=
operator|new
name|ArrayList
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|refs2
operator|.
name|add
argument_list|(
name|sref2
argument_list|)
expr_stmt|;
name|gfh
operator|.
name|find
argument_list|(
name|clientBC
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|refs2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The service should be hidden from the client"
argument_list|,
literal|0
argument_list|,
name|refs2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The service should have caused a proxy creation"
argument_list|,
literal|1
argument_list|,
name|gpc
operator|.
name|proxyMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A proxy creation job should have been created"
argument_list|,
literal|1
argument_list|,
name|gpc
operator|.
name|createProxyQueue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sref2
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
argument_list|,
name|gpc
operator|.
name|proxyMap
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
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
name|refs3
init|=
operator|new
name|ArrayList
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|refs3
operator|.
name|add
argument_list|(
name|sref2
argument_list|)
expr_stmt|;
comment|// Ensure that the hook bundle has nothing hidden
name|gfh
operator|.
name|find
argument_list|(
name|hookBC
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|refs3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The service should not be hidden from the hook bundle"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|sref2
argument_list|)
argument_list|,
name|refs3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No proxy creation caused in this case"
argument_list|,
literal|1
argument_list|,
name|gpc
operator|.
name|proxyMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No change expected"
argument_list|,
name|sref2
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
argument_list|,
name|gpc
operator|.
name|proxyMap
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
comment|// Ensure that the system bundle has nothing hidden
name|gfh
operator|.
name|find
argument_list|(
name|mockBundleContext
argument_list|(
literal|0L
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|refs3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The service should not be hidden from the framework bundle"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|sref2
argument_list|)
argument_list|,
name|refs3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No proxy creation caused in this case"
argument_list|,
literal|1
argument_list|,
name|gpc
operator|.
name|proxyMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No change expected"
argument_list|,
name|sref2
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
argument_list|,
name|gpc
operator|.
name|proxyMap
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
comment|// Ensure that if we ask for the same client again, it will not create another proxy
name|gpc
operator|.
name|createProxyQueue
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// Manually empty the queue
name|gfh
operator|.
name|find
argument_list|(
name|clientBC
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|refs3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The service should be hidden from the client"
argument_list|,
literal|0
argument_list|,
name|refs3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There is already a proxy for this client, no need for an additional one"
argument_list|,
literal|1
argument_list|,
name|gpc
operator|.
name|proxyMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No additional jobs should have been scheduled"
argument_list|,
literal|0
argument_list|,
name|gpc
operator|.
name|createProxyQueue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No change expected"
argument_list|,
name|sref2
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
argument_list|,
name|gpc
operator|.
name|proxyMap
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
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
name|refs4
init|=
operator|new
name|ArrayList
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|refs4
operator|.
name|add
argument_list|(
name|sref2
argument_list|)
expr_stmt|;
comment|// another client should not get another proxy
name|BundleContext
name|client2BC
init|=
name|mockBundleContext
argument_list|(
literal|32768L
argument_list|)
decl_stmt|;
name|gfh
operator|.
name|find
argument_list|(
name|client2BC
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|refs4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The service should be hidden for this new client"
argument_list|,
literal|0
argument_list|,
name|refs4
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No proxy creation job should have been created"
argument_list|,
literal|0
argument_list|,
name|gpc
operator|.
name|createProxyQueue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No proxy creation caused in this case"
argument_list|,
literal|1
argument_list|,
name|gpc
operator|.
name|proxyMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No change expected"
argument_list|,
name|sref2
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
argument_list|,
name|gpc
operator|.
name|proxyMap
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testFindHookProxyServices
parameter_list|()
throws|throws
name|Exception
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"service.guard"
argument_list|,
literal|"(service.id=*)"
argument_list|)
expr_stmt|;
name|BundleContext
name|hookBC
init|=
name|mockConfigAdminBundleContext
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|GuardProxyCatalog
name|gpc
init|=
operator|new
name|GuardProxyCatalog
argument_list|(
name|hookBC
argument_list|)
decl_stmt|;
name|Filter
name|serviceFilter
init|=
name|FrameworkUtil
operator|.
name|createFilter
argument_list|(
literal|"(service.id=*)"
argument_list|)
decl_stmt|;
comment|// any service
name|GuardingFindHook
name|gfh
init|=
operator|new
name|GuardingFindHook
argument_list|(
name|hookBC
argument_list|,
name|gpc
argument_list|,
name|serviceFilter
argument_list|)
decl_stmt|;
name|BundleContext
name|clientBC
init|=
name|mockBundleContext
argument_list|(
literal|31L
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|,
literal|16L
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|GuardProxyCatalog
operator|.
name|PROXY_SERVICE_KEY
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|sref
init|=
name|mockServiceReference
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
name|refs
init|=
operator|new
name|ArrayList
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|refs
operator|.
name|add
argument_list|(
name|sref
argument_list|)
expr_stmt|;
name|gfh
operator|.
name|find
argument_list|(
name|clientBC
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
name|refs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No proxy should have been created for the proxy find"
argument_list|,
literal|0
argument_list|,
name|gpc
operator|.
name|proxyMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"As the proxy is for this bundle is should be visible and remain on the list"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|sref
argument_list|)
argument_list|,
name|refs
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNullFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|BundleContext
name|hookBC
init|=
name|mockBundleContext
argument_list|(
literal|5L
argument_list|)
decl_stmt|;
name|GuardProxyCatalog
name|gpc
init|=
operator|new
name|GuardProxyCatalog
argument_list|(
name|hookBC
argument_list|)
decl_stmt|;
name|GuardingFindHook
name|gfh
init|=
operator|new
name|GuardingFindHook
argument_list|(
name|hookBC
argument_list|,
name|gpc
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|gfh
operator|.
name|find
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// should just do nothing
block|}
specifier|private
name|BundleContext
name|mockBundleContext
parameter_list|(
name|long
name|id
parameter_list|)
throws|throws
name|Exception
block|{
name|Bundle
name|bundle
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|id
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|BundleContext
name|bc
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bc
operator|.
name|getBundle
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundle
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bc
operator|.
name|createFilter
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andAnswer
argument_list|(
operator|new
name|IAnswer
argument_list|<
name|Filter
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Filter
name|answer
parameter_list|()
throws|throws
name|Throwable
block|{
name|Filter
name|filter
init|=
name|FrameworkUtil
operator|.
name|createFilter
argument_list|(
operator|(
name|String
operator|)
name|EasyMock
operator|.
name|getCurrentArguments
argument_list|()
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
return|return
name|filter
return|;
block|}
block|}
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bc
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bundle
operator|.
name|getBundleContext
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bc
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
return|return
name|bc
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|private
name|BundleContext
name|mockConfigAdminBundleContext
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
modifier|...
name|configs
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
name|Configuration
index|[]
name|configurations
init|=
operator|new
name|Configuration
index|[
name|configs
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|configs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Configuration
name|conf
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|conf
operator|.
name|getProperties
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|configs
index|[
name|i
index|]
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|conf
operator|.
name|getPid
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|(
name|String
operator|)
name|configs
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|SERVICE_PID
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|configurations
index|[
name|i
index|]
operator|=
name|conf
expr_stmt|;
block|}
name|ConfigurationAdmin
name|ca
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ca
operator|.
name|listConfigurations
argument_list|(
literal|"(&(service.pid=org.apache.karaf.service.acl.*)(service.guard=*))"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|configurations
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|ca
argument_list|)
expr_stmt|;
specifier|final
name|ServiceReference
name|caSR
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ServiceReference
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|caSR
argument_list|)
expr_stmt|;
name|Bundle
name|b
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|b
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|877342449L
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|BundleContext
name|bc
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bc
operator|.
name|getBundle
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|b
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bc
operator|.
name|createFilter
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andAnswer
argument_list|(
operator|new
name|IAnswer
argument_list|<
name|Filter
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Filter
name|answer
parameter_list|()
throws|throws
name|Throwable
block|{
return|return
name|FrameworkUtil
operator|.
name|createFilter
argument_list|(
operator|(
name|String
operator|)
name|EasyMock
operator|.
name|getCurrentArguments
argument_list|()
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|String
name|cmFilter
init|=
literal|"(&(objectClass="
operator|+
name|ConfigurationAdmin
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|")"
operator|+
literal|"(!("
operator|+
name|GuardProxyCatalog
operator|.
name|PROXY_SERVICE_KEY
operator|+
literal|"=*)))"
decl_stmt|;
name|bc
operator|.
name|addServiceListener
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|ServiceListener
operator|.
name|class
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|cmFilter
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bc
operator|.
name|getServiceReferences
argument_list|(
name|EasyMock
operator|.
name|anyObject
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|cmFilter
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|caSR
block|}
block|)
function|.anyTimes
parameter_list|()
function|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bc
operator|.
name|getService
argument_list|(
name|caSR
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ca
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
parameter_list|(
name|bc
parameter_list|)
constructor_decl|;
return|return
name|bc
return|;
block|}
end_class

begin_function
specifier|private
name|ServiceReference
argument_list|<
name|Object
argument_list|>
name|mockServiceReference
parameter_list|(
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|ServiceReference
argument_list|<
name|Object
argument_list|>
name|sr
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ServiceReference
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Make sure the properties are 'live' in that if they change the reference changes too
name|EasyMock
operator|.
name|expect
argument_list|(
name|sr
operator|.
name|getPropertyKeys
argument_list|()
argument_list|)
operator|.
name|andAnswer
argument_list|(
operator|new
name|IAnswer
argument_list|<
name|String
index|[]
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
index|[]
name|answer
parameter_list|()
throws|throws
name|Throwable
block|{
return|return
name|Collections
operator|.
name|list
argument_list|(
name|props
operator|.
name|keys
argument_list|()
argument_list|)
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sr
operator|.
name|getProperty
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andAnswer
argument_list|(
operator|new
name|IAnswer
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|answer
parameter_list|()
throws|throws
name|Throwable
block|{
return|return
name|props
operator|.
name|get
argument_list|(
name|EasyMock
operator|.
name|getCurrentArguments
argument_list|()
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|sr
argument_list|)
expr_stmt|;
return|return
name|sr
return|;
block|}
end_function

unit|}
end_unit

