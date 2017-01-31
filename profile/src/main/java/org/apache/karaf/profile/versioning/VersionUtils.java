begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2016 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|profile
operator|.
name|versioning
package|;
end_package

begin_class
specifier|public
class|class
name|VersionUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SNAPSHOT
init|=
literal|"SNAPSHOT"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|DELIM_DASH
init|=
literal|'-'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|DELIM_DOT
init|=
literal|'.'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_VERSION
init|=
literal|"0.0.0"
decl_stmt|;
specifier|private
name|VersionUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|boolean
name|versionEquals
parameter_list|(
name|String
name|versionOne
parameter_list|,
name|String
name|versionTwo
parameter_list|)
block|{
if|if
condition|(
name|isSnapshotVersion
argument_list|(
name|versionOne
argument_list|)
operator|&&
name|isSnapshotVersion
argument_list|(
name|versionTwo
argument_list|)
condition|)
block|{
comment|// If both (and only if both) versions are snapshots, we compare without the snapshot classifier.
comment|// This is done to consider e.g. 1.2.0.SNAPSHOT and 1.2.SNAPSHOT equal.
name|versionOne
operator|=
name|versionWithoutSnapshot
argument_list|(
name|versionOne
argument_list|)
expr_stmt|;
name|versionTwo
operator|=
name|versionWithoutSnapshot
argument_list|(
name|versionTwo
argument_list|)
expr_stmt|;
block|}
comment|// Create comparable version objects.
specifier|final
name|ComparableVersion
name|cvOne
init|=
operator|new
name|ComparableVersion
argument_list|(
name|versionOne
argument_list|)
decl_stmt|;
specifier|final
name|ComparableVersion
name|cvTwo
init|=
operator|new
name|ComparableVersion
argument_list|(
name|versionTwo
argument_list|)
decl_stmt|;
comment|// Use equals of comparable version class.
return|return
name|cvOne
operator|.
name|equals
argument_list|(
name|cvTwo
argument_list|)
return|;
block|}
comment|/**      * Check if a version is a snapshot version.      *      * @param version the version to check      * @return true if {@code version} refers a snapshot version otherwise false      */
specifier|protected
specifier|static
name|boolean
name|isSnapshotVersion
parameter_list|(
specifier|final
name|String
name|version
parameter_list|)
block|{
return|return
name|version
operator|.
name|endsWith
argument_list|(
name|SNAPSHOT
argument_list|)
return|;
block|}
comment|/**      * Remove the snapshot classifier at the end of a version.      *      * @param version the version      * @return the given {@code version} without the snapshot classifier      */
specifier|protected
specifier|static
name|String
name|versionWithoutSnapshot
parameter_list|(
specifier|final
name|String
name|version
parameter_list|)
block|{
name|int
name|idx
decl_stmt|;
name|idx
operator|=
name|version
operator|.
name|lastIndexOf
argument_list|(
name|SNAPSHOT
argument_list|)
expr_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
return|return
name|version
return|;
block|}
elseif|else
if|if
condition|(
name|idx
operator|==
literal|0
condition|)
block|{
return|return
name|EMPTY_VERSION
return|;
block|}
else|else
block|{
specifier|final
name|char
name|delim
init|=
name|version
operator|.
name|charAt
argument_list|(
name|idx
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|delim
operator|==
name|DELIM_DOT
operator|||
name|delim
operator|==
name|DELIM_DASH
condition|)
block|{
operator|--
name|idx
expr_stmt|;
block|}
return|return
name|version
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit
