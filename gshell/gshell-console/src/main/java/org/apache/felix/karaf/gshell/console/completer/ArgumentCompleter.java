begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) 2002-2007, Marc Prud'hommeaux. All rights reserved.  *  * This software is distributable under the BSD license. See the terms of the  * BSD license in the documentation provided with this software.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|gshell
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
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|console
operator|.
name|Completer
import|;
end_import

begin_class
specifier|public
class|class
name|ArgumentCompleter
implements|implements
name|Completer
block|{
specifier|final
name|Completer
index|[]
name|completers
decl_stmt|;
specifier|final
name|ArgumentDelimiter
name|delim
decl_stmt|;
name|boolean
name|strict
init|=
literal|true
decl_stmt|;
comment|/**      *  Constuctor: create a new completor with the default      *  argument separator of " ".      *      *  @param  completer  the embedded completer      */
specifier|public
name|ArgumentCompleter
parameter_list|(
specifier|final
name|Completer
name|completer
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|Completer
index|[]
block|{
name|completer
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      *  Constuctor: create a new completor with the default      *  argument separator of " ".      *      *  @param  completers  the List of completors to use      */
specifier|public
name|ArgumentCompleter
parameter_list|(
specifier|final
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
parameter_list|)
block|{
name|this
argument_list|(
name|completers
operator|.
name|toArray
argument_list|(
operator|new
name|Completer
index|[
name|completers
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      *  Constuctor: create a new completor with the default      *  argument separator of " ".      *      *  @param  completers  the embedded argument completers      */
specifier|public
name|ArgumentCompleter
parameter_list|(
specifier|final
name|Completer
index|[]
name|completers
parameter_list|)
block|{
name|this
argument_list|(
name|completers
argument_list|,
operator|new
name|WhitespaceArgumentDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      *  Constuctor: create a new completor with the specified      *  argument delimiter.      *      *  @param  completer the embedded completer      *  @param  delim     the delimiter for parsing arguments      */
specifier|public
name|ArgumentCompleter
parameter_list|(
specifier|final
name|Completer
name|completer
parameter_list|,
specifier|final
name|ArgumentDelimiter
name|delim
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|Completer
index|[]
block|{
name|completer
block|}
argument_list|,
name|delim
argument_list|)
expr_stmt|;
block|}
comment|/**      *  Constuctor: create a new completor with the specified      *  argument delimiter.      *      *  @param  completers the embedded completers      *  @param  delim      the delimiter for parsing arguments      */
specifier|public
name|ArgumentCompleter
parameter_list|(
specifier|final
name|Completer
index|[]
name|completers
parameter_list|,
specifier|final
name|ArgumentDelimiter
name|delim
parameter_list|)
block|{
name|this
operator|.
name|completers
operator|=
name|completers
expr_stmt|;
name|this
operator|.
name|delim
operator|=
name|delim
expr_stmt|;
block|}
comment|/**      *  If true, a completion at argument index N will only succeed      *  if all the completions from 0-(N-1) also succeed.      */
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
comment|/**      *  Returns whether a completion at argument index N will succees      *  if all the completions from arguments 0-(N-1) also succeed.      */
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
name|delim
operator|.
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
if|if
condition|(
name|argIndex
operator|<
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|final
name|Completer
name|comp
decl_stmt|;
comment|// if we are beyond the end of the completors, just use the last one
if|if
condition|(
name|argIndex
operator|>=
name|completers
operator|.
name|length
condition|)
block|{
name|comp
operator|=
name|completers
index|[
name|completers
operator|.
name|length
operator|-
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
name|comp
operator|=
name|completers
index|[
name|argIndex
index|]
expr_stmt|;
block|}
comment|// ensure that all the previous completors are successful before
comment|// allowing this completor to pass (only if strict is true).
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|getStrict
argument_list|()
operator|&&
operator|(
name|i
operator|<
name|argIndex
operator|)
condition|;
name|i
operator|++
control|)
block|{
name|Completer
name|sub
init|=
name|completers
index|[
operator|(
name|i
operator|>=
name|completers
operator|.
name|length
operator|)
condition|?
operator|(
name|completers
operator|.
name|length
operator|-
literal|1
operator|)
else|:
name|i
index|]
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
name|String
name|arg
init|=
operator|(
operator|(
name|args
operator|==
literal|null
operator|)
operator|||
operator|(
name|i
operator|>=
name|args
operator|.
name|length
operator|)
operator|)
condition|?
literal|""
else|:
name|args
index|[
name|i
index|]
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|subCandidates
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|sub
operator|.
name|complete
argument_list|(
name|arg
argument_list|,
name|arg
operator|.
name|length
argument_list|()
argument_list|,
name|subCandidates
argument_list|)
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
if|if
condition|(
name|subCandidates
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
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
comment|/**          *  Special case: when completing in the middle of a line, and the          *  area under the cursor is a delimiter, then trim any delimiters          *  from the candidates, since we do not need to have an extra          *  delimiter.          *          *  E.g., if we have a completion for "foo", and we          *  enter "f bar" into the buffer, and move to after the "f"          *  and hit TAB, we want "foo bar" instead of "foo  bar".          */
if|if
condition|(
operator|(
name|cursor
operator|!=
name|buffer
operator|.
name|length
argument_list|()
operator|)
operator|&&
name|delim
operator|.
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
name|delim
operator|.
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
comment|/**      *  The {@link ArgumentCompleter.ArgumentDelimiter} allows custom      *  breaking up of a {@link String} into individual arguments in      *  order to dispatch the arguments to the nested {@link Completer}.      *      *  @author<a href="mailto:mwp1@cornell.edu">Marc Prud'hommeaux</a>      */
specifier|public
specifier|static
interface|interface
name|ArgumentDelimiter
block|{
comment|/**          *  Break the specified buffer into individual tokens          *  that can be completed on their own.          *          *  @param  buffer           the buffer to split          *  @param  argumentPosition the current position of the          *                           cursor in the buffer          *  @return                  the tokens          */
name|ArgumentList
name|delimit
parameter_list|(
name|String
name|buffer
parameter_list|,
name|int
name|argumentPosition
parameter_list|)
function_decl|;
comment|/**          *  Returns true if the specified character is a whitespace          *  parameter.          *          *  @param  buffer the complete command buffer          *  @param  pos    the index of the character in the buffer          *  @return        true if the character should be a delimiter          */
name|boolean
name|isDelimiter
parameter_list|(
name|String
name|buffer
parameter_list|,
name|int
name|pos
parameter_list|)
function_decl|;
block|}
comment|/**      *  Abstract implementation of a delimiter that uses the      *  {@link #isDelimiter} method to determine if a particular      *  character should be used as a delimiter.      *      *  @author<a href="mailto:mwp1@cornell.edu">Marc Prud'hommeaux</a>      */
specifier|public
specifier|abstract
specifier|static
class|class
name|AbstractArgumentDelimiter
implements|implements
name|ArgumentDelimiter
block|{
specifier|private
name|char
index|[]
name|quoteChars
init|=
operator|new
name|char
index|[]
block|{
literal|'\''
block|,
literal|'"'
block|}
decl_stmt|;
specifier|private
name|char
index|[]
name|escapeChars
init|=
operator|new
name|char
index|[]
block|{
literal|'\\'
block|}
decl_stmt|;
specifier|public
name|void
name|setQuoteChars
parameter_list|(
specifier|final
name|char
index|[]
name|quoteChars
parameter_list|)
block|{
name|this
operator|.
name|quoteChars
operator|=
name|quoteChars
expr_stmt|;
block|}
specifier|public
name|char
index|[]
name|getQuoteChars
parameter_list|()
block|{
return|return
name|this
operator|.
name|quoteChars
return|;
block|}
specifier|public
name|void
name|setEscapeChars
parameter_list|(
specifier|final
name|char
index|[]
name|escapeChars
parameter_list|)
block|{
name|this
operator|.
name|escapeChars
operator|=
name|escapeChars
expr_stmt|;
block|}
specifier|public
name|char
index|[]
name|getEscapeChars
parameter_list|()
block|{
return|return
name|this
operator|.
name|escapeChars
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
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|StringBuffer
name|arg
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|argpos
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|bindex
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|(
name|buffer
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|i
operator|<=
name|buffer
operator|.
name|length
argument_list|()
operator|)
condition|;
name|i
operator|++
control|)
block|{
comment|// once we reach the cursor, set the
comment|// position of the selected index
if|if
condition|(
name|i
operator|==
name|cursor
condition|)
block|{
name|bindex
operator|=
name|args
operator|.
name|size
argument_list|()
expr_stmt|;
comment|// the position in the current argument is just the
comment|// length of the current argument
name|argpos
operator|=
name|arg
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|i
operator|==
name|buffer
operator|.
name|length
argument_list|()
operator|)
operator|||
name|isDelimiter
argument_list|(
name|buffer
argument_list|,
name|i
argument_list|)
condition|)
block|{
if|if
condition|(
name|arg
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
name|arg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|arg
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// reset the arg
block|}
block|}
else|else
block|{
name|arg
operator|.
name|append
argument_list|(
name|buffer
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
operator|new
name|ArgumentList
argument_list|(
name|args
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|args
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|bindex
argument_list|,
name|argpos
argument_list|,
name|cursor
argument_list|)
return|;
block|}
comment|/**          *  Returns true if the specified character is a whitespace          *  parameter. Check to ensure that the character is not          *  escaped by any of          *  {@link #getQuoteChars}, and is not escaped by ant of the          *  {@link #getEscapeChars}, and returns true from          *  {@link #isDelimiterChar}.          *          *  @param  buffer the complete command buffer          *  @param  pos    the index of the character in the buffer          *  @return        true if the character should be a delimiter          */
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
if|if
condition|(
name|isQuoted
argument_list|(
name|buffer
argument_list|,
name|pos
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|isEscaped
argument_list|(
name|buffer
argument_list|,
name|pos
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
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
name|isQuoted
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
literal|false
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
if|if
condition|(
name|pos
operator|<=
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|(
name|escapeChars
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|i
operator|<
name|escapeChars
operator|.
name|length
operator|)
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|buffer
operator|.
name|charAt
argument_list|(
name|pos
argument_list|)
operator|==
name|escapeChars
index|[
name|i
index|]
condition|)
block|{
return|return
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
comment|// escape escape
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**          *  Returns true if the character at the specified position          *  if a delimiter. This method will only be called if the          *  character is not enclosed in any of the          *  {@link #getQuoteChars}, and is not escaped by ant of the          *  {@link #getEscapeChars}. To perform escaping manually,          *  override {@link #isDelimiter} instead.          */
specifier|public
specifier|abstract
name|boolean
name|isDelimiterChar
parameter_list|(
name|String
name|buffer
parameter_list|,
name|int
name|pos
parameter_list|)
function_decl|;
block|}
comment|/**      *  {@link ArgumentCompleter.ArgumentDelimiter}      *  implementation that counts all      *  whitespace (as reported by {@link Character#isWhitespace})      *  as being a delimiter.      *      *  @author<a href="mailto:mwp1@cornell.edu">Marc Prud'hommeaux</a>      */
specifier|public
specifier|static
class|class
name|WhitespaceArgumentDelimiter
extends|extends
name|AbstractArgumentDelimiter
block|{
comment|/**          *  The character is a delimiter if it is whitespace, and the          *  preceeding character is not an escape character.          */
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
block|}
comment|/**      *  The result of a delimited buffer.      *      *  @author<a href="mailto:mwp1@cornell.edu">Marc Prud'hommeaux</a>      */
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
comment|/**          *  @param  arguments           the array of tokens          *  @param  cursorArgumentIndex the token index of the cursor          *  @param  argumentPosition    the position of the cursor in the          *                              current token          *  @param  bufferPosition      the position of the cursor in          *                              the whole buffer          */
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

