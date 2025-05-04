package common;

import enums.Shape;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class ShapesDrawn implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Shape shape;
    private final Point startPt;
    private final Point endPt;

    /**
     * Constructor
     * @param shape the shape
     * @param startPt startPt
     * @param endPt endPt
     */
    public ShapesDrawn(Shape shape, Point startPt, Point endPt) {
        this.shape = shape;
        this.startPt = startPt;
        this.endPt = endPt;
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
}
