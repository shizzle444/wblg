package org.tbg.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.tbg.fileio.DeflateWriter;
import org.tbg.fileio.ZipWriter;

public enum CompressionType {
  ZIP("zip", "Zip compressed file"),
  GZIP("gz", "GZip/deflate compressed file"),
  TXT("txt", "Plaintext file");
  
  private final String extension;
  
  private final String description;
  
  CompressionType(String paramString1, String paramString2) {
    this.extension = paramString1;
    this.description = paramString2;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public String getExtension() {
    return "." + this.extension;
  }
  
  public Writer getFileWriter(String paramString) throws IOException {
    return (Writer)(equals(ZIP) ? new ZipWriter(new File(paramString)) : (equals(GZIP) ? new DeflateWriter(new File(paramString)) : new FileWriter(paramString)));
  }
  
  public String toString() {
    return this.description;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\CompressionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */