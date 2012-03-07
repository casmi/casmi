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
public class SystemPropertyTest {

    @Test
    public void allPropertiesTest() {
        for (String name : SystemUtil.getAllProperties()) {
            System.out.println(name);
        }
    }

    @Test
    public void getPropertyTest() {

        System.out.println("-- Java -----------");
        System.out.println(SystemUtil.JAVA_VENDOR);
        System.out.println(SystemUtil.JAVA_VENDOR_URL);
        System.out.println(SystemUtil.JAVA_VERSION);
        System.out.println(SystemUtil.JAVA_COMPILER);
        System.out.println(SystemUtil.JAVA_HOME);
        System.out.println(SystemUtil.JAVA_CLASS_PATH);
        System.out.println(SystemUtil.JAVA_LIB_PATH);

        System.out.println("-- JVM ------------");
        System.out.println(SystemUtil.JVM_NAME);
        System.out.println(SystemUtil.JVM_VENDOR);
        System.out.println(SystemUtil.JVM_VERSION);
        System.out.println(SystemUtil.JVM_SPEC_NAME);
        System.out.println(SystemUtil.JVM_SPEC_VENDOR);
        System.out.println(SystemUtil.JVM_SPEC_VERSION);

        System.out.println("-- User -----------");
        System.out.println(SystemUtil.USER_NAME);
        System.out.println(SystemUtil.USER_DIR);
        System.out.println(SystemUtil.USER_HOME);

        System.out.println("-- OS -------------");
        System.out.println(SystemUtil.OS_NAME);
        System.out.println(SystemUtil.OS_VERSION);
        System.out.println(SystemUtil.OS_ARCH);

        System.out.println("-- Separator ------");
        System.out.println(SystemUtil.PATH_SEPARATOR);
        System.out.println(SystemUtil.FILE_SEPARATOR);
        System.out.println(SystemUtil.LINE_SEPARATOR);

    }
}
