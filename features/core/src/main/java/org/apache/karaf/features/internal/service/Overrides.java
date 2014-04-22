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
name|internal
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|Version
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Resource
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
import|import static
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|resolver
operator|.
name|Util
operator|.
name|getSymbolicName
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|resolver
operator|.
name|Util
operator|.
name|getVersion
import|;
end_import

begin_comment
comment|/**  * Helper class to deal with overriden bundles at feature installation time.  */
end_comment

begin_class
specifier|public
class|class
name|Overrides
block|{
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
name|Overrides
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|OVERRIDE_RANGE
init|=
literal|"range"
decl_stmt|;
comment|/**      * Compute a list of bundles to install, taking into account overrides.      *      * The file containing the overrides will be loaded from the given url.      * Blank lines and lines starting with a '#' will be ignored, all other lines      * are considered as urls to override bundles.      *      * The list of resources to resolve will be scanned and for each bundle,      * if a bundle override matches that resource, it will be used instead.      *      * Matching is done on bundle symbolic name (they have to be the same)      * and version (the bundle override version needs to be greater than the      * resource to be resolved, and less than the next minor version.  A range      * directive can be added to the override url in which case, the matching      * will succeed if the resource to be resolved is within the given range.      *      * @param resources the list of resources to resolve      * @param overrides list of bundle overrides      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Resource
parameter_list|>
name|void
name|override
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|resources
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|overrides
parameter_list|)
block|{
comment|// Do override replacement
for|for
control|(
name|Clause
name|override
range|:
name|Parser
operator|.
name|parseClauses
argument_list|(
name|overrides
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|overrides
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
control|)
block|{
name|String
name|url
init|=
name|override
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|vr
init|=
name|override
operator|.
name|getAttribute
argument_list|(
name|OVERRIDE_RANGE
argument_list|)
decl_stmt|;
name|T
name|over
init|=
name|resources
operator|.
name|get
argument_list|(
name|url
argument_list|)
decl_stmt|;
if|if
condition|(
name|over
operator|==
literal|null
condition|)
block|{
comment|// Ignore invalid overrides
continue|continue;
block|}
for|for
control|(
name|String
name|uri
range|:
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|resources
operator|.
name|keySet
argument_list|()
argument_list|)
control|)
block|{
name|Resource
name|res
init|=
name|resources
operator|.
name|get
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|getSymbolicName
argument_list|(
name|res
argument_list|)
operator|.
name|equals
argument_list|(
name|getSymbolicName
argument_list|(
name|over
argument_list|)
argument_list|)
condition|)
block|{
name|VersionRange
name|range
decl_stmt|;
if|if
condition|(
name|vr
operator|==
literal|null
condition|)
block|{
comment|// default to micro version compatibility
name|Version
name|v1
init|=
name|getVersion
argument_list|(
name|res
argument_list|)
decl_stmt|;
name|Version
name|v2
init|=
operator|new
name|Version
argument_list|(
name|v1
operator|.
name|getMajor
argument_list|()
argument_list|,
name|v1
operator|.
name|getMinor
argument_list|()
operator|+
literal|1
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|range
operator|=
operator|new
name|VersionRange
argument_list|(
literal|false
argument_list|,
name|v1
argument_list|,
name|v2
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|range
operator|=
name|VersionRange
operator|.
name|parseVersionRange
argument_list|(
name|vr
argument_list|)
expr_stmt|;
block|}
comment|// The resource matches, so replace it with the overridden resource
comment|// if the override is actually a newer version than what we currently have
if|if
condition|(
name|range
operator|.
name|contains
argument_list|(
name|getVersion
argument_list|(
name|over
argument_list|)
argument_list|)
operator|&&
name|getVersion
argument_list|(
name|res
argument_list|)
operator|.
name|compareTo
argument_list|(
name|getVersion
argument_list|(
name|over
argument_list|)
argument_list|)
operator|<
literal|0
condition|)
block|{
name|resources
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|over
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|loadOverrides
parameter_list|(
name|String
name|overridesUrl
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|overrides
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|overridesUrl
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|is
init|=
operator|new
name|URL
argument_list|(
name|overridesUrl
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|line
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|overrides
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Unable to load overrides bundles list"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|overrides
return|;
block|}
specifier|public
specifier|static
name|String
name|extractUrl
parameter_list|(
name|String
name|override
parameter_list|)
block|{
name|Clause
index|[]
name|cs
init|=
name|Parser
operator|.
name|parseClauses
argument_list|(
operator|new
name|String
index|[]
block|{
name|override
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|cs
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Override contains more than one clause: "
operator|+
name|override
argument_list|)
throw|;
block|}
return|return
name|cs
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

