package org.tbg.ip;

import java.io.Serializable;

public class IpRange implements IpRangeInterface, Serializable {
  protected long start;
  
  protected long end;
  
  public IpRange(String paramString1, String paramString2) {
    this.start = ipv4.fromString(paramString1);
    this.end = ipv4.fromString(paramString2);
  }
  
  public IpRange(String paramString) {
    this.start = ipv4.fromString(paramString.substring(0, paramString.indexOf("/")));
    long l = ipv4.numHosts(Integer.parseInt(paramString.substring(paramString.indexOf("/") + 1)));
    this.start &= l ^ 0xFFFFFFFFFFFFFFFFL;
    this.end = this.start | l;
  }
  
  public IpRange(long paramLong1, long paramLong2) {
    this.start = paramLong1;
    this.end = paramLong2;
  }
  
  public IpRange() {}
  
  public long getStart() {
    return this.start;
  }
  
  public long getEnd() {
    return this.end;
  }
  
  public void setEnd(long paramLong) {
    this.end = paramLong;
  }
  
  public void setStart(long paramLong) {
    this.start = paramLong;
  }
  
  public long length() {
    return this.end - this.start;
  }
  
  public boolean isLeftOf(IpRange paramIpRange) {
    return (this.end < paramIpRange.start);
  }
  
  public boolean isRightOf(IpRange paramIpRange) {
    return (this.start > paramIpRange.end);
  }
  
  public int hashCode() {
    null = 3;
    null = 83 * null + (int)(this.start ^ this.start >>> 32L);
    return 83 * null + (int)(this.end ^ this.end >>> 32L);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == null)
      return false; 
    if (getClass() != paramObject.getClass())
      return false; 
    IpRange ipRange = (IpRange)paramObject;
    return (this.start != ipRange.start) ? false : (!(this.end != ipRange.end));
  }
  
  boolean equals(IpRange paramIpRange) {
    return (this.start == paramIpRange.start && this.end == paramIpRange.end);
  }
  
  public int compareTo(IpRangeInterface paramIpRangeInterface) {
    return (this.start == paramIpRangeInterface.getStart()) ? 0 : ((this.start > paramIpRangeInterface.getStart()) ? 1 : -1);
  }
  
  public boolean adjacent(IpRangeInterface paramIpRangeInterface) {
    return (this.end + 1L == paramIpRangeInterface.getStart()) ? true : ((this.start == paramIpRangeInterface.getEnd() + 1L));
  }
  
  public boolean canMerge(IpRangeInterface paramIpRangeInterface) {
    if (equals(paramIpRangeInterface) == true)
      return true; 
    if (adjacent(paramIpRangeInterface) == true)
      return true; 
    long l1 = paramIpRangeInterface.getStart();
    long l2 = paramIpRangeInterface.getEnd();
    return (l1 >= this.start && l2 <= this.end) ? true : ((this.start >= l1 && this.end <= l2) ? true : ((this.start >= l1 && this.start <= l2) ? true : ((l1 >= this.start && l1 <= this.end))));
  }
  
  public boolean isInside(IpRangeInterface paramIpRangeInterface) {
    return (equals(paramIpRangeInterface) == true) ? true : ((paramIpRangeInterface.getStart() >= this.start && paramIpRangeInterface.getEnd() <= this.end));
  }
  
  public int isBlocked(IpRangeInterface paramIpRangeInterface) {
    if (isInside(paramIpRangeInterface) == true)
      return 1; 
    long l1 = paramIpRangeInterface.getStart();
    long l2 = paramIpRangeInterface.getEnd();
    return (this.start >= l1 && this.end <= l2) ? 2 : ((this.start >= l1 && this.start <= l2) ? 2 : ((l1 >= this.start && l1 <= this.end) ? 2 : 0));
  }
  
  public int rangeStart() {
    Long long_ = new Long(this.start >>> 24L);
    return long_.intValue();
  }
  
  public IpRange merge(IpRangeInterface paramIpRangeInterface) {
    long l1 = (this.start < paramIpRangeInterface.getStart()) ? this.start : paramIpRangeInterface.getStart();
    long l2 = (this.end > paramIpRangeInterface.getEnd()) ? this.end : paramIpRangeInterface.getEnd();
    return new IpRange(l1, l2);
  }
  
  public String toString() {
    return ipv4.fromLong(this.start).concat("-").concat(ipv4.fromLong(this.end));
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\IpRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */