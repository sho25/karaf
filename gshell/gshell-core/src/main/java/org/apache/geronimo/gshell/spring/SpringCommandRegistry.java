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
name|java
operator|.
name|util
operator|.
name|Map
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
name|command
operator|.
name|CommandContext
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
name|common
operator|.
name|Arguments
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
name|NotFoundException
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
name|AliasNode
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
name|CommandNode
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
name|GroupNode
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
name|Layout
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
name|registry
operator|.
name|DefaultCommandRegistry
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
name|registry
operator|.
name|DuplicateRegistrationException
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
name|registry
operator|.
name|NotRegisteredException
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
name|registry
operator|.
name|RegistryException
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

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: gnodet  * Date: Oct 11, 2007  * Time: 3:47:24 PM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
specifier|public
class|class
name|SpringCommandRegistry
extends|extends
name|DefaultCommandRegistry
implements|implements
name|LayoutManager
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SEPARATOR
init|=
literal|":"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ALIAS_PREFIX
init|=
literal|"alias:"
decl_stmt|;
specifier|private
name|Environment
name|env
decl_stmt|;
specifier|private
name|MutableLayout
name|layout
init|=
operator|new
name|MutableLayout
argument_list|()
decl_stmt|;
specifier|public
name|SpringCommandRegistry
parameter_list|(
name|Environment
name|env
parameter_list|)
block|{
name|this
operator|.
name|env
operator|=
name|env
expr_stmt|;
block|}
specifier|public
name|void
name|register
parameter_list|(
specifier|final
name|Command
name|command
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
throws|throws
name|RegistryException
block|{
comment|// Find command name
name|String
name|name
init|=
name|command
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|':'
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|':'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|containsKey
argument_list|(
literal|"name"
argument_list|)
condition|)
block|{
name|name
operator|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
block|}
comment|// Find rank
name|int
name|rank
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|properties
operator|.
name|containsKey
argument_list|(
literal|"rank"
argument_list|)
condition|)
block|{
name|rank
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"rank"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Find or create the subshell group
name|GroupNode
name|gn
init|=
name|layout
decl_stmt|;
name|String
name|shell
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"shell"
argument_list|)
decl_stmt|;
name|String
index|[]
name|aliases
init|=
name|properties
operator|.
name|get
argument_list|(
literal|"alias"
argument_list|)
operator|!=
literal|null
condition|?
name|properties
operator|.
name|get
argument_list|(
literal|"alias"
argument_list|)
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
else|:
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|shell
argument_list|)
condition|)
block|{
name|Node
name|n
init|=
name|gn
operator|.
name|find
argument_list|(
name|shell
argument_list|)
decl_stmt|;
name|MutableGroupNode
name|g
init|=
operator|new
name|MutableGroupNode
argument_list|(
name|shell
argument_list|)
decl_stmt|;
name|gn
operator|.
name|add
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|shell
operator|!=
literal|null
operator|&&
name|shell
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Node
name|n
init|=
name|gn
operator|.
name|find
argument_list|(
name|shell
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|==
literal|null
condition|)
block|{
name|MutableGroupNode
name|g
init|=
operator|new
name|MutableGroupNode
argument_list|(
name|shell
argument_list|)
decl_stmt|;
name|gn
operator|.
name|add
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|register
argument_list|(
operator|new
name|GroupCommand
argument_list|(
name|shell
argument_list|,
name|g
argument_list|)
argument_list|)
expr_stmt|;
name|gn
operator|=
name|g
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|instanceof
name|GroupNode
condition|)
block|{
name|gn
operator|=
operator|(
name|GroupNode
operator|)
name|n
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"A command conflicts has been detected when registering "
operator|+
name|command
operator|.
name|getId
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|CommandNode
name|cn
init|=
operator|new
name|CommandNode
argument_list|(
name|name
argument_list|,
name|command
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|gn
operator|.
name|add
argument_list|(
name|cn
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|aliases
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|aliases
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|AliasNode
name|an
init|=
operator|new
name|AliasNode
argument_list|(
name|aliases
index|[
name|i
index|]
argument_list|,
name|ALIAS_PREFIX
operator|+
name|command
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|gn
operator|.
name|add
argument_list|(
name|an
argument_list|)
expr_stmt|;
block|}
block|}
name|register
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|unregister
parameter_list|(
specifier|final
name|Command
name|command
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
throws|throws
name|RegistryException
block|{
comment|// Find command name
name|String
name|name
init|=
name|command
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|':'
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|':'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|containsKey
argument_list|(
literal|"name"
argument_list|)
condition|)
block|{
name|name
operator|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
block|}
name|String
name|shell
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"shell"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|shell
argument_list|)
operator|||
name|shell
operator|==
literal|null
operator|||
name|shell
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|Node
name|n
init|=
name|layout
operator|.
name|find
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|layout
operator|.
name|removeNode
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MutableGroupNode
name|gn
init|=
operator|(
name|MutableGroupNode
operator|)
name|layout
operator|.
name|find
argument_list|(
name|shell
argument_list|)
decl_stmt|;
name|Node
name|n
init|=
name|gn
operator|.
name|find
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|gn
operator|.
name|removeNode
argument_list|(
name|n
argument_list|)
expr_stmt|;
if|if
condition|(
name|gn
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|layout
operator|.
name|removeNode
argument_list|(
name|gn
argument_list|)
expr_stmt|;
name|unregister
argument_list|(
name|lookup
argument_list|(
name|shell
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|unregister
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Layout
name|getLayout
parameter_list|()
block|{
return|return
name|layout
return|;
block|}
specifier|public
name|Node
name|findNode
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|NotFoundException
block|{
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
name|CURRENT_NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|start
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|findNode
argument_list|(
name|start
argument_list|,
name|s
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NotFoundException
name|e
parameter_list|)
block|{
comment|// Ignore, we need to try at root level
block|}
block|}
return|return
name|findNode
argument_list|(
name|layout
argument_list|,
name|s
argument_list|)
return|;
block|}
specifier|public
name|Node
name|findNode
parameter_list|(
name|Node
name|node
parameter_list|,
name|String
name|s
parameter_list|)
throws|throws
name|NotFoundException
block|{
if|if
condition|(
name|node
operator|instanceof
name|GroupNode
condition|)
block|{
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
name|ALIAS_PREFIX
argument_list|)
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
name|ALIAS_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|Node
name|n
init|=
name|recursiveFind
argument_list|(
operator|(
name|GroupNode
operator|)
name|node
argument_list|,
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|!=
literal|null
condition|)
block|{
return|return
name|n
return|;
block|}
throw|throw
operator|new
name|NotFoundException
argument_list|(
name|s
argument_list|)
throw|;
block|}
name|Node
name|n
init|=
operator|(
operator|(
name|GroupNode
operator|)
name|node
operator|)
operator|.
name|find
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NotFoundException
argument_list|(
name|s
argument_list|)
throw|;
block|}
if|if
condition|(
name|n
operator|instanceof
name|GroupNode
condition|)
block|{
return|return
operator|new
name|CommandNode
argument_list|(
name|n
operator|.
name|getName
argument_list|()
argument_list|,
name|n
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
return|return
name|n
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|NotFoundException
argument_list|(
name|s
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Node
name|recursiveFind
parameter_list|(
name|GroupNode
name|groupNode
parameter_list|,
name|String
name|s
parameter_list|)
block|{
for|for
control|(
name|Node
name|n
range|:
name|groupNode
operator|.
name|nodes
argument_list|()
control|)
block|{
if|if
condition|(
name|n
operator|instanceof
name|CommandNode
operator|&&
operator|(
operator|(
name|CommandNode
operator|)
name|n
operator|)
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|n
return|;
block|}
elseif|else
if|if
condition|(
name|n
operator|instanceof
name|GroupNode
condition|)
block|{
name|Node
name|n2
init|=
name|recursiveFind
argument_list|(
operator|(
name|GroupNode
operator|)
name|n
argument_list|,
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|n2
operator|!=
literal|null
condition|)
block|{
return|return
name|n2
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Node
name|findNode
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|searchPath
parameter_list|)
throws|throws
name|NotFoundException
block|{
return|return
name|findNode
argument_list|(
name|path
argument_list|)
return|;
block|}
specifier|public
class|class
name|GroupCommand
implements|implements
name|Command
block|{
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|GroupNode
name|gn
decl_stmt|;
specifier|public
name|GroupCommand
parameter_list|(
name|String
name|id
parameter_list|,
name|GroupNode
name|gn
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|gn
operator|=
name|gn
expr_stmt|;
block|}
annotation|@
name|Deprecated
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
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
literal|"Group command"
return|;
block|}
specifier|public
name|Object
name|execute
parameter_list|(
name|CommandContext
name|commandContext
parameter_list|,
name|Object
modifier|...
name|objects
parameter_list|)
throws|throws
name|Exception
block|{
name|env
operator|.
name|getVariables
argument_list|()
operator|.
name|set
argument_list|(
name|CURRENT_NODE
argument_list|,
name|gn
argument_list|)
expr_stmt|;
if|if
condition|(
name|objects
operator|.
name|length
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|String
name|cmdId
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|objects
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|Node
name|n
init|=
name|gn
operator|.
name|find
argument_list|(
name|cmdId
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|==
literal|null
condition|)
block|{
name|n
operator|=
name|layout
operator|.
name|find
argument_list|(
name|cmdId
argument_list|)
expr_stmt|;
block|}
name|CommandContext
name|ctx
init|=
name|commandContext
decl_stmt|;
name|Command
name|cmd
decl_stmt|;
if|if
condition|(
name|n
operator|instanceof
name|CommandNode
condition|)
block|{
name|cmd
operator|=
name|lookup
argument_list|(
operator|(
operator|(
name|CommandNode
operator|)
name|n
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|instanceof
name|GroupNode
condition|)
block|{
name|cmd
operator|=
operator|new
name|GroupCommand
argument_list|(
name|cmdId
argument_list|,
operator|(
name|GroupNode
operator|)
name|n
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|instanceof
name|AliasNode
condition|)
block|{
name|cmd
operator|=
name|lookup
argument_list|(
operator|(
operator|(
name|AliasNode
operator|)
name|n
operator|)
operator|.
name|getCommand
argument_list|()
operator|.
name|substring
argument_list|(
name|ALIAS_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unrecognized node type: "
operator|+
name|n
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|cmd
operator|.
name|execute
argument_list|(
name|ctx
argument_list|,
name|Arguments
operator|.
name|shift
argument_list|(
name|objects
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|env
operator|.
name|getVariables
argument_list|()
operator|.
name|unset
argument_list|(
name|CURRENT_NODE
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|SUCCESS
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|MutableLayout
extends|extends
name|Layout
block|{
specifier|public
name|void
name|removeNode
parameter_list|(
name|Node
name|n
parameter_list|)
block|{
name|nodes
operator|.
name|remove
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|MutableGroupNode
extends|extends
name|GroupNode
block|{
specifier|public
name|MutableGroupNode
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeNode
parameter_list|(
name|Node
name|n
parameter_list|)
block|{
name|nodes
operator|.
name|remove
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

