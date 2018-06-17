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
name|jpa
operator|.
name|provider
operator|.
name|ds
operator|.
name|hibernate
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|jpa
operator|.
name|template
operator|.
name|JpaTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|jpa
operator|.
name|template
operator|.
name|TransactionType
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
name|jpa
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
name|jpa
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Implementation of the booking service using the JPA entity manager service (provided by Karaf).  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
name|service
operator|=
name|BookingService
operator|.
name|class
argument_list|,
name|immediate
operator|=
literal|true
argument_list|)
specifier|public
class|class
name|BookingServiceImpl
implements|implements
name|BookingService
block|{
annotation|@
name|Reference
argument_list|(
name|target
operator|=
literal|"(osgi.unit.name=booking-hibernate)"
argument_list|)
specifier|private
name|JpaTemplate
name|jpaTemplate
decl_stmt|;
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
name|jpaTemplate
operator|.
name|tx
argument_list|(
name|TransactionType
operator|.
name|RequiresNew
argument_list|,
name|entityManager
lambda|->
block|{
name|entityManager
operator|.
name|persist
argument_list|(
name|booking
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|add
parameter_list|(
name|String
name|flight
parameter_list|,
name|String
name|customer
parameter_list|)
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
name|jpaTemplate
operator|.
name|tx
argument_list|(
name|TransactionType
operator|.
name|RequiresNew
argument_list|,
name|entityManager
lambda|->
block|{
name|entityManager
operator|.
name|persist
argument_list|(
name|booking
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Booking
argument_list|>
name|list
parameter_list|()
block|{
return|return
name|jpaTemplate
operator|.
name|txExpr
argument_list|(
name|TransactionType
operator|.
name|Supports
argument_list|,
name|entityManager
lambda|->
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"SELECT b FROM Booking b"
argument_list|,
name|Booking
operator|.
name|class
argument_list|)
operator|.
name|getResultList
argument_list|()
argument_list|)
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
name|jpaTemplate
operator|.
name|txExpr
argument_list|(
name|TransactionType
operator|.
name|Supports
argument_list|,
name|entityManager
lambda|->
name|entityManager
operator|.
name|find
argument_list|(
name|Booking
operator|.
name|class
argument_list|,
name|id
argument_list|)
argument_list|)
return|;
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
name|jpaTemplate
operator|.
name|tx
argument_list|(
name|TransactionType
operator|.
name|RequiresNew
argument_list|,
name|entityManager
lambda|->
block|{
name|Booking
name|booking
init|=
name|entityManager
operator|.
name|find
argument_list|(
name|Booking
operator|.
name|class
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|booking
operator|!=
literal|null
condition|)
block|{
name|entityManager
operator|.
name|remove
argument_list|(
name|booking
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

