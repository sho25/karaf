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
name|deployer
operator|.
name|blueprint
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
name|FileInputStream
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
name|OutputStream
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
name|jar
operator|.
name|JarInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|BlueprintDeploymentListenerTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|BLUEPRINT_ENTRY
init|=
literal|"OSGI-INF/blueprint/"
decl_stmt|;
specifier|public
name|void
name|testPackagesExtraction
parameter_list|()
throws|throws
name|Exception
block|{
name|BlueprintDeploymentListener
name|l
init|=
operator|new
name|BlueprintDeploymentListener
argument_list|()
decl_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|pkgs
init|=
name|BlueprintTransformer
operator|.
name|analyze
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|BlueprintTransformer
operator|.
name|parse
argument_list|(
name|f
operator|.
name|toURL
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pkgs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pkgs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|pkgs
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.aries.blueprint.sample"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCustomManifest
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|f
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"smx"
argument_list|,
literal|".jar"
argument_list|)
decl_stmt|;
try|try
block|{
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|BlueprintTransformer
operator|.
name|transform
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test.xml"
argument_list|)
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|JarInputStream
name|jar
init|=
operator|new
name|JarInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|jar
operator|.
name|getManifest
argument_list|()
operator|.
name|write
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
comment|// KARAF-1617
specifier|public
name|void
name|testAppendDescriptorFileExtension
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|expectedFileName
init|=
literal|"test-fileextension"
decl_stmt|;
name|File
name|f
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"smx"
argument_list|,
literal|".jar"
argument_list|)
decl_stmt|;
try|try
block|{
name|ZipEntry
name|zipEntry
init|=
literal|null
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|BlueprintTransformer
operator|.
name|transform
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|expectedFileName
argument_list|)
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|JarInputStream
name|jar
init|=
operator|new
name|JarInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|zipEntry
operator|=
name|jar
operator|.
name|getNextEntry
argument_list|()
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|zipEntry
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|zipEntry
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
name|zipEntry
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
name|expectedFileName
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"blueprint service descriptor JAR file entry"
argument_list|,
name|zipEntry
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BLUEPRINT_ENTRY
operator|+
name|expectedFileName
operator|+
literal|".xml"
argument_list|,
name|zipEntry
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testAppendNotTwiceDescriptorFileExtension
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|expectedFileName
init|=
literal|"test.xml"
decl_stmt|;
name|File
name|f
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"smx"
argument_list|,
literal|".jar"
argument_list|)
decl_stmt|;
try|try
block|{
name|ZipEntry
name|zipEntry
init|=
literal|null
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|BlueprintTransformer
operator|.
name|transform
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|expectedFileName
argument_list|)
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|JarInputStream
name|jar
init|=
operator|new
name|JarInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|zipEntry
operator|=
name|jar
operator|.
name|getNextEntry
argument_list|()
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|zipEntry
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|zipEntry
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
name|zipEntry
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
name|expectedFileName
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"blueprint service descriptor JAR file entry"
argument_list|,
name|zipEntry
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BLUEPRINT_ENTRY
operator|+
name|expectedFileName
argument_list|,
name|zipEntry
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

