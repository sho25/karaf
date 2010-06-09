begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Map
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|converter
operator|.
name|DefaultConverter
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

begin_class
specifier|public
class|class
name|DefaultActionPreparator
implements|implements
name|ActionPreparator
block|{
specifier|protected
specifier|static
specifier|final
name|Option
name|HELP
init|=
operator|new
name|Option
argument_list|()
block|{
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
literal|"--help"
return|;
block|}
specifier|public
name|String
index|[]
name|aliases
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{ }
return|;
block|}
specifier|public
name|String
name|description
parameter_list|()
block|{
return|return
literal|"Display this help message"
return|;
block|}
specifier|public
name|boolean
name|required
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|multiValued
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationType
parameter_list|()
block|{
return|return
name|Option
operator|.
name|class
return|;
block|}
block|}
decl_stmt|;
specifier|public
name|boolean
name|prepare
parameter_list|(
name|Action
name|action
parameter_list|,
name|CommandSession
name|session
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|Option
argument_list|,
name|Field
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<
name|Option
argument_list|,
name|Field
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Argument
argument_list|,
name|Field
argument_list|>
name|arguments
init|=
operator|new
name|HashMap
argument_list|<
name|Argument
argument_list|,
name|Field
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Argument
argument_list|>
name|orderedArguments
init|=
operator|new
name|ArrayList
argument_list|<
name|Argument
argument_list|>
argument_list|()
decl_stmt|;
comment|// Introspect
for|for
control|(
name|Class
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
name|put
argument_list|(
name|option
argument_list|,
name|field
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
name|arguments
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
name|orderedArguments
operator|.
name|size
argument_list|()
operator|<=
name|index
condition|)
block|{
name|orderedArguments
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|orderedArguments
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
argument_list|)
throw|;
block|}
name|orderedArguments
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
comment|// Check indexes are correct
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|orderedArguments
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|orderedArguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Missing argument for index: "
operator|+
name|i
argument_list|)
throw|;
block|}
block|}
comment|// Populate
name|Map
argument_list|<
name|Option
argument_list|,
name|Object
argument_list|>
name|optionValues
init|=
operator|new
name|HashMap
argument_list|<
name|Option
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Argument
argument_list|,
name|Object
argument_list|>
name|argumentValues
init|=
operator|new
name|HashMap
argument_list|<
name|Argument
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|processOptions
init|=
literal|true
decl_stmt|;
name|int
name|argIndex
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|params
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|param
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// Check for help
if|if
condition|(
name|HELP
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
name|param
argument_list|)
operator|||
name|Arrays
operator|.
name|asList
argument_list|(
name|HELP
operator|.
name|aliases
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|param
argument_list|)
condition|)
block|{
name|printUsage
argument_list|(
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
argument_list|,
name|options
operator|.
name|keySet
argument_list|()
argument_list|,
name|arguments
operator|.
name|keySet
argument_list|()
argument_list|,
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|processOptions
operator|&&
name|param
operator|instanceof
name|String
operator|&&
operator|(
operator|(
name|String
operator|)
name|param
operator|)
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|boolean
name|isKeyValuePair
init|=
operator|(
operator|(
name|String
operator|)
name|param
operator|)
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
operator|!=
operator|-
literal|1
decl_stmt|;
name|String
name|name
decl_stmt|;
name|Object
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isKeyValuePair
condition|)
block|{
name|name
operator|=
operator|(
operator|(
name|String
operator|)
name|param
operator|)
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
operator|(
operator|(
name|String
operator|)
name|param
operator|)
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
argument_list|)
expr_stmt|;
name|value
operator|=
operator|(
operator|(
name|String
operator|)
name|param
operator|)
operator|.
name|substring
argument_list|(
operator|(
operator|(
name|String
operator|)
name|param
operator|)
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
operator|(
name|String
operator|)
name|param
expr_stmt|;
block|}
name|Option
name|option
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Option
name|opt
range|:
name|options
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|opt
operator|.
name|name
argument_list|()
argument_list|)
operator|||
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|opt
operator|.
name|aliases
argument_list|()
argument_list|,
name|name
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|option
operator|=
name|opt
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|option
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Undefined option: "
operator|+
name|param
argument_list|)
throw|;
block|}
name|Field
name|field
init|=
name|options
operator|.
name|get
argument_list|(
name|option
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
operator|(
name|field
operator|.
name|getType
argument_list|()
operator|==
name|boolean
operator|.
name|class
operator|||
name|field
operator|.
name|getType
argument_list|()
operator|==
name|Boolean
operator|.
name|class
operator|)
condition|)
block|{
name|value
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|value
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Missing value for option "
operator|+
name|param
argument_list|)
throw|;
block|}
if|if
condition|(
name|option
operator|.
name|multiValued
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|l
init|=
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|optionValues
operator|.
name|get
argument_list|(
name|option
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|l
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|optionValues
operator|.
name|put
argument_list|(
name|option
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
name|l
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|optionValues
operator|.
name|put
argument_list|(
name|option
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|processOptions
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|argIndex
operator|>=
name|orderedArguments
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Too many arguments specified"
argument_list|)
throw|;
block|}
name|Argument
name|argument
init|=
name|orderedArguments
operator|.
name|get
argument_list|(
name|argIndex
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|argument
operator|.
name|multiValued
argument_list|()
condition|)
block|{
name|argIndex
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|argument
operator|.
name|multiValued
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|l
init|=
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|argumentValues
operator|.
name|get
argument_list|(
name|argument
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|l
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|argumentValues
operator|.
name|put
argument_list|(
name|argument
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
name|l
operator|.
name|add
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|argumentValues
operator|.
name|put
argument_list|(
name|argument
argument_list|,
name|param
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Check required arguments / options
for|for
control|(
name|Option
name|option
range|:
name|options
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|option
operator|.
name|required
argument_list|()
operator|&&
name|optionValues
operator|.
name|get
argument_list|(
name|option
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option "
operator|+
name|option
operator|.
name|name
argument_list|()
operator|+
literal|" is required"
argument_list|)
throw|;
block|}
block|}
for|for
control|(
name|Argument
name|argument
range|:
name|arguments
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|argument
operator|.
name|required
argument_list|()
operator|&&
name|argumentValues
operator|.
name|get
argument_list|(
name|argument
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Argument "
operator|+
name|argument
operator|.
name|name
argument_list|()
operator|+
literal|" is required"
argument_list|)
throw|;
block|}
block|}
comment|// Convert and inject values
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Option
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|optionValues
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Field
name|field
init|=
name|options
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|convert
argument_list|(
name|action
argument_list|,
name|session
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|field
operator|.
name|getGenericType
argument_list|()
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|field
operator|.
name|set
argument_list|(
name|action
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Argument
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|argumentValues
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Field
name|field
init|=
name|arguments
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|convert
argument_list|(
name|action
argument_list|,
name|session
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|field
operator|.
name|getGenericType
argument_list|()
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|field
operator|.
name|set
argument_list|(
name|action
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
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
name|command
operator|.
name|description
argument_list|()
operator|!=
literal|null
operator|&&
name|command
operator|.
name|description
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
literal|"syntax: "
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
name|syntax
operator|+=
literal|" [arguments]"
expr_stmt|;
block|}
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
literal|"arguments:"
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
literal|"  "
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
literal|"  "
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
literal|"options:"
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
name|out
operator|.
name|print
argument_list|(
literal|"  "
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|option
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"  "
argument_list|)
expr_stmt|;
if|if
condition|(
name|option
operator|.
name|aliases
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|Arrays
operator|.
name|toString
argument_list|(
name|option
operator|.
name|aliases
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|")  "
argument_list|)
expr_stmt|;
block|}
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
name|Object
name|convert
parameter_list|(
name|Action
name|action
parameter_list|,
name|CommandSession
name|session
parameter_list|,
name|Object
name|value
parameter_list|,
name|Type
name|toType
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultConverter
argument_list|(
name|action
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
operator|.
name|convert
argument_list|(
name|value
argument_list|,
name|toType
argument_list|)
return|;
block|}
block|}
end_class

end_unit

