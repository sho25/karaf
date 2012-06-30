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
name|wrapper
operator|.
name|commands
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|commands
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
name|commands
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
name|console
operator|.
name|AbstractAction
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
name|wrapper
operator|.
name|WrapperService
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
name|wrapper
operator|.
name|internal
operator|.
name|WrapperServiceImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
import|;
end_import

begin_comment
comment|/**  * Installs the Karaf instance as a service in your operating system.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"wrapper"
argument_list|,
name|name
operator|=
literal|"install"
argument_list|,
name|description
operator|=
literal|"Install the container as a system service in the OS."
argument_list|)
specifier|public
class|class
name|Install
extends|extends
name|AbstractAction
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-n"
argument_list|,
name|aliases
operator|=
block|{
literal|"--name"
block|}
argument_list|,
name|description
operator|=
literal|"The service name that will be used when installing the service. (Default: karaf)"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|name
init|=
literal|"karaf"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-d"
argument_list|,
name|aliases
operator|=
block|{
literal|"--display"
block|}
argument_list|,
name|description
operator|=
literal|"The display name of the service."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|displayName
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-D"
argument_list|,
name|aliases
operator|=
block|{
literal|"--description"
block|}
argument_list|,
name|description
operator|=
literal|"The description of the service."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|description
init|=
literal|""
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-s"
argument_list|,
name|aliases
operator|=
block|{
literal|"--start-type"
block|}
argument_list|,
name|description
operator|=
literal|"Mode in which the service is installed. AUTO_START or DEMAND_START (Default: AUTO_START)"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|startType
init|=
literal|"AUTO_START"
decl_stmt|;
specifier|private
name|WrapperService
name|wrapperService
init|=
operator|new
name|WrapperServiceImpl
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setWrapperService
parameter_list|(
name|WrapperService
name|wrapperService
parameter_list|)
block|{
name|this
operator|.
name|wrapperService
operator|=
name|wrapperService
expr_stmt|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|File
index|[]
name|wrapperPaths
init|=
name|wrapperService
operator|.
name|install
argument_list|(
name|name
argument_list|,
name|displayName
argument_list|,
name|description
argument_list|,
name|startType
argument_list|)
decl_stmt|;
name|String
name|os
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|,
literal|"Unknown"
argument_list|)
decl_stmt|;
name|File
name|wrapperConf
init|=
name|wrapperPaths
index|[
literal|0
index|]
decl_stmt|;
name|File
name|serviceFile
init|=
name|wrapperPaths
index|[
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Setup complete.  You may wish to tweak the JVM properties in the wrapper configuration file:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t"
operator|+
name|wrapperConf
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"before installing and starting the service."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|os
operator|.
name|startsWith
argument_list|(
literal|"Win"
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"To install the service, run: "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  C:> "
operator|+
name|serviceFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" install"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Once installed, to start the service run: "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  C:> net start \""
operator|+
name|name
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Once running, to stop the service run: "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  C:> net stop \""
operator|+
name|name
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Once stopped, to remove the installed the service run: "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  C:> "
operator|+
name|serviceFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" remove"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|os
operator|.
name|startsWith
argument_list|(
literal|"Mac OS X"
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"At this time it is not known how to get this service to start when the machine is rebooted."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"If you know how to install the following service script so that it gets started"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"when OS X starts, please email dev@felix.apache.org and let us know how so"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"we can update this message."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To start the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ "
operator|+
name|serviceFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" start"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To stop the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ "
operator|+
name|serviceFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" stop"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|os
operator|.
name|startsWith
argument_list|(
literal|"Linux"
argument_list|)
condition|)
block|{
name|File
name|debianVersion
init|=
operator|new
name|File
argument_list|(
literal|"/etc/debian_version"
argument_list|)
decl_stmt|;
name|File
name|redhatRelease
init|=
operator|new
name|File
argument_list|(
literal|"/etc/redhat-release"
argument_list|)
decl_stmt|;
if|if
condition|(
name|redhatRelease
operator|.
name|exists
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To install the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ ln -s "
operator|+
name|serviceFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" /etc/init.d/"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ chkconfig "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" --add"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To start the service when the machine is rebooted:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ chkconfig "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" on"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To disable starting the service when the machine is rebooted:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ chkconfig "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" off"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To start the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ service "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" start"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To stop the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ service "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" stop"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To uninstall the service :"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ chkconfig "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" --del"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ rm /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|debianVersion
operator|.
name|exists
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To install the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ ln -s "
operator|+
name|serviceFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" /etc/init.d/"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To start the service when the machine is rebooted:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ update-rc.d "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" defaults"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To disable starting the service when the machine is rebooted:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ update-rc.d -f "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" remove"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To start the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" start"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To stop the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" stop"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To uninstall the service :"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ rm /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
literal|"On Redhat/Fedora/CentOS Systems:"
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To install the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ ln -s "
operator|+
name|serviceFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" /etc/init.d/"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ chkconfig "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" --add"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To start the service when the machine is rebooted:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ chkconfig "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" on"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To disable starting the service when the machine is rebooted:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ chkconfig "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" off"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To start the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ service "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" start"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To stop the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ service "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" stop"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To uninstall the service :"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ chkconfig "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" --del"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ rm /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
literal|"On Ubuntu/Debian Systems:"
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To install the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ ln -s "
operator|+
name|serviceFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" /etc/init.d/"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To start the service when the machine is rebooted:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ update-rc.d "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" defaults"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To disable starting the service when the machine is rebooted:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ update-rc.d -f "
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" remove"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To start the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" start"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To stop the service:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" stop"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To uninstall the service :"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ rm /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

