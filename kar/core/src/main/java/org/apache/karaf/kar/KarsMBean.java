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
name|kar
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_interface
specifier|public
interface|interface
name|KarsMBean
block|{
comment|/**      * List the installed KAR files.      *      * @return the list of KAR files.      * @throws Exception in case of listing failure.      */
name|List
argument_list|<
name|String
argument_list|>
name|getKars
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Install a KAR file from the given URL.      *      * @param url the JAR URL.      * @throws Exception in case of installation failure.      */
name|void
name|install
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Uninstall a KAR file.      *       * @param name the name of the KAR file.      * @throws Exception in case of uninstall failure.      */
name|void
name|uninstall
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

