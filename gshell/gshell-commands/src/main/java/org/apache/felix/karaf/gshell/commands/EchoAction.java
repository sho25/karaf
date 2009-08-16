begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|commands
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
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
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
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Option
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
name|gogo
operator|.
name|commands
operator|.
name|Argument
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
name|karaf
operator|.
name|gshell
operator|.
name|console
operator|.
name|OsgiCommandSupport
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"shell"
argument_list|,
name|name
operator|=
literal|"echo"
argument_list|,
name|description
operator|=
literal|"Echo or print arguments to STDOUT"
argument_list|)
specifier|public
class|class
name|EchoAction
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-n"
argument_list|,
name|description
operator|=
literal|"Do not print the trailing newline character"
argument_list|)
specifier|private
name|boolean
name|noTrailingNewline
init|=
literal|false
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"Arguments"
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|args
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|!=
literal|null
condition|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|arg
range|:
name|args
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|noTrailingNewline
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

