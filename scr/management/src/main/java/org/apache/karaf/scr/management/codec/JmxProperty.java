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
name|scr
operator|.
name|management
operator|.
name|codec
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|scr
operator|.
name|management
operator|.
name|ServiceComponentRuntimeMBean
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|JmxProperty
block|{
comment|/**      * The CompositeType which represents a single property      */
specifier|public
specifier|final
specifier|static
name|CompositeType
name|PROPERTY
init|=
name|createPropertyType
argument_list|()
decl_stmt|;
comment|/**      * The TabularType which represents a list of properties      */
specifier|public
specifier|final
specifier|static
name|TabularType
name|PROPERTY_TABLE
init|=
name|createPropertyTableType
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|CompositeData
name|data
decl_stmt|;
specifier|public
name|JmxProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
try|try
block|{
name|String
index|[]
name|itemNames
init|=
name|ServiceComponentRuntimeMBean
operator|.
name|PROPERTY
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
name|key
expr_stmt|;
name|itemValues
index|[
literal|1
index|]
operator|=
name|value
expr_stmt|;
name|data
operator|=
operator|new
name|CompositeDataSupport
argument_list|(
name|PROPERTY
argument_list|,
name|itemNames
argument_list|,
name|itemValues
argument_list|)
expr_stmt|;
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
literal|"Cannot form property open data"
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|TabularDataSupport
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|PROPERTY_TABLE
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|e
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|e
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
operator|new
name|JmxProperty
argument_list|(
name|key
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
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
name|CompositeType
name|createPropertyType
parameter_list|()
block|{
try|try
block|{
name|String
name|description
init|=
literal|"This type encapsulates Scr properties"
decl_stmt|;
name|String
index|[]
name|itemNames
init|=
name|ServiceComponentRuntimeMBean
operator|.
name|PROPERTY
decl_stmt|;
name|OpenType
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
name|itemDescriptions
index|[
literal|0
index|]
operator|=
literal|"The property key"
expr_stmt|;
name|itemDescriptions
index|[
literal|1
index|]
operator|=
literal|"The property value"
expr_stmt|;
return|return
operator|new
name|CompositeType
argument_list|(
literal|"Property"
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
literal|"Unable to build property type"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|TabularType
name|createPropertyTableType
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|TabularType
argument_list|(
literal|"References"
argument_list|,
literal|"The table of all properties"
argument_list|,
name|PROPERTY
argument_list|,
operator|new
name|String
index|[]
block|{
name|ServiceComponentRuntimeMBean
operator|.
name|PROPERTY_KEY
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
literal|"Unable to build properties table type"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

