package org.tbg.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;
import org.tbg.action.GenericAction;

public class InputUtil {
  public static void addPopupActions(final JTextComponent jtc) {
    jtc.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent param1MouseEvent) {
            int i = param1MouseEvent.getModifiers();
            if ((i & 0x4) != 0) {
              JPopupMenu jPopupMenu = InputUtil.createPopup(jtc);
              jPopupMenu.show(jtc, param1MouseEvent.getX(), param1MouseEvent.getY());
            } 
          }
        });
    jtc.getActionMap().put("Copy", (Action)new GenericAction(jtc, "copy", "Copy", null));
    jtc.getActionMap().put("Paste", (Action)new GenericAction(jtc, "paste", "Paste", null));
  }
  
  private static JPopupMenu createPopup(JTextComponent paramJTextComponent) {
    JPopupMenu jPopupMenu = new JPopupMenu();
    ActionMap actionMap = paramJTextComponent.getActionMap();
    jPopupMenu.add(new JMenuItem(actionMap.get("Copy")));
    jPopupMenu.add(new JMenuItem(actionMap.get("Paste")));
    return jPopupMenu;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\InputUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */