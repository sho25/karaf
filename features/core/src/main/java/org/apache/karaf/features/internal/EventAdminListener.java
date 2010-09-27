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
name|features
operator|.
name|internal
package|;
end_package

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
name|Hashtable
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
name|features
operator|.
name|EventConstants
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
name|features
operator|.
name|FeatureEvent
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
name|features
operator|.
name|FeaturesListener
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
name|features
operator|.
name|RepositoryEvent
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
name|service
operator|.
name|event
operator|.
name|Event
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
name|event
operator|.
name|EventAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_comment
comment|/**  * A listener to publish events to EventAdmin  */
end_comment

begin_class
specifier|public
class|class
name|EventAdminListener
implements|implements
name|FeaturesListener
block|{
specifier|private
specifier|final
name|ServiceTracker
name|tracker
decl_stmt|;
specifier|public
name|EventAdminListener
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|(
name|context
argument_list|,
name|EventAdmin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|featureEvent
parameter_list|(
name|FeatureEvent
name|event
parameter_list|)
block|{
name|EventAdmin
name|eventAdmin
init|=
operator|(
name|EventAdmin
operator|)
name|tracker
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|eventAdmin
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|TYPE
argument_list|,
name|event
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|EVENT
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|TIMESTAMP
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|FEATURE_NAME
argument_list|,
name|event
operator|.
name|getFeature
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|FEATURE_VERSION
argument_list|,
name|event
operator|.
name|getFeature
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|topic
decl_stmt|;
switch|switch
condition|(
name|event
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|FeatureInstalled
case|:
name|topic
operator|=
name|EventConstants
operator|.
name|TOPIC_FEATURES_INSTALLED
expr_stmt|;
break|break;
case|case
name|FeatureUninstalled
case|:
name|topic
operator|=
name|EventConstants
operator|.
name|TOPIC_FEATURES_UNINSTALLED
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown features event type: "
operator|+
name|event
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
name|eventAdmin
operator|.
name|postEvent
argument_list|(
operator|new
name|Event
argument_list|(
name|topic
argument_list|,
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|repositoryEvent
parameter_list|(
name|RepositoryEvent
name|event
parameter_list|)
block|{
name|EventAdmin
name|eventAdmin
init|=
operator|(
name|EventAdmin
operator|)
name|tracker
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|eventAdmin
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|TYPE
argument_list|,
name|event
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|EVENT
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|TIMESTAMP
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|REPOSITORY_NAME
argument_list|,
name|event
operator|.
name|getRepository
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|REPOSITORY_URI
argument_list|,
name|event
operator|.
name|getRepository
argument_list|()
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|topic
decl_stmt|;
switch|switch
condition|(
name|event
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|RepositoryAdded
case|:
name|topic
operator|=
name|EventConstants
operator|.
name|TOPIC_REPOSITORY_ADDED
expr_stmt|;
break|break;
case|case
name|RepositoryRemoved
case|:
name|topic
operator|=
name|EventConstants
operator|.
name|TOPIC_REPOSITORY_REMOVED
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown repository event type: "
operator|+
name|event
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
name|eventAdmin
operator|.
name|postEvent
argument_list|(
operator|new
name|Event
argument_list|(
name|topic
argument_list|,
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

