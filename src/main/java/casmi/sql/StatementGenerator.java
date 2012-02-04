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
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

class StatementGenerator {

    private static final Hashtable<List<Object>, String> TYPE_TABLE = 
        new Hashtable<List<Object>, String>();
    
    private static final Hashtable<List<Object>, String> STATEMENT_TABLE = 
        new Hashtable<List<Object>, String>();
    
    private enum SQLStatement {
        CREATE_TABLE,
        AUTO_INCREMENT,
        DROP,
        INSERT,
        UPDATE,
        DELETE,
        TRUNCATE,
        SELECT,
    }

    // Initialize TYPE_TABLE and STATEMENT_TABLE.
    static {
        // TYPE_TABLE ----------------------------------------------------------
        // SQLite3 
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,          null),     "NULL");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,     int.class),  "INTEGER");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3, Integer.class),  "INTEGER");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,   short.class),  "INTEGER");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,   Short.class),  "INTEGER");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,    long.class),  "INTEGER");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,    Long.class),  "INTEGER");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,   float.class),     "REAL");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,   Float.class),     "REAL");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,  double.class),     "REAL");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,  Double.class),     "REAL");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,  String.class),     "TEXT");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,    Date.class),     "TEXT");
        TYPE_TABLE.put(key1(SQLType.SQLITE_3,    Blob.class),     "BLOB");
        // MySQL5 
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,           null),     "NULL");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,      int.class),      "INT");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,  Integer.class),      "INT");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,    short.class),      "INT");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,    Short.class),      "INT");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,     long.class),      "INT");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,     Long.class),      "INT");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,    float.class),    "FLOAT");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,    Float.class),    "FLOAT");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,   double.class),   "DOUBLE");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,   Double.class),   "DOUBLE");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,   String.class),     "TEXT");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,     Date.class), "DATETIME");
        TYPE_TABLE.put(key1(SQLType.MYSQL_5,     Blob.class),     "BLOB");
        
        // SQL_TABLE -----------------------------------------------------------
        // SQLite3
        STATEMENT_TABLE.put(key2(SQLType.SQLITE_3, SQLStatement.CREATE_TABLE),   "CREATE TABLE :table (:fields)");
        STATEMENT_TABLE.put(key2(SQLType.SQLITE_3, SQLStatement.AUTO_INCREMENT), "AUTOINCREMENT");
        STATEMENT_TABLE.put(key2(SQLType.SQLITE_3, SQLStatement.DROP),           "DROP TABLE :table");
        STATEMENT_TABLE.put(key2(SQLType.SQLITE_3, SQLStatement.INSERT),         "INSERT INTO :table (:fields) VALUES (:values)");
        STATEMENT_TABLE.put(key2(SQLType.SQLITE_3, SQLStatement.UPDATE),         "UPDATE :table SET :sets WHERE :key=:key_value");
        STATEMENT_TABLE.put(key2(SQLType.SQLITE_3, SQLStatement.DELETE),         "DELETE FROM :table WHERE :where");
        STATEMENT_TABLE.put(key2(SQLType.SQLITE_3, SQLStatement.TRUNCATE),       "DELETE FROM :table");
        STATEMENT_TABLE.put(key2(SQLType.SQLITE_3, SQLStatement.SELECT),         "SELECT :selects FROM :table :query");
        // MySQL5
        STATEMENT_TABLE.put(key2(SQLType.MYSQL_5,  SQLStatement.CREATE_TABLE),   "CREATE TABLE :table (:fields)");
        STATEMENT_TABLE.put(key2(SQLType.MYSQL_5,  SQLStatement.AUTO_INCREMENT), "AUTO_INCREMENT");
        STATEMENT_TABLE.put(key2(SQLType.MYSQL_5,  SQLStatement.DROP),           "DROP TABLE :table");
        STATEMENT_TABLE.put(key2(SQLType.MYSQL_5,  SQLStatement.INSERT),         "INSERT INTO :table (:fields) VALUES (:values)");
        STATEMENT_TABLE.put(key2(SQLType.MYSQL_5,  SQLStatement.UPDATE),         "UPDATE :table SET :sets WHERE :key=:id");
        STATEMENT_TABLE.put(key2(SQLType.MYSQL_5,  SQLStatement.DELETE),         "DELETE FROM :table WHERE :where");
        STATEMENT_TABLE.put(key2(SQLType.MYSQL_5,  SQLStatement.TRUNCATE),       "TRUNCATE TABLE :table");
        STATEMENT_TABLE.put(key2(SQLType.MYSQL_5,  SQLStatement.SELECT),         "SELECT :selects FROM :table :query");
    }
    
    private static List<Object> key1(SQLType type, Class<?> cls) {

        List<Object> key = new ArrayList<Object>();
        key.add(type);
        key.add(cls);
        return key;
    }
    
    private static List<Object> key2(SQLType type, SQLStatement sqlStmt) {

        List<Object> key = new ArrayList<Object>();
        key.add(type);
        key.add(sqlStmt);
        return key;
    }

    static String type(SQLType type, Class<?> cls) {

        return TYPE_TABLE.get(key1(type, cls));
    }

    static String createTable(SQLType type, String tablename, String fields) {
       
        String stmt = STATEMENT_TABLE.get(key2(type, SQLStatement.CREATE_TABLE)); 
        stmt = stmt.replaceAll(":table", tablename);
        stmt = stmt.replaceAll(":fields", fields);
        return stmt;
    }
    
    static String createTable(Entity entity) {
        StringBuilder sb = new StringBuilder();
        
        boolean autoPrimaryKey = entity.autoPrimaryKey;
        Column primaryKey = entity.primaryKey;
        
        switch (entity.sql.getSQLType()) {
        case MYSQL_5:
        {
            if (autoPrimaryKey) {
                sb.append("id ");
                sb.append(StatementGenerator.type(SQLType.MYSQL_5, int.class));
                sb.append(' ');
                sb.append(StatementGenerator.autoIncrement(SQLType.MYSQL_5));
            } else {
                sb.append(entity.primaryKey.getField());
                sb.append(' ');
                sb.append(StatementGenerator.type(SQLType.MYSQL_5, entity.primaryKey.getType()));
            }
            
            for (Column column : entity.columns) {
                sb.append(',');
                sb.append(column.getField());
                sb.append(' ');
                sb.append(StatementGenerator.type(SQLType.MYSQL_5, column.getType()));
            }
            
            if (autoPrimaryKey) {
                sb.append(',');
                sb.append("PRIMARY KEY(id)");
            } else {
                sb.append(',');
                sb.append("PRIMARY KEY(");
                sb.append(primaryKey.getField());
                if (primaryKey.getType() == String.class) {
                    sb.append("(255)");
                }
                sb.append(')');
            }
            
            break;
        }
        case SQLITE_3:
        {
            if (autoPrimaryKey) {
                sb.append("id ");
                sb.append(StatementGenerator.type(SQLType.SQLITE_3, int.class));
                sb.append(" PRIMARY KEY ");
                sb.append(StatementGenerator.autoIncrement(SQLType.SQLITE_3));
            } else {
                sb.append(entity.primaryKey.getField());
                sb.append(' ');
                sb.append(StatementGenerator.type(SQLType.SQLITE_3, entity.primaryKey.getType()));
                sb.append(" PRIMARY KEY");
            }
            
            for (Column column : entity.columns) {
                sb.append(',');
                sb.append(column.getField());
                sb.append(' ');
                sb.append(StatementGenerator.type(SQLType.SQLITE_3, column.getType()));
            }
            
            break;
        }
        }
        
        return StatementGenerator.createTable(entity.sql.getSQLType(), entity.tablename, sb.toString());
    }
    
    static String autoIncrement(SQLType type) {
        return STATEMENT_TABLE.get(key2(type, SQLStatement.AUTO_INCREMENT));
    }
    
    static String drop(SQLType type, String tablename) {
        
        String stmt = STATEMENT_TABLE.get(key2(type, SQLStatement.DROP));
        stmt = stmt.replaceAll(":table", tablename);
        return stmt;
    }
    
    static String insert(SQLType type) {
        
        return STATEMENT_TABLE.get(key2(type, SQLStatement.INSERT));
    }
    
    static String update(SQLType type) {
        
        return STATEMENT_TABLE.get(key2(type, SQLStatement.UPDATE));
    }
    
    static String delete(SQLType type, String tablename, String where) {
        
        String stmt = STATEMENT_TABLE.get(key2(type, SQLStatement.DELETE));
        stmt = stmt.replaceAll(":table", tablename);
        stmt = stmt.replaceAll(":where", where);
        return stmt;
    }
    
    static String truncate(SQLType type, String tablename) {
        
        String stmt = STATEMENT_TABLE.get(key2(type, SQLStatement.TRUNCATE));
        stmt = stmt.replaceAll(":table", tablename);
        return stmt;
    }
    
    static String select(SQLType type) {
        
        return STATEMENT_TABLE.get(key2(type, SQLStatement.SELECT));
    }
}
