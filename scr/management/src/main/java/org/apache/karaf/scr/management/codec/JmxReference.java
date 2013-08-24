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
name|felix
operator|.
name|scr
operator|.
name|Reference
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
name|scr
operator|.
name|management
operator|.
name|ScrServiceMBean
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
name|Constants
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

begin_class
specifier|public
class|class
name|JmxReference
block|{
comment|/**      * The CompositeType which represents a single reference      */
specifier|public
specifier|final
specifier|static
name|CompositeType
name|REFERENCE
init|=
name|createReferenceType
argument_list|()
decl_stmt|;
comment|/**      * The TabularType which represents a list of references      */
specifier|public
specifier|final
specifier|static
name|TabularType
name|REFERENCE_TABLE
init|=
name|createReferenceTableType
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|CompositeData
name|data
decl_stmt|;
comment|//String[] COMPONENT = { REFERENCE_NAME, REFERENCE_STATE, REFERENCE_CARDINALITY, REFERENCE_AVAILABILITY, REFERENCE_POLICY, REFERENCE_BOUND_SERVICES};
specifier|public
name|JmxReference
parameter_list|(
name|Reference
name|reference
parameter_list|)
block|{
try|try
block|{
name|String
index|[]
name|itemNames
init|=
name|ScrServiceMBean
operator|.
name|REFERENCE
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
name|reference
operator|.
name|getName
argument_list|()
expr_stmt|;
name|itemValues
index|[
literal|1
index|]
operator|=
name|reference
operator|.
name|isSatisfied
argument_list|()
expr_stmt|;
name|itemValues
index|[
literal|2
index|]
operator|=
name|getCardinality
argument_list|(
name|reference
argument_list|)
expr_stmt|;
name|itemValues
index|[
literal|3
index|]
operator|=
name|getAvailability
argument_list|(
name|reference
argument_list|)
expr_stmt|;
name|itemValues
index|[
literal|4
index|]
operator|=
name|getPolicy
argument_list|(
name|reference
argument_list|)
expr_stmt|;
name|itemValues
index|[
literal|5
index|]
operator|=
name|getBoundServices
argument_list|(
name|reference
argument_list|)
expr_stmt|;
name|data
operator|=
operator|new
name|CompositeDataSupport
argument_list|(
name|REFERENCE
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
literal|"Cannot form feature open data"
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
name|Reference
modifier|...
name|references
parameter_list|)
block|{
name|TabularDataSupport
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|REFERENCE_TABLE
argument_list|)
decl_stmt|;
if|if
condition|(
name|references
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Reference
name|reference
range|:
name|references
control|)
block|{
name|table
operator|.
name|put
argument_list|(
operator|new
name|JmxReference
argument_list|(
name|reference
argument_list|)
operator|.
name|asCompositeData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|table
return|;
block|}
specifier|private
specifier|static
name|CompositeType
name|createReferenceType
parameter_list|()
block|{
try|try
block|{
name|String
name|description
init|=
literal|"This type encapsulates Scr references"
decl_stmt|;
name|String
index|[]
name|itemNames
init|=
name|ScrServiceMBean
operator|.
name|REFERENCE
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
name|BOOLEAN
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
name|itemTypes
index|[
literal|3
index|]
operator|=
name|SimpleType
operator|.
name|STRING
expr_stmt|;
name|itemTypes
index|[
literal|4
index|]
operator|=
name|SimpleType
operator|.
name|STRING
expr_stmt|;
name|itemTypes
index|[
literal|5
index|]
operator|=
operator|new
name|ArrayType
argument_list|(
literal|1
argument_list|,
name|SimpleType
operator|.
name|STRING
argument_list|)
expr_stmt|;
name|itemDescriptions
index|[
literal|0
index|]
operator|=
literal|"The name of the reference"
expr_stmt|;
name|itemDescriptions
index|[
literal|1
index|]
operator|=
literal|"The state of the reference"
expr_stmt|;
name|itemDescriptions
index|[
literal|2
index|]
operator|=
literal|"The cardinality of the reference"
expr_stmt|;
name|itemDescriptions
index|[
literal|3
index|]
operator|=
literal|"The availability of the reference"
expr_stmt|;
name|itemDescriptions
index|[
literal|4
index|]
operator|=
literal|"The policy of the reference"
expr_stmt|;
name|itemDescriptions
index|[
literal|5
index|]
operator|=
literal|"The bound services"
expr_stmt|;
return|return
operator|new
name|CompositeType
argument_list|(
literal|"Reference"
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
literal|"Unable to build reference type"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|TabularType
name|createReferenceTableType
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
literal|"The table of all references"
argument_list|,
name|REFERENCE
argument_list|,
name|ScrServiceMBean
operator|.
name|REFERENCE
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
literal|"Unable to build references table type"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns a literal for the {@link Reference} cardinality.      * @param reference     The target {@link Reference}.      * @return              "Multiple" or "Single".      */
specifier|private
specifier|static
name|String
name|getCardinality
parameter_list|(
name|Reference
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|.
name|isMultiple
argument_list|()
condition|)
block|{
return|return
name|ScrServiceMBean
operator|.
name|REFERENCE_CARDINALITY_MULTIPLE
return|;
block|}
else|else
block|{
return|return
name|ScrServiceMBean
operator|.
name|REFERENCE_CARDINALITY_SINGLE
return|;
block|}
block|}
comment|/**      * Returns a literal for the {@link Reference} availability.      * @param reference     The target {@link Reference}.      * @return              "Mandatory" or "Optional".      */
specifier|private
specifier|static
name|String
name|getAvailability
parameter_list|(
name|Reference
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|.
name|isOptional
argument_list|()
condition|)
block|{
return|return
name|ScrServiceMBean
operator|.
name|REFERENCE_AVAILABILITY_OPTIONAL
return|;
block|}
else|else
block|{
return|return
name|ScrServiceMBean
operator|.
name|REFERENCE_AVAILABILITY_MANDATORY
return|;
block|}
block|}
comment|/**      * Returns a literal for the {@link Reference} policy.      * @param reference     The target {@link Reference}.      * @return              "Static" or "Dynamic".      */
specifier|private
specifier|static
name|String
name|getPolicy
parameter_list|(
name|Reference
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|.
name|isStatic
argument_list|()
condition|)
block|{
return|return
name|ScrServiceMBean
operator|.
name|REFERENCE_POLICY_STATIC
return|;
block|}
else|else
block|{
return|return
name|ScrServiceMBean
operator|.
name|REFERENCE_POLICY_DYNAMIC
return|;
block|}
block|}
comment|/**      * Returns The bound service ids.      * @param reference     The target {@link Reference}.      * @return      */
specifier|private
specifier|static
name|String
index|[]
name|getBoundServices
parameter_list|(
name|Reference
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|.
name|getBoundServiceReferences
argument_list|()
operator|==
literal|null
operator|||
name|reference
operator|.
name|getBoundServiceReferences
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
else|else
block|{
name|String
index|[]
name|ids
init|=
operator|new
name|String
index|[
name|reference
operator|.
name|getBoundServiceReferences
argument_list|()
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
name|reference
operator|.
name|getBoundServiceReferences
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ids
index|[
name|i
index|]
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|reference
operator|.
name|getBoundServiceReferences
argument_list|()
index|[
name|i
index|]
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ids
return|;
block|}
block|}
block|}
end_class

end_unit

