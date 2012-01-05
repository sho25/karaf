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
name|*
import|;
end_import

begin_comment
comment|/**  * Service interface with public versions of the protected  * {@code java.net.URLStreamHandler} methods.  *<p>  * The important differences between this interface and the  * {@code URLStreamHandler} class are that the {@code setURL}  * method is absent and the {@code parseURL} method takes a  * {@link URLStreamHandlerSetter} object as the first argument. Classes  * implementing this interface must call the {@code setURL} method on the  * {@code URLStreamHandlerSetter} object received in the  * {@code parseURL} method instead of  * {@code URLStreamHandler.setURL} to avoid a  * {@code SecurityException}.  *   * @see AbstractURLStreamHandlerService  *   * @ThreadSafe  * @version $Id: 4982ef5b407669975afe2856a9702246d2d9c2ba $  */
end_comment

begin_interface
specifier|public
interface|interface
name|URLStreamHandlerService
block|{
comment|/** 	 * @see "java.net.URLStreamHandler.openConnection" 	 */
specifier|public
name|URLConnection
name|openConnection
parameter_list|(
name|URL
name|u
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|IOException
function_decl|;
comment|/** 	 * Parse a URL. This method is called by the {@code URLStreamHandler} 	 * proxy, instead of {@code java.net.URLStreamHandler.parseURL}, 	 * passing a {@code URLStreamHandlerSetter} object. 	 *  	 * @param realHandler The object on which {@code setURL} must be 	 *        invoked for this URL. 	 * @see "java.net.URLStreamHandler.parseURL" 	 */
specifier|public
name|void
name|parseURL
parameter_list|(
name|URLStreamHandlerSetter
name|realHandler
parameter_list|,
name|URL
name|u
parameter_list|,
name|String
name|spec
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|limit
parameter_list|)
function_decl|;
comment|/** 	 * @see "java.net.URLStreamHandler.toExternalForm" 	 */
specifier|public
name|String
name|toExternalForm
parameter_list|(
name|URL
name|u
parameter_list|)
function_decl|;
comment|/** 	 * @see "java.net.URLStreamHandler.equals(URL, URL)" 	 */
specifier|public
name|boolean
name|equals
parameter_list|(
name|URL
name|u1
parameter_list|,
name|URL
name|u2
parameter_list|)
function_decl|;
comment|/** 	 * @see "java.net.URLStreamHandler.getDefaultPort" 	 */
specifier|public
name|int
name|getDefaultPort
parameter_list|()
function_decl|;
comment|/** 	 * @see "java.net.URLStreamHandler.getHostAddress" 	 */
specifier|public
name|InetAddress
name|getHostAddress
parameter_list|(
name|URL
name|u
parameter_list|)
function_decl|;
comment|/** 	 * @see "java.net.URLStreamHandler.hashCode(URL)" 	 */
specifier|public
name|int
name|hashCode
parameter_list|(
name|URL
name|u
parameter_list|)
function_decl|;
comment|/** 	 * @see "java.net.URLStreamHandler.hostsEqual" 	 */
specifier|public
name|boolean
name|hostsEqual
parameter_list|(
name|URL
name|u1
parameter_list|,
name|URL
name|u2
parameter_list|)
function_decl|;
comment|/** 	 * @see "java.net.URLStreamHandler.sameFile" 	 */
specifier|public
name|boolean
name|sameFile
parameter_list|(
name|URL
name|u1
parameter_list|,
name|URL
name|u2
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

