begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *   */
end_comment

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
name|web
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|osgi
operator|.
name|service
operator|.
name|event
operator|.
name|Event
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|event
operator|.
name|EventHandler
import|;
end_import

begin_comment
comment|/**  * @author Achim  *  */
end_comment

begin_class
specifier|public
class|class
name|WebEventHandler
implements|implements
name|EventHandler
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|Long
argument_list|,
name|String
argument_list|>
name|bundleEvents
init|=
operator|new
name|HashMap
argument_list|<
name|Long
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/* (non-Javadoc) 	 * @see org.osgi.service.event.EventHandler#handleEvent(org.osgi.service.event.Event) 	 */
specifier|public
name|void
name|handleEvent
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|String
name|topic
init|=
name|event
operator|.
name|getTopic
argument_list|()
decl_stmt|;
name|Long
name|bundleID
init|=
operator|(
name|Long
operator|)
name|event
operator|.
name|getProperty
argument_list|(
literal|"bundle.id"
argument_list|)
decl_stmt|;
name|getBundleEvents
argument_list|()
operator|.
name|put
argument_list|(
name|bundleID
argument_list|,
name|topic
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @return the bundleEvents 	 */
specifier|public
name|Map
argument_list|<
name|Long
argument_list|,
name|String
argument_list|>
name|getBundleEvents
parameter_list|()
block|{
return|return
name|bundleEvents
return|;
block|}
block|}
end_class

end_unit

