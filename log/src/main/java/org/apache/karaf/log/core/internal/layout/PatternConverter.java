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
name|core
operator|.
name|internal
operator|.
name|layout
package|;
end_package

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|logging
operator|.
name|spi
operator|.
name|PaxLoggingEvent
import|;
end_import

begin_comment
comment|/**  *<p>PatternConverter is an abtract class that provides the  * formatting functionality that derived classes need.</p>  *  *<p>Conversion specifiers in a conversion patterns are parsed to  * individual PatternConverters. Each of which is responsible for  * converting a logging event in a converter specific manner.</p>  *  * @since 0.8.2  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PatternConverter
block|{
specifier|public
name|PatternConverter
name|next
decl_stmt|;
name|int
name|min
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|max
init|=
literal|0x7FFFFFFF
decl_stmt|;
name|boolean
name|leftAlign
init|=
literal|false
decl_stmt|;
specifier|protected
name|PatternConverter
parameter_list|()
block|{     }
specifier|protected
name|PatternConverter
parameter_list|(
name|FormattingInfo
name|fi
parameter_list|)
block|{
name|min
operator|=
name|fi
operator|.
name|min
expr_stmt|;
name|max
operator|=
name|fi
operator|.
name|max
expr_stmt|;
name|leftAlign
operator|=
name|fi
operator|.
name|leftAlign
expr_stmt|;
block|}
comment|/**      * Derived pattern converters must override this method in order to      * convert conversion specifiers in the correct way.      *      * @param event The {@link PaxLoggingEvent} to convert.      * @return The {@link String} representing the {@link PaxLoggingEvent}.      */
specifier|abstract
specifier|protected
name|String
name|convert
parameter_list|(
name|PaxLoggingEvent
name|event
parameter_list|)
function_decl|;
comment|/**      * A template method for formatting in a converter specific way.      *      * @param sbuf The {@link StringBuffer} used for formatting the {@link PaxLoggingEvent}.      * @param e    The {@link PaxLoggingEvent} to format.      */
specifier|public
name|void
name|format
parameter_list|(
name|StringBuffer
name|sbuf
parameter_list|,
name|PaxLoggingEvent
name|e
parameter_list|)
block|{
name|String
name|s
init|=
name|convert
argument_list|(
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
if|if
condition|(
literal|0
operator|<
name|min
condition|)
name|spacePad
argument_list|(
name|sbuf
argument_list|,
name|min
argument_list|)
expr_stmt|;
return|return;
block|}
name|int
name|len
init|=
name|s
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|len
operator|>
name|max
condition|)
name|sbuf
operator|.
name|append
argument_list|(
name|s
operator|.
name|substring
argument_list|(
name|len
operator|-
name|max
argument_list|)
argument_list|)
expr_stmt|;
elseif|else
if|if
condition|(
name|len
operator|<
name|min
condition|)
block|{
if|if
condition|(
name|leftAlign
condition|)
block|{
name|sbuf
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|spacePad
argument_list|(
name|sbuf
argument_list|,
name|min
operator|-
name|len
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|spacePad
argument_list|(
name|sbuf
argument_list|,
name|min
operator|-
name|len
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
else|else
name|sbuf
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
specifier|static
name|String
index|[]
name|SPACES
init|=
block|{
literal|" "
block|,
literal|"  "
block|,
literal|"    "
block|,
literal|"        "
block|,
comment|//1,2,4,8 spaces
literal|"                "
block|,
comment|// 16 spaces
literal|"                                "
block|}
decl_stmt|;
comment|// 32 spaces
comment|/**      * Fast space padding method.      *      * @param sbuf   The {@link StringBuffer} used for space padding.      * @param length The padding length.      */
specifier|public
name|void
name|spacePad
parameter_list|(
name|StringBuffer
name|sbuf
parameter_list|,
name|int
name|length
parameter_list|)
block|{
while|while
condition|(
name|length
operator|>=
literal|32
condition|)
block|{
name|sbuf
operator|.
name|append
argument_list|(
name|SPACES
index|[
literal|5
index|]
argument_list|)
expr_stmt|;
name|length
operator|-=
literal|32
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|4
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
if|if
condition|(
operator|(
name|length
operator|&
operator|(
literal|1
operator|<<
name|i
operator|)
operator|)
operator|!=
literal|0
condition|)
block|{
name|sbuf
operator|.
name|append
argument_list|(
name|SPACES
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

