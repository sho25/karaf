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
name|shell
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
name|InputStream
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
name|gogo
operator|.
name|commands
operator|.
name|Argument
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
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"osgi"
argument_list|,
name|name
operator|=
literal|"update"
argument_list|,
name|description
operator|=
literal|"Update bundle."
argument_list|)
specifier|public
class|class
name|UpdateBundle
extends|extends
name|BundleCommand
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"location"
argument_list|,
name|description
operator|=
literal|"The bundles update location"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|location
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|location
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|is
init|=
operator|new
name|URL
argument_list|(
name|location
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|update
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bundle
operator|.
name|update
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

