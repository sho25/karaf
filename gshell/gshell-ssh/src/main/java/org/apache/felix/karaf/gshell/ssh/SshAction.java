begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|///*
end_comment

begin_comment
comment|// * Licensed to the Apache Software Foundation (ASF) under one
end_comment

begin_comment
comment|// * or more contributor license agreements.  See the NOTICE file
end_comment

begin_comment
comment|// * distributed with this work for additional information
end_comment

begin_comment
comment|// * regarding copyright ownership.  The ASF licenses this file
end_comment

begin_comment
comment|// * to you under the Apache License, Version 2.0 (the
end_comment

begin_comment
comment|// * "License"); you may not use this file except in compliance
end_comment

begin_comment
comment|// * with the License.  You may obtain a copy of the License at
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// *  http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// * Unless required by applicable law or agreed to in writing,
end_comment

begin_comment
comment|// * software distributed under the License is distributed on an
end_comment

begin_comment
comment|// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
end_comment

begin_comment
comment|// * KIND, either express or implied.  See the License for the
end_comment

begin_comment
comment|// * specific language governing permissions and limitations
end_comment

begin_comment
comment|// * under the License.
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//package org.apache.felix.karaf.gshell.ssh;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import org.apache.sshd.ClientChannel;
end_comment

begin_comment
comment|//import org.apache.sshd.ClientSession;
end_comment

begin_comment
comment|//import org.apache.sshd.SshClient;
end_comment

begin_comment
comment|//import org.apache.sshd.client.future.ConnectFuture;
end_comment

begin_comment
comment|//import org.apache.sshd.common.util.NoCloseInputStream;
end_comment

begin_comment
comment|//import org.apache.sshd.common.util.NoCloseOutputStream;
end_comment

begin_comment
comment|//import org.apache.felix.karaf.gshell.console.OsgiCommandSupport;
end_comment

begin_comment
comment|//import org.apache.felix.karaf.gshell.console.BlueprintContainerAware;
end_comment

begin_comment
comment|//import org.apache.felix.gogo.commands.Option;
end_comment

begin_comment
comment|//import org.apache.felix.gogo.commands.Argument;
end_comment

begin_comment
comment|//import org.slf4j.Logger;
end_comment

begin_comment
comment|//import org.slf4j.LoggerFactory;
end_comment

begin_comment
comment|//import org.osgi.service.blueprint.container.BlueprintContainer;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///**
end_comment

begin_comment
comment|// * Connect to a SSH server.
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// * @version $Rev: 721244 $ $Date: 2008-11-27 18:19:56 +0100 (Thu, 27 Nov 2008) $
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public class SshAction
end_comment

begin_comment
comment|//    extends OsgiCommandSupport implements BlueprintContainerAware
end_comment

begin_comment
comment|//{
end_comment

begin_comment
comment|//    private final Logger log = LoggerFactory.getLogger(getClass());
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    @Option(name="-l", aliases={"--username"}, description = "Username")
end_comment

begin_comment
comment|//    private String username;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    @Option(name="-P", aliases={"--password"}, description = "Password")
end_comment

begin_comment
comment|//    private String password;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    @Argument(required=true, description = "Host")
end_comment

begin_comment
comment|//    private String hostname;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    @Option(name="-p", aliases={"--port"}, description = "Port")
end_comment

begin_comment
comment|//    private int port = 22;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private BlueprintContainer container;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	private ClientSession session;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setBlueprintContainer(final BlueprintContainer container) {
end_comment

begin_comment
comment|//        assert container != null;
end_comment

begin_comment
comment|//        this.container = container;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    /**
end_comment

begin_comment
comment|//     * Helper to validate that prompted username or password is not null or empty.
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    private class UsernamePasswordValidator
end_comment

begin_comment
comment|//        implements PromptReader.Validator
end_comment

begin_comment
comment|//    {
end_comment

begin_comment
comment|//        private String type;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        private int count = 0;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        private int max = 3;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        public UsernamePasswordValidator(final String type) {
end_comment

begin_comment
comment|//            assert type != null;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            this.type = type;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        public boolean isValid(final String value) {
end_comment

begin_comment
comment|//            count++;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            if (value != null&& value.trim().length()> 0) {
end_comment

begin_comment
comment|//                return true;
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            if (count>= max) {
end_comment

begin_comment
comment|//                throw new RuntimeException("Too many attempts; failed to prompt user for " + type + " after " + max + " tries");
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            return false;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    @Override
end_comment

begin_comment
comment|//    protected Object doExecute() throws Exception {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        //
end_comment

begin_comment
comment|//        // TODO: Parse hostname for<username>@<hostname>
end_comment

begin_comment
comment|//        //
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        System.out.println("Connecting to host " + hostname + " on port " + port);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        // If the username/password was not configured via cli, then prompt the user for the values
end_comment

begin_comment
comment|//        if (username == null || password == null) {
end_comment

begin_comment
comment|//            PromptReader prompter = new PromptReader(io);
end_comment

begin_comment
comment|//            log.debug("Prompting user for credentials");
end_comment

begin_comment
comment|//            if (username == null) {
end_comment

begin_comment
comment|//                username = prompter.readLine("Login: ", new UsernamePasswordValidator("login"));
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            if (password == null) {
end_comment

begin_comment
comment|//                text = messages.getMessage("prompt.password");
end_comment

begin_comment
comment|//                password = prompter.readPassword("Password: ", new UsernamePasswordValidator("password"));
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        // Create the client from prototype
end_comment

begin_comment
comment|//        SshClient client = (SshClient) container.getComponentInstance(SshClient.class.getName());
end_comment

begin_comment
comment|//        log.debug("Created client: {}", client);
end_comment

begin_comment
comment|//        client.start();;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        try {
end_comment

begin_comment
comment|//            ConnectFuture future = client.connect(hostname, port);
end_comment

begin_comment
comment|//            future.await();
end_comment

begin_comment
comment|//            session = future.getSession();
end_comment

begin_comment
comment|//            try {
end_comment

begin_comment
comment|//                System.out.println("Connected");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                session.authPassword(username, password);
end_comment

begin_comment
comment|//                int ret = session.waitFor(ClientSession.WAIT_AUTH | ClientSession.CLOSED | ClientSession.AUTHED, 0);
end_comment

begin_comment
comment|//                if ((ret& ClientSession.AUTHED) == 0) {
end_comment

begin_comment
comment|//                    System.err.println("Authentication failed");
end_comment

begin_comment
comment|//                    return null;
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                ClientChannel channel = session.createChannel("shell");
end_comment

begin_comment
comment|//                channel.setIn(new NoCloseInputStream(System.in));
end_comment

begin_comment
comment|//                channel.setOut(new NoCloseOutputStream(System.out));
end_comment

begin_comment
comment|//                channel.setErr(new NoCloseOutputStream(System.err));
end_comment

begin_comment
comment|//                channel.open();
end_comment

begin_comment
comment|//                channel.waitFor(ClientChannel.CLOSED, 0);
end_comment

begin_comment
comment|//            } finally {
end_comment

begin_comment
comment|//                session.close(false);
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//        } finally {
end_comment

begin_comment
comment|//            client.stop();
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        return null;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

