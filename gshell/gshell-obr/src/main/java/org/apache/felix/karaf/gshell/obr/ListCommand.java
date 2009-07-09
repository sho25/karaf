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
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|obr
package|;
end_package

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
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Version
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
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Argument
import|;
end_import

begin_class
specifier|public
class|class
name|ListCommand
extends|extends
name|ObrCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|args
decl_stmt|;
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
name|String
name|substr
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|args
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|arg
range|:
name|args
control|)
block|{
comment|// Add a space in between tokens.
if|if
condition|(
name|substr
operator|==
literal|null
condition|)
block|{
name|substr
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|substr
operator|+=
literal|" "
expr_stmt|;
block|}
name|substr
operator|+=
name|arg
expr_stmt|;
block|}
block|}
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|substr
operator|==
literal|null
operator|)
operator|||
operator|(
name|substr
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"(|(presentationname=*)(symbolicname=*))"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"(|(presentationname=*"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|substr
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"*)(symbolicname=*"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|substr
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"*))"
argument_list|)
expr_stmt|;
block|}
name|Resource
index|[]
name|resources
init|=
name|admin
operator|.
name|discoverResources
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|resIdx
init|=
literal|0
init|;
operator|(
name|resources
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|resIdx
operator|<
name|resources
operator|.
name|length
operator|)
condition|;
name|resIdx
operator|++
control|)
block|{
name|String
name|name
init|=
name|resources
index|[
name|resIdx
index|]
operator|.
name|getPresentationName
argument_list|()
decl_stmt|;
name|Version
name|version
init|=
name|resources
index|[
name|resIdx
index|]
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|name
operator|+
literal|" ("
operator|+
name|version
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|resources
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
literal|"No matching bundles."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

