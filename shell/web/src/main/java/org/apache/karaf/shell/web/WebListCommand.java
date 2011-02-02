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
name|shell
operator|.
name|web
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Command
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
name|console
operator|.
name|OsgiCommandSupport
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
operator|.
name|WebTopic
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
name|Constants
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
name|startlevel
operator|.
name|StartLevel
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"web"
argument_list|,
name|name
operator|=
literal|"list"
argument_list|,
name|description
operator|=
literal|"Lists details for war bundles."
argument_list|)
specifier|public
class|class
name|WebListCommand
extends|extends
name|OsgiCommandSupport
block|{
specifier|private
name|StartLevel
name|startLevelService
decl_stmt|;
specifier|private
name|WebEventHandler
name|eventHandler
decl_stmt|;
comment|/* (non-Javadoc) 	 * @see org.apache.karaf.shell.war.WarCommandSupport#doExecute(org.osgi.service.packageadmin.PackageAdmin) 	 */
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|()
block|{
name|Bundle
index|[]
name|bundles
init|=
name|getBundleContext
argument_list|()
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
name|eventHandler
operator|.
name|getBundleEvents
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundles
operator|!=
literal|null
condition|)
block|{
name|String
name|level
init|=
operator|(
name|startLevelService
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
literal|"  Level "
decl_stmt|;
name|String
name|webState
init|=
operator|(
name|bundleEvents
operator|==
literal|null
operator|||
name|bundleEvents
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
literal|""
else|:
literal|"  Web-State     "
decl_stmt|;
name|String
name|headers
init|=
literal|"   ID   State       "
decl_stmt|;
name|headers
operator|+=
name|webState
operator|+
name|level
operator|+
literal|" Web-ContextPath           Name"
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|headers
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bundles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|//First check if this bundle contains  a webapp ctxt
name|String
name|webappctxt
init|=
operator|(
name|String
operator|)
name|bundles
index|[
name|i
index|]
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
name|webappctxt
operator|==
literal|null
condition|)
name|webappctxt
operator|=
operator|(
name|String
operator|)
name|bundles
index|[
name|i
index|]
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"Webapp-Context"
argument_list|)
expr_stmt|;
comment|//this one is used by pax-web but is deprecated.
if|if
condition|(
name|webappctxt
operator|==
literal|null
condition|)
continue|continue;
comment|//only list war archives.
name|webappctxt
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|// Get the bundle name or location.
name|String
name|name
init|=
operator|(
name|String
operator|)
name|bundles
index|[
name|i
index|]
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
comment|// If there is no name, then default to symbolic name.
name|name
operator|=
operator|(
name|name
operator|==
literal|null
operator|)
condition|?
name|bundles
index|[
name|i
index|]
operator|.
name|getSymbolicName
argument_list|()
else|:
name|name
expr_stmt|;
comment|// If there is no symbolic name, resort to location.
name|name
operator|=
operator|(
name|name
operator|==
literal|null
operator|)
condition|?
name|bundles
index|[
name|i
index|]
operator|.
name|getLocation
argument_list|()
else|:
name|name
expr_stmt|;
comment|// Show bundle version if not showing location.
name|String
name|version
init|=
operator|(
name|String
operator|)
name|bundles
index|[
name|i
index|]
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
name|l
init|=
name|bundles
index|[
name|i
index|]
operator|.
name|getBundleId
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|l
argument_list|)
decl_stmt|;
if|if
condition|(
name|startLevelService
operator|==
literal|null
condition|)
block|{
name|level
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|level
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|startLevelService
operator|.
name|getBundleStartLevel
argument_list|(
name|bundles
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|level
operator|.
name|length
argument_list|()
operator|<
literal|5
condition|)
block|{
name|level
operator|=
literal|" "
operator|+
name|level
expr_stmt|;
block|}
while|while
condition|(
name|id
operator|.
name|length
argument_list|()
operator|<
literal|4
condition|)
block|{
name|id
operator|=
literal|" "
operator|+
name|id
expr_stmt|;
block|}
comment|//prepend ctxt with slash (looks better)
if|if
condition|(
operator|!
name|webappctxt
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
name|webappctxt
operator|=
literal|"/"
operator|+
name|webappctxt
expr_stmt|;
while|while
condition|(
name|webappctxt
operator|.
name|length
argument_list|()
operator|<
literal|24
condition|)
block|{
name|webappctxt
operator|+=
literal|" "
expr_stmt|;
block|}
name|String
name|line
init|=
literal|"["
operator|+
name|id
operator|+
literal|"] ["
operator|+
name|getStateString
argument_list|(
name|bundles
index|[
name|i
index|]
argument_list|)
operator|+
literal|"]"
decl_stmt|;
if|if
condition|(
name|bundleEvents
operator|!=
literal|null
operator|&&
operator|!
name|bundleEvents
operator|.
name|isEmpty
argument_list|()
condition|)
name|line
operator|+=
literal|" ["
operator|+
name|getWebStateString
argument_list|(
name|bundles
index|[
name|i
index|]
argument_list|)
operator|+
literal|"] "
expr_stmt|;
name|line
operator|+=
literal|" ["
operator|+
name|level
operator|+
literal|"] ["
operator|+
name|webappctxt
operator|+
literal|"] "
operator|+
name|name
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
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
specifier|public
name|String
name|getWebStateString
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|long
name|bundleId
init|=
name|bundle
operator|.
name|getBundleId
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
name|eventHandler
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
literal|"Unknown    "
expr_stmt|;
name|topic
operator|=
literal|"Failed     "
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
comment|/** 	 * @param startLevelService the startLevelService to set 	 */
specifier|public
name|void
name|setStartLevelService
parameter_list|(
name|StartLevel
name|startLevelService
parameter_list|)
block|{
name|this
operator|.
name|startLevelService
operator|=
name|startLevelService
expr_stmt|;
block|}
comment|/** 	 * @return the startLevelService 	 */
specifier|public
name|StartLevel
name|getStartLevelService
parameter_list|()
block|{
return|return
name|startLevelService
return|;
block|}
comment|/** 	 * @param eventHandler the eventHandler to set 	 */
specifier|public
name|void
name|setEventHandler
parameter_list|(
name|WebEventHandler
name|eventHandler
parameter_list|)
block|{
name|this
operator|.
name|eventHandler
operator|=
name|eventHandler
expr_stmt|;
block|}
comment|/** 	 * @return the eventHandler 	 */
specifier|public
name|WebEventHandler
name|getEventHandler
parameter_list|()
block|{
return|return
name|eventHandler
return|;
block|}
block|}
end_class

end_unit

