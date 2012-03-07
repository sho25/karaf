begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|config
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|Option
import|;
end_import

begin_comment
comment|/**  * Abstract class which commands that are related to property processing should extend.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ConfigPropertyCommandSupport
extends|extends
name|ConfigCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
literal|"--pid"
argument_list|,
name|description
operator|=
literal|"The configuration pid"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|protected
name|String
name|pid
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-b"
argument_list|,
name|aliases
operator|=
block|{
literal|"--bypass-storage"
block|}
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|description
operator|=
literal|"Do not store the configuration in a properties file, but feed it directly to ConfigAdmin"
argument_list|)
specifier|protected
name|boolean
name|bypassStorage
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|Dictionary
name|props
init|=
name|getEditedProps
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
operator|&&
name|pid
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"No configuration is being edited--run the edit command first"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|props
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
block|}
name|propertyAction
argument_list|(
name|props
argument_list|)
expr_stmt|;
if|if
condition|(
name|requiresUpdate
argument_list|(
name|pid
argument_list|)
condition|)
block|{
name|this
operator|.
name|configRepository
operator|.
name|update
argument_list|(
name|pid
argument_list|,
name|props
argument_list|,
name|bypassStorage
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Perform an action on the properties.      * @param props      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|protected
specifier|abstract
name|void
name|propertyAction
parameter_list|(
name|Dictionary
name|props
parameter_list|)
function_decl|;
comment|/**      * Checks if the configuration requires to be updated.      * The default behavior is to update if a valid pid has been passed to the method.      * @param pid      * @return      */
specifier|protected
name|boolean
name|requiresUpdate
parameter_list|(
name|String
name|pid
parameter_list|)
block|{
if|if
condition|(
name|pid
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Retrieves confguration from the pid, if used or delegates to session from getting the configuration.      * @return      * @throws Exception      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|protected
name|Dictionary
name|getEditedProps
parameter_list|()
throws|throws
name|Exception
block|{
name|Dictionary
name|props
init|=
name|this
operator|.
name|configRepository
operator|.
name|getConfigProperties
argument_list|(
name|pid
argument_list|)
decl_stmt|;
return|return
operator|(
name|props
operator|!=
literal|null
operator|)
condition|?
name|props
else|:
name|super
operator|.
name|getEditedProps
argument_list|()
return|;
block|}
block|}
end_class

end_unit

