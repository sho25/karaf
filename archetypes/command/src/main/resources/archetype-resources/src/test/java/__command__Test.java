begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_expr_stmt
unit|#
name|set
argument_list|(
name|$symbol_pound
operator|=
literal|'#'
argument_list|)
expr|#
name|set
argument_list|(
name|$symbol_dollar
operator|=
literal|'$'
argument_list|)
expr|#
name|set
argument_list|(
name|$symbol_escape
operator|=
literal|'\' )
package|package
name|$
block|{
package|package
block|}
end_expr_stmt

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Test cases for {@link ${command}}  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
class|class
name|$
block|{
name|command
block|}
end_class

begin_expr_stmt
name|Test
expr|extends
name|TestCase
block|{
specifier|private
name|$
block|{
name|command
block|}
name|command
block|;      @
name|Override
specifier|protected
name|void
name|setUp
argument_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
block|;     }
expr|@
name|Override
specifier|protected
name|void
name|tearDown
argument_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
end_expr_stmt

begin_function
unit|}      	public
name|void
name|testCommand
parameter_list|()
block|{  	}
end_function

unit|}
end_unit

