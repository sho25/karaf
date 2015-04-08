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
name|diagnostic
operator|.
name|core
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|diagnostic
operator|.
name|core
operator|.
name|Dump
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
name|diagnostic
operator|.
name|core
operator|.
name|DumpDestination
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
name|diagnostic
operator|.
name|core
operator|.
name|common
operator|.
name|ZipDumpDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|sun
operator|.
name|misc
operator|.
name|Signal
import|;
end_import

begin_import
import|import
name|sun
operator|.
name|misc
operator|.
name|SignalHandler
import|;
end_import

begin_class
specifier|public
class|class
name|DumpHandler
implements|implements
name|SignalHandler
implements|,
name|Closeable
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SIGNAL
init|=
literal|"HUP"
decl_stmt|;
specifier|private
name|BundleContext
name|context
decl_stmt|;
specifier|private
name|SignalHandler
name|previous
decl_stmt|;
specifier|public
name|DumpHandler
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|previous
operator|=
name|sun
operator|.
name|misc
operator|.
name|Signal
operator|.
name|handle
argument_list|(
operator|new
name|Signal
argument_list|(
name|SIGNAL
argument_list|)
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handle
parameter_list|(
name|Signal
name|signal
parameter_list|)
block|{
name|SimpleDateFormat
name|dumpFormat
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd_HHmmss-SSS"
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
literal|"dump-"
operator|+
name|dumpFormat
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
operator|+
literal|".zip"
decl_stmt|;
name|DumpDestination
name|destination
init|=
operator|new
name|ZipDumpDestination
argument_list|(
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
argument_list|)
decl_stmt|;
name|Dump
operator|.
name|dump
argument_list|(
name|context
argument_list|,
name|destination
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
name|sun
operator|.
name|misc
operator|.
name|Signal
operator|.
name|handle
argument_list|(
operator|new
name|Signal
argument_list|(
name|SIGNAL
argument_list|)
argument_list|,
name|previous
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

