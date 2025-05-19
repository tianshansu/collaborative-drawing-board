/**
 * Name: Tianshan Su
 * Student ID: 875734
 */
package client;

import common.ShapesDrawn;
import common.WhiteBoardUIBasic;
import common.interfaces.ClientInterface;
import constants.ClientConstants;

import constants.MessageConstants;


import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * ClientInterfaceImpl class, implements ClientInterface, handling callbacks from the server
 */
public class ClientInterfaceImpl extends UnicastRemoteObject implements ClientInterface {
    String username;
    WhiteBoardUIBasic ui;

    /**
     * Constructor
     * @param ui UI
     * @param username username
     * @throws RemoteException RemoteException
     */
    public ClientInterfaceImpl(WhiteBoardUIBasic ui, String username) throws RemoteException {
        this.ui = ui;
        this.username = username;
        ui.setCurrentUserName(username);
    }

    /**
     * Update the current user list
     *
     * @param userList the new user list
     * @throws RemoteException RemoteException
     */
    @Override
    public void updateUserList(List<String> userList) throws RemoteException {
        SwingUtilities.invokeLater(() -> ui.updateUserList(userList));
    }

    /**
     * Get notified when the manager disconnects - a separate thread
     *
     * @throws RemoteException RemoteException
     */
    @Override
    public void getNotifiedWhenManagerDisconnected() throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    ui,
                    ClientConstants.MANAGER_DISCONNECTED,
                    MessageConstants.DIALOG_TEXT_MANAGER_OFFLINE,
                    JOptionPane.WARNING_MESSAGE
            );
            System.exit(0);
        });
    }

    /**
     * Update the client's canvas after the manager draws something
     *
     * @throws RemoteException RemoteException
     */
    public void updateCanvas(List<ShapesDrawn> shapesDrawnList) throws RemoteException {
        SwingUtilities.invokeLater(() -> ui.updateCanvas(shapesDrawnList));
    }

    /**
     * update client's UI to show new chat msg
     *
     * @param username username of that msg
     * @param chatMsg  the actual chat msg
     * @throws RemoteException RemoteException
     */
    @Override
    public void updateChatMsg(String username, String chatMsg) throws RemoteException {
        SwingUtilities.invokeLater(() -> ui.addNewChatMsg(username, chatMsg));
    }

    /**
     * This current user being kicked by the manager
     * @throws RemoteException RemoteException
     */
    @Override
    public void kicked() throws RemoteException {
        //show msg and exit
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    ui,
                    ClientConstants.KICKED_BY_MANAGER,
                    ClientConstants.KICKED_TITLE,
                    JOptionPane.WARNING_MESSAGE
            );
            //exit
            System.exit(0);
        });
    }

    /**
     * Set whether the current whiteboard is active
     * @param active true if active
     * @throws RemoteException RemoteException
     */
    @Override
    public void setWhiteboardActive(boolean active) throws RemoteException {
        SwingUtilities.invokeLater(() -> ui.setWhiteboardActive(active));
    }

    /**
     * Update the user's editing status
     * @param username username of the user
     * @param isEditing true if is editing
     * @throws RemoteException RemoteException
     */
    @Override
    public void updateEditUsers(String username, boolean isEditing) throws RemoteException {
        SwingUtilities.invokeLater(() -> ui.setUserEditing(username, isEditing));
    }
}

