package org.tbg.ip;

public interface IpRangeInterface extends Comparable<IpRangeInterface> {
  int rangeStart();
  
  long getStart();
  
  long getEnd();
  
  int isBlocked(IpRangeInterface paramIpRangeInterface);
  
  boolean isInside(IpRangeInterface paramIpRangeInterface);
  
  int compareTo(IpRangeInterface paramIpRangeInterface);
  
  IpRangeInterface merge(IpRangeInterface paramIpRangeInterface);
  
  boolean canMerge(IpRangeInterface paramIpRangeInterface);
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\IpRangeInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */