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
name|bundle
operator|.
name|core
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_comment
comment|/**  * SPI to track an extended bundle state for injection frameworks like blueprint that  * also reports on  dependencies and status on the injection container level  */
end_comment

begin_interface
specifier|public
interface|interface
name|BundleStateService
block|{
specifier|public
specifier|final
specifier|static
name|String
name|NAME_BLUEPRINT
init|=
literal|"Blueprint"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|NAME_SPRING_DM
init|=
literal|"Spring DM"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|NAME_DS
init|=
literal|"Declarative Services"
decl_stmt|;
comment|/**      * Name of the framework the implementation supports.      * Should return one of the NAME_ constants above if it matches      *        * @return name of the framework      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Give a textual report about the details of a bundle status      * like missing namespace handlers or service dependencies.      * Should also give the details if there are config errors      *       * @param bundle the bundle to get diag for.      * @return diagnostic details.      */
name|String
name|getDiag
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
function_decl|;
comment|/**      * Report the bundle state from the framework point of view.       * If the framework is not active it should return Unknown.      *       * @param bundle the bundle to get state for.      * @return the current bundle state.      */
name|BundleState
name|getState
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

