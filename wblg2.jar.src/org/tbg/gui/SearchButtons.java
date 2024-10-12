package org.tbg.gui;

import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;
import org.jdesktop.layout.GroupLayout;

public class SearchButtons extends JPanel {
  private JButton abortButton;
  
  private JButton clearButton;
  
  private JCheckBox fuzzyCheckBox;
  
  private JLabel jLabel1;
  
  private JLabel jLabel2;
  
  private JPanel jPanel1;
  
  private JButton loadButton;
  
  private JComboBox maxRangeBox;
  
  private JComboBox minRangeBox;
  
  private JButton rangeSearchButton;
  
  private JButton saveButton;
  
  private JButton searchButton;
  
  public SearchButtons(ActionListener paramActionListener) {
    initComponents();
    this.searchButton.addActionListener(paramActionListener);
    this.loadButton.addActionListener(paramActionListener);
    this.clearButton.addActionListener(paramActionListener);
    this.saveButton.addActionListener(paramActionListener);
    this.abortButton.addActionListener(paramActionListener);
    this.rangeSearchButton.addActionListener(paramActionListener);
    this.abortButton.setEnabled(false);
    DefaultComboBoxModel<String> defaultComboBoxModel1 = (DefaultComboBoxModel)this.minRangeBox.getModel();
    DefaultComboBoxModel<String> defaultComboBoxModel2 = (DefaultComboBoxModel)this.maxRangeBox.getModel();
    for (byte b = 0; b < 33; b++) {
      defaultComboBoxModel1.addElement("/" + b);
      defaultComboBoxModel2.addElement("/" + b);
    } 
    this.maxRangeBox.setSelectedItem("/0");
    this.minRangeBox.setSelectedItem("/32");
  }
  
  private void initComponents() {
    this.searchButton = new JButton();
    this.loadButton = new JButton();
    this.fuzzyCheckBox = new JCheckBox();
    this.saveButton = new JButton();
    this.clearButton = new JButton();
    this.abortButton = new JButton();
    this.jPanel1 = new JPanel();
    this.jLabel1 = new JLabel();
    this.jLabel2 = new JLabel();
    this.minRangeBox = new JComboBox();
    this.maxRangeBox = new JComboBox();
    this.rangeSearchButton = new JButton();
    setBorder(new SoftBevelBorder(0));
    this.searchButton.setIcon(new ImageIcon(getClass().getResource("/search.png")));
    this.searchButton.setToolTipText("perform search");
    this.searchButton.setActionCommand("search");
    this.loadButton.setIcon(new ImageIcon(getClass().getResource("/fileopen.png")));
    this.loadButton.setToolTipText("load search-terms from file");
    this.loadButton.setActionCommand("load_search");
    this.fuzzyCheckBox.setText("fuzzy match");
    this.fuzzyCheckBox.setToolTipText("include misspelled results (only works in local WBL searches)");
    this.fuzzyCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.fuzzyCheckBox.setMargin(new Insets(0, 0, 0, 0));
    this.saveButton.setIcon(new ImageIcon(getClass().getResource("/saveas.png")));
    this.saveButton.setToolTipText("save search-terms to file");
    this.saveButton.setActionCommand("save_search");
    this.clearButton.setIcon(new ImageIcon(getClass().getResource("/clear.png")));
    this.clearButton.setToolTipText("clear search-terms");
    this.clearButton.setActionCommand("clear_search");
    this.abortButton.setIcon(new ImageIcon(getClass().getResource("/stop.png")));
    this.abortButton.setToolTipText("abort running search");
    this.abortButton.setActionCommand("abort_search");
    this.jPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "IP range limits"));
    this.jLabel1.setText("Min:");
    this.jLabel2.setText("Max:");
    this.rangeSearchButton.setIcon(new ImageIcon(getClass().getResource("/find.png")));
    this.rangeSearchButton.setToolTipText("search only by range (does not work for online searches)");
    this.rangeSearchButton.setActionCommand("search_range");
    GroupLayout groupLayout1 = new GroupLayout(this.jPanel1);
    this.jPanel1.setLayout((LayoutManager)groupLayout1);
    groupLayout1.setHorizontalGroup((GroupLayout.Group)groupLayout1.createParallelGroup(1).add((GroupLayout.Group)groupLayout1.createSequentialGroup().addContainerGap().add(this.jLabel1).addPreferredGap(0).add(this.minRangeBox, -2, -1, -2).addPreferredGap(0).add(this.jLabel2).addPreferredGap(0).add(this.maxRangeBox, -2, -1, -2).addPreferredGap(0).add(this.rangeSearchButton, -2, 40, -2).addContainerGap(-1, 32767)));
    groupLayout1.setVerticalGroup((GroupLayout.Group)groupLayout1.createParallelGroup(1).add(this.rangeSearchButton, -2, 32, -2).add((GroupLayout.Group)groupLayout1.createParallelGroup(3).add(this.maxRangeBox, -2, -1, -2).add(this.jLabel2).add(this.minRangeBox, -2, -1, -2).add(this.jLabel1)));
    GroupLayout groupLayout2 = new GroupLayout(this);
    setLayout((LayoutManager)groupLayout2);
    groupLayout2.setHorizontalGroup((GroupLayout.Group)groupLayout2.createParallelGroup(1).add((GroupLayout.Group)groupLayout2.createSequentialGroup().addContainerGap().add(this.searchButton, -2, 40, -2).add(18, 18, 18).add(this.fuzzyCheckBox).addPreferredGap(0).add(this.jPanel1, -2, -1, -2).addPreferredGap(0).add(this.loadButton, -2, 40, -2).addPreferredGap(0).add(this.saveButton, -2, 40, -2).addPreferredGap(0).add(this.clearButton, -2, 40, -2).add(19, 19, 19).add(this.abortButton, -2, 40, -2).addContainerGap(-1, 32767)));
    groupLayout2.setVerticalGroup((GroupLayout.Group)groupLayout2.createParallelGroup(1).add((GroupLayout.Group)groupLayout2.createSequentialGroup().add(21, 21, 21).add((GroupLayout.Group)groupLayout2.createParallelGroup(3).add(this.clearButton, -2, 32, -2).add(this.saveButton, -2, 32, -2).add(this.loadButton, -2, 32, -2).add(this.abortButton, -2, 32, -2).add(this.searchButton, -2, 32, -2).add(this.fuzzyCheckBox))).add(this.jPanel1, -2, -1, -2));
  }
  
  public boolean doFuzzyMatch() {
    return this.fuzzyCheckBox.isSelected();
  }
  
  public int getMinRangeSize() {
    String str = (String)this.minRangeBox.getSelectedItem();
    str = str.substring(1);
    return Integer.parseInt(str);
  }
  
  public int getMaxRangeSize() {
    String str = (String)this.maxRangeBox.getSelectedItem();
    str = str.substring(1);
    return Integer.parseInt(str);
  }
  
  public void setSearchEnabled(boolean paramBoolean) {
    this.searchButton.setEnabled(paramBoolean);
    this.abortButton.setEnabled(!paramBoolean);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\SearchButtons.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */