package org.tbg.util;

import org.tbg.ip.IpRangeDescr;
import org.tbg.rpsl.IRD_Consumer;

public class IRD_ChangeDescription implements IRD_Consumer {
  private String newDescription;
  
  public IRD_ChangeDescription(String paramString) {
    this.newDescription = new String(paramString);
  }
  
  public void consume(IpRangeDescr paramIpRangeDescr) {
    paramIpRangeDescr.setDescription(this.newDescription);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\IRD_ChangeDescription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */