package org.tbg.ip;

import java.util.Vector;

public interface TConsumer<T extends IpRangeInterface> {
  void addString(String paramString1, String paramString2);
  
  void addCidrString(String paramString);
  
  Vector<T> done();
  
  void clear();
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\TConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */