begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|bundle
operator|.
name|core
package|;
end_package

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
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_interface
specifier|public
interface|interface
name|BundleSelector
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|selectBundles
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|,
name|boolean
name|defaultAllBundles
parameter_list|,
name|boolean
name|mayAccessSystemBundle
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

