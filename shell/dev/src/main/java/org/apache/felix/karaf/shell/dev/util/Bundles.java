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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|dev
operator|.
name|util
package|;
end_package

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

begin_comment
comment|/**  * A set of utility methods for working with {@link org.osgi.framework.Bundle}s  */
end_comment

begin_class
specifier|public
class|class
name|Bundles
block|{
comment|/**      * Return a String representation of a bundle state      */
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|int
name|state
parameter_list|)
block|{
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|Bundle
operator|.
name|UNINSTALLED
case|:
return|return
literal|"UNINSTALLED"
return|;
case|case
name|Bundle
operator|.
name|INSTALLED
case|:
return|return
literal|"INSTALLED"
return|;
case|case
name|Bundle
operator|.
name|RESOLVED
case|:
return|return
literal|"RESOLVED"
return|;
case|case
name|Bundle
operator|.
name|STARTING
case|:
return|return
literal|"STARTING"
return|;
case|case
name|Bundle
operator|.
name|STOPPING
case|:
return|return
literal|"STOPPING"
return|;
case|case
name|Bundle
operator|.
name|ACTIVE
case|:
return|return
literal|"ACTIVE"
return|;
default|default :
return|return
literal|"UNKNOWN"
return|;
block|}
block|}
block|}
end_class

end_unit

