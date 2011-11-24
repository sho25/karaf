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
name|shell
operator|.
name|bundles
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|commands
operator|.
name|Argument
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
name|shell
operator|.
name|commands
operator|.
name|Command
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
name|ServiceReference
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"bundle"
argument_list|,
name|name
operator|=
literal|"start-level"
argument_list|,
name|description
operator|=
literal|"Gets or sets the start level of a bundle."
argument_list|)
specifier|public
class|class
name|StartLevel
extends|extends
name|BundleCommand
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"startLevel"
argument_list|,
name|description
operator|=
literal|"The bundle's new start level"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|Integer
name|level
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Get package admin service.
name|ServiceReference
name|ref
init|=
name|getBundleContext
argument_list|()
operator|.
name|getServiceReference
argument_list|(
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|startlevel
operator|.
name|StartLevel
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"StartLevel service is unavailable."
argument_list|)
expr_stmt|;
return|return;
block|}
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|startlevel
operator|.
name|StartLevel
name|sl
init|=
name|getService
argument_list|(
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|startlevel
operator|.
name|StartLevel
operator|.
name|class
argument_list|,
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|sl
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"StartLevel service is unavailable."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|level
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Level "
operator|+
name|sl
operator|.
name|getBundleStartLevel
argument_list|(
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|level
operator|<
literal|50
operator|)
operator|&&
name|sl
operator|.
name|getBundleStartLevel
argument_list|(
name|bundle
argument_list|)
operator|>
literal|50
condition|)
block|{
for|for
control|(
init|;
condition|;
control|)
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"You are about to designate bundle as a system bundle.  Do you wish to continue (yes/no): "
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|int
name|c
init|=
name|System
operator|.
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
return|return;
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\r'
operator|||
name|c
operator|==
literal|'\n'
condition|)
block|{
break|break;
block|}
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
name|String
name|str
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"yes"
operator|.
name|equals
argument_list|(
name|str
argument_list|)
condition|)
block|{
name|sl
operator|.
name|setBundleStartLevel
argument_list|(
name|bundle
argument_list|,
name|level
argument_list|)
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
literal|"no"
operator|.
name|equals
argument_list|(
name|str
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
block|}
else|else
block|{
name|sl
operator|.
name|setBundleStartLevel
argument_list|(
name|bundle
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

