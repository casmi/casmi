package casmi.extension.coni.listener;

import casmi.extension.coni.CONI;
import casmi.matrix.Vertex;

public interface HandListener {

    void handCreate(CONI coni, int userID, Vertex position, float time);
    
    void handDestroy(CONI coni, int userID, float time);
    
    void handUpdate(CONI coni, int userID, Vertex position, float time);
}
