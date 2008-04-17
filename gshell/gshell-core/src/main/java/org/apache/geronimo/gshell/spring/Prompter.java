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
name|geronimo
operator|.
name|gshell
operator|.
name|spring
package|;
end_package

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
name|ansi
operator|.
name|Renderer
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
name|branding
operator|.
name|Branding
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
name|console
operator|.
name|Console
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
name|layout
operator|.
name|LayoutManager
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
name|layout
operator|.
name|model
operator|.
name|Node
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
name|shell
operator|.
name|Environment
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
name|shell
operator|.
name|ShellInfo
import|;
end_import

begin_comment
comment|/**  * A prompter that displays the current sub-shell.  */
end_comment

begin_class
specifier|public
class|class
name|Prompter
implements|implements
name|Console
operator|.
name|Prompter
block|{
specifier|private
name|Renderer
name|renderer
init|=
operator|new
name|Renderer
argument_list|()
decl_stmt|;
specifier|private
name|ShellInfo
name|shellInfo
decl_stmt|;
specifier|private
name|Environment
name|env
decl_stmt|;
specifier|private
name|Branding
name|branding
decl_stmt|;
specifier|public
name|Prompter
parameter_list|(
name|ShellInfo
name|shellInfo
parameter_list|,
name|Environment
name|env
parameter_list|)
block|{
name|this
operator|.
name|shellInfo
operator|=
name|shellInfo
expr_stmt|;
name|this
operator|.
name|env
operator|=
name|env
expr_stmt|;
block|}
specifier|public
name|Branding
name|getBranding
parameter_list|()
block|{
return|return
name|branding
return|;
block|}
specifier|public
name|void
name|setBranding
parameter_list|(
name|Branding
name|branding
parameter_list|)
block|{
name|this
operator|.
name|branding
operator|=
name|branding
expr_stmt|;
block|}
specifier|public
name|String
name|prompt
parameter_list|()
block|{
name|String
name|userName
init|=
name|shellInfo
operator|.
name|getUserName
argument_list|()
decl_stmt|;
name|String
name|hostName
init|=
name|shellInfo
operator|.
name|getLocalHost
argument_list|()
operator|.
name|getHostName
argument_list|()
decl_stmt|;
name|Node
name|start
init|=
operator|(
name|Node
operator|)
name|env
operator|.
name|getVariables
argument_list|()
operator|.
name|get
argument_list|(
name|LayoutManager
operator|.
name|CURRENT_NODE
argument_list|)
decl_stmt|;
name|String
name|path
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|start
operator|!=
literal|null
condition|)
block|{
name|path
operator|=
name|start
operator|.
name|getPath
argument_list|()
expr_stmt|;
name|path
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|' '
argument_list|)
expr_stmt|;
block|}
comment|// return renderer.render("@|bold " + userName + "|@" + hostName + ":@|bold " + path + "|> ");
comment|// I think a simpler prompt would be best.
return|return
name|renderer
operator|.
name|render
argument_list|(
literal|"@|bold "
operator|+
name|branding
operator|.
name|getName
argument_list|()
operator|+
name|path
operator|+
literal|"|> "
argument_list|)
return|;
block|}
block|}
end_class

end_unit

