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
name|config
operator|.
name|command
operator|.
name|completers
package|;
end_package

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
name|Enumeration
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
name|List
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
name|service
operator|.
name|command
operator|.
name|CommandSession
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
name|config
operator|.
name|command
operator|.
name|ConfigCommandSupport
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
name|console
operator|.
name|CommandSessionHolder
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
name|console
operator|.
name|Completer
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
name|console
operator|.
name|completer
operator|.
name|ArgumentCompleter
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
name|console
operator|.
name|completer
operator|.
name|StringsCompleter
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
name|InvalidSyntaxException
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
name|ConfigurationAdmin
import|;
end_import

begin_comment
comment|/**  * {@link jline.Completor} for Configuration Admin properties.  *  * Displays a list of existing properties based on the current configuration being edited.  *  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationPropertyCompleter
implements|implements
name|Completer
block|{
specifier|private
specifier|final
name|StringsCompleter
name|delegate
init|=
operator|new
name|StringsCompleter
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OPTION
init|=
literal|"-p"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ALIAS
init|=
literal|"--pid"
decl_stmt|;
specifier|private
name|ConfigurationAdmin
name|configAdmin
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|int
name|complete
parameter_list|(
specifier|final
name|String
name|buffer
parameter_list|,
specifier|final
name|int
name|cursor
parameter_list|,
specifier|final
name|List
name|candidates
parameter_list|)
block|{
name|CommandSession
name|session
init|=
name|CommandSessionHolder
operator|.
name|getSession
argument_list|()
decl_stmt|;
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|String
name|pid
init|=
name|getPid
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|propertyNames
init|=
name|getPropertyNames
argument_list|(
name|pid
argument_list|)
decl_stmt|;
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|propertyNames
operator|!=
literal|null
operator|&&
operator|!
name|propertyNames
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|addAll
argument_list|(
name|propertyNames
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|delegate
operator|.
name|complete
argument_list|(
name|buffer
argument_list|,
name|cursor
argument_list|,
name|candidates
argument_list|)
return|;
block|}
comment|/**      * Retrieves the pid stored in the {@link CommandSession} or passed as an argument.      * Argument takes precedence from pid stored in the {@link CommandSession}.      * @param commandSession      * @return      */
specifier|private
name|String
name|getPid
parameter_list|(
name|CommandSession
name|commandSession
parameter_list|)
block|{
name|String
name|pid
init|=
operator|(
name|String
operator|)
name|commandSession
operator|.
name|get
argument_list|(
name|ConfigCommandSupport
operator|.
name|PROPERTY_CONFIG_PID
argument_list|)
decl_stmt|;
name|ArgumentCompleter
operator|.
name|ArgumentList
name|list
init|=
operator|(
name|ArgumentCompleter
operator|.
name|ArgumentList
operator|)
name|commandSession
operator|.
name|get
argument_list|(
name|ArgumentCompleter
operator|.
name|ARGUMENTS_LIST
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
operator|&&
name|list
operator|.
name|getArguments
argument_list|()
operator|!=
literal|null
operator|&&
name|list
operator|.
name|getArguments
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|arguments
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|list
operator|.
name|getArguments
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|arguments
operator|.
name|contains
argument_list|(
name|OPTION
argument_list|)
condition|)
block|{
name|int
name|index
init|=
name|arguments
operator|.
name|indexOf
argument_list|(
name|OPTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|>
name|index
condition|)
block|{
return|return
name|arguments
operator|.
name|get
argument_list|(
name|index
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|arguments
operator|.
name|contains
argument_list|(
name|ALIAS
argument_list|)
condition|)
block|{
name|int
name|index
init|=
name|arguments
operator|.
name|indexOf
argument_list|(
name|ALIAS
argument_list|)
decl_stmt|;
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|>
name|index
condition|)
block|{
return|return
name|arguments
operator|.
name|get
argument_list|(
name|index
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
block|}
return|return
name|pid
return|;
block|}
comment|/**      * Returns the property names for the given pid.      * @param pid      * @return      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getPropertyNames
parameter_list|(
name|String
name|pid
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|propertyNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|pid
operator|!=
literal|null
condition|)
block|{
name|Configuration
name|configuration
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Configuration
index|[]
name|configs
init|=
name|configAdmin
operator|.
name|listConfigurations
argument_list|(
literal|"(service.pid="
operator|+
name|pid
operator|+
literal|")"
argument_list|)
decl_stmt|;
if|if
condition|(
name|configs
operator|!=
literal|null
operator|&&
name|configs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|configuration
operator|=
name|configs
index|[
literal|0
index|]
expr_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|Dictionary
name|properties
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|Enumeration
name|keys
init|=
name|properties
operator|.
name|keys
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|propertyNames
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|keys
operator|.
name|nextElement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//Ignore
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
comment|//Ignore
block|}
block|}
return|return
name|propertyNames
return|;
block|}
specifier|public
name|ConfigurationAdmin
name|getConfigAdmin
parameter_list|()
block|{
return|return
name|configAdmin
return|;
block|}
specifier|public
name|void
name|setConfigAdmin
parameter_list|(
name|ConfigurationAdmin
name|configAdmin
parameter_list|)
block|{
name|this
operator|.
name|configAdmin
operator|=
name|configAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

