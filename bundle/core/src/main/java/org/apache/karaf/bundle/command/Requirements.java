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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|wiring
operator|.
name|BundleCapability
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
name|wiring
operator|.
name|BundleRequirement
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
name|wiring
operator|.
name|BundleWire
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
name|wiring
operator|.
name|BundleWiring
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
literal|"requirements"
argument_list|,
name|description
operator|=
literal|"Displays OSGi requirements of a given bundles."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|Requirements
extends|extends
name|BundlesCommand
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NONSTANDARD_SERVICE_NAMESPACE
init|=
literal|"service"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_MESSAGE
init|=
literal|"[EMPTY]"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|UNRESOLVED_MESSAGE
init|=
literal|"[UNRESOLVED]"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--namespace"
argument_list|)
name|String
name|namespace
init|=
literal|"*"
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
block|{     }
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|(
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|separatorNeeded
init|=
literal|false
decl_stmt|;
name|Pattern
name|ns
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|namespace
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"\\\\."
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\*"
argument_list|,
literal|".*"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Bundle
name|b
range|:
name|bundles
control|)
block|{
if|if
condition|(
name|separatorNeeded
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
block|}
comment|// Print out any matching generic requirements.
name|BundleWiring
name|wiring
init|=
name|b
operator|.
name|adapt
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|wiring
operator|!=
literal|null
condition|)
block|{
name|String
name|title
init|=
name|b
operator|+
literal|" requires:"
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
name|boolean
name|matches
init|=
name|printMatchingRequirements
argument_list|(
name|wiring
argument_list|,
name|ns
argument_list|)
decl_stmt|;
comment|// Handle service requirements separately, since they aren't part
comment|// of the generic model in OSGi.
if|if
condition|(
name|matchNamespace
argument_list|(
name|ns
argument_list|,
name|NONSTANDARD_SERVICE_NAMESPACE
argument_list|)
condition|)
block|{
name|matches
operator||=
name|printServiceRequirements
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
comment|// If there were no requirements for the specified namespace,
comment|// then say so.
if|if
condition|(
operator|!
name|matches
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|namespace
operator|+
literal|" "
operator|+
name|EMPTY_MESSAGE
argument_list|)
expr_stmt|;
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
literal|"Bundle "
operator|+
name|b
operator|.
name|getBundleId
argument_list|()
operator|+
literal|" is not resolved."
argument_list|)
expr_stmt|;
block|}
name|separatorNeeded
operator|=
literal|true
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|boolean
name|printMatchingRequirements
parameter_list|(
name|BundleWiring
name|wiring
parameter_list|,
name|Pattern
name|namespace
parameter_list|)
block|{
name|List
argument_list|<
name|BundleWire
argument_list|>
name|wires
init|=
name|wiring
operator|.
name|getRequiredWires
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|BundleRequirement
argument_list|,
name|List
argument_list|<
name|BundleWire
argument_list|>
argument_list|>
name|aggregateReqs
init|=
name|aggregateRequirements
argument_list|(
name|namespace
argument_list|,
name|wires
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BundleRequirement
argument_list|>
name|allReqs
init|=
name|wiring
operator|.
name|getRequirements
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|boolean
name|matches
init|=
literal|false
decl_stmt|;
for|for
control|(
name|BundleRequirement
name|req
range|:
name|allReqs
control|)
block|{
if|if
condition|(
name|matchNamespace
argument_list|(
name|namespace
argument_list|,
name|req
operator|.
name|getNamespace
argument_list|()
argument_list|)
condition|)
block|{
name|matches
operator|=
literal|true
expr_stmt|;
name|List
argument_list|<
name|BundleWire
argument_list|>
name|providers
init|=
name|aggregateReqs
operator|.
name|get
argument_list|(
name|req
argument_list|)
decl_stmt|;
if|if
condition|(
name|providers
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
name|req
operator|.
name|getNamespace
argument_list|()
operator|+
literal|"; "
operator|+
name|req
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|FILTER_DIRECTIVE
argument_list|)
operator|+
literal|" resolved by:"
argument_list|)
expr_stmt|;
for|for
control|(
name|BundleWire
name|wire
range|:
name|providers
control|)
block|{
name|String
name|msg
decl_stmt|;
name|Object
name|keyAttr
init|=
name|wire
operator|.
name|getCapability
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|wire
operator|.
name|getCapability
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyAttr
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
name|wire
operator|.
name|getCapability
argument_list|()
operator|.
name|getNamespace
argument_list|()
operator|+
literal|"; "
operator|+
name|keyAttr
operator|+
literal|" "
operator|+
name|getVersionFromCapability
argument_list|(
name|wire
operator|.
name|getCapability
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|wire
operator|.
name|getCapability
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|msg
operator|=
literal|"   "
operator|+
name|msg
operator|+
literal|" from "
operator|+
name|wire
operator|.
name|getProviderWiring
argument_list|()
operator|.
name|getBundle
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
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
name|req
operator|.
name|getNamespace
argument_list|()
operator|+
literal|"; "
operator|+
name|req
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|FILTER_DIRECTIVE
argument_list|)
operator|+
literal|" "
operator|+
name|UNRESOLVED_MESSAGE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|matches
return|;
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|BundleRequirement
argument_list|,
name|List
argument_list|<
name|BundleWire
argument_list|>
argument_list|>
name|aggregateRequirements
parameter_list|(
name|Pattern
name|namespace
parameter_list|,
name|List
argument_list|<
name|BundleWire
argument_list|>
name|wires
parameter_list|)
block|{
comment|// Aggregate matching capabilities.
name|Map
argument_list|<
name|BundleRequirement
argument_list|,
name|List
argument_list|<
name|BundleWire
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|BundleWire
name|wire
range|:
name|wires
control|)
block|{
if|if
condition|(
name|matchNamespace
argument_list|(
name|namespace
argument_list|,
name|wire
operator|.
name|getRequirement
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|)
condition|)
block|{
name|map
operator|.
name|computeIfAbsent
argument_list|(
name|wire
operator|.
name|getRequirement
argument_list|()
argument_list|,
name|k
lambda|->
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|wire
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
specifier|static
name|boolean
name|printServiceRequirements
parameter_list|(
name|Bundle
name|b
parameter_list|)
block|{
name|boolean
name|matches
init|=
literal|false
decl_stmt|;
try|try
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|refs
init|=
name|b
operator|.
name|getServicesInUse
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|refs
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|refs
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|matches
operator|=
literal|true
expr_stmt|;
comment|// Print properties for each service.
for|for
control|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|ref
range|:
name|refs
control|)
block|{
comment|// Print object class with "namespace".
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|NONSTANDARD_SERVICE_NAMESPACE
operator|+
literal|"; "
operator|+
name|ShellUtil
operator|.
name|getValueString
argument_list|(
name|ref
operator|.
name|getProperty
argument_list|(
literal|"objectClass"
argument_list|)
argument_list|)
operator|+
literal|" provided by:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|ref
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|ex
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|matches
return|;
block|}
specifier|private
specifier|static
name|String
name|getVersionFromCapability
parameter_list|(
name|BundleCapability
name|c
parameter_list|)
block|{
name|Object
name|o
init|=
name|c
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
name|c
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION_ATTRIBUTE
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|o
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
name|o
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|boolean
name|matchNamespace
parameter_list|(
name|Pattern
name|namespace
parameter_list|,
name|String
name|actual
parameter_list|)
block|{
return|return
name|namespace
operator|.
name|matcher
argument_list|(
name|actual
argument_list|)
operator|.
name|matches
argument_list|()
return|;
block|}
block|}
end_class

end_unit

