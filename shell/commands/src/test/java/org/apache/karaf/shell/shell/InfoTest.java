begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|shell
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|InfoTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|TestCase
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|testUptime
parameter_list|()
throws|throws
name|Exception
block|{
name|InfoAction
name|infoAction
init|=
operator|new
name|InfoAction
argument_list|()
decl_stmt|;
comment|// uptime 30 seconds
name|assertEquals
argument_list|(
literal|"30.000 seconds"
argument_list|,
name|infoAction
operator|.
name|printDuration
argument_list|(
literal|30
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
comment|// uptime 2 minutes
name|assertEquals
argument_list|(
literal|"2 minutes"
argument_list|,
name|infoAction
operator|.
name|printDuration
argument_list|(
literal|2
operator|*
literal|60
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
comment|// uptime 2 hours
name|assertEquals
argument_list|(
literal|"2 hours"
argument_list|,
name|infoAction
operator|.
name|printDuration
argument_list|(
literal|2
operator|*
literal|3600
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
comment|// update 2 days and 2 hours
name|assertEquals
argument_list|(
literal|"2 days 2 hours"
argument_list|,
name|infoAction
operator|.
name|printDuration
argument_list|(
literal|50
operator|*
literal|3600
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

