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
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writer class extends java.io.PrintWriter.
 *
 * @author T. Takeuchi
 *
 */
public class Writer extends java.io.PrintWriter {

    /**
     * Creates Writer object from java.io.Writer.
     *
     * @param writer java.io.Writer object.
     */
    public Writer(java.io.Writer writer) {

        super(writer);
    }

    /**
     * Creates Writer object from java.io.OutputStream.
     *
     * @param os java.io.OutputStream object.
     */
    public Writer(java.io.OutputStream os) {

        this(new java.io.OutputStreamWriter(os));
    }

    /**
     * Creates Writer object from File.
     *
     * @param file
     *            a File object to write to
     *
     * @throws IOException
     *             If the file exists but is a directory rather than a regular file,
     *             does not exist but cannot be created, or cannot be opened for
     *             any other reason.
     */
    public Writer(File file) throws IOException {

        this(file, false);
    }

    /**
     * Creates Writer object from File.
     * If the second argument is <code>true</code>, then bytes will be written to
     * the end of the file rather than the beginning.
     *
     * @param file
     *            a File object to write to
     * @param append
     *            If <code>true</code>, then bytes will be written to the end of
     *            the file rather than the beginning.
     *
     * @throws IOException
     *             If the file exists but is a directory rather than a regular file,
     *             does not exist but cannot be created, or cannot be opened for
     *             any other reason.
     */
    public Writer(File file, boolean append) throws IOException {

        this(new FileWriter(file, append));
    }

    /**
     * Creates Writer object from a file path.
     *
     * @param pathname the file path string
     *
     * @throws IOException
     *             if the file exists but is a directory rather than a regular file,
     *             does not exist but cannot be created, or cannot be opened for
     *             any other reason
     */
    public Writer(String pathname) throws IOException {

        this(new File(pathname));
    }

    /**
     * Creates Writer object from a file path.
     * If the second argument is <code>true</code>, then bytes will be written to
     * the end of the file rather than the beginning.
     *
     * @param pathname the file path string
     * @param append
     *            If <code>true</code>, then bytes will be written to the end of
     *            the file rather than the beginning.
     *
     * @throws IOException
     *             if the file exists but is a directory rather than a regular file,
     *             does not exist but cannot be created, or cannot be opened for
     *             any other reason
     */
    public Writer(String pathname, boolean append) throws IOException {

        this(new File(pathname), append);
    }
}
