package org.tbg.threads;

import java.util.Vector;
import org.tbg.FilesTableModel;
import org.tbg.blocklist.BLTableModel;
import org.tbg.gui.WBLGPanel;
import org.tbg.util.TimeDiff;
import org.tbg.wbl.WBLHolder;

public class BlockCheckThread implements Runnable {
  public void run() {
    TimeDiff timeDiff = new TimeDiff();
    WBLGPanel wBLGPanel = WBLGPanel.getInstance();
    FilesTableModel filesTableModel = (FilesTableModel)wBLGPanel.blockFilesPanel.getDataTable().getModel();
    Vector vector1 = filesTableModel.getActiveList();
    if (vector1.size() == 0)
      return; 
    WBLHolder wBLHolder = WBLHolder.getInstance();
    wBLHolder.loadBlocklists(vector1);
    BLTableModel bLTableModel = (BLTableModel)wBLGPanel.resultTable.getModel();
    Vector vector2 = bLTableModel.getData();
    wBLHolder.updateBlocked(vector2);
    wBLGPanel.setResults(vector2);
    wBLGPanel.sap.reportStatus(Integer.toString(vector2.size()) + " entries" + " [time: " + timeDiff.getDeltaTime() + "]");
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\threads\BlockCheckThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */