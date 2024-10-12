package org.tbg.threads;

import java.awt.Component;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.tbg.FilesTableModel;
import org.tbg.arin.RangeSearchTool;
import org.tbg.exceptions.EmptyWBLException;
import org.tbg.filter.ird.IRDSizeFilter;
import org.tbg.gui.WBLGPanel;
import org.tbg.util.RangeTooBigException;
import org.tbg.util.TimeDiff;
import org.tbg.wbl.WBLHolder;
import org.tbg.wbl.WBLReader;

public class WBLChildSearchThread implements Runnable {
  String parentAsCidr;
  
  String wblFile;
  
  boolean isBlocklistFormat = false;
  
  public WBLChildSearchThread(String paramString1, String paramString2) {
    this.parentAsCidr = paramString1.trim();
    this.wblFile = paramString2;
  }
  
  public WBLChildSearchThread(String paramString1, String paramString2, boolean paramBoolean) {
    this.parentAsCidr = paramString1.trim();
    this.wblFile = paramString2;
    this.isBlocklistFormat = paramBoolean;
  }
  
  public WBLChildSearchThread(String paramString) {
    this.parentAsCidr = paramString.trim();
    this.wblFile = null;
  }
  
  public void run() {
    byte b = 0;
    if (this.parentAsCidr.indexOf('/') > -1) {
      b = 1;
    } else if (this.parentAsCidr.indexOf('-') > -1) {
      b = 2;
    } 
    WBLGPanel wBLGPanel = WBLGPanel.getInstance();
    if (b == 0) {
      JOptionPane.showMessageDialog((Component)wBLGPanel, "Neither a CIDR nor inet-range format?", "Invalid range format", 0);
      return;
    } 
    wBLGPanel.setSearchEnabled(false);
    TimeDiff timeDiff = new TimeDiff();
    FilesTableModel filesTableModel = (FilesTableModel)wBLGPanel.dbFilesPanel.getDataTable().getModel();
    filesTableModel = (FilesTableModel)wBLGPanel.blockFilesPanel.getDataTable().getModel();
    Vector vector = filesTableModel.getActiveList();
    WBLHolder wBLHolder = WBLHolder.getInstance();
    wBLHolder.loadBlocklists(vector);
    wBLGPanel.clearResults();
    int i = wBLGPanel.searchButtons.getMinRangeSize();
    int j = wBLGPanel.searchButtons.getMaxRangeSize();
    IRDSizeFilter iRDSizeFilter = new IRDSizeFilter(i, j);
    try {
      WBLReader.abortSearch = false;
      Vector vector1 = null;
      if (this.wblFile != null) {
        Vector<String> vector2 = new Vector();
        vector2.add(this.wblFile);
        vector1 = wBLHolder.findChildren(vector2, this.isBlocklistFormat, this.parentAsCidr, iRDSizeFilter);
      } else {
        RangeSearchTool rangeSearchTool = new RangeSearchTool(iRDSizeFilter);
        if (b == 1) {
          vector1 = rangeSearchTool.search(this.parentAsCidr);
        } else {
          vector1 = rangeSearchTool.search(this.parentAsCidr.substring(0, this.parentAsCidr.indexOf('-')).trim(), this.parentAsCidr.substring(this.parentAsCidr.indexOf('-') + 1).trim());
        } 
      } 
      wBLGPanel.setResults(vector1);
      String str1 = "";
      if (WBLReader.abortSearch == true)
        str1 = "aborted -- "; 
      String str2 = "";
      if (wBLGPanel.sap.checkIfError())
        str2 = " -- error(s) occurred, please check log"; 
      wBLGPanel.sap.reportStatus(str1 + Integer.toString(vector1.size()) + " entries" + " [time: " + timeDiff.getDeltaTime() + "]" + str2);
    } catch (RangeTooBigException rangeTooBigException) {
      wBLGPanel.sap.reportException((Throwable)rangeTooBigException);
      JOptionPane.showMessageDialog((Component)wBLGPanel, rangeTooBigException.getMessage(), "Unsupported range requested", 0);
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
      wBLGPanel.sap.reportException(outOfMemoryError);
    } catch (Exception exception) {
      wBLGPanel.sap.reportException(exception);
    } 
    Runtime.getRuntime().gc();
    wBLGPanel.setSearchEnabled(true);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\threads\WBLChildSearchThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */