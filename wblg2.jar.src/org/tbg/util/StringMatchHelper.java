package org.tbg.util;

import java.util.ArrayList;
import java.util.Collection;

public class StringMatchHelper {
  private String match = "";
  
  private ArrayList<Rect> entries = new ArrayList<>();
  
  public StringMatchHelper(Collection<String> paramCollection) {
    for (String str : paramCollection) {
      str = str.trim();
      if (str.endsWith(":")) {
        this.entries.add(new Rect(str));
        continue;
      } 
      this.entries.add(new Rect(str + ":"));
    } 
  }
  
  public boolean matches(String paramString) {
    for (Rect rect : this.entries) {
      if (paramString.indexOf(rect.s) == 0) {
        this.match = paramString.substring(rect.len).trim();
        return true;
      } 
    } 
    return false;
  }
  
  public String getMatch() {
    return this.match;
  }
  
  class Rect {
    public String s;
    
    public int len;
    
    public Rect(String param1String) {
      this.s = param1String;
      this.len = param1String.length();
    }
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\StringMatchHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */