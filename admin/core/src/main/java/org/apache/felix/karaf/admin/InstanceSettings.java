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
name|felix
operator|.
name|karaf
operator|.
name|admin
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|InstanceSettings
block|{
specifier|private
specifier|final
name|int
name|port
decl_stmt|;
specifier|private
specifier|final
name|String
name|location
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|featureURLs
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|features
decl_stmt|;
specifier|public
name|InstanceSettings
parameter_list|(
name|int
name|port
parameter_list|,
name|String
name|location
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|featureURLs
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|features
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
name|this
operator|.
name|featureURLs
operator|=
name|featureURLs
expr_stmt|;
name|this
operator|.
name|features
operator|=
name|features
expr_stmt|;
block|}
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|location
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getFeatureURLs
parameter_list|()
block|{
return|return
name|featureURLs
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getFeatures
parameter_list|()
block|{
return|return
name|features
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
name|o
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|InstanceSettings
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|InstanceSettings
name|is
init|=
operator|(
name|InstanceSettings
operator|)
name|o
decl_stmt|;
return|return
name|is
operator|.
name|port
operator|==
name|port
operator|&&
operator|(
name|location
operator|==
literal|null
condition|?
name|is
operator|.
name|location
operator|==
literal|null
else|:
name|location
operator|.
name|equals
argument_list|(
name|is
operator|.
name|location
argument_list|)
operator|)
operator|&&
operator|(
name|featureURLs
operator|==
literal|null
condition|?
name|is
operator|.
name|featureURLs
operator|==
literal|null
else|:
name|featureURLs
operator|.
name|equals
argument_list|(
name|is
operator|.
name|featureURLs
argument_list|)
operator|)
operator|&&
operator|(
name|features
operator|==
literal|null
condition|?
name|is
operator|.
name|features
operator|==
literal|null
else|:
name|features
operator|.
name|equals
argument_list|(
name|is
operator|.
name|features
argument_list|)
operator|)
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
name|rc
init|=
literal|17
decl_stmt|;
name|rc
operator|=
literal|37
operator|*
name|port
expr_stmt|;
if|if
condition|(
name|location
operator|!=
literal|null
condition|)
block|{
name|rc
operator|=
literal|37
operator|*
name|location
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|featureURLs
operator|!=
literal|null
condition|)
block|{
name|rc
operator|=
literal|37
operator|*
name|featureURLs
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|features
operator|!=
literal|null
condition|)
block|{
name|rc
operator|=
literal|37
operator|*
name|features
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|rc
return|;
block|}
block|}
end_class

end_unit

