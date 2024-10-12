package org.tbg.arinws;

import java.util.HashMap;
import java.util.Vector;
import org.tbg.filter.ird.IRDFilter;
import org.tbg.ip.IpRangeDescr;
import org.tbg.util.SAPReport;

public class ResultFetcherWS {
  private SAPReport reporter;
  
  private static boolean abortSearch = false;
  
  private boolean lookupASNs;
  
  public ResultFetcherWS(SAPReport paramSAPReport, boolean paramBoolean) {
    this.reporter = paramSAPReport;
    abortSearch = false;
    this.lookupASNs = paramBoolean;
  }
  
  public Vector<IpRangeDescr> searchSingle(String paramString, IRDFilter paramIRDFilter) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    throw new RuntimeException("not implemented");
  }
  
  public Vector<IpRangeDescr> search(Vector<String> paramVector, IRDFilter paramIRDFilter) {
    abortSearch = false;
    Vector<IpRangeDescr> vector = new Vector();
    int i = paramVector.size();
    for (byte b = 0; b < i; b++) {
      vector.addAll(searchSingle(paramVector.elementAt(b), paramIRDFilter));
      if (abortSearch)
        break; 
      this.reporter.reportProgress((b + 1) * 100 / i);
    } 
    return vector;
  }
  
  public void abort() {
    abortSearch = true;
    this.reporter.reportStatus("trying to abort online search - this may take a while");
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\arinws\ResultFetcherWS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */