package casmi.sql;

import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MySQLTest {

    private static final String HOST     = "localhost";
    private static final String DATABASE = "casmi";
    private static final String USER     = "casmi_user";
    private static final String PASSWORD = "casmi_password";
    
    private static MySQL mysql = null;
    
    public static void cleanup() {
        
        try {
            mysql.execute("DROP TABLE example");
        } catch (SQLException e) {
            // Ignore.
        }
        try {
            mysql.execute("DROP TABLE example2");
        } catch (SQLException e) {
            // Ignore.
        }
        try {
            mysql.execute("DROP TABLE " + mysql.getTablename(Alcohol.class));
        } catch (SQLException e) {
            // Ignore.
        }
    }
    
    @BeforeClass
    public static void beforeClass() {
        
        try {
            mysql = new MySQL(HOST, DATABASE, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to create instance");
        }
        try {
            mysql.connect();
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to connect database");
        }
        
    	cleanup();
    }
    
    @AfterClass
    public static void AfterClass() {
        
        mysql.close();
    }
    
    @Test
    public void createTableTest() {

        try {
            mysql.execute("CREATE TABLE example (id integer, text text, date datetime, value double)");
            mysql.execute("INSERT INTO example VALUES (0, 'Test 1', '2011-07-15', 0.1)");
            mysql.execute("INSERT INTO example VALUES (1, 'Test 2', '2011-07-15 13:24', 0.2)");
            mysql.execute("INSERT INTO example VALUES (2, 'Test 3', '2011-07-15 13:25:36', 0.3)");

            mysql.execute("CREATE TABLE example2 (id integer, value double)");
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to create table or insert data");
        }
    }

    @Test
    public void selectTest() {

        try {
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
            fail("failed to execute SELECT sentence");
        }
    }

    @Test
    public void insertTest() {

        try {
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
            
            mysql.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to insert data");
        }
    }
    
    @Test
    public void orSaveTest() {

        Alcohol alcohol;
        
        alcohol = mysql.entity(Alcohol.class);
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
        
        alcohol = mysql.entity(Alcohol.class);
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
            alcohols = mysql.all(Alcohol.class);
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
            alcohol = mysql.find(Alcohol.class, 2);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohol);
        System.out.println(alcohol);
        
        // first
        try {
            alcohol = mysql.first(Alcohol.class);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohol);
        System.out.println(alcohol);
        
        // last
        try {
            alcohol = mysql.last(Alcohol.class);
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
        
        // select
        try {
            alcohols = mysql.all(Alcohol.class, new Query().select("name", "abv"));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        Assert.assertNotNull(alcohols);
        for (Alcohol a : alcohols) {
            System.out.println(a);
            
            Assert.assertNotNull(a.getID());
            Assert.assertNotNull(a.getName());
            Assert.assertNotNull(a.getAbv());
            Assert.assertNull(a.origin);
        }
        
        // where
        try {
            alcohol = mysql.first(Alcohol.class, new Query().where("abv=15"));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        System.out.println(alcohol);
        
        try {
            alcohol = mysql.last(Alcohol.class, new Query().where("abv=15"));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to find.");
        }
        System.out.println(alcohol);
        
        // order by
        try {
            alcohols = mysql.all(Alcohol.class, new Query().order("abv").desc(true));
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
            alcohol = mysql.find(Alcohol.class, 1);
            alcohol.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to delete.");
        }
    }
    
    @Test
    public void orTruncateTest() {
        
        try {
            mysql.truncate(Alcohol.class);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to truncate.");
        }
    }
    
    @Test
    public void orDropTest() {
        
        try {
            mysql.drop(Alcohol.class);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to drop table.");
        }
    }
    
}
