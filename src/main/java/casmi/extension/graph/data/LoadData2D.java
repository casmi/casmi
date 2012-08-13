/*   casmi examples
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package casmi.extension.graph.data;

import java.io.IOException;
import java.net.URL;

import casmi.parser.CSV;

/**
 * LoadData2D Class.
 * 
 * @author K. Nishimura
 */

public class LoadData2D {
	

	public static MatrixData2D load(URL filePath){
		return loadCSVData(filePath);
	}
	
	public static MatrixData2D loadWithoutAxisName(URL filePath){
		return loadCSVDataWithoutAxisName(filePath);
	}
	
	private static MatrixData2D loadCSVData(URL filePath) {
    	
    	MatrixData2D m = new MatrixData2D();
    	int count = 0;
    	
        try {
        	System.out.println("open " + filePath);
        	CSV csv = new CSV(filePath);
        	String[] col;
        	
            while ((col = csv.readLine()) != null) {
            	if(count == 0){
            		m.setAxis(col[0], col[1]);
            	}else{
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
        	System.out.println("open " + filePath);
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
