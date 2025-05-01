package client;

import common.ServerInterface;
import common.WhiteBoardUIBasic;
import server.ServerInterfaceImpl;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class ClientUI extends WhiteBoardUIBasic {
    ServerInterface server;

    /**
     * Constructor
     * @param server server's proxy
     * @param username client's username
     */
    public ClientUI(ServerInterface server,String username) {
        super();
        setTitle("Shared Whiteboard - Client");
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


}
