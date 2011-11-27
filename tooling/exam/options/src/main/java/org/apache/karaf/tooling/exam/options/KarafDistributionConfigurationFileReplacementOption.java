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
name|karaf
operator|.
name|tooling
operator|.
name|exam
operator|.
name|options
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

begin_comment
comment|/**  * If you do not want to replace (or extend) values in a file but rather simply want to replace a configuration file  * "brute force" this option is the one of your choice. It simply removes the original file and replaces it with the one  * configured here.  */
end_comment

begin_class
specifier|public
class|class
name|KarafDistributionConfigurationFileReplacementOption
extends|extends
name|KarafDistributionConfigurationFileOption
block|{
specifier|private
name|File
name|source
decl_stmt|;
specifier|public
name|KarafDistributionConfigurationFileReplacementOption
parameter_list|(
name|String
name|configurationFilePath
parameter_list|,
name|File
name|source
parameter_list|)
block|{
name|super
argument_list|(
name|configurationFilePath
argument_list|)
expr_stmt|;
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
specifier|public
name|File
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
block|}
end_class

end_unit

