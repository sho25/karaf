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
name|jaas
operator|.
name|config
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * POJO for a login module.  * It contains the class name, flags and a map of options.  */
end_comment

begin_class
specifier|public
class|class
name|Module
block|{
specifier|private
name|String
name|className
decl_stmt|;
specifier|private
name|String
name|flags
decl_stmt|;
specifier|private
name|Properties
name|options
decl_stmt|;
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
name|String
name|getFlags
parameter_list|()
block|{
return|return
name|flags
return|;
block|}
specifier|public
name|void
name|setFlags
parameter_list|(
name|String
name|flags
parameter_list|)
block|{
name|this
operator|.
name|flags
operator|=
name|flags
expr_stmt|;
block|}
specifier|public
name|Properties
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
specifier|public
name|void
name|setOptions
parameter_list|(
name|Properties
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
block|}
end_class

end_unit

