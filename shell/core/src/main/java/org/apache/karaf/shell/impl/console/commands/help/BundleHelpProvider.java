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
name|impl
operator|.
name|console
operator|.
name|commands
operator|.
name|help
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
name|ByteArrayOutputStream
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
name|net
operator|.
name|URL
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
name|api
operator|.
name|console
operator|.
name|Session
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
name|api
operator|.
name|console
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
name|karaf
operator|.
name|shell
operator|.
name|impl
operator|.
name|console
operator|.
name|commands
operator|.
name|help
operator|.
name|wikidoc
operator|.
name|AnsiPrintingWikiVisitor
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
name|impl
operator|.
name|console
operator|.
name|commands
operator|.
name|help
operator|.
name|wikidoc
operator|.
name|WikiParser
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
name|impl
operator|.
name|console
operator|.
name|commands
operator|.
name|help
operator|.
name|wikidoc
operator|.
name|WikiVisitor
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
name|support
operator|.
name|ShellUtil
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
name|FrameworkUtil
import|;
end_import

begin_class
specifier|public
class|class
name|BundleHelpProvider
implements|implements
name|HelpProvider
block|{
annotation|@
name|Override
specifier|public
name|String
name|getHelp
parameter_list|(
name|Session
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
literal|"bundle|"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|"bundle|"
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
if|if
condition|(
name|path
operator|.
name|matches
argument_list|(
literal|"[0-9]*"
argument_list|)
condition|)
block|{
name|long
name|id
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|BundleContext
name|bundleContext
init|=
name|FrameworkUtil
operator|.
name|getBundle
argument_list|(
name|getClass
argument_list|()
argument_list|)
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
name|Bundle
name|bundle
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|!=
literal|null
condition|)
block|{
name|String
name|title
init|=
name|ShellUtil
operator|.
name|getBundleName
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|ps
init|=
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|ps
operator|.
name|println
argument_list|(
literal|"\n"
operator|+
name|title
argument_list|)
expr_stmt|;
name|ps
operator|.
name|println
argument_list|(
name|ShellUtil
operator|.
name|getUnderlineString
argument_list|(
name|title
argument_list|)
argument_list|)
expr_stmt|;
name|URL
name|bundleInfo
init|=
name|bundle
operator|.
name|getEntry
argument_list|(
literal|"OSGI-INF/bundle.info"
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundleInfo
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|bundleInfo
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|int
name|maxSize
init|=
literal|80
decl_stmt|;
name|Terminal
name|terminal
init|=
name|session
operator|.
name|getTerminal
argument_list|()
decl_stmt|;
if|if
condition|(
name|terminal
operator|!=
literal|null
condition|)
block|{
name|maxSize
operator|=
name|terminal
operator|.
name|getWidth
argument_list|()
expr_stmt|;
block|}
name|WikiVisitor
name|visitor
init|=
operator|new
name|AnsiPrintingWikiVisitor
argument_list|(
name|ps
argument_list|,
name|maxSize
argument_list|)
decl_stmt|;
name|WikiParser
name|parser
init|=
operator|new
name|WikiParser
argument_list|(
name|visitor
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|ps
operator|.
name|close
argument_list|()
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
block|}
end_class

end_unit

