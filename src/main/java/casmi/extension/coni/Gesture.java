package casmi.extension.coni;

public enum Gesture {

    WAVE,
    
    CLICK,
    
    RAISE_HAND,
    
    MOVING_HAND;
    
    static final String toString(Gesture gesture) {
        switch(gesture) {
        case WAVE:
            return "Wave";
        case CLICK:
            return "Click";
        case RAISE_HAND:
            return "RaiseHand";
        case MOVING_HAND:
            return "MovingHand";
        }
        
        return null; // dummy
    }
    
    static final String toString(Gesture[] gestures) {
        StringBuilder sb = new StringBuilder();
        for (Gesture g : gestures) {
            sb.append(toString(g));
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    static final Gesture toGesture(String gestureStr) {
        if (gestureStr.equals("Wave")) {
            return Gesture.WAVE;
        } else if (gestureStr.equals("Click")) {
            return Gesture.CLICK;
        } else if (gestureStr.equals("RaiseHand")) {
            return Gesture.RAISE_HAND;
        } else if (gestureStr.equals("MovingHand")) {
            return Gesture.MOVING_HAND;
        }
        
        return null;
    }
}
