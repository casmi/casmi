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

package casmi;

/**
 * Mouse class.
 *
 * @author T. Takeuchi
 */
public class Mouse {

    private int x = 0;
    private int y = 0;

    private int prevX = 0;
    private int prevY = 0;

    private boolean leftButtonPressed = false;
    private boolean middleButtonPressed = false;
    private boolean rightButtonPressed = false;

    private boolean pressed = false;
    private boolean clicked = false;
    private boolean doubleClicked = false;
    private boolean entered = false;
    private boolean exited = false;
    private boolean released = false;
    private boolean dragged = false;
    private boolean moved = false;

    private int wheelRotation = 0;

    private long mouseClickLeftTime = 0;
    private long mouseClickRightTime = 0;
    private long mouseClickMiddleTime = 0;

    Mouse() {}

    public int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    void setY(int y) {
        this.y = y;
    }

    public int getPrevX() {
        return prevX;
    }

    void setPrevX(int prevX) {
        this.prevX = prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    void setPrevY(int prevY) {
        this.prevY = prevY;
    }

    @Deprecated
    public int getPrvX() {
        return prevX;
    }

    @Deprecated
    public int getPrvY() {
        return prevY;
    }

    public boolean isButtonPressed(MouseButton button) {
        switch (button) {
        case LEFT:
            return leftButtonPressed;
        case MIDDLE:
            return middleButtonPressed;
        case RIGHT:
            return rightButtonPressed;
        default:
            break;
        }
        return false;
    }

    void setButtonPressed(MouseButton button, boolean pressed) {
        switch (button) {
        case LEFT:
            leftButtonPressed = pressed;
            break;
        case MIDDLE:
            middleButtonPressed = pressed;
            break;
        case RIGHT:
            rightButtonPressed = pressed;
            break;
        default:
            break;
        }
    }

    public boolean isPressed() {
        return pressed;
    }

    void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isClicked() {
        return clicked;
    }

    void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isEntered() {
        return entered;
    }

    void setEntered(boolean entered) {
        this.entered = entered;
    }

    public boolean isExited() {
        return exited;
    }

    void setExited(boolean exited) {
        this.exited = exited;
    }

    public boolean isReleased() {
        return released;
    }

    void setReleased(boolean released) {
        this.released = released;
    }

    boolean isDragged() {
        return dragged;
    }

    void setDragged(boolean dragged) {
        this.dragged = dragged;
    }

    boolean isMoved() {
        return moved;
    }

    void setMoved(boolean moved) {
        this.moved = moved;
    }

    public int getWheelRotation() {
        return wheelRotation;
    }

    void setWheelRotation(int wheelRotation) {
        this.wheelRotation = wheelRotation;
    }

    public boolean isDoubleClicked() {
        return doubleClicked;
    }

    public void setDoubleClicked(boolean doubleClicked) {
        this.doubleClicked = doubleClicked;
    }

    public long getMouseClickLeftTime() {
        return mouseClickLeftTime;
    }

    public void setMouseClickLeftTime(long mouseClickTime) {
        this.mouseClickLeftTime = mouseClickTime;
    }

    public long getMouseClickRightTime() {
        return mouseClickRightTime;
    }

    public void setMouseClickRightTime(long mouseClickRightTime) {
        this.mouseClickRightTime = mouseClickRightTime;
    }

    public long getMouseClickMiddleTime() {
        return mouseClickMiddleTime;
    }

    public void setMouseClickMiddleTime(long mouseClickMiddleTime) {
        this.mouseClickMiddleTime = mouseClickMiddleTime;
    }
}
