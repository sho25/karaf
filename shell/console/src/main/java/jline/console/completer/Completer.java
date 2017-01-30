begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) 2002-2016, the original author or authors.  *  * This software is distributable under the BSD license. See the terms of the  * BSD license in the documentation provided with this software.  *  * http://www.opensource.org/licenses/bsd-license.php  */
end_comment

begin_package
package|package
name|jline
operator|.
name|console
operator|.
name|completer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A completer is the mechanism by which tab-completion candidates will be resolved.  *  * @author<a href="mailto:mwp1@cornell.edu">Marc Prud'hommeaux</a>  * @author<a href="mailto:jason@planet57.com">Jason Dillon</a>  * @since 2.3  */
end_comment

begin_interface
specifier|public
interface|interface
name|Completer
block|{
comment|//
comment|// FIXME: Check if we can use CharSequece for buffer?
comment|//
comment|/**      * Populates<i>candidates</i> with a list of possible completions for the<i>buffer</i>.      *      * The<i>candidates</i> list will not be sorted before being displayed to the user: thus, the      * complete method should sort the {@link List} before returning.      *      * @param buffer        The buffer      * @param cursor        The current position of the cursor in the<i>buffer</i>      * @param candidates    The {@link List} of candidates to populate      * @return              The index of the<i>buffer</i> for which the completion will be relative      */
name|int
name|complete
parameter_list|(
name|String
name|buffer
parameter_list|,
name|int
name|cursor
parameter_list|,
name|List
argument_list|<
name|CharSequence
argument_list|>
name|candidates
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

