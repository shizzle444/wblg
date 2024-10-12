package org.tbg.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import org.tbg.action.GenericAction;

public class MyInputDialog extends JDialog {
  public JTextField inputField = new JTextField();
  
  public JOptionPane optionPane;
  
  public String btn1s = "Update";
  
  public String btn2s = "Cancel";
  
  public MyInputDialog(JFrame paramJFrame, String paramString1, String paramString2) {
    super(paramJFrame, false);
    this.btn1s = paramString1;
    this.btn2s = "Cancel";
    String str = paramString2;
    setTitle(str);
    Object[] arrayOfObject1 = { str, this.inputField };
    Object[] arrayOfObject2 = { this.btn1s, this.btn2s };
    this.optionPane = new JOptionPane(arrayOfObject1, -1, -1, null, arrayOfObject2, arrayOfObject2[0]);
    setContentPane(this.optionPane);
    setDefaultCloseOperation(0);
    pack();
    setVisible(true);
    this.optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
    this.inputField.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent param1MouseEvent) {
            int i = param1MouseEvent.getModifiers();
            if ((i & 0x4) != 0) {
              JPopupMenu jPopupMenu = MyInputDialog.this.createPopup();
              jPopupMenu.show(MyInputDialog.this.inputField, param1MouseEvent.getX(), param1MouseEvent.getY());
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


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\MyInputDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */