package server;

import common.interfaces.ServerInterface;
import common.UIUtils;
import constants.MessageConstants;
import constants.ServerConstants;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Locale;

public class CreateWhiteBoard {

    /**
     * Main method to start the server
     *
     * @param args command line arguments. args[0]=host name, args[1]=service name
     */
    public static void main(String[] args) {
        //set the system language to English - to display the dialog button correctly
        Locale.setDefault(Locale.ENGLISH);
        int serverPort=-1;

        //check if the arguments length is 3
        if (args.length != 3) {
            UIUtils.exitWithMsg(null, MessageConstants.ARGUMENT_LENGTH_INCORRECT);
        }

        //get all the information needed from the command line
        String serverIp = args[0];
        try{
            serverPort = Integer.parseInt(args[1]);
            if(serverPort < 1024 || serverPort > 65535){
                UIUtils.exitWithMsg(null, MessageConstants.PORT_OUT_OF_RANGE);
            }
        }catch (NumberFormatException e){
            UIUtils.exitWithMsg(null, MessageConstants.PORT_NUMBER_INCORRECT_FORMAT);
        }

        String username = args[2];

        //create the server UI
        ServerUI serverUI = new ServerUI();

        try {
            LocateRegistry.createRegistry(serverPort); // Start and register an RMI registry on port 1099

            ServerInterface whiteBoardServer = new ServerInterfaceImpl(serverUI, username);
            Naming.rebind("rmi://" + serverIp + ":" + serverPort + "/" + ServerConstants.SERVICE_NAME, whiteBoardServer);
            System.out.println("Whiteboard RMI Server is running...");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



}
