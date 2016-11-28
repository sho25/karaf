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
name|Collections
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|XmlElement
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
name|XmlTransient
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|version
operator|.
name|VersionCleaner
import|;
end_import

begin_comment
comment|/**  *<p>Definition of the Feature.</p>  *<p>Java class for feature complex type.</p>  *<p>The following schema fragment specifies the expected content contained within this class.</p>  *<pre>  *&lt;complexType name="feature"&gt;  *&lt;complexContent&gt;  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;  *&lt;sequence&gt;  *&lt;element name="details" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;  *&lt;element name="config" type="{http://karaf.apache.org/xmlns/features/v1.0.0}config" maxOccurs="unbounded" minOccurs="0"/&gt;  *&lt;element name="configfile" type="{http://karaf.apache.org/xmlns/features/v1.0.0}configFile" maxOccurs="unbounded" minOccurs="0"/&gt;  *&lt;element name="feature" type="{http://karaf.apache.org/xmlns/features/v1.0.0}dependency" maxOccurs="unbounded" minOccurs="0"/&gt;  *&lt;element name="bundle" type="{http://karaf.apache.org/xmlns/features/v1.0.0}bundle" maxOccurs="unbounded" minOccurs="0"/&gt;  *&lt;element name="conditional" type="{http://karaf.apache.org/xmlns/features/v1.0.0}conditional" maxOccurs="unbounded" minOccurs="0"/&gt;  *&lt;element name="capability" type="{http://karaf.apache.org/xmlns/features/v1.0.0}capability" maxOccurs="unbounded" minOccurs="0"/&gt;  *&lt;element name="requirement" type="{http://karaf.apache.org/xmlns/features/v1.0.0}requirement" maxOccurs="unbounded" minOccurs="0"/&gt;  *&lt;/sequence&gt;  *&lt;attribute name="name" use="required" type="{http://karaf.apache.org/xmlns/features/v1.0.0}featureName" /&gt;  *&lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" default="0.0.0" /&gt;  *&lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;  *&lt;attribute name="resolver" type="{http://karaf.apache.org/xmlns/features/v1.0.0}resolver" /&gt;  *&lt;/restriction&gt;  *&lt;/complexContent&gt;  *&lt;/complexType&gt;  *</pre>  */
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
literal|"library"
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
specifier|final
name|String
name|VERSION_SEPARATOR
init|=
literal|"/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_VERSION
init|=
literal|"0.0.0"
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"details"
argument_list|,
name|namespace
operator|=
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesNamespaces
operator|.
name|URI_CURRENT
argument_list|)
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
name|XmlTransient
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
annotation|@
name|XmlAttribute
specifier|protected
name|Boolean
name|hidden
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"conditional"
argument_list|,
name|namespace
operator|=
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesNamespaces
operator|.
name|URI_CURRENT
argument_list|)
specifier|protected
name|List
argument_list|<
name|Conditional
argument_list|>
name|conditional
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"capability"
argument_list|,
name|namespace
operator|=
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesNamespaces
operator|.
name|URI_CURRENT
argument_list|)
specifier|protected
name|List
argument_list|<
name|Capability
argument_list|>
name|capability
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"requirement"
argument_list|,
name|namespace
operator|=
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesNamespaces
operator|.
name|URI_CURRENT
argument_list|)
specifier|protected
name|List
argument_list|<
name|Requirement
argument_list|>
name|requirement
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"scoping"
argument_list|,
name|namespace
operator|=
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesNamespaces
operator|.
name|URI_CURRENT
argument_list|)
specifier|protected
name|Scoping
name|scoping
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"library"
argument_list|,
name|namespace
operator|=
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesNamespaces
operator|.
name|URI_CURRENT
argument_list|)
specifier|protected
name|List
argument_list|<
name|Library
argument_list|>
name|library
decl_stmt|;
annotation|@
name|XmlTransient
specifier|protected
name|String
name|namespace
decl_stmt|;
annotation|@
name|XmlTransient
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|resourceRepositories
decl_stmt|;
annotation|@
name|XmlTransient
specifier|protected
name|String
name|repositoryUrl
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
name|VersionCleaner
operator|.
name|clean
argument_list|(
name|version
argument_list|)
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
name|VERSION_SEPARATOR
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
name|VERSION_SEPARATOR
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
name|VERSION_SEPARATOR
argument_list|)
operator|+
name|VERSION_SEPARATOR
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
name|VERSION_SEPARATOR
operator|+
name|getVersion
argument_list|()
return|;
block|}
comment|/**      * Get the value of the name property.      *      * @return possible object is {@link String}      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Set the value of the name property.      *      * @param value allowed object is {@link String}      */
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
comment|/**      * Get the value of the version property.      *      * @return possible object is {@link String}      */
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
comment|/**      * Set the value of the version property.      *      * @param value allowed object is {@link String}      */
annotation|@
name|XmlAttribute
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
name|VersionCleaner
operator|.
name|clean
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Since version has a default value ("0.0.0"), returns      * whether or not the version has been set.      *      * @return true if the feature has a version, false else.      */
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
comment|/**      * Get the value of the description property.      *      * @return possible object is {@link String}.      */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
comment|/**      * Set the value of the description property.      *      * @param value allowed object is {@link String}.      */
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
comment|/**      * Get the feature details.      *      * @return the feature details.      */
specifier|public
name|String
name|getDetails
parameter_list|()
block|{
return|return
name|details
return|;
block|}
comment|/**      * Set the feature details.      *      * @param details the feature details.      */
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
comment|/**      * Get the value of the resolver property.      *      * @return possible object is {@link String}.      */
specifier|public
name|String
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
comment|/**      * Get the feature install flag.      *      * @return the feature install flags.      */
specifier|public
name|String
name|getInstall
parameter_list|()
block|{
return|return
name|install
return|;
block|}
comment|/**      * Set the feature install flag.      *      * @param install the feature install flag.      */
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
comment|/**      * Set the value of the resolver property.      *      * @param value allowed object is {@link String}.      */
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
comment|/**      * Get the value of the startLevel property.      *      * @return possible object is {@link Integer}.      */
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
comment|/**      * Set the value of the startLevel property.      *      * @param value allowed object is {@link Integer}.      */
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
comment|/**      * Get the value of the hidden property.      *      * @return true if the feature is hidden, false else.      */
specifier|public
name|boolean
name|isHidden
parameter_list|()
block|{
return|return
name|hidden
operator|==
literal|null
condition|?
literal|false
else|:
name|hidden
return|;
block|}
comment|/**      * Set the value of the hidden property.      *      * @param value true to set the feature as hidden, false else.      */
specifier|public
name|void
name|setHidden
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|hidden
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the value of the conditional property.      *      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the feature property.      *      * For example, to add a new item, do as follows:      *      *<pre>      *    getConditionals().add(newItem);      *</pre>      *      * Objects of the following type(s) are allowed in the list      * {@link Conditional}.      *      * @return the list of feature conditions.      */
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
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|conditional
return|;
block|}
comment|/**      * Get the feature capabilities.      *      * @return the feature capabilities as a {@link List}..      */
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
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|capability
return|;
block|}
comment|/**      * Get the feature requirements.      *      * @return the feature requirements as a {@link List}.      */
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
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|requirement
return|;
block|}
comment|/**      * Get the feature scoping.      *      * @return the feature scoping.      */
specifier|public
name|Scoping
name|getScoping
parameter_list|()
block|{
return|return
name|scoping
return|;
block|}
comment|/**      * Set the feature scoping.      *      * @param scoping the feature scoping.      */
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
comment|/**      * Return a string representation of the feature.      *      * @return the feature string representation.      */
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
block|{
return|return
literal|true
return|;
block|}
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
block|{
return|return
literal|false
return|;
block|}
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
block|{
return|return
literal|false
return|;
block|}
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
block|{
return|return
literal|false
return|;
block|}
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
specifier|public
name|List
argument_list|<
name|Library
argument_list|>
name|getLibraries
parameter_list|()
block|{
if|if
condition|(
name|library
operator|==
literal|null
condition|)
block|{
name|library
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|library
return|;
block|}
specifier|public
name|void
name|postUnmarshall
parameter_list|()
block|{
if|if
condition|(
name|conditional
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Conditional
name|c
range|:
name|conditional
control|)
block|{
name|c
operator|.
name|setOwner
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Config
name|c
range|:
name|config
control|)
block|{
name|String
name|v
init|=
name|c
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|v
operator|=
name|Stream
operator|.
name|of
argument_list|(
name|v
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|String
operator|::
name|trim
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|"\n"
argument_list|,
literal|""
argument_list|,
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|setValue
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getResourceRepositories
parameter_list|()
block|{
return|return
name|resourceRepositories
operator|!=
literal|null
condition|?
name|resourceRepositories
else|:
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|void
name|setResourceRepositories
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|resourceRepositories
parameter_list|)
block|{
name|this
operator|.
name|resourceRepositories
operator|=
name|resourceRepositories
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRepositoryUrl
parameter_list|()
block|{
return|return
name|repositoryUrl
return|;
block|}
specifier|public
name|void
name|setRepositoryUrl
parameter_list|(
name|String
name|repositoryUrl
parameter_list|)
block|{
name|this
operator|.
name|repositoryUrl
operator|=
name|repositoryUrl
expr_stmt|;
block|}
block|}
end_class

end_unit

