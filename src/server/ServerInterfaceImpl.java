package server;

import common.ClientInterface;
import common.ServerInterface;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {
    ServerUI serverUI;
    String username;
    private final List<String> currentUsernames=new ArrayList<>();
    private final Map<String, ClientInterface> connectedClients = new HashMap<>();


    /**
     * Get current connected users list
     * @return connectedClients map
     */
    public Map<String, ClientInterface> getConnectedUserMap() {
        return connectedClients;
    }

    /**
     * Get all current usernames
     * @return current username list
     */
    public List<String> getCurrentUsernames() {
        return currentUsernames;
    }


    /**
     * Constructor
     * @param serverUI serverUI
     * @throws RemoteException RemoteException
     */
    protected ServerInterfaceImpl(ServerUI serverUI,String username) throws RemoteException {
        super();
        this.serverUI = serverUI;
        this.username = username;
        currentUsernames.add(username); //add the manager's username to username list
        serverUI.addUser(currentUsernames);
    }


    /**
     * Register the new client in server for future remote function invocation
     * @param username client's username
     * @param client the client obj
     * @throws RemoteException RemoteException
     */
    @Override
    public void registerClient(String username, ClientInterface client) throws RemoteException {
        //add the new client's name and ref to the map and list
        connectedClients.put(username,client);
        currentUsernames.add(username);
        serverUI.addUser(currentUsernames);

        //update the user list for all clients
        for (ClientInterface c : connectedClients.values()) {
            c.updateUserList(currentUsernames);
        }
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

