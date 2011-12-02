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
name|shell
operator|.
name|services
package|;
end_package

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
name|shell
operator|.
name|commands
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
name|OsgiCommandSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|Function
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
name|bundles
operator|.
name|BundleSelector
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
name|util
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
name|ServiceReference
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"service"
argument_list|,
name|name
operator|=
literal|"list"
argument_list|,
name|description
operator|=
literal|"Lists OSGi services."
argument_list|)
specifier|public
class|class
name|ListServices
extends|extends
name|OsgiCommandSupport
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
literal|"ids"
argument_list|,
name|description
operator|=
literal|"The list of bundle (identified by IDs or name or name/version) separated by whitespaces"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|ids
decl_stmt|;
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
literal|"Shows all services. (By default Karaf commands are hidden)"
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
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|BundleContext
name|bundleContext
init|=
name|getBundleContext
argument_list|()
decl_stmt|;
name|BundleSelector
name|selector
init|=
operator|new
name|BundleSelector
argument_list|(
name|bundleContext
argument_list|,
name|session
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|selector
operator|.
name|selectBundles
argument_list|(
name|ids
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundles
operator|==
literal|null
operator|||
name|bundles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Bundle
index|[]
name|allBundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
name|printBundles
argument_list|(
name|allBundles
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|printBundles
argument_list|(
name|bundles
operator|.
name|toArray
argument_list|(
operator|new
name|Bundle
index|[]
block|{}
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|printBundles
parameter_list|(
name|Bundle
index|[]
name|bundles
parameter_list|,
name|boolean
name|showProperties
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
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
literal|"objectClass"
argument_list|)
decl_stmt|;
name|boolean
name|print
init|=
name|showAll
operator|||
operator|!
name|isCommand
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
argument_list|(
literal|""
argument_list|)
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
name|isCommand
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
name|objectClass
operator|.
name|equals
argument_list|(
name|Function
operator|.
name|class
operator|.
name|getName
argument_list|()
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

