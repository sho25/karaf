begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|packages
operator|.
name|core
operator|.
name|internal
operator|.
name|filter
package|;
end_package

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
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|FilterParser
block|{
specifier|public
name|Expression
name|parse
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
return|return
name|parse
argument_list|(
operator|new
name|ExprTokenizer
argument_list|(
name|filter
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|Expression
name|parse
parameter_list|(
name|ExprTokenizer
name|tokenizer
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|"("
operator|.
name|equals
argument_list|(
name|tokenizer
operator|.
name|nextToken
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid Syntax"
argument_list|)
throw|;
block|}
return|return
name|parseFilterComp
argument_list|(
name|tokenizer
argument_list|)
return|;
block|}
specifier|private
name|Expression
name|parseFilterComp
parameter_list|(
name|ExprTokenizer
name|tokenizer
parameter_list|)
block|{
name|String
name|token
init|=
name|tokenizer
operator|.
name|peekNextToken
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"!"
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
return|return
operator|new
name|NotExpression
argument_list|(
name|parse
argument_list|(
name|tokenizer
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
literal|"&"
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
return|return
operator|new
name|AndExpression
argument_list|(
name|parseFilterList
argument_list|(
name|tokenizer
argument_list|)
argument_list|)
return|;
block|}
return|return
name|parseItem
argument_list|(
name|tokenizer
argument_list|)
return|;
block|}
specifier|private
name|Expression
index|[]
name|parseFilterList
parameter_list|(
name|ExprTokenizer
name|tokenizer
parameter_list|)
block|{
name|List
argument_list|<
name|Expression
argument_list|>
name|exprList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
literal|"("
operator|.
name|equals
argument_list|(
name|tokenizer
operator|.
name|peekNextToken
argument_list|()
argument_list|)
condition|)
block|{
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|exprList
operator|.
name|add
argument_list|(
name|parseFilterComp
argument_list|(
name|tokenizer
argument_list|)
argument_list|)
expr_stmt|;
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
return|return
name|exprList
operator|.
name|toArray
argument_list|(
operator|new
name|Expression
index|[]
block|{}
argument_list|)
return|;
block|}
specifier|private
name|Expression
name|parseItem
parameter_list|(
name|ExprTokenizer
name|tokenizer
parameter_list|)
block|{
name|String
name|attr
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|String
name|filterType
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
return|return
operator|new
name|SimpleItem
argument_list|(
name|attr
argument_list|,
name|filterType
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

