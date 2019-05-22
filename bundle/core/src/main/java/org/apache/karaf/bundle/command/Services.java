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
name|bundle
operator|.
name|command
package|;
end_package

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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|shell
operator|.
name|support
operator|.
name|ShellUtil
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
name|Bundle
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
name|Constants
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

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"bundle"
argument_list|,
name|name
operator|=
literal|"services"
argument_list|,
name|description
operator|=
literal|"Lists OSGi services per Bundle"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|Services
extends|extends
name|BundlesCommand
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-a"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Shows all services. (Karaf commands and completers are hidden by default)"
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
name|showAll
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Shows the services each bundle uses. (By default the provided services are shown)"
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
name|inUse
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Shows the properties of the services"
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
name|showProperties
init|=
literal|false
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|hidden
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"org.apache.felix.service.command.Function"
argument_list|,
literal|"org.apache.karaf.shell.console.Completer"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|executeOnBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|refs
init|=
operator|(
name|inUse
operator|)
condition|?
name|bundle
operator|.
name|getServicesInUse
argument_list|()
else|:
name|bundle
operator|.
name|getRegisteredServices
argument_list|()
decl_stmt|;
name|printServices
argument_list|(
name|bundle
argument_list|,
name|refs
argument_list|,
name|showProperties
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|printServices
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|refs
parameter_list|,
name|boolean
name|showProperties
parameter_list|)
block|{
name|boolean
name|headerPrinted
init|=
literal|false
decl_stmt|;
name|boolean
name|needSeparator
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|refs
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|serviceRef
range|:
name|refs
control|)
block|{
name|String
index|[]
name|objectClass
init|=
operator|(
name|String
index|[]
operator|)
name|serviceRef
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|OBJECTCLASS
argument_list|)
decl_stmt|;
name|boolean
name|print
init|=
name|showAll
operator|||
operator|!
name|isCommandOrCompleter
argument_list|(
name|objectClass
argument_list|)
decl_stmt|;
comment|// Print header if we have not already done so.
if|if
condition|(
operator|!
name|headerPrinted
condition|)
block|{
name|headerPrinted
operator|=
literal|true
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|String
name|title
init|=
name|ShellUtil
operator|.
name|getBundleName
argument_list|(
name|bundle
argument_list|)
operator|+
operator|(
operator|(
name|inUse
operator|)
condition|?
literal|" uses:"
else|:
literal|" provides:"
operator|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|ShellUtil
operator|.
name|getUnderlineString
argument_list|(
name|title
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|print
condition|)
block|{
comment|// Print service separator if necessary.
if|if
condition|(
name|needSeparator
operator|&&
name|showProperties
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showProperties
condition|)
block|{
name|printProperties
argument_list|(
name|serviceRef
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
name|ShellUtil
operator|.
name|getValueString
argument_list|(
name|objectClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|needSeparator
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|isCommandOrCompleter
parameter_list|(
name|String
index|[]
name|objectClasses
parameter_list|)
block|{
for|for
control|(
name|String
name|objectClass
range|:
name|objectClasses
control|)
block|{
if|if
condition|(
name|hidden
operator|.
name|contains
argument_list|(
name|objectClass
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|printProperties
parameter_list|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|serviceRef
parameter_list|)
block|{
for|for
control|(
name|String
name|key
range|:
name|serviceRef
operator|.
name|getPropertyKeys
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|key
operator|+
literal|" = "
operator|+
name|ShellUtil
operator|.
name|getValueString
argument_list|(
name|serviceRef
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

