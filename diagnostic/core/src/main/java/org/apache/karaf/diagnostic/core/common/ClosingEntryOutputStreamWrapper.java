begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|diagnostic
operator|.
name|core
operator|.
name|common
package|;
end_package

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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipOutputStream
import|;
end_import

begin_comment
comment|/**  * Output stream which closes entry instead closing whole stream.  *   * @author ldywicki  */
end_comment

begin_class
specifier|public
class|class
name|ClosingEntryOutputStreamWrapper
extends|extends
name|OutputStream
block|{
comment|/** 	 * Wrapped ZIP output stream.  	 */
specifier|private
name|ZipOutputStream
name|outputStream
decl_stmt|;
comment|/** 	 * Creates new OutputStream. 	 *  	 * @param outputStream Wrapped output stream. 	 */
specifier|public
name|ClosingEntryOutputStreamWrapper
parameter_list|(
name|ZipOutputStream
name|outputStream
parameter_list|)
block|{
name|this
operator|.
name|outputStream
operator|=
name|outputStream
expr_stmt|;
block|}
comment|/** 	 * {@inheritDoc} 	 */
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * {@inheritDoc} 	 */
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * {@inheritDoc} 	 */
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * {@inheritDoc} 	 */
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
comment|// close entry instead of closing stream.
name|outputStream
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

