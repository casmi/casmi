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

package casmi.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader class extends BufferedReader.
 *
 * @author T. Takeuchi
 *
 */
public class Reader extends java.io.BufferedReader {

    /**
     * Creates Reader object from java.io.Reader.
     *
     * @param reader java.io.Reader object.
     */
    public Reader(java.io.Reader reader) {

        super(reader);
    }

    /**
     * Creates Reader object from java.io.InputStream.
     *
     * @param is java.io.InputStream object.
     */
    public Reader(java.io.InputStream is) {

        this(new java.io.InputStreamReader(is));
    }

    /**
     * Creates Reader object from File.
     *
     * @param file File object.
     *
     * @throws FileNotFoundException
     *             If the file does not exist, is a directory rather than a
     *             regular
     *             file, or for some other reason cannot be opened for reading.
     */
    public Reader(File file) throws FileNotFoundException {

        this(new java.io.FileReader(file));
    }

    /**
     * Creates Reader object from file path.
     *
     * @param pathname The file path string.
     *
     * @throws FileNotFoundException
     *             If the file does not exist, is a directory rather than a
     *             regular
     *             file, or for some other reason cannot be opened for reading.
     */
    public Reader(String pathname) throws FileNotFoundException {

        this(new java.io.FileReader(new File(pathname)));
    }

    public Reader(URL pathurl) throws FileNotFoundException, URISyntaxException {
        this(new java.io.FileReader(new File(pathurl.toURI())));
    }

    public void close() {

        try {
            super.close();
        } catch (IOException e) {
            // Ignore.
        }
    }

    /**
     * Reads all lines and return as a String array.
     *
     * @return The String array.
     *
     * @throws IOException If an I/O error occurs.
     */
    public String[] readLines() throws IOException {

        List<String> list = new ArrayList<String>();
        String line;
        while ((line = readLine()) != null) {
            list.add(line);
        }

        return list.toArray(new String[0]);
    }

    /**
     * Reads all lines and return as a String.
     *
     * @return The String.
     *
     * @throws IOException If an I/O error occurs.
     */
    public String readAll() throws IOException {

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }
}
