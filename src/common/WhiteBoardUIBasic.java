package common;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WhiteBoardUIBasic extends JFrame {
    private final DefaultListModel<String> userListModel;


    /**
     * Constructor
     */
    public WhiteBoardUIBasic() {
        setTitle("Shared Whiteboard - Manager");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // TODO: add other components

        //create the right panel to store user list and chat panels
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 800));


        //user list panel
        JPanel userListPanel = new JPanel();
        userListPanel.setBackground(Color.WHITE);
        userListPanel.setPreferredSize(new Dimension(300, 200));
        userListPanel.setBorder(BorderFactory.createTitledBorder("User List"));
        rightPanel.add(userListPanel);
        //add user list component
        userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        userListPanel.add(scrollPane, BorderLayout.CENTER);



        //chat panel
        JPanel chatPanel = new JPanel();
        chatPanel.setBackground(Color.WHITE);
        chatPanel.setPreferredSize(new Dimension(300, 600));
        chatPanel.setBorder(BorderFactory.createTitledBorder("Chat"));
        rightPanel.add(chatPanel);

        //add the right panel to jFrame
        add(rightPanel, BorderLayout.EAST);



        setVisible(true);
    }

    public void addUser(List<String> userList) {
        userListModel.clear();
        for (String name : userList) {
            userListModel.addElement(name);
        }
    }
}
