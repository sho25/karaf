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
name|tooling
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
name|ByteArrayOutputStream
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
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
name|features
operator|.
name|internal
operator|.
name|model
operator|.
name|Features
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
name|features
operator|.
name|internal
operator|.
name|model
operator|.
name|JaxbUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
comment|/**  * @version $Rev:$ $Date:$  */
end_comment

begin_class
specifier|public
class|class
name|GenerateDescriptorMojoTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testReadXml
parameter_list|()
throws|throws
name|JAXBException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
throws|,
name|XMLStreamException
block|{
name|InputStream
name|in
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"input-features.xml"
argument_list|)
decl_stmt|;
name|Features
name|featuresRoot
init|=
name|JaxbUtil
operator|.
name|unmarshal
argument_list|(
name|in
argument_list|,
literal|false
argument_list|)
decl_stmt|;
assert|assert
name|featuresRoot
operator|.
name|getRepository
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
assert|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|JaxbUtil
operator|.
name|marshal
argument_list|(
name|featuresRoot
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
assert|assert
name|s
operator|.
name|indexOf
argument_list|(
literal|"repository"
argument_list|)
operator|>
operator|-
literal|1
assert|;
assert|assert
name|s
operator|.
name|indexOf
argument_list|(
literal|"http://karaf.apache.org/xmlns/features/v1.0.0"
argument_list|)
operator|>
operator|-
literal|1
assert|;
block|}
block|}
end_class

end_unit

