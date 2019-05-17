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
name|support
operator|.
name|completers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|DirectoryStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|FileSystems
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|api
operator|.
name|console
operator|.
name|Candidate
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
name|console
operator|.
name|CommandLine
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
name|console
operator|.
name|Completer
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
name|console
operator|.
name|Session
import|;
end_import

begin_class
specifier|public
class|class
name|UriCompleter
implements|implements
name|Completer
block|{
annotation|@
name|Override
specifier|public
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|completeCandidates
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|Candidate
argument_list|>
name|candidates
parameter_list|)
block|{
name|String
name|arg
init|=
name|commandLine
operator|.
name|getCursorArgument
argument_list|()
decl_stmt|;
if|if
condition|(
name|arg
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|arg
operator|.
name|startsWith
argument_list|(
literal|"mvn:"
argument_list|)
condition|)
block|{
name|maven
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
name|file
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|file
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|Candidate
argument_list|>
name|candidates
parameter_list|)
block|{
name|String
name|buffer
init|=
name|commandLine
operator|.
name|getCursorArgument
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|buffer
operator|.
name|substring
argument_list|(
literal|"file:"
operator|.
name|length
argument_list|()
argument_list|,
name|commandLine
operator|.
name|getArgumentPosition
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|rem
init|=
literal|""
decl_stmt|;
try|try
block|{
name|Path
name|dir
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
for|for
control|(
name|Path
name|root
range|:
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getRootDirectories
argument_list|()
control|)
block|{
name|candidates
operator|.
name|add
argument_list|(
operator|new
name|Candidate
argument_list|(
name|root
operator|.
name|toString
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|dir
operator|=
name|Paths
operator|.
name|get
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dir
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|decode
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|path
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|rem
operator|=
name|dir
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
name|dir
operator|=
name|dir
operator|.
name|getParent
argument_list|()
expr_stmt|;
if|if
condition|(
name|dir
operator|==
literal|null
condition|)
block|{
name|dir
operator|=
name|Paths
operator|.
name|get
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|dir
argument_list|)
condition|)
block|{
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|paths
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|dir
argument_list|,
name|rem
operator|+
literal|"*"
argument_list|)
init|)
block|{
for|for
control|(
name|Path
name|child
range|:
name|paths
control|)
block|{
name|String
name|name
init|=
name|encode
argument_list|(
name|child
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|isDir
init|=
name|Files
operator|.
name|isDirectory
argument_list|(
name|child
argument_list|)
decl_stmt|;
if|if
condition|(
name|isDir
condition|)
block|{
name|name
operator|+=
literal|"/"
expr_stmt|;
block|}
name|String
name|dirstr
init|=
name|dir
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|dir
operator|.
name|toString
argument_list|()
else|:
name|dir
operator|.
name|toString
argument_list|()
operator|+
literal|"/"
decl_stmt|;
name|candidates
operator|.
name|add
argument_list|(
operator|new
name|Candidate
argument_list|(
literal|"file:"
operator|+
name|dirstr
operator|+
name|name
argument_list|,
operator|!
name|isDir
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
specifier|private
name|String
name|encode
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
return|;
block|}
specifier|private
name|String
name|decode
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|.
name|replaceAll
argument_list|(
literal|"%20"
argument_list|,
literal|" "
argument_list|)
return|;
block|}
specifier|private
name|void
name|maven
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|Candidate
argument_list|>
name|candidates
parameter_list|)
block|{
name|String
name|repo
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
literal|"/.m2/repository"
decl_stmt|;
name|String
name|buffer
init|=
name|commandLine
operator|.
name|getCursorArgument
argument_list|()
decl_stmt|;
name|String
name|mvn
init|=
name|buffer
operator|.
name|substring
argument_list|(
literal|"mvn:"
operator|.
name|length
argument_list|()
argument_list|,
name|commandLine
operator|.
name|getArgumentPosition
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|rem
init|=
literal|""
decl_stmt|;
try|try
block|{
name|String
index|[]
name|parts
init|=
name|mvn
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|0
operator|||
name|parts
operator|.
name|length
operator|==
literal|1
operator|&&
operator|!
name|mvn
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|StringBuilder
name|known
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringBuilder
name|group
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
index|[]
name|dirs
init|=
name|parts
operator|.
name|length
operator|>
literal|0
condition|?
name|parts
index|[
literal|0
index|]
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
else|:
operator|new
name|String
index|[]
block|{
literal|""
block|}
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|0
operator|&&
name|parts
index|[
literal|0
index|]
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
for|for
control|(
name|String
name|dir
range|:
name|dirs
control|)
block|{
name|known
operator|.
name|append
argument_list|(
name|dir
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|group
operator|.
name|append
argument_list|(
name|dir
argument_list|)
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
name|dirs
operator|.
name|length
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|known
operator|.
name|append
argument_list|(
name|dirs
index|[
name|i
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|group
operator|.
name|append
argument_list|(
name|dirs
index|[
name|i
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
name|rem
operator|=
name|dirs
index|[
name|dirs
operator|.
name|length
operator|-
literal|1
index|]
expr_stmt|;
block|}
name|Path
name|rep
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|Path
name|dir
init|=
name|rep
operator|.
name|resolve
argument_list|(
name|known
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|paths
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|dir
argument_list|,
name|rem
operator|+
literal|"*"
argument_list|)
init|)
block|{
for|for
control|(
name|Path
name|path
range|:
name|paths
control|)
block|{
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|candidates
operator|.
name|add
argument_list|(
operator|new
name|Candidate
argument_list|(
literal|"mvn:"
operator|+
name|group
operator|+
name|name
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|rem
operator|=
name|group
operator|+
name|rem
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|1
operator|||
name|parts
operator|.
name|length
operator|==
literal|2
operator|&&
operator|!
name|mvn
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|rem
operator|=
name|parts
operator|.
name|length
operator|>
literal|1
condition|?
name|parts
index|[
literal|1
index|]
else|:
literal|""
expr_stmt|;
name|Path
name|dir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repo
operator|+
literal|"/"
operator|+
name|parts
index|[
literal|0
index|]
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"/"
argument_list|)
argument_list|)
decl_stmt|;
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|paths
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|dir
argument_list|,
name|rem
operator|+
literal|"*"
argument_list|)
init|)
block|{
for|for
control|(
name|Path
name|path
range|:
name|paths
control|)
block|{
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|candidates
operator|.
name|add
argument_list|(
operator|new
name|Candidate
argument_list|(
literal|"mvn:"
operator|+
name|parts
index|[
literal|0
index|]
operator|+
literal|"/"
operator|+
name|name
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|2
operator|||
name|parts
operator|.
name|length
operator|==
literal|3
operator|&&
operator|!
name|mvn
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|rem
operator|=
name|parts
operator|.
name|length
operator|>
literal|2
condition|?
name|parts
index|[
literal|2
index|]
else|:
literal|""
expr_stmt|;
name|Path
name|dir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repo
operator|+
literal|"/"
operator|+
name|parts
index|[
literal|0
index|]
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|"/"
operator|+
name|parts
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|paths
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|dir
argument_list|,
name|rem
operator|+
literal|"*"
argument_list|)
init|)
block|{
for|for
control|(
name|Path
name|path
range|:
name|paths
control|)
block|{
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|candidates
operator|.
name|add
argument_list|(
operator|new
name|Candidate
argument_list|(
literal|"mvn:"
operator|+
name|parts
index|[
literal|0
index|]
operator|+
literal|"/"
operator|+
name|parts
index|[
literal|1
index|]
operator|+
literal|"/"
operator|+
name|name
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
end_class

end_unit

