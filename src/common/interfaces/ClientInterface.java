package common.interfaces;

import common.ShapesDrawn;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

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

    void kicked() throws RemoteException;

    void setWhiteboardActive(boolean active) throws RemoteException;
}
