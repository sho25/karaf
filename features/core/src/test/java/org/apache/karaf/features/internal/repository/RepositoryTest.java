begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|internal
operator|.
name|repository
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|util
operator|.
name|zip
operator|.
name|GZIPOutputStream
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
name|StreamUtils
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
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Resource
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
name|assertEquals
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
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|namespace
operator|.
name|BundleNamespace
operator|.
name|BUNDLE_NAMESPACE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|namespace
operator|.
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|namespace
operator|.
name|PackageNamespace
operator|.
name|PACKAGE_NAMESPACE
import|;
end_import

begin_class
specifier|public
class|class
name|RepositoryTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testXml
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"repo.xml"
argument_list|)
decl_stmt|;
name|XmlRepository
name|repo
init|=
operator|new
name|XmlRepository
argument_list|(
name|url
operator|.
name|toExternalForm
argument_list|()
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJson
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"repo.json"
argument_list|)
decl_stmt|;
name|JsonRepository
name|repo
init|=
operator|new
name|JsonRepository
argument_list|(
name|url
operator|.
name|toExternalForm
argument_list|()
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXmlGzip
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"repo.xml"
argument_list|)
decl_stmt|;
name|url
operator|=
name|gzip
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|XmlRepository
name|repo
init|=
operator|new
name|XmlRepository
argument_list|(
name|url
operator|.
name|toExternalForm
argument_list|()
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJsonGzip
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"repo.json"
argument_list|)
decl_stmt|;
name|url
operator|=
name|gzip
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|JsonRepository
name|repo
init|=
operator|new
name|JsonRepository
argument_list|(
name|url
operator|.
name|toExternalForm
argument_list|()
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verify
parameter_list|(
name|BaseRepository
name|repo
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|repo
operator|.
name|getResources
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repo
operator|.
name|getResources
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Resource
name|resource
init|=
name|repo
operator|.
name|getResources
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getCapabilities
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getCapabilities
argument_list|(
name|BUNDLE_NAMESPACE
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getCapabilities
argument_list|(
name|PACKAGE_NAMESPACE
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getRequirements
argument_list|(
name|PACKAGE_NAMESPACE
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|URL
name|gzip
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|temp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"repo"
argument_list|,
literal|".tmp"
argument_list|)
decl_stmt|;
try|try
init|(
name|OutputStream
name|os
init|=
operator|new
name|GZIPOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|temp
argument_list|)
argument_list|)
init|;
name|InputStream
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|StreamUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
return|return
name|temp
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
return|;
block|}
block|}
end_class

end_unit

