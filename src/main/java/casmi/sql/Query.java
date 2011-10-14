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

import java.util.ArrayList;
import java.util.List;

public class Query {

    private List<String> selects = new ArrayList<String>();
    private  String where;
    private  String group;
    private  String order;
    private boolean desc; 
    private     int limit;
    
    private boolean selectEnable;
    private boolean whereEnable;
    private boolean groupEnable;
    private boolean orderEnable;
    private boolean limitEnable;
    
    public Query select(String... fields) {
        
        for (String f : fields) {
            selects.add(f);
        }
        selectEnable = true;
        return this;
    }
    
    public Query where(String where) {
        
        this.where = where;
        whereEnable = true;
        return this;
    }
    
    public Query andWhere(String where) {
        
        if (whereEnable) {
            this.where += " AND " + where; 
        } else {
            where(where);
        }
        return this;
    }
    
    public Query orWhere(String where) {
        
        if (whereEnable) {
            this.where += " OR " + where; 
        } else {
            where(where);
        }
        return this;
    }
    
    public Query group(String field) {
         
        group = field;
        groupEnable = true;
        return this;
    }
    
    public Query order(String field) {
        
        order = field;
        orderEnable = true;
        return this;
    }
    
    public Query desc(boolean desc) {
        
        this.desc = desc;
        return this;
    }
    
    public Query limit(int num) {
        
        limit = num;
        limitEnable = true;
        return this;
    }
    
    String[] getSelects() {
    
        return selects.toArray(new String[selects.size()]);
    }

    String getWhere() {
    
        return where;
    }

    String getGroup() {
    
        return group;
    }

    String getOrder() {
    
        return order;
    }
    
    boolean isDesc() {
        
        return desc;
    }

    int getLimit() {
    
        return limit;
    }

    boolean isSelectEnable() {
    
        return selectEnable;
    }

    boolean isWhereEnable() {
    
        return whereEnable;
    }

    boolean isGroupEnable() {
    
        return groupEnable;
    }

    boolean isOrderEnable() {
    
        return orderEnable;
    }
    
    boolean isLimitEnable() {
    
        return limitEnable;
    }
}
