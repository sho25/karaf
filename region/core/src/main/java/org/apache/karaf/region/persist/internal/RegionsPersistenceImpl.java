begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|region
operator|.
name|persist
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
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
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|RegionsPersistence
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
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|model
operator|.
name|FilterAttributeType
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
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|model
operator|.
name|FilterBundleType
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
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|model
operator|.
name|FilterNamespaceType
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
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|model
operator|.
name|FilterPackageType
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
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|model
operator|.
name|FilterType
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
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|model
operator|.
name|RegionBundleType
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
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|model
operator|.
name|RegionType
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
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|model
operator|.
name|RegionsType
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
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|util
operator|.
name|ManifestHeaderProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|Region
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionDigraph
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionFilterBuilder
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
name|BundleException
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
name|wiring
operator|.
name|BundleCapability
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
name|wiring
operator|.
name|BundleRevision
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

begin_class
specifier|public
class|class
name|RegionsPersistenceImpl
implements|implements
name|RegionsPersistence
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RegionsPersistenceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
name|RegionDigraph
name|regionDigraph
decl_stmt|;
specifier|private
name|Region
name|kernel
decl_stmt|;
specifier|private
name|Bundle
name|framework
decl_stmt|;
specifier|public
name|RegionsPersistenceImpl
parameter_list|(
name|RegionDigraph
name|regionDigraph
parameter_list|,
name|Bundle
name|framework
parameter_list|)
throws|throws
name|JAXBException
throws|,
name|BundleException
throws|,
name|IOException
throws|,
name|InvalidSyntaxException
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Loading region digraph persistence"
argument_list|)
expr_stmt|;
name|this
operator|.
name|framework
operator|=
name|framework
expr_stmt|;
name|this
operator|.
name|regionDigraph
operator|=
name|regionDigraph
expr_stmt|;
name|kernel
operator|=
name|regionDigraph
operator|.
name|getRegion
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|jaxbContext
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|RegionsType
operator|.
name|class
argument_list|)
expr_stmt|;
name|load
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|install
parameter_list|(
name|Bundle
name|b
parameter_list|,
name|String
name|regionName
parameter_list|)
throws|throws
name|BundleException
block|{
name|Region
name|region
init|=
name|regionDigraph
operator|.
name|getRegion
argument_list|(
name|regionName
argument_list|)
decl_stmt|;
if|if
condition|(
name|region
operator|==
literal|null
condition|)
block|{
name|region
operator|=
name|regionDigraph
operator|.
name|createRegion
argument_list|(
name|regionName
argument_list|)
expr_stmt|;
block|}
name|kernel
operator|.
name|removeBundle
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|region
operator|.
name|addBundle
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
name|void
name|save
parameter_list|(
name|RegionsType
name|regionsType
parameter_list|,
name|Writer
name|out
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Marshaller
name|marshaller
init|=
name|jaxbContext
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|regionsType
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|void
name|load
parameter_list|()
throws|throws
name|IOException
throws|,
name|BundleException
throws|,
name|JAXBException
throws|,
name|InvalidSyntaxException
block|{
if|if
condition|(
name|this
operator|.
name|regionDigraph
operator|.
name|getRegions
argument_list|()
operator|.
name|size
argument_list|()
operator|<=
literal|1
condition|)
block|{
name|File
name|etc
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|regionsConfig
init|=
operator|new
name|File
argument_list|(
name|etc
argument_list|,
literal|"regions-config.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|regionsConfig
operator|.
name|exists
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"initializing region digraph from etc/regions-config.xml"
argument_list|)
expr_stmt|;
name|Reader
name|in
init|=
operator|new
name|FileReader
argument_list|(
name|regionsConfig
argument_list|)
decl_stmt|;
try|try
block|{
name|load
argument_list|(
name|this
operator|.
name|regionDigraph
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"no regions config file"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|load
parameter_list|(
name|RegionDigraph
name|regionDigraph
parameter_list|,
name|Reader
name|in
parameter_list|)
throws|throws
name|JAXBException
throws|,
name|BundleException
throws|,
name|InvalidSyntaxException
block|{
name|RegionsType
name|regionsType
init|=
name|load
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|load
argument_list|(
name|regionsType
argument_list|,
name|regionDigraph
argument_list|)
expr_stmt|;
block|}
name|RegionsType
name|load
parameter_list|(
name|Reader
name|in
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|jaxbContext
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
return|return
operator|(
name|RegionsType
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|in
argument_list|)
return|;
block|}
name|void
name|load
parameter_list|(
name|RegionsType
name|regionsType
parameter_list|,
name|RegionDigraph
name|regionDigraph
parameter_list|)
throws|throws
name|BundleException
throws|,
name|InvalidSyntaxException
block|{
name|BundleContext
name|frameworkContext
init|=
name|framework
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
for|for
control|(
name|RegionType
name|regionType
range|:
name|regionsType
operator|.
name|getRegion
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|regionType
operator|.
name|getName
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Creating region: "
operator|+
name|name
argument_list|)
expr_stmt|;
name|Region
name|region
init|=
name|regionDigraph
operator|.
name|createRegion
argument_list|(
name|name
argument_list|)
decl_stmt|;
for|for
control|(
name|RegionBundleType
name|bundleType
range|:
name|regionType
operator|.
name|getBundle
argument_list|()
control|)
block|{
if|if
condition|(
name|bundleType
operator|.
name|getId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|region
operator|.
name|addBundle
argument_list|(
name|bundleType
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Bundle
name|b
init|=
name|frameworkContext
operator|.
name|getBundle
argument_list|(
name|bundleType
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|region
operator|.
name|addBundle
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|FilterType
name|filterType
range|:
name|regionsType
operator|.
name|getFilter
argument_list|()
control|)
block|{
name|Region
name|from
init|=
name|regionDigraph
operator|.
name|getRegion
argument_list|(
name|filterType
operator|.
name|getFrom
argument_list|()
argument_list|)
decl_stmt|;
name|Region
name|to
init|=
name|regionDigraph
operator|.
name|getRegion
argument_list|(
name|filterType
operator|.
name|getTo
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Creating filter between "
operator|+
name|from
operator|.
name|getName
argument_list|()
operator|+
literal|" to "
operator|+
name|to
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|RegionFilterBuilder
name|builder
init|=
name|regionDigraph
operator|.
name|createRegionFilterBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|FilterBundleType
name|bundleType
range|:
name|filterType
operator|.
name|getBundle
argument_list|()
control|)
block|{
name|String
name|symbolicName
init|=
name|bundleType
operator|.
name|getSymbolicName
argument_list|()
decl_stmt|;
name|String
name|version
init|=
name|bundleType
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundleType
operator|.
name|getId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Bundle
name|b
init|=
name|frameworkContext
operator|.
name|getBundle
argument_list|(
name|bundleType
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|symbolicName
operator|=
name|b
operator|.
name|getSymbolicName
argument_list|()
expr_stmt|;
name|version
operator|=
name|b
operator|.
name|getVersion
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|String
name|namespace
init|=
name|BundleRevision
operator|.
name|BUNDLE_NAMESPACE
decl_stmt|;
name|List
argument_list|<
name|FilterAttributeType
argument_list|>
name|attributeTypes
init|=
name|bundleType
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
name|buildFilter
argument_list|(
name|symbolicName
argument_list|,
name|version
argument_list|,
name|namespace
argument_list|,
name|attributeTypes
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|FilterPackageType
name|packageType
range|:
name|filterType
operator|.
name|getPackage
argument_list|()
control|)
block|{
name|String
name|packageName
init|=
name|packageType
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|version
init|=
name|packageType
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|String
name|namespace
init|=
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
decl_stmt|;
name|List
argument_list|<
name|FilterAttributeType
argument_list|>
name|attributeTypes
init|=
name|packageType
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
name|buildFilter
argument_list|(
name|packageName
argument_list|,
name|version
argument_list|,
name|namespace
argument_list|,
name|attributeTypes
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|to
operator|==
name|kernel
condition|)
block|{
comment|//add framework exports
name|BundleRevision
name|rev
init|=
name|framework
operator|.
name|adapt
argument_list|(
name|BundleRevision
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BundleCapability
argument_list|>
name|caps
init|=
name|rev
operator|.
name|getDeclaredCapabilities
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
for|for
control|(
name|BundleCapability
name|cap
range|:
name|caps
control|)
block|{
name|String
name|filter
init|=
name|ManifestHeaderProcessor
operator|.
name|generateFilter
argument_list|(
name|filter
argument_list|(
name|cap
operator|.
name|getAttributes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|builder
operator|.
name|allow
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
comment|//TODO explicit services?
for|for
control|(
name|FilterNamespaceType
name|namespaceType
range|:
name|filterType
operator|.
name|getNamespace
argument_list|()
control|)
block|{
name|String
name|namespace
init|=
name|namespaceType
operator|.
name|getName
argument_list|()
decl_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|FilterAttributeType
name|attributeType
range|:
name|namespaceType
operator|.
name|getAttribute
argument_list|()
control|)
block|{
name|attributes
operator|.
name|put
argument_list|(
name|attributeType
operator|.
name|getName
argument_list|()
argument_list|,
name|attributeType
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|filter
init|=
name|ManifestHeaderProcessor
operator|.
name|generateFilter
argument_list|(
name|attributes
argument_list|)
decl_stmt|;
name|builder
operator|.
name|allow
argument_list|(
name|namespace
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
name|regionDigraph
operator|.
name|connect
argument_list|(
name|from
argument_list|,
name|builder
operator|.
name|build
argument_list|()
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|filter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|attributes
argument_list|)
decl_stmt|;
name|result
operator|.
name|remove
argument_list|(
literal|"bundle-version"
argument_list|)
expr_stmt|;
name|result
operator|.
name|remove
argument_list|(
literal|"bundle-symbolic-name"
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|private
name|void
name|buildFilter
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|namespace
parameter_list|,
name|List
argument_list|<
name|FilterAttributeType
argument_list|>
name|attributeTypes
parameter_list|,
name|RegionFilterBuilder
name|builder
parameter_list|)
throws|throws
name|InvalidSyntaxException
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|namespace
operator|!=
literal|null
condition|)
block|{
name|attributes
operator|.
name|put
argument_list|(
name|namespace
argument_list|,
name|packageName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|attributes
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|FilterAttributeType
name|attributeType
range|:
name|attributeTypes
control|)
block|{
name|attributes
operator|.
name|put
argument_list|(
name|attributeType
operator|.
name|getName
argument_list|()
argument_list|,
name|attributeType
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|filter
init|=
name|ManifestHeaderProcessor
operator|.
name|generateFilter
argument_list|(
name|attributes
argument_list|)
decl_stmt|;
name|builder
operator|.
name|allow
argument_list|(
name|namespace
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

