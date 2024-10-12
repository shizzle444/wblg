package org.tbg.ripe;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Query {
  private static String simpleBase = "http://www.db.ripe.net/whois/?form_type=simple&searchtext=";
  
  public static URL makeSimpleQuery(String paramString) throws MalformedURLException, UnsupportedEncodingException {
    String str = simpleBase + URLEncoder.encode(paramString, "UTF-8");
    return new URL(str);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ripe\Query.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */