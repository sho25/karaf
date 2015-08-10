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
name|commands
operator|.
name|impl
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
name|Constructor
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
name|InvocationTargetException
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
name|ArrayList
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
name|HashMap
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
name|lifecycle
operator|.
name|Reference
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
name|lifecycle
operator|.
name|Service
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
name|support
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
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|converter
operator|.
name|GenericType
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
name|support
operator|.
name|converter
operator|.
name|ReifiedType
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
name|wiring
operator|.
name|BundleCapability
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
name|wiring
operator|.
name|BundleRevision
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
name|wiring
operator|.
name|BundleWiring
import|;
end_import

begin_comment
comment|/**  * Instantiate a new object  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"shell"
argument_list|,
name|name
operator|=
literal|"new"
argument_list|,
name|description
operator|=
literal|"Creates a new java object."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|NewAction
implements|implements
name|Action
block|{
annotation|@
name|Argument
argument_list|(
name|name
operator|=
literal|"class"
argument_list|,
name|index
operator|=
literal|0
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"FQN of the class to load"
argument_list|)
name|String
name|clazzName
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|name
operator|=
literal|"args"
argument_list|,
name|index
operator|=
literal|1
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|description
operator|=
literal|"Constructor arguments"
argument_list|)
name|List
argument_list|<
name|Object
argument_list|>
name|args
decl_stmt|;
name|boolean
name|reorderArguments
decl_stmt|;
specifier|protected
name|DefaultConverter
name|converter
decl_stmt|;
annotation|@
name|Reference
name|BundleContext
name|context
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|==
literal|null
condition|)
block|{
name|args
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
name|String
name|packageName
init|=
name|getPackageName
argument_list|(
name|clazzName
argument_list|)
decl_stmt|;
name|ClassLoader
name|classLoader
init|=
name|getClassLoaderForPackage
argument_list|(
name|packageName
argument_list|)
decl_stmt|;
name|converter
operator|=
operator|new
name|DefaultConverter
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|converter
operator|.
name|convert
argument_list|(
name|clazzName
argument_list|,
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Handle arrays
comment|/*         if (clazz.isArray()) {             Object obj = Array.newInstance(clazz.getComponentType(), args.size());             for (int i = 0; i< args.size(); i++) {                 Array.set(obj, i, convert(args.get(i), clazz.getComponentType()));             }             return obj;         }         */
comment|// Map of matching constructors
name|Map
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|matches
init|=
name|findMatchingConstructors
argument_list|(
name|clazz
argument_list|,
name|args
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ReifiedType
index|[
name|args
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|matches
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
try|try
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|match
init|=
name|matches
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
name|newInstance
argument_list|(
name|match
operator|.
name|getKey
argument_list|()
argument_list|,
name|match
operator|.
name|getValue
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error when instantiating object of class "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
name|getRealCause
argument_list|(
name|e
argument_list|)
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|matches
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unable to find a matching constructor on class "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|" for arguments "
operator|+
name|args
operator|+
literal|" when instantiating object."
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Multiple matching constructors found on class "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|" for arguments "
operator|+
name|args
operator|+
literal|" when instantiating object: "
operator|+
name|matches
operator|.
name|keySet
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|getPackageName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|nameSeperator
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameSeperator
operator|<=
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|nameSeperator
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * Get class loader offering a named package. This only works if we do not care      * which package we get in case of several package versions      *        * @param reqPackageName      * @return      */
specifier|private
name|ClassLoader
name|getClassLoaderForPackage
parameter_list|(
name|String
name|reqPackageName
parameter_list|)
block|{
name|Bundle
index|[]
name|bundles
init|=
name|context
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|BundleRevision
name|rev
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleRevision
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|rev
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|BundleCapability
argument_list|>
name|caps
init|=
name|rev
operator|.
name|getDeclaredCapabilities
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
for|for
control|(
name|BundleCapability
name|cap
range|:
name|caps
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attr
init|=
name|cap
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|String
name|packageName
init|=
operator|(
name|String
operator|)
name|attr
operator|.
name|get
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|packageName
operator|.
name|equals
argument_list|(
name|reqPackageName
argument_list|)
condition|)
block|{
name|BundleWiring
name|wiring
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|wiring
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|//
comment|// Code below comes from Aries blueprint implementation.  Given this code is not available
comment|// from a public API it has been copied here.
comment|//
specifier|private
name|Object
name|newInstance
parameter_list|(
name|Constructor
name|constructor
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|constructor
operator|.
name|newInstance
argument_list|(
name|args
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|findMatchingConstructors
parameter_list|(
name|Class
name|type
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|args
parameter_list|,
name|List
argument_list|<
name|ReifiedType
argument_list|>
name|types
parameter_list|)
block|{
name|Map
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|matches
init|=
operator|new
name|HashMap
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// Get constructors
name|List
argument_list|<
name|Constructor
argument_list|>
name|constructors
init|=
operator|new
name|ArrayList
argument_list|<
name|Constructor
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|type
operator|.
name|getConstructors
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// Discard any signature with wrong cardinality
for|for
control|(
name|Iterator
argument_list|<
name|Constructor
argument_list|>
name|it
init|=
name|constructors
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
if|if
condition|(
name|it
operator|.
name|next
argument_list|()
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|!=
name|args
operator|.
name|size
argument_list|()
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Find a direct match with assignment
if|if
condition|(
name|matches
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|Map
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|nmatches
init|=
operator|new
name|HashMap
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Constructor
name|cns
range|:
name|constructors
control|)
block|{
name|boolean
name|found
init|=
literal|true
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|match
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
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
name|args
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ReifiedType
name|argType
init|=
operator|new
name|GenericType
argument_list|(
name|cns
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|types
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|!=
literal|null
operator|&&
operator|!
name|argType
operator|.
name|getRawClass
argument_list|()
operator|.
name|equals
argument_list|(
name|types
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getRawClass
argument_list|()
argument_list|)
condition|)
block|{
name|found
operator|=
literal|false
expr_stmt|;
break|break;
block|}
if|if
condition|(
operator|!
name|isAssignable
argument_list|(
name|args
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|argType
argument_list|)
condition|)
block|{
name|found
operator|=
literal|false
expr_stmt|;
break|break;
block|}
try|try
block|{
name|match
operator|.
name|add
argument_list|(
name|convert
argument_list|(
name|args
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|cns
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|found
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|found
condition|)
block|{
name|nmatches
operator|.
name|put
argument_list|(
name|cns
argument_list|,
name|match
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|nmatches
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|matches
operator|=
name|nmatches
expr_stmt|;
block|}
block|}
comment|// Find a direct match with conversion
if|if
condition|(
name|matches
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|Map
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|nmatches
init|=
operator|new
name|HashMap
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Constructor
name|cns
range|:
name|constructors
control|)
block|{
name|boolean
name|found
init|=
literal|true
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|match
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
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
name|args
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ReifiedType
name|argType
init|=
operator|new
name|GenericType
argument_list|(
name|cns
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|types
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|!=
literal|null
operator|&&
operator|!
name|argType
operator|.
name|getRawClass
argument_list|()
operator|.
name|equals
argument_list|(
name|types
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getRawClass
argument_list|()
argument_list|)
condition|)
block|{
name|found
operator|=
literal|false
expr_stmt|;
break|break;
block|}
try|try
block|{
name|Object
name|val
init|=
name|convert
argument_list|(
name|args
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|argType
argument_list|)
decl_stmt|;
name|match
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|found
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|found
condition|)
block|{
name|nmatches
operator|.
name|put
argument_list|(
name|cns
argument_list|,
name|match
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|nmatches
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|matches
operator|=
name|nmatches
expr_stmt|;
block|}
block|}
comment|// Start reordering with assignment
if|if
condition|(
name|matches
operator|.
name|size
argument_list|()
operator|!=
literal|1
operator|&&
name|reorderArguments
operator|&&
name|args
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|Map
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|nmatches
init|=
operator|new
name|HashMap
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Constructor
name|cns
range|:
name|constructors
control|)
block|{
name|ArgumentMatcher
name|matcher
init|=
operator|new
name|ArgumentMatcher
argument_list|(
name|cns
operator|.
name|getGenericParameterTypes
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|match
init|=
name|matcher
operator|.
name|match
argument_list|(
name|args
argument_list|,
name|types
argument_list|)
decl_stmt|;
if|if
condition|(
name|match
operator|!=
literal|null
condition|)
block|{
name|nmatches
operator|.
name|put
argument_list|(
name|cns
argument_list|,
name|match
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|nmatches
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|matches
operator|=
name|nmatches
expr_stmt|;
block|}
block|}
comment|// Start reordering with conversion
if|if
condition|(
name|matches
operator|.
name|size
argument_list|()
operator|!=
literal|1
operator|&&
name|reorderArguments
operator|&&
name|args
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|Map
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|nmatches
init|=
operator|new
name|HashMap
argument_list|<
name|Constructor
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Constructor
name|cns
range|:
name|constructors
control|)
block|{
name|ArgumentMatcher
name|matcher
init|=
operator|new
name|ArgumentMatcher
argument_list|(
name|cns
operator|.
name|getGenericParameterTypes
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|match
init|=
name|matcher
operator|.
name|match
argument_list|(
name|args
argument_list|,
name|types
argument_list|)
decl_stmt|;
if|if
condition|(
name|match
operator|!=
literal|null
condition|)
block|{
name|nmatches
operator|.
name|put
argument_list|(
name|cns
argument_list|,
name|match
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|nmatches
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|matches
operator|=
name|nmatches
expr_stmt|;
block|}
block|}
return|return
name|matches
return|;
block|}
specifier|protected
name|Object
name|convert
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Type
name|type
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|converter
operator|.
name|convert
argument_list|(
name|obj
argument_list|,
operator|new
name|GenericType
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Object
name|convert
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ReifiedType
name|type
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|converter
operator|.
name|convert
argument_list|(
name|obj
argument_list|,
name|type
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isAssignable
parameter_list|(
name|Object
name|source
parameter_list|,
name|ReifiedType
name|target
parameter_list|)
block|{
return|return
name|source
operator|==
literal|null
operator|||
operator|(
name|target
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|&&
name|unwrap
argument_list|(
name|target
operator|.
name|getRawClass
argument_list|()
argument_list|)
operator|.
name|isAssignableFrom
argument_list|(
name|unwrap
argument_list|(
name|source
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
operator|)
return|;
block|}
specifier|private
specifier|static
name|Class
name|unwrap
parameter_list|(
name|Class
name|c
parameter_list|)
block|{
name|Class
name|u
init|=
name|primitives
operator|.
name|get
argument_list|(
name|c
argument_list|)
decl_stmt|;
return|return
name|u
operator|!=
literal|null
condition|?
name|u
else|:
name|c
return|;
block|}
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|,
name|Class
argument_list|>
name|primitives
decl_stmt|;
static|static
block|{
name|primitives
operator|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|,
name|Class
argument_list|>
argument_list|()
expr_stmt|;
name|primitives
operator|.
name|put
argument_list|(
name|byte
operator|.
name|class
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitives
operator|.
name|put
argument_list|(
name|short
operator|.
name|class
argument_list|,
name|Short
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitives
operator|.
name|put
argument_list|(
name|char
operator|.
name|class
argument_list|,
name|Character
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitives
operator|.
name|put
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitives
operator|.
name|put
argument_list|(
name|long
operator|.
name|class
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitives
operator|.
name|put
argument_list|(
name|float
operator|.
name|class
argument_list|,
name|Float
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitives
operator|.
name|put
argument_list|(
name|double
operator|.
name|class
argument_list|,
name|Double
operator|.
name|class
argument_list|)
expr_stmt|;
name|primitives
operator|.
name|put
argument_list|(
name|boolean
operator|.
name|class
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Object
name|UNMATCHED
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|private
class|class
name|ArgumentMatcher
block|{
specifier|private
name|List
argument_list|<
name|TypeEntry
argument_list|>
name|entries
decl_stmt|;
specifier|private
name|boolean
name|convert
decl_stmt|;
specifier|public
name|ArgumentMatcher
parameter_list|(
name|Type
index|[]
name|types
parameter_list|,
name|boolean
name|convert
parameter_list|)
block|{
name|entries
operator|=
operator|new
name|ArrayList
argument_list|<
name|TypeEntry
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Type
name|type
range|:
name|types
control|)
block|{
name|entries
operator|.
name|add
argument_list|(
operator|new
name|TypeEntry
argument_list|(
operator|new
name|GenericType
argument_list|(
name|type
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|convert
operator|=
name|convert
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|match
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
parameter_list|,
name|List
argument_list|<
name|ReifiedType
argument_list|>
name|forcedTypes
parameter_list|)
block|{
if|if
condition|(
name|find
argument_list|(
name|arguments
argument_list|,
name|forcedTypes
argument_list|)
condition|)
block|{
return|return
name|getArguments
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|getArguments
parameter_list|()
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|TypeEntry
name|entry
range|:
name|entries
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|argument
operator|==
name|UNMATCHED
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"There are unmatched types"
argument_list|)
throw|;
block|}
else|else
block|{
name|list
operator|.
name|add
argument_list|(
name|entry
operator|.
name|argument
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
specifier|private
name|boolean
name|find
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
parameter_list|,
name|List
argument_list|<
name|ReifiedType
argument_list|>
name|forcedTypes
parameter_list|)
block|{
if|if
condition|(
name|entries
operator|.
name|size
argument_list|()
operator|==
name|arguments
operator|.
name|size
argument_list|()
condition|)
block|{
name|boolean
name|matched
init|=
literal|true
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
name|arguments
operator|.
name|size
argument_list|()
operator|&&
name|matched
condition|;
name|i
operator|++
control|)
block|{
name|matched
operator|=
name|find
argument_list|(
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|forcedTypes
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|matched
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|find
parameter_list|(
name|Object
name|arg
parameter_list|,
name|ReifiedType
name|forcedType
parameter_list|)
block|{
for|for
control|(
name|TypeEntry
name|entry
range|:
name|entries
control|)
block|{
name|Object
name|val
init|=
name|arg
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|argument
operator|!=
name|UNMATCHED
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|forcedType
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|forcedType
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|type
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
elseif|else
if|if
condition|(
name|arg
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|convert
condition|)
block|{
try|try
block|{
comment|// TODO: call canConvert instead of convert()
name|val
operator|=
name|convert
argument_list|(
name|arg
argument_list|,
name|entry
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
continue|continue;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|isAssignable
argument_list|(
name|arg
argument_list|,
name|entry
operator|.
name|type
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
block|}
name|entry
operator|.
name|argument
operator|=
name|val
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|TypeEntry
block|{
specifier|private
specifier|final
name|ReifiedType
name|type
decl_stmt|;
specifier|private
name|Object
name|argument
decl_stmt|;
specifier|public
name|TypeEntry
parameter_list|(
name|ReifiedType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|argument
operator|=
name|UNMATCHED
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|Throwable
name|getRealCause
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|instanceof
name|InvocationTargetException
operator|&&
name|t
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|t
operator|.
name|getCause
argument_list|()
return|;
block|}
return|return
name|t
return|;
block|}
block|}
end_class

end_unit

