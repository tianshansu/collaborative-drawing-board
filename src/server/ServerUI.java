package server;

import common.interfaces.ServerInterface;
import common.WhiteBoardUIBasic;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class ServerUI extends WhiteBoardUIBasic {
    ServerInterface server;

    public void setServer(ServerInterface server) {
        this.server = server;
    }

    /**
     * Constructor
     */
    public ServerUI() {
        super();
        setTitle("Shared Whiteboard - Manager");

        //add window listener to listen to the server's operation(when closing the window)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(server != null) {
                    try {
                        ((ServerInterfaceImpl) server).notifyClientsWhenOffline();
                    } catch (RemoteException ex) {
                        System.out.println(ex.getMessage());
                    }

                    System.exit(0);

                }

            }
        });

    }
}
