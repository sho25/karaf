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
name|webconsole
operator|.
name|http
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
name|io
operator|.
name|PrintWriter
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
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Servlet
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
name|WebConsoleConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|json
operator|.
name|JSONException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|json
operator|.
name|JSONWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|ServletEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|WebEvent
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

begin_comment
comment|/**  * WebConsole plugin to use with HTTP service.  */
end_comment

begin_class
specifier|public
class|class
name|HttpPlugin
extends|extends
name|AbstractWebConsolePlugin
block|{
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|HttpPlugin
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"http"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LABEL
init|=
literal|"Http"
decl_stmt|;
specifier|private
name|ClassLoader
name|classLoader
decl_stmt|;
specifier|private
name|String
name|featuresJs
init|=
literal|"/http/res/ui/http-contexts.js"
decl_stmt|;
specifier|private
name|ServletEventHandler
name|servletEventHandler
decl_stmt|;
specifier|private
name|WebEventHandler
name|webEventHandler
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|boolean
name|isHtmlRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|start
parameter_list|()
block|{
name|super
operator|.
name|activate
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|classLoader
operator|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
name|this
operator|.
name|log
operator|.
name|info
argument_list|(
name|LABEL
operator|+
literal|" plugin activated"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|this
operator|.
name|log
operator|.
name|info
argument_list|(
name|LABEL
operator|+
literal|" plugin deactivated"
argument_list|)
expr_stmt|;
name|super
operator|.
name|deactivate
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|NAME
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|LABEL
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|renderContent
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
comment|// get request info from request attribute
specifier|final
name|PrintWriter
name|pw
init|=
name|response
operator|.
name|getWriter
argument_list|()
decl_stmt|;
name|String
name|appRoot
init|=
operator|(
name|String
operator|)
name|request
operator|.
name|getAttribute
argument_list|(
name|WebConsoleConstants
operator|.
name|ATTR_APP_ROOT
argument_list|)
decl_stmt|;
specifier|final
name|String
name|featuresScriptTag
init|=
literal|"<script src='"
operator|+
name|appRoot
operator|+
name|this
operator|.
name|featuresJs
operator|+
literal|"' language='JavaScript'></script>"
decl_stmt|;
name|pw
operator|.
name|println
argument_list|(
name|featuresScriptTag
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"<script type='text/javascript'>"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"//<![CDATA["
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"var imgRoot = '"
operator|+
name|appRoot
operator|+
literal|"/res/imgs';"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"// ]]>"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"</script>"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"<div id='plugin_content'/>"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"<script type='text/javascript'>"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"//<![CDATA["
argument_list|)
expr_stmt|;
name|pw
operator|.
name|print
argument_list|(
literal|"renderFeatures( "
argument_list|)
expr_stmt|;
name|writeJSON
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|" )"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"// ]]>"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"</script>"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|URL
name|getResource
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|NAME
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
operator|||
name|path
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|URL
name|url
init|=
name|this
operator|.
name|classLoader
operator|.
name|getResource
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|ins
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ins
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
if|if
condition|(
name|ins
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|log
operator|.
name|error
argument_list|(
literal|"failed to open "
operator|+
name|url
argument_list|)
expr_stmt|;
name|url
operator|=
literal|null
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|this
operator|.
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|url
operator|=
literal|null
expr_stmt|;
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
name|e
parameter_list|)
block|{
name|this
operator|.
name|log
operator|.
name|error
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
block|}
block|}
return|return
name|url
return|;
block|}
specifier|private
name|void
name|writeJSON
parameter_list|(
specifier|final
name|PrintWriter
name|pw
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|List
argument_list|<
name|ServletDetails
argument_list|>
name|servlets
init|=
name|this
operator|.
name|getServletDetails
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|WebDetail
argument_list|>
name|web
init|=
name|this
operator|.
name|getWebDetails
argument_list|()
decl_stmt|;
specifier|final
name|String
name|statusLine
init|=
name|this
operator|.
name|getStatusLine
argument_list|(
name|servlets
argument_list|,
name|web
argument_list|)
decl_stmt|;
specifier|final
name|JSONWriter
name|jw
init|=
operator|new
name|JSONWriter
argument_list|(
name|pw
argument_list|)
decl_stmt|;
try|try
block|{
name|jw
operator|.
name|object
argument_list|()
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"status"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|statusLine
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"contexts"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|array
argument_list|()
expr_stmt|;
for|for
control|(
name|ServletDetails
name|servlet
range|:
name|servlets
control|)
block|{
name|jw
operator|.
name|object
argument_list|()
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|servlet
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"servlet"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|servlet
operator|.
name|getServlet
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"servletName"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|servlet
operator|.
name|getServletName
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"state"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|servlet
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"alias"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|servlet
operator|.
name|getAlias
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"urls"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|array
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|url
range|:
name|servlet
operator|.
name|getUrls
argument_list|()
control|)
block|{
name|jw
operator|.
name|value
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
name|jw
operator|.
name|endArray
argument_list|()
expr_stmt|;
name|jw
operator|.
name|endObject
argument_list|()
expr_stmt|;
block|}
name|jw
operator|.
name|endArray
argument_list|()
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"web"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|array
argument_list|()
expr_stmt|;
for|for
control|(
name|WebDetail
name|webDetail
range|:
name|web
control|)
block|{
name|jw
operator|.
name|object
argument_list|()
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|webDetail
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"bundlestate"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|webDetail
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"contextpath"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|webDetail
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"state"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|webDetail
operator|.
name|getWebState
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|endObject
argument_list|()
expr_stmt|;
block|}
name|jw
operator|.
name|endArray
argument_list|()
expr_stmt|;
name|jw
operator|.
name|endObject
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSONException
name|je
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|je
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|List
argument_list|<
name|ServletDetails
argument_list|>
name|getServletDetails
parameter_list|()
block|{
name|Collection
argument_list|<
name|ServletEvent
argument_list|>
name|events
init|=
name|servletEventHandler
operator|.
name|getServletEvents
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ServletDetails
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|ServletDetails
argument_list|>
argument_list|(
name|events
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ServletEvent
name|event
range|:
name|events
control|)
block|{
name|Servlet
name|servlet
init|=
name|event
operator|.
name|getServlet
argument_list|()
decl_stmt|;
name|String
name|servletClassName
init|=
literal|" "
decl_stmt|;
if|if
condition|(
name|servlet
operator|!=
literal|null
condition|)
block|{
name|servletClassName
operator|=
name|servlet
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|servletClassName
operator|=
name|servletClassName
operator|.
name|substring
argument_list|(
name|servletClassName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|+
literal|1
argument_list|,
name|servletClassName
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|servletName
init|=
name|event
operator|.
name|getServletName
argument_list|()
operator|!=
literal|null
condition|?
name|event
operator|.
name|getServletName
argument_list|()
else|:
literal|" "
decl_stmt|;
if|if
condition|(
name|servletName
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|servletName
operator|=
name|servletName
operator|.
name|substring
argument_list|(
name|servletName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|+
literal|1
argument_list|,
name|servletName
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|alias
init|=
name|event
operator|.
name|getAlias
argument_list|()
operator|!=
literal|null
condition|?
name|event
operator|.
name|getAlias
argument_list|()
else|:
literal|" "
decl_stmt|;
name|String
index|[]
name|urls
init|=
operator|(
name|String
index|[]
operator|)
operator|(
name|event
operator|.
name|getUrlParameter
argument_list|()
operator|!=
literal|null
condition|?
name|event
operator|.
name|getUrlParameter
argument_list|()
else|:
operator|new
name|String
index|[]
block|{
literal|""
block|}
operator|)
decl_stmt|;
name|ServletDetails
name|details
init|=
operator|new
name|ServletDetails
argument_list|()
decl_stmt|;
name|details
operator|.
name|setId
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|details
operator|.
name|setAlias
argument_list|(
name|alias
argument_list|)
expr_stmt|;
name|details
operator|.
name|setServlet
argument_list|(
name|servletClassName
argument_list|)
expr_stmt|;
name|details
operator|.
name|setServletName
argument_list|(
name|servletName
argument_list|)
expr_stmt|;
name|details
operator|.
name|setState
argument_list|(
name|getStateString
argument_list|(
name|event
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|details
operator|.
name|setUrls
argument_list|(
name|urls
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|details
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|protected
name|List
argument_list|<
name|WebDetail
argument_list|>
name|getWebDetails
parameter_list|()
block|{
name|Map
argument_list|<
name|Long
argument_list|,
name|WebEvent
argument_list|>
name|bundleEvents
init|=
name|webEventHandler
operator|.
name|getBundleEvents
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WebDetail
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|WebDetail
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|WebEvent
name|event
range|:
name|bundleEvents
operator|.
name|values
argument_list|()
control|)
block|{
name|WebDetail
name|webDetail
init|=
operator|new
name|WebDetail
argument_list|()
decl_stmt|;
name|webDetail
operator|.
name|setBundleId
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|webDetail
operator|.
name|setContextPath
argument_list|(
name|event
operator|.
name|getContextPath
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|concat
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|state
init|=
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getState
argument_list|()
decl_stmt|;
name|String
name|stateStr
decl_stmt|;
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|ACTIVE
condition|)
block|{
name|stateStr
operator|=
literal|"Active"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|INSTALLED
condition|)
block|{
name|stateStr
operator|=
literal|"Installed"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|RESOLVED
condition|)
block|{
name|stateStr
operator|=
literal|"Resolved"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|STARTING
condition|)
block|{
name|stateStr
operator|=
literal|"Starting"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|STOPPING
condition|)
block|{
name|stateStr
operator|=
literal|"Stopping"
expr_stmt|;
block|}
else|else
block|{
name|stateStr
operator|=
literal|"Unknown"
expr_stmt|;
block|}
name|webDetail
operator|.
name|setState
argument_list|(
name|stateStr
argument_list|)
expr_stmt|;
name|webDetail
operator|.
name|setWebState
argument_list|(
name|getStateString
argument_list|(
name|event
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|webDetail
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|String
name|getStatusLine
parameter_list|(
name|List
argument_list|<
name|ServletDetails
argument_list|>
name|servlets
parameter_list|,
name|List
argument_list|<
name|WebDetail
argument_list|>
name|web
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|states
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ServletDetails
name|servlet
range|:
name|servlets
control|)
block|{
name|Integer
name|count
init|=
name|states
operator|.
name|get
argument_list|(
name|servlet
operator|.
name|getState
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|null
condition|)
block|{
name|states
operator|.
name|put
argument_list|(
name|servlet
operator|.
name|getState
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|states
operator|.
name|put
argument_list|(
name|servlet
operator|.
name|getState
argument_list|()
argument_list|,
literal|1
operator|+
name|count
argument_list|)
expr_stmt|;
block|}
block|}
name|StringBuilder
name|stateSummary
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|state
range|:
name|states
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|stateSummary
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|first
operator|=
literal|false
expr_stmt|;
name|stateSummary
operator|.
name|append
argument_list|(
name|state
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|state
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|"Http contexts: "
operator|+
name|stateSummary
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getStateString
parameter_list|(
name|int
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|WebEvent
operator|.
name|DEPLOYING
case|:
return|return
literal|"Deploying"
return|;
case|case
name|WebEvent
operator|.
name|DEPLOYED
case|:
return|return
literal|"Deployed"
return|;
case|case
name|WebEvent
operator|.
name|UNDEPLOYING
case|:
return|return
literal|"Undeploying"
return|;
case|case
name|WebEvent
operator|.
name|UNDEPLOYED
case|:
return|return
literal|"Undeployed"
return|;
case|case
name|WebEvent
operator|.
name|FAILED
case|:
return|return
literal|"Failed"
return|;
case|case
name|WebEvent
operator|.
name|WAITING
case|:
return|return
literal|"Waiting"
return|;
default|default:
return|return
literal|"Failed"
return|;
block|}
block|}
specifier|public
name|void
name|setServletEventHandler
parameter_list|(
name|ServletEventHandler
name|eventHandler
parameter_list|)
block|{
name|this
operator|.
name|servletEventHandler
operator|=
name|eventHandler
expr_stmt|;
block|}
specifier|public
name|void
name|setWebEventHandler
parameter_list|(
name|WebEventHandler
name|eventHandler
parameter_list|)
block|{
name|this
operator|.
name|webEventHandler
operator|=
name|eventHandler
expr_stmt|;
block|}
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
block|}
end_class

end_unit

