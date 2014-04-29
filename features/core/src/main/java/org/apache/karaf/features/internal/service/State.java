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
operator|.
name|internal
operator|.
name|service
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
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|atomic
operator|.
name|AtomicBoolean
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
name|features
operator|.
name|internal
operator|.
name|util
operator|.
name|MapUtils
import|;
end_import

begin_class
specifier|public
class|class
name|State
block|{
specifier|public
specifier|final
name|AtomicBoolean
name|bootDone
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
specifier|public
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|repositories
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|requestedFeatures
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|installedFeatures
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|stateFeatures
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
name|managedBundles
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
specifier|final
name|Map
argument_list|<
name|Long
argument_list|,
name|Long
argument_list|>
name|bundleChecksums
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|State
name|copy
parameter_list|()
block|{
name|State
name|state
init|=
operator|new
name|State
argument_list|()
decl_stmt|;
name|state
operator|.
name|bootDone
operator|.
name|set
argument_list|(
name|bootDone
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|MapUtils
operator|.
name|copy
argument_list|(
name|repositories
argument_list|,
name|state
operator|.
name|repositories
argument_list|)
expr_stmt|;
name|MapUtils
operator|.
name|copy
argument_list|(
name|requestedFeatures
argument_list|,
name|state
operator|.
name|requestedFeatures
argument_list|)
expr_stmt|;
name|MapUtils
operator|.
name|copy
argument_list|(
name|installedFeatures
argument_list|,
name|state
operator|.
name|installedFeatures
argument_list|)
expr_stmt|;
name|MapUtils
operator|.
name|copy
argument_list|(
name|stateFeatures
argument_list|,
name|state
operator|.
name|stateFeatures
argument_list|)
expr_stmt|;
name|MapUtils
operator|.
name|copy
argument_list|(
name|managedBundles
argument_list|,
name|state
operator|.
name|managedBundles
argument_list|)
expr_stmt|;
name|MapUtils
operator|.
name|copy
argument_list|(
name|bundleChecksums
argument_list|,
name|state
operator|.
name|bundleChecksums
argument_list|)
expr_stmt|;
return|return
name|state
return|;
block|}
block|}
end_class

end_unit

