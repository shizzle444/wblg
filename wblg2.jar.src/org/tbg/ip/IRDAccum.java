package org.tbg.ip;

import java.util.Vector;
import org.tbg.filter.WBLLoaderFilter;
import org.tbg.filter.ird.IRDFilter;

public class IRDAccum implements TConsumer<IpRangeDescr> {
  private Vector<IpRangeDescr> vec = new Vector<>();
  
  private final IRDFilter filter;
  
  private final WBLLoaderFilter preFilter;
  
  public IRDAccum(WBLLoaderFilter paramWBLLoaderFilter, IRDFilter paramIRDFilter) {
    this.preFilter = paramWBLLoaderFilter;
    this.filter = paramIRDFilter;
  }
  
  private void add(IpRangeDescr paramIpRangeDescr) {
    if (this.preFilter.match(paramIpRangeDescr.getDescription()) > -1 && this.filter.match(paramIpRangeDescr))
      this.vec.add(paramIpRangeDescr); 
  }
  
  public void addCidrString(String paramString) {
    add(new IpRangeDescr(paramString));
  }
  
  public void addString(String paramString1, String paramString2) {
    String[] arrayOfString = paramString1.split("-");
    add(new IpRangeDescr(arrayOfString[0].trim(), arrayOfString[1].trim(), paramString2, ""));
  }
  
  public void clear() {
    this.vec.clear();
  }
  
  public Vector<IpRangeDescr> done() {
    return this.vec;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\IRDAccum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */