package server;

import common.interfaces.ServerInterface;
import common.WhiteBoardUIBasic;
import constants.ServerConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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


    private void exit() {
        if (server != null) {
            try {
                ((ServerInterfaceImpl) server).notifyClientsWhenOffline();
            } catch (RemoteException ex) {
                System.out.println(ex.getMessage());
            }
            System.exit(0);
        }
    }

    private void initialseServerUi() {
        setTitle(ServerConstants.WINDOW_TITLE);

        //add window listener to listen to the server's operation(when closing the window)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        JMenu fileMenu = new JMenu(ServerConstants.MENU_FILE);
        fileMenu.setBackground(Color.WHITE);

        // new canvas
        JMenuItem newItem = new JMenuItem(ServerConstants.MENU_NEW);
        newItem.setBackground(Color.WHITE);
        newItem.addActionListener(e -> {
            try {
                ((ServerInterfaceImpl) server).clearCanvas();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        // open a saved canvas
        JMenuItem openItem = new JMenuItem(ServerConstants.MENU_OPEN);
        openItem.setBackground(Color.WHITE);
        openItem.addActionListener(e -> {
            openCanvasToFile();
        });

        // save the current canvas
        JMenuItem saveItem = new JMenuItem(ServerConstants.MENU_SAVE);
        saveItem.setBackground(Color.WHITE);
        saveItem.addActionListener(e -> {
            saveCanvasToFile();
        });

        //save the current canvas to an image
        JMenuItem saveAsItem = new JMenuItem(ServerConstants.MENU_SAVE_AS);
        saveAsItem.setBackground(Color.WHITE);
        saveAsItem.addActionListener(e -> {
            saveAsImg();
        });
        //close the window
        JMenuItem closeItem = new JMenuItem(ServerConstants.MENU_CLOSE);
        closeItem.setBackground(Color.WHITE);
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

    private String showInputDialog(String msg, String title) {
        String filename = JOptionPane.showInputDialog(
                this,
                msg,
                title,
                JOptionPane.PLAIN_MESSAGE
        );
        return filename;
    }

    private void saveAsImg(){
        //get the size of drawing panel and create the image
        BufferedImage image = new BufferedImage(getDrawingPanel().getWidth(), getDrawingPanel().getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        getDrawingPanel().paint(graphics2D);
        graphics2D.dispose();

        //check whether the directory exists, if not create the directory
        File dir = new File(ServerConstants.CANVAS_SAVE_AS_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filename = showInputDialog(ServerConstants.INPUT_DIALOG_SAVE_AS_MSG, ServerConstants.INPUT_DIALOG_SAVE_AS_TITLE);
        //check whether the file exists
        checkFileNameOverwrite(filename,ServerConstants.FILE_ALREADY_EXISTS_MSG,ServerConstants.FILE_ALREADY_EXISTS_TITLE);
        File file = new File(ServerConstants.CANVAS_SAVE_AS_PATH + filename+".jpg");
        try {
            ImageIO.write(image, "jpg", file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private void saveCanvasToFile() {
        //give the file a name
        String filename = showInputDialog(ServerConstants.INPUT_DIALOG_SAVE_MSG, ServerConstants.INPUT_DIALOG_SAVE_TITLE);
        //check whether the file exists
        checkFileNameOverwrite(filename,ServerConstants.FILE_ALREADY_EXISTS_MSG,ServerConstants.FILE_ALREADY_EXISTS_TITLE);
        //save it to file
        try {
            ((ServerInterfaceImpl) server).saveCanvasToFile(filename);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void openCanvasToFile() {
        //give the file a name
        String filename = showInputDialog(ServerConstants.INPUT_DIALOG_OPEN_MSG, ServerConstants.INPUT_DIALOG_OPEN_TITLE);
        //check whether the file exists
        checkFileNameExists(filename,ServerConstants.FILE_NOT_EXIST_MSG,ServerConstants.ERROR);

        //load the list from file
        try {
            ((ServerInterfaceImpl) server).loadCanvasFromFile(filename);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void checkFileNameOverwrite(String filename, String msg, String title) {
        //check this filename exists
        File file = new File(ServerConstants.CANVAS_SAVE_PATH + filename);
        //if yes, ask the user if they want to overwrite
        if (file.exists()) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    msg,
                    title,
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.NO_OPTION) {
                return;
            }
        }
    }

    private void checkFileNameExists(String filename, String msg, String title) {
        //check this filename exists
        File file = new File(ServerConstants.CANVAS_SAVE_PATH + filename);
        //if yes, ask the user if they want to overwrite
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
        }
    }
}
