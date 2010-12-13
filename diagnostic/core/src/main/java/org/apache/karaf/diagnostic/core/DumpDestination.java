begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|diagnostic
operator|.
name|core
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_comment
comment|/**  * Destination for created dumps.  *   * @author ldywicki  */
end_comment

begin_interface
specifier|public
interface|interface
name|DumpDestination
block|{
name|OutputStream
name|add
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|save
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

