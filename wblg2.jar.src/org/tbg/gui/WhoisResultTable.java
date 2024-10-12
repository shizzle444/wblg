package org.tbg.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.tbg.blocklist.BLTableModel;
import org.tbg.filter.ird.IRDFilter;
import org.tbg.ip.IpRangeDescr;
import org.tbg.ip.IpRangeInterface;
import org.tbg.rpsl.IRD_Consumer;
import org.tbg.util.Configuration;
import org.tbg.util.IRD_Visitor;

public class WhoisResultTable extends JTable {
  private int sortByCol;
  
  private static int blockedSecondarySort = 3;
  
  private int resultSelectedIndex;
  
  private static BlockedRenderer blockedRenderer = new BlockedRenderer();
  
  JPopupMenu popup;
  
  public WhoisResultTable() {
    super((TableModel)new BLTableModel());
    setColumnSelectionAllowed(false);
    addMouseListener(new PopupHelper());
    JTableHeader jTableHeader = getTableHeader();
    jTableHeader.setReorderingAllowed(false);
    jTableHeader.addMouseMotionListener(new ToolTipHelper());
    jTableHeader.addMouseListener(new SortHelper());
    TableColumn tableColumn = getColumnModel().getColumn(0);
    tableColumn.setPreferredWidth(10);
    tableColumn.setMaxWidth(10);
    tableColumn = getColumnModel().getColumn(1);
    tableColumn.setPreferredWidth(250);
    tableColumn.setMaxWidth(250);
    tableColumn = getColumnModel().getColumn(2);
    tableColumn.setPreferredWidth(10);
    tableColumn.setMaxWidth(10);
    this.sortByCol = 1;
    setAutoResizeMode(0);
    setAutoscrolls(false);
    ((DefaultTableCellRenderer)getTableHeader().getDefaultRenderer()).setHorizontalAlignment(2);
  }
  
  protected void processKeyEvent(KeyEvent paramKeyEvent) {
    int i = paramKeyEvent.getKeyCode();
    if (i == 33 || i == 34) {
      super.processKeyEvent(paramKeyEvent);
      scrollToResult(getSelectedRow());
    } else if (i == 37 || i == 39 || i == 38 || i == 40) {
      setAutoscrolls(true);
      super.processKeyEvent(paramKeyEvent);
      setAutoscrolls(false);
    } else {
      super.processKeyEvent(paramKeyEvent);
    } 
  }
  
  public void resizeToFit() {
    TableColumnModel tableColumnModel = getColumnModel();
    TableModel tableModel = getModel();
    int i = tableColumnModel.getColumnMargin() * 2;
    int j = getParent().getWidth() - 270 + i * 3;
    int k = tableModel.getRowCount();
    TableColumn tableColumn = tableColumnModel.getColumn(3);
    int m = tableColumn.getModelIndex();
    for (byte b = 0; b < k; b++) {
      TableCellRenderer tableCellRenderer = getCellRenderer(b, 3);
      int n = (tableCellRenderer.getTableCellRendererComponent(this, tableModel.getValueAt(b, m), false, false, b, 3).getPreferredSize()).width;
      if (n > j)
        j = n; 
    } 
    tableColumn.setPreferredWidth(j + i);
  }
  
  public TableCellRenderer getCellRenderer(int paramInt1, int paramInt2) {
    return (paramInt2 == 2) ? blockedRenderer : super.getCellRenderer(paramInt1, paramInt2);
  }
  
  public Component prepareRenderer(TableCellRenderer paramTableCellRenderer, int paramInt1, int paramInt2) {
    Component component = super.prepareRenderer(paramTableCellRenderer, paramInt1, paramInt2);
    BLTableModel bLTableModel = (BLTableModel)getModel();
    IpRangeDescr ipRangeDescr = bLTableModel.getElementAt(paramInt1);
    if (paramInt2 != 2 && !isCellSelected(paramInt1, paramInt2))
      if (ipRangeDescr.isHighlighted() == 1) {
        component.setBackground(Color.yellow);
      } else if (ipRangeDescr.isHighlighted() == 2) {
        component.setBackground(Color.orange);
      } else {
        component.setBackground(getBackground());
      }  
    if (paramInt2 == 2 && component instanceof JComponent) {
      JComponent jComponent = (JComponent)component;
      jComponent.setToolTipText(ipRangeDescr.toToolTip());
    } 
    return component;
  }
  
  private void setResults(Vector<IpRangeDescr> paramVector) {
    BLTableModel bLTableModel = (BLTableModel)getModel();
    bLTableModel.setData(paramVector);
    resizeToFit();
  }
  
  public void setResults(Vector<IpRangeDescr> paramVector, int paramInt) {
    setResults(paramVector);
    this.sortByCol = paramInt;
  }
  
  public Vector<IpRangeDescr> getResults() {
    BLTableModel bLTableModel = (BLTableModel)getModel();
    return bLTableModel.getData();
  }
  
  public void scrollToResult(int paramInt) {
    scrollRectToVisible(getCellRect(paramInt, 0, true));
  }
  
  public void resetSubSearch(byte paramByte) {
    BLTableModel bLTableModel = (BLTableModel)getModel();
    int i = bLTableModel.getRowCount();
    for (byte b = 0; b < i; b++) {
      IpRangeDescr ipRangeDescr = bLTableModel.getElementAt(b);
      byte b1 = ipRangeDescr.isHighlighted();
      if (b1 > paramByte)
        ipRangeDescr.setHighlighted(paramByte); 
    } 
    setResults(resort(bLTableModel.getData(), this.sortByCol), this.sortByCol);
  }
  
  public void searchInResults(String paramString, int paramInt) {
    if (paramString.length() == 0) {
      resetSubSearch((byte)0);
      return;
    } 
    BLTableModel bLTableModel = (BLTableModel)getModel();
    Vector vector = bLTableModel.getData();
    String str = paramString.toLowerCase();
    int i = bLTableModel.getRowCount();
    for (byte b = 0; b < i; b++) {
      IpRangeDescr ipRangeDescr = bLTableModel.getElementAt(b);
      byte b1 = ipRangeDescr.isHighlighted();
      if (b1 == paramInt) {
        String str1 = ipRangeDescr.getNetName().toLowerCase();
        if (str1.indexOf(str) > -1) {
          ipRangeDescr.setHighlighted(b1 + 1);
        } else {
          str1 = ipRangeDescr.getDescription().toLowerCase();
          if (str1.indexOf(str) > -1)
            ipRangeDescr.setHighlighted(b1 + 1); 
        } 
      } 
    } 
    setResults(resort(bLTableModel.getData(), this.sortByCol), this.sortByCol);
  }
  
  public void selectMarkedResults() {
    BLTableModel bLTableModel = (BLTableModel)getModel();
    Vector<IpRangeDescr> vector = bLTableModel.getData();
    int i = vector.size() - 1;
    int j = i + 1;
    for (int k = i; k >= 0; k--) {
      IpRangeDescr ipRangeDescr = vector.elementAt(k);
      if (!ipRangeDescr.getSelected()) {
        j = k + 1;
        break;
      } 
    } 
    if (j < i + 1) {
      clearSelection();
      addRowSelectionInterval(j, i);
      scrollToResult(j);
    } 
  }
  
  public void selectEntries(IRDFilter paramIRDFilter) {
    BLTableModel bLTableModel = (BLTableModel)getModel();
    clearSelection();
    Vector<IpRangeDescr> vector = bLTableModel.getData();
    int i = vector.size();
    for (byte b = 0; b < i; b++) {
      IpRangeDescr ipRangeDescr = vector.elementAt(b);
      if (paramIRDFilter.match(ipRangeDescr))
        addRowSelectionInterval(b, b); 
    } 
  }
  
  public void walkSelectedEntries(IRD_Consumer paramIRD_Consumer) {
    BLTableModel bLTableModel = (BLTableModel)getModel();
    int[] arrayOfInt = getSelectedRows();
    StatusAndProgress statusAndProgress = (WBLGPanel.getInstance()).sap;
    for (byte b = 0; b < arrayOfInt.length; b++) {
      paramIRD_Consumer.consume(bLTableModel.getElementAt(arrayOfInt[b]));
      statusAndProgress.reportProgress((b + 1) * 100 / arrayOfInt.length);
    } 
    bLTableModel.fire();
    resizeToFit();
    for (int i : arrayOfInt)
      addRowSelectionInterval(i, i); 
  }
  
  public void deleteSelectedEntries() {
    BLTableModel bLTableModel = (BLTableModel)getModel();
    int[] arrayOfInt = getSelectedRows();
    Vector<IpRangeDescr> vector = new Vector();
    for (byte b = 0; b < arrayOfInt.length; b++)
      vector.add(bLTableModel.getElementAt(arrayOfInt[b])); 
    bLTableModel.removeElements(vector);
    (WBLGPanel.getInstance()).sap.reportStatus(bLTableModel.getRowCount() + " entries remaining");
  }
  
  public void visitSelectedEntries(IRD_Visitor paramIRD_Visitor) {
    BLTableModel bLTableModel = (BLTableModel)getModel();
    int[] arrayOfInt = getSelectedRows();
    for (byte b = 0; b < arrayOfInt.length; b++)
      paramIRD_Visitor.visit(bLTableModel.getElementAt(arrayOfInt[b])); 
    setResults(resort(bLTableModel.getData(), this.sortByCol), this.sortByCol);
  }
  
  public void deleteEntries(IRDFilter paramIRDFilter) {
    BLTableModel bLTableModel = (BLTableModel)getModel();
    Vector vector = bLTableModel.getMatching(paramIRDFilter);
    bLTableModel.removeElements(vector);
    (WBLGPanel.getInstance()).sap.reportStatus(bLTableModel.getRowCount() + " entries remaining");
  }
  
  public void walkEntries(IRDFilter paramIRDFilter, IRD_Consumer paramIRD_Consumer) {
    BLTableModel bLTableModel = (BLTableModel)getModel();
    Vector<IpRangeDescr> vector = bLTableModel.getData();
    int i = vector.size();
    for (byte b = 0; b < i; b++) {
      IpRangeDescr ipRangeDescr = vector.elementAt(b);
      if (paramIRDFilter.match(ipRangeDescr))
        paramIRD_Consumer.consume(ipRangeDescr); 
    } 
  }
  
  public static Vector<IpRangeDescr> resort(Vector<IpRangeDescr> paramVector, int paramInt) {
    if (paramInt == 0) {
      Collections.sort(paramVector, new IRD_MComp());
    } else if (paramInt == 1) {
      Collections.sort(paramVector);
    } else if (paramInt == 2) {
      Collections.sort(paramVector, new IRD_BComp());
    } else {
      Collections.sort(paramVector, new IRD_DComp());
    } 
    return paramVector;
  }
  
  class PopupHelper extends MouseAdapter {
    public void mousePressed(MouseEvent param1MouseEvent) {
      int i = param1MouseEvent.getModifiers();
      if ((i & 0x4) != 0) {
        WhoisResultTable whoisResultTable = WhoisResultTable.this;
        int j = whoisResultTable.rowAtPoint(param1MouseEvent.getPoint());
        WhoisResultTable.this.resultSelectedIndex = j;
        boolean bool = Configuration.getInstance().getPropBool("right_click_select", "false");
        if (bool) {
          whoisResultTable.clearSelection();
          whoisResultTable.addRowSelectionInterval(j, j);
        } 
        if (WhoisResultTable.this.popup == null)
          WhoisResultTable.this.popup = WBLGPanel.getInstance().createResultsPopup(); 
        WhoisResultTable.this.popup.show(whoisResultTable, param1MouseEvent.getX(), param1MouseEvent.getY());
      } 
    }
  }
  
  class SortHelper extends MouseAdapter {
    public void mouseClicked(MouseEvent param1MouseEvent) {
      WhoisResultTable whoisResultTable = WhoisResultTable.this;
      TableColumnModel tableColumnModel = whoisResultTable.getColumnModel();
      int i = tableColumnModel.getColumnIndexAtX(param1MouseEvent.getX());
      int j = whoisResultTable.convertColumnIndexToModel(i);
      int k = param1MouseEvent.getModifiers();
      if (WhoisResultTable.this.sortByCol == 2 && (k & 0x4) == 4 && (j == 1 || j == 3)) {
        WhoisResultTable.blockedSecondarySort = j;
      } else {
        WhoisResultTable.this.sortByCol = j;
      } 
      BLTableModel bLTableModel = (BLTableModel)whoisResultTable.getModel();
      whoisResultTable.setResults(WhoisResultTable.resort(bLTableModel.getData(), WhoisResultTable.this.sortByCol));
      if (j == 0 && (k & 0x4) == 4)
        whoisResultTable.selectMarkedResults(); 
    }
  }
  
  class ToolTipHelper extends MouseMotionAdapter {
    public void mouseMoved(MouseEvent param1MouseEvent) {
      JTableHeader jTableHeader = (JTableHeader)param1MouseEvent.getSource();
      TableColumnModel tableColumnModel = jTableHeader.getTable().getColumnModel();
      int i = tableColumnModel.getColumnIndexAtX(param1MouseEvent.getX());
      int j = WhoisResultTable.this.convertColumnIndexToModel(i);
      switch (j) {
        case 0:
          jTableHeader.setToolTipText("<html>left-click: sort by 'marked' attribute<br>right-click: sort &amp; select marked</html>");
          break;
        case 1:
          jTableHeader.setToolTipText("sort by IP");
          break;
        case 2:
          jTableHeader.setToolTipText("sort by 'blocklisted' attribute");
          break;
        case 3:
          jTableHeader.setToolTipText("sort by description");
          break;
      } 
    }
  }
  
  public static class IRD_DComp implements Comparator<IpRangeDescr> {
    public int compare(IpRangeDescr param1IpRangeDescr1, IpRangeDescr param1IpRangeDescr2) {
      return param1IpRangeDescr1.compareDescription(param1IpRangeDescr2);
    }
    
    public boolean equals(Object param1Object) {
      return false;
    }
  }
  
  public static class IRD_NComp implements Comparator<IpRangeDescr> {
    public int compare(IpRangeDescr param1IpRangeDescr1, IpRangeDescr param1IpRangeDescr2) {
      int i = param1IpRangeDescr1.compareHighlighted(param1IpRangeDescr2);
      if (i != 0)
        return i; 
      String str1 = param1IpRangeDescr1.getNetName();
      String str2 = param1IpRangeDescr2.getNetName();
      return str1.compareTo(str2);
    }
    
    public boolean equals(Object param1Object) {
      return false;
    }
  }
  
  public static class IRD_BComp implements Comparator<IpRangeDescr> {
    public boolean equals(Object param1Object) {
      return false;
    }
    
    public int compare(IpRangeDescr param1IpRangeDescr1, IpRangeDescr param1IpRangeDescr2) {
      int i = param1IpRangeDescr1.compareBlocked(param1IpRangeDescr2);
      if (i == 0)
        if (WhoisResultTable.blockedSecondarySort == 3) {
          i = param1IpRangeDescr1.compareDescription(param1IpRangeDescr2);
        } else {
          i = param1IpRangeDescr1.compareTo((IpRangeInterface)param1IpRangeDescr2);
        }  
      return i;
    }
  }
  
  public static class IRD_MComp implements Comparator<IpRangeDescr> {
    public boolean equals(Object param1Object) {
      return false;
    }
    
    public int compare(IpRangeDescr param1IpRangeDescr1, IpRangeDescr param1IpRangeDescr2) {
      int i = param1IpRangeDescr1.compareSelected(param1IpRangeDescr2);
      if (i == 0)
        i = param1IpRangeDescr1.compareDescription(param1IpRangeDescr2); 
      return i;
    }
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\WhoisResultTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */