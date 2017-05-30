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
name|tools
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tools
operator|.
name|utils
operator|.
name|model
operator|.
name|KarafPropertyEdit
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
name|tools
operator|.
name|utils
operator|.
name|model
operator|.
name|KarafPropertyEdits
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
name|IOException
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
name|HashMap
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

begin_comment
comment|/**  * Apply a set of edits, reading files from a stock etc dir.  */
end_comment

begin_class
specifier|public
class|class
name|KarafPropertiesEditor
block|{
specifier|private
name|File
name|inputEtc
decl_stmt|;
specifier|private
name|File
name|outputEtc
decl_stmt|;
specifier|private
name|KarafPropertyEdits
name|edits
decl_stmt|;
specifier|public
name|KarafPropertiesEditor
name|setInputEtc
parameter_list|(
name|File
name|inputEtc
parameter_list|)
block|{
name|this
operator|.
name|inputEtc
operator|=
name|inputEtc
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|KarafPropertiesEditor
name|setOutputEtc
parameter_list|(
name|File
name|outputEtc
parameter_list|)
block|{
name|this
operator|.
name|outputEtc
operator|=
name|outputEtc
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|KarafPropertiesEditor
name|setEdits
parameter_list|(
name|KarafPropertyEdits
name|edits
parameter_list|)
block|{
name|this
operator|.
name|edits
operator|=
name|edits
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|KarafPropertyEdit
argument_list|>
argument_list|>
name|editsByFile
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// organize edits by file.
for|for
control|(
name|KarafPropertyEdit
name|edit
range|:
name|edits
operator|.
name|getEdits
argument_list|()
control|)
block|{
name|editsByFile
operator|.
name|computeIfAbsent
argument_list|(
name|edit
operator|.
name|getFile
argument_list|()
argument_list|,
name|k
lambda|->
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|edit
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|KarafPropertyEdit
argument_list|>
argument_list|>
name|fileOps
range|:
name|editsByFile
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|File
name|input
init|=
operator|new
name|File
argument_list|(
name|inputEtc
argument_list|,
name|fileOps
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|KarafPropertiesFile
name|propsFile
init|=
operator|new
name|KarafPropertiesFile
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|propsFile
operator|.
name|load
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|KarafPropertyEdit
argument_list|>
name|edits
init|=
name|fileOps
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|KarafPropertyEdit
name|edit
range|:
name|edits
control|)
block|{
name|propsFile
operator|.
name|apply
argument_list|(
name|edit
argument_list|)
expr_stmt|;
block|}
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|outputEtc
argument_list|,
name|fileOps
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|propsFile
operator|.
name|store
argument_list|(
name|outputFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

