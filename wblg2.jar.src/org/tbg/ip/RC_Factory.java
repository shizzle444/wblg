package org.tbg.ip;

public interface RC_Factory<T> {
  T makeNew(String paramString);
  
  T makeNew(String paramString1, String paramString2, String paramString3);
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\RC_Factory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */