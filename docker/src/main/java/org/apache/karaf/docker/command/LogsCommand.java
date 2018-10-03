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
operator|.
name|command
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|docker
operator|.
name|command
operator|.
name|completers
operator|.
name|ContainersNameCompleter
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
name|action
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Command
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
name|action
operator|.
name|Completion
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
name|action
operator|.
name|Option
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
name|action
operator|.
name|lifecycle
operator|.
name|Service
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"docker"
argument_list|,
name|name
operator|=
literal|"logs"
argument_list|,
name|description
operator|=
literal|"Fetch the logs of a container"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|LogsCommand
extends|extends
name|DockerCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"container"
argument_list|,
name|description
operator|=
literal|"Name or ID of the container"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
annotation|@
name|Completion
argument_list|(
name|ContainersNameCompleter
operator|.
name|class
argument_list|)
name|String
name|container
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--stdout"
argument_list|,
name|description
operator|=
literal|"Display stdout"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|stdout
init|=
literal|true
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--stderr"
argument_list|,
name|description
operator|=
literal|"Display stderr"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|stderr
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--timestamps"
argument_list|,
name|description
operator|=
literal|"Show timestamps"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|timestamps
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--details"
argument_list|,
name|description
operator|=
literal|"Show extra details provided to logs"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|details
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|stdout
operator|&&
operator|!
name|stderr
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"You have at least to choose one stream: stdout or stderr using the corresponding command options"
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|getDockerService
argument_list|()
operator|.
name|logs
argument_list|(
name|container
argument_list|,
name|stdout
argument_list|,
name|stderr
argument_list|,
name|timestamps
argument_list|,
name|details
argument_list|,
name|url
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit
