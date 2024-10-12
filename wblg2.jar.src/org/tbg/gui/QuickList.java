package org.tbg.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.tbg.FilesTableModel;

public class QuickList extends JScrollPane implements ActionListener {
  private JTable slavedTable;
  
  public QuickList(String paramString, JTable paramJTable) {
    FilesTableModel filesTableModel = (FilesTableModel)paramJTable.getModel();
    this.slavedTable = new JTable((TableModel)filesTableModel.mClone());
    commonInit(paramString);
  }
  
  public QuickList(String paramString) {
    FilesTableModel filesTableModel = new FilesTableModel(paramString);
    filesTableModel.setCol2Name("database");
    this.slavedTable = new JTable((TableModel)filesTableModel);
    commonInit("online search");
  }
  
  private void commonInit(String paramString) {
    JPanel jPanel1 = new JPanel(new BorderLayout());
    jPanel1.add(this.slavedTable, "Center");
    JPanel jPanel2 = new JPanel();
    jPanel1.add(jPanel2, "South");
    JButton jButton = new JButton("all on");
    jButton.setToolTipText("turn all entries on");
    jButton.setActionCommand("on");
    jButton.addActionListener(this);
    jPanel2.add(jButton);
    jButton = new JButton("all off");
    jButton.setToolTipText("turn all entries off");
    jButton.setActionCommand("off");
    jButton.addActionListener(this);
    jPanel2.add(jButton);
    setViewportView(jPanel1);
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), paramString));
    JTableHeader jTableHeader = this.slavedTable.getTableHeader();
    jTableHeader.setReorderingAllowed(false);
    TableColumn tableColumn = this.slavedTable.getColumnModel().getColumn(0);
    tableColumn.setPreferredWidth(60);
    tableColumn.setMaxWidth(60);
    setPreferredSize(new Dimension(200, 100));
    tableColumn = this.slavedTable.getColumnModel().getColumn(1);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    String str = paramActionEvent.getActionCommand();
    if (str.equals("off")) {
      turnAllOn(false);
    } else if (str.equals("on")) {
      turnAllOn(true);
    } 
  }
  
  private void turnAllOn(boolean paramBoolean) {
    FilesTableModel filesTableModel = (FilesTableModel)this.slavedTable.getModel();
    int i = filesTableModel.getRowCount();
    for (byte b = 0; b < i; b++)
      filesTableModel.setValueAt(Boolean.valueOf(paramBoolean), b, 0); 
    filesTableModel.fire(false);
  }
  
  public FilesTableModel getModel() {
    return (FilesTableModel)this.slavedTable.getModel();
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\QuickList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */