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
name|geronimo
operator|.
name|gshell
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|annotation
operator|.
name|CommandComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|clp
operator|.
name|Argument
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
name|Constants
import|;
end_import

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: gnodet  * Date: Oct 3, 2007  * Time: 12:10:15 PM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
annotation|@
name|CommandComponent
argument_list|(
name|id
operator|=
literal|"osgi:headers"
argument_list|,
name|description
operator|=
literal|"Display headers"
argument_list|)
specifier|public
class|class
name|Headers
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"Bundles ids"
argument_list|)
name|List
argument_list|<
name|Long
argument_list|>
name|ids
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ids
operator|!=
literal|null
operator|&&
operator|!
name|ids
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|long
name|id
range|:
name|ids
control|)
block|{
name|Bundle
name|bundle
init|=
name|getBundleContext
argument_list|()
operator|.
name|getBundle
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|!=
literal|null
condition|)
block|{
name|printHeaders
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|io
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Bundle ID "
operator|+
name|id
operator|+
literal|" is invalid."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Bundle
index|[]
name|bundles
init|=
name|getBundleContext
argument_list|()
operator|.
name|getBundles
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
name|bundles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|printHeaders
argument_list|(
name|bundles
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|protected
name|void
name|printHeaders
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|title
init|=
name|Util
operator|.
name|getBundleName
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n"
operator|+
name|title
argument_list|)
expr_stmt|;
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
name|Util
operator|.
name|getUnderlineString
argument_list|(
name|title
argument_list|)
argument_list|)
expr_stmt|;
name|Dictionary
name|dict
init|=
name|bundle
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|Enumeration
name|keys
init|=
name|dict
operator|.
name|keys
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Object
name|k
init|=
operator|(
name|String
operator|)
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Object
name|v
init|=
name|dict
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
name|k
operator|+
literal|" = "
operator|+
name|Util
operator|.
name|getValueString
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

