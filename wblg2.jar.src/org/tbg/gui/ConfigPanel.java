package org.tbg.gui;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextField;
import org.jdesktop.layout.GroupLayout;
import org.tbg.ItemTableModel;
import org.tbg.action.GenericAction;
import org.tbg.util.Configuration;

public class ConfigPanel extends JPanel {
  private Configuration conf;
  
  private JCheckBox alphaSortCheck;
  
  private JCheckBox arinDoASN;
  
  private JTextField browserField;
  
  private NumericTextField cidrAutnumDays;
  
  private JPanel dbImportPanel;
  
  private NumericTextField dnsTimeoutField;
  
  private JSlider fuzzyThreshold;
  
  private JCheckBox fuzzyTryHard;
  
  private JLabel jLabel1;
  
  private JLabel jLabel10;
  
  private JLabel jLabel3;
  
  private JLabel jLabel4;
  
  private JLabel jLabel5;
  
  private JLabel jLabel6;
  
  private JLabel jLabel7;
  
  private JLabel jLabel8;
  
  private JLabel jLabel9;
  
  private JPanel jPanel1;
  
  private JPanel jPanel2;
  
  private JPanel jPanel3;
  
  private JCheckBox rightClickSelect;
  
  private JCheckBox singleInstanceCheck;
  
  private JComboBox threadPrioBox;
  
  private JCheckBox warnCompSearchInfo;
  
  private ItemListTable emailList;
  
  private ItemListTable fieldsList;
  
  public ConfigPanel() {
    initComponents();
    this.conf = Configuration.getInstance();
    this.singleInstanceCheck.setSelected(this.conf.getPropBool("single_instance", "true"));
    this.warnCompSearchInfo.setSelected(this.conf.getPropBool("warn_composite_search", "true"));
    this.threadPrioBox.setSelectedIndex(prio2idx(this.conf.getPropInt("thread_priority", Integer.toString(5))));
    this.fuzzyThreshold.setValue(this.conf.getPropInt("fuzzy_threshold", "5"));
    this.fuzzyTryHard.setSelected(this.conf.getPropBool("fuzzy_substring", "false"));
    this.rightClickSelect.setSelected(this.conf.getPropBool("right_click_select", "false"));
    this.alphaSortCheck.setSelected(this.conf.getPropBool("initial_sort_alpha", "false"));
    this.arinDoASN.setSelected(this.conf.getPropBool("arin_use_asns", "true"));
    this.cidrAutnumDays.setText(this.conf.getProp("cidr_autnum_days", "7"));
    this.dnsTimeoutField.setText(this.conf.getProp("dns_timeout", "10"));
    String str = System.getProperty("os.name");
    if (str.startsWith("Mac OS")) {
      this.browserField.setEnabled(false);
    } else if (str.startsWith("Windows")) {
      this.browserField.setEnabled(false);
    } else {
      this.browserField.setEnabled(true);
      this.browserField.setText(this.conf.getProp("browser", "firefox"));
    } 
    this.singleInstanceCheck.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ConfigPanel.this.conf.setProperty("single_instance", Boolean.toString(ConfigPanel.this.singleInstanceCheck.isSelected()));
            JOptionPane.showMessageDialog(ConfigPanel.this, "You need to restart the program to make this change effective.", "Program restart required!", 1);
          }
        });
    this.arinDoASN.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ConfigPanel.this.conf.setProperty("arin_use_asns", Boolean.toString(ConfigPanel.this.arinDoASN.isSelected()));
          }
        });
    this.alphaSortCheck.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ConfigPanel.this.conf.setProperty("initial_sort_alpha", Boolean.toString(ConfigPanel.this.alphaSortCheck.isSelected()));
            (WBLGPanel.getInstance()).initialSortAlpha = ConfigPanel.this.alphaSortCheck.isSelected();
          }
        });
    this.warnCompSearchInfo.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ConfigPanel.this.conf.setProperty("warn_composite_search", Boolean.toString(ConfigPanel.this.warnCompSearchInfo.isSelected()));
          }
        });
    this.rightClickSelect.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ConfigPanel.this.conf.setProperty("right_click_select", Boolean.toString(ConfigPanel.this.rightClickSelect.isSelected()));
          }
        });
    this.threadPrioBox.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ConfigPanel.this.conf.setProperty("thread_priority", Integer.toString(ConfigPanel.this.idx2prio(ConfigPanel.this.threadPrioBox.getSelectedIndex())));
          }
        });
    this.browserField.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ConfigPanel.this.conf.setProperty("browser", ConfigPanel.this.browserField.getText());
          }
        });
    this.browserField.addFocusListener(new FocusAdapter() {
          public void focusLost(FocusEvent param1FocusEvent) {
            ConfigPanel.this.conf.setProperty("browser", ConfigPanel.this.browserField.getText());
          }
        });
    this.fuzzyTryHard.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ConfigPanel.this.conf.setProperty("fuzzy_substring", Boolean.toString(ConfigPanel.this.fuzzyTryHard.isSelected()));
          }
        });
    this.fuzzyThreshold.addFocusListener(new FocusAdapter() {
          public void focusLost(FocusEvent param1FocusEvent) {
            ConfigPanel.this.conf.setProperty("fuzzy_threshold", Integer.toString(ConfigPanel.this.fuzzyThreshold.getValue()));
          }
        });
    this.cidrAutnumDays.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ConfigPanel.this.conf.setProperty("cidr_autnum_days", ConfigPanel.this.cidrAutnumDays.getText());
          }
        });
    this.cidrAutnumDays.addFocusListener(new FocusAdapter() {
          public void focusLost(FocusEvent param1FocusEvent) {
            ConfigPanel.this.conf.setProperty("cidr_autnum_days", ConfigPanel.this.cidrAutnumDays.getText());
          }
        });
    this.dnsTimeoutField.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ConfigPanel.this.conf.setProperty("dns_timeout", ConfigPanel.this.dnsTimeoutField.getText());
          }
        });
    this.dnsTimeoutField.addFocusListener(new FocusAdapter() {
          public void focusLost(FocusEvent param1FocusEvent) {
            ConfigPanel.this.conf.setProperty("dns_timeout", ConfigPanel.this.dnsTimeoutField.getText());
          }
        });
    this.dbImportPanel.setLayout(new GridLayout(1, 2));
    this.emailList = new ItemListTable("Fields scanned for email-addresses", "db_import_email", "changed=false,notify=false,owner=false");
    this.fieldsList = new ItemListTable("Fields appended to description", "db_import_fields", "admin-c=false,mnt-by=false,tech-c=false");
    this.dbImportPanel.add(this.emailList);
    this.dbImportPanel.add(this.fieldsList);
  }
  
  public ItemTableModel getEmailItemList() {
    return this.emailList.getDataModel();
  }
  
  public ItemTableModel getFieldsItemList() {
    return this.fieldsList.getDataModel();
  }
  
  private void delegateAction(JComponent paramJComponent, String paramString1, String paramString2, Icon paramIcon) {
    paramJComponent.getActionMap().put(paramString2, (Action)new GenericAction(paramJComponent, paramString1, paramString2, paramIcon));
  }
  
  private JPopupMenu createPopup(JComponent paramJComponent) {
    JPopupMenu jPopupMenu = new JPopupMenu();
    ActionMap actionMap = paramJComponent.getActionMap();
    jPopupMenu.add(new JMenuItem(actionMap.get("Copy")));
    jPopupMenu.add(new JMenuItem(actionMap.get("Paste")));
    return jPopupMenu;
  }
  
  public int prio2idx(int paramInt) {
    byte b = 0;
    if (paramInt == 1) {
      b = 0;
    } else if (paramInt == 5) {
      b = 1;
    } else if (paramInt == 10) {
      b = 2;
    } 
    return b;
  }
  
  public int idx2prio(int paramInt) {
    byte b = 5;
    if (paramInt == 0) {
      b = 1;
    } else if (paramInt == 1) {
      b = 5;
    } else if (paramInt == 2) {
      b = 10;
    } 
    return b;
  }
  
  private void initComponents() {
    this.jPanel1 = new JPanel();
    this.browserField = new JTextField();
    this.jLabel1 = new JLabel();
    this.jLabel7 = new JLabel();
    this.jLabel8 = new JLabel();
    this.cidrAutnumDays = new NumericTextField();
    this.jPanel2 = new JPanel();
    this.singleInstanceCheck = new JCheckBox();
    this.threadPrioBox = new JComboBox();
    this.jLabel3 = new JLabel();
    this.warnCompSearchInfo = new JCheckBox();
    this.rightClickSelect = new JCheckBox();
    this.alphaSortCheck = new JCheckBox();
    this.arinDoASN = new JCheckBox();
    this.dnsTimeoutField = new NumericTextField();
    this.jLabel9 = new JLabel();
    this.jLabel10 = new JLabel();
    this.jPanel3 = new JPanel();
    this.fuzzyTryHard = new JCheckBox();
    this.jLabel4 = new JLabel();
    this.jLabel5 = new JLabel();
    this.fuzzyThreshold = new JSlider();
    this.jLabel6 = new JLabel();
    this.dbImportPanel = new JPanel();
    this.jPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "web access"));
    this.jLabel1.setText("browser");
    this.jLabel7.setText("update CIDR ASN file after");
    this.jLabel8.setText("days");
    GroupLayout groupLayout1 = new GroupLayout(this.jPanel1);
    this.jPanel1.setLayout((LayoutManager)groupLayout1);
    groupLayout1.setHorizontalGroup((GroupLayout.Group)groupLayout1.createParallelGroup(1).add((GroupLayout.Group)groupLayout1.createSequentialGroup().addContainerGap().add((GroupLayout.Group)groupLayout1.createParallelGroup(1).add((GroupLayout.Group)groupLayout1.createSequentialGroup().add(this.jLabel1).addPreferredGap(0).add(this.browserField, -2, 223, -2)).add((GroupLayout.Group)groupLayout1.createSequentialGroup().add(this.jLabel7).addPreferredGap(0).add(this.cidrAutnumDays, -2, 48, -2).addPreferredGap(0).add(this.jLabel8))).addContainerGap(300, 32767)));
    groupLayout1.setVerticalGroup((GroupLayout.Group)groupLayout1.createParallelGroup(1).add((GroupLayout.Group)groupLayout1.createSequentialGroup().add((GroupLayout.Group)groupLayout1.createParallelGroup(3).add(this.jLabel1).add(this.browserField, -2, -1, -2)).addPreferredGap(0).add((GroupLayout.Group)groupLayout1.createParallelGroup(3).add(this.cidrAutnumDays, -2, -1, -2).add(this.jLabel8).add(this.jLabel7)).addContainerGap(-1, 32767)));
    this.jPanel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "other options"));
    this.singleInstanceCheck.setText("allow only one instance to run");
    this.singleInstanceCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.threadPrioBox.setModel(new DefaultComboBoxModel<>(new String[] { "Low", "Normal", "High" }));
    this.threadPrioBox.setSelectedIndex(1);
    this.jLabel3.setText("Thread priority:");
    this.warnCompSearchInfo.setText("warn about searching WBL & online at once");
    this.warnCompSearchInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.rightClickSelect.setText("select results with right-click");
    this.rightClickSelect.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.alphaSortCheck.setText("initial sort: alphabetically");
    this.alphaSortCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.arinDoASN.setText("lookup ASNs in arin results");
    this.arinDoASN.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.jLabel9.setText("DNS timeout:");
    this.jLabel10.setText("seconds");
    GroupLayout groupLayout2 = new GroupLayout(this.jPanel2);
    this.jPanel2.setLayout((LayoutManager)groupLayout2);
    groupLayout2.setHorizontalGroup((GroupLayout.Group)groupLayout2.createParallelGroup(1).add((GroupLayout.Group)groupLayout2.createSequentialGroup().addContainerGap().add((GroupLayout.Group)groupLayout2.createParallelGroup(1).add(this.singleInstanceCheck).add(this.warnCompSearchInfo).add((GroupLayout.Group)groupLayout2.createSequentialGroup().add(this.jLabel3).addPreferredGap(0).add(this.threadPrioBox, -2, 86, -2))).add(34, 34, 34).add((GroupLayout.Group)groupLayout2.createParallelGroup(1).add(this.alphaSortCheck).add(this.rightClickSelect).add((GroupLayout.Group)groupLayout2.createSequentialGroup().add(this.jLabel9).addPreferredGap(0).add(this.dnsTimeoutField, -2, 44, -2).addPreferredGap(0).add(this.jLabel10)).add(this.arinDoASN)).addContainerGap(44, 32767)));
    groupLayout2.setVerticalGroup((GroupLayout.Group)groupLayout2.createParallelGroup(1).add((GroupLayout.Group)groupLayout2.createSequentialGroup().add(this.rightClickSelect).addPreferredGap(0).add((GroupLayout.Group)groupLayout2.createParallelGroup(3).add(this.singleInstanceCheck).add(this.alphaSortCheck)).addPreferredGap(0).add((GroupLayout.Group)groupLayout2.createParallelGroup(3).add(this.warnCompSearchInfo).add(this.arinDoASN)).addPreferredGap(0, 24, 32767).add((GroupLayout.Group)groupLayout2.createParallelGroup(3).add(this.jLabel3).add(this.threadPrioBox, -2, -1, -2).add(this.jLabel9).add(this.dnsTimeoutField, -2, -1, -2).add(this.jLabel10)).add(21, 21, 21)));
    this.jPanel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "fuzzy search options"));
    this.fuzzyTryHard.setText("try really hard");
    this.fuzzyTryHard.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.jLabel4.setText("match threshold:");
    this.jLabel5.setText("perfect");
    this.fuzzyThreshold.setMajorTickSpacing(1);
    this.fuzzyThreshold.setMaximum(10);
    this.fuzzyThreshold.setPaintTicks(true);
    this.fuzzyThreshold.setSnapToTicks(true);
    this.fuzzyThreshold.setValue(5);
    this.jLabel6.setText("loose");
    GroupLayout groupLayout3 = new GroupLayout(this.jPanel3);
    this.jPanel3.setLayout((LayoutManager)groupLayout3);
    groupLayout3.setHorizontalGroup((GroupLayout.Group)groupLayout3.createParallelGroup(1).add((GroupLayout.Group)groupLayout3.createSequentialGroup().addContainerGap().add((GroupLayout.Group)groupLayout3.createParallelGroup(1).add((GroupLayout.Group)groupLayout3.createSequentialGroup().add(this.jLabel4).add(18, 18, 18).add(this.jLabel5).add(1, 1, 1).add(this.fuzzyThreshold, -2, 117, -2).add(2, 2, 2).add(this.jLabel6)).add(this.fuzzyTryHard)).addContainerGap(259, 32767)));
    groupLayout3.setVerticalGroup((GroupLayout.Group)groupLayout3.createParallelGroup(1).add((GroupLayout.Group)groupLayout3.createSequentialGroup().add((GroupLayout.Group)groupLayout3.createParallelGroup(1).add((GroupLayout.Group)groupLayout3.createSequentialGroup().add((GroupLayout.Group)groupLayout3.createParallelGroup(3).add(this.jLabel4).add(this.jLabel5)).add(7, 7, 7).add(this.fuzzyTryHard)).add(this.fuzzyThreshold, -2, -1, -2).add(this.jLabel6)).addContainerGap(14, 32767)));
    this.dbImportPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "DB import"));
    GroupLayout groupLayout4 = new GroupLayout(this.dbImportPanel);
    this.dbImportPanel.setLayout((LayoutManager)groupLayout4);
    groupLayout4.setHorizontalGroup((GroupLayout.Group)groupLayout4.createParallelGroup(1).add(0, 597, 32767));
    groupLayout4.setVerticalGroup((GroupLayout.Group)groupLayout4.createParallelGroup(1).add(0, 157, 32767));
    GroupLayout groupLayout5 = new GroupLayout(this);
    setLayout((LayoutManager)groupLayout5);
    groupLayout5.setHorizontalGroup((GroupLayout.Group)groupLayout5.createParallelGroup(1).add(this.jPanel1, -1, -1, 32767).add(this.jPanel2, -1, -1, 32767).add(this.jPanel3, -1, -1, 32767).add(this.dbImportPanel, -1, -1, 32767));
    groupLayout5.setVerticalGroup((GroupLayout.Group)groupLayout5.createParallelGroup(1).add((GroupLayout.Group)groupLayout5.createSequentialGroup().add(this.jPanel1, -2, -1, -2).addPreferredGap(0).add(this.jPanel2, -2, -1, -2).addPreferredGap(0).add(this.jPanel3, -2, -1, -2).addPreferredGap(0).add(this.dbImportPanel, -1, -1, 32767).addContainerGap()));
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\ConfigPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */