begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|utils
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|String
operator|.
name|format
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|reflect
operator|.
name|MethodUtils
operator|.
name|invokeMethod
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|deployer
operator|.
name|kar
operator|.
name|KarArtifactInstaller
operator|.
name|FEATURE_CLASSIFIER
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Set
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
name|RepositoryUtils
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
name|plugin
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
name|maven
operator|.
name|project
operator|.
name|ProjectBuildingRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|RepositorySystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|RepositorySystemSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
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
name|sonatype
operator|.
name|aether
operator|.
name|collection
operator|.
name|CollectRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|collection
operator|.
name|CollectResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|collection
operator|.
name|DependencyCollectionContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|collection
operator|.
name|DependencyCollectionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|collection
operator|.
name|DependencyGraphTransformer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|collection
operator|.
name|DependencySelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|graph
operator|.
name|Dependency
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|graph
operator|.
name|DependencyNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|repository
operator|.
name|RemoteRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|resolution
operator|.
name|ArtifactRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|resolution
operator|.
name|ArtifactResolutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|resolution
operator|.
name|ArtifactResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|DefaultRepositorySystemSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|artifact
operator|.
name|DefaultArtifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|graph
operator|.
name|selector
operator|.
name|AndDependencySelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|graph
operator|.
name|selector
operator|.
name|ExclusionDependencySelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|graph
operator|.
name|selector
operator|.
name|OptionalDependencySelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|graph
operator|.
name|transformer
operator|.
name|ChainedDependencyGraphTransformer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|graph
operator|.
name|transformer
operator|.
name|ConflictMarker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|graph
operator|.
name|transformer
operator|.
name|JavaDependencyContextRefiner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|graph
operator|.
name|transformer
operator|.
name|JavaEffectiveScopeCalculator
import|;
end_import

begin_comment
comment|/**  * This is a dependency helper compliant with Maven 3.0 (using Aether Sonatype lib)  */
end_comment

begin_class
specifier|public
class|class
name|Dependency30Helper
implements|implements
name|DependencyHelper
block|{
comment|/**      * The entry point to Aether, i.e. the component doing all the work.      */
specifier|private
specifier|final
name|RepositorySystem
name|repositorySystem
decl_stmt|;
comment|/**      * The current repository/network configuration of Maven.      */
specifier|private
specifier|final
name|RepositorySystemSession
name|repositorySystemSession
decl_stmt|;
comment|/**      * The project's defined repositories to use for the resolution of project dependencies.      */
specifier|private
specifier|final
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|projectRepositories
decl_stmt|;
comment|// dependencies we are interested in
specifier|protected
name|Set
argument_list|<
name|LocalDependency
argument_list|>
name|localDependencies
decl_stmt|;
comment|// log of what happened during search
specifier|protected
name|String
name|treeListing
decl_stmt|;
specifier|public
name|Dependency30Helper
parameter_list|(
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|projectRepositories
parameter_list|,
name|RepositorySystemSession
name|repositorySystemSession
parameter_list|,
name|RepositorySystem
name|repositorySystem
parameter_list|)
block|{
name|this
operator|.
name|projectRepositories
operator|=
name|projectRepositories
expr_stmt|;
name|this
operator|.
name|repositorySystemSession
operator|=
name|repositorySystemSession
expr_stmt|;
name|this
operator|.
name|repositorySystem
operator|=
name|repositorySystem
expr_stmt|;
block|}
specifier|public
name|void
name|setRepositorySession
parameter_list|(
specifier|final
name|ProjectBuildingRequest
name|request
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
try|try
block|{
name|invokeMethod
argument_list|(
name|request
argument_list|,
literal|"setRepositorySession"
argument_list|,
name|repositorySystemSession
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
decl||
name|IllegalAccessException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Cannot set repository session on project building request"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|LocalDependency
argument_list|>
name|getLocalDependencies
parameter_list|()
block|{
return|return
name|this
operator|.
name|localDependencies
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTreeListing
parameter_list|()
block|{
return|return
name|this
operator|.
name|treeListing
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|getDependencies
parameter_list|(
name|MavenProject
name|project
parameter_list|,
name|boolean
name|useTransitiveDependencies
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|DependencyNode
name|rootNode
init|=
name|getDependencyTree
argument_list|(
name|RepositoryUtils
operator|.
name|toArtifact
argument_list|(
name|project
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|()
decl_stmt|;
name|scanner
operator|.
name|scan
argument_list|(
name|rootNode
argument_list|,
name|useTransitiveDependencies
argument_list|)
expr_stmt|;
name|localDependencies
operator|=
name|scanner
operator|.
name|localDependencies
expr_stmt|;
name|treeListing
operator|=
name|scanner
operator|.
name|getLog
argument_list|()
expr_stmt|;
block|}
specifier|private
name|DependencyNode
name|getDependencyTree
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
try|try
block|{
name|CollectRequest
name|collectRequest
init|=
operator|new
name|CollectRequest
argument_list|(
operator|new
name|Dependency
argument_list|(
name|artifact
argument_list|,
literal|"compile"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|projectRepositories
argument_list|)
decl_stmt|;
name|DefaultRepositorySystemSession
name|session
init|=
operator|new
name|DefaultRepositorySystemSession
argument_list|(
name|repositorySystemSession
argument_list|)
decl_stmt|;
name|session
operator|.
name|setDependencySelector
argument_list|(
operator|new
name|AndDependencySelector
argument_list|(
operator|new
name|OptionalDependencySelector
argument_list|()
argument_list|,
operator|new
name|ScopeDependencySelector1
argument_list|()
argument_list|,
operator|new
name|ExclusionDependencySelector
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|DependencyGraphTransformer
name|transformer
init|=
operator|new
name|ChainedDependencyGraphTransformer
argument_list|(
operator|new
name|ConflictMarker
argument_list|()
argument_list|,
operator|new
name|JavaEffectiveScopeCalculator
argument_list|()
argument_list|,
operator|new
name|JavaDependencyContextRefiner
argument_list|()
argument_list|)
decl_stmt|;
name|session
operator|.
name|setDependencyGraphTransformer
argument_list|(
name|transformer
argument_list|)
expr_stmt|;
name|CollectResult
name|result
init|=
name|repositorySystem
operator|.
name|collectDependencies
argument_list|(
name|session
argument_list|,
name|collectRequest
argument_list|)
decl_stmt|;
return|return
name|result
operator|.
name|getRoot
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|DependencyCollectionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Cannot build project dependency tree"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Aether's ScopeDependencySelector appears to always exclude the configured scopes (test and provided) and there is no way to configure it to      * accept the top level provided scope dependencies. We need this 3 layers cake since Aether never actually uses the top level selector you give it,      * it always starts by getting the child to apply to the project's dependencies.      */
specifier|private
specifier|static
class|class
name|ScopeDependencySelector1
implements|implements
name|DependencySelector
block|{
specifier|private
name|DependencySelector
name|child
init|=
operator|new
name|ScopeDependencySelector2
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|selectDependency
parameter_list|(
name|Dependency
name|dependency
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"This does not appear to be called"
argument_list|)
throw|;
block|}
specifier|public
name|DependencySelector
name|deriveChildSelector
parameter_list|(
name|DependencyCollectionContext
name|context
parameter_list|)
block|{
return|return
name|child
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ScopeDependencySelector2
implements|implements
name|DependencySelector
block|{
specifier|private
name|DependencySelector
name|child
init|=
operator|new
name|ScopeDependencySelector3
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|selectDependency
parameter_list|(
name|Dependency
name|dependency
parameter_list|)
block|{
name|String
name|scope
init|=
name|dependency
operator|.
name|getScope
argument_list|()
decl_stmt|;
return|return
operator|!
literal|"test"
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
return|;
block|}
specifier|public
name|DependencySelector
name|deriveChildSelector
parameter_list|(
name|DependencyCollectionContext
name|context
parameter_list|)
block|{
return|return
name|child
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ScopeDependencySelector3
implements|implements
name|DependencySelector
block|{
specifier|public
name|boolean
name|selectDependency
parameter_list|(
name|Dependency
name|dependency
parameter_list|)
block|{
name|String
name|scope
init|=
name|dependency
operator|.
name|getScope
argument_list|()
decl_stmt|;
return|return
operator|!
literal|"test"
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
return|;
block|}
specifier|public
name|DependencySelector
name|deriveChildSelector
parameter_list|(
name|DependencyCollectionContext
name|context
parameter_list|)
block|{
return|return
name|this
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|Scanner
block|{
specifier|private
specifier|static
enum|enum
name|Accept
block|{
name|ACCEPT
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
block|,
name|PROVIDED
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
block|,
name|STOP
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
block|;
specifier|private
specifier|final
name|boolean
name|more
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|local
decl_stmt|;
specifier|private
name|Accept
parameter_list|(
name|boolean
name|more
parameter_list|,
name|boolean
name|local
parameter_list|)
block|{
name|this
operator|.
name|more
operator|=
name|more
expr_stmt|;
name|this
operator|.
name|local
operator|=
name|local
expr_stmt|;
block|}
specifier|public
name|boolean
name|isContinue
parameter_list|()
block|{
return|return
name|more
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|local
return|;
block|}
block|}
comment|// all the dependencies needed, with provided dependencies removed
specifier|private
specifier|final
name|Set
argument_list|<
name|LocalDependency
argument_list|>
name|localDependencies
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|// dependencies from ancestor, to be removed from localDependencies
specifier|private
specifier|final
name|Set
argument_list|<
name|Artifact
argument_list|>
name|dependencies
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|StringBuilder
name|log
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|public
name|void
name|scan
parameter_list|(
name|DependencyNode
name|rootNode
parameter_list|,
name|boolean
name|useTransitiveDependencies
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
for|for
control|(
name|DependencyNode
name|child
range|:
name|rootNode
operator|.
name|getChildren
argument_list|()
control|)
block|{
name|scan
argument_list|(
name|rootNode
argument_list|,
name|child
argument_list|,
name|Accept
operator|.
name|ACCEPT
argument_list|,
name|useTransitiveDependencies
argument_list|,
literal|false
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useTransitiveDependencies
condition|)
block|{
name|localDependencies
operator|.
name|removeAll
argument_list|(
name|dependencies
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|scan
parameter_list|(
name|DependencyNode
name|parentNode
parameter_list|,
name|DependencyNode
name|dependencyNode
parameter_list|,
name|Accept
name|parentAccept
parameter_list|,
name|boolean
name|useTransitiveDependencies
parameter_list|,
name|boolean
name|isFromFeature
parameter_list|,
name|String
name|indent
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|Accept
name|accept
init|=
name|accept
argument_list|(
name|dependencyNode
argument_list|,
name|parentAccept
argument_list|)
decl_stmt|;
if|if
condition|(
name|accept
operator|.
name|isLocal
argument_list|()
condition|)
block|{
if|if
condition|(
name|isFromFeature
condition|)
block|{
if|if
condition|(
operator|!
name|isFeature
argument_list|(
name|dependencyNode
argument_list|)
condition|)
block|{
name|log
operator|.
name|append
argument_list|(
name|indent
argument_list|)
operator|.
name|append
argument_list|(
literal|"from feature:"
argument_list|)
operator|.
name|append
argument_list|(
name|dependencyNode
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|dependencies
operator|.
name|add
argument_list|(
name|dependencyNode
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|append
argument_list|(
name|indent
argument_list|)
operator|.
name|append
argument_list|(
literal|"is feature:"
argument_list|)
operator|.
name|append
argument_list|(
name|dependencyNode
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|append
argument_list|(
name|indent
argument_list|)
operator|.
name|append
argument_list|(
literal|"local:"
argument_list|)
operator|.
name|append
argument_list|(
name|dependencyNode
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|localDependencies
operator|.
name|contains
argument_list|(
name|dependencyNode
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|append
argument_list|(
name|indent
argument_list|)
operator|.
name|append
argument_list|(
literal|"already in feature, returning:"
argument_list|)
operator|.
name|append
argument_list|(
name|dependencyNode
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// TODO resolve scope conflicts
name|localDependencies
operator|.
name|add
argument_list|(
operator|new
name|LocalDependency
argument_list|(
name|dependencyNode
operator|.
name|getDependency
argument_list|()
operator|.
name|getScope
argument_list|()
argument_list|,
name|dependencyNode
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|parentNode
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFeature
argument_list|(
name|dependencyNode
argument_list|)
operator|||
operator|!
name|useTransitiveDependencies
condition|)
block|{
name|isFromFeature
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|useTransitiveDependencies
operator|&&
name|accept
operator|.
name|isContinue
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|DependencyNode
argument_list|>
name|children
init|=
name|dependencyNode
operator|.
name|getChildren
argument_list|()
decl_stmt|;
for|for
control|(
name|DependencyNode
name|child
range|:
name|children
control|)
block|{
name|scan
argument_list|(
name|dependencyNode
argument_list|,
name|child
argument_list|,
name|accept
argument_list|,
name|useTransitiveDependencies
argument_list|,
name|isFromFeature
argument_list|,
name|indent
operator|+
literal|" "
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|String
name|getLog
parameter_list|()
block|{
return|return
name|log
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|Accept
name|accept
parameter_list|(
name|DependencyNode
name|dependency
parameter_list|,
name|Accept
name|previous
parameter_list|)
block|{
name|String
name|scope
init|=
name|dependency
operator|.
name|getDependency
argument_list|()
operator|.
name|getScope
argument_list|()
decl_stmt|;
if|if
condition|(
name|scope
operator|==
literal|null
operator|||
literal|"runtime"
operator|.
name|equalsIgnoreCase
argument_list|(
name|scope
argument_list|)
operator|||
literal|"compile"
operator|.
name|equalsIgnoreCase
argument_list|(
name|scope
argument_list|)
condition|)
block|{
return|return
name|previous
return|;
block|}
if|if
condition|(
literal|"provided"
operator|.
name|equalsIgnoreCase
argument_list|(
name|scope
argument_list|)
condition|)
block|{
return|return
name|Accept
operator|.
name|PROVIDED
return|;
block|}
return|return
name|Accept
operator|.
name|STOP
return|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isFeature
parameter_list|(
name|DependencyNode
name|dependencyNode
parameter_list|)
block|{
return|return
name|isFeature
argument_list|(
name|dependencyNode
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isFeature
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|artifact
operator|.
name|getExtension
argument_list|()
operator|.
name|equals
argument_list|(
literal|"kar"
argument_list|)
operator|||
name|FEATURE_CLASSIFIER
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isArtifactAFeature
parameter_list|(
name|Object
name|artifact
parameter_list|)
block|{
return|return
name|Dependency30Helper
operator|.
name|isFeature
argument_list|(
operator|(
name|Artifact
operator|)
name|artifact
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getBaseVersion
parameter_list|(
name|Object
name|artifact
parameter_list|)
block|{
return|return
operator|(
operator|(
name|Artifact
operator|)
name|artifact
operator|)
operator|.
name|getBaseVersion
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getGroupId
parameter_list|(
name|Object
name|artifact
parameter_list|)
block|{
return|return
operator|(
operator|(
name|Artifact
operator|)
name|artifact
operator|)
operator|.
name|getGroupId
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getArtifactId
parameter_list|(
name|Object
name|artifact
parameter_list|)
block|{
return|return
operator|(
operator|(
name|Artifact
operator|)
name|artifact
operator|)
operator|.
name|getArtifactId
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassifier
parameter_list|(
name|Object
name|artifact
parameter_list|)
block|{
return|return
operator|(
operator|(
name|Artifact
operator|)
name|artifact
operator|)
operator|.
name|getClassifier
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|File
name|resolve
parameter_list|(
name|Object
name|artifact
parameter_list|,
name|Log
name|log
parameter_list|)
block|{
name|ArtifactRequest
name|request
init|=
operator|new
name|ArtifactRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setArtifact
argument_list|(
operator|(
name|Artifact
operator|)
name|artifact
argument_list|)
expr_stmt|;
name|request
operator|.
name|setRepositories
argument_list|(
name|projectRepositories
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Resolving artifact "
operator|+
name|artifact
operator|+
literal|" from "
operator|+
name|projectRepositories
argument_list|)
expr_stmt|;
name|ArtifactResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|repositorySystem
operator|.
name|resolveArtifact
argument_list|(
name|repositorySystemSession
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArtifactResolutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Could not resolve "
operator|+
name|artifact
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved artifact "
operator|+
name|artifact
operator|+
literal|" to "
operator|+
name|result
operator|.
name|getArtifact
argument_list|()
operator|.
name|getFile
argument_list|()
operator|+
literal|" from "
operator|+
name|result
operator|.
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|getArtifact
argument_list|()
operator|.
name|getFile
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|File
name|resolveById
parameter_list|(
name|String
name|id
parameter_list|,
name|Log
name|log
parameter_list|)
throws|throws
name|MojoFailureException
block|{
if|if
condition|(
name|id
operator|.
name|startsWith
argument_list|(
literal|"mvn:"
argument_list|)
condition|)
block|{
if|if
condition|(
name|id
operator|.
name|contains
argument_list|(
literal|"!"
argument_list|)
condition|)
block|{
name|id
operator|=
name|id
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|"mvn:"
operator|.
name|length
argument_list|()
argument_list|)
operator|+
name|id
operator|.
name|substring
argument_list|(
name|id
operator|.
name|indexOf
argument_list|(
literal|"!"
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|id
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|id
operator|=
name|id
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|id
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
name|id
operator|=
name|MavenUtil
operator|.
name|mvnToAether
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|ArtifactRequest
name|request
init|=
operator|new
name|ArtifactRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setArtifact
argument_list|(
operator|new
name|DefaultArtifact
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setRepositories
argument_list|(
operator|(
name|List
argument_list|<
name|RemoteRepository
argument_list|>
operator|)
name|projectRepositories
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Resolving artifact "
operator|+
name|id
operator|+
literal|" from "
operator|+
name|projectRepositories
argument_list|)
expr_stmt|;
name|ArtifactResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|repositorySystem
operator|.
name|resolveArtifact
argument_list|(
name|repositorySystemSession
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArtifactResolutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Could not resolve "
operator|+
name|id
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|MojoFailureException
argument_list|(
name|format
argument_list|(
literal|"Couldn't resolve artifact %s"
argument_list|,
name|id
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved artifact "
operator|+
name|id
operator|+
literal|" to "
operator|+
name|result
operator|.
name|getArtifact
argument_list|()
operator|.
name|getFile
argument_list|()
operator|+
literal|" from "
operator|+
name|result
operator|.
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|getArtifact
argument_list|()
operator|.
name|getFile
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|artifactToMvn
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
name|artifact
parameter_list|,
name|String
name|versionOrRange
parameter_list|)
block|{
return|return
name|this
operator|.
name|artifactToMvn
argument_list|(
name|RepositoryUtils
operator|.
name|toArtifact
argument_list|(
name|artifact
argument_list|)
argument_list|,
name|versionOrRange
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|artifactToMvn
parameter_list|(
name|Object
name|_artifact
parameter_list|,
name|String
name|versionOrRange
parameter_list|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|_artifact
decl_stmt|;
name|String
name|bundleName
decl_stmt|;
if|if
condition|(
name|artifact
operator|.
name|getExtension
argument_list|()
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|)
operator|&&
name|MavenUtil
operator|.
name|isEmpty
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
condition|)
block|{
name|bundleName
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"mvn:%s/%s/%s"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|versionOrRange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|MavenUtil
operator|.
name|isEmpty
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
condition|)
block|{
name|bundleName
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"mvn:%s/%s/%s/%s"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|versionOrRange
argument_list|,
name|artifact
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bundleName
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"mvn:%s/%s/%s/%s/%s"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|versionOrRange
argument_list|,
name|artifact
operator|.
name|getExtension
argument_list|()
argument_list|,
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|bundleName
return|;
block|}
annotation|@
name|Override
specifier|public
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
name|mvnToArtifact
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|name
operator|=
name|MavenUtil
operator|.
name|mvnToAether
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|DefaultArtifact
name|artifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
name|mavenArtifact
init|=
name|RepositoryUtils
operator|.
name|toArtifact
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
return|return
name|mavenArtifact
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|pathFromMaven
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|name
return|;
block|}
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|name
operator|=
name|MavenUtil
operator|.
name|mvnToAether
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|pathFromAether
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|pathFromAether
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|DefaultArtifact
name|artifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
name|mavenArtifact
init|=
name|RepositoryUtils
operator|.
name|toArtifact
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
return|return
name|MavenUtil
operator|.
name|layout
operator|.
name|pathOf
argument_list|(
name|mavenArtifact
argument_list|)
return|;
block|}
block|}
end_class

end_unit

