package server;

import common.interfaces.ServerInterface;
import common.WhiteBoardUIBasic;

import javax.swing.*;
import java.awt.*;
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
        initialseServerUi();
    }


    private void exit(){
        if(server != null) {
            try {
                ((ServerInterfaceImpl) server).notifyClientsWhenOffline();
            } catch (RemoteException ex) {
                System.out.println(ex.getMessage());
            }
            System.exit(0);
        }
    }

    private void initialseServerUi(){
        setTitle("Shared Whiteboard - Manager");
        //add window listener to listen to the server's operation(when closing the window)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        // new canvas
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> {
            try {
                server.clearCanvas();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
        // open a saved canvas
        JMenuItem openItem = new JMenuItem("Open");
        // save the current canvas
        JMenuItem saveItem = new JMenuItem("Save");
        //save the current canvas to an image
        JMenuItem saveAsItem = new JMenuItem("SaveAs");
        //close the window
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> {
            exit();
        });
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(closeItem);
        menuBar.add(fileMenu);
        add(menuBar, BorderLayout.NORTH);
    }
}
