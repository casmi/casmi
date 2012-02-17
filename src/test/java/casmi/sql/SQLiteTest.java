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
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import casmi.util.FileUtil;
import casmi.util.SystemUtil;

/**
 * Test class for SQLite.
 * 
 * @author T. Takeuchi
 * 
 */
public class SQLiteTest {

    private static final String DATABASE_PATH = SystemUtil.JAVA_TMP_PATH + "test.sqlite3"; 
    private static SQLite sqlite = null;

    public static void cleanup() {

        FileUtil.delete(new File(DATABASE_PATH));
    }

    @BeforeClass
    public static void beforeClass() {

        cleanup();

        try {
            SQLite.createDatabase(DATABASE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create database file.");
        }

        try {
            sqlite = new SQLite(DATABASE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed create instance");
        }
    }

    @AfterClass
    public static void afterClass() {

        sqlite.close();

        cleanup();
    }

    @Test(expected = SQLException.class)
    public void nonConnectionTest() throws SQLException {
        sqlite.execute("CREATE TABLE example (id INTEGER, text TEXT, date TEXT, value REAL)");
    }
    
    @Test
    public void connectTest() {
        try {
            sqlite.connect();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to connect.");
        } 
    }
    
    @Test
    public void createTableTest() {

        try {
            sqlite.execute("CREATE TABLE example (id INTEGER, text TEXT, date TEXT, value REAL)");
            sqlite.execute("INSERT INTO example VALUES (0, 'Test 1', '2011-07-15', 0.1)");
            sqlite.execute("INSERT INTO example VALUES (1, 'Test 2', '2011-07-15 13:24', 0.2)");
            sqlite.execute("INSERT INTO example VALUES (2, 'Test 3', '2011-07-15 13:25:36', 0.3)");

            sqlite.execute("CREATE TABLE example2 (id INTEGER, value REAL)");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Some Error Occurred");
        }
    }

    @Test
    public void selectTest() {

        try {
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

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Some Error Occurred");
        }
    }

    @Test
    public void insertTest() {

        try {
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
            
            sqlite.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Some Error Occurred");
        }
    }

    @Test
    public void orSaveTest() {

        Alcohol alcohol;
        
        alcohol = sqlite.entity(Alcohol.class);
        Assert.assertNotNull(alcohol);

        alcohol.setName("Urakasumi");
        alcohol.setAbv(15);
        alcohol.origin = "Miyagi";
        try {
            alcohol.save();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to save");
        }
        
        alcohol = sqlite.entity(Alcohol.class);
        Assert.assertNotNull(alcohol);

        alcohol.setName("Houhai");
        alcohol.setAbv(16);
        alcohol.origin = "Aomori";
        try {
            alcohol.save();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to save");
        }
    }

    @Test
    public void orFindTest() {
        
        Alcohol[] alcohols = null;
        Alcohol   alcohol  = null;
        
        // all
        try {
            alcohols = sqlite.all(Alcohol.class);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohols);
        for (Alcohol a : alcohols) {
            System.out.println(a);
        }
        
        // find
        try {
            alcohol = sqlite.find(Alcohol.class, 2);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohol);
        System.out.println(alcohol);
        
        // first
        try {
            alcohol = sqlite.first(Alcohol.class);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohol);
        System.out.println(alcohol);
        
        // last
        try {
            alcohol = sqlite.last(Alcohol.class);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohol);
        System.out.println(alcohol);
    }
    
    @Test
    public void orFindByQueryTest() {
        
        Alcohol[] alcohols = null;
        Alcohol   alcohol  = null;
        
        // select with a primary key
        try {
            alcohols = sqlite.all(Alcohol.class, new Query().select("name", "abv"));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohols);
        for (Alcohol a : alcohols) {
            System.out.println(a);
            
            Assert.assertNotNull(a.getName());
            Assert.assertNotNull(a.getAbv());
            Assert.assertNull(a.origin);
        }
        
        // select without a primary key
        try {
            alcohols = sqlite.all(Alcohol.class, new Query().select("abv", "origin"));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohols);
        for (Alcohol a : alcohols) {
            System.out.println(a);
            
            Assert.assertNull(a.getName());
            Assert.assertNotNull(a.getAbv());
            Assert.assertNotNull(a.origin);
        }
        
        // where
        try {
            alcohol = sqlite.first(Alcohol.class, new Query().where("abv=15"));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        System.out.println(alcohol);
        
        try {
            alcohol = sqlite.last(Alcohol.class, new Query().where("abv=15"));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        System.out.println(alcohol);
        
        // order by
        try {
            alcohols = sqlite.all(Alcohol.class, new Query().order("abv").desc(true));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohols);
        for (Alcohol a : alcohols) {
            System.out.println(a);
        }
    }

    @Test
    public void orDeleteTest() {

        Alcohol alcohol = null;
        
        try {
            alcohol = sqlite.find(Alcohol.class, 1);
            alcohol.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to delete.");
        }
    }
    
    @Test
    public void orTruncateTest() {
        
        try {
            sqlite.truncate(Alcohol.class);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to truncate.");
        }
    }
    
    @Test
    public void orAnnotationTest() {
        Alcohol2 alcohol;
        
        alcohol = sqlite.entity(Alcohol2.class);
        Assert.assertNotNull(alcohol);

        alcohol.setName("Urakasumi");
        alcohol.setAbv(15);
        alcohol.origin = "Miyagi";
        try {
            alcohol.save();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to save");
        }
        
        alcohol = sqlite.entity(Alcohol2.class);
        Assert.assertNotNull(alcohol);

        alcohol.setName("Houhai");
        alcohol.setAbv(16);
        alcohol.origin = "Aomori";
        try {
            alcohol.save();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to save");
        }
        
        Alcohol2[] alcohols = null;
        try {
            alcohols = sqlite.all(Alcohol2.class);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohols);
        for (Alcohol2 a : alcohols) {
            System.out.println(a);
        }
    }
}