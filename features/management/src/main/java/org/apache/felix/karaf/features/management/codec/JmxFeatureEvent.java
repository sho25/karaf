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
name|features
operator|.
name|management
operator|.
name|codec
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeatureEvent
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
name|JmxFeatureEvent
block|{
specifier|public
specifier|static
specifier|final
name|CompositeType
name|FEATURE_EVENT
decl_stmt|;
specifier|private
specifier|final
name|CompositeData
name|data
decl_stmt|;
specifier|public
name|JmxFeatureEvent
parameter_list|(
name|FeatureEvent
name|event
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
name|FEATURE_EVENT
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
name|event
operator|.
name|getFeature
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|itemValues
index|[
literal|1
index|]
operator|=
name|event
operator|.
name|getFeature
argument_list|()
operator|.
name|getVersion
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|event
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|FeatureInstalled
case|:
name|itemValues
index|[
literal|2
index|]
operator|=
name|FeaturesServiceMBean
operator|.
name|FEATURE_EVENT_EVENT_TYPE_INSTALLED
expr_stmt|;
break|break;
case|case
name|FeatureUninstalled
case|:
name|itemValues
index|[
literal|2
index|]
operator|=
name|FeaturesServiceMBean
operator|.
name|FEATURE_EVENT_EVENT_TYPE_UNINSTALLED
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unsupported event type: "
operator|+
name|event
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
name|data
operator|=
operator|new
name|CompositeDataSupport
argument_list|(
name|FEATURE_EVENT
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
literal|"Cannot form feature event open data"
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
static|static
block|{
name|FEATURE_EVENT
operator|=
name|createFeatureEventType
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|CompositeType
name|createFeatureEventType
parameter_list|()
block|{
try|try
block|{
name|String
name|description
init|=
literal|"This type identify a Karaf feature event"
decl_stmt|;
name|String
index|[]
name|itemNames
init|=
name|FeaturesServiceMBean
operator|.
name|FEATURE_EVENT
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
name|itemTypes
index|[
literal|2
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
literal|"The id of the feature"
expr_stmt|;
name|itemDescriptions
index|[
literal|1
index|]
operator|=
literal|"The version of the feature"
expr_stmt|;
name|itemDescriptions
index|[
literal|2
index|]
operator|=
literal|"The type of the event"
expr_stmt|;
return|return
operator|new
name|CompositeType
argument_list|(
literal|"FeatureEvent"
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
literal|"Unable to build featureEvent type"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

