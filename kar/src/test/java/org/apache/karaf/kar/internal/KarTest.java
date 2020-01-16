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
name|kar
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

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
name|net
operator|.
name|URI
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
name|KarTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|karExtractTest
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
name|Kar
name|kar
init|=
operator|new
name|Kar
argument_list|(
operator|new
name|URI
argument_list|(
literal|"https://repo1.maven.org/maven2/org/apache/karaf/features/framework/4.2.2/framework-4.2.2.kar"
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test/framework-repo"
argument_list|)
decl_stmt|;
name|repoDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|resourcesDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test/framework-resources"
argument_list|)
decl_stmt|;
name|resourcesDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|kar
operator|.
name|extract
argument_list|(
name|repoDir
argument_list|,
name|resourcesDir
argument_list|)
expr_stmt|;
name|File
index|[]
name|repoDirFiles
init|=
name|repoDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repoDirFiles
operator|.
name|length
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"org"
argument_list|,
name|repoDirFiles
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|File
index|[]
name|resourceDirFiles
init|=
name|resourcesDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|resourceDirFiles
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|badKarExtractTest
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
literal|"../../../../foo.bar"
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
name|Kar
name|kar
init|=
operator|new
name|Kar
argument_list|(
operator|new
name|URI
argument_list|(
literal|"file:target/test/bad.kar"
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test/repo"
argument_list|)
decl_stmt|;
name|repoDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|resourceDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test/resources"
argument_list|)
decl_stmt|;
name|resourceDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|kar
operator|.
name|extract
argument_list|(
name|repoDir
argument_list|,
name|resourceDir
argument_list|)
expr_stmt|;
name|File
index|[]
name|repoDirFiles
init|=
name|repoDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repoDirFiles
operator|.
name|length
argument_list|)
expr_stmt|;
name|File
index|[]
name|resourceDirFiles
init|=
name|resourceDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resourceDirFiles
operator|.
name|length
argument_list|)
expr_stmt|;
name|badKarFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|badEncodedKarExtractTest
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
literal|"badencoded.kar"
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
comment|// Use the encoded form of ".." here
name|ZipEntry
name|entry
init|=
operator|new
name|ZipEntry
argument_list|(
literal|"%2e%2e/%2e%2e/%2e%2e/%2e%2e/foo.bar"
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
name|Kar
name|kar
init|=
operator|new
name|Kar
argument_list|(
operator|new
name|URI
argument_list|(
literal|"file:target/test/badencoded.kar"
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test/repo"
argument_list|)
decl_stmt|;
name|repoDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|resourceDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test/resources"
argument_list|)
decl_stmt|;
name|resourceDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|kar
operator|.
name|extract
argument_list|(
name|repoDir
argument_list|,
name|resourceDir
argument_list|)
expr_stmt|;
name|File
index|[]
name|repoDirFiles
init|=
name|repoDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repoDirFiles
operator|.
name|length
argument_list|)
expr_stmt|;
name|File
index|[]
name|resourceDirFiles
init|=
name|resourceDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resourceDirFiles
operator|.
name|length
argument_list|)
expr_stmt|;
name|badKarFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

