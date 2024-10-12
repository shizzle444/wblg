package org.tbg.threads;

import java.awt.Component;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.tbg.FilesTableModel;
import org.tbg.exceptions.EmptyWBLException;
import org.tbg.gui.WBLGPanel;
import org.tbg.util.Configuration;
import org.tbg.util.TimeDiff;
import org.tbg.wbl.WBLHolder;
import org.tbg.wbl.WBLReader;

public class SearchThread implements Runnable {
  private String singleFile = null;
  
  private boolean byRangeOnly = false;
  
  private int netOnly = 0;
  
  private boolean searchBL = false;
  
  public SearchThread() {}
  
  public SearchThread(String paramString) {}
  
  public SearchThread(boolean paramBoolean) {}
  
  public SearchThread(int paramInt) {}
  
  public SearchThread(String paramString, int paramInt) {
    this.searchBL = true;
  }
  
  public void run() {
    Vector vector1;
    Vector<String> vector;
    WBLGPanel wBLGPanel = WBLGPanel.getInstance();
    if (this.byRangeOnly) {
      vector1 = new Vector();
    } else {
      vector1 = wBLGPanel.getQuery();
      if (vector1.size() == 0)
        return; 
    } 
    wBLGPanel.setSearchEnabled(false);
    TimeDiff timeDiff = new TimeDiff();
    FilesTableModel filesTableModel = (FilesTableModel)wBLGPanel.dbFilesPanel.getDataTable().getModel();
    if (this.singleFile == null) {
      if (this.netOnly > 0) {
        vector = new Vector();
      } else {
        vector = filesTableModel.getActiveList();
        this.netOnly = wBLGPanel.getNetSearch();
      } 
    } else {
      vector = new Vector();
      if (!this.searchBL) {
        vector.add(this.singleFile);
        this.singleFile = null;
      } 
    } 
    filesTableModel = (FilesTableModel)wBLGPanel.blockFilesPanel.getDataTable().getModel();
    Vector vector2 = filesTableModel.getActiveList();
    WBLHolder wBLHolder = WBLHolder.getInstance();
    wBLHolder.loadBlocklists(vector2);
    wBLGPanel.clearResults();
    boolean bool = wBLGPanel.searchButtons.doFuzzyMatch();
    int i = wBLGPanel.searchButtons.getMinRangeSize();
    int j = wBLGPanel.searchButtons.getMaxRangeSize();
    boolean bool1 = false;
    if (this.byRangeOnly)
      this.netOnly = 0; 
    if (vector.size() > 0 && this.netOnly > 0 && Configuration.getInstance().getPropBool("warn_composite_search", "true")) {
      int k = JOptionPane.showConfirmDialog((Component)wBLGPanel, "Searching in local WBL files & online at once may not work as you expect.\n\nPlease read the documentation; you can disable this warning in the configuration.\nContinue anyway?", "Are you sure you want to do this?", 0, 2);
      if (k == 1) {
        wBLGPanel.setSearchEnabled(true);
        return;
      } 
    } 
    try {
      WBLReader.abortSearch = false;
      Vector vector3 = wBLHolder.findMatching(vector, this.singleFile, vector1, bool, this.netOnly, i, j);
      wBLGPanel.setResults(vector3);
      String str1 = "";
      if (WBLReader.abortSearch == true)
        str1 = "aborted -- "; 
      String str2 = "";
      if (wBLGPanel.sap.checkIfError())
        str2 = " -- error(s) occurred, please check log"; 
      wBLGPanel.sap.reportStatus(str1 + Integer.toString(vector3.size()) + " entries" + " [time: " + timeDiff.getDeltaTime() + "]" + str2);
    } catch (EmptyWBLException emptyWBLException) {
      wBLGPanel.sap.reportException((Throwable)emptyWBLException);
      StringBuilder stringBuilder = new StringBuilder();
      String str = System.getProperty("line.separator");
      stringBuilder.append(emptyWBLException.getFileName());
      stringBuilder.append(" may be empty; please deactivate it and try again.");
      stringBuilder.append(str);
      stringBuilder.append("See 'empty database' in the documentation.");
      JOptionPane.showMessageDialog((Component)wBLGPanel, stringBuilder.toString(), "Error searching in database", 0);
    } catch (OutOfMemoryError outOfMemoryError) {
      wBLGPanel.sap.reportError(outOfMemoryError.toString());
      outOfMemoryError.printStackTrace();
    } catch (Exception exception) {
      wBLGPanel.sap.reportException(exception);
    } 
    Runtime.getRuntime().gc();
    wBLGPanel.setSearchEnabled(true);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\threads\SearchThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */