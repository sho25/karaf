begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
name|command
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|features
operator|.
name|FeaturesService
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
name|Argument
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
name|console
operator|.
name|AbstractAction
import|;
end_import

begin_comment
comment|/**  * Concatenate and print files and/or URLs.  *  * @version $Rev: 593392 $ $Date: 2007-11-09 03:14:15 +0100 (Fri, 09 Nov 2007) $  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"feature"
argument_list|,
name|name
operator|=
literal|"chooseurl"
argument_list|,
name|description
operator|=
literal|"Add a repository url for well known features"
argument_list|)
specifier|public
class|class
name|ChooseUrlCommand
extends|extends
name|AbstractAction
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
literal|""
argument_list|,
name|description
operator|=
literal|""
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|""
argument_list|,
name|description
operator|=
literal|""
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|FeatureFinder
name|featureFinder
decl_stmt|;
specifier|private
name|FeaturesService
name|featuresService
decl_stmt|;
specifier|public
name|void
name|setFeatureFinder
parameter_list|(
name|FeatureFinder
name|featureFinder
parameter_list|)
block|{
name|this
operator|.
name|featureFinder
operator|=
name|featureFinder
expr_stmt|;
block|}
specifier|public
name|void
name|setFeaturesService
parameter_list|(
name|FeaturesService
name|featuresService
parameter_list|)
block|{
name|this
operator|.
name|featuresService
operator|=
name|featuresService
expr_stmt|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|effectiveVersion
init|=
operator|(
name|version
operator|==
literal|null
operator|)
condition|?
literal|"LATEST"
else|:
name|version
decl_stmt|;
name|URI
name|uri
init|=
name|featureFinder
operator|.
name|getUriFor
argument_list|(
name|name
argument_list|,
name|effectiveVersion
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No feature found for name "
operator|+
name|name
operator|+
literal|" and version "
operator|+
name|version
argument_list|)
throw|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"adding feature url "
operator|+
name|uri
argument_list|)
expr_stmt|;
name|featuresService
operator|.
name|addRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

