package org.tbg;

import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.tbg.util.Configuration;

public class ItemTableModel extends AbstractTableModel {
  private String[] colNames = new String[] { "active", "field" };
  
  private Vector<ItemRec> data = new Vector<>();
  
  private Configuration config = Configuration.getInstance();
  
  private String input;
  
  public ItemTableModel(String paramString1, String paramString2) {
    this.input = paramString1;
    String str = this.config.getProp(paramString1, paramString2);
    parse(str);
  }
  
  public void parse(String paramString) {
    StringTokenizer stringTokenizer = new StringTokenizer(paramString, ",");
    this.data.clear();
    while (stringTokenizer.hasMoreTokens()) {
      StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), "=");
      this.data.add(new ItemRec(stringTokenizer1.nextToken(), Boolean.parseBoolean(stringTokenizer1.nextToken())));
    } 
    fireTableDataChanged();
    this.config.setProperty(this.input, toString());
  }
  
  public Object getValueAt(int paramInt1, int paramInt2) {
    ItemRec itemRec = this.data.elementAt(paramInt1);
    return (paramInt2 == 0) ? Boolean.valueOf(itemRec.active) : itemRec.field;
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
  
  public Class getColumnClass(int paramInt) {
    return getValueAt(0, paramInt).getClass();
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return (paramInt2 == 0);
  }
  
  public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
    ItemRec itemRec = this.data.elementAt(paramInt1);
    Boolean bool = (Boolean)paramObject;
    if (paramInt2 == 0)
      itemRec.active = bool.booleanValue(); 
    this.config.setProperty(this.input, toString());
  }
  
  public Vector<String> getActiveList() {
    Vector<String> vector = new Vector();
    for (byte b = 0; b < this.data.size(); b++) {
      ItemRec itemRec = this.data.elementAt(b);
      if (itemRec.active == true)
        vector.add(new String(itemRec.field)); 
    } 
    return vector;
  }
  
  public int moveUp(int paramInt) {
    if (paramInt == 0)
      return paramInt; 
    Collections.swap(this.data, paramInt - 1, paramInt);
    fireTableDataChanged();
    return paramInt - 1;
  }
  
  public int moveDown(int paramInt) {
    if (paramInt == this.data.size() - 1)
      return paramInt; 
    Collections.swap(this.data, paramInt, paramInt + 1);
    fireTableDataChanged();
    return paramInt + 1;
  }
  
  public void append(String paramString) {
    this.data.add(new ItemRec(paramString, true));
    this.config.setProperty(this.input, toString());
    fireTableDataChanged();
  }
  
  public void remove(int paramInt) {
    if (paramInt < 0 || paramInt >= this.data.size())
      return; 
    this.data.remove(paramInt);
    this.config.setProperty(this.input, toString());
    fireTableDataChanged();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < this.data.size(); b++) {
      ItemRec itemRec = this.data.elementAt(b);
      stringBuilder.append(itemRec.field);
      stringBuilder.append("=");
      stringBuilder.append(itemRec.active);
      if (b < this.data.size() - 1)
        stringBuilder.append(","); 
    } 
    return stringBuilder.toString();
  }
  
  class ItemRec {
    public boolean active;
    
    public String field;
    
    public ItemRec(String param1String, boolean param1Boolean) {
      this.field = param1String;
      this.active = param1Boolean;
    }
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ItemTableModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */