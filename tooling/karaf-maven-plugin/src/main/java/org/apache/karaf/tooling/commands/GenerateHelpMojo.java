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
name|commands
package|;
end_package

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
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
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
name|maven
operator|.
name|artifact
operator|.
name|DependencyResolutionRequiredException
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
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
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
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
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
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
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
name|plugins
operator|.
name|annotations
operator|.
name|ResolutionScope
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

begin_comment
comment|/**  * Generates help documentation for Karaf commands  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"commands-generate-help"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|GENERATE_RESOURCES
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|RUNTIME
argument_list|,
name|inheritByDefault
operator|=
literal|false
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|)
specifier|public
class|class
name|GenerateHelpMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The output folder      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/docbkx/sources"
argument_list|)
specifier|protected
name|File
name|targetFolder
decl_stmt|;
comment|/**      * The output format      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"docbx"
argument_list|)
specifier|protected
name|String
name|format
decl_stmt|;
comment|/**      * The classloader to use for loading the commands.      * Can be "project" or "plugin"      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"project"
argument_list|)
specifier|protected
name|String
name|classLoader
decl_stmt|;
comment|/**      * Includes the --help command output in the generated documentation      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|protected
name|boolean
name|includeHelpOption
decl_stmt|;
comment|/**      * The maven project.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project}"
argument_list|)
specifier|protected
name|MavenProject
name|project
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
specifier|private
specifier|static
specifier|final
name|String
name|FORMAT_ASCIIDOC
init|=
literal|"asciidoc"
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
operator|&&
operator|!
name|FORMAT_ASCIIDOC
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
literal|". Supported formats are: asciidoc, docbx, or conf."
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
init|=
name|createFinder
argument_list|(
name|classLoader
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
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
name|CommandHelpPrinter
name|helpPrinter
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|FORMAT_ASCIIDOC
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|helpPrinter
operator|=
operator|new
name|AsciiDoctorCommandHelpPrinter
argument_list|()
expr_stmt|;
block|}
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
name|helpPrinter
operator|=
operator|new
name|UserConfCommandHelpPrinter
argument_list|()
expr_stmt|;
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
name|helpPrinter
operator|=
operator|new
name|DocBookCommandHelpPrinter
argument_list|()
expr_stmt|;
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
name|String
name|commandSuffix
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|FORMAT_ASCIIDOC
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|commandSuffix
operator|=
literal|"adoc"
expr_stmt|;
block|}
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
name|commandSuffix
operator|=
literal|"conf"
expr_stmt|;
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
name|commandSuffix
operator|=
literal|"xml"
expr_stmt|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|classes
control|)
block|{
try|try
block|{
name|Action
name|action
init|=
operator|(
name|Action
operator|)
name|clazz
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Command
name|cmd
init|=
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// skip the *-help command
if|if
condition|(
name|cmd
operator|.
name|scope
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
condition|)
continue|continue;
name|File
name|output
init|=
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
literal|"."
operator|+
name|commandSuffix
argument_list|)
decl_stmt|;
name|FileOutputStream
name|outStream
init|=
operator|new
name|FileOutputStream
argument_list|(
name|output
argument_list|)
decl_stmt|;
name|PrintStream
name|out
init|=
operator|new
name|PrintStream
argument_list|(
name|outStream
argument_list|)
decl_stmt|;
name|helpPrinter
operator|.
name|printHelp
argument_list|(
name|action
argument_list|,
name|out
argument_list|,
name|includeHelpOption
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|outStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|commands
operator|.
name|computeIfAbsent
argument_list|(
name|cmd
operator|.
name|scope
argument_list|()
argument_list|,
name|k
lambda|->
operator|new
name|TreeSet
argument_list|<>
argument_list|()
argument_list|)
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
name|String
name|overViewSuffix
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|FORMAT_ASCIIDOC
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|overViewSuffix
operator|=
literal|"adoc"
expr_stmt|;
block|}
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
name|overViewSuffix
operator|=
literal|"conf"
expr_stmt|;
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
name|overViewSuffix
operator|=
literal|"xml"
expr_stmt|;
block|}
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
literal|"commands."
operator|+
name|overViewSuffix
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|helpPrinter
operator|.
name|printOverview
argument_list|(
name|commands
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
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
specifier|private
name|ClassFinder
name|createFinder
parameter_list|(
name|String
name|classloaderType
parameter_list|)
throws|throws
name|DependencyResolutionRequiredException
throws|,
name|MalformedURLException
throws|,
name|Exception
throws|,
name|MojoFailureException
block|{
name|ClassFinder
name|finder
decl_stmt|;
if|if
condition|(
literal|"project"
operator|.
name|equals
argument_list|(
name|classloaderType
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
return|return
name|finder
return|;
block|}
block|}
end_class

end_unit

