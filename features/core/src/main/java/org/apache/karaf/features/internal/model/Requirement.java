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
comment|/**  *   * Additional requirement for a feature.  *               *   *<p>Java class for bundle complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType name="capability">  *&lt;simpleContent>  *&lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">  *&lt;/extension>  *&lt;/simpleContent>  *&lt;/complexType>  *</pre>  *   *   */
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
literal|"requirement"
argument_list|,
name|propOrder
operator|=
block|{
literal|"value"
block|}
argument_list|)
specifier|public
class|class
name|Requirement
implements|implements
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Requirement
block|{
annotation|@
name|XmlValue
specifier|protected
name|String
name|value
decl_stmt|;
specifier|public
name|Requirement
parameter_list|()
block|{     }
specifier|public
name|Requirement
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
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
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
name|Requirement
name|bundle
init|=
operator|(
name|Requirement
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|?
operator|!
name|value
operator|.
name|equals
argument_list|(
name|bundle
operator|.
name|value
argument_list|)
else|:
name|bundle
operator|.
name|value
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
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|hashCode
argument_list|()
else|:
literal|0
decl_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

