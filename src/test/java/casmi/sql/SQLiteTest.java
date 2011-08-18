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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SQLiteTest {

	public static void cleanup() {
		File file = new File("/tmp/test.sqlite3");
		if( file != null ) {
			file.delete();
		}
	}
	
	@BeforeClass
	public static void beforeClass() {
		cleanup();
		
		try {
            SQLite.createDatabase("/tmp/test.sqlite3");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create database file.");
        }
	}
	
	@AfterClass
	public static void afterClass() {
		cleanup();	
	}
    
    @Test
    public void createTableTest() {
        SQLite sqlite = null;

        try {
            sqlite = new SQLite("/tmp/test.sqlite3");
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed create instance");
        }

        try {
            sqlite.connect();

            sqlite.execute("CREATE TABLE example (id INTEGER, text TEXT, date TEXT, value REAL)");
            sqlite.execute("INSERT INTO example VALUES (0, 'Test 1', '2011-07-15', 0.1)");
            sqlite.execute("INSERT INTO example VALUES (1, 'Test 2', '2011-07-15 13:24', 0.2)");
            sqlite.execute("INSERT INTO example VALUES (2, 'Test 3', '2011-07-15 13:25:36', 0.3)");
            
            sqlite.execute("CREATE TABLE example2 (id INTEGER, value REAL)");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Some Error Occurred");
        } finally {
            sqlite.close();
        }
    }

    @Test
    public void selectTest() {

        SQLite sqlite = null;

        try {
            sqlite = new SQLite("/tmp/test.sqlite3");
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed create instance");
        }

        try {
            sqlite.connect();

            sqlite.execute("SELECT * FROM example");

            while (sqlite.next()) {
                int id = sqlite.getInt(1);
                String text = sqlite.getString("text");
                java.util.Date date = sqlite.getDate(3);
                float value = sqlite.getFloat("value");

                Assert.assertTrue(id >= 0);
                Assert.assertNotNull(text);
                Assert.assertNotNull(date);

                System.out.println(id + " | " + text + " | " + date + " | " + value);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Some Error Occurred");
        } finally {
            sqlite.close();
        }
    }

    @Test
    public void insertTest() {

        SQLite sqlite = null;

        try {
            sqlite = new SQLite("/tmp/test.sqlite3");
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed create instance");
        }

        try {
            sqlite.connect();

            sqlite.execute("DELETE FROM example2");

            sqlite.setAutoCommit(false);

            for (int i = 0; i < 10; i++) {
                sqlite.execute("INSERT INTO example2 VALUES (" + i + ", " + i / 100.0 + ")");
            }
            sqlite.commit();

            for (int i = 10; i < 20; i++) {
                sqlite.execute("INSERT INTO example2 VALUES (" + i + ", " + i / 100.0 + ")");
            }
            sqlite.rollback();

            sqlite.execute("SELECT * FROM example2");
            while (sqlite.next()) {
                sqlite.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Some Error Occurred");
        } finally {
            sqlite.close();
        }
    }
}
