begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Copyright 2009 Marcin.  *   *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *   *       http://www.apache.org/licenses/LICENSE-2.0  *   *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|admin
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
name|List
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
name|shell
operator|.
name|admin
operator|.
name|AdminService
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
name|shell
operator|.
name|admin
operator|.
name|Instance
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
name|shell
operator|.
name|admin
operator|.
name|InstanceSettings
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
comment|/**  * Felix Web Console plugin for interacting with the {@link AdminService}  */
end_comment

begin_class
specifier|public
class|class
name|AdminPlugin
extends|extends
name|AbstractWebConsolePlugin
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"admin"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LABEL
init|=
literal|"Admin"
decl_stmt|;
specifier|private
name|String
name|adminJs
init|=
literal|"/admin/res/ui/admin.js"
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|AdminService
name|adminService
decl_stmt|;
specifier|private
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|AdminPlugin
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ClassLoader
name|classLoader
decl_stmt|;
comment|/**      * Blueprint lifecycle callback methods      */
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
name|getTitle
parameter_list|()
block|{
return|return
name|LABEL
return|;
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
specifier|protected
name|void
name|renderContent
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
specifier|final
name|PrintWriter
name|pw
init|=
name|res
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
name|req
operator|.
name|getAttribute
argument_list|(
literal|"org.apache.felix.webconsole.internal.servlet.OsgiManager.appRoot"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|adminScriptTag
init|=
literal|"<script src='"
operator|+
name|appRoot
operator|+
name|this
operator|.
name|adminJs
operator|+
literal|"' language='JavaScript'></script>"
decl_stmt|;
name|pw
operator|.
name|println
argument_list|(
name|adminScriptTag
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
literal|"renderAdmin( "
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
annotation|@
name|Override
specifier|protected
name|void
name|doPost
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
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
name|String
name|name
init|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"name"
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
literal|"create"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|int
name|port
init|=
name|parsePortNumber
argument_list|(
name|req
operator|.
name|getParameter
argument_list|(
literal|"port"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|location
init|=
name|parseString
argument_list|(
name|req
operator|.
name|getParameter
argument_list|(
literal|"location"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|featureURLs
init|=
name|parseStringList
argument_list|(
name|req
operator|.
name|getParameter
argument_list|(
literal|"featureURLs"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|features
init|=
name|parseStringList
argument_list|(
name|req
operator|.
name|getParameter
argument_list|(
literal|"features"
argument_list|)
argument_list|)
decl_stmt|;
name|InstanceSettings
name|settings
init|=
operator|new
name|InstanceSettings
argument_list|(
name|port
argument_list|,
name|location
argument_list|,
name|featureURLs
argument_list|,
name|features
argument_list|)
decl_stmt|;
name|success
operator|=
name|createInstance
argument_list|(
name|name
argument_list|,
name|settings
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"destroy"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|success
operator|=
name|destroyInstance
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"start"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|String
name|javaOpts
init|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"javaOpts"
argument_list|)
decl_stmt|;
name|success
operator|=
name|startInstance
argument_list|(
name|name
argument_list|,
name|javaOpts
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"stop"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|success
operator|=
name|stopInstance
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|success
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{             }
name|this
operator|.
name|renderJSON
argument_list|(
name|res
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
name|res
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Parse the String value, returning<code>null</code> if the String is empty       */
specifier|private
name|String
name|parseString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|value
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|parseStringList
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|el
range|:
name|value
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|String
name|trimmed
init|=
name|el
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|trimmed
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
name|list
operator|.
name|add
argument_list|(
name|trimmed
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
comment|/*      * Parse the port number for the String given, returning 0 if the String does not represent an integer       */
specifier|private
name|int
name|parsePortNumber
parameter_list|(
name|String
name|port
parameter_list|)
block|{
try|try
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|port
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
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
block|{
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
specifier|final
name|Instance
index|[]
name|instances
init|=
name|adminService
operator|.
name|getInstances
argument_list|()
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
name|getStatusLine
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"instances"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|array
argument_list|()
expr_stmt|;
for|for
control|(
name|Instance
name|i
range|:
name|instances
control|)
block|{
name|instanceInfo
argument_list|(
name|jw
argument_list|,
name|i
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
name|ex
parameter_list|)
block|{
name|Logger
operator|.
name|getLogger
argument_list|(
name|AdminPlugin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Logger
operator|.
name|getLogger
argument_list|(
name|AdminPlugin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|instanceInfo
parameter_list|(
name|JSONWriter
name|jw
parameter_list|,
name|Instance
name|instance
parameter_list|)
throws|throws
name|JSONException
throws|,
name|Exception
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
literal|"pid"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|instance
operator|.
name|getPid
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
name|instance
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"port"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|instance
operator|.
name|getPort
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
name|instance
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|jw
operator|.
name|key
argument_list|(
literal|"location"
argument_list|)
expr_stmt|;
name|jw
operator|.
name|value
argument_list|(
name|instance
operator|.
name|getLocation
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
name|action
argument_list|(
name|jw
argument_list|,
literal|"destroy"
argument_list|,
literal|"Destroy"
argument_list|,
literal|"delete"
argument_list|)
expr_stmt|;
if|if
condition|(
name|instance
operator|.
name|getState
argument_list|()
operator|.
name|equals
argument_list|(
name|Instance
operator|.
name|STARTED
argument_list|)
condition|)
block|{
name|action
argument_list|(
name|jw
argument_list|,
literal|"stop"
argument_list|,
literal|"Stop"
argument_list|,
literal|"stop"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|instance
operator|.
name|getState
argument_list|()
operator|.
name|equals
argument_list|(
name|Instance
operator|.
name|STARTING
argument_list|)
condition|)
block|{
name|action
argument_list|(
name|jw
argument_list|,
literal|"stop"
argument_list|,
literal|"Stop"
argument_list|,
literal|"stop"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|instance
operator|.
name|getState
argument_list|()
operator|.
name|equals
argument_list|(
name|Instance
operator|.
name|STOPPED
argument_list|)
condition|)
block|{
name|action
argument_list|(
name|jw
argument_list|,
literal|"start"
argument_list|,
literal|"Start"
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
specifier|private
name|String
name|getStatusLine
parameter_list|()
block|{
specifier|final
name|Instance
index|[]
name|instances
init|=
name|adminService
operator|.
name|getInstances
argument_list|()
decl_stmt|;
name|int
name|started
init|=
literal|0
decl_stmt|,
name|starting
init|=
literal|0
decl_stmt|,
name|stopped
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Instance
name|instance
range|:
name|instances
control|)
block|{
try|try
block|{
if|if
condition|(
name|instance
operator|.
name|getState
argument_list|()
operator|.
name|equals
argument_list|(
name|Instance
operator|.
name|STARTED
argument_list|)
condition|)
block|{
name|started
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|instance
operator|.
name|getState
argument_list|()
operator|.
name|equals
argument_list|(
name|Instance
operator|.
name|STARTING
argument_list|)
condition|)
block|{
name|starting
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|instance
operator|.
name|getState
argument_list|()
operator|.
name|equals
argument_list|(
name|Instance
operator|.
name|STOPPED
argument_list|)
condition|)
block|{
name|stopped
operator|++
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Logger
operator|.
name|getLogger
argument_list|(
name|AdminPlugin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
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
literal|"Instance information: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|instances
operator|.
name|length
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" instance"
argument_list|)
expr_stmt|;
if|if
condition|(
name|instances
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'s'
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|" in total"
argument_list|)
expr_stmt|;
if|if
condition|(
name|started
operator|==
name|instances
operator|.
name|length
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" - all started"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|started
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
name|buffer
operator|.
name|append
argument_list|(
name|started
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" started"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|starting
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
name|buffer
operator|.
name|append
argument_list|(
name|starting
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" starting"
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
name|boolean
name|createInstance
parameter_list|(
name|String
name|name
parameter_list|,
name|InstanceSettings
name|settings
parameter_list|)
block|{
try|try
block|{
name|adminService
operator|.
name|createInstance
argument_list|(
name|name
argument_list|,
name|settings
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Logger
operator|.
name|getLogger
argument_list|(
name|AdminPlugin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|destroyInstance
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|Instance
name|instance
init|=
name|adminService
operator|.
name|getInstance
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|destroy
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Logger
operator|.
name|getLogger
argument_list|(
name|AdminPlugin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|startInstance
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|javaOpts
parameter_list|)
block|{
try|try
block|{
name|Instance
name|instance
init|=
name|adminService
operator|.
name|getInstance
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|start
argument_list|(
name|javaOpts
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Logger
operator|.
name|getLogger
argument_list|(
name|AdminPlugin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|stopInstance
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|Instance
name|instance
init|=
name|adminService
operator|.
name|getInstance
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|stop
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Logger
operator|.
name|getLogger
argument_list|(
name|AdminPlugin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * @param adminService the adminService to set      */
specifier|public
name|void
name|setAdminService
parameter_list|(
name|AdminService
name|adminService
parameter_list|)
block|{
name|this
operator|.
name|adminService
operator|=
name|adminService
expr_stmt|;
block|}
comment|/**      * @param bundleContext the bundleContext to set      */
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

