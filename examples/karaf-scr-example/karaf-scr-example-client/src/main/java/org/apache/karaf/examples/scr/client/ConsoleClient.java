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
name|scr
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
name|scr
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
name|scr
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
name|Activate
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
name|Deactivate
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

begin_class
annotation|@
name|Component
specifier|public
class|class
name|ConsoleClient
block|{
specifier|private
name|boolean
name|running
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|BookingService
name|bookingService
decl_stmt|;
annotation|@
name|Activate
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
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
literal|1L
argument_list|)
expr_stmt|;
name|booking
operator|.
name|setFlight
argument_list|(
literal|"AF520"
argument_list|)
expr_stmt|;
name|booking
operator|.
name|setCustomer
argument_list|(
literal|"John Doe"
argument_list|)
expr_stmt|;
name|bookingService
operator|.
name|add
argument_list|(
name|booking
argument_list|)
expr_stmt|;
name|booking
operator|=
operator|new
name|Booking
argument_list|()
expr_stmt|;
name|booking
operator|.
name|setId
argument_list|(
literal|2L
argument_list|)
expr_stmt|;
name|booking
operator|.
name|setFlight
argument_list|(
literal|"AF59"
argument_list|)
expr_stmt|;
name|booking
operator|.
name|setCustomer
argument_list|(
literal|"Alan Parker"
argument_list|)
expr_stmt|;
name|bookingService
operator|.
name|add
argument_list|(
name|booking
argument_list|)
expr_stmt|;
name|running
operator|=
literal|true
expr_stmt|;
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
name|running
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"-----------"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|booking
operator|.
name|getId
argument_list|()
operator|+
literal|" - "
operator|+
name|booking
operator|.
name|getFlight
argument_list|()
operator|+
literal|" - "
operator|+
name|booking
operator|.
name|getCustomer
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// nothing to do
block|}
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Deactivate
specifier|public
name|void
name|deactivate
parameter_list|()
throws|throws
name|Exception
block|{
name|running
operator|=
literal|false
expr_stmt|;
block|}
block|}
end_class

end_unit
