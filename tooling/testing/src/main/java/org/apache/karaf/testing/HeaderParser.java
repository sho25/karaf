begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|testing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

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
name|io
operator|.
name|InputStream
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
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
import|;
end_import

begin_comment
comment|/**  * Utility class to parse a standard OSGi header with paths.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|HeaderParser
block|{
comment|// Private constructor for static final class
specifier|private
name|HeaderParser
parameter_list|()
block|{     }
specifier|public
specifier|static
name|InputStream
name|wireTapManifest
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Manifest
name|mf
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedInputStream
name|bis
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|is
argument_list|,
literal|64
operator|*
literal|1024
argument_list|)
decl_stmt|;
name|bis
operator|.
name|mark
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
expr_stmt|;
name|JarInputStream
name|jis
init|=
operator|new
name|JarInputStream
argument_list|(
name|bis
argument_list|)
decl_stmt|;
name|mf
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putAll
argument_list|(
name|jis
operator|.
name|getManifest
argument_list|()
operator|.
name|getMainAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|mf
operator|.
name|getEntries
argument_list|()
operator|.
name|putAll
argument_list|(
name|jis
operator|.
name|getManifest
argument_list|()
operator|.
name|getEntries
argument_list|()
argument_list|)
expr_stmt|;
name|bis
operator|.
name|reset
argument_list|()
expr_stmt|;
return|return
name|bis
return|;
block|}
comment|/**      * Parse a given OSGi header into a list of paths      *      * @param header the OSGi header to parse      * @return the list of paths extracted from this header      */
specifier|public
specifier|static
name|List
argument_list|<
name|PathElement
argument_list|>
name|parseHeader
parameter_list|(
name|String
name|header
parameter_list|)
block|{
name|List
argument_list|<
name|PathElement
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<
name|PathElement
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|==
literal|null
operator|||
name|header
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|elements
return|;
block|}
name|String
index|[]
name|clauses
init|=
name|parseDelimitedString
argument_list|(
name|header
argument_list|,
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|clause
range|:
name|clauses
control|)
block|{
name|String
index|[]
name|tokens
init|=
name|clause
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokens
operator|.
name|length
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid header clause: "
operator|+
name|clause
argument_list|)
throw|;
block|}
name|PathElement
name|elem
init|=
operator|new
name|PathElement
argument_list|(
name|tokens
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
name|elements
operator|.
name|add
argument_list|(
name|elem
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|tokens
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|pos
init|=
name|tokens
index|[
name|i
index|]
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|pos
operator|>
literal|0
operator|&&
name|tokens
index|[
name|i
index|]
operator|.
name|charAt
argument_list|(
name|pos
operator|-
literal|1
argument_list|)
operator|==
literal|':'
condition|)
block|{
name|String
name|name
init|=
name|tokens
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
operator|-
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|tokens
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|&&
name|value
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|elem
operator|.
name|addDirective
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|name
init|=
name|tokens
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|tokens
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|&&
name|value
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|elem
operator|.
name|addAttribute
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|elem
operator|=
operator|new
name|PathElement
argument_list|(
name|tokens
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|elements
operator|.
name|add
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|elements
return|;
block|}
comment|/**      * Parses delimited string and returns an array containing the tokens. This      * parser obeys quotes, so the delimiter character will be ignored if it is      * inside of a quote. This method assumes that the quote character is not      * included in the set of delimiter characters.      * @param value the delimited string to parse.      * @param delim the characters delimiting the tokens.      * @return an array of string tokens or null if there were no tokens.     **/
specifier|public
specifier|static
name|String
index|[]
name|parseDelimitedString
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|delim
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
literal|""
expr_stmt|;
block|}
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|int
name|CHAR
init|=
literal|1
decl_stmt|;
name|int
name|DELIMITER
init|=
literal|2
decl_stmt|;
name|int
name|STARTQUOTE
init|=
literal|4
decl_stmt|;
name|int
name|ENDQUOTE
init|=
literal|8
decl_stmt|;
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|expecting
init|=
operator|(
name|CHAR
operator||
name|DELIMITER
operator||
name|STARTQUOTE
operator|)
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
name|value
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
name|value
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|boolean
name|isDelimiter
init|=
operator|(
name|delim
operator|.
name|indexOf
argument_list|(
name|c
argument_list|)
operator|>=
literal|0
operator|)
decl_stmt|;
name|boolean
name|isQuote
init|=
operator|(
name|c
operator|==
literal|'"'
operator|)
decl_stmt|;
if|if
condition|(
name|isDelimiter
operator|&&
operator|(
operator|(
name|expecting
operator|&
name|DELIMITER
operator|)
operator|>
literal|0
operator|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|sb
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
name|delete
argument_list|(
literal|0
argument_list|,
name|sb
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|expecting
operator|=
operator|(
name|CHAR
operator||
name|DELIMITER
operator||
name|STARTQUOTE
operator|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isQuote
operator|&&
operator|(
operator|(
name|expecting
operator|&
name|STARTQUOTE
operator|)
operator|>
literal|0
operator|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|expecting
operator|=
name|CHAR
operator||
name|ENDQUOTE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isQuote
operator|&&
operator|(
operator|(
name|expecting
operator|&
name|ENDQUOTE
operator|)
operator|>
literal|0
operator|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|expecting
operator|=
operator|(
name|CHAR
operator||
name|STARTQUOTE
operator||
name|DELIMITER
operator|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|expecting
operator|&
name|CHAR
operator|)
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid delimited string: "
operator|+
name|value
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|list
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|list
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
specifier|static
class|class
name|PathElement
block|{
specifier|private
name|String
name|path
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|directives
decl_stmt|;
specifier|public
name|PathElement
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|attributes
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|directives
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|path
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAttributes
parameter_list|()
block|{
return|return
name|attributes
return|;
block|}
specifier|public
name|String
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|attributes
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|addAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|attributes
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDirectives
parameter_list|()
block|{
return|return
name|directives
return|;
block|}
specifier|public
name|String
name|getDirective
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|directives
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|addDirective
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|directives
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|this
operator|.
name|path
argument_list|)
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
name|directive
range|:
name|this
operator|.
name|directives
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
operator|.
name|append
argument_list|(
name|directive
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":="
argument_list|)
operator|.
name|append
argument_list|(
name|directive
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|attribute
range|:
name|this
operator|.
name|attributes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
operator|.
name|append
argument_list|(
name|attribute
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|attribute
operator|.
name|getValue
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
block|}
end_class

end_unit

