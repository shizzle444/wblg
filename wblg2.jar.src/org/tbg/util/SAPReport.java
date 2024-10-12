package org.tbg.util;

public interface SAPReport {
  void reportStatus(String paramString);
  
  void reportError(String paramString);
  
  void reportException(Throwable paramThrowable);
  
  void reportProgress(int paramInt);
  
  boolean checkIfError();
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\SAPReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */