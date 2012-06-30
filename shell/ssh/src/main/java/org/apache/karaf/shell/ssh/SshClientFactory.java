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
name|ssh
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|SshClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|agent
operator|.
name|SshAgentFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|ServerKeyVerifier
import|;
end_import

begin_class
specifier|public
class|class
name|SshClientFactory
block|{
specifier|private
name|SshAgentFactory
name|agentFactory
decl_stmt|;
specifier|private
name|File
name|knownHosts
decl_stmt|;
specifier|public
name|SshClientFactory
parameter_list|(
name|SshAgentFactory
name|agentFactory
parameter_list|,
name|File
name|knownHosts
parameter_list|)
block|{
name|this
operator|.
name|agentFactory
operator|=
name|agentFactory
expr_stmt|;
name|this
operator|.
name|knownHosts
operator|=
name|knownHosts
expr_stmt|;
block|}
specifier|public
name|SshClient
name|create
parameter_list|(
name|boolean
name|quiet
parameter_list|)
block|{
name|SshClient
name|client
init|=
name|SshClient
operator|.
name|setUpDefaultClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|setAgentFactory
argument_list|(
name|agentFactory
argument_list|)
expr_stmt|;
name|KnownHostsManager
name|knownHostsManager
init|=
operator|new
name|KnownHostsManager
argument_list|(
name|knownHosts
argument_list|)
decl_stmt|;
name|ServerKeyVerifier
name|serverKeyVerifier
init|=
operator|new
name|ServerKeyVerifierImpl
argument_list|(
name|knownHostsManager
argument_list|,
name|quiet
argument_list|)
decl_stmt|;
name|client
operator|.
name|setServerKeyVerifier
argument_list|(
name|serverKeyVerifier
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

