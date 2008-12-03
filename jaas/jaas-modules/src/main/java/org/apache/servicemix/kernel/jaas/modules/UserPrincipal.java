begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|kernel
operator|.
name|jaas
operator|.
name|modules
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_class
specifier|public
class|class
name|UserPrincipal
implements|implements
name|Principal
block|{
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|public
name|UserPrincipal
parameter_list|(
name|String
name|name
parameter_list|)
block|{
assert|assert
name|name
operator|!=
literal|null
assert|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|UserPrincipal
operator|)
condition|)
return|return
literal|false
return|;
name|UserPrincipal
name|that
init|=
operator|(
name|UserPrincipal
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|?
operator|!
name|name
operator|.
name|equals
argument_list|(
name|that
operator|.
name|name
argument_list|)
else|:
name|that
operator|.
name|name
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|name
operator|!=
literal|null
condition|?
name|name
operator|.
name|hashCode
argument_list|()
else|:
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"UserPrincipal["
operator|+
name|name
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

