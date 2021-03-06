begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
package|;
end_package

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
name|PrintWriter
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
name|Terminal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Size
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|impl
operator|.
name|AbstractTerminal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|utils
operator|.
name|InfoCmp
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|utils
operator|.
name|NonBlockingReader
import|;
end_import

begin_class
specifier|public
class|class
name|KarafTerminal
extends|extends
name|AbstractTerminal
implements|implements
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Terminal
block|{
specifier|private
specifier|final
name|Terminal
name|terminal
decl_stmt|;
specifier|public
name|KarafTerminal
parameter_list|(
name|Terminal
name|terminal
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
literal|"Karaf"
argument_list|,
name|terminal
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|terminal
operator|=
name|terminal
expr_stmt|;
name|String
name|type
init|=
name|terminal
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|&&
name|terminal
operator|.
name|isAnsiSupported
argument_list|()
condition|)
block|{
name|type
operator|=
literal|"ansi"
expr_stmt|;
block|}
name|String
name|caps
decl_stmt|;
try|try
block|{
name|caps
operator|=
name|InfoCmp
operator|.
name|getInfoCmp
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
try|try
block|{
name|caps
operator|=
name|InfoCmp
operator|.
name|getInfoCmp
argument_list|(
literal|"ansi"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e2
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|e2
argument_list|)
throw|;
block|}
block|}
try|try
block|{
name|InfoCmp
operator|.
name|parseInfoCmp
argument_list|(
name|caps
argument_list|,
name|bools
argument_list|,
name|ints
argument_list|,
name|strings
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// TODO
block|}
block|}
annotation|@
name|Override
specifier|public
name|NonBlockingReader
name|reader
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrintWriter
name|writer
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|input
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|OutputStream
name|output
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Attributes
name|getAttributes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setAttributes
parameter_list|(
name|Attributes
name|attr
parameter_list|)
block|{      }
annotation|@
name|Override
specifier|public
name|Size
name|getSize
parameter_list|()
block|{
name|int
name|h
init|=
name|terminal
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|w
init|=
name|terminal
operator|.
name|getWidth
argument_list|()
decl_stmt|;
return|return
operator|new
name|Size
argument_list|(
name|w
argument_list|,
name|h
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setSize
parameter_list|(
name|Size
name|size
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{     }
block|}
end_class

end_unit

