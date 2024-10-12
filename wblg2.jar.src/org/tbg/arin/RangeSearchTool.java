package org.tbg.arin;

import java.util.Collections;
import java.util.Vector;
import org.tbg.filter.ird.IRDSizeFilter;
import org.tbg.gui.WBLGPanel;
import org.tbg.ip.IpRangeDescr;
import org.tbg.util.RangeTooBigException;
import org.tbg.wbl.WBLHolder;

public class RangeSearchTool {
  private IpRangeDescr parent = null;
  
  private WBLHolder wbl = WBLHolder.getInstance();
  
  private WBLGPanel wblg = WBLGPanel.getInstance();
  
  private IRDSizeFilter sizeFilter;
  
  public RangeSearchTool(IRDSizeFilter paramIRDSizeFilter) {
    this.sizeFilter = paramIRDSizeFilter;
  }
  
  public Vector<IpRangeDescr> search(String paramString) throws RangeTooBigException {
    this.parent = new IpRangeDescr(paramString);
    Vector<IpRangeDescr> vector = searchCidrInternal();
    Collections.sort(vector);
    this.wbl.updateBlocked(vector);
    return vector;
  }
  
  public Vector<IpRangeDescr> search(String paramString1, String paramString2) throws RangeTooBigException {
    this.parent = new IpRangeDescr(paramString1, paramString2, "", "");
    Vector<IpRangeDescr> vector = searchCidrInternal();
    vector = filterByRange(vector);
    Collections.sort(vector);
    this.wbl.updateBlocked(vector);
    return vector;
  }
  
  private String getAsBinaryString(long paramLong) {
    String str = Long.toBinaryString(paramLong);
    int i = 32 - str.length();
    if (i != 0)
      str = "00000000000000000000000000000000".substring(0, i) + str; 
    return str;
  }
  
  private IpRangeDescr calculateCidrParent(IpRangeDescr paramIpRangeDescr) {
    String str1 = getAsBinaryString(paramIpRangeDescr.getStart());
    String str2 = getAsBinaryString(paramIpRangeDescr.getEnd());
    byte b;
    for (b = 0; str1.charAt(b) == str2.charAt(b); b++);
    String str3 = paramIpRangeDescr.getStartString() + "/" + b;
    System.out.println(str3);
    return new IpRangeDescr(str3);
  }
  
  private Vector<IpRangeDescr> searchCidrInternal() throws RangeTooBigException {
    long l = this.parent.getEnd() - this.parent.getStart();
    Vector<IpRangeDescr> vector = null;
    if (l < 255L) {
      vector = searchLt24(this.parent.getStart());
    } else if (l == 255L) {
      vector = search24(this.parent.getStart());
    } else if (l < 65535L) {
      vector = searchLt16(this.parent.getStart());
    } else if (l == 65535L) {
      vector = search16(this.parent.getStart());
    } else if (l < 16777215L) {
      vector = searchLt8(this.parent.getStart());
    } else if (l == 16777215L) {
      vector = search8(this.parent.getStart());
    } else {
      throw new RangeTooBigException(this.parent.getStartString() + "-" + this.parent.getEndString());
    } 
    return filterBySize(vector);
  }
  
  private Vector<IpRangeDescr> filterByRange(Vector<IpRangeDescr> paramVector) {
    Vector<IpRangeDescr> vector = new Vector();
    for (IpRangeDescr ipRangeDescr : paramVector) {
      if (this.parent.isInside(ipRangeDescr))
        vector.add(ipRangeDescr); 
    } 
    return vector;
  }
  
  private Vector<IpRangeDescr> filterBySize(Vector<IpRangeDescr> paramVector) {
    Vector<IpRangeDescr> vector = new Vector();
    for (IpRangeDescr ipRangeDescr : paramVector) {
      if (this.sizeFilter.match(ipRangeDescr))
        vector.add(ipRangeDescr); 
    } 
    return vector;
  }
  
  private static String long2str(long paramLong, int paramInt) {
    long l1 = paramLong % 256L;
    paramLong -= l1;
    paramLong /= 256L;
    long l2 = paramLong % 256L;
    paramLong -= l2;
    paramLong /= 256L;
    long l3 = paramLong % 256L;
    paramLong -= l3;
    paramLong /= 256L;
    long l4 = paramLong % 256L;
    String str = "net-";
    if (paramInt >= 1)
      str = str.concat(Long.toString(l4)).concat("-"); 
    if (paramInt >= 2)
      str = str.concat(Long.toString(l3)).concat("-"); 
    if (paramInt >= 3)
      str = str.concat(Long.toString(l2)).concat("-"); 
    if (paramInt >= 4)
      str = str.concat(Long.toString(l1)).concat("-"); 
    return str;
  }
  
  private Vector<IpRangeDescr> searchLt24(long paramLong) {
    return filterByRange(search24(paramLong));
  }
  
  private Vector<IpRangeDescr> search24(long paramLong) {
    return this.wbl.findArinChildren(long2str(paramLong, 3).concat("*"));
  }
  
  private Vector<IpRangeDescr> searchLt16(long paramLong) {
    return filterByRange(search16(paramLong));
  }
  
  private Vector<IpRangeDescr> search16(long paramLong) {
    Vector<IpRangeDescr> vector = this.wbl.findArinChildren(long2str(paramLong, 2).concat("*"));
    if (vector.size() < 256)
      return vector; 
    this.wblg.sap.checkIfError();
    vector = new Vector<>();
    long l;
    for (l = paramLong; l < this.parent.getEnd(); l += 256L)
      vector.addAll(search24(l)); 
    return vector;
  }
  
  private Vector<IpRangeDescr> searchLt8(long paramLong) {
    return filterByRange(search8(paramLong));
  }
  
  private Vector<IpRangeDescr> search8(long paramLong) {
    Vector<IpRangeDescr> vector = this.wbl.findArinChildren(long2str(paramLong, 1).concat("*"));
    if (vector.size() < 256)
      return vector; 
    this.wblg.sap.checkIfError();
    vector = new Vector<>();
    long l1 = paramLong;
    IpRangeDescr ipRangeDescr = this.parent.clone();
    long l2 = this.parent.getEnd() - l1 + 1L;
    while (l1 < ipRangeDescr.getEnd()) {
      this.parent.setRange(l1, l1 + 65535L);
      vector.addAll(search16(l1));
      l1 += 65536L;
      this.wblg.sap.reportProgress((int)((l2 - ipRangeDescr.getEnd() - l1) * 100L / l2));
    } 
    this.parent.setRange(ipRangeDescr.getStart(), ipRangeDescr.getEnd());
    return vector;
  }
  
  private Vector<IpRangeDescr> wbl_findArinChildren(String paramString) {
    (WBLGPanel.getInstance()).sap.reportStatus("Searching: " + paramString + " " + this.parent.getStartString() + "-" + this.parent.getEndString());
    Vector<IpRangeDescr> vector = new Vector();
    for (byte b = 0; b < 'ā'; b++)
      vector.add(new IpRangeDescr(0L, 0L, "foo", "bar")); 
    return vector;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\arin\RangeSearchTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */