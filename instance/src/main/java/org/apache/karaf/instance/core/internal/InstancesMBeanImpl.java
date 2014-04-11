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
name|instance
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
name|Arrays
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
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StandardMBean
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|instance
operator|.
name|core
operator|.
name|Instance
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
name|instance
operator|.
name|core
operator|.
name|InstanceSettings
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
name|instance
operator|.
name|core
operator|.
name|InstancesMBean
import|;
end_import

begin_class
specifier|public
class|class
name|InstancesMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|InstancesMBean
block|{
specifier|static
specifier|final
name|String
name|DEBUG_OPTS
init|=
literal|" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
decl_stmt|;
specifier|static
specifier|final
name|String
name|DEFAULT_OPTS
init|=
literal|"-server -Xmx512M -Dcom.sun.management.jmxremote"
decl_stmt|;
specifier|private
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|instance
operator|.
name|core
operator|.
name|InstanceService
name|instanceService
decl_stmt|;
specifier|public
name|InstancesMBeanImpl
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|instance
operator|.
name|core
operator|.
name|InstanceService
name|instanceService
parameter_list|)
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|InstancesMBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|instanceService
operator|=
name|instanceService
expr_stmt|;
block|}
specifier|public
name|int
name|createInstance
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|sshPort
parameter_list|,
name|int
name|rmiRegistryPort
parameter_list|,
name|int
name|rmiServerPort
parameter_list|,
name|String
name|location
parameter_list|,
name|String
name|javaOpts
parameter_list|,
name|String
name|features
parameter_list|,
name|String
name|featureURLs
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|location
argument_list|)
condition|)
block|{
name|location
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|javaOpts
argument_list|)
condition|)
block|{
name|javaOpts
operator|=
literal|null
expr_stmt|;
block|}
name|InstanceSettings
name|settings
init|=
operator|new
name|InstanceSettings
argument_list|(
name|sshPort
argument_list|,
name|rmiRegistryPort
argument_list|,
name|rmiServerPort
argument_list|,
name|location
argument_list|,
name|javaOpts
argument_list|,
name|parseStringList
argument_list|(
name|featureURLs
argument_list|)
argument_list|,
name|parseStringList
argument_list|(
name|features
argument_list|)
argument_list|)
decl_stmt|;
name|Instance
name|inst
init|=
name|instanceService
operator|.
name|createInstance
argument_list|(
name|name
argument_list|,
name|settings
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|inst
operator|!=
literal|null
condition|)
block|{
return|return
name|inst
operator|.
name|getPid
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|changeSshPort
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|getExistingInstance
argument_list|(
name|name
argument_list|)
operator|.
name|changeSshPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|changeRmiRegistryPort
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|getExistingInstance
argument_list|(
name|name
argument_list|)
operator|.
name|changeRmiRegistryPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|changeRmiServerPort
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|getExistingInstance
argument_list|(
name|name
argument_list|)
operator|.
name|changeRmiServerPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|changeJavaOpts
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|javaOpts
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|getExistingInstance
argument_list|(
name|name
argument_list|)
operator|.
name|changeJavaOpts
argument_list|(
name|javaOpts
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|destroyInstance
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|getExistingInstance
argument_list|(
name|name
argument_list|)
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|startInstance
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|getExistingInstance
argument_list|(
name|name
argument_list|)
operator|.
name|start
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|startInstance
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|opts
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|getExistingInstance
argument_list|(
name|name
argument_list|)
operator|.
name|start
argument_list|(
name|opts
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|startInstance
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|opts
parameter_list|,
name|boolean
name|wait
parameter_list|,
name|boolean
name|debug
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|Instance
name|child
init|=
name|getExistingInstance
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|String
name|options
init|=
name|opts
decl_stmt|;
if|if
condition|(
name|options
operator|==
literal|null
condition|)
block|{
name|options
operator|=
name|child
operator|.
name|getJavaOpts
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|==
literal|null
condition|)
block|{
name|options
operator|=
name|DEFAULT_OPTS
expr_stmt|;
block|}
if|if
condition|(
name|debug
condition|)
block|{
name|options
operator|+=
name|DEBUG_OPTS
expr_stmt|;
block|}
if|if
condition|(
name|wait
condition|)
block|{
name|String
name|state
init|=
name|child
operator|.
name|getState
argument_list|()
decl_stmt|;
if|if
condition|(
name|Instance
operator|.
name|STOPPED
operator|.
name|equals
argument_list|(
name|state
argument_list|)
condition|)
block|{
name|child
operator|.
name|start
argument_list|(
name|opts
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Instance
operator|.
name|STARTED
operator|.
name|equals
argument_list|(
name|state
argument_list|)
condition|)
block|{
do|do
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|state
operator|=
name|child
operator|.
name|getState
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|Instance
operator|.
name|STARTING
operator|.
name|equals
argument_list|(
name|state
argument_list|)
condition|)
do|;
block|}
block|}
else|else
block|{
name|child
operator|.
name|start
argument_list|(
name|opts
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|stopInstance
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|getExistingInstance
argument_list|(
name|name
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|renameInstance
parameter_list|(
name|String
name|originalName
parameter_list|,
name|String
name|newName
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|instanceService
operator|.
name|renameInstance
argument_list|(
name|originalName
argument_list|,
name|newName
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|renameInstance
parameter_list|(
name|String
name|originalName
parameter_list|,
name|String
name|newName
parameter_list|,
name|boolean
name|verbose
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|instanceService
operator|.
name|renameInstance
argument_list|(
name|originalName
argument_list|,
name|newName
argument_list|,
name|verbose
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|cloneInstance
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|cloneName
parameter_list|,
name|int
name|sshPort
parameter_list|,
name|int
name|rmiRegistryPort
parameter_list|,
name|int
name|rmiServerPort
parameter_list|,
name|String
name|location
parameter_list|,
name|String
name|javaOpts
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|location
argument_list|)
condition|)
block|{
name|location
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|javaOpts
argument_list|)
condition|)
block|{
name|javaOpts
operator|=
literal|null
expr_stmt|;
block|}
name|InstanceSettings
name|settings
init|=
operator|new
name|InstanceSettings
argument_list|(
name|sshPort
argument_list|,
name|rmiRegistryPort
argument_list|,
name|rmiServerPort
argument_list|,
name|location
argument_list|,
name|javaOpts
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|instanceService
operator|.
name|cloneInstance
argument_list|(
name|name
argument_list|,
name|cloneName
argument_list|,
name|settings
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|TabularData
name|getInstances
parameter_list|()
throws|throws
name|MBeanException
block|{
name|List
argument_list|<
name|Instance
argument_list|>
name|instances
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|instanceService
operator|.
name|getInstances
argument_list|()
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
name|InstanceToTableMapper
operator|.
name|tableFrom
argument_list|(
name|instances
argument_list|)
decl_stmt|;
return|return
name|table
return|;
block|}
specifier|private
name|Instance
name|getExistingInstance
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Instance
name|i
init|=
name|instanceService
operator|.
name|getInstance
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instance '"
operator|+
name|name
operator|+
literal|"' does not exist"
argument_list|)
throw|;
block|}
return|return
name|i
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|parseStringList
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|el
range|:
name|value
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|String
name|trimmed
init|=
name|el
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|trimmed
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
name|list
operator|.
name|add
argument_list|(
name|trimmed
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit
