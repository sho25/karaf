begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|ssh
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|ServerBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|SshServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|NamedFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|cipher
operator|.
name|BuiltinCiphers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|cipher
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|compression
operator|.
name|Compression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|kex
operator|.
name|KeyExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|mac
operator|.
name|Mac
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
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

begin_class
specifier|public
class|class
name|SshUtils
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SshUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|>
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|S
argument_list|>
argument_list|>
name|filter
parameter_list|(
name|Class
argument_list|<
name|S
argument_list|>
name|type
parameter_list|,
name|Collection
argument_list|<
name|NamedFactory
argument_list|<
name|S
argument_list|>
argument_list|>
name|factories
parameter_list|,
name|String
index|[]
name|names
parameter_list|)
block|{
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|S
argument_list|>
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|name
operator|=
name|name
operator|.
name|trim
argument_list|()
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|NamedFactory
argument_list|<
name|S
argument_list|>
name|factory
range|:
name|factories
control|)
block|{
if|if
condition|(
name|factory
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Configured "
operator|+
name|type
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|" '"
operator|+
name|name
operator|+
literal|"' not available"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|Mac
argument_list|>
argument_list|>
name|buildMacs
parameter_list|(
name|String
index|[]
name|names
parameter_list|)
block|{
return|return
name|filter
argument_list|(
name|Mac
operator|.
name|class
argument_list|,
operator|new
name|ServerConfig
argument_list|()
operator|.
name|getMacFactories
argument_list|()
argument_list|,
name|names
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|Cipher
argument_list|>
argument_list|>
name|buildCiphers
parameter_list|(
name|String
index|[]
name|names
parameter_list|)
block|{
name|ServerConfig
name|defaults
init|=
operator|new
name|ServerConfig
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|Cipher
argument_list|>
argument_list|>
name|avail
init|=
name|defaults
operator|.
name|getCipherFactories
argument_list|()
decl_stmt|;
return|return
name|filter
argument_list|(
name|Cipher
operator|.
name|class
argument_list|,
name|avail
argument_list|,
name|names
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|KeyExchange
argument_list|>
argument_list|>
name|buildKexAlgorithms
parameter_list|(
name|String
index|[]
name|names
parameter_list|)
block|{
name|ServerConfig
name|defaults
init|=
operator|new
name|ServerConfig
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|KeyExchange
argument_list|>
argument_list|>
name|avail
init|=
name|defaults
operator|.
name|getKeyExchangeFactories
argument_list|()
decl_stmt|;
return|return
name|filter
argument_list|(
name|KeyExchange
operator|.
name|class
argument_list|,
name|avail
argument_list|,
name|names
argument_list|)
return|;
block|}
comment|/**      * Simple helper class to avoid duplicating available configuration entries.      */
specifier|private
specifier|static
specifier|final
class|class
name|ServerConfig
extends|extends
name|ServerBuilder
block|{
specifier|public
name|ServerConfig
parameter_list|()
block|{
name|this
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
comment|/**          * Just initializes the default configuration - does not create a          * server instance.          *          * @return always<code>null</code>          */
annotation|@
name|Override
specifier|public
name|SshServer
name|build
parameter_list|()
block|{
return|return
name|this
operator|.
name|build
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/**          * Just initializes the default configuration - does not create a          * server instance.          *          * @return always<code>null</code>          */
annotation|@
name|Override
specifier|public
name|SshServer
name|build
parameter_list|(
name|boolean
name|isFillWithDefaultValues
parameter_list|)
block|{
if|if
condition|(
name|isFillWithDefaultValues
condition|)
block|{
name|this
operator|.
name|fillWithDefaultValues
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|KeyExchange
argument_list|>
argument_list|>
name|getKeyExchangeFactories
parameter_list|()
block|{
return|return
name|keyExchangeFactories
return|;
block|}
specifier|public
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|Cipher
argument_list|>
argument_list|>
name|getCipherFactories
parameter_list|()
block|{
return|return
name|cipherFactories
return|;
block|}
specifier|public
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|Compression
argument_list|>
argument_list|>
name|getCompressionFactories
parameter_list|()
block|{
return|return
name|compressionFactories
return|;
block|}
specifier|public
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|Mac
argument_list|>
argument_list|>
name|getMacFactories
parameter_list|()
block|{
return|return
name|macFactories
return|;
block|}
block|}
block|}
end_class

end_unit

