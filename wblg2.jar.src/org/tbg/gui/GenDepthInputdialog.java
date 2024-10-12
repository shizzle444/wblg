package org.tbg.gui;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.layout.GroupLayout;
import org.tbg.util.Configuration;

public class GenDepthInputdialog extends JPanel {
  private JButton cancelButton;
  
  private JTextField depthBox;
  
  private JButton generateButton;
  
  private JLabel jLabel1;
  
  private JLabel jLabel2;
  
  private JLabel jLabel3;
  
  private JTextField prefixBox;
  
  private JTextField queryBox;
  
  public GenDepthInputdialog(final WBLGPanel.GDPostCall pc) {
    initComponents();
    String str = Configuration.getInstance().getProp("depth_search_depth", this.depthBox.getText());
    this.depthBox.setText(str);
    InputUtil.addPopupActions(this.depthBox);
    InputUtil.addPopupActions(this.prefixBox);
    InputUtil.addPopupActions(this.queryBox);
    this.cancelButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            pc.close();
          }
        });
    this.generateButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            try {
              int i = Integer.parseInt(GenDepthInputdialog.this.depthBox.getText());
              Configuration.getInstance().setProperty("depth_search_depth", GenDepthInputdialog.this.depthBox.getText().trim());
              pc.make(i, GenDepthInputdialog.this.prefixBox.getText(), GenDepthInputdialog.this.queryBox.getText());
            } catch (Exception exception) {
              (WBLGPanel.getInstance()).sap.reportException(exception);
            } 
          }
        });
  }
  
  private void initComponents() {
    this.jLabel1 = new JLabel();
    this.jLabel2 = new JLabel();
    this.depthBox = new JTextField();
    this.queryBox = new JTextField();
    this.cancelButton = new JButton();
    this.generateButton = new JButton();
    this.jLabel3 = new JLabel();
    this.prefixBox = new JTextField();
    this.jLabel1.setText("depth:");
    this.jLabel2.setText("query term:");
    this.depthBox.setText("30");
    this.cancelButton.setText("cancel");
    this.generateButton.setText("generate");
    this.jLabel3.setText("prefix:");
    this.prefixBox.setText("_");
    GroupLayout groupLayout = new GroupLayout(this);
    setLayout((LayoutManager)groupLayout);
    groupLayout.setHorizontalGroup((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createSequentialGroup().addContainerGap().add((GroupLayout.Group)groupLayout.createParallelGroup(1).add(this.cancelButton).add((GroupLayout.Group)groupLayout.createSequentialGroup().add(this.jLabel2).addPreferredGap(0).add(this.queryBox, -2, 276, -2)).add((GroupLayout.Group)groupLayout.createSequentialGroup().add((GroupLayout.Group)groupLayout.createParallelGroup(1).add(this.jLabel1).add(this.jLabel3)).add(44, 44, 44).add((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createSequentialGroup().add(188, 188, 188).add(this.generateButton)).add((GroupLayout.Group)groupLayout.createParallelGroup(2, false).add(1, this.prefixBox).add(1, this.depthBox, -1, 77, 32767))))).addContainerGap(29, 32767)));
    groupLayout.setVerticalGroup((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createSequentialGroup().addContainerGap().add((GroupLayout.Group)groupLayout.createParallelGroup(3).add(this.jLabel1).add(this.depthBox, -2, -1, -2)).addPreferredGap(0).add((GroupLayout.Group)groupLayout.createParallelGroup(3).add(this.jLabel3).add(this.prefixBox, -2, -1, -2)).add(17, 17, 17).add((GroupLayout.Group)groupLayout.createParallelGroup(3).add(this.jLabel2).add(this.queryBox, -2, -1, -2)).addPreferredGap(0, 21, 32767).add((GroupLayout.Group)groupLayout.createParallelGroup(3).add(this.cancelButton).add(this.generateButton)).addContainerGap()));
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\GenDepthInputdialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */