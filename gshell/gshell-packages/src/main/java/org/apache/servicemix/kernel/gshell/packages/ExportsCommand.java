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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|packages
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
name|clp
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
name|geronimo
operator|.
name|gshell
operator|.
name|clp
operator|.
name|Option
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|packageadmin
operator|.
name|ExportedPackage
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
name|packageadmin
operator|.
name|PackageAdmin
import|;
end_import

begin_class
specifier|public
class|class
name|ExportsCommand
extends|extends
name|PackageCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-i"
argument_list|,
name|aliases
operator|=
block|{
literal|"--imports"
block|}
argument_list|,
name|description
operator|=
literal|"List bundles importing the packages"
argument_list|)
name|boolean
name|imports
decl_stmt|;
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
literal|"bundle ids"
argument_list|)
name|List
argument_list|<
name|Long
argument_list|>
name|ids
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|PackageAdmin
name|admin
parameter_list|)
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
name|printExports
argument_list|(
name|io
operator|.
name|out
argument_list|,
name|bundle
argument_list|,
name|admin
operator|.
name|getExportedPackages
argument_list|(
name|bundle
argument_list|)
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
name|printExports
argument_list|(
name|io
operator|.
name|out
argument_list|,
literal|null
argument_list|,
name|admin
operator|.
name|getExportedPackages
argument_list|(
operator|(
name|Bundle
operator|)
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|printExports
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|Bundle
name|target
parameter_list|,
name|ExportedPackage
index|[]
name|exports
parameter_list|)
block|{
if|if
condition|(
operator|(
name|exports
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|exports
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|exports
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Bundle
name|bundle
init|=
name|exports
index|[
name|i
index|]
operator|.
name|getExportingBundle
argument_list|()
decl_stmt|;
name|out
operator|.
name|print
argument_list|(
name|getBundleName
argument_list|(
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|": "
operator|+
name|exports
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|imports
condition|)
block|{
name|Bundle
index|[]
name|bs
init|=
name|exports
index|[
name|i
index|]
operator|.
name|getImportingBundles
argument_list|()
decl_stmt|;
if|if
condition|(
name|bs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Bundle
name|b
range|:
name|bs
control|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\t"
operator|+
name|getBundleName
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
name|getBundleName
argument_list|(
name|target
argument_list|)
operator|+
literal|": No active exported packages."
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
end_class

end_unit

