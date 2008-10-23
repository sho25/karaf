begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|core
operator|.
name|vfs
operator|.
name|mvn
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|FileObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|FileSystemException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|FileSystemOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|FileSystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|FileName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|provider
operator|.
name|url
operator|.
name|UrlFileProvider
import|;
end_import

begin_class
specifier|public
class|class
name|MvnFileProvider
extends|extends
name|UrlFileProvider
block|{
comment|/**      * Locates a file object, by absolute URI.      */
specifier|public
specifier|synchronized
name|FileObject
name|findFile
parameter_list|(
specifier|final
name|FileObject
name|baseFile
parameter_list|,
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|FileSystemOptions
name|fileSystemOptions
parameter_list|)
throws|throws
name|FileSystemException
block|{
try|try
block|{
specifier|final
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|URL
name|rootUrl
init|=
operator|new
name|URL
argument_list|(
name|url
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|key
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
name|rootUrl
operator|.
name|toString
argument_list|()
decl_stmt|;
name|FileSystem
name|fs
init|=
name|findFileSystem
argument_list|(
name|key
argument_list|,
name|fileSystemOptions
argument_list|)
decl_stmt|;
if|if
condition|(
name|fs
operator|==
literal|null
condition|)
block|{
name|String
name|extForm
init|=
name|rootUrl
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
specifier|final
name|FileName
name|rootName
init|=
name|getContext
argument_list|()
operator|.
name|parseURI
argument_list|(
name|extForm
argument_list|)
decl_stmt|;
comment|// final FileName rootName =
comment|//    new BasicFileName(rootUrl, FileName.ROOT_PATH);
name|fs
operator|=
operator|new
name|MvnFileSystem
argument_list|(
name|rootName
argument_list|,
name|fileSystemOptions
argument_list|)
expr_stmt|;
name|addFileSystem
argument_list|(
name|key
argument_list|,
name|fs
argument_list|)
expr_stmt|;
block|}
return|return
name|fs
operator|.
name|resolveFile
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FileSystemException
argument_list|(
literal|"vfs.provider.url/badly-formed-uri.error"
argument_list|,
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

