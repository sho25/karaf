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
name|converter
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
name|Array
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
name|GenericArrayType
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
name|ParameterizedType
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
name|lang
operator|.
name|reflect
operator|.
name|TypeVariable
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
name|WildcardType
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
name|Map
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

begin_class
specifier|public
class|class
name|GenericType
extends|extends
name|ReifiedType
block|{
specifier|private
specifier|static
specifier|final
name|GenericType
index|[]
name|EMPTY
init|=
operator|new
name|GenericType
index|[
literal|0
index|]
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
name|primitiveClasses
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|primitiveClasses
operator|.
name|put
argument_list|(
literal|"int"
argument_list|,
name|int
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitiveClasses
operator|.
name|put
argument_list|(
literal|"short"
argument_list|,
name|short
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitiveClasses
operator|.
name|put
argument_list|(
literal|"long"
argument_list|,
name|long
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitiveClasses
operator|.
name|put
argument_list|(
literal|"byte"
argument_list|,
name|byte
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitiveClasses
operator|.
name|put
argument_list|(
literal|"char"
argument_list|,
name|char
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitiveClasses
operator|.
name|put
argument_list|(
literal|"float"
argument_list|,
name|float
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitiveClasses
operator|.
name|put
argument_list|(
literal|"double"
argument_list|,
name|double
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitiveClasses
operator|.
name|put
argument_list|(
literal|"boolean"
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
name|GenericType
index|[]
name|parameters
decl_stmt|;
specifier|public
name|GenericType
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
name|this
argument_list|(
name|getConcreteClass
argument_list|(
name|type
argument_list|)
argument_list|,
name|parametersOf
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|GenericType
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|GenericType
modifier|...
name|parameters
parameter_list|)
block|{
name|super
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
specifier|public
specifier|static
name|GenericType
name|parse
parameter_list|(
name|String
name|type
parameter_list|,
name|Object
name|loader
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IllegalArgumentException
block|{
name|type
operator|=
name|type
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|// Check if this is an array
if|if
condition|(
name|type
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|)
block|{
name|GenericType
name|t
init|=
name|parse
argument_list|(
name|type
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|type
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
argument_list|,
name|loader
argument_list|)
decl_stmt|;
return|return
operator|new
name|GenericType
argument_list|(
name|Array
operator|.
name|newInstance
argument_list|(
name|t
operator|.
name|getRawClass
argument_list|()
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|,
name|t
argument_list|)
return|;
block|}
comment|// Check if this is a generic
name|int
name|genericIndex
init|=
name|type
operator|.
name|indexOf
argument_list|(
literal|'<'
argument_list|)
decl_stmt|;
if|if
condition|(
name|genericIndex
operator|>
literal|0
condition|)
block|{
if|if
condition|(
operator|!
name|type
operator|.
name|endsWith
argument_list|(
literal|">"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can not load type: "
operator|+
name|type
argument_list|)
throw|;
block|}
name|GenericType
name|base
init|=
name|parse
argument_list|(
name|type
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|genericIndex
argument_list|)
argument_list|,
name|loader
argument_list|)
decl_stmt|;
name|String
index|[]
name|params
init|=
name|type
operator|.
name|substring
argument_list|(
name|genericIndex
operator|+
literal|1
argument_list|,
name|type
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|GenericType
index|[]
name|types
init|=
operator|new
name|GenericType
index|[
name|params
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|params
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|types
index|[
name|i
index|]
operator|=
name|parse
argument_list|(
name|params
index|[
name|i
index|]
argument_list|,
name|loader
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|GenericType
argument_list|(
name|base
operator|.
name|getRawClass
argument_list|()
argument_list|,
name|types
argument_list|)
return|;
block|}
comment|// Primitive
if|if
condition|(
name|primitiveClasses
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|new
name|GenericType
argument_list|(
name|primitiveClasses
operator|.
name|get
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
comment|// Class
if|if
condition|(
name|loader
operator|instanceof
name|ClassLoader
condition|)
block|{
return|return
operator|new
name|GenericType
argument_list|(
operator|(
operator|(
name|ClassLoader
operator|)
name|loader
operator|)
operator|.
name|loadClass
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|loader
operator|instanceof
name|Bundle
condition|)
block|{
return|return
operator|new
name|GenericType
argument_list|(
operator|(
operator|(
name|Bundle
operator|)
name|loader
operator|)
operator|.
name|loadClass
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported loader: "
operator|+
name|loader
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|ReifiedType
name|getActualTypeArgument
parameter_list|(
name|int
name|i
parameter_list|)
block|{
if|if
condition|(
name|parameters
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|super
operator|.
name|getActualTypeArgument
argument_list|(
name|i
argument_list|)
return|;
block|}
return|return
name|parameters
index|[
name|i
index|]
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|parameters
operator|.
name|length
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|Class
name|cl
init|=
name|getRawClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|cl
operator|.
name|isArray
argument_list|()
condition|)
block|{
if|if
condition|(
name|parameters
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|parameters
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
operator|+
literal|"[]"
return|;
block|}
else|else
block|{
return|return
name|cl
operator|.
name|getComponentType
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"[]"
return|;
block|}
block|}
if|if
condition|(
name|parameters
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|cl
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<"
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
name|parameters
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|parameters
index|[
name|i
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|cl
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|static
name|GenericType
index|[]
name|parametersOf
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|Class
condition|)
block|{
name|Class
name|clazz
init|=
operator|(
name|Class
operator|)
name|type
decl_stmt|;
if|if
condition|(
name|clazz
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|GenericType
name|t
init|=
operator|new
name|GenericType
argument_list|(
name|clazz
operator|.
name|getComponentType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
operator|new
name|GenericType
index|[]
block|{
name|t
block|}
return|;
block|}
else|else
block|{
return|return
name|EMPTY
return|;
block|}
block|}
else|else
block|{
return|return
name|EMPTY
return|;
block|}
block|}
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|pt
init|=
operator|(
name|ParameterizedType
operator|)
name|type
decl_stmt|;
name|Type
index|[]
name|parameters
init|=
name|pt
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
name|GenericType
index|[]
name|gts
init|=
operator|new
name|GenericType
index|[
name|parameters
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|gts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|gts
index|[
name|i
index|]
operator|=
operator|new
name|GenericType
argument_list|(
name|parameters
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|gts
return|;
block|}
if|if
condition|(
name|type
operator|instanceof
name|GenericArrayType
condition|)
block|{
return|return
operator|new
name|GenericType
index|[]
block|{
operator|new
name|GenericType
argument_list|(
operator|(
operator|(
name|GenericArrayType
operator|)
name|type
operator|)
operator|.
name|getGenericComponentType
argument_list|()
argument_list|)
block|}
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getConcreteClass
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
name|Type
name|ntype
init|=
name|collapse
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|ntype
operator|instanceof
name|Class
condition|)
return|return
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|ntype
return|;
if|if
condition|(
name|ntype
operator|instanceof
name|ParameterizedType
condition|)
return|return
name|getConcreteClass
argument_list|(
name|collapse
argument_list|(
operator|(
operator|(
name|ParameterizedType
operator|)
name|ntype
operator|)
operator|.
name|getRawType
argument_list|()
argument_list|)
argument_list|)
return|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unknown type "
operator|+
name|type
argument_list|)
throw|;
block|}
specifier|static
name|Type
name|collapse
parameter_list|(
name|Type
name|target
parameter_list|)
block|{
if|if
condition|(
name|target
operator|instanceof
name|Class
operator|||
name|target
operator|instanceof
name|ParameterizedType
condition|)
block|{
return|return
name|target
return|;
block|}
elseif|else
if|if
condition|(
name|target
operator|instanceof
name|TypeVariable
condition|)
block|{
return|return
name|collapse
argument_list|(
operator|(
operator|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
operator|)
name|target
operator|)
operator|.
name|getBounds
argument_list|()
index|[
literal|0
index|]
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|target
operator|instanceof
name|GenericArrayType
condition|)
block|{
name|Type
name|t
init|=
name|collapse
argument_list|(
operator|(
operator|(
name|GenericArrayType
operator|)
name|target
operator|)
operator|.
name|getGenericComponentType
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|t
operator|instanceof
name|ParameterizedType
condition|)
name|t
operator|=
name|collapse
argument_list|(
operator|(
operator|(
name|ParameterizedType
operator|)
name|t
operator|)
operator|.
name|getRawType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Array
operator|.
name|newInstance
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|t
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|target
operator|instanceof
name|WildcardType
condition|)
block|{
name|WildcardType
name|wct
init|=
operator|(
name|WildcardType
operator|)
name|target
decl_stmt|;
if|if
condition|(
name|wct
operator|.
name|getLowerBounds
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
return|return
name|collapse
argument_list|(
name|wct
operator|.
name|getUpperBounds
argument_list|()
index|[
literal|0
index|]
argument_list|)
return|;
else|else
return|return
name|collapse
argument_list|(
name|wct
operator|.
name|getLowerBounds
argument_list|()
index|[
literal|0
index|]
argument_list|)
return|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Huh? "
operator|+
name|target
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

