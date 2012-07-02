begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|dev
operator|.
name|core
operator|.
name|internal
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|maven
operator|.
name|commons
operator|.
name|MavenConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|maven
operator|.
name|commons
operator|.
name|MavenConfigurationImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|maven
operator|.
name|commons
operator|.
name|MavenRepositoryURL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|wrap
operator|.
name|ServiceConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|util
operator|.
name|property
operator|.
name|DictionaryPropertyResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
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
name|MavenConfigService
block|{
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BundleWatcherImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ConfigurationAdmin
name|configurationAdmin
decl_stmt|;
specifier|public
name|MavenConfigService
parameter_list|(
name|ConfigurationAdmin
name|configurationAdmin
parameter_list|)
block|{
name|this
operator|.
name|configurationAdmin
operator|=
name|configurationAdmin
expr_stmt|;
block|}
specifier|public
name|File
name|getLocalRepository
parameter_list|()
block|{
comment|// Attempt to retrieve local repository location from MavenConfiguration
name|MavenConfiguration
name|configuration
init|=
name|retrieveMavenConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|MavenRepositoryURL
name|localRepositoryURL
init|=
name|configuration
operator|.
name|getLocalRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|localRepositoryURL
operator|!=
literal|null
condition|)
block|{
return|return
name|localRepositoryURL
operator|.
name|getFile
argument_list|()
operator|.
name|getAbsoluteFile
argument_list|()
return|;
block|}
block|}
comment|// If local repository not found assume default.
name|String
name|localRepo
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
literal|".m2"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"repository"
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|localRepo
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
return|;
block|}
specifier|private
name|MavenConfiguration
name|retrieveMavenConfiguration
parameter_list|()
block|{
name|MavenConfiguration
name|mavenConfiguration
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|ServiceConstants
operator|.
name|PID
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|Dictionary
name|dictonary
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|dictonary
operator|!=
literal|null
condition|)
block|{
name|DictionaryPropertyResolver
name|resolver
init|=
operator|new
name|DictionaryPropertyResolver
argument_list|(
name|dictonary
argument_list|)
decl_stmt|;
name|mavenConfiguration
operator|=
operator|new
name|MavenConfigurationImpl
argument_list|(
name|resolver
argument_list|,
name|ServiceConstants
operator|.
name|PID
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Error retrieving maven configuration"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|mavenConfiguration
return|;
block|}
block|}
end_class

end_unit

