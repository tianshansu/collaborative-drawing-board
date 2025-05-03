package common;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class WhiteBoardUIBasic extends JFrame {
    private final DefaultListModel<String> userListModel;


    /**
     * Constructor
     */
    public WhiteBoardUIBasic() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        //create the container panel for the left side (toolbar + canvas)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //create the top panel to store all buttons (relate to drawing)
        JPanel topToolPanel = new JPanel();
        topToolPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topToolPanel.setPreferredSize(new Dimension(900, 70));
        topToolPanel.setBackground(Color.WHITE);
        topToolPanel.setBorder(BorderFactory.createTitledBorder("Toolbar"));

        //add the UI buttons to top tool panel
        //line
        JButton lineButton = createIconButton("/common/assets/line-resized.png");
        topToolPanel.add(lineButton);
        //triangle
        JButton triangleButton = createIconButton("/common/assets/triangle-resized.png");
        topToolPanel.add(triangleButton);
        //oval
        JButton ovalButton = createIconButton("/common/assets/oval-resized.png");
        topToolPanel.add(ovalButton);
        //rect
        JButton rectButton = createIconButton("/common/assets/rect-resized.png");
        topToolPanel.add(rectButton);
        //free draw
        JButton freeDrawButton = createIconButton("/common/assets/free-resized.png");
        topToolPanel.add(freeDrawButton);
        //eraser
        JButton eraserButton = createIconButton("/common/assets/eraser-resized.png");
        topToolPanel.add(eraserButton);

        //colour
        JButton colourButton = new JButton();
        colourButton.setBackground(Color.BLACK);
        colourButton.setPreferredSize(new Dimension(20, 20));
        colourButton.setOpaque(true);
        colourButton.setBorderPainted(false);
        topToolPanel.add(colourButton);

        //size
        JButton sizeButton = new JButton("Size");
        sizeButton.setBorderPainted(false);
        sizeButton.setContentAreaFilled(false);
        sizeButton.setFocusPainted(false);
        sizeButton.setOpaque(false);
        sizeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sizeButton.setContentAreaFilled(true);
                sizeButton.setBackground(new Color(200, 200, 200));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sizeButton.setContentAreaFilled(false);
            }
        });
        topToolPanel.add(sizeButton);


        //add the top tool panel to mainPanel
        mainPanel.add(topToolPanel, BorderLayout.NORTH);


        //create the main drawing canvas panel
        JPanel drawingPanel = new JPanel();
        drawingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        drawingPanel.setPreferredSize(new Dimension(900, 730));
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setBorder(BorderFactory.createTitledBorder("Drawing Canvas"));
        //add the drawing panel to mainPanel
        mainPanel.add(drawingPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.WEST);

        setVisible(true);
    }

    /**
     * Update the user list on UI
     * @param userList new username list
     */
    public void updateUserList(List<String> userList) {
        userListModel.clear();
        for (String name : userList) {
            userListModel.addElement(name);
        }
    }


    public static JButton createIconButton(String resourcePath) {
        ImageIcon img = new ImageIcon(Objects.requireNonNull(WhiteBoardUIBasic.class.getResource(resourcePath)));
        JButton button = new JButton(img);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(35, 35));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setContentAreaFilled(true);
                button.setBackground(new Color(200, 200, 200));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setContentAreaFilled(false);
            }
        });
        return button;
    }

}
