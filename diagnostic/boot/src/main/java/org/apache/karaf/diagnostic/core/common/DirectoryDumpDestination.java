begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|diagnostic
operator|.
name|core
operator|.
name|common
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
name|OutputStream
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
name|diagnostic
operator|.
name|core
operator|.
name|DumpDestination
import|;
end_import

begin_comment
comment|/**  * Class which packages dumps to given directory.  */
end_comment

begin_class
specifier|public
class|class
name|DirectoryDumpDestination
implements|implements
name|DumpDestination
block|{
comment|/** 	 * Directory where dump files will be created. 	 */
specifier|private
name|File
name|directory
decl_stmt|;
specifier|public
name|DirectoryDumpDestination
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|directory
operator|=
name|file
expr_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|OutputStream
name|add
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|destination
init|=
operator|new
name|File
argument_list|(
name|directory
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"/"
argument_list|)
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"\\"
argument_list|)
condition|)
block|{
comment|// if name contains slashes we need to create sub directory
name|destination
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|FileOutputStream
argument_list|(
name|destination
argument_list|)
return|;
block|}
specifier|public
name|void
name|save
parameter_list|()
throws|throws
name|Exception
block|{
comment|// do nothing, all should be written to output streams
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"directory: "
operator|+
name|directory
return|;
block|}
block|}
end_class

end_unit

