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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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

begin_comment
comment|/**  *   * Definition of the Feature.  *               *   *<p>Java class for feature complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType name="feature">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="details" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>  *&lt;element name="config" type="{http://karaf.apache.org/xmlns/features/v1.0.0}config" maxOccurs="unbounded" minOccurs="0"/>  *&lt;element name="configfile" type="{http://karaf.apache.org/xmlns/features/v1.0.0}configFile" maxOccurs="unbounded" minOccurs="0"/>  *&lt;element name="feature" type="{http://karaf.apache.org/xmlns/features/v1.0.0}dependency" maxOccurs="unbounded" minOccurs="0"/>  *&lt;element name="bundle" type="{http://karaf.apache.org/xmlns/features/v1.0.0}bundle" maxOccurs="unbounded" minOccurs="0"/>  *&lt;element name="conditional" type="{http://karaf.apache.org/xmlns/features/v1.0.0}conditional" maxOccurs="unbounded" minOccurs="0"/>  *&lt;element name="capability" type="{http://karaf.apache.org/xmlns/features/v1.0.0}capability" maxOccurs="unbounded" minOccurs="0"/>  *&lt;element name="requirement" type="{http://karaf.apache.org/xmlns/features/v1.0.0}requirement" maxOccurs="unbounded" minOccurs="0"/>  *&lt;/sequence>  *&lt;attribute name="name" use="required" type="{http://karaf.apache.org/xmlns/features/v1.0.0}featureName" />  *&lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" default="0.0.0" />  *&lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />  *&lt;attribute name="resolver" type="{http://karaf.apache.org/xmlns/features/v1.0.0}resolver" />  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  *   *   */
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
literal|"feature"
argument_list|,
name|propOrder
operator|=
block|{
literal|"details"
block|,
literal|"config"
block|,
literal|"configfile"
block|,
literal|"feature"
block|,
literal|"bundle"
block|,
literal|"conditional"
block|,
literal|"capability"
block|,
literal|"requirement"
block|,
literal|"scoping"
block|}
argument_list|)
specifier|public
class|class
name|Feature
extends|extends
name|Content
implements|implements
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Feature
block|{
specifier|public
specifier|static
name|String
name|SPLIT_FOR_NAME_AND_VERSION
init|=
literal|"/"
decl_stmt|;
specifier|public
specifier|static
name|String
name|DEFAULT_VERSION
init|=
literal|"0.0.0"
decl_stmt|;
specifier|protected
name|String
name|details
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
specifier|protected
name|String
name|version
decl_stmt|;
annotation|@
name|XmlAttribute
specifier|protected
name|String
name|description
decl_stmt|;
annotation|@
name|XmlAttribute
specifier|protected
name|String
name|resolver
decl_stmt|;
annotation|@
name|XmlAttribute
specifier|protected
name|String
name|install
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"start-level"
argument_list|)
specifier|protected
name|Integer
name|startLevel
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Conditional
argument_list|>
name|conditional
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Capability
argument_list|>
name|capability
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Requirement
argument_list|>
name|requirement
decl_stmt|;
specifier|protected
name|Scoping
name|scoping
decl_stmt|;
specifier|public
name|Feature
parameter_list|()
block|{     }
specifier|public
name|Feature
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Feature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Feature
name|valueOf
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|.
name|contains
argument_list|(
name|SPLIT_FOR_NAME_AND_VERSION
argument_list|)
condition|)
block|{
name|String
name|strName
init|=
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
name|SPLIT_FOR_NAME_AND_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|strVersion
init|=
name|str
operator|.
name|substring
argument_list|(
name|str
operator|.
name|indexOf
argument_list|(
name|SPLIT_FOR_NAME_AND_VERSION
argument_list|)
operator|+
name|SPLIT_FOR_NAME_AND_VERSION
operator|.
name|length
argument_list|()
argument_list|,
name|str
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|Feature
argument_list|(
name|strName
argument_list|,
name|strVersion
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|Feature
argument_list|(
name|str
argument_list|)
return|;
block|}
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|+
name|SPLIT_FOR_NAME_AND_VERSION
operator|+
name|getVersion
argument_list|()
return|;
block|}
comment|/**      * Gets the value of the name property.      *       * @return      *     possible object is      *     {@link String }      *           */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Sets the value of the name property.      *       * @param value      *     allowed object is      *     {@link String }      *           */
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
comment|/**      * Gets the value of the version property.      *       * @return      *     possible object is      *     {@link String }      *           */
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
return|return
name|DEFAULT_VERSION
return|;
block|}
else|else
block|{
return|return
name|version
return|;
block|}
block|}
comment|/**      * Sets the value of the version property.      *       * @param value      *     allowed object is      *     {@link String }      *           */
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Since version has a default value ("0.0.0"), returns      * whether or not the version has been set.      */
specifier|public
name|boolean
name|hasVersion
parameter_list|()
block|{
return|return
name|this
operator|.
name|version
operator|!=
literal|null
return|;
block|}
comment|/**      * Gets the value of the description property.      *       * @return      *     possible object is      *     {@link String }      *           */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
comment|/**      * Sets the value of the description property.      *       * @param value      *     allowed object is      *     {@link String }      *           */
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|String
name|getDetails
parameter_list|()
block|{
return|return
name|details
return|;
block|}
specifier|public
name|void
name|setDetails
parameter_list|(
name|String
name|details
parameter_list|)
block|{
name|this
operator|.
name|details
operator|=
name|details
expr_stmt|;
block|}
comment|/**      * Gets the value of the resolver property.      *       * @return      *     possible object is      *     {@link String }      *           */
specifier|public
name|String
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
specifier|public
name|String
name|getInstall
parameter_list|()
block|{
return|return
name|install
return|;
block|}
specifier|public
name|void
name|setInstall
parameter_list|(
name|String
name|install
parameter_list|)
block|{
name|this
operator|.
name|install
operator|=
name|install
expr_stmt|;
block|}
comment|/**      * Sets the value of the resolver property.      *       * @param value      *     allowed object is      *     {@link String }      *           */
specifier|public
name|void
name|setResolver
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the startLevel property.      *       * @return      *     possible object is      *     {@link Integer }      *           */
specifier|public
name|int
name|getStartLevel
parameter_list|()
block|{
return|return
name|startLevel
operator|==
literal|null
condition|?
literal|0
else|:
name|startLevel
return|;
block|}
comment|/**      * Sets the value of the startLevel property.      *       * @param value      *     allowed object is      *     {@link Integer }      *           */
specifier|public
name|void
name|setStartLevel
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|startLevel
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the conditional property.      *<p/>      *<p/>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the feature property.      *<p/>      *<p/>      * For example, to add a new item, do as follows:      *<pre>      *    getConditionals().add(newItem);      *</pre>      *<p/>      *<p/>      *<p/>      * Objects of the following type(s) are allowed in the list      * {@link Conditional }      */
specifier|public
name|List
argument_list|<
name|Conditional
argument_list|>
name|getConditional
parameter_list|()
block|{
if|if
condition|(
name|conditional
operator|==
literal|null
condition|)
block|{
name|conditional
operator|=
operator|new
name|ArrayList
argument_list|<
name|Conditional
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|conditional
return|;
block|}
specifier|public
name|List
argument_list|<
name|Capability
argument_list|>
name|getCapabilities
parameter_list|()
block|{
if|if
condition|(
name|capability
operator|==
literal|null
condition|)
block|{
name|capability
operator|=
operator|new
name|ArrayList
argument_list|<
name|Capability
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|capability
return|;
block|}
specifier|public
name|List
argument_list|<
name|Requirement
argument_list|>
name|getRequirements
parameter_list|()
block|{
if|if
condition|(
name|requirement
operator|==
literal|null
condition|)
block|{
name|requirement
operator|=
operator|new
name|ArrayList
argument_list|<
name|Requirement
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|requirement
return|;
block|}
specifier|public
name|Scoping
name|getScoping
parameter_list|()
block|{
return|return
name|scoping
return|;
block|}
specifier|public
name|void
name|setScoping
parameter_list|(
name|Scoping
name|scoping
parameter_list|)
block|{
name|this
operator|.
name|scoping
operator|=
name|scoping
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getId
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|Feature
name|feature
init|=
operator|(
name|Feature
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|?
operator|!
name|name
operator|.
name|equals
argument_list|(
name|feature
operator|.
name|name
argument_list|)
else|:
name|feature
operator|.
name|name
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|version
operator|!=
literal|null
condition|?
operator|!
name|version
operator|.
name|equals
argument_list|(
name|feature
operator|.
name|version
argument_list|)
else|:
name|feature
operator|.
name|version
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|name
operator|!=
literal|null
condition|?
name|name
operator|.
name|hashCode
argument_list|()
else|:
literal|0
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|version
operator|!=
literal|null
condition|?
name|version
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|protected
name|void
name|interpolation
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
for|for
control|(
name|Enumeration
name|e
init|=
name|properties
operator|.
name|propertyNames
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|val
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\$\\{([^}]+)\\}"
argument_list|)
operator|.
name|matcher
argument_list|(
name|val
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|rep
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rep
operator|!=
literal|null
condition|)
block|{
name|val
operator|=
name|val
operator|.
name|replace
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|0
argument_list|)
argument_list|,
name|rep
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|reset
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
