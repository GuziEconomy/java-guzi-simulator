package guzi.exception;

import java.util.NoSuchElementException;

public class NoMoreGuzisException extends Exception {
  
  public NoMoreGuzisException() {
    super();
  }
  
  public NoMoreGuzisException(final NoSuchElementException e) {
    super(e);
  }
  
}
