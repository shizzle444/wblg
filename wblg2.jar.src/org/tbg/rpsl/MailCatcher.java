package org.tbg.rpsl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailCatcher {
  private static Pattern p = Pattern.compile(".*?([^ <]+@.+\\.[A-Za-z]+).*");
  
  private static HashSet<String> ignoredDomains;
  
  public MailCatcher(String paramString) {
    try {
      ignoredDomains = new HashSet<>();
      BufferedReader bufferedReader = new BufferedReader(new FileReader(paramString));
      while (bufferedReader.ready()) {
        String str = bufferedReader.readLine();
        ignoredDomains.add(str);
      } 
    } catch (Exception exception) {}
  }
  
  public String check(String paramString) {
    Matcher matcher = p.matcher(paramString);
    if (matcher.matches()) {
      String str = paramString.substring(matcher.start(1), matcher.end(1));
      if (!ignoredDomains.contains(str.substring(str.indexOf('@') + 1)))
        return str; 
    } 
    return null;
  }
  
  public static void main(String[] paramArrayOfString) {
    MailCatcher mailCatcher = new MailCatcher(paramArrayOfString[0]);
    String str = mailCatcher.check(paramArrayOfString[1]);
    if (str != null)
      System.out.println(str); 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\rpsl\MailCatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */