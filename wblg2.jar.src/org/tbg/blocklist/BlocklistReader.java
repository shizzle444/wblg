package org.tbg.blocklist;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;
import org.tbg.gui.WBLGPanel;
import org.tbg.ip.IpRangeInterface;
import org.tbg.ip.TConsumer;
import org.tbg.util.CompressionType;

public class BlocklistReader<T extends IpRangeInterface> {
  private long numLines = 0L;
  
  private CompressionType compressionType;
  
  private TConsumer<T> rc;
  
  public BlocklistReader(TConsumer<T> paramTConsumer) {
    this.rc = paramTConsumer;
  }
  
  private void handleReader(BufferedReader paramBufferedReader) throws IOException {
    String str = null;
    try {
      while (paramBufferedReader.ready() == true) {
        str = paramBufferedReader.readLine();
        int i = str.lastIndexOf(':');
        if (i == -1)
          throw new IllegalStateException("Input line not tokenized: " + str); 
        String str1 = str.substring(i + 1).trim();
        String str2 = str.substring(0, i).trim();
        this.rc.addString(str1, str2);
        this.numLines++;
      } 
    } catch (NumberFormatException numberFormatException) {
      (WBLGPanel.getInstance()).sap.reportError("Error loading blocklist line (" + this.numLines + "): " + str);
      throw numberFormatException;
    } 
  }
  
  public void loadGzipFile(String paramString) throws FileNotFoundException, IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(paramString))));
    handleReader(bufferedReader);
    bufferedReader.close();
    this.compressionType = CompressionType.GZIP;
  }
  
  public void loadZipFile(String paramString) throws FileNotFoundException, IOException {
    ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(paramString));
    zipInputStream.getNextEntry();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipInputStream));
    handleReader(bufferedReader);
    bufferedReader.close();
    this.compressionType = CompressionType.ZIP;
  }
  
  public void loadTxtFile(String paramString) throws FileNotFoundException, IOException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(paramString));
    handleReader(bufferedReader);
    bufferedReader.close();
    this.compressionType = CompressionType.TXT;
  }
  
  public long loadFile(String paramString) throws FileNotFoundException, IOException, Exception {
    this.numLines = 0L;
    if (paramString.endsWith(".zip") == true) {
      loadZipFile(paramString);
    } else if (paramString.endsWith(".gz") == true) {
      loadGzipFile(paramString);
    } else if (paramString.endsWith(".txt") == true) {
      loadTxtFile(paramString);
    } else if (paramString.endsWith(".p2p") == true) {
      loadTxtFile(paramString);
    } else {
      throw new Exception((new String("Unknown file-format: ")).concat(paramString));
    } 
    return this.numLines;
  }
  
  public Vector<T> getData() {
    return this.rc.done();
  }
  
  public void clear() {
    this.rc.clear();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\blocklist\BlocklistReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */