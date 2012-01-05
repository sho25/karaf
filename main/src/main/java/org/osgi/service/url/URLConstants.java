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

begin_comment
comment|/**  * Defines standard names for property keys associated with  * {@link URLStreamHandlerService} and {@code java.net.ContentHandler}  * services.  *   *<p>  * The values associated with these keys are of type  * {@code java.lang.String[]} or {@code java.lang.String}, unless  * otherwise indicated.  *   * @noimplement  * @version $Id: 5ec8db316249f4b956fe083b986c11153d0fa8fe $  */
end_comment

begin_interface
specifier|public
interface|interface
name|URLConstants
block|{
comment|/** 	 * Service property naming the protocols serviced by a 	 * URLStreamHandlerService. The property's value is a protocol name or an 	 * array of protocol names. 	 */
specifier|public
specifier|static
specifier|final
name|String
name|URL_HANDLER_PROTOCOL
init|=
literal|"url.handler.protocol"
decl_stmt|;
comment|/** 	 * Service property naming the MIME types serviced by a 	 * java.net.ContentHandler. The property's value is a MIME type or an array 	 * of MIME types. 	 */
specifier|public
specifier|static
specifier|final
name|String
name|URL_CONTENT_MIMETYPE
init|=
literal|"url.content.mimetype"
decl_stmt|;
block|}
end_interface

end_unit

