package org.tbg.exceptions;

public class EmptyWBLException extends Exception {
  private String _filename;
  
  public EmptyWBLException(String paramString) {
    super(paramString);
    this._filename = new String(paramString);
  }
  
  public String getFileName() {
    return this._filename;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\exceptions\EmptyWBLException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */