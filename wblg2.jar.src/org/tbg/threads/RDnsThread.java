package org.tbg.threads;

import java.util.Vector;
import org.tbg.blocklist.BLTableModel;
import org.tbg.gui.WBLGPanel;
import org.tbg.ip.IpRangeDescr;
import org.tbg.util.Configuration;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.SimpleResolver;

public class RDnsThread implements Runnable {
  private Vector<IpRangeDescr> data;
  
  private SimpleResolver res;
  
  private int type;
  
  private int dclass;
  
  public RDnsThread(Vector<IpRangeDescr> paramVector) {
    this.data = paramVector;
    try {
      this.res = new SimpleResolver();
    } catch (Exception exception) {
      throw new RuntimeException("Failed to create Resolver" + exception.toString());
    } 
    this.type = 12;
    this.dclass = 1;
    int i = Configuration.getInstance().getPropInt("dns_timeout", "10");
    this.res.setTimeout(i);
  }
  
  private String lookup(String paramString) {
    try {
      Name name = ReverseMap.fromAddress(paramString);
      Record record = Record.newRecord(name, this.type, this.dclass);
      Message message1 = Message.newQuery(record);
      Message message2 = this.res.send(message1);
      StringBuilder stringBuilder = new StringBuilder();
      if (message2.getHeader().getRcode() == 0) {
        Record[] arrayOfRecord = message2.getSectionArray(1);
        for (byte b = 0; b < arrayOfRecord.length; b++) {
          String str = arrayOfRecord[b].rdataToString();
          if (str.endsWith(".")) {
            stringBuilder.append(str.substring(0, str.length() - 1));
          } else {
            stringBuilder.append(str);
          } 
          if (b < arrayOfRecord.length - 1)
            stringBuilder.append(", "); 
        } 
        return stringBuilder.toString();
      } 
    } catch (Exception exception) {
      (WBLGPanel.getInstance()).sap.reportException(exception);
    } 
    return paramString;
  }
  
  public void run() {
    WBLGPanel wBLGPanel = WBLGPanel.getInstance();
    wBLGPanel.setSearchEnabled(false);
    BLTableModel bLTableModel = (BLTableModel)wBLGPanel.resultTable.getModel();
    byte b = 0;
    wBLGPanel.sap.reportProgress(0);
    for (IpRangeDescr ipRangeDescr : this.data) {
      String str = lookup(ipRangeDescr.getStartString());
      ipRangeDescr.setDescription(str);
      wBLGPanel.sap.reportProgress(++b * 100 / this.data.size());
    } 
    wBLGPanel.setResults(this.data);
    wBLGPanel.setSearchEnabled(true);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\threads\RDnsThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */