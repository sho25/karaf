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
name|extension
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
import|;
end_import

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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|namespace
operator|.
name|PackageNamespace
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
name|BundleRequirement
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
name|BundleWire
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Namespace
import|;
end_import

begin_class
specifier|public
class|class
name|BundleWiresTest
block|{
specifier|private
specifier|static
specifier|final
name|Path
name|BASE_PATH
init|=
operator|new
name|File
argument_list|(
literal|"target/bundles"
argument_list|)
operator|.
name|toPath
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|targetBundleVersion
init|=
literal|"1.0.1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|targetBundleId
init|=
literal|2
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|packageFilter
init|=
literal|"(&(osgi.wiring.package=org.osgi.framework)(version>=1.6.0)(!(version>=2.0.0)))"
decl_stmt|;
specifier|private
name|IMocksControl
name|c
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|init
parameter_list|()
block|{
name|c
operator|=
name|EasyMock
operator|.
name|createControl
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromBundle
parameter_list|()
throws|throws
name|IOException
block|{
name|BundleWire
name|wire
init|=
name|packageWire
argument_list|(
name|packageFilter
argument_list|,
name|bundleCap
argument_list|(
name|targetBundleId
argument_list|,
name|targetBundleVersion
argument_list|)
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|wiredBundle
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|wire
argument_list|)
argument_list|)
decl_stmt|;
name|c
operator|.
name|replay
argument_list|()
expr_stmt|;
name|BundleWires
name|bwires
init|=
operator|new
name|BundleWires
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|bwires
operator|.
name|save
argument_list|(
name|BASE_PATH
argument_list|)
expr_stmt|;
name|c
operator|.
name|verify
argument_list|()
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|Files
operator|.
name|lines
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/bundles/1"
argument_list|)
operator|.
name|toPath
argument_list|()
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|PackageNamespace
operator|.
name|PACKAGE_NAMESPACE
operator|+
literal|"; "
operator|+
name|packageFilter
argument_list|,
name|lines
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|targetBundleId
operator|+
literal|"; version="
operator|+
name|targetBundleVersion
argument_list|,
name|lines
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|bwires
operator|.
name|delete
argument_list|(
name|BASE_PATH
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromFile
parameter_list|()
throws|throws
name|IOException
block|{
name|BundleWires
name|wires
init|=
name|readFromFile
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|wires
operator|.
name|wiring
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|wire
init|=
name|wires
operator|.
name|wiring
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PackageNamespace
operator|.
name|PACKAGE_NAMESPACE
operator|+
literal|"; "
operator|+
name|packageFilter
argument_list|,
name|wire
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|targetBundleId
operator|+
literal|"; version="
operator|+
name|targetBundleVersion
argument_list|,
name|wire
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterMatches
parameter_list|()
throws|throws
name|IOException
block|{
name|BundleWires
name|wires
init|=
name|readFromFile
argument_list|()
decl_stmt|;
name|BundleRequirement
name|req
init|=
name|packageRequirement
argument_list|(
name|packageFilter
argument_list|)
decl_stmt|;
name|BundleCapability
name|candidate1
init|=
name|bundleCap
argument_list|(
name|targetBundleId
argument_list|,
name|targetBundleVersion
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BundleCapability
argument_list|>
name|candidates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|candidates
operator|.
name|add
argument_list|(
name|candidate1
argument_list|)
expr_stmt|;
name|BundleCapability
name|matchingCandidate
init|=
name|bundleCap
argument_list|(
name|targetBundleId
argument_list|,
literal|"1.1.0"
argument_list|)
decl_stmt|;
name|candidates
operator|.
name|add
argument_list|(
name|matchingCandidate
argument_list|)
expr_stmt|;
name|c
operator|.
name|replay
argument_list|()
expr_stmt|;
name|wires
operator|.
name|filterMatches
argument_list|(
name|req
argument_list|,
name|candidates
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|candidates
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|candidate1
argument_list|,
name|candidates
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|BundleWires
name|readFromFile
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|wiringsFile
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/wirings/1"
argument_list|)
decl_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|wiringsFile
argument_list|)
argument_list|)
decl_stmt|;
name|BundleWires
name|wires
init|=
operator|new
name|BundleWires
argument_list|(
literal|1
argument_list|,
name|reader
argument_list|)
decl_stmt|;
return|return
name|wires
return|;
block|}
specifier|private
name|BundleWire
name|packageWire
parameter_list|(
name|String
name|packageFilter
parameter_list|,
name|BundleCapability
name|bundleRef
parameter_list|)
block|{
name|BundleWire
name|wire
init|=
name|c
operator|.
name|createMock
argument_list|(
name|BundleWire
operator|.
name|class
argument_list|)
decl_stmt|;
name|BundleRequirement
name|req
init|=
name|packageRequirement
argument_list|(
name|packageFilter
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|wire
operator|.
name|getRequirement
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|wire
operator|.
name|getCapability
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundleRef
argument_list|)
expr_stmt|;
return|return
name|wire
return|;
block|}
specifier|private
name|BundleRequirement
name|packageRequirement
parameter_list|(
name|String
name|packageFilter
parameter_list|)
block|{
name|BundleRequirement
name|req
init|=
name|c
operator|.
name|createMock
argument_list|(
name|BundleRequirement
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|directives
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|directives
operator|.
name|put
argument_list|(
name|Namespace
operator|.
name|REQUIREMENT_FILTER_DIRECTIVE
argument_list|,
name|packageFilter
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|req
operator|.
name|getDirectives
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|directives
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|req
operator|.
name|getNamespace
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|PackageNamespace
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
expr_stmt|;
return|return
name|req
return|;
block|}
specifier|private
name|BundleCapability
name|bundleCap
parameter_list|(
name|long
name|bundleId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|BundleRevision
name|rev
init|=
name|c
operator|.
name|createMock
argument_list|(
name|BundleRevision
operator|.
name|class
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|c
operator|.
name|createMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundleId
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|rev
operator|.
name|getBundle
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|BundleCapability
name|cap
init|=
name|c
operator|.
name|createMock
argument_list|(
name|BundleCapability
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|cap
operator|.
name|getRevision
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|rev
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attrs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|VERSION_ATTRIBUTE
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|cap
operator|.
name|getAttributes
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|attrs
argument_list|)
expr_stmt|;
return|return
name|cap
return|;
block|}
specifier|private
name|Bundle
name|wiredBundle
parameter_list|(
name|List
argument_list|<
name|BundleWire
argument_list|>
name|wires
parameter_list|)
block|{
name|Bundle
name|bundle
init|=
name|c
operator|.
name|createMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|1l
argument_list|)
expr_stmt|;
name|BundleWiring
name|wiring
init|=
name|c
operator|.
name|createMock
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|wiring
operator|.
name|getRequiredWires
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|wires
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|wiring
argument_list|)
expr_stmt|;
return|return
name|bundle
return|;
block|}
block|}
end_class

end_unit

