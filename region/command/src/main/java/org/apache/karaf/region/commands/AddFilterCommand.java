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
name|region
operator|.
name|commands
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
name|aries
operator|.
name|util
operator|.
name|VersionRange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|util
operator|.
name|manifest
operator|.
name|ManifestHeaderProcessor
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
name|api
operator|.
name|action
operator|.
name|Argument
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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
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
name|RegionFilter
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
name|Constants
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"region"
argument_list|,
name|name
operator|=
literal|"filter-add"
argument_list|,
name|description
operator|=
literal|"Adds a filter between two regions."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|AddFilterCommand
extends|extends
name|RegionCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"from"
argument_list|,
name|description
operator|=
literal|"The from region."
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|fromRegion
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"to"
argument_list|,
name|description
operator|=
literal|"The to region."
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|toRegion
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|2
argument_list|,
name|name
operator|=
literal|"items"
argument_list|,
name|description
operator|=
literal|"The bundles by id and packages with version to allow."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|items
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|RegionDigraph
name|regionDigraph
parameter_list|)
throws|throws
name|Exception
block|{
name|Region
name|rFrom
init|=
name|getRegion
argument_list|(
name|regionDigraph
argument_list|,
name|fromRegion
argument_list|)
decl_stmt|;
name|Region
name|rTo
init|=
name|getRegion
argument_list|(
name|regionDigraph
argument_list|,
name|toRegion
argument_list|)
decl_stmt|;
name|RegionFilterBuilder
name|builder
init|=
name|regionDigraph
operator|.
name|createRegionFilterBuilder
argument_list|()
decl_stmt|;
name|BundleContext
name|framework
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|items
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|item
range|:
name|items
control|)
block|{
try|try
block|{
name|long
name|id
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|item
argument_list|)
decl_stmt|;
name|Bundle
name|b
init|=
name|framework
operator|.
name|getBundle
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|builder
operator|.
name|allow
argument_list|(
literal|"osgi.wiring.bundle"
argument_list|,
literal|"(osgi.wiring.bundle="
operator|+
name|b
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
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
name|parsed
range|:
name|ManifestHeaderProcessor
operator|.
name|parseImportString
argument_list|(
name|item
argument_list|)
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|packageName
init|=
name|parsed
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|parsed
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"osgi.wiring.package"
argument_list|,
name|packageName
argument_list|)
expr_stmt|;
name|String
name|filter
init|=
name|generateFilter
argument_list|(
name|attributes
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"adding filter "
operator|+
name|filter
argument_list|)
expr_stmt|;
name|builder
operator|.
name|allow
argument_list|(
literal|"osgi.wiring.package"
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|RegionFilter
name|f
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|regionDigraph
operator|.
name|connect
argument_list|(
name|rFrom
argument_list|,
name|f
argument_list|,
name|rTo
argument_list|)
expr_stmt|;
block|}
comment|//from aries util, with obr specific weirdness removed
specifier|public
specifier|static
name|String
name|generateFilter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attribs
parameter_list|)
block|{
name|StringBuilder
name|filter
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"(&"
argument_list|)
decl_stmt|;
name|boolean
name|realAttrib
init|=
literal|false
decl_stmt|;
name|StringBuffer
name|realAttribs
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|attribs
operator|==
literal|null
condition|)
block|{
name|attribs
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attrib
range|:
name|attribs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|attribName
init|=
name|attrib
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|attribName
operator|.
name|endsWith
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
comment|// skip all directives. It is used to affect the attribs on the
comment|// filter xml.
block|}
elseif|else
if|if
condition|(
operator|(
name|Constants
operator|.
name|VERSION_ATTRIBUTE
operator|.
name|equals
argument_list|(
name|attribName
argument_list|)
operator|)
operator|||
operator|(
name|Constants
operator|.
name|BUNDLE_VERSION_ATTRIBUTE
operator|.
name|equals
argument_list|(
name|attribName
argument_list|)
operator|)
condition|)
block|{
comment|// version and bundle-version attrib requires special
comment|// conversion.
name|realAttrib
operator|=
literal|true
expr_stmt|;
name|VersionRange
name|vr
init|=
name|ManifestHeaderProcessor
operator|.
name|parseVersionRange
argument_list|(
name|attrib
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|"("
operator|+
name|attribName
operator|+
literal|">="
operator|+
name|vr
operator|.
name|getMinimumVersion
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|vr
operator|.
name|getMaximumVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|filter
operator|.
name|append
argument_list|(
literal|")("
operator|+
name|attribName
operator|+
literal|"<="
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
name|vr
operator|.
name|getMaximumVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|vr
operator|.
name|getMaximumVersion
argument_list|()
operator|!=
literal|null
operator|&&
name|vr
operator|.
name|isMinimumExclusive
argument_list|()
condition|)
block|{
name|filter
operator|.
name|append
argument_list|(
literal|")(!("
operator|+
name|attribName
operator|+
literal|"="
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
name|vr
operator|.
name|getMinimumVersion
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|vr
operator|.
name|getMaximumVersion
argument_list|()
operator|!=
literal|null
operator|&&
name|vr
operator|.
name|isMaximumExclusive
argument_list|()
condition|)
block|{
name|filter
operator|.
name|append
argument_list|(
literal|")(!("
operator|+
name|attribName
operator|+
literal|"="
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
name|vr
operator|.
name|getMaximumVersion
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
name|filter
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Constants
operator|.
name|OBJECTCLASS
operator|.
name|equals
argument_list|(
name|attribName
argument_list|)
condition|)
block|{
name|realAttrib
operator|=
literal|true
expr_stmt|;
comment|// objectClass has a "," separated list of interfaces
name|String
index|[]
name|values
init|=
name|attrib
operator|.
name|getValue
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|values
control|)
name|filter
operator|.
name|append
argument_list|(
literal|"("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|s
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// attribName was not version..
name|realAttrib
operator|=
literal|true
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|"("
operator|+
name|attribName
operator|+
literal|"="
operator|+
name|attrib
operator|.
name|getValue
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
comment|// store all attributes in order to build up the mandatory
comment|// filter and separate them with ", "
comment|// skip bundle-symbolic-name in the mandatory directive query
if|if
condition|(
operator|!
operator|!
operator|!
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME_ATTRIBUTE
operator|.
name|equals
argument_list|(
name|attribName
argument_list|)
condition|)
block|{
name|realAttribs
operator|.
name|append
argument_list|(
name|attribName
argument_list|)
expr_stmt|;
name|realAttribs
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Prune (& off the front and ) off end
name|String
name|filterString
init|=
name|filter
operator|.
name|toString
argument_list|()
decl_stmt|;
name|int
name|openBraces
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|openBraces
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|i
operator|=
name|filterString
operator|.
name|indexOf
argument_list|(
literal|'('
argument_list|,
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|==
operator|-
literal|1
condition|)
block|{
break|break;
block|}
else|else
block|{
name|openBraces
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|openBraces
argument_list|<
literal|3
operator|&&
name|filterString
operator|.
name|length
operator|(
operator|)
argument_list|>
literal|2
condition|)
block|{
name|filter
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|filter
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
name|String
name|result
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|realAttrib
operator|!=
literal|false
condition|)
block|{
name|result
operator|=
name|filter
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

