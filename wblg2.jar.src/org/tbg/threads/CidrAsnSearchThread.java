package org.tbg.threads;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.tbg.FilesTableModel;
import org.tbg.cidr.ASN;
import org.tbg.cidr.AsnListParser;
import org.tbg.cidr.AutnumFetcher;
import org.tbg.cidr.ResultFetcher;
import org.tbg.filter.BasicFilter;
import org.tbg.filter.WBLLoaderFilter;
import org.tbg.filter.ird.IRDFilter;
import org.tbg.filter.ird.IRDSizeFilter;
import org.tbg.gui.WBLGPanel;
import org.tbg.util.Configuration;
import org.tbg.util.SAPReport;
import org.tbg.util.TimeDiff;
import org.tbg.wbl.WBLHolder;

public class CidrAsnSearchThread implements Runnable {
  private String asnFile = null;
  
  private SAPReport sap = null;
  
  private boolean searchByDescription = true;
  
  public CidrAsnSearchThread() {}
  
  public CidrAsnSearchThread(boolean paramBoolean) {}
  
  public String getLocalASNFile() throws IOException, InterruptedException {
    Configuration configuration = Configuration.getInstance();
    String str = configuration.getProp("cidr_autnum_file", "");
    if (str.length() == 0) {
      JFileChooser jFileChooser = new JFileChooser();
      if (jFileChooser.showDialog((Component)WBLGPanel.getInstance(), "Save") == 0) {
        str = jFileChooser.getSelectedFile().getPath();
        configuration.setProperty("cidr_autnum_file", str);
      } 
    } 
    long l1 = Long.parseLong(Configuration.getInstance().getProp("cidr_autnum_updated", "0"));
    long l2 = Long.parseLong(Configuration.getInstance().getProp("cidr_autnum_days", "7"));
    long l3 = (new Date()).getTime();
    long l4 = (l3 - l1) / 86400000L;
    File file = new File(str);
    if (!file.exists() || l4 > l2) {
      AutnumFetcher autnumFetcher = new AutnumFetcher(this.sap);
      String str1 = str + ".tmp";
      autnumFetcher.convertToFile(str1);
      File file1 = new File(str1);
      long l = file1.length();
      boolean bool = true;
      if (l == 0L) {
        JOptionPane.showMessageDialog(null, "The updated file is empty, keeping the old version. Try updating again at a later time.", "Update file is empty", 0);
        bool = false;
      } else if (l < file.length()) {
        int i = JOptionPane.showConfirmDialog(null, "The updated file is " + l + " bytes, the old one " + file.length() + ". Do you want to overwrite the file?", "The updated file is smaller than the old one", 0);
        if (i != 0)
          bool = false; 
      } 
      if (bool) {
        file1.renameTo(file);
      } else {
        file1.delete();
      } 
    } 
    return str;
  }
  
  private Vector<ASN> getQueryASN(Vector<String> paramVector) {
    Vector<ASN> vector = new Vector();
    for (String str : paramVector)
      vector.add(new ASN(str)); 
    return vector;
  }
  
  private Vector<ASN> getQueryDescr(Vector<String> paramVector) throws IOException, InterruptedException {
    this.asnFile = getLocalASNFile();
    BasicFilter basicFilter = new BasicFilter(paramVector);
    AsnListParser asnListParser = new AsnListParser(this.asnFile);
    return asnListParser.findMatching((WBLLoaderFilter)basicFilter);
  }
  
  public void run() {
    WBLGPanel wBLGPanel = WBLGPanel.getInstance();
    this.sap = (SAPReport)wBLGPanel.sap;
    try {
      TimeDiff timeDiff = new TimeDiff();
      WBLHolder wBLHolder = WBLHolder.getInstance();
      FilesTableModel filesTableModel = (FilesTableModel)wBLGPanel.blockFilesPanel.getDataTable().getModel();
      Vector vector = filesTableModel.getActiveList();
      int i = wBLGPanel.searchButtons.getMinRangeSize();
      int j = wBLGPanel.searchButtons.getMaxRangeSize();
      Vector<String> vector1 = wBLGPanel.getQuery();
      if (vector1.size() == 0)
        return; 
      wBLGPanel.setSearchEnabled(false);
      wBLHolder.loadBlocklists(vector);
      Vector<ASN> vector2 = this.searchByDescription ? getQueryDescr(vector1) : getQueryASN(vector1);
      wBLGPanel.sap.reportStatus(this.searchByDescription ? "searching ASN by description" : "searching by ASN");
      ResultFetcher resultFetcher = new ResultFetcher((SAPReport)wBLGPanel.sap);
      IRDSizeFilter iRDSizeFilter = new IRDSizeFilter(i, j);
      Vector<Comparable> vector3 = resultFetcher.search(vector2, (IRDFilter)iRDSizeFilter);
      Collections.sort(vector3);
      wBLHolder.updateBlocked(vector3);
      wBLGPanel.setResults(vector3);
      String str1 = "";
      String str2 = "";
      if (wBLGPanel.sap.checkIfError())
        str2 = " -- error(s) occurred, please check log"; 
      wBLGPanel.sap.reportStatus(str1 + Integer.toString(vector3.size()) + " entries" + " [time: " + timeDiff.getDeltaTime() + "]" + str2);
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


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\threads\CidrAsnSearchThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */