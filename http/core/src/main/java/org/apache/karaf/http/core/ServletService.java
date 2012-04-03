begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|http
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

begin_interface
specifier|public
interface|interface
name|ServletService
block|{
name|List
argument_list|<
name|ServletInfo
argument_list|>
name|getServlets
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

