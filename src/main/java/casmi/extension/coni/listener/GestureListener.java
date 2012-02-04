package casmi.extension.coni.listener;

import casmi.extension.coni.CONI;
import casmi.extension.coni.Gesture;
import casmi.matrix.Vertex;

public interface GestureListener {

    void gestureRecognized(CONI coni, Gesture gesture, Vertex idPosition, Vertex endPosition);
    
    void gestureProgress(CONI coni, Gesture gesture, Vertex position, float progress);
}
