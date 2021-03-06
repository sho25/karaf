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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|DirectoryDumpDestination
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
name|apache
operator|.
name|karaf
operator|.
name|diagnostic
operator|.
name|core
operator|.
name|providers
operator|.
name|BundleDumpProvider
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
name|providers
operator|.
name|EnvironmentDumpProvider
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
name|providers
operator|.
name|HeapDumpProvider
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
name|providers
operator|.
name|MemoryDumpProvider
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
name|providers
operator|.
name|ThreadDumpProvider
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
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
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
name|ServiceReference
import|;
end_import

begin_comment
comment|/**  * Dump helper  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Dump
block|{
specifier|public
specifier|static
name|DumpDestination
name|directory
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
operator|new
name|DirectoryDumpDestination
argument_list|(
name|file
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|DumpDestination
name|zip
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
operator|new
name|ZipDumpDestination
argument_list|(
name|file
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|dump
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|DumpDestination
name|destination
parameter_list|,
name|boolean
name|noThreadDump
parameter_list|,
name|boolean
name|noHeapDump
parameter_list|)
block|{
name|List
argument_list|<
name|DumpProvider
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|EnvironmentDumpProvider
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|MemoryDumpProvider
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|noThreadDump
condition|)
name|providers
operator|.
name|add
argument_list|(
operator|new
name|ThreadDumpProvider
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|noHeapDump
condition|)
name|providers
operator|.
name|add
argument_list|(
operator|new
name|HeapDumpProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|BundleDumpProvider
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|DumpProvider
name|provider
range|:
name|providers
control|)
block|{
try|try
block|{
name|provider
operator|.
name|createDump
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
try|try
block|{
for|for
control|(
name|ServiceReference
argument_list|<
name|DumpProvider
argument_list|>
name|ref
range|:
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|DumpProvider
operator|.
name|class
argument_list|,
literal|null
argument_list|)
control|)
block|{
name|DumpProvider
name|provider
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
try|try
block|{
name|provider
operator|.
name|createDump
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
finally|finally
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
try|try
block|{
name|destination
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
comment|// Private constructor
specifier|private
name|Dump
parameter_list|()
block|{ }
block|}
end_class

end_unit

