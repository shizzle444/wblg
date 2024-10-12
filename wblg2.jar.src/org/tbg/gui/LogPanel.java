package org.tbg.gui;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.jdesktop.layout.GroupLayout;
import org.tbg.action.GenericAction;
import org.tbg.util.ClipHelper;

public class LogPanel extends JPanel {
  private MListener listener;
  
  private String nl;
  
  private JButton clearLogButton;
  
  private JButton clipLogButton;
  
  private JScrollPane jScrollPane1;
  
  private JTextPane logPane;
  
  public LogPanel() {
    initComponents();
    this.listener = new MListener();
    this.clearLogButton.setActionCommand("clear");
    this.clearLogButton.addActionListener(this.listener);
    this.clipLogButton.setActionCommand("clipboard");
    this.clipLogButton.addActionListener(this.listener);
    this.nl = System.getProperty("line.separator");
    delegateAction(this.logPane, "copy", "Copy", null);
    this.logPane.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent param1MouseEvent) {
            int i = param1MouseEvent.getModifiers();
            if ((i & 0x4) != 0) {
              JPopupMenu jPopupMenu = LogPanel.this.createPopup(LogPanel.this.logPane);
              jPopupMenu.show(LogPanel.this.logPane, param1MouseEvent.getX(), param1MouseEvent.getY());
            } 
          }
        });
  }
  
  private JPopupMenu createPopup(JComponent paramJComponent) {
    JPopupMenu jPopupMenu = new JPopupMenu();
    ActionMap actionMap = paramJComponent.getActionMap();
    jPopupMenu.add(new JMenuItem(actionMap.get("Copy")));
    return jPopupMenu;
  }
  
  private void initComponents() {
    this.jScrollPane1 = new JScrollPane();
    this.logPane = new JTextPane();
    this.clearLogButton = new JButton();
    this.clipLogButton = new JButton();
    this.logPane.setEditable(false);
    this.jScrollPane1.setViewportView(this.logPane);
    this.clearLogButton.setText("clear log");
    this.clipLogButton.setText("copy log to clipboard");
    GroupLayout groupLayout = new GroupLayout(this);
    setLayout((LayoutManager)groupLayout);
    groupLayout.setHorizontalGroup((GroupLayout.Group)groupLayout.createParallelGroup(1).add(this.jScrollPane1, -1, 400, 32767).add((GroupLayout.Group)groupLayout.createSequentialGroup().addContainerGap().add(this.clearLogButton).addPreferredGap(0).add(this.clipLogButton).addContainerGap(129, 32767)));
    groupLayout.setVerticalGroup((GroupLayout.Group)groupLayout.createParallelGroup(1).add((GroupLayout.Group)groupLayout.createSequentialGroup().add(this.jScrollPane1, -1, 251, 32767).add(12, 12, 12).add((GroupLayout.Group)groupLayout.createParallelGroup(3).add(this.clearLogButton).add(this.clipLogButton)).addContainerGap()));
  }
  
  public JTextPane getLogPane() {
    return this.logPane;
  }
  
  public void appendStatus(String paramString) {
    append(paramString + this.nl, Color.BLACK);
  }
  
  public void appendError(String paramString) {
    append(paramString + this.nl, Color.RED);
  }
  
  private void delegateAction(JComponent paramJComponent, String paramString1, String paramString2, Icon paramIcon) {
    paramJComponent.getActionMap().put(paramString2, (Action)new GenericAction(paramJComponent, paramString1, paramString2, paramIcon));
  }
  
  private void append(String paramString, Color paramColor) {
    try {
      this.logPane.setEditable(true);
      StyleContext styleContext = StyleContext.getDefaultStyleContext();
      AttributeSet attributeSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, paramColor);
      int i = this.logPane.getDocument().getLength();
      this.logPane.setCaretPosition(i);
      this.logPane.setCharacterAttributes(attributeSet, false);
      this.logPane.replaceSelection(paramString);
    } catch (Exception exception) {
      try {
        this.logPane.setEditable(false);
      } catch (Exception exception1) {}
    } finally {
      try {
        this.logPane.setEditable(false);
      } catch (Exception exception) {}
    } 
  }
  
  class MListener implements ActionListener {
    public void actionPerformed(ActionEvent param1ActionEvent) {
      String str = param1ActionEvent.getActionCommand();
      if (str.equals("clear")) {
        LogPanel.this.logPane.setText("");
      } else if (str.equals("clipboard")) {
        ClipHelper clipHelper = ClipHelper.getInstance();
        clipHelper.setContent(LogPanel.this.logPane.getText());
      } 
    }
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\LogPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */