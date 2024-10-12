package org.tbg.ripe;

import java.io.IOException;
import java.util.Vector;
import org.htmlparser.Parser;
import org.tbg.filter.ird.IRDFilter;
import org.tbg.gui.WBLGPanel;
import org.tbg.ip.IpRangeDescr;
import org.tbg.rpsl.IRD_Consumer;
import org.tbg.rpsl.RpslParser;
import org.tbg.util.SAPReport;

public class SimpleFetcher {
  private SAPReport rep;
  
  private static boolean abortSearch = false;
  
  private Parser p;
  
  WBLGPanel wblg;
  
  Vector<String> emailImport;
  
  Vector<String> fieldsImport;
  
  Consumer cons = null;
  
  RpslParser rpsl = null;
  
  public SimpleFetcher(SAPReport paramSAPReport) {
    this.rep = paramSAPReport;
    this.wblg = WBLGPanel.getInstance();
    this.emailImport = this.wblg.configPanel.getEmailItemList().getActiveList();
    this.fieldsImport = this.wblg.configPanel.getFieldsItemList().getActiveList();
  }
  
  private void searchSingle(String paramString) {
    try {
      this.rep.reportStatus("searching ripe: " + paramString);
      this.p = new Parser(Query.makeSimpleQuery(paramString).openConnection());
      SimpleTextExtractor simpleTextExtractor = new SimpleTextExtractor();
      this.p.visitAllNodesWith(simpleTextExtractor);
      this.rpsl.loadText(simpleTextExtractor.getExtractedText());
    } catch (Exception exception) {
      this.rep.reportException(exception);
    } 
  }
  
  public Vector<IpRangeDescr> search(Vector<String> paramVector, IRDFilter paramIRDFilter) {
    this.cons = new Consumer(paramIRDFilter);
    this.rpsl = new RpslParser(this.rep, this.cons, this.emailImport, this.fieldsImport);
    int i = paramVector.size();
    byte b = 0;
    for (String str : paramVector) {
      if (abortSearch)
        break; 
      searchSingle(str);
      this.rep.reportProgress(++b * 100 / i);
    } 
    return this.cons.getData();
  }
  
  public void abort() {
    abortSearch = true;
    this.rep.reportStatus("trying to abort online search - this may take a while");
    if (this.p != null)
      try {
        this.p.getLexer().getPage().close();
      } catch (IOException iOException) {} 
  }
  
  class Consumer implements IRD_Consumer {
    private Vector<IpRangeDescr> data = new Vector<>();
    
    private IRDFilter filter;
    
    public Consumer(IRDFilter param1IRDFilter) {
      this.filter = param1IRDFilter;
    }
    
    public void consume(IpRangeDescr param1IpRangeDescr) {
      if (this.filter.match(param1IpRangeDescr))
        this.data.add(param1IpRangeDescr); 
    }
    
    public Vector<IpRangeDescr> getData() {
      return this.data;
    }
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ripe\SimpleFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */