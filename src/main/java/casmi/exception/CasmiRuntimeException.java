package casmi.exception;

public class CasmiRuntimeException extends java.lang.RuntimeException {

   /**
    * For subclasses only.
    */
   protected CasmiRuntimeException() {}

   /**
    * @param message
    */
   public CasmiRuntimeException(String message) {
       
       super(message);
   }

   /**
    * @param cause
    */
   public CasmiRuntimeException(Throwable cause) {
       
       super(cause);
   }

   /**
    * @param message
    * @param cause
    */
   public CasmiRuntimeException(String message, Throwable cause) {
       
       super(message, cause);
   }
}
