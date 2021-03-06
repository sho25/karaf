begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|impl
operator|.
name|console
operator|.
name|commands
package|;
end_package

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
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|Function
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
name|Process
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|builtins
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|Procedural
block|{
specifier|static
specifier|final
name|String
index|[]
name|functions
init|=
block|{
literal|"each"
block|,
literal|"if"
block|,
literal|"not"
block|,
literal|"throw"
block|,
literal|"try"
block|,
literal|"until"
block|,
literal|"while"
block|,
literal|"break"
block|,
literal|"continue"
block|}
decl_stmt|;
specifier|public
name|Object
name|_main
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
name|argv
operator|==
literal|null
operator|||
name|argv
operator|.
name|length
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
name|Process
name|process
init|=
name|Process
operator|.
name|Utils
operator|.
name|current
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|run
argument_list|(
name|session
argument_list|,
name|process
argument_list|,
name|argv
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OptionException
name|e
parameter_list|)
block|{
name|process
operator|.
name|err
argument_list|()
operator|.
name|println
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|process
operator|.
name|error
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HelpException
name|e
parameter_list|)
block|{
name|process
operator|.
name|err
argument_list|()
operator|.
name|println
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|process
operator|.
name|error
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ThrownException
name|e
parameter_list|)
block|{
name|process
operator|.
name|error
argument_list|(
literal|1
argument_list|)
expr_stmt|;
throw|throw
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
specifier|static
class|class
name|OptionException
extends|extends
name|Exception
block|{
specifier|public
name|OptionException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|HelpException
extends|extends
name|Exception
block|{
specifier|public
name|HelpException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|ThrownException
extends|extends
name|Exception
block|{
specifier|public
name|ThrownException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|BreakException
extends|extends
name|Exception
block|{     }
specifier|protected
specifier|static
class|class
name|ContinueException
extends|extends
name|Exception
block|{     }
specifier|protected
name|Options
name|parseOptions
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|String
index|[]
name|usage
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|HelpException
throws|,
name|OptionException
block|{
try|try
block|{
name|Options
name|opt
init|=
name|Options
operator|.
name|compile
argument_list|(
name|usage
argument_list|,
name|s
lambda|->
name|get
argument_list|(
name|session
argument_list|,
name|s
argument_list|)
argument_list|)
operator|.
name|parse
argument_list|(
name|argv
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|opt
operator|.
name|isSet
argument_list|(
literal|"help"
argument_list|)
condition|)
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|opt
operator|.
name|usage
argument_list|(
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|HelpException
argument_list|(
name|baos
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|opt
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OptionException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|String
name|get
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Object
name|o
init|=
name|session
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|o
operator|!=
literal|null
condition|?
name|o
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
specifier|protected
name|Object
name|run
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Process
name|process
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|Throwable
block|{
switch|switch
condition|(
name|argv
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
condition|)
block|{
case|case
literal|"each"
case|:
return|return
name|doEach
argument_list|(
name|session
argument_list|,
name|process
argument_list|,
name|argv
argument_list|)
return|;
case|case
literal|"if"
case|:
return|return
name|doIf
argument_list|(
name|session
argument_list|,
name|process
argument_list|,
name|argv
argument_list|)
return|;
case|case
literal|"not"
case|:
return|return
name|doNot
argument_list|(
name|session
argument_list|,
name|process
argument_list|,
name|argv
argument_list|)
return|;
case|case
literal|"throw"
case|:
return|return
name|doThrow
argument_list|(
name|session
argument_list|,
name|process
argument_list|,
name|argv
argument_list|)
return|;
case|case
literal|"try"
case|:
return|return
name|doTry
argument_list|(
name|session
argument_list|,
name|process
argument_list|,
name|argv
argument_list|)
return|;
case|case
literal|"until"
case|:
return|return
name|doUntil
argument_list|(
name|session
argument_list|,
name|process
argument_list|,
name|argv
argument_list|)
return|;
case|case
literal|"while"
case|:
return|return
name|doWhile
argument_list|(
name|session
argument_list|,
name|process
argument_list|,
name|argv
argument_list|)
return|;
case|case
literal|"break"
case|:
return|return
name|doBreak
argument_list|(
name|session
argument_list|,
name|process
argument_list|,
name|argv
argument_list|)
return|;
case|case
literal|"continue"
case|:
return|return
name|doContinue
argument_list|(
name|session
argument_list|,
name|process
argument_list|,
name|argv
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|doEach
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Process
name|process
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|usage
init|=
block|{
literal|"each -  loop over the elements"
block|,
literal|"Usage: each [-r] elements [do] { closure }"
block|,
literal|"         elements              an array to iterate on"
block|,
literal|"         closure               a closure to call"
block|,
literal|"  -? --help                    Show help"
block|,
literal|"  -r --result                  Return a list containing each iteration result"
block|,         }
decl_stmt|;
name|Options
name|opt
init|=
name|parseOptions
argument_list|(
name|session
argument_list|,
name|usage
argument_list|,
name|argv
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|elements
init|=
name|getElements
argument_list|(
name|opt
argument_list|)
decl_stmt|;
if|if
condition|(
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
literal|"do"
operator|.
name|equals
argument_list|(
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Function
argument_list|>
name|functions
init|=
name|getFunctions
argument_list|(
name|opt
argument_list|)
decl_stmt|;
if|if
condition|(
name|elements
operator|==
literal|null
operator|||
name|functions
operator|==
literal|null
operator|||
name|functions
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|process
operator|.
name|err
argument_list|()
operator|.
name|println
argument_list|(
literal|"usage: each elements [do] { closure }"
argument_list|)
expr_stmt|;
name|process
operator|.
name|err
argument_list|()
operator|.
name|println
argument_list|(
literal|"       elements: an array to iterate on"
argument_list|)
expr_stmt|;
name|process
operator|.
name|err
argument_list|()
operator|.
name|println
argument_list|(
literal|"       closure: a function or closure to call"
argument_list|)
expr_stmt|;
name|process
operator|.
name|error
argument_list|(
literal|2
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|x
range|:
name|elements
control|)
block|{
name|checkInterrupt
argument_list|()
expr_stmt|;
name|args
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|x
argument_list|)
expr_stmt|;
try|try
block|{
name|results
operator|.
name|add
argument_list|(
name|functions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|execute
argument_list|(
name|session
argument_list|,
name|args
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BreakException
name|b
parameter_list|)
block|{
break|break;
block|}
catch|catch
parameter_list|(
name|ContinueException
name|c
parameter_list|)
block|{
continue|continue;
block|}
block|}
return|return
name|opt
operator|.
name|isSet
argument_list|(
literal|"result"
argument_list|)
condition|?
name|results
else|:
literal|null
return|;
block|}
specifier|protected
name|Object
name|doIf
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Process
name|process
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|usage
init|=
block|{
literal|"if -  if / then / else construct"
block|,
literal|"Usage: if {condition} [then] {if-action} [elif {cond} [then] {elif-action}]... [else] {else-action}"
block|,
literal|"  -? --help                    Show help"
block|,         }
decl_stmt|;
name|Options
name|opt
init|=
name|parseOptions
argument_list|(
name|session
argument_list|,
name|usage
argument_list|,
name|argv
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Function
argument_list|>
name|conditions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Function
argument_list|>
name|actions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Function
name|elseFunction
init|=
literal|null
decl_stmt|;
name|int
name|step
init|=
literal|0
decl_stmt|;
name|boolean
name|error
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|opt
operator|.
name|argObjects
argument_list|()
control|)
block|{
switch|switch
condition|(
name|step
condition|)
block|{
case|case
literal|0
case|:
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|conditions
operator|.
name|add
argument_list|(
operator|(
name|Function
operator|)
name|obj
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
block|}
name|step
operator|=
literal|1
expr_stmt|;
break|break;
case|case
literal|1
case|:
if|if
condition|(
literal|"then"
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|step
operator|=
literal|2
expr_stmt|;
break|break;
block|}
case|case
literal|2
case|:
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|actions
operator|.
name|add
argument_list|(
operator|(
name|Function
operator|)
name|obj
argument_list|)
expr_stmt|;
name|step
operator|=
literal|3
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
block|}
break|break;
case|case
literal|3
case|:
if|if
condition|(
literal|"elif"
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|step
operator|=
literal|4
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"else"
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|step
operator|=
literal|7
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|elseFunction
operator|=
operator|(
name|Function
operator|)
name|obj
expr_stmt|;
name|step
operator|=
literal|8
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
block|}
break|break;
case|case
literal|4
case|:
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|conditions
operator|.
name|add
argument_list|(
operator|(
name|Function
operator|)
name|obj
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
block|}
name|step
operator|=
literal|5
expr_stmt|;
break|break;
case|case
literal|5
case|:
if|if
condition|(
literal|"then"
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|step
operator|=
literal|6
expr_stmt|;
break|break;
block|}
case|case
literal|6
case|:
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|actions
operator|.
name|add
argument_list|(
operator|(
name|Function
operator|)
name|obj
argument_list|)
expr_stmt|;
name|step
operator|=
literal|3
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
block|}
break|break;
case|case
literal|7
case|:
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|elseFunction
operator|=
operator|(
name|Function
operator|)
name|obj
expr_stmt|;
name|step
operator|=
literal|8
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
block|}
break|break;
case|case
literal|8
case|:
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|error
condition|)
block|{
break|break;
block|}
block|}
name|error
operator||=
name|conditions
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|error
operator||=
name|conditions
operator|.
name|size
argument_list|()
operator|!=
name|actions
operator|.
name|size
argument_list|()
expr_stmt|;
if|if
condition|(
name|error
condition|)
block|{
name|process
operator|.
name|err
argument_list|()
operator|.
name|println
argument_list|(
literal|"usage: if {condition} [then] {if-action} [elif {elif-action}]... [else] {else-action}"
argument_list|)
expr_stmt|;
name|process
operator|.
name|error
argument_list|(
literal|2
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|length
init|=
name|conditions
operator|.
name|size
argument_list|()
init|;
name|i
operator|<
name|length
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|isTrue
argument_list|(
name|session
argument_list|,
name|conditions
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|actions
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|execute
argument_list|(
name|session
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|elseFunction
operator|!=
literal|null
condition|)
block|{
return|return
name|elseFunction
operator|.
name|execute
argument_list|(
name|session
argument_list|,
literal|null
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Boolean
name|doNot
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Process
name|process
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|usage
init|=
block|{
literal|"not -  return the opposite condition"
block|,
literal|"Usage: not { condition }"
block|,
literal|"  -? --help                    Show help"
block|,         }
decl_stmt|;
name|Options
name|opt
init|=
name|parseOptions
argument_list|(
name|session
argument_list|,
name|usage
argument_list|,
name|argv
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Function
argument_list|>
name|functions
init|=
name|getFunctions
argument_list|(
name|opt
argument_list|)
decl_stmt|;
if|if
condition|(
name|functions
operator|==
literal|null
operator|||
name|functions
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|process
operator|.
name|err
argument_list|()
operator|.
name|println
argument_list|(
literal|"usage: not { condition }"
argument_list|)
expr_stmt|;
name|process
operator|.
name|error
argument_list|(
literal|2
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
operator|!
name|isTrue
argument_list|(
name|session
argument_list|,
name|functions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Object
name|doThrow
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Process
name|process
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|ThrownException
throws|,
name|HelpException
throws|,
name|OptionException
block|{
name|String
index|[]
name|usage
init|=
block|{
literal|"throw -  throw an exception"
block|,
literal|"Usage: throw [ message [ cause ] ]"
block|,
literal|"       throw exception"
block|,
literal|"       throw"
block|,
literal|"  -? --help                    Show help"
block|,         }
decl_stmt|;
name|Options
name|opt
init|=
name|parseOptions
argument_list|(
name|session
argument_list|,
name|usage
argument_list|,
name|argv
argument_list|)
decl_stmt|;
if|if
condition|(
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|Object
name|exception
init|=
name|session
operator|.
name|get
argument_list|(
literal|"exception"
argument_list|)
decl_stmt|;
if|if
condition|(
name|exception
operator|instanceof
name|Throwable
condition|)
throw|throw
operator|new
name|ThrownException
argument_list|(
operator|(
name|Throwable
operator|)
name|exception
argument_list|)
throw|;
else|else
throw|throw
operator|new
name|ThrownException
argument_list|(
operator|new
name|Exception
argument_list|()
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Throwable
condition|)
block|{
throw|throw
operator|new
name|ThrownException
argument_list|(
operator|(
name|Throwable
operator|)
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
throw|;
block|}
else|else
block|{
name|String
name|message
init|=
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Throwable
name|cause
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
if|if
condition|(
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|Throwable
condition|)
block|{
name|cause
operator|=
operator|(
name|Throwable
operator|)
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
throw|throw
operator|new
name|ThrownException
argument_list|(
operator|new
name|Exception
argument_list|(
name|message
argument_list|)
operator|.
name|initCause
argument_list|(
name|cause
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Object
name|doTry
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Process
name|process
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|usage
init|=
block|{
literal|"try -  try / catch / finally construct"
block|,
literal|"Usage: try { try-action } [ [catch] { catch-action } [ [finally] { finally-action } ]  ]"
block|,
literal|"  -? --help                    Show help"
block|,         }
decl_stmt|;
name|Options
name|opt
init|=
name|parseOptions
argument_list|(
name|session
argument_list|,
name|usage
argument_list|,
name|argv
argument_list|)
decl_stmt|;
name|Function
name|tryAction
init|=
literal|null
decl_stmt|;
name|Function
name|catchFunction
init|=
literal|null
decl_stmt|;
name|Function
name|finallyFunction
init|=
literal|null
decl_stmt|;
name|int
name|step
init|=
literal|0
decl_stmt|;
name|boolean
name|error
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|opt
operator|.
name|argObjects
argument_list|()
control|)
block|{
if|if
condition|(
name|tryAction
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|tryAction
operator|=
operator|(
name|Function
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"catch"
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
condition|)
block|{
if|if
condition|(
name|step
operator|!=
literal|1
condition|)
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"finally"
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
condition|)
block|{
if|if
condition|(
name|step
operator|!=
literal|1
operator|&&
name|step
operator|!=
literal|3
condition|)
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|4
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|step
operator|==
literal|1
operator|||
name|step
operator|==
literal|2
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|catchFunction
operator|=
operator|(
name|Function
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|3
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|step
operator|==
literal|3
operator|||
name|step
operator|==
literal|4
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|finallyFunction
operator|=
operator|(
name|Function
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|5
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|error
operator||=
name|tryAction
operator|==
literal|null
expr_stmt|;
name|error
operator||=
name|catchFunction
operator|==
literal|null
operator|&&
name|finallyFunction
operator|==
literal|null
expr_stmt|;
if|if
condition|(
name|error
condition|)
block|{
name|process
operator|.
name|err
argument_list|()
operator|.
name|println
argument_list|(
literal|"usage: try { try-action } [ [catch] { catch-action } [ [finally] { finally-action } ] ]"
argument_list|)
expr_stmt|;
name|process
operator|.
name|error
argument_list|(
literal|2
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|tryAction
operator|.
name|execute
argument_list|(
name|session
argument_list|,
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BreakException
name|b
parameter_list|)
block|{
throw|throw
name|b
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|session
operator|.
name|put
argument_list|(
literal|"exception"
argument_list|,
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
name|catchFunction
operator|!=
literal|null
condition|)
block|{
name|catchFunction
operator|.
name|execute
argument_list|(
name|session
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|finallyFunction
operator|!=
literal|null
condition|)
block|{
name|finallyFunction
operator|.
name|execute
argument_list|(
name|session
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Object
name|doWhile
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Process
name|process
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|usage
init|=
block|{
literal|"while -  while loop"
block|,
literal|"Usage: while { condition } [do] { action }"
block|,
literal|"  -? --help                    Show help"
block|,         }
decl_stmt|;
name|Options
name|opt
init|=
name|parseOptions
argument_list|(
name|session
argument_list|,
name|usage
argument_list|,
name|argv
argument_list|)
decl_stmt|;
name|Function
name|condition
init|=
literal|null
decl_stmt|;
name|Function
name|action
init|=
literal|null
decl_stmt|;
name|int
name|step
init|=
literal|0
decl_stmt|;
name|boolean
name|error
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|opt
operator|.
name|argObjects
argument_list|()
control|)
block|{
if|if
condition|(
name|condition
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|condition
operator|=
operator|(
name|Function
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"do"
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
condition|)
block|{
if|if
condition|(
name|step
operator|!=
literal|1
condition|)
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|step
operator|==
literal|1
operator|||
name|step
operator|==
literal|2
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|action
operator|=
operator|(
name|Function
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|3
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|error
operator||=
name|condition
operator|==
literal|null
expr_stmt|;
name|error
operator||=
name|action
operator|==
literal|null
expr_stmt|;
if|if
condition|(
name|error
condition|)
block|{
name|process
operator|.
name|err
argument_list|()
operator|.
name|println
argument_list|(
literal|"usage: while { condition } [do] { action }"
argument_list|)
expr_stmt|;
name|process
operator|.
name|error
argument_list|(
literal|2
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
while|while
condition|(
name|isTrue
argument_list|(
name|session
argument_list|,
name|condition
argument_list|)
condition|)
block|{
try|try
block|{
name|action
operator|.
name|execute
argument_list|(
name|session
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BreakException
name|b
parameter_list|)
block|{
break|break;
block|}
catch|catch
parameter_list|(
name|ContinueException
name|c
parameter_list|)
block|{
continue|continue;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Object
name|doUntil
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Process
name|process
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|usage
init|=
block|{
literal|"until -  until loop"
block|,
literal|"Usage: until { condition } [do] { action }"
block|,
literal|"  -? --help                    Show help"
block|,         }
decl_stmt|;
name|Options
name|opt
init|=
name|parseOptions
argument_list|(
name|session
argument_list|,
name|usage
argument_list|,
name|argv
argument_list|)
decl_stmt|;
name|Function
name|condition
init|=
literal|null
decl_stmt|;
name|Function
name|action
init|=
literal|null
decl_stmt|;
name|int
name|step
init|=
literal|0
decl_stmt|;
name|boolean
name|error
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|opt
operator|.
name|argObjects
argument_list|()
control|)
block|{
if|if
condition|(
name|condition
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|condition
operator|=
operator|(
name|Function
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"do"
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
condition|)
block|{
if|if
condition|(
name|step
operator|!=
literal|1
condition|)
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|step
operator|==
literal|1
operator|||
name|step
operator|==
literal|2
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Function
condition|)
block|{
name|action
operator|=
operator|(
name|Function
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|step
operator|=
literal|3
expr_stmt|;
block|}
else|else
block|{
name|error
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|error
operator||=
name|condition
operator|==
literal|null
expr_stmt|;
name|error
operator||=
name|action
operator|==
literal|null
expr_stmt|;
if|if
condition|(
name|error
condition|)
block|{
name|process
operator|.
name|err
argument_list|()
operator|.
name|println
argument_list|(
literal|"usage: until { condition } [do] { action }"
argument_list|)
expr_stmt|;
name|process
operator|.
name|error
argument_list|(
literal|2
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
while|while
condition|(
operator|!
name|isTrue
argument_list|(
name|session
argument_list|,
name|condition
argument_list|)
condition|)
block|{
try|try
block|{
name|action
operator|.
name|execute
argument_list|(
name|session
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BreakException
name|e
parameter_list|)
block|{
break|break;
block|}
catch|catch
parameter_list|(
name|ContinueException
name|c
parameter_list|)
block|{
continue|continue;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Object
name|doBreak
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Process
name|process
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|usage
init|=
block|{
literal|"break -  break from loop"
block|,
literal|"Usage: break"
block|,
literal|"  -? --help                    Show help"
block|,         }
decl_stmt|;
name|parseOptions
argument_list|(
name|session
argument_list|,
name|usage
argument_list|,
name|argv
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BreakException
argument_list|()
throw|;
block|}
specifier|protected
name|Object
name|doContinue
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Process
name|process
parameter_list|,
name|Object
index|[]
name|argv
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|usage
init|=
block|{
literal|"continue -  continue loop"
block|,
literal|"Usage: continue"
block|,
literal|"  -? --help                    Show help"
block|,         }
decl_stmt|;
name|parseOptions
argument_list|(
name|session
argument_list|,
name|usage
argument_list|,
name|argv
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ContinueException
argument_list|()
throw|;
block|}
specifier|private
name|boolean
name|isTrue
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Function
name|function
parameter_list|)
throws|throws
name|Exception
block|{
name|checkInterrupt
argument_list|()
expr_stmt|;
return|return
name|isTrue
argument_list|(
name|function
operator|.
name|execute
argument_list|(
name|session
argument_list|,
literal|null
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isTrue
parameter_list|(
name|Object
name|result
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|checkInterrupt
argument_list|()
expr_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|result
operator|instanceof
name|Boolean
condition|)
return|return
operator|(
name|Boolean
operator|)
name|result
return|;
if|if
condition|(
name|result
operator|instanceof
name|Number
condition|)
block|{
if|if
condition|(
literal|0
operator|==
operator|(
operator|(
name|Number
operator|)
name|result
operator|)
operator|.
name|intValue
argument_list|()
condition|)
return|return
literal|false
return|;
block|}
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|result
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
literal|"0"
operator|.
name|equals
argument_list|(
name|result
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|checkInterrupt
parameter_list|()
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|isInterrupted
argument_list|()
condition|)
throw|throw
operator|new
name|InterruptedException
argument_list|(
literal|"interrupted"
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Collection
argument_list|<
name|Object
argument_list|>
name|getElements
parameter_list|(
name|Options
name|opt
parameter_list|)
block|{
name|Collection
argument_list|<
name|Object
argument_list|>
name|elements
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Object
name|o
init|=
name|opt
operator|.
name|argObjects
argument_list|()
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Collection
condition|)
block|{
name|elements
operator|=
operator|(
name|Collection
argument_list|<
name|Object
argument_list|>
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|!=
literal|null
operator|&&
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|elements
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|elements
return|;
block|}
specifier|private
name|List
argument_list|<
name|Function
argument_list|>
name|getFunctions
parameter_list|(
name|Options
name|opt
parameter_list|)
block|{
name|List
argument_list|<
name|Function
argument_list|>
name|functions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|opt
operator|.
name|argObjects
argument_list|()
control|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Function
condition|)
block|{
name|functions
operator|.
name|add
argument_list|(
operator|(
name|Function
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|functions
operator|=
literal|null
expr_stmt|;
break|break;
block|}
block|}
return|return
name|functions
return|;
block|}
block|}
end_class

end_unit

