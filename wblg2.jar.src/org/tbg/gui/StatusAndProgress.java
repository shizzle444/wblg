package org.tbg.gui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.tbg.util.SAPReport;

public class StatusAndProgress implements SAPReport {
  public JLabel statusLabel = new JLabel("ready");
  
  public JProgressBar progressBar = new JProgressBar();
  
  private LogPanel logPanel;
  
  private boolean hadError;
  
  public StatusAndProgress(LogPanel paramLogPanel) {
    this.logPanel = paramLogPanel;
    this.hadError = false;
  }
  
  public void reportStatus(String paramString) {
    this.statusLabel.setText(paramString);
    this.logPanel.appendStatus(paramString);
  }
  
  public void reportError(String paramString) {
    this.statusLabel.setText(paramString);
    this.logPanel.appendError(paramString);
    this.hadError = true;
  }
  
  public void reportException(Throwable paramThrowable) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    paramThrowable.printStackTrace(printStream);
    this.statusLabel.setText(paramThrowable.toString());
    this.logPanel.appendError(byteArrayOutputStream.toString());
    this.hadError = true;
  }
  
  public void reportProgress(int paramInt) {
    this.progressBar.setValue(paramInt);
  }
  
  public boolean checkIfError() {
    boolean bool = this.hadError;
    this.hadError = false;
    return bool;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\StatusAndProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */