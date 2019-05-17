begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2008 Alin Dreghiciu.  *  * Licensed  under the  Apache License,  Version 2.0  (the "License");  * you may not use  this file  except in  compliance with the License.  * You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed  under the  License is distributed on an "AS IS" BASIS,  * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or  * implied.  *  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|maven
operator|.
name|core
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
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|Objects
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|mvn
operator|.
name|ServiceConstants
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
comment|/**  * An URL of Maven repository that knows if it contains SNAPSHOT versions and/or releases.  *  * @author Alin Dreghiciu  * @author Guillaume Nodet  * @since 0.2.1, February 07, 2008  *  * @see org.ops4j.pax.url.mvn.internal.config.MavenRepositoryURL  */
end_comment

begin_class
specifier|public
class|class
name|MavenRepositoryURL
block|{
comment|/*      * String OPTION_ALLOW_SNAPSHOTS = "snapshots";      * String OPTION_DISALLOW_RELEASES = "noreleases";      * String OPTION_MULTI = "multi";      * String OPTION_ID = "id";      * String OPTION_UPDATE = "update";      * String OPTION_RELEASES_UPDATE = "releasesUpdate";      * String OPTION_SNAPSHOTS_UPDATE = "snapshotsUpdate";      * String OPTION_CHECKSUM = "checksum";      * String OPTION_RELEASES_CHECKSUM = "releasesChecksum";      * String OPTION_SNAPSHOTS_CHECKSUM = "snapshotsChecksum";      */
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MavenRepositoryURL
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Repository Id.      */
specifier|private
specifier|final
name|String
name|m_id
decl_stmt|;
comment|/**      * Repository URL.      */
specifier|private
name|URL
name|m_repositoryURL
decl_stmt|;
comment|/**      * Repository file (only if URL is a file URL).      */
specifier|private
specifier|final
name|File
name|m_file
decl_stmt|;
comment|/**      * True if the repository contains snapshots.      */
specifier|private
name|boolean
name|m_snapshotsEnabled
decl_stmt|;
comment|/**      * True if the repository contains releases.      */
specifier|private
name|boolean
name|m_releasesEnabled
decl_stmt|;
comment|/**      * Repository update policy      */
specifier|private
name|String
name|m_releasesUpdatePolicy
decl_stmt|;
comment|/**      * Repository update policy      */
specifier|private
name|String
name|m_snapshotsUpdatePolicy
decl_stmt|;
comment|/**      * Repository checksum policy      */
specifier|private
name|String
name|m_releasesChecksumPolicy
decl_stmt|;
comment|/**      * Repository checksum policy      */
specifier|private
name|String
name|m_snapshotsChecksumPolicy
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|m_multi
decl_stmt|;
comment|/**      * Where the repository was defined (PID or settings.xml)      */
specifier|private
specifier|final
name|FROM
name|m_from
decl_stmt|;
comment|/**      * Creates a maven repository URL bases on a string spec. The path can be marked with @snapshots and/or @noreleases      * (not case sensitive).      *      * @param repositorySpec url spec of repository      *      * @throws MalformedURLException if spec contains a malformed maven repository url      */
specifier|public
name|MavenRepositoryURL
parameter_list|(
specifier|final
name|String
name|repositorySpec
parameter_list|)
throws|throws
name|MalformedURLException
block|{
specifier|final
name|String
index|[]
name|segments
init|=
name|repositorySpec
operator|.
name|split
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
decl_stmt|;
specifier|final
name|StringBuilder
name|urlBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|snapshotEnabled
init|=
literal|false
decl_stmt|;
name|boolean
name|releasesEnabled
init|=
literal|true
decl_stmt|;
name|boolean
name|multi
init|=
literal|false
decl_stmt|;
name|String
name|name
init|=
literal|null
decl_stmt|;
name|String
name|update
init|=
literal|null
decl_stmt|;
name|String
name|updateReleases
init|=
literal|null
decl_stmt|;
name|String
name|updateSnapshots
init|=
literal|null
decl_stmt|;
name|String
name|checksum
init|=
literal|null
decl_stmt|;
name|String
name|checksumReleases
init|=
literal|null
decl_stmt|;
name|String
name|checksumSnapshots
init|=
literal|null
decl_stmt|;
name|FROM
name|from
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
name|i
operator|<
name|segments
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|segment
init|=
name|segments
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|segment
operator|.
name|equalsIgnoreCase
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_ALLOW_SNAPSHOTS
argument_list|)
condition|)
block|{
name|snapshotEnabled
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|segment
operator|.
name|equalsIgnoreCase
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_DISALLOW_RELEASES
argument_list|)
condition|)
block|{
name|releasesEnabled
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|segment
operator|.
name|equalsIgnoreCase
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_MULTI
argument_list|)
condition|)
block|{
name|multi
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|segment
operator|.
name|startsWith
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_ID
operator|+
literal|"="
argument_list|)
condition|)
block|{
try|try
block|{
name|name
operator|=
name|segments
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem with segment "
operator|+
name|segments
index|[
name|i
index|]
operator|+
literal|" in "
operator|+
name|repositorySpec
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|segment
operator|.
name|startsWith
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_RELEASES_UPDATE
operator|+
literal|"="
argument_list|)
condition|)
block|{
try|try
block|{
name|updateReleases
operator|=
name|segments
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem with segment "
operator|+
name|segments
index|[
name|i
index|]
operator|+
literal|" in "
operator|+
name|repositorySpec
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|segment
operator|.
name|startsWith
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_SNAPSHOTS_UPDATE
operator|+
literal|"="
argument_list|)
condition|)
block|{
try|try
block|{
name|updateSnapshots
operator|=
name|segments
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem with segment "
operator|+
name|segments
index|[
name|i
index|]
operator|+
literal|" in "
operator|+
name|repositorySpec
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|segment
operator|.
name|startsWith
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_UPDATE
operator|+
literal|"="
argument_list|)
condition|)
block|{
try|try
block|{
name|update
operator|=
name|segments
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem with segment "
operator|+
name|segments
index|[
name|i
index|]
operator|+
literal|" in "
operator|+
name|repositorySpec
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|segment
operator|.
name|startsWith
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_RELEASES_CHECKSUM
operator|+
literal|"="
argument_list|)
condition|)
block|{
try|try
block|{
name|checksumReleases
operator|=
name|segments
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem with segment "
operator|+
name|segments
index|[
name|i
index|]
operator|+
literal|" in "
operator|+
name|repositorySpec
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|segment
operator|.
name|startsWith
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_SNAPSHOTS_CHECKSUM
operator|+
literal|"="
argument_list|)
condition|)
block|{
try|try
block|{
name|checksumSnapshots
operator|=
name|segments
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem with segment "
operator|+
name|segments
index|[
name|i
index|]
operator|+
literal|" in "
operator|+
name|repositorySpec
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|segment
operator|.
name|startsWith
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_CHECKSUM
operator|+
literal|"="
argument_list|)
condition|)
block|{
try|try
block|{
name|checksum
operator|=
name|segments
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem with segment "
operator|+
name|segments
index|[
name|i
index|]
operator|+
literal|" in "
operator|+
name|repositorySpec
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|segment
operator|.
name|startsWith
argument_list|(
literal|"_from="
argument_list|)
condition|)
block|{
try|try
block|{
name|from
operator|=
name|FROM
operator|.
name|valueOf
argument_list|(
name|segments
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{                 }
block|}
else|else
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|urlBuilder
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
expr_stmt|;
block|}
name|urlBuilder
operator|.
name|append
argument_list|(
name|segments
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|spec
init|=
name|buildSpec
argument_list|(
name|urlBuilder
argument_list|)
decl_stmt|;
name|m_repositoryURL
operator|=
operator|new
name|URL
argument_list|(
name|spec
argument_list|)
expr_stmt|;
name|m_snapshotsEnabled
operator|=
name|snapshotEnabled
expr_stmt|;
name|m_releasesEnabled
operator|=
name|releasesEnabled
expr_stmt|;
name|m_multi
operator|=
name|multi
expr_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|String
name|warn
init|=
literal|"Repository spec "
operator|+
name|spec
operator|+
literal|" does not contain an identifier. This is deprecated& discouraged& just evil."
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
name|warn
argument_list|)
expr_stmt|;
name|name
operator|=
literal|"repo_"
operator|+
name|spec
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
name|m_id
operator|=
name|name
expr_stmt|;
name|m_releasesUpdatePolicy
operator|=
name|updateReleases
operator|!=
literal|null
condition|?
name|updateReleases
else|:
name|update
expr_stmt|;
name|m_snapshotsUpdatePolicy
operator|=
name|updateSnapshots
operator|!=
literal|null
condition|?
name|updateSnapshots
else|:
name|update
expr_stmt|;
name|m_releasesChecksumPolicy
operator|=
name|checksumReleases
operator|!=
literal|null
condition|?
name|checksumReleases
else|:
name|checksum
expr_stmt|;
name|m_snapshotsChecksumPolicy
operator|=
name|checksumSnapshots
operator|!=
literal|null
condition|?
name|checksumSnapshots
else|:
name|checksum
expr_stmt|;
name|m_from
operator|=
name|from
operator|!=
literal|null
condition|?
name|from
else|:
name|FROM
operator|.
name|PID
expr_stmt|;
if|if
condition|(
name|m_repositoryURL
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
literal|"file"
argument_list|)
condition|)
block|{
try|try
block|{
comment|// You must transform to URI to decode the path (manage a path with a space or non
comment|// us character)
comment|// like D:/documents%20and%20Settings/SESA170017/.m2/repository
comment|// the path can be store in path part or in scheme specific part (if is relatif
comment|// path)
comment|// the anti-slash character is not a valid character for uri.
name|spec
operator|=
name|spec
operator|.
name|replaceAll
argument_list|(
literal|"\\\\"
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|spec
operator|=
name|spec
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
expr_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|spec
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|uri
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
name|path
operator|=
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
expr_stmt|;
name|m_file
operator|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|m_file
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|buildSpec
parameter_list|(
name|StringBuilder
name|urlBuilder
parameter_list|)
block|{
name|String
name|spec
init|=
name|urlBuilder
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|spec
operator|.
name|endsWith
argument_list|(
literal|"\\"
argument_list|)
operator|&&
operator|!
name|spec
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|spec
operator|=
name|spec
operator|+
literal|"/"
expr_stmt|;
block|}
return|return
name|spec
return|;
block|}
comment|/**      * Getter.      *      * @return repository id      */
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|m_id
return|;
block|}
comment|/**      * Getter.      *      * @return repository URL      */
specifier|public
name|URL
name|getURL
parameter_list|()
block|{
return|return
name|m_repositoryURL
return|;
block|}
specifier|public
name|void
name|setURL
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|this
operator|.
name|m_repositoryURL
operator|=
name|url
expr_stmt|;
block|}
comment|/**      * Getter.      *      * @return repository file      */
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|m_file
return|;
block|}
comment|/**      * Getter.      *      * @return true if the repository contains releases      */
specifier|public
name|boolean
name|isReleasesEnabled
parameter_list|()
block|{
return|return
name|m_releasesEnabled
return|;
block|}
specifier|public
name|void
name|setReleasesEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|m_releasesEnabled
operator|=
name|enabled
expr_stmt|;
block|}
comment|/**      * Getter.      *      * @return true if the repository contains snapshots      */
specifier|public
name|boolean
name|isSnapshotsEnabled
parameter_list|()
block|{
return|return
name|m_snapshotsEnabled
return|;
block|}
specifier|public
name|void
name|setSnapshotsEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|m_snapshotsEnabled
operator|=
name|enabled
expr_stmt|;
block|}
specifier|public
name|String
name|getReleasesUpdatePolicy
parameter_list|()
block|{
return|return
name|m_releasesUpdatePolicy
return|;
block|}
specifier|public
name|String
name|getSnapshotsUpdatePolicy
parameter_list|()
block|{
return|return
name|m_snapshotsUpdatePolicy
return|;
block|}
specifier|public
name|String
name|getReleasesChecksumPolicy
parameter_list|()
block|{
return|return
name|m_releasesChecksumPolicy
return|;
block|}
specifier|public
name|String
name|getSnapshotsChecksumPolicy
parameter_list|()
block|{
return|return
name|m_snapshotsChecksumPolicy
return|;
block|}
specifier|public
name|void
name|setReleasesUpdatePolicy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|m_releasesUpdatePolicy
operator|=
name|policy
expr_stmt|;
block|}
specifier|public
name|void
name|setSnapshotsUpdatePolicy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|m_snapshotsUpdatePolicy
operator|=
name|policy
expr_stmt|;
block|}
specifier|public
name|void
name|setReleasesChecksumPolicy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|m_releasesChecksumPolicy
operator|=
name|policy
expr_stmt|;
block|}
specifier|public
name|void
name|setSnapshotsChecksumPolicy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|m_snapshotsChecksumPolicy
operator|=
name|policy
expr_stmt|;
block|}
specifier|public
name|FROM
name|getFrom
parameter_list|()
block|{
return|return
name|m_from
return|;
block|}
comment|/**      * Getter.      *      * @return true if the repository is a parent path of repos      */
specifier|public
name|boolean
name|isMulti
parameter_list|()
block|{
return|return
name|m_multi
return|;
block|}
comment|/**      * Getter.      *      * @return if the repository is a file based repository.      */
specifier|public
name|boolean
name|isFileRepository
parameter_list|()
block|{
return|return
name|m_file
operator|!=
literal|null
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
name|m_repositoryURL
operator|.
name|toString
argument_list|()
operator|+
literal|",releases="
operator|+
name|m_releasesEnabled
operator|+
literal|",snapshots="
operator|+
name|m_snapshotsEnabled
return|;
block|}
specifier|public
name|String
name|asRepositorySpec
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|m_repositoryURL
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|m_id
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_ID
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|m_id
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|m_releasesEnabled
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_DISALLOW_RELEASES
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m_snapshotsEnabled
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_ALLOW_SNAPSHOTS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m_releasesEnabled
condition|)
block|{
if|if
condition|(
operator|!
name|m_snapshotsEnabled
condition|)
block|{
if|if
condition|(
name|m_releasesUpdatePolicy
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_RELEASES_UPDATE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|m_releasesUpdatePolicy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m_releasesChecksumPolicy
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_RELEASES_CHECKSUM
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|m_releasesChecksumPolicy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|m_snapshotsEnabled
condition|)
block|{
if|if
condition|(
operator|!
name|m_releasesEnabled
condition|)
block|{
if|if
condition|(
name|m_snapshotsUpdatePolicy
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_SNAPSHOTS_UPDATE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|m_snapshotsUpdatePolicy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m_snapshotsChecksumPolicy
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_SNAPSHOTS_CHECKSUM
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|m_snapshotsChecksumPolicy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|m_snapshotsEnabled
operator|&&
name|m_releasesEnabled
condition|)
block|{
comment|// compact snapshots& release update& checksum policies
if|if
condition|(
name|m_releasesUpdatePolicy
operator|!=
literal|null
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|m_releasesUpdatePolicy
argument_list|,
name|m_snapshotsUpdatePolicy
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_UPDATE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|m_releasesUpdatePolicy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m_releasesChecksumPolicy
operator|!=
literal|null
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|m_releasesChecksumPolicy
argument_list|,
name|m_snapshotsChecksumPolicy
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|SEPARATOR_OPTIONS
argument_list|)
operator|.
name|append
argument_list|(
name|ServiceConstants
operator|.
name|OPTION_CHECKSUM
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|m_releasesChecksumPolicy
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
enum|enum
name|FROM
block|{
name|PID
argument_list|(
literal|"PID configuration"
argument_list|)
block|,
name|SETTINGS
argument_list|(
literal|"Maven XML settings"
argument_list|)
block|,
name|FALLBACK
argument_list|(
literal|"Fallback repository"
argument_list|)
block|;
specifier|private
name|String
name|source
decl_stmt|;
name|FROM
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
specifier|public
name|String
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
block|}
block|}
end_class

end_unit

