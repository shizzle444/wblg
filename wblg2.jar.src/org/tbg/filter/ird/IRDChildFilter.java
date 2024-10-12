package org.tbg.filter.ird;

import org.tbg.ip.IpRangeDescr;

public class IRDChildFilter implements IRDFilter {
  IpRangeDescr parent;
  
  public IRDChildFilter(String paramString) {
    int i = paramString.indexOf('-');
    if (i == -1) {
      this.parent = new IpRangeDescr(paramString);
    } else {
      this.parent = new IpRangeDescr(paramString.substring(0, i).trim(), paramString.substring(i + 1).trim(), "", "");
    } 
  }
  
  public boolean match(IpRangeDescr paramIpRangeDescr) {
    return this.parent.isInside(paramIpRangeDescr);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\ird\IRDChildFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */