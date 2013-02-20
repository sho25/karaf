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
name|tooling
operator|.
name|exam
operator|.
name|container
operator|.
name|internal
operator|.
name|adaptions
package|;
end_package

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

begin_comment
comment|/**  * Factory returning the correct version of the manipulator depending on the added Karaf version.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|KarafManipulatorFactory
block|{
specifier|private
name|KarafManipulatorFactory
parameter_list|()
block|{
comment|// Not required for a final class
block|}
specifier|public
specifier|static
name|KarafManipulator
name|createManipulator
parameter_list|(
name|String
name|karafVersion
parameter_list|)
block|{
name|int
name|dots
init|=
literal|0
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|i
operator|=
name|karafVersion
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|,
name|i
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|dots
operator|++
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|Version
name|version
decl_stmt|;
if|if
condition|(
name|dots
operator|<
literal|3
condition|)
block|{
name|version
operator|=
operator|new
name|Version
argument_list|(
name|karafVersion
operator|.
name|replaceFirst
argument_list|(
literal|"-"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|version
operator|=
operator|new
name|Version
argument_list|(
name|karafVersion
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|version
operator|.
name|getMajor
argument_list|()
operator|<
literal|2
operator|||
name|version
operator|.
name|getMajor
argument_list|()
operator|==
literal|2
operator|&&
name|version
operator|.
name|getMinor
argument_list|()
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Karaf versions< 2.2.0 are not supported"
argument_list|)
throw|;
block|}
return|return
operator|new
name|KarafManipulatorStartingFrom220
argument_list|()
return|;
block|}
block|}
end_class

end_unit

