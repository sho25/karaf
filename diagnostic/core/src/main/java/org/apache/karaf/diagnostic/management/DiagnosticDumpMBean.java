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
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanException
import|;
end_import

begin_comment
comment|/**  * Diagnostic MBean which allows to create dumps over JMX.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DiagnosticDumpMBean
block|{
comment|/**      * Creates dump over JMX.      *       * @param name Name of the dump.      * @throws MBeanException In case of any problems.      */
name|void
name|createDump
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Create dump with directory switch and name.      *       * @param directory Should dump be created in directory.      * @param name Name of the dump.      * @param noThreadDump True to not include thread dump, false else.      * @param noHeapDump True to not include heap dump, false else.      * @throws MBeanException In case of any problems.      */
name|void
name|createDump
parameter_list|(
name|boolean
name|directory
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|noThreadDump
parameter_list|,
name|boolean
name|noHeapDump
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
block|}
end_interface

end_unit

