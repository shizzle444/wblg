package org.tbg.util;

public class RangeTooBigException extends Exception {
  public RangeTooBigException(String paramString) {
    super("Requested range: " + paramString + " is larger than the maximum supported range: /8");
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\RangeTooBigException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */