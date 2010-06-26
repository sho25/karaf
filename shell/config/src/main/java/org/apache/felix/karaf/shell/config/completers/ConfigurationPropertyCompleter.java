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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|config
operator|.
name|completers
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|Completer
import|;
end_import

begin_comment
comment|/**  * {@link jline.Completor} for Configuration Admin properties.  *  * Displays a list of existing properties based on the current configuration being edited.  *  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationPropertyCompleter
implements|implements
name|Completer
block|{
specifier|public
name|int
name|complete
parameter_list|(
specifier|final
name|String
name|buffer
parameter_list|,
specifier|final
name|int
name|cursor
parameter_list|,
specifier|final
name|List
name|candidates
parameter_list|)
block|{
comment|// TODO: currently we have no way to access the session which is being run in this thread
return|return
operator|-
literal|1
return|;
comment|//        if (vars.get(ConfigCommandSupport.PROPERTY_CONFIG_PID) == null) {
comment|//            return -1;
comment|//        }
comment|//
comment|//        Dictionary props = (Dictionary) vars.get(ConfigCommandSupport.PROPERTY_CONFIG_PROPS);
comment|//        StringsCompleter delegate = new StringsCompleter();
comment|//
comment|//        for (Enumeration e = props.keys(); e.hasMoreElements();) {
comment|//            String key = (String) e.nextElement();
comment|//            delegate.getStrings().add(key);
comment|//        }
comment|//
comment|//        return delegate.complete(buffer, cursor, candidates);
block|}
block|}
end_class

end_unit

