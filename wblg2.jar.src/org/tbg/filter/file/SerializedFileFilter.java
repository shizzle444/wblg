package org.tbg.filter.file;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class SerializedFileFilter extends FileFilter {
  public boolean accept(File paramFile) {
    return paramFile.isDirectory() ? true : paramFile.getName().endsWith(".bin.gz");
  }
  
  public String getDescription() {
    return "Gzip compressed search result data";
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\filter\file\SerializedFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */