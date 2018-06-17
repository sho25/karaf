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
name|blueprint
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
name|karaf
operator|.
name|examples
operator|.
name|blueprint
operator|.
name|common
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
name|blueprint
operator|.
name|common
operator|.
name|BookingService
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

begin_comment
comment|/**  * Just an indirection service basically for the itest.  */
end_comment

begin_class
specifier|public
class|class
name|ClientServiceImpl
implements|implements
name|ClientService
block|{
specifier|private
name|BookingService
name|bookingService
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Booking
argument_list|>
name|bookings
parameter_list|()
block|{
return|return
name|bookingService
operator|.
name|list
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addBooking
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
name|BookingService
name|getBookingService
parameter_list|()
block|{
return|return
name|bookingService
return|;
block|}
specifier|public
name|void
name|setBookingService
parameter_list|(
name|BookingService
name|bookingService
parameter_list|)
block|{
name|this
operator|.
name|bookingService
operator|=
name|bookingService
expr_stmt|;
block|}
block|}
end_class

end_unit

