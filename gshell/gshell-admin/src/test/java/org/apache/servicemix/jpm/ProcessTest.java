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
name|servicemix
operator|.
name|jpm
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|ProcessTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCreate
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuilder
name|command
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|command
operator|.
name|append
argument_list|(
literal|"java -classpath "
argument_list|)
expr_stmt|;
name|String
name|clRes
init|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|".class"
decl_stmt|;
name|String
name|str
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|clRes
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|str
operator|=
name|str
operator|.
name|substring
argument_list|(
literal|"file:"
operator|.
name|length
argument_list|()
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
name|clRes
argument_list|)
argument_list|)
expr_stmt|;
name|command
operator|.
name|append
argument_list|(
name|str
argument_list|)
expr_stmt|;
name|command
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|command
operator|.
name|append
argument_list|(
name|MainTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|command
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|command
operator|.
name|append
argument_list|(
literal|60000
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Executing: "
operator|+
name|command
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ProcessBuilder
name|builder
init|=
name|ProcessBuilderFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
name|Process
name|p
init|=
name|builder
operator|.
name|command
argument_list|(
name|command
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Process: "
operator|+
name|p
operator|.
name|getPid
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|p
operator|.
name|getPid
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Running: "
operator|+
name|p
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Destroying"
argument_list|)
expr_stmt|;
name|p
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Running: "
operator|+
name|p
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*     @Test     @Ignore("When the process creation fails, no error is reported by the script")     public void testFailure() throws Exception {         ProcessBuilder builder = ProcessBuilderFactory.newInstance().newBuilder();         Process p = builder.command("ec").start();         fail("An exception should have been thrown");     }     */
block|}
end_class

end_unit

