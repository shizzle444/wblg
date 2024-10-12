package org.tbg.util;

import java.io.IOException;
import java.lang.reflect.Method;
import org.tbg.gui.WBLGPanel;
import org.tbg.ip.IpRangeDescr;
import org.tbg.rpsl.IRD_Consumer;

public class Browser {
  public static void openUrl(String paramString) throws IOException {
    Configuration configuration = Configuration.getInstance();
    String str = System.getProperty("os.name");
    if (str.startsWith("Mac OS")) {
      try {
        Class<?> clazz = Class.forName("com.apple.eio.FileManager");
        Method method = clazz.getDeclaredMethod("openURL", new Class[] { String.class });
        method.invoke(null, new Object[] { paramString });
      } catch (Exception exception) {
        (WBLGPanel.getInstance()).sap.reportException(exception);
      } 
    } else if (str.startsWith("Windows")) {
      Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + paramString);
    } else {
      String str1 = configuration.getProp("browser", "firefox");
      if (str1.equals("opera")) {
        String str2 = "openURL(" + paramString + ", new-page)";
        Runtime.getRuntime().exec(new String[] { str1, "--remote", str2 });
      } else {
        Runtime.getRuntime().exec(new String[] { str1, paramString });
      } 
    } 
  }
  
  public static class Consumer implements IRD_Consumer {
    private String base;
    
    public Consumer(String param1String) {
      this.base = new String(param1String);
    }
    
    public void consume(IpRangeDescr param1IpRangeDescr) {
      try {
        String str = this.base + param1IpRangeDescr.getStartString();
        Browser.openUrl(str);
      } catch (Exception exception) {
        (WBLGPanel.getInstance()).sap.reportException(exception);
      } 
    }
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\Browser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */