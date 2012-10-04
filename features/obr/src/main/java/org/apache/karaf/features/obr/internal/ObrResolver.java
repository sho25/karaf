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
name|obr
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Arrays
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
name|HashMap
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|bundlerepository
operator|.
name|Reason
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
name|bundlerepository
operator|.
name|Repository
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
name|bundlerepository
operator|.
name|RepositoryAdmin
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
name|bundlerepository
operator|.
name|Requirement
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
name|bundlerepository
operator|.
name|Resource
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
name|BundleInfo
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
name|Feature
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
name|Resolver
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

begin_class
specifier|public
class|class
name|ObrResolver
implements|implements
name|Resolver
block|{
specifier|private
name|RepositoryAdmin
name|repositoryAdmin
decl_stmt|;
specifier|private
name|boolean
name|resolveOptionalImports
decl_stmt|;
specifier|private
name|boolean
name|startByDefault
decl_stmt|;
specifier|private
name|int
name|startLevel
decl_stmt|;
specifier|public
name|RepositoryAdmin
name|getRepositoryAdmin
parameter_list|()
block|{
return|return
name|repositoryAdmin
return|;
block|}
specifier|public
name|void
name|setRepositoryAdmin
parameter_list|(
name|RepositoryAdmin
name|repositoryAdmin
parameter_list|)
block|{
name|this
operator|.
name|repositoryAdmin
operator|=
name|repositoryAdmin
expr_stmt|;
block|}
specifier|public
name|boolean
name|isResolveOptionalImports
parameter_list|()
block|{
return|return
name|resolveOptionalImports
return|;
block|}
comment|/**      * When set to<code>true</code>, the OBR resolver will try to resolve optional imports as well.      * Defaults to<code>false</code>      *      * @param resolveOptionalImports      */
specifier|public
name|void
name|setResolveOptionalImports
parameter_list|(
name|boolean
name|resolveOptionalImports
parameter_list|)
block|{
name|this
operator|.
name|resolveOptionalImports
operator|=
name|resolveOptionalImports
expr_stmt|;
block|}
specifier|public
name|void
name|setStartByDefault
parameter_list|(
name|boolean
name|startByDefault
parameter_list|)
block|{
name|this
operator|.
name|startByDefault
operator|=
name|startByDefault
expr_stmt|;
block|}
specifier|public
name|void
name|setStartLevel
parameter_list|(
name|int
name|startLevel
parameter_list|)
block|{
name|this
operator|.
name|startLevel
operator|=
name|startLevel
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|resolve
parameter_list|(
name|Feature
name|feature
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Requirement
argument_list|>
name|reqs
init|=
operator|new
name|ArrayList
argument_list|<
name|Requirement
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Resource
argument_list|>
name|ress
init|=
operator|new
name|ArrayList
argument_list|<
name|Resource
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Resource
argument_list|>
name|featureDeploy
init|=
operator|new
name|ArrayList
argument_list|<
name|Resource
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|BundleInfo
argument_list|>
name|infos
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|BundleInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|BundleInfo
name|bundleInfo
range|:
name|feature
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|bundleInfo
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|Requirement
name|req
init|=
name|parseRequirement
argument_list|(
name|bundleInfo
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|reqs
operator|.
name|add
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|infos
operator|.
name|put
argument_list|(
name|req
argument_list|,
name|bundleInfo
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|Resource
name|res
init|=
name|repositoryAdmin
operator|.
name|getHelper
argument_list|()
operator|.
name|createResource
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|ress
operator|.
name|add
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|infos
operator|.
name|put
argument_list|(
name|res
argument_list|,
name|bundleInfo
argument_list|)
expr_stmt|;
block|}
block|}
name|Repository
name|repository
init|=
name|repositoryAdmin
operator|.
name|getHelper
argument_list|()
operator|.
name|repository
argument_list|(
name|ress
operator|.
name|toArray
argument_list|(
operator|new
name|Resource
index|[
name|ress
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Repository
argument_list|>
name|repos
init|=
operator|new
name|ArrayList
argument_list|<
name|Repository
argument_list|>
argument_list|()
decl_stmt|;
name|repos
operator|.
name|add
argument_list|(
name|repositoryAdmin
operator|.
name|getSystemRepository
argument_list|()
argument_list|)
expr_stmt|;
name|repos
operator|.
name|add
argument_list|(
name|repositoryAdmin
operator|.
name|getLocalRepository
argument_list|()
argument_list|)
expr_stmt|;
name|repos
operator|.
name|add
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|repos
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repositoryAdmin
operator|.
name|listRepositories
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|bundlerepository
operator|.
name|Resolver
name|resolver
init|=
name|repositoryAdmin
operator|.
name|resolver
argument_list|(
name|repos
operator|.
name|toArray
argument_list|(
operator|new
name|Repository
index|[
name|repos
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Resource
name|res
range|:
name|ress
control|)
block|{
if|if
condition|(
operator|!
name|infos
operator|.
name|get
argument_list|(
name|res
argument_list|)
operator|.
name|isDependency
argument_list|()
condition|)
block|{
name|resolver
operator|.
name|add
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Requirement
name|req
range|:
name|reqs
control|)
block|{
name|resolver
operator|.
name|add
argument_list|(
name|req
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|doResolve
argument_list|(
name|resolver
argument_list|)
condition|)
block|{
name|StringWriter
name|w
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
name|w
argument_list|)
decl_stmt|;
name|Reason
index|[]
name|failedReqs
init|=
name|resolver
operator|.
name|getUnsatisfiedRequirements
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|failedReqs
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|failedReqs
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Unsatisfied requirement(s):"
argument_list|)
expr_stmt|;
name|printUnderline
argument_list|(
name|out
argument_list|,
literal|27
argument_list|)
expr_stmt|;
for|for
control|(
name|Reason
name|r
range|:
name|failedReqs
control|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|r
operator|.
name|getRequirement
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|":"
operator|+
name|r
operator|.
name|getRequirement
argument_list|()
operator|.
name|getFilter
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"      "
operator|+
name|r
operator|.
name|getResource
argument_list|()
operator|.
name|getPresentationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Could not resolve targets."
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Can not resolve feature:\n"
operator|+
name|w
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|bundles
init|=
operator|new
name|ArrayList
argument_list|<
name|BundleInfo
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Resource
argument_list|>
name|deploy
init|=
operator|new
name|ArrayList
argument_list|<
name|Resource
argument_list|>
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|deploy
argument_list|,
name|resolver
operator|.
name|getRequiredResources
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|resolveOptionalImports
condition|)
block|{
name|Collections
operator|.
name|addAll
argument_list|(
name|deploy
argument_list|,
name|resolver
operator|.
name|getOptionalResources
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|addAll
argument_list|(
name|deploy
argument_list|,
name|resolver
operator|.
name|getAddedResources
argument_list|()
argument_list|)
expr_stmt|;
name|deploy
operator|.
name|addAll
argument_list|(
name|featureDeploy
argument_list|)
expr_stmt|;
for|for
control|(
name|Resource
name|res
range|:
name|deploy
control|)
block|{
name|BundleInfo
name|info
init|=
name|infos
operator|.
name|get
argument_list|(
name|res
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
name|Reason
index|[]
name|reasons
init|=
name|resolver
operator|.
name|getReason
argument_list|(
name|res
argument_list|)
decl_stmt|;
if|if
condition|(
name|reasons
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Reason
name|r
range|:
name|reasons
control|)
block|{
name|info
operator|=
name|infos
operator|.
name|get
argument_list|(
name|r
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
block|}
block|}
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
name|info
operator|=
operator|new
name|BundleInfoImpl
argument_list|(
name|res
operator|.
name|getURI
argument_list|()
argument_list|,
name|this
operator|.
name|startLevel
argument_list|,
name|this
operator|.
name|startByDefault
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|bundles
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
return|return
name|bundles
return|;
block|}
specifier|private
name|boolean
name|doResolve
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|bundlerepository
operator|.
name|Resolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|resolveOptionalImports
condition|)
block|{
return|return
name|resolver
operator|.
name|resolve
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|resolver
operator|.
name|resolve
argument_list|(
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|bundlerepository
operator|.
name|Resolver
operator|.
name|NO_OPTIONAL_RESOURCES
argument_list|)
return|;
block|}
block|}
specifier|protected
name|void
name|printUnderline
parameter_list|(
name|PrintWriter
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
name|Requirement
name|parseRequirement
parameter_list|(
name|String
name|req
parameter_list|)
throws|throws
name|InvalidSyntaxException
block|{
name|int
name|p
init|=
name|req
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|name
decl_stmt|;
name|String
name|filter
decl_stmt|;
if|if
condition|(
name|p
operator|>
literal|0
condition|)
block|{
name|name
operator|=
name|req
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|filter
operator|=
name|req
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|req
operator|.
name|contains
argument_list|(
literal|"package"
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"package"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|req
operator|.
name|contains
argument_list|(
literal|"service"
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"service"
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
literal|"bundle"
expr_stmt|;
block|}
name|filter
operator|=
name|req
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|filter
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
condition|)
block|{
name|filter
operator|=
literal|"("
operator|+
name|filter
operator|+
literal|")"
expr_stmt|;
block|}
return|return
name|repositoryAdmin
operator|.
name|getHelper
argument_list|()
operator|.
name|requirement
argument_list|(
name|name
argument_list|,
name|filter
argument_list|)
return|;
block|}
block|}
end_class

end_unit

