package org.tbg.util;

import org.tbg.ip.IpRangeDescr;
import org.tbg.rpsl.IRD_Consumer;

public class MarkEntryConsumer implements IRD_Consumer {
  public static final byte SET = 0;
  
  public static final byte UNSET = 1;
  
  public static final byte TOGGLE = 2;
  
  private final byte val;
  
  public MarkEntryConsumer(byte paramByte) {
    this.val = paramByte;
  }
  
  public void consume(IpRangeDescr paramIpRangeDescr) {
    switch (this.val) {
      case 0:
        paramIpRangeDescr.setSelected(true);
        break;
      case 1:
        paramIpRangeDescr.setSelected(false);
        break;
      case 2:
        paramIpRangeDescr.setSelected(!paramIpRangeDescr.getSelected());
        break;
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\MarkEntryConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */