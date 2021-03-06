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
name|http
operator|.
name|core
package|;
end_package

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|WebEvent
import|;
end_import

begin_class
specifier|public
class|class
name|ServletInfo
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|long
name|bundleId
decl_stmt|;
specifier|private
name|String
name|className
decl_stmt|;
specifier|private
name|String
name|alias
decl_stmt|;
specifier|private
name|int
name|state
decl_stmt|;
specifier|private
name|String
index|[]
name|urls
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
name|void
name|setName
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
name|long
name|getBundleId
parameter_list|()
block|{
return|return
name|bundleId
return|;
block|}
specifier|public
name|void
name|setBundleId
parameter_list|(
name|long
name|bundleId
parameter_list|)
block|{
name|this
operator|.
name|bundleId
operator|=
name|bundleId
expr_stmt|;
block|}
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|className
return|;
block|}
specifier|public
name|void
name|setClassName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
block|}
specifier|public
name|int
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
specifier|public
name|void
name|setState
parameter_list|(
name|int
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
specifier|public
name|String
name|getAlias
parameter_list|()
block|{
return|return
name|alias
return|;
block|}
specifier|public
name|void
name|setAlias
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getUrls
parameter_list|()
block|{
return|return
name|urls
return|;
block|}
specifier|public
name|void
name|setUrls
parameter_list|(
name|String
index|[]
name|urls
parameter_list|)
block|{
name|this
operator|.
name|urls
operator|=
name|urls
expr_stmt|;
block|}
specifier|public
name|String
name|getStateString
parameter_list|()
block|{
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|WebEvent
operator|.
name|DEPLOYING
case|:
return|return
literal|"Deploying  "
return|;
case|case
name|WebEvent
operator|.
name|DEPLOYED
case|:
return|return
literal|"Deployed   "
return|;
case|case
name|WebEvent
operator|.
name|UNDEPLOYING
case|:
return|return
literal|"Undeploying"
return|;
case|case
name|WebEvent
operator|.
name|UNDEPLOYED
case|:
return|return
literal|"Undeployed "
return|;
case|case
name|WebEvent
operator|.
name|FAILED
case|:
return|return
literal|"Failed     "
return|;
case|case
name|WebEvent
operator|.
name|WAITING
case|:
return|return
literal|"Waiting    "
return|;
default|default:
return|return
literal|"Failed     "
return|;
block|}
block|}
block|}
end_class

end_unit

