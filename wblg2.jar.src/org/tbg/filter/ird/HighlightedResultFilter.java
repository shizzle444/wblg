package org.tbg.filter.ird;

import org.tbg.ip.IpRangeDescr;

public class HighlightedResultFilter implements IRDFilter {
  public boolean match(IpRangeDescr paramIpRangeDescr) {
    return (paramIpRangeDescr.isHighlighted() > 0);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\ird\HighlightedResultFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */