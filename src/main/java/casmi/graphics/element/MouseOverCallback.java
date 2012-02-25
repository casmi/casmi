package casmi.graphics.element;


public interface MouseOverCallback extends MouseEventCallback{
	void run(MouseOverTypes eventtype, Element element);	
}
