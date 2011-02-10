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
name|tooling
operator|.
name|cmdhelp
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|OutputStreamWriter
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
name|Writer
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
name|Field
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
name|URLClassLoader
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
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|gogo
operator|.
name|commands
operator|.
name|Action
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
name|gogo
operator|.
name|commands
operator|.
name|Argument
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
name|gogo
operator|.
name|commands
operator|.
name|Command
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
name|gogo
operator|.
name|commands
operator|.
name|Option
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
name|gogo
operator|.
name|commands
operator|.
name|basic
operator|.
name|ActionPreparator
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
name|commands
operator|.
name|BlueprintCommand
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|AbstractMojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoFailureException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|project
operator|.
name|MavenProject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xbean
operator|.
name|finder
operator|.
name|ClassFinder
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

begin_comment
comment|/**  * Generates docbook for Karaf commands  *  * @version $Revision: 1.1 $  * @goal cmdhelp  * @phase generate-resources  * @execute phase="generate-resources"  * @requiresDependencyResolution runtime  * @inheritByDefault false  * @description Generates help for Karaf commands  */
end_comment

begin_class
specifier|public
class|class
name|CmdHelpMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The maven project.      *      * @parameter expression="${project}"      * @required      */
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The output folder      *      * @parameter default-value="${project.build.directory}/docbkx/sources"      */
specifier|protected
name|File
name|targetFolder
decl_stmt|;
comment|/**      * The output format      *      * @parameter default-value="docbx";      */
specifier|protected
name|String
name|format
decl_stmt|;
comment|/**      * The classloader to use to load the commands      *      * @parameter default-value="project"      */
specifier|protected
name|String
name|classLoader
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FORMAT_CONF
init|=
literal|"conf"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FORMAT_DOCBX
init|=
literal|"docbx"
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
try|try
block|{
if|if
condition|(
operator|!
name|FORMAT_DOCBX
operator|.
name|equals
argument_list|(
name|format
argument_list|)
operator|&&
operator|!
name|FORMAT_CONF
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Unsupported format: "
operator|+
name|format
operator|+
literal|". Supported formats are: docbx or conf."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|targetFolder
operator|.
name|exists
argument_list|()
condition|)
block|{
name|targetFolder
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|ClassFinder
name|finder
decl_stmt|;
if|if
condition|(
literal|"project"
operator|.
name|equals
argument_list|(
name|classLoader
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|URL
argument_list|>
name|urls
init|=
operator|new
name|ArrayList
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|project
operator|.
name|getCompileClasspathElements
argument_list|()
control|)
block|{
name|String
name|path
init|=
operator|(
name|String
operator|)
name|object
decl_stmt|;
name|urls
operator|.
name|add
argument_list|(
operator|new
name|File
argument_list|(
name|path
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ClassLoader
name|loader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|urls
operator|.
name|toArray
argument_list|(
operator|new
name|URL
index|[
name|urls
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|finder
operator|=
operator|new
name|ClassFinder
argument_list|(
name|loader
argument_list|,
name|urls
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"plugin"
operator|.
name|equals
argument_list|(
name|classLoader
argument_list|)
condition|)
block|{
name|finder
operator|=
operator|new
name|ClassFinder
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"classLoader attribute must be 'project' or 'plugin'"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|Class
argument_list|>
name|classes
init|=
name|finder
operator|.
name|findAnnotatedClasses
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|classes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"No command found"
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|commands
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|clazz
range|:
name|classes
control|)
block|{
try|try
block|{
name|String
name|help
init|=
operator|new
name|HelpPrinter
argument_list|(
name|clazz
argument_list|)
operator|.
name|printHelp
argument_list|(
name|format
argument_list|)
decl_stmt|;
name|Command
name|cmd
init|=
operator|(
name|Command
operator|)
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
name|File
name|output
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|FORMAT_DOCBX
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|output
operator|=
operator|new
name|File
argument_list|(
name|targetFolder
argument_list|,
name|cmd
operator|.
name|scope
argument_list|()
operator|+
literal|"-"
operator|+
name|cmd
operator|.
name|name
argument_list|()
operator|+
literal|".xml"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|FORMAT_CONF
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|output
operator|=
operator|new
name|File
argument_list|(
name|targetFolder
argument_list|,
name|cmd
operator|.
name|scope
argument_list|()
operator|+
literal|"-"
operator|+
name|cmd
operator|.
name|name
argument_list|()
operator|+
literal|".conf"
argument_list|)
expr_stmt|;
block|}
name|Writer
name|writer
init|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|output
argument_list|)
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|help
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|cmds
init|=
name|commands
operator|.
name|get
argument_list|(
name|cmd
operator|.
name|scope
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmds
operator|==
literal|null
condition|)
block|{
name|cmds
operator|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|commands
operator|.
name|put
argument_list|(
name|cmd
operator|.
name|scope
argument_list|()
argument_list|,
name|cmds
argument_list|)
expr_stmt|;
block|}
name|cmds
operator|.
name|add
argument_list|(
name|cmd
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Found command: "
operator|+
name|cmd
operator|.
name|scope
argument_list|()
operator|+
literal|":"
operator|+
name|cmd
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to write help for "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|FORMAT_DOCBX
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|PrintStream
name|writer
init|=
operator|new
name|PrintStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|targetFolder
argument_list|,
literal|"commands.xml"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<chapter id='commands' xmlns:xi=\"http://www.w3.org/2001/XInclude\">"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<title>Commands</title>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<toc></toc>"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|commands
operator|.
name|keySet
argument_list|()
control|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<section id='commands-"
operator|+
name|key
operator|+
literal|"'>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<title>"
operator|+
name|key
operator|+
literal|"</title>"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|cmd
range|:
name|commands
operator|.
name|get
argument_list|(
name|key
argument_list|)
control|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<xi:include href='"
operator|+
name|key
operator|+
literal|"-"
operator|+
name|cmd
operator|+
literal|".xml' parse='xml'/>"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"</section>"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"</chapter>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|FORMAT_CONF
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|PrintStream
name|writer
init|=
operator|new
name|PrintStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|targetFolder
argument_list|,
literal|"commands.conf"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"h1. Commands"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|commands
operator|.
name|keySet
argument_list|()
control|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"h2. "
operator|+
name|key
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|cmd
range|:
name|commands
operator|.
name|get
argument_list|(
name|key
argument_list|)
control|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"* ["
operator|+
name|key
operator|+
literal|":"
operator|+
name|cmd
operator|+
literal|"|"
operator|+
name|key
operator|+
literal|"-"
operator|+
name|cmd
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Error building commands help"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
class|class
name|HelpPrinter
extends|extends
name|BlueprintCommand
block|{
specifier|private
specifier|final
name|Class
argument_list|<
name|Action
argument_list|>
name|actionClass
decl_stmt|;
specifier|public
name|HelpPrinter
parameter_list|(
name|Class
argument_list|<
name|Action
argument_list|>
name|actionClass
parameter_list|)
block|{
name|this
operator|.
name|actionClass
operator|=
name|actionClass
expr_stmt|;
block|}
specifier|public
name|String
name|printHelp
parameter_list|(
name|String
name|format
parameter_list|)
throws|throws
name|Exception
block|{
name|PrintStream
name|oldout
init|=
name|System
operator|.
name|out
decl_stmt|;
try|try
block|{
name|Action
name|action
init|=
name|actionClass
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|CommandSession
name|session
init|=
operator|new
name|DummyCommandSession
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|newout
init|=
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|System
operator|.
name|setOut
argument_list|(
name|newout
argument_list|)
expr_stmt|;
name|ActionPreparator
name|preparator
decl_stmt|;
if|if
condition|(
name|FORMAT_DOCBX
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|preparator
operator|=
operator|new
name|DocbxPreparator
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|preparator
operator|=
operator|new
name|ConfPreparator
argument_list|()
expr_stmt|;
block|}
name|preparator
operator|.
name|prepare
argument_list|(
name|action
argument_list|,
name|session
argument_list|,
name|Collections
operator|.
expr|<
name|Object
operator|>
name|singletonList
argument_list|(
literal|"--help"
argument_list|)
argument_list|)
expr_stmt|;
name|newout
operator|.
name|close
argument_list|()
expr_stmt|;
name|baos
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
finally|finally
block|{
name|System
operator|.
name|setOut
argument_list|(
name|oldout
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
class|class
name|DocbxPreparator
extends|extends
name|BlueprintActionPreparator
block|{
annotation|@
name|Override
specifier|protected
name|void
name|printUsage
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Action
name|action
parameter_list|,
name|Map
argument_list|<
name|Option
argument_list|,
name|Field
argument_list|>
name|optionsMap
parameter_list|,
name|Map
argument_list|<
name|Argument
argument_list|,
name|Field
argument_list|>
name|argsMap
parameter_list|,
name|PrintStream
name|out
parameter_list|)
block|{
name|Command
name|command
init|=
name|action
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Argument
argument_list|>
name|arguments
init|=
operator|new
name|ArrayList
argument_list|<
name|Argument
argument_list|>
argument_list|(
name|argsMap
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|arguments
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Argument
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Argument
name|o1
parameter_list|,
name|Argument
name|o2
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|o1
operator|.
name|index
argument_list|()
argument_list|)
operator|.
name|compareTo
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|o2
operator|.
name|index
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Option
argument_list|>
name|options
init|=
operator|new
name|HashSet
argument_list|<
name|Option
argument_list|>
argument_list|(
name|optionsMap
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|options
operator|.
name|add
argument_list|(
name|HELP
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<section>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"<title>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|command
operator|.
name|scope
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|command
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</title>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<section>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<title>Description</title>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<para>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|command
operator|.
name|description
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</para>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</section>"
argument_list|)
expr_stmt|;
name|StringBuffer
name|syntax
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|syntax
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s:%s"
argument_list|,
name|command
operator|.
name|scope
argument_list|()
argument_list|,
name|command
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|syntax
operator|.
name|append
argument_list|(
literal|" [options]"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|syntax
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
for|for
control|(
name|Argument
name|argument
range|:
name|arguments
control|)
block|{
name|syntax
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|argument
operator|.
name|required
argument_list|()
condition|?
literal|"%s "
else|:
literal|"[%s] "
argument_list|,
name|argument
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|println
argument_list|(
literal|"<section>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<title>Syntax</title>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<para>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|syntax
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</para>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</section>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<section>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<title>Arguments</title>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<informaltable>"
argument_list|)
expr_stmt|;
for|for
control|(
name|Argument
name|argument
range|:
name|arguments
control|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<tr>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<td>"
operator|+
name|argument
operator|.
name|name
argument_list|()
operator|+
literal|"</td>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<td>"
operator|+
name|argument
operator|.
name|description
argument_list|()
operator|+
literal|"</td>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</tr>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"</informaltable>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</section>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<section>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<title>Options</title>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<informaltable>"
argument_list|)
expr_stmt|;
for|for
control|(
name|Option
name|option
range|:
name|options
control|)
block|{
name|String
name|opt
init|=
name|option
operator|.
name|name
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|alias
range|:
name|option
operator|.
name|aliases
argument_list|()
control|)
block|{
name|opt
operator|+=
literal|", "
operator|+
name|alias
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"<tr>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<td>"
operator|+
name|opt
operator|+
literal|"</td>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<td>"
operator|+
name|option
operator|.
name|description
argument_list|()
operator|+
literal|"</td>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</tr>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"</informaltable>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</section>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"</section>"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
class|class
name|ConfPreparator
extends|extends
name|BlueprintActionPreparator
block|{
annotation|@
name|Override
specifier|protected
name|void
name|printUsage
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|Action
name|action
parameter_list|,
name|Map
argument_list|<
name|Option
argument_list|,
name|Field
argument_list|>
name|optionsMap
parameter_list|,
name|Map
argument_list|<
name|Argument
argument_list|,
name|Field
argument_list|>
name|argsMap
parameter_list|,
name|PrintStream
name|out
parameter_list|)
block|{
name|Command
name|command
init|=
name|action
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Argument
argument_list|>
name|arguments
init|=
operator|new
name|ArrayList
argument_list|<
name|Argument
argument_list|>
argument_list|(
name|argsMap
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|arguments
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Argument
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Argument
name|o1
parameter_list|,
name|Argument
name|o2
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|o1
operator|.
name|index
argument_list|()
argument_list|)
operator|.
name|compareTo
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|o2
operator|.
name|index
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Option
argument_list|>
name|options
init|=
operator|new
name|HashSet
argument_list|<
name|Option
argument_list|>
argument_list|(
name|optionsMap
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|options
operator|.
name|add
argument_list|(
name|HELP
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"h1. "
operator|+
name|command
operator|.
name|scope
argument_list|()
operator|+
literal|":"
operator|+
name|command
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"h2. Description"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|command
operator|.
name|description
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|StringBuffer
name|syntax
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|syntax
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s:%s"
argument_list|,
name|command
operator|.
name|scope
argument_list|()
argument_list|,
name|command
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|syntax
operator|.
name|append
argument_list|(
literal|" \\[options\\]"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|syntax
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
for|for
control|(
name|Argument
name|argument
range|:
name|arguments
control|)
block|{
name|syntax
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|argument
operator|.
name|required
argument_list|()
condition|?
literal|"%s "
else|:
literal|"\\[%s\\] "
argument_list|,
name|argument
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|println
argument_list|(
literal|"h2. Syntax"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|syntax
operator|.
name|toString
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
name|arguments
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"h2. Arguments"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"|| Name || Description ||"
argument_list|)
expr_stmt|;
for|for
control|(
name|Argument
name|argument
range|:
name|arguments
control|)
block|{
name|String
name|description
init|=
name|argument
operator|.
name|description
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|argument
operator|.
name|required
argument_list|()
condition|)
block|{
try|try
block|{
name|argsMap
operator|.
name|get
argument_list|(
name|argument
argument_list|)
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|argsMap
operator|.
name|get
argument_list|(
name|argument
argument_list|)
operator|.
name|get
argument_list|(
name|action
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
operator|&&
operator|(
operator|!
operator|(
name|o
operator|instanceof
name|Boolean
operator|)
operator|||
operator|(
operator|(
name|Boolean
operator|)
name|o
operator|)
operator|)
operator|&&
operator|(
operator|!
operator|(
name|o
operator|instanceof
name|Number
operator|)
operator|||
operator|(
operator|(
name|Number
operator|)
name|o
operator|)
operator|.
name|doubleValue
argument_list|()
operator|!=
literal|0.0
operator|)
condition|)
block|{
name|description
operator|+=
literal|" (defaults to "
operator|+
name|o
operator|.
name|toString
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
name|out
operator|.
name|println
argument_list|(
literal|"| "
operator|+
name|argument
operator|.
name|name
argument_list|()
operator|+
literal|" | "
operator|+
name|description
operator|+
literal|" |"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"h2. Options"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"|| Name || Description ||"
argument_list|)
expr_stmt|;
for|for
control|(
name|Option
name|option
range|:
name|options
control|)
block|{
name|String
name|opt
init|=
name|option
operator|.
name|name
argument_list|()
decl_stmt|;
name|String
name|desc
init|=
name|option
operator|.
name|description
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|alias
range|:
name|option
operator|.
name|aliases
argument_list|()
control|)
block|{
name|opt
operator|+=
literal|", "
operator|+
name|alias
expr_stmt|;
block|}
try|try
block|{
name|optionsMap
operator|.
name|get
argument_list|(
name|option
argument_list|)
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|optionsMap
operator|.
name|get
argument_list|(
name|option
argument_list|)
operator|.
name|get
argument_list|(
name|action
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
operator|&&
operator|(
operator|!
operator|(
name|o
operator|instanceof
name|Boolean
operator|)
operator|||
operator|(
operator|(
name|Boolean
operator|)
name|o
operator|)
operator|)
operator|&&
operator|(
operator|!
operator|(
name|o
operator|instanceof
name|Number
operator|)
operator|||
operator|(
operator|(
name|Number
operator|)
name|o
operator|)
operator|.
name|doubleValue
argument_list|()
operator|!=
literal|0.0
operator|)
condition|)
block|{
name|desc
operator|+=
literal|" (defaults to "
operator|+
name|o
operator|.
name|toString
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
name|out
operator|.
name|println
argument_list|(
literal|"| "
operator|+
name|opt
operator|+
literal|" | "
operator|+
name|desc
operator|+
literal|" |"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|DummyCommandSession
implements|implements
name|CommandSession
block|{
specifier|public
name|Object
name|convert
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Object
name|instance
parameter_list|)
block|{
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
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|put
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{             }
specifier|public
name|Object
name|get
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|PrintStream
name|getConsole
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|InputStream
name|getKeyboard
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{             }
specifier|public
name|Object
name|execute
parameter_list|(
name|CharSequence
name|commandline
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

