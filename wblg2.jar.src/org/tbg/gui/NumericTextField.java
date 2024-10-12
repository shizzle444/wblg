package org.tbg.gui;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class NumericTextField extends JTextField {
  protected void processKeyEvent(KeyEvent paramKeyEvent) {
    char c = paramKeyEvent.getKeyChar();
    if (Character.isDigit(c)) {
      super.processKeyEvent(paramKeyEvent);
    } else if (paramKeyEvent.getKeyCode() == 8) {
      super.processKeyEvent(paramKeyEvent);
    } else {
      paramKeyEvent.consume();
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\NumericTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */