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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * Util class to manipulate String, especially around escape/unescape.  */
end_comment

begin_class
specifier|public
class|class
name|StringEscapeUtils
block|{
comment|/** Constant for the radix of hex numbers.*/
specifier|private
specifier|static
specifier|final
name|int
name|HEX_RADIX
init|=
literal|16
decl_stmt|;
comment|/** Constant for the length of a unicode literal.*/
specifier|private
specifier|static
specifier|final
name|int
name|UNICODE_LEN
init|=
literal|4
decl_stmt|;
comment|/**      *<p>Unescapes any Java literals found in the<code>String</code> to a      *<code>Writer</code>.</p> This is a slightly modified version of the      * StringEscapeUtils.unescapeJava() function in commons-lang that doesn't      * drop escaped separators (i.e '\,').      *      * @param str  the<code>String</code> to unescape, may be null      * @return the processed string      * @throws IllegalArgumentException if the Writer is<code>null</code>      */
specifier|public
specifier|static
name|String
name|unescapeJava
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|sz
init|=
name|str
operator|.
name|length
argument_list|()
decl_stmt|;
name|StringBuilder
name|out
init|=
operator|new
name|StringBuilder
argument_list|(
name|sz
argument_list|)
decl_stmt|;
name|StringBuilder
name|unicode
init|=
operator|new
name|StringBuilder
argument_list|(
name|UNICODE_LEN
argument_list|)
decl_stmt|;
name|boolean
name|hadSlash
init|=
literal|false
decl_stmt|;
name|boolean
name|inUnicode
init|=
literal|false
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
name|sz
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|inUnicode
condition|)
block|{
comment|// if in unicode, then we're reading unicode
comment|// values in somehow
name|unicode
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
if|if
condition|(
name|unicode
operator|.
name|length
argument_list|()
operator|==
name|UNICODE_LEN
condition|)
block|{
comment|// unicode now contains the four hex digits
comment|// which represents our unicode character
try|try
block|{
name|int
name|value
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|unicode
operator|.
name|toString
argument_list|()
argument_list|,
name|HEX_RADIX
argument_list|)
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|value
argument_list|)
expr_stmt|;
name|unicode
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|inUnicode
operator|=
literal|false
expr_stmt|;
name|hadSlash
operator|=
literal|false
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to parse unicode value: "
operator|+
name|unicode
argument_list|,
name|nfe
argument_list|)
throw|;
block|}
block|}
continue|continue;
block|}
if|if
condition|(
name|hadSlash
condition|)
block|{
comment|// handle an escaped value
name|hadSlash
operator|=
literal|false
expr_stmt|;
switch|switch
condition|(
name|ch
condition|)
block|{
case|case
literal|'\\'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\''
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\"'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'r'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\r'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'f'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\f'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'t'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\t'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'n'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'b'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\b'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'u'
case|:
comment|// uh-oh, we're in unicode country....
name|inUnicode
operator|=
literal|true
expr_stmt|;
break|break;
default|default :
name|out
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
break|break;
block|}
continue|continue;
block|}
elseif|else
if|if
condition|(
name|ch
operator|==
literal|'\\'
condition|)
block|{
name|hadSlash
operator|=
literal|true
expr_stmt|;
continue|continue;
block|}
name|out
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hadSlash
condition|)
block|{
comment|// then we're in the weird case of a \ at the end of the
comment|// string, let's output it anyway.
name|out
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
block|}
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      *<p>Escapes the characters in a<code>String</code> using Java String rules.</p>      *      *<p>Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.)</p>      *      *<p>So a tab becomes the characters<code>'\\'</code> and      *<code>'t'</code>.</p>      *      *<p>The only difference between Java strings and JavaScript strings      * is that in JavaScript, a single quote must be escaped.</p>      *      * Example:      *<pre>      * input string: He didn't say, "Stop!"      * output string: He didn't say, \"Stop!\"      *</pre>      *      * @param str  String to escape values in, may be null      * @return String with escaped values,<code>null</code> if null string input      */
specifier|public
specifier|static
name|String
name|escapeJava
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|sz
init|=
name|str
operator|.
name|length
argument_list|()
decl_stmt|;
name|StringBuilder
name|out
init|=
operator|new
name|StringBuilder
argument_list|(
name|sz
operator|*
literal|2
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
name|sz
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// handle unicode
if|if
condition|(
name|ch
operator|>
literal|0xfff
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"\\u"
argument_list|)
operator|.
name|append
argument_list|(
name|hex
argument_list|(
name|ch
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ch
operator|>
literal|0xff
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"\\u0"
argument_list|)
operator|.
name|append
argument_list|(
name|hex
argument_list|(
name|ch
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ch
operator|>
literal|0x7f
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"\\u00"
argument_list|)
operator|.
name|append
argument_list|(
name|hex
argument_list|(
name|ch
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ch
operator|<
literal|32
condition|)
block|{
switch|switch
condition|(
name|ch
condition|)
block|{
case|case
literal|'\b'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'b'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\n'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'n'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\t'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'t'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\f'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'f'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\r'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'r'
argument_list|)
expr_stmt|;
break|break;
default|default :
if|if
condition|(
name|ch
operator|>
literal|0xf
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"\\u00"
argument_list|)
operator|.
name|append
argument_list|(
name|hex
argument_list|(
name|ch
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|append
argument_list|(
literal|"\\u000"
argument_list|)
operator|.
name|append
argument_list|(
name|hex
argument_list|(
name|ch
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
else|else
block|{
switch|switch
condition|(
name|ch
condition|)
block|{
case|case
literal|'"'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\\'
case|:
name|out
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
break|break;
default|default :
name|out
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      *<p>Returns an upper case hexadecimal<code>String</code> for the given      * character.</p>      *      * @param ch The character to convert.      * @return An upper case hexadecimal<code>String</code>      */
specifier|public
specifier|static
name|String
name|hex
parameter_list|(
name|char
name|ch
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|toHexString
argument_list|(
name|ch
argument_list|)
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
return|;
block|}
block|}
end_class

end_unit

