package org.tbg.arin;

import java.io.IOException;
import java.util.Vector;
import org.htmlparser.Parser;
import org.tbg.cidr.ASN;
import org.tbg.filter.ird.IRDFilter;
import org.tbg.ip.IpRangeDescr;
import org.tbg.util.SAPReport;

public class ResultFetcher {
  private SAPReport reporter;
  
  private static boolean abortSearch = false;
  
  private boolean lookupASNs;
  
  private Parser p;
  
  public ResultFetcher(SAPReport paramSAPReport, boolean paramBoolean) {
    this.reporter = paramSAPReport;
    this.p = null;
    abortSearch = false;
    this.lookupASNs = paramBoolean;
  }
  
  public Vector<IpRangeDescr> searchSingle(String paramString, IRDFilter paramIRDFilter) {
    try {
      if (paramString.indexOf("n ") == 0) {
        str = paramString.substring(2).toLowerCase();
      } else {
        str = paramString.toLowerCase();
      } 
      if (str.indexOf('*') > -1)
        str = str.substring(0, str.indexOf('*')); 
      String str = str.replaceFirst("^_+", "");
      this.reporter.reportStatus("searching arin: " + paramString);
      this.p = new Parser(Query.makeSearchQuery(paramString).openConnection());
      ResultExtractVisitor resultExtractVisitor = new ResultExtractVisitor();
      this.p.visitAllNodesWith(resultExtractVisitor);
      ResultTransformer resultTransformer = new ResultTransformer(resultExtractVisitor.getExtractedText());
      Vector<IpRangeDescr> vector1 = resultTransformer.fetchAll();
      if (resultTransformer.getRec256())
        if (vector1.size() == 0) {
          this.reporter.reportError("Search term too broad: arin truncated results; no valid IP ranges where found");
        } else {
          this.reporter.reportError("Arin truncated results - narrow your search");
        }  
      Vector<IpRangeDescr> vector2 = new Vector();
      for (byte b = 0; b < vector1.size(); b++) {
        IpRangeDescr ipRangeDescr = vector1.elementAt(b);
        if (paramIRDFilter.match(ipRangeDescr)) {
          ipRangeDescr.autoBestMatch(str);
          vector2.add(ipRangeDescr);
        } else {
          this.reporter.reportStatus("Ignoring " + ipRangeDescr.toString());
        } 
      } 
      if (this.lookupASNs) {
        ResultTransformerASN resultTransformerASN = new ResultTransformerASN(resultExtractVisitor.getExtractedText());
        Vector<ASN> vector = resultTransformerASN.fetchAll();
        if (vector.size() > 0) {
          org.tbg.cidr.ResultFetcher resultFetcher = new org.tbg.cidr.ResultFetcher(this.reporter);
          vector2.addAll(resultFetcher.search(vector, paramIRDFilter));
        } 
      } 
      return vector2;
    } catch (Exception exception) {
      this.reporter.reportException(exception);
      return new Vector<>();
    } 
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
    if (this.p != null)
      try {
        this.p.getLexer().getPage().close();
      } catch (IOException iOException) {} 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\arin\ResultFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */