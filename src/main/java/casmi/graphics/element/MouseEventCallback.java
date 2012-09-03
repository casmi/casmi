package casmi.graphics.element;

/**
 * Callback interface for mouseEvent.
 * 
 * @author Y. Ban
 */
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
