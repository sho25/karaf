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

begin_comment
comment|/**  *<p>Dependency of feature.</p>  *<p>Java class for dependency complex type.</p>  *<p>The following schema fragment specifies the expected content contained within this class.</p>  *<pre>  *&lt;complexType name="dependency"&gt;  *&lt;simpleContent&gt;  *&lt;extension base="&lt;http://karaf.apache.org/xmlns/features/v1.0.0&gt;featureName"&gt;  *&lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" default="0.0.0" /&gt;  *&lt;/extension&gt;  *&lt;/simpleContent&gt;  *&lt;/complexType&gt;  *</pre>  */
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
literal|"dependency"
argument_list|,
name|propOrder
operator|=
block|{
literal|"name"
block|}
argument_list|)
specifier|public
class|class
name|Dependency
implements|implements
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Dependency
block|{
annotation|@
name|XmlValue
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
name|boolean
name|prerequisite
decl_stmt|;
annotation|@
name|XmlAttribute
specifier|protected
name|boolean
name|dependency
decl_stmt|;
specifier|public
name|Dependency
parameter_list|()
block|{
comment|// Nothing to do
block|}
specifier|public
name|Dependency
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
comment|/**      * Feature name should be non empty string.      *      * @return possible object is      * {@link String }      */
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Sets the value of the value property.      *      * @param value allowed object is      *              {@link String }      */
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
comment|/**      * Gets the value of the version property.      *      * @return possible object is      * {@link String }      */
annotation|@
name|Override
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
name|Feature
operator|.
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
comment|/**      * Sets the value of the version property.      *      * @param value allowed object is      *              {@link String }      */
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
comment|/**      * Since version has a default value ("0.0.0"), returns whether or not the version has been set.      */
annotation|@
name|Override
specifier|public
name|boolean
name|hasVersion
parameter_list|()
block|{
return|return
name|version
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isPrerequisite
parameter_list|()
block|{
return|return
name|prerequisite
return|;
block|}
specifier|public
name|void
name|setPrerequisite
parameter_list|(
name|boolean
name|prerequisite
parameter_list|)
block|{
name|this
operator|.
name|prerequisite
operator|=
name|prerequisite
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDependency
parameter_list|()
block|{
return|return
name|dependency
return|;
block|}
specifier|public
name|void
name|setDependency
parameter_list|(
name|boolean
name|dependency
parameter_list|)
block|{
name|this
operator|.
name|dependency
operator|=
name|dependency
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|+
name|Feature
operator|.
name|VERSION_SEPARATOR
operator|+
name|getVersion
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
name|Dependency
name|that
init|=
operator|(
name|Dependency
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|prerequisite
operator|!=
name|that
operator|.
name|prerequisite
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|dependency
operator|!=
name|that
operator|.
name|dependency
condition|)
return|return
literal|false
return|;
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
name|that
operator|.
name|name
argument_list|)
else|:
name|that
operator|.
name|name
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
return|return
name|version
operator|!=
literal|null
condition|?
name|version
operator|.
name|equals
argument_list|(
name|that
operator|.
name|version
argument_list|)
else|:
name|that
operator|.
name|version
operator|==
literal|null
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
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|prerequisite
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|dependency
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

