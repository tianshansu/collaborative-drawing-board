package client;

import common.interfaces.ServerInterface;
import common.WhiteBoardUIBasic;
import constants.ClientConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ClientUI extends WhiteBoardUIBasic {
    ServerInterface server;
    private DefaultListModel<String> userListModel;

    /**
     * Constructor
     * @param server server's proxy
     * @param username client's username
     */
    public ClientUI(ServerInterface server,String username) {
        super();
        setTitle(ClientConstants.UI_TITLE);
        this.server = server;

        //add window listener to listen to the client's operation(when closing the window)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    server.userDisconnect(username); //disconnect this user from the server
                } catch (RemoteException ex) {
                    System.out.println(ex.getMessage());
                }
                System.exit(0);
            }
        });

    }

    /**
     * Update the user list
     * @param userList new username list
     */
    @Override
    public void updateUserList(List<String> userList) {
        currentUserList = new ArrayList<>(userList);
        userListModel.clear();
        for (String name : userList) {
            if (activeEditors.contains(name)) {
                userListModel.addElement(name + " 🟢");
            } else {
                userListModel.addElement(name);
            }
        }
    }

    @Override
    protected void addUserListPanel(){
        super.addUserListPanel();

        //add user list component
        userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(290, 160));
        userListPanel.add(scrollPane, BorderLayout.CENTER);
    }
}
