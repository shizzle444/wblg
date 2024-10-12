package org.tbg.ripe;

import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.Translate;
import org.htmlparser.visitors.NodeVisitor;

public class SimpleTextExtractor extends NodeVisitor {
  private StringBuffer textAccumulator = new StringBuffer();
  
  private boolean isInside = false;
  
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
    if (str.indexOf("Information related to") > -1) {
      this.isInside = true;
      this.textAccumulator.append("\n");
      return;
    } 
    if (!this.isInside)
      return; 
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
    if (paramTag.getTagName().equals("legend"))
      this.isInside = false; 
  }
  
  public void visitEndTag(Tag paramTag) {
    if (isPreTag(paramTag))
      this.preTagBeingProcessed = false; 
  }
  
  private boolean isPreTag(Tag paramTag) {
    return paramTag.getTagName().equals("PRE");
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ripe\SimpleTextExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */