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
name|bundle
operator|.
name|core
package|;
end_package

begin_comment
comment|/**  * Bundle status including framework status.  *   * The combined status will be the worst status from all frameworks this bundle uses.  *   * e.g. On OSGi level the BundleState is Active, on Blueprint level it is Waiting  * then the status should be Waiting.  */
end_comment

begin_enum
specifier|public
enum|enum
name|BundleState
block|{
name|Installed
block|,
name|Resolved
block|,
name|Unknown
block|,
name|GracePeriod
block|,
name|Waiting
block|,
name|Starting
block|,
name|Active
block|,
name|Stopping
block|,
name|Failure
block|, }
end_enum

end_unit

