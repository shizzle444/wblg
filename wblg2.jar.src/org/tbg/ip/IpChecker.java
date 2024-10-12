package org.tbg.ip;

import java.util.Vector;

public class IpChecker<T extends IpRangeInterface> {
  private Vector<T> store;
  
  private int[] indices;
  
  public IpChecker(Vector<T> paramVector) {
    this.store = paramVector;
    this.indices = new int[256];
    zeroIndices();
    setupIndices();
  }
  
  public IpChecker() {
    this.store = new Vector<>();
    this.indices = new int[256];
    zeroIndices();
  }
  
  public void setData(Vector<T> paramVector) {
    this.store.clear();
    this.store = paramVector;
    zeroIndices();
    setupIndices();
  }
  
  @Deprecated
  public boolean isBlocked(T paramT) {
    int i = paramT.rangeStart();
    int j = this.indices[i];
    int k = this.store.size();
    for (int m = j; m < k; m++) {
      IpRangeInterface ipRangeInterface = (IpRangeInterface)this.store.elementAt(m);
      if (ipRangeInterface.isInside((IpRangeInterface)paramT))
        return true; 
      if (ipRangeInterface.compareTo((IpRangeInterface)paramT) == 1)
        return false; 
    } 
    return false;
  }
  
  public int isBlockedNew(T paramT) {
    int i = paramT.rangeStart();
    int j = this.indices[i];
    int k = this.store.size();
    int m = 0;
    if (k == 0)
      return m; 
    int n;
    for (n = j; n < k; n++) {
      IpRangeInterface ipRangeInterface = (IpRangeInterface)this.store.elementAt(n);
      int i1 = ipRangeInterface.isBlocked((IpRangeInterface)paramT);
      if (i1 == 1)
        return i1; 
      if (i1 == 2)
        m = i1; 
      if (ipRangeInterface.getStart() > paramT.getEnd())
        break; 
    } 
    for (n = j; n >= 0; n--) {
      IpRangeInterface ipRangeInterface = (IpRangeInterface)this.store.elementAt(n);
      int i1 = ipRangeInterface.isBlocked((IpRangeInterface)paramT);
      if (i1 == 1)
        return i1; 
      if (i1 == 2)
        m = i1; 
      if (ipRangeInterface.getEnd() < paramT.getStart())
        break; 
    } 
    return m;
  }
  
  public int getNumRanges() {
    return this.store.size();
  }
  
  public void clear() {
    this.store.clear();
  }
  
  private void zeroIndices() {
    for (byte b = 0; b < 'Ā'; b++)
      this.indices[b] = 0; 
  }
  
  private void setupIndices() {
    int i = this.store.size();
    for (int j = i - 1; j >= 0; j--) {
      IpRangeInterface ipRangeInterface = (IpRangeInterface)this.store.elementAt(j);
      int k = ipRangeInterface.rangeStart();
      this.indices[k] = j;
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\IpChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */