package org.tbg.filter;

import java.util.Vector;

public class BasicFilter implements WBLLoaderFilter {
  private Vector<String> data;
  
  private int size;
  
  public BasicFilter(Vector<String> paramVector) {
    this.size = paramVector.size();
    this.data = new Vector<>(this.size);
    for (byte b = 0; b < this.size; b++)
      this.data.add(((String)paramVector.elementAt(b)).toLowerCase()); 
  }
  
  public void clear() {
    this.data.clear();
    this.size = this.data.size();
  }
  
  public int match(String paramString) throws OutOfMemoryError {
    for (byte b = 0; b < this.size; b++) {
      String str = paramString.toLowerCase();
      int i = str.indexOf(this.data.elementAt(b));
      if (i > -1)
        return i; 
    } 
    return -1;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\BasicFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */