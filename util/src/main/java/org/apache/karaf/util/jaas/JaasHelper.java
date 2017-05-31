begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|jaas
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
name|Permission
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
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedActionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|ProtectionDomain
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|SubjectDomainCombiner
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
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|RolePrincipal
import|;
end_import

begin_class
specifier|public
class|class
name|JaasHelper
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ROLE_WILDCARD
init|=
literal|"*"
decl_stmt|;
specifier|public
specifier|static
name|boolean
name|currentUserHasRole
parameter_list|(
name|String
name|requestedRole
parameter_list|)
block|{
if|if
condition|(
name|ROLE_WILDCARD
operator|.
name|equals
argument_list|(
name|requestedRole
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|AccessControlContext
name|acc
init|=
name|AccessController
operator|.
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|acc
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Subject
name|subject
init|=
name|Subject
operator|.
name|getSubject
argument_list|(
name|acc
argument_list|)
decl_stmt|;
if|if
condition|(
name|subject
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|currentUserHasRole
argument_list|(
name|subject
operator|.
name|getPrincipals
argument_list|()
argument_list|,
name|requestedRole
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|currentUserHasRole
parameter_list|(
name|Set
argument_list|<
name|Principal
argument_list|>
name|principals
parameter_list|,
name|String
name|requestedRole
parameter_list|)
block|{
if|if
condition|(
name|ROLE_WILDCARD
operator|.
name|equals
argument_list|(
name|requestedRole
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|clazz
decl_stmt|;
name|String
name|role
decl_stmt|;
name|int
name|index
init|=
name|requestedRole
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|clazz
operator|=
name|requestedRole
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|role
operator|=
name|requestedRole
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|clazz
operator|=
name|RolePrincipal
operator|.
name|class
operator|.
name|getName
argument_list|()
expr_stmt|;
name|role
operator|=
name|requestedRole
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
if|if
condition|(
name|clazz
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|role
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|void
name|runAs
parameter_list|(
specifier|final
name|Subject
name|subject
parameter_list|,
specifier|final
name|Runnable
name|action
parameter_list|)
block|{
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
name|doAs
argument_list|(
name|subject
argument_list|,
call|(
name|PrivilegedAction
argument_list|<
name|Object
argument_list|>
call|)
argument_list|(
parameter_list|()
lambda|->
block|{
name|action
operator|.
name|run
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|doAs
parameter_list|(
specifier|final
name|Subject
name|subject
parameter_list|,
specifier|final
name|PrivilegedAction
argument_list|<
name|T
argument_list|>
name|action
parameter_list|)
block|{
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
comment|// set up the new Subject-based AccessControlContext for doPrivileged
specifier|final
name|AccessControlContext
name|currentAcc
init|=
name|AccessController
operator|.
name|getContext
argument_list|()
decl_stmt|;
specifier|final
name|AccessControlContext
name|newAcc
init|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|AccessControlContext
argument_list|>
argument_list|()
block|{
specifier|public
name|AccessControlContext
name|run
parameter_list|()
block|{
if|if
condition|(
name|subject
operator|==
literal|null
condition|)
return|return
operator|new
name|AccessControlContext
argument_list|(
name|currentAcc
argument_list|,
literal|null
argument_list|)
return|;
else|else
return|return
operator|new
name|AccessControlContext
argument_list|(
name|currentAcc
argument_list|,
operator|new
name|OsgiSubjectDomainCombiner
argument_list|(
name|subject
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// call doPrivileged and push this new context on the stack
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
name|action
argument_list|,
name|newAcc
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|doAs
parameter_list|(
specifier|final
name|Subject
name|subject
parameter_list|,
specifier|final
name|PrivilegedExceptionAction
argument_list|<
name|T
argument_list|>
name|action
parameter_list|)
throws|throws
name|PrivilegedActionException
block|{
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
comment|// set up the new Subject-based AccessControlContext for doPrivileged
specifier|final
name|AccessControlContext
name|currentAcc
init|=
name|AccessController
operator|.
name|getContext
argument_list|()
decl_stmt|;
specifier|final
name|AccessControlContext
name|newAcc
init|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|AccessControlContext
argument_list|>
argument_list|()
block|{
specifier|public
name|AccessControlContext
name|run
parameter_list|()
block|{
if|if
condition|(
name|subject
operator|==
literal|null
condition|)
return|return
operator|new
name|AccessControlContext
argument_list|(
name|currentAcc
argument_list|,
literal|null
argument_list|)
return|;
else|else
return|return
operator|new
name|AccessControlContext
argument_list|(
name|currentAcc
argument_list|,
operator|new
name|OsgiSubjectDomainCombiner
argument_list|(
name|subject
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// call doPrivileged and push this new context on the stack
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
name|action
argument_list|,
name|newAcc
argument_list|)
return|;
block|}
specifier|public
specifier|static
class|class
name|OsgiSubjectDomainCombiner
extends|extends
name|SubjectDomainCombiner
block|{
specifier|private
specifier|final
name|Subject
name|subject
decl_stmt|;
specifier|public
name|OsgiSubjectDomainCombiner
parameter_list|(
name|Subject
name|subject
parameter_list|)
block|{
name|super
argument_list|(
name|subject
argument_list|)
expr_stmt|;
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
specifier|public
name|ProtectionDomain
index|[]
name|combine
parameter_list|(
name|ProtectionDomain
index|[]
name|currentDomains
parameter_list|,
name|ProtectionDomain
index|[]
name|assignedDomains
parameter_list|)
block|{
name|int
name|cLen
init|=
operator|(
name|currentDomains
operator|==
literal|null
condition|?
literal|0
else|:
name|currentDomains
operator|.
name|length
operator|)
decl_stmt|;
name|int
name|aLen
init|=
operator|(
name|assignedDomains
operator|==
literal|null
condition|?
literal|0
else|:
name|assignedDomains
operator|.
name|length
operator|)
decl_stmt|;
name|ProtectionDomain
index|[]
name|newDomains
init|=
operator|new
name|ProtectionDomain
index|[
name|cLen
operator|+
name|aLen
index|]
decl_stmt|;
name|Principal
index|[]
name|principals
init|=
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Principal
index|[
literal|0
index|]
argument_list|)
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
name|cLen
condition|;
name|i
operator|++
control|)
block|{
name|newDomains
index|[
name|i
index|]
operator|=
operator|new
name|DelegatingProtectionDomain
argument_list|(
name|currentDomains
index|[
name|i
index|]
argument_list|,
name|principals
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|aLen
condition|;
name|i
operator|++
control|)
block|{
name|newDomains
index|[
name|cLen
operator|+
name|i
index|]
operator|=
name|assignedDomains
index|[
name|i
index|]
expr_stmt|;
block|}
name|newDomains
operator|=
name|optimize
argument_list|(
name|newDomains
argument_list|)
expr_stmt|;
return|return
name|newDomains
return|;
block|}
specifier|private
name|ProtectionDomain
index|[]
name|optimize
parameter_list|(
name|ProtectionDomain
index|[]
name|domains
parameter_list|)
block|{
if|if
condition|(
name|domains
operator|==
literal|null
operator|||
name|domains
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ProtectionDomain
index|[]
name|optimized
init|=
operator|new
name|ProtectionDomain
index|[
name|domains
operator|.
name|length
index|]
decl_stmt|;
name|ProtectionDomain
name|pd
decl_stmt|;
name|int
name|num
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ProtectionDomain
name|domain
range|:
name|domains
control|)
block|{
if|if
condition|(
operator|(
name|pd
operator|=
name|domain
operator|)
operator|!=
literal|null
condition|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|num
operator|&&
operator|!
name|found
condition|;
name|j
operator|++
control|)
block|{
name|found
operator|=
operator|(
name|optimized
index|[
name|j
index|]
operator|==
name|pd
operator|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|optimized
index|[
name|num
operator|++
index|]
operator|=
name|pd
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|num
operator|>
literal|0
operator|&&
name|num
operator|<
name|domains
operator|.
name|length
condition|)
block|{
name|ProtectionDomain
index|[]
name|downSize
init|=
operator|new
name|ProtectionDomain
index|[
name|num
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|optimized
argument_list|,
literal|0
argument_list|,
name|downSize
argument_list|,
literal|0
argument_list|,
name|downSize
operator|.
name|length
argument_list|)
expr_stmt|;
name|optimized
operator|=
name|downSize
expr_stmt|;
block|}
return|return
operator|(
operator|(
name|num
operator|==
literal|0
operator|||
name|optimized
operator|.
name|length
operator|==
literal|0
operator|)
condition|?
literal|null
else|:
name|optimized
operator|)
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|DelegatingProtectionDomain
extends|extends
name|ProtectionDomain
block|{
specifier|private
specifier|final
name|ProtectionDomain
name|delegate
decl_stmt|;
name|DelegatingProtectionDomain
parameter_list|(
name|ProtectionDomain
name|delegate
parameter_list|,
name|Principal
index|[]
name|principals
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
operator|.
name|getCodeSource
argument_list|()
argument_list|,
name|delegate
operator|.
name|getPermissions
argument_list|()
argument_list|,
name|delegate
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|principals
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|implies
parameter_list|(
name|Permission
name|permission
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|implies
argument_list|(
name|permission
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

