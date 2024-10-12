package org.tbg.ip;

import java.io.Serializable;

public class IpRangeDescr extends IpRange implements IpRangeInterface, Serializable {
  private String descr;
  
  private String netname;
  
  private byte blockState = 0;
  
  private boolean selected = false;
  
  private byte highlighted = 0;
  
  private String bestMatch;
  
  public IpRangeDescr(String paramString1, String paramString2, String paramString3, String paramString4) {
    super(paramString1, paramString2);
    this.start = ipv4.fromString(paramString1);
    this.end = ipv4.fromString(paramString2);
    this.descr = new String(paramString3);
    this.netname = new String(paramString4);
    this.blockState = 0;
    this.selected = false;
    this.bestMatch = null;
  }
  
  public IpRangeDescr(IpRange paramIpRange, String paramString1, String paramString2) {
    super(paramIpRange.getStart(), paramIpRange.getEnd());
    this.descr = new String(paramString1);
    this.netname = new String(paramString2);
    this.bestMatch = this.descr;
  }
  
  public IpRangeDescr(String paramString1, String paramString2, byte paramByte, String paramString3, String paramString4) {
    super(paramString1, paramString2);
    this.blockState = paramByte;
    this.netname = new String(paramString4);
    this.descr = new String(paramString3);
    this.selected = false;
    this.bestMatch = null;
  }
  
  public IpRangeDescr(long paramLong1, long paramLong2, String paramString1, String paramString2) {
    super(paramLong1, paramLong2);
    this.descr = new String(paramString1);
    this.netname = new String(paramString2);
    this.blockState = 0;
    this.selected = false;
    this.bestMatch = null;
  }
  
  public IpRangeDescr(String paramString) {
    super(paramString);
    this.descr = "";
    this.netname = "";
    this.bestMatch = "";
    this.blockState = 0;
  }
  
  public IpRangeDescr() {}
  
  public String getDescr() {
    return this.descr;
  }
  
  public void setDescr(String paramString) {
    this.descr = paramString;
  }
  
  public byte getHighlighted() {
    return this.highlighted;
  }
  
  public void setHighlighted(byte paramByte) {
    this.highlighted = paramByte;
  }
  
  public String getNetname() {
    return this.netname;
  }
  
  public void setNetname(String paramString) {
    this.netname = paramString;
  }
  
  public boolean isSelected() {
    return this.selected;
  }
  
  public IpRangeDescr clone() {
    return new IpRangeDescr(this.start, this.end, this.descr, this.netname);
  }
  
  public boolean isInside(IpRangeDescr paramIpRangeDescr) {
    return (this.start == paramIpRangeDescr.start && this.end == paramIpRangeDescr.end) ? true : ((paramIpRangeDescr.start >= this.start && paramIpRangeDescr.end <= this.end));
  }
  
  public void setBestMatch(String paramString) {
    this.bestMatch = paramString;
  }
  
  public String getBestMatch() {
    if (this.bestMatch == null)
      this.bestMatch = this.descr; 
    return this.bestMatch;
  }
  
  public void resetBestMatch() {
    this.bestMatch = this.netname + ":" + this.descr;
  }
  
  public void setDescription(String paramString) {
    this.descr = new String(paramString);
    this.bestMatch = this.descr;
  }
  
  public void bestMatchFromDescription(int paramInt) {
    if (this.descr.indexOf(';') == -1) {
      this.bestMatch = this.descr;
    } else {
      int i = this.descr.lastIndexOf(';', paramInt);
      if (i < 0) {
        i = 0;
      } else {
        i++;
      } 
      int j = this.descr.indexOf(';', paramInt);
      if (j < 0)
        j = this.descr.length(); 
      if (i >= j) {
        i = paramInt;
        j = this.descr.indexOf(';', paramInt + 1);
        if (j < 0)
          j = this.descr.length(); 
      } 
      this.bestMatch = this.descr.substring(i, j).trim();
    } 
  }
  
  public void autoBestMatch(String paramString) {
    String str = this.descr.toLowerCase();
    if (str.indexOf(paramString) > -1) {
      this.bestMatch = this.descr;
    } else {
      str = this.netname.toLowerCase();
      if (str.indexOf(paramString) > -1) {
        this.bestMatch = this.netname;
      } else {
        this.bestMatch = this.descr;
      } 
    } 
  }
  
  public boolean equals(IpRangeDescr paramIpRangeDescr) {
    return (this.start == paramIpRangeDescr.start && this.end == paramIpRangeDescr.end);
  }
  
  public IpRangeDescr merge(IpRangeInterface paramIpRangeInterface) {
    long l1 = (this.start < paramIpRangeInterface.getStart()) ? this.start : paramIpRangeInterface.getStart();
    long l2 = (this.end > paramIpRangeInterface.getEnd()) ? this.end : paramIpRangeInterface.getEnd();
    return new IpRangeDescr(l1, l2, this.descr, this.netname);
  }
  
  public int compareTo(IpRangeInterface paramIpRangeInterface) {
    if (paramIpRangeInterface instanceof IpRangeDescr) {
      int i = compareHighlighted((IpRangeDescr)paramIpRangeInterface);
      if (i != 0)
        return i; 
    } 
    return super.compareTo(paramIpRangeInterface);
  }
  
  public int compareBlocked(IpRangeDescr paramIpRangeDescr) {
    int i = compareHighlighted(paramIpRangeDescr);
    return (i != 0) ? i : (this.blockState - paramIpRangeDescr.getBlockState());
  }
  
  public int compareDescription(IpRangeDescr paramIpRangeDescr) {
    int i = compareHighlighted(paramIpRangeDescr);
    if (i != 0)
      return i; 
    String str1 = this.bestMatch.toLowerCase();
    String str2 = paramIpRangeDescr.getBestMatch().toLowerCase();
    return str1.compareTo(str2);
  }
  
  public int compareHighlighted(IpRangeDescr paramIpRangeDescr) {
    return paramIpRangeDescr.isHighlighted() - this.highlighted;
  }
  
  public int compareSelected(IpRangeDescr paramIpRangeDescr) {
    int i = compareHighlighted(paramIpRangeDescr);
    if (i != 0)
      return i; 
    i = this.selected ? 1 : 0;
    byte b = paramIpRangeDescr.getSelected() ? 1 : 0;
    return i - b;
  }
  
  public IpRange toIpRange() {
    return new IpRange(this.start, this.end);
  }
  
  public String getStartString() {
    return ipv4.fromLong(this.start);
  }
  
  public String getEndString() {
    return ipv4.fromLong(this.end);
  }
  
  public void setRange(long paramLong1, long paramLong2) {
    this.start = paramLong1;
    this.end = paramLong2;
  }
  
  public byte getBlockState() {
    return this.blockState;
  }
  
  public String getSmartRangeString() {
    return (this.start == this.end) ? ipv4.fromLong(this.start) : ipv4.fromLong(this.start).concat("-").concat(ipv4.fromLong(this.end));
  }
  
  public String getRangeString() {
    return ipv4.fromLong(this.start).concat("-").concat(ipv4.fromLong(this.end));
  }
  
  public String getDescription() {
    return this.descr;
  }
  
  public String getNetName() {
    return this.netname;
  }
  
  public void setBlockState(byte paramByte) {
    this.blockState = paramByte;
  }
  
  public boolean getSelected() {
    return this.selected;
  }
  
  public void setSelected(boolean paramBoolean) {
    this.selected = paramBoolean;
  }
  
  public byte isHighlighted() {
    return this.highlighted;
  }
  
  public void setHighlighted(int paramInt) {
    this.highlighted = (byte)paramInt;
  }
  
  public String toToolTip() {
    return "<html>" + this.netname + "<br>" + this.descr + "</html>";
  }
  
  public String toBlocklistEntry() {
    String str;
    if (this.bestMatch != null)
      return this.bestMatch + ":" + getRangeString(); 
    int i = this.descr.indexOf(";");
    if (i > -1) {
      str = this.descr.substring(0, i) + ":" + getRangeString();
    } else {
      str = this.descr + ":" + getRangeString();
    } 
    return str;
  }
  
  public String toString() {
    return getRangeString().concat(":").concat(this.netname).concat(":").concat(this.descr);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\IpRangeDescr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */