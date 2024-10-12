package org.tbg.filter.ird;

import org.tbg.ip.IpRangeDescr;

public class MatchAllFilter implements IRDFilter {
  private final byte val = 1;
  
  public boolean match(IpRangeDescr paramIpRangeDescr) {
    return (paramIpRangeDescr.getBlockState() != this.val);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\ird\MatchAllFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */