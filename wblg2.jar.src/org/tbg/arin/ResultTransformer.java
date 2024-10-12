package org.tbg.arin;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.tbg.ip.IpRangeDescr;

public class ResultTransformer {
  private StringTokenizer lines;
  
  private Pattern ipRangePattern;
  
  private boolean rec256;
  
  public ResultTransformer(String paramString) {
    this.lines = new StringTokenizer(paramString, "\n");
    this.ipRangePattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} ?\\- ?\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    this.rec256 = false;
  }
  
  public boolean hasMoreTokens() {
    return this.lines.hasMoreTokens();
  }
  
  public IpRangeDescr next() {
    return this.lines.hasMoreTokens() ? splitLine(this.lines.nextToken()) : null;
  }
  
  public Vector<IpRangeDescr> fetchAll() {
    Vector<IpRangeDescr> vector = new Vector();
    while (this.lines.hasMoreTokens()) {
      IpRangeDescr ipRangeDescr = next();
      if (ipRangeDescr != null)
        vector.add(ipRangeDescr); 
    } 
    return vector;
  }
  
  public boolean getRec256() {
    return this.rec256;
  }
  
  private IpRangeDescr splitLine(String paramString) {
    if (paramString.indexOf("# This query resulted in more than 256 records") > -1)
      this.rec256 = true; 
    int i = paramString.lastIndexOf(')');
    int j = paramString.lastIndexOf('(');
    if (i == -1 || j == -1)
      return null; 
    String str1 = paramString.substring(i + 2);
    Matcher matcher = this.ipRangePattern.matcher(str1);
    if (!matcher.matches())
      return null; 
    int k = str1.indexOf(" - ");
    if (k == -1)
      return null; 
    String str2 = paramString.substring(0, j - 1);
    int m = str2.lastIndexOf(' ');
    return new IpRangeDescr(str1.substring(0, k), str1.substring(k + 3), str2.substring(0, m), str2.substring(m + 1));
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\arin\ResultTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */