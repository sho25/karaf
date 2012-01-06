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
name|karaf
operator|.
name|shell
operator|.
name|console
package|;
end_package

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
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
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
name|Proxy
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
name|Formatter
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
name|service
operator|.
name|command
operator|.
name|Converter
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
name|service
operator|.
name|command
operator|.
name|Function
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
name|InvalidSyntaxException
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
name|ServiceReference
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
name|startlevel
operator|.
name|FrameworkStartLevel
import|;
end_import

begin_class
specifier|public
class|class
name|Converters
implements|implements
name|Converter
block|{
specifier|private
specifier|final
name|BundleContext
name|context
decl_stmt|;
specifier|public
name|Converters
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|private
name|CharSequence
name|print
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
comment|// [ ID ] [STATE      ] [ SL ] symname
name|int
name|level
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
operator|.
name|getStartLevel
argument_list|()
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%5d|%-11s|%5d|%s (%s)"
argument_list|,
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|getState
argument_list|(
name|bundle
argument_list|)
argument_list|,
name|level
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|CharSequence
name|print
parameter_list|(
name|ServiceReference
name|ref
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Formatter
name|f
init|=
operator|new
name|Formatter
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|String
name|spid
init|=
literal|""
decl_stmt|;
name|Object
name|pid
init|=
name|ref
operator|.
name|getProperty
argument_list|(
literal|"service.pid"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pid
operator|!=
literal|null
condition|)
block|{
name|spid
operator|=
name|pid
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|f
operator|.
name|format
argument_list|(
literal|"%06d %3s %-40s %s"
argument_list|,
name|ref
operator|.
name|getProperty
argument_list|(
literal|"service.id"
argument_list|)
argument_list|,
name|ref
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|getShortNames
argument_list|(
operator|(
name|String
index|[]
operator|)
name|ref
operator|.
name|getProperty
argument_list|(
literal|"objectclass"
argument_list|)
argument_list|)
argument_list|,
name|spid
argument_list|)
expr_stmt|;
return|return
name|sb
return|;
block|}
specifier|private
name|CharSequence
name|getShortNames
parameter_list|(
name|String
index|[]
name|list
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|del
init|=
literal|""
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|list
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|del
operator|+
name|getShortName
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
name|del
operator|=
literal|" | "
expr_stmt|;
block|}
return|return
name|sb
return|;
block|}
specifier|private
name|CharSequence
name|getShortName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|n
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|<
literal|0
condition|)
block|{
name|n
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
name|n
operator|++
expr_stmt|;
block|}
return|return
name|name
operator|.
name|subSequence
argument_list|(
name|n
argument_list|,
name|name
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|getState
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
switch|switch
condition|(
name|bundle
operator|.
name|getState
argument_list|()
condition|)
block|{
case|case
name|Bundle
operator|.
name|ACTIVE
case|:
return|return
literal|"Active"
return|;
case|case
name|Bundle
operator|.
name|INSTALLED
case|:
return|return
literal|"Installed"
return|;
case|case
name|Bundle
operator|.
name|RESOLVED
case|:
return|return
literal|"Resolved"
return|;
case|case
name|Bundle
operator|.
name|STARTING
case|:
return|return
literal|"Starting"
return|;
case|case
name|Bundle
operator|.
name|STOPPING
case|:
return|return
literal|"Stopping"
return|;
case|case
name|Bundle
operator|.
name|UNINSTALLED
case|:
return|return
literal|"Uninstalled "
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Bundle
name|bundle
parameter_list|(
name|Bundle
name|i
parameter_list|)
block|{
return|return
name|i
return|;
block|}
specifier|public
name|Object
name|convert
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|desiredType
parameter_list|,
specifier|final
name|Object
name|in
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|desiredType
operator|==
name|Bundle
operator|.
name|class
condition|)
block|{
return|return
name|convertBundle
argument_list|(
name|in
argument_list|)
return|;
block|}
if|if
condition|(
name|desiredType
operator|==
name|ServiceReference
operator|.
name|class
condition|)
block|{
return|return
name|convertServiceReference
argument_list|(
name|in
argument_list|)
return|;
block|}
if|if
condition|(
name|desiredType
operator|==
name|Class
operator|.
name|class
condition|)
block|{
try|try
block|{
return|return
name|Class
operator|.
name|forName
argument_list|(
name|in
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
if|if
condition|(
name|desiredType
operator|.
name|isAssignableFrom
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|&&
name|in
operator|instanceof
name|InputStream
condition|)
block|{
return|return
name|read
argument_list|(
operator|(
operator|(
name|InputStream
operator|)
name|in
operator|)
argument_list|)
return|;
block|}
if|if
condition|(
name|in
operator|instanceof
name|Function
operator|&&
name|desiredType
operator|.
name|isInterface
argument_list|()
operator|&&
name|desiredType
operator|.
name|getDeclaredMethods
argument_list|()
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|desiredType
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|desiredType
block|}
argument_list|,
operator|new
name|InvocationHandler
argument_list|()
block|{
name|Function
name|command
init|=
operator|(
operator|(
name|Function
operator|)
name|in
operator|)
decl_stmt|;
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
return|return
name|command
operator|.
name|execute
argument_list|(
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|args
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Object
name|convertServiceReference
parameter_list|(
name|Object
name|in
parameter_list|)
throws|throws
name|InvalidSyntaxException
block|{
name|String
name|s
init|=
name|in
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
operator|&&
name|s
operator|.
name|endsWith
argument_list|(
literal|")"
argument_list|)
condition|)
block|{
name|ServiceReference
name|refs
index|[]
init|=
name|context
operator|.
name|getServiceReferences
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"(|(service.id=%s)(service.pid=%s))"
argument_list|,
name|in
argument_list|,
name|in
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
operator|!=
literal|null
operator|&&
name|refs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|refs
index|[
literal|0
index|]
return|;
block|}
block|}
name|ServiceReference
name|refs
index|[]
init|=
name|context
operator|.
name|getServiceReferences
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"(|(service.id=%s)(service.pid=%s))"
argument_list|,
name|in
argument_list|,
name|in
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
operator|!=
literal|null
operator|&&
name|refs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|refs
index|[
literal|0
index|]
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Object
name|convertBundle
parameter_list|(
name|Object
name|in
parameter_list|)
block|{
name|String
name|s
init|=
name|in
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
block|{
name|long
name|id
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|s
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|getBundle
argument_list|(
name|id
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
comment|// Ignore
block|}
name|Bundle
name|bundles
index|[]
init|=
name|context
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|b
range|:
name|bundles
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|b
return|;
block|}
if|if
condition|(
name|b
operator|.
name|getSymbolicName
argument_list|()
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|b
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|CharSequence
name|format
parameter_list|(
name|Object
name|target
parameter_list|,
name|int
name|level
parameter_list|,
name|Converter
name|converter
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|level
operator|==
name|INSPECT
operator|&&
name|target
operator|instanceof
name|InputStream
condition|)
block|{
return|return
name|read
argument_list|(
operator|(
operator|(
name|InputStream
operator|)
name|target
operator|)
argument_list|)
return|;
block|}
if|if
condition|(
name|level
operator|==
name|LINE
operator|&&
name|target
operator|instanceof
name|Bundle
condition|)
block|{
return|return
name|print
argument_list|(
operator|(
name|Bundle
operator|)
name|target
argument_list|)
return|;
block|}
if|if
condition|(
name|level
operator|==
name|LINE
operator|&&
name|target
operator|instanceof
name|ServiceReference
condition|)
block|{
return|return
name|print
argument_list|(
operator|(
name|ServiceReference
operator|)
name|target
argument_list|)
return|;
block|}
if|if
condition|(
name|level
operator|==
name|PART
operator|&&
name|target
operator|instanceof
name|Bundle
condition|)
block|{
return|return
operator|(
operator|(
name|Bundle
operator|)
name|target
operator|)
operator|.
name|getSymbolicName
argument_list|()
return|;
block|}
if|if
condition|(
name|level
operator|==
name|PART
operator|&&
name|target
operator|instanceof
name|ServiceReference
condition|)
block|{
return|return
name|getShortNames
argument_list|(
operator|(
name|String
index|[]
operator|)
operator|(
operator|(
name|ServiceReference
operator|)
name|target
operator|)
operator|.
name|getProperty
argument_list|(
literal|"objectclass"
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|CharSequence
name|read
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|c
decl_stmt|;
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|in
operator|.
name|read
argument_list|()
operator|)
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|c
operator|>=
literal|32
operator|&&
name|c
operator|<=
literal|0x7F
operator|||
name|c
operator|==
literal|'\n'
operator|||
name|c
operator|==
literal|'\r'
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|s
init|=
name|Integer
operator|.
name|toHexString
argument_list|(
name|c
argument_list|)
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\\"
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|<
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
return|;
block|}
block|}
end_class

end_unit

