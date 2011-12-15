begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|$
block|{
package|package
block|}
end_package

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
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

begin_class
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
block|{
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting the bundle"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Stopping the bundle"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

