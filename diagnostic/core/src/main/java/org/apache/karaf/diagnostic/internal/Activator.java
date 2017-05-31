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
name|diagnostic
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
name|ArrayList
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|diagnostic
operator|.
name|core
operator|.
name|DumpProvider
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
name|diagnostic
operator|.
name|management
operator|.
name|internal
operator|.
name|DiagnosticDumpMBeanImpl
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
name|diagnostic
operator|.
name|common
operator|.
name|FeaturesDumpProvider
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
name|diagnostic
operator|.
name|common
operator|.
name|LogDumpProvider
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
name|FeaturesService
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
name|internal
operator|.
name|model
operator|.
name|Features
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
name|tracker
operator|.
name|SingleServiceTracker
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
name|BundleActivator
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
name|ServiceRegistration
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

begin_class
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
block|{
specifier|private
name|List
argument_list|<
name|ServiceRegistration
argument_list|<
name|DumpProvider
argument_list|>
argument_list|>
name|registrations
decl_stmt|;
specifier|private
name|ServiceRegistration
argument_list|<
name|DumpProvider
argument_list|>
name|featuresProviderRegistration
decl_stmt|;
specifier|private
name|ServiceRegistration
name|mbeanRegistration
decl_stmt|;
specifier|private
name|SingleServiceTracker
argument_list|<
name|FeaturesService
argument_list|>
name|featuresServiceTracker
decl_stmt|;
specifier|private
name|ServiceTracker
argument_list|<
name|DumpProvider
argument_list|,
name|DumpProvider
argument_list|>
name|providersTracker
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
specifier|final
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|registrations
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|registrations
operator|.
name|add
argument_list|(
name|context
operator|.
name|registerService
argument_list|(
name|DumpProvider
operator|.
name|class
argument_list|,
operator|new
name|LogDumpProvider
argument_list|(
name|context
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|featuresServiceTracker
operator|=
operator|new
name|SingleServiceTracker
argument_list|<>
argument_list|(
name|context
argument_list|,
name|FeaturesService
operator|.
name|class
argument_list|,
parameter_list|(
name|oldFs
parameter_list|,
name|newFs
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|featuresProviderRegistration
operator|!=
literal|null
condition|)
block|{
name|featuresProviderRegistration
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|featuresProviderRegistration
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|newFs
operator|!=
literal|null
condition|)
block|{
name|featuresProviderRegistration
operator|=
name|context
operator|.
name|registerService
argument_list|(
name|DumpProvider
operator|.
name|class
argument_list|,
operator|new
name|FeaturesDumpProvider
argument_list|(
name|newFs
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|featuresServiceTracker
operator|.
name|open
argument_list|()
expr_stmt|;
specifier|final
name|DiagnosticDumpMBeanImpl
name|diagnostic
init|=
operator|new
name|DiagnosticDumpMBeanImpl
argument_list|()
decl_stmt|;
name|diagnostic
operator|.
name|setBundleContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"jmx.objectname"
argument_list|,
literal|"org.apache.karaf:type=diagnostic,name="
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|)
argument_list|)
expr_stmt|;
name|mbeanRegistration
operator|=
name|context
operator|.
name|registerService
argument_list|(
name|getInterfaceNames
argument_list|(
name|diagnostic
argument_list|)
argument_list|,
name|diagnostic
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|mbeanRegistration
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|featuresServiceTracker
operator|.
name|close
argument_list|()
expr_stmt|;
for|for
control|(
name|ServiceRegistration
argument_list|<
name|DumpProvider
argument_list|>
name|reg
range|:
name|registrations
control|)
block|{
name|reg
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|String
index|[]
name|getInterfaceNames
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|cl
init|=
name|object
operator|.
name|getClass
argument_list|()
init|;
name|cl
operator|!=
name|Object
operator|.
name|class
condition|;
name|cl
operator|=
name|cl
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
name|addSuperInterfaces
argument_list|(
name|names
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
return|return
name|names
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|names
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|void
name|addSuperInterfaces
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|names
parameter_list|,
name|Class
name|clazz
parameter_list|)
block|{
for|for
control|(
name|Class
name|cl
range|:
name|clazz
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|cl
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addSuperInterfaces
argument_list|(
name|names
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

