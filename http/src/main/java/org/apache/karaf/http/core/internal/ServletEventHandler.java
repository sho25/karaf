begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*   * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|http
operator|.
name|core
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|Iterator
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
name|ServletEvent
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
name|ServletListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_class
specifier|public
class|class
name|ServletEventHandler
implements|implements
name|ServletListener
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ServletEvent
argument_list|>
name|servletEvents
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
specifier|synchronized
name|void
name|servletEvent
parameter_list|(
name|ServletEvent
name|event
parameter_list|)
block|{
name|servletEvents
operator|.
name|put
argument_list|(
name|event
operator|.
name|getServletName
argument_list|()
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @return the servletEvents 	 */
specifier|public
specifier|synchronized
name|List
argument_list|<
name|ServletEvent
argument_list|>
name|getServletEvents
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|servletEvents
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|removeEventsForBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|ServletEvent
argument_list|>
argument_list|>
name|iterator
init|=
name|servletEvents
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|ServletEvent
argument_list|>
name|entry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getBundle
argument_list|()
operator|==
name|bundle
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

