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
name|web
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
name|web
operator|.
name|WebBundle
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
name|web
operator|.
name|WebContainerService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|WarManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|WebEvent
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
name|startlevel
operator|.
name|BundleStartLevel
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
name|List
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
comment|/**  * Implementation of the WebContainer service.  */
end_comment

begin_class
specifier|public
class|class
name|WebContainerServiceImpl
implements|implements
name|WebContainerService
block|{
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|WebEventHandler
name|webEventHandler
decl_stmt|;
specifier|private
name|WarManager
name|warManager
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|WebContainerServiceImpl
operator|.
name|class
argument_list|)
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
name|void
name|setWebEventHandler
parameter_list|(
name|WebEventHandler
name|webEventHandler
parameter_list|)
block|{
name|this
operator|.
name|webEventHandler
operator|=
name|webEventHandler
expr_stmt|;
block|}
specifier|public
name|void
name|setWarManager
parameter_list|(
name|WarManager
name|warManager
parameter_list|)
block|{
name|this
operator|.
name|warManager
operator|=
name|warManager
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|WebBundle
argument_list|>
name|list
parameter_list|()
throws|throws
name|Exception
block|{
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Long
argument_list|,
name|WebEvent
argument_list|>
name|bundleEvents
init|=
name|webEventHandler
operator|.
name|getBundleEvents
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WebBundle
argument_list|>
name|webBundles
init|=
operator|new
name|ArrayList
argument_list|<
name|WebBundle
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
comment|// first check if the bundle is a web bundle
name|String
name|contextPath
init|=
operator|(
name|String
operator|)
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"Web-ContextPath"
argument_list|)
decl_stmt|;
if|if
condition|(
name|contextPath
operator|==
literal|null
condition|)
block|{
name|contextPath
operator|=
operator|(
name|String
operator|)
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"Webapp-Context"
argument_list|)
expr_stmt|;
comment|// this one used by pax-web but is deprecated
block|}
if|if
condition|(
name|contextPath
operator|==
literal|null
condition|)
block|{
comment|// the bundle is not a web bundle
continue|continue;
block|}
name|WebBundle
name|webBundle
init|=
operator|new
name|WebBundle
argument_list|()
decl_stmt|;
name|contextPath
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|// get the bundle name
name|String
name|name
init|=
operator|(
name|String
operator|)
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|BUNDLE_NAME
argument_list|)
decl_stmt|;
comment|// if there is no name, then default to symbolic name
name|name
operator|=
operator|(
name|name
operator|==
literal|null
operator|)
condition|?
name|bundle
operator|.
name|getSymbolicName
argument_list|()
else|:
name|name
expr_stmt|;
comment|// if there is no symbolic name, resort to location
name|name
operator|=
operator|(
name|name
operator|==
literal|null
operator|)
condition|?
name|bundle
operator|.
name|getLocation
argument_list|()
else|:
name|name
expr_stmt|;
comment|// get the bundle version
name|String
name|version
init|=
operator|(
name|String
operator|)
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|)
decl_stmt|;
name|name
operator|=
operator|(
operator|(
name|version
operator|!=
literal|null
operator|)
operator|)
condition|?
name|name
operator|+
literal|" ("
operator|+
name|version
operator|+
literal|")"
else|:
name|name
expr_stmt|;
name|long
name|bundleId
init|=
name|bundle
operator|.
name|getBundleId
argument_list|()
decl_stmt|;
name|int
name|level
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleStartLevel
operator|.
name|class
argument_list|)
operator|.
name|getStartLevel
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|contextPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|contextPath
operator|=
literal|"/"
operator|+
name|contextPath
expr_stmt|;
block|}
name|webBundle
operator|.
name|setBundleId
argument_list|(
name|bundleId
argument_list|)
expr_stmt|;
name|webBundle
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|webBundle
operator|.
name|setContextPath
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|webBundle
operator|.
name|setLevel
argument_list|(
name|level
argument_list|)
expr_stmt|;
name|webBundle
operator|.
name|setState
argument_list|(
name|getStateString
argument_list|(
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
name|webBundle
operator|.
name|setWebState
argument_list|(
name|state
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|webBundles
operator|.
name|add
argument_list|(
name|webBundle
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|webBundles
return|;
block|}
specifier|public
name|void
name|start
parameter_list|(
name|List
argument_list|<
name|Long
argument_list|>
name|bundleIds
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|bundleIds
operator|!=
literal|null
operator|&&
operator|!
name|bundleIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|long
name|bundleId
range|:
name|bundleIds
control|)
block|{
if|if
condition|(
name|webEventHandler
operator|.
name|getBundleEvents
argument_list|()
operator|.
name|containsKey
argument_list|(
name|bundleId
argument_list|)
condition|)
block|{
name|WebEvent
name|webEvent
init|=
name|webEventHandler
operator|.
name|getBundleEvents
argument_list|()
operator|.
name|get
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|webEvent
operator|.
name|getBundle
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundle
operator|!=
literal|null
condition|)
block|{
comment|// deploy
name|warManager
operator|.
name|start
argument_list|(
name|bundleId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Bundle ID "
operator|+
name|bundleId
operator|+
literal|" is invalid"
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Bundle ID {} is invalid"
argument_list|,
name|bundleId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|(
name|List
argument_list|<
name|Long
argument_list|>
name|bundleIds
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|bundleIds
operator|!=
literal|null
operator|&&
operator|!
name|bundleIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|long
name|bundleId
range|:
name|bundleIds
control|)
block|{
if|if
condition|(
name|webEventHandler
operator|.
name|getBundleEvents
argument_list|()
operator|.
name|containsKey
argument_list|(
name|bundleId
argument_list|)
condition|)
block|{
name|WebEvent
name|webEvent
init|=
name|webEventHandler
operator|.
name|getBundleEvents
argument_list|()
operator|.
name|get
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|webEvent
operator|.
name|getBundle
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundle
operator|!=
literal|null
condition|)
block|{
comment|// deploy
name|warManager
operator|.
name|stop
argument_list|(
name|bundleId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Bundle ID "
operator|+
name|bundleId
operator|+
literal|" is invalid"
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Bundle ID {} is invalid"
argument_list|,
name|bundleId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|public
name|String
name|state
parameter_list|(
name|long
name|bundleId
parameter_list|)
block|{
name|Map
argument_list|<
name|Long
argument_list|,
name|WebEvent
argument_list|>
name|bundleEvents
init|=
name|webEventHandler
operator|.
name|getBundleEvents
argument_list|()
decl_stmt|;
name|String
name|topic
init|=
literal|"Unknown    "
decl_stmt|;
if|if
condition|(
name|bundleEvents
operator|.
name|containsKey
argument_list|(
name|bundleId
argument_list|)
condition|)
block|{
name|WebEvent
name|webEvent
init|=
name|bundleEvents
operator|.
name|get
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|webEvent
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|WebEvent
operator|.
name|DEPLOYING
case|:
name|topic
operator|=
literal|"Deploying  "
expr_stmt|;
break|break;
case|case
name|WebEvent
operator|.
name|DEPLOYED
case|:
name|topic
operator|=
literal|"Deployed   "
expr_stmt|;
break|break;
case|case
name|WebEvent
operator|.
name|UNDEPLOYING
case|:
name|topic
operator|=
literal|"Undeploying"
expr_stmt|;
break|break;
case|case
name|WebEvent
operator|.
name|UNDEPLOYED
case|:
name|topic
operator|=
literal|"Undeployed "
expr_stmt|;
break|break;
case|case
name|WebEvent
operator|.
name|FAILED
case|:
name|topic
operator|=
literal|"Failed     "
expr_stmt|;
break|break;
case|case
name|WebEvent
operator|.
name|WAITING
case|:
name|topic
operator|=
literal|"Waiting    "
expr_stmt|;
break|break;
default|default:
name|topic
operator|=
literal|"Failed     "
expr_stmt|;
block|}
block|}
while|while
condition|(
name|topic
operator|.
name|length
argument_list|()
operator|<
literal|11
condition|)
block|{
name|topic
operator|+=
literal|" "
expr_stmt|;
block|}
return|return
name|topic
return|;
block|}
comment|/**      * Return a string representation of the bundle state.      *       * TODO use an util method provided by bundle core      *       * @param bundle the target bundle.      * @return the string representation of the state      */
specifier|private
name|String
name|getStateString
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|int
name|state
init|=
name|bundle
operator|.
name|getState
argument_list|()
decl_stmt|;
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|ACTIVE
condition|)
block|{
return|return
literal|"Active     "
return|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|INSTALLED
condition|)
block|{
return|return
literal|"Installed  "
return|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|RESOLVED
condition|)
block|{
return|return
literal|"Resolved   "
return|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|STARTING
condition|)
block|{
return|return
literal|"Starting   "
return|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|STOPPING
condition|)
block|{
return|return
literal|"Stopping   "
return|;
block|}
else|else
block|{
return|return
literal|"Unknown    "
return|;
block|}
block|}
block|}
end_class

end_unit

