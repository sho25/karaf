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
name|scr
package|;
end_package

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
name|javax
operator|.
name|jws
operator|.
name|WebService
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.karaf.examples.soap.scr.BookingServiceSoap"
argument_list|,
name|serviceName
operator|=
literal|"Booking"
argument_list|)
specifier|public
class|class
name|BookingServiceSoapImpl
implements|implements
name|BookingServiceSoap
block|{
specifier|private
name|Map
argument_list|<
name|Long
argument_list|,
name|Booking
argument_list|>
name|bookings
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Booking
argument_list|>
name|list
parameter_list|()
block|{
return|return
name|bookings
operator|.
name|values
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Booking
name|get
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
return|return
name|bookings
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|add
parameter_list|(
name|Booking
name|booking
parameter_list|)
block|{
name|bookings
operator|.
name|put
argument_list|(
name|booking
operator|.
name|getId
argument_list|()
argument_list|,
name|booking
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|bookings
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

