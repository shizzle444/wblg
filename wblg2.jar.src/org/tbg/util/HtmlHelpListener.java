package org.tbg.util;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.tbg.gui.WBLGPanel;

public class HtmlHelpListener implements HyperlinkListener {
  public void hyperlinkUpdate(HyperlinkEvent paramHyperlinkEvent) {
    WBLGPanel wBLGPanel = WBLGPanel.getInstance();
    if (paramHyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
      try {
        System.out.println(paramHyperlinkEvent.getURL().toString());
        if (wBLGPanel.jep != null)
          wBLGPanel.jep.setPage(paramHyperlinkEvent.getURL()); 
      } catch (Exception exception) {
        wBLGPanel.sap.reportException(exception);
      }  
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\HtmlHelpListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */