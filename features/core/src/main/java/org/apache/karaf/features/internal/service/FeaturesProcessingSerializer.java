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
name|features
operator|.
name|internal
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
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
name|io
operator|.
name|OutputStreamWriter
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
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|JAXBContext
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
name|Marshaller
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
name|Unmarshaller
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
name|UnmarshallerHandler
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
name|SAXParserFactory
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
name|XMLInputFactory
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
name|XMLOutputFactory
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
name|processing
operator|.
name|FeaturesProcessing
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
name|processing
operator|.
name|ObjectFactory
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
name|util
operator|.
name|xml
operator|.
name|IndentingXMLEventWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|swissbox
operator|.
name|property
operator|.
name|BundleContextPropertyResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|util
operator|.
name|property
operator|.
name|DictionaryPropertyResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|util
operator|.
name|property
operator|.
name|PropertyResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|FrameworkUtil
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

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|Attributes
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
name|ContentHandler
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
name|InputSource
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
name|Locator
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

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|XMLReader
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
name|helpers
operator|.
name|AttributesImpl
import|;
end_import

begin_comment
comment|/**  * A class to help serialize {@link org.apache.karaf.features.internal.model.processing.FeaturesProcessing} model  * but with added template comments for main sections of<code>org.apache.karaf.features.xml</code> file.  */
end_comment

begin_class
specifier|public
class|class
name|FeaturesProcessingSerializer
block|{
specifier|public
specifier|static
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FeaturesProcessingSerializer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|JAXBContext
name|FEATURES_PROCESSING_CONTEXT
decl_stmt|;
specifier|public
name|FeaturesProcessingSerializer
parameter_list|()
block|{
name|Bundle
name|bundle
init|=
name|FrameworkUtil
operator|.
name|getBundle
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|bundle
operator|==
literal|null
condition|?
literal|null
else|:
name|bundle
operator|.
name|getBundleContext
argument_list|()
expr_stmt|;
try|try
block|{
name|FEATURES_PROCESSING_CONTEXT
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|ObjectFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Reads {@link FeaturesProcessing features processing model} from input stream      * @param stream      * @return      */
specifier|public
name|FeaturesProcessing
name|read
parameter_list|(
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|this
operator|.
name|read
argument_list|(
name|stream
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Reads {@link FeaturesProcessing features processing model} from input stream      * @param stream      * @param versions additional properties to resolve placeholders in features processing XML      * @return      */
specifier|public
name|FeaturesProcessing
name|read
parameter_list|(
name|InputStream
name|stream
parameter_list|,
name|Properties
name|versions
parameter_list|)
throws|throws
name|Exception
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|FEATURES_PROCESSING_CONTEXT
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|UnmarshallerHandler
name|handler
init|=
name|unmarshaller
operator|.
name|getUnmarshallerHandler
argument_list|()
decl_stmt|;
comment|// BundleContextPropertyResolver gives access to e.g., ${karaf.base}
specifier|final
name|PropertyResolver
name|resolver
init|=
name|bundleContext
operator|==
literal|null
condition|?
operator|new
name|DictionaryPropertyResolver
argument_list|(
name|versions
argument_list|)
else|:
operator|new
name|DictionaryPropertyResolver
argument_list|(
name|versions
argument_list|,
operator|new
name|BundleContextPropertyResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
decl_stmt|;
comment|// indirect unmarshaling with property resolution inside XML attribute values and CDATA
name|SAXParserFactory
name|spf
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|spf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|XMLReader
name|xmlReader
init|=
name|spf
operator|.
name|newSAXParser
argument_list|()
operator|.
name|getXMLReader
argument_list|()
decl_stmt|;
name|xmlReader
operator|.
name|setContentHandler
argument_list|(
operator|new
name|ResolvingContentHandler
argument_list|(
operator|new
name|Properties
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|resolver
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|String
name|value
init|=
name|resolver
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|value
operator|==
literal|null
condition|?
name|defaultValue
else|:
name|value
return|;
block|}
block|}
argument_list|,
name|handler
argument_list|)
argument_list|)
expr_stmt|;
name|xmlReader
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|stream
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|(
name|FeaturesProcessing
operator|)
name|handler
operator|.
name|getResult
argument_list|()
return|;
block|}
comment|/**      * Writes the model to output stream and adds comments for main sections.      * @param model      * @param output      */
specifier|public
name|void
name|write
parameter_list|(
name|FeaturesProcessing
name|model
parameter_list|,
name|OutputStream
name|output
parameter_list|)
block|{
try|try
block|{
comment|// JAXB model as stream which is next parsed as XMLEvents
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Marshaller
name|marshaller
init|=
name|FEATURES_PROCESSING_CONTEXT
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|model
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|baos
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|emptyElements
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|emptyElements
operator|.
name|put
argument_list|(
literal|"blacklistedRepositories"
argument_list|,
name|model
operator|.
name|getBlacklistedRepositories
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|emptyElements
operator|.
name|put
argument_list|(
literal|"blacklistedFeatures"
argument_list|,
name|model
operator|.
name|getBlacklistedFeatures
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|emptyElements
operator|.
name|put
argument_list|(
literal|"blacklistedBundles"
argument_list|,
name|model
operator|.
name|getBlacklistedBundles
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|emptyElements
operator|.
name|put
argument_list|(
literal|"overrideBundleDependency"
argument_list|,
name|model
operator|.
name|getOverrideBundleDependency
argument_list|()
operator|.
name|getRepositories
argument_list|()
operator|.
name|size
argument_list|()
operator|+
name|model
operator|.
name|getOverrideBundleDependency
argument_list|()
operator|.
name|getFeatures
argument_list|()
operator|.
name|size
argument_list|()
operator|+
name|model
operator|.
name|getOverrideBundleDependency
argument_list|()
operator|.
name|getBundles
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|emptyElements
operator|.
name|put
argument_list|(
literal|"bundleReplacements"
argument_list|,
name|model
operator|.
name|getBundleReplacements
argument_list|()
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|emptyElements
operator|.
name|put
argument_list|(
literal|"featureReplacements"
argument_list|,
name|model
operator|.
name|getFeatureReplacements
argument_list|()
operator|.
name|getReplacements
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
comment|// A mix of direct write and stream of XML events. It's not easy (without knowing StAX impl) to
comment|// output self closed tags for example.
name|BufferedWriter
name|writer
init|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|output
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--\n"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"    Configuration generated by Karaf Assembly Builder\n"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"-->\n"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"feature-processing-comments.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|XMLEventReader
name|xmlEventReader
init|=
name|XMLInputFactory
operator|.
name|newFactory
argument_list|()
operator|.
name|createXMLEventReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|XMLEventWriter
name|xmlEventWriter
init|=
operator|new
name|IndentingXMLEventWriter
argument_list|(
name|XMLOutputFactory
operator|.
name|newFactory
argument_list|()
operator|.
name|createXMLEventWriter
argument_list|(
name|writer
argument_list|)
argument_list|,
literal|"    "
argument_list|)
decl_stmt|;
name|XMLEventFactory
name|evFactory
init|=
name|XMLEventFactory
operator|.
name|newFactory
argument_list|()
decl_stmt|;
name|int
name|depth
init|=
literal|0
decl_stmt|;
name|boolean
name|skipClose
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|xmlEventReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|XMLEvent
name|ev
init|=
name|xmlEventReader
operator|.
name|nextEvent
argument_list|()
decl_stmt|;
name|int
name|type
init|=
name|ev
operator|.
name|getEventType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|!=
name|XMLEvent
operator|.
name|START_DOCUMENT
operator|&&
name|type
operator|!=
name|XMLEvent
operator|.
name|END_DOCUMENT
condition|)
block|{
if|if
condition|(
name|type
operator|==
name|XMLEvent
operator|.
name|START_ELEMENT
condition|)
block|{
name|skipClose
operator|=
literal|false
expr_stmt|;
name|depth
operator|++
expr_stmt|;
if|if
condition|(
name|depth
operator|==
literal|2
condition|)
block|{
name|String
name|tag
init|=
name|ev
operator|.
name|asStartElement
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|comment
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|tag
argument_list|)
decl_stmt|;
name|xmlEventWriter
operator|.
name|add
argument_list|(
name|evFactory
operator|.
name|createCharacters
argument_list|(
literal|"\n    "
argument_list|)
argument_list|)
expr_stmt|;
name|xmlEventWriter
operator|.
name|add
argument_list|(
name|evFactory
operator|.
name|createComment
argument_list|(
literal|" "
operator|+
name|comment
operator|+
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|emptyElements
operator|.
name|get
argument_list|(
name|tag
argument_list|)
operator|!=
literal|null
operator|&&
name|emptyElements
operator|.
name|get
argument_list|(
name|tag
argument_list|)
condition|)
block|{
name|skipClose
operator|=
literal|true
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<"
operator|+
name|tag
operator|+
literal|" />\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|XMLEvent
operator|.
name|END_ELEMENT
condition|)
block|{
name|skipClose
operator|=
literal|false
expr_stmt|;
name|depth
operator|--
expr_stmt|;
if|if
condition|(
name|depth
operator|==
literal|1
condition|)
block|{
name|String
name|tag
init|=
name|ev
operator|.
name|asEndElement
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|comment
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|tag
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyElements
operator|.
name|get
argument_list|(
name|tag
argument_list|)
operator|!=
literal|null
operator|&&
name|emptyElements
operator|.
name|get
argument_list|(
name|tag
argument_list|)
condition|)
block|{
name|skipClose
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|type
operator|==
name|XMLEvent
operator|.
name|END_ELEMENT
operator|&&
name|depth
operator|==
literal|0
condition|)
block|{
name|xmlEventWriter
operator|.
name|add
argument_list|(
name|evFactory
operator|.
name|createCharacters
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|skipClose
condition|)
block|{
name|xmlEventWriter
operator|.
name|add
argument_list|(
name|ev
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|==
name|XMLEvent
operator|.
name|START_ELEMENT
operator|&&
name|depth
operator|==
literal|1
condition|)
block|{
name|xmlEventWriter
operator|.
name|add
argument_list|(
name|evFactory
operator|.
name|createCharacters
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ResolvingContentHandler
implements|implements
name|ContentHandler
block|{
specifier|public
specifier|static
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ResolvingContentHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Properties
name|properties
decl_stmt|;
specifier|private
name|ContentHandler
name|target
decl_stmt|;
specifier|private
name|boolean
name|inElement
init|=
literal|false
decl_stmt|;
specifier|private
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
specifier|public
name|ResolvingContentHandler
parameter_list|(
name|Properties
name|properties
parameter_list|,
name|ContentHandler
name|target
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setDocumentLocator
parameter_list|(
name|Locator
name|locator
parameter_list|)
block|{
name|target
operator|.
name|setDocumentLocator
argument_list|(
name|locator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|startDocument
parameter_list|()
throws|throws
name|SAXException
block|{
name|target
operator|.
name|startDocument
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|endDocument
parameter_list|()
throws|throws
name|SAXException
block|{
name|target
operator|.
name|endDocument
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|startPrefixMapping
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|SAXException
block|{
name|target
operator|.
name|startPrefixMapping
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
name|endPrefixMapping
parameter_list|(
name|String
name|prefix
parameter_list|)
throws|throws
name|SAXException
block|{
name|target
operator|.
name|endPrefixMapping
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|startElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|,
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
name|AttributesImpl
name|resolvedAttributes
init|=
operator|new
name|AttributesImpl
argument_list|(
name|atts
argument_list|)
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
name|atts
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|resolvedAttributes
operator|.
name|setAttribute
argument_list|(
name|i
argument_list|,
name|atts
operator|.
name|getURI
argument_list|(
name|i
argument_list|)
argument_list|,
name|atts
operator|.
name|getLocalName
argument_list|(
name|i
argument_list|)
argument_list|,
name|atts
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
argument_list|,
name|atts
operator|.
name|getType
argument_list|(
name|i
argument_list|)
argument_list|,
name|resolve
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|inElement
condition|)
block|{
name|flushBuffer
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|inElement
operator|=
literal|true
expr_stmt|;
name|target
operator|.
name|startElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|qName
argument_list|,
name|resolvedAttributes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|endElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|inElement
condition|)
block|{
name|flushBuffer
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|inElement
operator|=
literal|false
expr_stmt|;
block|}
name|target
operator|.
name|endElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|qName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|characters
parameter_list|(
name|char
index|[]
name|ch
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|inElement
condition|)
block|{
name|sw
operator|.
name|append
argument_list|(
operator|new
name|String
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|target
operator|.
name|characters
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|ignorableWhitespace
parameter_list|(
name|char
index|[]
name|ch
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// only elements without PCDATA in DTD have whitespace passed to this method. so ignore
name|target
operator|.
name|ignorableWhitespace
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|processingInstruction
parameter_list|(
name|String
name|target
parameter_list|,
name|String
name|data
parameter_list|)
throws|throws
name|SAXException
block|{
name|this
operator|.
name|target
operator|.
name|processingInstruction
argument_list|(
name|target
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|skippedEntity
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
name|target
operator|.
name|skippedEntity
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**          * Pass collected characters to target {@link ContentHandler}          * @param resolve whether to expect placeholders in collected text          */
specifier|private
name|void
name|flushBuffer
parameter_list|(
name|boolean
name|resolve
parameter_list|)
throws|throws
name|SAXException
block|{
name|String
name|value
init|=
name|sw
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|resolved
init|=
name|resolve
condition|?
name|resolve
argument_list|(
name|value
argument_list|)
else|:
name|value
decl_stmt|;
name|target
operator|.
name|characters
argument_list|(
name|resolved
operator|.
name|toCharArray
argument_list|()
argument_list|,
literal|0
argument_list|,
name|resolved
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|sw
operator|=
operator|new
name|StringWriter
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|resolve
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|String
name|resolved
init|=
name|org
operator|.
name|ops4j
operator|.
name|util
operator|.
name|collections
operator|.
name|PropertyResolver
operator|.
name|resolve
argument_list|(
name|properties
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolved
operator|.
name|contains
argument_list|(
literal|"${"
argument_list|)
condition|)
block|{
comment|// there are still unresolved properties - just log warning
name|LOG
operator|.
name|warn
argument_list|(
literal|"Value {} has unresolved properties, please check configuration."
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|resolved
return|;
block|}
block|}
block|}
end_class

end_unit

