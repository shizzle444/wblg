package org.tbg.filter.ird;

import java.util.ArrayList;
import org.tbg.ip.IpRangeDescr;

public class SecondaryFilterChain implements IRDFilter {
  private ArrayList<IRDFilter> chain = new ArrayList<>();
  
  public SecondaryFilterChain() {}
  
  public SecondaryFilterChain(IRDFilter paramIRDFilter) {
    this();
    this.chain.add(paramIRDFilter);
  }
  
  public void addFilter(IRDFilter paramIRDFilter) {
    this.chain.add(paramIRDFilter);
  }
  
  public boolean match(IpRangeDescr paramIpRangeDescr) {
    for (IRDFilter iRDFilter : this.chain) {
      if (!iRDFilter.match(paramIpRangeDescr))
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\ird\SecondaryFilterChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */