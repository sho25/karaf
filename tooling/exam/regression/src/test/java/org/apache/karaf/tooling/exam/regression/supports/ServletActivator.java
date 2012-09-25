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
name|tooling
operator|.
name|exam
operator|.
name|regression
operator|.
name|supports
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
name|http
operator|.
name|HttpContext
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
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
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
name|ServletActivator
implements|implements
name|BundleActivator
block|{
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ServletActivator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|HttpServiceTracker
name|tracker
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|bc
parameter_list|)
throws|throws
name|Exception
block|{
name|tracker
operator|=
operator|new
name|HttpServiceTracker
argument_list|(
name|bc
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|bc
parameter_list|)
throws|throws
name|Exception
block|{
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
class|class
name|HttpServiceTracker
extends|extends
name|ServiceTracker
argument_list|<
name|HttpService
argument_list|,
name|HttpService
argument_list|>
block|{
specifier|public
name|HttpServiceTracker
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|HttpService
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|HttpService
name|addingService
parameter_list|(
name|ServiceReference
argument_list|<
name|HttpService
argument_list|>
name|reference
parameter_list|)
block|{
name|HttpService
name|httpService
init|=
name|context
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
specifier|final
name|HttpContext
name|httpContext
init|=
name|httpService
operator|.
name|createDefaultHttpContext
argument_list|()
decl_stmt|;
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|initParams
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|initParams
operator|.
name|put
argument_list|(
literal|"servlet-name"
argument_list|,
literal|"TestServlet"
argument_list|)
expr_stmt|;
try|try
block|{
name|httpService
operator|.
name|registerServlet
argument_list|(
name|EchoServlet
operator|.
name|ALIAS
argument_list|,
operator|new
name|EchoServlet
argument_list|()
argument_list|,
name|initParams
argument_list|,
name|httpContext
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Servlet registered successfully"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|addingService
argument_list|(
name|reference
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removedService
parameter_list|(
name|ServiceReference
argument_list|<
name|HttpService
argument_list|>
name|reference
parameter_list|,
name|HttpService
name|service
parameter_list|)
block|{
name|HttpService
name|httpService
init|=
name|context
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|httpService
operator|.
name|unregister
argument_list|(
name|EchoServlet
operator|.
name|ALIAS
argument_list|)
expr_stmt|;
name|super
operator|.
name|removedService
argument_list|(
name|reference
argument_list|,
name|service
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

