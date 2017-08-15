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
name|Properties
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
name|lifecycle
operator|.
name|Service
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
name|annotation
operator|.
name|Managed
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
name|annotation
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
name|annotation
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
name|annotation
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
name|model
operator|.
name|Resource
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
name|Component
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|plexus
operator|.
name|build
operator|.
name|incremental
operator|.
name|BuildContext
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
literal|"BNDExtension-Bundle-Activator"
argument_list|)
specifier|protected
name|String
name|activatorProperty
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"BNDExtension-Require-Capability"
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
literal|"BNDExtension-Provide-Capability"
argument_list|)
specifier|protected
name|String
name|capabilitiesProperty
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/generated/karaf-tracker"
argument_list|)
specifier|protected
name|String
name|outputDirectory
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
annotation|@
name|Component
specifier|private
name|BuildContext
name|buildContext
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
name|boolean
name|addSourceDirectory
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|requirements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|capabilities
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|activators
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
name|URL
name|outputDirectoryUrl
init|=
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
name|outputDirectoryUrl
operator|.
name|getPath
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
if|if
condition|(
name|BundleActivator
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|activators
operator|.
name|add
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
name|writeServiceProperties
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
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
name|requirements
operator|.
name|add
argument_list|(
name|getRequirement
argument_list|(
name|req
argument_list|)
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
name|capabilities
operator|.
name|add
argument_list|(
name|getCapability
argument_list|(
name|cap
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|addSourceDirectory
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|addSourceDirectory
condition|)
block|{
name|Resource
name|resource
init|=
operator|new
name|Resource
argument_list|()
decl_stmt|;
name|resource
operator|.
name|setDirectory
argument_list|(
name|outputDirectory
argument_list|)
expr_stmt|;
name|project
operator|.
name|addResource
argument_list|(
name|resource
argument_list|)
expr_stmt|;
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
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|requirements
argument_list|)
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
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|capabilities
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|activators
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|project
operator|.
name|getProperties
argument_list|()
operator|.
name|setProperty
argument_list|(
name|activatorProperty
argument_list|,
name|activators
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|project
operator|.
name|getProperties
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"BNDExtension-Private-Package"
argument_list|,
literal|"org.apache.karaf.util.tracker"
argument_list|)
expr_stmt|;
name|project
operator|.
name|getProperties
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"BNDPrependExtension-Import-Package"
argument_list|,
literal|"!org.apache.karaf.util.tracker.annotation"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|services
init|=
name|finder
operator|.
name|findAnnotatedClasses
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|packages
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|services
control|)
block|{
name|packages
operator|.
name|add
argument_list|(
name|clazz
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|packages
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|project
operator|.
name|getProperties
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"BNDExtension-Karaf-Commands"
argument_list|,
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|packages
argument_list|)
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
name|String
name|getRequirement
parameter_list|(
name|RequireService
name|req
parameter_list|)
block|{
name|String
name|fltWithClass
init|=
name|combine
argument_list|(
name|req
operator|.
name|filter
argument_list|()
argument_list|,
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
argument_list|)
decl_stmt|;
return|return
literal|"osgi.service;effective:=active;filter:=\""
operator|+
name|fltWithClass
operator|+
literal|"\""
return|;
block|}
specifier|private
name|String
name|getCapability
parameter_list|(
name|ProvideService
name|cap
parameter_list|)
block|{
return|return
literal|"osgi.service;effective:=active;objectClass="
operator|+
name|cap
operator|.
name|value
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|private
name|void
name|writeServiceProperties
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClazz
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|Services
name|services
init|=
name|serviceClazz
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
name|props
operator|.
name|setProperty
argument_list|(
name|req
operator|.
name|value
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|req
operator|.
name|filter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Managed
name|managed
init|=
name|serviceClazz
operator|.
name|getAnnotation
argument_list|(
name|Managed
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|managed
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
literal|"pid"
argument_list|,
name|managed
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|outputDirectory
argument_list|,
literal|"OSGI-INF/karaf-tracker/"
operator|+
name|serviceClazz
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
try|try
init|(
name|OutputStream
name|os
init|=
name|buildContext
operator|.
name|newFileOutputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
name|props
operator|.
name|store
argument_list|(
name|os
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|combine
parameter_list|(
name|String
name|filter1
parameter_list|,
name|String
name|filter2
parameter_list|)
block|{
if|if
condition|(
name|filter1
operator|!=
literal|null
operator|&&
operator|!
name|filter1
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|"(&"
operator|+
name|filter2
operator|+
name|filter1
operator|+
literal|")"
return|;
block|}
else|else
block|{
return|return
name|filter2
return|;
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

