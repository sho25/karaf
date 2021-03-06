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
name|client
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|examples
operator|.
name|soap
operator|.
name|api
operator|.
name|Booking
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
name|examples
operator|.
name|soap
operator|.
name|blueprint
operator|.
name|BookingServiceSoap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
specifier|public
class|class
name|CxfClient
block|{
specifier|private
name|BookingServiceSoap
name|bookingService
decl_stmt|;
specifier|public
name|CxfClient
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|BookingServiceSoap
operator|.
name|class
argument_list|)
expr_stmt|;
name|bookingService
operator|=
operator|(
name|BookingServiceSoap
operator|)
name|factory
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|Booking
name|booking
parameter_list|)
block|{
name|bookingService
operator|.
name|add
argument_list|(
name|booking
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|Booking
argument_list|>
name|list
parameter_list|()
block|{
return|return
name|bookingService
operator|.
name|list
argument_list|()
return|;
block|}
block|}
end_class

end_unit

