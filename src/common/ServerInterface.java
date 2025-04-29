package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    /**
     * Client request to join the server
     * @param username client's username
     * @return True if server accepts, False if server declines
     * @throws RemoteException RemoteException
     */
    boolean requestJoin(String username) throws RemoteException;
}
