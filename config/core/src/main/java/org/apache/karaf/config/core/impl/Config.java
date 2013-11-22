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
name|config
operator|.
name|core
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|Hashtable
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
name|javax
operator|.
name|management
operator|.
name|MBeanException
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|config
operator|.
name|core
operator|.
name|ConfigMBean
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
name|config
operator|.
name|core
operator|.
name|ConfigRepository
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

begin_comment
comment|/**  * Implementation of the ConfigMBean.  */
end_comment

begin_class
specifier|public
class|class
name|Config
extends|extends
name|StandardMBean
implements|implements
name|ConfigMBean
block|{
specifier|private
name|ConfigRepository
name|configRepo
decl_stmt|;
specifier|public
name|Config
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
specifier|private
name|Configuration
name|getConfiguration
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|IOException
block|{
name|Configuration
name|configuration
init|=
name|configRepo
operator|.
name|getConfigAdmin
argument_list|()
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
return|return
name|configuration
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
name|Dictionary
name|getConfigProperties
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|IOException
block|{
name|Configuration
name|configuration
init|=
name|getConfiguration
argument_list|(
name|pid
argument_list|)
decl_stmt|;
name|Dictionary
name|dictionary
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|dictionary
operator|==
literal|null
condition|)
block|{
name|dictionary
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|Properties
argument_list|()
expr_stmt|;
block|}
return|return
name|dictionary
return|;
block|}
comment|/**      * Get all config pids      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getConfigs
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
name|Configuration
index|[]
name|configurations
init|=
name|this
operator|.
name|configRepo
operator|.
name|getConfigAdmin
argument_list|()
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|void
name|create
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|configRepo
operator|.
name|update
argument_list|(
name|pid
argument_list|,
operator|new
name|Hashtable
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|update
parameter_list|(
name|String
name|pid
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dictionary
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|properties
operator|.
name|keySet
argument_list|()
control|)
block|{
name|dictionary
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|configRepo
operator|.
name|update
argument_list|(
name|pid
argument_list|,
name|dictionary
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|delete
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|this
operator|.
name|configRepo
operator|.
name|delete
argument_list|(
name|pid
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|listProperties
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|Dictionary
name|dictionary
init|=
name|getConfigProperties
argument_list|(
name|pid
argument_list|)
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|void
name|deleteProperty
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|key
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|Dictionary
name|dictionary
init|=
name|getConfigProperties
argument_list|(
name|pid
argument_list|)
decl_stmt|;
name|dictionary
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|configRepo
operator|.
name|update
argument_list|(
name|pid
argument_list|,
name|dictionary
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|public
name|void
name|appendProperty
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
name|MBeanException
block|{
try|try
block|{
name|Dictionary
name|dictionary
init|=
name|getConfigProperties
argument_list|(
name|pid
argument_list|)
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
name|configRepo
operator|.
name|update
argument_list|(
name|pid
argument_list|,
name|dictionary
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|public
name|void
name|setProperty
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
name|MBeanException
block|{
try|try
block|{
name|Dictionary
name|dictionary
init|=
name|getConfigProperties
argument_list|(
name|pid
argument_list|)
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
name|configRepo
operator|.
name|update
argument_list|(
name|pid
argument_list|,
name|dictionary
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setConfigRepo
parameter_list|(
name|ConfigRepository
name|configRepo
parameter_list|)
block|{
name|this
operator|.
name|configRepo
operator|=
name|configRepo
expr_stmt|;
block|}
block|}
end_class

end_unit

