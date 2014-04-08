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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|startlevel
operator|.
name|BundleStartLevel
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_class
specifier|public
class|class
name|TestBase
block|{
specifier|public
name|Bundle
name|createDummyBundle
parameter_list|(
name|long
name|id
parameter_list|,
specifier|final
name|String
name|symbolicName
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|)
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
comment|// Be aware that this means all bundles are treated as different
name|expect
argument_list|(
name|bundle
operator|.
name|compareTo
argument_list|(
name|EasyMock
operator|.
expr|<
name|Bundle
operator|>
name|anyObject
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|1
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
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
name|expect
argument_list|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|symbolicName
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getHeaders
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|headers
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|BundleStartLevel
name|sl
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|BundleStartLevel
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|sl
operator|.
name|isPersistentlyStarted
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleStartLevel
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|sl
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|bundle
argument_list|,
name|sl
argument_list|)
expr_stmt|;
return|return
name|bundle
return|;
block|}
specifier|public
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|(
name|String
modifier|...
name|keyAndHeader
parameter_list|)
block|{
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headersTable
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|c
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|c
operator|<
name|keyAndHeader
operator|.
name|length
condition|)
block|{
name|String
name|key
init|=
name|keyAndHeader
index|[
name|c
operator|++
index|]
decl_stmt|;
name|String
name|value
init|=
name|keyAndHeader
index|[
name|c
operator|++
index|]
decl_stmt|;
name|headersTable
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|headersTable
return|;
block|}
specifier|public
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
parameter_list|(
name|Feature
modifier|...
name|features
parameter_list|)
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
name|featuresMap
init|=
operator|new
name|HashMap
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
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|features
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|featureVersion
init|=
name|getOrCreate
argument_list|(
name|featuresMap
argument_list|,
name|feature
argument_list|)
decl_stmt|;
name|featureVersion
operator|.
name|put
argument_list|(
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|,
name|feature
argument_list|)
expr_stmt|;
block|}
return|return
name|featuresMap
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|getOrCreate
parameter_list|(
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
name|featuresMap
parameter_list|,
name|Feature
name|feature
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|featureVersion
init|=
name|featuresMap
operator|.
name|get
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|featureVersion
operator|==
literal|null
condition|)
block|{
name|featureVersion
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|()
expr_stmt|;
name|featuresMap
operator|.
name|put
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|,
name|featureVersion
argument_list|)
expr_stmt|;
block|}
return|return
name|featureVersion
return|;
block|}
specifier|public
name|Feature
name|feature
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|feature
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Feature
name|feature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
operator|new
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
argument_list|(
name|name
argument_list|,
name|version
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|Bundle
argument_list|>
name|setOf
parameter_list|(
name|Bundle
modifier|...
name|elements
parameter_list|)
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|Bundle
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|Long
argument_list|>
name|setOf
parameter_list|(
name|Long
modifier|...
name|elements
parameter_list|)
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|Long
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|setOf
parameter_list|(
name|String
modifier|...
name|elements
parameter_list|)
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|Feature
argument_list|>
name|setOf
parameter_list|(
name|Feature
modifier|...
name|elements
parameter_list|)
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|Feature
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit
