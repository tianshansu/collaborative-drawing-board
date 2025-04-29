package server;

import common.ServerInterface;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {
    ServerUI serverUI;

    /**
     * Constructor
     * @param serverUI serverUI
     * @throws RemoteException RemoteException
     */
    protected ServerInterfaceImpl(ServerUI serverUI) throws RemoteException {
        super();
        this.serverUI = serverUI;
    }


    /**
     * Client request to join the server
     * @param username client's username
     * @return True if server accepts, False if server declines
     * @throws RemoteException RemoteException
     */
    @Override
    public boolean requestJoin(String username) throws RemoteException {
        final int[] allowJoin = new int[1];

        try {
            SwingUtilities.invokeAndWait(() ->
                    allowJoin[0] = JOptionPane.showConfirmDialog(
                            serverUI,
                            username + " wants to join the whiteboard. Allow?",
                            "A New User Wants to Join",
                            JOptionPane.YES_NO_OPTION
                    )
            );

        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return allowJoin[0] == JOptionPane.YES_OPTION;
    }
}

