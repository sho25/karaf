begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|NamespaceContext
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
name|XMLEventFactory
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
name|XMLEventReader
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
name|XMLEventWriter
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
name|XMLStreamConstants
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|events
operator|.
name|XMLEvent
import|;
end_import

begin_class
specifier|public
class|class
name|IndentingXMLEventWriter
implements|implements
name|XMLEventWriter
block|{
specifier|private
specifier|static
specifier|final
name|XMLEventFactory
name|factory
init|=
name|XMLEventFactory
operator|.
name|newFactory
argument_list|()
decl_stmt|;
specifier|private
name|XMLEventWriter
name|wrappedWriter
decl_stmt|;
specifier|private
name|int
name|depth
init|=
literal|0
decl_stmt|;
specifier|private
name|boolean
name|newLineBeforeStartElement
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|indentBeforeEndElement
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|indentationString
init|=
literal|"    "
decl_stmt|;
comment|/**      * @param wrappedWriter      * @param indentation      */
specifier|public
name|IndentingXMLEventWriter
parameter_list|(
name|XMLEventWriter
name|wrappedWriter
parameter_list|,
name|String
name|indentation
parameter_list|)
block|{
name|this
operator|.
name|wrappedWriter
operator|=
name|wrappedWriter
expr_stmt|;
name|this
operator|.
name|indentationString
operator|=
name|indentation
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|this
operator|.
name|wrappedWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|add
parameter_list|(
name|XMLEvent
name|event
parameter_list|)
throws|throws
name|XMLStreamException
block|{
switch|switch
condition|(
name|event
operator|.
name|getEventType
argument_list|()
condition|)
block|{
case|case
name|XMLStreamConstants
operator|.
name|START_DOCUMENT
case|:
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createCharacters
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|START_ELEMENT
case|:
if|if
condition|(
name|this
operator|.
name|newLineBeforeStartElement
condition|)
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createCharacters
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|newLineBeforeStartElement
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|indentBeforeEndElement
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|possiblyIndent
argument_list|()
expr_stmt|;
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|this
operator|.
name|depth
operator|++
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|END_ELEMENT
case|:
name|this
operator|.
name|newLineBeforeStartElement
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|depth
operator|--
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|indentBeforeEndElement
condition|)
name|this
operator|.
name|possiblyIndent
argument_list|()
expr_stmt|;
name|this
operator|.
name|indentBeforeEndElement
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createCharacters
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|COMMENT
case|:
case|case
name|XMLStreamConstants
operator|.
name|PROCESSING_INSTRUCTION
case|:
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createCharacters
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|newLineBeforeStartElement
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|indentBeforeEndElement
operator|=
literal|true
expr_stmt|;
break|break;
default|default:
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Indent at non-zero depth      * @throws XMLStreamException      */
specifier|private
name|void
name|possiblyIndent
parameter_list|()
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|this
operator|.
name|depth
operator|>
literal|0
condition|)
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|this
operator|.
name|depth
condition|;
name|i
operator|++
control|)
name|sb
operator|.
name|append
argument_list|(
name|this
operator|.
name|indentationString
argument_list|)
expr_stmt|;
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createCharacters
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|add
parameter_list|(
name|XMLEventReader
name|reader
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|this
operator|.
name|wrappedWriter
operator|.
name|add
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|this
operator|.
name|wrappedWriter
operator|.
name|getPrefix
argument_list|(
name|uri
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|this
operator|.
name|wrappedWriter
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setDefaultNamespace
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|this
operator|.
name|wrappedWriter
operator|.
name|setDefaultNamespace
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setNamespaceContext
parameter_list|(
name|NamespaceContext
name|context
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|this
operator|.
name|wrappedWriter
operator|.
name|setNamespaceContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
return|return
name|this
operator|.
name|wrappedWriter
operator|.
name|getNamespaceContext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|this
operator|.
name|wrappedWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setIndentationString
parameter_list|(
name|String
name|indentationString
parameter_list|)
block|{
name|this
operator|.
name|indentationString
operator|=
name|indentationString
expr_stmt|;
block|}
block|}
end_class

end_unit
