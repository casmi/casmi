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

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import casmi.Applet;
import casmi.Mouse;

/**
 * Class for creating popup menu easily.
 *
 * @author S. Yoshida
 */
public class PopupMenu extends JPopupMenu {

    public void show(Applet target) {
        Mouse m = target.getMouse();
        show(target.getWindowFrame(), m.getX(), target.getHeight() - m.getY());
    }

    public void addMenuItem(final String buttonName, final PopupMenuActionListener listener) {
        JMenuItem menuItem = new JMenuItem(buttonName);

        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                if (listener != null) {
                    listener.performed();
                }
            }
        });

        add(menuItem);
    }
}
