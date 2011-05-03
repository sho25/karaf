begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*   * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|http
package|;
end_package

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
name|javax
operator|.
name|servlet
operator|.
name|Servlet
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
name|gogo
operator|.
name|commands
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
name|console
operator|.
name|OsgiCommandSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|ServletEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|web
operator|.
name|service
operator|.
name|spi
operator|.
name|WebEvent
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"http"
argument_list|,
name|name
operator|=
literal|"list"
argument_list|,
name|description
operator|=
literal|"Lists details for servlets."
argument_list|)
specifier|public
class|class
name|ServletListCommand
extends|extends
name|OsgiCommandSupport
block|{
specifier|private
name|ServletEventHandler
name|eventHandler
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|headers
init|=
literal|" ID   Servlet                        Servlet-Name              State         Alias              Url             "
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|headers
argument_list|)
expr_stmt|;
for|for
control|(
name|ServletEvent
name|event
range|:
name|eventHandler
operator|.
name|getServletEvents
argument_list|()
control|)
block|{
name|Servlet
name|servlet
init|=
name|event
operator|.
name|getServlet
argument_list|()
decl_stmt|;
name|String
name|servletClassName
init|=
literal|" "
decl_stmt|;
if|if
condition|(
name|servlet
operator|!=
literal|null
condition|)
block|{
name|servletClassName
operator|=
name|servlet
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|servletClassName
operator|=
name|servletClassName
operator|.
name|substring
argument_list|(
name|servletClassName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|+
literal|1
argument_list|,
name|servletClassName
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|servletClassName
operator|=
name|CommandUtils
operator|.
name|trimToSize
argument_list|(
name|servletClassName
argument_list|,
literal|28
argument_list|)
expr_stmt|;
name|String
name|servletName
init|=
name|event
operator|.
name|getServletName
argument_list|()
operator|!=
literal|null
condition|?
name|event
operator|.
name|getServletName
argument_list|()
else|:
literal|" "
decl_stmt|;
if|if
condition|(
name|servletName
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|servletName
operator|=
name|servletName
operator|.
name|substring
argument_list|(
name|servletName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|+
literal|1
argument_list|,
name|servletName
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|servletName
operator|=
name|CommandUtils
operator|.
name|trimToSize
argument_list|(
name|servletName
argument_list|,
literal|23
argument_list|)
expr_stmt|;
name|String
name|alias
init|=
name|event
operator|.
name|getAlias
argument_list|()
operator|!=
literal|null
condition|?
name|event
operator|.
name|getAlias
argument_list|()
else|:
literal|" "
decl_stmt|;
name|alias
operator|=
name|CommandUtils
operator|.
name|trimToSize
argument_list|(
name|alias
argument_list|,
literal|16
argument_list|)
expr_stmt|;
name|String
index|[]
name|urls
init|=
operator|(
name|String
index|[]
operator|)
operator|(
name|event
operator|.
name|getUrlParameter
argument_list|()
operator|!=
literal|null
condition|?
name|event
operator|.
name|getUrlParameter
argument_list|()
else|:
operator|new
name|String
index|[]
block|{
literal|""
block|}
operator|)
decl_stmt|;
name|String
name|line
init|=
literal|"["
operator|+
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
operator|+
literal|"] ["
operator|+
name|servletClassName
operator|+
literal|"] ["
operator|+
name|servletName
operator|+
literal|"] ["
operator|+
name|getStateString
argument_list|(
name|event
operator|.
name|getType
argument_list|()
argument_list|)
operator|+
literal|"] ["
operator|+
name|alias
operator|+
literal|"] ["
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|urls
argument_list|)
operator|+
literal|"]"
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getStateString
parameter_list|(
name|int
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|WebEvent
operator|.
name|DEPLOYING
case|:
return|return
literal|"Deploying  "
return|;
case|case
name|WebEvent
operator|.
name|DEPLOYED
case|:
return|return
literal|"Deployed   "
return|;
case|case
name|WebEvent
operator|.
name|UNDEPLOYING
case|:
return|return
literal|"Undeploying"
return|;
case|case
name|WebEvent
operator|.
name|UNDEPLOYED
case|:
return|return
literal|"Undeployed "
return|;
case|case
name|WebEvent
operator|.
name|FAILED
case|:
return|return
literal|"Failed     "
return|;
case|case
name|WebEvent
operator|.
name|WAITING
case|:
return|return
literal|"Waiting    "
return|;
default|default:
return|return
literal|"Failed     "
return|;
block|}
block|}
comment|/** 	 * @return the eventHandler 	 */
specifier|public
name|ServletEventHandler
name|getEventHandler
parameter_list|()
block|{
return|return
name|eventHandler
return|;
block|}
comment|/** 	 * @param eventHandler the eventHandler to set 	 */
specifier|public
name|void
name|setEventHandler
parameter_list|(
name|ServletEventHandler
name|eventHandler
parameter_list|)
block|{
name|this
operator|.
name|eventHandler
operator|=
name|eventHandler
expr_stmt|;
block|}
block|}
end_class

end_unit

