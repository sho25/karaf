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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|osgi
package|;
end_package

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
name|ConcurrentHashMap
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
name|BundleListener
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
name|blueprint
operator|.
name|container
operator|.
name|BlueprintEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  *  * TODO: use event admin to receive WAIT topics notifications from blueprint extender  *  */
end_comment

begin_class
specifier|public
class|class
name|BlueprintListener
implements|implements
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintListener
implements|,
name|BundleListener
implements|,
name|BundleStateListener
implements|,
name|BundleStateListener
operator|.
name|Factory
block|{
specifier|public
specifier|static
enum|enum
name|BlueprintState
block|{
name|Unknown
block|,
name|Creating
block|,
name|Created
block|,
name|Destroying
block|,
name|Destroyed
block|,
name|Failure
block|,
name|GracePeriod
block|,
name|Waiting
block|}
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BlueprintListener
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Long
argument_list|,
name|BlueprintState
argument_list|>
name|states
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|public
name|BlueprintListener
parameter_list|()
block|{
name|this
operator|.
name|states
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Long
argument_list|,
name|BlueprintState
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"Blueprint   "
return|;
block|}
specifier|public
name|String
name|getState
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|BlueprintState
name|state
init|=
name|states
operator|.
name|get
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|==
literal|null
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|!=
name|Bundle
operator|.
name|ACTIVE
operator|||
name|state
operator|==
name|BlueprintState
operator|.
name|Unknown
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|state
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|BundleStateListener
name|getListener
parameter_list|()
block|{
return|return
name|this
return|;
block|}
specifier|public
name|BlueprintState
name|getBlueprintState
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|BlueprintState
name|state
init|=
name|states
operator|.
name|get
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|==
literal|null
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|!=
name|Bundle
operator|.
name|ACTIVE
condition|)
block|{
name|state
operator|=
name|BlueprintState
operator|.
name|Unknown
expr_stmt|;
block|}
return|return
name|state
return|;
block|}
specifier|public
name|void
name|blueprintEvent
parameter_list|(
name|BlueprintEvent
name|blueprintEvent
parameter_list|)
block|{
name|BlueprintState
name|state
init|=
name|getState
argument_list|(
name|blueprintEvent
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Blueprint app state changed to "
operator|+
name|state
operator|+
literal|" for bundle "
operator|+
name|blueprintEvent
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|states
operator|.
name|put
argument_list|(
name|blueprintEvent
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|state
argument_list|)
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
if|if
condition|(
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
name|states
operator|.
name|remove
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
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|bundleContext
operator|.
name|addBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|bundleContext
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|private
name|BlueprintState
name|getState
parameter_list|(
name|BlueprintEvent
name|blueprintEvent
parameter_list|)
block|{
switch|switch
condition|(
name|blueprintEvent
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|BlueprintEvent
operator|.
name|CREATING
case|:
return|return
name|BlueprintState
operator|.
name|Creating
return|;
case|case
name|BlueprintEvent
operator|.
name|CREATED
case|:
return|return
name|BlueprintState
operator|.
name|Created
return|;
case|case
name|BlueprintEvent
operator|.
name|DESTROYING
case|:
return|return
name|BlueprintState
operator|.
name|Destroying
return|;
case|case
name|BlueprintEvent
operator|.
name|DESTROYED
case|:
return|return
name|BlueprintState
operator|.
name|Destroyed
return|;
case|case
name|BlueprintEvent
operator|.
name|FAILURE
case|:
return|return
name|BlueprintState
operator|.
name|Failure
return|;
case|case
name|BlueprintEvent
operator|.
name|GRACE_PERIOD
case|:
return|return
name|BlueprintState
operator|.
name|GracePeriod
return|;
case|case
name|BlueprintEvent
operator|.
name|WAITING
case|:
return|return
name|BlueprintState
operator|.
name|Waiting
return|;
default|default:
return|return
name|BlueprintState
operator|.
name|Unknown
return|;
block|}
block|}
block|}
end_class

end_unit

