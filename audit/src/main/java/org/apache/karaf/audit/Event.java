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
name|audit
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_interface
specifier|public
interface|interface
name|Event
block|{
name|String
name|TYPE_SHELL
init|=
literal|"shell"
decl_stmt|;
name|String
name|TYPE_LOG
init|=
literal|"log"
decl_stmt|;
name|String
name|TYPE_SERVICE
init|=
literal|"service"
decl_stmt|;
name|String
name|TYPE_BUNDLE
init|=
literal|"bundle"
decl_stmt|;
name|String
name|TYPE_LOGIN
init|=
literal|"login"
decl_stmt|;
name|String
name|TYPE_JMX
init|=
literal|"jmx"
decl_stmt|;
name|String
name|TYPE_FRAMEWORK
init|=
literal|"framework"
decl_stmt|;
name|String
name|TYPE_WEB
init|=
literal|"web"
decl_stmt|;
name|String
name|TYPE_REPOSITORIES
init|=
literal|"repositories"
decl_stmt|;
name|String
name|TYPE_FEATURES
init|=
literal|"features"
decl_stmt|;
name|String
name|TYPE_BLUEPRINT
init|=
literal|"blueprint"
decl_stmt|;
name|String
name|TYPE_UNKNOWN
init|=
literal|"unknown"
decl_stmt|;
name|long
name|timestamp
parameter_list|()
function_decl|;
name|Subject
name|subject
parameter_list|()
function_decl|;
name|String
name|type
parameter_list|()
function_decl|;
name|String
name|subtype
parameter_list|()
function_decl|;
name|Iterable
argument_list|<
name|String
argument_list|>
name|keys
parameter_list|()
function_decl|;
name|Object
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

