begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|examples
operator|.
name|soap
operator|.
name|api
package|;
end_package

begin_comment
comment|/**  * A regular POJO.  */
end_comment

begin_class
specifier|public
class|class
name|Booking
block|{
specifier|private
name|Long
name|id
decl_stmt|;
specifier|private
name|String
name|customer
decl_stmt|;
specifier|private
name|String
name|flight
decl_stmt|;
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getCustomer
parameter_list|()
block|{
return|return
name|customer
return|;
block|}
specifier|public
name|void
name|setCustomer
parameter_list|(
name|String
name|customer
parameter_list|)
block|{
name|this
operator|.
name|customer
operator|=
name|customer
expr_stmt|;
block|}
specifier|public
name|String
name|getFlight
parameter_list|()
block|{
return|return
name|flight
return|;
block|}
specifier|public
name|void
name|setFlight
parameter_list|(
name|String
name|flight
parameter_list|)
block|{
name|this
operator|.
name|flight
operator|=
name|flight
expr_stmt|;
block|}
block|}
end_class

end_unit

