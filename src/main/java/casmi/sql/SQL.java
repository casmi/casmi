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

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class for SQL classes.
 * 
 * @author T. Takeuchi
 * 
 */
abstract class SQL {

    private final SQLType sqlType;

    /** java.sql.Connection. */
    protected Connection connection;

    protected SQL(SQLType sqlType) {

        this.sqlType = sqlType;
    }

    /**
     * Returns a type of SQL database in this class.
     * 
     * @return A type of SQL database.
     */
    public SQLType getSQLType() {

        return sqlType;
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
     * Connect the database.
     * 
     * @throws SQLException
     */
    abstract void connect() throws SQLException;

    /**
     * Close the database.
     * 
     * @throws SQLException
     */
    abstract void close();

    /**
     * Executes the given SQL statement, which may return multiple results. In
     * some (uncommon) situations, a single SQL statement may return multiple
     * result sets and/or update counts. Normally you can ignore this unless you
     * are (1) executing a stored procedure that you know may return multiple
     * results or (2) you are dynamically executing an unknown SQL string. The
     * execute method executes an SQL statement and indicates the form of the
     * first result. You must then use the methods getResultSet or
     * getUpdateCount to retrieve the result, and getMoreResults to move to any
     * subsequent result(s).
     * 
     * @param sql
     *            any SQL statement
     * @throws SQLException
     */
    abstract void execute(String sql, Object... params) throws SQLException;

    /**
     * Retrieves the current auto-commit mode for this Connection object.
     * 
     * @return the current state of this Connection object's auto-commit mode.
     * @throws SQLException
     */
    abstract boolean getAutoCommit() throws SQLException;

    /**
     * Sets this connection's auto-commit mode to the given state. If a
     * connection is in auto-commit mode, then all its SQL statements will be
     * executed and committed as individual transactions. Otherwise, its SQL
     * statements are grouped into transactions that are terminated by a call to
     * either the method commit or the method rollback. By default, new
     * connections are in auto-commit mode.
     * 
     * @param autoCommit
     * @throws SQLException
     */
    abstract void setAutoCommit(boolean autoCommit) throws SQLException;

    /**
     * Makes all changes made since the previous commit/rollback permanent and
     * releases any database locks currently held by this Connection object.
     * This method should be used only when auto-commit mode has been disabled.
     * 
     * @throws SQLException
     */
    abstract void commit() throws SQLException;

    /**
     * Undoes all changes made in the current transaction and releases any
     * database locks currently held by this Connection object. This method
     * should be used only when auto-commit mode has been disabled.
     * 
     * @throws SQLException
     */
    abstract void rollback() throws SQLException;

    /**
     * Moves the cursor forward one row from its current position. A ResultSet
     * cursor is initially positioned before the first row; the first call to
     * the method next makes the first row the current row; the second call
     * makes the second row the current row, and so on.
     * 
     * @return
     *     <code>true</code> if the new current row is valid; <code>false</code> if there are no more rows
     * 
     * @throws SQLException
     */
    abstract boolean next() throws SQLException;
    
    abstract <T> T get(ResultSet resultSet, Class<T> type, String field) throws SQLException;

    // -------------------------------------------------------------------------
    // For O/R mapping.
    // -------------------------------------------------------------------------

    public <T extends Entity> T entity(Class<T> type) {

        T entity = null;
        try {
            if (type.isMemberClass()) {
                Constructor<T> con = type.getConstructor(new Class[] {type.getDeclaringClass()});
                entity = con.newInstance(new Object[] {type.getDeclaringClass().newInstance()});
            } else {
                entity = type.newInstance();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        entity.init(this, type);

        return entity;
    }

    public <T extends Entity> T[] all(Class<T> type) throws SQLException {

        String sqlStr = "SELECT * FROM " + getTablename(type);
        return find(type, sqlStr);
    }
    
    public <T extends Entity> T[] all(Class<T> type, Query query) throws SQLException {
        
        String sqlStr = "SELECT :select FROM :table :query";
        StringBuilder sb;
        
        // select
        if (query.isSelectEnable()) {
            sb = new StringBuilder();
            boolean flag = false;
            
            for (String select : query.getSelects()) {
                if (sb.length() != 0) sb.append(',');
                if (select.equals("id")) flag = true;
                
                sb.append(select);
            }
            
            // if the entity has an automatic primary key("id") and not selected,
            // append the key automatically.
            if (!flag && Entity.isAutoPrimaryKey(type)) {
                sb.insert(0, "id,");
            }
            
            sqlStr = sqlStr.replaceAll(":select", sb.toString());
        } else {
            sqlStr = sqlStr.replaceAll(":select", "*");
        }
        
        // table
        sqlStr = sqlStr.replaceAll(":table", getTablename(type));
        
        // query ---------------------------------------------------------------
        sb = new StringBuilder();
        
        // where
        if (query.isWhereEnable()) {
            sb.append("WHERE ");
            sb.append(query.getWhere());
        }

        // group by
        if (query.isGroupEnable()) {
            sb.append(" GROUP BY ");
            sb.append(query.getGroup());
        }
        
        // order by
        if (query.isOrderEnable()) {
            sb.append(" ORDER BY ");
            sb.append(query.getOrder());
        }
        
        // descending
        if (query.isDesc()) {
            sb.append(" DESC");
        }
        
        // limit
        if (query.isLimitEnable()) {
            sb.append(" LIMIT ");
            sb.append(query.getLimit());
        }
        
        sqlStr = sqlStr.replaceAll(":query", sb.toString());
        // ---------------------------------------------------------------------
        
        return find(type, sqlStr, query.getSelects());
    }
    
    // TODO: this method is correct only if a primary key is not specified.
    public <T extends Entity> T find(Class<T> type, int id) throws SQLException {
        T[] entities = all(type, new Query().where("id=" + id));
        if (entities.length == 0) return null;
        return entities[0];
    }
    
    // TODO: this method is correct only if a primary key is not specified.
    public <T extends Entity> T find(Class<T> type, int id, Query query) throws SQLException {
        query.andWhere("id=" + id);
        T[] entities = all(type, query);
        if (entities.length == 0) return null;
        return entities[0];
    }
    
    public <T extends Entity> T first(Class<T> type) throws SQLException {
        T[] entities = all(type, new Query().limit(1));
        if (entities.length == 0) return null;
        return entities[0];
    }
    
    public <T extends Entity> T first(Class<T> type, Query query) throws SQLException {
        T[] entities = all(type, query.limit(1));
        if (entities.length == 0) return null;
        return entities[0];
    }
    
    public <T extends Entity> T last(Class<T> type) throws SQLException {
        T[] entities = all(type, new Query().order(Entity.getPrimaryKeyField(type)).desc(true).limit(1));
        if (entities.length == 0) return null;
        return entities[entities.length - 1];
    }
    
    public <T extends Entity> T last(Class<T> type, Query query) throws SQLException {
        if (!query.isOrderEnable()) {
            query.order(Entity.getPrimaryKeyField(type));
        }
        T[] entities = all(type, query.desc(true).limit(1));
        if (entities.length == 0) return null;
        return entities[entities.length - 1];
    }
    
    @SuppressWarnings("unchecked")
    private <T extends Entity> T[] find(Class<T> type, String sqlStr, String... selects) throws SQLException {
        if (connection == null)
            throw new SQLException("Connection is not exist.");
        
        List<T> list = new ArrayList<T>();

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sqlStr);

        while (rs.next()) {
            T entity = entity(type);
            if (selects        == null ||
                selects.length == 0    || selects[0].equals('*')) {
                entity.setValuesFromResultSet(rs);
            } else {
                entity.setValuesFromReslutSet(rs, selects);
            }
            list.add(entity);
        }
        
        rs.close();
        statement.close();

        if (list.isEmpty()) return (T[])Array.newInstance(type, 0);
        
        return list.toArray((T[])Array.newInstance(type, list.size()));
    }
    
    public <T extends Entity> void truncate(Class<T> type) throws SQLException {
        if (connection == null)
            throw new SQLException("Connection is not exist.");
        
        Statement statement = connection.createStatement();
        String stmt = StatementGenerator.truncate(sqlType, getTablename(type));
        statement.executeUpdate(stmt);
        statement.close();
    }

    public <T extends Entity> String getTablename(Class<T> type) {
        
        return Entity.getTablename(type);
    }
}