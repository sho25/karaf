begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|impl
operator|.
name|jline
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
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
name|Constants
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
name|startlevel
operator|.
name|FrameworkStartLevel
import|;
end_import

begin_class
specifier|public
class|class
name|BundleWatcher
implements|implements
name|Runnable
block|{
specifier|private
specifier|final
name|BundleContext
name|context
decl_stmt|;
specifier|private
specifier|final
name|Runnable
name|consoleStartCallBack
decl_stmt|;
specifier|private
specifier|final
name|PrintStream
name|out
decl_stmt|;
specifier|private
specifier|final
name|int
name|defaultStartLevel
decl_stmt|;
specifier|public
name|BundleWatcher
parameter_list|(
name|BundleContext
name|context
parameter_list|,
name|int
name|defaultStartLevel
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|Runnable
name|consoleStartCallBack
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|defaultStartLevel
operator|=
name|defaultStartLevel
expr_stmt|;
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|this
operator|.
name|consoleStartCallBack
operator|=
name|consoleStartCallBack
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|boolean
name|startConsole
init|=
literal|false
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"Apache Karaf starting up. Press Enter to start the shell now ..."
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|startConsole
condition|)
block|{
name|BundleStats
name|stats
init|=
name|getBundleStats
argument_list|()
decl_stmt|;
name|stats
operator|.
name|print
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|//out.print(Ansi.ansi().cursorUp(1).toString());
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
if|if
condition|(
name|System
operator|.
name|in
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
name|char
name|ch
init|=
operator|(
name|char
operator|)
name|System
operator|.
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|ch
operator|==
literal|'\r'
condition|)
block|{
name|startConsole
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{             }
if|if
condition|(
name|stats
operator|.
name|startLevel
operator|==
name|defaultStartLevel
condition|)
block|{
name|startConsole
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|BundleStats
name|stats
init|=
name|getBundleStats
argument_list|()
decl_stmt|;
name|stats
operator|.
name|print
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|consoleStartCallBack
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
specifier|private
name|BundleStats
name|getBundleStats
parameter_list|()
block|{
name|Bundle
index|[]
name|bundles
init|=
name|context
operator|.
name|getBundles
argument_list|()
decl_stmt|;
name|BundleStats
name|stats
init|=
operator|new
name|BundleStats
argument_list|()
decl_stmt|;
name|stats
operator|.
name|numTotal
operator|=
name|bundles
operator|.
name|length
expr_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
if|if
condition|(
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|ACTIVE
condition|)
block|{
name|stats
operator|.
name|numActive
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|RESOLVED
condition|)
block|{
name|stats
operator|.
name|numResolved
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|INSTALLED
condition|)
block|{
name|stats
operator|.
name|numInstalled
operator|++
expr_stmt|;
block|}
block|}
name|Bundle
name|frameworkBundle
init|=
name|context
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|FrameworkStartLevel
name|fsl
init|=
name|frameworkBundle
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
decl_stmt|;
name|stats
operator|.
name|startLevel
operator|=
name|fsl
operator|.
name|getStartLevel
argument_list|()
expr_stmt|;
return|return
name|stats
return|;
block|}
class|class
name|BundleStats
block|{
name|int
name|numResolved
init|=
literal|0
decl_stmt|;
name|int
name|numActive
init|=
literal|0
decl_stmt|;
name|int
name|numInstalled
init|=
literal|0
decl_stmt|;
name|int
name|numTotal
init|=
literal|0
decl_stmt|;
name|int
name|startLevel
init|=
literal|0
decl_stmt|;
specifier|public
name|void
name|print
parameter_list|(
name|PrintStream
name|out
parameter_list|)
block|{
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Bundles - total: %d, active: %d, resolved: %d, installed: %d, startlevel: %d         "
argument_list|,
name|numTotal
argument_list|,
name|numActive
argument_list|,
name|numResolved
argument_list|,
name|numInstalled
argument_list|,
name|startLevel
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

