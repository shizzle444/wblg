package org.tbg.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import org.tbg.action.GenericAction;

public class SubSearchDialog extends JDialog {
  private final AbstractAction searchAction = new AbstractAction("search") {
      public void actionPerformed(ActionEvent param1ActionEvent) {
        SubSearchDialog.this.setVisible(false);
        (WBLGPanel.getInstance()).resultTable.searchInResults(SubSearchDialog.this.inputField.getText(), 0);
      }
    };
  
  private final AbstractAction nextSearchAction = new AbstractAction("search next") {
      public void actionPerformed(ActionEvent param1ActionEvent) {
        SubSearchDialog.this.setVisible(false);
        (WBLGPanel.getInstance()).resultTable.searchInResults(SubSearchDialog.this.inputField.getText(), 1);
      }
    };
  
  private final AbstractAction cancelAction = new AbstractAction("cancel") {
      public void actionPerformed(ActionEvent param1ActionEvent) {
        SubSearchDialog.this.setVisible(false);
      }
    };
  
  private final AbstractAction resetAction = new AbstractAction("reset all") {
      public void actionPerformed(ActionEvent param1ActionEvent) {
        SubSearchDialog.this.setVisible(false);
        (WBLGPanel.getInstance()).resultTable.resetSubSearch((byte)0);
      }
    };
  
  private final AbstractAction resetLastAction = new AbstractAction("reset last") {
      public void actionPerformed(ActionEvent param1ActionEvent) {
        SubSearchDialog.this.setVisible(false);
        (WBLGPanel.getInstance()).resultTable.resetSubSearch((byte)1);
      }
    };
  
  private final JTextField inputField = new JTextField();
  
  private final JButton searchButton;
  
  private final JButton resetButton;
  
  private final JButton cancelButton = new JButton(this.cancelAction);
  
  public SubSearchDialog(JFrame paramJFrame, boolean paramBoolean) {
    super(paramJFrame, false);
    JPanel jPanel1 = new JPanel(new BorderLayout());
    jPanel1.add(this.inputField, "Center");
    JPanel jPanel2 = new JPanel(new FlowLayout());
    jPanel1.add(jPanel2, "South");
    if (paramBoolean) {
      setTitle("search next");
      this.searchButton = new JButton(this.nextSearchAction);
      this.resetButton = new JButton(this.resetLastAction);
    } else {
      setTitle("search in results");
      this.searchButton = new JButton(this.searchAction);
      this.resetButton = new JButton(this.resetAction);
    } 
    jPanel2.add(this.searchButton);
    jPanel2.add(this.cancelButton);
    jPanel2.add(this.resetButton);
    getContentPane().add(jPanel1);
    setDefaultCloseOperation(0);
    pack();
    setVisible(true);
    this.inputField.requestFocus();
    this.inputField.addActionListener(this.searchAction);
    this.inputField.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent param1MouseEvent) {
            int i = param1MouseEvent.getModifiers();
            if ((i & 0x4) != 0) {
              JPopupMenu jPopupMenu = SubSearchDialog.this.createPopup();
              jPopupMenu.show(SubSearchDialog.this.inputField, param1MouseEvent.getX(), param1MouseEvent.getY());
            } 
          }
        });
    this.inputField.getActionMap().put("Copy", (Action)new GenericAction(this.inputField, "copy", "Copy", null));
    this.inputField.getActionMap().put("Paste", (Action)new GenericAction(this.inputField, "paste", "Paste", null));
  }
  
  private JPopupMenu createPopup() {
    JPopupMenu jPopupMenu = new JPopupMenu();
    ActionMap actionMap = this.inputField.getActionMap();
    jPopupMenu.add(new JMenuItem(actionMap.get("Copy")));
    jPopupMenu.add(new JMenuItem(actionMap.get("Paste")));
    return jPopupMenu;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\SubSearchDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */