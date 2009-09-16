begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Copyright 2009 Marcin.  *  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|webconsole
operator|.
name|gogo
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
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
name|webconsole
operator|.
name|AbstractWebConsolePlugin
import|;
end_import

begin_comment
comment|/**  * TODO: remove this class when upgrading to webconsole 1.2.12  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractResourceAwareWebConsolePlugin
extends|extends
name|AbstractWebConsolePlugin
block|{
specifier|public
specifier|static
specifier|final
name|String
name|GET_RESOURCE_METHOD_NAME
init|=
literal|"getResource"
decl_stmt|;
specifier|private
specifier|final
name|Method
name|getResourceMethod
decl_stmt|;
block|{
name|getResourceMethod
operator|=
name|getGetResourceMethod
argument_list|()
expr_stmt|;
block|}
comment|/**      * Renders the web console page for the request. This consist of the following      * five parts called in order:      *<ol>      *<li>Send back a requested resource      *<li>{@link #startResponse(HttpServletRequest, HttpServletResponse)}</li>      *<li>{@link #renderTopNavigation(HttpServletRequest, PrintWriter)}</li>      *<li>{@link #renderContent(HttpServletRequest, HttpServletResponse)}</li>      *<li>{@link #endResponse(PrintWriter)}</li>      *</ol>      *<p>      *<b>Note</b>: If a resource is sent back for the request only the first      * step is executed. Otherwise the first step is a null-operation actually      * and the latter four steps are executed in order.      */
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|spoolResource
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
condition|)
block|{
name|PrintWriter
name|pw
init|=
name|startResponse
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
decl_stmt|;
name|renderTopNavigation
argument_list|(
name|request
argument_list|,
name|pw
argument_list|)
expr_stmt|;
name|renderContent
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|endResponse
argument_list|(
name|pw
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Object
name|getResourceProvider
parameter_list|()
block|{
return|return
name|this
return|;
block|}
comment|/**      * Returns a method which is called on the      * {@link #getResourceProvider() resource provder} class to return an URL      * to a resource which may be spooled when requested. The method has the      * following signature:      *<pre>      * [modifier] URL getResource(String path);      *</pre>      * Where the<i>[modifier]</i> may be<code>public</code>,<code>protected</code>      * or<code>private</code> (if the method is declared in the class of the      * resource provider). It is suggested to use the<code>private</code>      * modifier if the method is declared in the resource provider class or      * the<code>protected</code> modifier if the method is declared in a      * base class of the resource provider.      *      * @return The<code>getResource(String)</code> method or<code>null</code>      *      if the {@link #getResourceProvider() resource provider} is      *<code>null</code> or does not provide such a method.      */
specifier|private
name|Method
name|getGetResourceMethod
parameter_list|()
block|{
name|Method
name|tmpGetResourceMethod
init|=
literal|null
decl_stmt|;
name|Object
name|resourceProvider
init|=
name|getResourceProvider
argument_list|()
decl_stmt|;
if|if
condition|(
name|resourceProvider
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
name|cl
init|=
name|resourceProvider
operator|.
name|getClass
argument_list|()
decl_stmt|;
while|while
condition|(
name|tmpGetResourceMethod
operator|==
literal|null
operator|&&
name|cl
operator|!=
name|Object
operator|.
name|class
condition|)
block|{
name|Method
index|[]
name|methods
init|=
name|cl
operator|.
name|getDeclaredMethods
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
name|methods
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Method
name|m
init|=
name|methods
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|GET_RESOURCE_METHOD_NAME
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|1
operator|&&
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|==
name|String
operator|.
name|class
operator|&&
name|m
operator|.
name|getReturnType
argument_list|()
operator|==
name|URL
operator|.
name|class
condition|)
block|{
comment|// ensure modifier is protected or public or the private
comment|// method is defined in the plugin class itself
name|int
name|mod
init|=
name|m
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
if|if
condition|(
name|Modifier
operator|.
name|isProtected
argument_list|(
name|mod
argument_list|)
operator|||
name|Modifier
operator|.
name|isPublic
argument_list|(
name|mod
argument_list|)
operator|||
operator|(
name|Modifier
operator|.
name|isPrivate
argument_list|(
name|mod
argument_list|)
operator|&&
name|cl
operator|==
name|resourceProvider
operator|.
name|getClass
argument_list|()
operator|)
condition|)
block|{
name|m
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tmpGetResourceMethod
operator|=
name|m
expr_stmt|;
break|break;
block|}
block|}
block|}
name|cl
operator|=
name|cl
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|tmpGetResourceMethod
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|tmpGetResourceMethod
return|;
block|}
comment|/**      * If the request addresses a resource which may be served by the      *<code>getResource</code> method of the      * {@link #getResourceProvider() resource provider}, this method serves it      * and returns<code>true</code>. Otherwise<code>false</code> is returned.      *<code>false</code> is also returned if the resource provider has no      *<code>getResource</code> method.      *<p>      * If<code>true</code> is returned, the request is considered complete and      * request processing terminates. Otherwise request processing continues      * with normal plugin rendering.      *      * @param request The request object      * @param response The response object      * @return<code>true</code> if the request causes a resource to be sent back.      *      * @throws IOException If an error occurrs accessing or spooling the resource.      */
specifier|private
name|boolean
name|spoolResource
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
block|{
comment|// no resource if no resource accessor
if|if
condition|(
name|getResourceMethod
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|pi
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
name|InputStream
name|ins
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// check for a resource, fail if none
name|URL
name|url
init|=
operator|(
name|URL
operator|)
name|getResourceMethod
operator|.
name|invoke
argument_list|(
name|getResourceProvider
argument_list|()
argument_list|,
operator|new
name|Object
index|[]
block|{
name|pi
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// open the connection and the stream (we use the stream to be able
comment|// to at least hint to close the connection because there is no
comment|// method to explicitly close the conneciton, unfortunately)
name|URLConnection
name|connection
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|ins
operator|=
name|connection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
comment|// check whether we may return 304/UNMODIFIED
name|long
name|lastModified
init|=
name|connection
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastModified
operator|>
literal|0
condition|)
block|{
name|long
name|ifModifiedSince
init|=
name|request
operator|.
name|getDateHeader
argument_list|(
literal|"If-Modified-Since"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ifModifiedSince
operator|>=
operator|(
name|lastModified
operator|/
literal|1000
operator|*
literal|1000
operator|)
condition|)
block|{
comment|// Round down to the nearest second for a proper compare
comment|// A ifModifiedSince of -1 will always be less
name|response
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_MODIFIED
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// have to send, so set the last modified header now
name|response
operator|.
name|setDateHeader
argument_list|(
literal|"Last-Modified"
argument_list|,
name|lastModified
argument_list|)
expr_stmt|;
block|}
comment|// describe the contents
name|response
operator|.
name|setContentType
argument_list|(
name|getServletContext
argument_list|()
operator|.
name|getMimeType
argument_list|(
name|pi
argument_list|)
argument_list|)
expr_stmt|;
name|response
operator|.
name|setIntHeader
argument_list|(
literal|"Content-Length"
argument_list|,
name|connection
operator|.
name|getContentLength
argument_list|()
argument_list|)
expr_stmt|;
comment|// spool the actual contents
name|OutputStream
name|out
init|=
name|response
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|2048
index|]
decl_stmt|;
name|int
name|rd
decl_stmt|;
while|while
condition|(
operator|(
name|rd
operator|=
name|ins
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|rd
argument_list|)
expr_stmt|;
block|}
comment|// over and out ...
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|iae
parameter_list|)
block|{
comment|// log or throw ???
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ite
parameter_list|)
block|{
comment|// log or throw ???
comment|// Throwable cause = ite.getTargetException();
block|}
finally|finally
block|{
if|if
condition|(
name|ins
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{                 }
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

