begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * Based on http://antony.lesuisse.org/software/ajaxterm/  *  Public Domain License  */
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
name|PipedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PipedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InterruptedIOException
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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|util
operator|.
name|zip
operator|.
name|GZIPOutputStream
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
name|net
operator|.
name|URL
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
name|gshell
operator|.
name|console
operator|.
name|jline
operator|.
name|Console
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
name|gshell
operator|.
name|console
operator|.
name|Completer
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
name|gshell
operator|.
name|console
operator|.
name|completer
operator|.
name|AggregateCompleter
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
name|service
operator|.
name|command
operator|.
name|CommandProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|command
operator|.
name|CommandSession
import|;
end_import

begin_comment
comment|/**  * The<code>GogoPlugin</code>  */
end_comment

begin_class
specifier|public
class|class
name|GogoPlugin
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
literal|"gogo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LABEL
init|=
literal|"Gogo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TERM_WIDTH
init|=
literal|120
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TERM_HEIGHT
init|=
literal|39
decl_stmt|;
specifier|private
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|GogoPlugin
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|CommandProcessor
name|commandProcessor
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
decl_stmt|;
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
specifier|public
name|void
name|setCommandProcessor
parameter_list|(
name|CommandProcessor
name|commandProcessor
parameter_list|)
block|{
name|this
operator|.
name|commandProcessor
operator|=
name|commandProcessor
expr_stmt|;
block|}
specifier|public
name|void
name|setCompleters
parameter_list|(
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
parameter_list|)
block|{
name|this
operator|.
name|completers
operator|=
name|completers
expr_stmt|;
block|}
comment|/*     * Blueprint lifecycle callback methods     */
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
name|request
operator|.
name|getContextPath
argument_list|()
operator|+
name|request
operator|.
name|getServletPath
argument_list|()
decl_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"<link href=\""
operator|+
name|appRoot
operator|+
literal|"/gogo/res/ui/gogo.css\" rel=\"stylesheet\" type=\"text/css\" />"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"<script src=\""
operator|+
name|appRoot
operator|+
literal|"/gogo/res/ui/gogo.js\" type=\"text/javascript\"></script>"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"<div id='console'><div id='term'></div></div>"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"<script type=\"text/javascript\"><!--"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"window.onload = function() { gogo.Terminal(document.getElementById(\"term\"), "
operator|+
name|TERM_WIDTH
operator|+
literal|", "
operator|+
name|TERM_HEIGHT
operator|+
literal|"); }"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
literal|"--></script>"
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
name|URL
name|url
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
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
annotation|@
name|Override
specifier|protected
name|void
name|doPost
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
name|String
name|encoding
init|=
name|request
operator|.
name|getHeader
argument_list|(
literal|"Accept-Encoding"
argument_list|)
decl_stmt|;
name|boolean
name|supportsGzip
init|=
operator|(
name|encoding
operator|!=
literal|null
operator|&&
name|encoding
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"gzip"
argument_list|)
operator|>
operator|-
literal|1
operator|)
decl_stmt|;
name|SessionTerminal
name|st
init|=
operator|(
name|SessionTerminal
operator|)
name|request
operator|.
name|getSession
argument_list|(
literal|true
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"terminal"
argument_list|)
decl_stmt|;
if|if
condition|(
name|st
operator|==
literal|null
operator|||
name|st
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|st
operator|=
operator|new
name|SessionTerminal
argument_list|()
expr_stmt|;
name|request
operator|.
name|getSession
argument_list|()
operator|.
name|setAttribute
argument_list|(
literal|"terminal"
argument_list|,
name|st
argument_list|)
expr_stmt|;
block|}
name|String
name|str
init|=
name|request
operator|.
name|getParameter
argument_list|(
literal|"k"
argument_list|)
decl_stmt|;
name|String
name|f
init|=
name|request
operator|.
name|getParameter
argument_list|(
literal|"f"
argument_list|)
decl_stmt|;
name|String
name|dump
init|=
name|st
operator|.
name|handle
argument_list|(
name|str
argument_list|,
name|f
operator|!=
literal|null
operator|&&
name|f
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|dump
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|supportsGzip
condition|)
block|{
name|response
operator|.
name|setHeader
argument_list|(
literal|"Content-Encoding"
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
name|response
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/html"
argument_list|)
expr_stmt|;
try|try
block|{
name|GZIPOutputStream
name|gzos
init|=
operator|new
name|GZIPOutputStream
argument_list|(
name|response
operator|.
name|getOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|gzos
operator|.
name|write
argument_list|(
name|dump
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|gzos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ie
parameter_list|)
block|{
comment|// handle the error here
name|ie
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|response
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
name|dump
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
class|class
name|SessionTerminal
implements|implements
name|Runnable
block|{
specifier|private
name|Terminal
name|terminal
decl_stmt|;
specifier|private
name|Console
name|console
decl_stmt|;
specifier|private
name|PipedOutputStream
name|in
decl_stmt|;
specifier|private
name|PipedInputStream
name|out
decl_stmt|;
specifier|private
name|boolean
name|closed
decl_stmt|;
specifier|public
name|SessionTerminal
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|this
operator|.
name|terminal
operator|=
operator|new
name|Terminal
argument_list|(
name|TERM_WIDTH
argument_list|,
name|TERM_HEIGHT
argument_list|)
expr_stmt|;
name|terminal
operator|.
name|write
argument_list|(
literal|"\u001b\u005B20\u0068"
argument_list|)
expr_stmt|;
comment|// set newline mode on
name|in
operator|=
operator|new
name|PipedOutputStream
argument_list|()
expr_stmt|;
name|out
operator|=
operator|new
name|PipedInputStream
argument_list|()
expr_stmt|;
name|PrintStream
name|pipedOut
init|=
operator|new
name|PrintStream
argument_list|(
operator|new
name|PipedOutputStream
argument_list|(
name|out
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|console
operator|=
operator|new
name|Console
argument_list|(
name|commandProcessor
argument_list|,
operator|new
name|PipedInputStream
argument_list|(
name|in
argument_list|)
argument_list|,
name|pipedOut
argument_list|,
name|pipedOut
argument_list|,
operator|new
name|WebTerminal
argument_list|(
name|TERM_WIDTH
argument_list|,
name|TERM_HEIGHT
argument_list|)
argument_list|,
operator|new
name|AggregateCompleter
argument_list|(
name|completers
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|CommandSession
name|session
init|=
name|console
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"APPLICATION"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|,
literal|"root"
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"USER"
argument_list|,
literal|"karaf"
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"COLUMNS"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|TERM_WIDTH
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"LINES"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|TERM_HEIGHT
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
operator|new
name|Thread
argument_list|(
name|console
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
operator|new
name|Thread
argument_list|(
name|this
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|closed
return|;
block|}
specifier|public
name|String
name|handle
parameter_list|(
name|String
name|str
parameter_list|,
name|boolean
name|forceDump
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
if|if
condition|(
name|str
operator|!=
literal|null
operator|&&
name|str
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|d
init|=
name|terminal
operator|.
name|pipe
argument_list|(
name|str
argument_list|)
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|d
operator|.
name|getBytes
argument_list|()
control|)
block|{
name|in
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
name|in
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|closed
operator|=
literal|true
expr_stmt|;
throw|throw
name|e
throw|;
block|}
try|try
block|{
return|return
name|terminal
operator|.
name|dump
argument_list|(
literal|10
argument_list|,
name|forceDump
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InterruptedIOException
argument_list|(
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
for|for
control|(
init|;
condition|;
control|)
block|{
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|int
name|l
init|=
name|out
operator|.
name|read
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|InputStreamReader
name|r
init|=
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|l
argument_list|)
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|int
name|c
init|=
name|r
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|==
operator|-
literal|1
condition|)
block|{
break|break;
block|}
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
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|terminal
operator|.
name|write
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|s
init|=
name|terminal
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|byte
name|b
range|:
name|s
operator|.
name|getBytes
argument_list|()
control|)
block|{
name|in
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|closed
operator|=
literal|true
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

