package org.tbg.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class TwoActionButton extends JButton {
  Action left = null;
  
  Action right = null;
  
  String tooltip = "";
  
  public TwoActionButton() {}
  
  public TwoActionButton(Action paramAction1, Action paramAction2, String paramString) {
    super(paramAction1);
    this.tooltip = paramString;
    this.left = paramAction1;
    this.right = paramAction2;
    addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent param1MouseEvent) {
            if (SwingUtilities.isRightMouseButton(param1MouseEvent)) {
              TwoActionButton twoActionButton = (TwoActionButton)param1MouseEvent.getSource();
              twoActionButton.activateRight();
            } 
          }
        });
    setToolTipText(paramString);
  }
  
  public void activateRight() {
    setAction(this.right);
    doClick();
    setAction(this.left);
    setToolTipText(this.tooltip);
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\TwoActionButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */