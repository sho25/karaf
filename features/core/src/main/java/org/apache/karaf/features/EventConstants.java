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
name|features
package|;
end_package

begin_comment
comment|/**  * Constants for EventAdmin events  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|EventConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EVENT
init|=
literal|"event"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP
init|=
literal|"timestamp"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FEATURE_NAME
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FEATURE_VERSION
init|=
literal|"version"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_NAME
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_URI
init|=
literal|"uri"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_EVENTS
init|=
literal|"org/apache/karaf/features"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_FEATURES_INSTALLED
init|=
name|TOPIC_EVENTS
operator|+
literal|"/features/INSTALLED"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_FEATURES_UNINSTALLED
init|=
name|TOPIC_EVENTS
operator|+
literal|"/features/UNINSTALLED"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_REPOSITORY_ADDED
init|=
name|TOPIC_EVENTS
operator|+
literal|"/repositories/ADDED"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_REPOSITORY_REMOVED
init|=
name|TOPIC_EVENTS
operator|+
literal|"/repositories/REMOVED"
decl_stmt|;
specifier|private
name|EventConstants
parameter_list|()
block|{
comment|// non-instantiable class
block|}
block|}
end_class

end_unit

