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

package casmi.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import casmi.Applet;

/**
 * Class for creating popup menu easily.
 *
 * @author S. Yoshida
 */
public class PopupMenu extends JPopupMenu {

    private final Applet target;

    public PopupMenu(Applet target) {
        this.target = target;
    }

    public void show(MouseEvent event) {
        show(event.getComponent(), event.getX(), event.getY());
    }

    public void show(int x, int y) {
        show(target, x, y);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void show() {
        show(target, target.getMouseX(), target.getHeight() - target.getMouseY());
    }

    public void addMenuItem(final String buttonName, final String methodName, final Object... args) {

        JMenuItem menuItem = new JMenuItem(buttonName);

        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                Method method;
                try {
                    int length = args.length;
                    Class<?>[] c = new Class[length];
                    for (int i = 0; i < length; i++) {
                        c[i] = args[i].getClass();
                    }
                    method = target.getClass().getMethod(methodName, c);
                } catch (SecurityException exception) {
                    throw new RuntimeException(exception);
                } catch (NoSuchMethodException exception) {
                    throw new RuntimeException(exception);
                }
                try {
                    method.invoke(target, args);
                } catch (IllegalArgumentException exception) {
                    throw new RuntimeException(exception);
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(exception);
                } catch (InvocationTargetException exception) {
                    throw new RuntimeException(exception);
                }
            }
        });

        add(menuItem);
    }
}
