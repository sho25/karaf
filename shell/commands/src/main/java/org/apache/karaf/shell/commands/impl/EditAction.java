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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|OutputStream
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
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|jline
operator|.
name|TerminalSupport
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
name|console
operator|.
name|Terminal
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
name|Reference
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
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|StreamUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jledit
operator|.
name|ConsoleEditor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jledit
operator|.
name|EditorFactory
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
literal|"edit"
argument_list|,
name|description
operator|=
literal|"Calls a text editor."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|EditAction
implements|implements
name|Action
block|{
specifier|private
specifier|final
name|Pattern
name|URL_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[^: ]+:[^ ]+"
argument_list|)
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
literal|"url"
argument_list|,
name|description
operator|=
literal|"The url of the resource to edit."
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
name|url
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|EditorFactory
name|editorFactory
decl_stmt|;
annotation|@
name|Reference
name|Terminal
name|terminal
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
name|URLConnection
name|connection
init|=
literal|null
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|OutputStream
name|os
init|=
literal|null
decl_stmt|;
name|String
name|path
init|=
literal|null
decl_stmt|;
name|boolean
name|isLocal
init|=
literal|true
decl_stmt|;
name|String
name|sourceUrl
init|=
name|url
decl_stmt|;
comment|// if no url format found, assume file url
if|if
condition|(
operator|!
name|URL_PATTERN
operator|.
name|matcher
argument_list|(
name|sourceUrl
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|sourceUrl
argument_list|)
decl_stmt|;
name|sourceUrl
operator|=
literal|"file://"
operator|+
name|f
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
name|URL
name|u
init|=
operator|new
name|URL
argument_list|(
name|sourceUrl
argument_list|)
decl_stmt|;
comment|// if its not a file url
if|if
condition|(
operator|!
name|u
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
literal|"file"
argument_list|)
condition|)
block|{
name|isLocal
operator|=
literal|false
expr_stmt|;
try|try
block|{
name|connection
operator|=
name|u
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|is
operator|=
name|connection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Failed to open "
operator|+
name|sourceUrl
operator|+
literal|" for reading."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
try|try
block|{
name|os
operator|=
name|connection
operator|.
name|getOutputStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Failed to open "
operator|+
name|sourceUrl
operator|+
literal|" for writing."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// copy the resource to a tmp location
name|FileOutputStream
name|fos
init|=
literal|null
decl_stmt|;
try|try
block|{
name|path
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.data"
argument_list|)
operator|+
literal|"/editor/"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
expr_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|getParentFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|f
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|StreamUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|fos
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Failed to copy resource from url:"
operator|+
name|sourceUrl
operator|+
literal|" to tmp file: "
operator|+
name|path
operator|+
literal|"  for editing."
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|StreamUtils
operator|.
name|close
argument_list|(
name|fos
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|path
operator|=
name|u
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
comment|// call the editor
name|ConsoleEditor
name|editor
init|=
name|editorFactory
operator|.
name|create
argument_list|(
name|getTerminal
argument_list|()
argument_list|)
decl_stmt|;
name|editor
operator|.
name|setTitle
argument_list|(
literal|"Karaf"
argument_list|)
expr_stmt|;
name|editor
operator|.
name|open
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|editor
operator|.
name|setOpenEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|editor
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// if resource is not local, copy the resource back
if|if
condition|(
operator|!
name|isLocal
condition|)
block|{
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
try|try
block|{
name|StreamUtils
operator|.
name|copy
argument_list|(
name|fis
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|StreamUtils
operator|.
name|close
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|StreamUtils
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
name|StreamUtils
operator|.
name|close
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Gets the {@link jline.Terminal} from the current session.      *      * @return      * @throws Exception      */
specifier|private
name|jline
operator|.
name|Terminal
name|getTerminal
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
return|return
operator|(
name|jline
operator|.
name|Terminal
operator|)
name|terminal
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getTerminal"
argument_list|)
operator|.
name|invoke
argument_list|(
name|terminal
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
operator|new
name|TerminalSupport
argument_list|(
literal|true
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|getWidth
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getHeight
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|getHeight
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
specifier|public
name|EditorFactory
name|getEditorFactory
parameter_list|()
block|{
return|return
name|editorFactory
return|;
block|}
specifier|public
name|void
name|setEditorFactory
parameter_list|(
name|EditorFactory
name|editorFactory
parameter_list|)
block|{
name|this
operator|.
name|editorFactory
operator|=
name|editorFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

