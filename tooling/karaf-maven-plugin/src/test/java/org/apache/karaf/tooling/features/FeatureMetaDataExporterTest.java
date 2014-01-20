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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|UnsupportedEncodingException
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
name|tooling
operator|.
name|features
operator|.
name|model
operator|.
name|ArtifactRef
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
name|tooling
operator|.
name|features
operator|.
name|model
operator|.
name|BundleRef
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
name|tooling
operator|.
name|features
operator|.
name|model
operator|.
name|Feature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|DefaultArtifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|handler
operator|.
name|DefaultArtifactHandler
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

begin_class
specifier|public
class|class
name|FeatureMetaDataExporterTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testWriteFeature
parameter_list|()
throws|throws
name|XMLStreamException
throws|,
name|UnsupportedEncodingException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|FeatureMetaDataExporter
name|featureMetaDataExporter
init|=
operator|new
name|FeatureMetaDataExporter
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|BundleRef
name|bundle
init|=
operator|new
name|BundleRef
argument_list|(
literal|"mvn:org.apache.example/example/1.0.0"
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|Artifact
name|bundleArtifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
literal|"org.apache.example"
argument_list|,
literal|"example"
argument_list|,
literal|"1.0.0"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|,
literal|null
argument_list|,
operator|new
name|DefaultArtifactHandler
argument_list|()
argument_list|)
decl_stmt|;
name|bundle
operator|.
name|setArtifact
argument_list|(
name|bundleArtifact
argument_list|)
expr_stmt|;
name|ArtifactRef
name|configFile
init|=
operator|new
name|ArtifactRef
argument_list|(
literal|"mvn:org.apache.example/example/1.0.0/cfg"
argument_list|)
decl_stmt|;
name|Artifact
name|configFileArtifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
literal|"org.apache.example"
argument_list|,
literal|"example"
argument_list|,
literal|"1.0.0"
argument_list|,
literal|null
argument_list|,
literal|"xml"
argument_list|,
literal|"exampleconfig"
argument_list|,
operator|new
name|DefaultArtifactHandler
argument_list|()
argument_list|)
decl_stmt|;
name|configFile
operator|.
name|setArtifact
argument_list|(
name|configFileArtifact
argument_list|)
expr_stmt|;
name|Feature
name|feature
init|=
operator|new
name|Feature
argument_list|(
literal|"example"
argument_list|)
decl_stmt|;
name|feature
operator|.
name|addBundle
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|feature
operator|.
name|addConfigFile
argument_list|(
name|configFile
argument_list|)
expr_stmt|;
name|featureMetaDataExporter
operator|.
name|writeFeature
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|featureMetaDataExporter
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|formatString
argument_list|(
name|baos
operator|.
name|toString
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|expectedValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|expectedValue
parameter_list|()
block|{
return|return
name|formatString
argument_list|(
literal|"<features>"
operator|+
literal|"<feature name=\"example\">"
operator|+
literal|"<bundle start-level=\"10\" name=\"example-1.0.0.jar\" groupId=\"org.apache.example\" artifactId=\"example\" type=\"jar\" version=\"1.0.0\">mvn:org.apache.example/example/1.0.0</bundle>"
operator|+
literal|"<config name=\"example-1.0.0-exampleconfig.xml\" groupId=\"org.apache.example\" artifactId=\"example\" type=\"xml\" classifier=\"exampleconfig\" version=\"1.0.0\">mvn:org.apache.example/example/1.0.0/cfg</config>"
operator|+
literal|"</feature>"
operator|+
literal|"</features>"
argument_list|)
return|;
block|}
specifier|private
name|String
name|formatString
parameter_list|(
name|String
name|string
parameter_list|)
block|{
return|return
name|string
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|""
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r"
argument_list|,
literal|""
argument_list|)
return|;
block|}
block|}
end_class

end_unit

