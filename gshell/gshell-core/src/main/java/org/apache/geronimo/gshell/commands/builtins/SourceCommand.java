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
name|geronimo
operator|.
name|gshell
operator|.
name|commands
operator|.
name|builtins
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
name|InputStreamReader
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
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|clp
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
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|CommandExecutor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|annotation
operator|.
name|CommandComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|annotation
operator|.
name|Requirement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|support
operator|.
name|OsgiCommandSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|IOUtil
import|;
end_import

begin_comment
comment|/**  * Read and execute commands from a file/url in the current shell environment.  *  * @version $Rev: 600779 $ $Date: 2007-12-04 04:55:33 +0100 (Tue, 04 Dec 2007) $  */
end_comment

begin_class
annotation|@
name|CommandComponent
argument_list|(
name|id
operator|=
literal|"gshell-builtins:source"
argument_list|,
name|description
operator|=
literal|"Load a file/url into the current shell"
argument_list|)
specifier|public
class|class
name|SourceCommand
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Requirement
specifier|private
name|CommandExecutor
name|executor
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"Source file"
argument_list|)
specifier|private
name|String
name|source
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
decl_stmt|;
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|url
operator|=
operator|new
name|File
argument_list|(
name|source
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
name|BufferedReader
name|reader
init|=
name|openReader
argument_list|(
name|url
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|line
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
name|String
name|tmp
init|=
name|line
operator|.
name|trim
argument_list|()
decl_stmt|;
comment|// Ignore empty lines and comments
if|if
condition|(
name|tmp
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|||
name|tmp
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|executor
operator|.
name|execute
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|BufferedReader
name|openReader
parameter_list|(
specifier|final
name|Object
name|source
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedReader
name|reader
decl_stmt|;
if|if
condition|(
name|source
operator|instanceof
name|File
condition|)
block|{
name|File
name|file
init|=
operator|(
name|File
operator|)
name|source
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using source file: {}"
argument_list|,
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
elseif|else
if|if
condition|(
name|source
operator|instanceof
name|URL
condition|)
block|{
name|URL
name|url
init|=
operator|(
name|URL
operator|)
name|source
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using source URL: {}"
argument_list|,
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
else|else
block|{
name|String
name|tmp
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|source
argument_list|)
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
name|tmp
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using source URL: {}"
argument_list|,
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
name|tmp
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using source file: {}"
argument_list|,
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
name|tmp
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|reader
return|;
block|}
block|}
end_class

end_unit

