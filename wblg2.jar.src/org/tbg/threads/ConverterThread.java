package org.tbg.threads;

import java.awt.Component;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.tbg.exceptions.EmptyWBLException;
import org.tbg.gui.StatusAndProgress;
import org.tbg.gui.WBLGPanel;
import org.tbg.rpsl.IRD_Consumer;
import org.tbg.rpsl.RpslParser;
import org.tbg.util.SAPReport;
import org.tbg.util.TimeDiff;
import org.tbg.wbl.WBLWriter;

public class ConverterThread implements Runnable {
  private Vector<String> files;
  
  public ConverterThread(Vector<String> paramVector) {
    this.files = paramVector;
  }
  
  private String makeOutFilename(String paramString) {
    return paramString.substring(0, paramString.indexOf('.')) + ".wbl.gz";
  }
  
  public void run() {
    WBLGPanel wBLGPanel = WBLGPanel.getInstance();
    Vector vector1 = wBLGPanel.configPanel.getEmailItemList().getActiveList();
    Vector vector2 = wBLGPanel.configPanel.getFieldsItemList().getActiveList();
    StatusAndProgress statusAndProgress = wBLGPanel.sap;
    Vector<String> vector = new Vector();
    try {
      TimeDiff timeDiff = new TimeDiff();
      for (byte b = 0; b < this.files.size(); b++) {
        String str1 = this.files.elementAt(b);
        String str2 = makeOutFilename(str1);
        statusAndProgress.reportStatus("converting " + str1);
        WBLWriter wBLWriter = new WBLWriter(str2);
        RpslParser rpslParser = new RpslParser((SAPReport)statusAndProgress, (IRD_Consumer)wBLWriter, vector1, vector2);
        rpslParser.loadStrippedGz(str1);
        try {
          wBLWriter.done();
        } catch (EmptyWBLException emptyWBLException) {
          vector.add(emptyWBLException.getFileName());
        } 
      } 
      statusAndProgress.reportStatus("done [time: " + timeDiff.getDeltaTime() + "]");
      if (vector.size() > 0) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = System.getProperty("line.separator");
        stringBuilder.append("The following database(s) are empty; you should not use them:");
        stringBuilder.append(str);
        for (byte b1 = 0; b1 < vector.size(); b1++) {
          stringBuilder.append(vector.elementAt(b1));
          stringBuilder.append(str);
        } 
        JOptionPane.showMessageDialog((Component)WBLGPanel.getInstance(), stringBuilder.toString(), "Error converting database(s)", 0);
      } 
    } catch (Exception exception) {
      statusAndProgress.reportException(exception);
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\threads\ConverterThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */