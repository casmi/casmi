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
  
package casmi.util;

import org.junit.Test;

/**
 * @author T. Takeuchi
 */
public class SystemUtilTest {

    @Test
    public void testGetOS() {
        String out = "";
        OS os = SystemUtil.getOS();

        switch (os) {
        case MAC:
            out = "Mac";
            break;
        case MAC_64:
            out = "Mac_64";
            break;
        case WIN:
            out = "Windows";
            break;
        case WIN_64:
            out = "Windows_64";
            break;
        case LINUX:
            out = "Linux";
            break;
        case LINUX_64:
            out = "Linux_64";
            break;
        default:
            out = "Other";
            break;
        }

        System.out.println(out);
    }

}