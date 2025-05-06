package common;

import common.interfaces.ServerInterface;
import enums.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhiteBoardUIBasic extends JFrame {
    private final DefaultListModel<String> userListModel;
    private Point startPt = null;
    private Point endPt = null;
    private Shape currentShape = null;
    private Color currentColor = Color.black;
    private int penSize = 3;
    private ServerInterface serverInterface = null;
    private List<ShapesDrawn> allShapes = new ArrayList<>();
    List<Point> freeDrawPoints = new ArrayList<>();

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
        freeDrawButton.addActionListener(e -> {
            currentShape = Shape.FREE_DRAW;
        });
        topToolPanel.add(freeDrawButton);

        //eraser
        JButton eraserButton = createIconButton("/common/assets/eraser-resized.png");
        eraserButton.addActionListener(e -> {
            currentShape = Shape.FREE_DRAW;
            currentColor=Color.WHITE;
        });
        topToolPanel.add(eraserButton);

        //colour
        JButton colourButton = new JButton();
        colourButton.setBackground(Color.BLACK);
        colourButton.setPreferredSize(new Dimension(20, 20));
        colourButton.setOpaque(true);
        colourButton.setBorderPainted(false);
        colourButton.addActionListener(e -> {
            currentColor= JColorChooser.showDialog(null, "Choose a color", currentColor);
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
        JPanel drawingPanel = new JPanel() {
            {
                addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (currentShape == Shape.FREE_DRAW) {
                            freeDrawPoints.clear();
                            freeDrawPoints.add(e.getPoint());
                            repaint();
                            return;
                        }

                        startPt = e.getPoint(); //record the location of start pt

                    }

                    public void mouseReleased(MouseEvent e) {
                        if (currentShape == Shape.FREE_DRAW) {
                            freeDrawPoints.add(e.getPoint());
                            try {
                                //add the free draw line to list in server
                                ShapesDrawn shape = new ShapesDrawn(currentShape, new ArrayList<>(freeDrawPoints),currentColor,penSize);
                                serverInterface.drawNewShape(shape);
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                            repaint();
                            return;
                        }

                        endPt = e.getPoint(); //record the location of end pt

                        repaint();
                        System.out.println(serverInterface);
                        if (startPt != null && endPt != null && currentShape != null && serverInterface != null) {
                            try {
                                serverInterface.drawNewShape(new ShapesDrawn(currentShape, startPt, endPt,currentColor,penSize)); //add this new line to the list
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
                            freeDraw(graphics2D,shapesDrawn.getPoints());//free draw lines
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
                            drawLine(graphics2D, startPt, endPt); // draw line
                            break;
                        case TRIANGLE:
                            drawTriangle(graphics2D, startPt, endPt);//draw triangle
                            break;
                        case OVAL:
                            drawOval(graphics2D, startPt, endPt);//draw oval
                            break;
                        case RECTANGLE:
                            drawRect(graphics2D, startPt, endPt);//draw rectangle
                            break;
                        case FREE_DRAW:
                            freeDraw(graphics2D,freeDrawPoints);//free draw
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
     *
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

    private void freeDraw(Graphics2D graphics2D,List<Point> freeDrawPoints) {
        for (int i = 1; i < freeDrawPoints.size(); i++) {
            Point p1 = freeDrawPoints.get(i - 1);
            Point p2 = freeDrawPoints.get(i);
            graphics2D.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
}


