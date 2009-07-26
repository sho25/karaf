begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
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
name|console
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|Set
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|Action
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
name|Command
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
name|Option
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
name|basic
operator|.
name|AbstractCommand
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
name|basic
operator|.
name|ActionPreparator
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
name|basic
operator|.
name|DefaultActionPreparator
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
name|karaf
operator|.
name|gshell
operator|.
name|console
operator|.
name|BlueprintContainerAware
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
name|karaf
operator|.
name|gshell
operator|.
name|console
operator|.
name|BundleContextAware
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
name|karaf
operator|.
name|gshell
operator|.
name|console
operator|.
name|CompletableFunction
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
name|karaf
operator|.
name|gshell
operator|.
name|console
operator|.
name|Completer
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
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
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
name|blueprint
operator|.
name|container
operator|.
name|Converter
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
name|command
operator|.
name|CommandSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
import|;
end_import

begin_class
specifier|public
class|class
name|BlueprintCommand
extends|extends
name|AbstractCommand
implements|implements
name|CompletableFunction
block|{
specifier|protected
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
specifier|protected
name|Converter
name|blueprintConverter
decl_stmt|;
specifier|protected
name|String
name|actionId
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
decl_stmt|;
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
specifier|public
name|void
name|setBlueprintConverter
parameter_list|(
name|Converter
name|blueprintConverter
parameter_list|)
block|{
name|this
operator|.
name|blueprintConverter
operator|=
name|blueprintConverter
expr_stmt|;
block|}
specifier|public
name|void
name|setActionId
parameter_list|(
name|String
name|actionId
parameter_list|)
block|{
name|this
operator|.
name|actionId
operator|=
name|actionId
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Completer
argument_list|>
name|getCompleters
parameter_list|()
block|{
return|return
name|completers
return|;
block|}
specifier|public
name|void
name|setCompleters
parameter_list|(
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
parameter_list|)
block|{
name|this
operator|.
name|completers
operator|=
name|completers
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ActionPreparator
name|getPreparator
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|BlueprintActionPreparator
argument_list|()
return|;
block|}
class|class
name|BlueprintActionPreparator
extends|extends
name|DefaultActionPreparator
block|{
annotation|@
name|Override
specifier|protected
name|Object
name|convert
parameter_list|(
name|Action
name|action
parameter_list|,
name|CommandSession
name|commandSession
parameter_list|,
name|Object
name|o
parameter_list|,
name|Type
name|type
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|blueprintConverter
operator|.
name|convert
argument_list|(
name|o
argument_list|,
operator|new
name|GenericType
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|printUsage
parameter_list|(
name|Command
name|command
parameter_list|,
name|Set
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|,
name|Set
argument_list|<
name|Argument
argument_list|>
name|arguments
parameter_list|,
name|PrintStream
name|out
parameter_list|)
block|{
name|options
operator|=
operator|new
name|HashSet
argument_list|<
name|Option
argument_list|>
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|HELP
argument_list|)
expr_stmt|;
if|if
condition|(
name|command
operator|!=
literal|null
operator|&&
operator|(
name|command
operator|.
name|description
argument_list|()
operator|!=
literal|null
operator|)
operator|||
name|command
operator|.
name|name
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
literal|"DESCRIPTION"
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
if|if
condition|(
name|command
operator|.
name|name
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|command
operator|.
name|scope
argument_list|()
argument_list|)
operator|.
name|a
argument_list|(
literal|":"
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
name|command
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|print
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|command
operator|.
name|description
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
name|String
name|syntax
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|command
operator|!=
literal|null
condition|)
block|{
name|syntax
operator|+=
name|command
operator|.
name|scope
argument_list|()
operator|+
literal|":"
operator|+
name|command
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|syntax
operator|+=
literal|" [options]"
expr_stmt|;
block|}
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|syntax
operator|+=
literal|" "
expr_stmt|;
block|}
for|for
control|(
name|Argument
name|argument
range|:
name|arguments
control|)
block|{
if|if
condition|(
name|argument
operator|.
name|required
argument_list|()
condition|)
block|{
name|syntax
operator|+=
literal|"["
operator|+
name|argument
operator|.
name|name
argument_list|()
operator|+
literal|"]"
expr_stmt|;
block|}
else|else
block|{
name|syntax
operator|+=
name|argument
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
literal|"SYNTAX"
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|syntax
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
literal|"ARGUMENTS"
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Argument
name|argument
range|:
name|arguments
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|argument
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|argument
operator|.
name|description
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
literal|"OPTIONS"
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Option
name|option
range|:
name|options
control|)
block|{
name|String
name|opt
init|=
name|option
operator|.
name|name
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|alias
range|:
name|option
operator|.
name|aliases
argument_list|()
control|)
block|{
name|opt
operator|+=
literal|", "
operator|+
name|alias
expr_stmt|;
block|}
name|out
operator|.
name|print
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|opt
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"\t\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|option
operator|.
name|description
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|printFormatted
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|str
parameter_list|,
name|int
name|termWidth
parameter_list|,
name|PrintStream
name|out
parameter_list|)
block|{
name|int
name|pfxLen
init|=
name|length
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
block|}
specifier|protected
name|int
name|length
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|str
operator|.
name|length
argument_list|()
return|;
block|}
block|}
specifier|protected
name|Action
name|createNewAction
parameter_list|()
throws|throws
name|Exception
block|{
name|Action
name|action
init|=
operator|(
name|Action
operator|)
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|actionId
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|instanceof
name|BlueprintContainerAware
condition|)
block|{
operator|(
operator|(
name|BlueprintContainerAware
operator|)
name|action
operator|)
operator|.
name|setBlueprintContainer
argument_list|(
name|blueprintContainer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|action
operator|instanceof
name|BundleContextAware
condition|)
block|{
name|BundleContext
name|context
init|=
operator|(
name|BundleContext
operator|)
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
literal|"blueprintBundleContext"
argument_list|)
decl_stmt|;
operator|(
operator|(
name|BundleContextAware
operator|)
name|action
operator|)
operator|.
name|setBundleContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|action
return|;
block|}
block|}
end_class

end_unit

