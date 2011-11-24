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
name|BufferedReader
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
name|File
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
name|net
operator|.
name|URL
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
name|util
operator|.
name|List
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
name|AbstractAction
import|;
end_import

begin_comment
comment|/**  * Concatenate and print files and/or URLs.  *  * @version $Rev: 593392 $ $Date: 2007-11-09 03:14:15 +0100 (Fri, 09 Nov 2007) $  */
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
literal|"cat"
argument_list|,
name|description
operator|=
literal|"Displays the content of a file or URL."
argument_list|)
specifier|public
class|class
name|CatAction
extends|extends
name|AbstractAction
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-n"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Number the output lines, starting at 1."
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
name|displayLineNumbers
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
literal|"paths or urls"
argument_list|,
name|description
operator|=
literal|"A list of file paths or urls to display separated by whitespace (use - for STDIN)"
argument_list|,
name|required
operator|=
literal|true
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
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
comment|//
comment|// Support "-" if length is one, and read from io.in
comment|// This will help test command pipelines.
comment|//
if|if
condition|(
name|paths
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
literal|"-"
operator|.
name|equals
argument_list|(
name|paths
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Printing STDIN"
argument_list|)
expr_stmt|;
name|cat
argument_list|(
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|System
operator|.
name|in
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
name|cat
argument_list|(
name|reader
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
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|cat
parameter_list|(
specifier|final
name|BufferedReader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|line
decl_stmt|;
name|int
name|lineno
init|=
literal|1
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|displayLineNumbers
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%6d  "
argument_list|,
name|lineno
operator|++
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

