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
name|jaas
operator|.
name|modules
operator|.
name|syncope
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|SyncopeLoginModuleTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRolesExtractionSyncope1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|syncopeResponse
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
operator|+
literal|"<user>\n"
operator|+
literal|"<attributes>\n"
operator|+
literal|"<attribute>\n"
operator|+
literal|"<readonly>false</readonly>\n"
operator|+
literal|"<schema>cool</schema>\n"
operator|+
literal|"<value>false</value>\n"
operator|+
literal|"</attribute>\n"
operator|+
literal|"<attribute>\n"
operator|+
literal|"<readonly>false</readonly>\n"
operator|+
literal|"<schema>email</schema>\n"
operator|+
literal|"<value>karaf@example.net</value>\n"
operator|+
literal|"</attribute>\n"
operator|+
literal|"<attribute>\n"
operator|+
literal|"<readonly>false</readonly>\n"
operator|+
literal|"<schema>fullname</schema>\n"
operator|+
literal|"<value>karaf</value>\n"
operator|+
literal|"</attribute>\n"
operator|+
literal|"<attribute>\n"
operator|+
literal|"<readonly>false</readonly>\n"
operator|+
literal|"<schema>gender</schema>\n"
operator|+
literal|"<value>M</value>\n"
operator|+
literal|"</attribute>\n"
operator|+
literal|"<attribute>\n"
operator|+
literal|"<readonly>false</readonly>\n"
operator|+
literal|"<schema>surname</schema>\n"
operator|+
literal|"<value>karaf</value>\n"
operator|+
literal|"</attribute>\n"
operator|+
literal|"<attribute>\n"
operator|+
literal|"<readonly>false</readonly>\n"
operator|+
literal|"<schema>userId</schema>\n"
operator|+
literal|"<value>karaf@example.net</value>\n"
operator|+
literal|"</attribute>\n"
operator|+
literal|"</attributes>\n"
operator|+
literal|"<derivedAttributes/>\n"
operator|+
literal|"<id>100</id>\n"
operator|+
literal|"<propagationStatuses/>\n"
operator|+
literal|"<resources/>\n"
operator|+
literal|"<virtualAttributes/>\n"
operator|+
literal|"<creationDate>2014-08-12T18:37:09.202+02:00</creationDate>\n"
operator|+
literal|"<failedLogins>0</failedLogins>\n"
operator|+
literal|"<lastLoginDate>2014-08-13T09:38:02.204+02:00</lastLoginDate>\n"
operator|+
literal|"<memberships>\n"
operator|+
literal|"<membership>\n"
operator|+
literal|"<attributes/>\n"
operator|+
literal|"<derivedAttributes/>\n"
operator|+
literal|"<id>100</id>\n"
operator|+
literal|"<propagationStatuses/>\n"
operator|+
literal|"<resources/>\n"
operator|+
literal|"<virtualAttributes/>\n"
operator|+
literal|"<resources/>\n"
operator|+
literal|"<roleId>100</roleId>\n"
operator|+
literal|"<roleName>admin</roleName>\n"
operator|+
literal|"</membership>\n"
operator|+
literal|"<membership>\n"
operator|+
literal|"<attributes/>\n"
operator|+
literal|"<derivedAttributes/>\n"
operator|+
literal|"<id>101</id>\n"
operator|+
literal|"<propagationStatuses/>\n"
operator|+
literal|"<resources/>\n"
operator|+
literal|"<virtualAttributes/>\n"
operator|+
literal|"<resources/>\n"
operator|+
literal|"<roleId>101</roleId>\n"
operator|+
literal|"<roleName>another</roleName>\n"
operator|+
literal|"</membership>\n"
operator|+
literal|"</memberships>\n"
operator|+
literal|"<password>36460D3A3C1E27C0DB2AF23344475EE712DD3C9D</password>\n"
operator|+
literal|"<status>active</status>\n"
operator|+
literal|"<username>karaf</username>\n"
operator|+
literal|"</user>\n"
decl_stmt|;
name|SyncopeLoginModule
name|syncopeLoginModule
init|=
operator|new
name|SyncopeLoginModule
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
name|syncopeLoginModule
operator|.
name|extractingRolesSyncope1
argument_list|(
name|syncopeResponse
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|roles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"admin"
argument_list|,
name|roles
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"another"
argument_list|,
name|roles
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRolesExtractionSyncope2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|syncopeResponse
init|=
literal|"{\n"
operator|+
literal|"\n"
operator|+
literal|"   \"@class\":\"org.apache.syncope.common.lib.to.UserTO\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"creator\":\"admin\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"creationDate\":\"2017-07-31T08:36:41.000+0000\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"lastModifier\":\"admin\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"lastChangeDate\":\"2017-08-01T08:46:19.236+0000\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"key\":\"e5a131b0-eb66-4115-a131-b0eb66511579\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"type\":\"USER\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"realm\":\"/karaf\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"status\":\"created\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"password\":null,\n"
operator|+
literal|"\n"
operator|+
literal|"   \"token\":null,\n"
operator|+
literal|"\n"
operator|+
literal|"   \"tokenExpireTime\":null,\n"
operator|+
literal|"\n"
operator|+
literal|"   \"username\":\"karaf\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"lastLoginDate\":\"2017-08-01T08:46:19.224+0000\",\n"
operator|+
literal|"\n"
operator|+
literal|"   \"changePwdDate\":null,\n"
operator|+
literal|"\n"
operator|+
literal|"   \"failedLogins\":0,\n"
operator|+
literal|"\n"
operator|+
literal|"   \"securityQuestion\":null,\n"
operator|+
literal|"\n"
operator|+
literal|"   \"securityAnswer\":null,\n"
operator|+
literal|"\n"
operator|+
literal|"   \"mustChangePassword\":false,\n"
operator|+
literal|"\n"
operator|+
literal|"   \"auxClasses\":[\n"
operator|+
literal|"\n"
operator|+
literal|" \n"
operator|+
literal|"\n"
operator|+
literal|"   ],\n"
operator|+
literal|"\n"
operator|+
literal|"   \"plainAttrs\":[\n"
operator|+
literal|"\n"
operator|+
literal|" \n"
operator|+
literal|"\n"
operator|+
literal|"   ],\n"
operator|+
literal|"\n"
operator|+
literal|"   \"derAttrs\":[\n"
operator|+
literal|"\n"
operator|+
literal|" \n"
operator|+
literal|"\n"
operator|+
literal|"   ],\n"
operator|+
literal|"\n"
operator|+
literal|"   \"virAttrs\":[\n"
operator|+
literal|"\n"
operator|+
literal|" \n"
operator|+
literal|"\n"
operator|+
literal|"   ],\n"
operator|+
literal|"\n"
operator|+
literal|"   \"resources\":[\n"
operator|+
literal|"\n"
operator|+
literal|" \n"
operator|+
literal|"\n"
operator|+
literal|"   ],\n"
operator|+
literal|"\n"
operator|+
literal|"   \"roles\":[\n"
operator|+
literal|"\n"
operator|+
literal|"      \"admin\", \"another\"\n"
operator|+
literal|"\n"
operator|+
literal|"   ],\n"
operator|+
literal|"\n"
operator|+
literal|"   \"dynRoles\":[\n"
operator|+
literal|"\n"
operator|+
literal|"      \"admin\"\n"
operator|+
literal|"\n"
operator|+
literal|"   ],\n"
operator|+
literal|"\n"
operator|+
literal|"   \"relationships\":[\n"
operator|+
literal|"\n"
operator|+
literal|" \n"
operator|+
literal|"\n"
operator|+
literal|"   ],\n"
operator|+
literal|"\n"
operator|+
literal|"   \"memberships\":[\n"
operator|+
literal|"\n"
operator|+
literal|"      {\n"
operator|+
literal|"\n"
operator|+
literal|"         \"type\":\"Membership\",\n"
operator|+
literal|"\n"
operator|+
literal|"         \"rightType\":\"GROUP\",\n"
operator|+
literal|"\n"
operator|+
literal|"         \"rightKey\":\"3847aa78-3202-4d8f-87aa-7832026d8fba\",\n"
operator|+
literal|"\n"
operator|+
literal|"         \"groupName\":\"manager\",\n"
operator|+
literal|"\n"
operator|+
literal|"         \"plainAttrs\":[\n"
operator|+
literal|"\n"
operator|+
literal|" \n"
operator|+
literal|"\n"
operator|+
literal|"         ],\n"
operator|+
literal|"\n"
operator|+
literal|"         \"derAttrs\":[\n"
operator|+
literal|"\n"
operator|+
literal|" \n"
operator|+
literal|"\n"
operator|+
literal|"         ],\n"
operator|+
literal|"\n"
operator|+
literal|"         \"virAttrs\":[\n"
operator|+
literal|"\n"
operator|+
literal|" \n"
operator|+
literal|"\n"
operator|+
literal|"         ]\n"
operator|+
literal|"\n"
operator|+
literal|"      }\n"
operator|+
literal|"\n"
operator|+
literal|"   ],\n"
operator|+
literal|"\n"
operator|+
literal|"   \"dynGroups\":[\n"
operator|+
literal|"\n"
operator|+
literal|" \n"
operator|+
literal|"\n"
operator|+
literal|"   ]\n"
operator|+
literal|"\n"
operator|+
literal|"}"
decl_stmt|;
name|SyncopeLoginModule
name|syncopeLoginModule
init|=
operator|new
name|SyncopeLoginModule
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
name|syncopeLoginModule
operator|.
name|extractingRolesSyncope2
argument_list|(
name|syncopeResponse
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|roles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"admin"
argument_list|,
name|roles
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"another"
argument_list|,
name|roles
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

