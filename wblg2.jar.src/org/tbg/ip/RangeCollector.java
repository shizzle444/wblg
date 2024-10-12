package org.tbg.ip;

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.tbg.gui.StatusAndProgress;
import org.tbg.gui.WBLGPanel;

public class RangeCollector<T extends IpRangeInterface> implements TConsumer<T> {
  private boolean doCollapse = true;
  
  private Vector<T> store = new Vector<>();
  
  private RC_Factory<T> factory;
  
  public RangeCollector(RC_Factory<T> paramRC_Factory) {
    this.factory = paramRC_Factory;
  }
  
  public void addString(String paramString1, String paramString2) {
    StringTokenizer stringTokenizer = new StringTokenizer(paramString1, "-");
    this.store.add(this.factory.makeNew(stringTokenizer.nextToken(), stringTokenizer.nextToken(), paramString2));
  }
  
  public void addCidrString(String paramString) {
    this.store.add(this.factory.makeNew(paramString));
  }
  
  public void setDoCollapse(boolean paramBoolean) {
    this.doCollapse = paramBoolean;
  }
  
  public Vector<T> done() {
    Collections.sort((List)this.store);
    if (this.doCollapse)
      collapse(); 
    return this.store;
  }
  
  public void clear() {
    this.store.clear();
  }
  
  private void collapse() {
    StatusAndProgress statusAndProgress = (WBLGPanel.getInstance()).sap;
    int i = this.store.size();
    if (i <= 1)
      return; 
    byte b = 0;
    Vector<IpRangeInterface> vector = new Vector();
    boolean bool = false;
    IpRangeInterface ipRangeInterface1 = (IpRangeInterface)this.store.elementAt(b++);
    IpRangeInterface ipRangeInterface2 = null;
    while (b < i) {
      ipRangeInterface2 = (IpRangeInterface)this.store.elementAt(b);
      if (ipRangeInterface1.canMerge(ipRangeInterface2)) {
        ipRangeInterface1 = ipRangeInterface1.merge(ipRangeInterface2);
        bool = true;
      } else {
        vector.add(ipRangeInterface1);
        ipRangeInterface1 = ipRangeInterface2;
        bool = false;
      } 
      statusAndProgress.reportProgress(++b * 100 / i);
    } 
    if (bool) {
      vector.add(ipRangeInterface1);
    } else {
      vector.add(ipRangeInterface2);
    } 
    this.store = (Vector)vector;
    statusAndProgress.reportProgress(0);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\RangeCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */