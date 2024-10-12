package org.tbg.ip;

public class IpRangeFactory implements RC_Factory<IpRange> {
  public IpRange makeNew(String paramString) {
    return new IpRange(paramString);
  }
  
  public IpRange makeNew(String paramString1, String paramString2, String paramString3) {
    return new IpRange(paramString1, paramString2);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\IpRangeFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */