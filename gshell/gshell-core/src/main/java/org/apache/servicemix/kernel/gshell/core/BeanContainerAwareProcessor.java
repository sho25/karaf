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
name|gshell
operator|.
name|core
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|spring
operator|.
name|BeanContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|spring
operator|.
name|BeanContainerAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|BeanPostProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_class
specifier|public
class|class
name|BeanContainerAwareProcessor
implements|implements
name|InitializingBean
implements|,
name|BeanPostProcessor
implements|,
name|ApplicationContextAware
block|{
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
name|BeanContainer
name|container
decl_stmt|;
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|container
operator|=
operator|new
name|BeanContainerWrapper
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|postProcessBeforeInitialization
parameter_list|(
specifier|final
name|Object
name|bean
parameter_list|,
specifier|final
name|String
name|beanName
parameter_list|)
throws|throws
name|BeansException
block|{
assert|assert
name|bean
operator|!=
literal|null
assert|;
if|if
condition|(
name|bean
operator|instanceof
name|BeanContainerAware
condition|)
block|{
operator|(
operator|(
name|BeanContainerAware
operator|)
name|bean
operator|)
operator|.
name|setBeanContainer
argument_list|(
name|container
argument_list|)
expr_stmt|;
block|}
return|return
name|bean
return|;
block|}
specifier|public
name|Object
name|postProcessAfterInitialization
parameter_list|(
specifier|final
name|Object
name|bean
parameter_list|,
specifier|final
name|String
name|beanName
parameter_list|)
throws|throws
name|BeansException
block|{
return|return
name|bean
return|;
block|}
block|}
end_class

end_unit

