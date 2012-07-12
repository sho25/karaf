begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|commands
operator|.
name|meta
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
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|commands
operator|.
name|Option
import|;
end_import

begin_class
specifier|public
class|class
name|ActionMetaDataFactory
block|{
specifier|public
name|ActionMetaData
name|create
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
parameter_list|)
block|{
name|Command
name|command
init|=
name|getCommand
argument_list|(
name|actionClass
argument_list|)
decl_stmt|;
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
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|actionClass
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
operator|==
literal|null
condition|)
block|{
name|option
operator|=
name|getAndConvertDeprecatedOption
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
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
operator|==
literal|null
condition|)
block|{
name|argument
operator|=
name|getAndConvertDeprecatedArgument
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
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
operator|+
literal|" on Action "
operator|+
name|actionClass
operator|.
name|getName
argument_list|()
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
name|assertIndexesAreCorrect
argument_list|(
name|actionClass
argument_list|,
name|orderedArguments
argument_list|)
expr_stmt|;
return|return
operator|new
name|ActionMetaData
argument_list|(
name|actionClass
argument_list|,
name|command
argument_list|,
name|options
argument_list|,
name|arguments
argument_list|,
name|orderedArguments
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Command
name|getCommand
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
parameter_list|)
block|{
name|Command
name|command
init|=
name|actionClass
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|command
operator|==
literal|null
condition|)
block|{
name|command
operator|=
name|getAndConvertDeprecatedCommand
argument_list|(
name|actionClass
argument_list|)
expr_stmt|;
block|}
return|return
name|command
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|public
name|Command
name|getAndConvertDeprecatedCommand
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
parameter_list|)
block|{
specifier|final
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
name|oldCommand
init|=
name|actionClass
operator|.
name|getAnnotation
argument_list|(
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
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldCommand
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|Command
argument_list|()
block|{
annotation|@
name|Override
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
name|Command
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|scope
parameter_list|()
block|{
return|return
name|oldCommand
operator|.
name|scope
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
name|oldCommand
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|detailedDescription
parameter_list|()
block|{
return|return
name|oldCommand
operator|.
name|detailedDescription
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|description
parameter_list|()
block|{
return|return
name|oldCommand
operator|.
name|description
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|private
name|Option
name|getAndConvertDeprecatedOption
parameter_list|(
name|Field
name|field
parameter_list|)
block|{
specifier|final
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
name|oldOption
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
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
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldOption
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|Option
argument_list|()
block|{
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|String
name|valueToShowInHelp
parameter_list|()
block|{
return|return
name|oldOption
operator|.
name|valueToShowInHelp
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|required
parameter_list|()
block|{
return|return
name|oldOption
operator|.
name|required
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
name|oldOption
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|multiValued
parameter_list|()
block|{
return|return
name|oldOption
operator|.
name|multiValued
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|description
parameter_list|()
block|{
return|return
name|oldOption
operator|.
name|description
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|aliases
parameter_list|()
block|{
return|return
name|oldOption
operator|.
name|aliases
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|private
name|Argument
name|getAndConvertDeprecatedArgument
parameter_list|(
name|Field
name|field
parameter_list|)
block|{
specifier|final
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
name|oldArgument
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
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
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldArgument
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|Argument
argument_list|()
block|{
annotation|@
name|Override
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
name|Argument
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|valueToShowInHelp
parameter_list|()
block|{
return|return
name|oldArgument
operator|.
name|valueToShowInHelp
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|required
parameter_list|()
block|{
return|return
name|oldArgument
operator|.
name|required
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
name|oldArgument
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|multiValued
parameter_list|()
block|{
return|return
name|oldArgument
operator|.
name|multiValued
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|index
parameter_list|()
block|{
return|return
name|oldArgument
operator|.
name|index
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|description
parameter_list|()
block|{
return|return
name|oldArgument
operator|.
name|description
argument_list|()
return|;
block|}
block|}
return|;
block|}
specifier|private
name|Argument
name|replaceDefaultArgument
parameter_list|(
name|Field
name|field
parameter_list|,
name|Argument
name|argument
parameter_list|)
block|{
if|if
condition|(
name|Argument
operator|.
name|DEFAULT
operator|.
name|equals
argument_list|(
name|argument
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
specifier|final
name|Argument
name|delegate
init|=
name|argument
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|field
operator|.
name|getName
argument_list|()
decl_stmt|;
name|argument
operator|=
operator|new
name|Argument
argument_list|()
block|{
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|description
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|description
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|required
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|required
argument_list|()
return|;
block|}
specifier|public
name|int
name|index
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|index
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|multiValued
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|multiValued
argument_list|()
return|;
block|}
specifier|public
name|String
name|valueToShowInHelp
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|valueToShowInHelp
argument_list|()
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
name|delegate
operator|.
name|annotationType
argument_list|()
return|;
block|}
block|}
expr_stmt|;
block|}
return|return
name|argument
return|;
block|}
specifier|private
name|void
name|assertIndexesAreCorrect
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
parameter_list|,
name|List
argument_list|<
name|Argument
argument_list|>
name|orderedArguments
parameter_list|)
block|{
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
operator|+
literal|" on Action "
operator|+
name|actionClass
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

