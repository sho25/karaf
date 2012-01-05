begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2009, 2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|launch
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Bundle
import|;
end_import

begin_comment
comment|/**  * A factory for creating {@link Framework} instances.  *   *<p>  * A framework implementation jar must contain the following resource:  *   *<pre>  * /META-INF/services/org.osgi.framework.launch.FrameworkFactory  *</pre>  *   * This UTF-8 encoded resource must contain the name of the framework  * implementation's FrameworkFactory implementation class. Space and tab  * characters, including blank lines, in the resource must be ignored. The  * number sign ('#'&#92;u0023) and all characters following it on each line are  * a comment and must be ignored.  *   *<p>  * Launchers can find the name of the FrameworkFactory implementation class in  * the resource and then load and construct a FrameworkFactory object for the  * framework implementation. The FrameworkFactory implementation class must have  * a public, no-argument constructor. Java&#8482; SE 6 introduced the  * {@code ServiceLoader} class which can create a FrameworkFactory instance  * from the resource.  *   * @ThreadSafe  * @noimplement  * @version $Id: c370e19dba77231f0dbf1601218ad97b20391ea0 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|FrameworkFactory
block|{
comment|/** 	 * Create a new {@link Framework} instance. 	 *  	 * @param configuration The framework properties to configure the new 	 *        framework instance. If framework properties are not provided by 	 *        the configuration argument, the created framework instance must 	 *        use some reasonable default configuration appropriate for the 	 *        current VM. For example, the system packages for the current 	 *        execution environment should be properly exported. The specified 	 *        configuration argument may be {@code null}. The created 	 *        framework instance must copy any information needed from the 	 *        specified configuration argument since the configuration argument 	 *        can be changed after the framework instance has been created. 	 * @return A new, configured {@link Framework} instance. The framework 	 *         instance must be in the {@link Bundle#INSTALLED} state. 	 * @throws SecurityException If the caller does not have 	 *         {@code AllPermission}, and the Java Runtime Environment 	 *         supports permissions. 	 */
name|Framework
name|newFramework
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|configuration
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

