package org.tbg.cidr;

public class ASN {
  public String asn;
  
  public String description;
  
  public ASN(String paramString1, String paramString2) {
    this.asn = new String(paramString1);
    this.description = new String(paramString2);
  }
  
  public ASN(String paramString) {
    this.asn = new String(paramString);
    this.description = null;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\cidr\ASN.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */