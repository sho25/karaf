begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|config
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
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
name|SSLContext
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
name|SSLServerSocketFactory
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

begin_comment
comment|/**  * Based on http://svn.apache.org/repos/asf/geronimo/trunk/modules/management/  *                      src/java/org/apache/geronimo/management/geronimo/KeystoreManager.java  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|KeystoreManager
block|{
name|KeystoreInstance
name|getKeystore
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Gets a SSLContext using one Keystore to access the private key      * and another to provide the list of trusted certificate authorities.      * @param provider      * @param protocol The SSL protocol to use      * @param algorithm The SSL algorithm to use      * @param keyStore The key keystore name as provided by listKeystores.  The      *                 KeystoreInstance for this keystore must be unlocked.      * @param keyAlias The name of the private key in the keystore.  The      *                 KeystoreInstance for this keystore must have unlocked      *                 this key.      * @param trustStore The trust keystore name as provided by listKeystores.      *                   The KeystoreInstance for this keystore must have      *                   unlocked this key.      *      * @throws KeystoreIsLocked Occurs when the requested key keystore cannot      *                          be used because it has not been unlocked.      * @throws KeyIsLocked Occurs when the requested private key in the key      *                     keystore cannot be used because it has not been      *                     unlocked.      */
name|SSLContext
name|createSSLContext
parameter_list|(
name|String
name|provider
parameter_list|,
name|String
name|protocol
parameter_list|,
name|String
name|algorithm
parameter_list|,
name|String
name|keyStore
parameter_list|,
name|String
name|keyAlias
parameter_list|,
name|String
name|trustStore
parameter_list|)
throws|throws
name|GeneralSecurityException
function_decl|;
comment|/**      * Gets a ServerSocketFactory using one Keystore to access the private key      * and another to provide the list of trusted certificate authorities.      * @param provider      * @param protocol The SSL protocol to use      * @param algorithm The SSL algorithm to use      * @param keyStore The key keystore name as provided by listKeystores.  The      *                 KeystoreInstance for this keystore must be unlocked.      * @param keyAlias The name of the private key in the keystore.  The      *                 KeystoreInstance for this keystore must have unlocked      *                 this key.      * @param trustStore The trust keystore name as provided by listKeystores.      *                   The KeystoreInstance for this keystore must have      *                   unlocked this key.      *      * @throws KeystoreIsLocked Occurs when the requested key keystore cannot      *                          be used because it has not been unlocked.      * @throws KeyIsLocked Occurs when the requested private key in the key      *                     keystore cannot be used because it has not been      *                     unlocked.      */
name|SSLServerSocketFactory
name|createSSLServerFactory
parameter_list|(
name|String
name|provider
parameter_list|,
name|String
name|protocol
parameter_list|,
name|String
name|algorithm
parameter_list|,
name|String
name|keyStore
parameter_list|,
name|String
name|keyAlias
parameter_list|,
name|String
name|trustStore
parameter_list|)
throws|throws
name|GeneralSecurityException
function_decl|;
comment|/**      * Gets a SocketFactory using one Keystore to access the private key      * and another to provide the list of trusted certificate authorities.      * @param provider The SSL provider to use, or null for the default      * @param protocol The SSL protocol to use      * @param algorithm The SSL algorithm to use      * @param keyStore The key keystore name as provided by listKeystores.  The      *                 KeystoreInstance for this keystore must be unlocked.      * @param keyAlias The name of the private key in the keystore.  The      *                 KeystoreInstance for this keystore must have unlocked      *                 this key.      * @param trustStore The trust keystore name as provided by listKeystores.      *                   The KeystoreInstance for this keystore must have      *                   unlocked this key.      *      * @throws KeystoreIsLocked Occurs when the requested key keystore cannot      *                          be used because it has not been unlocked.      * @throws KeyIsLocked Occurs when the requested private key in the key      *                     keystore cannot be used because it has not been      *                     unlocked.      * @throws GeneralSecurityException      */
name|SSLSocketFactory
name|createSSLFactory
parameter_list|(
name|String
name|provider
parameter_list|,
name|String
name|protocol
parameter_list|,
name|String
name|algorithm
parameter_list|,
name|String
name|keyStore
parameter_list|,
name|String
name|keyAlias
parameter_list|,
name|String
name|trustStore
parameter_list|)
throws|throws
name|GeneralSecurityException
function_decl|;
block|}
end_interface

end_unit

