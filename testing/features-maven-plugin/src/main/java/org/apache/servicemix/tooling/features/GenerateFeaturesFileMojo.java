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
name|tooling
operator|.
name|features
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
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Dependency
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

begin_comment
comment|/**  * Generates the features XML file  *  * @version $Revision: 1.1 $  * @goal generate-features-file  * @phase generate-resources  * @requiresDependencyResolution runtime  * @description Generates the features XML file  */
end_comment

begin_class
specifier|public
class|class
name|GenerateFeaturesFileMojo
extends|extends
name|MojoSupport
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|SEPARATOR
init|=
literal|"/"
decl_stmt|;
comment|/**      * The file to generate      *      * @parameter default-value="${project.build.directory}/classes/feature.xml"      */
specifier|private
name|File
name|outputFile
decl_stmt|;
comment|/**      * The name of the feature, which defaults to the artifact ID if its not specified      *      * @parameter default-value="${project.artifactId}"      */
specifier|private
name|String
name|featureName
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
name|OutputStream
name|out
init|=
literal|null
decl_stmt|;
try|try
block|{
name|outputFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|out
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|outputFile
argument_list|)
expr_stmt|;
name|PrintStream
name|printer
init|=
operator|new
name|PrintStream
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|populateProperties
argument_list|(
name|printer
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Created: "
operator|+
name|outputFile
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
literal|"Unable to create dependencies file: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|out
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
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Failed to close: "
operator|+
name|outputFile
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
block|}
specifier|protected
name|void
name|populateProperties
parameter_list|(
name|PrintStream
name|out
parameter_list|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<features>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<feature name='"
operator|+
name|featureName
operator|+
literal|"'>"
argument_list|)
expr_stmt|;
name|writeBundle
argument_list|(
name|out
argument_list|,
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|Iterator
name|iterator
init|=
name|project
operator|.
name|getDependencies
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Dependency
name|dependency
init|=
operator|(
name|Dependency
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|isValidDependency
argument_list|(
name|dependency
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"  "
argument_list|)
expr_stmt|;
name|writeBundle
argument_list|(
name|out
argument_list|,
name|dependency
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dependency
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|dependency
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|println
argument_list|(
literal|"</feature>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</features>"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isValidDependency
parameter_list|(
name|Dependency
name|dependency
parameter_list|)
block|{
comment|// TODO filter out only compile time dependencies which are OSGi bundles?
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|writeBundle
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"<bundle>mvn:"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"</bundle>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

