package common;

import common.interfaces.ServerInterface;
import enums.Shape;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhiteBoardUIBasic extends JFrame {
    private DefaultListModel<String> userListModel;
    private Point startPt;
    private Point endPt;
    private Shape currentShape;
    private Color currentColor = Color.BLACK;
    private Color colourBeforeEraser = Color.BLACK;
    private Font currentFont;
    private int penSize = 3;
    private ServerInterface serverInterface;
    private List<ShapesDrawn> allShapes = new ArrayList<>();
    private List<Point> freeDrawPoints = new ArrayList<>();
    private JPanel drawingPanel;
    private JPanel msgPanel;
    private String currentUserName;

    /**
     * Constructor
     */
    public WhiteBoardUIBasic() {
        setSize(1200, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //initialise the right panel (user list + chat)
        initialiseRightPanel();
        //initialise the main panel (top toolbar + drawing canvas)
        initialiseMainPanel();
        setVisible(true);
    }

    /**
     * Get drawing panel
     * @return
     */
    protected JPanel getDrawingPanel() {
        return drawingPanel;
    }

    /**
     * set server interface
     * @param serverInterface serverInterface
     */
    public void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    /**
     * set current user's usrename - to pass it to server during chatting
     * @param currentUserName currentUserName
     */
    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
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

    /**
     * update the drawing canvas
     * @param drawnList the shapes drawing history list
     */
    public void updateCanvas(List<ShapesDrawn> drawnList) {
        allShapes.clear();
        allShapes.addAll(drawnList);
        startPt = null;
        endPt = null;
        freeDrawPoints.clear();
        repaint();
    }

    public void addNewChatMsg(String username, String chatMsg) {
        JLabel msgLabel = new JLabel( "<html><body style='width:220px;'>" + chatMsg + "</body></html>");//auto change line
        msgLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel msgContainer = new JPanel();
        msgContainer.setBackground(Color.WHITE);
        msgContainer.setLayout(new BoxLayout(msgContainer, BoxLayout.Y_AXIS));
        msgContainer.add(usernameLabel);
        msgContainer.add(msgLabel);
        msgPanel.add(msgContainer);
        msgContainer.add(Box.createVerticalStrut(20)); //add the gap under each msg container
        msgPanel.revalidate();
        msgPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) msgPanel.getParent().getParent();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });

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

    private void drawLine(Graphics2D graphics2D, Point startPt, Point endPt) {
        graphics2D.drawLine(startPt.x, startPt.y, endPt.x, endPt.y);
    }

    private void drawTriangle(Graphics2D graphics2D, Point startPt, Point endPt) {
        Point p1 = new Point((startPt.x + endPt.x) / 2, startPt.y);
        Point p2 = new Point(startPt.x, endPt.y);
        Point p3 = new Point(endPt.x, endPt.y);
        int[] xPoints = {p1.x, p2.x, p3.x};
        int[] yPoints = {p1.y, p2.y, p3.y};
        graphics2D.drawPolygon(xPoints, yPoints, 3);
    }

    private void drawOval(Graphics2D graphics2D, Point startPt, Point endPt) {
        int x = Math.min(startPt.x, endPt.x);
        int y = Math.min(startPt.y, endPt.y);
        int width = Math.abs(endPt.x - startPt.x);
        int height = Math.abs(endPt.y - startPt.y);
        graphics2D.drawOval(x, y, width, height);
    }

    private void drawRect(Graphics2D graphics2D, Point startPt, Point endPt) {
        int x = Math.min(startPt.x, endPt.x);
        int y = Math.min(startPt.y, endPt.y);
        int width = Math.abs(endPt.x - startPt.x);
        int height = Math.abs(endPt.y - startPt.y);

        graphics2D.drawRect(x, y, width, height);
    }

    private void freeDraw(Graphics2D graphics2D, List<Point> freeDrawPoints) {
        for (int i = 1; i < freeDrawPoints.size(); i++) {
            Point p1 = freeDrawPoints.get(i - 1);
            Point p2 = freeDrawPoints.get(i);
            graphics2D.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    private void initialiseMainPanel() {
        //create the container panel for the left side (toolbar + canvas)
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(887, 70));
        mainPanel.setLayout(new BorderLayout());

        //create the top panel to store all buttons (relate to drawing)
        JPanel topToolPanel = new JPanel();
        topToolPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topToolPanel.setPreferredSize(new Dimension(900, 70));
        topToolPanel.setBackground(Color.WHITE);
        topToolPanel.setBorder(createBorderWithTitle(Color.BLACK,"Toolbar"));

        //add the UI buttons to top tool panel
        //line
        JButton lineButton = createIconButton("/common/assets/line-resized.png");
        lineButton.addActionListener(e -> {
            currentShape = Shape.LINE;
            currentColor = colourBeforeEraser; //change the colour back to the previous colour
        });
        topToolPanel.add(lineButton);

        //triangle
        JButton triangleButton = createIconButton("/common/assets/triangle-resized.png");
        triangleButton.addActionListener(e -> {
            currentShape = Shape.TRIANGLE;
            currentColor = colourBeforeEraser;
        });
        topToolPanel.add(triangleButton);

        //oval
        JButton ovalButton = createIconButton("/common/assets/oval-resized.png");
        ovalButton.addActionListener(e -> {
            currentShape = Shape.OVAL;
            currentColor = colourBeforeEraser;
        });
        topToolPanel.add(ovalButton);

        //rect
        JButton rectButton = createIconButton("/common/assets/rect-resized.png");
        rectButton.addActionListener(e -> {
            currentShape = Shape.RECTANGLE;
            currentColor = colourBeforeEraser;
        });
        topToolPanel.add(rectButton);

        //free draw
        JButton freeDrawButton = createIconButton("/common/assets/free-resized.png");
        freeDrawButton.addActionListener(e -> {
            currentShape = Shape.FREE_DRAW;
            currentColor = colourBeforeEraser;
        });
        topToolPanel.add(freeDrawButton);

        //text input
        JButton textButton = createIconButton("/common/assets/text-resized.png");
        textButton.addActionListener(e -> {
            currentShape = Shape.TEXT;
            currentColor = colourBeforeEraser;
        });
        topToolPanel.add(textButton);

        //eraser
        JButton eraserButton = createIconButton("/common/assets/eraser-resized.png");
        eraserButton.addActionListener(e -> {
            currentShape = Shape.FREE_DRAW;
            currentColor = Color.WHITE;
        });
        topToolPanel.add(eraserButton);

        //colour
        JButton colourButton = new JButton();
        colourButton.setBackground(Color.BLACK);
        colourButton.setPreferredSize(new Dimension(20, 20));
        colourButton.setOpaque(true);
        colourButton.setBorderPainted(false);
        colourButton.addActionListener(e -> {
            currentColor = JColorChooser.showDialog(null, "Choose a color", currentColor);
            colourBeforeEraser = currentColor;
            colourButton.setBackground(currentColor);
        });
        topToolPanel.add(colourButton);

        // pen size
        JLabel sizeTextField = new JLabel("Size");
        sizeTextField.setPreferredSize(new Dimension(30, 30));
        sizeTextField.setBackground(Color.WHITE);
        sizeTextField.setBorder(null);
        topToolPanel.add(sizeTextField);


        //pen size slider
        JSlider penSizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, penSize);
        penSizeSlider.setPreferredSize(new Dimension(150, 40));
        penSizeSlider.setBackground(Color.WHITE);
        penSizeSlider.addChangeListener(e -> {
            penSize = penSizeSlider.getValue();
        });
        topToolPanel.add(penSizeSlider);


        //add the top tool panel to mainPanel
        mainPanel.add(topToolPanel, BorderLayout.NORTH);


        //create the main drawing canvas panel
        drawingPanel = new JPanel() {
            {
                addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        // free draw
                        if (currentShape == Shape.FREE_DRAW) {
                            freeDrawPoints.clear();
                            freeDrawPoints.add(e.getPoint());
                            repaint();
                            return;
                        }

                        //input text
                        if (currentShape == Shape.TEXT) {
                            Point clickPoint = e.getPoint();//get the text location
                            drawingPanel.setLayout(null);
                            JTextField textField = new JTextField();
                            textField.setOpaque(false);
                            textField.setBorder(null);
                            textField.setBounds(clickPoint.x, clickPoint.y, 300, 60);
                            currentFont=new Font("Arial", Font.PLAIN, penSize + 20);
                            textField.setFont(currentFont);
                            textField.setForeground(currentColor); //set text colour

                            textField.addFocusListener(new FocusAdapter() {
                                //remove the text field once the user clicks somewhere else
                                @Override
                                public void focusLost(FocusEvent e) {
                                    //add this text in the list in server
                                    try {
                                        ShapesDrawn shape=new ShapesDrawn(currentShape, clickPoint, currentColor, textField.getText(),currentFont);
                                        allShapes.add(shape);
                                        serverInterface.drawNewShape(shape);
                                        textField.setText("");
                                    } catch (RemoteException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    drawingPanel.remove(textField);

                                    drawingPanel.revalidate();
                                    drawingPanel.repaint();
                                }
                            });
                            drawingPanel.add(textField);
                            drawingPanel.revalidate();
                            drawingPanel.repaint();
                        }

                        startPt = e.getPoint(); //record the location of start pt

                    }


                    public void mouseReleased(MouseEvent e) {
                        if (currentShape == Shape.FREE_DRAW) {
                            freeDrawPoints.add(e.getPoint());
                            try {
                                //add the free draw line to list in server
                                ShapesDrawn shape = new ShapesDrawn(currentShape, new ArrayList<>(freeDrawPoints), currentColor, penSize);
                                serverInterface.drawNewShape(shape);
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                            repaint();
                            return;
                        }

                        endPt = e.getPoint(); //record the location of end pt

                        repaint();
                        if (startPt != null && endPt != null && currentShape != null && serverInterface != null) {
                            try {
                                ShapesDrawn shape =new ShapesDrawn(currentShape, startPt, endPt, currentColor, penSize);
                                serverInterface.drawNewShape(shape); //add this new line to the list
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });

                addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (currentShape == Shape.FREE_DRAW) {
                            freeDrawPoints.add(e.getPoint());
                            repaint();
                        }
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2D = (Graphics2D) g;

                //draw all existing shapes
                for (ShapesDrawn shapesDrawn : allShapes) {
                    graphics2D.setStroke(new BasicStroke(shapesDrawn.getPenSize()));
                    graphics2D.setColor(shapesDrawn.getColor());
                    switch (shapesDrawn.getShape()) {
                        case LINE:
                            drawLine(graphics2D, shapesDrawn.getStartPt(), shapesDrawn.getEndPt());// draw line
                            break;
                        case TRIANGLE:
                            drawTriangle(graphics2D, shapesDrawn.getStartPt(), shapesDrawn.getEndPt());//draw triangle
                            break;
                        case OVAL:
                            drawOval(graphics2D, shapesDrawn.getStartPt(), shapesDrawn.getEndPt());//draw oval
                            break;
                        case RECTANGLE:
                            drawRect(graphics2D, shapesDrawn.getStartPt(), shapesDrawn.getEndPt());//draw rectangle
                            break;
                        case FREE_DRAW:
                            freeDraw(graphics2D, shapesDrawn.getPoints());//free draw lines
                            break;
                        case TEXT:
                            graphics2D.setColor(shapesDrawn.getColor());
                            graphics2D.setFont(shapesDrawn.getFont());
                            graphics2D.drawString(shapesDrawn.getText(), shapesDrawn.getStartPt().x, shapesDrawn.getStartPt().y+40);
                            break;
                        default:
                            break;
                    }
                }

                //draw the new shapes
                if (currentShape != null) {
                    graphics2D.setStroke(new BasicStroke(penSize));
                    graphics2D.setColor(currentColor);
                    switch (currentShape) {
                        case LINE:
                            if (startPt != null && endPt != null) {
                                drawLine(graphics2D, startPt, endPt); // draw line
                            }
                            break;
                        case TRIANGLE:
                            if (startPt != null && endPt != null) {
                                drawTriangle(graphics2D, startPt, endPt);//draw triangle
                            }
                            break;
                        case OVAL:
                            if (startPt != null && endPt != null) {
                                drawOval(graphics2D, startPt, endPt);//draw oval
                            }
                            break;
                        case RECTANGLE:
                            if (startPt != null && endPt != null) {
                                drawRect(graphics2D, startPt, endPt);//draw rectangle
                            }
                            break;
                        case FREE_DRAW:
                            if (freeDrawPoints.size() > 1) {
                                freeDraw(graphics2D, freeDrawPoints);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        drawingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        drawingPanel.setPreferredSize(new Dimension(800, 730));
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setBorder(createBorderWithTitle(Color.BLACK,"Drawing Canvas"));

        //add the drawing panel to mainPanel
        mainPanel.add(drawingPanel, BorderLayout.CENTER);

        //add the main panel to screen
        add(mainPanel, BorderLayout.WEST);
    }

    private void initialiseRightPanel() {
        //create the right panel to store user list and chat panels
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 800));

        //user list panel
        JPanel userListPanel = new JPanel();
        userListPanel.setBackground(Color.WHITE);
        userListPanel.setPreferredSize(new Dimension(300, 200));
        userListPanel.setBorder(createBorderWithTitle(Color.BLACK,"User List"));
        rightPanel.add(userListPanel);

        //add user list component
        userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(290, 160));
        userListPanel.add(scrollPane, BorderLayout.CENTER);

        //chat panel
        JPanel chatPanel = new JPanel();
        chatPanel.setBackground(Color.WHITE);
        chatPanel.setPreferredSize(new Dimension(300, 600));
        chatPanel.setBorder(createBorderWithTitle(Color.BLACK,"Chat"));
        chatPanel.setLayout(new BorderLayout());
        //msg panel
        msgPanel = new JPanel();
        msgPanel.setBackground(Color.WHITE);
        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
        JScrollPane msgScrollPane = new JScrollPane(msgPanel);
        msgScrollPane.setBorder(null);
        chatPanel.add(msgScrollPane, BorderLayout.CENTER);

        //input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setPreferredSize(new Dimension(300, 50));
        //inputPanel.setBorder(BorderFactory.createTitledBorder(""));
        inputPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        inputPanel.setLayout(new BorderLayout());
        //input box
        JTextArea inputArea = new JTextArea();
        inputArea.setPreferredSize(new Dimension(280, 50));
        inputArea.setBackground(Color.WHITE);
        //inputArea.setBorder(BorderFactory.createTitledBorder(""));
        inputArea.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputPanel.add(inputArea, BorderLayout.CENTER);
        //send msg button
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            if (!inputArea.getText().isEmpty()) {
                //send the msg to server and broadcast
                try {
                    serverInterface.sendNewChatMsg(currentUserName,inputArea.getText());
                    inputArea.setText("");//clear the input box
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);
        rightPanel.add(chatPanel);

        //add the right panel to jFrame
        add(rightPanel, BorderLayout.EAST);

    }

    private TitledBorder createBorderWithTitle(Color borderColor,String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                new LineBorder(borderColor), title
        );
        return border;
    }
}


