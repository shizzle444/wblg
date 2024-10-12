package org.tbg.filter;

import java.util.Vector;
import org.tbg.util.Bitap;

public class FuzzyFilter implements WBLLoaderFilter {
  private Vector<String> data;
  
  private int size;
  
  private float bapThreshold;
  
  private boolean bapSubstring;
  
  public FuzzyFilter(Vector<String> paramVector, float paramFloat, boolean paramBoolean) {
    this.size = paramVector.size();
    this.data = new Vector<>(this.size);
    for (byte b = 0; b < this.size; b++)
      this.data.add(((String)paramVector.elementAt(b)).toLowerCase()); 
    this.bapThreshold = paramFloat;
    this.bapSubstring = paramBoolean;
  }
  
  public void clear() {
    this.data.clear();
    this.size = this.data.size();
  }
  
  private int locate(String paramString1, String paramString2) {
    int j = paramString1.length();
    Bitap bitap = new Bitap(paramString1, paramString2);
    Bitap.setThreshold(this.bapThreshold);
    int i = bitap.locate(0);
    int k = 0;
    while (i == -1) {
      k = paramString1.indexOf(' ', k);
      if (k == -1 || k + 1 >= j)
        break; 
      i = bitap.locate(++k);
    } 
    return i;
  }
  
  public int match(String paramString) {
    for (byte b = 0; b < this.size; b++) {
      int i;
      String str = paramString.toLowerCase();
      if (this.bapSubstring) {
        i = locate(str, this.data.elementAt(b));
      } else {
        Bitap bitap = new Bitap(str, this.data.elementAt(b));
        Bitap.setThreshold(this.bapThreshold);
        i = bitap.locate(0);
      } 
      if (i > -1)
        return i; 
    } 
    return -1;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\FuzzyFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */