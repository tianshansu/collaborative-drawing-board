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

    /**
     * Constructor
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

    }

    public ShapesDrawn(Shape shape,List<Point> freeDrawPts,Color color, int penSize) {
        this.shape = shape;
        this.points = freeDrawPts;
        this.startPt = null;
        this.endPt = null;
        this.color = color;
        this.penSize = penSize;
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
        return new ArrayList<>(points);
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
}
