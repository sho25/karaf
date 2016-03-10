begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|api
operator|.
name|action
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
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * Used to denote a class represents a command which is executable  * within a shell/scope or as a command line process.  *  * All classes annotated with @Command should implement the  * {@link org.apache.karaf.shell.api.action.Action} interface.  */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|}
argument_list|)
specifier|public
annotation_defn|@interface
name|Command
block|{
comment|/**      * Returns the scope or sub shell of the command.      *      * @return the command scope.      */
name|String
name|scope
parameter_list|()
function_decl|;
comment|/**      * Returns the name of the command if used inside a shell.      *      * @return the command name.      */
name|String
name|name
parameter_list|()
function_decl|;
comment|/**      * Returns the description of the command which is used to generate command line help.      *      * @return the command description.      */
name|String
name|description
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Returns a detailed description of the command.      * This description will be shown in the help for the command.      * Longer descriptions can be externalized using a      *<code>classpath:[location]</code> url, in which case the      * description will be loaded from the bundle at the given location,      * relatively to the implementation of the command.      *      * @return the command long description.      */
name|String
name|detailedDescription
parameter_list|()
default|default
literal|""
function_decl|;
block|}
end_annotation_defn

end_unit

