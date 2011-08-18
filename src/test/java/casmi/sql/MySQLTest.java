package casmi.sql;

import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MySQLTest {

    static final String HOST = "localhost";
    static final String DATABASE = "casmi";
    static final String USER = "hoge";
    static final String PASSWORD = "hoge";
    
    public static void cleanup() {
        MySQL mysql = new MySQL(HOST, DATABASE, USER, PASSWORD);
        try {
            mysql.connect();
            mysql.execute("DROP TABLE example");
            mysql.execute("DROP TABLE example2");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysql.close();
        }
    }
    
    @BeforeClass
    public static void beforeClass() {
        cleanup();
    }
    
    @Test
    public void createTableTest() {
        MySQL mysql = null;

        try {
            mysql = new MySQL(HOST, DATABASE, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed create instance");
        }

        try {
            mysql.connect();

            mysql.execute("CREATE TABLE example (id integer, text text, date datetime, value double)");
            mysql.execute("INSERT INTO example VALUES (0, 'Test 1', '2011-07-15', 0.1)");
            mysql.execute("INSERT INTO example VALUES (1, 'Test 2', '2011-07-15 13:24', 0.2)");
            mysql.execute("INSERT INTO example VALUES (2, 'Test 3', '2011-07-15 13:25:36', 0.3)");

            mysql.execute("CREATE TABLE example2 (id integer, value double)");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Some Error Occurred");
        } finally {
            mysql.close();
        }
    }

    @Test
    public void selectTest() {

        MySQL mysql = null;

        try {
            mysql = new MySQL(HOST, DATABASE, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed create instance");
        }

        try {
            mysql.connect();

            mysql.execute("SELECT * FROM example");

            while (mysql.next()) {
                int id = mysql.getInt(1);
                String text = mysql.getString("text");
                java.util.Date date = mysql.getDate(3);
                float value = mysql.getFloat("value");

                Assert.assertTrue(id >= 0);
                Assert.assertNotNull(text);
                Assert.assertNotNull(date);

                System.out.println(id + " | " + text + " | " + date + " | " + value);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Some Error Occurred");
        } finally {
            mysql.close();
        }
    }

    @Test
    public void insertTest() {

        MySQL mysql = null;

        try {
            mysql = new MySQL(HOST, DATABASE, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed create instance");
        }

        try {
            mysql.connect();

            mysql.execute("DELETE FROM example2");

            mysql.setAutoCommit(false);

            for (int i = 0; i < 10; i++) {
                mysql.execute("INSERT INTO example2 VALUES (" + i + ", " + i / 100.0 + ")");
            }
            mysql.commit();

            for (int i = 10; i < 20; i++) {
                mysql.execute("INSERT INTO example2 VALUES (" + i + ", " + i / 100.0 + ")");
            }
            mysql.rollback();

            mysql.execute("SELECT * FROM example2");
            while (mysql.next()) {
                mysql.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Some Error Occurred");
        } finally {
            mysql.close();
        }
    }

}
