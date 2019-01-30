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
name|tooling
operator|.
name|utils
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
name|FileWriter
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
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|maven
operator|.
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|layout
operator|.
name|DefaultRepositoryLayout
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|Snapshot
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|SnapshotVersion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|Versioning
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|io
operator|.
name|xpp3
operator|.
name|MetadataXpp3Writer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|artifact
operator|.
name|DefaultArtifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|repository
operator|.
name|RemoteRepository
import|;
end_import

begin_comment
comment|/**  * Util method for Maven manipulation (URL convert, metadata generation, etc).  */
end_comment

begin_class
specifier|public
class|class
name|MavenUtil
block|{
specifier|static
specifier|final
name|DefaultRepositoryLayout
name|layout
init|=
operator|new
name|DefaultRepositoryLayout
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|aetherPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"([^: ]+):([^: ]+)(:([^: ]*)(:([^: ]+))?)?:([^: ]+)"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|mvnPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(?:(?:wrap:)|(?:blueprint:))?mvn:([^/ ]+)/([^/ ]+)/([^/$ ]*)(/([^/$ ]+)(/([^/$ ]+))?)?(/\\$.+)?"
argument_list|)
decl_stmt|;
comment|/**      * Convert PAX URL mvn format to aether coordinate format.      * N.B. we do not handle repository-url in mvn urls.      * N.B. version is required in mvn urls.      *      * @param name PAX URL mvn format: mvn-uri := [ 'wrap:' ] 'mvn:' [ repository-url '!' ] group-id '/' artifact-id [ '/' [version] [ '/' [type] [ '/' classifier ] ] ] ]      * @return aether coordinate format:&lt;groupId&gt;:&lt;artifactId&gt;[:&lt;extension&gt;[:&lt;classifier&gt;]]:&lt;version&gt;      */
specifier|public
specifier|static
name|String
name|mvnToAether
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|mvnPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|name
return|;
block|}
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
comment|//groupId
name|b
operator|.
name|append
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
comment|//artifactId
name|String
name|extension
init|=
name|m
operator|.
name|group
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|String
name|classifier
init|=
name|m
operator|.
name|group
argument_list|(
literal|7
argument_list|)
decl_stmt|;
if|if
condition|(
name|present
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
if|if
condition|(
name|present
argument_list|(
name|extension
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
name|extension
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
literal|"jar:"
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|present
argument_list|(
name|extension
argument_list|)
operator|&&
operator|!
literal|"jar"
operator|.
name|equals
argument_list|(
name|extension
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
name|extension
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
block|}
block|}
name|b
operator|.
name|append
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Convert PAX URL mvn format to an aether Artifact.      * N.B. we do not handle repository-url in mvn urls.      * N.B. version is required in mvn urls.      *      * @param name PAX URL mvn format: mvn-uri := [ 'wrap:' ] 'mvn:' [ repository-url '!' ] group-id '/' artifact-id [ '/' [version] [ '/' [type] [ '/' classifier ] ] ] ]      * @return aether Artifact      */
specifier|public
specifier|static
name|DefaultArtifact
name|mvnToArtifact
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|mvnPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|name
argument_list|)
return|;
block|}
name|String
name|groupId
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|m
operator|.
name|group
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|String
name|extension
init|=
name|m
operator|.
name|group
argument_list|(
literal|5
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|present
argument_list|(
name|extension
argument_list|)
condition|)
block|{
name|extension
operator|=
literal|"jar"
expr_stmt|;
block|}
name|String
name|classifier
init|=
name|m
operator|.
name|group
argument_list|(
literal|7
argument_list|)
decl_stmt|;
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|present
argument_list|(
name|classifier
argument_list|)
condition|?
name|classifier
else|:
literal|""
argument_list|,
name|extension
argument_list|,
name|version
argument_list|)
return|;
block|}
comment|/**      * Convert Aether coordinate format to an aether Artifact.      * N.B. we do not handle repository-url in mvn urls.      * N.B. version is required in mvn urls.      *      * @param name aether coordinate format:&lt;groupId&gt;:&lt;artifactId&gt;[:&lt;extension&gt;[:&lt;classifier&gt;]]:&lt;version&gt;      * @return aether Artifact      */
specifier|public
specifier|static
name|DefaultArtifact
name|aetherToArtifact
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|aetherPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|name
argument_list|)
return|;
block|}
name|String
name|groupId
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|m
operator|.
name|group
argument_list|(
literal|7
argument_list|)
decl_stmt|;
name|String
name|extension
init|=
name|m
operator|.
name|group
argument_list|(
literal|4
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|present
argument_list|(
name|extension
argument_list|)
condition|)
block|{
name|extension
operator|=
literal|"jar"
expr_stmt|;
block|}
name|String
name|classifier
init|=
name|m
operator|.
name|group
argument_list|(
literal|6
argument_list|)
decl_stmt|;
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|present
argument_list|(
name|classifier
argument_list|)
condition|?
name|classifier
else|:
literal|""
argument_list|,
name|extension
argument_list|,
name|version
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|boolean
name|present
parameter_list|(
name|String
name|part
parameter_list|)
block|{
return|return
name|part
operator|!=
literal|null
operator|&&
operator|!
name|part
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * Convert Aether coordinate format to PAX mvn format.      * N.B. we do not handle repository-url in mvn urls.      * N.B. version is required in mvn urls.      *      * @param name aether coordinate format:&lt;groupId&gt;:&lt;artifactId&gt;[:&lt;extension&gt;[:&lt;classifier&gt;]]:&lt;version&gt;      * @return PAX URL mvn format: mvn-uri := 'mvn:' [ repository-url '!' ] group-id '/' artifact-id [ '/' [version] [ '/' [type] [ '/' classifier ] ] ] ]      */
specifier|public
specifier|static
name|String
name|aetherToMvn
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|aetherPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|name
return|;
block|}
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"mvn:"
argument_list|)
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
comment|//groupId
name|b
operator|.
name|append
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
comment|//artifactId
name|b
operator|.
name|append
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|7
argument_list|)
argument_list|)
expr_stmt|;
comment|//version
name|String
name|extension
init|=
name|m
operator|.
name|group
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|String
name|classifier
init|=
name|m
operator|.
name|group
argument_list|(
literal|6
argument_list|)
decl_stmt|;
if|if
condition|(
name|present
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
if|if
condition|(
name|present
argument_list|(
name|extension
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|extension
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
literal|"/jar"
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|present
argument_list|(
name|extension
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|extension
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isEmpty
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
return|return
name|classifier
operator|==
literal|null
operator|||
name|classifier
operator|.
name|length
argument_list|()
operator|==
literal|0
return|;
block|}
comment|/**      * Generate the maven-metadata-local.xml for the given Maven<code>Artifact</code>.      *      * @param artifact the Maven<code>Artifact</code>.      * @param target   the target maven-metadata-local.xml file to generate.      * @throws IOException if the maven-metadata-local.xml can't be generated.      */
specifier|public
specifier|static
name|void
name|generateMavenMetadata
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|target
parameter_list|)
throws|throws
name|IOException
block|{
name|target
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|metadata
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setModelVersion
argument_list|(
literal|"1.1.0"
argument_list|)
expr_stmt|;
name|Versioning
name|versioning
init|=
operator|new
name|Versioning
argument_list|()
decl_stmt|;
name|versioning
operator|.
name|setLastUpdatedTimestamp
argument_list|(
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Snapshot
name|snapshot
init|=
operator|new
name|Snapshot
argument_list|()
decl_stmt|;
name|snapshot
operator|.
name|setLocalCopy
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|setSnapshot
argument_list|(
name|snapshot
argument_list|)
expr_stmt|;
name|SnapshotVersion
name|snapshotVersion
init|=
operator|new
name|SnapshotVersion
argument_list|()
decl_stmt|;
name|snapshotVersion
operator|.
name|setClassifier
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|snapshotVersion
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|snapshotVersion
operator|.
name|setExtension
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|snapshotVersion
operator|.
name|setUpdated
argument_list|(
name|versioning
operator|.
name|getLastUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|addSnapshotVersion
argument_list|(
name|snapshotVersion
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setVersioning
argument_list|(
name|versioning
argument_list|)
expr_stmt|;
name|MetadataXpp3Writer
name|metadataWriter
init|=
operator|new
name|MetadataXpp3Writer
argument_list|()
decl_stmt|;
name|Writer
name|writer
init|=
operator|new
name|FileWriter
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|metadataWriter
operator|.
name|write
argument_list|(
name|writer
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getFileName
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"-"
operator|+
name|artifact
operator|.
name|getVersion
argument_list|()
operator|+
operator|(
name|artifact
operator|.
name|getClassifier
argument_list|()
operator|!=
literal|null
condition|?
literal|"-"
operator|+
name|artifact
operator|.
name|getClassifier
argument_list|()
else|:
literal|""
operator|)
operator|+
literal|"."
operator|+
name|artifact
operator|.
name|getType
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|getDir
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"/"
operator|+
name|artifact
operator|.
name|getBaseVersion
argument_list|()
operator|+
literal|"/"
return|;
block|}
comment|/**      * Changes maven configuration of remote repositories to a list of repositories for pax-url-aether      * @param remoteRepositories      * @return      */
specifier|public
specifier|static
name|String
name|remoteRepositoryList
parameter_list|(
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|remoteRepositories
parameter_list|)
block|{
name|StringBuilder
name|remotes
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|RemoteRepository
name|rr
range|:
name|remoteRepositories
control|)
block|{
if|if
condition|(
name|remotes
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|remotes
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|remotes
operator|.
name|append
argument_list|(
name|rr
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|remotes
operator|.
name|append
argument_list|(
literal|"@id="
argument_list|)
operator|.
name|append
argument_list|(
name|rr
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|rr
operator|.
name|getPolicy
argument_list|(
literal|false
argument_list|)
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
name|remotes
operator|.
name|append
argument_list|(
literal|"@noreleases"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rr
operator|.
name|getPolicy
argument_list|(
literal|true
argument_list|)
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
name|remotes
operator|.
name|append
argument_list|(
literal|"@snapshots"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|remotes
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

