begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|javax
operator|.
name|xml
operator|.
name|bind
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|Method
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|ConsoleHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
operator|.
name|JAXB_CONTEXT_FACTORY
import|;
end_import

begin_comment
comment|//import java.lang.reflect.InvocationTargetException;
end_comment

begin_comment
comment|/**  * This class is package private and therefore is not exposed as part of the   * JAXB API.  *  * This code is designed to implement the JAXB 1.0 spec pluggability feature  *  * @author<ul><li>Ryan Shoemaker, Sun Microsystems, Inc.</li></ul>  * @see JAXBContext  */
end_comment

begin_class
class|class
name|ContextFinder
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
decl_stmt|;
static|static
block|{
name|logger
operator|=
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"javax.xml.bind"
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|GetPropertyAction
argument_list|(
literal|"jaxb.debug"
argument_list|)
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// disconnect the logger from a bigger framework (if any)
comment|// and take the matters into our own hands
name|logger
operator|.
name|setUseParentHandlers
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|ALL
argument_list|)
expr_stmt|;
name|ConsoleHandler
name|handler
init|=
operator|new
name|ConsoleHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|ALL
argument_list|)
expr_stmt|;
name|logger
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// don't change the setting of this logger
comment|// to honor what other frameworks
comment|// have done on configurations.
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// just to be extra safe. in particular System.getProperty may throw
comment|// SecurityException.
block|}
block|}
comment|/**      * If the {@link InvocationTargetException} wraps an exception that shouldn't be wrapped,      * throw the wrapped exception.      */
specifier|private
specifier|static
name|void
name|handleInvocationTargetException
parameter_list|(
name|InvocationTargetException
name|x
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Throwable
name|t
init|=
name|x
operator|.
name|getTargetException
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|t
operator|instanceof
name|JAXBException
condition|)
comment|// one of our exceptions, just re-throw
throw|throw
operator|(
name|JAXBException
operator|)
name|t
throw|;
if|if
condition|(
name|t
operator|instanceof
name|RuntimeException
condition|)
comment|// avoid wrapping exceptions unnecessarily
throw|throw
operator|(
name|RuntimeException
operator|)
name|t
throw|;
if|if
condition|(
name|t
operator|instanceof
name|Error
condition|)
throw|throw
operator|(
name|Error
operator|)
name|t
throw|;
block|}
block|}
comment|/**      * Determine if two types (JAXBContext in this case) will generate a ClassCastException.      *      * For example, (targetType)originalType      *      * @param originalType      *          The Class object of the type being cast      * @param targetType      *          The Class object of the type that is being cast to      * @return JAXBException to be thrown.      */
specifier|private
specifier|static
name|JAXBException
name|handleClassCastException
parameter_list|(
name|Class
name|originalType
parameter_list|,
name|Class
name|targetType
parameter_list|)
block|{
specifier|final
name|URL
name|targetTypeURL
init|=
name|which
argument_list|(
name|targetType
argument_list|)
decl_stmt|;
name|ClassLoader
name|cl
init|=
name|originalType
operator|.
name|getClassLoader
argument_list|()
operator|!=
literal|null
condition|?
name|originalType
operator|.
name|getClassLoader
argument_list|()
else|:
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
decl_stmt|;
return|return
operator|new
name|JAXBException
argument_list|(
name|Messages
operator|.
name|format
argument_list|(
name|Messages
operator|.
name|ILLEGAL_CAST
argument_list|,
comment|// we don't care where the impl class is, we want to know where JAXBContext lives in the impl
comment|// class' ClassLoader
name|cl
operator|.
name|getResource
argument_list|(
literal|"javax/xml/bind/JAXBContext.class"
argument_list|)
argument_list|,
name|targetTypeURL
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Create an instance of a class using the specified ClassLoader      */
specifier|static
name|JAXBContext
name|newInstance
parameter_list|(
name|String
name|contextPath
parameter_list|,
name|String
name|className
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|,
name|Map
name|properties
parameter_list|)
throws|throws
name|JAXBException
block|{
try|try
block|{
name|Class
name|spiClass
init|=
name|safeLoadClass
argument_list|(
name|className
argument_list|,
name|classLoader
argument_list|)
decl_stmt|;
comment|/*              * javax.xml.bind.context.factory points to a class which has a              * static method called 'createContext' that              * returns a javax.xml.JAXBContext.              */
name|Object
name|context
init|=
literal|null
decl_stmt|;
comment|// first check the method that takes Map as the third parameter.
comment|// this is added in 2.0.
try|try
block|{
name|Method
name|m
init|=
name|spiClass
operator|.
name|getMethod
argument_list|(
literal|"createContext"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|ClassLoader
operator|.
name|class
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Throw an early exception instead of having an exception thrown in the createContext method
if|if
condition|(
name|m
operator|.
name|getReturnType
argument_list|()
operator|!=
name|JAXBContext
operator|.
name|class
condition|)
block|{
throw|throw
name|handleClassCastException
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|,
name|JAXBContext
operator|.
name|class
argument_list|)
throw|;
block|}
comment|// any failure in invoking this method would be considered fatal
name|context
operator|=
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|contextPath
argument_list|,
name|classLoader
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// it's not an error for the provider not to have this method.
block|}
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
comment|// try the old method that doesn't take properties. compatible with 1.0.
comment|// it is an error for an implementation not to have both forms of the createContext method.
name|Method
name|m
init|=
name|spiClass
operator|.
name|getMethod
argument_list|(
literal|"createContext"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Throw an early exception instead of having an exception thrown in the createContext method
if|if
condition|(
name|m
operator|.
name|getReturnType
argument_list|()
operator|!=
name|JAXBContext
operator|.
name|class
condition|)
block|{
throw|throw
name|handleClassCastException
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|,
name|JAXBContext
operator|.
name|class
argument_list|)
throw|;
block|}
comment|// any failure in invoking this method would be considered fatal
name|context
operator|=
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|contextPath
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|context
operator|instanceof
name|JAXBContext
operator|)
condition|)
block|{
comment|// the cast would fail, so generate an exception with a nice message
throw|throw
name|handleClassCastException
argument_list|(
name|context
operator|.
name|getClass
argument_list|()
argument_list|,
name|JAXBContext
operator|.
name|class
argument_list|)
throw|;
block|}
return|return
operator|(
name|JAXBContext
operator|)
name|context
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|x
parameter_list|)
block|{
throw|throw
operator|new
name|JAXBException
argument_list|(
name|Messages
operator|.
name|format
argument_list|(
name|Messages
operator|.
name|PROVIDER_NOT_FOUND
argument_list|,
name|className
argument_list|)
argument_list|,
name|x
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|x
parameter_list|)
block|{
name|handleInvocationTargetException
argument_list|(
name|x
argument_list|)
expr_stmt|;
comment|// for other exceptions, wrap the internal target exception
comment|// with a JAXBException
name|Throwable
name|e
init|=
name|x
decl_stmt|;
if|if
condition|(
name|x
operator|.
name|getTargetException
argument_list|()
operator|!=
literal|null
condition|)
name|e
operator|=
name|x
operator|.
name|getTargetException
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|JAXBException
argument_list|(
name|Messages
operator|.
name|format
argument_list|(
name|Messages
operator|.
name|COULD_NOT_INSTANTIATE
argument_list|,
name|className
argument_list|,
name|e
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|x
parameter_list|)
block|{
comment|// avoid wrapping RuntimeException to JAXBException,
comment|// because it indicates a bug in this code.
throw|throw
name|x
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|x
parameter_list|)
block|{
comment|// can't catch JAXBException because the method is hidden behind
comment|// reflection.  Root element collisions detected in the call to
comment|// createContext() are reported as JAXBExceptions - just re-throw it
comment|// some other type of exception - just wrap it
throw|throw
operator|new
name|JAXBException
argument_list|(
name|Messages
operator|.
name|format
argument_list|(
name|Messages
operator|.
name|COULD_NOT_INSTANTIATE
argument_list|,
name|className
argument_list|,
name|x
argument_list|)
argument_list|,
name|x
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create an instance of a class using the specified ClassLoader      */
specifier|static
name|JAXBContext
name|newInstance
parameter_list|(
name|Class
index|[]
name|classes
parameter_list|,
name|Map
name|properties
parameter_list|,
name|String
name|className
parameter_list|)
throws|throws
name|JAXBException
block|{
name|ClassLoader
name|cl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|Class
name|spi
decl_stmt|;
try|try
block|{
name|spi
operator|=
name|safeLoadClass
argument_list|(
name|className
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JAXBException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
comment|// extra check to avoid costly which operation if not logged
name|logger
operator|.
name|fine
argument_list|(
literal|"loaded "
operator|+
name|className
operator|+
literal|" from "
operator|+
name|which
argument_list|(
name|spi
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Method
name|m
decl_stmt|;
try|try
block|{
name|m
operator|=
name|spi
operator|.
name|getMethod
argument_list|(
literal|"createContext"
argument_list|,
name|Class
index|[]
operator|.
expr|class
argument_list|,
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JAXBException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// Fallback for JAXB 1.0 compatibility (at least JAXB TCK tests are using that feature)
try|try
block|{
name|Object
name|context
init|=
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|classes
argument_list|,
name|properties
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|context
operator|instanceof
name|JAXBContext
operator|)
condition|)
block|{
comment|// the cast would fail, so generate an exception with a nice message
throw|throw
name|handleClassCastException
argument_list|(
name|context
operator|.
name|getClass
argument_list|()
argument_list|,
name|JAXBContext
operator|.
name|class
argument_list|)
throw|;
block|}
return|return
operator|(
name|JAXBContext
operator|)
name|context
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JAXBException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
name|handleInvocationTargetException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|Throwable
name|x
init|=
name|e
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getTargetException
argument_list|()
operator|!=
literal|null
condition|)
name|x
operator|=
name|e
operator|.
name|getTargetException
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|JAXBException
argument_list|(
name|x
argument_list|)
throw|;
block|}
block|}
specifier|static
name|JAXBContext
name|find
parameter_list|(
name|String
name|factoryId
parameter_list|,
name|String
name|contextPath
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|,
name|Map
name|properties
parameter_list|)
throws|throws
name|JAXBException
block|{
comment|// TODO: do we want/need another layer of searching in $java.home/lib/jaxb.properties like JAXP?
specifier|final
name|String
name|jaxbContextFQCN
init|=
name|JAXBContext
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// search context path for jaxb.properties first
name|StringBuilder
name|propFileName
decl_stmt|;
name|StringTokenizer
name|packages
init|=
operator|new
name|StringTokenizer
argument_list|(
name|contextPath
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|factoryClassName
decl_stmt|;
if|if
condition|(
operator|!
name|packages
operator|.
name|hasMoreTokens
argument_list|()
condition|)
comment|// no context is specified
throw|throw
operator|new
name|JAXBException
argument_list|(
name|Messages
operator|.
name|format
argument_list|(
name|Messages
operator|.
name|NO_PACKAGE_IN_CONTEXTPATH
argument_list|)
argument_list|)
throw|;
name|logger
operator|.
name|fine
argument_list|(
literal|"Searching jaxb.properties"
argument_list|)
expr_stmt|;
while|while
condition|(
name|packages
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|packageName
init|=
name|packages
operator|.
name|nextToken
argument_list|(
literal|":"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
comment|// com.acme.foo -> com/acme/foo/jaxb.properties
name|propFileName
operator|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|packageName
argument_list|)
operator|.
name|append
argument_list|(
literal|"/jaxb.properties"
argument_list|)
expr_stmt|;
name|Properties
name|props
init|=
name|loadJAXBProperties
argument_list|(
name|classLoader
argument_list|,
name|propFileName
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|props
operator|.
name|containsKey
argument_list|(
name|factoryId
argument_list|)
condition|)
block|{
name|factoryClassName
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|factoryId
argument_list|)
expr_stmt|;
return|return
name|newInstance
argument_list|(
name|contextPath
argument_list|,
name|factoryClassName
argument_list|,
name|classLoader
argument_list|,
name|properties
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|JAXBException
argument_list|(
name|Messages
operator|.
name|format
argument_list|(
name|Messages
operator|.
name|MISSING_PROPERTY
argument_list|,
name|packageName
argument_list|,
name|factoryId
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
name|logger
operator|.
name|fine
argument_list|(
literal|"Searching the system property"
argument_list|)
expr_stmt|;
comment|// search for a system property second (javax.xml.bind.JAXBContext)
name|factoryClassName
operator|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|GetPropertyAction
argument_list|(
name|jaxbContextFQCN
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|factoryClassName
operator|!=
literal|null
condition|)
block|{
return|return
name|newInstance
argument_list|(
name|contextPath
argument_list|,
name|factoryClassName
argument_list|,
name|classLoader
argument_list|,
name|properties
argument_list|)
return|;
block|}
name|logger
operator|.
name|fine
argument_list|(
literal|"Searching META-INF/services"
argument_list|)
expr_stmt|;
comment|// search META-INF services next
name|BufferedReader
name|r
decl_stmt|;
try|try
block|{
specifier|final
name|StringBuilder
name|resource
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"META-INF/services/"
argument_list|)
operator|.
name|append
argument_list|(
name|jaxbContextFQCN
argument_list|)
decl_stmt|;
specifier|final
name|InputStream
name|resourceStream
init|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
name|resource
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceStream
operator|!=
literal|null
condition|)
block|{
name|r
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|resourceStream
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|factoryClassName
operator|=
name|r
operator|.
name|readLine
argument_list|()
operator|.
name|trim
argument_list|()
expr_stmt|;
name|r
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|newInstance
argument_list|(
name|contextPath
argument_list|,
name|factoryClassName
argument_list|,
name|classLoader
argument_list|,
name|properties
argument_list|)
return|;
block|}
else|else
block|{
name|logger
operator|.
name|fine
argument_list|(
literal|"Unable to load:"
operator|+
name|resource
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JAXBException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// else no provider found
name|logger
operator|.
name|fine
argument_list|(
literal|"Trying to create the platform default provider"
argument_list|)
expr_stmt|;
return|return
name|newInstance
argument_list|(
name|contextPath
argument_list|,
name|PLATFORM_DEFAULT_FACTORY_CLASS
argument_list|,
name|classLoader
argument_list|,
name|properties
argument_list|)
return|;
block|}
comment|// TODO: log each step in the look up process
specifier|static
name|JAXBContext
name|find
parameter_list|(
name|Class
index|[]
name|classes
parameter_list|,
name|Map
name|properties
parameter_list|)
throws|throws
name|JAXBException
block|{
comment|// TODO: do we want/need another layer of searching in $java.home/lib/jaxb.properties like JAXP?
specifier|final
name|String
name|jaxbContextFQCN
init|=
name|JAXBContext
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|factoryClassName
decl_stmt|;
comment|// search for jaxb.properties in the class loader of each class first
for|for
control|(
specifier|final
name|Class
name|c
range|:
name|classes
control|)
block|{
comment|// this classloader is used only to load jaxb.properties, so doing this should be safe.
name|ClassLoader
name|classLoader
init|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
block|{
specifier|public
name|ClassLoader
name|run
parameter_list|()
block|{
return|return
name|c
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Package
name|pkg
init|=
name|c
operator|.
name|getPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|pkg
operator|==
literal|null
condition|)
continue|continue;
comment|// this is possible for primitives, arrays, and classes that are loaded by poorly implemented ClassLoaders
name|String
name|packageName
init|=
name|pkg
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
comment|// TODO: do we want to optimize away searching the same package?  org.Foo, org.Bar, com.Baz
comment|//       classes from the same package might come from different class loades, so it might be a bad idea
comment|// TODO: it's easier to look things up from the class
comment|// c.getResourceAsStream("jaxb.properties");
comment|// build the resource name and use the property loader code
name|String
name|resourceName
init|=
name|packageName
operator|+
literal|"/jaxb.properties"
decl_stmt|;
name|logger
operator|.
name|fine
argument_list|(
literal|"Trying to locate "
operator|+
name|resourceName
argument_list|)
expr_stmt|;
name|Properties
name|props
init|=
name|loadJAXBProperties
argument_list|(
name|classLoader
argument_list|,
name|resourceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|fine
argument_list|(
literal|"  not found"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|fine
argument_list|(
literal|"  found"
argument_list|)
expr_stmt|;
if|if
condition|(
name|props
operator|.
name|containsKey
argument_list|(
name|JAXB_CONTEXT_FACTORY
argument_list|)
condition|)
block|{
comment|// trim() seems redundant, but adding to satisfy customer complaint
name|factoryClassName
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|JAXB_CONTEXT_FACTORY
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
return|return
name|newInstance
argument_list|(
name|classes
argument_list|,
name|properties
argument_list|,
name|factoryClassName
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|JAXBException
argument_list|(
name|Messages
operator|.
name|format
argument_list|(
name|Messages
operator|.
name|MISSING_PROPERTY
argument_list|,
name|packageName
argument_list|,
name|JAXB_CONTEXT_FACTORY
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
comment|// search for a system property second (javax.xml.bind.JAXBContext)
name|logger
operator|.
name|fine
argument_list|(
literal|"Checking system property "
operator|+
name|jaxbContextFQCN
argument_list|)
expr_stmt|;
name|factoryClassName
operator|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|GetPropertyAction
argument_list|(
name|jaxbContextFQCN
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|factoryClassName
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|fine
argument_list|(
literal|"  found "
operator|+
name|factoryClassName
argument_list|)
expr_stmt|;
return|return
name|newInstance
argument_list|(
name|classes
argument_list|,
name|properties
argument_list|,
name|factoryClassName
argument_list|)
return|;
block|}
name|logger
operator|.
name|fine
argument_list|(
literal|"  not found"
argument_list|)
expr_stmt|;
comment|// search META-INF services next
name|logger
operator|.
name|fine
argument_list|(
literal|"Checking META-INF/services"
argument_list|)
expr_stmt|;
name|BufferedReader
name|r
decl_stmt|;
try|try
block|{
specifier|final
name|String
name|resource
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"META-INF/services/"
argument_list|)
operator|.
name|append
argument_list|(
name|jaxbContextFQCN
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ClassLoader
name|classLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|URL
name|resourceURL
decl_stmt|;
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
name|resourceURL
operator|=
name|ClassLoader
operator|.
name|getSystemResource
argument_list|(
name|resource
argument_list|)
expr_stmt|;
else|else
name|resourceURL
operator|=
name|classLoader
operator|.
name|getResource
argument_list|(
name|resource
argument_list|)
expr_stmt|;
if|if
condition|(
name|resourceURL
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|fine
argument_list|(
literal|"Reading "
operator|+
name|resourceURL
argument_list|)
expr_stmt|;
name|r
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|resourceURL
operator|.
name|openStream
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|factoryClassName
operator|=
name|r
operator|.
name|readLine
argument_list|()
operator|.
name|trim
argument_list|()
expr_stmt|;
return|return
name|newInstance
argument_list|(
name|classes
argument_list|,
name|properties
argument_list|,
name|factoryClassName
argument_list|)
return|;
block|}
else|else
block|{
name|logger
operator|.
name|fine
argument_list|(
literal|"Unable to find: "
operator|+
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JAXBException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// else no provider found
name|logger
operator|.
name|fine
argument_list|(
literal|"Trying to create the platform default provider"
argument_list|)
expr_stmt|;
return|return
name|newInstance
argument_list|(
name|classes
argument_list|,
name|properties
argument_list|,
name|PLATFORM_DEFAULT_FACTORY_CLASS
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Properties
name|loadJAXBProperties
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|,
name|String
name|propFileName
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Properties
name|props
init|=
literal|null
decl_stmt|;
try|try
block|{
name|URL
name|url
decl_stmt|;
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
name|url
operator|=
name|ClassLoader
operator|.
name|getSystemResource
argument_list|(
name|propFileName
argument_list|)
expr_stmt|;
else|else
name|url
operator|=
name|classLoader
operator|.
name|getResource
argument_list|(
name|propFileName
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|fine
argument_list|(
literal|"loading props from "
operator|+
name|url
argument_list|)
expr_stmt|;
name|props
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Unable to load "
operator|+
name|propFileName
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JAXBException
argument_list|(
name|ioe
operator|.
name|toString
argument_list|()
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
return|return
name|props
return|;
block|}
comment|/**      * Search the given ClassLoader for an instance of the specified class and      * return a string representation of the URL that points to the resource.      *      * @param clazz      *          The class to search for      * @param loader      *          The ClassLoader to search.  If this parameter is null, then the      *          system class loader will be searched      * @return      *          the URL for the class or null if it wasn't found      */
specifier|static
name|URL
name|which
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
name|String
name|classnameAsResource
init|=
name|clazz
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|".class"
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
expr_stmt|;
block|}
return|return
name|loader
operator|.
name|getResource
argument_list|(
name|classnameAsResource
argument_list|)
return|;
block|}
comment|/**      * Get the URL for the Class from it's ClassLoader.      *      * Convenience method for {@link #which(Class, ClassLoader)}.      *      * Equivalent to calling: which(clazz, clazz.getClassLoader())      *      * @param clazz      *          The class to search for      * @return      *          the URL for the class or null if it wasn't found      */
specifier|static
name|URL
name|which
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
return|return
name|which
argument_list|(
name|clazz
argument_list|,
name|clazz
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * When JAXB is in J2SE, rt.jar has to have a JAXB implementation.      * However, rt.jar cannot have META-INF/services/javax.xml.bind.JAXBContext      * because if it has, it will take precedence over any file that applications have      * in their jar files.      *      *<p>      * When the user bundles his own JAXB implementation, we'd like to use it, and we      * want the platform default to be used only when there's no other JAXB provider.      *      *<p>      * For this reason, we have to hard-code the class name into the API.      */
specifier|private
specifier|static
specifier|final
name|String
name|PLATFORM_DEFAULT_FACTORY_CLASS
init|=
literal|"com.sun.xml.internal.bind.v2.ContextFactory"
decl_stmt|;
comment|/**      * Loads the class, provided that the calling thread has an access to the class being loaded.      */
specifier|private
specifier|static
name|Class
name|safeLoadClass
parameter_list|(
name|String
name|className
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
comment|// using Osig locator to load the spi class
try|try
block|{
name|Class
name|spiClass
init|=
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|specs
operator|.
name|locator
operator|.
name|OsgiLocator
operator|.
name|locate
argument_list|(
name|JAXBContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|spiClass
operator|!=
literal|null
condition|)
block|{
return|return
name|spiClass
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{        }
name|logger
operator|.
name|fine
argument_list|(
literal|"Trying to load "
operator|+
name|className
argument_list|)
expr_stmt|;
try|try
block|{
comment|// make sure that the current thread has an access to the package of the given name.
name|SecurityManager
name|s
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|int
name|i
init|=
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
name|s
operator|.
name|checkPackageAccess
argument_list|(
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
block|{
return|return
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|classLoader
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|se
parameter_list|)
block|{
comment|// anyone can access the platform default factory class without permission
if|if
condition|(
name|PLATFORM_DEFAULT_FACTORY_CLASS
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|)
return|;
block|}
throw|throw
name|se
throw|;
block|}
block|}
block|}
end_class

end_unit

