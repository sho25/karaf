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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|commands
package|;
end_package

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
name|Reader
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|OsgiCommandSupport
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
name|gogo
operator|.
name|commands
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
import|;
end_import

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
literal|"grep"
argument_list|,
name|description
operator|=
literal|"Prints lines matching the given pattern"
argument_list|)
specifier|public
class|class
name|GrepAction
extends|extends
name|OsgiCommandSupport
block|{
specifier|public
specifier|static
enum|enum
name|ColorOption
block|{
name|never
block|,
name|always
block|,
name|auto
block|}
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"pattern"
argument_list|,
name|description
operator|=
literal|"Regular expression"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|regex
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
literal|"--line-number"
block|}
argument_list|,
name|description
operator|=
literal|"Prefixes each line of output with the line number within its input file."
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
name|lineNumber
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-v"
argument_list|,
name|aliases
operator|=
block|{
literal|"--invert-match"
block|}
argument_list|,
name|description
operator|=
literal|"Inverts the sense of matching, to select non-matching lines."
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
name|invertMatch
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-w"
argument_list|,
name|aliases
operator|=
block|{
literal|"--word-regexp"
block|}
argument_list|,
name|description
operator|=
literal|"Selects only those lines containing matches that form whole "
operator|+
literal|"words.  The test is that the matching substring must either be "
operator|+
literal|"at  the beginning of the line, or preceded by a non-word constituent "
operator|+
literal|"character.  Similarly, it must be either at the end of "
operator|+
literal|"the line or followed by a non-word constituent character.  "
operator|+
literal|"Word-constituent characters are letters, digits, and the underscore."
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
name|wordRegexp
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-x"
argument_list|,
name|aliases
operator|=
block|{
literal|"--line-regexp"
block|}
argument_list|,
name|description
operator|=
literal|"Selects only those matches that exactly match the whole line."
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
name|lineRegexp
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-i"
argument_list|,
name|aliases
operator|=
block|{
literal|"--ignore-case"
block|}
argument_list|,
name|description
operator|=
literal|"Ignores case distinctions in both the PATTERN and the input files."
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
name|ignoreCase
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-c"
argument_list|,
name|aliases
operator|=
block|{
literal|"--count"
block|}
argument_list|,
name|description
operator|=
literal|"only print a count of matching lines per FILE"
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
name|count
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--color"
argument_list|,
name|aliases
operator|=
block|{
literal|"--colour"
block|}
argument_list|,
name|description
operator|=
literal|"use markers to distinguish the matching string. WHEN may be `always', `never' or `auto'"
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
name|ColorOption
name|color
init|=
name|ColorOption
operator|.
name|auto
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-B"
argument_list|,
name|aliases
operator|=
block|{
literal|"--before-context"
block|}
argument_list|,
name|description
operator|=
literal|"Print NUM lines of leading context before matching lines.  Places a line containing -- between contiguous groups of matches."
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
name|int
name|before
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-A"
argument_list|,
name|aliases
operator|=
block|{
literal|"--after-context"
block|}
argument_list|,
name|description
operator|=
literal|"Print NUM lines of trailing context after matching lines.  Places a line containing -- between contiguous groups of matches."
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
name|int
name|after
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-C"
argument_list|,
name|aliases
operator|=
block|{
literal|"--context"
block|}
argument_list|,
name|description
operator|=
literal|"Print NUM lines of output context.  Places a line containing -- between contiguous groups of matches."
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
name|int
name|context
init|=
literal|0
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|after
operator|<
literal|0
condition|)
block|{
name|after
operator|=
name|context
expr_stmt|;
block|}
if|if
condition|(
name|before
operator|<
literal|0
condition|)
block|{
name|before
operator|=
name|context
expr_stmt|;
block|}
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
name|String
name|regexp
init|=
name|regex
decl_stmt|;
if|if
condition|(
name|wordRegexp
condition|)
block|{
name|regexp
operator|=
literal|"\\b"
operator|+
name|regexp
operator|+
literal|"\\b"
expr_stmt|;
block|}
if|if
condition|(
name|lineRegexp
condition|)
block|{
name|regexp
operator|=
literal|"^"
operator|+
name|regexp
operator|+
literal|"$"
expr_stmt|;
block|}
else|else
block|{
name|regexp
operator|=
literal|".*"
operator|+
name|regexp
operator|+
literal|".*"
expr_stmt|;
block|}
name|Pattern
name|p
decl_stmt|;
name|Pattern
name|p2
decl_stmt|;
if|if
condition|(
name|ignoreCase
condition|)
block|{
name|p
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regexp
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
expr_stmt|;
name|p2
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|p
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regexp
argument_list|)
expr_stmt|;
name|p2
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|boolean
name|firstPrint
init|=
literal|true
decl_stmt|;
name|int
name|nb
init|=
literal|0
decl_stmt|;
name|int
name|lineno
init|=
literal|1
decl_stmt|;
name|String
name|line
decl_stmt|;
name|int
name|lineMatch
init|=
literal|0
decl_stmt|;
name|Reader
name|r
init|=
operator|new
name|InputStreamReader
argument_list|(
name|System
operator|.
name|in
argument_list|)
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|readLine
argument_list|(
name|r
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|p
operator|.
name|matcher
argument_list|(
name|line
argument_list|)
operator|.
name|matches
argument_list|()
operator|^
name|invertMatch
condition|)
block|{
name|Matcher
name|matcher2
init|=
name|p2
operator|.
name|matcher
argument_list|(
name|line
argument_list|)
decl_stmt|;
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
name|matcher2
operator|.
name|find
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|invertMatch
operator|&&
name|color
operator|!=
name|ColorOption
operator|.
name|never
condition|)
block|{
name|matcher2
operator|.
name|appendReplacement
argument_list|(
name|sb
argument_list|,
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|bg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|YELLOW
argument_list|)
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|BLACK
argument_list|)
operator|.
name|a
argument_list|(
name|matcher2
operator|.
name|group
argument_list|()
argument_list|)
operator|.
name|reset
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|matcher2
operator|.
name|appendReplacement
argument_list|(
name|sb
argument_list|,
name|matcher2
operator|.
name|group
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|nb
operator|++
expr_stmt|;
block|}
name|matcher2
operator|.
name|appendTail
argument_list|(
name|sb
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|count
operator|&&
name|lineNumber
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%6d  "
argument_list|,
name|lineno
argument_list|)
operator|+
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|lines
operator|.
name|add
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|lineMatch
operator|=
name|lines
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|lineMatch
operator|!=
literal|0
operator|&
name|lineMatch
operator|+
name|after
operator|+
name|before
operator|<=
name|lines
operator|.
name|size
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|count
condition|)
block|{
if|if
condition|(
operator|!
name|firstPrint
operator|&&
name|before
operator|+
name|after
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|firstPrint
operator|=
literal|false
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
name|lineMatch
operator|+
name|after
condition|;
name|i
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|lines
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
while|while
condition|(
name|lines
operator|.
name|size
argument_list|()
operator|>
name|before
condition|)
block|{
name|lines
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|lineMatch
operator|=
literal|0
expr_stmt|;
block|}
name|lines
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
while|while
condition|(
name|lineMatch
operator|==
literal|0
operator|&&
name|lines
operator|.
name|size
argument_list|()
operator|>
name|before
condition|)
block|{
name|lines
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
name|lineno
operator|++
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|count
operator|&&
name|lineMatch
operator|>
literal|0
condition|)
block|{
if|if
condition|(
operator|!
name|firstPrint
operator|&&
name|before
operator|+
name|after
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|firstPrint
operator|=
literal|false
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
name|lineMatch
operator|+
name|after
condition|;
name|i
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|lines
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|count
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|nb
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{         }
return|return
literal|null
return|;
block|}
specifier|private
name|String
name|readLine
parameter_list|(
name|Reader
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|i
init|=
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|==
operator|-
literal|1
operator|&&
name|buf
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|i
operator|==
operator|-
literal|1
operator|||
name|i
operator|==
literal|'\n'
operator|||
name|i
operator|==
literal|'\r'
condition|)
block|{
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
name|buf
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

