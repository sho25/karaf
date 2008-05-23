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
name|geronimo
operator|.
name|gshell
operator|.
name|spring
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
name|io
operator|.
name|FileOutputStream
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
name|OutputStream
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
name|java
operator|.
name|util
operator|.
name|Scanner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|clp
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
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|annotation
operator|.
name|CommandComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|common
operator|.
name|io
operator|.
name|PumpStreamHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|support
operator|.
name|OsgiCommandSupport
import|;
end_import

begin_comment
comment|/**  * Creates a new servicemix instance   *  * @version $Rev$ $Date$  */
end_comment

begin_class
annotation|@
name|CommandComponent
argument_list|(
name|id
operator|=
literal|"smx:create"
argument_list|,
name|description
operator|=
literal|"Create a new ServiceMix instance"
argument_list|)
specifier|public
class|class
name|CreateCommand
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"Where to create the new ServiceMix instance"
argument_list|)
specifier|private
name|String
name|instance
init|=
literal|null
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|File
name|serviceMixBase
init|=
operator|new
name|File
argument_list|(
name|instance
argument_list|)
operator|.
name|getCanonicalFile
argument_list|()
decl_stmt|;
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Creating new instance at: @|bold "
operator|+
name|serviceMixBase
operator|+
literal|"|"
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"bin"
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"etc"
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"system"
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"deploy"
argument_list|)
expr_stmt|;
name|mkdir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"data"
argument_list|)
expr_stmt|;
name|copyResourceToDir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"etc/config.properties"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|copyResourceToDir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"etc/org.apache.servicemix.features.cfg"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|copyResourceToDir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"etc/org.apache.servicemix.shell.cfg"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|copyResourceToDir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"etc/org.ops4j.pax.logging.cfg"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|copyResourceToDir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"etc/startup.properties"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|copyResourceToDir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"etc/system.properties"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"${servicemix.home}"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"servicemix.home"
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"${servicemix.base}"
argument_list|,
name|serviceMixBase
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Win"
argument_list|)
condition|)
block|{
name|copyFilteredResourceToDir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"bin/servicemix.bat"
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|copyFilteredResourceToDir
argument_list|(
name|serviceMixBase
argument_list|,
literal|"bin/servicemix"
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|chmod
argument_list|(
operator|new
name|File
argument_list|(
name|serviceMixBase
argument_list|,
literal|"bin/servicemix"
argument_list|)
argument_list|,
literal|"a+x"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
return|return
literal|0
return|;
block|}
specifier|private
name|void
name|copyResourceToDir
parameter_list|(
name|File
name|target
parameter_list|,
name|String
name|resource
parameter_list|,
name|boolean
name|text
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|target
argument_list|,
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|outFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Creating file: @|bold "
operator|+
name|outFile
operator|.
name|getPath
argument_list|()
operator|+
literal|"|"
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|CreateCommand
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|text
condition|)
block|{
comment|// Read it line at a time so that we can use the platform line ending when we write it out.
name|PrintStream
name|out
init|=
operator|new
name|PrintStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|(
name|is
argument_list|)
decl_stmt|;
while|while
condition|(
name|scanner
operator|.
name|hasNextLine
argument_list|()
condition|)
block|{
name|String
name|line
init|=
name|scanner
operator|.
name|nextLine
argument_list|()
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|safeClose
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Binary so just write it out the way it came in.
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|target
argument_list|,
name|resource
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|c
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|is
operator|.
name|read
argument_list|()
operator|)
operator|>=
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|safeClose
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|safeClose
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|copyFilteredResourceToDir
parameter_list|(
name|File
name|target
parameter_list|,
name|String
name|resource
parameter_list|,
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|target
argument_list|,
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|outFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Creating file: @|bold "
operator|+
name|outFile
operator|.
name|getPath
argument_list|()
operator|+
literal|"|"
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|CreateCommand
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
decl_stmt|;
try|try
block|{
comment|// Read it line at a time so that we can use the platform line ending when we write it out.
name|PrintStream
name|out
init|=
operator|new
name|PrintStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|(
name|is
argument_list|)
decl_stmt|;
while|while
condition|(
name|scanner
operator|.
name|hasNextLine
argument_list|()
condition|)
block|{
name|String
name|line
init|=
name|scanner
operator|.
name|nextLine
argument_list|()
decl_stmt|;
name|line
operator|=
name|filter
argument_list|(
name|line
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|safeClose
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|safeClose
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|safeClose
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
return|return;
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ignore
parameter_list|)
block|{ 		}
block|}
specifier|private
name|void
name|safeClose
parameter_list|(
name|OutputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
return|return;
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ignore
parameter_list|)
block|{ 		}
block|}
specifier|private
name|String
name|filter
parameter_list|(
name|String
name|line
parameter_list|,
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|i
range|:
name|props
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|int
name|p1
init|=
name|line
operator|.
name|indexOf
argument_list|(
name|i
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|p1
operator|>=
literal|0
condition|)
block|{
name|String
name|l1
init|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p1
argument_list|)
decl_stmt|;
name|String
name|l2
init|=
name|line
operator|.
name|substring
argument_list|(
name|p1
operator|+
name|i
operator|.
name|getKey
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|line
operator|=
name|l1
operator|+
name|i
operator|.
name|getValue
argument_list|()
operator|+
name|l2
expr_stmt|;
block|}
block|}
return|return
name|line
return|;
block|}
specifier|private
name|void
name|mkdir
parameter_list|(
name|File
name|serviceMixBase
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|serviceMixBase
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Creating dir:  @|bold "
operator|+
name|file
operator|.
name|getPath
argument_list|()
operator|+
literal|"|"
argument_list|)
expr_stmt|;
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|int
name|chmod
parameter_list|(
name|File
name|serviceFile
parameter_list|,
name|String
name|mode
parameter_list|)
throws|throws
name|Exception
block|{
name|ProcessBuilder
name|builder
init|=
operator|new
name|ProcessBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|command
argument_list|(
literal|"chmod"
argument_list|,
name|mode
argument_list|,
name|serviceFile
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|Process
name|p
init|=
name|builder
operator|.
name|start
argument_list|()
decl_stmt|;
name|PumpStreamHandler
name|handler
init|=
operator|new
name|PumpStreamHandler
argument_list|(
name|io
operator|.
name|inputStream
argument_list|,
name|io
operator|.
name|outputStream
argument_list|,
name|io
operator|.
name|errorStream
argument_list|)
decl_stmt|;
name|handler
operator|.
name|attach
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|handler
operator|.
name|start
argument_list|()
expr_stmt|;
name|int
name|status
init|=
name|p
operator|.
name|waitFor
argument_list|()
decl_stmt|;
name|handler
operator|.
name|stop
argument_list|()
expr_stmt|;
return|return
name|status
return|;
block|}
block|}
end_class

end_unit

