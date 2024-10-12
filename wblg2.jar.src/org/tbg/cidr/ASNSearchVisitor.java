package org.tbg.cidr;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.visitors.NodeVisitor;
import org.tbg.ip.IpRange;
import org.tbg.ip.IpRangeFactory;
import org.tbg.ip.RC_Factory;
import org.tbg.ip.RangeCollector;

public class ASNSearchVisitor extends NodeVisitor {
  private boolean insidePre = false;
  
  private int insideData = 0;
  
  private boolean insideAnchor = false;
  
  private Pattern cidrIpPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/\\d{1,2}");
  
  private RangeCollector collect = new RangeCollector((RC_Factory)new IpRangeFactory());
  
  private String descr = null;
  
  private int ulCount = 0;
  
  public void visitStringNode(Text paramText) {
    if (this.ulCount == 1)
      this.descr = paramText.getText().trim(); 
    String str = paramText.getText();
    if (this.insidePre && str.indexOf("Prefix") > -1 && str.indexOf("AS Path") > -1)
      this.insideData = 1; 
    if (this.insidePre && this.insideAnchor && this.insideData == 1) {
      Matcher matcher = this.cidrIpPattern.matcher(str);
      if (matcher.matches()) {
        String str1 = matcher.group();
        this.collect.addCidrString(str1);
      } 
    } 
  }
  
  public void visitTag(Tag paramTag) {
    if (isTag(paramTag, "PRE"))
      this.insidePre = true; 
    if (isTag(paramTag, "A"))
      this.insideAnchor = true; 
    if (isTag(paramTag, "UL"))
      this.ulCount++; 
  }
  
  public void visitEndTag(Tag paramTag) {
    if (isTag(paramTag, "PRE")) {
      this.insidePre = false;
      if (this.insideData == 1)
        this.insideData = 2; 
    } 
    if (isTag(paramTag, "A"))
      this.insideAnchor = false; 
    if (isTag(paramTag, "UL"))
      this.ulCount++; 
  }
  
  private boolean isTag(Tag paramTag, String paramString) {
    return paramTag.getTagName().equals(paramString);
  }
  
  public Vector<IpRange> getData() {
    return this.collect.done();
  }
  
  public String getDescription() {
    return this.descr;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\cidr\ASNSearchVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */