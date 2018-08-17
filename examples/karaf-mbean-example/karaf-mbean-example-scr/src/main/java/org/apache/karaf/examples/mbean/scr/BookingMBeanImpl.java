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
name|mbean
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
name|mbean
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
name|mbean
operator|.
name|api
operator|.
name|BookingService
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
name|component
operator|.
name|annotations
operator|.
name|Component
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
name|component
operator|.
name|annotations
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StandardMBean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|*
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
name|property
operator|=
literal|"jmx.objectname=org.apache.karaf.examples:type=booking,name=default"
argument_list|)
specifier|public
class|class
name|BookingMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|BookingMBean
block|{
annotation|@
name|Reference
specifier|private
name|BookingService
name|bookingService
decl_stmt|;
specifier|public
name|BookingMBeanImpl
parameter_list|()
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|BookingMBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|getBookings
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
name|CompositeType
name|bookingType
init|=
operator|new
name|CompositeType
argument_list|(
literal|"booking"
argument_list|,
literal|"Booking"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"flight"
block|,
literal|"customer"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID"
block|,
literal|"Flight"
block|,
literal|"Customer"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|LONG
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|}
argument_list|)
decl_stmt|;
name|TabularType
name|tabularType
init|=
operator|new
name|TabularType
argument_list|(
literal|"bookings"
argument_list|,
literal|"Bookings"
argument_list|,
name|bookingType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|}
argument_list|)
decl_stmt|;
name|TabularData
name|tabularData
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|tabularType
argument_list|)
decl_stmt|;
for|for
control|(
name|Booking
name|booking
range|:
name|bookingService
operator|.
name|list
argument_list|()
control|)
block|{
name|CompositeData
name|compositeData
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|bookingType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"flight"
block|,
literal|"customer"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|booking
operator|.
name|getId
argument_list|()
block|,
name|booking
operator|.
name|getFlight
argument_list|()
block|,
name|booking
operator|.
name|getCustomer
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|tabularData
operator|.
name|put
argument_list|(
name|compositeData
argument_list|)
expr_stmt|;
block|}
return|return
name|tabularData
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|add
parameter_list|(
name|long
name|id
parameter_list|,
name|String
name|flight
parameter_list|,
name|String
name|customer
parameter_list|)
throws|throws
name|MBeanException
block|{
name|Booking
name|booking
init|=
operator|new
name|Booking
argument_list|()
decl_stmt|;
name|booking
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|booking
operator|.
name|setFlight
argument_list|(
name|flight
argument_list|)
expr_stmt|;
name|booking
operator|.
name|setCustomer
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|bookingService
operator|.
name|add
argument_list|(
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
name|long
name|id
parameter_list|)
throws|throws
name|MBeanException
block|{
name|bookingService
operator|.
name|remove
argument_list|(
name|id
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

