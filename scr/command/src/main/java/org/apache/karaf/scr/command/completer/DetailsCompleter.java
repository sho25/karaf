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
name|scr
operator|.
name|command
operator|.
name|completer
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|scr
operator|.
name|Component
import|;
end_import

begin_class
specifier|public
class|class
name|DetailsCompleter
extends|extends
name|ScrCompleterSupport
block|{
comment|/**      * Overrides the super method noted below. See super documentation for      * details.      *       * @see org.apache.karaf.scr.command.completer.ScrCompleterSupport#availableComponent(org.apache.felix.scr.Component)      */
annotation|@
name|Override
specifier|public
name|boolean
name|availableComponent
parameter_list|(
name|Component
name|component
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|component
operator|!=
literal|null
operator|)
return|;
block|}
block|}
end_class

end_unit

