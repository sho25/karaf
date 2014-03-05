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
name|commands
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

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
name|io
operator|.
name|FileReader
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
name|io
operator|.
name|InputStreamReader
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|Argument
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

begin_comment
comment|/**  * Sort lines of text  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"shell"
argument_list|,
name|name
operator|=
literal|"sort"
argument_list|,
name|description
operator|=
literal|"Writes sorted concatenation of all files to standard output."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|SortAction
implements|implements
name|Action
block|{
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-f"
argument_list|,
name|aliases
operator|=
block|{
literal|"-ignore-case"
block|}
argument_list|,
name|description
operator|=
literal|"fold lower case to upper case characters"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|boolean
name|caseInsensitive
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
literal|"--reverse"
block|}
argument_list|,
name|description
operator|=
literal|"reverse the result of comparisons"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|boolean
name|reverse
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|aliases
operator|=
block|{
literal|"--unique"
block|}
argument_list|,
name|description
operator|=
literal|"output only the first of an equal run"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|boolean
name|unique
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-t"
argument_list|,
name|aliases
operator|=
block|{
literal|"--field-separator"
block|}
argument_list|,
name|description
operator|=
literal|"use SEP instead of non-blank to blank transition"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|separator
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-b"
argument_list|,
name|aliases
operator|=
block|{
literal|"--ignore-leading-blanks"
block|}
argument_list|,
name|description
operator|=
literal|"ignore leading blanks"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|boolean
name|ignoreBlanks
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-k"
argument_list|,
name|aliases
operator|=
block|{
literal|"--key"
block|}
argument_list|,
name|description
operator|=
literal|"Fields to use for sorting separated by whitespaces"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|sortFields
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-n"
argument_list|,
name|aliases
operator|=
block|{
literal|"--numeric-sort"
block|}
argument_list|,
name|description
operator|=
literal|"compare according to string numerical value"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|boolean
name|numeric
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"files"
argument_list|,
name|description
operator|=
literal|"A list of files separated by whitespaces"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|paths
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
if|if
condition|(
name|paths
operator|!=
literal|null
operator|&&
name|paths
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|filename
range|:
name|paths
control|)
block|{
name|BufferedReader
name|reader
decl_stmt|;
comment|// First try a URL
try|try
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Printing URL: "
operator|+
name|url
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|ignore
parameter_list|)
block|{
comment|// They try a file
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Printing file: "
operator|+
name|file
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|read
argument_list|(
name|reader
argument_list|,
name|lines
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
name|sort
argument_list|(
name|lines
argument_list|,
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sort
argument_list|(
name|System
operator|.
name|in
argument_list|,
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|read
parameter_list|(
name|BufferedReader
name|r
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|s
init|=
name|r
operator|.
name|readLine
argument_list|()
init|;
name|s
operator|!=
literal|null
condition|;
name|s
operator|=
name|r
operator|.
name|readLine
argument_list|()
control|)
block|{
name|lines
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|sort
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|PrintStream
name|out
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|strings
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|BufferedReader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|read
argument_list|(
name|r
argument_list|,
name|strings
argument_list|)
expr_stmt|;
name|sort
argument_list|(
name|strings
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|sort
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|strings
parameter_list|,
name|PrintStream
name|out
parameter_list|)
throws|throws
name|Exception
block|{
name|char
name|sep
init|=
operator|(
name|separator
operator|==
literal|null
operator|||
name|separator
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|)
condition|?
literal|'\0'
else|:
name|separator
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|strings
argument_list|,
operator|new
name|SortComparator
argument_list|(
name|caseInsensitive
argument_list|,
name|reverse
argument_list|,
name|ignoreBlanks
argument_list|,
name|numeric
argument_list|,
name|sep
argument_list|,
name|sortFields
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|last
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|strings
control|)
block|{
if|if
condition|(
operator|!
name|unique
operator|||
name|last
operator|==
literal|null
operator|||
operator|!
name|s
operator|.
name|equals
argument_list|(
name|last
argument_list|)
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|last
operator|=
name|s
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|SortComparator
implements|implements
name|Comparator
argument_list|<
name|String
argument_list|>
block|{
specifier|private
name|boolean
name|caseInsensitive
decl_stmt|;
specifier|private
name|boolean
name|reverse
decl_stmt|;
specifier|private
name|boolean
name|ignoreBlanks
decl_stmt|;
specifier|private
name|boolean
name|numeric
decl_stmt|;
specifier|private
name|char
name|separator
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Key
argument_list|>
name|sortKeys
decl_stmt|;
specifier|private
specifier|static
name|Pattern
name|fpPattern
decl_stmt|;
static|static
block|{
specifier|final
name|String
name|Digits
init|=
literal|"(\\p{Digit}+)"
decl_stmt|;
specifier|final
name|String
name|HexDigits
init|=
literal|"(\\p{XDigit}+)"
decl_stmt|;
specifier|final
name|String
name|Exp
init|=
literal|"[eE][+-]?"
operator|+
name|Digits
decl_stmt|;
specifier|final
name|String
name|fpRegex
init|=
literal|"([\\x00-\\x20]*[+-]?(NaN|Infinity|((("
operator|+
name|Digits
operator|+
literal|"(\\.)?("
operator|+
name|Digits
operator|+
literal|"?)("
operator|+
name|Exp
operator|+
literal|")?)|(\\.("
operator|+
name|Digits
operator|+
literal|")("
operator|+
name|Exp
operator|+
literal|")?)|(((0[xX]"
operator|+
name|HexDigits
operator|+
literal|"(\\.)?)|(0[xX]"
operator|+
name|HexDigits
operator|+
literal|"?(\\.)"
operator|+
name|HexDigits
operator|+
literal|"))[pP][+-]?"
operator|+
name|Digits
operator|+
literal|"))"
operator|+
literal|"[fFdD]?))[\\x00-\\x20]*)(.*)"
decl_stmt|;
name|fpPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|fpRegex
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SortComparator
parameter_list|(
name|boolean
name|caseInsensitive
parameter_list|,
name|boolean
name|reverse
parameter_list|,
name|boolean
name|ignoreBlanks
parameter_list|,
name|boolean
name|numeric
parameter_list|,
name|char
name|separator
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|sortFields
parameter_list|)
block|{
name|this
operator|.
name|caseInsensitive
operator|=
name|caseInsensitive
expr_stmt|;
name|this
operator|.
name|reverse
operator|=
name|reverse
expr_stmt|;
name|this
operator|.
name|separator
operator|=
name|separator
expr_stmt|;
name|this
operator|.
name|ignoreBlanks
operator|=
name|ignoreBlanks
expr_stmt|;
name|this
operator|.
name|numeric
operator|=
name|numeric
expr_stmt|;
if|if
condition|(
name|sortFields
operator|==
literal|null
operator|||
name|sortFields
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|sortFields
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|sortFields
operator|.
name|add
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
block|}
name|sortKeys
operator|=
operator|new
name|ArrayList
argument_list|<
name|Key
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|f
range|:
name|sortFields
control|)
block|{
name|sortKeys
operator|.
name|add
argument_list|(
operator|new
name|Key
argument_list|(
name|f
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|o1
parameter_list|,
name|String
name|o2
parameter_list|)
block|{
name|int
name|res
init|=
literal|0
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|fi1
init|=
name|getFieldIndexes
argument_list|(
name|o1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|fi2
init|=
name|getFieldIndexes
argument_list|(
name|o2
argument_list|)
decl_stmt|;
for|for
control|(
name|Key
name|key
range|:
name|sortKeys
control|)
block|{
name|int
index|[]
name|k1
init|=
name|getSortKey
argument_list|(
name|o1
argument_list|,
name|fi1
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|int
index|[]
name|k2
init|=
name|getSortKey
argument_list|(
name|o2
argument_list|,
name|fi2
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|numeric
condition|)
block|{
name|Double
name|d1
init|=
name|getDouble
argument_list|(
name|o1
argument_list|,
name|k1
index|[
literal|0
index|]
argument_list|,
name|k1
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|Double
name|d2
init|=
name|getDouble
argument_list|(
name|o2
argument_list|,
name|k2
index|[
literal|0
index|]
argument_list|,
name|k2
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|res
operator|=
name|d1
operator|.
name|compareTo
argument_list|(
name|d2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|res
operator|=
name|compareRegion
argument_list|(
name|o1
argument_list|,
name|k1
index|[
literal|0
index|]
argument_list|,
name|k1
index|[
literal|1
index|]
argument_list|,
name|o2
argument_list|,
name|k2
index|[
literal|0
index|]
argument_list|,
name|k2
index|[
literal|1
index|]
argument_list|,
name|key
operator|.
name|caseInsensitive
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|res
operator|!=
literal|0
condition|)
block|{
if|if
condition|(
name|key
operator|.
name|reverse
condition|)
block|{
name|res
operator|=
operator|-
name|res
expr_stmt|;
block|}
break|break;
block|}
block|}
return|return
name|res
return|;
block|}
specifier|protected
name|Double
name|getDouble
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|fpPattern
operator|.
name|matcher
argument_list|(
name|s
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
argument_list|)
decl_stmt|;
name|m
operator|.
name|find
argument_list|()
expr_stmt|;
return|return
operator|new
name|Double
argument_list|(
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|m
operator|.
name|end
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|int
name|compareRegion
parameter_list|(
name|String
name|s1
parameter_list|,
name|int
name|start1
parameter_list|,
name|int
name|end1
parameter_list|,
name|String
name|s2
parameter_list|,
name|int
name|start2
parameter_list|,
name|int
name|end2
parameter_list|,
name|boolean
name|caseInsensitive
parameter_list|)
block|{
name|int
name|n1
init|=
name|end1
decl_stmt|,
name|n2
init|=
name|end2
decl_stmt|;
for|for
control|(
name|int
name|i1
init|=
name|start1
init|,
name|i2
init|=
name|start2
init|;
name|i1
operator|<
name|end1
operator|&&
name|i2
operator|<
name|n2
condition|;
name|i1
operator|++
operator|,
name|i2
operator|++
control|)
block|{
name|char
name|c1
init|=
name|s1
operator|.
name|charAt
argument_list|(
name|i1
argument_list|)
decl_stmt|;
name|char
name|c2
init|=
name|s2
operator|.
name|charAt
argument_list|(
name|i2
argument_list|)
decl_stmt|;
if|if
condition|(
name|c1
operator|!=
name|c2
condition|)
block|{
if|if
condition|(
name|caseInsensitive
condition|)
block|{
name|c1
operator|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|c1
argument_list|)
expr_stmt|;
name|c2
operator|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|c2
argument_list|)
expr_stmt|;
if|if
condition|(
name|c1
operator|!=
name|c2
condition|)
block|{
name|c1
operator|=
name|Character
operator|.
name|toLowerCase
argument_list|(
name|c1
argument_list|)
expr_stmt|;
name|c2
operator|=
name|Character
operator|.
name|toLowerCase
argument_list|(
name|c2
argument_list|)
expr_stmt|;
if|if
condition|(
name|c1
operator|!=
name|c2
condition|)
block|{
return|return
name|c1
operator|-
name|c2
return|;
block|}
block|}
block|}
else|else
block|{
return|return
name|c1
operator|-
name|c2
return|;
block|}
block|}
block|}
return|return
name|n1
operator|-
name|n2
return|;
block|}
specifier|protected
name|int
index|[]
name|getSortKey
parameter_list|(
name|String
name|str
parameter_list|,
name|List
argument_list|<
name|Integer
argument_list|>
name|fields
parameter_list|,
name|Key
name|key
parameter_list|)
block|{
name|int
name|start
decl_stmt|;
name|int
name|end
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startField
operator|*
literal|2
operator|<=
name|fields
operator|.
name|size
argument_list|()
condition|)
block|{
name|start
operator|=
name|fields
operator|.
name|get
argument_list|(
operator|(
name|key
operator|.
name|startField
operator|-
literal|1
operator|)
operator|*
literal|2
argument_list|)
expr_stmt|;
if|if
condition|(
name|key
operator|.
name|ignoreBlanksStart
condition|)
block|{
while|while
condition|(
name|start
operator|<
name|fields
operator|.
name|get
argument_list|(
operator|(
name|key
operator|.
name|startField
operator|-
literal|1
operator|)
operator|*
literal|2
operator|+
literal|1
argument_list|)
operator|&&
name|Character
operator|.
name|isWhitespace
argument_list|(
name|str
operator|.
name|charAt
argument_list|(
name|start
argument_list|)
argument_list|)
condition|)
block|{
name|start
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|key
operator|.
name|startChar
operator|>
literal|0
condition|)
block|{
name|start
operator|=
name|Math
operator|.
name|min
argument_list|(
name|start
operator|+
name|key
operator|.
name|startChar
operator|-
literal|1
argument_list|,
name|fields
operator|.
name|get
argument_list|(
operator|(
name|key
operator|.
name|startField
operator|-
literal|1
operator|)
operator|*
literal|2
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|start
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|key
operator|.
name|endField
operator|>
literal|0
operator|&&
name|key
operator|.
name|endField
operator|*
literal|2
operator|<=
name|fields
operator|.
name|size
argument_list|()
condition|)
block|{
name|end
operator|=
name|fields
operator|.
name|get
argument_list|(
operator|(
name|key
operator|.
name|endField
operator|-
literal|1
operator|)
operator|*
literal|2
argument_list|)
expr_stmt|;
if|if
condition|(
name|key
operator|.
name|ignoreBlanksEnd
condition|)
block|{
while|while
condition|(
name|end
operator|<
name|fields
operator|.
name|get
argument_list|(
operator|(
name|key
operator|.
name|endField
operator|-
literal|1
operator|)
operator|*
literal|2
operator|+
literal|1
argument_list|)
operator|&&
name|Character
operator|.
name|isWhitespace
argument_list|(
name|str
operator|.
name|charAt
argument_list|(
name|end
argument_list|)
argument_list|)
condition|)
block|{
name|end
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|key
operator|.
name|endChar
operator|>
literal|0
condition|)
block|{
name|end
operator|=
name|Math
operator|.
name|min
argument_list|(
name|end
operator|+
name|key
operator|.
name|endChar
operator|-
literal|1
argument_list|,
name|fields
operator|.
name|get
argument_list|(
operator|(
name|key
operator|.
name|endField
operator|-
literal|1
operator|)
operator|*
literal|2
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|end
operator|=
name|str
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|int
index|[]
block|{
name|start
block|,
name|end
block|}
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Integer
argument_list|>
name|getFieldIndexes
parameter_list|(
name|String
name|o
parameter_list|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|fields
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|o
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|separator
operator|==
literal|'\0'
condition|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
name|fields
operator|.
name|add
argument_list|(
literal|0
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|idx
init|=
literal|1
init|;
name|idx
operator|<
name|o
operator|.
name|length
argument_list|()
condition|;
name|idx
operator|++
control|)
block|{
if|if
condition|(
name|Character
operator|.
name|isWhitespace
argument_list|(
name|o
operator|.
name|charAt
argument_list|(
name|idx
argument_list|)
argument_list|)
operator|&&
operator|!
name|Character
operator|.
name|isWhitespace
argument_list|(
name|o
operator|.
name|charAt
argument_list|(
name|idx
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
name|fields
operator|.
name|add
argument_list|(
name|idx
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fields
operator|.
name|add
argument_list|(
name|idx
argument_list|)
expr_stmt|;
block|}
block|}
name|fields
operator|.
name|add
argument_list|(
name|o
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|last
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|idx
init|=
name|o
operator|.
name|indexOf
argument_list|(
name|separator
argument_list|)
init|;
name|idx
operator|>=
literal|0
condition|;
name|idx
operator|=
name|o
operator|.
name|indexOf
argument_list|(
name|separator
argument_list|,
name|idx
operator|+
literal|1
argument_list|)
control|)
block|{
if|if
condition|(
name|last
operator|>=
literal|0
condition|)
block|{
name|fields
operator|.
name|add
argument_list|(
name|last
argument_list|)
expr_stmt|;
name|fields
operator|.
name|add
argument_list|(
name|idx
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|fields
operator|.
name|add
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|fields
operator|.
name|add
argument_list|(
name|idx
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|last
operator|=
name|idx
operator|+
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|last
operator|<
name|o
operator|.
name|length
argument_list|()
condition|)
block|{
name|fields
operator|.
name|add
argument_list|(
name|last
operator|<
literal|0
condition|?
literal|0
else|:
name|last
argument_list|)
expr_stmt|;
name|fields
operator|.
name|add
argument_list|(
name|o
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|fields
return|;
block|}
specifier|public
class|class
name|Key
block|{
name|int
name|startField
decl_stmt|;
name|int
name|startChar
decl_stmt|;
name|int
name|endField
decl_stmt|;
name|int
name|endChar
decl_stmt|;
name|boolean
name|ignoreBlanksStart
decl_stmt|;
name|boolean
name|ignoreBlanksEnd
decl_stmt|;
name|boolean
name|caseInsensitive
decl_stmt|;
name|boolean
name|reverse
decl_stmt|;
name|boolean
name|numeric
decl_stmt|;
specifier|public
name|Key
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|boolean
name|modifiers
init|=
literal|false
decl_stmt|;
name|boolean
name|startPart
init|=
literal|true
decl_stmt|;
name|boolean
name|inField
init|=
literal|true
decl_stmt|;
name|boolean
name|inChar
init|=
literal|false
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|str
operator|.
name|toCharArray
argument_list|()
control|)
block|{
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'0'
case|:
case|case
literal|'1'
case|:
case|case
literal|'2'
case|:
case|case
literal|'3'
case|:
case|case
literal|'4'
case|:
case|case
literal|'5'
case|:
case|case
literal|'6'
case|:
case|case
literal|'7'
case|:
case|case
literal|'8'
case|:
case|case
literal|'9'
case|:
if|if
condition|(
operator|!
name|inField
operator|&&
operator|!
name|inChar
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bad field syntax: "
operator|+
name|str
argument_list|)
throw|;
block|}
if|if
condition|(
name|startPart
condition|)
block|{
if|if
condition|(
name|inChar
condition|)
block|{
name|startChar
operator|=
name|startChar
operator|*
literal|10
operator|+
operator|(
name|c
operator|-
literal|'0'
operator|)
expr_stmt|;
block|}
else|else
block|{
name|startField
operator|=
name|startField
operator|*
literal|10
operator|+
operator|(
name|c
operator|-
literal|'0'
operator|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|inChar
condition|)
block|{
name|endChar
operator|=
name|endChar
operator|*
literal|10
operator|+
operator|(
name|c
operator|-
literal|'0'
operator|)
expr_stmt|;
block|}
else|else
block|{
name|endField
operator|=
name|endField
operator|*
literal|10
operator|+
operator|(
name|c
operator|-
literal|'0'
operator|)
expr_stmt|;
block|}
block|}
break|break;
case|case
literal|'.'
case|:
if|if
condition|(
operator|!
name|inField
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bad field syntax: "
operator|+
name|str
argument_list|)
throw|;
block|}
name|inField
operator|=
literal|false
expr_stmt|;
name|inChar
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|'n'
case|:
name|inField
operator|=
literal|false
expr_stmt|;
name|inChar
operator|=
literal|false
expr_stmt|;
name|modifiers
operator|=
literal|true
expr_stmt|;
name|numeric
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|'f'
case|:
name|inField
operator|=
literal|false
expr_stmt|;
name|inChar
operator|=
literal|false
expr_stmt|;
name|modifiers
operator|=
literal|true
expr_stmt|;
name|caseInsensitive
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|'r'
case|:
name|inField
operator|=
literal|false
expr_stmt|;
name|inChar
operator|=
literal|false
expr_stmt|;
name|modifiers
operator|=
literal|true
expr_stmt|;
name|reverse
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|'b'
case|:
name|inField
operator|=
literal|false
expr_stmt|;
name|inChar
operator|=
literal|false
expr_stmt|;
name|modifiers
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|startPart
condition|)
block|{
name|ignoreBlanksStart
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|ignoreBlanksEnd
operator|=
literal|true
expr_stmt|;
block|}
break|break;
case|case
literal|','
case|:
name|inField
operator|=
literal|true
expr_stmt|;
name|inChar
operator|=
literal|false
expr_stmt|;
name|startPart
operator|=
literal|false
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bad field syntax: "
operator|+
name|str
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|modifiers
condition|)
block|{
name|ignoreBlanksStart
operator|=
name|ignoreBlanksEnd
operator|=
name|SortComparator
operator|.
name|this
operator|.
name|ignoreBlanks
expr_stmt|;
name|reverse
operator|=
name|SortComparator
operator|.
name|this
operator|.
name|reverse
expr_stmt|;
name|caseInsensitive
operator|=
name|SortComparator
operator|.
name|this
operator|.
name|caseInsensitive
expr_stmt|;
name|numeric
operator|=
name|SortComparator
operator|.
name|this
operator|.
name|numeric
expr_stmt|;
block|}
if|if
condition|(
name|startField
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bad field syntax: "
operator|+
name|str
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

