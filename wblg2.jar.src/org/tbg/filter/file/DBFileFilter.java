package org.tbg.filter.file;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class DBFileFilter extends FileFilter {
  public boolean accept(File paramFile) {
    if (paramFile.isDirectory())
      return true; 
    String str = paramFile.getName();
    return str.endsWith(".gz");
  }
  
  public String getDescription() {
    return "Gzip compressed whois-database";
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\file\DBFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */