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
operator|.
name|model
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
name|io
operator|.
name|StringReader
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
name|Collections
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
name|Properties
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
name|Matcher
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|BundleInfo
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
name|ConfigFileInfo
import|;
end_import

begin_class
annotation|@
name|XmlTransient
specifier|public
class|class
name|Content
block|{
specifier|protected
name|List
argument_list|<
name|Config
argument_list|>
name|config
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ConfigFile
argument_list|>
name|configfile
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Dependency
argument_list|>
name|feature
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundle
decl_stmt|;
comment|/**      * Gets the value of the config property.      *<p/>      *<p/>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the config property.      *<p/>      *<p/>      * For example, to add a new item, do as follows:      *<pre>      *    getConfig().add(newItem);      *</pre>      *<p/>      *<p/>      *<p/>      * Objects of the following type(s) are allowed in the list      * {@link Config }      */
specifier|public
name|List
argument_list|<
name|Config
argument_list|>
name|getConfig
parameter_list|()
block|{
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
operator|new
name|ArrayList
argument_list|<
name|Config
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|config
return|;
block|}
comment|/**      * Gets the value of the configfile property.      *<p/>      *<p/>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the configfile property.      *<p/>      *<p/>      * For example, to add a new item, do as follows:      *<pre>      *    getConfigfile().add(newItem);      *</pre>      *<p/>      *<p/>      *<p/>      * Objects of the following type(s) are allowed in the list      * {@link ConfigFile }      */
specifier|public
name|List
argument_list|<
name|ConfigFile
argument_list|>
name|getConfigfile
parameter_list|()
block|{
if|if
condition|(
name|configfile
operator|==
literal|null
condition|)
block|{
name|configfile
operator|=
operator|new
name|ArrayList
argument_list|<
name|ConfigFile
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|configfile
return|;
block|}
comment|/**      * Gets the value of the feature property.      *<p/>      *<p/>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the feature property.      *<p/>      *<p/>      * For example, to add a new item, do as follows:      *<pre>      *    getFeature().add(newItem);      *</pre>      *<p/>      *<p/>      *<p/>      * Objects of the following type(s) are allowed in the list      * {@link Dependency }      */
specifier|public
name|List
argument_list|<
name|Dependency
argument_list|>
name|getFeature
parameter_list|()
block|{
if|if
condition|(
name|feature
operator|==
literal|null
condition|)
block|{
name|feature
operator|=
operator|new
name|ArrayList
argument_list|<
name|Dependency
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|feature
return|;
block|}
comment|/**      * Gets the value of the bundle property.      *<p/>      *<p/>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the bundle property.      *<p/>      *<p/>      * For example, to add a new item, do as follows:      *<pre>      *    getBundle().add(newItem);      *</pre>      *<p/>      *<p/>      *<p/>      * Objects of the following type(s) are allowed in the list      * {@link Bundle }      */
specifier|public
name|List
argument_list|<
name|Bundle
argument_list|>
name|getBundle
parameter_list|()
block|{
if|if
condition|(
name|bundle
operator|==
literal|null
condition|)
block|{
name|bundle
operator|=
operator|new
name|ArrayList
argument_list|<
name|Bundle
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|bundle
return|;
block|}
specifier|public
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Dependency
argument_list|>
name|getDependencies
parameter_list|()
block|{
return|return
name|Collections
operator|.
expr|<
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Dependency
operator|>
name|unmodifiableList
argument_list|(
name|getFeature
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|getBundles
parameter_list|()
block|{
return|return
name|Collections
operator|.
expr|<
name|BundleInfo
operator|>
name|unmodifiableList
argument_list|(
name|getBundle
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getConfigurations
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Config
name|config
range|:
name|getConfig
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|config
operator|.
name|getName
argument_list|()
decl_stmt|;
name|StringReader
name|propStream
init|=
operator|new
name|StringReader
argument_list|(
name|config
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|props
operator|.
name|load
argument_list|(
name|propStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore??
block|}
name|interpolation
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|propMap
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
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|props
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|propMap
operator|.
name|put
argument_list|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|propMap
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|List
argument_list|<
name|ConfigFileInfo
argument_list|>
name|getConfigurationFiles
parameter_list|()
block|{
return|return
name|Collections
operator|.
expr|<
name|ConfigFileInfo
operator|>
name|unmodifiableList
argument_list|(
name|getConfigfile
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|protected
name|void
name|interpolation
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
for|for
control|(
name|Enumeration
name|e
init|=
name|properties
operator|.
name|propertyNames
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|val
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\$\\{([^}]+)\\}"
argument_list|)
operator|.
name|matcher
argument_list|(
name|val
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|rep
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rep
operator|!=
literal|null
condition|)
block|{
name|val
operator|=
name|val
operator|.
name|replace
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|0
argument_list|)
argument_list|,
name|rep
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|reset
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
