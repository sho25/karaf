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
name|servicemix
operator|.
name|gshell
operator|.
name|features
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
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|obr
operator|.
name|ObrCommandSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|support
operator|.
name|OsgiCommandSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|gshell
operator|.
name|features
operator|.
name|Feature
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
name|ServiceRegistration
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
name|obr
operator|.
name|RepositoryAdmin
import|;
end_import

begin_comment
comment|/**  * Simple command which prompts the user and installs the needed bundles.  * The bundles need to be available through the OBR repositories.  */
end_comment

begin_class
specifier|public
class|class
name|CommandProxy
extends|extends
name|ObrCommandSupport
block|{
specifier|private
name|Feature
name|feature
decl_stmt|;
specifier|private
name|ServiceRegistration
name|registration
decl_stmt|;
specifier|public
name|CommandProxy
parameter_list|(
name|Feature
name|feature
parameter_list|,
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|feature
operator|=
name|feature
expr_stmt|;
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"shell"
argument_list|,
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"rank"
argument_list|,
literal|"-1"
argument_list|)
expr_stmt|;
name|registration
operator|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|Command
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|this
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|OsgiCommandSupport
name|createCommand
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|feature
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|feature
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|protected
name|void
name|doExecute
parameter_list|(
name|RepositoryAdmin
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"This feature is not yet installed.  Do you want to install it (y/n) ? "
argument_list|)
expr_stmt|;
name|int
name|c
init|=
name|io
operator|.
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'y'
operator|||
name|c
operator|==
literal|'Y'
condition|)
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Installing feature.  Please wait..."
argument_list|)
expr_stmt|;
name|registration
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|deploy
argument_list|(
name|admin
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|deploy
parameter_list|(
name|RepositoryAdmin
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|bundles
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|feature
operator|.
name|getBundles
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|idx0
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
operator|++
name|idx0
operator|<
name|bundles
operator|.
name|size
argument_list|()
condition|)
block|{
if|if
condition|(
name|bundles
operator|.
name|get
argument_list|(
name|idx0
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"obr:"
argument_list|)
condition|)
block|{
name|String
index|[]
name|b
init|=
name|bundles
operator|.
name|get
argument_list|(
name|idx0
argument_list|)
operator|.
name|substring
argument_list|(
literal|"obr:"
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|doDeploy
argument_list|(
name|admin
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|b
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Bundle
name|bundle
init|=
name|getBundleContext
argument_list|()
operator|.
name|installBundle
argument_list|(
name|bundles
operator|.
name|get
argument_list|(
name|idx0
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|bundle
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|readLine
parameter_list|(
name|Reader
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|i
init|=
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|i
operator|==
operator|-
literal|1
operator|)
operator|||
operator|(
name|i
operator|==
literal|'\n'
operator|)
operator|||
operator|(
name|i
operator|==
literal|'\r'
operator|)
condition|)
block|{
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
name|buf
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

