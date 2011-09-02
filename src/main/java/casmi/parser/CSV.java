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

package casmi.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * CSV parser class.
 * 
 * <p>
 * This class uses <a href="http://opencsv.sourceforge.net/">opencsv-2.1</a>
 * library (licensed by Apache 2.0).
 * </p>
 * 
 * <p>
 * If you want to parse other separated files (e.g. TSV file), you can also use
 * this CSV class. Use a constructor "CSV(String pathname, char separator)"
 * like,
 * 
 * <pre>
 * CSV tsv = new CSV(&quot;file.tsv&quot;, &quot;\t&quot;);
 * </pre>
 * </p>
 * 
 * @author T. Takeuchi
 * 
 */
public class CSV {

    /** Default separator. */
    private static final char DEFAULT_SEPARATOR = ',';

    /** A File object. */
    private final File file;

    /** A separator. */
    private char separator = DEFAULT_SEPARATOR;

    /**
     * CSVReader object in opencsv library.
     */
    private CSVReader csvReader = null;

    /**
     * CSVWriter object. in opencsv library.
     */
    private CSVWriter csvWriter = null;

    // -------------------------------------------------------------------------
    // Constructors.
    // -------------------------------------------------------------------------

    /**
     * Creates CSV object from a File.
     * 
     * @param file a File object of a csv file
     */
    public CSV(File file) {
        this(file, DEFAULT_SEPARATOR);
    }

    /**
     * Creates CSV object from a File.
     * Second argument is a character to separate each element.
     * 
     * @param file a File object of a csv file
     */
    public CSV(File file, char separator) {
        this.file = file;
        this.separator = separator;
    }

    /**
     * Creates CSV object from a file path string.
     * 
     * @param file a file path string of a csv file
     */
    public CSV(String pathname) {

        this(pathname, DEFAULT_SEPARATOR);
    }

    /**
     * Creates CSV object from a file path string.
     * Second argument is a character to separate each element.
     * 
     * @param file a file path string of a csv file
     */
    public CSV(String pathname, char separator) {

        this(new File(pathname), separator);
    }

    // -------------------------------------------------------------------------
    // Getters and setters.
    // -------------------------------------------------------------------------

    /**
     * Returns the character to separate each element.
     * 
     * @return the separator <code>char</code>.
     */
    public char getSeparator() {

        return separator;
    }

    // -------------------------------------------------------------------------
    // Other methods.
    // -------------------------------------------------------------------------

    /**
     * Reads the next line from the buffer and converts to a string array.
     * 
     * @return
     *         a string array with each comma-separated element as a separate
     *         entry
     * 
     * @throws IOException
     *             if bad things happen during the read
     */
    public String[] readLine() throws IOException {

        openReader();
        return csvReader.readNext();
    }

    /**
     * Reads the entire file into a List with each element being a String[] of
     * tokens.
     * 
     * @return
     *         a List of String[], with each String[] representing a line of the
     *         file
     * 
     * @throws IOException
     *             if bad things happen during the read
     */
    public java.util.List<String[]> readAll() throws IOException {

        openReader();
        return csvReader.readAll();
    }

    /**
     * Writes the next line to the file.
     * 
     * @param elements
     *            element strings or a string array with each comma-separated
     *            element
     *            as a separate entry
     * 
     * @throws IOException
     *             if an I/O error occurs
     */
    public void writeLine(String... elements) throws IOException {

        openWriter();
        csvWriter.writeNext(elements);
    }

    /**
     * Writes the entire list to a CSV file.
     * The list is assumed to be a String[].
     * 
     * @param allLines
     *            a List of String[], with each String[] representing a line of
     *            the file.
     * 
     * @throws IOException
     *             if an I/O error occurs
     */
    public void writeAll(java.util.List<String[]> allLines) throws IOException {

        openWriter();
        csvWriter.writeAll(allLines);
    }

    private void openReader() throws FileNotFoundException {

        if (csvReader == null) {
            csvReader = new CSVReader(new java.io.FileReader(file), separator);
        }
    }

    private void openWriter() throws IOException {

        if (csvWriter == null) {
            csvWriter = new CSVWriter(new java.io.FileWriter(file), separator);
        }
    }

    /**
     * Close reader and writer objects.
     * 
     * @throws IOException
     *             if an I/O error occurs
     */
    public void close() throws IOException {

        if (csvReader != null) csvReader.close();
        if (csvWriter != null) csvWriter.close();
    }
}
