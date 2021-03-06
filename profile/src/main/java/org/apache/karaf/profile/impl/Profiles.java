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
name|profile
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|nio
operator|.
name|file
operator|.
name|FileVisitResult
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|SimpleFileVisitor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|StandardOpenOption
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|attribute
operator|.
name|BasicFileAttributes
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
name|utils
operator|.
name|properties
operator|.
name|TypedProperties
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
name|profile
operator|.
name|PlaceholderResolver
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
name|profile
operator|.
name|Profile
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
name|profile
operator|.
name|ProfileBuilder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|profile
operator|.
name|impl
operator|.
name|Utils
operator|.
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * Static utilities to work with {@link Profile profiles}.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Profiles
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PROFILE_FOLDER_SUFFIX
init|=
literal|".profile"
decl_stmt|;
comment|/**      * Loads profiles from given directory path. A profile is represented as directory with<code>.profile</code>      * extension. Subdirectories constitute part of {@link Profile#getId} - directory separators are changed to      *<code>-</code>.      * For example, profile contained in directory<code>mq/broker/standalone.profile</code> will have      * id =<code>mq-broker-standalone</code>.      */
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Profile
argument_list|>
name|loadProfiles
parameter_list|(
specifier|final
name|Path
name|root
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Profile
argument_list|>
name|profiles
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Files
operator|.
name|walkFileTree
argument_list|(
name|root
argument_list|,
operator|new
name|SimpleFileVisitor
argument_list|<
name|Path
argument_list|>
argument_list|()
block|{
name|ProfileBuilder
name|builder
decl_stmt|;
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|preVisitDirectory
parameter_list|(
name|Path
name|dir
parameter_list|,
name|BasicFileAttributes
name|attrs
parameter_list|)
block|{
name|Path
name|fileName
init|=
name|dir
operator|.
name|getFileName
argument_list|()
decl_stmt|;
if|if
condition|(
name|fileName
operator|!=
literal|null
operator|&&
operator|(
name|fileName
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
name|PROFILE_FOLDER_SUFFIX
argument_list|)
operator|||
name|fileName
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
name|PROFILE_FOLDER_SUFFIX
operator|+
literal|"/"
argument_list|)
operator|)
condition|)
block|{
name|String
name|profileId
init|=
name|root
operator|.
name|relativize
argument_list|(
name|dir
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|profileId
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|profileId
operator|=
name|profileId
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|profileId
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|profileId
operator|=
name|profileId
operator|.
name|replace
argument_list|(
name|root
operator|.
name|getFileSystem
argument_list|()
operator|.
name|getSeparator
argument_list|()
argument_list|,
literal|"-"
argument_list|)
expr_stmt|;
name|profileId
operator|=
name|profileId
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|profileId
operator|.
name|length
argument_list|()
operator|-
name|PROFILE_FOLDER_SUFFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|=
name|ProfileBuilder
operator|.
name|Factory
operator|.
name|create
argument_list|(
name|profileId
argument_list|)
expr_stmt|;
block|}
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|postVisitDirectory
parameter_list|(
name|Path
name|dir
parameter_list|,
name|IOException
name|exc
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|exc
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exc
throw|;
block|}
if|if
condition|(
name|builder
operator|!=
literal|null
condition|)
block|{
name|Profile
name|profile
init|=
name|builder
operator|.
name|getProfile
argument_list|()
decl_stmt|;
name|profiles
operator|.
name|put
argument_list|(
name|profile
operator|.
name|getId
argument_list|()
argument_list|,
name|profile
argument_list|)
expr_stmt|;
name|builder
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|visitFile
parameter_list|(
name|Path
name|file
parameter_list|,
name|BasicFileAttributes
name|attrs
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|builder
operator|!=
literal|null
condition|)
block|{
name|String
name|pid
init|=
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|Files
operator|.
name|readAllBytes
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|builder
operator|.
name|addFileConfiguration
argument_list|(
name|pid
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|profiles
return|;
block|}
comment|/**      * Deletes profile by given {@link Profile#getId()} from<code>root</code> path.      * @param root      * @param id      * @throws IOException      */
specifier|public
specifier|static
name|void
name|deleteProfile
parameter_list|(
name|Path
name|root
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|IOException
block|{
name|Path
name|path
init|=
name|root
operator|.
name|resolve
argument_list|(
name|id
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
name|root
operator|.
name|getFileSystem
argument_list|()
operator|.
name|getSeparator
argument_list|()
argument_list|)
operator|+
name|PROFILE_FOLDER_SUFFIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|Files
operator|.
name|walkFileTree
argument_list|(
name|path
argument_list|,
operator|new
name|SimpleFileVisitor
argument_list|<
name|Path
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|visitFile
parameter_list|(
name|Path
name|file
parameter_list|,
name|BasicFileAttributes
name|attrs
parameter_list|)
throws|throws
name|IOException
block|{
name|Files
operator|.
name|delete
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|postVisitDirectory
parameter_list|(
name|Path
name|dir
parameter_list|,
name|IOException
name|exc
parameter_list|)
throws|throws
name|IOException
block|{
name|Files
operator|.
name|delete
argument_list|(
name|dir
argument_list|)
expr_stmt|;
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Writes given {@link Profile} under a path specified as<code>root</code>. Directory name to store a profile is      * derived from {@link Profile#getId()}      * @param root      * @param profile      * @throws IOException      */
specifier|public
specifier|static
name|void
name|writeProfile
parameter_list|(
name|Path
name|root
parameter_list|,
name|Profile
name|profile
parameter_list|)
throws|throws
name|IOException
block|{
name|Path
name|path
init|=
name|root
operator|.
name|resolve
argument_list|(
name|profile
operator|.
name|getId
argument_list|()
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
name|root
operator|.
name|getFileSystem
argument_list|()
operator|.
name|getSeparator
argument_list|()
argument_list|)
operator|+
name|PROFILE_FOLDER_SUFFIX
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|path
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|entry
range|:
name|profile
operator|.
name|getFileConfigurations
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Files
operator|.
name|write
argument_list|(
name|path
operator|.
name|resolve
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|StandardOpenOption
operator|.
name|CREATE_NEW
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *<p>Gets an<em>overlay</em> profile for given<code>profile</code>, where passed in map of additional profiles      * is searched for possible parent profiles of given<code>profile</code>.</p>      * @param profile      * @param profiles      * @return      */
specifier|public
specifier|static
name|Profile
name|getOverlay
parameter_list|(
name|Profile
name|profile
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Profile
argument_list|>
name|profiles
parameter_list|)
block|{
return|return
name|getOverlay
argument_list|(
name|profile
argument_list|,
name|profiles
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      *<p>Gets an<em>overlay</em> profile for given<code>profile</code>, where passed in map of additional profiles      * is searched for possible parent profiles of given<code>profile</code>.</p>      *<p><code>environment</code> may be used to select different<em>variants</em> of profile configuration files.      * For example, if<code>environment</code> is specified, configuration for<code>my.pid</code> PID will be read      * from<code>my.pid.cfg#&lt;environment&gt;</code>.</p>      * @param profile      * @param profiles      * @param environment      * @return      */
specifier|public
specifier|static
name|Profile
name|getOverlay
parameter_list|(
name|Profile
name|profile
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Profile
argument_list|>
name|profiles
parameter_list|,
name|String
name|environment
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|profile
argument_list|,
literal|"profile is null"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|profile
argument_list|,
literal|"profiles is null"
argument_list|)
expr_stmt|;
if|if
condition|(
name|profile
operator|.
name|isOverlay
argument_list|()
condition|)
block|{
return|return
name|profile
return|;
block|}
else|else
block|{
name|String
name|profileId
init|=
name|profile
operator|.
name|getId
argument_list|()
decl_stmt|;
name|ProfileBuilder
name|builder
init|=
name|ProfileBuilder
operator|.
name|Factory
operator|.
name|create
argument_list|(
name|profileId
argument_list|)
decl_stmt|;
operator|new
name|OverlayOptionsProvider
argument_list|(
name|profiles
argument_list|,
name|profile
argument_list|,
name|environment
argument_list|)
operator|.
name|addOptions
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|getProfile
argument_list|()
return|;
block|}
block|}
comment|/**      * Gets an<code>effective</code> profile with single property placeholder resolver for<code>${profile:xxx}</code>      * placeholders and with<code>finalSubstitution</code> set to<code>true</code>.      * @param profile      * @return      */
specifier|public
specifier|static
name|Profile
name|getEffective
parameter_list|(
specifier|final
name|Profile
name|profile
parameter_list|)
block|{
return|return
name|getEffective
argument_list|(
name|profile
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Gets an<code>effective</code> profile with single property placeholder resolver for<code>${profile:xxx}</code>      * placeholders.      * @param profile      * @param finalSubstitution      * @return      */
specifier|public
specifier|static
name|Profile
name|getEffective
parameter_list|(
specifier|final
name|Profile
name|profile
parameter_list|,
name|boolean
name|finalSubstitution
parameter_list|)
block|{
return|return
name|getEffective
argument_list|(
name|profile
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|PlaceholderResolvers
operator|.
name|ProfilePlaceholderResolver
argument_list|()
argument_list|)
argument_list|,
name|finalSubstitution
argument_list|)
return|;
block|}
comment|/**      * Gets an<code>effective</code> profile with<code>finalSubstitution</code> set to<code>true</code>.      * @param profile      * @param resolvers      * @return      */
specifier|public
specifier|static
name|Profile
name|getEffective
parameter_list|(
specifier|final
name|Profile
name|profile
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|PlaceholderResolver
argument_list|>
name|resolvers
parameter_list|)
block|{
return|return
name|getEffective
argument_list|(
name|profile
argument_list|,
name|resolvers
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      *<p>Gets an<em>effective</em> profile for given<code>profile</code>. Effective profile has all property      * placeholders resolved. When<code>finalSubstitution</code> is<code>true</code>, placeholders that can't      * be resolved are replaced with empty strings. When it's<code>false</code>, placeholders are left unchanged.</p>      * @param profile      * @param resolvers      * @param finalSubstitution      * @return      */
specifier|public
specifier|static
name|Profile
name|getEffective
parameter_list|(
specifier|final
name|Profile
name|profile
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|PlaceholderResolver
argument_list|>
name|resolvers
parameter_list|,
name|boolean
name|finalSubstitution
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|profile
argument_list|,
literal|"profile is null"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|profile
argument_list|,
literal|"resolvers is null"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|TypedProperties
argument_list|>
name|originals
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|TypedProperties
argument_list|>
name|originals2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|entry
range|:
name|profile
operator|.
name|getFileConfigurations
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|endsWith
argument_list|(
name|Profile
operator|.
name|PROPERTIES_SUFFIX
argument_list|)
condition|)
block|{
try|try
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|length
argument_list|()
operator|-
name|Profile
operator|.
name|PROPERTIES_SUFFIX
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|TypedProperties
name|props
init|=
operator|new
name|TypedProperties
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|originals
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|props
operator|=
operator|new
name|TypedProperties
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|props
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|originals2
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can not load properties for "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|final
name|Map
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
name|dynamic
init|=
name|TypedProperties
operator|.
name|prepare
argument_list|(
name|originals
argument_list|)
decl_stmt|;
name|TypedProperties
operator|.
name|substitute
argument_list|(
name|originals
argument_list|,
name|dynamic
argument_list|,
parameter_list|(
name|pid
parameter_list|,
name|key
parameter_list|,
name|value
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PlaceholderResolver
name|resolver
range|:
name|resolvers
control|)
block|{
if|if
condition|(
name|resolver
operator|.
name|getScheme
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|val
init|=
name|resolver
operator|.
name|resolve
argument_list|(
name|dynamic
argument_list|,
name|pid
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
return|return
name|val
return|;
block|}
block|}
block|}
if|if
condition|(
name|value
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|String
name|scheme
init|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|value
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|toSubst
init|=
name|value
operator|.
name|substring
argument_list|(
name|scheme
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|PlaceholderResolver
name|resolver
range|:
name|resolvers
control|)
block|{
if|if
condition|(
name|scheme
operator|.
name|equals
argument_list|(
name|resolver
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|val
init|=
name|resolver
operator|.
name|resolve
argument_list|(
name|dynamic
argument_list|,
name|pid
argument_list|,
name|key
argument_list|,
name|toSubst
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
return|return
name|val
return|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
argument_list|,
name|finalSubstitution
argument_list|)
expr_stmt|;
comment|// Force computation while preserving layout
name|ProfileBuilder
name|builder
init|=
name|ProfileBuilder
operator|.
name|Factory
operator|.
name|createFrom
argument_list|(
name|profile
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|cfg
range|:
name|originals
operator|.
name|keySet
argument_list|()
control|)
block|{
name|TypedProperties
name|original
init|=
name|originals
operator|.
name|get
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
name|TypedProperties
name|original2
init|=
name|originals2
operator|.
name|get
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
name|original2
operator|.
name|putAll
argument_list|(
name|original
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addFileConfiguration
argument_list|(
name|cfg
operator|+
name|Profile
operator|.
name|PROPERTIES_SUFFIX
argument_list|,
name|Utils
operator|.
name|toBytes
argument_list|(
name|original2
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Compute the new profile
return|return
name|builder
operator|.
name|getProfile
argument_list|()
return|;
block|}
comment|/**      *<p>Helper internal class to configure {@link ProfileBuilder} used to create an<em>overlay</em> profile.</p>      *<p>There are strict rules built on a concept of profiles being<em>containers of file configurations</em>.      * Each profile may contain files with the same name. Profiles may be set in multi-parent - child relationship.      * Such graph of profiles is searched in depth-first fashion, while child (being a root of the graph) has      * highest priority.</p>      *<p>Files from higher-priority profile override files from parent profiles. Special case are PID files (with      * {@link Profile#PROPERTIES_SUFFIX} extension). These files are not simply taken from child profiles. Child      * profiles may have own version of given PID configuration file, but these files are overwritten at property      * level.</p>      *<p>For example, if parent profile specifies:<pre>      * property1 = v1      * property2 = v2      *</pre> and child profile specifies:<pre>      * property1 = v1a      * property3 = v3a      *</pre>an<em>overlay</em> profile for child profile uses:<pre>      * property1 = v1a      * property2 = v2      * property3 = v3a      *</pre></p>      */
specifier|static
specifier|private
class|class
name|OverlayOptionsProvider
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Profile
argument_list|>
name|profiles
decl_stmt|;
specifier|private
specifier|final
name|Profile
name|self
decl_stmt|;
specifier|private
specifier|final
name|String
name|environment
decl_stmt|;
specifier|private
specifier|static
class|class
name|SupplementControl
block|{
name|byte
index|[]
name|data
decl_stmt|;
name|TypedProperties
name|props
decl_stmt|;
block|}
specifier|private
name|OverlayOptionsProvider
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Profile
argument_list|>
name|profiles
parameter_list|,
name|Profile
name|self
parameter_list|,
name|String
name|environment
parameter_list|)
block|{
name|this
operator|.
name|profiles
operator|=
name|profiles
expr_stmt|;
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|environment
operator|=
name|environment
expr_stmt|;
block|}
specifier|private
name|ProfileBuilder
name|addOptions
parameter_list|(
name|ProfileBuilder
name|builder
parameter_list|)
block|{
name|builder
operator|.
name|setAttributes
argument_list|(
name|self
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setFileConfigurations
argument_list|(
name|getFileConfigurations
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setOverlay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|getFileConfigurations
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|SupplementControl
argument_list|>
name|aggregate
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Profile
name|profile
range|:
name|getInheritedProfiles
argument_list|()
control|)
block|{
name|supplement
argument_list|(
name|profile
argument_list|,
name|aggregate
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|rc
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|SupplementControl
argument_list|>
name|entry
range|:
name|aggregate
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|SupplementControl
name|ctrl
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctrl
operator|.
name|props
operator|!=
literal|null
condition|)
block|{
name|ctrl
operator|.
name|data
operator|=
name|Utils
operator|.
name|toBytes
argument_list|(
name|ctrl
operator|.
name|props
argument_list|)
expr_stmt|;
block|}
name|rc
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|ctrl
operator|.
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|rc
return|;
block|}
specifier|private
name|List
argument_list|<
name|Profile
argument_list|>
name|getInheritedProfiles
parameter_list|()
block|{
name|List
argument_list|<
name|Profile
argument_list|>
name|profiles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|fillParentProfiles
argument_list|(
name|self
argument_list|,
name|profiles
argument_list|)
expr_stmt|;
return|return
name|profiles
return|;
block|}
specifier|private
name|void
name|fillParentProfiles
parameter_list|(
name|Profile
name|profile
parameter_list|,
name|List
argument_list|<
name|Profile
argument_list|>
name|profiles
parameter_list|)
block|{
if|if
condition|(
operator|!
name|profiles
operator|.
name|contains
argument_list|(
name|profile
argument_list|)
condition|)
block|{
for|for
control|(
name|String
name|parentId
range|:
name|profile
operator|.
name|getParentIds
argument_list|()
control|)
block|{
name|Profile
name|parent
init|=
name|getRequiredProfile
argument_list|(
name|parentId
argument_list|)
decl_stmt|;
name|fillParentProfiles
argument_list|(
name|parent
argument_list|,
name|profiles
argument_list|)
expr_stmt|;
block|}
name|profiles
operator|.
name|add
argument_list|(
name|profile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|supplement
parameter_list|(
name|Profile
name|profile
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|SupplementControl
argument_list|>
name|aggregate
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|configs
init|=
name|profile
operator|.
name|getFileConfigurations
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|configs
operator|.
name|keySet
argument_list|()
control|)
block|{
comment|// Ignore environment specific configs
if|if
condition|(
name|key
operator|.
name|contains
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|byte
index|[]
name|value
init|=
name|configs
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|environment
operator|!=
literal|null
operator|&&
name|configs
operator|.
name|containsKey
argument_list|(
name|key
operator|+
literal|"#"
operator|+
name|environment
argument_list|)
condition|)
block|{
name|value
operator|=
name|configs
operator|.
name|get
argument_list|(
name|key
operator|+
literal|"#"
operator|+
name|environment
argument_list|)
expr_stmt|;
block|}
comment|// we can use fine grained inheritance based updating if it's
comment|// a properties file.
if|if
condition|(
name|key
operator|.
name|endsWith
argument_list|(
name|Profile
operator|.
name|PROPERTIES_SUFFIX
argument_list|)
condition|)
block|{
name|SupplementControl
name|ctrl
init|=
name|aggregate
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctrl
operator|!=
literal|null
condition|)
block|{
comment|// we can update the file..
name|TypedProperties
name|childMap
init|=
name|Utils
operator|.
name|toProperties
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|childMap
operator|.
name|remove
argument_list|(
name|Profile
operator|.
name|DELETED
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|ctrl
operator|.
name|props
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|// Update the entries...
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|p
range|:
name|childMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|Profile
operator|.
name|DELETED
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|ctrl
operator|.
name|props
operator|.
name|remove
argument_list|(
name|p
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ctrl
operator|.
name|props
operator|.
name|put
argument_list|(
name|p
operator|.
name|getKey
argument_list|()
argument_list|,
name|p
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// new file..
name|ctrl
operator|=
operator|new
name|SupplementControl
argument_list|()
expr_stmt|;
name|ctrl
operator|.
name|props
operator|=
name|Utils
operator|.
name|toProperties
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|aggregate
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|ctrl
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// not a properties file? we can only overwrite.
name|SupplementControl
name|ctrl
init|=
operator|new
name|SupplementControl
argument_list|()
decl_stmt|;
name|ctrl
operator|.
name|data
operator|=
name|value
expr_stmt|;
name|aggregate
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|ctrl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Profile
name|getRequiredProfile
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Profile
name|profile
init|=
name|profiles
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|profile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to find required profile "
operator|+
name|id
argument_list|)
throw|;
block|}
return|return
name|profile
return|;
block|}
block|}
specifier|private
name|Profiles
parameter_list|()
block|{ }
block|}
end_class

end_unit

