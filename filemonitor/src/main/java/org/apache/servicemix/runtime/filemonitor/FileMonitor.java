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
name|servicemix
operator|.
name|runtime
operator|.
name|filemonitor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|FileInputStream
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
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Hashtable
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
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|taskdefs
operator|.
name|Jar
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
name|BundleException
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
name|InvalidSyntaxException
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
name|service
operator|.
name|cm
operator|.
name|Configuration
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
name|cm
operator|.
name|ConfigurationAdmin
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
name|packageadmin
operator|.
name|PackageAdmin
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

begin_comment
comment|/**  * Watches a deploy directory for files that are added, updated or removed then processing them.  * Currently we support OSGi bundles, OSGi configuration files and expanded directories of OSGi bundles.  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|FileMonitor
block|{
comment|// Define a few logging levels.
specifier|public
specifier|static
specifier|final
name|int
name|TRACE
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DEBUG
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|INFO
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|WARN
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ERROR
init|=
literal|4
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|FATAL
init|=
literal|5
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NONE
init|=
literal|6
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|CONFIG_DIR
init|=
literal|"org.apache.servicemix.filemonitor.configDir"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|DEPLOY_DIR
init|=
literal|"org.apache.servicemix.filemonitor.monitorDir"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|GENERATED_JAR_DIR
init|=
literal|"org.apache.servicemix.filemonitor.generatedJarDir"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|SCAN_INTERVAL
init|=
literal|"org.apache.servicemix.filemonitor.scanInterval"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|ALIAS_KEY
init|=
literal|"_alias_factory_pid"
decl_stmt|;
specifier|private
name|FileMonitorActivator
name|activator
decl_stmt|;
specifier|private
name|File
name|configDir
init|=
operator|new
name|File
argument_list|(
literal|"./etc"
argument_list|)
decl_stmt|;
specifier|private
name|File
name|deployDir
init|=
operator|new
name|File
argument_list|(
literal|"./deploy"
argument_list|)
decl_stmt|;
specifier|private
name|File
name|generateDir
init|=
operator|new
name|File
argument_list|(
literal|"./data/generated-bundles"
argument_list|)
decl_stmt|;
specifier|private
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|()
decl_stmt|;
specifier|private
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
specifier|private
name|long
name|scanInterval
init|=
literal|500L
decl_stmt|;
specifier|private
name|boolean
name|loggedConfigAdminWarning
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Bundle
argument_list|>
name|changedBundles
init|=
operator|new
name|ArrayList
argument_list|<
name|Bundle
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundlesToStart
init|=
operator|new
name|ArrayList
argument_list|<
name|Bundle
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundlesToUpdate
init|=
operator|new
name|ArrayList
argument_list|<
name|Bundle
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|artifactToBundle
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|int
name|logLevel
init|=
name|NONE
decl_stmt|;
specifier|public
name|FileMonitor
parameter_list|()
block|{     }
specifier|public
name|FileMonitor
parameter_list|(
name|FileMonitorActivator
name|activator
parameter_list|,
name|Dictionary
name|properties
parameter_list|)
block|{
name|this
operator|.
name|activator
operator|=
name|activator
expr_stmt|;
name|File
name|value
init|=
name|getFileValue
argument_list|(
name|properties
argument_list|,
name|CONFIG_DIR
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|configDir
operator|=
name|value
expr_stmt|;
block|}
name|value
operator|=
name|getFileValue
argument_list|(
name|properties
argument_list|,
name|DEPLOY_DIR
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|deployDir
operator|=
name|value
expr_stmt|;
block|}
name|value
operator|=
name|getFileValue
argument_list|(
name|properties
argument_list|,
name|GENERATED_JAR_DIR
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|generateDir
operator|=
name|value
expr_stmt|;
block|}
name|Long
name|i
init|=
name|getLongValue
argument_list|(
name|properties
argument_list|,
name|SCAN_INTERVAL
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
literal|null
condition|)
block|{
name|scanInterval
operator|=
name|i
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|configDir
operator|!=
literal|null
condition|)
block|{
name|configDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|deployDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|generateDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|File
argument_list|>
name|dirs
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|configDir
operator|!=
literal|null
condition|)
block|{
name|dirs
operator|.
name|add
argument_list|(
name|configDir
argument_list|)
expr_stmt|;
block|}
name|dirs
operator|.
name|add
argument_list|(
name|deployDir
argument_list|)
expr_stmt|;
name|scanner
operator|.
name|setScanDirs
argument_list|(
name|dirs
argument_list|)
expr_stmt|;
name|scanner
operator|.
name|setScanInterval
argument_list|(
name|scanInterval
argument_list|)
expr_stmt|;
name|scanner
operator|.
name|addListener
argument_list|(
operator|new
name|Scanner
operator|.
name|BulkListener
argument_list|()
block|{
specifier|public
name|void
name|filesChanged
parameter_list|(
name|List
name|filenames
parameter_list|)
throws|throws
name|Exception
block|{
name|onFilesChanged
argument_list|(
name|filenames
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|log
argument_list|(
literal|"Starting to monitor the deploy directory: "
operator|+
name|deployDir
operator|+
literal|" every "
operator|+
name|scanInterval
operator|+
literal|" millis"
argument_list|)
expr_stmt|;
if|if
condition|(
name|configDir
operator|!=
literal|null
condition|)
block|{
name|log
argument_list|(
literal|"Config directory is at: "
operator|+
name|configDir
argument_list|)
expr_stmt|;
block|}
name|log
argument_list|(
literal|"Will generate bundles from expanded source directories to: "
operator|+
name|generateDir
argument_list|)
expr_stmt|;
name|scanner
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|scanner
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
specifier|public
name|BundleContext
name|getContext
parameter_list|()
block|{
return|return
name|activator
operator|.
name|getContext
argument_list|()
return|;
block|}
specifier|public
name|FileMonitorActivator
name|getActivator
parameter_list|()
block|{
return|return
name|activator
return|;
block|}
specifier|public
name|void
name|setActivator
parameter_list|(
name|FileMonitorActivator
name|activator
parameter_list|)
block|{
name|this
operator|.
name|activator
operator|=
name|activator
expr_stmt|;
block|}
specifier|public
name|File
name|getConfigDir
parameter_list|()
block|{
return|return
name|configDir
return|;
block|}
specifier|public
name|void
name|setConfigDir
parameter_list|(
name|File
name|configDir
parameter_list|)
block|{
name|this
operator|.
name|configDir
operator|=
name|configDir
expr_stmt|;
block|}
specifier|public
name|File
name|getDeployDir
parameter_list|()
block|{
return|return
name|deployDir
return|;
block|}
specifier|public
name|void
name|setDeployDir
parameter_list|(
name|File
name|deployDir
parameter_list|)
block|{
name|this
operator|.
name|deployDir
operator|=
name|deployDir
expr_stmt|;
block|}
specifier|public
name|File
name|getGenerateDir
parameter_list|()
block|{
return|return
name|generateDir
return|;
block|}
specifier|public
name|void
name|setGenerateDir
parameter_list|(
name|File
name|generateDir
parameter_list|)
block|{
name|this
operator|.
name|generateDir
operator|=
name|generateDir
expr_stmt|;
block|}
specifier|public
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
specifier|public
name|void
name|setProject
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
block|}
specifier|public
name|long
name|getScanInterval
parameter_list|()
block|{
return|return
name|scanInterval
return|;
block|}
specifier|public
name|void
name|setScanInterval
parameter_list|(
name|long
name|scanInterval
parameter_list|)
block|{
name|this
operator|.
name|scanInterval
operator|=
name|scanInterval
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
specifier|protected
name|void
name|onFilesChanged
parameter_list|(
name|List
name|filenames
parameter_list|)
block|{
name|changedBundles
operator|.
name|clear
argument_list|()
expr_stmt|;
name|bundlesToStart
operator|.
name|clear
argument_list|()
expr_stmt|;
name|bundlesToUpdate
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|File
argument_list|>
name|bundleJarsCreated
init|=
operator|new
name|HashSet
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|filename
range|:
name|filenames
control|)
block|{
name|String
name|name
init|=
name|filename
operator|.
name|toString
argument_list|()
decl_stmt|;
name|boolean
name|added
init|=
literal|true
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|name
argument_list|)
decl_stmt|;
try|try
block|{
name|debug
argument_list|(
literal|"File changed: "
operator|+
name|filename
operator|+
literal|" with type: "
operator|+
name|filename
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|=
name|transformArtifact
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|added
operator|=
literal|false
expr_stmt|;
name|String
name|transformedFile
init|=
name|artifactToBundle
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|transformedFile
operator|!=
literal|null
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|transformedFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// now lets iterate to find the parent directory
name|File
name|jardir
init|=
name|getExpandedBundleRootDirectory
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|jardir
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|bundleJarsCreated
operator|.
name|contains
argument_list|(
name|jardir
argument_list|)
condition|)
block|{
name|bundleJarsCreated
operator|.
name|add
argument_list|(
name|jardir
argument_list|)
expr_stmt|;
name|File
name|newBundle
init|=
name|createBundleJar
argument_list|(
name|jardir
argument_list|)
decl_stmt|;
name|deployBundle
argument_list|(
name|newBundle
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
operator|||
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".zip"
argument_list|)
operator|||
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".war"
argument_list|)
condition|)
block|{
if|if
condition|(
name|added
condition|)
block|{
name|deployBundle
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|undeployBundle
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".cfg"
argument_list|)
condition|)
block|{
if|if
condition|(
name|added
condition|)
block|{
name|updateConfiguration
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|deleteConfiguration
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"MANIFEST.MF"
argument_list|)
condition|)
block|{
name|File
name|parentFile
init|=
name|file
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentFile
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"META-INF"
argument_list|)
condition|)
block|{
name|File
name|bundleDir
init|=
name|parentFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|isValidBundleSourceDirectory
argument_list|(
name|bundleDir
argument_list|)
condition|)
block|{
name|undeployBundle
argument_list|(
name|bundleDir
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|warn
argument_list|(
literal|"Failed to process: "
operator|+
name|file
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|refreshPackagesAndStartOrUpdateBundles
argument_list|()
expr_stmt|;
block|}
specifier|private
name|File
name|transformArtifact
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceReference
index|[]
name|srvRefs
init|=
name|getContext
argument_list|()
operator|.
name|getAllServiceReferences
argument_list|(
name|DeploymentListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|srvRefs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|sr
range|:
name|srvRefs
control|)
block|{
try|try
block|{
name|DeploymentListener
name|deploymentListener
init|=
operator|(
name|DeploymentListener
operator|)
name|getContext
argument_list|()
operator|.
name|getService
argument_list|(
name|sr
argument_list|)
decl_stmt|;
if|if
condition|(
name|deploymentListener
operator|.
name|canHandle
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|File
name|transformedFile
init|=
name|deploymentListener
operator|.
name|handle
argument_list|(
name|file
argument_list|,
name|getGenerateDir
argument_list|()
argument_list|)
decl_stmt|;
name|artifactToBundle
operator|.
name|put
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|transformedFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|file
operator|=
name|transformedFile
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|getContext
argument_list|()
operator|.
name|ungetService
argument_list|(
name|sr
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|file
return|;
block|}
specifier|protected
name|void
name|deployBundle
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
throws|,
name|BundleException
block|{
name|log
argument_list|(
literal|"Deloying: "
operator|+
name|file
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|Bundle
name|bundle
init|=
name|getBundleForJarFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|!=
literal|null
condition|)
block|{
name|changedBundles
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|bundlesToUpdate
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bundle
operator|=
name|getContext
argument_list|()
operator|.
name|installBundle
argument_list|(
name|file
operator|.
name|getCanonicalFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|in
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isBundleFragment
argument_list|(
name|bundle
argument_list|)
condition|)
block|{
name|bundlesToStart
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|closeQuietly
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|undeployBundle
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|BundleException
throws|,
name|IOException
block|{
name|log
argument_list|(
literal|"Undeloying: "
operator|+
name|file
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|Bundle
name|bundle
init|=
name|getBundleForJarFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|==
literal|null
condition|)
block|{
name|warn
argument_list|(
literal|"Could not find Bundle for file: "
operator|+
name|file
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|changedBundles
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|stop
argument_list|()
expr_stmt|;
name|bundle
operator|.
name|uninstall
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|Bundle
name|getBundleForJarFile
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|absoluteFilePath
init|=
name|file
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|Bundle
name|bundles
index|[]
init|=
name|getContext
argument_list|()
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bundles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Bundle
name|bundle
init|=
name|bundles
index|[
name|i
index|]
decl_stmt|;
name|String
name|location
init|=
name|bundle
operator|.
name|getLocation
argument_list|()
decl_stmt|;
if|if
condition|(
name|location
operator|.
name|endsWith
argument_list|(
name|absoluteFilePath
argument_list|)
condition|)
block|{
return|return
name|bundle
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|updateConfiguration
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
name|ConfigurationAdmin
name|configurationAdmin
init|=
name|activator
operator|.
name|getConfigurationAdmin
argument_list|()
decl_stmt|;
if|if
condition|(
name|configurationAdmin
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|loggedConfigAdminWarning
condition|)
block|{
name|warn
argument_list|(
literal|"No ConfigurationAdmin so cannot deploy configurations"
argument_list|)
expr_stmt|;
name|loggedConfigAdminWarning
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|properties
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|closeQuietly
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|String
index|[]
name|pid
init|=
name|parsePid
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|Hashtable
name|hashtable
init|=
operator|new
name|Hashtable
argument_list|()
decl_stmt|;
name|hashtable
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
if|if
condition|(
name|pid
index|[
literal|1
index|]
operator|!=
literal|null
condition|)
block|{
name|hashtable
operator|.
name|put
argument_list|(
name|ALIAS_KEY
argument_list|,
name|pid
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
name|Configuration
name|config
init|=
name|getConfiguration
argument_list|(
name|pid
index|[
literal|0
index|]
argument_list|,
name|pid
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getBundleLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|config
operator|.
name|setBundleLocation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|config
operator|.
name|update
argument_list|(
name|hashtable
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|closeQuietly
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|deleteConfiguration
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
name|String
index|[]
name|pid
init|=
name|parsePid
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|Configuration
name|config
init|=
name|getConfiguration
argument_list|(
name|pid
index|[
literal|0
index|]
argument_list|,
name|pid
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|config
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|Configuration
name|getConfiguration
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|factoryPid
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
name|ConfigurationAdmin
name|configurationAdmin
init|=
name|activator
operator|.
name|getConfigurationAdmin
argument_list|()
decl_stmt|;
if|if
condition|(
name|factoryPid
operator|!=
literal|null
condition|)
block|{
name|Configuration
index|[]
name|configs
init|=
name|configurationAdmin
operator|.
name|listConfigurations
argument_list|(
literal|"(|("
operator|+
name|ALIAS_KEY
operator|+
literal|"="
operator|+
name|pid
operator|+
literal|")(.alias_factory_pid="
operator|+
name|factoryPid
operator|+
literal|"))"
argument_list|)
decl_stmt|;
if|if
condition|(
name|configs
operator|==
literal|null
operator|||
name|configs
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|configurationAdmin
operator|.
name|createFactoryConfiguration
argument_list|(
name|pid
argument_list|,
literal|null
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|configs
index|[
literal|0
index|]
return|;
block|}
block|}
else|else
block|{
return|return
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
specifier|protected
name|String
index|[]
name|parsePid
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|String
name|path
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|pid
init|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
decl_stmt|;
name|int
name|n
init|=
name|pid
operator|.
name|indexOf
argument_list|(
literal|'-'
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|String
name|factoryPid
init|=
name|pid
operator|.
name|substring
argument_list|(
name|n
operator|+
literal|1
argument_list|)
decl_stmt|;
name|pid
operator|=
name|pid
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{
name|pid
block|,
name|factoryPid
block|}
return|;
block|}
else|else
block|{
return|return
operator|new
name|String
index|[]
block|{
name|pid
block|,
literal|null
block|}
return|;
block|}
block|}
specifier|protected
name|PackageAdmin
name|getPackageAdmin
parameter_list|()
block|{
name|ServiceTracker
name|packageAdminTracker
init|=
name|activator
operator|.
name|getPackageAdminTracker
argument_list|()
decl_stmt|;
if|if
condition|(
name|packageAdminTracker
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|(
name|PackageAdmin
operator|)
name|packageAdminTracker
operator|.
name|waitForService
argument_list|(
literal|5000L
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|boolean
name|isBundleFragment
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|PackageAdmin
name|packageAdmin
init|=
name|getPackageAdmin
argument_list|()
decl_stmt|;
if|if
condition|(
name|packageAdmin
operator|!=
literal|null
condition|)
block|{
return|return
name|packageAdmin
operator|.
name|getBundleType
argument_list|(
name|bundle
argument_list|)
operator|==
name|PackageAdmin
operator|.
name|BUNDLE_TYPE_FRAGMENT
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|void
name|refreshPackagesAndStartOrUpdateBundles
parameter_list|()
block|{
name|PackageAdmin
name|packageAdmin
init|=
name|getPackageAdmin
argument_list|()
decl_stmt|;
if|if
condition|(
name|packageAdmin
operator|!=
literal|null
condition|)
block|{
name|Bundle
index|[]
name|bundles
init|=
operator|new
name|Bundle
index|[
name|changedBundles
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|changedBundles
operator|.
name|toArray
argument_list|(
name|bundles
argument_list|)
expr_stmt|;
name|packageAdmin
operator|.
name|refreshPackages
argument_list|(
name|bundles
argument_list|)
expr_stmt|;
block|}
name|changedBundles
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundlesToUpdate
control|)
block|{
try|try
block|{
name|bundle
operator|.
name|update
argument_list|()
expr_stmt|;
name|log
argument_list|(
literal|"Updated: "
operator|+
name|bundle
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BundleException
name|e
parameter_list|)
block|{
name|warn
argument_list|(
literal|"Failed to update bundle: "
operator|+
name|bundle
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundlesToStart
control|)
block|{
try|try
block|{
name|bundle
operator|.
name|start
argument_list|()
expr_stmt|;
name|log
argument_list|(
literal|"Started: "
operator|+
name|bundle
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BundleException
name|e
parameter_list|)
block|{
name|warn
argument_list|(
literal|"Failed to start bundle: "
operator|+
name|bundle
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|File
name|createBundleJar
parameter_list|(
name|File
name|dir
parameter_list|)
throws|throws
name|BundleException
throws|,
name|IOException
block|{
name|Jar
name|jar
init|=
operator|new
name|Jar
argument_list|()
decl_stmt|;
name|jar
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|generateDir
argument_list|,
name|dir
operator|.
name|getName
argument_list|()
operator|+
literal|".jar"
argument_list|)
decl_stmt|;
if|if
condition|(
name|destFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|undeployBundle
argument_list|(
name|destFile
argument_list|)
expr_stmt|;
name|destFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|log
argument_list|(
literal|"Creating jar:  "
operator|+
name|destFile
operator|+
literal|" from dir: "
operator|+
name|dir
argument_list|)
expr_stmt|;
name|jar
operator|.
name|setDestFile
argument_list|(
name|destFile
argument_list|)
expr_stmt|;
name|jar
operator|.
name|setManifest
argument_list|(
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"META-INF"
argument_list|)
argument_list|,
literal|"MANIFEST.MF"
argument_list|)
argument_list|)
expr_stmt|;
name|jar
operator|.
name|setBasedir
argument_list|(
name|dir
argument_list|)
expr_stmt|;
name|jar
operator|.
name|init
argument_list|()
expr_stmt|;
name|jar
operator|.
name|perform
argument_list|()
expr_stmt|;
return|return
name|destFile
return|;
block|}
comment|/**      * Returns the root directory of the expanded OSGi bundle if the file is part of an expanded OSGi bundle      * or null if it is not      */
specifier|protected
name|File
name|getExpandedBundleRootDirectory
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|parent
init|=
name|file
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|String
name|rootPath
init|=
name|deployDir
operator|.
name|getCanonicalPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|getCanonicalPath
argument_list|()
operator|.
name|equals
argument_list|(
name|rootPath
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|containsManifest
argument_list|(
name|file
argument_list|)
condition|)
block|{
return|return
name|file
return|;
block|}
block|}
if|if
condition|(
name|isValidBundleSourceDirectory
argument_list|(
name|parent
argument_list|)
condition|)
block|{
return|return
name|getExpandedBundleRootDirectory
argument_list|(
name|parent
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns true if the given directory is a valid child directory within the {@link #deployDir}      */
specifier|protected
name|boolean
name|isValidBundleSourceDirectory
parameter_list|(
name|File
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|dir
operator|!=
literal|null
condition|)
block|{
name|String
name|parentPath
init|=
name|dir
operator|.
name|getCanonicalPath
argument_list|()
decl_stmt|;
name|String
name|rootPath
init|=
name|deployDir
operator|.
name|getCanonicalPath
argument_list|()
decl_stmt|;
return|return
operator|!
name|parentPath
operator|.
name|equals
argument_list|(
name|rootPath
argument_list|)
operator|&&
name|parentPath
operator|.
name|startsWith
argument_list|(
name|rootPath
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Returns true if the given directory contains a valid manifest file      */
specifier|protected
name|boolean
name|containsManifest
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
name|File
name|metaInfDir
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"META-INF"
argument_list|)
decl_stmt|;
if|if
condition|(
name|metaInfDir
operator|.
name|exists
argument_list|()
operator|&&
name|metaInfDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
name|manifest
init|=
operator|new
name|File
argument_list|(
name|metaInfDir
argument_list|,
literal|"MANIFEST.MF"
argument_list|)
decl_stmt|;
return|return
name|manifest
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|manifest
operator|.
name|isDirectory
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|File
name|getFileValue
parameter_list|(
name|Dictionary
name|properties
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|Object
name|value
init|=
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|File
condition|)
block|{
return|return
operator|(
name|File
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Long
name|getLongValue
parameter_list|(
name|Dictionary
name|properties
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|Object
name|value
init|=
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Long
condition|)
block|{
return|return
operator|(
name|Long
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|closeQuietly
parameter_list|(
name|Closeable
name|in
parameter_list|)
block|{
try|try
block|{
name|in
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
name|warn
argument_list|(
literal|"Failed to close stream. "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|debug
parameter_list|(
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|logLevel
operator|<=
name|DEBUG
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"DEBUG: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|log
parameter_list|(
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|logLevel
operator|<=
name|INFO
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"INFO: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|warn
parameter_list|(
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|logLevel
operator|<=
name|WARN
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"WARN: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|warn
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|logLevel
operator|<=
name|WARN
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"WARN: "
operator|+
name|message
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

