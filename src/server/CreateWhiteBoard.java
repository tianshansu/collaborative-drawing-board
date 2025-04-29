package server;

import common.ServerInterface;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Locale;

public class CreateWhiteBoard {

    /**
     * Main method to start the server
     * @param args command line arguments. args[0]=host name, args[1]=service name
     */
    public static void main(String[] args) {
        //set the system language to English - to display the dialog button correctly
        Locale.setDefault(Locale.ENGLISH);

        String hostName = "localhost";
        String serviceName = "WhiteBoardService";
        if (args.length == 2) {
            hostName = args[0];
            serviceName = args[1];
        }

        ServerUI serverUI= new ServerUI();

        try {
            LocateRegistry.createRegistry(1099); // Start and register an RMI registry on port 1099

            ServerInterface whiteBoardServer = new ServerInterfaceImpl(serverUI);
            Naming.rebind("rmi://" + hostName + "/" + serviceName, whiteBoardServer);

            System.out.println("Whiteboard RMI Server is running...");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
