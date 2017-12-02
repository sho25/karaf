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
name|features
package|;
end_package

begin_interface
specifier|public
interface|interface
name|Library
block|{
name|String
name|TYPE_ENDORSED
init|=
literal|"endorsed"
decl_stmt|;
name|String
name|TYPE_EXTENSION
init|=
literal|"extension"
decl_stmt|;
name|String
name|TYPE_BOOT
init|=
literal|"boot"
decl_stmt|;
name|String
name|TYPE_DEFAULT
init|=
literal|"default"
decl_stmt|;
name|String
name|getLocation
parameter_list|()
function_decl|;
name|String
name|getType
parameter_list|()
function_decl|;
comment|/**      * Whether given library's exported packages should be added to<code>org.osgi.framework.system.packages.extra</code>      * property in<code>${karaf.etc}/config.properties</code>.      * @return      */
name|boolean
name|isExport
parameter_list|()
function_decl|;
comment|/**      * Whether given library's exported packages should be added to<code>org.osgi.framework.bootdelegation</code>      * property in<code>${karaf.etc}/config.properties</code>      * @return      */
name|boolean
name|isDelegate
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

