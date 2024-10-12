package org.tbg.blocklist;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.tbg.filter.ird.IRDFilter;
import org.tbg.ip.IpRangeDescr;

public class BLTableModel extends AbstractTableModel {
  private String[] colNames = new String[] { " M", "  IP-Range", "BL", "  Description" };
  
  private Vector<IpRangeDescr> data = new Vector<>();
  
  public void fire() {
    fireTableDataChanged();
  }
  
  public void clear() {
    this.data.clear();
    fireTableDataChanged();
  }
  
  public void setData(Vector<IpRangeDescr> paramVector) {
    this.data = paramVector;
    fireTableDataChanged();
  }
  
  public Vector<IpRangeDescr> getData() {
    return this.data;
  }
  
  public int getColumnCount() {
    return this.colNames.length;
  }
  
  public int getRowCount() {
    return this.data.size();
  }
  
  public String getColumnName(int paramInt) {
    return this.colNames[paramInt];
  }
  
  public Object getValueAt(int paramInt1, int paramInt2) {
    IpRangeDescr ipRangeDescr = this.data.elementAt(paramInt1);
    return (paramInt2 == 0) ? Boolean.valueOf(ipRangeDescr.getSelected()) : ((paramInt2 == 1) ? ipRangeDescr.getSmartRangeString() : ((paramInt2 == 2) ? new Byte(ipRangeDescr.getBlockState()) : ipRangeDescr.getBestMatch()));
  }
  
  public IpRangeDescr getElementAt(int paramInt) {
    return this.data.elementAt(paramInt);
  }
  
  public Class getColumnClass(int paramInt) {
    return getValueAt(0, paramInt).getClass();
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return (paramInt2 == 0);
  }
  
  public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
    IpRangeDescr ipRangeDescr = this.data.elementAt(paramInt1);
    Boolean bool = (Boolean)paramObject;
    if (paramInt2 == 0)
      ipRangeDescr.setSelected(bool.booleanValue()); 
  }
  
  public Vector<IpRangeDescr> getMatching(IRDFilter paramIRDFilter) {
    Vector<IpRangeDescr> vector = new Vector();
    int i = this.data.size();
    for (byte b = 0; b < i; b++) {
      IpRangeDescr ipRangeDescr = this.data.elementAt(b);
      if (paramIRDFilter.match(ipRangeDescr))
        vector.add(ipRangeDescr); 
    } 
    return vector;
  }
  
  public void removeElements(Vector<IpRangeDescr> paramVector) {
    this.data.removeAll(paramVector);
    fireTableDataChanged();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\blocklist\BLTableModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */