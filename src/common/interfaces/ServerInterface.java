package common.interfaces;

import common.ShapesDrawn;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    /**
     * Register the new client in server for future remote function invocation
     * @param username client's username
     * @param client the client obj
     * @throws RemoteException RemoteException
     */
    void registerClient(String username, ClientInterface client) throws RemoteException;


    /**
     * Client request to join the server
     * @param username client's username
     * @return True if server accepts, False if server declines
     * @throws RemoteException RemoteException
     */
    boolean requestJoin(String username) throws RemoteException;

    /**
     * Disconnect the user
     * @param username user's username
     * @throws RemoteException RemoteException
     */
    void userDisconnect(String username) throws RemoteException;

    /**
     * Submit a new shape to server
     * @param shape the new shape
     * @throws RemoteException RemoteException
     */
    void drawNewShape(ShapesDrawn shape) throws RemoteException;
}
