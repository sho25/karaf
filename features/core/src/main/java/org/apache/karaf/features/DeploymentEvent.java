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
comment|/**  * Enumeration of deployment events. Each deployment is an operation potentially involving multiple features and its  * bundles. Deployments cannot overlap.  */
end_comment

begin_enum
specifier|public
enum|enum
name|DeploymentEvent
block|{
comment|/**      * A new deployment operation has started.      */
name|DEPLOYMENT_STARTED
block|,
comment|/**      * Bundle install/uninstall operations within this deployment have completed.      */
name|BUNDLES_INSTALLED
block|,
comment|/**      * Bundle resolution has completed, but the bundles have not yet been started.      */
name|BUNDLES_RESOLVED
block|,
comment|/**      * The deployment has completed.      */
name|DEPLOYMENT_FINISHED
block|, }
end_enum

end_unit

