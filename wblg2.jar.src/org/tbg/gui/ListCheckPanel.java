package org.tbg.gui;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.jdesktop.layout.GroupLayout;

public class ListCheckPanel extends JPanel {
  private JButton clearButton;
  
  private JScrollPane jScrollPane1;
  
  private JTextArea listCheckArea;
  
  private JButton listCheckButton;
  
  private JButton loadButton;
  
  private JButton saveButton;
  
  public ListCheckPanel(ActionListener paramActionListener) {
    initComponents();
    AbstractAction abstractAction1 = new AbstractAction("Delete") {
        public void actionPerformed(ActionEvent param1ActionEvent) {
          if (ListCheckPanel.this.listCheckArea.getSelectedText() != null)
            ListCheckPanel.this.listCheckArea.replaceSelection(""); 
        }
      };
    this.listCheckArea.getActionMap().put("Delete", abstractAction1);
    AbstractAction abstractAction2 = new AbstractAction("ClearAll") {
        public void actionPerformed(ActionEvent param1ActionEvent) {
          ListCheckPanel.this.listCheckArea.setText("");
        }
      };
    this.listCheckArea.getActionMap().put("Delete", abstractAction2);
    this.listCheckButton.addActionListener(paramActionListener);
    this.listCheckButton.setActionCommand("check_list");
    this.clearButton.addActionListener(abstractAction2);
    this.clearButton.setActionCommand("Delete");
    this.loadButton.addActionListener(paramActionListener);
    this.loadButton.setActionCommand("load_listcheck");
    this.saveButton.addActionListener(paramActionListener);
    this.saveButton.setActionCommand("save_listcheck");
  }
  
  private void initComponents() {
    this.jScrollPane1 = new JScrollPane();
    this.listCheckArea = new JTextArea();
    this.listCheckButton = new JButton();
    this.loadButton = new JButton();
    this.saveButton = new JButton();
    this.clearButton = new JButton();
    this.listCheckArea.setColumns(20);
    this.listCheckArea.setRows(5);
    this.jScrollPane1.setViewportView(this.listCheckArea);
    this.listCheckButton.setText("check against blocklist(s)");
    this.loadButton.setIcon(new ImageIcon(getClass().getResource("/fileopen.png")));
    this.loadButton.setToolTipText("load list of ranges to be checked");
    this.saveButton.setIcon(new ImageIcon(getClass().getResource("/saveas.png")));
    this.saveButton.setToolTipText("save ranges to textfile");
    this.clearButton.setIcon(new ImageIcon(getClass().getResource("/clear.png")));
    this.clearButton.setToolTipText("clear textbox");
    GroupLayout groupLayout = new GroupLayout(this);
    setLayout((LayoutManager)groupLayout);
    groupLayout.setHorizontalGroup((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createSequentialGroup().addContainerGap().add(this.listCheckButton).add(16, 16, 16).add(this.loadButton, -2, 40, -2).addPreferredGap(0).add(this.saveButton, -2, 40, -2).addPreferredGap(0).add(this.clearButton, -2, 40, -2).addContainerGap(206, 32767)).add(this.jScrollPane1, -1, 558, 32767));
    groupLayout.setVerticalGroup((GroupLayout.Group)groupLayout.createParallelGroup(1).add(2, (GroupLayout.Group)groupLayout.createSequentialGroup().add(this.jScrollPane1, -1, 280, 32767).addPreferredGap(0).add((GroupLayout.Group)groupLayout.createParallelGroup(3).add(this.listCheckButton).add(this.loadButton, -2, 32, -2).add(this.saveButton, -2, 32, -2).add(this.clearButton, -2, 32, -2)).addContainerGap()));
  }
  
  public Vector<String> getLines() {
    StringTokenizer stringTokenizer;
    Vector<String> vector = new Vector();
    String str = this.listCheckArea.getText();
    if (str.indexOf("\r\n") > -1) {
      stringTokenizer = new StringTokenizer(str, "\r\n");
    } else {
      stringTokenizer = new StringTokenizer(str, "\n");
    } 
    while (stringTokenizer.hasMoreTokens()) {
      String str1 = stringTokenizer.nextToken();
      if (str1.length() > 0)
        vector.add(new String(str1)); 
    } 
    return vector;
  }
  
  public JTextArea getArea() {
    return this.listCheckArea;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\ListCheckPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */