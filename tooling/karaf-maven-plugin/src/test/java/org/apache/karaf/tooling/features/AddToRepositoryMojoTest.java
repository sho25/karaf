begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|features
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
name|tooling
operator|.
name|features
operator|.
name|model
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|factory
operator|.
name|DefaultArtifactFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|handler
operator|.
name|manager
operator|.
name|ArtifactHandlerManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|handler
operator|.
name|manager
operator|.
name|DefaultArtifactHandlerManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoFailureException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|util
operator|.
name|HashMap
import|;
end_import

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: heathkesler  * Date: 8/14/12  * Time: 9:39 AM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
specifier|public
class|class
name|AddToRepositoryMojoTest
extends|extends
name|AddToRepositoryMojo
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|AddToRepositoryMojoTest
parameter_list|()
throws|throws
name|NoSuchFieldException
throws|,
name|IllegalAccessException
block|{
name|factory
operator|=
operator|new
name|DefaultArtifactFactory
argument_list|()
expr_stmt|;
name|ArtifactHandlerManager
name|artifactHandlerManager
init|=
operator|new
name|DefaultArtifactHandlerManager
argument_list|()
decl_stmt|;
name|Field
name|f
init|=
name|factory
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"artifactHandlerManager"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
name|factory
argument_list|,
name|artifactHandlerManager
argument_list|)
expr_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|f
operator|=
name|artifactHandlerManager
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"artifactHandlers"
argument_list|)
expr_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
name|artifactHandlerManager
argument_list|,
operator|new
name|HashMap
argument_list|()
argument_list|)
expr_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{     }
annotation|@
name|Test
specifier|public
name|void
name|testSimpleURL
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|in
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"input-repository.xml"
argument_list|)
decl_stmt|;
name|Repository
name|repo
init|=
operator|new
name|Repository
argument_list|(
name|in
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|repos
init|=
name|repo
operator|.
name|getDefinedRepositories
argument_list|()
decl_stmt|;
assert|assert
name|repos
operator|.
name|length
operator|==
literal|1
assert|;
assert|assert
name|repos
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
literal|"http://foo.org"
argument_list|)
assert|;
block|}
block|}
end_class

end_unit

