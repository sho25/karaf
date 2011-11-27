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

begin_comment
comment|/**  * This option allows to extend configurations in each configuration file based on the karaf.home location. The value  * extends the current value (e.g. a=b to a=a,b) instead of replacing it. If there is no current value it is added.  *  * If you would like to have add or replace functionality please use the  * {@link KarafDistributionConfigurationFilePutOption} instead.  */
end_comment

begin_class
specifier|public
class|class
name|KarafDistributionConfigurationFileExtendOption
extends|extends
name|KarafDistributionConfigurationFileOption
block|{
specifier|public
name|KarafDistributionConfigurationFileExtendOption
parameter_list|(
name|String
name|configurationFilePath
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|configurationFilePath
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|KarafDistributionConfigurationFileExtendOption
parameter_list|(
name|ConfigurationPointer
name|pointer
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|pointer
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

