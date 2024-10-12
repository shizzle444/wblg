package org.tbg.ip;

public class IpRangeDescrFactory implements RC_Factory<IpRangeDescr> {
  public IpRangeDescr makeNew(String paramString) {
    return new IpRangeDescr(paramString);
  }
  
  public IpRangeDescr makeNew(String paramString1, String paramString2, String paramString3) {
    return new IpRangeDescr(paramString1, paramString2, paramString3, "");
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\IpRangeDescrFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */