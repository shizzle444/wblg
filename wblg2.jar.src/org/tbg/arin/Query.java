package org.tbg.arin;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Query {
  private static String baseUrl = "http://ws.arin.net/whois/";
  
  public static URL makeSearchQuery(String paramString) throws MalformedURLException, UnsupportedEncodingException {
    String str = baseUrl + "?queryinput=" + URLEncoder.encode("- " + paramString, "UTF-8");
    return new URL(str);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\arin\Query.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */