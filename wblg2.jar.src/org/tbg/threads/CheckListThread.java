package org.tbg.threads;

import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;
import org.tbg.FilesTableModel;
import org.tbg.gui.WBLGPanel;
import org.tbg.ip.IpRangeDescr;
import org.tbg.util.TimeDiff;
import org.tbg.wbl.WBLHolder;

public class CheckListThread implements Runnable {
  private Vector<IpRangeDescr> convertLines(Vector<String> paramVector) {
    Vector<IpRangeDescr> vector = new Vector();
    WBLGPanel wBLGPanel = WBLGPanel.getInstance();
    Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    for (String str1 : paramVector) {
      int i = str1.lastIndexOf(':');
      String str2 = null;
      String str3 = null;
      if (i > -1) {
        str2 = str1.substring(i + 1).replaceAll(" ", "");
        str3 = str1.substring(0, i);
      } else {
        str2 = str1.replaceAll(" ", "");
        str3 = "";
      } 
      if (str2.indexOf("!%20NET-") == 0) {
        str2 = str2.substring(8);
        str2 = str2.replaceFirst("-", ".").replaceFirst("-", ".").replaceFirst("-", ".").replaceFirst("-\\d+-", "-");
      } 
      StringTokenizer stringTokenizer = new StringTokenizer(str2, "-");
      String str4 = null;
      String str5 = null;
      if (stringTokenizer.hasMoreTokens())
        str4 = stringTokenizer.nextToken(); 
      if (stringTokenizer.hasMoreTokens())
        str5 = stringTokenizer.nextToken(); 
      if (str4 != null && str5 != null && pattern.matcher(str4).matches() && pattern.matcher(str5).matches()) {
        IpRangeDescr ipRangeDescr = new IpRangeDescr(str4, str5, str3, "");
        ipRangeDescr.setBestMatch(ipRangeDescr.getDescription());
        vector.add(ipRangeDescr);
        continue;
      } 
      wBLGPanel.sap.reportError("Ignoring input line: " + str1);
    } 
    return vector;
  }
  
  public void run() {
    WBLGPanel wBLGPanel = WBLGPanel.getInstance();
    try {
      TimeDiff timeDiff = new TimeDiff();
      FilesTableModel filesTableModel = (FilesTableModel)wBLGPanel.blockFilesPanel.getDataTable().getModel();
      Vector vector = filesTableModel.getActiveList();
      WBLHolder wBLHolder = WBLHolder.getInstance();
      if (wBLGPanel.listCheckPanel == null)
        return; 
      Vector<IpRangeDescr> vector1 = convertLines(wBLGPanel.listCheckPanel.getLines());
      if (vector1.size() == 0) {
        wBLGPanel.sap.reportError("No valid entry found to check");
        wBLGPanel.sap.checkIfError();
        return;
      } 
      wBLGPanel.clearResults();
      wBLHolder.loadBlocklists(vector);
      Collections.sort(vector1);
      vector1 = wBLHolder.cleanSorted(vector1);
      wBLHolder.updateBlocked(vector1);
      wBLGPanel.setResults(vector1);
      String str = "";
      if (wBLGPanel.sap.checkIfError())
        str = " -- error(s) occurred, please check log"; 
      wBLGPanel.sap.reportStatus("checked " + vector1.size() + " entries [time: " + timeDiff.getDeltaTime() + "]" + str);
    } catch (Exception exception) {
      wBLGPanel.sap.reportException(exception);
    } 
    Runtime.getRuntime().gc();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\threads\CheckListThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */