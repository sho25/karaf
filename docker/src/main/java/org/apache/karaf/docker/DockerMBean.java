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
name|docker
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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

begin_interface
specifier|public
interface|interface
name|DockerMBean
block|{
name|TabularData
name|ps
parameter_list|(
name|boolean
name|showAll
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|version
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|provision
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|sshPort
parameter_list|,
name|String
name|jmxRmiPort
parameter_list|,
name|String
name|jmxRmiRegistryPort
parameter_list|,
name|String
name|httpPort
parameter_list|,
name|boolean
name|copy
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|rm
parameter_list|(
name|String
name|container
parameter_list|,
name|boolean
name|removeVolumes
parameter_list|,
name|boolean
name|force
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|rename
parameter_list|(
name|String
name|container
parameter_list|,
name|String
name|newName
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|start
parameter_list|(
name|String
name|container
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|stop
parameter_list|(
name|String
name|container
parameter_list|,
name|int
name|timeToWait
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|restart
parameter_list|(
name|String
name|container
parameter_list|,
name|int
name|timeToWait
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|kill
parameter_list|(
name|String
name|container
parameter_list|,
name|String
name|signal
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|pause
parameter_list|(
name|String
name|container
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|unpause
parameter_list|(
name|String
name|container
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|String
name|logs
parameter_list|(
name|String
name|container
parameter_list|,
name|boolean
name|stdout
parameter_list|,
name|boolean
name|stderr
parameter_list|,
name|boolean
name|timestamps
parameter_list|,
name|boolean
name|details
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|commit
parameter_list|(
name|String
name|container
parameter_list|,
name|String
name|repo
parameter_list|,
name|String
name|tag
parameter_list|,
name|String
name|message
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|TabularData
name|images
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|TabularData
name|search
parameter_list|(
name|String
name|term
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|tag
parameter_list|(
name|String
name|image
parameter_list|,
name|String
name|tag
parameter_list|,
name|String
name|repo
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|rmi
parameter_list|(
name|String
name|image
parameter_list|,
name|boolean
name|force
parameter_list|,
name|boolean
name|noprune
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|pull
parameter_list|(
name|String
name|image
parameter_list|,
name|String
name|tag
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|push
parameter_list|(
name|String
name|image
parameter_list|,
name|String
name|tag
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
block|}
end_interface

end_unit

