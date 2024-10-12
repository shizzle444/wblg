package org.tbg.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.tbg.threads.SearchThread;
import org.tbg.util.Configuration;

public class SingleDBAction extends AbstractAction {
  private String filename;
  
  private boolean blockfile;
  
  public SingleDBAction(String paramString1, String paramString2, boolean paramBoolean) {
    super(paramString1);
    this.blockfile = paramBoolean;
    this.filename = new String(paramString2);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    Thread thread = new Thread(this.blockfile ? (Runnable)new SearchThread(this.filename, 1) : (Runnable)new SearchThread(this.filename));
    int i = Configuration.getInstance().getPropInt("thread_priority", Integer.toString(5));
    thread.setPriority(i);
    thread.start();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\action\SingleDBAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */