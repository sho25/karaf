begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|main
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_interface
specifier|public
interface|interface
name|ArtifactResolver
block|{
name|URI
name|resolve
parameter_list|(
name|URI
name|artifactUri
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

