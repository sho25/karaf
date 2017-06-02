begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|eclipse
operator|.
name|osgi
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|NLS
block|{
specifier|public
specifier|static
name|void
name|initializeMessages
parameter_list|(
specifier|final
name|String
name|bundleName
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|String
name|resource
init|=
name|bundleName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|".properties"
decl_stmt|;
specifier|final
name|InputStream
name|input
init|=
name|clazz
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
decl_stmt|;
try|try
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|input
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|properties
operator|.
name|stringPropertyNames
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
try|try
block|{
name|Field
name|field
init|=
name|clazz
operator|.
name|getDeclaredField
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|field
operator|.
name|set
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
try|try
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
specifier|public
specifier|static
name|String
name|bind
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|binding
parameter_list|)
block|{
return|return
name|bind
argument_list|(
name|message
argument_list|,
operator|new
name|Object
index|[]
block|{
name|binding
block|}
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|bind
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|binding1
parameter_list|,
name|Object
name|binding2
parameter_list|)
block|{
return|return
name|bind
argument_list|(
name|message
argument_list|,
operator|new
name|Object
index|[]
block|{
name|binding1
block|,
name|binding2
block|}
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|bind
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
index|[]
name|bindings
parameter_list|)
block|{
name|int
name|length
init|=
name|message
operator|.
name|length
argument_list|()
decl_stmt|;
comment|//estimate correct size of string buffer to avoid growth
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|message
operator|.
name|length
argument_list|()
operator|+
operator|(
name|bindings
operator|!=
literal|null
condition|?
name|bindings
operator|.
name|length
operator|*
literal|5
else|:
literal|0
operator|)
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|message
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'{'
case|:
name|int
name|index
init|=
name|message
operator|.
name|indexOf
argument_list|(
literal|'}'
argument_list|,
name|i
argument_list|)
decl_stmt|;
comment|// if we don't have a matching closing brace then...
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
block|}
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|length
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
block|}
comment|// look for a substitution
name|int
name|number
init|=
operator|-
literal|1
decl_stmt|;
try|try
block|{
name|number
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|message
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|bindings
operator|==
literal|null
operator|||
name|number
operator|>=
name|bindings
operator|.
name|length
operator|||
name|number
operator|<
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"<missing argument>"
argument_list|)
expr_stmt|;
comment|//$NON-NLS-1$
name|i
operator|=
name|index
expr_stmt|;
break|break;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|bindings
index|[
name|number
index|]
argument_list|)
expr_stmt|;
name|i
operator|=
name|index
expr_stmt|;
break|break;
case|case
literal|'\''
case|:
comment|// if a single quote is the last char on the line then skip it
name|int
name|nextIndex
init|=
name|i
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|nextIndex
operator|>=
name|length
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
block|}
name|char
name|next
init|=
name|message
operator|.
name|charAt
argument_list|(
name|nextIndex
argument_list|)
decl_stmt|;
comment|// if the next char is another single quote then write out one
if|if
condition|(
name|next
operator|==
literal|'\''
condition|)
block|{
name|i
operator|++
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
block|}
comment|// otherwise we want to read until we get to the next single quote
name|index
operator|=
name|message
operator|.
name|indexOf
argument_list|(
literal|'\''
argument_list|,
name|nextIndex
argument_list|)
expr_stmt|;
comment|// if there are no more in the string, then skip it
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
block|}
comment|// otherwise write out the chars inside the quotes
name|buffer
operator|.
name|append
argument_list|(
name|message
operator|.
name|substring
argument_list|(
name|nextIndex
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|i
operator|=
name|index
expr_stmt|;
break|break;
default|default :
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

