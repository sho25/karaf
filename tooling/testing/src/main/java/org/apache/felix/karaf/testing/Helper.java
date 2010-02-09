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
name|felix
operator|.
name|karaf
operator|.
name|testing
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
name|Enumeration
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
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|options
operator|.
name|MavenArtifactProvisionOption
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|options
operator|.
name|SystemPropertyOption
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|bootClasspathLibrary
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|bootDelegationPackages
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|frameworkStartLevel
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|maven
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|wrappedBundle
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|rawPaxRunnerOption
import|;
end_import

begin_comment
comment|/**  * Helper class for setting up a pax-exam test environment for karaf.  *  * A simple configuration for pax-exam can be create using the following  * code:  *<pre>  *   @Configuration  *   public static Option[] configuration() throws Exception{  *       return combine(  *           // Default karaf environment  *           Helper.getDefaultOptions(),  *           // Test on both equinox and felix  *           equinox(), felix()  *       );  *   }  *</pre>  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Helper
block|{
specifier|private
name|Helper
parameter_list|()
block|{     }
comment|/**      *  Create an provisioning option for the specified maven artifact      * (groupId and artifactId), using the version found in the list      * of dependencies of this maven project.      *      * @param groupId the groupId of the maven bundle      * @param artifactId the artifactId of the maven bundle      * @return the provisioning option for the given bundle      */
specifier|public
specifier|static
name|MavenArtifactProvisionOption
name|mavenBundle
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
block|{
return|return
name|CoreOptions
operator|.
name|mavenBundle
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
return|;
block|}
comment|/**      * Return a map of system properties for karaf.      * The default karaf home directory is "target/karaf.home".      *      * @return a list of system properties for karaf      */
specifier|public
specifier|static
name|Properties
name|getDefaultSystemOptions
parameter_list|()
block|{
return|return
name|getDefaultSystemOptions
argument_list|(
literal|"target/karaf.home"
argument_list|)
return|;
block|}
comment|/**      * Return a map of system properties for karaf,      * using the specified folder for the karaf home directory.      *      * @param karafHome the karaf home directory      * @return a list of system properties for karaf      */
specifier|public
specifier|static
name|Properties
name|getDefaultSystemOptions
parameter_list|(
name|String
name|karafHome
parameter_list|)
block|{
name|Properties
name|sysProps
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|sysProps
operator|.
name|setProperty
argument_list|(
literal|"karaf.name"
argument_list|,
literal|"root"
argument_list|)
expr_stmt|;
name|sysProps
operator|.
name|setProperty
argument_list|(
literal|"karaf.home"
argument_list|,
name|karafHome
argument_list|)
expr_stmt|;
name|sysProps
operator|.
name|setProperty
argument_list|(
literal|"karaf.base"
argument_list|,
name|karafHome
argument_list|)
expr_stmt|;
name|sysProps
operator|.
name|setProperty
argument_list|(
literal|"karaf.startLocalConsole"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|sysProps
operator|.
name|setProperty
argument_list|(
literal|"karaf.startRemoteShell"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|sysProps
operator|.
name|setProperty
argument_list|(
literal|"org.osgi.framework.startlevel.beginning"
argument_list|,
literal|"100"
argument_list|)
expr_stmt|;
return|return
name|sysProps
return|;
block|}
comment|/**      * Return an array of pax-exam options to correctly configure the osgi      * framework for karaf.      *      * @param sysOptions test-specific system property options      * @return default pax-exam options for karaf osgi framework      */
specifier|public
specifier|static
name|Option
index|[]
name|getDefaultConfigOptions
parameter_list|(
name|SystemPropertyOption
modifier|...
name|sysOptions
parameter_list|)
block|{
return|return
name|getDefaultConfigOptions
argument_list|(
name|getDefaultSystemOptions
argument_list|()
argument_list|,
name|getResource
argument_list|(
literal|"/org/apache/felix/karaf/testing/config.properties"
argument_list|)
argument_list|,
name|sysOptions
argument_list|)
return|;
block|}
comment|/**      * Return an array of pax-exam options to configure the osgi      * framework for karaf, given the system properties and the      * location of the osgi framework properties file.      *      * @param sysProps karaf system properties      * @param configProperties the URL to load the osgi framework properties from      * @param sysOptions test-specific system property options      * @return pax-exam options for karaf osgi framework      */
specifier|public
specifier|static
name|Option
index|[]
name|getDefaultConfigOptions
parameter_list|(
name|Properties
name|sysProps
parameter_list|,
name|URL
name|configProperties
parameter_list|,
name|SystemPropertyOption
modifier|...
name|sysOptions
parameter_list|)
block|{
comment|// Load props
name|Properties
name|configProps
init|=
name|loadProperties
argument_list|(
name|configProperties
argument_list|)
decl_stmt|;
comment|// Set system props
for|for
control|(
name|Enumeration
name|e
init|=
name|sysProps
operator|.
name|propertyNames
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|configProps
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|sysProps
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Perform variable substitution for system properties.
for|for
control|(
name|Enumeration
name|e
init|=
name|configProps
operator|.
name|propertyNames
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|configProps
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|substVars
argument_list|(
name|configProps
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
argument_list|,
name|name
argument_list|,
literal|null
argument_list|,
name|configProps
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Transform system properties to VM options
name|List
argument_list|<
name|Option
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|Option
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|vmOptions
init|=
literal|"-Dorg.ops4j.pax.exam.rbc.rmi.port=1099"
decl_stmt|;
for|for
control|(
name|Enumeration
name|e
init|=
name|configProps
operator|.
name|propertyNames
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|configProps
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|value
operator|=
name|align
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"org.osgi.framework.system.packages"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|String
name|extra
init|=
name|align
argument_list|(
name|configProps
operator|.
name|getProperty
argument_list|(
literal|"org.osgi.framework.system.packages.extra"
argument_list|)
argument_list|)
decl_stmt|;
name|vmOptions
operator|=
name|vmOptions
operator|+
literal|" -D"
operator|+
name|name
operator|+
literal|"="
operator|+
name|value
operator|+
literal|","
operator|+
name|extra
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"org.osgi.framework.bootdelegation"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|bootDelegationPackages
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|vmOptions
operator|=
name|vmOptions
operator|+
literal|" -D"
operator|+
name|name
operator|+
literal|"="
operator|+
name|value
expr_stmt|;
block|}
block|}
comment|// add test-specific system properties
if|if
condition|(
name|sysOptions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|SystemPropertyOption
name|sysOption
range|:
name|sysOptions
control|)
block|{
name|vmOptions
operator|=
name|vmOptions
operator|+
literal|" -D"
operator|+
name|sysOption
operator|.
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|sysOption
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|configProps
operator|.
name|getProperty
argument_list|(
literal|"org.osgi.framework.startlevel.beginning"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|frameworkStartLevel
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|configProps
operator|.
name|getProperty
argument_list|(
literal|"org.osgi.framework.startlevel.beginning"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|options
operator|.
name|add
argument_list|(
name|rawPaxRunnerOption
argument_list|(
literal|"--vmOptions"
argument_list|,
name|vmOptions
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|options
operator|.
name|toArray
argument_list|(
operator|new
name|Option
index|[
name|options
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Return an array of pax-exam options for the provisioning of karaf system bundles.      *      * @return an array of pax-exam options for provisioning karaf system bundles      */
specifier|public
specifier|static
name|Option
index|[]
name|getDefaultProvisioningOptions
parameter_list|()
block|{
return|return
name|getDefaultProvisioningOptions
argument_list|(
name|getDefaultSystemOptions
argument_list|()
argument_list|,
name|getResource
argument_list|(
literal|"/org/apache/felix/karaf/testing/startup.properties"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Return an array of pax-exam options for the provisioning of karaf system bundles,      * given the karaf system properties and the location of the startup bundles config file.      *      * @param sysProps karaf system properties      * @param startupProperties the URL to load the system bundles from      * @return an array of pax-exam options for provisioning karaf system bundles      */
specifier|public
specifier|static
name|Option
index|[]
name|getDefaultProvisioningOptions
parameter_list|(
name|Properties
name|sysProps
parameter_list|,
name|URL
name|startupProperties
parameter_list|)
block|{
name|Properties
name|startupProps
init|=
name|loadProperties
argument_list|(
name|startupProperties
argument_list|)
decl_stmt|;
comment|// Perform variable substitution for system properties.
for|for
control|(
name|Enumeration
name|e
init|=
name|startupProps
operator|.
name|propertyNames
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|startupProps
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|substVars
argument_list|(
name|startupProps
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
argument_list|,
name|name
argument_list|,
literal|null
argument_list|,
name|sysProps
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Transform to sys props options
name|List
argument_list|<
name|Option
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|Option
argument_list|>
argument_list|()
decl_stmt|;
name|options
operator|.
name|add
argument_list|(
name|bootClasspathLibrary
argument_list|(
name|mavenBundle
argument_list|(
literal|"org.apache.felix.karaf.jaas"
argument_list|,
literal|"org.apache.felix.karaf.jaas.boot"
argument_list|)
argument_list|)
operator|.
name|afterFramework
argument_list|()
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|bootClasspathLibrary
argument_list|(
name|mavenBundle
argument_list|(
literal|"org.apache.felix.karaf"
argument_list|,
literal|"org.apache.felix.karaf.main"
argument_list|)
argument_list|)
operator|.
name|afterFramework
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Enumeration
name|e
init|=
name|startupProps
operator|.
name|propertyNames
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|startupProps
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|MavenArtifactProvisionOption
name|opt
init|=
name|convertToMaven
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|opt
operator|.
name|getURL
argument_list|()
operator|.
name|contains
argument_list|(
literal|"org.apache.felix.karaf.features"
argument_list|)
condition|)
block|{
name|opt
operator|.
name|noStart
argument_list|()
expr_stmt|;
block|}
name|opt
operator|.
name|startLevel
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|opt
argument_list|)
expr_stmt|;
block|}
name|options
operator|.
name|add
argument_list|(
name|mavenBundle
argument_list|(
literal|"org.apache.felix.karaf.tooling"
argument_list|,
literal|"org.apache.felix.karaf.tooling.testing"
argument_list|)
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|wrappedBundle
argument_list|(
name|maven
argument_list|(
literal|"org.ops4j.pax.exam"
argument_list|,
literal|"pax-exam-container-default"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// We need to add pax-exam-junit here when running with the ibm
comment|// jdk to avoid the following exception during the test run:
comment|// ClassNotFoundException: org.ops4j.pax.exam.junit.Configuration
if|if
condition|(
literal|"IBM Corporation"
operator|.
name|equals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.vendor"
argument_list|)
argument_list|)
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|wrappedBundle
argument_list|(
name|maven
argument_list|(
literal|"org.ops4j.pax.exam"
argument_list|,
literal|"pax-exam-junit"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|options
operator|.
name|toArray
argument_list|(
operator|new
name|Option
index|[
name|options
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Return an array of options for setting up a pax-exam test environment for karaf.      *      * @return an array of pax-exam options      */
specifier|public
specifier|static
name|Option
index|[]
name|getDefaultOptions
parameter_list|()
block|{
return|return
name|getDefaultOptions
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * Return an array of options for setting up a pax-exam test environment for karaf.      *      * @param sysOptions test-specific system property options      * @return an array of pax-exam options      */
specifier|public
specifier|static
name|Option
index|[]
name|getDefaultOptions
parameter_list|(
name|SystemPropertyOption
modifier|...
name|sysOptions
parameter_list|)
block|{
return|return
name|combine
argument_list|(
name|getDefaultConfigOptions
argument_list|(
name|sysOptions
argument_list|)
argument_list|,
name|getDefaultProvisioningOptions
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Retrieve the pax-exam option for provisioning the given maven bundle.      *      * @param options the list of pax-exam options      * @param groupId the maven group id      * @param artifactId the maven artifact id      * @return the pax-exam provisioning option for the bundle or<code>null</code> if not found      */
specifier|public
specifier|static
name|MavenArtifactProvisionOption
name|findMaven
parameter_list|(
name|Option
index|[]
name|options
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
block|{
for|for
control|(
name|Option
name|option
range|:
name|options
control|)
block|{
if|if
condition|(
name|option
operator|instanceof
name|MavenArtifactProvisionOption
condition|)
block|{
name|MavenArtifactProvisionOption
name|mvn
init|=
operator|(
name|MavenArtifactProvisionOption
operator|)
name|option
decl_stmt|;
if|if
condition|(
name|mvn
operator|.
name|getURL
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"mvn:"
operator|+
name|groupId
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"/"
argument_list|)
condition|)
block|{
return|return
name|mvn
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|Properties
name|loadProperties
parameter_list|(
name|URL
name|location
parameter_list|)
block|{
try|try
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|location
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|props
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to load properties from "
operator|+
name|location
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|URL
name|getResource
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|url
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|Helper
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to find resource "
operator|+
name|location
argument_list|)
throw|;
block|}
return|return
name|url
return|;
block|}
specifier|private
specifier|static
specifier|final
name|String
name|DELIM_START
init|=
literal|"${"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DELIM_STOP
init|=
literal|"}"
decl_stmt|;
comment|/**      *<p>      * This method performs property variable substitution on the      * specified value. If the specified value contains the syntax      *<tt>${&lt;prop-name&gt;}</tt>, where<tt>&lt;prop-name&gt;</tt>      * refers to either a configuration property or a system property,      * then the corresponding property value is substituted for the variable      * placeholder. Multiple variable placeholders may exist in the      * specified value as well as nested variable placeholders, which      * are substituted from inner most to outer most. Configuration      * properties override system properties.      *</p>      *      * @param val         The string on which to perform property substitution.      * @param currentKey  The key of the property being evaluated used to      *                    detect cycles.      * @param cycleMap    Map of variable references used to detect nested cycles.      * @param configProps Set of configuration properties.      * @return The value of the specified string after system property substitution.      * @throws IllegalArgumentException If there was a syntax error in the      *                                  property placeholder syntax or a recursive variable reference.      */
specifier|private
specifier|static
name|String
name|substVars
parameter_list|(
name|String
name|val
parameter_list|,
name|String
name|currentKey
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cycleMap
parameter_list|,
name|Properties
name|configProps
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// If there is currently no cycle map, then create
comment|// one for detecting cycles for this invocation.
if|if
condition|(
name|cycleMap
operator|==
literal|null
condition|)
block|{
name|cycleMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|// Put the current key in the cycle map.
name|cycleMap
operator|.
name|put
argument_list|(
name|currentKey
argument_list|,
name|currentKey
argument_list|)
expr_stmt|;
comment|// Assume we have a value that is something like:
comment|// "leading ${foo.${bar}} middle ${baz} trailing"
comment|// Find the first ending '}' variable delimiter, which
comment|// will correspond to the first deepest nested variable
comment|// placeholder.
name|int
name|stopDelim
init|=
name|val
operator|.
name|indexOf
argument_list|(
name|DELIM_STOP
argument_list|)
decl_stmt|;
comment|// Find the matching starting "${" variable delimiter
comment|// by looping until we find a start delimiter that is
comment|// greater than the stop delimiter we have found.
name|int
name|startDelim
init|=
name|val
operator|.
name|indexOf
argument_list|(
name|DELIM_START
argument_list|)
decl_stmt|;
while|while
condition|(
name|stopDelim
operator|>=
literal|0
condition|)
block|{
name|int
name|idx
init|=
name|val
operator|.
name|indexOf
argument_list|(
name|DELIM_START
argument_list|,
name|startDelim
operator|+
name|DELIM_START
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|idx
operator|<
literal|0
operator|)
operator|||
operator|(
name|idx
operator|>
name|stopDelim
operator|)
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|idx
operator|<
name|stopDelim
condition|)
block|{
name|startDelim
operator|=
name|idx
expr_stmt|;
block|}
block|}
comment|// If we do not have a start or stop delimiter, then just
comment|// return the existing value.
if|if
condition|(
operator|(
name|startDelim
operator|<
literal|0
operator|)
operator|&&
operator|(
name|stopDelim
operator|<
literal|0
operator|)
condition|)
block|{
return|return
name|val
return|;
block|}
comment|// At this point, we found a stop delimiter without a start,
comment|// so throw an exception.
elseif|else
if|if
condition|(
operator|(
operator|(
name|startDelim
operator|<
literal|0
operator|)
operator|||
operator|(
name|startDelim
operator|>
name|stopDelim
operator|)
operator|)
operator|&&
operator|(
name|stopDelim
operator|>=
literal|0
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"stop delimiter with no start delimiter: "
operator|+
name|val
argument_list|)
throw|;
block|}
comment|// At this point, we have found a variable placeholder so
comment|// we must perform a variable substitution on it.
comment|// Using the start and stop delimiter indices, extract
comment|// the first, deepest nested variable placeholder.
name|String
name|variable
init|=
name|val
operator|.
name|substring
argument_list|(
name|startDelim
operator|+
name|DELIM_START
operator|.
name|length
argument_list|()
argument_list|,
name|stopDelim
argument_list|)
decl_stmt|;
comment|// Verify that this is not a recursive variable reference.
if|if
condition|(
name|cycleMap
operator|.
name|get
argument_list|(
name|variable
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"recursive variable reference: "
operator|+
name|variable
argument_list|)
throw|;
block|}
comment|// Get the value of the deepest nested variable placeholder.
comment|// Try to configuration properties first.
name|String
name|substValue
init|=
operator|(
name|configProps
operator|!=
literal|null
operator|)
condition|?
name|configProps
operator|.
name|getProperty
argument_list|(
name|variable
argument_list|,
literal|null
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|substValue
operator|==
literal|null
condition|)
block|{
comment|// Ignore unknown property values.
name|substValue
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|variable
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
comment|// Remove the found variable from the cycle map, since
comment|// it may appear more than once in the value and we don't
comment|// want such situations to appear as a recursive reference.
name|cycleMap
operator|.
name|remove
argument_list|(
name|variable
argument_list|)
expr_stmt|;
comment|// Append the leading characters, the substituted value of
comment|// the variable, and the trailing characters to get the new
comment|// value.
name|val
operator|=
name|val
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|startDelim
argument_list|)
operator|+
name|substValue
operator|+
name|val
operator|.
name|substring
argument_list|(
name|stopDelim
operator|+
name|DELIM_STOP
operator|.
name|length
argument_list|()
argument_list|,
name|val
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now perform substitution again, since there could still
comment|// be substitutions to make.
name|val
operator|=
name|substVars
argument_list|(
name|val
argument_list|,
name|currentKey
argument_list|,
name|cycleMap
argument_list|,
name|configProps
argument_list|)
expr_stmt|;
comment|// Return the value.
return|return
name|val
return|;
block|}
specifier|private
specifier|static
name|MavenArtifactProvisionOption
name|convertToMaven
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|String
index|[]
name|p
init|=
name|location
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|length
operator|>=
literal|4
operator|&&
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|1
index|]
operator|.
name|startsWith
argument_list|(
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|3
index|]
operator|+
literal|"-"
operator|+
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|2
index|]
argument_list|)
condition|)
block|{
name|MavenArtifactProvisionOption
name|opt
init|=
operator|new
name|MavenArtifactProvisionOption
argument_list|()
decl_stmt|;
name|int
name|artifactIdVersionLength
init|=
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|3
index|]
operator|.
name|length
argument_list|()
operator|+
literal|1
operator|+
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|2
index|]
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// (artifactId + "-" + version).length
if|if
condition|(
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|1
index|]
operator|.
name|charAt
argument_list|(
name|artifactIdVersionLength
argument_list|)
operator|==
literal|'-'
condition|)
block|{
name|opt
operator|.
name|classifier
argument_list|(
operator|(
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|1
index|]
operator|.
name|substring
argument_list|(
name|artifactIdVersionLength
operator|+
literal|1
argument_list|,
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|1
index|]
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
operator|)
argument_list|)
expr_stmt|;
block|}
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|p
operator|.
name|length
operator|-
literal|3
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|j
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|p
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
name|opt
operator|.
name|groupId
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|opt
operator|.
name|artifactId
argument_list|(
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|3
index|]
argument_list|)
expr_stmt|;
name|opt
operator|.
name|version
argument_list|(
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|2
index|]
argument_list|)
expr_stmt|;
name|opt
operator|.
name|type
argument_list|(
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|1
index|]
operator|.
name|substring
argument_list|(
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|1
index|]
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|opt
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to extract maven information from "
operator|+
name|location
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|String
name|align
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|replaceAll
argument_list|(
literal|"\r"
argument_list|,
literal|""
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|""
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
else|:
literal|""
return|;
block|}
block|}
end_class

end_unit

