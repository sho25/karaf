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
name|encryption
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

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
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|Encryption
import|;
end_import

begin_import
import|import
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
name|EncryptionService
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
name|BasicEncryption
implements|implements
name|Encryption
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BasicEncryption
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|algorithm
decl_stmt|;
specifier|private
name|String
name|encoding
decl_stmt|;
specifier|private
name|MessageDigest
name|md
decl_stmt|;
specifier|public
name|BasicEncryption
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
for|for
control|(
name|String
name|key
range|:
name|params
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|EncryptionService
operator|.
name|ALGORITHM
operator|.
name|equalsIgnoreCase
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|algorithm
operator|=
name|params
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|EncryptionService
operator|.
name|ENCODING
operator|.
name|equalsIgnoreCase
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|encoding
operator|=
name|params
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported encryption parameter: "
operator|+
name|key
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|algorithm
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Digest algorithm must be specified"
argument_list|)
throw|;
block|}
comment|// Check if the algorithm algorithm is available
try|try
block|{
name|md
operator|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Initialization failed. Digest algorithm "
operator|+
name|algorithm
operator|+
literal|" is not available."
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to configure login module: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|encoding
operator|!=
literal|null
operator|&&
name|encoding
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
operator|!
name|EncryptionService
operator|.
name|ENCODING_HEXADECIMAL
operator|.
name|equalsIgnoreCase
argument_list|(
name|encoding
argument_list|)
operator|&&
operator|!
name|EncryptionService
operator|.
name|ENCODING_BASE64
operator|.
name|equalsIgnoreCase
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Initialization failed. Digest encoding "
operator|+
name|encoding
operator|+
literal|" is not supported."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to configure login module. Digest Encoding "
operator|+
name|encoding
operator|+
literal|" not supported."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|encryptPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Digest the user provided password
name|byte
index|[]
name|data
init|=
name|md
operator|.
name|digest
argument_list|(
name|password
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
operator|||
name|encoding
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|||
name|EncryptionService
operator|.
name|ENCODING_HEXADECIMAL
operator|.
name|equalsIgnoreCase
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
return|return
name|hexEncode
argument_list|(
name|data
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|EncryptionService
operator|.
name|ENCODING_BASE64
operator|.
name|equalsIgnoreCase
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
return|return
name|base64Encode
argument_list|(
name|data
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to configure login module. Digest Encoding "
operator|+
name|encoding
operator|+
literal|" not supported."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|checkPassword
parameter_list|(
name|String
name|provided
parameter_list|,
name|String
name|real
parameter_list|)
block|{
if|if
condition|(
name|real
operator|==
literal|null
operator|&&
name|provided
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|real
operator|==
literal|null
operator|||
name|provided
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// both are non-null
name|String
name|encoded
init|=
name|encryptPassword
argument_list|(
name|provided
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
operator|||
name|encoding
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|||
name|EncryptionService
operator|.
name|ENCODING_HEXADECIMAL
operator|.
name|equalsIgnoreCase
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
return|return
name|real
operator|.
name|equalsIgnoreCase
argument_list|(
name|encoded
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|EncryptionService
operator|.
name|ENCODING_BASE64
operator|.
name|equalsIgnoreCase
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
return|return
name|real
operator|.
name|equals
argument_list|(
name|encoded
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|hexTable
init|=
block|{
operator|(
name|byte
operator|)
literal|'0'
block|,
operator|(
name|byte
operator|)
literal|'1'
block|,
operator|(
name|byte
operator|)
literal|'2'
block|,
operator|(
name|byte
operator|)
literal|'3'
block|,
operator|(
name|byte
operator|)
literal|'4'
block|,
operator|(
name|byte
operator|)
literal|'5'
block|,
operator|(
name|byte
operator|)
literal|'6'
block|,
operator|(
name|byte
operator|)
literal|'7'
block|,
operator|(
name|byte
operator|)
literal|'8'
block|,
operator|(
name|byte
operator|)
literal|'9'
block|,
operator|(
name|byte
operator|)
literal|'a'
block|,
operator|(
name|byte
operator|)
literal|'b'
block|,
operator|(
name|byte
operator|)
literal|'c'
block|,
operator|(
name|byte
operator|)
literal|'d'
block|,
operator|(
name|byte
operator|)
literal|'e'
block|,
operator|(
name|byte
operator|)
literal|'f'
block|}
decl_stmt|;
specifier|public
specifier|static
name|String
name|hexEncode
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
block|{
name|int
name|inOff
init|=
literal|0
decl_stmt|;
name|int
name|length
init|=
name|in
operator|.
name|length
decl_stmt|;
name|byte
index|[]
name|out
init|=
operator|new
name|byte
index|[
name|length
operator|*
literal|2
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|j
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
operator|,
name|j
operator|+=
literal|2
control|)
block|{
name|out
index|[
name|j
index|]
operator|=
name|hexTable
index|[
operator|(
name|in
index|[
name|inOff
index|]
operator|>>
literal|4
operator|)
operator|&
literal|0x0f
index|]
expr_stmt|;
name|out
index|[
name|j
operator|+
literal|1
index|]
operator|=
name|hexTable
index|[
name|in
index|[
name|inOff
index|]
operator|&
literal|0x0f
index|]
expr_stmt|;
name|inOff
operator|++
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|out
argument_list|)
return|;
block|}
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|encodingTable
init|=
block|{
operator|(
name|byte
operator|)
literal|'A'
block|,
operator|(
name|byte
operator|)
literal|'B'
block|,
operator|(
name|byte
operator|)
literal|'C'
block|,
operator|(
name|byte
operator|)
literal|'D'
block|,
operator|(
name|byte
operator|)
literal|'E'
block|,
operator|(
name|byte
operator|)
literal|'F'
block|,
operator|(
name|byte
operator|)
literal|'G'
block|,
operator|(
name|byte
operator|)
literal|'H'
block|,
operator|(
name|byte
operator|)
literal|'I'
block|,
operator|(
name|byte
operator|)
literal|'J'
block|,
operator|(
name|byte
operator|)
literal|'K'
block|,
operator|(
name|byte
operator|)
literal|'L'
block|,
operator|(
name|byte
operator|)
literal|'M'
block|,
operator|(
name|byte
operator|)
literal|'N'
block|,
operator|(
name|byte
operator|)
literal|'O'
block|,
operator|(
name|byte
operator|)
literal|'P'
block|,
operator|(
name|byte
operator|)
literal|'Q'
block|,
operator|(
name|byte
operator|)
literal|'R'
block|,
operator|(
name|byte
operator|)
literal|'S'
block|,
operator|(
name|byte
operator|)
literal|'T'
block|,
operator|(
name|byte
operator|)
literal|'U'
block|,
operator|(
name|byte
operator|)
literal|'V'
block|,
operator|(
name|byte
operator|)
literal|'W'
block|,
operator|(
name|byte
operator|)
literal|'X'
block|,
operator|(
name|byte
operator|)
literal|'Y'
block|,
operator|(
name|byte
operator|)
literal|'Z'
block|,
operator|(
name|byte
operator|)
literal|'a'
block|,
operator|(
name|byte
operator|)
literal|'b'
block|,
operator|(
name|byte
operator|)
literal|'c'
block|,
operator|(
name|byte
operator|)
literal|'d'
block|,
operator|(
name|byte
operator|)
literal|'e'
block|,
operator|(
name|byte
operator|)
literal|'f'
block|,
operator|(
name|byte
operator|)
literal|'g'
block|,
operator|(
name|byte
operator|)
literal|'h'
block|,
operator|(
name|byte
operator|)
literal|'i'
block|,
operator|(
name|byte
operator|)
literal|'j'
block|,
operator|(
name|byte
operator|)
literal|'k'
block|,
operator|(
name|byte
operator|)
literal|'l'
block|,
operator|(
name|byte
operator|)
literal|'m'
block|,
operator|(
name|byte
operator|)
literal|'n'
block|,
operator|(
name|byte
operator|)
literal|'o'
block|,
operator|(
name|byte
operator|)
literal|'p'
block|,
operator|(
name|byte
operator|)
literal|'q'
block|,
operator|(
name|byte
operator|)
literal|'r'
block|,
operator|(
name|byte
operator|)
literal|'s'
block|,
operator|(
name|byte
operator|)
literal|'t'
block|,
operator|(
name|byte
operator|)
literal|'u'
block|,
operator|(
name|byte
operator|)
literal|'v'
block|,
operator|(
name|byte
operator|)
literal|'w'
block|,
operator|(
name|byte
operator|)
literal|'x'
block|,
operator|(
name|byte
operator|)
literal|'y'
block|,
operator|(
name|byte
operator|)
literal|'z'
block|,
operator|(
name|byte
operator|)
literal|'0'
block|,
operator|(
name|byte
operator|)
literal|'1'
block|,
operator|(
name|byte
operator|)
literal|'2'
block|,
operator|(
name|byte
operator|)
literal|'3'
block|,
operator|(
name|byte
operator|)
literal|'4'
block|,
operator|(
name|byte
operator|)
literal|'5'
block|,
operator|(
name|byte
operator|)
literal|'6'
block|,
operator|(
name|byte
operator|)
literal|'7'
block|,
operator|(
name|byte
operator|)
literal|'8'
block|,
operator|(
name|byte
operator|)
literal|'9'
block|,
operator|(
name|byte
operator|)
literal|'+'
block|,
operator|(
name|byte
operator|)
literal|'/'
block|}
decl_stmt|;
specifier|private
specifier|static
name|byte
name|padding
init|=
operator|(
name|byte
operator|)
literal|'='
decl_stmt|;
comment|/**      * encode the input data producing a base 64 encoded byte array.      *      * @return a byte array containing the base 64 encoded data.      */
specifier|public
specifier|static
name|String
name|base64Encode
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|ByteArrayOutputStream
name|bOut
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|base64Encode
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|,
name|bOut
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"exception encoding base64 string: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|String
argument_list|(
name|bOut
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * encode the input data producing a base 64 output stream.      *      * @return the number of bytes produced.      */
specifier|public
specifier|static
name|int
name|base64Encode
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|length
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|modulus
init|=
name|length
operator|%
literal|3
decl_stmt|;
name|int
name|dataLength
init|=
operator|(
name|length
operator|-
name|modulus
operator|)
decl_stmt|;
name|int
name|a1
decl_stmt|,
name|a2
decl_stmt|,
name|a3
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|off
init|;
name|i
operator|<
name|off
operator|+
name|dataLength
condition|;
name|i
operator|+=
literal|3
control|)
block|{
name|a1
operator|=
name|data
index|[
name|i
index|]
operator|&
literal|0xff
expr_stmt|;
name|a2
operator|=
name|data
index|[
name|i
operator|+
literal|1
index|]
operator|&
literal|0xff
expr_stmt|;
name|a3
operator|=
name|data
index|[
name|i
operator|+
literal|2
index|]
operator|&
literal|0xff
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|encodingTable
index|[
operator|(
name|a1
operator|>>>
literal|2
operator|)
operator|&
literal|0x3f
index|]
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|encodingTable
index|[
operator|(
operator|(
name|a1
operator|<<
literal|4
operator|)
operator||
operator|(
name|a2
operator|>>>
literal|4
operator|)
operator|)
operator|&
literal|0x3f
index|]
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|encodingTable
index|[
operator|(
operator|(
name|a2
operator|<<
literal|2
operator|)
operator||
operator|(
name|a3
operator|>>>
literal|6
operator|)
operator|)
operator|&
literal|0x3f
index|]
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|encodingTable
index|[
name|a3
operator|&
literal|0x3f
index|]
argument_list|)
expr_stmt|;
block|}
comment|/*          * process the tail end.          */
name|int
name|b1
decl_stmt|,
name|b2
decl_stmt|,
name|b3
decl_stmt|;
name|int
name|d1
decl_stmt|,
name|d2
decl_stmt|;
switch|switch
condition|(
name|modulus
condition|)
block|{
case|case
literal|0
case|:
comment|/* nothing left to do */
break|break;
case|case
literal|1
case|:
name|d1
operator|=
name|data
index|[
name|off
operator|+
name|dataLength
index|]
operator|&
literal|0xff
expr_stmt|;
name|b1
operator|=
operator|(
name|d1
operator|>>>
literal|2
operator|)
operator|&
literal|0x3f
expr_stmt|;
name|b2
operator|=
operator|(
name|d1
operator|<<
literal|4
operator|)
operator|&
literal|0x3f
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|encodingTable
index|[
name|b1
index|]
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|encodingTable
index|[
name|b2
index|]
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|padding
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|padding
argument_list|)
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|d1
operator|=
name|data
index|[
name|off
operator|+
name|dataLength
index|]
operator|&
literal|0xff
expr_stmt|;
name|d2
operator|=
name|data
index|[
name|off
operator|+
name|dataLength
operator|+
literal|1
index|]
operator|&
literal|0xff
expr_stmt|;
name|b1
operator|=
operator|(
name|d1
operator|>>>
literal|2
operator|)
operator|&
literal|0x3f
expr_stmt|;
name|b2
operator|=
operator|(
operator|(
name|d1
operator|<<
literal|4
operator|)
operator||
operator|(
name|d2
operator|>>>
literal|4
operator|)
operator|)
operator|&
literal|0x3f
expr_stmt|;
name|b3
operator|=
operator|(
name|d2
operator|<<
literal|2
operator|)
operator|&
literal|0x3f
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|encodingTable
index|[
name|b1
index|]
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|encodingTable
index|[
name|b2
index|]
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|encodingTable
index|[
name|b3
index|]
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|padding
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
operator|(
name|dataLength
operator|/
literal|3
operator|)
operator|*
literal|4
operator|+
operator|(
operator|(
name|modulus
operator|==
literal|0
operator|)
condition|?
literal|0
else|:
literal|4
operator|)
return|;
block|}
block|}
end_class

end_unit

