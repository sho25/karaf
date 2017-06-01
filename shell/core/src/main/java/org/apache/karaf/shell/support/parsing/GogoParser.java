begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|// DWB14: parser loops if // comment at start of program
end_comment

begin_comment
comment|// DWB15: allow program to have trailing ';'
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
name|support
operator|.
name|parsing
package|;
end_package

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
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|GogoParser
block|{
name|int
name|current
init|=
literal|0
decl_stmt|;
name|String
name|text
decl_stmt|;
name|boolean
name|escaped
decl_stmt|;
specifier|static
specifier|final
name|String
name|SPECIAL
init|=
literal|"<;|{[\"'$`(="
decl_stmt|;
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
decl_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|statements
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|statement
decl_stmt|;
name|int
name|cursor
decl_stmt|;
name|int
name|start
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|c0
decl_stmt|;
name|int
name|c1
decl_stmt|;
name|int
name|c2
decl_stmt|;
name|int
name|c3
decl_stmt|;
specifier|public
name|GogoParser
parameter_list|(
name|String
name|text
parameter_list|,
name|int
name|cursor
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
name|this
operator|.
name|cursor
operator|=
name|cursor
expr_stmt|;
block|}
specifier|public
name|void
name|ws
parameter_list|()
block|{
comment|// derek: BUGFIX: loop if comment  at beginning of input
comment|//while (!eof()&& isWhitespace(peek())) {
while|while
condition|(
operator|!
name|eof
argument_list|()
operator|&&
operator|(
operator|!
name|escaped
operator|&&
name|isWhitespace
argument_list|(
name|peek
argument_list|()
argument_list|)
operator|||
name|current
operator|==
literal|0
operator|)
condition|)
block|{
if|if
condition|(
name|current
operator|!=
literal|0
operator|||
operator|!
name|escaped
operator|&&
name|isWhitespace
argument_list|(
name|peek
argument_list|()
argument_list|)
condition|)
block|{
name|current
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|peek
argument_list|()
operator|==
literal|'/'
operator|&&
name|current
operator|<
name|text
operator|.
name|length
argument_list|()
operator|-
literal|2
operator|&&
name|text
operator|.
name|charAt
argument_list|(
name|current
operator|+
literal|1
argument_list|)
operator|==
literal|'/'
condition|)
block|{
name|comment
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|current
operator|==
literal|0
condition|)
block|{
break|break;
block|}
block|}
block|}
specifier|private
name|boolean
name|isWhitespace
parameter_list|(
name|char
name|ch
parameter_list|)
block|{
return|return
name|ch
operator|!=
literal|'\n'
operator|&&
name|Character
operator|.
name|isWhitespace
argument_list|(
name|ch
argument_list|)
return|;
block|}
specifier|private
name|void
name|comment
parameter_list|()
block|{
while|while
condition|(
operator|!
name|eof
argument_list|()
operator|&&
name|peek
argument_list|()
operator|!=
literal|'\n'
operator|&&
name|peek
argument_list|()
operator|!=
literal|'\r'
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|eof
parameter_list|()
block|{
return|return
name|current
operator|>=
name|text
operator|.
name|length
argument_list|()
return|;
block|}
specifier|public
name|char
name|peek
parameter_list|()
block|{
return|return
name|peek
argument_list|(
literal|false
argument_list|)
return|;
block|}
name|char
name|peek
parameter_list|(
name|boolean
name|increment
parameter_list|)
block|{
name|escaped
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|eof
argument_list|()
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|last
init|=
name|current
decl_stmt|;
name|char
name|c
init|=
name|text
operator|.
name|charAt
argument_list|(
name|current
operator|++
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\\'
condition|)
block|{
name|escaped
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|eof
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Eof found after \\"
argument_list|)
throw|;
block|}
name|c
operator|=
name|text
operator|.
name|charAt
argument_list|(
name|current
operator|++
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'t'
case|:
name|c
operator|=
literal|'\t'
expr_stmt|;
break|break;
case|case
literal|'\r'
case|:
case|case
literal|'\n'
case|:
name|c
operator|=
literal|' '
expr_stmt|;
break|break;
case|case
literal|'b'
case|:
name|c
operator|=
literal|'\b'
expr_stmt|;
break|break;
case|case
literal|'f'
case|:
name|c
operator|=
literal|'\f'
expr_stmt|;
break|break;
case|case
literal|'n'
case|:
name|c
operator|=
literal|'\n'
expr_stmt|;
break|break;
case|case
literal|'r'
case|:
name|c
operator|=
literal|'\r'
expr_stmt|;
break|break;
case|case
literal|'u'
case|:
name|c
operator|=
name|unicode
argument_list|()
expr_stmt|;
name|current
operator|+=
literal|4
expr_stmt|;
break|break;
default|default:
comment|// We just take the next character literally
comment|// but have the escaped flag set, important for {},[] etc
block|}
block|}
if|if
condition|(
name|cursor
operator|>
name|last
operator|&&
name|cursor
operator|<=
name|current
condition|)
block|{
name|c0
operator|=
name|program
operator|!=
literal|null
condition|?
name|program
operator|.
name|size
argument_list|()
else|:
literal|0
expr_stmt|;
name|c1
operator|=
name|statements
operator|!=
literal|null
condition|?
name|statements
operator|.
name|size
argument_list|()
else|:
literal|0
expr_stmt|;
name|c2
operator|=
name|statement
operator|!=
literal|null
condition|?
name|statement
operator|.
name|size
argument_list|()
else|:
literal|0
expr_stmt|;
name|c3
operator|=
operator|(
name|start
operator|>=
literal|0
operator|)
condition|?
name|current
operator|-
name|start
else|:
literal|0
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|increment
condition|)
block|{
name|current
operator|=
name|last
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|public
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
parameter_list|()
block|{
name|program
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|ws
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|eof
argument_list|()
condition|)
block|{
name|program
operator|.
name|add
argument_list|(
name|pipeline
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
name|peek
argument_list|()
operator|==
literal|';'
operator|||
name|peek
argument_list|()
operator|==
literal|'\n'
condition|)
block|{
name|current
operator|++
expr_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|pipeline
init|=
name|pipeline
argument_list|()
decl_stmt|;
name|program
operator|.
name|add
argument_list|(
name|pipeline
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|eof
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Program has trailing text: "
operator|+
name|context
argument_list|(
name|current
argument_list|)
argument_list|)
throw|;
block|}
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
name|p
init|=
name|program
decl_stmt|;
name|program
operator|=
literal|null
expr_stmt|;
return|return
name|p
return|;
block|}
name|CharSequence
name|context
parameter_list|(
name|int
name|around
parameter_list|)
block|{
return|return
name|text
operator|.
name|subSequence
argument_list|(
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|current
operator|-
literal|20
argument_list|)
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|text
operator|.
name|length
argument_list|()
argument_list|,
name|current
operator|+
literal|4
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|pipeline
parameter_list|()
block|{
name|statements
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|statements
operator|.
name|add
argument_list|(
name|statement
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
name|peek
argument_list|()
operator|==
literal|'|'
condition|)
block|{
name|current
operator|++
expr_stmt|;
name|ws
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|eof
argument_list|()
condition|)
block|{
name|statements
operator|.
name|add
argument_list|(
name|statement
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|statements
operator|.
name|add
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|s
init|=
name|statements
decl_stmt|;
name|statements
operator|=
literal|null
expr_stmt|;
return|return
name|s
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|statement
parameter_list|()
block|{
name|statement
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|statement
operator|.
name|add
argument_list|(
name|value
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|eof
argument_list|()
condition|)
block|{
name|ws
argument_list|()
expr_stmt|;
if|if
condition|(
name|peek
argument_list|()
operator|==
literal|'|'
operator|||
name|peek
argument_list|()
operator|==
literal|';'
operator|||
name|peek
argument_list|()
operator|==
literal|'\n'
condition|)
block|{
break|break;
block|}
if|if
condition|(
operator|!
name|eof
argument_list|()
condition|)
block|{
name|statement
operator|.
name|add
argument_list|(
name|messy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|String
argument_list|>
name|s
init|=
name|statement
decl_stmt|;
name|statement
operator|=
literal|null
expr_stmt|;
return|return
name|s
return|;
block|}
specifier|public
name|String
name|messy
parameter_list|()
block|{
name|start
operator|=
name|current
expr_stmt|;
name|char
name|c
init|=
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|>
literal|0
operator|&&
name|SPECIAL
operator|.
name|indexOf
argument_list|(
name|c
argument_list|)
operator|<
literal|0
condition|)
block|{
name|current
operator|++
expr_stmt|;
try|try
block|{
while|while
condition|(
operator|!
name|eof
argument_list|()
condition|)
block|{
name|c
operator|=
name|peek
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|escaped
operator|&&
operator|(
name|c
operator|==
literal|';'
operator|||
name|c
operator|==
literal|'|'
operator|||
name|c
operator|==
literal|'\n'
operator|||
name|isWhitespace
argument_list|(
name|c
argument_list|)
operator|)
condition|)
block|{
break|break;
block|}
name|next
argument_list|()
expr_stmt|;
block|}
return|return
name|text
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|current
argument_list|)
return|;
block|}
finally|finally
block|{
name|start
operator|=
operator|-
literal|1
expr_stmt|;
block|}
block|}
else|else
block|{
return|return
name|value
argument_list|()
return|;
block|}
block|}
specifier|public
name|int
name|position
parameter_list|()
block|{
return|return
name|current
return|;
block|}
specifier|public
name|int
name|cursorArgumentIndex
parameter_list|()
block|{
return|return
name|c2
return|;
block|}
specifier|public
name|int
name|argumentPosition
parameter_list|()
block|{
return|return
name|c3
return|;
block|}
specifier|public
name|String
name|value
parameter_list|()
block|{
name|ws
argument_list|()
expr_stmt|;
name|start
operator|=
name|current
expr_stmt|;
try|try
block|{
name|char
name|c
init|=
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|escaped
condition|)
block|{
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'{'
case|:
return|return
name|text
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|find
argument_list|(
literal|'}'
argument_list|,
literal|'{'
argument_list|)
argument_list|)
return|;
case|case
literal|'('
case|:
return|return
name|text
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|find
argument_list|(
literal|')'
argument_list|,
literal|'('
argument_list|)
argument_list|)
return|;
case|case
literal|'['
case|:
return|return
name|text
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|find
argument_list|(
literal|']'
argument_list|,
literal|'['
argument_list|)
argument_list|)
return|;
case|case
literal|'<'
case|:
return|return
name|text
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|find
argument_list|(
literal|'>'
argument_list|,
literal|'<'
argument_list|)
argument_list|)
return|;
case|case
literal|'='
case|:
return|return
name|text
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|current
argument_list|)
return|;
case|case
literal|'"'
case|:
case|case
literal|'\''
case|:
name|quote
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
comment|// Some identifier or number
while|while
condition|(
operator|!
name|eof
argument_list|()
condition|)
block|{
name|c
operator|=
name|peek
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|escaped
condition|)
block|{
if|if
condition|(
name|isWhitespace
argument_list|(
name|c
argument_list|)
operator|||
name|c
operator|==
literal|';'
operator|||
name|c
operator|==
literal|'|'
operator|||
name|c
operator|==
literal|'='
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'{'
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
name|find
argument_list|(
literal|'}'
argument_list|,
literal|'{'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'('
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
name|find
argument_list|(
literal|')'
argument_list|,
literal|'('
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'<'
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
name|find
argument_list|(
literal|'>'
argument_list|,
literal|'<'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'['
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
name|find
argument_list|(
literal|']'
argument_list|,
literal|'['
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'\''
operator|||
name|c
operator|==
literal|'"'
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
name|quote
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|next
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|next
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|text
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|current
argument_list|)
return|;
block|}
finally|finally
block|{
name|start
operator|=
operator|-
literal|1
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|escaped
parameter_list|()
block|{
return|return
name|escaped
return|;
block|}
specifier|public
name|char
name|next
parameter_list|()
block|{
return|return
name|peek
argument_list|(
literal|true
argument_list|)
return|;
block|}
name|char
name|unicode
parameter_list|()
block|{
if|if
condition|(
name|current
operator|+
literal|4
operator|>
name|text
operator|.
name|length
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unicode \\u escape at eof at pos ..."
operator|+
name|context
argument_list|(
name|current
argument_list|)
operator|+
literal|"..."
argument_list|)
throw|;
block|}
name|String
name|s
init|=
name|text
operator|.
name|subSequence
argument_list|(
name|current
argument_list|,
name|current
operator|+
literal|4
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|int
name|n
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s
argument_list|,
literal|16
argument_list|)
decl_stmt|;
return|return
operator|(
name|char
operator|)
name|n
return|;
block|}
name|int
name|find
parameter_list|(
name|char
name|target
parameter_list|,
name|char
name|deeper
parameter_list|)
block|{
name|int
name|start
init|=
name|current
decl_stmt|;
name|int
name|level
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|level
operator|!=
literal|0
condition|)
block|{
if|if
condition|(
name|eof
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Eof found in the middle of a compound for '"
operator|+
name|target
operator|+
name|deeper
operator|+
literal|"', begins at "
operator|+
name|context
argument_list|(
name|start
argument_list|)
argument_list|)
throw|;
block|}
name|char
name|c
init|=
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|escaped
condition|)
block|{
if|if
condition|(
name|c
operator|==
name|target
condition|)
block|{
name|level
operator|--
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|c
operator|==
name|deeper
condition|)
block|{
name|level
operator|++
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|c
operator|==
literal|'"'
condition|)
block|{
name|quote
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|c
operator|==
literal|'\''
condition|)
block|{
name|quote
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|c
operator|==
literal|'`'
condition|)
block|{
name|quote
argument_list|(
literal|'`'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
return|return
name|current
return|;
block|}
name|int
name|quote
parameter_list|(
name|char
name|which
parameter_list|)
block|{
while|while
condition|(
operator|!
name|eof
argument_list|()
operator|&&
operator|(
name|peek
argument_list|()
operator|!=
name|which
operator|||
name|escaped
operator|)
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
block|}
return|return
name|current
operator|++
return|;
block|}
name|CharSequence
name|findVar
parameter_list|()
block|{
name|int
name|start
init|=
name|current
decl_stmt|;
name|char
name|c
init|=
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'{'
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
name|int
name|end
init|=
name|find
argument_list|(
literal|'}'
argument_list|,
literal|'{'
argument_list|)
decl_stmt|;
return|return
name|text
operator|.
name|subSequence
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
return|;
block|}
if|if
condition|(
name|c
operator|==
literal|'('
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
name|int
name|end
init|=
name|find
argument_list|(
literal|')'
argument_list|,
literal|'('
argument_list|)
decl_stmt|;
return|return
name|text
operator|.
name|subSequence
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
return|;
block|}
if|if
condition|(
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|c
argument_list|)
condition|)
block|{
while|while
condition|(
name|c
operator|==
literal|'$'
condition|)
block|{
name|c
operator|=
name|next
argument_list|()
expr_stmt|;
block|}
while|while
condition|(
operator|!
name|eof
argument_list|()
operator|&&
operator|(
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|c
argument_list|)
operator|||
name|c
operator|==
literal|'.'
operator|)
operator|&&
name|c
operator|!=
literal|'$'
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
name|c
operator|=
name|peek
argument_list|()
expr_stmt|;
block|}
return|return
name|text
operator|.
name|subSequence
argument_list|(
name|start
argument_list|,
name|current
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Reference to variable does not match syntax of a variable: "
operator|+
name|context
argument_list|(
name|start
argument_list|)
argument_list|)
throw|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"..."
operator|+
name|context
argument_list|(
name|current
argument_list|)
operator|+
literal|"..."
return|;
block|}
specifier|public
name|String
name|unescape
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|eof
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|next
argument_list|()
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
block|}
end_class

end_unit

