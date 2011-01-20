begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|slf4j
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|helpers
operator|.
name|MarkerIgnoringBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|helpers
operator|.
name|MessageFormatter
import|;
end_import

begin_comment
comment|/**  * A simple logger that can be controlled from the ssh client  */
end_comment

begin_class
specifier|public
class|class
name|SimpleLogger
extends|extends
name|MarkerIgnoringBase
block|{
specifier|public
specifier|static
specifier|final
name|int
name|ERROR
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|WARN
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|INFO
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DEBUG
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TRACE
init|=
literal|4
decl_stmt|;
specifier|private
specifier|static
name|int
name|level
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
name|long
name|startTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LINE_SEPARATOR
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|int
name|getLevel
parameter_list|()
block|{
return|return
name|level
return|;
block|}
specifier|public
specifier|static
name|void
name|setLevel
parameter_list|(
name|int
name|level
parameter_list|)
block|{
name|SimpleLogger
operator|.
name|level
operator|=
name|level
expr_stmt|;
block|}
specifier|private
name|String
name|name
decl_stmt|;
name|SimpleLogger
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|boolean
name|isTraceEnabled
parameter_list|()
block|{
return|return
name|isLogEnabled
argument_list|(
name|TRACE
argument_list|)
return|;
block|}
specifier|public
name|void
name|trace
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
name|TRACE
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|trace
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
name|arg
parameter_list|)
block|{
name|log
argument_list|(
name|TRACE
argument_list|,
name|format
argument_list|,
name|arg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|trace
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
name|arg1
parameter_list|,
name|Object
name|arg2
parameter_list|)
block|{
name|log
argument_list|(
name|TRACE
argument_list|,
name|format
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|trace
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
index|[]
name|argArray
parameter_list|)
block|{
name|log
argument_list|(
name|TRACE
argument_list|,
name|format
argument_list|,
name|argArray
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|trace
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|log
argument_list|(
name|TRACE
argument_list|,
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDebugEnabled
parameter_list|()
block|{
return|return
name|isLogEnabled
argument_list|(
name|DEBUG
argument_list|)
return|;
block|}
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
name|DEBUG
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
name|arg
parameter_list|)
block|{
name|log
argument_list|(
name|DEBUG
argument_list|,
name|format
argument_list|,
name|arg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
name|arg1
parameter_list|,
name|Object
name|arg2
parameter_list|)
block|{
name|log
argument_list|(
name|DEBUG
argument_list|,
name|format
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
index|[]
name|argArray
parameter_list|)
block|{
name|log
argument_list|(
name|DEBUG
argument_list|,
name|format
argument_list|,
name|argArray
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|log
argument_list|(
name|DEBUG
argument_list|,
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isInfoEnabled
parameter_list|()
block|{
return|return
name|isLogEnabled
argument_list|(
name|INFO
argument_list|)
return|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
name|INFO
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
name|arg
parameter_list|)
block|{
name|log
argument_list|(
name|INFO
argument_list|,
name|format
argument_list|,
name|arg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
name|arg1
parameter_list|,
name|Object
name|arg2
parameter_list|)
block|{
name|log
argument_list|(
name|INFO
argument_list|,
name|format
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
index|[]
name|argArray
parameter_list|)
block|{
name|log
argument_list|(
name|INFO
argument_list|,
name|format
argument_list|,
name|argArray
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|log
argument_list|(
name|INFO
argument_list|,
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isWarnEnabled
parameter_list|()
block|{
return|return
name|isLogEnabled
argument_list|(
name|WARN
argument_list|)
return|;
block|}
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
name|WARN
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
name|arg
parameter_list|)
block|{
name|log
argument_list|(
name|WARN
argument_list|,
name|format
argument_list|,
name|arg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
name|arg1
parameter_list|,
name|Object
name|arg2
parameter_list|)
block|{
name|log
argument_list|(
name|WARN
argument_list|,
name|format
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
index|[]
name|argArray
parameter_list|)
block|{
name|log
argument_list|(
name|WARN
argument_list|,
name|format
argument_list|,
name|argArray
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|log
argument_list|(
name|WARN
argument_list|,
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isErrorEnabled
parameter_list|()
block|{
return|return
name|isLogEnabled
argument_list|(
name|ERROR
argument_list|)
return|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
name|ERROR
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
name|arg
parameter_list|)
block|{
name|log
argument_list|(
name|ERROR
argument_list|,
name|format
argument_list|,
name|arg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
name|arg1
parameter_list|,
name|Object
name|arg2
parameter_list|)
block|{
name|log
argument_list|(
name|ERROR
argument_list|,
name|format
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
index|[]
name|argArray
parameter_list|)
block|{
name|log
argument_list|(
name|ERROR
argument_list|,
name|format
argument_list|,
name|argArray
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|log
argument_list|(
name|ERROR
argument_list|,
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isLogEnabled
parameter_list|(
name|int
name|level
parameter_list|)
block|{
return|return
name|SimpleLogger
operator|.
name|level
operator|>=
name|level
return|;
block|}
specifier|protected
name|void
name|log
parameter_list|(
name|int
name|level
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|isLogEnabled
argument_list|(
name|level
argument_list|)
condition|)
block|{
name|doLog
argument_list|(
name|level
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|log
parameter_list|(
name|int
name|level
parameter_list|,
name|String
name|format
parameter_list|,
name|Object
name|arg
parameter_list|)
block|{
if|if
condition|(
name|isLogEnabled
argument_list|(
name|level
argument_list|)
condition|)
block|{
name|String
name|msg
init|=
name|MessageFormatter
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|arg
argument_list|)
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|doLog
argument_list|(
name|level
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|log
parameter_list|(
name|int
name|level
parameter_list|,
name|String
name|format
parameter_list|,
name|Object
name|arg1
parameter_list|,
name|Object
name|arg2
parameter_list|)
block|{
if|if
condition|(
name|isLogEnabled
argument_list|(
name|level
argument_list|)
condition|)
block|{
name|String
name|msg
init|=
name|MessageFormatter
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|doLog
argument_list|(
name|level
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|log
parameter_list|(
name|int
name|level
parameter_list|,
name|String
name|format
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
block|{
if|if
condition|(
name|isLogEnabled
argument_list|(
name|level
argument_list|)
condition|)
block|{
name|String
name|msg
init|=
name|MessageFormatter
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|args
argument_list|)
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|doLog
argument_list|(
name|level
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|log
parameter_list|(
name|int
name|level
parameter_list|,
name|String
name|msg
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|isLogEnabled
argument_list|(
name|level
argument_list|)
condition|)
block|{
name|doLog
argument_list|(
name|level
argument_list|,
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
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
name|t
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|long
name|millis
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|millis
operator|-
name|startTime
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" ["
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"] "
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|level
condition|)
block|{
case|case
name|TRACE
case|:
name|buf
operator|.
name|append
argument_list|(
literal|"TRACE"
argument_list|)
expr_stmt|;
break|break;
case|case
name|DEBUG
case|:
name|buf
operator|.
name|append
argument_list|(
literal|"DEBUG"
argument_list|)
expr_stmt|;
break|break;
case|case
name|INFO
case|:
name|buf
operator|.
name|append
argument_list|(
literal|"INFO"
argument_list|)
expr_stmt|;
break|break;
case|case
name|WARN
case|:
name|buf
operator|.
name|append
argument_list|(
literal|"WARN"
argument_list|)
expr_stmt|;
break|break;
case|case
name|ERROR
case|:
name|buf
operator|.
name|append
argument_list|(
literal|"ERROR"
argument_list|)
expr_stmt|;
break|break;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" - "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|LINE_SEPARATOR
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
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
block|}
block|}
end_class

end_unit

