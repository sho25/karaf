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
name|shell
operator|.
name|web
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|WebEvent
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
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|WebListener
import|;
end_import

begin_comment
comment|/**  * Class implementing {@link WebListener} service to retrieve {@link WebEvent}  */
end_comment

begin_comment
comment|//public class WebEventHandler implements WebListener {
end_comment

begin_class
specifier|public
class|class
name|WebEventHandler
implements|implements
name|WebListener
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|Long
argument_list|,
name|WebEvent
argument_list|>
name|bundleEvents
init|=
operator|new
name|HashMap
argument_list|<
name|Long
argument_list|,
name|WebEvent
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|Map
argument_list|<
name|Long
argument_list|,
name|WebEvent
argument_list|>
name|getBundleEvents
parameter_list|()
block|{
return|return
name|bundleEvents
return|;
block|}
specifier|public
name|void
name|webEvent
parameter_list|(
name|WebEvent
name|event
parameter_list|)
block|{
name|getBundleEvents
argument_list|()
operator|.
name|put
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

