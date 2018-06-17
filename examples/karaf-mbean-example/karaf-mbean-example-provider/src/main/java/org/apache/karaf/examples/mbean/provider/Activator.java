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
name|provider
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
name|util
operator|.
name|tracker
operator|.
name|BaseActivator
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
name|util
operator|.
name|tracker
operator|.
name|annotation
operator|.
name|ProvideService
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
name|util
operator|.
name|tracker
operator|.
name|annotation
operator|.
name|Services
import|;
end_import

begin_class
annotation|@
name|Services
argument_list|(
name|provides
operator|=
block|{
annotation|@
name|ProvideService
argument_list|(
name|BookingService
operator|.
name|class
argument_list|)
block|}
argument_list|)
specifier|public
class|class
name|Activator
extends|extends
name|BaseActivator
block|{
annotation|@
name|Override
specifier|public
name|void
name|doStart
parameter_list|()
block|{
name|BookingServiceMemoryImpl
name|bookingService
init|=
operator|new
name|BookingServiceMemoryImpl
argument_list|()
decl_stmt|;
name|register
argument_list|(
name|BookingService
operator|.
name|class
argument_list|,
name|bookingService
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

