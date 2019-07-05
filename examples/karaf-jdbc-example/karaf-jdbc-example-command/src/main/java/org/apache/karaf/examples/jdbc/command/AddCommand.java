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
name|jdbc
operator|.
name|command
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
name|jdbc
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
name|jdbc
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
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Action
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Argument
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Command
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Option
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Reference
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_class
annotation|@
name|Service
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"booking"
argument_list|,
name|name
operator|=
literal|"add"
argument_list|,
name|description
operator|=
literal|"Add a booking"
argument_list|)
specifier|public
class|class
name|AddCommand
implements|implements
name|Action
block|{
annotation|@
name|Reference
specifier|private
name|BookingService
name|bookingService
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-i"
argument_list|,
name|aliases
operator|=
literal|"--id"
argument_list|,
name|description
operator|=
literal|"Booking ID"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|Long
name|id
init|=
literal|0L
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"customer"
argument_list|,
name|description
operator|=
literal|"Booking customer"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|customer
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"flight"
argument_list|,
name|description
operator|=
literal|"Booking flight"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|flight
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
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
if|if
condition|(
name|id
operator|==
literal|0
condition|)
block|{
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|id
operator|=
operator|new
name|Long
argument_list|(
name|random
operator|.
name|nextInt
argument_list|(
literal|9000000
argument_list|)
operator|+
literal|1000000
argument_list|)
expr_stmt|;
block|}
name|booking
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|booking
operator|.
name|setCustomer
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|booking
operator|.
name|setFlight
argument_list|(
name|flight
argument_list|)
expr_stmt|;
name|bookingService
operator|.
name|add
argument_list|(
name|booking
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

