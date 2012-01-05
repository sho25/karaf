begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|weaving
package|;
end_package

begin_comment
comment|/**  * A weaving exception used to indicate that the class load should be failed but  * the weaving hook must not be blacklisted by the framework.  *   *<p>  * This exception conforms to the general purpose exception chaining mechanism.  *   * @version $Id: eb38b85f6ed66ec445fb2f0ee7143df021327a9a $  */
end_comment

begin_class
specifier|public
class|class
name|WeavingException
extends|extends
name|RuntimeException
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
comment|/** 	 * Creates a {@code WeavingException} with the specified message and 	 * exception cause. 	 *  	 * @param msg The associated message. 	 * @param cause The cause of this exception. 	 */
specifier|public
name|WeavingException
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Creates a {@code WeavingException} with the specified message. 	 *  	 * @param msg The message. 	 */
specifier|public
name|WeavingException
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

