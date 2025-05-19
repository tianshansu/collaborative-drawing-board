/**
 * Name: Tianshan Su
 * Student ID: 875734
 */
package common.interfaces;

import common.ShapesDrawn;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * ClientInterface, extends Remote, the remote methods that can be invoked by the server
 */
public interface ClientInterface extends Remote {

    /**
     * Update the user list on the client side
     * @param userList the new user list
     * @throws RemoteException RemoteException
     */
    void updateUserList(List<String> userList) throws RemoteException;

    /**
     * Get the dialog when the manager is offline
     * @throws RemoteException RemoteException
     */
    void getNotifiedWhenManagerDisconnected() throws RemoteException;

    /**
     * Update the client's canvas after the manager draws something
     * @param shapesDrawnList the list of the shapes drawn
     * @throws RemoteException RemoteException
     */
    void updateCanvas(List<ShapesDrawn> shapesDrawnList) throws RemoteException;

    /**
     * update client's UI to show new chat msg
     * @param username username of that msg
     * @param chatMsg the actual chat msg
     * @throws RemoteException RemoteException
     */
    void updateChatMsg(String username,String chatMsg) throws RemoteException;

    /**
     * This current user being kicked by the manager
     * @throws RemoteException RemoteException
     */
    void kicked() throws RemoteException;

    /**
     * Set whether the current whiteboard is active
     * @param active true if active
     * @throws RemoteException RemoteException
     */
    void setWhiteboardActive(boolean active) throws RemoteException;

    /**
     * Update the user's editing status
     * @param username username of the user
     * @param isEditing true if is editing
     * @throws RemoteException RemoteException
     */
    void updateEditUsers(String username, boolean isEditing) throws RemoteException;
}
