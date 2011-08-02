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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * File utility class.
 * 
 * @author T. Takeuchi
 * 
 */
public class FileUtil {

    /**
     * The system-dependent default name-separator character, represented as a
     * string for convenience.
     * This string contains a single character.
     * This field is initialized to contain the first character of the value of
     * the system property file.separator. On UNIX systems the value of this field
     * is '/'; on Microsoft Windows systems it is '\\'.
     */
    public static final String SEPARATOR = java.io.File.separator;

    /**
     * The system-dependent path-separator character.
     * This field is initialized to contain the first character of the value of
     * the system property path.separator.
     * This character is used to separate filenames in a sequence of files given
     * as a path list. On UNIX systems, this character is ':'; on Microsoft
     * Windows systems it is ';'.
     */
    public static final String PATH_SEPARATOR = java.io.File.pathSeparator;

    /**
     * Copies a file to a new location preserving the file date.
     * 
     * @param src
     *            An existing file to copy, must not be <code>null</code>.
     * @param dest
     *            The new file, must not be <code>null</code> and exist.
     * 
     * @throws IOException
     *             If copying is failed.
     */
    public static void copyFile(File src, File dest) throws IOException {

        if (src == null)
            throw new NullPointerException("Source must not be null");
        if (dest == null)
            throw new NullPointerException("Destination must not be null");
        if (!src.exists())
            throw new FileNotFoundException("Source '" + src + "' does not exist");
        if (!src.isFile())
            throw new IOException("Source '" + src + "' is not a file");
        if (dest.exists())
            throw new IOException("Destination '" + dest + "' is already exists");

        FileChannel in = null, out = null;
        try {
            in = new FileInputStream(src).getChannel();
            out = new FileOutputStream(dest).getChannel();

            in.transferTo(0, in.size(), out);
        } finally {
            in.close();
            out.close();
        }

        if (src.length() != dest.length()) {
            throw new IOException("Failed to copy full contents from '" +
                src + "' to '" + dest + "'");
        }

        dest.setLastModified(src.lastModified());
    }

    /**
     * Get suffix from File.
     * 
     * @param file File object.
     * 
     * @return Suffix.
     */
    public static String getSuffix(File file) {

        String filename = file.getName();
        int index = filename.lastIndexOf(".");
        if (index != -1) {
            return filename.substring(index + 1);
        }

        return filename;
    }

    /**
     * Search an absolute file path from current working directory recursively.
     * 
     * @param fileName file name to search.
     * @return an absolute file path.
     */
    public static String searchFilePath(String fileName) {
        java.io.File dir = new java.io.File(System.getProperty("user.dir"));
        return recursiveSearch(dir, fileName);
    }

    /**
     * Recursive method for searching file path.
     * 
     * @param dir
     * @param fileName
     * @return
     */
    private static String recursiveSearch(java.io.File dir, String fileName) {
        for (String name : dir.list()) {
            java.io.File file = new java.io.File(dir.getAbsolutePath() + "/" + name);
            if (name.compareTo(fileName) == 0) return file.getAbsolutePath();
            if (file.isDirectory()) {
                String filePath = recursiveSearch(file, fileName);
                if (filePath != null) return filePath;
            }
        }

        return null;
    }
}
