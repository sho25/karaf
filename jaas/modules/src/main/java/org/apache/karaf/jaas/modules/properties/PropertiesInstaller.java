begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|properties
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|fileinstall
operator|.
name|ArtifactInstaller
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
name|utils
operator|.
name|properties
operator|.
name|Properties
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

begin_class
specifier|public
class|class
name|PropertiesInstaller
implements|implements
name|ArtifactInstaller
block|{
specifier|private
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PropertiesInstaller
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|usersFileName
decl_stmt|;
specifier|private
name|File
name|usersFile
decl_stmt|;
name|PropertiesLoginModule
name|propertiesLoginModule
decl_stmt|;
specifier|public
name|PropertiesInstaller
parameter_list|(
name|PropertiesLoginModule
name|propertiesLoginModule
parameter_list|,
name|String
name|usersFile
parameter_list|)
block|{
name|this
operator|.
name|propertiesLoginModule
operator|=
name|propertiesLoginModule
expr_stmt|;
name|this
operator|.
name|usersFileName
operator|=
name|usersFile
expr_stmt|;
block|}
specifier|public
name|boolean
name|canHandle
parameter_list|(
name|File
name|artifact
parameter_list|)
block|{
if|if
condition|(
name|usersFile
operator|==
literal|null
condition|)
block|{
name|usersFile
operator|=
operator|new
name|File
argument_list|(
name|usersFileName
argument_list|)
expr_stmt|;
block|}
return|return
name|artifact
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
name|usersFile
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|install
parameter_list|(
name|File
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|usersFile
operator|==
literal|null
condition|)
block|{
name|usersFile
operator|=
operator|new
name|File
argument_list|(
name|usersFileName
argument_list|)
expr_stmt|;
block|}
name|Properties
name|userProperties
init|=
operator|new
name|Properties
argument_list|(
name|usersFile
argument_list|)
decl_stmt|;
name|this
operator|.
name|propertiesLoginModule
operator|.
name|encryptedPassword
argument_list|(
name|userProperties
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|update
parameter_list|(
name|File
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|usersFile
operator|==
literal|null
condition|)
block|{
name|usersFile
operator|=
operator|new
name|File
argument_list|(
name|usersFileName
argument_list|)
expr_stmt|;
block|}
name|Properties
name|userProperties
init|=
operator|new
name|Properties
argument_list|(
name|usersFile
argument_list|)
decl_stmt|;
name|this
operator|.
name|propertiesLoginModule
operator|.
name|encryptedPassword
argument_list|(
name|userProperties
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|uninstall
parameter_list|(
name|File
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"the users.properties was removed"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

