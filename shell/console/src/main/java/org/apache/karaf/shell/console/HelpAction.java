begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|console
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|jline
operator|.
name|Terminal
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
name|gogo
operator|.
name|commands
operator|.
name|Action
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
name|gogo
operator|.
name|commands
operator|.
name|Argument
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
name|gogo
operator|.
name|commands
operator|.
name|Command
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
name|gogo
operator|.
name|commands
operator|.
name|basic
operator|.
name|AbstractCommand
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
name|gogo
operator|.
name|commands
operator|.
name|basic
operator|.
name|DefaultActionPreparator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|command
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  * Displays help on the available commands  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"*"
argument_list|,
name|name
operator|=
literal|"help"
argument_list|,
name|description
operator|=
literal|"Displays this help or help about a command"
argument_list|)
specifier|public
class|class
name|HelpAction
extends|extends
name|AbstractAction
block|{
annotation|@
name|Argument
argument_list|(
name|name
operator|=
literal|"command"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|description
operator|=
literal|"The command to get help for"
argument_list|)
specifier|private
name|String
name|command
decl_stmt|;
specifier|public
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|command
operator|==
literal|null
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|(
name|Set
argument_list|<
name|String
argument_list|>
operator|)
name|session
operator|.
name|get
argument_list|(
literal|".commands"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|names
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Terminal
name|term
init|=
operator|(
name|Terminal
operator|)
name|session
operator|.
name|get
argument_list|(
literal|".jline.terminal"
argument_list|)
decl_stmt|;
name|PrintStream
name|out
init|=
name|System
operator|.
name|out
decl_stmt|;
name|SortedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|commands
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|String
name|description
init|=
literal|null
decl_stmt|;
name|Function
name|function
init|=
operator|(
name|Function
operator|)
name|session
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|function
operator|=
name|unProxy
argument_list|(
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|function
operator|instanceof
name|AbstractCommand
condition|)
block|{
try|try
block|{
name|Method
name|mth
init|=
name|AbstractCommand
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"createNewAction"
argument_list|)
decl_stmt|;
name|mth
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Action
name|action
init|=
operator|(
name|Action
operator|)
name|mth
operator|.
name|invoke
argument_list|(
name|function
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|clazz
init|=
name|action
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Command
name|ann
init|=
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
name|description
operator|=
name|ann
operator|.
name|description
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{                         }
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"*:"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|commands
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Post process the commands list
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
literal|"COMMANDS"
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|commands
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"        "
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|NameScoping
operator|.
name|getCommandNameWithoutGlobalPrefix
argument_list|(
name|session
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
name|key
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DefaultActionPreparator
operator|.
name|printFormatted
argument_list|(
literal|"                "
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|term
operator|!=
literal|null
condition|?
name|term
operator|.
name|getWidth
argument_list|()
else|:
literal|80
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|session
operator|.
name|execute
argument_list|(
name|command
operator|+
literal|" --help"
argument_list|)
return|;
block|}
block|}
specifier|protected
name|Function
name|unProxy
parameter_list|(
name|Function
name|function
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|function
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"CommandProxy"
argument_list|)
condition|)
block|{
name|Field
name|contextField
init|=
name|function
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"context"
argument_list|)
decl_stmt|;
name|Field
name|referenceField
init|=
name|function
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"reference"
argument_list|)
decl_stmt|;
name|contextField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|referenceField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|BundleContext
name|context
init|=
operator|(
name|BundleContext
operator|)
name|contextField
operator|.
name|get
argument_list|(
name|function
argument_list|)
decl_stmt|;
name|ServiceReference
name|reference
init|=
operator|(
name|ServiceReference
operator|)
name|referenceField
operator|.
name|get
argument_list|(
name|function
argument_list|)
decl_stmt|;
name|Object
name|target
init|=
name|context
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|target
operator|instanceof
name|Function
condition|)
block|{
name|function
operator|=
operator|(
name|Function
operator|)
name|target
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|context
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{         }
return|return
name|function
return|;
block|}
block|}
end_class

end_unit

