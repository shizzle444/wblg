package org.tbg.util;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.tbg.gui.WBLGPanel;
import org.tbg.gui.WhoisResultTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLMenu {
  private JPopupMenu menu;
  
  private final String menuFilename = "menu.xml";
  
  private final WhoisResultTable table = (WBLGPanel.getInstance()).resultTable;
  
  public XMLMenu(JPopupMenu paramJPopupMenu) throws Exception {
    this.menu = paramJPopupMenu;
    File file = new File("menu.xml");
    if (file.exists()) {
      load(new FileInputStream(file));
    } else {
      load(getClass().getResourceAsStream("/menu.xml"));
    } 
  }
  
  private AbstractAction buildAction(String paramString1, final String url) {
    return new AbstractAction(paramString1) {
        public void actionPerformed(ActionEvent param1ActionEvent) {
          XMLMenu.this.table.walkSelectedEntries(new Browser.Consumer(url));
        }
      };
  }
  
  private void load(InputStream paramInputStream) throws Exception {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    Document document = documentBuilder.parse(paramInputStream);
    NodeList nodeList = document.getDocumentElement().getElementsByTagName("category");
    int i = nodeList.getLength();
    for (byte b = 0; b < i; b++) {
      Element element = (Element)nodeList.item(b);
      String str = element.getAttribute("name");
      JMenu jMenu = new JMenu(str);
      Node node;
      for (node = element.getFirstChild(); node != null && node.getNodeType() != 1; node = node.getNextSibling());
      while (node != null) {
        Element element1 = (Element)node;
        if (element1.getNodeName().equals("sep")) {
          jMenu.add(new JSeparator());
        } else {
          String str1 = element1.getAttribute("name");
          String str2 = element1.getFirstChild().getNodeValue();
          System.out.println(str1 + ":" + str2);
          jMenu.add(new JMenuItem(buildAction(str1, str2)));
        } 
        for (node = node.getNextSibling(); node != null && node.getNodeType() != 1; node = node.getNextSibling());
      } 
      this.menu.add(jMenu);
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\XMLMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */