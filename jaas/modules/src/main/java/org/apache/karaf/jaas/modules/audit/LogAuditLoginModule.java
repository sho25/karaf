begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|audit
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
name|jaas
operator|.
name|modules
operator|.
name|JAASUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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

begin_class
specifier|public
class|class
name|LogAuditLoginModule
extends|extends
name|AbstractAuditLoginModule
block|{
specifier|public
specifier|static
specifier|final
name|String
name|LOG_LEVEL_OPTION
init|=
literal|"level"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOG_LOGGER_OPTION
init|=
literal|"logger"
decl_stmt|;
specifier|private
name|String
name|level
init|=
literal|"INFO"
decl_stmt|;
specifier|private
name|Logger
name|logger
decl_stmt|;
specifier|public
name|void
name|initialize
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|sharedState
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
parameter_list|)
block|{
name|super
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
name|callbackHandler
argument_list|,
name|sharedState
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|level
operator|=
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|LOG_LEVEL_OPTION
argument_list|)
expr_stmt|;
name|logger
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|LOG_LOGGER_OPTION
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|synchronized
name|void
name|audit
parameter_list|(
name|Action
name|action
parameter_list|,
name|String
name|username
parameter_list|)
block|{
name|String
name|actionStr
decl_stmt|;
switch|switch
condition|(
name|action
condition|)
block|{
case|case
name|ATTEMPT
case|:
name|actionStr
operator|=
literal|"Authentication attempt"
expr_stmt|;
break|break;
case|case
name|SUCCESS
case|:
name|actionStr
operator|=
literal|"Authentication succeeded"
expr_stmt|;
break|break;
case|case
name|FAILURE
case|:
name|actionStr
operator|=
literal|"Authentication failed"
expr_stmt|;
break|break;
case|case
name|LOGOUT
case|:
name|actionStr
operator|=
literal|"Explicit logout"
expr_stmt|;
break|break;
default|default:
name|actionStr
operator|=
name|action
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|level
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"debug"
argument_list|)
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"{} - {} - {}"
argument_list|,
name|actionStr
argument_list|,
name|username
argument_list|,
name|getPrincipalInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|level
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"trace"
argument_list|)
condition|)
block|{
name|logger
operator|.
name|trace
argument_list|(
literal|"{} - {} - {}"
argument_list|,
name|actionStr
argument_list|,
name|username
argument_list|,
name|getPrincipalInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|level
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"warn"
argument_list|)
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"{} - {} - {}"
argument_list|,
name|actionStr
argument_list|,
name|username
argument_list|,
name|getPrincipalInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|level
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"error"
argument_list|)
condition|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"{} - {} - {}"
argument_list|,
name|actionStr
argument_list|,
name|username
argument_list|,
name|getPrincipalInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"{} - {} - {}"
argument_list|,
name|actionStr
argument_list|,
name|username
argument_list|,
name|getPrincipalInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

