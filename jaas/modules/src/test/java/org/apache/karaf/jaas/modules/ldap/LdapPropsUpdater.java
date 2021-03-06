begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|ldap
package|;
end_package

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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_class
specifier|public
class|class
name|LdapPropsUpdater
block|{
specifier|private
name|LdapPropsUpdater
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|ldapProps
parameter_list|(
name|String
name|propsPath
parameter_list|,
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mapFunction
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
name|Path
name|inPath
init|=
operator|new
name|File
argument_list|(
name|basedir
operator|+
literal|"/src/test/resources/"
operator|+
name|propsPath
argument_list|)
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|Files
operator|.
name|lines
argument_list|(
name|inPath
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|mapFunction
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|Path
name|outPath
init|=
operator|new
name|File
argument_list|(
name|basedir
operator|+
literal|"/target/test-classes/"
operator|+
name|propsPath
argument_list|)
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|outPath
argument_list|,
name|lines
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

