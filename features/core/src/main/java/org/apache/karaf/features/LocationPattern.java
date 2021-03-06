begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
package|;
end_package

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
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|VersionCleaner
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
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|maven
operator|.
name|Parser
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

begin_comment
comment|/**  *<p>Helper class to compare Maven URIs (and falling back to other URIs) that may use globs and version ranges.</p>  *  *<p>Each Maven URI may contain these components: groupId, artifactId, optional version, optional type and optional  * classifier. Concrete URIs do not use globs and use precise versions (we do not consider<code>LATEST</code>  * and<code>RELEASE</code> Maven versions here).</p>  *  *<p>When comparing two Maven URIs, we split them to components and may use RegExps and  * {@link org.apache.felix.utils.version.VersionRange}s</p>  *  *<p>When pattern URI doesn't use<code>mvn:</code> scheme, plain {@link String#equals(Object)} is used or  * {@link Matcher#matches()} when pattern uses<code>*</code> glob.</p>  */
end_comment

begin_class
specifier|public
class|class
name|LocationPattern
block|{
specifier|public
specifier|static
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LocationPattern
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|originalUri
decl_stmt|;
specifier|private
name|Pattern
name|originalPattern
decl_stmt|;
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|Pattern
name|groupIdPattern
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|Pattern
name|artifactIdPattern
decl_stmt|;
specifier|private
name|String
name|versionString
decl_stmt|;
specifier|private
name|Version
name|version
decl_stmt|;
specifier|private
name|VersionRange
name|versionRange
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|Pattern
name|typePattern
decl_stmt|;
specifier|private
name|String
name|classifier
decl_stmt|;
specifier|private
name|Pattern
name|classifierPattern
decl_stmt|;
specifier|public
name|LocationPattern
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"URI to match should not be null"
argument_list|)
throw|;
block|}
name|originalUri
operator|=
name|uri
expr_stmt|;
if|if
condition|(
operator|!
name|originalUri
operator|.
name|startsWith
argument_list|(
literal|"mvn:"
argument_list|)
condition|)
block|{
name|originalPattern
operator|=
name|toRegExp
argument_list|(
name|originalUri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|Parser
name|parser
init|=
literal|null
decl_stmt|;
try|try
block|{
name|parser
operator|=
operator|new
name|Parser
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|Parser
operator|.
name|VERSION_LATEST
operator|.
name|equals
argument_list|(
name|parser
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|parser
operator|.
name|setVersion
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|groupId
operator|=
name|parser
operator|.
name|getGroup
argument_list|()
expr_stmt|;
if|if
condition|(
name|groupId
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|groupIdPattern
operator|=
name|toRegExp
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
block|}
name|artifactId
operator|=
name|parser
operator|.
name|getArtifact
argument_list|()
expr_stmt|;
if|if
condition|(
name|artifactId
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|artifactIdPattern
operator|=
name|toRegExp
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
block|}
name|versionString
operator|=
name|parser
operator|.
name|getVersion
argument_list|()
expr_stmt|;
if|if
condition|(
name|versionString
operator|!=
literal|null
operator|&&
name|versionString
operator|.
name|length
argument_list|()
operator|>=
literal|1
condition|)
block|{
try|try
block|{
name|char
name|first
init|=
name|versionString
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
operator|==
literal|'['
operator|||
name|first
operator|==
literal|'('
condition|)
block|{
comment|// range
name|versionRange
operator|=
operator|new
name|VersionRange
argument_list|(
name|versionString
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|version
operator|=
operator|new
name|Version
argument_list|(
name|VersionCleaner
operator|.
name|clean
argument_list|(
name|versionString
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|IllegalArgumentException
name|mue
init|=
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't parse version \""
operator|+
name|versionString
operator|+
literal|"\" as OSGi version object."
argument_list|,
name|e
argument_list|)
decl_stmt|;
throw|throw
name|mue
throw|;
block|}
block|}
name|type
operator|=
name|parser
operator|.
name|getType
argument_list|()
expr_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
operator|&&
name|type
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|typePattern
operator|=
name|toRegExp
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
name|classifier
operator|=
name|parser
operator|.
name|getClassifier
argument_list|()
expr_stmt|;
if|if
condition|(
name|classifier
operator|!=
literal|null
operator|&&
name|classifier
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|classifierPattern
operator|=
name|toRegExp
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|getOriginalUri
parameter_list|()
block|{
return|return
name|originalUri
return|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|String
name|getVersionString
parameter_list|()
block|{
return|return
name|versionString
return|;
block|}
comment|/**      * Converts a String with one special character (<code>*</code>) into working {@link Pattern}      * @param value      * @return      */
specifier|public
specifier|static
name|Pattern
name|toRegExp
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// TODO: escape all RegExp special chars that are valid path characters, only convert '*' into '.*'
return|return
name|Pattern
operator|.
name|compile
argument_list|(
name|value
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"\\\\\\."
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"\\\\\\$"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\^"
argument_list|,
literal|"\\\\\\^"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\*"
argument_list|,
literal|".*"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns<code>true</code> if this location pattern matches other pattern.      * @param otherUri      * @return      */
specifier|public
name|boolean
name|matches
parameter_list|(
name|String
name|otherUri
parameter_list|)
block|{
if|if
condition|(
name|otherUri
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|originalPattern
operator|!=
literal|null
condition|)
block|{
comment|// this pattern is not mvn:
return|return
name|originalPattern
operator|.
name|matcher
argument_list|(
name|otherUri
argument_list|)
operator|.
name|matches
argument_list|()
return|;
block|}
name|LocationPattern
name|other
decl_stmt|;
try|try
block|{
name|other
operator|=
operator|new
name|LocationPattern
argument_list|(
name|otherUri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Can't parse \""
operator|+
name|otherUri
operator|+
literal|"\" as Maven URI. Ignoring."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|other
operator|.
name|originalPattern
operator|!=
literal|null
condition|)
block|{
comment|// other pattern is not mvn:
return|return
literal|false
return|;
block|}
if|if
condition|(
name|other
operator|.
name|versionRange
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Matched URI can't use version ranges: "
operator|+
name|otherUri
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|boolean
name|match
decl_stmt|;
if|if
condition|(
name|groupIdPattern
operator|==
literal|null
condition|)
block|{
name|match
operator|=
name|groupId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|groupId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|match
operator|=
name|groupIdPattern
operator|.
name|matcher
argument_list|(
name|other
operator|.
name|groupId
argument_list|)
operator|.
name|matches
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|artifactIdPattern
operator|==
literal|null
condition|)
block|{
name|match
operator|=
name|artifactId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|artifactId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|match
operator|=
name|artifactIdPattern
operator|.
name|matcher
argument_list|(
name|other
operator|.
name|artifactId
argument_list|)
operator|.
name|matches
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|versionRange
operator|!=
literal|null
operator|&&
name|other
operator|.
name|version
operator|!=
literal|null
condition|)
block|{
name|match
operator|=
name|versionRange
operator|.
name|contains
argument_list|(
name|other
operator|.
name|version
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|match
operator|=
name|version
operator|==
literal|null
operator|||
name|version
operator|.
name|equals
argument_list|(
name|other
operator|.
name|version
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|typePattern
operator|!=
literal|null
condition|)
block|{
name|match
operator|=
name|typePattern
operator|.
name|matcher
argument_list|(
name|other
operator|.
name|type
operator|==
literal|null
condition|?
literal|"jar"
else|:
name|other
operator|.
name|type
argument_list|)
operator|.
name|matches
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|match
operator|=
name|versionString
operator|==
literal|null
operator|||
name|type
operator|.
name|equals
argument_list|(
name|other
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|classifierPattern
operator|!=
literal|null
condition|)
block|{
name|match
operator|=
name|classifierPattern
operator|.
name|matcher
argument_list|(
name|other
operator|.
name|classifier
operator|==
literal|null
condition|?
literal|""
else|:
name|other
operator|.
name|classifier
argument_list|)
operator|.
name|matches
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|classifier
operator|!=
literal|null
condition|)
block|{
name|match
operator|=
name|classifier
operator|.
name|equals
argument_list|(
name|other
operator|.
name|classifier
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|match
operator|=
name|other
operator|.
name|classifierPattern
operator|==
literal|null
expr_stmt|;
block|}
return|return
name|match
return|;
block|}
comment|/**      * Returns {@code true} if this location pattern strictly matches other pattern.      *<p>      * In a strict match, if this instance doesn't contain a classifier,      * it won't match against an otherUri which contains one.      */
specifier|public
name|boolean
name|strictlyMatches
parameter_list|(
name|String
name|otherUri
parameter_list|)
block|{
name|boolean
name|match
init|=
name|matches
argument_list|(
name|otherUri
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|match
condition|)
return|return
literal|false
return|;
comment|// refine the loose match
name|LocationPattern
name|other
init|=
literal|null
decl_stmt|;
try|try
block|{
name|other
operator|=
operator|new
name|LocationPattern
argument_list|(
name|otherUri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ignored
parameter_list|)
block|{
return|return
literal|true
return|;
comment|// not a "mvn:" uri
block|}
if|if
condition|(
name|classifierPattern
operator|==
literal|null
operator|&&
name|classifier
operator|==
literal|null
condition|)
block|{
name|match
operator|=
name|other
operator|.
name|classifierPattern
operator|==
literal|null
operator|&&
name|other
operator|.
name|classifier
operator|==
literal|null
expr_stmt|;
block|}
return|return
name|match
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|originalUri
return|;
block|}
block|}
end_class

end_unit

