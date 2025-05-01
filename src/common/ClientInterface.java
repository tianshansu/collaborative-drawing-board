package common;

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
}
