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
name|management
operator|.
name|mbeans
operator|.
name|services
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
name|services
operator|.
name|ServicesMBean
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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|*
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
name|List
import|;
end_import

begin_comment
comment|/**  * Implementation of the Services MBean.  */
end_comment

begin_class
specifier|public
class|class
name|ServicesMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|ServicesMBean
block|{
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|public
name|ServicesMBeanImpl
parameter_list|()
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|ServicesMBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TabularData
name|getService
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getService
argument_list|(
operator|-
literal|1
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|TabularData
name|getService
parameter_list|(
name|boolean
name|inUse
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getService
argument_list|(
operator|-
literal|1
argument_list|,
name|inUse
argument_list|)
return|;
block|}
specifier|public
name|TabularData
name|getService
parameter_list|(
name|long
name|bundleId
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getService
argument_list|(
name|bundleId
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|TabularData
name|getService
parameter_list|(
name|long
name|bundleId
parameter_list|,
name|boolean
name|inUse
parameter_list|)
throws|throws
name|Exception
block|{
name|CompositeType
name|serviceType
init|=
operator|new
name|CompositeType
argument_list|(
literal|"Service"
argument_list|,
literal|"OSGi Service"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Interfaces"
block|,
literal|"Properties"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Interfaces class name of the service"
block|,
literal|"Properties of the service"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
operator|new
name|ArrayType
argument_list|(
literal|1
argument_list|,
name|SimpleType
operator|.
name|STRING
argument_list|)
block|,
operator|new
name|ArrayType
argument_list|(
literal|1
argument_list|,
name|SimpleType
operator|.
name|STRING
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|TabularType
name|tableType
init|=
operator|new
name|TabularType
argument_list|(
literal|"Services"
argument_list|,
literal|"Table of OSGi Services"
argument_list|,
name|serviceType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Interfaces"
block|}
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|tableType
argument_list|)
decl_stmt|;
name|Bundle
index|[]
name|bundles
decl_stmt|;
if|if
condition|(
name|bundleId
operator|>=
literal|0
condition|)
block|{
name|bundles
operator|=
operator|new
name|Bundle
index|[]
block|{
name|bundleContext
operator|.
name|getBundle
argument_list|(
name|bundleId
argument_list|)
block|}
expr_stmt|;
block|}
else|else
block|{
name|bundles
operator|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
empty_stmt|;
name|ServiceReference
index|[]
name|serviceReferences
decl_stmt|;
if|if
condition|(
name|inUse
condition|)
block|{
name|serviceReferences
operator|=
name|bundle
operator|.
name|getServicesInUse
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|serviceReferences
operator|=
name|bundle
operator|.
name|getRegisteredServices
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|serviceReferences
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|reference
range|:
name|serviceReferences
control|)
block|{
name|String
index|[]
name|interfaces
init|=
operator|(
name|String
index|[]
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"objectClass"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|properties
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
name|String
name|key
range|:
name|reference
operator|.
name|getPropertyKeys
argument_list|()
control|)
block|{
name|properties
operator|.
name|add
argument_list|(
name|key
operator|+
literal|" = "
operator|+
name|reference
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|serviceType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Interfaces"
block|,
literal|"Properties"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|interfaces
block|,
name|properties
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|table
return|;
block|}
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|this
operator|.
name|bundleContext
return|;
block|}
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
block|}
end_class

end_unit

