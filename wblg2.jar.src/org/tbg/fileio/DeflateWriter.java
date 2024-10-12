package org.tbg.fileio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class DeflateWriter extends Writer {
  private final FileOutputStream fos;
  
  private final OutputStreamWriter writer;
  
  public DeflateWriter(File paramFile) throws IOException {
    this.fos = new FileOutputStream(paramFile);
    this.writer = new OutputStreamWriter(this.fos);
  }
  
  public void write(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException {
    this.writer.write(paramArrayOfchar, paramInt1, paramInt2);
  }
  
  public void flush() throws IOException {
    this.writer.flush();
  }
  
  public void close() throws IOException {
    this.writer.close();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\fileio\DeflateWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */