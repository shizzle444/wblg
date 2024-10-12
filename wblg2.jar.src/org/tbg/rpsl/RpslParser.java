package org.tbg.rpsl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import org.tbg.ip.IpRangeDescr;
import org.tbg.util.SAPReport;
import org.tbg.util.StringMatchHelper;

public class RpslParser {
  private int pState;
  
  private String inetnum;
  
  private String description;
  
  private String netname;
  
  private Vector<IpRangeDescr> data;
  
  private IRD_Consumer consumer;
  
  private MailCatcher mc;
  
  private HashSet<String> mstore;
  
  private static boolean extraInfo;
  
  private SAPReport reporter;
  
  private StringMatchHelper emailHelper;
  
  private StringMatchHelper fieldHelper;
  
  public RpslParser() {
    this.inetnum = new String();
    this.netname = new String();
    this.description = new String();
    this.data = new Vector<>();
    this.consumer = null;
    this.mc = new MailCatcher("ignored_mail_domains.txt");
    this.mstore = new HashSet<>();
    extraInfo = false;
    this.reporter = null;
  }
  
  public RpslParser(SAPReport paramSAPReport, IRD_Consumer paramIRD_Consumer, Vector<String> paramVector1, Vector<String> paramVector2) {
    this.reporter = paramSAPReport;
    this.inetnum = new String();
    this.netname = new String();
    this.description = new String();
    this.data = null;
    this.consumer = paramIRD_Consumer;
    this.mc = new MailCatcher("ignored_mail_domains.txt");
    this.mstore = new HashSet<>();
    extraInfo = false;
    if (paramVector1.size() > 0 || paramVector2.size() > 0) {
      extraInfo = true;
      this.emailHelper = new StringMatchHelper(paramVector1);
      this.fieldHelper = new StringMatchHelper(paramVector2);
    } 
  }
  
  public void loadStrippedGz(String paramString) throws FileNotFoundException, IOException {
    FileInputStream fileInputStream = new FileInputStream(paramString);
    int i = fileInputStream.available();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(fileInputStream)));
    this.pState = 0;
    while (bufferedReader.ready() == true) {
      if (this.reporter != null) {
        long l = (i - fileInputStream.available());
        l = l * 100L / i;
        this.reporter.reportProgress((int)l);
      } 
      String str = bufferedReader.readLine();
      try {
        handleLine(str);
      } catch (NoSuchElementException noSuchElementException) {
        System.out.println("Error for line: ".concat(this.inetnum));
        noSuchElementException.printStackTrace();
      } 
    } 
  }
  
  public void loadText(String paramString) {
    StringReader stringReader = new StringReader(paramString);
    BufferedReader bufferedReader = new BufferedReader(stringReader);
    try {
      String str;
      while ((str = bufferedReader.readLine()) != null)
        handleLine(str); 
    } catch (IOException iOException) {
      this.reporter.reportException(iOException);
    } 
  }
  
  private void handleLine(String paramString) {
    if (paramString.length() == 0) {
      if (this.pState == 1) {
        this.inetnum = this.inetnum.replace('\t', ' ');
        StringTokenizer stringTokenizer = new StringTokenizer(this.inetnum, " - ");
        mailsInStore();
        IpRangeDescr ipRangeDescr = new IpRangeDescr(stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), this.description, this.netname);
        if (this.data != null) {
          this.data.add(ipRangeDescr);
        } else {
          this.consumer.consume(ipRangeDescr);
        } 
        this.mstore.clear();
      } 
      this.pState = 0;
    } else if (this.pState == 3) {
      this.inetnum = this.inetnum.concat(paramString.trim());
      this.pState = 1;
    } else if (paramString.indexOf("inetnum:") == 0) {
      this.pState = 1;
      this.inetnum = paramString.substring(8).trim();
      this.description = new String();
      if (this.inetnum.indexOf("-") == -1)
        this.pState = 3; 
    } else if (paramString.indexOf("netname:") == 0) {
      this.netname = paramString.substring(8).trim();
    } else if (this.pState == 1 && paramString.indexOf("descr:") == 0) {
      String str = paramString.substring(6).trim();
      if (str.indexOf("~") == 0)
        return; 
      if (str.matches("^\\x2a+\r?$"))
        return; 
      if (str.matches("^=+\r?$"))
        return; 
      if (str.indexOf("reassign to \"") == 0) {
        int i = str.length();
        if (str.lastIndexOf('"') == i - 1)
          i--; 
        str = str.substring(13, i).trim();
      } 
      if (str.endsWith(",") == true)
        str = str.substring(0, str.length() - 1); 
      storeDescr(str);
    } else if (extraInfo == true && this.pState == 1) {
      if (this.emailHelper.matches(paramString)) {
        checkMailAndStore(this.emailHelper.getMatch());
      } else if (this.fieldHelper.matches(paramString)) {
        this.mstore.add(this.fieldHelper.getMatch());
      } 
    } 
  }
  
  private void checkMailAndStore(String paramString) {
    String str = this.mc.check(paramString);
    if (str != null)
      this.mstore.add(str); 
  }
  
  private void mailsInStore() {
    Iterator<String> iterator = this.mstore.iterator();
    while (iterator.hasNext())
      storeDescr(iterator.next()); 
  }
  
  private void storeDescr(String paramString) {
    if (this.description.length() > 0) {
      this.description = this.description.concat("; ").concat(paramString);
    } else {
      this.description = this.description.concat(paramString);
    } 
  }
  
  public void output() {
    Collections.sort(this.data);
    int i = this.data.size();
    for (byte b = 0; b < i; b++)
      System.out.println(((IpRangeDescr)this.data.elementAt(b)).toString()); 
  }
  
  public Vector<IpRangeDescr> getData() {
    Collections.sort(this.data);
    return this.data;
  }
  
  public static void main(String[] paramArrayOfString) {
    RpslParser rpslParser = new RpslParser();
    try {
      System.out.println("loading...");
      rpslParser.loadStrippedGz(paramArrayOfString[0]);
      System.out.println("sorting...");
      rpslParser.output();
    } catch (Exception exception) {
      System.out.println(exception.toString());
      exception.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\rpsl\RpslParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */