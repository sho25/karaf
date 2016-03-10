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
comment|/**  * The @Completion annotation can be used on a field annotated with  * {@link Option} or {@link Argument} to specify the completion  * method to use for this field.  *  * @see org.apache.karaf.shell.api.console.Completer  * @see org.apache.karaf.shell.support.completers.StringsCompleter  */
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
name|Completion
block|{
comment|/**      * The completer class to use for this field.      * The console registry will be used to look for      * a completer of this class.      *      * A special case for simple static completions is to use      * {@link org.apache.karaf.shell.support.completers.StringsCompleter},      * in which case, the<code>values</code> property will be used      * as the list of possible completions.      *      * @return the completer class.      */
name|Class
argument_list|<
name|?
argument_list|>
name|value
parameter_list|()
function_decl|;
comment|/**      * When using a static completer, returns the possible values.      *      * @return possible completion values as string array.      */
name|String
index|[]
name|values
argument_list|()
expr|default
block|{ }
expr_stmt|;
comment|/**      * When using a static completer, indicates if completion      * should be done case sensitive or not.      *      * @return true if the completion is case sensitive, false else.      */
name|boolean
name|caseSensitive
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

