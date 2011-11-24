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
name|diagnostic
operator|.
name|command
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|diagnostic
operator|.
name|core
operator|.
name|DumpDestination
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
name|diagnostic
operator|.
name|core
operator|.
name|DumpProvider
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
name|diagnostic
operator|.
name|core
operator|.
name|common
operator|.
name|DirectoryDumpDestination
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
name|diagnostic
operator|.
name|core
operator|.
name|common
operator|.
name|ZipDumpDestination
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

begin_comment
comment|/**  * Command to create dump from shell.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"dev"
argument_list|,
name|name
operator|=
literal|"create-dump"
argument_list|,
name|description
operator|=
literal|"Creates zip archive with diagnostic info."
argument_list|)
specifier|public
class|class
name|DumpCommand
extends|extends
name|OsgiCommandSupport
block|{
comment|/**      * Registered dump providers.      */
specifier|private
name|List
argument_list|<
name|DumpProvider
argument_list|>
name|providers
init|=
operator|new
name|LinkedList
argument_list|<
name|DumpProvider
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Output format of the filename if not defined otherwise      */
specifier|private
name|SimpleDateFormat
name|dumpFormat
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd_HHmmss"
argument_list|)
decl_stmt|;
comment|/**      * Directory switch.      */
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-d"
argument_list|,
name|aliases
operator|=
literal|"--directory"
argument_list|,
name|description
operator|=
literal|"Creates dump in a directory in place of a ZIP archive"
argument_list|)
name|boolean
name|directory
decl_stmt|;
comment|/**      * Name of created directory or archive.      */
annotation|@
name|Argument
argument_list|(
name|name
operator|=
literal|"name"
argument_list|,
name|description
operator|=
literal|"Name of created zip or directory"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
name|String
name|fileName
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|DumpDestination
name|destination
decl_stmt|;
if|if
condition|(
name|providers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
literal|"Unable to create dump. No providers were found"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// create default file name if none provided
if|if
condition|(
name|fileName
operator|==
literal|null
operator|||
name|fileName
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|fileName
operator|=
name|dumpFormat
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|directory
condition|)
block|{
name|fileName
operator|+=
literal|".zip"
expr_stmt|;
block|}
block|}
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
comment|// if directory switch is on, create dump in directory
if|if
condition|(
name|directory
condition|)
block|{
name|destination
operator|=
operator|new
name|DirectoryDumpDestination
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|destination
operator|=
operator|new
name|ZipDumpDestination
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DumpProvider
name|provider
range|:
name|providers
control|)
block|{
name|provider
operator|.
name|createDump
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
name|destination
operator|.
name|save
argument_list|()
expr_stmt|;
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
literal|"Diagnostic dump created."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|/**      * Sets dump providers to use.      *       * @param providers Providers.      */
specifier|public
name|void
name|setProviders
parameter_list|(
name|List
argument_list|<
name|DumpProvider
argument_list|>
name|providers
parameter_list|)
block|{
name|this
operator|.
name|providers
operator|=
name|providers
expr_stmt|;
block|}
block|}
end_class

end_unit

