package org.tbg.wbl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.tbg.exceptions.EmptyWBLException;
import org.tbg.filter.NotFiltered;
import org.tbg.filter.WBLLoaderFilter;
import org.tbg.filter.ird.IRDFilter;
import org.tbg.filter.ird.IRDSizeFilter;
import org.tbg.ip.IpRangeDescr;
import org.tbg.util.SAPReport;

public class WBLReader {
  public static SAPReport reporter = null;
  
  public static boolean abortSearch = false;
  
  public static Vector<IpRangeDescr> loadFiltered(String paramString, WBLLoaderFilter paramWBLLoaderFilter, IRDFilter paramIRDFilter) throws FileNotFoundException, IOException, EmptyWBLException, OutOfMemoryError {
    abortSearch = false;
    FileInputStream fileInputStream = new FileInputStream(paramString);
    int i = fileInputStream.available();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(fileInputStream)));
    Vector<IpRangeDescr> vector = new Vector();
    while (!abortSearch && bufferedReader.ready() == true) {
      if (reporter != null) {
        long l = (i - fileInputStream.available());
        l = l * 100L / i;
        reporter.reportProgress((int)l);
      } 
      String str = bufferedReader.readLine();
      if (str == null)
        throw new EmptyWBLException(paramString); 
      WBLTokenizer wBLTokenizer = new WBLTokenizer(str);
      int j = paramWBLLoaderFilter.match(wBLTokenizer.getDescription());
      if (j > -1) {
        IpRangeDescr ipRangeDescr = new IpRangeDescr(wBLTokenizer.getStartOfRange(), wBLTokenizer.getEndOfRange(), (byte)0, wBLTokenizer.getDescription(), wBLTokenizer.getNetName());
        if (paramIRDFilter.match(ipRangeDescr)) {
          ipRangeDescr.bestMatchFromDescription(j);
          vector.add(ipRangeDescr);
        } 
        continue;
      } 
      j = paramWBLLoaderFilter.match(wBLTokenizer.getNetName());
      if (j > -1) {
        IpRangeDescr ipRangeDescr = new IpRangeDescr(wBLTokenizer.getStartOfRange(), wBLTokenizer.getEndOfRange(), (byte)0, wBLTokenizer.getDescription(), wBLTokenizer.getNetName());
        if (paramIRDFilter.match(ipRangeDescr)) {
          ipRangeDescr.setBestMatch(ipRangeDescr.getNetName());
          vector.add(ipRangeDescr);
        } 
      } 
    } 
    return vector;
  }
  
  public static Vector<IpRangeDescr> load(String paramString) throws FileNotFoundException, IOException, EmptyWBLException, OutOfMemoryError {
    NotFiltered notFiltered = new NotFiltered();
    return loadFiltered(paramString, (WBLLoaderFilter)notFiltered, (IRDFilter)new IRDSizeFilter(32, 0));
  }
  
  public static void save(String paramString, Vector<IpRangeDescr> paramVector) throws FileNotFoundException, IOException {
    GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(new FileOutputStream(paramString));
    DataOutputStream dataOutputStream = new DataOutputStream(gZIPOutputStream);
    int i = paramVector.size();
    for (byte b = 0; b < i; b++) {
      if (reporter != null)
        reporter.reportProgress(b * 100 / i); 
      dataOutputStream.writeBytes(((IpRangeDescr)paramVector.elementAt(b)).toString().concat("\n"));
    } 
    gZIPOutputStream.finish();
    gZIPOutputStream.close();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\wbl\WBLReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */