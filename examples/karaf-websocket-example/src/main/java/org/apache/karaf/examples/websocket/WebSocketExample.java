begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|examples
operator|.
name|websocket
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|api
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|api
operator|.
name|annotations
operator|.
name|OnWebSocketClose
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|api
operator|.
name|annotations
operator|.
name|OnWebSocketConnect
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|api
operator|.
name|annotations
operator|.
name|WebSocket
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
name|component
operator|.
name|annotations
operator|.
name|Activate
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
name|component
operator|.
name|annotations
operator|.
name|Component
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
name|component
operator|.
name|annotations
operator|.
name|Deactivate
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
name|component
operator|.
name|annotations
operator|.
name|Reference
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
name|http
operator|.
name|HttpService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
name|name
operator|=
literal|"example-websocket"
argument_list|,
name|immediate
operator|=
literal|true
argument_list|)
annotation|@
name|WebSocket
specifier|public
class|class
name|WebSocketExample
block|{
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|Session
argument_list|>
name|sessions
init|=
name|Collections
operator|.
name|synchronizedSet
argument_list|(
operator|new
name|HashSet
argument_list|<
name|Session
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|notification
init|=
literal|false
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|HttpService
name|httpService
decl_stmt|;
annotation|@
name|OnWebSocketConnect
specifier|public
name|void
name|onOpen
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|session
operator|.
name|setIdleTimeout
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|sessions
operator|.
name|add
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
annotation|@
name|OnWebSocketClose
specifier|public
name|void
name|onClose
parameter_list|(
name|Session
name|session
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|sessions
operator|.
name|remove
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Activate
specifier|public
name|void
name|activate
parameter_list|()
throws|throws
name|Exception
block|{
name|httpService
operator|.
name|registerServlet
argument_list|(
literal|"/example-websocket"
argument_list|,
operator|new
name|WebsocketExampleServlet
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|notification
operator|=
literal|true
expr_stmt|;
name|Thread
name|notification
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|NotificationThread
argument_list|(
name|sessions
argument_list|)
argument_list|)
decl_stmt|;
name|notification
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Deactivate
specifier|public
name|void
name|deactivate
parameter_list|()
throws|throws
name|Exception
block|{
name|httpService
operator|.
name|unregister
argument_list|(
literal|"/example-websocket"
argument_list|)
expr_stmt|;
name|notification
operator|=
literal|false
expr_stmt|;
block|}
class|class
name|NotificationThread
implements|implements
name|Runnable
block|{
specifier|private
name|Set
argument_list|<
name|Session
argument_list|>
name|sessions
decl_stmt|;
specifier|public
name|NotificationThread
parameter_list|(
name|Set
argument_list|<
name|Session
argument_list|>
name|sessions
parameter_list|)
block|{
name|this
operator|.
name|sessions
operator|=
name|sessions
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
while|while
condition|(
name|notification
condition|)
block|{
for|for
control|(
name|Session
name|session
range|:
name|sessions
control|)
block|{
name|session
operator|.
name|getRemote
argument_list|()
operator|.
name|sendString
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

