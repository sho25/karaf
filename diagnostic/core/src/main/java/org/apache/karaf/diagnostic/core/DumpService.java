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

begin_comment
comment|/**  * Dump service which allows to customize dump creation process.  *   * @author ldywicki  */
end_comment

begin_interface
specifier|public
interface|interface
name|DumpService
block|{
comment|/** 	 * Return registered providers. 	 *  	 * @return Providers registered in OSGi service registry. 	 */
name|List
argument_list|<
name|DumpProvider
argument_list|>
name|listProviders
parameter_list|()
function_decl|;
comment|/** 	 * List destinations where dumps can be stored. 	 *  	 * @return Destinations registered in OSGi service registry. 	 */
name|List
argument_list|<
name|DumpDestination
argument_list|>
name|listDestinations
parameter_list|()
function_decl|;
comment|/** 	 * Make dump using given providers. 	 *  	 * @param destination Store destination. 	 * @param providers Dump providers to use. 	 * @return True if dump was created. 	 */
name|boolean
name|dump
parameter_list|(
name|DumpDestination
name|destination
parameter_list|,
name|DumpProvider
modifier|...
name|providers
parameter_list|)
function_decl|;
comment|/** 	 * Creates data witch all dump providers. 	 *  	 * @param destination Store destination. 	 * @return True if dump was created. 	 */
name|boolean
name|dumpAll
parameter_list|(
name|DumpDestination
name|destination
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

