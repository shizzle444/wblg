package org.tbg.fileio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipWriter extends Writer {
  final FileOutputStream fos;
  
  final ZipOutputStream zipFile;
  
  final OutputStreamWriter writer;
  
  public ZipWriter(File paramFile) throws IOException {
    this.fos = new FileOutputStream(paramFile);
    this.zipFile = new ZipOutputStream(this.fos);
    ZipEntry zipEntry = new ZipEntry(paramFile.getName());
    this.zipFile.putNextEntry(zipEntry);
    this.writer = new OutputStreamWriter(this.zipFile);
  }
  
  public void close() throws IOException {
    this.writer.close();
  }
  
  public void flush() throws IOException {
    this.writer.flush();
  }
  
  public void write(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException {
    this.writer.write(paramArrayOfchar, paramInt1, paramInt2);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\fileio\ZipWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */