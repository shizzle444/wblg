package org.tbg.util;

import org.tbg.ip.IpRangeDescr;

public class IRD_HighlightRemover implements IRD_Visitor {
  public void visit(IpRangeDescr paramIpRangeDescr) {
    paramIpRangeDescr.setHighlighted(0);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\IRD_HighlightRemover.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */