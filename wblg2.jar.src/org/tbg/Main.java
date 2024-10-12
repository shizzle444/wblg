package org.tbg;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import org.tbg.exceptions.AlreadyRunningException;
import org.tbg.gui.WBLGPanel;
import org.tbg.util.Configuration;

public class Main {
  private static JFrame frame;
  
  private static Configuration config;
  
  private static WBLGPanel wblg;
  
  public static void createAndShowGUI() {
    frame = new JFrame("Whois Blocklist GUI");
    frame.setDefaultCloseOperation(3);
    try {
      config = new Configuration("wblg-new_config.txt");
    } catch (AlreadyRunningException alreadyRunningException) {
      JOptionPane.showMessageDialog(frame, "Only one instance of this program should run at any time. You can change this in the config file if you want to.", "The program is already running!", 0);
      System.exit(1);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    wblg = new WBLGPanel();
    frame.setContentPane((Container)wblg);
    frame.setJMenuBar(wblg.createMenuBar());
    wblg.screenSetup();
    frame.setVisible(true);
    ToolTipManager.sharedInstance().setDismissDelay(2147483647);
    WindowAdapter windowAdapter = new WindowAdapter() {
        public void windowClosing(WindowEvent param1WindowEvent) {
          Main.wblg.saveConfig();
        }
      };
    frame.addWindowListener(windowAdapter);
    frame.addWindowStateListener(windowAdapter);
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      createAndShowGUI();
    } catch (Exception exception) {
      exception.printStackTrace();
      wblg.sap.reportException(exception);
    } catch (OutOfMemoryError outOfMemoryError) {
      wblg.sap.reportException(outOfMemoryError);
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */