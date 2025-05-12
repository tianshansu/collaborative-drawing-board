package server;

import common.interfaces.ServerInterface;
import common.WhiteBoardUIBasic;
import constants.ServerConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;


public class ServerUI extends WhiteBoardUIBasic {
    private ServerInterface server;
    private JTable userTable;

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

    /**
     * Update the user list panel
     * @param userList new username list
     */
    @Override
    public void updateUserList(List<String> userList) {
        String[] columnNames = { "User", "Operation" };

        if (userList == null || userList.isEmpty()) {
            userTable.setModel(new DefaultTableModel(columnNames, 0));
            return;
        }

        String[][] data = new String[userList.size()][2];
        for (int i = 0; i < userList.size(); i++) {
            data[i][0] = userList.get(i);
            //only show the kick text for ordinary users
            if(i!=0){
                data[i][1] = "kick";
            }else {
                data[i][1] = "";
            }

        }

        userTable.setModel(new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        if (userTable.getColumnCount() >= 2) {
            userTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            userTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        }

        userTable.setShowVerticalLines(false);
        userTable.setRowSelectionAllowed(false);
        userTable.setBackground(Color.WHITE);
        userTable.getTableHeader().setBackground(Color.WHITE);
        userTable.setGridColor(Color.BLACK);
        userTable.setShowGrid(true);
        userTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        userTable.revalidate();
        userTable.repaint();
    }

    @Override
    protected void addUserListPanel(){
        super.addUserListPanel();

        JScrollPane scrollPaneUsers = new JScrollPane((Component) null);
        scrollPaneUsers.setPreferredSize(new Dimension(290, 200));
        scrollPaneUsers.setBackground(Color.WHITE);
        scrollPaneUsers.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        scrollPaneUsers.getViewport().setBackground(Color.WHITE);

        userTable = new JTable();
        userTable.setBackground(Color.WHITE);
        userTable.getTableHeader().setBackground(Color.WHITE);

        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //when click the delete text, call service and delete it
                int row = userTable.rowAtPoint(e.getPoint());
                int column = userTable.columnAtPoint(e.getPoint());


                //only do the action if the client clicked operation column
                if (column == 1&&row!=0) {
                    String userToRemove = (String)userTable.getValueAt(row, 0); //get the username of that user
                    try {
                        server.kickUser(userToRemove);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                userTable.revalidate();
                userTable.repaint();
            }
        });
        scrollPaneUsers.setViewportView(userTable);
        userListPanel.add(scrollPaneUsers);

    }

    private void closeCurrentBoard() {
        try {
            ((ServerInterfaceImpl)server).closeBoard();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialseServerUi() {
        setTitle(ServerConstants.WINDOW_TITLE);

        //add window listener to listen to the server's operation(when closing the window)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeCurrentBoard();
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
                ((ServerInterfaceImpl) server).newBoard();
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
        //close the current board
        JMenuItem closeItem = new JMenuItem(ServerConstants.MENU_CLOSE);
        closeItem.setBackground(Color.WHITE);
        closeItem.addActionListener(e -> {
            closeCurrentBoard();
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
