package org.tbg.filter.ird;

import org.tbg.ip.IpRangeDescr;

public class BlockedResultFilter implements IRDFilter {
  private final byte val;
  
  private final boolean negate;
  
  public BlockedResultFilter(int paramInt) {
    this.val = (byte)paramInt;
    this.negate = false;
  }
  
  public BlockedResultFilter(int paramInt, boolean paramBoolean) {
    this.val = (byte)paramInt;
    this.negate = paramBoolean;
  }
  
  public boolean match(IpRangeDescr paramIpRangeDescr) {
    return (((paramIpRangeDescr.getBlockState() == this.val)) != this.negate);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\ird\BlockedResultFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */