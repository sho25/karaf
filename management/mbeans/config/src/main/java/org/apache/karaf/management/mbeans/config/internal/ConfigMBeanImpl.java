begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|management
operator|.
name|mbeans
operator|.
name|config
operator|.
name|internal
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
name|management
operator|.
name|mbeans
operator|.
name|config
operator|.
name|ConfigMBean
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
name|cm
operator|.
name|Configuration
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
name|cm
operator|.
name|ConfigurationAdmin
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
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * Implementation of the ConfigMBean.  */
end_comment

begin_class
specifier|public
class|class
name|ConfigMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|ConfigMBean
block|{
specifier|private
name|ConfigurationAdmin
name|configurationAdmin
decl_stmt|;
specifier|public
name|ConfigurationAdmin
name|getConfigurationAdmin
parameter_list|()
block|{
return|return
name|this
operator|.
name|configurationAdmin
return|;
block|}
specifier|public
name|void
name|setConfigurationAdmin
parameter_list|(
name|ConfigurationAdmin
name|configurationAdmin
parameter_list|)
block|{
name|this
operator|.
name|configurationAdmin
operator|=
name|configurationAdmin
expr_stmt|;
block|}
specifier|public
name|ConfigMBeanImpl
parameter_list|()
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|ConfigMBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
index|[]
name|configurations
init|=
name|configurationAdmin
operator|.
name|listConfigurations
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pids
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|configurations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|pids
operator|.
name|add
argument_list|(
name|configurations
index|[
name|i
index|]
operator|.
name|getPid
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|pids
return|;
block|}
specifier|public
name|void
name|delete
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Configuration PID "
operator|+
name|pid
operator|+
literal|" doesn't exist"
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|proplist
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Configuration PID "
operator|+
name|pid
operator|+
literal|" doesn't exist"
argument_list|)
throw|;
block|}
name|Dictionary
name|dictionary
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|propertiesMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Enumeration
name|e
init|=
name|dictionary
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|Object
name|key
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|dictionary
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|propertiesMap
operator|.
name|put
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|propertiesMap
return|;
block|}
specifier|public
name|void
name|propdel
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|key
parameter_list|)
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Configuration PID "
operator|+
name|pid
operator|+
literal|" doesn't exist"
argument_list|)
throw|;
block|}
name|Dictionary
name|dictionary
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|dictionary
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|propappend
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Configuration PID "
operator|+
name|pid
operator|+
literal|" doesn't exist"
argument_list|)
throw|;
block|}
name|Dictionary
name|dictionary
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|Object
name|currentValue
init|=
name|dictionary
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentValue
operator|==
literal|null
condition|)
block|{
name|dictionary
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|currentValue
operator|instanceof
name|String
condition|)
block|{
name|dictionary
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|currentValue
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Current value is not a String"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|propset
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Configuration PID "
operator|+
name|pid
operator|+
literal|" doesn't exist"
argument_list|)
throw|;
block|}
name|Dictionary
name|dictionary
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|dictionary
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

