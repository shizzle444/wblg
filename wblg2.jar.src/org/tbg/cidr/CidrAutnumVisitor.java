package org.tbg.cidr;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.visitors.NodeVisitor;

public class CidrAutnumVisitor extends NodeVisitor {
  private boolean insideLink = false;
  
  private boolean insidePre = false;
  
  private String lastASN = null;
  
  private String nl = System.getProperty("line.separator");
  
  private Consumer consumer;
  
  public CidrAutnumVisitor(String paramString) {
    this.consumer = new SavingConsumer(paramString);
  }
  
  public void visitStringNode(Text paramText) {
    if (!this.insidePre)
      return; 
    if (this.insideLink)
      return; 
    if (this.lastASN == null)
      return; 
    String str = paramText.getText().replace("\r?\n", "");
    this.consumer.consume(this.lastASN, str.trim());
  }
  
  public void visitTag(Tag paramTag) {
    if (isTag(paramTag, "A")) {
      LinkTag linkTag = (LinkTag)paramTag;
      this.lastASN = extractASN(linkTag.getLink());
      this.insideLink = true;
    } else if (isTag(paramTag, "PRE")) {
      this.insidePre = true;
    } 
  }
  
  public void visitEndTag(Tag paramTag) {
    if (isTag(paramTag, "A")) {
      this.insideLink = false;
    } else if (isTag(paramTag, "PRE")) {
      this.insidePre = false;
      this.consumer.done();
    } 
  }
  
  private boolean isTag(Tag paramTag, String paramString) {
    return paramTag.getTagName().equals(paramString);
  }
  
  private String extractASN(String paramString) {
    int i = paramString.indexOf("as=");
    int j = paramString.indexOf("&");
    return (i == -1 || j == -1) ? null : paramString.substring(i + 3, j);
  }
  
  public static void main(String[] paramArrayOfString) {
    String str = "http://www.cidr-report.org/as2.0/autnums.html";
    try {
      Parser parser = new Parser(str);
      parser.visitAllNodesWith(new CidrAutnumVisitor(paramArrayOfString[0]));
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public class SavingConsumer implements Consumer {
    private GZIPOutputStream out;
    
    private DataOutputStream dout;
    
    public SavingConsumer(String param1String) {
      try {
        this.out = new GZIPOutputStream(new FileOutputStream(param1String));
        this.dout = new DataOutputStream(this.out);
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
    }
    
    public void consume(String param1String1, String param1String2) {
      try {
        this.dout.writeBytes(param1String1 + ":" + param1String2 + CidrAutnumVisitor.this.nl);
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
    }
    
    public void done() {
      try {
        this.out.finish();
        this.out.close();
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
    }
  }
  
  public static interface Consumer {
    void consume(String param1String1, String param1String2);
    
    void done();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\cidr\CidrAutnumVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */