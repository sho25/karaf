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
name|packages
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|Option
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
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"package"
argument_list|,
name|name
operator|=
literal|"exports"
argument_list|,
name|description
operator|=
literal|"Displays exported packages."
argument_list|)
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
literal|"List bundles importing the specified packages"
argument_list|)
name|boolean
name|imports
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-s"
argument_list|,
name|description
operator|=
literal|"Shows the symbolic name"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|showSymbolic
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-d"
argument_list|,
name|aliases
operator|=
block|{
literal|"--details"
block|}
argument_list|,
name|description
operator|=
literal|"List bundles in a master detail table"
argument_list|)
name|boolean
name|details
decl_stmt|;
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
literal|"The IDs of bundles to check"
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
name|void
name|doExecute
parameter_list|(
name|PackageAdmin
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|format
init|=
literal|""
decl_stmt|;
name|int
name|index
init|=
literal|1
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|add
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|format
operator|+=
literal|"%"
operator|+
operator|(
name|index
operator|++
operator|)
operator|+
literal|"$6s"
expr_stmt|;
if|if
condition|(
name|showSymbolic
condition|)
block|{
name|headers
operator|.
name|add
argument_list|(
literal|"Symbolic name"
argument_list|)
expr_stmt|;
name|format
operator|+=
literal|" %"
operator|+
operator|(
name|index
operator|++
operator|)
operator|+
literal|"$-40s "
expr_stmt|;
block|}
name|headers
operator|.
name|add
argument_list|(
literal|"Packages"
argument_list|)
expr_stmt|;
name|format
operator|+=
literal|" %"
operator|+
operator|(
name|index
operator|++
operator|)
operator|+
literal|"$-40s"
expr_stmt|;
if|if
condition|(
name|imports
condition|)
block|{
name|headers
operator|.
name|add
argument_list|(
literal|"Imported by"
argument_list|)
expr_stmt|;
name|format
operator|+=
literal|" %"
operator|+
operator|(
name|index
operator|++
operator|)
operator|+
literal|"$-40s"
expr_stmt|;
block|}
name|format
operator|+=
literal|"\n"
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|headers
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
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
name|System
operator|.
name|out
argument_list|,
name|format
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
for|for
control|(
name|Bundle
name|bundle
range|:
name|getBundleContext
argument_list|()
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|printExports
argument_list|(
name|System
operator|.
name|out
argument_list|,
name|format
argument_list|,
name|bundle
argument_list|,
name|admin
operator|.
name|getExportedPackages
argument_list|(
operator|(
name|Bundle
operator|)
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|printExports
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|String
name|format
parameter_list|,
name|Bundle
name|target
parameter_list|,
name|ExportedPackage
index|[]
name|exports
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|columns
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
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
name|columns
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
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
name|columns
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|showSymbolic
condition|)
block|{
name|columns
operator|.
name|add
argument_list|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Do not repeat ID, Symbolic names etc when bundle exports more that one package
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|int
name|size
init|=
name|columns
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|details
condition|)
block|{
name|columns
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|fillDetailRecord
argument_list|(
name|columns
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
block|}
name|columns
operator|.
name|add
argument_list|(
name|exports
index|[
name|i
index|]
operator|.
name|toString
argument_list|()
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
operator|&&
name|bs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|bs
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|columns
operator|.
name|add
argument_list|(
name|getBundleName
argument_list|(
name|bs
index|[
name|j
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|columns
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
comment|//Do not repeat ID, Symbolic names etc when package is imported by more than one bundles
if|if
condition|(
name|details
condition|)
block|{
name|int
name|size
init|=
name|columns
operator|.
name|size
argument_list|()
decl_stmt|;
name|columns
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|fillDetailRecord
argument_list|(
name|columns
argument_list|,
name|size
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|columns
operator|.
name|remove
argument_list|(
name|columns
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|columns
operator|.
name|add
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|out
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|columns
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|out
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|columns
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|columns
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|target
operator|.
name|getBundleId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|showSymbolic
condition|)
block|{
name|columns
operator|.
name|add
argument_list|(
name|target
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|columns
operator|.
name|add
argument_list|(
literal|"No active exported packages."
argument_list|)
expr_stmt|;
if|if
condition|(
name|imports
condition|)
block|{
name|columns
operator|.
name|add
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|columns
operator|.
name|toArray
argument_list|()
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
comment|/**      * Method that creates an empty list of string that serves as detail record.      * @param colums      * @param size      */
specifier|public
name|void
name|fillDetailRecord
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|colums
parameter_list|,
name|int
name|size
parameter_list|)
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|colums
operator|.
name|add
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

