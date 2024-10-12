package org.tbg.blocklist;

import java.util.HashSet;
import java.util.Vector;
import org.tbg.ip.IpRangeDescr;

public class ListDiff {
  private static final byte IN_LEFT = 3;
  
  private static final byte IN_RIGHT = 4;
  
  public Vector<IpRangeDescr> process(Vector<IpRangeDescr> paramVector1, Vector<IpRangeDescr> paramVector2) {
    Vector<IpRangeDescr> vector = new Vector();
    HashSet<IpRangeDescr> hashSet1 = new HashSet<>(paramVector1);
    HashSet<IpRangeDescr> hashSet2 = new HashSet<>(paramVector2);
    HashSet<IpRangeDescr> hashSet3 = new HashSet<>(paramVector1);
    hashSet3.removeAll(hashSet2);
    for (IpRangeDescr ipRangeDescr : hashSet3) {
      ipRangeDescr.setBlockState((byte)3);
      vector.add(ipRangeDescr);
    } 
    hashSet2.removeAll(hashSet1);
    for (IpRangeDescr ipRangeDescr : hashSet2) {
      ipRangeDescr.setBlockState((byte)4);
      vector.add(ipRangeDescr);
    } 
    return vector;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\blocklist\ListDiff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */