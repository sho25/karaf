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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|FrameworkEvent
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
name|FrameworkListener
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

begin_comment
comment|/**  * Delay the start of the console until the desired start level is reached or enter is pressed  */
end_comment

begin_class
class|class
name|DelayedStarted
extends|extends
name|Thread
implements|implements
name|FrameworkListener
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SYSTEM_PROP_KARAF_CONSOLE_STARTED
init|=
literal|"karaf.console.started"
decl_stmt|;
specifier|private
specifier|final
name|AtomicBoolean
name|started
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|InputStream
name|in
decl_stmt|;
specifier|private
specifier|final
name|Runnable
name|console
decl_stmt|;
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
name|DelayedStarted
parameter_list|(
name|Runnable
name|console
parameter_list|,
name|BundleContext
name|bundleContext
parameter_list|,
name|InputStream
name|in
parameter_list|)
block|{
name|super
argument_list|(
literal|"Karaf Shell Console Thread"
argument_list|)
expr_stmt|;
name|this
operator|.
name|console
operator|=
name|console
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
name|int
name|defaultStartLevel
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|FRAMEWORK_BEGINNING_STARTLEVEL
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|startLevel
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
operator|.
name|getStartLevel
argument_list|()
decl_stmt|;
if|if
condition|(
name|startLevel
operator|>=
name|defaultStartLevel
condition|)
block|{
name|started
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bundleContext
operator|.
name|addFrameworkListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|frameworkEvent
argument_list|(
operator|new
name|FrameworkEvent
argument_list|(
name|FrameworkEvent
operator|.
name|STARTLEVEL_CHANGED
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
while|while
condition|(
operator|!
name|started
operator|.
name|get
argument_list|()
condition|)
block|{
if|if
condition|(
name|in
operator|.
name|available
argument_list|()
operator|==
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
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
operator|||
name|ch
operator|==
literal|'\n'
condition|)
block|{
name|started
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
comment|// Signal to the main module that it can stop displaying the startup progress
name|System
operator|.
name|setProperty
argument_list|(
name|SYSTEM_PROP_KARAF_CONSOLE_STARTED
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|.
name|removeFrameworkListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|console
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|frameworkEvent
parameter_list|(
name|FrameworkEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|FrameworkEvent
operator|.
name|STARTLEVEL_CHANGED
condition|)
block|{
name|int
name|defaultStartLevel
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|FRAMEWORK_BEGINNING_STARTLEVEL
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|startLevel
init|=
name|this
operator|.
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
operator|.
name|getStartLevel
argument_list|()
decl_stmt|;
if|if
condition|(
name|startLevel
operator|>=
name|defaultStartLevel
condition|)
block|{
name|started
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

