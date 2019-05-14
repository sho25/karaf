begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|javax
operator|.
name|xml
operator|.
name|xpath
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|Method
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
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ServiceConfigurationError
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ServiceLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_class
class|class
name|$XPathFactoryFinder
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_PACKAGE
init|=
literal|"com.sun.org.apache.xpath.internal"
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|debug
init|=
literal|false
decl_stmt|;
static|static
block|{
try|try
block|{
name|debug
operator|=
name|getSystemProperty
argument_list|(
literal|"jaxp.debug"
argument_list|)
operator|!=
literal|null
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|unused
parameter_list|)
block|{
name|debug
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
specifier|final
name|Properties
name|cacheProps
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
specifier|private
specifier|volatile
specifier|static
name|boolean
name|firstTime
init|=
literal|true
decl_stmt|;
specifier|private
specifier|static
name|void
name|debugPrintln
parameter_list|(
name|Supplier
argument_list|<
name|String
argument_list|>
name|msgGen
parameter_list|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"JAXP: "
operator|+
name|msgGen
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|final
name|ClassLoader
name|classLoader
decl_stmt|;
specifier|public
name|$XPathFactoryFinder
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
block|{
name|this
operator|.
name|classLoader
operator|=
name|loader
expr_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|debugDisplayClassLoader
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|debugDisplayClassLoader
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|classLoader
operator|==
name|getContextClassLoader
argument_list|()
condition|)
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"using thread context class loader ("
operator|+
name|classLoader
operator|+
literal|") for search"
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|unused
parameter_list|)
block|{         }
if|if
condition|(
name|classLoader
operator|==
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
condition|)
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"using system class loader ("
operator|+
name|classLoader
operator|+
literal|") for search"
argument_list|)
expr_stmt|;
return|return;
block|}
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"using class loader ("
operator|+
name|classLoader
operator|+
literal|") for search"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XPathFactory
name|newFactory
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|XPathFactoryConfigurationException
block|{
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
name|XPathFactory
name|f
init|=
name|_newFactory
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"factory '"
operator|+
name|f
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' was found for "
operator|+
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"unable to find a factory for "
operator|+
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|f
return|;
block|}
specifier|private
name|XPathFactory
name|_newFactory
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|XPathFactoryConfigurationException
block|{
name|XPathFactory
name|xpathFactory
init|=
literal|null
decl_stmt|;
name|String
name|propertyName
init|=
name|SERVICE_CLASS
operator|.
name|getName
argument_list|()
operator|+
literal|":"
operator|+
name|uri
decl_stmt|;
try|try
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"Looking up system property '"
operator|+
name|propertyName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|String
name|r
init|=
name|getSystemProperty
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"The value is '"
operator|+
name|r
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|xpathFactory
operator|=
name|createInstance
argument_list|(
name|r
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|xpathFactory
operator|!=
literal|null
condition|)
block|{
return|return
name|xpathFactory
return|;
block|}
block|}
else|else
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"The property is undefined."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"failed to look up system property '"
operator|+
name|propertyName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
name|String
name|javah
init|=
name|getSystemProperty
argument_list|(
literal|"java.home"
argument_list|)
decl_stmt|;
name|String
name|configFile
init|=
name|javah
operator|+
name|File
operator|.
name|separator
operator|+
literal|"conf"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"jaxp.properties"
decl_stmt|;
try|try
block|{
if|if
condition|(
name|firstTime
condition|)
block|{
synchronized|synchronized
init|(
name|cacheProps
init|)
block|{
if|if
condition|(
name|firstTime
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|configFile
argument_list|)
decl_stmt|;
name|firstTime
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|doesFileExist
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"Read properties file "
operator|+
name|f
argument_list|)
expr_stmt|;
name|cacheProps
operator|.
name|load
argument_list|(
name|getFileInputStream
argument_list|(
name|f
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|final
name|String
name|factoryClassName
init|=
name|cacheProps
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"found "
operator|+
name|factoryClassName
operator|+
literal|" in $java.home/conf/jaxp.properties"
argument_list|)
expr_stmt|;
if|if
condition|(
name|factoryClassName
operator|!=
literal|null
condition|)
block|{
name|xpathFactory
operator|=
name|createInstance
argument_list|(
name|factoryClassName
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|xpathFactory
operator|!=
literal|null
condition|)
block|{
return|return
name|xpathFactory
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
assert|assert
name|xpathFactory
operator|==
literal|null
assert|;
name|xpathFactory
operator|=
name|findServiceProvider
argument_list|(
name|uri
argument_list|)
expr_stmt|;
if|if
condition|(
name|xpathFactory
operator|!=
literal|null
condition|)
block|{
return|return
name|xpathFactory
return|;
block|}
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|XPathFactory
operator|.
name|DEFAULT_OBJECT_MODEL_URI
argument_list|)
condition|)
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"attempting to use the platform default W3C DOM XPath lib"
argument_list|)
expr_stmt|;
return|return
name|createInstance
argument_list|(
literal|"com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl"
argument_list|,
literal|true
argument_list|)
return|;
block|}
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"all things were tried, but none was found. bailing out."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|createClass
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|Class
name|clazz
decl_stmt|;
name|boolean
name|internal
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getSecurityManager
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|className
operator|!=
literal|null
operator|&&
name|className
operator|.
name|startsWith
argument_list|(
name|DEFAULT_PACKAGE
argument_list|)
condition|)
block|{
name|internal
operator|=
literal|true
expr_stmt|;
block|}
block|}
try|try
block|{
if|if
condition|(
name|classLoader
operator|!=
literal|null
operator|&&
operator|!
name|internal
condition|)
block|{
name|clazz
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|,
literal|false
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|clazz
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
return|return
name|clazz
return|;
block|}
name|XPathFactory
name|createInstance
parameter_list|(
name|String
name|className
parameter_list|)
throws|throws
name|XPathFactoryConfigurationException
block|{
return|return
name|createInstance
argument_list|(
name|className
argument_list|,
literal|false
argument_list|)
return|;
block|}
name|XPathFactory
name|createInstance
parameter_list|(
name|String
name|className
parameter_list|,
name|boolean
name|useServicesMechanism
parameter_list|)
throws|throws
name|XPathFactoryConfigurationException
block|{
name|XPathFactory
name|xPathFactory
init|=
literal|null
decl_stmt|;
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"createInstance("
operator|+
name|className
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|createClass
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"failed to getClass("
operator|+
name|className
operator|+
literal|")"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"loaded "
operator|+
name|className
operator|+
literal|" from "
operator|+
name|which
argument_list|(
name|clazz
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|useServicesMechanism
condition|)
block|{
name|xPathFactory
operator|=
name|newInstanceNoServiceLoader
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xPathFactory
operator|==
literal|null
condition|)
block|{
name|xPathFactory
operator|=
operator|(
name|XPathFactory
operator|)
name|clazz
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
decl||
name|IllegalAccessException
decl||
name|InstantiationException
name|classCastException
parameter_list|)
block|{
name|debugPrintln
argument_list|(
parameter_list|()
lambda|->
literal|"could not instantiate "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|classCastException
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
return|return
name|xPathFactory
return|;
block|}
specifier|private
specifier|static
name|XPathFactory
name|newInstanceNoServiceLoader
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
parameter_list|)
throws|throws
name|XPathFactoryConfigurationException
block|{
if|if
condition|(
name|System
operator|.
name|getSecurityManager
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|Method
name|creationMethod
init|=
name|providerClass
operator|.
name|getDeclaredMethod
argument_list|(
literal|"newXPathFactoryNoServiceLoader"
argument_list|)
decl_stmt|;
specifier|final
name|int
name|modifiers
init|=
name|creationMethod
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
comment|// Do not call "newXPathFactoryNoServiceLoader" if it's
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isStatic
argument_list|(
name|modifiers
argument_list|)
operator|||
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Only calls "newXPathFactoryNoServiceLoader" if it's
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|returnType
init|=
name|creationMethod
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
if|if
condition|(
name|SERVICE_CLASS
operator|.
name|isAssignableFrom
argument_list|(
name|returnType
argument_list|)
condition|)
block|{
return|return
name|SERVICE_CLASS
operator|.
name|cast
argument_list|(
name|creationMethod
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ClassCastException
argument_list|(
name|returnType
operator|+
literal|" cannot be cast to "
operator|+
name|SERVICE_CLASS
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XPathFactoryConfigurationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exc
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|boolean
name|isObjectModelSupportedBy
parameter_list|(
specifier|final
name|XPathFactory
name|factory
parameter_list|,
specifier|final
name|String
name|objectModel
parameter_list|,
name|AccessControlContext
name|acc
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|run
parameter_list|()
block|{
return|return
name|factory
operator|.
name|isObjectModelSupported
argument_list|(
name|objectModel
argument_list|)
return|;
block|}
block|}
argument_list|,
name|acc
argument_list|)
return|;
block|}
specifier|private
name|XPathFactory
name|findServiceProvider
parameter_list|(
specifier|final
name|String
name|objectModel
parameter_list|)
throws|throws
name|XPathFactoryConfigurationException
block|{
assert|assert
name|objectModel
operator|!=
literal|null
assert|;
specifier|final
name|AccessControlContext
name|acc
init|=
name|AccessController
operator|.
name|getContext
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|XPathFactory
argument_list|>
argument_list|()
block|{
specifier|public
name|XPathFactory
name|run
parameter_list|()
block|{
specifier|final
name|ServiceLoader
argument_list|<
name|XPathFactory
argument_list|>
name|loader
init|=
name|ServiceLoader
operator|.
name|load
argument_list|(
name|SERVICE_CLASS
argument_list|)
decl_stmt|;
for|for
control|(
name|XPathFactory
name|factory
range|:
name|loader
control|)
block|{
if|if
condition|(
name|isObjectModelSupportedBy
argument_list|(
name|factory
argument_list|,
name|objectModel
argument_list|,
name|acc
argument_list|)
condition|)
block|{
return|return
name|factory
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ServiceConfigurationError
name|error
parameter_list|)
block|{
throw|throw
operator|new
name|XPathFactoryConfigurationException
argument_list|(
name|error
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|XPathFactory
argument_list|>
name|SERVICE_CLASS
init|=
name|XPathFactory
operator|.
name|class
decl_stmt|;
specifier|private
specifier|static
name|String
name|which
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|getClassSource
argument_list|(
name|clazz
argument_list|)
return|;
block|}
specifier|static
name|ClassLoader
name|getContextClassLoader
parameter_list|()
throws|throws
name|SecurityException
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
call|(
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
call|)
argument_list|()
operator|->
block|{
name|ClassLoader
name|cl
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
block|;
if|if
condition|(
name|cl
operator|==
literal|null
condition|)
block|{
name|cl
operator|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
expr_stmt|;
block|}
return|return
name|cl
return|;
block|}
block|)
class|;
end_class

begin_function
unit|}      private
specifier|static
name|String
name|getSystemProperty
parameter_list|(
specifier|final
name|String
name|propName
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
call|(
name|PrivilegedAction
argument_list|<
name|String
argument_list|>
call|)
argument_list|()
operator|->
name|System
operator|.
name|getProperty
argument_list|(
name|propName
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_function
specifier|private
specifier|static
name|FileInputStream
name|getFileInputStream
parameter_list|(
specifier|final
name|File
name|file
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
try|try
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
call|(
name|PrivilegedExceptionAction
argument_list|<
name|FileInputStream
argument_list|>
call|)
argument_list|()
operator|->
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PrivilegedActionException
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|FileNotFoundException
operator|)
name|e
operator|.
name|getException
argument_list|()
throw|;
block|}
block|}
end_function

begin_function
specifier|private
specifier|static
name|String
name|getClassSource
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
call|(
name|PrivilegedAction
argument_list|<
name|String
argument_list|>
call|)
argument_list|()
operator|->
block|{
name|CodeSource
name|cs
operator|=
name|cls
operator|.
name|getProtectionDomain
argument_list|()
operator|.
name|getCodeSource
argument_list|()
block|;
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
name|URL
name|loc
init|=
name|cs
operator|.
name|getLocation
argument_list|()
decl_stmt|;
return|return
name|loc
operator|!=
literal|null
condition|?
name|loc
operator|.
name|toString
argument_list|()
else|:
literal|"(no location)"
return|;
block|}
else|else
block|{
return|return
literal|"(no code source)"
return|;
block|}
block|}
end_function

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_function
unit|}      private
specifier|static
name|boolean
name|doesFileExist
parameter_list|(
specifier|final
name|File
name|f
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|(
name|PrivilegedAction
argument_list|<
name|Boolean
argument_list|>
operator|)
name|f
operator|::
name|exists
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

