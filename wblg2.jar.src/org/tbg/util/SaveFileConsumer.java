package org.tbg.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Method;
import org.tbg.ip.IpRangeDescr;
import org.tbg.rpsl.IRD_Consumer;

public class SaveFileConsumer implements IRD_Consumer {
  private Method methodToCall;
  
  private static final Class[] noType = new Class[0];
  
  private BufferedWriter bw;
  
  public SaveFileConsumer(String paramString1, String paramString2) {
    try {
      this.methodToCall = IpRangeDescr.class.getMethod(paramString2, noType);
      this.bw = new BufferedWriter(new FileWriter(paramString1));
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void finish() {
    try {
      this.bw.close();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void consume(IpRangeDescr paramIpRangeDescr) {
    try {
      String str = (String)this.methodToCall.invoke(paramIpRangeDescr, new Object[0]);
      this.bw.write(str);
      this.bw.newLine();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\SaveFileConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */