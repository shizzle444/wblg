package org.tbg.wbl;

public class WBLTokenizer {
  private String data;
  
  private int a;
  
  private int b;
  
  private int c;
  
  public WBLTokenizer(String paramString) {
    this.data = new String(paramString);
    this.a = this.data.indexOf(":");
    this.b = this.data.indexOf(":", this.a + 1);
    String str = this.data.substring(0, this.a);
    this.c = str.indexOf("-");
  }
  
  public String getStartOfRange() {
    return this.data.substring(0, this.c);
  }
  
  public String getEndOfRange() {
    return this.data.substring(this.c + 1, this.a);
  }
  
  public String getRange() {
    return this.data.substring(0, this.a);
  }
  
  public String getNetName() {
    return this.data.substring(this.a + 1, this.b);
  }
  
  public String getDescription() {
    return this.data.substring(this.b + 1);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\wbl\WBLTokenizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */