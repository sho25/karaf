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
name|log
operator|.
name|command
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
name|api
operator|.
name|action
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
operator|.
name|Completion
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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Reference
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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
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
name|support
operator|.
name|completers
operator|.
name|StringsCompleter
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
name|log
operator|.
name|LogService
import|;
end_import

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

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"log"
argument_list|,
name|name
operator|=
literal|"log"
argument_list|,
name|description
operator|=
literal|"Log a message."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|LogEntry
implements|implements
name|Action
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"message"
argument_list|,
name|description
operator|=
literal|"The message to log"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|message
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--level"
argument_list|,
name|aliases
operator|=
block|{
literal|"-l"
block|}
argument_list|,
name|description
operator|=
literal|"The level the message will be logged at"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
annotation|@
name|Completion
argument_list|(
name|value
operator|=
name|StringsCompleter
operator|.
name|class
argument_list|,
name|values
operator|=
block|{
literal|"DEBUG"
block|,
literal|"INFO"
block|,
literal|"WARNING"
block|,
literal|"ERROR"
block|}
argument_list|)
specifier|private
name|String
name|level
init|=
literal|"INFO"
decl_stmt|;
annotation|@
name|Reference
name|LogService
name|logService
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|mappings
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|LogEntry
parameter_list|()
block|{
name|mappings
operator|.
name|put
argument_list|(
literal|"ERROR"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mappings
operator|.
name|put
argument_list|(
literal|"WARNING"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|mappings
operator|.
name|put
argument_list|(
literal|"INFO"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|mappings
operator|.
name|put
argument_list|(
literal|"DEBUG"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|logService
operator|.
name|log
argument_list|(
name|toLevel
argument_list|(
name|level
operator|.
name|toUpperCase
argument_list|()
argument_list|)
argument_list|,
name|message
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|int
name|toLevel
parameter_list|(
name|String
name|logLevel
parameter_list|)
block|{
name|Integer
name|level
init|=
name|mappings
operator|.
name|get
argument_list|(
name|logLevel
argument_list|)
decl_stmt|;
if|if
condition|(
name|level
operator|==
literal|null
condition|)
block|{
name|level
operator|=
literal|3
expr_stmt|;
block|}
return|return
name|level
return|;
block|}
block|}
end_class

end_unit
