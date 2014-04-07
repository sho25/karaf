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
name|features
operator|.
name|internal
operator|.
name|resolver
package|;
end_package

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|Slf4jResolverLog
extends|extends
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|resolver
operator|.
name|Logger
block|{
specifier|private
specifier|final
name|Logger
name|logger
decl_stmt|;
specifier|public
name|Slf4jResolverLog
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|super
argument_list|(
name|LOG_DEBUG
argument_list|)
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doLog
parameter_list|(
name|int
name|level
parameter_list|,
name|String
name|msg
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
switch|switch
condition|(
name|level
condition|)
block|{
case|case
name|LOG_ERROR
case|:
name|logger
operator|.
name|error
argument_list|(
name|msg
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
break|break;
case|case
name|LOG_WARNING
case|:
name|logger
operator|.
name|warn
argument_list|(
name|msg
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
break|break;
case|case
name|LOG_INFO
case|:
name|logger
operator|.
name|info
argument_list|(
name|msg
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
break|break;
case|case
name|LOG_DEBUG
case|:
name|logger
operator|.
name|debug
argument_list|(
name|msg
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

end_unit

