package org.tbg.cidr;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.zip.GZIPOutputStream;
import org.tbg.util.Configuration;
import org.tbg.util.SAPReport;

public class AutnumFetcher {
  private static String url = "https://www.cidr-report.org/as2.0/autnums.html";
  
  private SAPReport reporter;
  
  public AutnumFetcher(SAPReport paramSAPReport) {
    this.reporter = paramSAPReport;
  }
  
  public void convertToFile(String paramString) throws IOException, FileNotFoundException {
    URL uRL = new URL(url);
    HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
    int i = httpURLConnection.getResponseCode();
    if (i != 200) {
      this.reporter.reportError("Fetching " + url + " failed with code: " + i);
      return;
    } 
    this.reporter.reportStatus("Fetching " + url);
    GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(new FileOutputStream(paramString));
    DataOutputStream dataOutputStream = new DataOutputStream(gZIPOutputStream);
    int j = httpURLConnection.getContentLength();
    int k = 0;
    String str2 = System.getProperty("line.separator");
    String str1;
    while ((str1 = bufferedReader.readLine()) != null) {
      k += str1.length();
      this.reporter.reportProgress(k * 100 / j);
      int m = str1.indexOf("as=");
      if (m > -1) {
        String str3 = str1.substring(m + 3, str1.indexOf('&'));
        String str4 = str1.substring(str1.indexOf("</a>") + 4).trim();
        dataOutputStream.writeBytes(str3 + ":" + str4 + str2);
      } 
    } 
    bufferedReader.close();
    gZIPOutputStream.close();
    Date date = new Date();
    Configuration configuration = Configuration.getInstance();
    configuration.setProperty("cidr_autnum_updated", Long.toString(date.getTime()));
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\cidr\AutnumFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */