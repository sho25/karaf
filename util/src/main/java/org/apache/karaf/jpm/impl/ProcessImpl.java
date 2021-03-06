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
name|jpm
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

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
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InterruptedIOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jpm
operator|.
name|Process
import|;
end_import

begin_class
specifier|public
class|class
name|ProcessImpl
implements|implements
name|Process
block|{
comment|/**      *       */
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8140632422386086507L
decl_stmt|;
specifier|private
name|int
name|pid
decl_stmt|;
comment|//private File input;
comment|//private File output;
comment|//private File error;
specifier|public
name|ProcessImpl
parameter_list|(
name|int
name|pid
comment|/*, File input, File output, File error*/
parameter_list|)
block|{
name|this
operator|.
name|pid
operator|=
name|pid
expr_stmt|;
comment|//this.input = input;
comment|//this.output = output;
comment|//this.error = error;
block|}
specifier|public
name|int
name|getPid
parameter_list|()
block|{
return|return
name|pid
return|;
block|}
specifier|public
name|boolean
name|isRunning
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|ScriptUtils
operator|.
name|isWindows
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"${pid}"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|pid
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|ret
init|=
name|ScriptUtils
operator|.
name|execute
argument_list|(
literal|"running"
argument_list|,
name|props
argument_list|)
decl_stmt|;
return|return
name|ret
operator|==
literal|0
return|;
block|}
else|else
block|{
try|try
block|{
name|java
operator|.
name|lang
operator|.
name|Process
name|process
init|=
operator|new
name|java
operator|.
name|lang
operator|.
name|ProcessBuilder
argument_list|(
literal|"ps"
argument_list|,
literal|"-p"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|pid
argument_list|)
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
try|try
init|(
name|BufferedReader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|process
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|r
operator|.
name|readLine
argument_list|()
expr_stmt|;
comment|// skip headers
name|String
name|s
init|=
name|r
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|boolean
name|running
init|=
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|length
argument_list|()
operator|>
literal|0
decl_stmt|;
name|process
operator|.
name|waitFor
argument_list|()
expr_stmt|;
return|return
name|running
return|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InterruptedIOException
argument_list|()
throw|;
block|}
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|ret
decl_stmt|;
if|if
condition|(
name|ScriptUtils
operator|.
name|isWindows
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"${pid}"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|pid
argument_list|)
argument_list|)
expr_stmt|;
name|ret
operator|=
name|ScriptUtils
operator|.
name|execute
argument_list|(
literal|"destroy"
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ret
operator|=
name|ScriptUtils
operator|.
name|executeProcess
argument_list|(
operator|new
name|java
operator|.
name|lang
operator|.
name|ProcessBuilder
argument_list|(
literal|"kill"
argument_list|,
literal|"-9"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|pid
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ret
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to destroy process, it may already be terminated"
argument_list|)
throw|;
block|}
block|}
comment|/*     public OutputStream getInputStream() throws FileNotFoundException {         return new FileOutputStream(input);     }      public InputStream getOutputStream() throws FileNotFoundException {         return new FileInputStream(output);     }      public InputStream getErrorStream() throws FileNotFoundException {         return new FileInputStream(error);     }     */
specifier|public
name|int
name|waitFor
parameter_list|()
throws|throws
name|InterruptedException
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|int
name|exitValue
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
specifier|static
name|Process
name|create
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|command
parameter_list|)
throws|throws
name|IOException
block|{
comment|//File input = Files.createTempFile("jpm.", ".input").toFile();
comment|//File output = Files.createTempFile("jpm.", ".output").toFile();
comment|//File error = Files.createTempFile("jpm.", ".error").toFile();
name|File
name|pidFile
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
literal|"jpm."
argument_list|,
literal|".pid"
argument_list|)
operator|.
name|toFile
argument_list|()
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|//props.put("${in.file}", input.getCanonicalPath());
comment|//props.put("${out.file}", output.getCanonicalPath());
comment|//props.put("${err.file}", error.getCanonicalPath());
name|props
operator|.
name|put
argument_list|(
literal|"${pid.file}"
argument_list|,
name|pidFile
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"${dir}"
argument_list|,
name|dir
operator|!=
literal|null
condition|?
name|dir
operator|.
name|getCanonicalPath
argument_list|()
else|:
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|ScriptUtils
operator|.
name|isWindows
argument_list|()
condition|)
block|{
name|command
operator|=
name|command
operator|.
name|replaceAll
argument_list|(
literal|"\""
argument_list|,
literal|"\"\""
argument_list|)
expr_stmt|;
block|}
name|props
operator|.
name|put
argument_list|(
literal|"${command}"
argument_list|,
name|command
argument_list|)
expr_stmt|;
name|int
name|ret
init|=
name|ScriptUtils
operator|.
name|execute
argument_list|(
literal|"start"
argument_list|,
name|props
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to create process (error code: "
operator|+
name|ret
operator|+
literal|")"
argument_list|)
throw|;
block|}
name|int
name|pid
init|=
name|readPid
argument_list|(
name|pidFile
argument_list|)
decl_stmt|;
return|return
operator|new
name|ProcessImpl
argument_list|(
name|pid
comment|/*, input, output, error*/
argument_list|)
return|;
block|}
finally|finally
block|{
name|pidFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|Process
name|attach
parameter_list|(
name|int
name|pid
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|ProcessImpl
argument_list|(
name|pid
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|int
name|readPid
parameter_list|(
name|File
name|pidFile
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|pidFile
argument_list|)
init|)
block|{
name|BufferedReader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|pidString
init|=
name|r
operator|.
name|readLine
argument_list|()
decl_stmt|;
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|pidString
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

