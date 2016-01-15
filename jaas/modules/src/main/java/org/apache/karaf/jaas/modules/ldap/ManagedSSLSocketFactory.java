begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|ldap
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSocketFactory
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|ManagedSSLSocketFactory
extends|extends
name|SSLSocketFactory
block|{
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|SSLSocketFactory
argument_list|>
name|factories
init|=
operator|new
name|ThreadLocal
argument_list|<
name|SSLSocketFactory
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|void
name|setSocketFactory
parameter_list|(
name|SSLSocketFactory
name|factory
parameter_list|)
block|{
name|factories
operator|.
name|set
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|SSLSocketFactory
name|getDefault
parameter_list|()
block|{
name|SSLSocketFactory
name|factory
init|=
name|factories
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No SSLSocketFactory parameters have been set!"
argument_list|)
throw|;
block|}
return|return
name|factory
return|;
block|}
block|}
end_class

end_unit
