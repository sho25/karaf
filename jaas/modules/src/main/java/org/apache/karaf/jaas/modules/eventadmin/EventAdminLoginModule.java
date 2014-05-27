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
name|eventadmin
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|Callback
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|NameCallback
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
name|login
operator|.
name|LoginException
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
name|spi
operator|.
name|LoginModule
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

begin_class
specifier|public
class|class
name|EventAdminLoginModule
implements|implements
name|LoginModule
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_EVENTS
init|=
literal|"org/apache/karaf/jaas"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_LOGIN
init|=
name|TOPIC_EVENTS
operator|+
literal|"/LOGIN"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_SUCCESS
init|=
name|TOPIC_EVENTS
operator|+
literal|"/SUCCESS"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_FAILURE
init|=
name|TOPIC_EVENTS
operator|+
literal|"/FAILURE"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_LOGOUT
init|=
name|TOPIC_EVENTS
operator|+
literal|"/LOGOUT"
decl_stmt|;
specifier|private
name|Subject
name|subject
decl_stmt|;
specifier|private
name|CallbackHandler
name|handler
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
decl_stmt|;
specifier|private
name|String
name|username
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|CallbackHandler
name|handler
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
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
name|this
operator|.
name|handler
operator|=
name|handler
expr_stmt|;
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
name|this
operator|.
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
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|login
parameter_list|()
throws|throws
name|LoginException
block|{
name|NameCallback
name|user
init|=
operator|new
name|NameCallback
argument_list|(
literal|"User name:"
argument_list|)
decl_stmt|;
name|Callback
index|[]
name|callbacks
init|=
operator|new
name|Callback
index|[]
block|{
name|user
block|}
decl_stmt|;
try|try
block|{
name|handler
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|LoginException
operator|)
operator|new
name|LoginException
argument_list|(
literal|"Unable to process callback: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|callbacks
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Number of callbacks changed by server!"
argument_list|)
throw|;
block|}
name|user
operator|=
operator|(
name|NameCallback
operator|)
name|callbacks
index|[
literal|0
index|]
expr_stmt|;
name|username
operator|=
name|user
operator|.
name|getName
argument_list|()
expr_stmt|;
name|sendEvent
argument_list|(
name|TOPIC_LOGIN
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|commit
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|sendEvent
argument_list|(
name|TOPIC_SUCCESS
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|abort
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
comment|//work around initial "fake" login
name|sendEvent
argument_list|(
name|TOPIC_FAILURE
argument_list|)
expr_stmt|;
name|username
operator|=
literal|null
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|logout
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|sendEvent
argument_list|(
name|TOPIC_LOGOUT
argument_list|)
expr_stmt|;
name|username
operator|=
literal|null
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|sendEvent
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
if|if
condition|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
literal|"eventadmin.enabled"
argument_list|)
argument_list|)
condition|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
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
try|try
block|{
name|Inner
operator|.
name|send
argument_list|(
name|bundleContext
argument_list|,
name|topic
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
specifier|static
class|class
name|Inner
block|{
specifier|public
specifier|static
name|void
name|send
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|String
name|topic
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
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
name|admin
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
name|admin
operator|.
name|sendEvent
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

