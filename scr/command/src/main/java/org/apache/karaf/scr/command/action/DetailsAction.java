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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|scr
operator|.
name|Component
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
name|scr
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
name|felix
operator|.
name|scr
operator|.
name|ScrService
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
name|service
operator|.
name|component
operator|.
name|ComponentConstants
import|;
end_import

begin_comment
comment|/**  * Displays the details associated with a given component by supplying its component name.  */
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
literal|"Displays a list of available components"
argument_list|)
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
literal|"The name of the Component to display the detials of"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
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
name|ScrService
name|scrService
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
name|getBoldString
argument_list|(
literal|"Component Details"
argument_list|)
argument_list|)
expr_stmt|;
name|Component
index|[]
name|components
init|=
name|scrService
operator|.
name|getComponents
argument_list|(
name|name
argument_list|)
decl_stmt|;
for|for
control|(
name|Component
name|component
range|:
name|ScrUtils
operator|.
name|emptyIfNull
argument_list|(
name|Component
operator|.
name|class
argument_list|,
name|components
argument_list|)
control|)
block|{
name|printDetail
argument_list|(
literal|"  Name                : "
argument_list|,
name|component
operator|.
name|getName
argument_list|()
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
name|component
operator|.
name|getState
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Reference
index|[]
name|references
init|=
name|component
operator|.
name|getReferences
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|getBoldString
argument_list|(
literal|"References"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Reference
name|reference
range|:
name|ScrUtils
operator|.
name|emptyIfNull
argument_list|(
name|Reference
operator|.
name|class
argument_list|,
name|references
argument_list|)
control|)
block|{
name|printDetail
argument_list|(
literal|"  Reference           : "
argument_list|,
name|reference
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|printDetail
argument_list|(
literal|"    State             : "
argument_list|,
operator|(
name|reference
operator|.
name|isSatisfied
argument_list|()
operator|)
condition|?
literal|"satisfied"
else|:
literal|"unsatisfied"
argument_list|)
expr_stmt|;
name|printDetail
argument_list|(
literal|"    Multiple          : "
argument_list|,
operator|(
name|reference
operator|.
name|isMultiple
argument_list|()
condition|?
literal|"multiple"
else|:
literal|"single"
operator|)
argument_list|)
expr_stmt|;
name|printDetail
argument_list|(
literal|"    Optional          : "
argument_list|,
operator|(
name|reference
operator|.
name|isOptional
argument_list|()
condition|?
literal|"optional"
else|:
literal|"mandatory"
operator|)
argument_list|)
expr_stmt|;
name|printDetail
argument_list|(
literal|"    Policy            : "
argument_list|,
operator|(
name|reference
operator|.
name|isStatic
argument_list|()
condition|?
literal|"static"
else|:
literal|"dynamic"
operator|)
argument_list|)
expr_stmt|;
comment|// list bound services
name|ServiceReference
index|[]
name|boundRefs
init|=
name|reference
operator|.
name|getServiceReferences
argument_list|()
decl_stmt|;
for|for
control|(
name|ServiceReference
name|serviceReference
range|:
name|ScrUtils
operator|.
name|emptyIfNull
argument_list|(
name|ServiceReference
operator|.
name|class
argument_list|,
name|boundRefs
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
name|getProperty
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
name|getProperty
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
name|getProperty
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
name|getProperty
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
name|ServiceReference
operator|.
name|class
argument_list|,
name|boundRefs
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
name|getBoldString
argument_list|(
name|header
argument_list|)
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

