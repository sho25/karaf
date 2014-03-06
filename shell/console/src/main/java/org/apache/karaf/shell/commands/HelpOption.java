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
name|karaf
operator|.
name|shell
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|HelpOption
block|{
specifier|public
specifier|static
specifier|final
name|Option
name|HELP
init|=
operator|new
name|Option
argument_list|()
block|{
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
literal|"--help"
return|;
block|}
specifier|public
name|String
index|[]
name|aliases
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{}
return|;
block|}
specifier|public
name|String
name|description
parameter_list|()
block|{
return|return
literal|"Display this help message"
return|;
block|}
specifier|public
name|boolean
name|required
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|multiValued
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|valueToShowInHelp
parameter_list|()
block|{
return|return
name|Option
operator|.
name|DEFAULT_STRING
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationType
parameter_list|()
block|{
return|return
name|Option
operator|.
name|class
return|;
block|}
block|}
decl_stmt|;
block|}
end_class

end_unit

