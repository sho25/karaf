begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|command
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessControlContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|table
operator|.
name|Row
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
name|support
operator|.
name|table
operator|.
name|ShellTable
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"jaas"
argument_list|,
name|name
operator|=
literal|"whoami"
argument_list|,
name|description
operator|=
literal|"List currently active principals according to JAAS."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|WhoamiCommand
implements|implements
name|Action
block|{
specifier|private
specifier|static
specifier|final
name|String
name|USER_CLASS
init|=
literal|"org.apache.karaf.jaas.boot.principal.UserPrincipal"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GROUP_CLASS
init|=
literal|"org.apache.karaf.jaas.boot.principal.GroupPrincipal"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ROLE_CLASS
init|=
literal|"org.apache.karaf.jaas.boot.principal.RolePrincipal"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ALL_CLASS
init|=
literal|"java.security.Principal"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-g"
argument_list|,
name|aliases
operator|=
block|{
literal|"--groups"
block|}
argument_list|,
name|description
operator|=
literal|"Show groups instead of user."
argument_list|)
name|boolean
name|groups
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-r"
argument_list|,
name|aliases
operator|=
block|{
literal|"--roles"
block|}
argument_list|,
name|description
operator|=
literal|"Show roles instead of user."
argument_list|)
name|boolean
name|roles
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-a"
argument_list|,
name|aliases
operator|=
block|{
literal|"--all"
block|}
argument_list|,
name|description
operator|=
literal|"Show all JAAS principals regardless of type."
argument_list|)
name|boolean
name|all
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--no-format"
argument_list|,
name|description
operator|=
literal|"Disable table rendered output."
argument_list|)
name|boolean
name|noFormat
init|=
literal|false
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
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
comment|// Get the currently-active JAAS Subject.
name|AccessControlContext
name|acc
init|=
name|AccessController
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|Subject
name|subj
init|=
name|Subject
operator|.
name|getSubject
argument_list|(
name|acc
argument_list|)
decl_stmt|;
name|String
name|classString
init|=
name|USER_CLASS
decl_stmt|;
if|if
condition|(
name|groups
condition|)
block|{
name|classString
operator|=
name|GROUP_CLASS
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|roles
condition|)
block|{
name|classString
operator|=
name|ROLE_CLASS
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|all
condition|)
block|{
name|classString
operator|=
name|ALL_CLASS
expr_stmt|;
block|}
name|Class
name|c
init|=
name|Class
operator|.
name|forName
argument_list|(
name|classString
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Principal
argument_list|>
name|principals
init|=
name|subj
operator|.
name|getPrincipals
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Name"
argument_list|)
expr_stmt|;
if|if
condition|(
name|all
condition|)
block|{
name|table
operator|.
name|column
argument_list|(
literal|"Class"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Principal
name|p
range|:
name|principals
control|)
block|{
name|Row
name|row
init|=
name|table
operator|.
name|addRow
argument_list|()
decl_stmt|;
name|row
operator|.
name|addContent
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|all
condition|)
block|{
name|row
operator|.
name|addContent
argument_list|(
name|p
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|,
operator|!
name|noFormat
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit
