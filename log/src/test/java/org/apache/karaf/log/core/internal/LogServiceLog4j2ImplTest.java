begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|log
operator|.
name|core
operator|.
name|internal
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|hasEntry
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
specifier|public
class|class
name|LogServiceLog4j2ImplTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ROOT_LOGGER
init|=
literal|"log4j.rootLogger"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOGGER_PREFIX
init|=
literal|"log4j2.logger."
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAME_SUFFIX
init|=
literal|".name"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LEVEL_SUFFIX
init|=
literal|".level"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testLoggerNameWithNumbers
parameter_list|()
block|{
specifier|final
name|String
name|name
init|=
literal|"some_logger_name"
decl_stmt|;
specifier|final
name|String
name|logger
init|=
literal|"org.ops4j.pax.web"
decl_stmt|;
specifier|final
name|String
name|level
init|=
literal|"WARN"
decl_stmt|;
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
name|ROOT_LOGGER
argument_list|,
literal|"INFO"
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
name|LOGGER_PREFIX
operator|+
name|name
operator|+
name|NAME_SUFFIX
argument_list|,
name|logger
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
name|LOGGER_PREFIX
operator|+
name|name
operator|+
name|LEVEL_SUFFIX
argument_list|,
name|level
argument_list|)
expr_stmt|;
specifier|final
name|LogServiceInternal
name|logServiceInternal
init|=
operator|new
name|LogServiceLog4j2Impl
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|logServiceInternal
operator|.
name|getLevel
argument_list|(
name|logger
argument_list|)
argument_list|,
name|hasEntry
argument_list|(
name|logger
argument_list|,
name|level
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetLevelForLoggerNameWithNumbers
parameter_list|()
block|{
specifier|final
name|String
name|logger
init|=
literal|"org.ops4j.pax.web"
decl_stmt|;
specifier|final
name|String
name|level
init|=
literal|"WARN"
decl_stmt|;
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
name|ROOT_LOGGER
argument_list|,
literal|"INFO"
argument_list|)
expr_stmt|;
specifier|final
name|LogServiceInternal
name|logServiceInternal
init|=
operator|new
name|LogServiceLog4j2Impl
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|logServiceInternal
operator|.
name|setLevel
argument_list|(
name|logger
argument_list|,
name|level
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|logServiceInternal
operator|.
name|getLevel
argument_list|(
name|logger
argument_list|)
argument_list|,
name|hasEntry
argument_list|(
name|logger
argument_list|,
name|level
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUpdateLevelForLoggerNameWithNumbers
parameter_list|()
block|{
specifier|final
name|String
name|name
init|=
literal|"some_logger_name"
decl_stmt|;
specifier|final
name|String
name|logger
init|=
literal|"org.ops4j.pax.web"
decl_stmt|;
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
name|ROOT_LOGGER
argument_list|,
literal|"INFO"
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
name|LOGGER_PREFIX
operator|+
name|name
operator|+
name|NAME_SUFFIX
argument_list|,
name|logger
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
name|LOGGER_PREFIX
operator|+
name|name
operator|+
name|LEVEL_SUFFIX
argument_list|,
literal|"WARN"
argument_list|)
expr_stmt|;
specifier|final
name|LogServiceInternal
name|logServiceInternal
init|=
operator|new
name|LogServiceLog4j2Impl
argument_list|(
name|config
argument_list|)
decl_stmt|;
specifier|final
name|String
name|newLevel
init|=
literal|"TRACE"
decl_stmt|;
name|logServiceInternal
operator|.
name|setLevel
argument_list|(
name|logger
argument_list|,
name|newLevel
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|logServiceInternal
operator|.
name|getLevel
argument_list|(
name|logger
argument_list|)
argument_list|,
name|hasEntry
argument_list|(
name|logger
argument_list|,
name|newLevel
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
