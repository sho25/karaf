begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2008, 2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|hooks
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_comment
comment|/**  * OSGi Framework Service Find Hook Service.  *   *<p>  * Bundles registering this service will be called during framework service find  * (get service references) operations.  *   * @ThreadSafe  * @version $Id: 4a939200fa6634a563379b057e11bd1b5d174b9d $  */
end_comment

begin_interface
specifier|public
interface|interface
name|FindHook
block|{
comment|/** 	 * Find hook method. This method is called during the service find operation 	 * (for example, {@link BundleContext#getServiceReferences(String, String)} 	 * ). This method can filter the result of the find operation. 	 *  	 * @param context The bundle context of the bundle performing the find 	 *        operation. 	 * @param name The class name of the services to find or {@code null} 	 *        to find all services. 	 * @param filter The filter criteria of the services to find or 	 *        {@code null} for no filter criteria. 	 * @param allServices {@code true} if the find operation is the result 	 *        of a call to 	 *        {@link BundleContext#getAllServiceReferences(String, String)} 	 * @param references A collection of Service References to be returned as a 	 *        result of the find operation. The implementation of this method 	 *        may remove service references from the collection to prevent the 	 *        references from being returned to the bundle performing the find 	 *        operation. The collection supports all the optional 	 *        {@code Collection} operations except {@code add} and 	 *        {@code addAll}. Attempting to add to the collection will 	 *        result in an {@code UnsupportedOperationException}. The 	 *        collection is not synchronized. 	 */
name|void
name|find
parameter_list|(
name|BundleContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|filter
parameter_list|,
name|boolean
name|allServices
parameter_list|,
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
name|references
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

