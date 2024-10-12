package org.tbg.gui;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.layout.GroupLayout;
import org.tbg.ip.IpRangeDescr;
import org.tbg.util.Configuration;

public class RangeIpInputdialog extends JPanel {
  private static String lastRange = "";
  
  private JButton cancelButton;
  
  private JButton generateButton;
  
  private NumericTextField intBox;
  
  private JLabel jLabel1;
  
  private JLabel jLabel3;
  
  private JTextField rangeBox;
  
  public RangeIpInputdialog(final WBLGPanel.RIPostCall pc) {
    initComponents();
    String str = Configuration.getInstance().getProp("ip_list_interval", this.intBox.getText());
    this.intBox.setText(str);
    this.rangeBox.setText(lastRange);
    InputUtil.addPopupActions(this.rangeBox);
    InputUtil.addPopupActions(this.intBox);
    this.cancelButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            pc.close();
          }
        });
    this.generateButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            try {
              IpRangeDescr ipRangeDescr;
              String str = RangeIpInputdialog.this.rangeBox.getText().trim();
              int i = str.indexOf('-');
              if (i == -1) {
                ipRangeDescr = new IpRangeDescr(str);
              } else {
                ipRangeDescr = new IpRangeDescr(str.substring(0, i).trim(), str.substring(i + 1).trim(), "", "");
              } 
              int j = Integer.parseInt(RangeIpInputdialog.this.intBox.getText());
              RangeIpInputdialog.lastRange = str;
              Configuration.getInstance().setProperty("ip_list_interval", RangeIpInputdialog.this.intBox.getText());
              pc.make(ipRangeDescr.getStart(), ipRangeDescr.getEnd(), j);
            } catch (Exception exception) {
              (WBLGPanel.getInstance()).sap.reportException(exception);
            } 
          }
        });
  }
  
  private void initComponents() {
    this.intBox = new NumericTextField();
    this.rangeBox = new JTextField();
    this.jLabel1 = new JLabel();
    this.jLabel3 = new JLabel();
    this.cancelButton = new JButton();
    this.generateButton = new JButton();
    this.intBox.setText("256");
    this.jLabel1.setText("range:");
    this.jLabel3.setText("interval:");
    this.cancelButton.setText("cancel");
    this.generateButton.setText("generate");
    GroupLayout groupLayout = new GroupLayout(this);
    setLayout((LayoutManager)groupLayout);
    groupLayout.setHorizontalGroup((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createSequentialGroup().addContainerGap().add((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createSequentialGroup().add((GroupLayout.Group)groupLayout.createParallelGroup(1).add(this.jLabel1).add(this.jLabel3)).add(18, 18, 18).add((GroupLayout.Group)groupLayout.createParallelGroup(1).add(this.intBox, -2, 37, -2).add((GroupLayout.Group)groupLayout.createParallelGroup(2).add(this.generateButton).add(this.rangeBox, -2, 256, -2)))).add(this.cancelButton)).addContainerGap(24, 32767)));
    groupLayout.setVerticalGroup((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createSequentialGroup().addContainerGap().add((GroupLayout.Group)groupLayout.createParallelGroup(3).add(this.jLabel1).add(this.rangeBox, -2, -1, -2)).addPreferredGap(0).add((GroupLayout.Group)groupLayout.createParallelGroup(3).add(this.jLabel3).add(this.intBox, -2, -1, -2)).addPreferredGap(0).add((GroupLayout.Group)groupLayout.createParallelGroup(3).add(this.cancelButton).add(this.generateButton)).addContainerGap(27, 32767)));
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\RangeIpInputdialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */