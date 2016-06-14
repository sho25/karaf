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
name|shell
operator|.
name|commands
operator|.
name|impl
package|;
end_package

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
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
literal|"date"
argument_list|,
name|description
operator|=
literal|"Display the current time in the given FORMAT"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|DateAction
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-d"
argument_list|,
name|aliases
operator|=
block|{
literal|"--date"
block|}
argument_list|,
name|description
operator|=
literal|"Display time described, not now"
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|,
name|required
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|date
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
literal|"format"
argument_list|,
name|description
operator|=
literal|"Output format"
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|,
name|required
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|format
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
name|Date
name|d
decl_stmt|;
if|if
condition|(
name|date
operator|==
literal|null
operator|||
name|date
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"now"
argument_list|)
condition|)
block|{
name|d
operator|=
operator|new
name|Date
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|date
operator|.
name|startsWith
argument_list|(
literal|"@"
argument_list|)
condition|)
block|{
name|d
operator|=
operator|new
name|Date
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|d
operator|=
operator|new
name|Date
argument_list|(
name|date
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|format
operator|==
literal|null
condition|)
block|{
name|format
operator|=
literal|"%+"
expr_stmt|;
block|}
comment|// transform Unix format to Java SimpleDateFormat (if required)
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|quote
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
name|format
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
name|format
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'%'
condition|)
block|{
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|format
operator|.
name|length
argument_list|()
condition|)
block|{
if|if
condition|(
name|quote
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|quote
operator|=
literal|false
expr_stmt|;
block|}
name|c
operator|=
name|format
operator|.
name|charAt
argument_list|(
operator|++
name|i
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'+'
case|:
case|case
literal|'A'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"MMM EEE d HH:mm:ss yyyy"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'a'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"EEE"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'B'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"MMMMMMM"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'b'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"MMM"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'C'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"yy"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'c'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"MMM EEE d HH:mm:ss yyyy"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'D'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"MM/dd/yy"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'d'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"dd"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'e'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"dd"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'F'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"yyyy-MM-dd"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'G'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"YYYY"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'g'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"YY"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'H'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"HH"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'h'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"MMM"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'I'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"hh"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'j'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"DDD"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'k'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"HH"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'l'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"hh"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'M'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"mm"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'m'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"MM"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'N'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"S"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'n'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'P'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"aa"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'p'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"aa"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'r'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"hh:mm:ss aa"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'R'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"HH:mm"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'S'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"ss"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'s'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"S"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'T'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"HH:mm:ss"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'t'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'U'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"w"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'u'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"u"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'V'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"W"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'v'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"dd-MMM-yyyy"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'W'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"w"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'w'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"u"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'X'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"HH:mm:ss"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'x'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"MM/dd/yy"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'Y'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"yyyy"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'y'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"yy"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'Z'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"z"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'z'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'%'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"%"
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|quote
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|(
name|c
operator|>=
literal|'A'
operator|&&
name|c
operator|<=
literal|'Z'
operator|||
name|c
operator|>=
literal|'a'
operator|&&
name|c
operator|<=
literal|'z'
operator|)
operator|&&
operator|!
name|quote
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|quote
operator|=
literal|true
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|DateFormat
name|df
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|df
operator|.
name|format
argument_list|(
name|d
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

