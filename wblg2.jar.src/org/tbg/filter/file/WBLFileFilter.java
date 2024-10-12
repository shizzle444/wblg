package org.tbg.filter.file;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class WBLFileFilter extends FileFilter {
  public boolean accept(File paramFile) {
    if (paramFile.isDirectory())
      return true; 
    String str = paramFile.getName();
    return str.endsWith(".wbl.gz");
  }
  
  public String getDescription() {
    return "Gzip compressed whois-blocklist";
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\file\WBLFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */