begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2002, 2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|url
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_comment
comment|/**  * Interface used by {@code URLStreamHandlerService} objects to call the  * {@code setURL} method on the proxy {@code URLStreamHandler}  * object.  *   *<p>  * Objects of this type are passed to the  * {@link URLStreamHandlerService#parseURL} method. Invoking the  * {@code setURL} method on the {@code URLStreamHandlerSetter}  * object will invoke the {@code setURL} method on the proxy  * {@code URLStreamHandler} object that is actually registered with  * {@code java.net.URL} for the protocol.  *   * @ThreadSafe  * @version $Id: f55d4c29678503c244f56dcb2b5621b3be11cc8d $  */
end_comment

begin_interface
specifier|public
interface|interface
name|URLStreamHandlerSetter
block|{
comment|/** 	 * @see "java.net.URLStreamHandler.setURL(URL,String,String,int,String,String)" 	 *  	 * @deprecated This method is only for compatibility with handlers written 	 *             for JDK 1.1. 	 */
specifier|public
name|void
name|setURL
parameter_list|(
name|URL
name|u
parameter_list|,
name|String
name|protocol
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|file
parameter_list|,
name|String
name|ref
parameter_list|)
function_decl|;
comment|/** 	 * @see "java.net.URLStreamHandler.setURL(URL,String,String,int,String,String,String,String)" 	 */
specifier|public
name|void
name|setURL
parameter_list|(
name|URL
name|u
parameter_list|,
name|String
name|protocol
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|authority
parameter_list|,
name|String
name|userInfo
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|query
parameter_list|,
name|String
name|ref
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

