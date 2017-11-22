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
name|service
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Repository
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
name|features
operator|.
name|internal
operator|.
name|model
operator|.
name|Features
import|;
end_import

begin_comment
comment|/**  * Service that can process (enhance, modify, trim, ...) a set of features read from {@link Repository}.  */
end_comment

begin_interface
specifier|public
interface|interface
name|FeaturesProcessor
block|{
comment|/**      * Checks whether given repository URI is<em>blacklisted</em>      * @param uri      * @return      */
name|boolean
name|isRepositoryBlacklisted
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Processes original {@link Features JAXB model of features}      * @param features      */
name|void
name|process
parameter_list|(
name|Features
name|features
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

