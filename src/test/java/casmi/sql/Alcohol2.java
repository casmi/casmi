package casmi.sql;

import casmi.sql.annotation.Fieldname;
import casmi.sql.annotation.Ignore;
import casmi.sql.annotation.PrimaryKey;
import casmi.sql.annotation.Tablename;

@Tablename("alcohol_table")
public class Alcohol2 extends Entity {

    @PrimaryKey
    private String name;
    
    @Fieldname("alcohol_by_volume")
    private int abv;
    
    public String origin;
    
    @Ignore
    public int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAbv() {
        return abv;
    }

    public void setAbv(int abv) {
        this.abv = abv;
    }
}
