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
name|karaf
operator|.
name|log
operator|.
name|core
operator|.
name|internal
package|;
end_package

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
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

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
name|StringWriter
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|LogServiceLog4j2XmlImplTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testInsertIndentedTabs
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
literal|"<Configuration>\n"
operator|+
literal|"\t<Loggers>\n"
operator|+
literal|"\t</Loggers>\n"
operator|+
literal|"</Configuration>"
decl_stmt|;
name|String
name|out
init|=
name|insertIndented
argument_list|(
name|xml
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<Configuration>\n"
operator|+
literal|"\t<Loggers>\n"
operator|+
literal|"\t\t<Logger/>\n"
operator|+
literal|"\t</Loggers>\n"
operator|+
literal|"</Configuration>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|insertIndented
argument_list|(
name|xml
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<Configuration>\n"
operator|+
literal|"\t<Loggers>\n"
operator|+
literal|"\t\t<Logger/>\n"
operator|+
literal|"\t</Loggers>\n"
operator|+
literal|"</Configuration>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsertIndentedSpaces
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
literal|"<Configuration>\n"
operator|+
literal|"<Loggers>\n"
operator|+
literal|"</Loggers>\n"
operator|+
literal|"</Configuration>"
decl_stmt|;
name|String
name|out
init|=
name|insertIndented
argument_list|(
name|xml
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<Configuration>\n"
operator|+
literal|"<Loggers>\n"
operator|+
literal|"<Logger/>\n"
operator|+
literal|"</Loggers>\n"
operator|+
literal|"</Configuration>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|insertIndented
argument_list|(
name|xml
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<Configuration>\n"
operator|+
literal|"<Loggers>\n"
operator|+
literal|"<Logger/>\n"
operator|+
literal|"</Loggers>\n"
operator|+
literal|"</Configuration>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsertIndentedTabsWithRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
literal|"<Configuration>\n"
operator|+
literal|"\t<Loggers>\n"
operator|+
literal|"\t\t<Root/>\n"
operator|+
literal|"\t</Loggers>\n"
operator|+
literal|"</Configuration>"
decl_stmt|;
name|String
name|out
init|=
name|insertIndented
argument_list|(
name|xml
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<Configuration>\n"
operator|+
literal|"\t<Loggers>\n"
operator|+
literal|"\t\t<Root/>\n"
operator|+
literal|"\t\t<Logger/>\n"
operator|+
literal|"\t</Loggers>\n"
operator|+
literal|"</Configuration>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|insertIndented
argument_list|(
name|xml
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<Configuration>\n"
operator|+
literal|"\t<Loggers>\n"
operator|+
literal|"\t\t<Logger/>\n"
operator|+
literal|"\t\t<Root/>\n"
operator|+
literal|"\t</Loggers>\n"
operator|+
literal|"</Configuration>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsertIndentedSpacesWithRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
literal|"<Configuration>\n"
operator|+
literal|"<Loggers>\n"
operator|+
literal|"<Root/>\n"
operator|+
literal|"</Loggers>\n"
operator|+
literal|"</Configuration>"
decl_stmt|;
name|String
name|out
init|=
name|insertIndented
argument_list|(
name|xml
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<Configuration>\n"
operator|+
literal|"<Loggers>\n"
operator|+
literal|"<Root/>\n"
operator|+
literal|"<Logger/>\n"
operator|+
literal|"</Loggers>\n"
operator|+
literal|"</Configuration>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|insertIndented
argument_list|(
name|xml
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<Configuration>\n"
operator|+
literal|"<Loggers>\n"
operator|+
literal|"<Logger/>\n"
operator|+
literal|"<Root/>\n"
operator|+
literal|"</Loggers>\n"
operator|+
literal|"</Configuration>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|insertIndented
parameter_list|(
name|String
name|xml
parameter_list|,
name|boolean
name|atBeginning
parameter_list|)
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|LogServiceLog4j2XmlImpl
operator|.
name|loadConfig
argument_list|(
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|xml
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Element
name|element
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"Logger"
argument_list|)
decl_stmt|;
name|LogServiceLog4j2XmlImpl
operator|.
name|insertIndented
argument_list|(
operator|(
name|Element
operator|)
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getElementsByTagName
argument_list|(
literal|"Loggers"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
argument_list|,
name|element
argument_list|,
name|atBeginning
argument_list|)
expr_stmt|;
try|try
init|(
name|StringWriter
name|os
init|=
operator|new
name|StringWriter
argument_list|()
init|)
block|{
name|TransformerFactory
name|tFactory
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Transformer
name|transformer
init|=
name|tFactory
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|,
literal|"the.xml"
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|os
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|os
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

