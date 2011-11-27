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
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_comment
comment|/**  * Option to configure the Karaf -Dkaraf.startLocalConsole and -Dkaraf.startRemoteShell options. Per default both are  * started automatically. If you like to change this behavior simply add this option to your container configuration.  */
end_comment

begin_class
specifier|public
class|class
name|KarafDistributionConfigurationConsoleOption
implements|implements
name|Option
block|{
specifier|private
name|Boolean
name|startLocalConsole
decl_stmt|;
specifier|private
name|Boolean
name|startRemoteShell
decl_stmt|;
specifier|public
name|KarafDistributionConfigurationConsoleOption
parameter_list|(
name|Boolean
name|startLocalConsole
parameter_list|,
name|Boolean
name|startRemoteShell
parameter_list|)
block|{
name|this
operator|.
name|startLocalConsole
operator|=
name|startLocalConsole
expr_stmt|;
name|this
operator|.
name|startRemoteShell
operator|=
name|startRemoteShell
expr_stmt|;
block|}
comment|/**      * Sets the -Dkaraf.startLocalConsole to true      */
specifier|public
name|KarafDistributionConfigurationConsoleOption
name|startLocalConsole
parameter_list|()
block|{
name|startLocalConsole
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the -Dkaraf.startLocalConsole to false      */
specifier|public
name|KarafDistributionConfigurationConsoleOption
name|ignoreLocalConsole
parameter_list|()
block|{
name|startLocalConsole
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the -Dkaraf.startRemoteShell to true      */
specifier|public
name|KarafDistributionConfigurationConsoleOption
name|startRemoteShell
parameter_list|()
block|{
name|startRemoteShell
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the -Dkaraf.startRemoteShell to false      */
specifier|public
name|KarafDistributionConfigurationConsoleOption
name|ignoreRemoteShell
parameter_list|()
block|{
name|startRemoteShell
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Boolean
name|getStartLocalConsole
parameter_list|()
block|{
return|return
name|startLocalConsole
return|;
block|}
specifier|public
name|Boolean
name|getStartRemoteShell
parameter_list|()
block|{
return|return
name|startRemoteShell
return|;
block|}
block|}
end_class

end_unit

