begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|itests
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Inherited
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assume
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|MethodRule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|FrameworkMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|Statement
import|;
end_import

begin_class
specifier|public
class|class
name|RunIfRule
implements|implements
name|MethodRule
block|{
specifier|public
interface|interface
name|RunIfCondition
block|{
name|boolean
name|isSatisfied
parameter_list|()
function_decl|;
block|}
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|METHOD
block|,
name|ElementType
operator|.
name|ANNOTATION_TYPE
block|}
argument_list|)
annotation|@
name|Inherited
specifier|public
annotation_defn|@interface
name|RunIf
block|{
name|Class
argument_list|<
name|?
extends|extends
name|RunIfCondition
argument_list|>
name|condition
parameter_list|()
function_decl|;
block|}
specifier|public
name|Statement
name|apply
parameter_list|(
name|Statement
name|base
parameter_list|,
name|FrameworkMethod
name|method
parameter_list|,
name|Object
name|target
parameter_list|)
block|{
name|List
argument_list|<
name|RunIf
argument_list|>
name|ignores
init|=
name|findRunIfs
argument_list|(
name|method
operator|.
name|getAnnotations
argument_list|()
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|RunIf
argument_list|>
argument_list|()
argument_list|,
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ignores
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|base
return|;
block|}
for|for
control|(
name|RunIf
name|ignore
range|:
name|ignores
control|)
block|{
name|RunIfCondition
name|condition
init|=
name|newCondition
argument_list|(
name|ignore
argument_list|,
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|condition
operator|.
name|isSatisfied
argument_list|()
condition|)
block|{
return|return
operator|new
name|IgnoreStatement
argument_list|(
name|condition
argument_list|)
return|;
block|}
block|}
return|return
name|base
return|;
block|}
specifier|private
name|List
argument_list|<
name|RunIf
argument_list|>
name|findRunIfs
parameter_list|(
name|Annotation
index|[]
name|annotations
parameter_list|,
name|List
argument_list|<
name|RunIf
argument_list|>
name|ignores
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|>
name|tested
parameter_list|)
block|{
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|tested
operator|.
name|add
argument_list|(
name|annotation
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|annotation
operator|instanceof
name|RunIf
condition|)
block|{
name|ignores
operator|.
name|add
argument_list|(
operator|(
name|RunIf
operator|)
name|annotation
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|findRunIfs
argument_list|(
name|annotation
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|ignores
argument_list|,
name|tested
argument_list|)
expr_stmt|;
for|for
control|(
name|Class
name|cl
range|:
name|annotation
operator|.
name|getClass
argument_list|()
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|findRunIfs
argument_list|(
name|cl
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|ignores
argument_list|,
name|tested
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|ignores
return|;
block|}
specifier|private
name|RunIfCondition
name|newCondition
parameter_list|(
name|RunIf
name|annotation
parameter_list|,
name|Object
name|instance
parameter_list|)
block|{
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|RunIfCondition
argument_list|>
name|cond
init|=
name|annotation
operator|.
name|condition
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|cond
operator|.
name|isMemberClass
argument_list|()
condition|)
block|{
if|if
condition|(
name|Modifier
operator|.
name|isStatic
argument_list|(
name|cond
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|cond
operator|.
name|getDeclaredConstructor
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{}
block|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|instance
operator|!=
literal|null
operator|&&
name|instance
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|cond
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|cond
operator|.
name|getDeclaredConstructor
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|instance
operator|.
name|getClass
argument_list|()
block|}
block|)
operator|.
name|newInstance
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to instanciate "
operator|+
name|cond
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|cond
operator|.
name|newInstance
argument_list|()
return|;
block|}
block|}
end_class

begin_catch
catch|catch
parameter_list|(
name|RuntimeException
name|re
parameter_list|)
block|{
throw|throw
name|re
throw|;
block|}
end_catch

begin_catch
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
end_catch

begin_class
unit|}      private
specifier|static
class|class
name|IgnoreStatement
extends|extends
name|Statement
block|{
specifier|private
name|RunIfCondition
name|condition
decl_stmt|;
name|IgnoreStatement
parameter_list|(
name|RunIfCondition
name|condition
parameter_list|)
block|{
name|this
operator|.
name|condition
operator|=
name|condition
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|evaluate
parameter_list|()
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
literal|"Ignored by "
operator|+
name|condition
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

unit|}
end_unit
