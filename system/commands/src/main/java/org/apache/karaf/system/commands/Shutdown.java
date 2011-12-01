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
name|system
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
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
name|karaf
operator|.
name|shell
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
name|karaf
operator|.
name|shell
operator|.
name|commands
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|OsgiCommandSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|system
operator|.
name|SystemService
import|;
end_import

begin_comment
comment|/**  * Command to shut down Karaf container.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"system"
argument_list|,
name|name
operator|=
literal|"shutdown"
argument_list|,
name|description
operator|=
literal|"Shutdown Karaf."
argument_list|)
specifier|public
class|class
name|Shutdown
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-f"
argument_list|,
name|aliases
operator|=
literal|"--force"
argument_list|,
name|description
operator|=
literal|"Force the shutdown without confirmation message."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|force
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-r"
argument_list|,
name|aliases
operator|=
literal|"--reboot"
argument_list|,
name|description
operator|=
literal|"Reboot the Karaf container."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|reboot
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-h"
argument_list|,
name|aliases
operator|=
literal|"--halt"
argument_list|,
name|description
operator|=
literal|"Halt the Karaf container."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|halt
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-c"
argument_list|,
name|aliases
operator|=
literal|"--clean"
argument_list|,
name|description
operator|=
literal|"Clean the Karaf container (working directory) during reboot."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|clean
init|=
literal|false
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|name
operator|=
literal|"time"
argument_list|,
name|index
operator|=
literal|0
argument_list|,
name|description
operator|=
literal|"Shutdown after a specified delay. The time argument can have different"
operator|+
literal|" formats. First, it can be an abolute time in the format hh:mm, in which hh is the hour (1 or 2 digits) and mm"
operator|+
literal|" is the minute of the hour (in two digits). Second, it can be in the format +m, in which m is the number of minutes"
operator|+
literal|" to wait. The word now is an alias for +0."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|time
decl_stmt|;
specifier|private
name|SystemService
name|systemService
decl_stmt|;
specifier|public
name|void
name|setSystemService
parameter_list|(
name|SystemService
name|systemService
parameter_list|)
block|{
name|this
operator|.
name|systemService
operator|=
name|systemService
expr_stmt|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|force
condition|)
block|{
if|if
condition|(
name|reboot
condition|)
block|{
name|systemService
operator|.
name|reboot
argument_list|(
name|time
argument_list|,
name|clean
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|systemService
operator|.
name|halt
argument_list|(
name|time
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
for|for
control|(
init|;
condition|;
control|)
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|karafName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|reboot
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Confirm: reboot instance %s (yes/no): "
argument_list|,
name|karafName
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Confirm: halt instance %s (yes/no): "
argument_list|,
name|karafName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|int
name|c
init|=
name|session
operator|.
name|getKeyboard
argument_list|()
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\r'
operator|||
name|c
operator|==
literal|'\n'
condition|)
block|{
break|break;
block|}
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
name|String
name|str
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|str
operator|.
name|equals
argument_list|(
literal|"yes"
argument_list|)
condition|)
block|{
if|if
condition|(
name|reboot
condition|)
block|{
name|systemService
operator|.
name|reboot
argument_list|(
name|time
argument_list|,
name|clean
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|systemService
operator|.
name|halt
argument_list|(
name|time
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

