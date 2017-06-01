begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|commands
package|;
end_package

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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|List
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Set
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
name|api
operator|.
name|action
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
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
name|api
operator|.
name|action
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
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
name|karaf
operator|.
name|shell
operator|.
name|impl
operator|.
name|action
operator|.
name|command
operator|.
name|HelpOption
import|;
end_import

begin_comment
comment|/**  * Prints documentation in wiki syntax  */
end_comment

begin_class
specifier|public
class|class
name|UserConfCommandHelpPrinter
extends|extends
name|AbstractCommandHelpPrinter
block|{
annotation|@
name|Override
specifier|public
name|void
name|printHelp
parameter_list|(
name|Action
name|action
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|boolean
name|includeHelpOption
parameter_list|)
block|{
name|Command
name|command
init|=
name|action
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Option
argument_list|>
name|options
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Argument
argument_list|>
name|arguments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Argument
argument_list|,
name|Field
argument_list|>
name|argFields
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Option
argument_list|,
name|Field
argument_list|>
name|optFields
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|action
operator|.
name|getClass
argument_list|()
init|;
name|type
operator|!=
literal|null
condition|;
name|type
operator|=
name|type
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
for|for
control|(
name|Field
name|field
range|:
name|type
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|Option
name|option
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|option
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
name|Argument
name|argument
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Argument
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|argument
operator|!=
literal|null
condition|)
block|{
name|argument
operator|=
name|replaceDefaultArgument
argument_list|(
name|field
argument_list|,
name|argument
argument_list|)
expr_stmt|;
name|argFields
operator|.
name|put
argument_list|(
name|argument
argument_list|,
name|field
argument_list|)
expr_stmt|;
name|int
name|index
init|=
name|argument
operator|.
name|index
argument_list|()
decl_stmt|;
while|while
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|<=
name|index
condition|)
block|{
name|arguments
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|arguments
operator|.
name|get
argument_list|(
name|index
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Duplicate argument index: "
operator|+
name|index
operator|+
literal|" on Action "
operator|+
name|action
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|arguments
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|argument
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|includeHelpOption
condition|)
name|options
operator|.
name|add
argument_list|(
name|HelpOption
operator|.
name|HELP
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"h1. "
operator|+
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
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"h2. Description"
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
name|StringBuffer
name|syntax
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|syntax
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s:%s"
argument_list|,
name|command
operator|.
name|scope
argument_list|()
argument_list|,
name|command
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
operator|.
name|append
argument_list|(
literal|" \\[options\\]"
argument_list|)
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
name|syntax
operator|.
name|append
argument_list|(
literal|' '
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
name|syntax
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|argument
operator|.
name|required
argument_list|()
condition|?
literal|"%s "
else|:
literal|"\\[%s\\] "
argument_list|,
name|argument
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|println
argument_list|(
literal|"h2. Syntax"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|syntax
operator|.
name|toString
argument_list|()
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
literal|"h2. Arguments"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"|| Name || Description ||"
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
name|String
name|description
init|=
name|argument
operator|.
name|description
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|argument
operator|.
name|required
argument_list|()
condition|)
block|{
name|Object
name|o
init|=
name|getDefaultValue
argument_list|(
name|action
argument_list|,
name|argFields
operator|.
name|get
argument_list|(
name|argument
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|defaultValue
init|=
name|getDefaultValueString
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
name|description
operator|+=
literal|" (defaults to "
operator|+
name|o
operator|.
name|toString
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
block|}
name|out
operator|.
name|println
argument_list|(
literal|"| "
operator|+
name|argument
operator|.
name|name
argument_list|()
operator|+
literal|" | "
operator|+
name|description
operator|+
literal|" |"
argument_list|)
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
literal|"h2. Options"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"|| Name || Description ||"
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
name|String
name|desc
init|=
name|option
operator|.
name|description
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
name|Object
name|o
init|=
name|getDefaultValue
argument_list|(
name|action
argument_list|,
name|optFields
operator|.
name|get
argument_list|(
name|option
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|defaultValue
init|=
name|getDefaultValueString
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
name|desc
operator|+=
literal|" (defaults to "
operator|+
name|defaultValue
operator|+
literal|")"
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"| "
operator|+
name|opt
operator|+
literal|" | "
operator|+
name|desc
operator|+
literal|" |"
argument_list|)
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
name|command
operator|.
name|detailedDescription
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"h2. Details"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|command
operator|.
name|detailedDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|printOverview
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|commands
parameter_list|,
name|PrintStream
name|writer
parameter_list|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"h1. Commands"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|commands
operator|.
name|keySet
argument_list|()
control|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"h2. "
operator|+
name|key
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|cmd
range|:
name|commands
operator|.
name|get
argument_list|(
name|key
argument_list|)
control|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"* ["
operator|+
name|key
operator|+
literal|":"
operator|+
name|cmd
operator|+
literal|"|"
operator|+
name|key
operator|+
literal|"-"
operator|+
name|cmd
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

