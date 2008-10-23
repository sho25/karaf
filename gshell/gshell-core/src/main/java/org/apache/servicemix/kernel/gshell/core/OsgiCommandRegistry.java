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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|registry
operator|.
name|CommandRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_class
specifier|public
class|class
name|OsgiCommandRegistry
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
specifier|private
name|CommandRegistry
name|commandRegistry
decl_stmt|;
specifier|public
name|OsgiCommandRegistry
parameter_list|(
name|CommandRegistry
name|commandRegistry
parameter_list|)
block|{
name|this
operator|.
name|commandRegistry
operator|=
name|commandRegistry
expr_stmt|;
block|}
specifier|public
name|void
name|register
parameter_list|(
specifier|final
name|Command
name|command
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
name|commandRegistry
operator|.
name|registerCommand
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|unregister
parameter_list|(
specifier|final
name|Command
name|command
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
name|commandRegistry
operator|.
name|removeCommand
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

