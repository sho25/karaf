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
name|tooling
operator|.
name|tracker
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|tracker
operator|.
name|ProvideService
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
name|util
operator|.
name|tracker
operator|.
name|RequireService
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
name|util
operator|.
name|tracker
operator|.
name|Services
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
name|Artifact
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
comment|/**  * Generates service requirement and capabilities for activators  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"service-metadata-generate"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|PROCESS_CLASSES
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|COMPILE_PLUS_RUNTIME
argument_list|,
name|inheritByDefault
operator|=
literal|false
argument_list|)
specifier|public
class|class
name|GenerateServiceMetadata
extends|extends
name|AbstractMojo
block|{
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
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"requirements"
argument_list|)
specifier|protected
name|String
name|requirementsProperty
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"capabilities"
argument_list|)
specifier|protected
name|String
name|capabilitiesProperty
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
name|StringBuilder
name|requirements
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringBuilder
name|capabilities
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
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
name|Services
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|URL
name|classUrl
init|=
name|clazz
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|".class"
argument_list|)
decl_stmt|;
if|if
condition|(
name|classUrl
operator|==
literal|null
operator|||
operator|!
name|classUrl
operator|.
name|getPath
argument_list|()
operator|.
name|startsWith
argument_list|(
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getOutputDirectory
argument_list|()
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Ignoring "
operator|+
name|classUrl
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|Services
name|services
init|=
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|Services
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|services
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RequireService
name|req
range|:
name|services
operator|.
name|requires
argument_list|()
control|)
block|{
name|String
name|flt
init|=
name|req
operator|.
name|filter
argument_list|()
decl_stmt|;
if|if
condition|(
name|flt
operator|!=
literal|null
operator|&&
operator|!
name|flt
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|flt
operator|=
literal|"(&(objectClass="
operator|+
name|req
operator|.
name|value
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|")"
operator|+
name|flt
operator|+
literal|")"
expr_stmt|;
block|}
else|else
block|{
name|flt
operator|=
literal|"(objectClass="
operator|+
name|req
operator|.
name|value
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
if|if
condition|(
name|requirements
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|requirements
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|requirements
operator|.
name|append
argument_list|(
literal|"osgi.service;effective:=active;filter:=\""
argument_list|)
operator|.
name|append
argument_list|(
name|flt
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ProvideService
name|cap
range|:
name|services
operator|.
name|provides
argument_list|()
control|)
block|{
if|if
condition|(
name|capabilities
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|capabilities
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|capabilities
operator|.
name|append
argument_list|(
literal|"osgi.service;effective:=active;objectClass="
argument_list|)
operator|.
name|append
argument_list|(
name|cap
operator|.
name|value
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|project
operator|.
name|getProperties
argument_list|()
operator|.
name|setProperty
argument_list|(
name|requirementsProperty
argument_list|,
name|requirements
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|project
operator|.
name|getProperties
argument_list|()
operator|.
name|setProperty
argument_list|(
name|capabilitiesProperty
argument_list|,
name|capabilities
operator|.
name|toString
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
name|Exception
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
argument_list|<>
argument_list|()
decl_stmt|;
name|urls
operator|.
name|add
argument_list|(
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getOutputDirectory
argument_list|()
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|project
operator|.
name|getArtifacts
argument_list|()
control|)
block|{
name|File
name|file
init|=
name|artifact
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|urls
operator|.
name|add
argument_list|(
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"classpath: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
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

