begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2004-2011 QOS.ch  * All rights reserved.  *  * Permission is hereby granted, free  of charge, to any person obtaining  * a  copy  of this  software  and  associated  documentation files  (the  * "Software"), to  deal in  the Software without  restriction, including  * without limitation  the rights to  use, copy, modify,  merge, publish,  * distribute,  sublicense, and/or sell  copies of  the Software,  and to  * permit persons to whom the Software  is furnished to do so, subject to  * the following conditions:  *  * The  above  copyright  notice  and  this permission  notice  shall  be  * included in all copies or substantial portions of the Software.  *  * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,  * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF  * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND  * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE  * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION  * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION  * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.  *  */
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

