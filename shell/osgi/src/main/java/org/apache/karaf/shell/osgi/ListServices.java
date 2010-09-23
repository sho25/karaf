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
name|osgi
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
name|gogo
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
name|felix
operator|.
name|gogo
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
name|felix
operator|.
name|gogo
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
name|ServiceReference
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"osgi"
argument_list|,
name|name
operator|=
literal|"ls"
argument_list|,
name|description
operator|=
literal|"Lists OSGi services"
argument_list|)
specifier|public
class|class
name|ListServices
extends|extends
name|OsgiCommandSupport
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
literal|"Shows all services"
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
literal|"Shows services which are in use"
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
literal|"Show only services for the given bundle ids"
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
name|Long
argument_list|>
name|ids
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ids
operator|!=
literal|null
operator|&&
operator|!
name|ids
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|long
name|id
range|:
name|ids
control|)
block|{
name|Bundle
name|bundle
init|=
name|getBundleContext
argument_list|()
operator|.
name|getBundle
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|!=
literal|null
condition|)
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
name|ServiceReference
index|[]
name|refs
init|=
literal|null
decl_stmt|;
comment|// Get registered or in-use services.
if|if
condition|(
name|inUse
condition|)
block|{
name|refs
operator|=
name|bundle
operator|.
name|getServicesInUse
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|refs
operator|=
name|bundle
operator|.
name|getRegisteredServices
argument_list|()
expr_stmt|;
block|}
comment|// Print properties for each service.
for|for
control|(
name|int
name|refIdx
init|=
literal|0
init|;
operator|(
name|refs
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|refIdx
operator|<
name|refs
operator|.
name|length
operator|)
condition|;
name|refIdx
operator|++
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
name|refs
index|[
name|refIdx
index|]
operator|.
name|getProperty
argument_list|(
literal|"objectClass"
argument_list|)
decl_stmt|;
comment|// Determine if we need to print the service, depending
comment|// on whether it is a command service or not.
name|boolean
name|print
init|=
literal|true
decl_stmt|;
for|for
control|(
name|int
name|ocIdx
init|=
literal|0
init|;
operator|!
name|showAll
operator|&&
operator|(
name|ocIdx
operator|<
name|objectClass
operator|.
name|length
operator|)
condition|;
name|ocIdx
operator|++
control|)
block|{
if|if
condition|(
name|objectClass
index|[
name|ocIdx
index|]
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
name|print
operator|=
literal|false
expr_stmt|;
block|}
block|}
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
name|String
name|title
init|=
name|Util
operator|.
name|getBundleName
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|title
operator|=
operator|(
name|inUse
operator|)
condition|?
name|title
operator|+
literal|" uses:"
else|:
name|title
operator|+
literal|" provides:"
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
name|title
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|Util
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
name|showAll
operator|||
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
comment|// Print service properties.
name|String
index|[]
name|keys
init|=
name|refs
index|[
name|refIdx
index|]
operator|.
name|getPropertyKeys
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|keyIdx
init|=
literal|0
init|;
operator|(
name|keys
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|keyIdx
operator|<
name|keys
operator|.
name|length
operator|)
condition|;
name|keyIdx
operator|++
control|)
block|{
name|Object
name|v
init|=
name|refs
index|[
name|refIdx
index|]
operator|.
name|getProperty
argument_list|(
name|keys
index|[
name|keyIdx
index|]
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|keys
index|[
name|keyIdx
index|]
operator|+
literal|" = "
operator|+
name|Util
operator|.
name|getValueString
argument_list|(
name|v
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
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Bundle ID "
operator|+
name|id
operator|+
literal|" is invalid."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Bundle
index|[]
name|bundles
init|=
name|getBundleContext
argument_list|()
operator|.
name|getBundles
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundles
operator|!=
literal|null
condition|)
block|{
comment|// TODO: Sort list.
for|for
control|(
name|int
name|bundleIdx
init|=
literal|0
init|;
name|bundleIdx
operator|<
name|bundles
operator|.
name|length
condition|;
name|bundleIdx
operator|++
control|)
block|{
name|boolean
name|headerPrinted
init|=
literal|false
decl_stmt|;
name|ServiceReference
index|[]
name|refs
init|=
literal|null
decl_stmt|;
comment|// Get registered or in-use services.
if|if
condition|(
name|inUse
condition|)
block|{
name|refs
operator|=
name|bundles
index|[
name|bundleIdx
index|]
operator|.
name|getServicesInUse
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|refs
operator|=
name|bundles
index|[
name|bundleIdx
index|]
operator|.
name|getRegisteredServices
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|int
name|refIdx
init|=
literal|0
init|;
operator|(
name|refs
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|refIdx
operator|<
name|refs
operator|.
name|length
operator|)
condition|;
name|refIdx
operator|++
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
name|refs
index|[
name|refIdx
index|]
operator|.
name|getProperty
argument_list|(
literal|"objectClass"
argument_list|)
decl_stmt|;
comment|// Determine if we need to print the service, depending
comment|// on whether it is a command service or not.
name|boolean
name|print
init|=
literal|true
decl_stmt|;
for|for
control|(
name|int
name|ocIdx
init|=
literal|0
init|;
operator|!
name|showAll
operator|&&
operator|(
name|ocIdx
operator|<
name|objectClass
operator|.
name|length
operator|)
condition|;
name|ocIdx
operator|++
control|)
block|{
if|if
condition|(
name|objectClass
index|[
name|ocIdx
index|]
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
name|print
operator|=
literal|false
expr_stmt|;
block|}
block|}
comment|// Print the service if necessary.
if|if
condition|(
name|showAll
operator|||
name|print
condition|)
block|{
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
name|String
name|title
init|=
name|Util
operator|.
name|getBundleName
argument_list|(
name|bundles
index|[
name|bundleIdx
index|]
argument_list|)
decl_stmt|;
name|title
operator|=
operator|(
name|inUse
operator|)
condition|?
name|title
operator|+
literal|" uses:"
else|:
name|title
operator|+
literal|" provides:"
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n"
operator|+
name|title
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|Util
operator|.
name|getUnderlineString
argument_list|(
name|title
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|Util
operator|.
name|getValueString
argument_list|(
name|objectClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"There are no registered services."
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

