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
name|impl
operator|.
name|console
operator|.
name|commands
operator|.
name|help
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Session
import|;
end_import

begin_class
specifier|public
class|class
name|SingleCommandHelpProvider
implements|implements
name|HelpProvider
block|{
specifier|public
name|String
name|getHelp
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|.
name|indexOf
argument_list|(
literal|'|'
argument_list|)
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"command|"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|"command|"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|ps
init|=
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Session
name|s
init|=
name|session
operator|.
name|getFactory
argument_list|()
operator|.
name|create
argument_list|(
name|bais
argument_list|,
name|ps
argument_list|,
name|ps
argument_list|,
name|session
argument_list|)
decl_stmt|;
name|s
operator|.
name|put
argument_list|(
name|Session
operator|.
name|SCOPE
argument_list|,
name|session
operator|.
name|get
argument_list|(
name|Session
operator|.
name|SCOPE
argument_list|)
argument_list|)
expr_stmt|;
name|s
operator|.
name|put
argument_list|(
name|Session
operator|.
name|SUBSHELL
argument_list|,
name|session
operator|.
name|get
argument_list|(
name|Session
operator|.
name|SUBSHELL
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|s
operator|.
name|execute
argument_list|(
name|path
operator|+
literal|" --help"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
finally|finally
block|{
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|baos
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

