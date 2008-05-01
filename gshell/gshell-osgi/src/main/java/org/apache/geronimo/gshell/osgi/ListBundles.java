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
name|geronimo
operator|.
name|gshell
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|clp
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|annotation
operator|.
name|CommandComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|support
operator|.
name|OsgiCommandSupport
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
name|startlevel
operator|.
name|StartLevel
import|;
end_import

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: gnodet  * Date: Oct 3, 2007  * Time: 9:44:20 AM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
annotation|@
name|CommandComponent
argument_list|(
name|id
operator|=
literal|"osgi:list-bundles"
argument_list|,
name|description
operator|=
literal|"List bundles"
argument_list|)
specifier|public
class|class
name|ListBundles
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-l"
argument_list|,
name|description
operator|=
literal|"Show locations"
argument_list|)
name|boolean
name|showLoc
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-s"
argument_list|,
name|description
operator|=
literal|"Show symbolic name"
argument_list|)
name|boolean
name|showSymbolic
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|description
operator|=
literal|"Show update"
argument_list|)
name|boolean
name|showUpdate
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceReference
name|ref
init|=
name|getBundleContext
argument_list|()
operator|.
name|getServiceReference
argument_list|(
name|StartLevel
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|StartLevel
name|sl
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|sl
operator|=
operator|(
name|StartLevel
operator|)
name|getBundleContext
argument_list|()
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sl
operator|==
literal|null
condition|)
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"StartLevel service is unavailable."
argument_list|)
expr_stmt|;
block|}
try|try
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
if|if
condition|(
name|bundles
operator|!=
literal|null
condition|)
block|{
comment|// Display active start level.
if|if
condition|(
name|sl
operator|!=
literal|null
condition|)
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"START LEVEL "
operator|+
name|sl
operator|.
name|getStartLevel
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Print column headers.
name|String
name|msg
init|=
literal|" Name"
decl_stmt|;
if|if
condition|(
name|showLoc
condition|)
block|{
name|msg
operator|=
literal|" Location"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|showSymbolic
condition|)
block|{
name|msg
operator|=
literal|" Symbolic name"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|showUpdate
condition|)
block|{
name|msg
operator|=
literal|" Update location"
expr_stmt|;
block|}
name|String
name|level
init|=
operator|(
name|sl
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
literal|"  Level "
decl_stmt|;
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   ID "
operator|+
literal|"  State       "
operator|+
name|level
operator|+
name|msg
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
comment|// Overwrite the default value is the user specifically
comment|// requested to display one or the other.
if|if
condition|(
name|showLoc
condition|)
block|{
name|name
operator|=
name|bundles
index|[
name|i
index|]
operator|.
name|getLocation
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|showSymbolic
condition|)
block|{
name|name
operator|=
name|bundles
index|[
name|i
index|]
operator|.
name|getSymbolicName
argument_list|()
expr_stmt|;
name|name
operator|=
operator|(
name|name
operator|==
literal|null
operator|)
condition|?
literal|"<no symbolic name>"
else|:
name|name
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|showUpdate
condition|)
block|{
name|name
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
name|Constants
operator|.
name|BUNDLE_UPDATELOCATION
argument_list|)
expr_stmt|;
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
block|}
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
operator|!
name|showLoc
operator|&&
operator|!
name|showUpdate
operator|&&
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
name|sl
operator|==
literal|null
condition|)
block|{
name|level
operator|=
literal|"1"
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
name|sl
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
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
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
operator|.
name|getState
argument_list|()
argument_list|)
operator|+
literal|"] ["
operator|+
name|level
operator|+
literal|"] "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"There are no installed bundles."
argument_list|)
expr_stmt|;
block|}
return|return
name|SUCCESS
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
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
specifier|public
name|String
name|getStateString
parameter_list|(
name|int
name|i
parameter_list|)
block|{
if|if
condition|(
name|i
operator|==
name|Bundle
operator|.
name|ACTIVE
condition|)
return|return
literal|"Active     "
return|;
elseif|else
if|if
condition|(
name|i
operator|==
name|Bundle
operator|.
name|INSTALLED
condition|)
return|return
literal|"Installed  "
return|;
elseif|else
if|if
condition|(
name|i
operator|==
name|Bundle
operator|.
name|RESOLVED
condition|)
return|return
literal|"Resolved   "
return|;
elseif|else
if|if
condition|(
name|i
operator|==
name|Bundle
operator|.
name|STARTING
condition|)
return|return
literal|"Starting   "
return|;
elseif|else
if|if
condition|(
name|i
operator|==
name|Bundle
operator|.
name|STOPPING
condition|)
return|return
literal|"Stopping   "
return|;
return|return
literal|"Unknown    "
return|;
block|}
block|}
end_class

end_unit

