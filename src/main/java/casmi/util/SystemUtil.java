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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import casmi.exception.CasmiException;

/**
 * Utility class to access system properties easily.
 *
 * @author Xcoo Inc.
 *
 */
public class SystemUtil {

    // Java
    /** Java class paths. */
    public static final String JAVA_CLASS_PATH = System.getProperty("java.class.path");
    /** Java compiler name. */
    public static final String JAVA_COMPILER = System.getProperty("java.compiler");
    /** Java home directory. */
    public static final String JAVA_HOME = System.getProperty("java.home");
    /** Java library path. */
    public static final String JAVA_LIB_PATH = System.getProperty("java.library.path");
    /** Java vendor name. */
    public static final String JAVA_VENDOR = System.getProperty("java.vendor");
    /** Java vendor URL. */
    public static final String JAVA_VENDOR_URL = System.getProperty("java.vendor.url");
    /** Java version. */
    public static final String JAVA_VERSION = System.getProperty("java.version");
    /** Java temp dir. */
    public static final String JAVA_TMP_PATH = System.getProperty("java.io.tmpdir");

    // JVM
    /** JVM name. */
    public static final String JVM_NAME = System.getProperty("java.vm.name");
    /** JVM vendor name. */
    public static final String JVM_VENDOR = System.getProperty("java.vm.vendor");
    /** JVM version. */
    public static final String JVM_VERSION = System.getProperty("java.vm.version");
    /** JVM name in specification. */
    public static final String JVM_SPEC_NAME = System.getProperty("java.vm.specification.name");
    /** JVM vendor name in specification. */
    public static final String JVM_SPEC_VENDOR = System.getProperty("java.vm.specification.vendor");
    /** JVM version in specification. */
    public static final String JVM_SPEC_VERSION = System.getProperty("java.vm.specification.version");

    // OS
    /** OS name (e.g. "Mac OS X", "Linux", etc.). */
    public static final String OS_NAME = System.getProperty("os.name");
    /** OS architecture (e.g. "x86", "x86_64", etc.). */
    public static final String OS_ARCH = System.getProperty("os.arch");
    /** OS version (e.g. "10.6.8", etc.). */
    public static final String OS_VERSION = System.getProperty("os.version");

    // User
    /** User's working directory. */
    public static final String USER_DIR = System.getProperty("user.dir");
    /** User's name. */
    public static final String USER_NAME = System.getProperty("user.name");
    /** User's home directory. */
    public static final String USER_HOME = System.getProperty("user.home");

    // Separator
    /** File separator string of the system. */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    /** Path separator string of the system. */
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");
    /** Line separator string of the system. */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Get all property names.
     *
     * @return String array of all property names.
     */
    static public String[] getAllProperties() {
        java.util.Properties prop = System.getProperties();
        java.util.ArrayList<String> list = new java.util.ArrayList<String>();
        java.util.Enumeration<?> enumeration = prop.propertyNames();
        while (enumeration.hasMoreElements()) {
            list.add((String)enumeration.nextElement());
        }
        return list.toArray(new String[0]);
    }

    /**
     * Get information of OS environment.
     * Detects OS name (e.g. Mac OS X) and architecture (e.g. x86_64).
     *
     * @return OSEnv enum object.
     */
    public static OS getOS() {
        String osname = SystemUtil.OS_NAME;
        String osarch = SystemUtil.OS_ARCH;

        if (osname.indexOf("Mac") != -1) {
            if (osarch.equals("x86")) return OS.MAC;
            else if (osarch.equals("x86_64")) return OS.MAC_64;
        } else if (osname.indexOf("Windows") != -1) {
            if (osarch.equals("x86")) return OS.WIN;
            else if (osarch.equals("x86_64")||osarch.equals("amd64")) return OS.WIN_64;
        } else if (osname.indexOf("Linux") != -1) {
            if (osarch.equals("i386")) return OS.LINUX;
            else if (osarch.equals("amd64")) return OS.LINUX_64;
        }

        return OS.OTHER;
    }

    /**
     * Launches the default browser to display a URL.
     *
     * @param url
     *            The URL to be displayed in the user default browser.
     *
     * @throws NetException
     *            If errors occur
     */
    public static void browse(URL url) throws CasmiException {

        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI(url.toString()));
        } catch (URISyntaxException e) {
            throw new CasmiException(e);
        } catch (IOException e) {
            throw new CasmiException(e);
        }
    }
}
