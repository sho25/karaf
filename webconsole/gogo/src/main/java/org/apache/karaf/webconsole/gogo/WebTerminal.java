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
name|webconsole
operator|.
name|gogo
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
name|util
operator|.
name|EnumSet
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
name|Signal
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
name|SignalListener
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
name|impl
operator|.
name|ExternalTerminal
import|;
end_import

begin_class
specifier|public
class|class
name|WebTerminal
extends|extends
name|ExternalTerminal
implements|implements
name|Terminal
block|{
specifier|public
name|WebTerminal
parameter_list|(
name|int
name|width
parameter_list|,
name|int
name|height
parameter_list|,
name|InputStream
name|input
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
literal|"Karaf Web Terminal"
argument_list|,
literal|"ansi"
argument_list|,
name|input
argument_list|,
name|output
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|size
operator|.
name|setColumns
argument_list|(
name|width
argument_list|)
expr_stmt|;
name|size
operator|.
name|setRows
argument_list|(
name|height
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
return|return
name|size
operator|.
name|getColumns
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getHeight
parameter_list|()
block|{
return|return
name|size
operator|.
name|getRows
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|)
block|{
comment|// TODO:JLINE
block|}
annotation|@
name|Override
specifier|public
name|void
name|addSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|,
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
name|Signal
modifier|...
name|signal
parameter_list|)
block|{
comment|// TODO:JLINE
block|}
annotation|@
name|Override
specifier|public
name|void
name|addSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|,
name|EnumSet
argument_list|<
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
name|Signal
argument_list|>
name|signals
parameter_list|)
block|{
comment|// TODO:JLINE
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|)
block|{
comment|// TODO:JLINE
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isAnsiSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEchoEnabled
parameter_list|()
block|{
return|return
name|echo
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setEchoEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|echo
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

