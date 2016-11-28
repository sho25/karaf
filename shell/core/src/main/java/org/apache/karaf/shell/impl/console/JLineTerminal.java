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
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|IntConsumer
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
name|Cursor
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
name|MouseEvent
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
name|utils
operator|.
name|InfoCmp
operator|.
name|Capability
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
name|JLineTerminal
implements|implements
name|Terminal
implements|,
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
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Terminal
name|terminal
decl_stmt|;
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|Signal
argument_list|,
name|Set
argument_list|<
name|SignalListener
argument_list|>
argument_list|>
name|listeners
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|Signal
argument_list|,
name|SignalHandler
argument_list|>
name|handlers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|JLineTerminal
parameter_list|(
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Terminal
name|terminal
parameter_list|)
block|{
name|this
operator|.
name|terminal
operator|=
name|terminal
expr_stmt|;
for|for
control|(
name|Signal
name|signal
range|:
name|Signal
operator|.
name|values
argument_list|()
control|)
block|{
name|terminal
operator|.
name|handle
argument_list|(
name|signal
argument_list|,
name|this
operator|::
name|handle
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|getType
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|puts
parameter_list|(
name|Capability
name|capability
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
block|{
return|return
name|terminal
operator|.
name|puts
argument_list|(
name|capability
argument_list|,
name|params
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|getBooleanCapability
parameter_list|(
name|Capability
name|capability
parameter_list|)
block|{
return|return
name|terminal
operator|.
name|getBooleanCapability
argument_list|(
name|capability
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|getNumericCapability
parameter_list|(
name|Capability
name|capability
parameter_list|)
block|{
return|return
name|terminal
operator|.
name|getNumericCapability
argument_list|(
name|capability
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getStringCapability
parameter_list|(
name|Capability
name|capability
parameter_list|)
block|{
return|return
name|terminal
operator|.
name|getStringCapability
argument_list|(
name|capability
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|getWidth
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
name|terminal
operator|.
name|getHeight
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|flush
parameter_list|()
block|{
name|terminal
operator|.
name|flush
argument_list|()
expr_stmt|;
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
name|terminal
operator|.
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
name|terminal
operator|.
name|echo
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|terminal
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|NonBlockingReader
name|reader
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|reader
argument_list|()
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
name|terminal
operator|.
name|writer
argument_list|()
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
name|terminal
operator|.
name|input
argument_list|()
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
name|terminal
operator|.
name|output
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Attributes
name|enterRawMode
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|enterRawMode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|echo
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|echo
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|echo
parameter_list|(
name|boolean
name|echo
parameter_list|)
block|{
return|return
name|terminal
operator|.
name|echo
argument_list|(
name|echo
argument_list|)
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
name|terminal
operator|.
name|getAttributes
argument_list|()
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
block|{
name|terminal
operator|.
name|setAttributes
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Size
name|getSize
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|getSize
argument_list|()
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
name|terminal
operator|.
name|setSize
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|raise
parameter_list|(
name|Signal
name|signal
parameter_list|)
block|{
name|terminal
operator|.
name|raise
argument_list|(
name|signal
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|SignalHandler
name|handle
parameter_list|(
name|Signal
name|signal
parameter_list|,
name|SignalHandler
name|handler
parameter_list|)
block|{
return|return
name|handlers
operator|.
name|put
argument_list|(
name|signal
argument_list|,
name|handler
argument_list|)
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
name|addSignalListener
argument_list|(
name|listener
argument_list|,
name|EnumSet
operator|.
name|allOf
argument_list|(
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
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
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
name|signals
parameter_list|)
block|{
for|for
control|(
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
name|sig
range|:
name|signals
control|)
block|{
name|addSignalListener
argument_list|(
name|listener
argument_list|,
name|sig
argument_list|)
expr_stmt|;
block|}
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
for|for
control|(
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
name|sig
range|:
name|signals
control|)
block|{
name|addSignalListener
argument_list|(
name|listener
argument_list|,
name|sig
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
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
name|signal
parameter_list|)
block|{
name|Set
argument_list|<
name|SignalListener
argument_list|>
name|ls
init|=
name|listeners
operator|.
name|compute
argument_list|(
name|signal
argument_list|(
name|signal
argument_list|)
argument_list|,
operator|(
name|s
expr|,
name|l
operator|)
operator|->
name|l
operator|!=
literal|null
condition|?
name|l
else|:
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
name|ls
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
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
for|for
control|(
name|Signal
name|signal
range|:
name|Signal
operator|.
name|values
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|SignalListener
argument_list|>
name|ls
init|=
name|listeners
operator|.
name|get
argument_list|(
name|signal
argument_list|)
decl_stmt|;
if|if
condition|(
name|ls
operator|!=
literal|null
condition|)
block|{
name|ls
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|Cursor
name|getCursorPosition
parameter_list|(
name|IntConsumer
name|discarded
parameter_list|)
block|{
return|return
name|terminal
operator|.
name|getCursorPosition
argument_list|(
name|discarded
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasMouseSupport
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|hasMouseSupport
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|trackMouse
parameter_list|(
name|MouseTracking
name|tracking
parameter_list|)
block|{
return|return
name|terminal
operator|.
name|trackMouse
argument_list|(
name|tracking
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|MouseEvent
name|readMouseEvent
parameter_list|()
block|{
return|return
name|terminal
operator|.
name|readMouseEvent
argument_list|()
return|;
block|}
specifier|private
name|Signal
name|signal
parameter_list|(
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
name|sig
parameter_list|)
block|{
switch|switch
condition|(
name|sig
condition|)
block|{
case|case
name|INT
case|:
return|return
name|Signal
operator|.
name|INT
return|;
case|case
name|QUIT
case|:
return|return
name|Signal
operator|.
name|QUIT
return|;
case|case
name|TSTP
case|:
return|return
name|Signal
operator|.
name|TSTP
return|;
case|case
name|CONT
case|:
return|return
name|Signal
operator|.
name|CONT
return|;
case|case
name|WINCH
case|:
return|return
name|Signal
operator|.
name|WINCH
return|;
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|private
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
name|signal
parameter_list|(
name|Signal
name|sig
parameter_list|)
block|{
switch|switch
condition|(
name|sig
condition|)
block|{
case|case
name|INT
case|:
return|return
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
operator|.
name|INT
return|;
case|case
name|QUIT
case|:
return|return
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
operator|.
name|QUIT
return|;
case|case
name|TSTP
case|:
return|return
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
operator|.
name|TSTP
return|;
case|case
name|CONT
case|:
return|return
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
operator|.
name|CONT
return|;
case|case
name|WINCH
case|:
return|return
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
operator|.
name|WINCH
return|;
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|protected
name|void
name|handle
parameter_list|(
name|Signal
name|signal
parameter_list|)
block|{
name|SignalHandler
name|handler
init|=
name|handlers
operator|.
name|get
argument_list|(
name|signal
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|handler
operator|.
name|handle
argument_list|(
name|signal
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|SignalListener
argument_list|>
name|sl
init|=
name|listeners
operator|.
name|get
argument_list|(
name|signal
argument_list|)
decl_stmt|;
if|if
condition|(
name|sl
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|SignalListener
name|l
range|:
name|sl
control|)
block|{
name|l
operator|.
name|signal
argument_list|(
name|signal
argument_list|(
name|signal
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

