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
name|jdbc
operator|.
name|command
operator|.
name|ds
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|jdbc
operator|.
name|command
operator|.
name|JdbcCommandSupport
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
name|lifecycle
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
name|table
operator|.
name|ShellTable
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
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|jdbc
operator|.
name|DataSourceFactory
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"jdbc"
argument_list|,
name|name
operator|=
literal|"ds-factories"
argument_list|,
name|description
operator|=
literal|"List the JDBC DataSourceFactories"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|DSFactoriesCommand
extends|extends
name|JdbcCommandSupport
block|{
annotation|@
name|Reference
name|BundleContext
name|context
decl_stmt|;
specifier|private
name|Comparator
argument_list|<
name|DataSourceFactoryInfo
argument_list|>
name|comparator
init|=
operator|new
name|DataSourceFactoryComparator
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Name"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Class"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Version"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Registration bundle"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|DataSourceFactoryInfo
argument_list|>
name|factories
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|DataSourceFactory
argument_list|>
argument_list|>
name|refs
init|=
name|context
operator|.
name|getServiceReferences
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|ServiceReference
argument_list|<
name|DataSourceFactory
argument_list|>
name|ref
range|:
name|refs
control|)
block|{
name|DataSourceFactoryInfo
name|info
init|=
operator|new
name|DataSourceFactoryInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|driverName
operator|=
operator|(
name|String
operator|)
name|ref
operator|.
name|getProperty
argument_list|(
name|DataSourceFactory
operator|.
name|OSGI_JDBC_DRIVER_NAME
argument_list|)
expr_stmt|;
name|info
operator|.
name|driverClass
operator|=
operator|(
name|String
operator|)
name|ref
operator|.
name|getProperty
argument_list|(
name|DataSourceFactory
operator|.
name|OSGI_JDBC_DRIVER_CLASS
argument_list|)
expr_stmt|;
name|info
operator|.
name|driverVersion
operator|=
operator|(
name|String
operator|)
name|ref
operator|.
name|getProperty
argument_list|(
name|DataSourceFactory
operator|.
name|OSGI_JDBC_DRIVER_VERSION
argument_list|)
expr_stmt|;
if|if
condition|(
name|ref
operator|.
name|getBundle
argument_list|()
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|bundle
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"%s [%s]"
argument_list|,
name|ref
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|ref
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|info
operator|.
name|bundle
operator|=
literal|""
expr_stmt|;
block|}
name|factories
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DataSourceFactoryInfo
name|info
range|:
name|factories
control|)
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|info
operator|.
name|driverName
argument_list|,
name|info
operator|.
name|driverClass
argument_list|,
name|info
operator|.
name|driverVersion
argument_list|,
name|info
operator|.
name|bundle
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
class|class
name|DataSourceFactoryComparator
implements|implements
name|Comparator
argument_list|<
name|DataSourceFactoryInfo
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|DataSourceFactoryInfo
name|dsf1
parameter_list|,
name|DataSourceFactoryInfo
name|dsf2
parameter_list|)
block|{
name|int
name|r1
init|=
literal|0
decl_stmt|;
name|int
name|r2
init|=
literal|0
decl_stmt|;
name|int
name|r3
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|dsf1
operator|.
name|bundle
operator|!=
literal|null
operator|||
name|dsf2
operator|.
name|bundle
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dsf1
operator|.
name|bundle
operator|==
literal|null
condition|)
block|{
name|r1
operator|=
operator|-
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dsf2
operator|.
name|bundle
operator|==
literal|null
condition|)
block|{
name|r1
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
name|r1
operator|=
name|dsf1
operator|.
name|bundle
operator|.
name|compareTo
argument_list|(
name|dsf2
operator|.
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dsf1
operator|.
name|driverName
operator|!=
literal|null
operator|||
name|dsf2
operator|.
name|driverName
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dsf1
operator|.
name|driverName
operator|==
literal|null
condition|)
block|{
name|r2
operator|=
operator|-
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dsf2
operator|.
name|driverName
operator|==
literal|null
condition|)
block|{
name|r2
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
name|r2
operator|=
name|dsf1
operator|.
name|driverName
operator|.
name|compareTo
argument_list|(
name|dsf2
operator|.
name|driverName
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dsf1
operator|.
name|driverClass
operator|!=
literal|null
operator|||
name|dsf2
operator|.
name|driverClass
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dsf1
operator|.
name|driverClass
operator|==
literal|null
condition|)
block|{
name|r3
operator|=
operator|-
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dsf2
operator|.
name|driverClass
operator|==
literal|null
condition|)
block|{
name|r3
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
name|r3
operator|=
name|dsf1
operator|.
name|driverClass
operator|.
name|compareTo
argument_list|(
name|dsf2
operator|.
name|driverClass
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r1
operator|==
literal|0
condition|?
operator|(
name|r2
operator|==
literal|0
condition|?
name|r3
else|:
name|r2
operator|)
else|:
name|r1
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|DataSourceFactoryInfo
block|{
specifier|public
name|String
name|driverName
decl_stmt|;
specifier|public
name|String
name|driverClass
decl_stmt|;
specifier|public
name|String
name|driverVersion
decl_stmt|;
specifier|public
name|String
name|bundle
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|DataSourceFactoryInfo
name|that
init|=
operator|(
name|DataSourceFactoryInfo
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|driverName
argument_list|,
name|that
operator|.
name|driverName
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|driverClass
argument_list|,
name|that
operator|.
name|driverClass
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|driverVersion
argument_list|,
name|that
operator|.
name|driverVersion
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|bundle
argument_list|,
name|that
operator|.
name|bundle
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|driverName
argument_list|,
name|driverClass
argument_list|,
name|driverVersion
argument_list|,
name|bundle
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

