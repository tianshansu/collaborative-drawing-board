package client;

import common.ServerInterface;

import java.rmi.Naming;
import java.util.Locale;

public class Client {

    /**
     * Main method to start the client
     * @param args command line arguments. args[0]=host name, args[1]=service name, args[2]=username
     */
    public static void main(String[] args) {
        //set the system language to English - to display the dialog button correctly
        Locale.setDefault(Locale.ENGLISH);

        String hostName = "localhost";
        String serviceName = "WhiteBoardService";
        String username = "Susan";
        if (args.length == 3) {
            hostName = args[0];
            serviceName = args[1];
            username = args[2];
        } else if (args.length == 1) {
            username = args[0];
        }
        try {
            ServerInterface whiteBoardServer = (ServerInterface) Naming.lookup("rmi://" + hostName + "/" + serviceName);
            System.out.println("requestJoin called by: " + username);
            System.out.println(whiteBoardServer.requestJoin(username));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
