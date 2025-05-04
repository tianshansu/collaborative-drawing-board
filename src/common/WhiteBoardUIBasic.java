package common;

import common.interfaces.ServerInterface;
import enums.Shape;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhiteBoardUIBasic extends JFrame {
    private final DefaultListModel<String> userListModel;
    private Point startPt = null;
    private Point endPt = null;
    private Shape currentShape = null;
    private ServerInterface serverInterface = null;
    private List<ShapesDrawn> allShapes = new ArrayList<>();

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
        lineButton.addActionListener(e -> {
            currentShape = Shape.LINE;
        });
        topToolPanel.add(lineButton);

        //triangle
        JButton triangleButton = createIconButton("/common/assets/triangle-resized.png");
        triangleButton.addActionListener(e -> {
            currentShape = Shape.TRIANGLE;
        });
        topToolPanel.add(triangleButton);

        //oval
        JButton ovalButton = createIconButton("/common/assets/oval-resized.png");
        ovalButton.addActionListener(e -> {
            currentShape = Shape.OVAL;
        });
        topToolPanel.add(ovalButton);

        //rect
        JButton rectButton = createIconButton("/common/assets/rect-resized.png");
        rectButton.addActionListener(e -> {
            currentShape = Shape.RECTANGLE;
        });
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
        JPanel drawingPanel = new JPanel() {
            {
                addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        startPt = e.getPoint(); //record the location of start pt
                    }

                    public void mouseReleased(MouseEvent e) {
                        endPt = e.getPoint(); //record the location of end pt
                        repaint();
                        System.out.println(serverInterface);
                        if (startPt != null && endPt != null && currentShape != null && serverInterface!=null) {
                            try {
                                serverInterface.drawNewShape(new ShapesDrawn(currentShape, startPt, endPt)); //add this new line to the list
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2D = (Graphics2D) g;

                //set pen size
                graphics2D.setStroke(new BasicStroke(3));
                //set pen colour
                graphics2D.setColor(Color.BLACK);

                //draw all existing shapes
                for (ShapesDrawn shapesDrawn : allShapes) {
                    switch (shapesDrawn.getShape()) {
                        case LINE:
                            graphics2D.drawLine(shapesDrawn.getStartPt().x,
                                    shapesDrawn.getStartPt().y,
                                    shapesDrawn.getEndPt().x,
                                    shapesDrawn.getEndPt().y); // draw line
                            break;
                    }
                }

                //draw the new shapes
                if (currentShape != null) {
                    switch (currentShape) {
                        case LINE:
                            graphics2D.drawLine(startPt.x, startPt.y, endPt.x, endPt.y); // draw line
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        drawingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        drawingPanel.setPreferredSize(new Dimension(900, 730));
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setBorder(BorderFactory.createTitledBorder("Drawing Canvas"));

        //add the drawing panel to mainPanel
        mainPanel.add(drawingPanel, BorderLayout.CENTER);

        //add the main panel to screen
        add(mainPanel, BorderLayout.WEST);

        setVisible(true);
    }

    public void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
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

    public void updateCanvas(List<ShapesDrawn> drawnList) {
        allShapes.clear();
        allShapes.addAll(drawnList);
        repaint();
    }

    private static JButton createIconButton(String resourcePath) {
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


