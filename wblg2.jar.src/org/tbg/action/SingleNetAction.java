package org.tbg.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.tbg.threads.SearchThread;
import org.tbg.util.Configuration;

public class SingleNetAction extends AbstractAction {
  int netSearchType;
  
  public SingleNetAction(String paramString, int paramInt) {
    super(paramString);
    this.netSearchType = paramInt;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    Thread thread = new Thread((Runnable)new SearchThread(this.netSearchType));
    int i = Configuration.getInstance().getPropInt("thread_priority", Integer.toString(5));
    thread.setPriority(i);
    thread.start();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\action\SingleNetAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */