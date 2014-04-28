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
name|internal
operator|.
name|service
package|;
end_package

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
name|Version
import|;
end_import

begin_class
specifier|public
class|class
name|BundleComparator
implements|implements
name|Comparator
argument_list|<
name|Bundle
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Bundle
name|o1
parameter_list|,
name|Bundle
name|o2
parameter_list|)
block|{
name|String
name|bsn1
init|=
name|o1
operator|.
name|getSymbolicName
argument_list|()
decl_stmt|;
name|String
name|bsn2
init|=
name|o2
operator|.
name|getSymbolicName
argument_list|()
decl_stmt|;
name|int
name|c
init|=
name|bsn1
operator|.
name|compareTo
argument_list|(
name|bsn2
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
name|Version
name|v1
init|=
name|o1
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|Version
name|v2
init|=
name|o2
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|c
operator|=
name|v1
operator|.
name|compareTo
argument_list|(
name|v2
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
name|c
operator|=
name|o1
operator|.
name|hashCode
argument_list|()
operator|-
name|o2
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|c
return|;
block|}
block|}
end_class

end_unit

