package org.tbg.cidr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import org.tbg.filter.WBLLoaderFilter;

public class AsnListParser {
  private FileInputStream fin;
  
  private BufferedReader br;
  
  public AsnListParser(String paramString) {
    try {
      this.fin = new FileInputStream(paramString);
      this.br = new BufferedReader(new InputStreamReader(new GZIPInputStream(this.fin)));
    } catch (Exception exception) {}
  }
  
  public Vector<ASN> findMatching(WBLLoaderFilter paramWBLLoaderFilter) {
    Vector<ASN> vector = new Vector();
    try {
      while (this.br.ready() == true) {
        String str1 = this.br.readLine();
        StringTokenizer stringTokenizer = new StringTokenizer(str1, ":");
        String str2 = stringTokenizer.nextToken();
        String str3 = stringTokenizer.nextToken();
        if (paramWBLLoaderFilter.match(str3) != -1)
          vector.add(new ASN(str2, str3)); 
      } 
    } catch (Exception exception) {}
    return vector;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\cidr\AsnListParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */