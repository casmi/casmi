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

package casmi.parser;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

public class CSVTest {

    private static final String EXAMPLE_CSV = CSVTest.class.getResource("example.csv").getPath();
    private static final String WRITE_EXAMPLE_CSV = CSVTest.class.getResource("write_example.csv").getPath();
    
    @Test
    public void readLineTest() {

        CSV csv = new CSV(EXAMPLE_CSV);

        try {
            String[] line;
            while ((line = csv.readLine()) != null) {
                for (String str : line) {
                    System.out.print(str + ", ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to read next line.");
        } finally {
            csv.close();
        }
    }

    @Test
    public void writeLineTest() {

        CSV csv = new CSV(WRITE_EXAMPLE_CSV);

        try {
            csv.writeLine("Urakasumi", "15", "Miyagi");

            String[] line = {"Houhai", "16", "Aomori"};
            csv.writeLine(line);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to write next line.");
        } finally {
            csv.close();
        }
    }
}
