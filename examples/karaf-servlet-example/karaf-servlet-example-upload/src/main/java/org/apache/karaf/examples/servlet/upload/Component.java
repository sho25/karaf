begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|examples
operator|.
name|servlet
operator|.
name|upload
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|annotations
operator|.
name|Activate
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
name|component
operator|.
name|annotations
operator|.
name|Deactivate
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
name|component
operator|.
name|annotations
operator|.
name|Reference
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
name|http
operator|.
name|HttpService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_class
annotation|@
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|annotations
operator|.
name|Component
specifier|public
class|class
name|Component
block|{
annotation|@
name|Reference
specifier|protected
name|HttpService
name|httpService
decl_stmt|;
annotation|@
name|Activate
specifier|public
name|void
name|activate
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|tmpDir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
specifier|final
name|Path
name|uploadPath
init|=
name|Paths
operator|.
name|get
argument_list|(
name|tmpDir
argument_list|,
literal|"karaf"
argument_list|,
literal|"upload"
argument_list|)
decl_stmt|;
name|uploadPath
operator|.
name|toFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|httpService
operator|.
name|registerServlet
argument_list|(
literal|"/upload-example"
argument_list|,
operator|new
name|UploadServlet
argument_list|(
name|uploadPath
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deactivate
specifier|public
name|void
name|deactivate
parameter_list|()
throws|throws
name|Exception
block|{
name|httpService
operator|.
name|unregister
argument_list|(
literal|"/upload-example"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

