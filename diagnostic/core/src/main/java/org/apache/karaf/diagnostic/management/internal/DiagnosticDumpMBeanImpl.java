begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
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
operator|.
name|internal
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
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StandardMBean
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
name|diagnostic
operator|.
name|core
operator|.
name|Dump
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
name|diagnostic
operator|.
name|core
operator|.
name|DumpDestination
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
name|diagnostic
operator|.
name|management
operator|.
name|DiagnosticDumpMBean
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
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Implementation of diagnostic mbean.  */
end_comment

begin_class
specifier|public
class|class
name|DiagnosticDumpMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|DiagnosticDumpMBean
block|{
comment|/**      * Dump providers.      */
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|SimpleDateFormat
name|dumpFormat
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd_HHmmss"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DiagnosticDumpMBeanImpl
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Creates new diagnostic mbean.      *      * @throws NotCompliantMBeanException      */
specifier|public
name|DiagnosticDumpMBeanImpl
parameter_list|()
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|DiagnosticDumpMBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates dump witch given name      *      * @param name Name of the dump.      */
specifier|public
name|void
name|createDump
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|createDump
argument_list|(
literal|false
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|createDump
parameter_list|(
name|boolean
name|directory
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|name
operator|=
name|dumpFormat
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|directory
condition|)
block|{
name|name
operator|+=
literal|".zip"
expr_stmt|;
block|}
block|}
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|DumpDestination
name|destination
decl_stmt|;
if|if
condition|(
name|directory
condition|)
block|{
name|destination
operator|=
name|Dump
operator|.
name|directory
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|destination
operator|=
name|Dump
operator|.
name|zip
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
name|Dump
operator|.
name|dump
argument_list|(
name|bundleContext
argument_list|,
name|destination
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Created dump "
operator|+
name|destination
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the bundle context      */
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
block|}
end_class

end_unit

