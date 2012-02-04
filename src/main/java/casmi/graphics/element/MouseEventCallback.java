package casmi.graphics.element;

public interface MouseEventCallback {

    public enum MouseOverTypes {
 
        ENTERED,

        EXISTED,
        
        EXITED
    }

    public enum MouseClickTypes {

        CLICKED,
        
        PRESSED,
        
        RELEASED,
        
        DRAGGED,
        
        MOVED
    }

}
