begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|apache
operator|.
name|felix
operator|.
name|fileinstall
operator|.
name|ArtifactTransformer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ErrorHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_comment
comment|/**  * A deployment listener that listens for spring xml applications  * and creates bundles for these.  */
end_comment

begin_class
specifier|public
class|class
name|SpringDeploymentListener
implements|implements
name|ArtifactTransformer
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|SpringDeploymentListener
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|DocumentBuilderFactory
name|dbf
decl_stmt|;
specifier|public
name|boolean
name|canHandle
parameter_list|(
name|File
name|artifact
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|artifact
operator|.
name|isFile
argument_list|()
operator|&&
name|artifact
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
name|Document
name|doc
init|=
name|parse
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"beans"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
literal|"http://www.springframework.org/schema/beans"
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Unable to parse deployed file "
operator|+
name|artifact
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|File
name|transform
parameter_list|(
name|File
name|artifact
parameter_list|,
name|File
name|tmpDir
parameter_list|)
block|{
try|try
block|{
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|tmpDir
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
operator|+
literal|".jar"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|destFile
argument_list|)
decl_stmt|;
name|SpringTransformer
operator|.
name|transform
argument_list|(
name|artifact
operator|.
name|toURL
argument_list|()
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
name|destFile
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Unable to build spring application bundle"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|protected
name|Document
name|parse
parameter_list|(
name|File
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|dbf
operator|==
literal|null
condition|)
block|{
name|dbf
operator|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|db
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|ErrorHandler
argument_list|()
block|{
specifier|public
name|void
name|warning
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{             }
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{             }
specifier|public
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
throw|throw
name|exception
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|db
operator|.
name|parse
argument_list|(
name|artifact
argument_list|)
return|;
block|}
block|}
end_class

end_unit

