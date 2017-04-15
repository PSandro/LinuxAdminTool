package com.linuxgodmode.business;

import com.linuxgodmode.business.exception.ConnectionAlreadyInitializedException;
import com.linuxgodmode.business.exception.ConnectionNotInitializedException;
import com.linuxgodmode.business.exception.InitialisationFailException;
import com.google.common.base.Preconditions;
import com.jcraft.jsch.JSchException;

import de.lsa.logic.CommandManagerImpl;
import de.lsa.logic.CommandThreadExectutor;
import de.lsa.logic.ICommandManager;
import de.lsa.logic.connection.ISSHConnection;
import de.lsa.logic.connection.JSCHConnection;

/**
 * Created by sandro on 10.04.17 for project androidapp.
 */

public class ConnectionManager {

    private final CommandThreadExectutor threadExecutor = new CommandThreadExectutor("LGM-Thread", 6);
    private ICommandManager commandManager = null;
    private ISSHConnection sshConnection = null;
    private static final ConnectionManager connectionManager = new ConnectionManager();

    public CommandThreadExectutor getThreadExecutor() {
        return threadExecutor;
    }

    public ICommandManager getCommandManager() throws ConnectionNotInitializedException {
        if (commandManager == null) throw new ConnectionNotInitializedException();
        return commandManager;
    }

    public void uninitConnection() throws ConnectionNotInitializedException {
        if (this.sshConnection != null && this.commandManager != null) {
            this.sshConnection = null;
            this.commandManager = null;
        } else throw new ConnectionNotInitializedException();
    }

    public void initConnection(String user, String host, String password, int port) throws ConnectionAlreadyInitializedException, InitialisationFailException {
        Preconditions.checkNotNull(user, "The user cannot be null");
        Preconditions.checkNotNull(host, "The host cannot be null");
        Preconditions.checkNotNull(password, "The password cannot be null");

        //TODO thro JschException and run without threadExecutor

        ISSHConnection[] connection = new ISSHConnection[]{null};

        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    connection[0] = new JSCHConnection(user, host, password, port);
                } catch (JSchException e) {
                    e.printStackTrace();
                }
                synchronized (ConnectionManager.this) {
                    ConnectionManager.this.notify();
                }
            }
        });

        synchronized (ConnectionManager.this) {
            try {
                ConnectionManager.this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (connection[0] == null) throw new InitialisationFailException();

        if (this.commandManager == null && this.sshConnection == null) {
            this.sshConnection = connection[0];
            commandManager = new CommandManagerImpl(connection[0], threadExecutor);
        } else throw new ConnectionAlreadyInitializedException();

    }

    public boolean isConnected() {
        return this.sshConnection != null && this.commandManager != null;
    }


    public static ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
