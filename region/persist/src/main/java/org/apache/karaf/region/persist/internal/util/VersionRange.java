begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|Version
import|;
end_import

begin_comment
comment|/**  * @version $Rev:$ $Date:$  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|VersionRange
block|{
comment|/** A string representation of the version. */
specifier|private
name|String
name|version
decl_stmt|;
comment|/** The minimum desired version for the bundle */
specifier|private
name|Version
name|minimumVersion
decl_stmt|;
comment|/** The maximum desired version for the bundle */
specifier|private
name|Version
name|maximumVersion
decl_stmt|;
comment|/** True if the match is exclusive of the minimum version */
specifier|private
name|boolean
name|minimumExclusive
decl_stmt|;
comment|/** True if the match is exclusive of the maximum version */
specifier|private
name|boolean
name|maximumExclusive
decl_stmt|;
comment|/** A regexp to select the version */
specifier|private
specifier|static
specifier|final
name|Pattern
name|versionCapture
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"?(.*?)\"?$"
argument_list|)
decl_stmt|;
comment|/**      *      * @param version      *            version for the verioninfo      */
specifier|public
name|VersionRange
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|processVersionAttribute
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method should be used to create a version range from a single      * version string.      * @param version      *            version for the versioninfo      * @param exactVersion      *            whether this is an exact version {@code true} or goes to infinity      *            {@code false}      */
specifier|public
name|VersionRange
parameter_list|(
name|String
name|version
parameter_list|,
name|boolean
name|exactVersion
parameter_list|)
block|{
if|if
condition|(
name|exactVersion
condition|)
block|{
comment|// Do not store this string as it might be just a version, or a range!
name|processExactVersionAttribute
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|processVersionAttribute
argument_list|(
name|this
operator|.
name|version
argument_list|)
expr_stmt|;
block|}
name|assertInvariants
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor designed for internal use only.      *      * @param maximumVersion      * @param maximumExclusive      * @param minimumVersion      * @param minimumExclusive      * @throws IllegalArgumentException      *             if parameters are not valid.      */
specifier|private
name|VersionRange
parameter_list|(
name|Version
name|maximumVersion
parameter_list|,
name|boolean
name|maximumExclusive
parameter_list|,
name|Version
name|minimumVersion
parameter_list|,
name|boolean
name|minimumExclusive
parameter_list|)
block|{
name|this
operator|.
name|maximumVersion
operator|=
name|maximumVersion
expr_stmt|;
name|this
operator|.
name|maximumExclusive
operator|=
name|maximumExclusive
expr_stmt|;
name|this
operator|.
name|minimumVersion
operator|=
name|minimumVersion
expr_stmt|;
name|this
operator|.
name|minimumExclusive
operator|=
name|minimumExclusive
expr_stmt|;
name|assertInvariants
argument_list|()
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *      * @see org.apache.aries.application.impl.VersionRange#toString()      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// Some constructors don't take in a string that we can return directly,
comment|// so construct one if needed
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|maximumVersion
operator|==
literal|null
condition|)
block|{
name|version
operator|=
name|minimumVersion
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|version
operator|=
operator|(
name|minimumExclusive
condition|?
literal|"("
else|:
literal|"["
operator|)
operator|+
name|minimumVersion
operator|+
literal|","
operator|+
name|maximumVersion
operator|+
operator|(
name|maximumExclusive
condition|?
literal|")"
else|:
literal|"]"
operator|)
expr_stmt|;
block|}
block|}
return|return
name|this
operator|.
name|version
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
literal|17
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|minimumVersion
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|minimumExclusive
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|maximumVersion
operator|!=
literal|null
condition|?
name|maximumVersion
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|maximumExclusive
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|this
operator|==
name|other
condition|)
block|{
name|result
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|other
operator|instanceof
name|VersionRange
condition|)
block|{
name|VersionRange
name|vr
init|=
operator|(
name|VersionRange
operator|)
name|other
decl_stmt|;
name|result
operator|=
name|minimumVersion
operator|.
name|equals
argument_list|(
name|vr
operator|.
name|minimumVersion
argument_list|)
operator|&&
name|minimumExclusive
operator|==
name|vr
operator|.
name|minimumExclusive
operator|&&
operator|(
name|maximumVersion
operator|==
literal|null
condition|?
name|vr
operator|.
name|maximumVersion
operator|==
literal|null
else|:
name|maximumVersion
operator|.
name|equals
argument_list|(
name|vr
operator|.
name|maximumVersion
argument_list|)
operator|)
operator|&&
name|maximumExclusive
operator|==
name|vr
operator|.
name|maximumExclusive
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * this method returns the exact version from the versionInfo obj.      * this is used for DeploymentContent only to return a valid exact version      * otherwise, null is returned.      * @return the exact version      */
specifier|public
name|Version
name|getExactVersion
parameter_list|()
block|{
name|Version
name|v
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isExactVersion
argument_list|()
condition|)
block|{
name|v
operator|=
name|getMinimumVersion
argument_list|()
expr_stmt|;
block|}
return|return
name|v
return|;
block|}
comment|/**      * get the maximum version      * @return    the maximum version      */
specifier|public
name|Version
name|getMaximumVersion
parameter_list|()
block|{
return|return
name|maximumVersion
return|;
block|}
comment|/**      * get the minimum version      * @return    the minimum version      */
specifier|public
name|Version
name|getMinimumVersion
parameter_list|()
block|{
return|return
name|minimumVersion
return|;
block|}
comment|/**      * is the maximum version exclusive      * @return is the max version in the range.      */
specifier|public
name|boolean
name|isMaximumExclusive
parameter_list|()
block|{
return|return
name|maximumExclusive
return|;
block|}
comment|/**      * is the maximum version unbounded      * @return true if no upper bound was specified.      */
specifier|public
name|boolean
name|isMaximumUnbounded
parameter_list|()
block|{
name|boolean
name|unbounded
init|=
name|maximumVersion
operator|==
literal|null
decl_stmt|;
return|return
name|unbounded
return|;
block|}
comment|/**      * is the minimum version exclusive      * @return true if the min version is in range.      */
specifier|public
name|boolean
name|isMinimumExclusive
parameter_list|()
block|{
return|return
name|minimumExclusive
return|;
block|}
comment|/**      * this is designed for deployed-version as that is the exact version.      *      * @param version      * @return      * @throws IllegalArgumentException      */
specifier|private
name|boolean
name|processExactVersionAttribute
parameter_list|(
name|String
name|version
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|boolean
name|success
init|=
name|processVersionAttribute
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|maximumVersion
operator|==
literal|null
condition|)
block|{
name|maximumVersion
operator|=
name|minimumVersion
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|minimumVersion
operator|.
name|equals
argument_list|(
name|maximumVersion
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Version is not exact: "
operator|+
name|version
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|!
operator|!
name|isExactVersion
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Version is not exact: "
operator|+
name|version
argument_list|)
throw|;
block|}
return|return
name|success
return|;
block|}
comment|/**      * process the version attribute,      *      * @param version      *            the value to be processed      * @return      * @throws IllegalArgumentException      */
specifier|private
name|boolean
name|processVersionAttribute
parameter_list|(
name|String
name|version
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Version is null"
argument_list|)
throw|;
block|}
name|Matcher
name|matches
init|=
name|versionCapture
operator|.
name|matcher
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|matches
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|versions
init|=
name|matches
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|versions
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
operator|||
name|versions
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
operator|)
operator|&&
operator|(
name|versions
operator|.
name|endsWith
argument_list|(
literal|"]"
argument_list|)
operator|||
name|versions
operator|.
name|endsWith
argument_list|(
literal|")"
argument_list|)
operator|)
condition|)
block|{
if|if
condition|(
name|versions
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
condition|)
name|minimumExclusive
operator|=
literal|false
expr_stmt|;
elseif|else
if|if
condition|(
name|versions
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
condition|)
name|minimumExclusive
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|versions
operator|.
name|endsWith
argument_list|(
literal|"]"
argument_list|)
condition|)
name|maximumExclusive
operator|=
literal|false
expr_stmt|;
elseif|else
if|if
condition|(
name|versions
operator|.
name|endsWith
argument_list|(
literal|")"
argument_list|)
condition|)
name|maximumExclusive
operator|=
literal|true
expr_stmt|;
name|int
name|index
init|=
name|versions
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
decl_stmt|;
name|String
name|minVersion
init|=
name|versions
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|index
argument_list|)
decl_stmt|;
name|String
name|maxVersion
init|=
name|versions
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|versions
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
try|try
block|{
name|minimumVersion
operator|=
operator|new
name|Version
argument_list|(
name|minVersion
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|maximumVersion
operator|=
operator|new
name|Version
argument_list|(
name|maxVersion
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Version cannot be decoded: "
operator|+
name|version
argument_list|,
name|nfe
argument_list|)
throw|;
block|}
block|}
else|else
block|{
try|try
block|{
if|if
condition|(
name|versions
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
name|minimumVersion
operator|=
operator|new
name|Version
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
else|else
name|minimumVersion
operator|=
operator|new
name|Version
argument_list|(
name|versions
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Version cannot be decoded: "
operator|+
name|version
argument_list|,
name|nfe
argument_list|)
throw|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Version cannot be decoded: "
operator|+
name|version
argument_list|)
throw|;
block|}
return|return
name|success
return|;
block|}
comment|/**      * Assert object invariants. Called by constructors to verify that arguments      * were valid.      *      * @throws IllegalArgumentException      *             if invariants are violated.      */
specifier|private
name|void
name|assertInvariants
parameter_list|()
block|{
if|if
condition|(
name|minimumVersion
operator|==
literal|null
operator|||
operator|!
name|isRangeValid
argument_list|(
name|minimumVersion
argument_list|,
name|minimumExclusive
argument_list|,
name|maximumVersion
argument_list|,
name|maximumExclusive
argument_list|)
condition|)
block|{
name|IllegalArgumentException
name|e
init|=
operator|new
name|IllegalArgumentException
argument_list|()
decl_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
comment|/**      * Check if the supplied parameters describe a valid version range.      *      * @param min      *            the minimum version.      * @param minExclusive      *            whether the minimum version is exclusive.      * @param max      *            the maximum version.      * @param maxExclusive      *            whether the maximum version is exclusive.      * @return true is the range is valid; otherwise false.      */
specifier|private
name|boolean
name|isRangeValid
parameter_list|(
name|Version
name|min
parameter_list|,
name|boolean
name|minExclusive
parameter_list|,
name|Version
name|max
parameter_list|,
name|boolean
name|maxExclusive
parameter_list|)
block|{
name|boolean
name|result
decl_stmt|;
comment|// A null maximum version is unbounded so means that minimum is smaller
comment|// than
comment|// maximum.
name|int
name|minMaxCompare
init|=
operator|(
name|max
operator|==
literal|null
condition|?
operator|-
literal|1
else|:
name|min
operator|.
name|compareTo
argument_list|(
name|max
argument_list|)
operator|)
decl_stmt|;
if|if
condition|(
name|minMaxCompare
operator|>
literal|0
condition|)
block|{
comment|// Minimum larger than maximum is invalid.
name|result
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|minMaxCompare
operator|==
literal|0
operator|&&
operator|(
name|minExclusive
operator|||
name|maxExclusive
operator|)
condition|)
block|{
comment|// If min and max are the same, and either are exclusive, no valid
comment|// range
comment|// exists.
name|result
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
comment|// Range is valid.
name|result
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * This method checks that the provided version matches the desired version.      *      * @param version      *            the version.      * @return true if the version matches, false otherwise.      */
specifier|public
name|boolean
name|matches
parameter_list|(
name|Version
name|version
parameter_list|)
block|{
name|boolean
name|result
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|getMaximumVersion
argument_list|()
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|this
operator|.
name|getMinimumVersion
argument_list|()
operator|.
name|compareTo
argument_list|(
name|version
argument_list|)
operator|<=
literal|0
expr_stmt|;
block|}
else|else
block|{
name|int
name|minN
init|=
name|this
operator|.
name|isMinimumExclusive
argument_list|()
condition|?
literal|0
else|:
literal|1
decl_stmt|;
name|int
name|maxN
init|=
name|this
operator|.
name|isMaximumExclusive
argument_list|()
condition|?
literal|0
else|:
literal|1
decl_stmt|;
name|result
operator|=
operator|(
name|this
operator|.
name|getMinimumVersion
argument_list|()
operator|.
name|compareTo
argument_list|(
name|version
argument_list|)
operator|<
name|minN
operator|)
operator|&&
operator|(
name|version
operator|.
name|compareTo
argument_list|(
name|this
operator|.
name|getMaximumVersion
argument_list|()
argument_list|)
operator|<
name|maxN
operator|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * check if the versioninfo is the exact version      * @return true if the range will match 1 exact version.      */
specifier|public
name|boolean
name|isExactVersion
parameter_list|()
block|{
return|return
name|minimumVersion
operator|.
name|equals
argument_list|(
name|maximumVersion
argument_list|)
operator|&&
name|minimumExclusive
operator|==
name|maximumExclusive
operator|&&
operator|!
operator|!
operator|!
name|minimumExclusive
return|;
block|}
comment|/**      * Create a new version range that is the intersection of {@code this} and the argument.      * In other words, the largest version range that lies within both {@code this} and      * the parameter.      * @param r a version range to be intersected with {@code this}.      * @return a new version range, or {@code null} if no intersection is possible.      */
specifier|public
name|VersionRange
name|intersect
parameter_list|(
name|VersionRange
name|r
parameter_list|)
block|{
comment|// Use the highest minimum version.
specifier|final
name|Version
name|newMinimumVersion
decl_stmt|;
specifier|final
name|boolean
name|newMinimumExclusive
decl_stmt|;
name|int
name|minCompare
init|=
name|minimumVersion
operator|.
name|compareTo
argument_list|(
name|r
operator|.
name|getMinimumVersion
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|minCompare
operator|>
literal|0
condition|)
block|{
name|newMinimumVersion
operator|=
name|minimumVersion
expr_stmt|;
name|newMinimumExclusive
operator|=
name|minimumExclusive
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|minCompare
operator|<
literal|0
condition|)
block|{
name|newMinimumVersion
operator|=
name|r
operator|.
name|getMinimumVersion
argument_list|()
expr_stmt|;
name|newMinimumExclusive
operator|=
name|r
operator|.
name|isMinimumExclusive
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|newMinimumVersion
operator|=
name|minimumVersion
expr_stmt|;
name|newMinimumExclusive
operator|=
operator|(
name|minimumExclusive
operator|||
name|r
operator|.
name|isMinimumExclusive
argument_list|()
operator|)
expr_stmt|;
block|}
comment|// Use the lowest maximum version.
specifier|final
name|Version
name|newMaximumVersion
decl_stmt|;
specifier|final
name|boolean
name|newMaximumExclusive
decl_stmt|;
comment|// null maximum version means unbounded, so the highest possible value.
if|if
condition|(
name|maximumVersion
operator|==
literal|null
condition|)
block|{
name|newMaximumVersion
operator|=
name|r
operator|.
name|getMaximumVersion
argument_list|()
expr_stmt|;
name|newMaximumExclusive
operator|=
name|r
operator|.
name|isMaximumExclusive
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|r
operator|.
name|getMaximumVersion
argument_list|()
operator|==
literal|null
condition|)
block|{
name|newMaximumVersion
operator|=
name|maximumVersion
expr_stmt|;
name|newMaximumExclusive
operator|=
name|maximumExclusive
expr_stmt|;
block|}
else|else
block|{
name|int
name|maxCompare
init|=
name|maximumVersion
operator|.
name|compareTo
argument_list|(
name|r
operator|.
name|getMaximumVersion
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|maxCompare
operator|<
literal|0
condition|)
block|{
name|newMaximumVersion
operator|=
name|maximumVersion
expr_stmt|;
name|newMaximumExclusive
operator|=
name|maximumExclusive
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|maxCompare
operator|>
literal|0
condition|)
block|{
name|newMaximumVersion
operator|=
name|r
operator|.
name|getMaximumVersion
argument_list|()
expr_stmt|;
name|newMaximumExclusive
operator|=
name|r
operator|.
name|isMaximumExclusive
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|newMaximumVersion
operator|=
name|maximumVersion
expr_stmt|;
name|newMaximumExclusive
operator|=
operator|(
name|maximumExclusive
operator|||
name|r
operator|.
name|isMaximumExclusive
argument_list|()
operator|)
expr_stmt|;
block|}
block|}
name|VersionRange
name|result
decl_stmt|;
if|if
condition|(
name|isRangeValid
argument_list|(
name|newMinimumVersion
argument_list|,
name|newMinimumExclusive
argument_list|,
name|newMaximumVersion
argument_list|,
name|newMaximumExclusive
argument_list|)
condition|)
block|{
name|result
operator|=
operator|new
name|VersionRange
argument_list|(
name|newMaximumVersion
argument_list|,
name|newMaximumExclusive
argument_list|,
name|newMinimumVersion
argument_list|,
name|newMinimumExclusive
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Parse a version range..      *      * @param s      * @return VersionRange object.      * @throws IllegalArgumentException      *             if the String could not be parsed as a VersionRange      */
specifier|public
specifier|static
name|VersionRange
name|parseVersionRange
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
operator|new
name|VersionRange
argument_list|(
name|s
argument_list|)
return|;
block|}
comment|/**      * Parse a version range and indicate if the version is an exact version      *      * @param s      * @param exactVersion      * @return VersionRange object.      * @throws IllegalArgumentException      *             if the String could not be parsed as a VersionRange      */
specifier|public
specifier|static
name|VersionRange
name|parseVersionRange
parameter_list|(
name|String
name|s
parameter_list|,
name|boolean
name|exactVersion
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
operator|new
name|VersionRange
argument_list|(
name|s
argument_list|,
name|exactVersion
argument_list|)
return|;
block|}
block|}
end_class

end_unit

