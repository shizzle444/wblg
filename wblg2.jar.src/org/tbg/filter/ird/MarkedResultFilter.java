package org.tbg.filter.ird;

import org.tbg.ip.IpRangeDescr;

public class MarkedResultFilter implements IRDFilter {
  public boolean match(IpRangeDescr paramIpRangeDescr) {
    return paramIpRangeDescr.getSelected();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\ird\MarkedResultFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */