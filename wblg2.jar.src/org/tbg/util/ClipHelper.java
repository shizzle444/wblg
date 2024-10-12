package org.tbg.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.Method;
import org.tbg.ip.IpRangeDescr;
import org.tbg.rpsl.IRD_Consumer;

public class ClipHelper implements ClipboardOwner {
  private static ClipHelper instance = null;
  
  private ClipHelper() {
    instance = this;
  }
  
  public static ClipHelper getInstance() {
    return (instance != null) ? instance : new ClipHelper();
  }
  
  public void setContent(String paramString) {
    StringSelection stringSelection = new StringSelection(paramString);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, this);
  }
  
  public String getContent() {
    String str = "";
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable transferable = clipboard.getContents(null);
    boolean bool = (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) ? true : false;
    if (bool)
      try {
        str = (String)transferable.getTransferData(DataFlavor.stringFlavor);
      } catch (UnsupportedFlavorException unsupportedFlavorException) {
        unsupportedFlavorException.printStackTrace();
      } catch (IOException iOException) {
        iOException.printStackTrace();
      }  
    return str;
  }
  
  public void lostOwnership(Clipboard paramClipboard, Transferable paramTransferable) {}
  
  public static class Consumer implements IRD_Consumer {
    private Method methodToCall;
    
    private StringBuilder sb;
    
    private String nl;
    
    private boolean isFirstEntry;
    
    private static final Class[] noType = new Class[0];
    
    public Consumer(String param1String) {
      try {
        this.methodToCall = IpRangeDescr.class.getMethod(param1String, noType);
        this.sb = new StringBuilder();
        this.nl = System.getProperty("line.separator");
        this.isFirstEntry = true;
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
    }
    
    public void finish() {
      String str = this.sb.toString();
      ClipHelper.getInstance().setContent(str);
    }
    
    public void consume(IpRangeDescr param1IpRangeDescr) {
      try {
        String str = (String)this.methodToCall.invoke(param1IpRangeDescr, new Object[0]);
        if (!this.isFirstEntry)
          this.sb.append(this.nl); 
        this.sb.append(str);
        this.isFirstEntry = false;
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
    }
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\ClipHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */