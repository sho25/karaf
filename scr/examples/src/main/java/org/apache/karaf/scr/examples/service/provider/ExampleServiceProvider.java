begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|scr
operator|.
name|examples
operator|.
name|service
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|scr
operator|.
name|examples
operator|.
name|service
operator|.
name|ExampleService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|log
operator|.
name|LogService
import|;
end_import

begin_import
import|import
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Reference
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Component
specifier|public
class|class
name|ExampleServiceProvider
implements|implements
name|ExampleService
block|{
specifier|public
specifier|static
specifier|final
name|String
name|COMPONENT_NAME
init|=
literal|"ExampleServiceProvider"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMPONENT_LABEL
init|=
literal|"Example Service Consumer Component"
decl_stmt|;
specifier|private
name|LogService
name|logService
decl_stmt|;
specifier|private
name|String
name|name
init|=
literal|"To whom it may concern"
decl_stmt|;
specifier|private
name|String
name|salutation
init|=
literal|"Hello"
decl_stmt|;
specifier|public
name|void
name|printGreetings
parameter_list|()
block|{
name|logService
operator|.
name|log
argument_list|(
name|LogService
operator|.
name|LOG_INFO
argument_list|,
name|salutation
operator|+
literal|" "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @param salutation the salutation to set 	 */
specifier|public
name|void
name|setSalutation
parameter_list|(
name|String
name|salutation
parameter_list|)
block|{
name|this
operator|.
name|salutation
operator|=
name|salutation
expr_stmt|;
block|}
comment|/** 	 * @param name the name to set 	 */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Reference
specifier|protected
name|void
name|setLogService
parameter_list|(
name|LogService
name|logService
parameter_list|)
block|{
name|this
operator|.
name|logService
operator|=
name|logService
expr_stmt|;
block|}
specifier|protected
name|void
name|unsetLogService
parameter_list|(
name|LogService
name|logService
parameter_list|)
block|{
name|this
operator|.
name|logService
operator|=
name|logService
expr_stmt|;
block|}
block|}
end_class

end_unit

