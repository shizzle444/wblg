package org.tbg.wbl;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;
import org.tbg.exceptions.EmptyWBLException;
import org.tbg.ip.IpRangeDescr;
import org.tbg.rpsl.IRD_Consumer;

public class WBLWriter implements IRD_Consumer {
  private GZIPOutputStream out;
  
  private DataOutputStream dout;
  
  private String filename;
  
  private int counter;
  
  public WBLWriter(String paramString) {
    this.filename = paramString;
    this.counter = 0;
    try {
      this.out = new GZIPOutputStream(new FileOutputStream(this.filename));
      this.dout = new DataOutputStream(this.out);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void consume(IpRangeDescr paramIpRangeDescr) {
    try {
      this.dout.writeBytes(paramIpRangeDescr.toString().concat("\n"));
      this.counter++;
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void done() throws EmptyWBLException {
    try {
      this.out.finish();
      this.out.close();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    if (this.counter == 0)
      throw new EmptyWBLException(this.filename); 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\wbl\WBLWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */