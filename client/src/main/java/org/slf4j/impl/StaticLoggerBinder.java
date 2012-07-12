begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|slf4j
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|ILoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|spi
operator|.
name|LoggerFactoryBinder
import|;
end_import

begin_comment
comment|/**  * The binding of {@link LoggerFactory} class with an actual instance of  * {@link ILoggerFactory} is performed using information returned by this class.  *   *   * @author Ceki G&uuml;lc&uuml;  */
end_comment

begin_class
specifier|public
class|class
name|StaticLoggerBinder
implements|implements
name|LoggerFactoryBinder
block|{
comment|/**    * The unique instance of this class.    *     */
specifier|private
specifier|static
specifier|final
name|StaticLoggerBinder
name|SINGLETON
init|=
operator|new
name|StaticLoggerBinder
argument_list|()
decl_stmt|;
comment|/**    * Return the singleton of this class.    *     * @return the StaticLoggerBinder singleton    */
specifier|public
specifier|static
specifier|final
name|StaticLoggerBinder
name|getSingleton
parameter_list|()
block|{
return|return
name|SINGLETON
return|;
block|}
comment|/**    * Declare the version of the SLF4J API this implementation is compiled    * against. The value of this field is usually modified with each release.    */
comment|// to avoid constant folding by the compiler, this field must *not* be final
specifier|public
specifier|static
name|String
name|REQUESTED_API_VERSION
init|=
literal|"1.6"
decl_stmt|;
comment|// !final
specifier|private
specifier|static
specifier|final
name|String
name|loggerFactoryClassStr
init|=
name|SimpleLoggerFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**    * The ILoggerFactory instance returned by the {@link #getLoggerFactory}    * method should always be the same object    */
specifier|private
specifier|final
name|ILoggerFactory
name|loggerFactory
decl_stmt|;
specifier|private
name|StaticLoggerBinder
parameter_list|()
block|{
name|loggerFactory
operator|=
operator|new
name|SimpleLoggerFactory
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ILoggerFactory
name|getLoggerFactory
parameter_list|()
block|{
return|return
name|loggerFactory
return|;
block|}
specifier|public
name|String
name|getLoggerFactoryClassStr
parameter_list|()
block|{
return|return
name|loggerFactoryClassStr
return|;
block|}
block|}
end_class

end_unit

