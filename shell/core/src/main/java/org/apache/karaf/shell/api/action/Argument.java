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
comment|/**  * Represents a positional argument on a command line (as opposed to an optional named {@link Option}  */
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
name|FIELD
block|}
argument_list|)
specifier|public
annotation_defn|@interface
name|Argument
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_STRING
init|=
literal|"DEFAULT"
decl_stmt|;
name|String
name|DEFAULT
init|=
literal|"##default"
decl_stmt|;
comment|/**      * Name of the argument.      * By default, the field name will be used.      *      * @return the argument name.      */
name|String
name|name
parameter_list|()
default|default
name|DEFAULT
function_decl|;
comment|/**      * A textual description of the argument.      *      * @return the argument description.      */
name|String
name|description
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Whether this argument is mandatory or not.      *      * @return true if the argument is required, false else.      */
name|boolean
name|required
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Position of the argument in the command line.      * When using multiple arguments, the indices must be      * starting from 0 and incrementing without any holes.      *      * @return the argument index.      */
name|int
name|index
parameter_list|()
default|default
literal|0
function_decl|;
comment|/**      * The last argument can be multi-valued in which case      * the field type must be a List.      *      * @return true if the argument has multiple values, false else.      */
name|boolean
name|multiValued
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * The generated help displays default values for arguments.      * In case the value displayed in the help to the user should      * be different that the default value of the field, one      * can use this property to specify the value to display.      *      * @return the argument help string representation.      */
name|String
name|valueToShowInHelp
parameter_list|()
default|default
name|DEFAULT_STRING
function_decl|;
block|}
end_annotation_defn

end_unit

