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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

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
class|class
name|ManagedSSLSocketFactory
extends|extends
name|SSLSocketFactory
implements|implements
name|Comparator
argument_list|<
name|Object
argument_list|>
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
argument_list|<>
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
specifier|private
name|SSLSocketFactory
name|delegate
decl_stmt|;
comment|// When<code>java.naming.ldap.factory.socket</code> property configures custom
comment|// {@link SSLSocketFactory}, LdapCtx invokes special compare method to enable pooling.
specifier|public
name|ManagedSSLSocketFactory
parameter_list|(
name|SSLSocketFactory
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getDefaultCipherSuites
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getDefaultCipherSuites
argument_list|()
return|;
block|}
specifier|public
name|String
index|[]
name|getSupportedCipherSuites
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getSupportedCipherSuites
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Socket
name|createSocket
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|createSocket
argument_list|()
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|Socket
name|s
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|boolean
name|autoClose
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|createSocket
argument_list|(
name|s
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|autoClose
argument_list|)
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|InetAddress
name|localHost
parameter_list|,
name|int
name|localPort
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|localHost
argument_list|,
name|localPort
argument_list|)
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|InetAddress
name|host
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|InetAddress
name|address
parameter_list|,
name|int
name|port
parameter_list|,
name|InetAddress
name|localAddress
parameter_list|,
name|int
name|localPort
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|createSocket
argument_list|(
name|address
argument_list|,
name|port
argument_list|,
name|localAddress
argument_list|,
name|localPort
argument_list|)
return|;
block|}
comment|/**      * For com.sun.jndi.ldap.ClientId#invokeComparator(com.sun.jndi.ldap.ClientId, com.sun.jndi.ldap.ClientId).      */
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|f1
parameter_list|,
name|Object
name|f2
parameter_list|)
block|{
if|if
condition|(
name|f1
operator|==
literal|null
operator|&&
name|f2
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|f1
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
if|if
condition|(
name|f2
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
comment|// com.sun.jndi.ldap.ClientId#invokeComparator() passes com.sun.jndi.ldap.ClientId.socketFactory as f1 and f2
comment|// these are String values
if|if
condition|(
name|f1
operator|instanceof
name|String
operator|&&
name|f2
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
operator|(
name|String
operator|)
name|f1
operator|)
operator|.
name|compareTo
argument_list|(
operator|(
name|String
operator|)
name|f2
argument_list|)
return|;
block|}
comment|// fallback to undefined behavior
return|return
name|f1
operator|.
name|toString
argument_list|()
operator|.
name|compareTo
argument_list|(
name|f2
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

