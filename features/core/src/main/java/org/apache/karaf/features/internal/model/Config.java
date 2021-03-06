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
name|internal
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|annotation
operator|.
name|XmlAccessType
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
name|annotation
operator|.
name|XmlAccessorType
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
name|annotation
operator|.
name|XmlAttribute
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
name|annotation
operator|.
name|XmlType
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
name|annotation
operator|.
name|XmlValue
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
name|ConfigInfo
import|;
end_import

begin_comment
comment|/**  *<p>Configuration entries which should be created during feature installation. This configuration may be used with OSGi Configuration Admin.</p>  *<p>Java class for config complex type.</p>  *<p>The following schema fragment specifies the expected content contained within this class.</p>  *<pre>  *&lt;complexType name="config"&gt;  *&lt;simpleContent&gt;  *&lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;  *&lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;  *&lt;/extension&gt;  *&lt;/simpleContent&gt;  *&lt;/complexType&gt;  *</pre>  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"config"
argument_list|,
name|propOrder
operator|=
block|{
literal|"value"
block|}
argument_list|)
specifier|public
class|class
name|Config
implements|implements
name|ConfigInfo
block|{
annotation|@
name|XmlValue
specifier|protected
name|String
name|value
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
specifier|protected
name|String
name|name
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
specifier|private
name|Boolean
name|append
init|=
literal|false
decl_stmt|;
annotation|@
name|XmlAttribute
specifier|private
name|Boolean
name|external
init|=
literal|false
decl_stmt|;
annotation|@
name|XmlAttribute
specifier|private
name|Boolean
name|override
init|=
literal|false
decl_stmt|;
comment|/**      * Gets the value of the value property.      *      * @return possible object is      * {@link String }      */
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * Sets the value of the value property.      *      * @param value allowed object is      *              {@link String }      */
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the name property.      *      * @return possible object is      * {@link String }      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Sets the value of the name property.      *      * @param value allowed object is      *              {@link String }      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|value
expr_stmt|;
block|}
comment|/** 	 * @return the append 	 */
specifier|public
name|boolean
name|isAppend
parameter_list|()
block|{
return|return
name|append
return|;
block|}
comment|/** 	 * @param append the append to set 	 */
specifier|public
name|void
name|setAppend
parameter_list|(
name|boolean
name|append
parameter_list|)
block|{
name|this
operator|.
name|append
operator|=
name|append
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExternal
parameter_list|()
block|{
return|return
name|external
return|;
block|}
specifier|public
name|void
name|setExternal
parameter_list|(
name|boolean
name|external
parameter_list|)
block|{
name|this
operator|.
name|external
operator|=
name|external
expr_stmt|;
block|}
comment|/** 	 * Gets the value of the override property. 	 * 	 * @return possible object is 	 * {@link Boolean } 	 */
specifier|public
name|boolean
name|isOverride
parameter_list|()
block|{
return|return
name|override
operator|==
literal|null
condition|?
literal|false
else|:
name|override
return|;
block|}
comment|/** 	 * Sets the value of the override property. 	 * 	 * @param value allowed object is 	 *              {@link Boolean } 	 */
specifier|public
name|void
name|setOverride
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|override
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|properties
operator|.
name|Properties
name|properties
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|properties
operator|.
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
operator|new
name|StringReader
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|props
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore??
block|}
return|return
name|props
return|;
block|}
block|}
end_class

end_unit

