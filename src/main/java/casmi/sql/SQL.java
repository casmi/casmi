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

import java.sql.SQLException;

interface SQL {

	/**
	 * Connect the database.
	 * 
	 * @throws SQLException
	 */
	void connect() throws SQLException;

	/**
	 * Close the database.
	 * 
	 * @throws SQLException
	 */
	void close();

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
	void execute(String sql, Object... params) throws SQLException;

	/**
	 * Retrieves the current auto-commit mode for this Connection object.
	 * 
	 * @return the current state of this Connection object's auto-commit mode.
	 * @throws SQLException
	 */
	boolean getAutoCommit() throws SQLException;

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
	void setAutoCommit(boolean autoCommit) throws SQLException;

	/**
	 * Makes all changes made since the previous commit/rollback permanent and
	 * releases any database locks currently held by this Connection object.
	 * This method should be used only when auto-commit mode has been disabled.
	 * 
	 * @throws SQLException
	 */
	void commit() throws SQLException;

	/**
	 * Undoes all changes made in the current transaction and releases any
	 * database locks currently held by this Connection object. This method
	 * should be used only when auto-commit mode has been disabled.
	 * 
	 * @throws SQLException
	 */
	void rollback() throws SQLException;

	/**
	 * Moves the cursor froward one row from its current position. A ResultSet
	 * cursor is initially positioned before the first row; the first call to
	 * the method next makes the first row the current row; the second call
	 * makes the second row the current row, and so on.
	 * 
	 * @return
	 * @throws SQLException
	 */
	boolean next() throws SQLException;
}