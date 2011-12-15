begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|$
block|{
package|package
block|}
end_package

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_class
specifier|public
class|class
name|MyServiceImpl
implements|implements
name|MyService
block|{
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
literal|"Echo processed: "
operator|+
name|message
return|;
block|}
block|}
end_class

end_unit

