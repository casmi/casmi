/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2012, Xcoo, Inc.
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import casmi.callback.MenuItemSelectCallback;

/**
 * @author T. Takeuchi
 */
public class MenuItem {

    private final MenuItem instance;

    private final JMenuItem jMenuItem;

    private MenuItemSelectCallback callback;

    public MenuItem() {
        instance = this;
        jMenuItem = new JMenuItem();
    }

    public MenuItem(String title) {
        instance = this;
        jMenuItem = new JMenuItem(title);

        jMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ev) {
                if (callback != null) {
                    callback.run(instance);
                }
            }
        });
    }

    public String getTitle() {
        return jMenuItem.getText();
    }

    public void addSelectCallback(MenuItemSelectCallback callback) {
        this.callback = callback;
    }

    final JMenuItem getJMenuItem() {
        return jMenuItem;
    }
}
