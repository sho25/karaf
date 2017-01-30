begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) 2002-2016, the original author or authors.  *  * This software is distributable under the BSD license. See the terms of the  * BSD license in the documentation provided with this software.  *  * http://www.opensource.org/licenses/bsd-license.php  */
end_comment

begin_package
package|package
name|jline
operator|.
name|internal
package|;
end_package

begin_comment
comment|// Some bits lifted from Guava's ( http://code.google.com/p/guava-libraries/ ) Preconditions.
end_comment

begin_comment
comment|/**  * Preconditions.  *  * @author<a href="mailto:jason@planet57.com">Jason Dillon</a>  * @since 2.7  */
end_comment

begin_class
specifier|public
class|class
name|Preconditions
block|{
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|checkNotNull
parameter_list|(
specifier|final
name|T
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
return|return
name|reference
return|;
block|}
block|}
end_class

end_unit

