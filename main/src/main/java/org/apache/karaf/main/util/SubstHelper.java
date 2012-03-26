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
name|main
operator|.
name|util
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_class
specifier|public
class|class
name|SubstHelper
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DELIM_START
init|=
literal|"${"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DELIM_STOP
init|=
literal|"}"
decl_stmt|;
comment|/**      *<p>      * This method performs property variable substitution on the      * specified value. If the specified value contains the syntax      *<tt>${&lt;prop-name&gt;}</tt>, where<tt>&lt;prop-name&gt;</tt>      * refers to either a configuration property or a system property,      * then the corresponding property value is substituted for the variable      * placeholder. Multiple variable placeholders may exist in the      * specified value as well as nested variable placeholders, which      * are substituted from inner most to outer most. Configuration      * properties override system properties.      *</p>      *      * @param val         The string on which to perform property substitution.      * @param currentKey  The key of the property being evaluated used to      *                    detect cycles.      * @param cycleMap    Map of variable references used to detect nested cycles.      * @param configProps Set of configuration properties.      * @return The value of the specified string after system property substitution.      * @throws IllegalArgumentException If there was a syntax error in the      *                                  property placeholder syntax or a recursive variable reference.      */
specifier|public
specifier|static
name|String
name|substVars
parameter_list|(
name|String
name|val
parameter_list|,
name|String
name|currentKey
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cycleMap
parameter_list|,
name|Properties
name|configProps
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// If there is currently no cycle map, then create
comment|// one for detecting cycles for this invocation.
if|if
condition|(
name|cycleMap
operator|==
literal|null
condition|)
block|{
name|cycleMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|// Put the current key in the cycle map.
name|cycleMap
operator|.
name|put
argument_list|(
name|currentKey
argument_list|,
name|currentKey
argument_list|)
expr_stmt|;
comment|// Assume we have a value that is something like:
comment|// "leading ${foo.${bar}} middle ${baz} trailing"
comment|// Find the first ending '}' variable delimiter, which
comment|// will correspond to the first deepest nested variable
comment|// placeholder.
name|int
name|stopDelim
init|=
name|val
operator|.
name|indexOf
argument_list|(
name|DELIM_STOP
argument_list|)
decl_stmt|;
comment|// Find the matching starting "${" variable delimiter
comment|// by looping until we find a start delimiter that is
comment|// greater than the stop delimiter we have found.
name|int
name|startDelim
init|=
name|val
operator|.
name|indexOf
argument_list|(
name|DELIM_START
argument_list|)
decl_stmt|;
while|while
condition|(
name|stopDelim
operator|>=
literal|0
condition|)
block|{
name|int
name|idx
init|=
name|val
operator|.
name|indexOf
argument_list|(
name|DELIM_START
argument_list|,
name|startDelim
operator|+
name|DELIM_START
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|idx
operator|<
literal|0
operator|)
operator|||
operator|(
name|idx
operator|>
name|stopDelim
operator|)
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|idx
operator|<
name|stopDelim
condition|)
block|{
name|startDelim
operator|=
name|idx
expr_stmt|;
block|}
block|}
comment|// If we do not have a start or stop delimiter, then just
comment|// return the existing value.
if|if
condition|(
operator|(
name|startDelim
operator|<
literal|0
operator|)
operator|&&
operator|(
name|stopDelim
operator|<
literal|0
operator|)
condition|)
block|{
return|return
name|val
return|;
block|}
comment|// At this point, we found a stop delimiter without a start,
comment|// so throw an exception.
elseif|else
if|if
condition|(
operator|(
operator|(
name|startDelim
operator|<
literal|0
operator|)
operator|||
operator|(
name|startDelim
operator|>
name|stopDelim
operator|)
operator|)
operator|&&
operator|(
name|stopDelim
operator|>=
literal|0
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"stop delimiter with no start delimiter: "
operator|+
name|val
argument_list|)
throw|;
block|}
comment|// At this point, we have found a variable placeholder so
comment|// we must perform a variable substitution on it.
comment|// Using the start and stop delimiter indices, extract
comment|// the first, deepest nested variable placeholder.
name|String
name|variable
init|=
name|val
operator|.
name|substring
argument_list|(
name|startDelim
operator|+
name|DELIM_START
operator|.
name|length
argument_list|()
argument_list|,
name|stopDelim
argument_list|)
decl_stmt|;
comment|// Verify that this is not a recursive variable reference.
if|if
condition|(
name|cycleMap
operator|.
name|get
argument_list|(
name|variable
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"recursive variable reference: "
operator|+
name|variable
argument_list|)
throw|;
block|}
comment|// Get the value of the deepest nested variable placeholder.
comment|// Try to configuration properties first.
name|String
name|substValue
init|=
operator|(
name|configProps
operator|!=
literal|null
operator|)
condition|?
name|configProps
operator|.
name|getProperty
argument_list|(
name|variable
argument_list|,
literal|null
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|substValue
operator|==
literal|null
condition|)
block|{
comment|// Ignore unknown property values.
name|substValue
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|variable
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
comment|// Remove the found variable from the cycle map, since
comment|// it may appear more than once in the value and we don't
comment|// want such situations to appear as a recursive reference.
name|cycleMap
operator|.
name|remove
argument_list|(
name|variable
argument_list|)
expr_stmt|;
comment|// Append the leading characters, the substituted value of
comment|// the variable, and the trailing characters to get the new
comment|// value.
name|val
operator|=
name|val
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|startDelim
argument_list|)
operator|+
name|substValue
operator|+
name|val
operator|.
name|substring
argument_list|(
name|stopDelim
operator|+
name|DELIM_STOP
operator|.
name|length
argument_list|()
argument_list|,
name|val
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now perform substitution again, since there could still
comment|// be substitutions to make.
name|val
operator|=
name|substVars
argument_list|(
name|val
argument_list|,
name|currentKey
argument_list|,
name|cycleMap
argument_list|,
name|configProps
argument_list|)
expr_stmt|;
comment|// Return the value.
return|return
name|val
return|;
block|}
block|}
end_class

end_unit

