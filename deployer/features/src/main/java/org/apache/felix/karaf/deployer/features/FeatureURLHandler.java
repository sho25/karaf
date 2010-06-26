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
name|deployer
operator|.
name|features
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|url
operator|.
name|AbstractURLStreamHandlerService
import|;
end_import

begin_comment
comment|/**  * URL handler for features  */
end_comment

begin_class
specifier|public
class|class
name|FeatureURLHandler
extends|extends
name|AbstractURLStreamHandlerService
block|{
specifier|private
specifier|static
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FeatureURLHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|String
name|SYNTAX
init|=
literal|"feature: xml-uri"
decl_stmt|;
specifier|private
name|URL
name|featureXmlURL
decl_stmt|;
comment|/**      * Open the connection for the given URL.      *      * @param url the url from which to open a connection.      * @return a connection on the specified URL.      * @throws java.io.IOException if an error occurs or if the URL is malformed.      */
annotation|@
name|Override
specifier|public
name|URLConnection
name|openConnection
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|url
operator|.
name|getPath
argument_list|()
operator|==
literal|null
operator|||
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
literal|"Path can not be null or empty. Syntax: "
operator|+
name|SYNTAX
argument_list|)
throw|;
block|}
name|featureXmlURL
operator|=
operator|new
name|URL
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Blueprint xml URL is: ["
operator|+
name|featureXmlURL
operator|+
literal|"]"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Connection
argument_list|(
name|url
argument_list|)
return|;
block|}
specifier|public
name|URL
name|getFeatureXmlURL
parameter_list|()
block|{
return|return
name|featureXmlURL
return|;
block|}
specifier|public
class|class
name|Connection
extends|extends
name|URLConnection
block|{
specifier|public
name|Connection
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|super
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|connect
parameter_list|()
throws|throws
name|IOException
block|{         }
annotation|@
name|Override
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|FeatureTransformer
operator|.
name|transform
argument_list|(
name|featureXmlURL
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Error opening blueprint xml url"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|(
name|IOException
operator|)
operator|new
name|IOException
argument_list|(
literal|"Error opening blueprint xml url"
argument_list|)
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

