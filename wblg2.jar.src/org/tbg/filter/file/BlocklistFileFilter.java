package org.tbg.filter.file;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class BlocklistFileFilter extends FileFilter {
  public boolean accept(File paramFile) {
    if (paramFile.isDirectory())
      return true; 
    String str = paramFile.getName();
    return (str.endsWith(".gz") || str.endsWith(".zip")) ? true : (str.endsWith(".txt") ? true : (str.endsWith(".p2p")));
  }
  
  public String getDescription() {
    return "Gzip/Zip compressed (or uncompressed) blocklist";
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\file\BlocklistFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */