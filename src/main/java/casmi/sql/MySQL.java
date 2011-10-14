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

import java.sql.Blob;
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

/**
 * MySQL class.
 * 
 * @author T. Takeuchi
 */
public class MySQL extends SQL {

    /** SQL database type. */
    private static final SQLType SQL_TYPE = SQLType.MYSQL_5;

    /** Driver name. */
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    /** Database URL. */
    private final String url;

    /** User name. */
    private final String user;

    /** Password. */
    private final String password;

    /** java.sql.Statement. */
    private Statement statement;

    /** java.sql.PreparedStatement. */
    private PreparedStatement preparedStatement;

    /** java.sql.ResultSet. */
    private ResultSet resultSet;

    // Load driver.
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates new MySQL object from the specified host and database name.
     * 
     * @param host
     *            The host name of a MySQL server.
     * 
     * @param database
     *            The database name.
     */
    public MySQL(String host, String database) {

        super(SQL_TYPE);

        if (host == null) {
            throw new IllegalArgumentException("The host name is null.");
        } else if (database == null) {
            throw new IllegalArgumentException("The database name is null.");
        }

        url = "jdbc:mysql://" + host + "/" + database;
        user = null;
        password = null;
    }

    /**
     * Creates new MySQL object from the specified host, database, user, and
     * password.
     * 
     * @param host
     *            The host name of a MySQL server.
     * 
     * @param database
     *            The database name.
     * 
     * @param user
     *            The user name to log in the MySQL server.
     * 
     * @param password
     *            The password to log in the MySQL server.
     */
    public MySQL(String host, String database, String user, String password) {

        super(SQL_TYPE);

        if (host == null) {
            throw new IllegalArgumentException("The host name is null.");
        } else if (database == null) {
            throw new IllegalArgumentException("The database name is null.");
        } else if (user == null) {
            throw new IllegalArgumentException("The user name is null.");
        } else if (password == null) {
            throw new IllegalArgumentException("The password is null.");
        }

        url = "jdbc:mysql://" + host + "/" + database;
        this.user = user;
        this.password = password;
    }

    @Override
    public void connect() throws SQLException {

        if (user == null || password == null) {
            connection = DriverManager.getConnection(url);
        } else {
            connection = DriverManager.getConnection(url, user, password);
        }
    }

    @Override
    public void close() {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Ignore.
            }
        }
    }

    /**
     * Closes statement and preparedStatement.
     */
    private void closeStatements() {

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // Ignore.
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                // Ignore.
            }
        }
    }

    @Override
    public void execute(String sql, Object... params) throws SQLException {

        if (connection == null)
            throw new SQLException("Connection is not exist.");

        closeStatements();

        if (0 < params.length) {
            // User a prepared statement.
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                setParameter(i + 1, params[i]);
            }
            if (!isSQLQuery(sql)) preparedStatement.executeUpdate();
            else resultSet = preparedStatement.executeQuery();
        } else {
            // User a normal statement.
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

        if (param instanceof Blob) {
            preparedStatement.setBlob(parameterIndex, (Blob)param);
        } else if (param instanceof java.util.Date) {
            preparedStatement.setDate(parameterIndex, DateUtil.toSqlDate((java.util.Date)param));
        } else if (param instanceof Double) {
            preparedStatement.setDouble(parameterIndex, (Double)param);
        } else if (param instanceof Float) {
            preparedStatement.setFloat(parameterIndex, (Float)param);
        } else if (param instanceof Integer) {
            preparedStatement.setInt(parameterIndex, (Integer)param);
        } else if (param instanceof String) {
            preparedStatement.setString(parameterIndex, (String)param);
        } else {
            throw new SQLException("The object type is not supported.");
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {

        if (connection == null)
            throw new SQLException("Connection is not exist.");
        return connection.getAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {

        if (connection == null)
            throw new SQLException("Connection is not exist.");
        connection.setAutoCommit(autoCommit);
    }

    @Override
    public void commit() throws SQLException {

        if (connection == null)
            throw new SQLException("Connection is not exist.");
        connection.commit();
    }

    @Override
    public void rollback() throws SQLException {

        if (connection == null)
            throw new SQLException("Connection is not exist.");
        connection.rollback();
    }

    @Override
    public boolean next() throws SQLException {

        if (resultSet == null)
            throw new SQLException("Result set is not exist.");
        return resultSet.next();
    }

    // -------------------------------------------------------------------------
    // Getters from resultSet.
    // -------------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    <T> T get(ResultSet resultSet, Class<T> type, String field) throws SQLException {
        
        if (type == Blob.class) {
            return (T)resultSet.getBlob(field);
        } else if (type ==     int.class ||
                   type == Integer.class) {
            return (T)(Integer)resultSet.getInt(field);
        } else if (type == double.class || 
                   type == Double.class) {
            return (T)(Double)resultSet.getDouble(field);
        } else if (type == float.class || 
                   type == Float.class) {
            return (T)(Float)resultSet.getFloat(field);
        } else if (type == java.util.Date.class) {
            return (T)DateUtil.toUtilDate(resultSet.getDate(field));
        } else if (type == String.class) {
            return (T)resultSet.getString(field);
        }
        
        return null;
    }
    
    /**
     * Retrieves the value of the designated column in the current row as a Blob
     * object in the Java programming language.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return
     *         a Blob object representing the SQL BLOB value in the specified
     *         column.
     * 
     * @throws SQLException
     *             if the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public Blob getBlob(int column) throws SQLException {

        if (resultSet == null)
            throw new SQLException("Result set is not exist.");

        return resultSet.getBlob(column);
    }

    /**
     * Retrieves the value of the designated column in the current row as a Blob
     * object in the Java programming language.
     * 
     * @param field
     *            The name of the field.
     * 
     * @return
     *         a Blob object representing the SQL BLOB value in the specified
     *         column.
     * 
     * @throws SQLException
     *             if the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public Blob getBlob(String field) throws SQLException {

        if (resultSet == null)
            throw new SQLException("Result set is not exist.");

        return resultSet.getBlob(field);
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
    public java.util.Date getDate(int column) throws SQLException, ParseException {

        if (resultSet == null)
            throw new SQLException("Result set is not exist.");

        return DateUtil.toUtilDate(resultSet.getDate(column));
    }

    /**
     * Retrieves the value of the designated column in the current row as
     * java.util.Date object.
     * 
     * @param field
     *            The name of the field.
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

        if (resultSet == null)
            throw new SQLException("Result set is not exist.");

        return DateUtil.toUtilDate(resultSet.getDate(field));
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
            throw new SQLException("Result set is not exist.");

        return resultSet.getDouble(column);
    }

    /**
     * Retrieves the value of the designated column in the current row as a
     * double in the Java programming language.
     * 
     * @param field
     *            The name of the field.
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
            throw new SQLException("Result set is not exist.");

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
            throw new SQLException("Result set is not exist.");

        return resultSet.getFloat(column);
    }

    /**
     * Retrieves the value of the designated column in the current row as a
     * float in the Java programming language.
     * 
     * @param field
     *            The name of the field.
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
            throw new SQLException("Result set is not exist.");

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
            throw new SQLException("Result set is not exist.");

        return resultSet.getInt(column);
    }

    /**
     * Retrieves the value of the designated column in the current row as a int
     * in the Java programming language.
     * 
     * @param field
     *            The name of the field.
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
     * Object in the Java programming language.
     * 
     * @param column
     *            The first column is 1, the second is 2, ...
     * 
     * @return
     *         A java.lang.Object holding the column value.
     * 
     * @throws SQLException
     *             If the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public Object getObject(int column) throws SQLException {

        if (resultSet == null)
            throw new SQLException("Result set is not exist.");

        return resultSet.getObject(column);
    }

    /**
     * Retrieves the value of the designated column in the current row as a
     * Object in the Java programming language.
     * 
     * @param column
     *            The name of the field.
     * 
     * @return
     *         A java.lang.Object holding the column value.
     * 
     * @throws SQLException
     *             If the columnIndex is not valid; if a database access error
     *             occurs or this method is called on a closed result set.
     */
    public Object getObject(String field) throws SQLException {

        if (resultSet == null)
            throw new SQLException("Result set is not exist.");

        return resultSet.getObject(field);
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
            throw new SQLException("Result set is not exist.");

        return resultSet.getString(column);
    }

    /**
     * Retrieves the value of the designated column in the current row as a
     * String in the Java programming language.
     * 
     * @param field
     *            The name of the field.
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
            throw new SQLException("Result set is not exist.");

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
            throw new SQLException("Result set is not exist.");

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
    
    // O/R mapping.
    
    public <T extends Entity> void drop(Class<T> type) throws SQLException {
        
        Statement statement = connection.createStatement();
        String stmt = StatementGenerator.drop(getSQLType(), getTablename(type));
        statement.executeUpdate(stmt);
        statement.close();
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
     * Return a user name.
     * 
     * @return A user name string.
     */
    public String getUser() {

        return user;
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
