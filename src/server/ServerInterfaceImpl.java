package server;

import common.ShapesDrawn;
import common.interfaces.ClientInterface;
import common.interfaces.ServerInterface;

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
    private List<ShapesDrawn> shapesDrawnList = new ArrayList<>(); //store all shapes on canvas


    /**
     * Constructor
     * @param serverUI serverUI
     * @throws RemoteException RemoteException
     */
    protected ServerInterfaceImpl(ServerUI serverUI,String username) throws RemoteException {
        super();
        this.serverUI = serverUI;
        this.username = username;
        serverUI.setServer(this);
        //serverUI.updateUserList(currentUsernames);
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
        updateUserListForAllUsers();
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

    /**
     * Disconnect the user
     * @param username user's username
     * @throws RemoteException RemoteException
     */
    @Override
    public void userDisconnect(String username) throws RemoteException {
        currentUsernames.remove(username);
        connectedClients.remove(username);
        updateUserListForAllUsers();

    }

    /**
     * Submit a new shape to server
     * @param shape the new shape
     * @throws RemoteException RemoteException
     */
    @Override
    public void drawNewShape(ShapesDrawn shape) throws RemoteException {
        shapesDrawnList.add(shape);
        for (ClientInterface client : connectedClients.values()) {
            client.updateCanvas(shapesDrawnList);
        }
    }

    /**
     * notify all current users when the manager is offline
     * @throws RemoteException RemoteException
     */
    public void notifyClientsWhenOffline() throws RemoteException {
        for (ClientInterface client : connectedClients.values()) {
            client.getNotifiedWhenManagerDisconnected();
        }
    }


    private void updateUserListForAllUsers() throws RemoteException {
        serverUI.updateUserList(currentUsernames);
        //update the user list for all clients
        for (ClientInterface client : connectedClients.values()) {
            client.updateUserList(currentUsernames);
        }
    }
}

