package casmi.util;

import static org.junit.Assert.fail;

import org.junit.Test;

public class CronTest {

    @Test
    public void test() {

        Cron cron = new Cron("* * * * *", new Runnable() {

            int cnt = 0;

            @Override
            public void run() {
                System.out.println("cron says " + cnt++);
            }
        });

        System.out.println(cron);
        
        cron.start();

        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("Sleep failed.");
        }

        cron.stop();
    }

}
