begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
name|karaf
operator|.
name|shell
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
specifier|public
class|class
name|EditAction
extends|extends
name|AbstractAction
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
specifier|private
name|EditorFactory
name|editorFactory
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
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
comment|//If no url format found, assume file url.
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
comment|//If its not a file url.
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
comment|//Copy the resource to a tmp location.
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
comment|//Call the editor
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
comment|//If resource is not local, copy the resource back.
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
comment|/**          * Gets the {@link jline.Terminal} from the current session.          *          * @return          * @throws Exception          */
specifier|private
name|Terminal
name|getTerminal
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|terminalObject
init|=
name|session
operator|.
name|get
argument_list|(
literal|".jline.terminal"
argument_list|)
decl_stmt|;
if|if
condition|(
name|terminalObject
operator|instanceof
name|Terminal
condition|)
block|{
return|return
operator|(
name|Terminal
operator|)
name|terminalObject
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Could not get Terminal from CommandSession."
argument_list|)
throw|;
block|}
comment|/**      * Copies the content of {@link InputStream} to {@link OutputStream}.      *      * @param input      * @param output      * @throws IOException      */
specifier|private
name|void
name|copy
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|1024
operator|*
literal|16
index|]
decl_stmt|;
name|int
name|n
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
operator|(
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
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

