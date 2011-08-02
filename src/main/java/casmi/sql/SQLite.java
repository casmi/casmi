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

package casmi.sql;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import casmi.util.DateUtil;
import casmi.util.FileUtil;

/**
 * SQLite class.
 * 
 * @author T. Takeuchi
 */
public class SQLite implements SQL {

    /** Driver name. */
    private static final String DRIVER = "org.sqlite.JDBC";

    /** Date formats. */
    private static final String[][] DATE_FORMATS = {
        {"[0-9]{4}-[0-9]{2}-[0-9]{2}", "yyyy-MM-dd"},
        {"[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}",
            "yyyy-MM-dd HH:mm"},
        {"[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}",
            "yyyy-MM-dd HH:mm:ss"},
        {"[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}",
            "yyyy-MM-dd'T'HH:mm"},
        {"[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}",
            "yyyy-MM-dd'T'HH:mm:ss"}};

    /** Database URL. */
    private String url;

    /** java.sql.Connection. */
    private Connection connection;

    /** java.sql.Statement. */
    private Statement statement;

    /** java.sql.PreparedStatement. */
    private PreparedStatement preparedStatement;

    /** java.sql.ResultSet. */
    private ResultSet resultSet;

    /**
     * Creates new SQLite object from the specified database file path.
     * 
     * @param dbPath
     *            The SQLite3 database file's path.
     */
    public SQLite(String dbPath) {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String path;

        File file = new File(dbPath);
        if (file.isFile()) {
            path = file.getAbsolutePath();
        } else {
            path = FileUtil.searchFilePath(dbPath);
        }
        if (path == null)
            throw new IllegalArgumentException();

        url = "jdbc:sqlite:" + path;
    }

    /**
     * Create SQLite3 database file.
     * 
     * @param dbPath
     *            database file path.
     * @throws IOException
     */
    public static void createDatabase(String dbPath) throws IOException {
      
        URL url = SQLite.class.getResource("template.sqlite3");

        if (url == null)
            throw new IOException("Template file does not found.");

        File src = new File(url.getPath());
        File dest = new File(dbPath);
        FileUtil.copyFile(src, dest);
    }

    @Override
    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {}
        }
    }

    /**
     * Closes statement and preparedStatement.
     */
    private void closeStatements() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {}
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {}
        }
    }

    @Override
    public void execute(String sql, Object... params) throws SQLException {
        if (connection == null)
            throw new SQLException();

        closeStatements();

        if (0 < params.length) {
            // Prepared statement.
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                setParameter(i + 1, params[i]);
            }
            if (!isSQLQuery(sql)) preparedStatement.executeUpdate();
            else resultSet = preparedStatement.executeQuery();
        } else {
            // Normal statement.
            statement = connection.createStatement();
            if (!isSQLQuery(sql)) statement.executeUpdate(sql);
            else resultSet = statement.executeQuery(sql);
        }
    }

    /**
     * Return true if the SQL is a query.
     * 
     * @param sql
     * @return
     */
    private boolean isSQLQuery(String sql) {
        Pattern pattern = Pattern.compile("select", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find())
            return true;
        return false;
    }

    /**
     * Set parameter on a prepared statement.
     * 
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param param
     *            the parameter object.
     * @throws SQLException
     */
    private void setParameter(int parameterIndex, Object param) throws SQLException {

        if (param instanceof java.util.Date) {
            String dateStr = DateUtil.format((java.util.Date)param, DATE_FORMATS[2][1]);
            preparedStatement.setString(parameterIndex, dateStr);
        } else if (param instanceof Double) {
            preparedStatement.setDouble(parameterIndex, (Double)param);
        } else if (param instanceof Float) {
            preparedStatement.setFloat(parameterIndex, (Float)param);
        } else if (param instanceof Integer) {
            preparedStatement.setInt(parameterIndex, (Integer)param);
        } else if (param instanceof String) {
            preparedStatement.setString(parameterIndex, (String)param);
        } else {
            throw new SQLException();
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        if (connection == null)
            throw new SQLException();
        return connection.getAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (connection == null)
            throw new SQLException();
        connection.setAutoCommit(autoCommit);
    }

    @Override
    public void commit() throws SQLException {
        if (connection == null)
            throw new SQLException();
        connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        if (connection == null)
            throw new SQLException();
        connection.rollback();
    }

    @Override
    public boolean next() throws SQLException {
        if (resultSet == null)
            throw new SQLException();
        return resultSet.next();
    }

    // -------------------------------------------------------------------------
    // Getters from resultSet.
    // -------------------------------------------------------------------------

    // public Blob getBlob(int column) throws SQLException {
    // if (resultSet == null) throw new SQLException();
    // return resultSet.getBlob(column);
    // }
    //
    // public Blob getBlob(String field) throws SQLException {
    // if (resultSet == null) throw new SQLException();
    // return resultSet.getBlob(field);
    // }

    /**
     * Retrieves the value of the designated column in the current row as
     * java.util.Date object.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return The column value; if the value is SQL NULL, the value returned is
     *         null.
     * 
     * @throws SQLException
     *             If the column index is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     * @throws ParseException
     *             If the column value is invalid as date.
     */
    public java.util.Date getDate(int column) throws SQLException, ParseException {

        if (resultSet == null) throw new SQLException();

        String dateStr = resultSet.getString(column);
        for (int i = 0; i < DATE_FORMATS.length; i++) {
            if (dateStr.matches(DATE_FORMATS[i][0])) return DateUtil.parse(dateStr, DATE_FORMATS[i][1]);
        }

        return null;
    }

    /**
     * Retrieves the value of the designated column in the current row as
     * java.util.Date object.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return The column value; if the value is SQL NULL, the value returned is
     *         null.
     * 
     * @throws SQLException
     *             If the column index is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     * @throws ParseException
     *             If the column value is invalid as date.
     */
    public java.util.Date getDate(String field) throws SQLException, ParseException {

        if (resultSet == null) throw new SQLException();

        String dateStr = resultSet.getString(field);
        for (int i = 0; i < DATE_FORMATS.length; i++) {
            if (dateStr.matches(DATE_FORMATS[i][0])) return DateUtil.parse(dateStr, DATE_FORMATS[i][1]);
        }

        return null;
    }

    /**
     * Retrieves the value of the designated column in the current row as a
     * double in the Java programming language.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return The column value; if the value is SQL NULL, the value returned is
     *         0.
     * 
     * @throws SQLException
     *             If the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public double getDouble(int column) throws SQLException {

        if (resultSet == null)
            throw new SQLException();
        return resultSet.getDouble(column);
    }

    /**
     * Retrieves the value of the designated column in the current row as a
     * double in the Java programming language.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return The column value; if the value is SQL NULL, the value returned is
     *         0.0.
     * 
     * @throws SQLException
     *             If the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public double getDouble(String field) throws SQLException {

        if (resultSet == null)
            throw new SQLException();
        return resultSet.getDouble(field);
    }

    /**
     * Retrieves the value of the designated column in the current row as a
     * float in the Java programming language.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return The column value; if the value is SQL NULL, the value returned is
     *         0.0f.
     * 
     * @throws SQLException
     *             If the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public float getFloat(int column) throws SQLException {

        if (resultSet == null)
            throw new SQLException();
        return resultSet.getFloat(column);
    }

    /**
     * Retrieves the value of the designated column in the current row as a
     * float in the Java programming language.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return The column value; if the value is SQL NULL, the value returned is
     *         0.0f.
     * 
     * @throws SQLException
     *             If the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public float getFloat(String field) throws SQLException {

        if (resultSet == null)
            throw new SQLException();
        return resultSet.getFloat(field);
    }

    /**
     * Retrieves the value of the designated column in the current row as int in
     * the Java programming language.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return The column value; if the value is SQL NULL, the value returned is
     *         0.
     * 
     * @throws SQLException
     *             If the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public int getInt(int column) throws SQLException {

        if (resultSet == null)
            throw new SQLException();
        return resultSet.getInt(column);
    }

    /**
     * Retrieves the value of the designated column in the current row as a int
     * in the Java programming language.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return The column value; if the value is SQL NULL, the value returned is
     *         0.
     * 
     * @throws SQLException
     *             If the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public int getInt(String field) throws SQLException {

        if (resultSet == null)
            throw new SQLException();
        return resultSet.getInt(field);
    }

    /**
     * Retrieves the value of the designated column in the current row as a
     * String in the Java programming language.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return The column value; if the value is SQL NULL, the value returned is
     *         null.
     * 
     * @throws SQLException
     *             If the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public String getString(int column) throws SQLException {

        if (resultSet == null)
            throw new SQLException();
        return resultSet.getString(column);
    }

    /**
     * Retrieves the value of the designated column in the current row as a
     * String in the Java programming language.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return The column value; if the value is SQL NULL, the value returned is
     *         null.
     * 
     * @throws SQLException
     *             If the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public String getString(String field) throws SQLException {

        if (resultSet == null)
            throw new SQLException();
        return resultSet.getString(field);
    }

    // -------------------------------------------------------------------------
    // Other methods.
    // -------------------------------------------------------------------------

    /**
     * Returns a string representation of the record.
     * 
     * @return a string representation of the record.
     * @throws SQLException
     */
    public String recordToString() throws SQLException {
        if (resultSet == null)
            throw new SQLException();

        String out = "| ";

        ResultSetMetaData rsmd = resultSet.getMetaData();
        for (int i = 0; i < rsmd.getColumnCount(); i++) {
            out += resultSet.getString(i + 1) + " | ";
        }

        return out;
    }

    /**
     * Prints a record string and then terminate the line simply.
     * 
     * @throws SQLException
     */
    public void println() throws SQLException {
        System.out.println(recordToString());
    }

    // -------------------------------------------------------------------------
    // Getter of private variables.
    // -------------------------------------------------------------------------

    /**
     * Return a database's URL.
     * 
     * @return A database's URL string.
     */
    public String getURL() {
        return url;
    }

    /**
     * Return java.sql.Connection object.
     * 
     * @return java.sql.Connection object.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Return java.sql.Statement object.
     * 
     * @return java.sql.Statement object.
     */
    public Statement getStatement() {
        return statement;
    }

    /**
     * Return java.sql.PreparedStatement object.
     * 
     * @return java.sql.PreparedStatement object.
     */
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    /**
     * Return java.sql.ResultSet object.
     * 
     * @return java.sql.ResultSet object.
     */
    public ResultSet getResultSet() {
        return resultSet;
    }
}
