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
name|features
operator|.
name|management
operator|.
name|codec
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|ArrayType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeDataSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|OpenDataException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|OpenType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|SimpleType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularDataSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularType
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
name|Feature
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
name|Repository
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
name|management
operator|.
name|FeaturesServiceMBean
import|;
end_import

begin_class
specifier|public
class|class
name|JmxRepository
block|{
specifier|public
specifier|static
specifier|final
name|CompositeType
name|REPOSITORY
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|TabularType
name|REPOSITORY_TABLE
decl_stmt|;
specifier|private
specifier|final
name|CompositeData
name|data
decl_stmt|;
specifier|public
name|JmxRepository
parameter_list|(
name|Repository
name|repository
parameter_list|)
block|{
try|try
block|{
name|String
index|[]
name|itemNames
init|=
name|FeaturesServiceMBean
operator|.
name|REPOSITORY
decl_stmt|;
name|Object
index|[]
name|itemValues
init|=
operator|new
name|Object
index|[
name|itemNames
operator|.
name|length
index|]
decl_stmt|;
name|itemValues
index|[
literal|0
index|]
operator|=
name|repository
operator|.
name|getName
argument_list|()
expr_stmt|;
name|itemValues
index|[
literal|1
index|]
operator|=
name|repository
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
name|itemValues
index|[
literal|2
index|]
operator|=
name|toStringArray
argument_list|(
name|repository
operator|.
name|getRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|itemValues
index|[
literal|3
index|]
operator|=
name|getFeatureIdentifierTable
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repository
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|=
operator|new
name|CompositeDataSupport
argument_list|(
name|REPOSITORY
argument_list|,
name|itemNames
argument_list|,
name|itemValues
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
name|IllegalStateException
argument_list|(
literal|"Cannot form repository open data"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|CompositeData
name|asCompositeData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
specifier|public
specifier|static
name|TabularData
name|tableFrom
parameter_list|(
name|Collection
argument_list|<
name|JmxRepository
argument_list|>
name|repositories
parameter_list|)
block|{
name|TabularDataSupport
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|REPOSITORY_TABLE
argument_list|)
decl_stmt|;
for|for
control|(
name|JmxRepository
name|repository
range|:
name|repositories
control|)
block|{
name|table
operator|.
name|put
argument_list|(
name|repository
operator|.
name|asCompositeData
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
specifier|private
specifier|static
name|String
index|[]
name|toStringArray
parameter_list|(
name|URI
index|[]
name|uris
parameter_list|)
block|{
if|if
condition|(
name|uris
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|res
init|=
operator|new
name|String
index|[
name|uris
operator|.
name|length
index|]
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
name|res
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|res
index|[
name|i
index|]
operator|=
name|uris
index|[
name|i
index|]
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
specifier|static
name|TabularData
name|getFeatureIdentifierTable
parameter_list|(
name|List
argument_list|<
name|Feature
argument_list|>
name|features
parameter_list|)
throws|throws
name|OpenDataException
block|{
name|TabularDataSupport
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|JmxFeature
operator|.
name|FEATURE_IDENTIFIER_TABLE
argument_list|)
decl_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|features
control|)
block|{
name|String
index|[]
name|itemNames
init|=
operator|new
name|String
index|[]
block|{
name|FeaturesServiceMBean
operator|.
name|FEATURE_NAME
block|,
name|FeaturesServiceMBean
operator|.
name|FEATURE_VERSION
block|}
decl_stmt|;
name|Object
index|[]
name|itemValues
init|=
operator|new
name|Object
index|[]
block|{
name|feature
operator|.
name|getName
argument_list|()
block|,
name|feature
operator|.
name|getVersion
argument_list|()
block|}
decl_stmt|;
name|CompositeData
name|ident
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|JmxFeature
operator|.
name|FEATURE_IDENTIFIER
argument_list|,
name|itemNames
argument_list|,
name|itemValues
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|ident
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
static|static
block|{
name|REPOSITORY
operator|=
name|createRepositoryType
argument_list|()
expr_stmt|;
name|REPOSITORY_TABLE
operator|=
name|createRepositoryTableType
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|CompositeType
name|createRepositoryType
parameter_list|()
block|{
try|try
block|{
name|String
name|description
init|=
literal|"This type identify a Karaf repository"
decl_stmt|;
name|String
index|[]
name|itemNames
init|=
name|FeaturesServiceMBean
operator|.
name|REPOSITORY
decl_stmt|;
name|OpenType
argument_list|<
name|?
argument_list|>
index|[]
name|itemTypes
init|=
operator|new
name|OpenType
index|[
name|itemNames
operator|.
name|length
index|]
decl_stmt|;
name|String
index|[]
name|itemDescriptions
init|=
operator|new
name|String
index|[
name|itemNames
operator|.
name|length
index|]
decl_stmt|;
name|itemTypes
index|[
literal|0
index|]
operator|=
name|SimpleType
operator|.
name|STRING
expr_stmt|;
name|itemTypes
index|[
literal|1
index|]
operator|=
name|SimpleType
operator|.
name|STRING
expr_stmt|;
name|itemTypes
index|[
literal|2
index|]
operator|=
operator|new
name|ArrayType
argument_list|<
name|String
argument_list|>
argument_list|(
literal|1
argument_list|,
name|SimpleType
operator|.
name|STRING
argument_list|)
expr_stmt|;
name|itemTypes
index|[
literal|3
index|]
operator|=
name|JmxFeature
operator|.
name|FEATURE_IDENTIFIER_TABLE
expr_stmt|;
name|itemDescriptions
index|[
literal|0
index|]
operator|=
literal|"The name of the repository"
expr_stmt|;
name|itemDescriptions
index|[
literal|1
index|]
operator|=
literal|"The uri of the repository"
expr_stmt|;
name|itemDescriptions
index|[
literal|2
index|]
operator|=
literal|"The dependent repositories"
expr_stmt|;
name|itemDescriptions
index|[
literal|3
index|]
operator|=
literal|"The list of included features"
expr_stmt|;
return|return
operator|new
name|CompositeType
argument_list|(
literal|"Repository"
argument_list|,
name|description
argument_list|,
name|itemNames
argument_list|,
name|itemDescriptions
argument_list|,
name|itemTypes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OpenDataException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to build repository type"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|TabularType
name|createRepositoryTableType
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|TabularType
argument_list|(
literal|"Features"
argument_list|,
literal|"The table of repositories"
argument_list|,
name|REPOSITORY
argument_list|,
operator|new
name|String
index|[]
block|{
name|FeaturesServiceMBean
operator|.
name|REPOSITORY_URI
block|}
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OpenDataException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to build repository table type"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

