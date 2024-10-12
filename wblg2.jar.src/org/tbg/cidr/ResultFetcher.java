package org.tbg.cidr;

import java.io.IOException;
import java.util.Vector;
import org.htmlparser.Parser;
import org.tbg.filter.ird.IRDFilter;
import org.tbg.ip.IpRange;
import org.tbg.ip.IpRangeDescr;
import org.tbg.util.SAPReport;

public class ResultFetcher {
  private Parser p;
  
  private SAPReport reporter;
  
  private boolean abortSearch;
  
  public ResultFetcher(SAPReport paramSAPReport) {
    this.reporter = paramSAPReport;
    this.abortSearch = false;
  }
  
  public Vector<IpRangeDescr> searchSingle(ASN paramASN, IRDFilter paramIRDFilter) {
    Vector<IpRangeDescr> vector = new Vector();
    try {
      this.reporter.reportStatus("cidr-report: " + paramASN.asn);
      this.p = new Parser(Query.makeSearchQuery(paramASN.asn).openConnection());
      ASNSearchVisitor aSNSearchVisitor = new ASNSearchVisitor();
      this.p.visitAllNodesWith(aSNSearchVisitor);
      Vector<IpRange> vector1 = aSNSearchVisitor.getData();
      String str = paramASN.description;
      if (str == null)
        str = aSNSearchVisitor.getDescription(); 
      for (IpRange ipRange : vector1) {
        IpRangeDescr ipRangeDescr = new IpRangeDescr(ipRange, str, paramASN.asn);
        if (paramIRDFilter.match(ipRangeDescr))
          vector.add(ipRangeDescr); 
      } 
      if (vector1.size() == 0)
        this.reporter.reportError("Search for: " + paramASN.asn + " did not contain any IP ranges"); 
    } catch (Exception exception) {
      this.reporter.reportException(exception);
    } 
    return vector;
  }
  
  public Vector<IpRangeDescr> search(Vector<ASN> paramVector, IRDFilter paramIRDFilter) {
    Vector<IpRangeDescr> vector = new Vector();
    int i = paramVector.size();
    byte b = 0;
    for (ASN aSN : paramVector) {
      vector.addAll(searchSingle(aSN, paramIRDFilter));
      this.reporter.reportProgress(++b * 100 / i);
      if (this.abortSearch)
        break; 
    } 
    return vector;
  }
  
  public void abort() {
    this.abortSearch = true;
    this.reporter.reportStatus("trying to abort online search - this may take a while");
    if (this.p != null)
      try {
        this.p.getLexer().getPage().close();
      } catch (IOException iOException) {} 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\cidr\ResultFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */