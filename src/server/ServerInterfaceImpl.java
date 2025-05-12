package server;

import common.ShapesDrawn;
import common.interfaces.ClientInterface;
import common.interfaces.ServerInterface;
import constants.ServerConstants;
import enums.JoinResult;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {
    ServerUI serverUI;
    String username;
    private final List<String> currentUsernames = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, ClientInterface> connectedClients = Collections.synchronizedMap(new HashMap<>());
    final List<ShapesDrawn> shapesDrawnList = Collections.synchronizedList(new ArrayList<>()); //store all shapes on canvas


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
        serverUI.setCurrentUserName(username);
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
        synchronized (this) {
            connectedClients.put(username, client);
            currentUsernames.add(username);
        }
        updateUserListForAllUsers();
        connectedClients.get(username).updateCanvas(shapesDrawnList);//update the new client's canvas
    }

    /**
     * Client request to join the server
     * @param username client's username
     * @return True if server accepts, False if server declines
     * @throws RemoteException RemoteException
     */
    @Override
    public JoinResult requestJoin(String username) throws RemoteException {
        synchronized (currentUsernames) {
            if (currentUsernames.contains(username)) {
                return JoinResult.NAME_TAKEN;
            }
        }

        final int[] allowJoin = new int[1];
        try {
            SwingUtilities.invokeAndWait(() ->
                    allowJoin[0] = JOptionPane.showConfirmDialog(
                            serverUI,
                            username + ServerConstants.JOIN_REQUEST_MSG,
                            ServerConstants.JOIN_REQUEST_TITLE,
                            JOptionPane.YES_NO_OPTION
                    )
            );

        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        if(allowJoin[0] == JOptionPane.YES_OPTION){
            return JoinResult.SUCCESS;
        }else {
            return JoinResult.REJECTED;
        }
    }

    /**
     * Disconnect the user
     * @param username user's username
     * @throws RemoteException RemoteException
     */
    @Override
    public void userDisconnect(String username) throws RemoteException {
        synchronized (this) {
            currentUsernames.remove(username);
            connectedClients.remove(username);
        }

        updateUserListForAllUsers();

    }

    /**
     * Submit a new shape to server
     * @param shape the new shape
     * @throws RemoteException RemoteException
     */
    @Override
    public void drawNewShape(ShapesDrawn shape) throws RemoteException {
        synchronized (shapesDrawnList) {
            if (!shapesDrawnList.contains(shape)) {
                shapesDrawnList.add(shape);
            }
        }
        for (ClientInterface client : connectedClients.values()) {
            client.updateCanvas(shapesDrawnList);
        }
    }

    /**
     * broadcast the new chat msg to all clients
     * @param username username of that msg sender
     * @param msg the actual msg
     * @throws RemoteException RemoteException
     */
    @Override
    public void sendNewChatMsg(String username, String msg) throws RemoteException {
        for (ClientInterface client : connectedClients.values()) {
            client.updateChatMsg(username,msg);
        }
    }

    /**
     * Empty the drawing canvas (the new button on server side)
     * @throws RemoteException RemoteException
     */
    public void clearCanvas() throws RemoteException {
        shapesDrawnList.clear();
        for (ClientInterface client : connectedClients.values()) {
            client.updateCanvas(shapesDrawnList);
        }
    }

    /**
     * Save the current canvas to file
     * @param filename new filename
     * @throws RemoteException RemoteException
     */
    public void saveCanvasToFile(String filename) throws RemoteException {
        try {
            //check whether the directory exists, if not create the directory
            File dir = new File(ServerConstants.CANVAS_SAVE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //write the list to file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ServerConstants.CANVAS_SAVE_PATH + filename));
            oos.writeObject(shapesDrawnList);
            oos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Load a canvas from file
     * @param filename new filename
     * @throws RemoteException RemoteException
     */
    public void loadCanvasFromFile(String filename) throws RemoteException {
        try {
            clearCanvas(); //clear the current canvas
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ServerConstants.CANVAS_SAVE_PATH+ filename));
            List<ShapesDrawn> loadedList = (List<ShapesDrawn>) ois.readObject();
            shapesDrawnList.clear();
            shapesDrawnList.addAll(loadedList);//add the contents in the current list
            //display the new canvas in all client UIs
            for (ClientInterface client : connectedClients.values()) {
                client.updateCanvas(shapesDrawnList);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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

    /**
     * kick a user by username
     * @param username user's username
     * @throws RemoteException RemoteException
     */
    @Override
    public void kickUser(String username) throws RemoteException {
        //remove the connection
        ClientInterface user;
        synchronized (this) {
            user = connectedClients.get(username);
            if (user != null) {
                currentUsernames.remove(username);
                connectedClients.remove(username);
            }
        }

        if (user != null) {
            updateUserListForAllUsers();

            new Thread(() -> {
                try {
                    user.kicked();
                } catch (RemoteException e) {
                    System.out.println(e.getMessage());
                }
            }).start();
        }

    }

    /**
     * Close current whiteboard
     * @throws RemoteException RemoteException
     */
    public void closeBoard() throws RemoteException {
        shapesDrawnList.clear();
        for (ClientInterface client : connectedClients.values()) {
            client.setWhiteboardActive(false);
            client.updateCanvas(shapesDrawnList);
        }
    }

    /**
     * Create a new whiteboard
     * @throws RemoteException RemoteException
     */
    public void newBoard() throws RemoteException {
        for (ClientInterface client : connectedClients.values()) {
            client.setWhiteboardActive(true);
            client.updateCanvas(shapesDrawnList);
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

