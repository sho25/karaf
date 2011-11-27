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
name|tooling
operator|.
name|exam
operator|.
name|options
operator|.
name|configs
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
name|tooling
operator|.
name|exam
operator|.
name|options
operator|.
name|ConfigurationPointer
import|;
end_import

begin_comment
comment|/**  * Pre configured property file pointers to the most commonly used properties in /etc/config.properties and  * /etc/org.ops4j.pax.logging.cfg.  */
end_comment

begin_interface
specifier|public
interface|interface
name|LoggingCfg
block|{
specifier|static
specifier|final
name|String
name|FILE_PATH
init|=
literal|"etc/org.ops4j.pax.logging.cfg"
decl_stmt|;
specifier|static
specifier|final
name|ConfigurationPointer
name|ROOT_LOGGER
init|=
operator|new
name|CustomPropertiesPointer
argument_list|(
literal|"log4j.rootLogger"
argument_list|)
decl_stmt|;
specifier|static
class|class
name|CustomPropertiesPointer
extends|extends
name|ConfigurationPointer
block|{
specifier|public
name|CustomPropertiesPointer
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|super
argument_list|(
name|FILE_PATH
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_interface

end_unit

