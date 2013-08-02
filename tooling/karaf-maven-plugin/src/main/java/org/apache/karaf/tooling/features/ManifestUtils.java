begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tooling
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
name|LinkedList
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
name|jar
operator|.
name|Manifest
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
name|utils
operator|.
name|manifest
operator|.
name|Clause
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
name|utils
operator|.
name|manifest
operator|.
name|Parser
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
name|utils
operator|.
name|version
operator|.
name|VersionRange
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

begin_comment
comment|/**  * A set of utility methods to ease working with {@link org.apache.felix.utils.manifest.Parser} and  * {@link org.apache.felix.utils.manifest.Clause}  */
end_comment

begin_class
specifier|public
class|class
name|ManifestUtils
block|{
specifier|private
name|ManifestUtils
parameter_list|()
block|{
comment|// hide the constructor
block|}
comment|/**      * Get the list of imports from the manifest.  If no imports have been defined, this method returns an empty list.      *      * @param manifest the manifest      * @return the list of imports      */
specifier|public
specifier|static
name|List
argument_list|<
name|Clause
argument_list|>
name|getImports
parameter_list|(
name|Manifest
name|manifest
parameter_list|)
block|{
name|List
argument_list|<
name|Clause
argument_list|>
name|result
init|=
operator|new
name|LinkedList
argument_list|<
name|Clause
argument_list|>
argument_list|()
decl_stmt|;
name|Clause
index|[]
name|clauses
init|=
name|Parser
operator|.
name|parseHeader
argument_list|(
name|getHeader
argument_list|(
name|Constants
operator|.
name|IMPORT_PACKAGE
argument_list|,
name|manifest
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Clause
name|clause
range|:
name|clauses
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|clause
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Get the list of non-optional imports from the manifest.      *      * @param manifest the manifest      * @return the list of non-optional imports      */
specifier|public
specifier|static
name|List
argument_list|<
name|Clause
argument_list|>
name|getMandatoryImports
parameter_list|(
name|Manifest
name|manifest
parameter_list|)
block|{
name|List
argument_list|<
name|Clause
argument_list|>
name|result
init|=
operator|new
name|LinkedList
argument_list|<
name|Clause
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Clause
name|clause
range|:
name|getImports
argument_list|(
name|manifest
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|isOptional
argument_list|(
name|clause
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|clause
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**      * Get the list of exports from the manifest.  If no exports have been defined, this method returns an empty list.      *      * @param manifest the manifest      * @return the list of exports      */
specifier|public
specifier|static
name|List
argument_list|<
name|Clause
argument_list|>
name|getExports
parameter_list|(
name|Manifest
name|manifest
parameter_list|)
block|{
name|List
argument_list|<
name|Clause
argument_list|>
name|result
init|=
operator|new
name|LinkedList
argument_list|<
name|Clause
argument_list|>
argument_list|()
decl_stmt|;
name|Clause
index|[]
name|clauses
init|=
name|Parser
operator|.
name|parseHeader
argument_list|(
name|getHeader
argument_list|(
name|Constants
operator|.
name|EXPORT_PACKAGE
argument_list|,
name|manifest
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Clause
name|clause
range|:
name|clauses
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|clause
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Check if a given manifest clause represents an optional import      *      * @param clause the manifest clause      * @return<code>true</code> for an optional import,<code>false</code> for mandatory imports      */
specifier|public
specifier|static
name|boolean
name|isOptional
parameter_list|(
name|Clause
name|clause
parameter_list|)
block|{
return|return
literal|"optional"
operator|.
name|equals
argument_list|(
name|clause
operator|.
name|getDirective
argument_list|(
literal|"resolution"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Check if the manifest contains the mandatory Bundle-Symbolic-Name      *      * @param manifest the manifest      * @return<code>true</code> if the manifest specifies a Bundle-Symbolic-Name      */
specifier|public
specifier|static
name|boolean
name|isBundle
parameter_list|(
name|Manifest
name|manifest
parameter_list|)
block|{
return|return
name|getBsn
argument_list|(
name|manifest
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
specifier|static
name|boolean
name|matches
parameter_list|(
name|Clause
name|requirement
parameter_list|,
name|Clause
name|export
parameter_list|)
block|{
if|if
condition|(
name|requirement
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|export
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|VersionRange
name|importVersionRange
init|=
name|getVersionRange
argument_list|(
name|requirement
argument_list|)
decl_stmt|;
name|VersionRange
name|exportVersionRange
init|=
name|getVersionRange
argument_list|(
name|export
argument_list|)
decl_stmt|;
name|VersionRange
name|intersection
init|=
name|importVersionRange
operator|.
name|intersect
argument_list|(
name|exportVersionRange
argument_list|)
decl_stmt|;
return|return
name|intersection
operator|!=
literal|null
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|String
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Manifest
name|manifest
parameter_list|)
block|{
name|String
name|value
init|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|getValue
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|value
return|;
block|}
specifier|public
specifier|static
name|String
name|getBsn
parameter_list|(
name|Manifest
name|manifest
parameter_list|)
block|{
name|String
name|bsn
init|=
name|getHeader
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
name|manifest
argument_list|)
decl_stmt|;
return|return
name|bsn
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|public
specifier|static
name|VersionRange
name|getVersionRange
parameter_list|(
name|Clause
name|clause
parameter_list|)
block|{
name|String
name|v
init|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|Constants
operator|.
name|VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|v
operator|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|Constants
operator|.
name|PACKAGE_SPECIFICATION_VERSION
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|v
operator|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION_ATTRIBUTE
argument_list|)
expr_stmt|;
block|}
return|return
name|VersionRange
operator|.
name|parseVersionRange
argument_list|(
name|v
argument_list|)
return|;
block|}
block|}
end_class

end_unit

