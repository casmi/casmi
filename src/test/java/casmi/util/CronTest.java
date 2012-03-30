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

package casmi.util;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author T. Takeuchi
 */
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
