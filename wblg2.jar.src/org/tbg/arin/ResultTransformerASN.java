package org.tbg.arin;

import java.util.StringTokenizer;
import java.util.Vector;
import org.tbg.cidr.ASN;

public class ResultTransformerASN {
  private StringTokenizer lines;
  
  private boolean rec256;
  
  public ResultTransformerASN(String paramString) {
    this.lines = new StringTokenizer(paramString, "\n");
    this.rec256 = false;
  }
  
  private ASN next() {
    String str1 = this.lines.nextToken();
    if (str1.indexOf("# This query resulted in more than 256 records") > -1)
      this.rec256 = true; 
    int i = str1.lastIndexOf(')');
    int j = str1.lastIndexOf('(');
    if (i == -1 || j == -1)
      return null; 
    String str2 = str1.substring(j + 1, i);
    if (!str2.matches("^AS\\d+$"))
      return null; 
    String str3 = str1.substring(0, j - 1);
    return new ASN(str2, str3);
  }
  
  public Vector<ASN> fetchAll() {
    Vector<ASN> vector = new Vector();
    while (this.lines.hasMoreTokens()) {
      ASN aSN = next();
      if (aSN != null)
        vector.add(aSN); 
    } 
    return vector;
  }
  
  public boolean getRec256() {
    return this.rec256;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\arin\ResultTransformerASN.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */