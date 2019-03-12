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
name|maven
operator|.
name|command
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

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
name|Optional
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
name|maven
operator|.
name|core
operator|.
name|MavenRepositoryURL
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
name|Option
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
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"maven"
argument_list|,
name|name
operator|=
literal|"repository-add"
argument_list|,
name|description
operator|=
literal|"Adds Maven repository"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|RepositoryAddCommand
extends|extends
name|RepositoryEditCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-idx"
argument_list|,
name|description
operator|=
literal|"Index at which new repository is to be inserted (0-based) (defaults to last - repository will be appended)"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|int
name|idx
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-s"
argument_list|,
name|aliases
operator|=
block|{
literal|"--snapshots"
block|}
argument_list|,
name|description
operator|=
literal|"Enable SNAPSHOT handling in the repository"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|snapshots
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-nr"
argument_list|,
name|aliases
operator|=
block|{
literal|"--no-releases"
block|}
argument_list|,
name|description
operator|=
literal|"Disable release handling in this repository"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|noReleases
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-up"
argument_list|,
name|aliases
operator|=
block|{
literal|"--update-policy"
block|}
argument_list|,
name|description
operator|=
literal|"Update policy for repository (never, daily (default), interval:N, always)"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|updatePolicy
init|=
literal|"daily"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-cp"
argument_list|,
name|aliases
operator|=
block|{
literal|"--checksum-policy"
block|}
argument_list|,
name|description
operator|=
literal|"Checksum policy for repository (ignore, warn (default), fail)"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|checksumPolicy
init|=
literal|"warn"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|aliases
operator|=
block|{
literal|"--username"
block|}
argument_list|,
name|description
operator|=
literal|"Username for remote repository"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|username
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
block|{
literal|"--password"
block|}
argument_list|,
name|description
operator|=
literal|"Password for remote repository (may be encrypted, see \"maven:password -ep\")"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|password
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"Repository URI. It may be file:// based, http(s):// based, may use other known protocol or even property placeholders (like ${karaf.base})"
argument_list|)
name|String
name|uri
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|edit
parameter_list|(
name|String
name|prefix
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
parameter_list|,
name|MavenRepositoryURL
index|[]
name|allRepos
parameter_list|,
name|MavenRepositoryURL
index|[]
name|pidRepos
parameter_list|,
name|MavenRepositoryURL
index|[]
name|settingsRepos
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|idx
operator|>
name|pidRepos
operator|.
name|length
condition|)
block|{
comment|// TOCONSIDER: should we allow to add repository to settings.xml too?
name|System
operator|.
name|err
operator|.
name|printf
argument_list|(
literal|"List of %s repositories has %d elements. Can't insert at position %s.\n"
argument_list|,
operator|(
name|defaultRepository
condition|?
literal|"default"
else|:
literal|"remote"
operator|)
argument_list|,
name|pidRepos
operator|.
name|length
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return;
block|}
name|Optional
argument_list|<
name|MavenRepositoryURL
argument_list|>
name|first
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|allRepos
argument_list|)
operator|.
name|filter
argument_list|(
parameter_list|(
name|repo
parameter_list|)
lambda|->
name|id
operator|.
name|equals
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
decl_stmt|;
if|if
condition|(
name|first
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|printf
argument_list|(
literal|"Repository with ID \"%s\" is already configured for URL %s\n"
argument_list|,
name|id
argument_list|,
name|first
operator|.
name|get
argument_list|()
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|SourceAnd
argument_list|<
name|String
argument_list|>
name|up
init|=
name|updatePolicy
argument_list|(
name|updatePolicy
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|up
operator|.
name|valid
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unknown value of update policy: \""
operator|+
name|updatePolicy
operator|+
literal|"\""
argument_list|)
expr_stmt|;
return|return;
block|}
name|SourceAnd
argument_list|<
name|String
argument_list|>
name|cp
init|=
name|checksumPolicy
argument_list|(
name|checksumPolicy
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cp
operator|.
name|valid
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unknown value of checksum policy: \""
operator|+
name|checksumPolicy
operator|+
literal|"\""
argument_list|)
expr_stmt|;
return|return;
block|}
name|SourceAnd
argument_list|<
name|String
argument_list|>
name|urlResolved
init|=
name|validateRepositoryURL
argument_list|(
name|uri
argument_list|,
name|defaultRepository
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|urlResolved
operator|.
name|valid
condition|)
block|{
return|return;
block|}
name|boolean
name|hasUsername
init|=
name|username
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|username
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|hasPassword
init|=
name|password
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|password
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|hasCredentials
init|=
name|hasUsername
operator|&&
name|hasPassword
decl_stmt|;
if|if
condition|(
operator|(
name|hasUsername
operator|&&
operator|!
name|hasPassword
operator|)
operator|||
operator|(
operator|!
name|hasUsername
operator|&&
name|hasPassword
operator|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Please specify both username and password"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|defaultRepository
operator|&&
name|hasCredentials
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"User credentials won't be used for default repository"
argument_list|)
expr_stmt|;
comment|// no return
block|}
comment|// credentials for remote repository can be stored only in settings.xml
if|if
condition|(
operator|!
name|defaultRepository
operator|&&
name|hasCredentials
condition|)
block|{
if|if
condition|(
operator|!
name|updateCredentials
argument_list|(
name|force
argument_list|,
name|id
argument_list|,
name|username
argument_list|,
name|password
argument_list|,
name|prefix
argument_list|,
name|config
argument_list|)
condition|)
block|{
return|return;
block|}
name|updateSettings
argument_list|(
name|prefix
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
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
name|urlResolved
operator|.
name|val
argument_list|()
argument_list|)
expr_stmt|;
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
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|snapshots
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
name|noReleases
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
name|updatePolicy
argument_list|)
expr_stmt|;
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
name|checksumPolicy
argument_list|)
expr_stmt|;
name|MavenRepositoryURL
name|newRepository
init|=
operator|new
name|MavenRepositoryURL
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|MavenRepositoryURL
argument_list|>
name|newRepos
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|pidRepos
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
condition|)
block|{
name|newRepos
operator|.
name|add
argument_list|(
name|idx
argument_list|,
name|newRepository
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newRepos
operator|.
name|add
argument_list|(
name|newRepository
argument_list|)
expr_stmt|;
block|}
name|updatePidRepositories
argument_list|(
name|prefix
argument_list|,
name|config
argument_list|,
name|defaultRepository
argument_list|,
name|newRepos
argument_list|,
name|settingsRepos
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|Configuration
name|cmConfig
init|=
name|cm
operator|.
name|getConfiguration
argument_list|(
name|PID
argument_list|)
decl_stmt|;
name|cmConfig
operator|.
name|update
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

end_unit

