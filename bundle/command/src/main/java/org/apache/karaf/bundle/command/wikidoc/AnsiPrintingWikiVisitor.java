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
name|bundle
operator|.
name|command
operator|.
name|wikidoc
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
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
operator|.
name|Attribute
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
operator|.
name|Color
import|;
end_import

begin_comment
comment|/**  * Translates the Wiki tags to Ansi escape sequences to display them on the console  */
end_comment

begin_class
specifier|public
class|class
name|AnsiPrintingWikiVisitor
implements|implements
name|WikiVisitor
block|{
specifier|private
name|PrintStream
name|out
decl_stmt|;
specifier|public
name|AnsiPrintingWikiVisitor
parameter_list|(
name|PrintStream
name|out
parameter_list|)
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|heading
parameter_list|(
name|int
name|level
parameter_list|,
name|String
name|header
parameter_list|)
block|{
name|this
operator|.
name|out
operator|.
name|print
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
name|header
argument_list|)
operator|.
name|a
argument_list|(
name|Attribute
operator|.
name|INTENSITY_BOLD_OFF
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|link
parameter_list|(
name|String
name|target
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|out
operator|.
name|print
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Color
operator|.
name|YELLOW
argument_list|)
operator|.
name|a
argument_list|(
name|target
argument_list|)
operator|.
name|fg
argument_list|(
name|Color
operator|.
name|DEFAULT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|enumeration
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|out
operator|.
name|print
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
literal|" * "
argument_list|)
operator|.
name|fg
argument_list|(
name|Color
operator|.
name|CYAN
argument_list|)
operator|.
name|a
argument_list|(
name|text
argument_list|)
operator|.
name|fg
argument_list|(
name|Color
operator|.
name|DEFAULT
argument_list|)
operator|.
name|a
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|text
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|out
operator|.
name|print
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

