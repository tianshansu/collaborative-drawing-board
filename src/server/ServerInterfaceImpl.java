package server;

import common.ShapesDrawn;
import common.interfaces.ClientInterface;
import common.interfaces.ServerInterface;
import constants.ServerConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
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
        connectedClients.put(username,client);
        currentUsernames.add(username);
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

//    public void saveAsImg(String filename) throws RemoteException {
//        File output = new File("output.jpg");
//        ImageIO.write(image, "jpg", output);
//    }


    private void updateUserListForAllUsers() throws RemoteException {
        serverUI.updateUserList(currentUsernames);
        //update the user list for all clients
        for (ClientInterface client : connectedClients.values()) {
            client.updateUserList(currentUsernames);
        }
    }
}

