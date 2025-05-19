/**
 * Name: Tianshan Su
 * Student ID: 875734
 */
package client;

import common.interfaces.ServerInterface;
import common.UIUtils;
import constants.ClientConstants;
import constants.MessageConstants;
import constants.ServerConstants;
import enums.JoinResult;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.util.Locale;

/**
 * Ordinary user's entry point - join a whiteboard
 */
public class JoinWhiteBoard {

    /**
     * Main method to start the client
     *
     * @param args command line arguments. args[0]=host name, args[1]=service name, args[2]=username
     */
    public static void main(String[] args) {
        //set the system language to English - to display the dialog button correctly
        Locale.setDefault(Locale.ENGLISH);

        int serverPort = -1;

        //check if the arguments length is 3
        if (args.length != 3) {
            UIUtils.exitWithMsg(null, MessageConstants.ARGUMENT_LENGTH_INCORRECT);
        }

        //get all the information needed from the command line
        String serverIP = args[0];
        try {
            serverPort = Integer.parseInt(args[1]);
            if (serverPort < 1024 || serverPort > 65535) {
                UIUtils.exitWithMsg(null, MessageConstants.PORT_OUT_OF_RANGE);
            }
        } catch (NumberFormatException e) {
            UIUtils.exitWithMsg(null, MessageConstants.PORT_NUMBER_INCORRECT_FORMAT);
        }

        String username = args[2];

        try {
            ServerInterface whiteBoardServer = (ServerInterface) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/" + ServerConstants.SERVICE_NAME);
            //send join request to the server(manager)
            JoinResult joinResult = whiteBoardServer.requestJoin(username);
            switch (joinResult) {
                case SUCCESS:
                    //create the client UI and register in server
                    ClientUI clientUI = new ClientUI(whiteBoardServer, username);
                    ClientInterfaceImpl whiteBoardClient = new ClientInterfaceImpl(clientUI, username);
                    whiteBoardServer.registerClient(username, whiteBoardClient);
                    clientUI.setServerInterface(whiteBoardServer);
                    break;
                case REJECTED:
                    //show rejection msg and exit
                    UIUtils.exitWithMsg(null, ClientConstants.JOIN_REQUEST_DECLINED);
                    break;
                case NAME_TAKEN:
                    //show name taken msg and exit
                    UIUtils.exitWithMsg(null, ClientConstants.NAME_TAKEN);
                    break;
            }
        } catch (NotBoundException e) {
            UIUtils.exitWithMsg(null, ClientConstants.NO_MANAGER_FOUND);
        } catch (UnknownHostException e){
            UIUtils.exitWithMsg(null, MessageConstants.UNKNOWN_HOST);
        } catch (MalformedURLException | RemoteException e) {
            UIUtils.exitWithMsg(null, ClientConstants.UNABLE_TO_CONNECT);
        }
    }

}
