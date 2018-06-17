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
name|blueprint
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
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NoResultException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PersistenceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|TypedQuery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|Transactional
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
name|Transactional
specifier|public
class|class
name|BookingServiceImpl
implements|implements
name|BookingService
block|{
annotation|@
name|PersistenceContext
argument_list|(
name|unitName
operator|=
literal|"booking-hibernate"
argument_list|)
specifier|private
name|EntityManager
name|entityManager
decl_stmt|;
specifier|public
name|void
name|setEntityManager
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|)
block|{
name|this
operator|.
name|entityManager
operator|=
name|entityManager
expr_stmt|;
block|}
annotation|@
name|Transactional
argument_list|(
name|Transactional
operator|.
name|TxType
operator|.
name|REQUIRES_NEW
argument_list|)
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
name|entityManager
operator|.
name|persist
argument_list|(
name|booking
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Transactional
argument_list|(
name|Transactional
operator|.
name|TxType
operator|.
name|REQUIRES_NEW
argument_list|)
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
name|entityManager
operator|.
name|persist
argument_list|(
name|booking
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Transactional
argument_list|(
name|Transactional
operator|.
name|TxType
operator|.
name|SUPPORTS
argument_list|)
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
name|TypedQuery
argument_list|<
name|Booking
argument_list|>
name|query
init|=
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
decl_stmt|;
return|return
name|query
operator|.
name|getResultList
argument_list|()
return|;
block|}
annotation|@
name|Transactional
argument_list|(
name|Transactional
operator|.
name|TxType
operator|.
name|SUPPORTS
argument_list|)
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
name|TypedQuery
argument_list|<
name|Booking
argument_list|>
name|query
init|=
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"SELECT b FROM Booking b WHERE b.id=:id"
argument_list|,
name|Booking
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|Booking
name|booking
init|=
literal|null
decl_stmt|;
try|try
block|{
name|booking
operator|=
name|query
operator|.
name|getSingleResult
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoResultException
name|e
parameter_list|)
block|{
comment|// nothing to do
block|}
return|return
name|booking
return|;
block|}
annotation|@
name|Transactional
argument_list|(
name|Transactional
operator|.
name|TxType
operator|.
name|REQUIRES_NEW
argument_list|)
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
name|Booking
name|booking
init|=
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|entityManager
operator|.
name|remove
argument_list|(
name|booking
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

