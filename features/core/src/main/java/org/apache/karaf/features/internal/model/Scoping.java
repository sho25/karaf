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
name|XmlType
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
literal|"scoping"
argument_list|,
name|propOrder
operator|=
block|{
literal|"imports"
block|,
literal|"exports"
block|}
argument_list|)
specifier|public
class|class
name|Scoping
implements|implements
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Scoping
block|{
annotation|@
name|XmlAttribute
name|boolean
name|acceptDependencies
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"import"
argument_list|)
name|List
argument_list|<
name|ScopeFilter
argument_list|>
name|imports
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"export"
argument_list|)
name|List
argument_list|<
name|ScopeFilter
argument_list|>
name|exports
decl_stmt|;
specifier|public
name|List
argument_list|<
name|ScopeFilter
argument_list|>
name|getImport
parameter_list|()
block|{
if|if
condition|(
name|imports
operator|==
literal|null
condition|)
block|{
name|imports
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|imports
return|;
block|}
specifier|public
name|List
argument_list|<
name|ScopeFilter
argument_list|>
name|getExport
parameter_list|()
block|{
if|if
condition|(
name|exports
operator|==
literal|null
condition|)
block|{
name|exports
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|exports
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|acceptDependencies
parameter_list|()
block|{
return|return
name|acceptDependencies
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|ScopeFilter
argument_list|>
name|getImports
parameter_list|()
block|{
return|return
name|getImport
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|ScopeFilter
argument_list|>
name|getExports
parameter_list|()
block|{
return|return
name|getExport
argument_list|()
return|;
block|}
block|}
end_class

end_unit

