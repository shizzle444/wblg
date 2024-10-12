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

public class ArinChildAction extends AbstractAction {
  public ArinChildAction(String paramString) {
    super(paramString);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    final WBLGPanel wblg = WBLGPanel.getInstance();
    if (WBLGPanel.arinChildDialogOn)
      return; 
    WBLGPanel.arinChildDialogOn = true;
    final MyInputDialog mid = new MyInputDialog((JFrame)SwingUtilities.windowForComponent((Component)wBLGPanel), "Search", "Enter parent range in CIDR or inet-range format");
    PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent param1PropertyChangeEvent) {
          try {
            String str = (String)mid.optionPane.getValue();
            if (str.equals(mid.btn1s)) {
              String str1 = mid.inputField.getText();
              if (str1.length() > 0)
                ArinChildAction.this.runSearch(str1); 
            } 
          } catch (Exception exception) {}
          mid.setVisible(false);
          WBLGPanel.arinChildDialogOn = false;
        }
      };
    myInputDialog.optionPane.addPropertyChangeListener(propertyChangeListener);
  }
  
  public void runSearch(String paramString) {
    Thread thread = new Thread((Runnable)new WBLChildSearchThread(paramString));
    int i = Configuration.getInstance().getPropInt("thread_priority", Integer.toString(5));
    thread.setPriority(i);
    thread.start();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\action\ArinChildAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */