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
name|felix
operator|.
name|karaf
operator|.
name|webconsole
operator|.
name|features
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
name|URI
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|karaf
operator|.
name|features
operator|.
name|Feature
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
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
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
name|karaf
operator|.
name|features
operator|.
name|Repository
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
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_comment
comment|/**  * The<code>FeaturesPlugin</code>  */
end_comment

begin_class
specifier|public
class|class
name|FeaturesPlugin
extends|extends
name|AbstractWebConsolePlugin
block|{
comment|/** Pseudo class version ID to keep the IDE quite. */
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"features"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LABEL
init|=
literal|"Features"
decl_stmt|;
specifier|private
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FeaturesPlugin
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ClassLoader
name|classLoader
decl_stmt|;
specifier|private
name|String
name|featuresJs
init|=
literal|"/features/res/ui/features.js"
decl_stmt|;
specifier|private
name|FeaturesService
name|featuresService
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
comment|//
comment|// Blueprint lifecycle callback methods
comment|//
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
comment|//
comment|// AbstractWebConsolePlugin interface
comment|//
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|NAME
return|;
block|}
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|LABEL
return|;
block|}
specifier|protected
name|void
name|doPost
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
specifier|final
name|String
name|action
init|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"action"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|feature
init|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"feature"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|version
init|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"version"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|url
init|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
name|success
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"installFeature"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|success
operator|=
name|this
operator|.
name|installFeature
argument_list|(
name|feature
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"uninstallFeature"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|success
operator|=
name|this
operator|.
name|uninstallFeature
argument_list|(
name|feature
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"refreshRepository"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|success
operator|=
name|this
operator|.
name|refreshRepository
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"removeRepository"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|success
operator|=
name|this
operator|.
name|removeRepository
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"addRepository"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|success
operator|=
name|this
operator|.
name|addRepository
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|success
condition|)
block|{
comment|// let's wait a little bit to give the framework time
comment|// to process our request
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|800
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// we ignore this
block|}
name|this
operator|.
name|renderJSON
argument_list|(
name|resp
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|doPost
argument_list|(
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"org.apache.felix.webconsole.internal.servlet.OsgiManager.appRoot"
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
comment|//
comment|// Additional methods
comment|//
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
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|//it means input parameter path is just plugin name like /features but not real resource path.
comment|//on felix the return url would be null in this case, which is correct expected behavior.
comment|//but on equinox the return url is like bundleresource://184.fwk1674485910/,
comment|//which cause NPE in AbstractWebConsolePlugin.spoolResource
comment|//so just return null ensure it works both with felix and equinox
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
try|try
block|{
name|InputStream
name|ins
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
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
block|}
return|return
name|url
return|;
block|}
specifier|private
name|boolean
name|installFeature
parameter_list|(
name|String
name|feature
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|featuresService
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
literal|"Shell Features service is unavailable."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|featuresService
operator|.
name|installFeature
argument_list|(
name|feature
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|log
operator|.
name|error
argument_list|(
literal|"failed to install feature: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|success
return|;
block|}
specifier|private
name|boolean
name|uninstallFeature
parameter_list|(
name|String
name|feature
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|featuresService
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
literal|"Shell Features service is unavailable."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|featuresService
operator|.
name|uninstallFeature
argument_list|(
name|feature
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|log
operator|.
name|error
argument_list|(
literal|"failed to install feature: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|success
return|;
block|}
specifier|private
name|boolean
name|removeRepository
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|featuresService
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
literal|"Shell Features service is unavailable."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|featuresService
operator|.
name|removeRepository
argument_list|(
operator|new
name|URI
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|log
operator|.
name|error
argument_list|(
literal|"failed to install feature: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|success
return|;
block|}
specifier|private
name|boolean
name|refreshRepository
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|featuresService
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
literal|"Shell Features service is unavailable."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|featuresService
operator|.
name|removeRepository
argument_list|(
operator|new
name|URI
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
name|featuresService
operator|.
name|addRepository
argument_list|(
operator|new
name|URI
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|log
operator|.
name|error
argument_list|(
literal|"failed to install feature: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|success
return|;
block|}
specifier|private
name|boolean
name|addRepository
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|featuresService
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
literal|"Shell Features service is unavailable."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|featuresService
operator|.
name|addRepository
argument_list|(
operator|new
name|URI
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|log
operator|.
name|error
argument_list|(
literal|"failed to install feature: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|success
return|;
block|}
specifier|private
name|void
name|renderJSON
parameter_list|(
specifier|final
name|HttpServletResponse
name|response
parameter_list|,
specifier|final
name|String
name|feature
parameter_list|)
throws|throws
name|IOException
block|{
name|response
operator|.
name|setContentType
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
name|response
operator|.
name|setCharacterEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
specifier|final
name|PrintWriter
name|pw
init|=
name|response
operator|.
name|getWriter
argument_list|()
decl_stmt|;
name|writeJSON
argument_list|(
name|pw
argument_list|)
expr_stmt|;
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
name|Repository
argument_list|>
name|repositories
init|=
name|this
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|ExtendedFeature
argument_list|>
name|features
init|=
name|this
operator|.
name|getFeatures
argument_list|(
name|repositories
argument_list|)
decl_stmt|;
specifier|final
name|String
name|statusLine
init|=
name|this
operator|.
name|getStatusLine
argument_list|(
name|features
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
literal|"repositories"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|array
argument_list|()
expr_stmt|;
for|for
control|(
name|Repository
name|r
range|:
name|repositories
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
literal|"name"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|r
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"url"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|r
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"actions"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|array
argument_list|()
expr_stmt|;
name|boolean
name|enable
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"bundle"
argument_list|)
condition|)
block|{
name|enable
operator|=
literal|false
expr_stmt|;
block|}
name|action
argument_list|(
name|jw
argument_list|,
name|enable
argument_list|,
literal|"refreshRepository"
argument_list|,
literal|"Refresh"
argument_list|,
literal|"refresh"
argument_list|)
expr_stmt|;
name|action
argument_list|(
name|jw
argument_list|,
name|enable
argument_list|,
literal|"removeRepository"
argument_list|,
literal|"Remove"
argument_list|,
literal|"delete"
argument_list|)
expr_stmt|;
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
literal|"features"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|array
argument_list|()
expr_stmt|;
for|for
control|(
name|ExtendedFeature
name|f
range|:
name|features
control|)
block|{
name|featureInfo
argument_list|(
name|jw
argument_list|,
name|f
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
specifier|private
name|List
argument_list|<
name|Repository
argument_list|>
name|getRepositories
parameter_list|()
block|{
name|List
argument_list|<
name|Repository
argument_list|>
name|repositories
init|=
operator|new
name|ArrayList
argument_list|<
name|Repository
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|featuresService
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
literal|"Shell Features service is unavailable."
argument_list|)
expr_stmt|;
return|return
name|repositories
return|;
block|}
try|try
block|{
for|for
control|(
name|Repository
name|r
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
name|repositories
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
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
argument_list|)
expr_stmt|;
block|}
return|return
name|repositories
return|;
block|}
specifier|private
name|List
argument_list|<
name|ExtendedFeature
argument_list|>
name|getFeatures
parameter_list|(
name|List
argument_list|<
name|Repository
argument_list|>
name|repositories
parameter_list|)
block|{
name|List
argument_list|<
name|ExtendedFeature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<
name|ExtendedFeature
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|featuresService
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
literal|"Shell Features service is unavailable."
argument_list|)
expr_stmt|;
return|return
name|features
return|;
block|}
try|try
block|{
for|for
control|(
name|Repository
name|r
range|:
name|repositories
control|)
block|{
for|for
control|(
name|Feature
name|f
range|:
name|r
operator|.
name|getFeatures
argument_list|()
control|)
block|{
name|ExtendedFeature
operator|.
name|State
name|state
init|=
name|featuresService
operator|.
name|isInstalled
argument_list|(
name|f
argument_list|)
condition|?
name|ExtendedFeature
operator|.
name|State
operator|.
name|INSTALLED
else|:
name|ExtendedFeature
operator|.
name|State
operator|.
name|UNINSTALLED
decl_stmt|;
name|features
operator|.
name|add
argument_list|(
operator|new
name|ExtendedFeature
argument_list|(
name|state
argument_list|,
name|r
operator|.
name|getName
argument_list|()
argument_list|,
name|f
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
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
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|features
argument_list|,
operator|new
name|ExtendedFeatureComparator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|features
return|;
block|}
class|class
name|ExtendedFeatureComparator
implements|implements
name|Comparator
argument_list|<
name|ExtendedFeature
argument_list|>
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|ExtendedFeature
name|o1
parameter_list|,
name|ExtendedFeature
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|private
name|String
name|getStatusLine
parameter_list|(
specifier|final
name|List
argument_list|<
name|ExtendedFeature
argument_list|>
name|features
parameter_list|)
block|{
name|int
name|installed
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ExtendedFeature
name|f
range|:
name|features
control|)
block|{
if|if
condition|(
name|f
operator|.
name|getState
argument_list|()
operator|==
name|ExtendedFeature
operator|.
name|State
operator|.
name|INSTALLED
condition|)
block|{
name|installed
operator|++
expr_stmt|;
block|}
block|}
specifier|final
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"Feature information: "
argument_list|)
expr_stmt|;
name|appendFeatureInfoCount
argument_list|(
name|buffer
argument_list|,
literal|"in total"
argument_list|,
name|features
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|installed
operator|==
name|features
operator|.
name|size
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" - all "
argument_list|)
expr_stmt|;
name|appendFeatureInfoCount
argument_list|(
name|buffer
argument_list|,
literal|"active."
argument_list|,
name|features
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|installed
operator|!=
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|appendFeatureInfoCount
argument_list|(
name|buffer
argument_list|,
literal|"installed"
argument_list|,
name|installed
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|appendFeatureInfoCount
parameter_list|(
specifier|final
name|StringBuffer
name|buf
parameter_list|,
name|String
name|msg
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" feature"
argument_list|)
expr_stmt|;
if|if
condition|(
name|count
operator|!=
literal|1
condition|)
name|buf
operator|.
name|append
argument_list|(
literal|'s'
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|featureInfo
parameter_list|(
name|JSONWriter
name|jw
parameter_list|,
name|ExtendedFeature
name|feature
parameter_list|)
throws|throws
name|JSONException
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
name|feature
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"version"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"repository"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|feature
operator|.
name|getRepository
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
name|ExtendedFeature
operator|.
name|State
name|state
init|=
name|feature
operator|.
name|getState
argument_list|()
decl_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|state
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"actions"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|array
argument_list|()
expr_stmt|;
if|if
condition|(
name|state
operator|==
name|ExtendedFeature
operator|.
name|State
operator|.
name|INSTALLED
condition|)
block|{
name|action
argument_list|(
name|jw
argument_list|,
literal|true
argument_list|,
literal|"uninstallFeature"
argument_list|,
literal|"Uninstall"
argument_list|,
literal|"delete"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|ExtendedFeature
operator|.
name|State
operator|.
name|UNINSTALLED
condition|)
block|{
name|action
argument_list|(
name|jw
argument_list|,
literal|true
argument_list|,
literal|"installFeature"
argument_list|,
literal|"Install"
argument_list|,
literal|"start"
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
specifier|private
name|void
name|action
parameter_list|(
name|JSONWriter
name|jw
parameter_list|,
name|boolean
name|enabled
parameter_list|,
name|String
name|op
parameter_list|,
name|String
name|title
parameter_list|,
name|String
name|image
parameter_list|)
throws|throws
name|JSONException
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
literal|"enabled"
argument_list|)
operator|.
name|value
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"op"
argument_list|)
operator|.
name|value
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"title"
argument_list|)
operator|.
name|value
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"image"
argument_list|)
operator|.
name|value
argument_list|(
name|image
argument_list|)
expr_stmt|;
name|jw
operator|.
name|endObject
argument_list|()
expr_stmt|;
block|}
comment|//
comment|// Dependency Injection setters
comment|//
specifier|public
name|void
name|setFeaturesService
parameter_list|(
name|FeaturesService
name|featuresService
parameter_list|)
block|{
name|this
operator|.
name|featuresService
operator|=
name|featuresService
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

