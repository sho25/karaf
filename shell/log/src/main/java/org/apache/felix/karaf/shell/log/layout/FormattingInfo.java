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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|log
operator|.
name|layout
package|;
end_package

begin_comment
comment|/**  * Copied from log4j  */
end_comment

begin_comment
comment|/**    FormattingInfo instances contain the information obtained when parsing    formatting modifiers in conversion modifiers.     @author<a href=mailto:jim_cakalic@na.biomerieux.com>Jim Cakalic</a>    @author Ceki G&uuml;lc&uuml;     @since 0.8.2  */
end_comment

begin_class
specifier|public
class|class
name|FormattingInfo
block|{
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
name|void
name|reset
parameter_list|()
block|{
name|min
operator|=
operator|-
literal|1
expr_stmt|;
name|max
operator|=
literal|0x7FFFFFFF
expr_stmt|;
name|leftAlign
operator|=
literal|false
expr_stmt|;
block|}
name|void
name|dump
parameter_list|()
block|{
comment|//LogLog.debug("min="+min+", max="+max+", leftAlign="+leftAlign);
block|}
block|}
end_class

end_unit

