begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|help
operator|.
name|impl
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
name|Arrays
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
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|HelpProvider
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
name|InterpolationHelper
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
name|InvalidSyntaxException
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

begin_class
specifier|public
class|class
name|HelpSystem
implements|implements
name|HelpProvider
block|{
specifier|private
name|BundleContext
name|context
decl_stmt|;
specifier|public
name|HelpSystem
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|synchronized
name|List
argument_list|<
name|HelpProvider
argument_list|>
name|getProviders
parameter_list|()
block|{
name|ServiceReference
argument_list|<
name|HelpProvider
argument_list|>
index|[]
name|refs
init|=
literal|null
decl_stmt|;
try|try
block|{
name|refs
operator|=
name|context
operator|.
name|getServiceReferences
argument_list|(
name|HelpProvider
operator|.
name|class
argument_list|,
literal|null
argument_list|)
operator|.
name|toArray
argument_list|(
operator|new
name|ServiceReference
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|Arrays
operator|.
name|sort
argument_list|(
name|refs
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|HelpProvider
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<
name|HelpProvider
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|refs
operator|.
name|length
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|context
operator|.
name|getService
argument_list|(
name|refs
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|providers
return|;
block|}
specifier|public
name|String
name|getHelp
parameter_list|(
specifier|final
name|CommandSession
name|session
parameter_list|,
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|path
operator|=
literal|"%root%"
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
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
name|props
operator|.
name|put
argument_list|(
literal|"data"
argument_list|,
literal|"${"
operator|+
name|path
operator|+
literal|"}"
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|HelpProvider
argument_list|>
name|providers
init|=
name|getProviders
argument_list|()
decl_stmt|;
name|InterpolationHelper
operator|.
name|performSubstitution
argument_list|(
name|props
argument_list|,
operator|new
name|InterpolationHelper
operator|.
name|SubstitutionCallback
argument_list|()
block|{
specifier|public
name|String
name|getValue
parameter_list|(
specifier|final
name|String
name|key
parameter_list|)
block|{
for|for
control|(
name|HelpProvider
name|hp
range|:
name|providers
control|)
block|{
name|String
name|result
init|=
name|hp
operator|.
name|getHelp
argument_list|(
name|session
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
return|return
name|removeNewLine
argument_list|(
name|result
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|props
operator|.
name|get
argument_list|(
literal|"data"
argument_list|)
return|;
block|}
specifier|private
name|String
name|removeNewLine
parameter_list|(
name|String
name|help
parameter_list|)
block|{
if|if
condition|(
name|help
operator|!=
literal|null
operator|&&
name|help
operator|.
name|endsWith
argument_list|(
literal|"\n"
argument_list|)
condition|)
block|{
name|help
operator|=
name|help
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|help
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|help
return|;
block|}
block|}
end_class

end_unit

