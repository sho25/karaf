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
name|http
operator|.
name|core
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|*
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|AbortableHttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|params
operator|.
name|ClientPNames
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|params
operator|.
name|CookiePolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|utils
operator|.
name|URIUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|cookie
operator|.
name|SM
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|InputStreamEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|DefaultHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|conn
operator|.
name|tsccm
operator|.
name|ThreadSafeClientConnManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|BasicHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|BasicHttpEntityEnclosingRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|BasicHttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|HeaderGroup
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|BasicHttpParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|HttpParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|EntityUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|Cookie
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
name|HttpServlet
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
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|OutputStream
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
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpCookie
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|BitSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * This is a simple servlet acting as a HTTP reverse proxy/gateway. It works with any webcontainer as it's a regular servlet.  */
end_comment

begin_class
specifier|public
class|class
name|ProxyServlet
extends|extends
name|HttpServlet
block|{
specifier|private
specifier|final
specifier|static
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ProxyServlet
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|proxyTo
decl_stmt|;
specifier|protected
name|boolean
name|doForwardIP
init|=
literal|true
decl_stmt|;
specifier|protected
name|boolean
name|doSendUrlFragment
init|=
literal|true
decl_stmt|;
specifier|private
name|HttpClient
name|proxyClient
decl_stmt|;
specifier|public
name|void
name|setIPForwarding
parameter_list|(
name|boolean
name|ipForwarding
parameter_list|)
block|{
name|this
operator|.
name|doForwardIP
operator|=
name|ipForwarding
expr_stmt|;
block|}
specifier|public
name|void
name|setProxyTo
parameter_list|(
name|String
name|proxyTo
parameter_list|)
block|{
name|this
operator|.
name|proxyTo
operator|=
name|proxyTo
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServletInfo
parameter_list|()
block|{
return|return
literal|"Apache Karaf Proxy Servlet"
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|ServletException
block|{
name|HttpParams
name|hcParams
init|=
operator|new
name|BasicHttpParams
argument_list|()
decl_stmt|;
name|hcParams
operator|.
name|setParameter
argument_list|(
name|ClientPNames
operator|.
name|COOKIE_POLICY
argument_list|,
name|CookiePolicy
operator|.
name|IGNORE_COOKIES
argument_list|)
expr_stmt|;
name|proxyClient
operator|=
name|createHttpClient
argument_list|(
name|hcParams
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|HttpClient
name|createHttpClient
parameter_list|(
name|HttpParams
name|hcParams
parameter_list|)
block|{
try|try
block|{
name|Class
name|clientClazz
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.http.impl.client.SystemDefaultHttpClient"
argument_list|)
decl_stmt|;
name|Constructor
name|constructor
init|=
name|clientClazz
operator|.
name|getConstructor
argument_list|(
name|HttpParams
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|(
name|HttpClient
operator|)
name|constructor
operator|.
name|newInstance
argument_list|(
name|hcParams
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// not a problem, we fallback on the "old" client
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// fallback on using "old" client style
return|return
operator|new
name|DefaultHttpClient
argument_list|(
operator|new
name|ThreadSafeClientConnManager
argument_list|()
argument_list|,
name|hcParams
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|destroy
parameter_list|()
block|{
comment|// starting for HttpComponents 4.3, clients implements Closeable
if|if
condition|(
name|proxyClient
operator|instanceof
name|Closeable
condition|)
block|{
try|try
block|{
operator|(
operator|(
name|Closeable
operator|)
name|proxyClient
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
argument_list|(
literal|"Error occurred when closing HTTP client in the proxy servlet destroy"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// older HttpComponents version requires to close the client
if|if
condition|(
name|proxyClient
operator|!=
literal|null
condition|)
block|{
name|proxyClient
operator|.
name|getConnectionManager
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
name|super
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|service
parameter_list|(
name|HttpServletRequest
name|servletRequest
parameter_list|,
name|HttpServletResponse
name|servletResponse
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|URI
name|locationUri
init|=
name|URI
operator|.
name|create
argument_list|(
name|proxyTo
argument_list|)
decl_stmt|;
name|HttpHost
name|host
init|=
name|URIUtils
operator|.
name|extractHost
argument_list|(
name|locationUri
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Proxy to {} (host {})"
argument_list|,
name|locationUri
argument_list|,
name|host
argument_list|)
expr_stmt|;
name|String
name|method
init|=
name|servletRequest
operator|.
name|getMethod
argument_list|()
decl_stmt|;
name|String
name|proxyRequestUri
init|=
name|rewriteUrlFromRequest
argument_list|(
name|servletRequest
argument_list|,
name|proxyTo
argument_list|)
decl_stmt|;
name|HttpRequest
name|proxyRequest
decl_stmt|;
comment|// spec: RFC 2616, sec 4.3: either of these two headers means there is a message body
if|if
condition|(
name|servletRequest
operator|.
name|getHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LENGTH
argument_list|)
operator|!=
literal|null
operator|||
name|servletRequest
operator|.
name|getHeader
argument_list|(
name|HttpHeaders
operator|.
name|TRANSFER_ENCODING
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|HttpEntityEnclosingRequest
name|entityProxyRequest
init|=
operator|new
name|BasicHttpEntityEnclosingRequest
argument_list|(
name|method
argument_list|,
name|proxyRequestUri
argument_list|)
decl_stmt|;
name|entityProxyRequest
operator|.
name|setEntity
argument_list|(
operator|new
name|InputStreamEntity
argument_list|(
name|servletRequest
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|servletRequest
operator|.
name|getContentLength
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|proxyRequest
operator|=
name|entityProxyRequest
expr_stmt|;
block|}
else|else
block|{
name|proxyRequest
operator|=
operator|new
name|BasicHttpRequest
argument_list|(
name|method
argument_list|,
name|proxyRequestUri
argument_list|)
expr_stmt|;
block|}
name|copyRequestHeaders
argument_list|(
name|servletRequest
argument_list|,
name|proxyRequest
argument_list|,
name|host
argument_list|)
expr_stmt|;
name|setXForwardedForHeader
argument_list|(
name|servletRequest
argument_list|,
name|proxyRequest
argument_list|)
expr_stmt|;
name|HttpResponse
name|proxyResponse
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// execute the request
name|proxyResponse
operator|=
name|proxyClient
operator|.
name|execute
argument_list|(
name|host
argument_list|,
name|proxyRequest
argument_list|)
expr_stmt|;
comment|// process the response
name|int
name|statusCode
init|=
name|proxyResponse
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
comment|// copying response headers to make sure SESSIONID or other Cookie which comes from the remote host
comment|// will be saved in client when the proxied URL was redirect to another one.
name|copyResponseHeaders
argument_list|(
name|proxyResponse
argument_list|,
name|servletRequest
argument_list|,
name|servletResponse
argument_list|)
expr_stmt|;
if|if
condition|(
name|doResponseRedirect
argument_list|(
name|servletRequest
argument_list|,
name|servletResponse
argument_list|,
name|proxyResponse
argument_list|,
name|statusCode
argument_list|,
name|proxyTo
argument_list|)
condition|)
block|{
comment|// the response is already "committed" now without any body to send
return|return;
block|}
comment|// pass the response code
name|servletResponse
operator|.
name|setStatus
argument_list|(
name|statusCode
argument_list|,
name|proxyResponse
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getReasonPhrase
argument_list|()
argument_list|)
expr_stmt|;
comment|// send the content to the client
name|copyResponseEntity
argument_list|(
name|proxyResponse
argument_list|,
name|servletResponse
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// abort request
if|if
condition|(
name|proxyRequest
operator|instanceof
name|AbortableHttpRequest
condition|)
block|{
name|AbortableHttpRequest
name|abortableHttpRequest
init|=
operator|(
name|AbortableHttpRequest
operator|)
name|proxyRequest
decl_stmt|;
name|abortableHttpRequest
operator|.
name|abort
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|(
name|RuntimeException
operator|)
name|e
throw|;
block|}
if|if
condition|(
name|e
operator|instanceof
name|ServletException
condition|)
block|{
throw|throw
operator|(
name|ServletException
operator|)
name|e
throw|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|proxyResponse
operator|!=
literal|null
condition|)
block|{
name|consumeQuietly
argument_list|(
name|proxyResponse
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|boolean
name|doResponseRedirect
parameter_list|(
name|HttpServletRequest
name|servletRequest
parameter_list|,
name|HttpServletResponse
name|servletResponse
parameter_list|,
name|HttpResponse
name|proxyResponse
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|String
name|proxyTo
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
comment|// check if the proxy is a redirect
if|if
condition|(
name|statusCode
operator|>=
name|HttpServletResponse
operator|.
name|SC_MULTIPLE_CHOICES
operator|&&
name|statusCode
operator|<
name|HttpServletResponse
operator|.
name|SC_NOT_MODIFIED
condition|)
block|{
name|Header
name|locationHeader
init|=
name|proxyResponse
operator|.
name|getLastHeader
argument_list|(
name|HttpHeaders
operator|.
name|LOCATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|locationHeader
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Received a redirect ("
operator|+
name|statusCode
operator|+
literal|") but without location ("
operator|+
name|HttpHeaders
operator|.
name|LOCATION
operator|+
literal|" header)"
argument_list|)
throw|;
block|}
comment|// modify the redirect to go to this proxy servlet rather than the proxied host
name|String
name|locationString
init|=
name|rewriteUrlFromResponse
argument_list|(
name|servletRequest
argument_list|,
name|locationHeader
operator|.
name|getValue
argument_list|()
argument_list|,
name|proxyTo
argument_list|)
decl_stmt|;
name|servletResponse
operator|.
name|sendRedirect
argument_list|(
name|locationString
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// 304 needs special handling.  See:
comment|// http://www.ics.uci.edu/pub/ietf/http/rfc1945.html#Code304
comment|// We get a 304 whenever passed an 'If-Modified-Since'
comment|// header and the data on disk has not changed; server
comment|// responds w/ a 304 saying I'm not going to send the
comment|// body because the file has not changed.
if|if
condition|(
name|statusCode
operator|==
name|HttpServletResponse
operator|.
name|SC_NOT_MODIFIED
condition|)
block|{
name|servletResponse
operator|.
name|setIntHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|servletResponse
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
return|return
literal|false
return|;
block|}
specifier|protected
name|void
name|close
parameter_list|(
name|Closeable
name|closeable
parameter_list|)
block|{
try|try
block|{
name|closeable
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|consumeQuietly
parameter_list|(
name|HttpEntity
name|httpEntity
parameter_list|)
block|{
try|try
block|{
name|EntityUtils
operator|.
name|consume
argument_list|(
name|httpEntity
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * These are the "hop-by-hop" headers that should not be copied.      * http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html      * We use an HttpClient HeaderGroup class instead of Set of String because this      * approach does case insensitive lookup faster.      */
specifier|protected
specifier|static
specifier|final
name|HeaderGroup
name|hopByHopHeaders
decl_stmt|;
static|static
block|{
name|hopByHopHeaders
operator|=
operator|new
name|HeaderGroup
argument_list|()
expr_stmt|;
name|String
index|[]
name|headers
init|=
operator|new
name|String
index|[]
block|{
literal|"Connection"
block|,
literal|"Keep-Alive"
block|,
literal|"Proxy-Authenticate"
block|,
literal|"Proxy-Authorization"
block|,
literal|"TE"
block|,
literal|"Trailers"
block|,
literal|"Transfer-Encoding"
block|,
literal|"Upgrade"
block|}
decl_stmt|;
for|for
control|(
name|String
name|header
range|:
name|headers
control|)
block|{
name|hopByHopHeaders
operator|.
name|addHeader
argument_list|(
operator|new
name|BasicHeader
argument_list|(
name|header
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Copy request headers from the servlet client to the proxy request.      */
specifier|protected
name|void
name|copyRequestHeaders
parameter_list|(
name|HttpServletRequest
name|servletRequest
parameter_list|,
name|HttpRequest
name|proxyRequest
parameter_list|,
name|HttpHost
name|host
parameter_list|)
block|{
name|Enumeration
name|headerNames
init|=
name|servletRequest
operator|.
name|getHeaderNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|headerNames
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|headerName
init|=
operator|(
name|String
operator|)
name|headerNames
operator|.
name|nextElement
argument_list|()
decl_stmt|;
comment|// instead the content-length is effectively set via InputStreamEntity
if|if
condition|(
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LENGTH
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|hopByHopHeaders
operator|.
name|containsHeader
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Enumeration
name|headers
init|=
name|servletRequest
operator|.
name|getHeaders
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
while|while
condition|(
name|headers
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|headerValue
init|=
operator|(
name|String
operator|)
name|headers
operator|.
name|nextElement
argument_list|()
decl_stmt|;
comment|// in case the proxy host is running multiple virtual servers, rewrite the Host header to guarantee we get content from the correct virtual server
if|if
condition|(
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|HttpHeaders
operator|.
name|HOST
argument_list|)
condition|)
block|{
name|headerValue
operator|=
name|host
operator|.
name|getHostName
argument_list|()
expr_stmt|;
if|if
condition|(
name|host
operator|.
name|getPort
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|headerValue
operator|+=
literal|":"
operator|+
name|host
operator|.
name|getPort
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|SM
operator|.
name|COOKIE
argument_list|)
condition|)
block|{
name|headerValue
operator|=
name|getRealCookie
argument_list|(
name|headerValue
argument_list|)
expr_stmt|;
block|}
name|proxyRequest
operator|.
name|addHeader
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|setXForwardedForHeader
parameter_list|(
name|HttpServletRequest
name|servletRequest
parameter_list|,
name|HttpRequest
name|proxyRequest
parameter_list|)
block|{
if|if
condition|(
name|doForwardIP
condition|)
block|{
name|String
name|newHeader
init|=
name|servletRequest
operator|.
name|getRemoteAddr
argument_list|()
decl_stmt|;
name|String
name|existingHeader
init|=
name|servletRequest
operator|.
name|getHeader
argument_list|(
literal|"X-Forwarded-For"
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingHeader
operator|!=
literal|null
condition|)
block|{
name|newHeader
operator|=
name|existingHeader
operator|+
literal|", "
operator|+
name|newHeader
expr_stmt|;
block|}
name|proxyRequest
operator|.
name|setHeader
argument_list|(
literal|"X-Forwarded-For"
argument_list|,
name|newHeader
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|copyResponseHeaders
parameter_list|(
name|HttpResponse
name|proxyResponse
parameter_list|,
name|HttpServletRequest
name|servletRequest
parameter_list|,
name|HttpServletResponse
name|servletResponse
parameter_list|)
block|{
for|for
control|(
name|Header
name|header
range|:
name|proxyResponse
operator|.
name|getAllHeaders
argument_list|()
control|)
block|{
if|if
condition|(
name|hopByHopHeaders
operator|.
name|containsHeader
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|SM
operator|.
name|SET_COOKIE
argument_list|)
operator|||
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|SM
operator|.
name|SET_COOKIE2
argument_list|)
condition|)
block|{
name|copyProxyCookie
argument_list|(
name|servletRequest
argument_list|,
name|servletResponse
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|servletResponse
operator|.
name|setHeader
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Copy cookie from the proxy to the servlet client.      * Replaces cookie path to local path and renames cookie to avoid collisions.      */
specifier|protected
name|void
name|copyProxyCookie
parameter_list|(
name|HttpServletRequest
name|servletRequest
parameter_list|,
name|HttpServletResponse
name|servletResponse
parameter_list|,
name|Header
name|header
parameter_list|)
block|{
name|List
argument_list|<
name|HttpCookie
argument_list|>
name|cookies
init|=
name|HttpCookie
operator|.
name|parse
argument_list|(
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|servletRequest
operator|.
name|getContextPath
argument_list|()
decl_stmt|;
comment|// path starts with / or is empty string
name|path
operator|+=
name|servletRequest
operator|.
name|getServletPath
argument_list|()
expr_stmt|;
comment|// servlet path starts with / or is empty string
for|for
control|(
name|HttpCookie
name|cookie
range|:
name|cookies
control|)
block|{
comment|//set cookie name prefixed w/ a proxy value so it won't collide w/ other cookies
name|String
name|proxyCookieName
init|=
name|getCookieNamePrefix
argument_list|()
operator|+
name|cookie
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Cookie
name|servletCookie
init|=
operator|new
name|Cookie
argument_list|(
name|proxyCookieName
argument_list|,
name|cookie
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|servletCookie
operator|.
name|setComment
argument_list|(
name|cookie
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
name|servletCookie
operator|.
name|setMaxAge
argument_list|(
operator|(
name|int
operator|)
name|cookie
operator|.
name|getMaxAge
argument_list|()
argument_list|)
expr_stmt|;
name|servletCookie
operator|.
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
comment|//set to the path of the proxy servlet
comment|// don't set cookie domain
name|servletCookie
operator|.
name|setSecure
argument_list|(
name|cookie
operator|.
name|getSecure
argument_list|()
argument_list|)
expr_stmt|;
name|servletCookie
operator|.
name|setVersion
argument_list|(
name|cookie
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|servletResponse
operator|.
name|addCookie
argument_list|(
name|servletCookie
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Take any client cookies that were originally from the proxy and prepare them to send to the      * proxy.  This relies on cookie headers being set correctly according to RFC 6265 Sec 5.4.      * This also blocks any local cookies from being sent to the proxy.      */
specifier|protected
name|String
name|getRealCookie
parameter_list|(
name|String
name|cookieValue
parameter_list|)
block|{
name|StringBuilder
name|escapedCookie
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|cookies
index|[]
init|=
name|cookieValue
operator|.
name|split
argument_list|(
literal|"; "
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|cookie
range|:
name|cookies
control|)
block|{
name|String
name|cookieSplit
index|[]
init|=
name|cookie
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|cookieSplit
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|String
name|cookieName
init|=
name|cookieSplit
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|cookieName
operator|.
name|startsWith
argument_list|(
name|getCookieNamePrefix
argument_list|()
argument_list|)
condition|)
block|{
name|cookieName
operator|=
name|cookieName
operator|.
name|substring
argument_list|(
name|getCookieNamePrefix
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|escapedCookie
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|escapedCookie
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
name|escapedCookie
operator|.
name|append
argument_list|(
name|cookieName
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|cookieSplit
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|cookieValue
operator|=
name|escapedCookie
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|cookieValue
return|;
block|}
comment|/**      * The string prefixing rewritten cookies.      */
specifier|protected
name|String
name|getCookieNamePrefix
parameter_list|()
block|{
return|return
literal|"!Proxy!"
operator|+
name|getServletConfig
argument_list|()
operator|.
name|getServletName
argument_list|()
return|;
block|}
comment|/**      * Copy response body data (the entity) from the proxy to the servlet client.      */
specifier|protected
name|void
name|copyResponseEntity
parameter_list|(
name|HttpResponse
name|proxyResponse
parameter_list|,
name|HttpServletResponse
name|servletResponse
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpEntity
name|entity
init|=
name|proxyResponse
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|OutputStream
name|servletOutputStream
init|=
name|servletResponse
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|entity
operator|.
name|writeTo
argument_list|(
name|servletOutputStream
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Reads the request URI from {@code servletRequest} and rewrites it, considering targetUri.      * It's used to make the new request.      */
specifier|protected
name|String
name|rewriteUrlFromRequest
parameter_list|(
name|HttpServletRequest
name|servletRequest
parameter_list|,
name|String
name|location
parameter_list|)
block|{
name|StringBuilder
name|uri
init|=
operator|new
name|StringBuilder
argument_list|(
literal|500
argument_list|)
decl_stmt|;
name|uri
operator|.
name|append
argument_list|(
name|location
argument_list|)
expr_stmt|;
comment|// Handle the path given to the servlet
if|if
condition|(
name|servletRequest
operator|.
name|getPathInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|//ex: /my/path.html
name|uri
operator|.
name|append
argument_list|(
name|encodeUriQuery
argument_list|(
name|servletRequest
operator|.
name|getPathInfo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Handle the query string& fragment
name|String
name|queryString
init|=
name|servletRequest
operator|.
name|getQueryString
argument_list|()
decl_stmt|;
comment|//ex:(following '?'): name=value&foo=bar#fragment
name|String
name|fragment
init|=
literal|null
decl_stmt|;
comment|//split off fragment from queryString, updating queryString if found
if|if
condition|(
name|queryString
operator|!=
literal|null
condition|)
block|{
name|int
name|fragIdx
init|=
name|queryString
operator|.
name|indexOf
argument_list|(
literal|'#'
argument_list|)
decl_stmt|;
if|if
condition|(
name|fragIdx
operator|>=
literal|0
condition|)
block|{
name|fragment
operator|=
name|queryString
operator|.
name|substring
argument_list|(
name|fragIdx
operator|+
literal|1
argument_list|)
expr_stmt|;
name|queryString
operator|=
name|queryString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fragIdx
argument_list|)
expr_stmt|;
block|}
block|}
name|queryString
operator|=
name|rewriteQueryStringFromRequest
argument_list|(
name|servletRequest
argument_list|,
name|queryString
argument_list|)
expr_stmt|;
if|if
condition|(
name|queryString
operator|!=
literal|null
operator|&&
name|queryString
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|uri
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
name|uri
operator|.
name|append
argument_list|(
name|encodeUriQuery
argument_list|(
name|queryString
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|doSendUrlFragment
operator|&&
name|fragment
operator|!=
literal|null
condition|)
block|{
name|uri
operator|.
name|append
argument_list|(
literal|'#'
argument_list|)
expr_stmt|;
name|uri
operator|.
name|append
argument_list|(
name|encodeUriQuery
argument_list|(
name|fragment
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|uri
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|String
name|rewriteQueryStringFromRequest
parameter_list|(
name|HttpServletRequest
name|servletRequest
parameter_list|,
name|String
name|queryString
parameter_list|)
block|{
return|return
name|queryString
return|;
block|}
comment|/**      * For a redirect response from the target server, this translates {@code theUrl} to redirect to      * and translates it to one the original client can use.      */
specifier|protected
name|String
name|rewriteUrlFromResponse
parameter_list|(
name|HttpServletRequest
name|servletRequest
parameter_list|,
name|String
name|theUrl
parameter_list|,
name|String
name|location
parameter_list|)
block|{
if|if
condition|(
name|theUrl
operator|.
name|startsWith
argument_list|(
name|location
argument_list|)
condition|)
block|{
name|String
name|curUrl
init|=
name|servletRequest
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// no query
name|String
name|pathInfo
init|=
name|servletRequest
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathInfo
operator|!=
literal|null
condition|)
block|{
assert|assert
name|curUrl
operator|.
name|endsWith
argument_list|(
name|pathInfo
argument_list|)
assert|;
name|curUrl
operator|=
name|curUrl
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|curUrl
operator|.
name|length
argument_list|()
operator|-
name|pathInfo
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// take pathInfo off
block|}
name|theUrl
operator|=
name|curUrl
operator|+
name|theUrl
operator|.
name|substring
argument_list|(
name|location
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|theUrl
return|;
block|}
comment|/**      * Encodes characters in the query or fragment part of the URI.      *      *<p>Unfortunately, an incoming URI sometimes has characters disallowed by the spec.  HttpClient      * insists that the outgoing proxied request has a valid URI because it uses Java's {@link URI}.      * To be more forgiving, we must escape the problematic characters.  See the URI class for the      * spec.</p>      *      * @param in The {@link CharSequence} to encode.      */
specifier|protected
specifier|static
name|CharSequence
name|encodeUriQuery
parameter_list|(
name|CharSequence
name|in
parameter_list|)
block|{
comment|//Note that I can't simply use URI.java to encode because it will escape pre-existing escaped things.
name|StringBuilder
name|outBuf
init|=
literal|null
decl_stmt|;
name|Formatter
name|formatter
init|=
literal|null
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
name|in
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|in
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|boolean
name|escape
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|128
condition|)
block|{
if|if
condition|(
name|asciiQueryChars
operator|.
name|get
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|escape
operator|=
literal|false
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|Character
operator|.
name|isISOControl
argument_list|(
name|c
argument_list|)
operator|&&
operator|!
name|Character
operator|.
name|isSpaceChar
argument_list|(
name|c
argument_list|)
condition|)
block|{
comment|//not-ascii
name|escape
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|escape
condition|)
block|{
if|if
condition|(
name|outBuf
operator|!=
literal|null
condition|)
name|outBuf
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//escape
if|if
condition|(
name|outBuf
operator|==
literal|null
condition|)
block|{
name|outBuf
operator|=
operator|new
name|StringBuilder
argument_list|(
name|in
operator|.
name|length
argument_list|()
operator|+
literal|5
operator|*
literal|3
argument_list|)
expr_stmt|;
name|outBuf
operator|.
name|append
argument_list|(
name|in
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|formatter
operator|=
operator|new
name|Formatter
argument_list|(
name|outBuf
argument_list|)
expr_stmt|;
block|}
comment|//leading %, 0 padded, width 2, capital hex
name|formatter
operator|.
name|format
argument_list|(
literal|"%%%02X"
argument_list|,
operator|(
name|int
operator|)
name|c
argument_list|)
expr_stmt|;
comment|//TODO
block|}
block|}
return|return
name|outBuf
operator|!=
literal|null
condition|?
name|outBuf
else|:
name|in
return|;
block|}
specifier|protected
specifier|static
specifier|final
name|BitSet
name|asciiQueryChars
decl_stmt|;
static|static
block|{
name|char
index|[]
name|c_unreserved
init|=
literal|"_-!.~'()*"
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
comment|//plus alphanum
name|char
index|[]
name|c_punct
init|=
literal|",;:$&+="
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|char
index|[]
name|c_reserved
init|=
literal|"?/[]@"
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
comment|//plus punct
name|asciiQueryChars
operator|=
operator|new
name|BitSet
argument_list|(
literal|128
argument_list|)
expr_stmt|;
for|for
control|(
name|char
name|c
init|=
literal|'a'
init|;
name|c
operator|<=
literal|'z'
condition|;
name|c
operator|++
control|)
name|asciiQueryChars
operator|.
name|set
argument_list|(
name|c
argument_list|)
expr_stmt|;
for|for
control|(
name|char
name|c
init|=
literal|'A'
init|;
name|c
operator|<=
literal|'Z'
condition|;
name|c
operator|++
control|)
name|asciiQueryChars
operator|.
name|set
argument_list|(
name|c
argument_list|)
expr_stmt|;
for|for
control|(
name|char
name|c
init|=
literal|'0'
init|;
name|c
operator|<=
literal|'9'
condition|;
name|c
operator|++
control|)
name|asciiQueryChars
operator|.
name|set
argument_list|(
name|c
argument_list|)
expr_stmt|;
for|for
control|(
name|char
name|c
range|:
name|c_unreserved
control|)
name|asciiQueryChars
operator|.
name|set
argument_list|(
name|c
argument_list|)
expr_stmt|;
for|for
control|(
name|char
name|c
range|:
name|c_punct
control|)
name|asciiQueryChars
operator|.
name|set
argument_list|(
name|c
argument_list|)
expr_stmt|;
for|for
control|(
name|char
name|c
range|:
name|c_reserved
control|)
name|asciiQueryChars
operator|.
name|set
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|asciiQueryChars
operator|.
name|set
argument_list|(
literal|'%'
argument_list|)
expr_stmt|;
comment|//leave existing percent escapes in place
block|}
block|}
end_class

end_unit

