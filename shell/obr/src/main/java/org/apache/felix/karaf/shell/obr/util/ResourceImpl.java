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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|obr
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|obr
operator|.
name|Capability
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|obr
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|obr
operator|.
name|Requirement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|obr
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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

begin_comment
comment|/**  * @version $Rev$ $Date$  */
end_comment

begin_class
specifier|public
class|class
name|ResourceImpl
implements|implements
name|Resource
block|{
specifier|private
specifier|final
name|Map
name|properties
decl_stmt|;
specifier|private
specifier|final
name|String
name|symbolicName
decl_stmt|;
specifier|private
specifier|final
name|String
name|presentationName
decl_stmt|;
specifier|private
specifier|final
name|Version
name|version
decl_stmt|;
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
specifier|private
specifier|final
name|URL
name|url
decl_stmt|;
specifier|private
specifier|final
name|Requirement
index|[]
name|requirements
decl_stmt|;
specifier|private
specifier|final
name|Capability
index|[]
name|capabilities
decl_stmt|;
specifier|private
specifier|final
name|String
index|[]
name|categories
decl_stmt|;
specifier|private
specifier|final
name|Repository
name|repository
decl_stmt|;
specifier|public
name|ResourceImpl
parameter_list|(
name|Map
name|properties
parameter_list|,
name|String
name|symbolicName
parameter_list|,
name|String
name|presentationName
parameter_list|,
name|Version
name|version
parameter_list|,
name|String
name|id
parameter_list|,
name|URL
name|url
parameter_list|,
name|Requirement
index|[]
name|requirements
parameter_list|,
name|Capability
index|[]
name|capabilities
parameter_list|,
name|String
index|[]
name|categories
parameter_list|,
name|Repository
name|repository
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
name|this
operator|.
name|symbolicName
operator|=
name|symbolicName
expr_stmt|;
name|this
operator|.
name|presentationName
operator|=
name|presentationName
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|requirements
operator|=
name|requirements
expr_stmt|;
name|this
operator|.
name|capabilities
operator|=
name|capabilities
expr_stmt|;
name|this
operator|.
name|categories
operator|=
name|categories
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
block|}
specifier|public
name|Map
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
specifier|public
name|String
name|getSymbolicName
parameter_list|()
block|{
return|return
name|symbolicName
return|;
block|}
specifier|public
name|String
name|getPresentationName
parameter_list|()
block|{
return|return
name|presentationName
return|;
block|}
specifier|public
name|Version
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|URL
name|getURL
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|Requirement
index|[]
name|getRequirements
parameter_list|()
block|{
return|return
name|requirements
return|;
block|}
specifier|public
name|Capability
index|[]
name|getCapabilities
parameter_list|()
block|{
return|return
name|capabilities
return|;
block|}
specifier|public
name|String
index|[]
name|getCategories
parameter_list|()
block|{
return|return
name|categories
return|;
block|}
specifier|public
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
block|}
end_class

end_unit

