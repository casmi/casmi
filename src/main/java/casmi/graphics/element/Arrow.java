/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package casmi.graphics.element;

import casmi.graphics.group.Group;
import casmi.matrix.Vertex;

/**
 * Arrow Class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Arrow extends Group {

    private Triangle triangle;
    private Rect rect;
    private double topSize;
    private double length;
    private double width;
    private ArrowAlign align = ArrowAlign.BOTTOM;
    private Vertex startPoint, endPoint;

    /**
     * Creates a new Arrow object using top size and length.
     *
     * @param topSize The size of a top-triangle.
     * @param length The length of the Arrow.
     */
    public Arrow(double topSize, double length) {
        this.topSize = topSize;
        this.length = length;
        this.width = topSize / 3.0;
        rect = new Rect(this.width, this.length - topSize * Math.cos(Math.PI / 6.0));
        rect.setPosition(0, (length - topSize * Math.cos(Math.PI / 6.0)) / 2.0);

        triangle =
            new Triangle(-topSize / 2, (length - topSize * Math.cos(Math.PI / 6.0)), topSize / 2,
                (length - topSize * Math.cos(Math.PI / 6.0)), 0, length);

        add(rect);
        add(triangle);

        this.setStroke(false);
    }


    /**
     * Creates a new Arrow object using top size, start point and end point.
     *
     * @param topSize The size of a top-triangle.
     * @param start The coordinates of the start point.
     * @param end The coordinates of the end point.
     */
    public Arrow(double topSize, Vertex start, Vertex end) {
        this.topSize = topSize;
        this.length =
            Math.sqrt(Math.pow(end.getY() - start.getY(), 2)
                + Math.pow(end.getX() - start.getX(), 2));
        this.width = topSize / 3.0;
        this.align = ArrowAlign.CORNER;
        rect = new Rect(this.width, this.length - topSize * Math.cos(Math.PI / 6.0));
        rect.setPosition(0, (length - topSize * Math.cos(Math.PI / 6.0)) / 2.0);

        triangle =
            new Triangle(-topSize / 2, (length - topSize * Math.cos(Math.PI / 6.0)), topSize / 2,
                (length - topSize * Math.cos(Math.PI / 6.0)), 0, length);

        add(rect);
        add(triangle);
        this.setPosition(start);
        this.setRotation(-Math.atan2(end.getX() - start.getX(), end.getY() - start.getY()) * 180
            / Math.PI);

        this.startPoint = start;
        this.endPoint = end;

        this.setStroke(false);
    }

    /**
     * Sets the width of this Arrow.
     *
     * @param width The width of the Arc.
     */
    public void setWidth(double width) {
        this.width = width;
        rect.set(this.width, rect.getHeight());
    }

    private void setArrow() {
        switch (align) {
        case TOP:
            rect.set(this.width, this.length - topSize * Math.cos(Math.PI / 6.0));
            rect.setPosition(0, -topSize * Math.cos(Math.PI / 6.0)
                - (length - topSize * Math.cos(Math.PI / 6.0)) / 2.0);
            triangle.set(-topSize / 2, -topSize * Math.cos(Math.PI / 6.0), topSize / 2,
                -topSize * Math.cos(Math.PI / 6.0), 0, 0);
            break;

        case CENTER:
            rect.set(this.width, this.length - topSize * Math.cos(Math.PI / 6.0));
            rect.setPosition(0, -topSize * Math.cos(Math.PI / 6.0) / 2.0);
            triangle.set(-topSize / 2, length / 2 - topSize * Math.cos(Math.PI / 6.0), topSize / 2,
                length / 2 - topSize * Math.cos(Math.PI / 6.0), 0, length / 2);
            break;


        case BOTTOM:
            rect.set(this.width, this.length - topSize * Math.cos(Math.PI / 6.0));
            rect.setPosition(0, (length - topSize * Math.cos(Math.PI / 6.0)) / 2.0);
            triangle.set(-topSize / 2, (length - topSize * Math.cos(Math.PI / 6.0)), topSize / 2,
                (length - topSize * Math.cos(Math.PI / 6.0)), 0, length);
            break;

        case CORNER:
            this.length =
                Math.sqrt(Math.pow(endPoint.getY() - startPoint.getY(), 2)
                    + Math.pow(endPoint.getX() - startPoint.getX(), 2));
            rect.set(this.width, this.length - topSize * Math.cos(Math.PI / 6.0));
            rect.setPosition(0, (length - topSize * Math.cos(Math.PI / 6.0)) / 2.0);
            triangle.set(-topSize / 2, (length - topSize * Math.cos(Math.PI / 6.0)), topSize / 2,
                (length - topSize * Math.cos(Math.PI / 6.0)), 0, length);
            this.setPosition(startPoint);
            this.setRotation(-Math.atan2(endPoint.getX() - startPoint.getX(), endPoint.getY()
                - startPoint.getY()) * 180 / Math.PI);
            break;
        }
    }

    /**
     * Gets the length of this Arrow.
     *
     * @return The length of the Arc.
     */
    public double getLength(double length) {
        return this.length;
    }

    /**
     * Sets the length of this Arrow.
     *
     * @param length The length of the Arc.
     */
    public void setLength(double length) {
        this.length = length;
        setArrow();
    }

    /**
     * Gets the size of top triangle of this Arrow.
     *
     * @return The width of the Arc.
     */
    public double getTopSize() {
        return this.topSize;
    }

    /**
     * Sets the size of top triangle of this Arrow.
     *
     * @param size The width of the Arc.
     */
    public void setTopSize(double size) {
        this.topSize = size;
        setArrow();
    }

    /**
     * Gets the positional Alignment of this Arrow.
     *
     * @return The ArrowAlign of the Arc.
     *
     * @see casmi.graphics.element.ArrowAlign
     */
    public ArrowAlign getAlign() {
        return this.align;
    }

    /**
     * Aligns the position of this Arrow.
     *
     * @param align The ArrowAlign of the Arc.
     *
     * @see casmi.graphics.element.ArrowAlign
     */
    public void setAlign(ArrowAlign align) {
        this.align = align;
        setArrow();
    }

    /**
     * Sets the position of corners of this Arrow.
     *
     * @param start The coordinates of the start point.
     * @param end The coordinates of the end point.
     *
     */
    public void setCorner(Vertex start, Vertex end) {
        this.startPoint = start;
        this.endPoint = end;
        this.align = ArrowAlign.CORNER;
        setArrow();
    }

    /**
     * Sets the coordinates of the start point of this Arrow.
     *
     * @param x The x-coordinate of the start point.
     * @param y The y-coordinate of the start point.
     *
     */
    public void setStartCorner(double x, double y) {
        this.startPoint.set(x, y);
        this.align = ArrowAlign.CORNER;
        setArrow();
    }

    /**
     * Sets the coordinates of the end point of this Arrow.
     *
     * @param x The x-coordinate of the end point.
     * @param y The y-coordinate of the end point.
     *
     */
    public void setEndCorner(double x, double y) {
        this.endPoint.set(x, y);
        this.align = ArrowAlign.CORNER;
        setArrow();
    }

    /**
     * Gets the coordinates of the start point of this Arrow.
     *
     * @return The coordinate of the start point.
     */
    public Vertex getStartCorner() {
        return startPoint;
    }

    /**
     * Gets the coordinates of the end point of this Arrow.
     *
     * @return The coordinate of the end point.
     */
    public Vertex getEndCorner() {
        return endPoint;
    }

    @Override
    public void update() {}
}
