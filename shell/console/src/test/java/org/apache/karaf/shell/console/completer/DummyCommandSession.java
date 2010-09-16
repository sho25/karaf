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
name|console
operator|.
name|completer
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|command
operator|.
name|CommandSession
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
name|PrintStream
import|;
end_import

begin_class
specifier|public
class|class
name|DummyCommandSession
implements|implements
name|CommandSession
block|{
specifier|public
name|Object
name|convert
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Object
name|instance
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|CharSequence
name|format
parameter_list|(
name|Object
name|target
parameter_list|,
name|int
name|level
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|put
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{     }
specifier|public
name|Object
name|get
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|PrintStream
name|getConsole
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|InputStream
name|getKeyboard
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{     }
specifier|public
name|Object
name|execute
parameter_list|(
name|CharSequence
name|commandline
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

