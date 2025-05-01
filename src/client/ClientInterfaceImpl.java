package client;

import common.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientInterfaceImpl extends UnicastRemoteObject implements ClientInterface {
    ClientUI clientUI;

    protected ClientInterfaceImpl(ClientUI clientUI) throws RemoteException {
        this.clientUI = clientUI;
    }

    @Override
    public void updateUserList(List<String> userList) throws RemoteException {
        clientUI.addUser(userList);
    }
}
