package org.tbg.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class BlockedRenderer extends JLabel implements TableCellRenderer {
  Border unselectedBorder = null;
  
  Border selectedBorder = null;
  
  boolean isBordered = true;
  
  public BlockedRenderer() {
    this.isBordered = false;
    setOpaque(true);
  }
  
  public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2) {
    byte b = ((Byte)paramObject).byteValue();
    Color color = Color.GREEN;
    switch (b) {
      case 1:
        color = Color.RED;
        break;
      case 2:
        color = new Color(255, 255, 0);
        break;
      case 3:
        color = new Color(127, 86, 70);
        break;
      case 4:
        color = new Color(84, 210, 170);
        break;
    } 
    setBackground(color);
    if (this.isBordered)
      if (paramBoolean1) {
        if (this.selectedBorder == null)
          this.selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, paramJTable.getSelectionBackground()); 
        setBorder(this.selectedBorder);
      } else {
        if (this.unselectedBorder == null)
          this.unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, paramJTable.getBackground()); 
        setBorder(this.unselectedBorder);
      }  
    return this;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\BlockedRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */