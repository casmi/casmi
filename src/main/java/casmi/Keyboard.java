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
 * Keyboard class.
 *
 * @author T. Takeuchi
 */
public class Keyboard {

    private char key;

    private int keyCode;

    private boolean pressed = false;
    private boolean released = false;
    private boolean typed = false;

    Keyboard() {}

    public char getKey() {
        return key;
    }

    void setKey(char key) {
        this.key = key;
    }

    public int getKeyCode() {
        return keyCode;
    }

    void setKeyCode(int keycode) {
        this.keyCode = keycode;
    }

    public boolean isPressed() {
        return pressed;
    }

    void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isReleased() {
        return released;
    }

    void setReleased(boolean released) {
        this.released = released;
    }

    public boolean isTyped() {
        return typed;
    }

    void setTyped(boolean typed) {
        this.typed = typed;
    }

}
