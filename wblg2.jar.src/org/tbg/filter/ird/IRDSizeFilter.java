package org.tbg.filter.ird;

import org.tbg.ip.IpRangeDescr;

public class IRDSizeFilter implements IRDFilter {
  private long min;
  
  private long max;
  
  public IRDSizeFilter(int paramInt1, int paramInt2) {
    this.min = (long)Math.pow(2.0D, (32 - paramInt1)) - 1L;
    this.max = (long)Math.pow(2.0D, (32 - paramInt2)) - 1L;
  }
  
  public boolean match(IpRangeDescr paramIpRangeDescr) {
    long l = paramIpRangeDescr.getEnd() - paramIpRangeDescr.getStart();
    return (l >= this.min && l <= this.max);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\ird\IRDSizeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */