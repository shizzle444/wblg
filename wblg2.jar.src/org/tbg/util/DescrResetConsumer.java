package org.tbg.util;

import org.tbg.ip.IpRangeDescr;
import org.tbg.rpsl.IRD_Consumer;

public class DescrResetConsumer implements IRD_Consumer {
  public void consume(IpRangeDescr paramIpRangeDescr) {
    paramIpRangeDescr.resetBestMatch();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\DescrResetConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */