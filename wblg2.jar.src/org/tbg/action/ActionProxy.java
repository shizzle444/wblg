package org.tbg.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;

public class ActionProxy extends AbstractAction {
  private ActionListener to;
  
  public ActionProxy(String paramString, ActionListener paramActionListener) {
    this.to = paramActionListener;
    putValue("ActionCommandKey", paramString);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    this.to.actionPerformed(paramActionEvent);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\action\ActionProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */