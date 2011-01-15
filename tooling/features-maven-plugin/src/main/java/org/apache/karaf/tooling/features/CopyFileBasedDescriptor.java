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

begin_class
specifier|public
class|class
name|CopyFileBasedDescriptor
block|{
specifier|private
name|File
name|sourceFile
decl_stmt|;
specifier|private
name|String
name|targetDirectory
decl_stmt|;
specifier|private
name|String
name|targetFileName
decl_stmt|;
specifier|public
name|File
name|getSourceFile
parameter_list|()
block|{
return|return
name|sourceFile
return|;
block|}
specifier|public
name|void
name|setSourceFile
parameter_list|(
name|File
name|sourceFile
parameter_list|)
block|{
name|this
operator|.
name|sourceFile
operator|=
name|sourceFile
expr_stmt|;
block|}
specifier|public
name|String
name|getTargetDirectory
parameter_list|()
block|{
return|return
name|targetDirectory
return|;
block|}
specifier|public
name|void
name|setTargetDirectory
parameter_list|(
name|String
name|targetDirectory
parameter_list|)
block|{
name|this
operator|.
name|targetDirectory
operator|=
name|targetDirectory
expr_stmt|;
block|}
specifier|public
name|String
name|getTargetFileName
parameter_list|()
block|{
return|return
name|targetFileName
return|;
block|}
specifier|public
name|void
name|setTargetFileName
parameter_list|(
name|String
name|targetFileName
parameter_list|)
block|{
name|this
operator|.
name|targetFileName
operator|=
name|targetFileName
expr_stmt|;
block|}
block|}
end_class

end_unit

