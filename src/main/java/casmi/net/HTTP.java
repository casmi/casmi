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

package casmi.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import casmi.io.Reader;
import casmi.io.Writer;

/**
 * HTTP connection class.
 * 
 * @author T. Takeuchi
 * 
 */
public class HTTP {

    /** HttpURLConnection object. */
    private final HttpURLConnection connection;

    /**
     * Creates HTTP object from URL.
     * Opens connection in this constructor.
     * 
     * @param url
     *            The URL object.
     * 
     * @throws IOException
     *             If an I/O error is occurred.
     */
    public HTTP(URL url) throws IOException {

        connection = (HttpURLConnection)url.openConnection();
    }

    /**
     * Creates HTTP object from URL string.
     * 
     * @param url
     *            The URL string.
     * 
     * @throws IOException
     *             If an I/O error occurred.
     */
    public HTTP(String url) throws IOException {

        this(new URL(url));
    }

    /**
     * Disconnect a HTTP connection.
     */
    public void disconnect() {

        connection.disconnect();
    }

    /**
     * Do a HTTP GET request forward specified URL.
     * Creates and return Reader object.
     * 
     * @throws IOException
     *             If an I/O error occurs while creating the Reader object.
     */
    public Reader requestGet() throws IOException {

        connection.setRequestMethod("GET");
        connection.setDoOutput(false);

        return new Reader(connection.getInputStream());
    }

    /**
     * Do a HTTP POST request forward specified URL and data string.
     * Creates and return Reader object.
     * 
     * @param data
     *            The data string that the request will contain.
     * 
     * @throws IOException
     *             If an I/O error occurs while writing data on the request or
     *             creating the Reader object.
     */
    public Reader requestPost(String data) throws IOException {

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        Writer writer = new Writer(connection.getOutputStream());
        writer.write(data);
        writer.close();

        return new Reader(connection.getInputStream());
    }

    // -------------------------------------------------------------------------
    // Getter and Setter.
    // -------------------------------------------------------------------------

    /**
     * Returns URL object to connect.
     * 
     * @return The URL object to connect.
     */
    public URL getUrl() {

        return connection.getURL();
    }

    /**
     * Returns HttpURLConnection object.
     * Returned object is a final field.
     * 
     * @return HttpURL Connection object.
     */
    public HttpURLConnection getConnection() {

        return connection;
    }
}
