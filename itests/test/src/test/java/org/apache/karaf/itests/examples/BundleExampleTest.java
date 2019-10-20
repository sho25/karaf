begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|itests
operator|.
name|examples
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
name|bundle
operator|.
name|client
operator|.
name|ClientService
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
name|bundle
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
name|bundle
operator|.
name|common
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
name|itests
operator|.
name|BaseTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|ExamReactorStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|PerClass
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
annotation|@
name|ExamReactorStrategy
argument_list|(
name|PerClass
operator|.
name|class
argument_list|)
specifier|public
class|class
name|BundleExampleTest
extends|extends
name|BaseTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
comment|// add bundle example features repository
name|addFeaturesRepository
argument_list|(
literal|"mvn:org.apache.karaf.examples/karaf-bundle-example-features/"
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.version"
argument_list|)
operator|+
literal|"/xml"
argument_list|)
expr_stmt|;
comment|// install the karaf-bundle-example-provider feature
name|installAndAssertFeature
argument_list|(
literal|"karaf-bundle-example-provider"
argument_list|)
expr_stmt|;
comment|// check the provider service
name|assertServiceAvailable
argument_list|(
name|BookingService
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// install the karaf-bundle-example-client feature
name|installAndAssertFeature
argument_list|(
literal|"karaf-bundle-example-client"
argument_list|)
expr_stmt|;
comment|// get the client service
name|assertServiceAvailable
argument_list|(
name|ClientService
operator|.
name|class
argument_list|)
expr_stmt|;
name|ClientService
name|clientService
init|=
name|getOsgiService
argument_list|(
name|ClientService
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// use the client service to manipulate the booking service
name|Booking
name|booking
init|=
operator|new
name|Booking
argument_list|(
literal|"Karaf Itest"
argument_list|,
literal|"IT001"
argument_list|)
decl_stmt|;
name|clientService
operator|.
name|addBooking
argument_list|(
name|booking
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Booking
argument_list|>
name|bookings
init|=
name|clientService
operator|.
name|bookings
argument_list|()
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Booking
name|b
range|:
name|bookings
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getCustomer
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Karaf Itest"
argument_list|)
operator|&&
name|b
operator|.
name|getFlight
argument_list|()
operator|.
name|equals
argument_list|(
literal|"IT001"
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|Assert
operator|.
name|assertTrue
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

