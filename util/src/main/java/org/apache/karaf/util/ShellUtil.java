begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|util
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|service
operator|.
name|command
operator|.
name|CommandSession
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
name|startlevel
operator|.
name|StartLevel
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

begin_class
specifier|public
class|class
name|ShellUtil
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ShellUtil
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|String
name|getBundleName
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
if|if
condition|(
name|bundle
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|BUNDLE_NAME
argument_list|)
decl_stmt|;
return|return
operator|(
name|name
operator|==
literal|null
operator|)
condition|?
literal|"Bundle "
operator|+
name|Long
operator|.
name|toString
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
else|:
name|name
operator|+
literal|" ("
operator|+
name|Long
operator|.
name|toString
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|+
literal|")"
return|;
block|}
return|return
literal|"[STALE BUNDLE]"
return|;
block|}
specifier|private
specifier|static
name|StringBuffer
name|m_sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|String
name|getUnderlineString
parameter_list|(
name|String
name|s
parameter_list|)
block|{
synchronized|synchronized
init|(
name|m_sb
init|)
block|{
name|m_sb
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|m_sb
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|s
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|m_sb
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
block|}
return|return
name|m_sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getValueString
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
synchronized|synchronized
init|(
name|m_sb
init|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|obj
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|String
index|[]
condition|)
block|{
name|String
index|[]
name|array
init|=
operator|(
name|String
index|[]
operator|)
name|obj
decl_stmt|;
return|return
name|convertTypedArrayToString
argument_list|(
name|array
argument_list|,
operator|new
name|StringConverter
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|convertObjectToString
parameter_list|(
name|String
name|toConvert
parameter_list|)
block|{
return|return
name|toConvert
return|;
block|}
block|}
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Boolean
condition|)
block|{
return|return
operator|(
operator|(
name|Boolean
operator|)
name|obj
operator|)
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Long
condition|)
block|{
return|return
operator|(
operator|(
name|Long
operator|)
name|obj
operator|)
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Integer
condition|)
block|{
return|return
operator|(
operator|(
name|Integer
operator|)
name|obj
operator|)
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Short
condition|)
block|{
return|return
operator|(
operator|(
name|Short
operator|)
name|obj
operator|)
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Double
condition|)
block|{
return|return
operator|(
operator|(
name|Double
operator|)
name|obj
operator|)
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Float
condition|)
block|{
return|return
operator|(
operator|(
name|Float
operator|)
name|obj
operator|)
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|URL
condition|)
block|{
return|return
operator|(
operator|(
name|URL
operator|)
name|obj
operator|)
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|URL
index|[]
condition|)
block|{
name|URL
index|[]
name|array
init|=
operator|(
name|URL
index|[]
operator|)
name|obj
decl_stmt|;
return|return
name|convertTypedArrayToString
argument_list|(
name|array
argument_list|,
operator|new
name|StringConverter
argument_list|<
name|URL
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|convertObjectToString
parameter_list|(
name|URL
name|toConvert
parameter_list|)
block|{
return|return
name|toConvert
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|URI
condition|)
block|{
try|try
block|{
return|return
operator|(
operator|(
name|URI
operator|)
name|obj
operator|)
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"URI could not be transformed to URL"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|obj
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|URI
index|[]
condition|)
block|{
name|URI
index|[]
name|array
init|=
operator|(
name|URI
index|[]
operator|)
name|obj
decl_stmt|;
return|return
name|convertTypedArrayToString
argument_list|(
name|array
argument_list|,
operator|new
name|StringConverter
argument_list|<
name|URI
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|convertObjectToString
parameter_list|(
name|URI
name|toConvert
parameter_list|)
block|{
try|try
block|{
return|return
name|toConvert
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"URI could not be transformed to URL"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|toConvert
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|"null"
return|;
block|}
else|else
block|{
return|return
name|obj
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
specifier|private
specifier|static
parameter_list|<
name|Type
parameter_list|>
name|String
name|convertTypedArrayToString
parameter_list|(
name|Type
index|[]
name|array
parameter_list|,
name|StringConverter
argument_list|<
name|Type
argument_list|>
name|converter
parameter_list|)
block|{
name|m_sb
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|m_sb
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|!=
literal|0
condition|)
block|{
name|m_sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|m_sb
operator|.
name|append
argument_list|(
name|converter
operator|.
name|convertObjectToString
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|m_sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
interface|interface
name|StringConverter
parameter_list|<
name|Type
parameter_list|>
block|{
name|String
name|convertObjectToString
parameter_list|(
name|Type
name|toConvert
parameter_list|)
function_decl|;
block|}
comment|/**      * Check if a bundle is a system bundle (start level< 50)      *       * @param bundleContext      * @param bundle      * @return true if the bundle has start level minor than 50      */
specifier|public
specifier|static
name|boolean
name|isASystemBundle
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|Bundle
name|bundle
parameter_list|)
block|{
name|ServiceReference
name|ref
init|=
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|StartLevel
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|StartLevel
name|sl
init|=
operator|(
name|StartLevel
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|sl
operator|!=
literal|null
condition|)
block|{
name|int
name|level
init|=
name|sl
operator|.
name|getBundleStartLevel
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|int
name|sbsl
init|=
literal|49
decl_stmt|;
specifier|final
name|String
name|sbslProp
init|=
name|bundleContext
operator|.
name|getProperty
argument_list|(
literal|"karaf.systemBundlesStartLevel"
argument_list|)
decl_stmt|;
if|if
condition|(
name|sbslProp
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|sbsl
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|sbslProp
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
name|level
operator|<=
name|sbsl
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Ask the user to confirm the access to a system bundle      *       * @param bundleId      * @param session      * @return true if the user confirm      * @throws IOException      */
specifier|public
specifier|static
name|boolean
name|accessToSystemBundleIsAllowed
parameter_list|(
name|long
name|bundleId
parameter_list|,
name|CommandSession
name|session
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
init|;
condition|;
control|)
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|"You are about to access system bundle "
operator|+
name|bundleId
operator|+
literal|".  Do you wish to continue (yes/no): "
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|int
name|c
init|=
name|session
operator|.
name|getKeyboard
argument_list|()
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\r'
operator|||
name|c
operator|==
literal|'\n'
condition|)
block|{
break|break;
block|}
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
name|String
name|str
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"yes"
operator|.
name|equals
argument_list|(
name|str
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
literal|"no"
operator|.
name|equals
argument_list|(
name|str
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

