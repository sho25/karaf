begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2000, 2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|osgi
operator|.
name|framework
package|;
end_package

begin_comment
comment|/**  * A Framework exception used to indicate that a filter string has an invalid  * syntax.  *   *<p>  * An {@code InvalidSyntaxException} object indicates that a filter  * string parameter has an invalid syntax and cannot be parsed. See  * {@link Filter} for a description of the filter string syntax.  *   *<p>  * This exception conforms to the general purpose exception chaining mechanism.  *   * @version $Id: adb84e3bc0b82b842e4da84542057fdf53e2ca6a $  */
end_comment

begin_class
specifier|public
class|class
name|InvalidSyntaxException
extends|extends
name|Exception
block|{
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|4295194420816491875L
decl_stmt|;
comment|/** 	 * The invalid filter string. 	 */
specifier|private
specifier|final
name|String
name|filter
decl_stmt|;
comment|/** 	 * Creates an exception of type {@code InvalidSyntaxException}. 	 *  	 *<p> 	 * This method creates an {@code InvalidSyntaxException} object with 	 * the specified message and the filter string which generated the 	 * exception. 	 *  	 * @param msg The message. 	 * @param filter The invalid filter string. 	 */
specifier|public
name|InvalidSyntaxException
parameter_list|(
name|String
name|msg
parameter_list|,
name|String
name|filter
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
comment|/** 	 * Creates an exception of type {@code InvalidSyntaxException}. 	 *  	 *<p> 	 * This method creates an {@code InvalidSyntaxException} object with 	 * the specified message and the filter string which generated the 	 * exception. 	 *  	 * @param msg The message. 	 * @param filter The invalid filter string. 	 * @param cause The cause of this exception. 	 * @since 1.3 	 */
specifier|public
name|InvalidSyntaxException
parameter_list|(
name|String
name|msg
parameter_list|,
name|String
name|filter
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
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
comment|/** 	 * Returns the filter string that generated the 	 * {@code InvalidSyntaxException} object. 	 *  	 * @return The invalid filter string. 	 * @see BundleContext#getServiceReferences 	 * @see BundleContext#addServiceListener(ServiceListener,String) 	 */
specifier|public
name|String
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
comment|/** 	 * Returns the cause of this exception or {@code null} if no cause was 	 * set. 	 *  	 * @return The cause of this exception or {@code null} if no cause was 	 *         set. 	 * @since 1.3 	 */
specifier|public
name|Throwable
name|getCause
parameter_list|()
block|{
return|return
name|super
operator|.
name|getCause
argument_list|()
return|;
block|}
comment|/** 	 * Initializes the cause of this exception to the specified value. 	 *  	 * @param cause The cause of this exception. 	 * @return This exception. 	 * @throws IllegalArgumentException If the specified cause is this 	 *         exception. 	 * @throws IllegalStateException If the cause of this exception has already 	 *         been set. 	 * @since 1.3 	 */
specifier|public
name|Throwable
name|initCause
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
return|return
name|super
operator|.
name|initCause
argument_list|(
name|cause
argument_list|)
return|;
block|}
block|}
end_class

end_unit

