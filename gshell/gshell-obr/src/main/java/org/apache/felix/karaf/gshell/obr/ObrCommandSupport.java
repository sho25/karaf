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
name|gshell
operator|.
name|obr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|karaf
operator|.
name|gshell
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
name|ServiceReference
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
name|Version
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
name|obr
operator|.
name|RepositoryAdmin
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
name|obr
operator|.
name|Requirement
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
name|obr
operator|.
name|Resolver
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
name|obr
operator|.
name|Resource
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|ObrCommandSupport
extends|extends
name|OsgiCommandSupport
block|{
specifier|protected
specifier|static
specifier|final
name|char
name|VERSION_DELIM
init|=
literal|','
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Get repository admin service.
name|ServiceReference
name|ref
init|=
name|getBundleContext
argument_list|()
operator|.
name|getServiceReference
argument_list|(
name|RepositoryAdmin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"RepositoryAdmin service is unavailable."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
try|try
block|{
name|RepositoryAdmin
name|admin
init|=
operator|(
name|RepositoryAdmin
operator|)
name|getBundleContext
argument_list|()
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|admin
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"RepositoryAdmin service is unavailable."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|doExecute
argument_list|(
name|admin
argument_list|)
expr_stmt|;
block|}
finally|finally
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
return|return
literal|null
return|;
block|}
specifier|protected
specifier|abstract
name|void
name|doExecute
parameter_list|(
name|RepositoryAdmin
name|admin
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|protected
name|Resource
index|[]
name|searchRepository
parameter_list|(
name|RepositoryAdmin
name|admin
parameter_list|,
name|String
name|targetId
parameter_list|,
name|String
name|targetVersion
parameter_list|)
block|{
comment|// Try to see if the targetId is a bundle ID.
try|try
block|{
name|Bundle
name|bundle
init|=
name|getBundleContext
argument_list|()
operator|.
name|getBundle
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|targetId
argument_list|)
argument_list|)
decl_stmt|;
name|targetId
operator|=
name|bundle
operator|.
name|getSymbolicName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|ex
parameter_list|)
block|{
comment|// It was not a number, so ignore.
block|}
comment|// The targetId may be a bundle name or a bundle symbolic name,
comment|// so create the appropriate LDAP query.
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"(|(presentationname="
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|targetId
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|")(symbolicname="
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|targetId
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"))"
argument_list|)
expr_stmt|;
if|if
condition|(
name|targetVersion
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
literal|"(&"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"(version="
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|targetVersion
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"))"
argument_list|)
expr_stmt|;
block|}
return|return
name|admin
operator|.
name|discoverResources
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Resource
name|selectNewestVersion
parameter_list|(
name|Resource
index|[]
name|resources
parameter_list|)
block|{
name|int
name|idx
init|=
operator|-
literal|1
decl_stmt|;
name|Version
name|v
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|(
name|resources
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|i
operator|<
name|resources
operator|.
name|length
operator|)
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|idx
operator|=
literal|0
expr_stmt|;
name|v
operator|=
name|resources
index|[
name|i
index|]
operator|.
name|getVersion
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Version
name|vtmp
init|=
name|resources
index|[
name|i
index|]
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|vtmp
operator|.
name|compareTo
argument_list|(
name|v
argument_list|)
operator|>
literal|0
condition|)
block|{
name|idx
operator|=
name|i
expr_stmt|;
name|v
operator|=
name|vtmp
expr_stmt|;
block|}
block|}
block|}
return|return
operator|(
name|idx
operator|<
literal|0
operator|)
condition|?
literal|null
else|:
name|resources
index|[
name|idx
index|]
return|;
block|}
specifier|protected
name|String
index|[]
name|getTarget
parameter_list|(
name|String
name|bundle
parameter_list|)
block|{
name|String
index|[]
name|target
decl_stmt|;
name|int
name|idx
init|=
name|bundle
operator|.
name|indexOf
argument_list|(
name|VERSION_DELIM
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|target
operator|=
operator|new
name|String
index|[]
block|{
name|bundle
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
block|,
name|bundle
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
block|}
expr_stmt|;
block|}
else|else
block|{
name|target
operator|=
operator|new
name|String
index|[]
block|{
name|bundle
block|,
literal|null
block|}
expr_stmt|;
block|}
return|return
name|target
return|;
block|}
specifier|protected
name|void
name|printUnderline
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|int
name|length
parameter_list|)
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
name|length
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doDeploy
parameter_list|(
name|RepositoryAdmin
name|admin
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|bundles
parameter_list|,
name|boolean
name|start
parameter_list|)
throws|throws
name|Exception
block|{
name|Resolver
name|resolver
init|=
name|admin
operator|.
name|resolver
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|bundle
range|:
name|bundles
control|)
block|{
name|String
index|[]
name|target
init|=
name|getTarget
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|Resource
name|resource
init|=
name|selectNewestVersion
argument_list|(
name|searchRepository
argument_list|(
name|admin
argument_list|,
name|target
index|[
literal|0
index|]
argument_list|,
name|target
index|[
literal|1
index|]
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|!=
literal|null
condition|)
block|{
name|resolver
operator|.
name|add
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unknown bundle - "
operator|+
name|target
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|(
name|resolver
operator|.
name|getAddedResources
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|resolver
operator|.
name|getAddedResources
argument_list|()
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
if|if
condition|(
name|resolver
operator|.
name|resolve
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Target resource(s):"
argument_list|)
expr_stmt|;
name|printUnderline
argument_list|(
name|System
operator|.
name|out
argument_list|,
literal|19
argument_list|)
expr_stmt|;
name|Resource
index|[]
name|resources
init|=
name|resolver
operator|.
name|getAddedResources
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|resIdx
init|=
literal|0
init|;
operator|(
name|resources
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|resIdx
operator|<
name|resources
operator|.
name|length
operator|)
condition|;
name|resIdx
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|resources
index|[
name|resIdx
index|]
operator|.
name|getPresentationName
argument_list|()
operator|+
literal|" ("
operator|+
name|resources
index|[
name|resIdx
index|]
operator|.
name|getVersion
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
name|resources
operator|=
name|resolver
operator|.
name|getRequiredResources
argument_list|()
expr_stmt|;
if|if
condition|(
operator|(
name|resources
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|resources
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\nRequired resource(s):"
argument_list|)
expr_stmt|;
name|printUnderline
argument_list|(
name|System
operator|.
name|out
argument_list|,
literal|21
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|resIdx
init|=
literal|0
init|;
name|resIdx
operator|<
name|resources
operator|.
name|length
condition|;
name|resIdx
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|resources
index|[
name|resIdx
index|]
operator|.
name|getPresentationName
argument_list|()
operator|+
literal|" ("
operator|+
name|resources
index|[
name|resIdx
index|]
operator|.
name|getVersion
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
name|resources
operator|=
name|resolver
operator|.
name|getOptionalResources
argument_list|()
expr_stmt|;
if|if
condition|(
operator|(
name|resources
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|resources
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\nOptional resource(s):"
argument_list|)
expr_stmt|;
name|printUnderline
argument_list|(
name|System
operator|.
name|out
argument_list|,
literal|21
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|resIdx
init|=
literal|0
init|;
name|resIdx
operator|<
name|resources
operator|.
name|length
condition|;
name|resIdx
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|resources
index|[
name|resIdx
index|]
operator|.
name|getPresentationName
argument_list|()
operator|+
literal|" ("
operator|+
name|resources
index|[
name|resIdx
index|]
operator|.
name|getVersion
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"\nDeploying..."
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|deploy
argument_list|(
name|start
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Requirement
index|[]
name|reqs
init|=
name|resolver
operator|.
name|getUnsatisfiedRequirements
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|reqs
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|reqs
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Unsatisfied requirement(s):"
argument_list|)
expr_stmt|;
name|printUnderline
argument_list|(
name|System
operator|.
name|out
argument_list|,
literal|27
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|reqIdx
init|=
literal|0
init|;
name|reqIdx
operator|<
name|reqs
operator|.
name|length
condition|;
name|reqIdx
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|reqs
index|[
name|reqIdx
index|]
operator|.
name|getFilter
argument_list|()
argument_list|)
expr_stmt|;
name|Resource
index|[]
name|resources
init|=
name|resolver
operator|.
name|getResources
argument_list|(
name|reqs
index|[
name|reqIdx
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|resIdx
init|=
literal|0
init|;
name|resIdx
operator|<
name|resources
operator|.
name|length
condition|;
name|resIdx
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"      "
operator|+
name|resources
index|[
name|resIdx
index|]
operator|.
name|getPresentationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Could not resolve targets."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

