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
name|obr
operator|.
name|command
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
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
name|FileOutputStream
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
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipOutputStream
import|;
end_import

begin_class
specifier|public
class|class
name|FileUtilTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|goodExtractTest
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|base
init|=
operator|new
name|File
argument_list|(
literal|"target/test"
argument_list|)
decl_stmt|;
name|base
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|goodKarFile
init|=
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"good.kar"
argument_list|)
decl_stmt|;
name|ZipOutputStream
name|zos
init|=
operator|new
name|ZipOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|goodKarFile
argument_list|)
argument_list|)
decl_stmt|;
name|ZipEntry
name|entry
init|=
operator|new
name|ZipEntry
argument_list|(
literal|"foo.bar"
argument_list|)
decl_stmt|;
name|zos
operator|.
name|putNextEntry
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|byte
index|[]
name|data
init|=
literal|"Test Data"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|zos
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|zos
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|zos
operator|.
name|close
argument_list|()
expr_stmt|;
name|JarInputStream
name|jis
init|=
operator|new
name|JarInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|goodKarFile
argument_list|)
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|unjar
argument_list|(
name|jis
argument_list|,
name|base
argument_list|)
expr_stmt|;
name|goodKarFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|badExtractTest
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|base
init|=
operator|new
name|File
argument_list|(
literal|"target/test"
argument_list|)
decl_stmt|;
name|base
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|badKarFile
init|=
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"bad.kar"
argument_list|)
decl_stmt|;
name|ZipOutputStream
name|zos
init|=
operator|new
name|ZipOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|badKarFile
argument_list|)
argument_list|)
decl_stmt|;
name|ZipEntry
name|entry
init|=
operator|new
name|ZipEntry
argument_list|(
literal|"../../../../../../../../../tmp/foo.bar"
argument_list|)
decl_stmt|;
name|zos
operator|.
name|putNextEntry
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|byte
index|[]
name|data
init|=
literal|"Test Data"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|zos
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|zos
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|zos
operator|.
name|close
argument_list|()
expr_stmt|;
name|JarInputStream
name|jis
init|=
operator|new
name|JarInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|badKarFile
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|FileUtil
operator|.
name|unjar
argument_list|(
name|jis
argument_list|,
name|base
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on extracting a jar with relative file paths"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expected
block|}
name|badKarFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
