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

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

/**
 * @author T. Takeuchi
 */
public class DateUtilTest {

    @Test
    public void getTest() {

        String out;
        int start = DateUtil.millis();
        int elipse;
        
        sleep(1000);
        
        elipse = DateUtil.millis() - start;
        out = "[" + elipse + "] second: " + DateUtil.second();
        System.out.println(out);

        sleep(1000);

        elipse = DateUtil.millis() - start;
        out = "[" + elipse + "] minute: " + DateUtil.minute();
        System.out.println(out);
        
        sleep(1000);

        elipse = DateUtil.millis() - start;
        out = "[" + elipse + "] hour: " + DateUtil.hour();
        System.out.println(out);

        sleep(1000);
        
        elipse = DateUtil.millis() - start;
        out = "[" + elipse + "] day: " + DateUtil.day();
        System.out.println(out);

        sleep(1000);
        
        elipse = DateUtil.millis() - start;
        out = "[" + elipse + "] month: " + DateUtil.month();
        System.out.println(out);

        sleep(1000);
        
        elipse = DateUtil.millis() - start;
        out = "[" + elipse + "] year: " + DateUtil.year();
        System.out.println(out);
    }

    private void sleep(long millis) {

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void formatAndParseTest() {
        
        // Format.
        Date date = new Date();
        String dateStr = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        System.out.println(dateStr);
        
        // Parse.
        try {
            date = DateUtil.parse(dateStr, "yyyy-MM-dd HH:mm:ss");
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
    }
}
