package org.tbg.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.tbg.gui.MyInputDialog;
import org.tbg.gui.WBLGPanel;
import org.tbg.threads.WBLChildSearchThread;
import org.tbg.util.Configuration;

public class SingleDBChildAction extends AbstractAction {
  String filename;
  
  boolean blockListFormat;
  
  public SingleDBChildAction(String paramString1, String paramString2, boolean paramBoolean) {
    super(paramString1);
    this.filename = paramString2;
    this.blockListFormat = paramBoolean;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    WBLGPanel wBLGPanel = WBLGPanel.getInstance();
    final MyInputDialog mid = new MyInputDialog((JFrame)SwingUtilities.windowForComponent((Component)wBLGPanel), "Search", "Enter parent range in CIDR or inet-range format");
    PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent param1PropertyChangeEvent) {
          try {
            String str = (String)mid.optionPane.getValue();
            if (str.equals(mid.btn1s)) {
              String str1 = mid.inputField.getText();
              if (str1.length() > 0) {
                Thread thread = new Thread((Runnable)new WBLChildSearchThread(str1, SingleDBChildAction.this.filename, SingleDBChildAction.this.blockListFormat));
                int i = Configuration.getInstance().getPropInt("thread_priority", Integer.toString(5));
                thread.setPriority(i);
                thread.start();
              } 
            } 
          } catch (Exception exception) {}
          mid.setVisible(false);
        }
      };
    myInputDialog.optionPane.addPropertyChangeListener(propertyChangeListener);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\action\SingleDBChildAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */