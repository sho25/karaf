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
comment|/**  *<p>Used to mark an optional named command line option who's name typically starts with "--" or "-".  * This annotation can be applied to attributes of a class implementing an Action.  * The value of the command line option will be automatically converted to the attribute type.</p>  * @see org.apache.karaf.shell.support.converter.DefaultConverter  *  *<h2>Example 1 (boolean option):</h2>  *<code>@Option(name="--force") boolean force;</code>  *  *<p>This will be represented as --force on the command line.</p>  *  *<h2>Example 2 (mandatory String option):</h2>  *<code>@Option(name="-name",required=true) String name;</code>  *  *<p>This will be represented as -name=&lt;myname&gt; on the command line and the command will be rejected if the  * option is not given.</p>  */
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
name|Option
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_STRING
init|=
literal|"DEFAULT"
decl_stmt|;
comment|/**      * The name of this option.  Usually starting with a '-'.      *      * @return the option name.      */
name|String
name|name
parameter_list|()
function_decl|;
comment|/**      * Specify a list of aliases for this option.      * Useful when using an option with short or long names.      *      * @return the option aliases (as a string array).      */
name|String
index|[]
name|aliases
argument_list|()
expr|default
block|{}
expr_stmt|;
comment|/**      * A textual description of the option.      *      * @return the option description.      */
name|String
name|description
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Whether this argument is mandatory or not.      *      * @return true if the option is required, false else.      */
name|boolean
name|required
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * The last argument can be multi-valued in which case      * the field type must be a List.  On the command line,      * multi-valued options are used with specifying the option      * multiple times with different values.      *      * @return true if the option is multivalued, false else.      */
name|boolean
name|multiValued
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * The generated help displays default values for arguments.      * In case the value displayed in the help to the user should      * be different that the default value of the field, one      * can use this property to specify the value to display.      *      * @return the option description as shown in the help.      */
name|String
name|valueToShowInHelp
parameter_list|()
default|default
name|DEFAULT_STRING
function_decl|;
block|}
end_annotation_defn

end_unit

