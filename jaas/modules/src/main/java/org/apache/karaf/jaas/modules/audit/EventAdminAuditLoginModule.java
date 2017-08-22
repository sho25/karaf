begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|audit
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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
name|ServiceReference
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
name|EventAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|EventAdminAuditLoginModule
extends|extends
name|AbstractAuditLoginModule
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_EVENTS
init|=
literal|"org/apache/karaf/login/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EventAdminAuditLoginModule
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|errorLogged
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|String
name|topic
decl_stmt|;
specifier|public
name|void
name|initialize
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|sharedState
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
parameter_list|)
block|{
name|super
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
name|callbackHandler
argument_list|,
name|sharedState
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|bundleContext
operator|=
operator|(
name|BundleContext
operator|)
name|options
operator|.
name|get
argument_list|(
name|BundleContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|topic
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
literal|"topic"
argument_list|)
expr_stmt|;
if|if
condition|(
name|topic
operator|==
literal|null
condition|)
block|{
name|topic
operator|=
name|TOPIC_EVENTS
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|topic
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|topic
operator|+=
literal|"/"
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|audit
parameter_list|(
name|Action
name|action
parameter_list|,
name|String
name|user
parameter_list|)
block|{
try|try
block|{
name|EventAdminAuditor
operator|.
name|audit
argument_list|(
name|bundleContext
argument_list|,
name|topic
operator|+
name|action
operator|.
name|toString
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|,
name|user
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
operator|!
name|errorLogged
condition|)
block|{
name|errorLogged
operator|=
literal|true
expr_stmt|;
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Unable to send security auditing EventAdmin events: "
operator|+
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|static
class|class
name|EventAdminAuditor
block|{
specifier|public
specifier|static
name|void
name|audit
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|String
name|topic
parameter_list|,
name|String
name|username
parameter_list|,
name|Subject
name|subject
parameter_list|)
block|{
name|ServiceReference
argument_list|<
name|EventAdmin
argument_list|>
name|ref
init|=
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|EventAdmin
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|EventAdmin
name|eventAdmin
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
name|topic
operator|.
name|substring
argument_list|(
name|topic
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
operator|+
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"timestamp"
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"subject"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|eventAdmin
operator|.
name|postEvent
argument_list|(
operator|new
name|Event
argument_list|(
name|topic
argument_list|,
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

