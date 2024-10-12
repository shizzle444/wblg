package org.tbg.gui;

import java.awt.Color;
import javax.swing.JLabel;

public class MemWatcher extends JLabel {
  private float max;
  
  private float total;
  
  private float free;
  
  private Thread updater;
  
  private float mb = 1048576.0F;
  
  public MemWatcher() {
    super("!");
    setForeground(Color.green);
    this.updater = new Thread(new Updater());
    this.updater.start();
  }
  
  public void updateValues() {
    Runtime runtime = Runtime.getRuntime();
    this.free = (float)runtime.freeMemory() / this.mb;
    this.total = (float)runtime.totalMemory() / this.mb;
    this.max = (float)runtime.maxMemory() / this.mb;
    if (this.total < this.max * 2.0F / 3.0F) {
      setForeground(Color.green);
    } else if (this.total < this.max) {
      setForeground(Color.blue);
    } else if (this.free < 5.0F) {
      setForeground(Color.red);
    } else {
      setForeground(Color.orange);
    } 
    String str = String.format("%.1f/%.1f/%.1f", new Object[] { Float.valueOf(this.free), Float.valueOf(this.total), Float.valueOf(this.max) });
    setToolTipText(str);
  }
  
  class Updater implements Runnable {
    public void run() {
      try {
        while (true) {
          MemWatcher.this.updateValues();
          Thread.sleep(500L);
        } 
      } catch (Exception exception) {
        return;
      } 
    }
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\MemWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */