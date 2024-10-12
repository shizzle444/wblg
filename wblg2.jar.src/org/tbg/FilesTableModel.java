package org.tbg;

import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class FilesTableModel extends AbstractTableModel {
  private static String sep = System.getProperty("file.separator");
  
  private String[] colNames = new String[] { "active", "filename" };
  
  private boolean shortNames = false;
  
  private Vector<FileActiveRec> data = new Vector<>();
  
  private FilesTableModel master = null;
  
  private FilesTableModel slave = null;
  
  public FilesTableModel() {}
  
  public void setCol2Name(String paramString) {
    this.colNames[1] = new String(paramString);
  }
  
  public FilesTableModel(String paramString) {
    StringTokenizer stringTokenizer = new StringTokenizer(paramString, ",");
    while (stringTokenizer.hasMoreTokens()) {
      StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), "=");
      this.data.add(new FileActiveRec(stringTokenizer1.nextToken(), Boolean.parseBoolean(stringTokenizer1.nextToken())));
    } 
  }
  
  public FilesTableModel mClone() {
    if (this.slave == null)
      this.slave = new FilesTableModel(this); 
    return this.slave;
  }
  
  private FilesTableModel(FilesTableModel paramFilesTableModel) {}
  
  public void fire(boolean paramBoolean) {
    fireTableDataChanged();
    if (paramBoolean) {
      if (this.slave != null)
        this.slave.fire(false); 
      if (this.master != null)
        this.master.fire(false); 
    } 
  }
  
  private Vector<FileActiveRec> getData() {
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
    FileActiveRec fileActiveRec = this.data.elementAt(paramInt1);
    return (paramInt2 == 0) ? Boolean.valueOf(fileActiveRec.active) : (this.shortNames ? fileActiveRec.getShortName() : fileActiveRec.filename);
  }
  
  public Vector<String> getActiveList() {
    Vector<String> vector = new Vector();
    for (byte b = 0; b < this.data.size(); b++) {
      FileActiveRec fileActiveRec = this.data.elementAt(b);
      if (fileActiveRec.active == true)
        vector.add(new String(fileActiveRec.filename)); 
    } 
    return vector;
  }
  
  public void append(String paramString) {
    this.data.add(new FileActiveRec(paramString));
    fire(true);
  }
  
  public void remove(int paramInt) {
    if (paramInt < 0 || paramInt >= this.data.size())
      return; 
    this.data.remove(paramInt);
    fire(true);
  }
  
  public Class getColumnClass(int paramInt) {
    return getValueAt(0, paramInt).getClass();
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return (paramInt2 == 0);
  }
  
  public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
    FileActiveRec fileActiveRec = this.data.elementAt(paramInt1);
    Boolean bool = (Boolean)paramObject;
    if (paramInt2 == 0)
      fileActiveRec.active = bool.booleanValue(); 
  }
  
  public int moveUp(int paramInt) {
    if (paramInt == 0)
      return paramInt; 
    Collections.swap(this.data, paramInt - 1, paramInt);
    fire(true);
    return paramInt - 1;
  }
  
  public int moveDown(int paramInt) {
    if (paramInt == this.data.size() - 1)
      return paramInt; 
    Collections.swap(this.data, paramInt, paramInt + 1);
    fire(true);
    return paramInt + 1;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < this.data.size(); b++) {
      FileActiveRec fileActiveRec = this.data.elementAt(b);
      stringBuilder.append(fileActiveRec.filename);
      stringBuilder.append("=");
      stringBuilder.append(fileActiveRec.active);
      if (b < this.data.size() - 1)
        stringBuilder.append(","); 
    } 
    return stringBuilder.toString();
  }
  
  class FileActiveRec {
    public boolean active = true;
    
    public String filename;
    
    public FileActiveRec(String param1String) {
      this.filename = new String(param1String);
    }
    
    public FileActiveRec(String param1String, boolean param1Boolean) {
      this.filename = param1String;
    }
    
    public String getShortName() {
      String str;
      if (this.filename.indexOf(FilesTableModel.sep) > -1) {
        str = this.filename.substring(this.filename.lastIndexOf(FilesTableModel.sep) + 1);
      } else {
        str = this.filename;
      } 
      if (str.indexOf('.') > -1)
        str = str.substring(0, str.indexOf('.')); 
      return str;
    }
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\FilesTableModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */