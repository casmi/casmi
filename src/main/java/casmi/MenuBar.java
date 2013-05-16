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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuBar;

/**
 * @author T. Takeuchi
 */
public class MenuBar {

    private final JMenuBar jMenuBar;
    private List<Menu> menuList = new ArrayList<Menu>();

    MenuBar () {
        jMenuBar = new JMenuBar();
    }

    public void addMenu(Menu menu) {
        menuList.add(menu);
        jMenuBar.add(menu.getJMenu());
    }

    public Menu getMenu(int index) {
        return menuList.get(index);
    }

    public int getMenuCount() {
        return jMenuBar.getMenuCount();
    }

    public boolean isSelected() {
        return jMenuBar.isSelected();
    }

    final JMenuBar getJMenuBar() {
        return jMenuBar;
    }
}
