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
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
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
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|java
operator|.
name|util
operator|.
name|List
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
literal|"info"
argument_list|,
name|description
operator|=
literal|"Displays detailed information of a given bundle"
argument_list|)
specifier|public
class|class
name|Info
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"ids"
argument_list|,
name|description
operator|=
literal|"A list of bundle IDs separated by whitespaces"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
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
name|printInfo
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
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
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|printInfo
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      *<p>      * Get the OSGI-INF/bundle.info entry from the bundle and display it.      *</p>      *      * @param bundle the bundle.      */
specifier|protected
name|void
name|printInfo
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
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
name|System
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
name|System
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
name|g
name|URL
name|bundleInfo
init|=
name|bundle
operator|.
name|getEntry
argument_list|(
literal|"OSGI-INF/bundle.info"
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundleInfo
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|bundleInfo
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
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
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
end_class

end_unit

