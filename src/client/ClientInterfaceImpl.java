package client;

import common.ClientInterface;
import constants.ClientConstants;

import constants.MessageConstants;


import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientInterfaceImpl extends UnicastRemoteObject implements ClientInterface {
    String username;
    ClientUI clientUI;

    /**
     * Constructor
     *
     * @param clientUI clientUI
     * @throws RemoteException RemoteException
     */
    protected ClientInterfaceImpl(ClientUI clientUI, String username) throws RemoteException {
        this.clientUI = clientUI;
        this.username = username;


    }

    /**
     * Update the current user list
     *
     * @param userList the new user list
     * @throws RemoteException RemoteException
     */
    @Override
    public void updateUserList(List<String> userList) throws RemoteException {
        clientUI.updateUserList(userList);
    }

    /**
     * Get notified when the manager disconnects - a separate thread
     *
     * @throws RemoteException RemoteException
     */
    @Override
    public void getNotifiedWhenManagerDisconnected() throws RemoteException {
        new Thread(() -> {
            JOptionPane.showMessageDialog(
                    clientUI,
                    ClientConstants.MANAGER_DISCONNECTED,
                    MessageConstants.DIALOG_TEXT_MANAGER_OFFLINE,
                    JOptionPane.WARNING_MESSAGE
            );
            System.exit(0);
        }).start();
    }


}

