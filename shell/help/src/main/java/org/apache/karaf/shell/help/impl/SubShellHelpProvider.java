begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|help
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|jline
operator|.
name|Terminal
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
name|CommandSession
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
name|commands
operator|.
name|basic
operator|.
name|DefaultActionPreparator
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
name|console
operator|.
name|HelpProvider
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
name|console
operator|.
name|SubShell
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
name|util
operator|.
name|IndentFormatter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
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
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_class
specifier|public
class|class
name|SubShellHelpProvider
implements|implements
name|HelpProvider
block|{
specifier|private
name|BundleContext
name|context
decl_stmt|;
specifier|private
name|ServiceTracker
name|tracker
decl_stmt|;
specifier|public
name|void
name|setContext
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
specifier|public
name|void
name|start
parameter_list|()
block|{
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|(
name|context
argument_list|,
name|SubShell
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getHelp
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|.
name|indexOf
argument_list|(
literal|'|'
argument_list|)
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"subshell|"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|"subshell|"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
for|for
control|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|ref
range|:
name|tracker
operator|.
name|getServiceReferences
argument_list|()
control|)
block|{
if|if
condition|(
name|path
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
argument_list|)
condition|)
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|printSubShellHelp
argument_list|(
name|session
argument_list|,
name|ref
operator|.
name|getBundle
argument_list|()
argument_list|,
operator|(
name|SubShell
operator|)
name|tracker
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
argument_list|,
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|baos
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|printSubShellHelp
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Bundle
name|bundle
parameter_list|,
name|SubShell
name|subShell
parameter_list|,
name|PrintStream
name|out
parameter_list|)
block|{
name|Terminal
name|term
init|=
name|session
operator|!=
literal|null
condition|?
operator|(
name|Terminal
operator|)
name|session
operator|.
name|get
argument_list|(
literal|".jline.terminal"
argument_list|)
else|:
literal|null
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|bold
argument_list|()
operator|.
name|a
argument_list|(
literal|"SUBSHELL"
argument_list|)
operator|.
name|boldOff
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"        "
argument_list|)
expr_stmt|;
if|if
condition|(
name|subShell
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|bold
argument_list|()
operator|.
name|a
argument_list|(
name|subShell
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|boldOff
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|print
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|subShell
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
if|if
condition|(
name|subShell
operator|.
name|getDetailedDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|bold
argument_list|()
operator|.
name|a
argument_list|(
literal|"DETAILS"
argument_list|)
operator|.
name|boldOff
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|desc
init|=
name|loadDescription
argument_list|(
name|bundle
argument_list|,
name|subShell
operator|.
name|getDetailedDescription
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|desc
operator|.
name|endsWith
argument_list|(
literal|"\n"
argument_list|)
condition|)
block|{
name|desc
operator|=
name|desc
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|desc
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|IndentFormatter
operator|.
name|printFormatted
argument_list|(
literal|"        "
argument_list|,
name|desc
argument_list|,
name|term
operator|!=
literal|null
condition|?
name|term
operator|.
name|getWidth
argument_list|()
else|:
literal|80
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"${command-list|"
operator|+
name|subShell
operator|.
name|getName
argument_list|()
operator|+
literal|":}"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|loadDescription
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|String
name|desc
parameter_list|)
block|{
if|if
condition|(
name|desc
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
condition|)
block|{
name|URL
name|url
init|=
name|bundle
operator|.
name|getResource
argument_list|(
name|desc
operator|.
name|substring
argument_list|(
literal|"classpath:"
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|desc
operator|=
literal|"Unable to load description from "
operator|+
name|desc
expr_stmt|;
block|}
else|else
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|Reader
name|r
init|=
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|int
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|r
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|sw
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
name|desc
operator|=
name|sw
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|desc
operator|=
literal|"Unable to load description from "
operator|+
name|desc
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|is
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
comment|// Ignore
block|}
block|}
block|}
block|}
return|return
name|desc
return|;
block|}
block|}
end_class

end_unit

