package common;

import enums.Shape;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShapesDrawn implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Shape shape;
    private final Point startPt;
    private final Point endPt;
    private final List<Point> points;
    private final Color color;
    private final int penSize;
    private final String text;
    private final Font font;

    /**
     * Constructor1: draw regular shapes
     * @param shape the shape
     * @param startPt startPt
     * @param endPt endPt
     */
    public ShapesDrawn(Shape shape, Point startPt, Point endPt,Color color, int penSize) {
        this.shape = shape;
        this.startPt = startPt;
        this.endPt = endPt;
        this.points = null;
        this.color = color;
        this.penSize = penSize;
        this.text = "";
        this.font = null;

    }

    /**
     * Constructor2: free draw
     * @param shape the shape
     * @param freeDrawPts point list
     * @param color colour
     * @param penSize pen size
     */
    public ShapesDrawn(Shape shape,List<Point> freeDrawPts,Color color, int penSize) {
        this.shape = shape;
        this.points = freeDrawPts;
        this.startPt = null;
        this.endPt = null;
        this.color = color;
        this.penSize = penSize;
        this.text = "";
        this.font = null;
    }

    /**
     * Constructor3: texts
     * @param shape the shape
     * @param startPt text location
     * @param color colour
     * @param text actual text
     */
    public ShapesDrawn(Shape shape, Point startPt, Color color, String text,Font font) {
        this.shape = shape;
        this.startPt = startPt;
        this.color = color;
        this.text = text;
        this.points = null;
        this.penSize = 0;
        this.endPt = null;
        this.font = font;
    }

    /**
     * Get shape
     * @return shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Get start pt
     * @return start pt
     */
    public Point getStartPt() {
        return startPt;
    }

    /**
     * Get end pt
     * @return end pt
     */
    public Point getEndPt() {
        return endPt;
    }

    /**
     * Get free draw pts list
     * @return the pts
     */
    public List<Point> getPoints() {
        if(points !=null){
            return new ArrayList<>(points);
        }
        return new ArrayList<>();

    }

    /**
     * Get colour
     * @return the colour
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get pen size
     * @return the pen size
     */
    public int getPenSize() {
        return penSize;
    }

    /**
     * Get text
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Get font
     * @return the font
     */
    public Font getFont() {
        return font;
    }
}
