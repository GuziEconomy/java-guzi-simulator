package guzi.exception;

public class ImpossiblePaymentException extends Exception {
  
  public ImpossiblePaymentException(final NoMoreGuzisException e) {
    super(e);
  }
  
}
