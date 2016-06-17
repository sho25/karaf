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
name|scr
operator|.
name|command
operator|.
name|action
package|;
end_package

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
name|TreeMap
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
name|scr
operator|.
name|command
operator|.
name|ScrCommandConstants
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
name|scr
operator|.
name|command
operator|.
name|ScrUtils
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
name|scr
operator|.
name|command
operator|.
name|completer
operator|.
name|DetailsCompleter
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
name|ansi
operator|.
name|SimpleAnsi
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
name|dto
operator|.
name|ServiceReferenceDTO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|ComponentConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|runtime
operator|.
name|ServiceComponentRuntime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|runtime
operator|.
name|dto
operator|.
name|ComponentConfigurationDTO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|runtime
operator|.
name|dto
operator|.
name|ComponentDescriptionDTO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|runtime
operator|.
name|dto
operator|.
name|ReferenceDTO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|runtime
operator|.
name|dto
operator|.
name|SatisfiedReferenceDTO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|runtime
operator|.
name|dto
operator|.
name|UnsatisfiedReferenceDTO
import|;
end_import

begin_comment
comment|/**  * Display the details associated with a given component by supplying its component name.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
name|ScrCommandConstants
operator|.
name|SCR_COMMAND
argument_list|,
name|name
operator|=
name|ScrCommandConstants
operator|.
name|DETAILS_FUNCTION
argument_list|,
name|description
operator|=
literal|"Display available components"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|DetailsAction
extends|extends
name|ScrActionSupport
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
literal|"name"
argument_list|,
name|description
operator|=
literal|"The component name"
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
name|DetailsCompleter
operator|.
name|class
argument_list|)
name|String
name|name
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|protected
name|Object
name|doScrAction
parameter_list|(
name|ServiceComponentRuntime
name|serviceComponentRuntime
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Executing the Details Action"
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|SimpleAnsi
operator|.
name|INTENSITY_BOLD
operator|+
literal|"Component Details"
operator|+
name|SimpleAnsi
operator|.
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
for|for
control|(
name|ComponentDescriptionDTO
name|component
range|:
name|serviceComponentRuntime
operator|.
name|getComponentDescriptionDTOs
argument_list|()
control|)
block|{
for|for
control|(
name|ComponentConfigurationDTO
name|config
range|:
name|serviceComponentRuntime
operator|.
name|getComponentConfigurationDTOs
argument_list|(
name|component
argument_list|)
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|component
operator|.
name|name
argument_list|)
condition|)
block|{
name|printDetail
argument_list|(
literal|"  Name                : "
argument_list|,
name|component
operator|.
name|name
argument_list|)
expr_stmt|;
name|printDetail
argument_list|(
literal|"  State               : "
argument_list|,
name|ScrUtils
operator|.
name|getState
argument_list|(
name|config
operator|.
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|component
operator|.
name|properties
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|SimpleAnsi
operator|.
name|INTENSITY_BOLD
operator|+
literal|"  Properties          : "
operator|+
name|SimpleAnsi
operator|.
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|printDetail
argument_list|(
literal|"    "
argument_list|,
name|key
operator|+
literal|"="
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|ReferenceDTO
index|[]
name|references
init|=
name|component
operator|.
name|references
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|SimpleAnsi
operator|.
name|INTENSITY_BOLD
operator|+
literal|"References"
operator|+
name|SimpleAnsi
operator|.
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
for|for
control|(
name|ReferenceDTO
name|reference
range|:
name|ScrUtils
operator|.
name|emptyIfNull
argument_list|(
name|ReferenceDTO
operator|.
name|class
argument_list|,
name|references
argument_list|)
control|)
block|{
name|ServiceReferenceDTO
index|[]
name|boundServices
init|=
literal|null
decl_stmt|;
name|boolean
name|satisfied
init|=
literal|true
decl_stmt|;
for|for
control|(
name|SatisfiedReferenceDTO
name|satRef
range|:
name|config
operator|.
name|satisfiedReferences
control|)
block|{
if|if
condition|(
name|satRef
operator|.
name|name
operator|.
name|equals
argument_list|(
name|reference
operator|.
name|name
argument_list|)
condition|)
block|{
name|boundServices
operator|=
name|satRef
operator|.
name|boundServices
expr_stmt|;
name|satisfied
operator|=
literal|true
expr_stmt|;
block|}
block|}
for|for
control|(
name|UnsatisfiedReferenceDTO
name|satRef
range|:
name|config
operator|.
name|unsatisfiedReferences
control|)
block|{
if|if
condition|(
name|satRef
operator|.
name|name
operator|.
name|equals
argument_list|(
name|reference
operator|.
name|name
argument_list|)
condition|)
block|{
name|boundServices
operator|=
name|satRef
operator|.
name|targetServices
expr_stmt|;
name|satisfied
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|printDetail
argument_list|(
literal|"  Reference           : "
argument_list|,
name|reference
operator|.
name|name
argument_list|)
expr_stmt|;
name|printDetail
argument_list|(
literal|"    State             : "
argument_list|,
name|satisfied
condition|?
literal|"satisfied"
else|:
literal|"unsatisfied"
argument_list|)
expr_stmt|;
name|printDetail
argument_list|(
literal|"    Cardinality       : "
argument_list|,
name|reference
operator|.
name|cardinality
argument_list|)
expr_stmt|;
name|printDetail
argument_list|(
literal|"    Policy            : "
argument_list|,
name|reference
operator|.
name|policy
argument_list|)
expr_stmt|;
name|printDetail
argument_list|(
literal|"    PolicyOption      : "
argument_list|,
name|reference
operator|.
name|policyOption
argument_list|)
expr_stmt|;
comment|// list bound services
for|for
control|(
name|ServiceReferenceDTO
name|serviceReference
range|:
name|ScrUtils
operator|.
name|emptyIfNull
argument_list|(
name|ServiceReferenceDTO
operator|.
name|class
argument_list|,
name|boundServices
argument_list|)
control|)
block|{
specifier|final
name|StringBuffer
name|b
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"Bound Service ID "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|serviceReference
operator|.
name|properties
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|componentName
init|=
operator|(
name|String
operator|)
name|serviceReference
operator|.
name|properties
operator|.
name|get
argument_list|(
name|ComponentConstants
operator|.
name|COMPONENT_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|componentName
operator|==
literal|null
condition|)
block|{
name|componentName
operator|=
operator|(
name|String
operator|)
name|serviceReference
operator|.
name|properties
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|SERVICE_PID
argument_list|)
expr_stmt|;
if|if
condition|(
name|componentName
operator|==
literal|null
condition|)
block|{
name|componentName
operator|=
operator|(
name|String
operator|)
name|serviceReference
operator|.
name|properties
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|SERVICE_DESCRIPTION
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|componentName
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|componentName
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
name|printDetail
argument_list|(
literal|"    Service Reference : "
argument_list|,
name|b
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ScrUtils
operator|.
name|emptyIfNull
argument_list|(
name|ServiceReferenceDTO
operator|.
name|class
argument_list|,
name|boundServices
argument_list|)
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|printDetail
argument_list|(
literal|"    Service Reference : "
argument_list|,
literal|"No Services bound"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|printDetail
parameter_list|(
name|String
name|header
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|SimpleAnsi
operator|.
name|INTENSITY_BOLD
operator|+
name|header
operator|+
name|SimpleAnsi
operator|.
name|INTENSITY_NORMAL
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

