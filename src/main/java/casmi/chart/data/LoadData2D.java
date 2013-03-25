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

package casmi.chart.data;

import java.io.IOException;
import java.net.URL;

import casmi.parser.CSV;

/**
 * LoadData2D Class.
 *
 * @author K. Nishimura
 */
public class LoadData2D {

    public static MatrixData2D load(URL filePath) {
        return loadCSVData(filePath);
    }

    public static MatrixData2D loadWithoutAxisName(URL filePath) {
        return loadCSVDataWithoutAxisName(filePath);
    }

    private static MatrixData2D loadCSVData(URL filePath) {
        MatrixData2D m = new MatrixData2D();
        int count = 0;

        try {
            CSV csv = new CSV(filePath);
            String[] col;

            while ((col = csv.readLine()) != null) {
                if (count == 0) {
                    m.setAxis(col[0], col[1]);
                } else {
                    m.setData(col[0], Double.parseDouble(col[1]));
                }
                count++;
            }
            csv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        m.calculate();
        return m;
    }

    private static MatrixData2D loadCSVDataWithoutAxisName(URL filePath) {

        MatrixData2D m = new MatrixData2D();

        try {
            CSV csv = new CSV(filePath);
            String[] col;

            while ((col = csv.readLine()) != null) {
                m.setData(col[0], Double.parseDouble(col[1]));
            }
            csv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        m.calculate();
        return m;
    }
}
