begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2007 Alin Dreghiciu.  * Copyright 2010,2011 Toni Menzel.  *   * Licensed  under the  Apache License,  Version 2.0  (the "License");  * you may not use  this file  except in  compliance with the License.  * You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed  under the  License is distributed on an "AS IS" BASIS,  * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or  * implied.  *  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|maven
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

begin_comment
comment|/**  * Parser for mvn: protocol.<br/>  *  * @author Alin Dreghiciu  * @author Toni Menzel  *  * @since August 10, 2007  */
end_comment

begin_class
specifier|public
class|class
name|Parser
block|{
comment|/**      * Default version if none present in the url.      */
specifier|public
specifier|static
specifier|final
name|String
name|VERSION_LATEST
init|=
literal|"LATEST"
decl_stmt|;
comment|/**      * Syntax for the url; to be shown on exception messages.      */
specifier|private
specifier|static
specifier|final
name|String
name|SYNTAX
init|=
literal|"mvn:[repository_url!]groupId/artifactId[/[version]/[type]]"
decl_stmt|;
comment|/**      * Separator between repository and artifact definition.      */
specifier|private
specifier|static
specifier|final
name|String
name|REPOSITORY_SEPARATOR
init|=
literal|"!"
decl_stmt|;
comment|/**      * Artifact definition segments separator.      */
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACT_SEPARATOR
init|=
literal|"/"
decl_stmt|;
comment|/**      * Snapshot version      */
specifier|private
specifier|static
specifier|final
name|String
name|VERSION_SNAPSHOT
init|=
literal|"SNAPSHOT"
decl_stmt|;
comment|/**      * Default type if not present in the url.      */
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_JAR
init|=
literal|"jar"
decl_stmt|;
comment|/**      * Final artifact path separator.      */
specifier|public
specifier|static
specifier|final
name|String
name|FILE_SEPARATOR
init|=
literal|"/"
decl_stmt|;
comment|/**      * Group id path separator.      */
specifier|private
specifier|static
specifier|final
name|String
name|GROUP_SEPARATOR
init|=
literal|"\\."
decl_stmt|;
comment|/**      * Separator used to constructs the artifact file name.      */
specifier|private
specifier|static
specifier|final
name|String
name|VERSION_SEPARATOR
init|=
literal|"-"
decl_stmt|;
comment|/**      * Artifact extension(type) separator.      */
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_SEPARATOR
init|=
literal|"."
decl_stmt|;
comment|/**      * Separator used to separate classifier in artifact name.      */
specifier|private
specifier|static
specifier|final
name|String
name|CLASSIFIER_SEPARATOR
init|=
literal|"-"
decl_stmt|;
comment|/**      * Maven metadata file.      */
specifier|private
specifier|static
specifier|final
name|String
name|METADATA_FILE
init|=
literal|"maven-metadata.xml"
decl_stmt|;
comment|/**      * Maven local metadata file.      */
specifier|private
specifier|static
specifier|final
name|String
name|METADATA_FILE_LOCAL
init|=
literal|"maven-metadata-local.xml"
decl_stmt|;
comment|/**      * Repository URL. Null if not present.      */
specifier|private
name|String
name|m_repositoryURL
decl_stmt|;
comment|/**      * Artifact group id.      */
specifier|private
name|String
name|m_group
decl_stmt|;
comment|/**      * Artifact id.      */
specifier|private
name|String
name|m_artifact
decl_stmt|;
comment|/**      * Artifact version.      */
specifier|private
name|String
name|m_version
decl_stmt|;
comment|/**      * Artifact type.      */
specifier|private
name|String
name|m_type
decl_stmt|;
comment|/**      * Artifact classifier.      */
specifier|private
name|String
name|m_classifier
decl_stmt|;
comment|/**      * Artifact classifier to use to build artifact name.      */
specifier|private
name|String
name|m_fullClassifier
decl_stmt|;
comment|/**      * Creates a new protocol parser.      *      * @param path the path part of the url (without starting mvn:)      *      * @throws MalformedURLException if provided path does not comply to expected syntax or an malformed repository URL      */
specifier|public
name|Parser
parameter_list|(
specifier|final
name|String
name|path
parameter_list|)
throws|throws
name|MalformedURLException
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
literal|"Path cannot be null. Syntax "
operator|+
name|SYNTAX
argument_list|)
throw|;
block|}
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|REPOSITORY_SEPARATOR
argument_list|)
operator|||
name|path
operator|.
name|endsWith
argument_list|(
name|REPOSITORY_SEPARATOR
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
literal|"Path cannot start or end with "
operator|+
name|REPOSITORY_SEPARATOR
operator|+
literal|". Syntax "
operator|+
name|SYNTAX
argument_list|)
throw|;
block|}
if|if
condition|(
name|path
operator|.
name|contains
argument_list|(
name|REPOSITORY_SEPARATOR
argument_list|)
condition|)
block|{
name|int
name|pos
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
name|REPOSITORY_SEPARATOR
argument_list|)
decl_stmt|;
name|parseArtifactPart
argument_list|(
name|path
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|m_repositoryURL
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
operator|+
literal|"@snapshots"
expr_stmt|;
block|}
else|else
block|{
name|parseArtifactPart
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the artifact path from the given maven uri.      * @param uri the maven uri      * @return the artifact path      * @throws MalformedURLException      */
specifier|public
specifier|static
name|String
name|pathFromMaven
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|MalformedURLException
block|{
if|if
condition|(
operator|!
name|uri
operator|.
name|startsWith
argument_list|(
literal|"mvn:"
argument_list|)
condition|)
block|{
return|return
name|uri
return|;
block|}
return|return
operator|new
name|Parser
argument_list|(
name|uri
operator|.
name|substring
argument_list|(
literal|"mvn:"
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
operator|.
name|getArtifactPath
argument_list|()
return|;
block|}
comment|/**      * Parses the artifact part of the url ( without the repository).      *      * @param part url part without protocol and repository.      *      * @throws MalformedURLException if provided path does not comply to syntax.      */
specifier|private
name|void
name|parseArtifactPart
parameter_list|(
specifier|final
name|String
name|part
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|String
index|[]
name|segments
init|=
name|part
operator|.
name|split
argument_list|(
name|ARTIFACT_SEPARATOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|segments
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
literal|"Invalid path. Syntax "
operator|+
name|SYNTAX
argument_list|)
throw|;
block|}
comment|// we must have a valid group
name|m_group
operator|=
name|segments
index|[
literal|0
index|]
expr_stmt|;
if|if
condition|(
name|m_group
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
literal|"Invalid groupId. Syntax "
operator|+
name|SYNTAX
argument_list|)
throw|;
block|}
comment|// valid artifact
name|m_artifact
operator|=
name|segments
index|[
literal|1
index|]
expr_stmt|;
if|if
condition|(
name|m_artifact
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
literal|"Invalid artifactId. Syntax "
operator|+
name|SYNTAX
argument_list|)
throw|;
block|}
comment|// version is optional but we have a default value
name|m_version
operator|=
name|VERSION_LATEST
expr_stmt|;
if|if
condition|(
name|segments
operator|.
name|length
operator|>=
literal|3
operator|&&
name|segments
index|[
literal|2
index|]
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|m_version
operator|=
name|segments
index|[
literal|2
index|]
expr_stmt|;
block|}
comment|// type is optional but we have a default value
name|m_type
operator|=
name|TYPE_JAR
expr_stmt|;
if|if
condition|(
name|segments
operator|.
name|length
operator|>=
literal|4
operator|&&
name|segments
index|[
literal|3
index|]
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|m_type
operator|=
name|segments
index|[
literal|3
index|]
expr_stmt|;
block|}
comment|// classifier is optional (if not present or empty we will have a null classifier
name|m_fullClassifier
operator|=
literal|""
expr_stmt|;
if|if
condition|(
name|segments
operator|.
name|length
operator|>=
literal|5
operator|&&
name|segments
index|[
literal|4
index|]
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|m_classifier
operator|=
name|segments
index|[
literal|4
index|]
expr_stmt|;
name|m_fullClassifier
operator|=
name|CLASSIFIER_SEPARATOR
operator|+
name|m_classifier
expr_stmt|;
block|}
block|}
comment|/**      * Returns the repository URL if present, null otherwise      *      * @return repository URL      */
specifier|public
name|String
name|getRepositoryURL
parameter_list|()
block|{
return|return
name|m_repositoryURL
return|;
block|}
comment|/**      * Returns the group id of the artifact.      *      * @return group Id      */
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|m_group
return|;
block|}
comment|/**      * Returns the artifact id.      *      * @return artifact id      */
specifier|public
name|String
name|getArtifact
parameter_list|()
block|{
return|return
name|m_artifact
return|;
block|}
comment|/**      * Returns the artifact version.      *      * @return version      */
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|m_version
return|;
block|}
comment|/**      * Returns the artifact type.      *      * @return type      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|m_type
return|;
block|}
comment|/**      * Returns the artifact classifier.      *      * @return classifier      */
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|m_classifier
return|;
block|}
comment|/**      * Returns the complete path to artifact as stated by Maven 2 repository layout.      *      * @return artifact path      */
specifier|public
name|String
name|getArtifactPath
parameter_list|()
block|{
return|return
name|getArtifactPath
argument_list|(
name|m_version
argument_list|)
return|;
block|}
comment|/**      * Returns the complete path to artifact as stated by Maven 2 repository layout.      *      * @param version The version of the artifact.      *      * @return artifact path      */
specifier|public
name|String
name|getArtifactPath
parameter_list|(
specifier|final
name|String
name|version
parameter_list|)
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|m_group
operator|.
name|replaceAll
argument_list|(
name|GROUP_SEPARATOR
argument_list|,
name|FILE_SEPARATOR
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|m_artifact
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|m_artifact
argument_list|)
operator|.
name|append
argument_list|(
name|VERSION_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
name|m_fullClassifier
argument_list|)
operator|.
name|append
argument_list|(
name|TYPE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|m_type
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns the version for an artifact for a snapshot version.      *      * @param version     The version of the snapshot.      * @param timestamp   The timestamp of the snapshot.      * @param buildnumber The buildnumber of the snapshot.      *      * @return artifact path      */
specifier|public
name|String
name|getSnapshotVersion
parameter_list|(
specifier|final
name|String
name|version
parameter_list|,
specifier|final
name|String
name|timestamp
parameter_list|,
specifier|final
name|String
name|buildnumber
parameter_list|)
block|{
return|return
name|version
operator|.
name|replace
argument_list|(
name|VERSION_SNAPSHOT
argument_list|,
name|timestamp
argument_list|)
operator|+
name|VERSION_SEPARATOR
operator|+
name|buildnumber
return|;
block|}
comment|/**      * Returns the complete path to artifact for a snapshot file.      *      * @param version     The version of the snapshot.      * @param timestamp   The timestamp of the snapshot.      * @param buildnumber The buildnumber of the snapshot.      *      * @return artifact path      */
specifier|public
name|String
name|getSnapshotPath
parameter_list|(
specifier|final
name|String
name|version
parameter_list|,
specifier|final
name|String
name|timestamp
parameter_list|,
specifier|final
name|String
name|buildnumber
parameter_list|)
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|m_group
operator|.
name|replaceAll
argument_list|(
name|GROUP_SEPARATOR
argument_list|,
name|FILE_SEPARATOR
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|m_artifact
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|m_artifact
argument_list|)
operator|.
name|append
argument_list|(
name|VERSION_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|getSnapshotVersion
argument_list|(
name|version
argument_list|,
name|timestamp
argument_list|,
name|buildnumber
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|m_fullClassifier
argument_list|)
operator|.
name|append
argument_list|(
name|TYPE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|m_type
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns the path to metdata file corresponding to this artifact version.      *      * @param version The version of the the metadata.      *      * @return metadata file path      */
specifier|public
name|String
name|getVersionMetadataPath
parameter_list|(
specifier|final
name|String
name|version
parameter_list|)
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|m_group
operator|.
name|replaceAll
argument_list|(
name|GROUP_SEPARATOR
argument_list|,
name|FILE_SEPARATOR
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|m_artifact
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|METADATA_FILE
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns the path to local metdata file corresponding to this artifact version.      *      * @param version The version of the the metadata.      *      * @return metadata file path      */
specifier|public
name|String
name|getVersionLocalMetadataPath
parameter_list|(
specifier|final
name|String
name|version
parameter_list|)
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|m_group
operator|.
name|replaceAll
argument_list|(
name|GROUP_SEPARATOR
argument_list|,
name|FILE_SEPARATOR
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|m_artifact
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|METADATA_FILE_LOCAL
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns the complete path to artifact local metadata file.      *      * @return artifact path      */
specifier|public
name|String
name|getArtifactLocalMetdataPath
parameter_list|()
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|m_group
operator|.
name|replaceAll
argument_list|(
name|GROUP_SEPARATOR
argument_list|,
name|FILE_SEPARATOR
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|m_artifact
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|METADATA_FILE_LOCAL
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns the complete path to artifact metadata file.      *      * @return artifact path      */
specifier|public
name|String
name|getArtifactMetdataPath
parameter_list|()
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|m_group
operator|.
name|replaceAll
argument_list|(
name|GROUP_SEPARATOR
argument_list|,
name|FILE_SEPARATOR
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|m_artifact
argument_list|)
operator|.
name|append
argument_list|(
name|FILE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|METADATA_FILE
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

