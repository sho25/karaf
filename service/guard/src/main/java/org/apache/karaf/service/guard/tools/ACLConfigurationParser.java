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
name|service
operator|.
name|guard
operator|.
name|tools
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
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
name|service
operator|.
name|guard
operator|.
name|impl
operator|.
name|GuardProxyCatalog
import|;
end_import

begin_class
specifier|public
class|class
name|ACLConfigurationParser
block|{
comment|// note that the order of the enums is important. Needs to be from most specific to least specific.
specifier|public
enum|enum
name|Specificity
block|{
name|ARGUMENT_MATCH
block|,
name|SIGNATURE_MATCH
block|,
name|NAME_MATCH
block|,
name|WILDCARD_MATCH
block|,
name|NO_MATCH
block|}
specifier|static
name|String
name|compulsoryRoles
decl_stmt|;
static|static
block|{
name|compulsoryRoles
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|GuardProxyCatalog
operator|.
name|KARAF_SECURED_COMMAND_COMPULSORY_ROLES_PROPERTY
argument_list|)
expr_stmt|;
block|}
comment|/**      *<p>Returns the roles that can invoke the given operation. This is determined by matching the      * operation details against configuration provided.</p>      *      *<p>The following configuration is supported. Keys are used to match an invocation against. The value can contain      * a comma-separated list of roles. Spaces are ignored for the role values. Note that comments are allowed in the      * value field after the hash {@code #} character:</p>      *      *<pre>      *     {@code      *     myMethod = role1, role2      *     methodName(int)[/17/] = role1                    # regex match, assume it's surrounded by ^ and $      *     methodName(int)[/[01]8/] = role2      *     methodName(int)["19"] = role3                    # exact value match      *     methodName(int) = role4                          # signature match      *     methodName(java.lang.String, int) = role5        # signature match      *     methodName =                                     # no roles can invoke this command      *     method* = role6                                  # name prefix/wildcard match. The asterisk must appear at the end.      *     }      *</pre>      *      *<p>The following algorithm is used to find matching roles:</p>      *<ol>      *<li>Find all regex and exact value matches. For all parameters these matches are found by calling {@code toString()}      *         on the parameters passed in. If there are multiple matches in this category all the matching roles are collected.      *         If any is found return these roles.      *</li>      *<li>Find a signature match. If found return the associated roles.</li>      *<li>Find a method name match. If found return the associated roles.</li>      *<li>Find a method name prefix/wildcard match. If more than one prefix match, the roles associated with the longest      *         prefix is used. So for example, if there are rules for {@code get*} and {@code *} only the roles associated with      *         {@code get*} are returned.      *</li>      *<li>If none of the above criteria match, this method returns {@code null}.</li>      *</ol>      *      * @param methodName the method name to be invoked.      * @param params the parameters provided for the invocation. May be {@code null} for cases there the parameters are not yet      *               known. In this case the roles that can<em>potentially</em> invoke the method are returned, although based on      *               parameter values the actual invocation may still be denied.      * @param signature the signature of the method specified as an array of class name. For simple types, the simple type name      *                  is used (e.g. "int").      * @param config the configuration to check against.      * @param addToRoles the list of roles (which may be empty) if a matching configuration iteam has been found.      * @return the specificity      */
specifier|public
specifier|static
name|Specificity
name|getRolesForInvocation
parameter_list|(
name|String
name|methodName
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|String
index|[]
name|signature
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|addToRoles
parameter_list|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|trimKeys
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|Specificity
name|s
init|=
name|getRolesBasedOnSignature
argument_list|(
name|methodName
argument_list|,
name|params
argument_list|,
name|signature
argument_list|,
name|properties
argument_list|,
name|addToRoles
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
name|Specificity
operator|.
name|NO_MATCH
condition|)
block|{
return|return
name|s
return|;
block|}
name|s
operator|=
name|getRolesBasedOnSignature
argument_list|(
name|methodName
argument_list|,
name|params
argument_list|,
literal|null
argument_list|,
name|properties
argument_list|,
name|addToRoles
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|!=
name|Specificity
operator|.
name|NO_MATCH
condition|)
block|{
return|return
name|s
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
name|getMethodNameWildcardRoles
argument_list|(
name|properties
argument_list|,
name|methodName
argument_list|)
decl_stmt|;
if|if
condition|(
name|roles
operator|!=
literal|null
condition|)
block|{
name|addToRoles
operator|.
name|addAll
argument_list|(
name|roles
argument_list|)
expr_stmt|;
return|return
name|Specificity
operator|.
name|WILDCARD_MATCH
return|;
block|}
elseif|else
if|if
condition|(
name|compulsoryRoles
operator|!=
literal|null
condition|)
block|{
name|addToRoles
operator|.
name|addAll
argument_list|(
name|ACLConfigurationParser
operator|.
name|parseRoles
argument_list|(
name|compulsoryRoles
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Specificity
operator|.
name|NAME_MATCH
return|;
block|}
else|else
block|{
return|return
name|Specificity
operator|.
name|NO_MATCH
return|;
block|}
block|}
specifier|private
specifier|static
name|Specificity
name|getRolesBasedOnSignature
parameter_list|(
name|String
name|methodName
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|String
index|[]
name|signature
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|)
block|{
if|if
condition|(
name|params
operator|!=
literal|null
condition|)
block|{
name|boolean
name|foundExactOrRegex
init|=
literal|false
decl_stmt|;
name|Object
name|exactArgMatchRoles
init|=
name|properties
operator|.
name|get
argument_list|(
name|getExactArgSignature
argument_list|(
name|methodName
argument_list|,
name|signature
argument_list|,
name|params
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|exactArgMatchRoles
operator|instanceof
name|String
condition|)
block|{
name|roles
operator|.
name|addAll
argument_list|(
name|parseRoles
argument_list|(
operator|(
name|String
operator|)
name|exactArgMatchRoles
argument_list|)
argument_list|)
expr_stmt|;
name|foundExactOrRegex
operator|=
literal|true
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|r
init|=
name|getRegexRoles
argument_list|(
name|properties
argument_list|,
name|methodName
argument_list|,
name|signature
argument_list|,
name|params
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|foundExactOrRegex
operator|=
literal|true
expr_stmt|;
name|roles
operator|.
name|addAll
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|foundExactOrRegex
condition|)
block|{
comment|// since we have the actual parameters we can match them and if they do we won't look for any
comment|// more generic rules...
return|return
name|Specificity
operator|.
name|ARGUMENT_MATCH
return|;
block|}
block|}
else|else
block|{
comment|// this is used in the case where parameters aren't known yet and the system wants to find out
comment|// what roles in principle can invoke this method
name|List
argument_list|<
name|String
argument_list|>
name|r
init|=
name|getExactArgOrRegexRoles
argument_list|(
name|properties
argument_list|,
name|methodName
argument_list|,
name|signature
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|roles
operator|.
name|addAll
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
name|Object
name|signatureRoles
init|=
name|properties
operator|.
name|get
argument_list|(
name|getSignature
argument_list|(
name|methodName
argument_list|,
name|signature
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|signatureRoles
operator|instanceof
name|String
condition|)
block|{
name|roles
operator|.
name|addAll
argument_list|(
name|parseRoles
argument_list|(
operator|(
name|String
operator|)
name|signatureRoles
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|signature
operator|==
literal|null
condition|?
name|Specificity
operator|.
name|NAME_MATCH
else|:
name|Specificity
operator|.
name|SIGNATURE_MATCH
return|;
block|}
return|return
name|Specificity
operator|.
name|NO_MATCH
return|;
block|}
specifier|private
specifier|static
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|trimKeys
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|d
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|e
init|=
name|properties
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|d
operator|.
name|put
argument_list|(
name|removeSpaces
argument_list|(
name|key
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|d
return|;
block|}
specifier|private
specifier|static
name|String
name|removeSpaces
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|char
name|quoteChar
init|=
literal|0
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
name|key
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|key
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|quoteChar
operator|==
literal|0
operator|&&
name|c
operator|==
literal|' '
condition|)
continue|continue;
if|if
condition|(
name|quoteChar
operator|==
literal|0
operator|&&
operator|(
name|c
operator|==
literal|'\"'
operator|||
name|c
operator|==
literal|'/'
operator|)
operator|&&
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
operator|(
name|sb
operator|.
name|charAt
argument_list|(
name|sb
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|'['
operator|||
name|sb
operator|.
name|charAt
argument_list|(
name|sb
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|','
operator|)
condition|)
block|{
comment|// we're in a quoted string
name|quoteChar
operator|=
name|c
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|quoteChar
operator|!=
literal|0
operator|&&
name|c
operator|==
name|quoteChar
condition|)
block|{
comment|// look ahead to see if the next non-space is the closing bracket or a comma, which ends the quoted string
for|for
control|(
name|int
name|j
init|=
name|i
operator|+
literal|1
init|;
name|j
operator|<
name|key
operator|.
name|length
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|key
operator|.
name|charAt
argument_list|(
name|j
argument_list|)
operator|==
literal|' '
condition|)
continue|continue;
if|if
condition|(
name|key
operator|.
name|charAt
argument_list|(
name|j
argument_list|)
operator|==
literal|']'
operator|||
name|key
operator|.
name|charAt
argument_list|(
name|j
argument_list|)
operator|==
literal|','
condition|)
name|quoteChar
operator|=
literal|0
expr_stmt|;
break|break;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
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
name|List
argument_list|<
name|String
argument_list|>
name|parseRoles
parameter_list|(
name|String
name|roleStr
parameter_list|)
block|{
name|int
name|hashIdx
init|=
name|roleStr
operator|.
name|indexOf
argument_list|(
literal|'#'
argument_list|)
decl_stmt|;
if|if
condition|(
name|hashIdx
operator|>=
literal|0
condition|)
block|{
comment|// you can put a comment at the end
name|roleStr
operator|=
name|roleStr
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|hashIdx
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|role
range|:
name|roleStr
operator|.
name|split
argument_list|(
literal|"[,]"
argument_list|)
control|)
block|{
name|String
name|trimmed
init|=
name|role
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|trimmed
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|roles
operator|.
name|add
argument_list|(
name|trimmed
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|roles
return|;
block|}
specifier|private
specifier|static
name|Object
name|getExactArgSignature
parameter_list|(
name|String
name|methodName
parameter_list|,
name|String
index|[]
name|signature
parameter_list|,
name|Object
index|[]
name|params
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|getSignature
argument_list|(
name|methodName
argument_list|,
name|signature
argument_list|)
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Object
name|param
range|:
name|params
control|)
block|{
if|if
condition|(
name|first
condition|)
name|first
operator|=
literal|false
expr_stmt|;
else|else
name|sb
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
if|if
condition|(
name|param
operator|!=
literal|null
condition|)
name|sb
operator|.
name|append
argument_list|(
name|param
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|String
name|getSignature
parameter_list|(
name|String
name|methodName
parameter_list|,
name|String
index|[]
name|signature
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|methodName
argument_list|)
decl_stmt|;
if|if
condition|(
name|signature
operator|==
literal|null
condition|)
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
name|sb
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|signature
control|)
block|{
if|if
condition|(
name|first
condition|)
name|first
operator|=
literal|false
expr_stmt|;
else|else
name|sb
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getRegexRoles
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|methodName
parameter_list|,
name|String
index|[]
name|signature
parameter_list|,
name|Object
index|[]
name|params
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|matchFound
init|=
literal|false
decl_stmt|;
name|String
name|methodSig
init|=
name|getSignature
argument_list|(
name|methodName
argument_list|,
name|signature
argument_list|)
decl_stmt|;
name|String
name|prefix
init|=
name|methodSig
operator|+
literal|"[/"
decl_stmt|;
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|e
init|=
name|properties
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
name|e
operator|.
name|nextElement
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
operator|&&
name|key
operator|.
name|endsWith
argument_list|(
literal|"/]"
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|regexArgs
init|=
name|getRegexDecl
argument_list|(
name|key
operator|.
name|substring
argument_list|(
name|methodSig
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|allParamsMatch
argument_list|(
name|regexArgs
argument_list|,
name|params
argument_list|)
condition|)
block|{
name|matchFound
operator|=
literal|true
expr_stmt|;
name|Object
name|roleStr
init|=
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|roleStr
operator|instanceof
name|String
condition|)
block|{
name|roles
operator|.
name|addAll
argument_list|(
name|parseRoles
argument_list|(
operator|(
name|String
operator|)
name|roleStr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|matchFound
condition|?
name|roles
else|:
literal|null
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getExactArgOrRegexRoles
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|methodName
parameter_list|,
name|String
index|[]
name|signature
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|matchFound
init|=
literal|false
decl_stmt|;
name|String
name|methodSig
init|=
name|getSignature
argument_list|(
name|methodName
argument_list|,
name|signature
argument_list|)
decl_stmt|;
name|String
name|prefix
init|=
name|methodSig
operator|+
literal|"["
decl_stmt|;
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|e
init|=
name|properties
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
name|e
operator|.
name|nextElement
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
operator|&&
name|key
operator|.
name|endsWith
argument_list|(
literal|"]"
argument_list|)
condition|)
block|{
name|matchFound
operator|=
literal|true
expr_stmt|;
name|Object
name|roleStr
init|=
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|roleStr
operator|instanceof
name|String
condition|)
block|{
name|roles
operator|.
name|addAll
argument_list|(
name|parseRoles
argument_list|(
operator|(
name|String
operator|)
name|roleStr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|matchFound
condition|?
name|roles
else|:
literal|null
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getMethodNameWildcardRoles
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|wildcardRules
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
operator|new
name|Comparator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|s1
parameter_list|,
name|String
name|s2
parameter_list|)
block|{
comment|// returns longer entries before shorter ones...
return|return
name|s2
operator|.
name|length
argument_list|()
operator|-
name|s1
operator|.
name|length
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|e
init|=
name|properties
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|String
name|prefix
init|=
name|key
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|methodName
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|wildcardRules
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|String
name|suffix
init|=
name|key
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|methodName
operator|.
name|endsWith
argument_list|(
name|suffix
argument_list|)
condition|)
block|{
name|wildcardRules
operator|.
name|put
argument_list|(
name|suffix
argument_list|,
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"*"
argument_list|)
operator|&&
name|key
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
operator|&&
name|key
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|String
name|middle
init|=
name|key
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|methodName
operator|.
name|contains
argument_list|(
name|middle
argument_list|)
condition|)
block|{
name|wildcardRules
operator|.
name|put
argument_list|(
name|middle
argument_list|,
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|wildcardRules
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
return|return
name|parseRoles
argument_list|(
name|wildcardRules
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
specifier|static
name|boolean
name|allParamsMatch
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|regexArgs
parameter_list|,
name|Object
index|[]
name|params
parameter_list|)
block|{
if|if
condition|(
name|regexArgs
operator|.
name|size
argument_list|()
operator|!=
name|params
operator|.
name|length
condition|)
return|return
literal|false
return|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|regexArgs
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|params
index|[
name|i
index|]
operator|==
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|params
index|[
name|i
index|]
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|matches
argument_list|(
name|regexArgs
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getRegexDecl
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|inRegex
init|=
literal|false
decl_stmt|;
name|StringBuilder
name|curRegex
init|=
operator|new
name|StringBuilder
argument_list|()
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
name|key
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|inRegex
condition|)
block|{
if|if
condition|(
name|key
operator|.
name|length
argument_list|()
operator|>
name|i
operator|+
literal|1
condition|)
block|{
name|String
name|s
init|=
name|key
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|i
operator|+
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"[/"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|",/"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|inRegex
operator|=
literal|true
expr_stmt|;
name|i
operator|++
expr_stmt|;
continue|continue;
block|}
block|}
block|}
else|else
block|{
name|String
name|s
init|=
name|key
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|i
operator|+
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"/]"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|"/,"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|l
operator|.
name|add
argument_list|(
name|curRegex
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|curRegex
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|inRegex
operator|=
literal|false
expr_stmt|;
continue|continue;
block|}
name|curRegex
operator|.
name|append
argument_list|(
name|key
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|l
return|;
block|}
block|}
end_class

end_unit

