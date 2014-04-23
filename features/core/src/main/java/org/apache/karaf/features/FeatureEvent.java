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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
import|;
end_import

begin_class
specifier|public
class|class
name|FeatureEvent
extends|extends
name|EventObject
block|{
specifier|public
specifier|static
enum|enum
name|EventType
block|{
name|FeatureInstalled
block|,
name|FeatureUninstalled
block|}
specifier|private
specifier|final
name|EventType
name|type
decl_stmt|;
specifier|private
specifier|final
name|Feature
name|feature
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|replay
decl_stmt|;
specifier|public
name|FeatureEvent
parameter_list|(
name|Feature
name|feature
parameter_list|,
name|EventType
name|type
parameter_list|,
name|boolean
name|replay
parameter_list|)
block|{
name|super
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|feature
operator|=
name|feature
expr_stmt|;
name|this
operator|.
name|replay
operator|=
name|replay
expr_stmt|;
block|}
specifier|public
name|EventType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|Feature
name|getFeature
parameter_list|()
block|{
return|return
name|feature
return|;
block|}
specifier|public
name|boolean
name|isReplay
parameter_list|()
block|{
return|return
name|replay
return|;
block|}
block|}
end_class

end_unit
