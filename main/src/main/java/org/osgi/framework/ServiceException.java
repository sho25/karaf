begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2007, 2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A service exception used to indicate that a service problem occurred.  *   *<p>  * A {@code ServiceException} object is created by the Framework or  * service implementation to denote an exception condition in the service. A  * type code is used to identify the exception type for future extendability.  * Service implementations may also create subclasses of  * {@code ServiceException}. When subclassing, the subclass should set  * the type to {@link #SUBCLASSED} to indicate that  * {@code ServiceException} has been subclassed.  *   *<p>  * This exception conforms to the general purpose exception chaining mechanism.  *   * @version $Id: 453b6021eed4543f754e20696b9f8b33a7e121ee $  * @since 1.5  */
end_comment

begin_class
specifier|public
class|class
name|ServiceException
extends|extends
name|RuntimeException
block|{
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3038963223712959631L
decl_stmt|;
comment|/** 	 * Type of service exception. 	 */
specifier|private
specifier|final
name|int
name|type
decl_stmt|;
comment|/** 	 * No exception type is unspecified. 	 */
specifier|public
specifier|static
specifier|final
name|int
name|UNSPECIFIED
init|=
literal|0
decl_stmt|;
comment|/** 	 * The service has been unregistered. 	 */
specifier|public
specifier|static
specifier|final
name|int
name|UNREGISTERED
init|=
literal|1
decl_stmt|;
comment|/** 	 * The service factory produced an invalid service object. 	 */
specifier|public
specifier|static
specifier|final
name|int
name|FACTORY_ERROR
init|=
literal|2
decl_stmt|;
comment|/** 	 * The service factory threw an exception. 	 */
specifier|public
specifier|static
specifier|final
name|int
name|FACTORY_EXCEPTION
init|=
literal|3
decl_stmt|;
comment|/** 	 * The exception is a subclass of ServiceException. The subclass should be 	 * examined for the type of the exception. 	 */
specifier|public
specifier|static
specifier|final
name|int
name|SUBCLASSED
init|=
literal|4
decl_stmt|;
comment|/** 	 * An error occurred invoking a remote service. 	 */
specifier|public
specifier|static
specifier|final
name|int
name|REMOTE
init|=
literal|5
decl_stmt|;
comment|/** 	 * The service factory resulted in a recursive call to itself for the 	 * requesting bundle. 	 *  	 * @since 1.6 	 */
specifier|public
specifier|static
specifier|final
name|int
name|FACTORY_RECURSION
init|=
literal|6
decl_stmt|;
comment|/** 	 * Creates a {@code ServiceException} with the specified message and 	 * exception cause. 	 *  	 * @param msg The associated message. 	 * @param cause The cause of this exception. 	 */
specifier|public
name|ServiceException
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|this
argument_list|(
name|msg
argument_list|,
name|UNSPECIFIED
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Creates a {@code ServiceException} with the specified message. 	 *  	 * @param msg The message. 	 */
specifier|public
name|ServiceException
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|this
argument_list|(
name|msg
argument_list|,
name|UNSPECIFIED
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Creates a {@code ServiceException} with the specified message, 	 * type and exception cause. 	 *  	 * @param msg The associated message. 	 * @param type The type for this exception. 	 * @param cause The cause of this exception. 	 */
specifier|public
name|ServiceException
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|type
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
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/** 	 * Creates a {@code ServiceException} with the specified message and 	 * type. 	 *  	 * @param msg The message. 	 * @param type The type for this exception. 	 */
specifier|public
name|ServiceException
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/** 	 * Returns the type for this exception or {@code UNSPECIFIED} if the 	 * type was unspecified or unknown. 	 *  	 * @return The type of this exception. 	 */
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

