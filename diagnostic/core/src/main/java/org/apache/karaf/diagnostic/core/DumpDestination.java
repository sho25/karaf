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

begin_comment
comment|/**  * Destination for created dumps.  *   * @author ldywicki  */
end_comment

begin_interface
specifier|public
interface|interface
name|DumpDestination
block|{
name|void
name|add
parameter_list|(
name|Dump
modifier|...
name|dump
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

