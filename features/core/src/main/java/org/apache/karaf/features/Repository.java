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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_comment
comment|/**  *<p>A repository of features. A runtime representation of JAXB model read from feature XML files.</p>  *  *<p>Original model may be subject to further processing (e.g., blacklisting)</p>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Repository
extends|extends
name|Blacklisting
block|{
comment|/**      * Logical name of the {@link Repository}      * @return      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Original URI of the {@link Repository}, where feature declarations were loaded from      * @return      */
name|URI
name|getURI
parameter_list|()
function_decl|;
comment|/**      * An array of referenced repository URIs (<code>/features/repository</code>)      * @return      */
name|URI
index|[]
name|getRepositories
parameter_list|()
function_decl|;
comment|/**      * An array of referenced resource repository URIs (<code>/features/resource-repository</code>)      * @return      */
name|URI
index|[]
name|getResourceRepositories
parameter_list|()
function_decl|;
comment|/**      * An array of {@link Feature features} in this {@link Repository} after possible processing.      * @return      */
name|Feature
index|[]
name|getFeatures
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

