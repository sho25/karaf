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
name|management
operator|.
name|mbeans
operator|.
name|obr
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
name|felix
operator|.
name|bundlerepository
operator|.
name|*
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
name|management
operator|.
name|mbeans
operator|.
name|obr
operator|.
name|ObrMBean
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
name|Version
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StandardMBean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|*
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

begin_comment
comment|/**  * Implementation of the OBR MBean.  */
end_comment

begin_class
specifier|public
class|class
name|ObrMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|ObrMBean
block|{
specifier|private
specifier|static
specifier|final
name|char
name|VERSION_DELIM
init|=
literal|','
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|RepositoryAdmin
name|repositoryAdmin
decl_stmt|;
specifier|public
name|ObrMBeanImpl
parameter_list|()
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|ObrMBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getUrls
parameter_list|()
throws|throws
name|Exception
block|{
name|Repository
index|[]
name|repositories
init|=
name|repositoryAdmin
operator|.
name|listRepositories
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|urls
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
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
name|repositories
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|urls
operator|.
name|add
argument_list|(
name|repositories
index|[
name|i
index|]
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|urls
return|;
block|}
specifier|public
name|TabularData
name|getBundles
parameter_list|()
throws|throws
name|Exception
block|{
name|CompositeType
name|bundleType
init|=
operator|new
name|CompositeType
argument_list|(
literal|"OBR Resource"
argument_list|,
literal|"Bundle available in the OBR"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"presentationname"
block|,
literal|"symbolicname"
block|,
literal|"version"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Presentation Name"
block|,
literal|"Symbolic Name"
block|,
literal|"Version"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|}
argument_list|)
decl_stmt|;
name|TabularType
name|tableType
init|=
operator|new
name|TabularType
argument_list|(
literal|"OBR Resources"
argument_list|,
literal|"Table of all resources/bundles available in the OBR"
argument_list|,
name|bundleType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"presentationname"
block|}
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|tableType
argument_list|)
decl_stmt|;
name|Resource
index|[]
name|resources
init|=
name|repositoryAdmin
operator|.
name|discoverResources
argument_list|(
literal|"(|(presentationname=*)(symbolicname=*))"
argument_list|)
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
name|resources
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|bundleType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"presentationname"
block|,
literal|"symbolicname"
block|,
literal|"version"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|resources
index|[
name|i
index|]
operator|.
name|getPresentationName
argument_list|()
block|,
name|resources
index|[
name|i
index|]
operator|.
name|getSymbolicName
argument_list|()
block|,
name|resources
index|[
name|i
index|]
operator|.
name|getVersion
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
specifier|public
name|void
name|addUrl
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|Exception
block|{
name|repositoryAdmin
operator|.
name|addRepository
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeUrl
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|Exception
block|{
name|repositoryAdmin
operator|.
name|removeRepository
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|refreshUrl
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|Exception
block|{
name|repositoryAdmin
operator|.
name|addRepository
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deployBundle
parameter_list|(
name|String
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
name|deployBundle
argument_list|(
name|bundle
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deployBundle
parameter_list|(
name|String
name|bundle
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
name|repositoryAdmin
operator|.
name|resolver
argument_list|()
decl_stmt|;
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
name|repositoryAdmin
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
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown bundle "
operator|+
name|target
index|[
literal|0
index|]
argument_list|)
throw|;
block|}
name|resolver
operator|.
name|add
argument_list|(
name|resource
argument_list|)
expr_stmt|;
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
block|{         }
if|if
condition|(
name|resolver
operator|.
name|resolve
argument_list|()
condition|)
block|{
name|Resource
index|[]
name|resources
init|=
name|resolver
operator|.
name|getAddedResources
argument_list|()
decl_stmt|;
name|resources
operator|=
name|resolver
operator|.
name|getRequiredResources
argument_list|()
expr_stmt|;
name|resources
operator|=
name|resolver
operator|.
name|getOptionalResources
argument_list|()
expr_stmt|;
try|try
block|{
name|resolver
operator|.
name|deploy
argument_list|(
name|start
condition|?
name|Resolver
operator|.
name|START
else|:
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Can't deploy using OBR"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
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
throws|throws
name|InvalidSyntaxException
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
specifier|private
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
specifier|private
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
operator|+
literal|1
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
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|this
operator|.
name|bundleContext
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
specifier|public
name|RepositoryAdmin
name|getRepositoryAdmin
parameter_list|()
block|{
return|return
name|this
operator|.
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
block|}
end_class

end_unit

