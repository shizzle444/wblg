package org.tbg.gui;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.jdesktop.layout.GroupLayout;
import org.tbg.FilesTableModel;
import org.tbg.util.Configuration;

public class FileListPanel extends JPanel implements ActionListener {
  private Configuration config;
  
  private String nameInConfig;
  
  private FileFilter fileFilter;
  
  private String dataDir;
  
  private JButton addButton;
  
  private JTable dataTable;
  
  private JButton downButton;
  
  private JPanel jPanel1;
  
  private JScrollPane jScrollPane1;
  
  private JButton removeButton;
  
  private JButton upButton;
  
  public FileListPanel(String paramString1, String paramString2, FileFilter paramFileFilter) {
    this.nameInConfig = paramString1;
    this.dataDir = paramString2;
    this.fileFilter = paramFileFilter;
    this.config = Configuration.getInstance();
    initComponents();
    this.dataTable.setSelectionMode(0);
    JTableHeader jTableHeader = this.dataTable.getTableHeader();
    jTableHeader.setReorderingAllowed(false);
    TableColumn tableColumn = this.dataTable.getColumnModel().getColumn(0);
    tableColumn.setPreferredWidth(60);
    tableColumn.setMaxWidth(60);
    this.addButton.addActionListener(this);
    this.removeButton.addActionListener(this);
    this.upButton.addActionListener(this);
    this.downButton.addActionListener(this);
  }
  
  private void initComponents() {
    this.jScrollPane1 = new JScrollPane();
    this.dataTable = new JTable();
    this.jPanel1 = new JPanel();
    this.addButton = new JButton();
    this.removeButton = new JButton();
    this.upButton = new JButton();
    this.downButton = new JButton();
    this.dataTable.setModel((TableModel)new FilesTableModel(this.config.getProp(this.nameInConfig, "")));
    this.jScrollPane1.setViewportView(this.dataTable);
    this.addButton.setIcon(new ImageIcon(getClass().getResource("/add.png")));
    this.addButton.setToolTipText("add new WBL database");
    this.addButton.setActionCommand("ADD");
    this.removeButton.setIcon(new ImageIcon(getClass().getResource("/remove.png")));
    this.removeButton.setToolTipText("remove selected entry");
    this.removeButton.setActionCommand("REMOVE");
    this.upButton.setIcon(new ImageIcon(getClass().getResource("/up.png")));
    this.upButton.setToolTipText("move selected entry up");
    this.upButton.setActionCommand("UP");
    this.downButton.setIcon(new ImageIcon(getClass().getResource("/down.png")));
    this.downButton.setToolTipText("move selected entry down");
    this.downButton.setActionCommand("DOWN");
    GroupLayout groupLayout1 = new GroupLayout(this.jPanel1);
    this.jPanel1.setLayout((LayoutManager)groupLayout1);
    groupLayout1.setHorizontalGroup((GroupLayout.Group)groupLayout1.createParallelGroup(1).add((GroupLayout.Group)groupLayout1.createSequentialGroup().add(51, 51, 51).add(this.addButton, -2, 40, -2).addPreferredGap(0).add(this.removeButton, -2, 40, -2).addPreferredGap(0, 46, 32767).add(this.upButton, -2, 40, -2).addPreferredGap(0).add(this.downButton, -2, 40, -2).addContainerGap()));
    groupLayout1.setVerticalGroup((GroupLayout.Group)groupLayout1.createParallelGroup(1).add(2, (GroupLayout.Group)groupLayout1.createSequentialGroup().addContainerGap(-1, 32767).add((GroupLayout.Group)groupLayout1.createParallelGroup(3).add(this.downButton, -2, 32, -2).add(this.upButton, -2, 32, -2).add(this.removeButton, -2, 32, -2).add(this.addButton, -2, 32, -2)).addContainerGap()));
    GroupLayout groupLayout2 = new GroupLayout(this);
    setLayout((LayoutManager)groupLayout2);
    groupLayout2.setHorizontalGroup((GroupLayout.Group)groupLayout2.createParallelGroup(1).add(this.jPanel1, -2, -1, -2).add(this.jScrollPane1, -1, 419, 32767));
    groupLayout2.setVerticalGroup((GroupLayout.Group)groupLayout2.createParallelGroup(1).add(2, (GroupLayout.Group)groupLayout2.createSequentialGroup().add(this.jScrollPane1, -1, 165, 32767).addPreferredGap(0).add(this.jPanel1, -2, -1, -2)));
  }
  
  public JTable getDataTable() {
    return this.dataTable;
  }
  
  private void moveTableEntry(boolean paramBoolean) {
    FilesTableModel filesTableModel = (FilesTableModel)this.dataTable.getModel();
    int i = this.dataTable.getSelectedRow();
    if (paramBoolean) {
      i = filesTableModel.moveUp(i);
    } else {
      i = filesTableModel.moveDown(i);
    } 
    this.dataTable.changeSelection(i, 0, false, false);
  }
  
  private void removeEntry() {
    FilesTableModel filesTableModel = (FilesTableModel)this.dataTable.getModel();
    filesTableModel.remove(this.dataTable.getSelectedRow());
  }
  
  private void addEntry() {
    JFileChooser jFileChooser = new JFileChooser(this.config.getProp(this.dataDir, System.getProperty("user.dir")));
    jFileChooser.addChoosableFileFilter(this.fileFilter);
    if (jFileChooser.showDialog(this, "Add") == 0) {
      this.config.setProperty(this.dataDir, jFileChooser.getSelectedFile().getParent());
      FilesTableModel filesTableModel = (FilesTableModel)this.dataTable.getModel();
      filesTableModel.append(jFileChooser.getSelectedFile().getPath());
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    String str = paramActionEvent.getActionCommand();
    if (str.equals("ADD")) {
      addEntry();
    } else if (str.equals("REMOVE")) {
      removeEntry();
    } else if (str.equals("UP")) {
      moveTableEntry(true);
    } else if (str.equals("DOWN")) {
      moveTableEntry(false);
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\FileListPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */