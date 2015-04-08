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
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
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

begin_class
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
block|{
name|Closeable
name|dumpHandler
decl_stmt|;
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|isWindows
argument_list|()
condition|)
block|{
name|ClassLoader
name|cl
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|dumpHandlerClazz
init|=
name|cl
operator|.
name|loadClass
argument_list|(
literal|"org.apache.karaf.diagnostic.core.internal.DumpHandler"
argument_list|)
decl_stmt|;
name|dumpHandler
operator|=
operator|(
name|Closeable
operator|)
name|dumpHandlerClazz
operator|.
name|getConstructor
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Will happen if sun.misc.SignalHandler is not available
block|}
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|dumpHandler
operator|!=
literal|null
operator|&&
operator|!
name|isWindows
argument_list|()
condition|)
block|{
name|dumpHandler
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isWindows
parameter_list|()
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|,
literal|"Unknown"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Win"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

