package org.tbg.arin;

import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.Translate;
import org.htmlparser.visitors.NodeVisitor;

public class ResultExtractVisitor extends NodeVisitor {
  private StringBuffer textAccumulator = new StringBuffer();
  
  private boolean preTagBeingProcessed = false;
  
  public String getExtractedText() {
    return this.textAccumulator.toString();
  }
  
  public void visitStringNode(Text paramText) {
    if (!this.preTagBeingProcessed)
      return; 
    String str = paramText.getText();
    if (str.length() == 0)
      return; 
    if (str.indexOf("# ARIN WHOIS database") > -1) {
      this.preTagBeingProcessed = false;
      return;
    } 
    str = Translate.decode(str);
    if (!this.preTagBeingProcessed)
      str = replaceNonBreakingSpaceWithOrdinarySpace(str); 
    this.textAccumulator.append(str);
  }
  
  private String replaceNonBreakingSpaceWithOrdinarySpace(String paramString) {
    return paramString.replace(' ', ' ');
  }
  
  public void visitTag(Tag paramTag) {
    if (isPreTag(paramTag))
      this.preTagBeingProcessed = true; 
  }
  
  public void visitEndTag(Tag paramTag) {
    if (isPreTag(paramTag))
      this.preTagBeingProcessed = false; 
  }
  
  private boolean isPreTag(Tag paramTag) {
    return paramTag.getTagName().equals("PRE");
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\arin\ResultExtractVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */