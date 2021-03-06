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
name|shell
operator|.
name|console
operator|.
name|completer
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
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|EnumSet
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
name|Map
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
name|gogo
operator|.
name|commands
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
name|felix
operator|.
name|gogo
operator|.
name|commands
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
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|CommandWithAction
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
name|gogo
operator|.
name|commands
operator|.
name|CompleterValues
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
name|gogo
operator|.
name|commands
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
name|CompletableFunction
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
name|NameScoping
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

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|OldArgumentCompleter
implements|implements
name|Completer
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|OldArgumentCompleter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ARGUMENTS_LIST
init|=
literal|"ARGUMENTS_LIST"
decl_stmt|;
specifier|final
name|Completer
name|commandCompleter
decl_stmt|;
specifier|final
name|Completer
name|optionsCompleter
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Completer
argument_list|>
name|argsCompleters
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
name|optionalCompleters
decl_stmt|;
specifier|final
name|CommandWithAction
name|function
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|Option
argument_list|,
name|Field
argument_list|>
name|fields
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
name|Option
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Field
argument_list|>
name|arguments
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|strict
init|=
literal|true
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|public
name|OldArgumentCompleter
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|CommandWithAction
name|function
parameter_list|,
name|String
name|command
parameter_list|)
block|{
name|this
operator|.
name|function
operator|=
name|function
expr_stmt|;
comment|// Command name completer
name|commandCompleter
operator|=
operator|new
name|StringsCompleter
argument_list|(
name|getNames
argument_list|(
name|session
argument_list|,
name|command
argument_list|)
argument_list|)
expr_stmt|;
comment|// Build options completer
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|function
operator|.
name|getActionClass
argument_list|()
init|;
name|type
operator|!=
literal|null
condition|;
name|type
operator|=
name|type
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
for|for
control|(
name|Field
name|field
range|:
name|type
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|Option
name|option
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|option
operator|!=
literal|null
condition|)
block|{
name|fields
operator|.
name|put
argument_list|(
name|option
argument_list|,
name|field
argument_list|)
expr_stmt|;
name|options
operator|.
name|put
argument_list|(
name|option
operator|.
name|name
argument_list|()
argument_list|,
name|option
argument_list|)
expr_stmt|;
name|String
index|[]
name|aliases
init|=
name|option
operator|.
name|aliases
argument_list|()
decl_stmt|;
if|if
condition|(
name|aliases
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|alias
range|:
name|aliases
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|alias
argument_list|,
name|option
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Argument
name|argument
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Argument
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|argument
operator|!=
literal|null
condition|)
block|{
name|Integer
name|key
init|=
name|argument
operator|.
name|index
argument_list|()
decl_stmt|;
if|if
condition|(
name|arguments
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Duplicate @Argument annotations on class "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" for index: "
operator|+
name|key
operator|+
literal|" see: "
operator|+
name|field
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|arguments
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|//        options.put(HelpOption.HELP.name(), HelpOption.HELP);
name|optionsCompleter
operator|=
operator|new
name|StringsCompleter
argument_list|(
name|options
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
comment|// Build arguments completers
name|argsCompleters
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
if|if
condition|(
name|function
operator|instanceof
name|CompletableFunction
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
name|opt
decl_stmt|;
try|try
block|{
comment|//
name|opt
operator|=
operator|(
operator|(
name|CompletableFunction
operator|)
name|function
operator|)
operator|.
name|getOptionalCompleters
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|opt
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|optionalCompleters
operator|=
name|opt
expr_stmt|;
name|List
argument_list|<
name|Completer
argument_list|>
name|fcl
init|=
operator|(
operator|(
name|CompletableFunction
operator|)
name|function
operator|)
operator|.
name|getCompleters
argument_list|()
decl_stmt|;
if|if
condition|(
name|fcl
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Completer
name|c
range|:
name|fcl
control|)
block|{
name|argsCompleters
operator|.
name|add
argument_list|(
name|c
operator|==
literal|null
condition|?
name|NullCompleter
operator|.
name|INSTANCE
else|:
name|c
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|argsCompleters
operator|.
name|add
argument_list|(
name|NullCompleter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|optionalCompleters
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Method
argument_list|>
name|methods
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|function
operator|.
name|getActionClass
argument_list|()
init|;
name|type
operator|!=
literal|null
condition|;
name|type
operator|=
name|type
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
for|for
control|(
name|Method
name|method
range|:
name|type
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
name|CompleterValues
name|completerMethod
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|CompleterValues
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|completerMethod
operator|!=
literal|null
condition|)
block|{
name|int
name|index
init|=
name|completerMethod
operator|.
name|index
argument_list|()
decl_stmt|;
name|Integer
name|key
init|=
name|index
decl_stmt|;
if|if
condition|(
name|index
operator|>=
name|arguments
operator|.
name|size
argument_list|()
operator|||
name|index
operator|<
literal|0
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Index out of range on @CompleterValues on class "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" for index: "
operator|+
name|key
operator|+
literal|" see: "
operator|+
name|method
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|methods
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Duplicate @CompleterMethod annotations on class "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" for index: "
operator|+
name|key
operator|+
literal|" see: "
operator|+
name|method
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|methods
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|size
init|=
name|arguments
operator|.
name|size
argument_list|()
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Completer
name|argCompleter
init|=
name|NullCompleter
operator|.
name|INSTANCE
decl_stmt|;
name|Method
name|method
init|=
name|methods
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
comment|// lets invoke the method
name|Action
name|action
init|=
name|function
operator|.
name|createNewAction
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|value
init|=
name|method
operator|.
name|invoke
argument_list|(
name|action
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
index|[]
condition|)
block|{
name|argCompleter
operator|=
operator|new
name|StringsCompleter
argument_list|(
operator|(
name|String
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|argCompleter
operator|=
operator|new
name|StringsCompleter
argument_list|(
operator|(
name|Collection
argument_list|<
name|String
argument_list|>
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Could not use value "
operator|+
name|value
operator|+
literal|" as set of completions!"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Could not invoke @CompleterMethod on "
operator|+
name|function
operator|+
literal|". "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
name|Throwable
name|target
init|=
name|e
operator|.
name|getTargetException
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
name|target
operator|=
name|e
expr_stmt|;
block|}
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Could not invoke @CompleterMethod on "
operator|+
name|function
operator|+
literal|". "
operator|+
name|target
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|function
operator|.
name|releaseAction
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Failed to release action: "
operator|+
name|action
operator|+
literal|". "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Field
name|field
init|=
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|field
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|File
operator|.
name|class
argument_list|)
condition|)
block|{
name|argCompleter
operator|=
operator|new
name|FileCompleter
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
operator|||
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|boolean
operator|.
name|class
argument_list|)
condition|)
block|{
name|argCompleter
operator|=
operator|new
name|StringsCompleter
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"false"
block|,
literal|"true"
block|}
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|Enum
operator|.
name|class
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|EnumSet
operator|.
name|allOf
argument_list|(
operator|(
name|Class
argument_list|<
name|Enum
argument_list|>
operator|)
name|type
argument_list|)
control|)
block|{
name|values
operator|.
name|add
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|argCompleter
operator|=
operator|new
name|StringsCompleter
argument_list|(
name|values
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO any other completers we can add?
block|}
block|}
name|argsCompleters
operator|.
name|add
argument_list|(
name|argCompleter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
index|[]
name|getNames
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|String
name|scopedCommand
parameter_list|)
block|{
name|String
name|command
init|=
name|NameScoping
operator|.
name|getCommandNameWithoutGlobalPrefix
argument_list|(
name|session
argument_list|,
name|scopedCommand
argument_list|)
decl_stmt|;
name|String
index|[]
name|s
init|=
name|command
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|s
return|;
block|}
else|else
block|{
return|return
operator|new
name|String
index|[]
block|{
name|command
block|,
name|s
index|[
literal|1
index|]
block|}
return|;
block|}
block|}
comment|/**      * If true, a completion at argument index N will only succeed      * if all the completions from 0-(N-1) also succeed.      *      * @param strict The new value of the strict flag.      */
specifier|public
name|void
name|setStrict
parameter_list|(
specifier|final
name|boolean
name|strict
parameter_list|)
block|{
name|this
operator|.
name|strict
operator|=
name|strict
expr_stmt|;
block|}
comment|/**      * Return whether a completion at argument index N will succees      * if all the completions from arguments 0-(N-1) also succeed.      *      * @return The value of the strict flag.      */
specifier|public
name|boolean
name|getStrict
parameter_list|()
block|{
return|return
name|this
operator|.
name|strict
return|;
block|}
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
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|ArgumentList
name|list
init|=
name|delimit
argument_list|(
name|buffer
argument_list|,
name|cursor
argument_list|)
decl_stmt|;
name|int
name|argpos
init|=
name|list
operator|.
name|getArgumentPosition
argument_list|()
decl_stmt|;
name|int
name|argIndex
init|=
name|list
operator|.
name|getCursorArgumentIndex
argument_list|()
decl_stmt|;
comment|//Store the argument list so that it can be used by completers.
name|CommandSession
name|commandSession
init|=
name|CommandSessionHolder
operator|.
name|getSession
argument_list|()
decl_stmt|;
if|if
condition|(
name|commandSession
operator|!=
literal|null
condition|)
block|{
name|commandSession
operator|.
name|put
argument_list|(
name|ARGUMENTS_LIST
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
name|Completer
name|comp
init|=
literal|null
decl_stmt|;
name|String
index|[]
name|args
init|=
name|list
operator|.
name|getArguments
argument_list|()
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
comment|// First argument is command name
if|if
condition|(
name|index
operator|<
name|argIndex
condition|)
block|{
comment|// Verify command name
if|if
condition|(
operator|!
name|verifyCompleter
argument_list|(
name|commandCompleter
argument_list|,
name|args
index|[
name|index
index|]
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|index
operator|++
expr_stmt|;
block|}
else|else
block|{
name|comp
operator|=
name|commandCompleter
expr_stmt|;
block|}
comment|// Now, check options
if|if
condition|(
name|comp
operator|==
literal|null
condition|)
block|{
while|while
condition|(
name|index
operator|<
name|argIndex
operator|&&
name|args
index|[
name|index
index|]
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|verifyCompleter
argument_list|(
name|optionsCompleter
argument_list|,
name|args
index|[
name|index
index|]
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|Option
name|option
init|=
name|options
operator|.
name|get
argument_list|(
name|args
index|[
name|index
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|option
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|Field
name|field
init|=
name|fields
operator|.
name|get
argument_list|(
name|option
argument_list|)
decl_stmt|;
if|if
condition|(
name|field
operator|!=
literal|null
operator|&&
name|field
operator|.
name|getType
argument_list|()
operator|!=
name|boolean
operator|.
name|class
operator|&&
name|field
operator|.
name|getType
argument_list|()
operator|!=
name|Boolean
operator|.
name|class
condition|)
block|{
if|if
condition|(
operator|++
name|index
operator|==
name|argIndex
condition|)
block|{
name|comp
operator|=
name|NullCompleter
operator|.
name|INSTANCE
expr_stmt|;
block|}
block|}
name|index
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|comp
operator|==
literal|null
operator|&&
name|index
operator|>=
name|argIndex
operator|&&
name|index
operator|<
name|args
operator|.
name|length
operator|&&
name|args
index|[
name|index
index|]
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|comp
operator|=
name|optionsCompleter
expr_stmt|;
block|}
block|}
comment|//Now check for if last Option has a completer
name|int
name|lastAgurmentIndex
init|=
name|argIndex
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|lastAgurmentIndex
operator|>=
literal|1
condition|)
block|{
name|Option
name|lastOption
init|=
name|options
operator|.
name|get
argument_list|(
name|args
index|[
name|lastAgurmentIndex
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastOption
operator|!=
literal|null
condition|)
block|{
name|Field
name|lastField
init|=
name|fields
operator|.
name|get
argument_list|(
name|lastOption
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastField
operator|!=
literal|null
operator|&&
name|lastField
operator|.
name|getType
argument_list|()
operator|!=
name|boolean
operator|.
name|class
operator|&&
name|lastField
operator|.
name|getType
argument_list|()
operator|!=
name|Boolean
operator|.
name|class
condition|)
block|{
name|Option
name|option
init|=
name|lastField
operator|.
name|getAnnotation
argument_list|(
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|option
operator|!=
literal|null
condition|)
block|{
name|Completer
name|optionValueCompleter
init|=
literal|null
decl_stmt|;
name|String
name|name
init|=
name|option
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|optionalCompleters
operator|!=
literal|null
operator|&&
name|name
operator|!=
literal|null
condition|)
block|{
name|optionValueCompleter
operator|=
name|optionalCompleters
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|optionValueCompleter
operator|==
literal|null
condition|)
block|{
name|String
index|[]
name|aliases
init|=
name|option
operator|.
name|aliases
argument_list|()
decl_stmt|;
if|if
condition|(
name|aliases
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|aliases
operator|.
name|length
operator|&&
name|optionValueCompleter
operator|==
literal|null
condition|;
name|i
operator|++
control|)
block|{
name|optionValueCompleter
operator|=
name|optionalCompleters
operator|.
name|get
argument_list|(
name|option
operator|.
name|aliases
argument_list|()
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|optionValueCompleter
operator|!=
literal|null
condition|)
block|{
name|comp
operator|=
name|optionValueCompleter
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|// Check arguments
if|if
condition|(
name|comp
operator|==
literal|null
condition|)
block|{
name|int
name|indexArg
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|index
operator|<
name|argIndex
condition|)
block|{
name|Completer
name|sub
init|=
name|argsCompleters
operator|.
name|get
argument_list|(
name|indexArg
operator|>=
name|argsCompleters
operator|.
name|size
argument_list|()
condition|?
name|argsCompleters
operator|.
name|size
argument_list|()
operator|-
literal|1
else|:
name|indexArg
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|verifyCompleter
argument_list|(
name|sub
argument_list|,
name|args
index|[
name|index
index|]
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|index
operator|++
expr_stmt|;
name|indexArg
operator|++
expr_stmt|;
block|}
name|comp
operator|=
name|argsCompleters
operator|.
name|get
argument_list|(
name|indexArg
operator|>=
name|argsCompleters
operator|.
name|size
argument_list|()
condition|?
name|argsCompleters
operator|.
name|size
argument_list|()
operator|-
literal|1
else|:
name|indexArg
argument_list|)
expr_stmt|;
block|}
name|int
name|ret
init|=
name|comp
operator|.
name|complete
argument_list|(
name|list
operator|.
name|getCursorArgument
argument_list|()
argument_list|,
name|argpos
argument_list|,
name|candidates
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|==
operator|-
literal|1
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|pos
init|=
name|ret
operator|+
operator|(
name|list
operator|.
name|getBufferPosition
argument_list|()
operator|-
name|argpos
operator|)
decl_stmt|;
comment|/**          * Special case: when completing in the middle of a line, and the          * area under the cursor is a delimiter, then trim any delimiters          * from the candidates, since we do not need to have an extra          * delimiter.          *          * E.g., if we have a completion for "foo", and we          * enter "f bar" into the buffer, and move to after the "f"          * and hit TAB, we want "foo bar" instead of "foo  bar".          */
if|if
condition|(
operator|(
name|buffer
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|cursor
operator|!=
name|buffer
operator|.
name|length
argument_list|()
operator|)
operator|&&
name|isDelimiter
argument_list|(
name|buffer
argument_list|,
name|cursor
argument_list|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|candidates
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|val
init|=
name|candidates
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
while|while
condition|(
operator|(
name|val
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
operator|&&
name|isDelimiter
argument_list|(
name|val
argument_list|,
name|val
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
condition|)
block|{
name|val
operator|=
name|val
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|val
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|candidates
operator|.
name|set
argument_list|(
name|i
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|pos
return|;
block|}
specifier|protected
name|boolean
name|verifyCompleter
parameter_list|(
name|Completer
name|completer
parameter_list|,
name|String
name|argument
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|candidates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
return|return
name|completer
operator|.
name|complete
argument_list|(
name|argument
argument_list|,
name|argument
operator|.
name|length
argument_list|()
argument_list|,
name|candidates
argument_list|)
operator|!=
operator|-
literal|1
operator|&&
operator|!
name|candidates
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|ArgumentList
name|delimit
parameter_list|(
specifier|final
name|String
name|buffer
parameter_list|,
specifier|final
name|int
name|cursor
parameter_list|)
block|{
name|Parser
name|parser
init|=
operator|new
name|Parser
argument_list|(
name|buffer
argument_list|,
name|cursor
argument_list|)
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|>
name|program
init|=
name|parser
operator|.
name|program
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pipe
init|=
name|program
operator|.
name|get
argument_list|(
name|parser
operator|.
name|c0
argument_list|)
operator|.
name|get
argument_list|(
name|parser
operator|.
name|c1
argument_list|)
decl_stmt|;
return|return
operator|new
name|ArgumentList
argument_list|(
name|pipe
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|pipe
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|parser
operator|.
name|c2
argument_list|,
name|parser
operator|.
name|c3
argument_list|,
name|cursor
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
operator|new
name|ArgumentList
argument_list|(
operator|new
name|String
index|[]
block|{
name|buffer
block|}
argument_list|,
literal|0
argument_list|,
name|cursor
argument_list|,
name|cursor
argument_list|)
return|;
block|}
block|}
comment|/**      * Return true if the specified character is a whitespace      * parameter. Check to ensure that the character is not      * escaped and returns true from      * {@link #isDelimiterChar}.      *      * @param buffer The complete command buffer.      * @param pos The index of the character in the buffer.      * @return True if the character should be a delimiter, false else.      */
specifier|public
name|boolean
name|isDelimiter
parameter_list|(
specifier|final
name|String
name|buffer
parameter_list|,
specifier|final
name|int
name|pos
parameter_list|)
block|{
return|return
operator|!
name|isEscaped
argument_list|(
name|buffer
argument_list|,
name|pos
argument_list|)
operator|&&
name|isDelimiterChar
argument_list|(
name|buffer
argument_list|,
name|pos
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isEscaped
parameter_list|(
specifier|final
name|String
name|buffer
parameter_list|,
specifier|final
name|int
name|pos
parameter_list|)
block|{
return|return
name|pos
operator|>
literal|0
operator|&&
name|buffer
operator|.
name|charAt
argument_list|(
name|pos
argument_list|)
operator|==
literal|'\\'
operator|&&
operator|!
name|isEscaped
argument_list|(
name|buffer
argument_list|,
name|pos
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * The character is a delimiter if it is whitespace, and the      * preceding character is not an escape character.      *      * @param buffer The complete command buffer.      * @param pos The position of the character in the buffer.      * @return True if the character is a delimiter, false else.      */
specifier|public
name|boolean
name|isDelimiterChar
parameter_list|(
name|String
name|buffer
parameter_list|,
name|int
name|pos
parameter_list|)
block|{
return|return
name|Character
operator|.
name|isWhitespace
argument_list|(
name|buffer
operator|.
name|charAt
argument_list|(
name|pos
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * The result of a delimited buffer.      */
specifier|public
specifier|static
class|class
name|ArgumentList
block|{
specifier|private
name|String
index|[]
name|arguments
decl_stmt|;
specifier|private
name|int
name|cursorArgumentIndex
decl_stmt|;
specifier|private
name|int
name|argumentPosition
decl_stmt|;
specifier|private
name|int
name|bufferPosition
decl_stmt|;
comment|/**          *  @param arguments The array of tokens.          *  @param cursorArgumentIndex The token index of the cursor.          *  @param argumentPosition The position of the cursor in the current token.          *  @param bufferPosition The position of the cursor in the whole buffer.          */
specifier|public
name|ArgumentList
parameter_list|(
name|String
index|[]
name|arguments
parameter_list|,
name|int
name|cursorArgumentIndex
parameter_list|,
name|int
name|argumentPosition
parameter_list|,
name|int
name|bufferPosition
parameter_list|)
block|{
name|this
operator|.
name|arguments
operator|=
name|arguments
expr_stmt|;
name|this
operator|.
name|cursorArgumentIndex
operator|=
name|cursorArgumentIndex
expr_stmt|;
name|this
operator|.
name|argumentPosition
operator|=
name|argumentPosition
expr_stmt|;
name|this
operator|.
name|bufferPosition
operator|=
name|bufferPosition
expr_stmt|;
block|}
specifier|public
name|void
name|setCursorArgumentIndex
parameter_list|(
name|int
name|cursorArgumentIndex
parameter_list|)
block|{
name|this
operator|.
name|cursorArgumentIndex
operator|=
name|cursorArgumentIndex
expr_stmt|;
block|}
specifier|public
name|int
name|getCursorArgumentIndex
parameter_list|()
block|{
return|return
name|this
operator|.
name|cursorArgumentIndex
return|;
block|}
specifier|public
name|String
name|getCursorArgument
parameter_list|()
block|{
if|if
condition|(
operator|(
name|cursorArgumentIndex
operator|<
literal|0
operator|)
operator|||
operator|(
name|cursorArgumentIndex
operator|>=
name|arguments
operator|.
name|length
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|arguments
index|[
name|cursorArgumentIndex
index|]
return|;
block|}
specifier|public
name|void
name|setArgumentPosition
parameter_list|(
name|int
name|argumentPosition
parameter_list|)
block|{
name|this
operator|.
name|argumentPosition
operator|=
name|argumentPosition
expr_stmt|;
block|}
specifier|public
name|int
name|getArgumentPosition
parameter_list|()
block|{
return|return
name|this
operator|.
name|argumentPosition
return|;
block|}
specifier|public
name|void
name|setArguments
parameter_list|(
name|String
index|[]
name|arguments
parameter_list|)
block|{
name|this
operator|.
name|arguments
operator|=
name|arguments
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getArguments
parameter_list|()
block|{
return|return
name|this
operator|.
name|arguments
return|;
block|}
specifier|public
name|void
name|setBufferPosition
parameter_list|(
name|int
name|bufferPosition
parameter_list|)
block|{
name|this
operator|.
name|bufferPosition
operator|=
name|bufferPosition
expr_stmt|;
block|}
specifier|public
name|int
name|getBufferPosition
parameter_list|()
block|{
return|return
name|this
operator|.
name|bufferPosition
return|;
block|}
block|}
block|}
end_class

end_unit

