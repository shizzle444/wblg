package org.tbg.wbl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;
import org.tbg.arin.ResultFetcher;
import org.tbg.blocklist.BlocklistReader;
import org.tbg.exceptions.EmptyWBLException;
import org.tbg.filter.BasicFilter;
import org.tbg.filter.FuzzyFilter;
import org.tbg.filter.NotFiltered;
import org.tbg.filter.WBLLoaderFilter;
import org.tbg.filter.ird.IRDChildFilter;
import org.tbg.filter.ird.IRDFilter;
import org.tbg.filter.ird.IRDSizeFilter;
import org.tbg.filter.ird.SecondaryFilterChain;
import org.tbg.filter.ird.SecondaryMatchAll;
import org.tbg.ip.IRDAccum;
import org.tbg.ip.IpChecker;
import org.tbg.ip.IpRange;
import org.tbg.ip.IpRangeDescr;
import org.tbg.ip.IpRangeFactory;
import org.tbg.ip.IpRangeInterface;
import org.tbg.ip.RC_Factory;
import org.tbg.ip.RangeCollector;
import org.tbg.ip.TConsumer;
import org.tbg.ripe.SimpleFetcher;
import org.tbg.util.Configuration;
import org.tbg.util.SAPReport;

public class WBLHolder {
  private static WBLHolder instance = null;
  
  private IpChecker<IpRange> ic = new IpChecker();
  
  private Vector<String> blockFiles = new Vector<>();
  
  private SAPReport reporter;
  
  private ResultFetcher arin;
  
  private SimpleFetcher ripe;
  
  public WBLHolder(SAPReport paramSAPReport) {
    this.reporter = paramSAPReport;
    this.arin = null;
    instance = this;
  }
  
  public static WBLHolder getInstance() {
    return instance;
  }
  
  public void loadBlocklists(Vector<String> paramVector) {
    if (this.blockFiles.equals(paramVector))
      return; 
    this.blockFiles = (Vector<String>)paramVector.clone();
    BlocklistReader blocklistReader = new BlocklistReader((TConsumer)new RangeCollector((RC_Factory)new IpRangeFactory()));
    long l = 0L;
    try {
      for (byte b = 0; b < paramVector.size(); b++) {
        this.reporter.reportStatus("loading " + (String)paramVector.elementAt(b));
        l += blocklistReader.loadFile(paramVector.elementAt(b));
      } 
    } catch (Exception exception) {
      this.reporter.reportException(exception);
      return;
    } 
    this.reporter.reportStatus("merging ranges");
    Vector vector = blocklistReader.getData();
    this.reporter.reportStatus("merged " + l + " to " + vector.size() + " ranges");
    this.ic.setData(vector);
  }
  
  public void updateBlocked(Vector<IpRangeDescr> paramVector) {
    this.reporter.reportStatus("updating entries");
    int i = paramVector.size();
    for (byte b = 0; b < i; b++) {
      IpRangeDescr ipRangeDescr = paramVector.elementAt(b);
      IpRange ipRange = ipRangeDescr.toIpRange();
      Integer integer = new Integer(this.ic.isBlockedNew((IpRangeInterface)ipRange));
      ipRangeDescr.setBlockState(integer.byteValue());
      this.reporter.reportProgress((b + 1) * 100 / i);
    } 
  }
  
  public Vector<IpRangeDescr> cleanSorted(Vector<IpRangeDescr> paramVector) {
    this.reporter.reportStatus("removing duplicate entries");
    Vector<IpRangeDescr> vector = new Vector();
    int i = paramVector.size();
    if (i == 0)
      return paramVector; 
    IpRangeDescr ipRangeDescr = paramVector.elementAt(0);
    vector.add(ipRangeDescr);
    for (byte b = 1; b < i; b++) {
      IpRangeDescr ipRangeDescr1 = paramVector.elementAt(b);
      if (!ipRangeDescr1.equals(ipRangeDescr)) {
        ipRangeDescr = ipRangeDescr1;
        vector.add(ipRangeDescr);
      } 
      this.reporter.reportProgress((b + 1) * 100 / i);
    } 
    return vector;
  }
  
  public Vector<IpRangeDescr> findChildren(Vector<String> paramVector, boolean paramBoolean, String paramString, IRDSizeFilter paramIRDSizeFilter) throws FileNotFoundException, IOException, EmptyWBLException {
    Vector<IpRangeDescr> vector = new Vector();
    SecondaryFilterChain secondaryFilterChain = new SecondaryFilterChain((IRDFilter)new IRDChildFilter(paramString));
    secondaryFilterChain.addFilter((IRDFilter)paramIRDSizeFilter);
    NotFiltered notFiltered = new NotFiltered();
    if (!paramBoolean) {
      for (byte b = 0; b < paramVector.size(); b++) {
        this.reporter.reportStatus("searching child ranges of " + paramString + " in " + (String)paramVector.elementAt(b));
        Vector<IpRangeDescr> vector1 = WBLReader.loadFiltered(paramVector.elementAt(b), (WBLLoaderFilter)notFiltered, (IRDFilter)secondaryFilterChain);
        vector.addAll(vector1);
        if (WBLReader.abortSearch == true)
          break; 
      } 
    } else {
      BlocklistReader blocklistReader = new BlocklistReader((TConsumer)new IRDAccum((WBLLoaderFilter)notFiltered, (IRDFilter)secondaryFilterChain));
      for (byte b = 0; b < paramVector.size(); b++) {
        this.reporter.reportStatus("searching child ranges of " + paramString + " in " + (String)paramVector.elementAt(b));
        try {
          blocklistReader.loadFile(paramVector.elementAt(b));
        } catch (Exception exception) {
          this.reporter.reportException(exception);
        } 
      } 
      vector.addAll(blocklistReader.getData());
    } 
    Collections.sort(vector);
    if (this.blockFiles.size() > 0)
      updateBlocked(vector); 
    return vector;
  }
  
  public Vector<IpRangeDescr> findArinChildren(String paramString) {
    this.arin = new ResultFetcher(this.reporter, false);
    return this.arin.searchSingle(paramString, (IRDFilter)new SecondaryMatchAll());
  }
  
  public Vector<IpRangeDescr> findMatching(Vector<String> paramVector1, String paramString, Vector<String> paramVector2, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3) throws FileNotFoundException, IOException, EmptyWBLException {
    BasicFilter basicFilter;
    Vector<IpRangeDescr> vector = new Vector();
    Configuration configuration = Configuration.getInstance();
    if (paramVector2.size() == 0) {
      NotFiltered notFiltered = new NotFiltered();
      paramInt1 = 0;
    } else if (paramBoolean) {
      float f = configuration.getPropInt("fuzzy_threshold", "5") / 10.0F;
      boolean bool = configuration.getPropBool("fuzzy_substring", "false");
      FuzzyFilter fuzzyFilter = new FuzzyFilter(paramVector2, f, bool);
    } else {
      basicFilter = new BasicFilter(paramVector2);
    } 
    IRDSizeFilter iRDSizeFilter = new IRDSizeFilter(paramInt2, paramInt3);
    for (byte b = 0; b < paramVector1.size(); b++) {
      this.reporter.reportStatus("searching in " + (String)paramVector1.elementAt(b));
      Vector<IpRangeDescr> vector1 = WBLReader.loadFiltered(paramVector1.elementAt(b), (WBLLoaderFilter)basicFilter, (IRDFilter)iRDSizeFilter);
      vector.addAll(vector1);
      if (WBLReader.abortSearch == true)
        break; 
    } 
    if (paramString != null) {
      this.reporter.reportStatus("searching in " + paramString);
      BlocklistReader blocklistReader = new BlocklistReader((TConsumer)new IRDAccum((WBLLoaderFilter)basicFilter, (IRDFilter)iRDSizeFilter));
      try {
        blocklistReader.loadFile(paramString);
      } catch (Exception exception) {
        this.reporter.reportException(exception);
      } 
      vector.addAll(blocklistReader.getData());
    } 
    if ((paramInt1 & 0x1) == 1 && !WBLReader.abortSearch) {
      boolean bool = configuration.getPropBool("arin_use_asns", "true");
      this.arin = new ResultFetcher(this.reporter, bool);
      vector.addAll(this.arin.search(paramVector2, (IRDFilter)iRDSizeFilter));
    } 
    if ((paramInt1 & 0x2) == 2 && !WBLReader.abortSearch) {
      this.ripe = new SimpleFetcher(this.reporter);
      vector.addAll(this.ripe.search(paramVector2, (IRDFilter)iRDSizeFilter));
    } 
    Collections.sort(vector);
    if (paramInt1 > 0 || paramVector1.size() > 1)
      vector = cleanSorted(vector); 
    if (this.blockFiles.size() > 0)
      updateBlocked(vector); 
    return vector;
  }
  
  public void abort() {
    WBLReader.abortSearch = true;
    if (this.arin != null)
      this.arin.abort(); 
    if (this.ripe != null)
      this.ripe.abort(); 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\wbl\WBLHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */