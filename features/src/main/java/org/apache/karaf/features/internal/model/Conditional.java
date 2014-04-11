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
name|Iterator
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
name|XmlType
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
literal|"conditional"
argument_list|,
name|propOrder
operator|=
block|{
literal|"condition"
block|,
literal|"config"
block|,
literal|"configfile"
block|,
literal|"feature"
block|,
literal|"bundle"
block|}
argument_list|)
specifier|public
class|class
name|Conditional
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
name|Conditional
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"condition"
argument_list|)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|condition
decl_stmt|;
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getCondition
parameter_list|()
block|{
if|if
condition|(
name|condition
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|condition
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|condition
return|;
block|}
annotation|@
name|Override
specifier|public
name|Feature
name|asFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|String
name|conditionName
init|=
name|name
operator|+
literal|"-condition-"
operator|+
name|getConditionId
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"[^A-Za-z0-9 ]"
argument_list|,
literal|"_"
argument_list|)
decl_stmt|;
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
operator|.
name|Feature
name|f
init|=
operator|new
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
operator|.
name|Feature
argument_list|(
name|conditionName
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|f
operator|.
name|getBundle
argument_list|()
operator|.
name|addAll
argument_list|(
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
name|f
operator|.
name|getConfig
argument_list|()
operator|.
name|addAll
argument_list|(
name|getConfig
argument_list|()
argument_list|)
expr_stmt|;
name|f
operator|.
name|getConfigfile
argument_list|()
operator|.
name|addAll
argument_list|(
name|getConfigfile
argument_list|()
argument_list|)
expr_stmt|;
name|f
operator|.
name|getFeature
argument_list|()
operator|.
name|addAll
argument_list|(
name|getFeature
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|f
return|;
block|}
specifier|private
name|String
name|getConditionId
parameter_list|()
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|cond
range|:
name|getCondition
argument_list|()
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"_"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|cond
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

