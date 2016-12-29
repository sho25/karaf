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
name|api
operator|.
name|action
operator|.
name|Action
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
name|Reference
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
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|ansi
operator|.
name|SimpleAnsi
operator|.
name|INTENSITY_BOLD
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|ansi
operator|.
name|SimpleAnsi
operator|.
name|INTENSITY_NORMAL
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
annotation|@
name|Service
specifier|public
class|class
name|Install
implements|implements
name|Action
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
init|=
literal|"karaf"
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
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-e"
argument_list|,
name|aliases
operator|=
block|{
literal|"--env"
block|}
argument_list|,
name|description
operator|=
literal|"Specify environment variable and values. To specify multiple environment variable and values, specify this flag multiple times."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
specifier|private
name|String
index|[]
name|envs
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-i"
argument_list|,
name|aliases
operator|=
block|{
literal|"--include"
block|}
argument_list|,
name|description
operator|=
literal|"Specify include statement for JSW wrapper conf. To specify multiple include statement, specify this flag multiple times."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
specifier|private
name|String
index|[]
name|includes
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|WrapperService
name|wrapperService
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
argument_list|,
name|envs
argument_list|,
name|includes
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
name|File
name|systemdFile
init|=
name|wrapperPaths
index|[
literal|2
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
name|INTENSITY_BOLD
operator|+
literal|"MS Windows system detected:"
operator|+
name|INTENSITY_NORMAL
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
name|INTENSITY_BOLD
operator|+
literal|"Mac OS X system detected:"
operator|+
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"to add bin/org.apache.karaf.KARAF as user service move this file into ~/Library/LaunchAgents/"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"> mv bin/org.apache.karaf.KARAF.plist ~/Library/LaunchAgents/"
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
literal|"to add org.apache.karaf.KARAF as system service move this into /Library/LaunchDaemons"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"> sudo mv bin/org.apache.karaf.KARAF.plist /Library/LaunchDaemons/"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"change owner and rights"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"> sudo chown root:wheel /Library/LaunchDaemons/org.apache.karaf.KARAF.plist"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"> sudo chmod u=rw,g=r,o=r /Library/LaunchDaemons/org.apache.karaf.KARAF.plist"
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
literal|"test your service"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"> launchctl load ~/Library/LaunchAgents/org.apache.karaf.KARAF.plist"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"> launchctl start org.apache.karaf.KARAF"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"> launchctl stop org.apache.karaf.KARAF"
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
literal|"after restart your session or system"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"you can use launchctl command to start and stop your service"
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
literal|"for removing the service call"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"> launchctl remove org.apache.karaf.KARAF"
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
name|INTENSITY_BOLD
operator|+
literal|"RedHat/Fedora/CentOS Linux system detected (SystemV):"
operator|+
name|INTENSITY_NORMAL
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
name|INTENSITY_BOLD
operator|+
literal|"Ubuntu/Debian Linux system detected (SystemV):"
operator|+
name|INTENSITY_NORMAL
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
name|INTENSITY_BOLD
operator|+
literal|"On Redhat/Fedora/CentOS Systems (SystemV):"
operator|+
name|INTENSITY_NORMAL
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
name|INTENSITY_BOLD
operator|+
literal|"On Ubuntu/Debian Systems (SystemV):"
operator|+
name|INTENSITY_NORMAL
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
if|if
condition|(
name|systemdFile
operator|!=
literal|null
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
name|INTENSITY_BOLD
operator|+
literal|"For systemd compliant Linux: "
operator|+
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  To install the service (and enable at system boot):"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   $ systemctl enable "
operator|+
name|systemdFile
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
literal|"   $ systemctl start "
operator|+
name|name
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
literal|"   $ systemctl stop "
operator|+
name|name
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
literal|"  To check the current service status:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   $ systemctl status "
operator|+
name|name
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
literal|"  To see service activity journal:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   $ journalctl -u "
operator|+
name|name
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
literal|"  To uninstall the service (and disable at system boot):"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   $ systemctl disable "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|os
operator|.
name|startsWith
argument_list|(
literal|"Solaris"
argument_list|)
operator|||
name|os
operator|.
name|startsWith
argument_list|(
literal|"SunOS"
argument_list|)
condition|)
block|{
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
literal|"  To start the service when the machine is rebooted for all multi-user run levels"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  and stopped for the halt, single-user and reboot runlevels:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ ln -s /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" /etc/rc0.d/K20"
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
literal|"    $ ln -s /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" /etc/rc1.d/K20"
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
literal|"    $ ln -s /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" /etc/rc2.d/S20"
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
literal|"    $ ln -s /etc/init.d/"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
operator|+
literal|" /etc/rc3.d/S20"
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
literal|"    If your application makes use of other services, then you will need to make"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    sure that your application is started after, and then shutdown before. This"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    is done by controlling the startup/shutdown order by setting the right order"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    value, which in this example it set to 20."
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    $ rm /etc/rc0.d/K20"
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
literal|"    $ rm /etc/rc1.d/K20"
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
literal|"    $ rm /etc/rc2.d/K20"
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
literal|"    $ rm /etc/rc3.d/K20"
operator|+
name|serviceFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

