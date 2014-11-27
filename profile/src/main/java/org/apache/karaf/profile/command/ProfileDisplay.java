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
name|command
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|ProfileService
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
name|command
operator|.
name|completers
operator|.
name|ProfileCompleter
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
name|Action
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
name|Completion
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
name|Reference
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

begin_class
annotation|@
name|Command
argument_list|(
name|name
operator|=
literal|"display"
argument_list|,
name|scope
operator|=
literal|"profile"
argument_list|,
name|description
operator|=
literal|"Displays information about the specified profile"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|ProfileDisplay
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--overlay"
argument_list|,
name|aliases
operator|=
literal|"-o"
argument_list|,
name|description
operator|=
literal|"Shows the overlay profile settings, taking into account the settings inherited from parent profiles."
argument_list|)
specifier|private
name|Boolean
name|overlay
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--effective"
argument_list|,
name|aliases
operator|=
literal|"-e"
argument_list|,
name|description
operator|=
literal|"Shows the effective profile settings, taking into account properties substitution."
argument_list|)
specifier|private
name|Boolean
name|effective
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--display-resources"
argument_list|,
name|aliases
operator|=
literal|"-r"
argument_list|,
name|description
operator|=
literal|"Displays the content of additional profile resources."
argument_list|)
specifier|private
name|Boolean
name|displayResources
init|=
literal|false
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|name
operator|=
literal|"profile"
argument_list|,
name|description
operator|=
literal|"The name of the profile."
argument_list|)
annotation|@
name|Completion
argument_list|(
name|ProfileCompleter
operator|.
name|class
argument_list|)
specifier|private
name|String
name|profileId
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|ProfileService
name|profileService
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|displayProfile
argument_list|(
name|profileService
operator|.
name|getRequiredProfile
argument_list|(
name|profileId
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|void
name|printConfigList
parameter_list|(
name|String
name|header
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
name|out
operator|.
name|println
argument_list|(
name|header
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|str
range|:
name|list
control|)
block|{
name|out
operator|.
name|printf
argument_list|(
literal|"\t%s\n"
argument_list|,
name|str
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|displayProfile
parameter_list|(
name|Profile
name|profile
parameter_list|)
block|{
name|PrintStream
name|output
init|=
name|System
operator|.
name|out
decl_stmt|;
name|output
operator|.
name|println
argument_list|(
literal|"Profile id: "
operator|+
name|profile
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|.
name|println
argument_list|(
literal|"Attributes: "
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
name|profile
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|props
operator|.
name|keySet
argument_list|()
control|)
block|{
name|output
operator|.
name|println
argument_list|(
literal|"\t"
operator|+
name|key
operator|+
literal|": "
operator|+
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|overlay
condition|)
block|{
name|profile
operator|=
name|profileService
operator|.
name|getOverlayProfile
argument_list|(
name|profile
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|effective
condition|)
block|{
name|profile
operator|=
name|profileService
operator|.
name|getEffectiveProfile
argument_list|(
name|profile
argument_list|)
expr_stmt|;
block|}
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
name|configuration
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|profile
operator|.
name|getConfigurations
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|resources
init|=
name|profile
operator|.
name|getFileConfigurations
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|agentConfiguration
init|=
name|profile
operator|.
name|getConfiguration
argument_list|(
name|Profile
operator|.
name|INTERNAL_PID
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|agentProperties
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|systemProperties
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|configProperties
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|otherResources
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
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
name|String
argument_list|>
name|entry
range|:
name|agentConfiguration
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
condition|)
block|{
name|value
operator|=
literal|"\t"
operator|+
name|value
operator|.
name|replace
argument_list|(
literal|","
argument_list|,
literal|",\n\t\t"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"system."
argument_list|)
condition|)
block|{
name|systemProperties
operator|.
name|add
argument_list|(
literal|"  "
operator|+
name|key
operator|.
name|substring
argument_list|(
literal|"system."
operator|.
name|length
argument_list|()
argument_list|)
operator|+
literal|" = "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"config."
argument_list|)
condition|)
block|{
name|configProperties
operator|.
name|add
argument_list|(
literal|"  "
operator|+
name|key
operator|.
name|substring
argument_list|(
literal|"config."
operator|.
name|length
argument_list|()
argument_list|)
operator|+
literal|" = "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|key
operator|.
name|startsWith
argument_list|(
literal|"feature."
argument_list|)
operator|&&
operator|!
name|key
operator|.
name|startsWith
argument_list|(
literal|"repository"
argument_list|)
operator|&&
operator|!
name|key
operator|.
name|startsWith
argument_list|(
literal|"bundle."
argument_list|)
operator|&&
operator|!
name|key
operator|.
name|startsWith
argument_list|(
literal|"fab."
argument_list|)
operator|&&
operator|!
name|key
operator|.
name|startsWith
argument_list|(
literal|"override."
argument_list|)
operator|&&
operator|!
name|key
operator|.
name|startsWith
argument_list|(
literal|"attribute."
argument_list|)
condition|)
block|{
name|agentProperties
operator|.
name|add
argument_list|(
literal|"  "
operator|+
name|key
operator|+
literal|" = "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|configuration
operator|.
name|containsKey
argument_list|(
name|Profile
operator|.
name|INTERNAL_PID
argument_list|)
condition|)
block|{
name|output
operator|.
name|println
argument_list|(
literal|"\nContainer settings"
argument_list|)
expr_stmt|;
name|output
operator|.
name|println
argument_list|(
literal|"----------------------------"
argument_list|)
expr_stmt|;
if|if
condition|(
name|profile
operator|.
name|getLibraries
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|printConfigList
argument_list|(
literal|"Libraries : "
argument_list|,
name|output
argument_list|,
name|profile
operator|.
name|getLibraries
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|profile
operator|.
name|getRepositories
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|printConfigList
argument_list|(
literal|"Repositories : "
argument_list|,
name|output
argument_list|,
name|profile
operator|.
name|getRepositories
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|profile
operator|.
name|getFeatures
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|printConfigList
argument_list|(
literal|"Features : "
argument_list|,
name|output
argument_list|,
name|profile
operator|.
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|profile
operator|.
name|getBundles
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|printConfigList
argument_list|(
literal|"Bundles : "
argument_list|,
name|output
argument_list|,
name|profile
operator|.
name|getBundles
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|profile
operator|.
name|getOverrides
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|printConfigList
argument_list|(
literal|"Overrides : "
argument_list|,
name|output
argument_list|,
name|profile
operator|.
name|getOverrides
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|agentProperties
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|printConfigList
argument_list|(
literal|"Agent Properties : "
argument_list|,
name|output
argument_list|,
name|agentProperties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|systemProperties
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|printConfigList
argument_list|(
literal|"System Properties : "
argument_list|,
name|output
argument_list|,
name|systemProperties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configProperties
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|printConfigList
argument_list|(
literal|"Config Properties : "
argument_list|,
name|output
argument_list|,
name|configProperties
argument_list|)
expr_stmt|;
block|}
name|configuration
operator|.
name|remove
argument_list|(
name|Profile
operator|.
name|INTERNAL_PID
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|println
argument_list|(
literal|"\nConfiguration details"
argument_list|)
expr_stmt|;
name|output
operator|.
name|println
argument_list|(
literal|"----------------------------"
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|cfg
range|:
name|configuration
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|output
operator|.
name|println
argument_list|(
literal|"PID: "
operator|+
name|cfg
operator|.
name|getKey
argument_list|()
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
name|String
argument_list|>
name|values
range|:
name|cfg
operator|.
name|getValue
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|output
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|values
operator|.
name|getKey
argument_list|()
operator|+
literal|" "
operator|+
name|values
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|println
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|println
argument_list|(
literal|"\nOther resources"
argument_list|)
expr_stmt|;
name|output
operator|.
name|println
argument_list|(
literal|"----------------------------"
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
name|resource
range|:
name|resources
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|resource
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|endsWith
argument_list|(
literal|".properties"
argument_list|)
condition|)
block|{
name|output
operator|.
name|println
argument_list|(
literal|"Resource: "
operator|+
name|resource
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|displayResources
condition|)
block|{
name|output
operator|.
name|println
argument_list|(
operator|new
name|String
argument_list|(
name|resource
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|output
operator|.
name|println
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit
