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
name|services
operator|.
name|mavenproxy
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|Managed
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
name|RequireService
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
name|url
operator|.
name|mvn
operator|.
name|MavenResolver
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
name|url
operator|.
name|mvn
operator|.
name|MavenResolvers
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
name|cm
operator|.
name|ManagedService
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

begin_class
annotation|@
name|Services
argument_list|(
name|requires
operator|=
annotation|@
name|RequireService
argument_list|(
name|HttpService
operator|.
name|class
argument_list|)
argument_list|)
annotation|@
name|Managed
argument_list|(
literal|"org.apache.karaf.services.mavenproxy"
argument_list|)
specifier|public
class|class
name|Activator
extends|extends
name|BaseActivator
implements|implements
name|ManagedService
block|{
specifier|private
name|HttpService
name|httpService
decl_stmt|;
specifier|private
name|String
name|alias
decl_stmt|;
specifier|private
name|MavenResolver
name|resolver
decl_stmt|;
specifier|private
name|MavenProxyServlet
name|servlet
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
name|httpService
operator|=
name|getTrackedService
argument_list|(
name|HttpService
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|httpService
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
name|pid
init|=
name|getString
argument_list|(
literal|"maven.pid"
argument_list|,
literal|"org.ops4j.pax.url.mvn"
argument_list|)
decl_stmt|;
name|String
name|alias
init|=
name|getString
argument_list|(
literal|"maven.alias"
argument_list|,
literal|"/mavenproxy"
argument_list|)
decl_stmt|;
name|String
name|realm
init|=
name|getString
argument_list|(
literal|"maven.realm"
argument_list|,
literal|"karaf"
argument_list|)
decl_stmt|;
name|String
name|downloadRole
init|=
name|getString
argument_list|(
literal|"maven.downloadRole"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|uploadRole
init|=
name|getString
argument_list|(
literal|"maven.uploadRole"
argument_list|,
literal|"karaf"
argument_list|)
decl_stmt|;
name|int
name|poolSize
init|=
name|getInt
argument_list|(
literal|"maven.poolSize"
argument_list|,
literal|8
argument_list|)
decl_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|config
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|e
init|=
name|getConfiguration
argument_list|()
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|val
init|=
name|getConfiguration
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|resolver
operator|=
name|MavenResolvers
operator|.
name|createMavenResolver
argument_list|(
literal|null
argument_list|,
name|config
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
name|this
operator|.
name|servlet
operator|=
operator|new
name|MavenProxyServlet
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
name|poolSize
argument_list|,
name|realm
argument_list|,
name|downloadRole
argument_list|,
name|uploadRole
argument_list|)
expr_stmt|;
name|this
operator|.
name|httpService
operator|.
name|registerServlet
argument_list|(
name|this
operator|.
name|alias
argument_list|,
name|this
operator|.
name|servlet
argument_list|,
name|config
argument_list|,
literal|null
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
name|httpService
operator|!=
literal|null
condition|)
block|{
name|httpService
operator|.
name|unregister
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|servlet
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|servlet
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|resolver
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|resolver
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
block|}
end_class

end_unit
