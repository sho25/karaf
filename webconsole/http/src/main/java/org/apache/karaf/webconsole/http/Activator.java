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
name|webconsole
operator|.
name|http
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Servlet
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|tracker
operator|.
name|BaseActivator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|tracker
operator|.
name|annotation
operator|.
name|Services
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|ServletListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|WebListener
import|;
end_import

begin_class
annotation|@
name|Services
specifier|public
class|class
name|Activator
extends|extends
name|BaseActivator
block|{
specifier|private
name|HttpPlugin
name|httpPlugin
decl_stmt|;
specifier|private
name|ServletEventHandler
name|eaHandler
decl_stmt|;
specifier|private
name|WebEventHandler
name|webEaHandler
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|eaHandler
operator|=
operator|new
name|ServletEventHandler
argument_list|()
expr_stmt|;
name|eaHandler
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|eaHandler
operator|.
name|init
argument_list|()
expr_stmt|;
name|register
argument_list|(
name|ServletListener
operator|.
name|class
argument_list|,
name|eaHandler
argument_list|)
expr_stmt|;
name|webEaHandler
operator|=
operator|new
name|WebEventHandler
argument_list|()
expr_stmt|;
name|webEaHandler
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|webEaHandler
operator|.
name|init
argument_list|()
expr_stmt|;
name|register
argument_list|(
name|WebListener
operator|.
name|class
argument_list|,
name|webEaHandler
argument_list|)
expr_stmt|;
name|httpPlugin
operator|=
operator|new
name|HttpPlugin
argument_list|()
expr_stmt|;
name|httpPlugin
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|httpPlugin
operator|.
name|setServletEventHandler
argument_list|(
name|eaHandler
argument_list|)
expr_stmt|;
name|httpPlugin
operator|.
name|setWebEventHandler
argument_list|(
name|webEaHandler
argument_list|)
expr_stmt|;
name|httpPlugin
operator|.
name|start
argument_list|()
expr_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
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
literal|"felix.webconsole.label"
argument_list|,
literal|"http"
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|Servlet
operator|.
name|class
argument_list|,
name|httpPlugin
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|httpPlugin
operator|!=
literal|null
condition|)
block|{
name|httpPlugin
operator|.
name|stop
argument_list|()
expr_stmt|;
name|httpPlugin
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|eaHandler
operator|!=
literal|null
condition|)
block|{
name|eaHandler
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|eaHandler
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|webEaHandler
operator|!=
literal|null
condition|)
block|{
name|webEaHandler
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|webEaHandler
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

