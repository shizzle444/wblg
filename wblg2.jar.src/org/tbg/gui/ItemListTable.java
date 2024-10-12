package org.tbg.gui;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.jdesktop.layout.GroupLayout;
import org.tbg.ItemTableModel;

public class ItemListTable extends JPanel implements ActionListener {
  private String input;
  
  private String inputDefault;
  
  private JButton addButton;
  
  private JScrollPane jScrollPane1;
  
  private JTable jTable1;
  
  private JLabel listTitle;
  
  private JButton removeButton;
  
  private JButton setDefaultButton;
  
  public ItemListTable(String paramString1, String paramString2, String paramString3) {
    this.input = paramString2;
    this.inputDefault = paramString3;
    initComponents();
    this.jTable1.setSelectionMode(0);
    JTableHeader jTableHeader = this.jTable1.getTableHeader();
    jTableHeader.setReorderingAllowed(false);
    TableColumn tableColumn = this.jTable1.getColumnModel().getColumn(0);
    tableColumn.setPreferredWidth(60);
    tableColumn.setMaxWidth(60);
    this.listTitle.setText(paramString1);
    this.addButton.addActionListener(this);
    this.removeButton.addActionListener(this);
    this.setDefaultButton.addActionListener(this);
  }
  
  public ItemTableModel getDataModel() {
    return (ItemTableModel)this.jTable1.getModel();
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    String str = paramActionEvent.getActionCommand();
    if (str.equals("ADD")) {
      addEntry();
    } else if (str.equals("REMOVE")) {
      removeEntry();
    } else if (str.equals("set_default")) {
      int i = JOptionPane.showConfirmDialog(this.setDefaultButton, "Reset to default value", "Do you really want to do this?", 0);
      if (i == 0) {
        ItemTableModel itemTableModel = (ItemTableModel)this.jTable1.getModel();
        itemTableModel.parse(this.inputDefault);
      } 
    } 
  }
  
  private void removeEntry() {
    ItemTableModel itemTableModel = (ItemTableModel)this.jTable1.getModel();
    itemTableModel.remove(this.jTable1.getSelectedRow());
  }
  
  private void addEntry() {
    String str = JOptionPane.showInputDialog(this, "Insert a field name to be exported from the DB; for example 'tech-c' or 'owner'", "Insert a field name", 3);
    if (str != null && str.length() > 0) {
      ItemTableModel itemTableModel = (ItemTableModel)this.jTable1.getModel();
      itemTableModel.append(str);
    } 
  }
  
  private void initComponents() {
    this.listTitle = new JLabel();
    this.jScrollPane1 = new JScrollPane();
    this.jTable1 = new JTable();
    this.addButton = new JButton();
    this.removeButton = new JButton();
    this.setDefaultButton = new JButton();
    this.listTitle.setText("jLabel1");
    this.jTable1.setModel((TableModel)new ItemTableModel(this.input, this.inputDefault));
    this.jScrollPane1.setViewportView(this.jTable1);
    this.addButton.setIcon(new ImageIcon(getClass().getResource("/add.png")));
    this.addButton.setToolTipText("click to add a new entry");
    this.addButton.setActionCommand("ADD");
    this.removeButton.setIcon(new ImageIcon(getClass().getResource("/remove.png")));
    this.removeButton.setToolTipText("click to remove selected entry");
    this.removeButton.setActionCommand("REMOVE");
    this.setDefaultButton.setText("set default");
    this.setDefaultButton.setActionCommand("set_default");
    GroupLayout groupLayout = new GroupLayout(this);
    setLayout((LayoutManager)groupLayout);
    groupLayout.setHorizontalGroup((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createSequentialGroup().addContainerGap().add(this.listTitle).addContainerGap(324, 32767)).add(this.jScrollPane1, -1, 381, 32767).add((GroupLayout.Group)groupLayout.createSequentialGroup().addContainerGap().add(this.addButton, -2, 40, -2).addPreferredGap(0).add(this.removeButton, -2, 40, -2).addPreferredGap(0).add(this.setDefaultButton).addContainerGap(176, 32767)));
    groupLayout.setVerticalGroup((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createSequentialGroup().add(this.listTitle).addPreferredGap(0).add(this.jScrollPane1, -2, 275, -2).addPreferredGap(0).add((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createParallelGroup(3).add(this.addButton, -2, 32, -2).add(this.removeButton, -2, 32, -2)).add(this.setDefaultButton, -1, 32, 32767)).addContainerGap()));
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\ItemListTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */