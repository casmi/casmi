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

import javax.swing.JMenu;

/**
 * @author T. Takeuchi
 */
public class Menu {

    private final JMenu jMenu;

    public Menu() {
        jMenu = new JMenu();
    }

    public Menu(String title) {
        jMenu = new JMenu(title);
    }

    public MenuItem addMenuItem(MenuItem item) {
        jMenu.add(item.getJMenuItem());
        return item;
    }

    public MenuItem addMenuItem(String title) {
        MenuItem item = new MenuItem(title);
        return addMenuItem(item);
    }

    public void addSeparator() {
        jMenu.addSeparator();
    }

    public void inserSeparator(int index) {
        jMenu.insertSeparator(index);
    }

    final JMenu getJMenu() {
        return jMenu;
    }
}
