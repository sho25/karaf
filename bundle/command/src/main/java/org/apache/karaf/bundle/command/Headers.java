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
name|bundle
operator|.
name|command
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|jline
operator|.
name|Terminal
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
name|utils
operator|.
name|manifest
operator|.
name|Attribute
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
name|utils
operator|.
name|manifest
operator|.
name|Clause
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
name|utils
operator|.
name|manifest
operator|.
name|Directive
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
name|utils
operator|.
name|manifest
operator|.
name|Parser
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
name|utils
operator|.
name|version
operator|.
name|VersionRange
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
name|utils
operator|.
name|version
operator|.
name|VersionTable
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
name|karaf
operator|.
name|shell
operator|.
name|inject
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
name|util
operator|.
name|ShellUtil
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
name|wiring
operator|.
name|BundleCapability
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
name|wiring
operator|.
name|BundleRevision
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
name|wiring
operator|.
name|BundleWiring
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"bundle"
argument_list|,
name|name
operator|=
literal|"headers"
argument_list|,
name|description
operator|=
literal|"Displays OSGi headers of a given bundles."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|Headers
extends|extends
name|BundlesCommand
block|{
specifier|protected
specifier|final
specifier|static
name|String
name|BUNDLE_PREFIX
init|=
literal|"Bundle-"
decl_stmt|;
specifier|protected
specifier|final
specifier|static
name|String
name|PACKAGE_SUFFFIX
init|=
literal|"-Package"
decl_stmt|;
specifier|protected
specifier|final
specifier|static
name|String
name|SERVICE_SUFFIX
init|=
literal|"-Service"
decl_stmt|;
specifier|protected
specifier|final
specifier|static
name|String
name|CAPABILITY_SUFFIX
init|=
literal|"-Capability"
decl_stmt|;
specifier|protected
specifier|final
specifier|static
name|String
name|IMPORT_PACKAGES_ATTRIB
init|=
literal|"Import-Package"
decl_stmt|;
specifier|protected
specifier|final
specifier|static
name|String
name|REQUIRE_BUNDLE_ATTRIB
init|=
literal|"Require-Bundle"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--indent"
argument_list|,
name|description
operator|=
literal|"Indentation method"
argument_list|)
name|int
name|indent
init|=
operator|-
literal|1
decl_stmt|;
specifier|public
name|Headers
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doExecute
parameter_list|(
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|printHeaders
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
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
name|ShellUtil
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
name|ShellUtil
operator|.
name|getUnderlineString
argument_list|(
name|title
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|indent
operator|==
literal|0
condition|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dict
init|=
name|bundle
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|String
argument_list|>
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|k
operator|+
literal|" = "
operator|+
name|ShellUtil
operator|.
name|getValueString
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|generateFormattedOutput
argument_list|(
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|generateFormattedOutput
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|StringBuilder
name|output
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|otherAttribs
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bundleAttribs
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|serviceAttribs
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|packagesAttribs
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dict
init|=
name|bundle
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|dict
operator|.
name|keys
argument_list|()
decl_stmt|;
comment|// do an initial loop and separate the attributes in different groups
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
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
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
name|BUNDLE_PREFIX
argument_list|)
condition|)
block|{
comment|// starts with Bundle-xxx
name|bundleAttribs
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|endsWith
argument_list|(
name|SERVICE_SUFFIX
argument_list|)
operator|||
name|k
operator|.
name|endsWith
argument_list|(
name|CAPABILITY_SUFFIX
argument_list|)
condition|)
block|{
comment|// ends with xxx-Service
name|serviceAttribs
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|endsWith
argument_list|(
name|PACKAGE_SUFFFIX
argument_list|)
condition|)
block|{
comment|// ends with xxx-Package
name|packagesAttribs
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|endsWith
argument_list|(
name|REQUIRE_BUNDLE_ATTRIB
argument_list|)
condition|)
block|{
comment|// require bundle statement
name|packagesAttribs
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the remaining attribs
name|otherAttribs
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
comment|// we will display the formatted result like this:
comment|// Bundle-Name (ID)
comment|// -----------------------
comment|// all other attributes
comment|//
comment|// all Bundle attributes
comment|//
comment|// all Service attributes
comment|//
comment|// all Package attributes
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|it
init|=
name|otherAttribs
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|e
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|output
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s = %s\n"
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|ShellUtil
operator|.
name|getValueString
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|otherAttribs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|output
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|it
operator|=
name|bundleAttribs
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|e
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|output
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s = %s\n"
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|ShellUtil
operator|.
name|getValueString
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bundleAttribs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|output
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|it
operator|=
name|serviceAttribs
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|e
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|output
operator|.
name|append
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|.
name|append
argument_list|(
literal|" = \n"
argument_list|)
expr_stmt|;
name|formatHeader
argument_list|(
name|ShellUtil
operator|.
name|getValueString
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
name|output
argument_list|,
name|indent
argument_list|)
expr_stmt|;
name|output
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serviceAttribs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|output
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|ClauseFormatter
argument_list|>
name|formatters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ClauseFormatter
argument_list|>
argument_list|()
decl_stmt|;
name|formatters
operator|.
name|put
argument_list|(
name|REQUIRE_BUNDLE_ATTRIB
argument_list|,
operator|new
name|ClauseFormatter
argument_list|()
block|{
specifier|public
name|void
name|pre
parameter_list|(
name|Clause
name|clause
parameter_list|,
name|StringBuilder
name|output
parameter_list|)
block|{
name|boolean
name|isSatisfied
init|=
name|checkBundle
argument_list|(
name|clause
operator|.
name|getName
argument_list|()
argument_list|,
name|clause
operator|.
name|getAttribute
argument_list|(
literal|"bundle-version"
argument_list|)
argument_list|)
decl_stmt|;
name|Ansi
operator|.
name|ansi
argument_list|(
name|output
argument_list|)
operator|.
name|fg
argument_list|(
name|isSatisfied
condition|?
name|Ansi
operator|.
name|Color
operator|.
name|DEFAULT
else|:
name|Ansi
operator|.
name|Color
operator|.
name|RED
argument_list|)
operator|.
name|a
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|post
parameter_list|(
name|Clause
name|clause
parameter_list|,
name|StringBuilder
name|output
parameter_list|)
block|{
name|Ansi
operator|.
name|ansi
argument_list|(
name|output
argument_list|)
operator|.
name|reset
argument_list|()
operator|.
name|a
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|formatters
operator|.
name|put
argument_list|(
name|IMPORT_PACKAGES_ATTRIB
argument_list|,
operator|new
name|ClauseFormatter
argument_list|()
block|{
specifier|public
name|void
name|pre
parameter_list|(
name|Clause
name|clause
parameter_list|,
name|StringBuilder
name|output
parameter_list|)
block|{
name|boolean
name|isSatisfied
init|=
name|checkPackage
argument_list|(
name|clause
operator|.
name|getName
argument_list|()
argument_list|,
name|clause
operator|.
name|getAttribute
argument_list|(
literal|"version"
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|isOptional
init|=
literal|"optional"
operator|.
name|equals
argument_list|(
name|clause
operator|.
name|getDirective
argument_list|(
literal|"resolution"
argument_list|)
argument_list|)
decl_stmt|;
name|Ansi
operator|.
name|ansi
argument_list|(
name|output
argument_list|)
operator|.
name|fg
argument_list|(
name|isSatisfied
condition|?
name|Ansi
operator|.
name|Color
operator|.
name|DEFAULT
else|:
name|Ansi
operator|.
name|Color
operator|.
name|RED
argument_list|)
operator|.
name|a
argument_list|(
name|isSatisfied
operator|||
name|isOptional
condition|?
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD_OFF
else|:
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|post
parameter_list|(
name|Clause
name|clause
parameter_list|,
name|StringBuilder
name|output
parameter_list|)
block|{
name|Ansi
operator|.
name|ansi
argument_list|(
name|output
argument_list|)
operator|.
name|reset
argument_list|()
operator|.
name|a
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|it
operator|=
name|packagesAttribs
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|e
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|output
operator|.
name|append
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|.
name|append
argument_list|(
literal|" = \n"
argument_list|)
expr_stmt|;
name|formatHeader
argument_list|(
name|ShellUtil
operator|.
name|getValueString
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|,
name|formatters
operator|.
name|get
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|output
argument_list|,
name|indent
argument_list|)
expr_stmt|;
name|output
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|packagesAttribs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|output
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
return|return
name|output
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
interface|interface
name|ClauseFormatter
block|{
name|void
name|pre
parameter_list|(
name|Clause
name|clause
parameter_list|,
name|StringBuilder
name|output
parameter_list|)
function_decl|;
name|void
name|post
parameter_list|(
name|Clause
name|clause
parameter_list|,
name|StringBuilder
name|output
parameter_list|)
function_decl|;
block|}
specifier|protected
name|void
name|formatHeader
parameter_list|(
name|String
name|header
parameter_list|,
name|ClauseFormatter
name|formatter
parameter_list|,
name|StringBuilder
name|builder
parameter_list|,
name|int
name|indent
parameter_list|)
block|{
name|Clause
index|[]
name|clauses
init|=
name|Parser
operator|.
name|parseHeader
argument_list|(
name|header
argument_list|)
decl_stmt|;
name|formatClauses
argument_list|(
name|clauses
argument_list|,
name|formatter
argument_list|,
name|builder
argument_list|,
name|indent
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|formatClauses
parameter_list|(
name|Clause
index|[]
name|clauses
parameter_list|,
name|ClauseFormatter
name|formatter
parameter_list|,
name|StringBuilder
name|builder
parameter_list|,
name|int
name|indent
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Clause
name|clause
range|:
name|clauses
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
literal|",\n"
argument_list|)
expr_stmt|;
block|}
name|formatClause
argument_list|(
name|clause
argument_list|,
name|formatter
argument_list|,
name|builder
argument_list|,
name|indent
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|formatClause
parameter_list|(
name|Clause
name|clause
parameter_list|,
name|ClauseFormatter
name|formatter
parameter_list|,
name|StringBuilder
name|builder
parameter_list|,
name|int
name|indent
parameter_list|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
if|if
condition|(
name|formatter
operator|!=
literal|null
condition|)
block|{
name|formatter
operator|.
name|pre
argument_list|(
name|clause
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
name|formatClause
argument_list|(
name|clause
argument_list|,
name|builder
argument_list|,
name|indent
argument_list|)
expr_stmt|;
if|if
condition|(
name|formatter
operator|!=
literal|null
condition|)
block|{
name|formatter
operator|.
name|post
argument_list|(
name|clause
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|int
name|getTermWidth
parameter_list|()
block|{
name|Terminal
name|term
init|=
operator|(
name|Terminal
operator|)
name|session
operator|.
name|get
argument_list|(
literal|".jline.terminal"
argument_list|)
decl_stmt|;
return|return
name|term
operator|!=
literal|null
condition|?
name|term
operator|.
name|getWidth
argument_list|()
else|:
literal|80
return|;
block|}
specifier|protected
name|void
name|formatClause
parameter_list|(
name|Clause
name|clause
parameter_list|,
name|StringBuilder
name|builder
parameter_list|,
name|int
name|indent
parameter_list|)
block|{
if|if
condition|(
name|indent
operator|<
literal|0
condition|)
block|{
if|if
condition|(
name|clause
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
operator|<
name|getTermWidth
argument_list|()
operator|-
literal|8
condition|)
block|{
comment|// -8 for tabs
name|indent
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
name|indent
operator|=
literal|3
expr_stmt|;
block|}
block|}
name|String
name|name
init|=
name|clause
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Directive
index|[]
name|directives
init|=
name|clause
operator|.
name|getDirectives
argument_list|()
decl_stmt|;
name|Attribute
index|[]
name|attributes
init|=
name|clause
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|directives
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Directive
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Directive
name|o1
parameter_list|,
name|Directive
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|attributes
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Attribute
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Attribute
name|o1
parameter_list|,
name|Attribute
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|directives
operator|!=
literal|null
operator|&&
name|i
operator|<
name|directives
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
if|if
condition|(
name|indent
operator|>
literal|1
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"\n\t\t"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|directives
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":="
argument_list|)
expr_stmt|;
name|String
name|v
init|=
name|directives
index|[
name|i
index|]
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
condition|)
block|{
if|if
condition|(
name|indent
operator|>
literal|2
operator|&&
name|v
operator|.
name|length
argument_list|()
operator|>
literal|20
condition|)
block|{
name|v
operator|=
name|v
operator|.
name|replace
argument_list|(
literal|","
argument_list|,
literal|",\n\t\t\t"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
name|v
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|attributes
operator|!=
literal|null
operator|&&
name|i
operator|<
name|attributes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
if|if
condition|(
name|indent
operator|>
literal|1
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"\n\t\t"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|attributes
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
name|String
name|v
init|=
name|attributes
index|[
name|i
index|]
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
condition|)
block|{
if|if
condition|(
name|indent
operator|>
literal|2
operator|&&
name|v
operator|.
name|length
argument_list|()
operator|>
literal|20
condition|)
block|{
name|v
operator|=
name|v
operator|.
name|replace
argument_list|(
literal|","
argument_list|,
literal|",\n\t\t\t"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
name|v
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|checkBundle
parameter_list|(
name|String
name|bundleName
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|VersionRange
name|vr
init|=
name|VersionRange
operator|.
name|parseVersionRange
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
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
operator|(
name|bundles
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|i
operator|<
name|bundles
operator|.
name|length
operator|)
condition|;
name|i
operator|++
control|)
block|{
name|String
name|sym
init|=
name|bundles
index|[
name|i
index|]
operator|.
name|getSymbolicName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|sym
operator|!=
literal|null
operator|)
operator|&&
name|sym
operator|.
name|equals
argument_list|(
name|bundleName
argument_list|)
condition|)
block|{
if|if
condition|(
name|vr
operator|.
name|contains
argument_list|(
name|bundles
index|[
name|i
index|]
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|checkPackage
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|VersionRange
name|range
init|=
name|VersionRange
operator|.
name|parseVersionRange
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
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
operator|(
name|bundles
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|i
operator|<
name|bundles
operator|.
name|length
operator|)
condition|;
name|i
operator|++
control|)
block|{
name|BundleWiring
name|wiring
init|=
name|bundles
index|[
name|i
index|]
operator|.
name|adapt
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BundleCapability
argument_list|>
name|caps
init|=
name|wiring
operator|!=
literal|null
condition|?
name|wiring
operator|.
name|getCapabilities
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|caps
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BundleCapability
name|cap
range|:
name|caps
control|)
block|{
name|String
name|n
init|=
name|getAttribute
argument_list|(
name|cap
argument_list|,
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
name|String
name|v
init|=
name|getAttribute
argument_list|(
name|cap
argument_list|,
name|Constants
operator|.
name|VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
if|if
condition|(
name|packageName
operator|.
name|equals
argument_list|(
name|n
argument_list|)
operator|&&
name|range
operator|.
name|contains
argument_list|(
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|v
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|String
name|getAttribute
parameter_list|(
name|BundleCapability
name|cap
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Object
name|obj
init|=
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|obj
operator|!=
literal|null
condition|?
name|obj
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

