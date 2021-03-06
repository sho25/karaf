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
name|docker
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_comment
comment|/**  * Represents a Docker image search result.  */
end_comment

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
specifier|public
class|class
name|ImageSearch
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"name"
argument_list|)
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"star_count"
argument_list|)
specifier|private
name|int
name|starCount
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"is_official"
argument_list|)
specifier|private
name|boolean
name|official
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"is_automated"
argument_list|)
specifier|private
name|boolean
name|automated
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"description"
argument_list|)
specifier|private
name|String
name|description
decl_stmt|;
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|int
name|getStarCount
parameter_list|()
block|{
return|return
name|starCount
return|;
block|}
specifier|public
name|boolean
name|isOfficial
parameter_list|()
block|{
return|return
name|official
return|;
block|}
specifier|public
name|boolean
name|isAutomated
parameter_list|()
block|{
return|return
name|automated
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
block|}
end_class

end_unit

