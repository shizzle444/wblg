package org.tbg.blocklist;

import java.io.IOException;
import java.io.Writer;
import org.tbg.gui.StatusAndProgress;
import org.tbg.gui.WBLGPanel;
import org.tbg.ip.IpRangeDescr;
import org.tbg.rpsl.IRD_Consumer;

public class BlocklistWriter implements IRD_Consumer {
  private Writer writer;
  
  private StatusAndProgress sap;
  
  public BlocklistWriter(Writer paramWriter) {
    this.writer = paramWriter;
    this.sap = (WBLGPanel.getInstance()).sap;
  }
  
  public void consume(IpRangeDescr paramIpRangeDescr) {
    try {
      this.writer.write(paramIpRangeDescr.toBlocklistEntry());
    } catch (IOException iOException) {
      this.sap.reportException(iOException);
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\blocklist\BlocklistWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */