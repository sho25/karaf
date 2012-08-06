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
package|;
end_package

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
name|BundleEvent
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
name|SynchronousBundleListener
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
comment|/**  * Watches the startup of the framework and displays a progress bar of the number of bundles started / total.  * The listener will remove itself after the desired start level is reached or the system property karaf.console.started is set to   * true.   */
end_comment

begin_class
class|class
name|StartupListener
implements|implements
name|FrameworkListener
implements|,
name|SynchronousBundleListener
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
name|BundleContext
name|context
decl_stmt|;
name|StartupListener
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|context
operator|.
name|addBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|context
operator|.
name|addFrameworkListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|bundleChanged
parameter_list|(
name|BundleEvent
name|bundleEvent
parameter_list|)
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
name|int
name|numActive
init|=
literal|0
decl_stmt|;
name|int
name|numBundles
init|=
name|bundles
operator|.
name|length
decl_stmt|;
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
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|FRAGMENT_HOST
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|numBundles
operator|--
expr_stmt|;
block|}
elseif|else
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
name|numActive
operator|++
expr_stmt|;
block|}
block|}
name|boolean
name|started
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|SYSTEM_PROP_KARAF_CONSOLE_STARTED
argument_list|,
literal|"false"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|started
condition|)
block|{
name|showProgressBar
argument_list|(
name|numActive
argument_list|,
name|numBundles
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|frameworkEvent
parameter_list|(
name|FrameworkEvent
name|frameworkEvent
parameter_list|)
block|{
if|if
condition|(
name|frameworkEvent
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
name|defStartLevel
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
name|context
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
name|defStartLevel
condition|)
block|{
name|context
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeFrameworkListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|showProgressBar
parameter_list|(
name|int
name|done
parameter_list|,
name|int
name|total
parameter_list|)
block|{
name|int
name|percent
init|=
operator|(
name|done
operator|*
literal|100
operator|)
operator|/
name|total
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"\r%3d%% ["
argument_list|,
name|percent
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|<
name|percent
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|==
name|percent
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
name|done
operator|==
name|total
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
block|}
block|}
end_class

end_unit

