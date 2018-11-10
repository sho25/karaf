begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|examples
operator|.
name|http
operator|.
name|resource
operator|.
name|whiteboard
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|annotations
operator|.
name|Component
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
name|service
operator|=
name|ExampleResource
operator|.
name|class
argument_list|,
name|property
operator|=
block|{
literal|"osgi.http.whiteboard.resource.pattern=/example/*"
block|,
literal|"osgi.http.whiteboard.resource.prefix=/resources"
block|}
argument_list|)
specifier|public
class|class
name|ExampleResource
block|{ }
end_class

end_unit

