package org.tbg.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import org.tbg.FilesTableModel;
import org.tbg.action.ActionProxy;
import org.tbg.action.ArinChildAction;
import org.tbg.action.GenericAction;
import org.tbg.action.SingleDBAction;
import org.tbg.action.SingleDBChildAction;
import org.tbg.action.SingleNetAction;
import org.tbg.blocklist.BLTableModel;
import org.tbg.filter.file.BlocklistFileFilter;
import org.tbg.filter.file.DBFileFilter;
import org.tbg.filter.file.WBLFileFilter;
import org.tbg.filter.ird.BlockedResultFilter;
import org.tbg.filter.ird.HighlightedResultFilter;
import org.tbg.filter.ird.IRDFilter;
import org.tbg.filter.ird.MarkedResultFilter;
import org.tbg.ip.IpRangeDescr;
import org.tbg.ip.ipv4;
import org.tbg.rpsl.IRD_Consumer;
import org.tbg.threads.BlockCheckThread;
import org.tbg.threads.CheckListThread;
import org.tbg.threads.CidrAsnSearchThread;
import org.tbg.threads.ConverterThread;
import org.tbg.threads.RDnsThread;
import org.tbg.threads.SearchThread;
import org.tbg.util.ClipHelper;
import org.tbg.util.Configuration;
import org.tbg.util.DescrResetConsumer;
import org.tbg.util.HtmlHelpListener;
import org.tbg.util.IRD_ChangeDescription;
import org.tbg.util.IRD_HighlightRemover;
import org.tbg.util.IRD_Visitor;
import org.tbg.util.MarkEntryConsumer;
import org.tbg.util.ProjectStats;
import org.tbg.util.SaveFileConsumer;
import org.tbg.util.XMLMenu;
import org.tbg.wbl.WBLHolder;
import org.tbg.wbl.WBLReader;

public class WBLGPanel extends JPanel {
  private static WBLGPanel instance = null;
  
  private WBLHolder wbl;
  
  private UndoManager undoer = new UndoManager();
  
  private Configuration config = Configuration.getInstance();
  
  private ShowTabHelper tabHelper = new ShowTabHelper();
  
  public boolean initialSortAlpha = this.config.getPropBool("initial_sort_alpha", "false");
  
  AbstractAction storeResults = new AbstractAction("save results") {
      public void actionPerformed(ActionEvent param1ActionEvent) {
        try {
          FileOutputStream fileOutputStream = new FileOutputStream("/tmp/out");
          GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(fileOutputStream);
          ObjectOutputStream objectOutputStream = new ObjectOutputStream(gZIPOutputStream);
          objectOutputStream.writeObject(WBLGPanel.this.resultTable.getResults());
          objectOutputStream.flush();
          objectOutputStream.close();
        } catch (IOException iOException) {
          System.out.println(iOException);
        } 
      }
    };
  
  AbstractAction loadResults = new AbstractAction("load results") {
      public void actionPerformed(ActionEvent param1ActionEvent) {
        try {
          FileInputStream fileInputStream = new FileInputStream("/tmp/out");
          GZIPInputStream gZIPInputStream = new GZIPInputStream(fileInputStream);
          ObjectInputStream objectInputStream = new ObjectInputStream(gZIPInputStream);
          Vector<IpRangeDescr> vector = (Vector)objectInputStream.readObject();
          WBLGPanel.this.resultTable.setResults(vector, 0);
          objectInputStream.close();
        } catch (Exception exception) {}
      }
    };
  
  private Listener listener = new Listener();
  
  ListTableModelListener listTableListener = new ListTableModelListener();
  
  public JTabbedPane tabbed = new JTabbedPane();
  
  private JMenuItem generateDepthMItem;
  
  private JMenuItem generateIPListMItem;
  
  public JPanel resultPanel;
  
  private JPanel toolPanel;
  
  public WhoisResultTable resultTable;
  
  public JPanel searchPanel;
  
  public JTextArea searchArea;
  
  public JPanel searchSingleDbPanel;
  
  public JPanel searchSingleBlPanel;
  
  public SearchButtons searchButtons;
  
  public FileListPanel dbFilesPanel;
  
  public FileListPanel blockFilesPanel;
  
  public QuickList onlineList;
  
  public TwoActionButton arinButton;
  
  public TwoActionButton ripeButton;
  
  public JButton rdnsListButton;
  
  public JButton cidrAsnDButton;
  
  public JButton cidrAsnButton;
  
  public ListCheckPanel listCheckPanel;
  
  private LogPanel logPanel;
  
  public ConfigPanel configPanel;
  
  public StatusAndProgress sap;
  
  public JEditorPane jep;
  
  public static boolean arinChildDialogOn = false;
  
  public WBLGPanel() {
    super(new BorderLayout());
    add(this.tabbed, "Center");
    this.resultPanel = new JPanel(new BorderLayout());
    if (this.config.getPropBool("toolbar", "true")) {
      this.toolPanel = createToolBar();
      this.resultPanel.add(this.toolPanel, "First");
    } 
    this.resultTable = new WhoisResultTable();
    JScrollPane jScrollPane1 = new JScrollPane(this.resultTable);
    this.resultPanel.add(jScrollPane1, "Center");
    this.tabbed.addTab("results", this.resultPanel);
    this.searchPanel = new JPanel();
    this.searchPanel.setLayout(new BoxLayout(this.searchPanel, 1));
    JPanel jPanel1 = new JPanel();
    jPanel1.setLayout(new BoxLayout(jPanel1, 0));
    this.searchArea = new JTextArea();
    this.searchArea.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent param1MouseEvent) {
            int i = param1MouseEvent.getModifiers();
            if ((i & 0x4) != 0) {
              JPopupMenu jPopupMenu = WBLGPanel.this.createEditPopup(WBLGPanel.this.searchArea);
              jPopupMenu.show(WBLGPanel.this.searchArea, param1MouseEvent.getX(), param1MouseEvent.getY());
            } 
          }
        });
    addUndoSupport(this.searchArea);
    delegateAction(this.searchArea, "selectAll", "Select all", (Icon)null);
    delegateAction(this.searchArea, "cut", "Cut", (Icon)null);
    delegateAction(this.searchArea, "copy", "Copy", (Icon)null);
    delegateAction(this.searchArea, "paste", "Paste", (Icon)null);
    this.searchArea.getActionMap().put("Delete", new AbstractAction("Delete") {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            if (WBLGPanel.this.searchArea.getSelectedText() != null)
              WBLGPanel.this.searchArea.replaceSelection(""); 
          }
        });
    JScrollPane jScrollPane2 = new JScrollPane(this.searchArea);
    jScrollPane2.setPreferredSize(new Dimension(600, 700));
    jPanel1.add(jScrollPane2);
    JPanel jPanel2 = new JPanel();
    jPanel2.setLayout(new BoxLayout(jPanel2, 1));
    jPanel1.add(jPanel2);
    this.searchButtons = new SearchButtons(this.listener);
    this.searchSingleDbPanel = new JPanel(new FlowLayout(0));
    this.searchSingleBlPanel = new JPanel(new FlowLayout(0));
    this.searchPanel.add(jPanel1);
    this.searchPanel.add(this.searchButtons);
    this.searchPanel.add(this.searchSingleDbPanel);
    this.searchPanel.add(this.searchSingleBlPanel);
    JPanel jPanel3 = new JPanel(new FlowLayout(0));
    this.arinButton = new TwoActionButton((Action)new SingleNetAction("arin", 1), (Action)new ArinChildAction("arin: children"), "<html>left: search online: arin by description<br>right: search online: arin for children of parent range</html>");
    jPanel3.add(this.arinButton);
    this.ripeButton = new TwoActionButton((Action)new SingleNetAction("ripe", 2), (Action)new SingleNetAction("ripe", 2), "search online: ripe (simple search)");
    jPanel3.add(this.ripeButton);
    this.cidrAsnDButton = new JButton("cidr-report: description");
    this.cidrAsnDButton.setActionCommand("cidr_asn_d");
    this.cidrAsnDButton.addActionListener(this.listener);
    this.cidrAsnDButton.setToolTipText("search cidr-report ASN entries by description");
    jPanel3.add(this.cidrAsnDButton);
    this.cidrAsnButton = new JButton("cidr-report: ASN");
    this.cidrAsnButton.setActionCommand("cidr_asn_n");
    this.cidrAsnButton.addActionListener(this.listener);
    this.cidrAsnButton.setToolTipText("search cidr-report ASN entries by ASN");
    jPanel3.add(this.cidrAsnButton);
    this.rdnsListButton = new JButton("RDNS from list");
    this.rdnsListButton.setActionCommand("iplist_rdns");
    this.rdnsListButton.addActionListener(this.listener);
    this.rdnsListButton.setToolTipText("lookup hostnames from list of IPs (use Tools->generate IP list)");
    jPanel3.add(this.rdnsListButton);
    this.searchPanel.add(jPanel3);
    this.dbFilesPanel = new FileListPanel("db_files", "db_data_dir", (FileFilter)new WBLFileFilter());
    jPanel2.add(new QuickList("quick dbase", this.dbFilesPanel.getDataTable()));
    String str = this.config.getProp("online_search", "arin=true,ripe=true");
    if (str.indexOf(',') == -1) {
      JOptionPane.showMessageDialog(this, "Updated 'online search' configuration; might have reset 'active' setting.\nPlease check!", "Configuration updated", 1);
      this.config.setProperty("online_search", "arin=true,ripe=true");
      str = this.config.getProperty("online_search");
    } 
    this.onlineList = new QuickList(str);
    jPanel2.add(this.onlineList);
    this.blockFilesPanel = new FileListPanel("blocklist_files", "blocklist_data_dir", (FileFilter)new BlocklistFileFilter());
    jPanel2.add(new QuickList("quick blocklist", this.blockFilesPanel.getDataTable()));
    this.tabbed.addTab("search", this.searchPanel);
    this.tabbed.addTab("dbase files", this.dbFilesPanel);
    this.tabbed.addTab("blocklists", this.blockFilesPanel);
    this.configPanel = new ConfigPanel();
    this.tabbed.addTab("config", this.configPanel);
    this.logPanel = new LogPanel();
    this.tabbed.addTab("log", this.logPanel);
    if (this.config.getPropBool("list_check", "false"))
      createListCheck(); 
    this.tabbed.setSelectedComponent(this.searchPanel);
    this.sap = new StatusAndProgress(this.logPanel);
    JPanel jPanel4 = new JPanel(new BorderLayout());
    jPanel4.add(this.sap.statusLabel, "West");
    JPanel jPanel5 = new JPanel();
    jPanel5.add(this.sap.progressBar);
    jPanel5.add(new MemWatcher());
    jPanel4.add(jPanel5, "East");
    add(jPanel4, "South");
    this.wbl = new WBLHolder(this.sap);
    WBLReader.reporter = this.sap;
    dynamicDbButtons();
    instance = this;
    this.tabbed.addChangeListener(new HelpVisibleHandler());
    proxyKey(this.searchArea, 10, 2, "CTRL-Enter", "search", this.listener);
    proxyKey(this, 70, 2, "CTRL-f", "sub_search", this.listener);
    proxyKey(this, 82, 2, "CTRL-r", "clear_highlight", this.listener);
    proxyKey(this.resultTable, 36, 0, "HOME", "scroll_top", this.listener);
    proxyKey(this.resultTable, 35, 0, "END", "scroll_bottom", this.listener);
    proxyKey(this, "F1", "F1", "show_help", this.listener);
    proxyKey(this, "F2", "F2", "results", this.tabHelper);
    proxyKey(this, "F3", "F3", "search", this.tabHelper);
    proxyKey(this, "F4", "F4", "dbase files", this.tabHelper);
    proxyKey(this, "F5", "F5", "blocklists", this.tabHelper);
    proxyKey(this, "F6", "F6", "config", this.tabHelper);
    this.dbFilesPanel.getDataTable().getModel().addTableModelListener(this.listTableListener);
    this.blockFilesPanel.getDataTable().getModel().addTableModelListener(this.listTableListener);
  }
  
  public static WBLGPanel getInstance() {
    return instance;
  }
  
  public JMenuBar getMenuBar() {
    JFrame jFrame = (JFrame)SwingUtilities.windowForComponent(this);
    return jFrame.getJMenuBar();
  }
  
  public JMenu getMenuByName(String paramString) {
    JMenuBar jMenuBar = getMenuBar();
    int i = jMenuBar.getMenuCount();
    for (byte b = 0; b < i; b++) {
      JMenu jMenu = jMenuBar.getMenu(b);
      if (jMenu.getText().equals(paramString))
        return jMenu; 
    } 
    return null;
  }
  
  public boolean doArinSearch() {
    FilesTableModel filesTableModel = this.onlineList.getModel();
    return ((Boolean)filesTableModel.getValueAt(0, 0)).booleanValue();
  }
  
  public int getNetSearch() {
    FilesTableModel filesTableModel = this.onlineList.getModel();
    int i = 0;
    if (((Boolean)filesTableModel.getValueAt(0, 0)).booleanValue())
      i |= 0x1; 
    if (((Boolean)filesTableModel.getValueAt(1, 0)).booleanValue())
      i |= 0x2; 
    return i;
  }
  
  private void dynamicDbButtons() {
    this.searchSingleDbPanel.removeAll();
    FilesTableModel filesTableModel = (FilesTableModel)this.dbFilesPanel.getDataTable().getModel();
    int i = filesTableModel.getRowCount();
    String str1 = System.getProperty("file.separator");
    String str2 = ".wbl.gz";
    byte b;
    for (b = 0; b < i; b++) {
      String str3 = (String)filesTableModel.getValueAt(b, 1);
      String str4 = new String(str3);
      int j = str3.lastIndexOf(str2);
      if (j < 0)
        j = str3.length(); 
      if (str3.indexOf(str1) > -1)
        str4 = str3.substring(str3.lastIndexOf(str1) + 1, j); 
      str4 = str4 + " wbl";
      TwoActionButton twoActionButton = new TwoActionButton((Action)new SingleDBAction(str4, str3, false), (Action)new SingleDBChildAction(str4, str3, false), "<html>left: search " + str4 + " by description<br>right:search " + str4 + " child ranges</html>");
      this.searchSingleDbPanel.add(twoActionButton);
    } 
    this.searchSingleBlPanel.removeAll();
    filesTableModel = (FilesTableModel)this.blockFilesPanel.getDataTable().getModel();
    i = filesTableModel.getRowCount();
    for (b = 0; b < i; b++) {
      String str3 = (String)filesTableModel.getValueAt(b, 1);
      String str4 = new String(str3);
      int j = str3.lastIndexOf(".");
      if (j < 0)
        j = str3.length(); 
      if (str3.indexOf(str1) > -1)
        str4 = str3.substring(str3.lastIndexOf(str1) + 1, j); 
      TwoActionButton twoActionButton = new TwoActionButton((Action)new SingleDBAction(str4, str3, true), (Action)new SingleDBChildAction(str4, str3, true), "<html>left: search " + str4 + " by description<br>right:search " + str4 + " child ranges</html>");
      this.searchSingleBlPanel.add(twoActionButton);
    } 
  }
  
  public void setSearchEnabled(boolean paramBoolean) {
    int i = this.searchSingleDbPanel.getComponentCount();
    byte b;
    for (b = 0; b < i; b++) {
      JButton jButton = (JButton)this.searchSingleDbPanel.getComponent(b);
      jButton.setEnabled(paramBoolean);
    } 
    i = this.searchSingleBlPanel.getComponentCount();
    for (b = 0; b < i; b++) {
      JButton jButton = (JButton)this.searchSingleBlPanel.getComponent(b);
      jButton.setEnabled(paramBoolean);
    } 
    this.searchButtons.setSearchEnabled(paramBoolean);
    this.arinButton.setEnabled(paramBoolean);
    this.ripeButton.setEnabled(paramBoolean);
    this.cidrAsnButton.setEnabled(paramBoolean);
    this.cidrAsnDButton.setEnabled(paramBoolean);
    this.rdnsListButton.setEnabled(paramBoolean);
  }
  
  public void clearResults() {
    BLTableModel bLTableModel = (BLTableModel)this.resultTable.getModel();
    bLTableModel.clear();
    Runtime.getRuntime().gc();
  }
  
  public Vector<String> getQuery() {
    StringTokenizer stringTokenizer;
    Vector<String> vector = new Vector();
    String str = this.searchArea.getText();
    if (str.indexOf("\r\n") > -1) {
      stringTokenizer = new StringTokenizer(str, "\r\n");
    } else {
      stringTokenizer = new StringTokenizer(str, "\n");
    } 
    while (stringTokenizer.hasMoreTokens()) {
      String str1 = stringTokenizer.nextToken();
      if (str1.length() > 0)
        vector.add(new String(str1)); 
    } 
    return vector;
  }
  
  public void setResults(Vector<IpRangeDescr> paramVector) {
    BLTableModel bLTableModel = (BLTableModel)this.resultTable.getModel();
    byte b = 1;
    if (this.initialSortAlpha) {
      b = 3;
      paramVector = WhoisResultTable.resort(paramVector, b);
    } 
    this.resultTable.setResults(paramVector, b);
    if (paramVector.size() > 0)
      this.tabbed.setSelectedIndex(0); 
  }
  
  public void runListCheck() {
    int i = Configuration.getInstance().getPropInt("thread_priority", Integer.toString(5));
    Thread thread = new Thread((Runnable)new CheckListThread());
    thread.setPriority(i);
    thread.start();
  }
  
  public void runSearch(boolean paramBoolean) {
    SearchThread searchThread;
    int i = Configuration.getInstance().getPropInt("thread_priority", Integer.toString(5));
    if (paramBoolean) {
      searchThread = new SearchThread(true);
    } else {
      searchThread = new SearchThread();
    } 
    Thread thread = new Thread((Runnable)searchThread);
    thread.setPriority(i);
    thread.start();
  }
  
  public void subSearch() {}
  
  public void runCidrAsnSearch(boolean paramBoolean) {
    Thread thread = new Thread((Runnable)new CidrAsnSearchThread(paramBoolean));
    thread.start();
  }
  
  private String splitIpArin(String paramString, int paramInt) {
    StringTokenizer stringTokenizer = new StringTokenizer(paramString, ".");
    String[] arrayOfString = new String[4];
    for (byte b1 = 0; b1 < paramInt; b1++)
      arrayOfString[b1] = stringTokenizer.nextToken(); 
    String str = "";
    for (byte b2 = 0; b2 < paramInt; b2++)
      str = str + arrayOfString[b2] + "-"; 
    return str;
  }
  
  private void convertFiles() {
    JFileChooser jFileChooser = openChooser(getFileDirectory("db_data_dir"), (FileFilter)new DBFileFilter());
    jFileChooser.setMultiSelectionEnabled(true);
    if (jFileChooser.showDialog(this, "Select db(s) to convert") == 0) {
      File[] arrayOfFile = jFileChooser.getSelectedFiles();
      Vector<String> vector = new Vector(arrayOfFile.length);
      for (byte b = 0; b < arrayOfFile.length; b++)
        vector.add(arrayOfFile[b].getPath()); 
      Thread thread = new Thread((Runnable)new ConverterThread(vector));
      thread.setPriority(1);
      thread.start();
    } 
  }
  
  private void delegateAction(JComponent paramJComponent, String paramString1, String paramString2, Icon paramIcon) {
    paramJComponent.getActionMap().put(paramString2, (Action)new GenericAction(paramJComponent, paramString1, paramString2, paramIcon));
  }
  
  private void proxyKey(JComponent paramJComponent, String paramString1, String paramString2, String paramString3, ActionListener paramActionListener) {
    paramJComponent.getInputMap(1).put(KeyStroke.getKeyStroke(paramString1), paramString2);
    paramJComponent.getActionMap().put(paramString2, (Action)new ActionProxy(paramString3, paramActionListener));
  }
  
  private void proxyKey(JComponent paramJComponent, int paramInt1, int paramInt2, String paramString1, String paramString2, ActionListener paramActionListener) {
    paramJComponent.getInputMap(1).put(KeyStroke.getKeyStroke(paramInt1, paramInt2), paramString1);
    paramJComponent.getActionMap().put(paramString1, (Action)new ActionProxy(paramString2, paramActionListener));
  }
  
  private String getFileDirectory(String paramString) {
    return this.config.getProp(paramString, System.getProperty("user.dir"));
  }
  
  private void searchChooserLoading(boolean paramBoolean) {
    String str = "Load";
    if (!paramBoolean)
      str = "Save"; 
    JFileChooser jFileChooser = openChooser(getFileDirectory("query_data_dir"), (FileFilter)null);
    if (jFileChooser.showDialog(this, str) == 0) {
      if (paramBoolean) {
        loadTerms(jFileChooser.getSelectedFile().getPath(), this.searchArea);
      } else {
        saveTerms(jFileChooser.getSelectedFile().getPath(), this.searchArea);
      } 
      this.config.setProperty("query_data_dir", jFileChooser.getSelectedFile().getParent());
    } 
  }
  
  private void listCheckChooserLoading(boolean paramBoolean) {
    String str = "Load";
    if (!paramBoolean)
      str = "Save"; 
    JFileChooser jFileChooser = openChooser(getFileDirectory("query_data_dir"), (FileFilter)null);
    if (jFileChooser.showDialog(this, str) == 0) {
      if (paramBoolean) {
        loadTerms(jFileChooser.getSelectedFile().getPath(), this.listCheckPanel.getArea());
      } else {
        saveTerms(jFileChooser.getSelectedFile().getPath(), this.listCheckPanel.getArea());
      } 
      this.config.setProperty("query_data_dir", jFileChooser.getSelectedFile().getParent());
    } 
  }
  
  private void loadTerms(String paramString, JTextArea paramJTextArea) {
    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(paramString));
      StringBuilder stringBuilder = new StringBuilder();
      String str;
      while ((str = bufferedReader.readLine()) != null)
        stringBuilder.append(str + "\n"); 
      paramJTextArea.setText(stringBuilder.toString());
    } catch (Exception exception) {
      this.sap.reportException(exception);
    } catch (OutOfMemoryError outOfMemoryError) {
      this.sap.reportException(outOfMemoryError);
    } 
  }
  
  private void saveTerms(String paramString, JTextArea paramJTextArea) {
    try {
      StringTokenizer stringTokenizer;
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(paramString));
      String str = paramJTextArea.getText();
      if (str.indexOf("\r\n") > -1) {
        stringTokenizer = new StringTokenizer(str, "\r\n");
      } else {
        stringTokenizer = new StringTokenizer(str, "\n");
      } 
      while (stringTokenizer.hasMoreTokens()) {
        String str1 = stringTokenizer.nextToken();
        bufferedWriter.write(str1, 0, str1.length());
        bufferedWriter.newLine();
      } 
      bufferedWriter.close();
    } catch (Exception exception) {
      this.sap.reportException(exception);
    } 
  }
  
  private void depthSearchDialog() {
    JDialog jDialog = new JDialog((JFrame)SwingUtilities.windowForComponent(this), "generate arin depth search query");
    jDialog.getContentPane().add(new GenDepthInputdialog(new GDPostCall(jDialog)));
    jDialog.pack();
    jDialog.setVisible(true);
  }
  
  private void iplistDialog(boolean paramBoolean) {
    JDialog jDialog = new JDialog((JFrame)SwingUtilities.windowForComponent(this), "generate IP list");
    jDialog.getContentPane().add(new RangeIpInputdialog(new RIPostCall(jDialog, paramBoolean)));
    jDialog.pack();
    jDialog.setVisible(true);
  }
  
  private void iplistLookup() {
    Vector<IpRangeDescr> vector = new Vector();
    Vector<String> vector1 = getQuery();
    for (String str : vector1) {
      long l = ipv4.fromString(str.trim());
      vector.add(new IpRangeDescr(l, l, "", ""));
    } 
    this.sap.reportStatus("looking up hostnames");
    try {
      Thread thread = new Thread((Runnable)new RDnsThread(vector));
      thread.start();
    } catch (Exception exception) {
      this.sap.reportException(exception);
    } 
  }
  
  private void walkAndUpdateDescription() {
    final MyInputDialog sdd = new MyInputDialog((JFrame)SwingUtilities.windowForComponent(this), "Update", "Enter new description");
    PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent param1PropertyChangeEvent) {
          try {
            String str = (String)sdd.optionPane.getValue();
            if (str.equals(sdd.btn1s)) {
              IRD_ChangeDescription iRD_ChangeDescription = new IRD_ChangeDescription(sdd.inputField.getText());
              WBLGPanel.this.resultTable.walkSelectedEntries((IRD_Consumer)iRD_ChangeDescription);
            } 
          } catch (Exception exception) {}
          sdd.setVisible(false);
        }
      };
    myInputDialog.optionPane.addPropertyChangeListener(propertyChangeListener);
  }
  
  private void walkAndClip(String paramString) {
    try {
      ClipHelper.Consumer consumer = new ClipHelper.Consumer(paramString);
      this.resultTable.walkSelectedEntries((IRD_Consumer)consumer);
      consumer.finish();
    } catch (OutOfMemoryError outOfMemoryError) {
      int i = JOptionPane.showConfirmDialog(this, "Insufficient memory to perform copy operation!\nWould you like to save to a file instead?", outOfMemoryError.toString(), 0, 0);
      if (i == 0)
        walkAndSave(paramString); 
    } 
  }
  
  private void walkAndSave(final String what) {
    JFileChooser jFileChooser = openChooser(getFileDirectory("query_data_dir"), (FileFilter)null);
    if (jFileChooser.showDialog(this, "Save") == 0) {
      this.config.setProperty("query_data_dir", jFileChooser.getSelectedFile().getParent());
      final String path = jFileChooser.getSelectedFile().getPath();
      Thread thread = new Thread(new Runnable() {
            public void run() {
              SaveFileConsumer saveFileConsumer = new SaveFileConsumer(path, what);
              WBLGPanel.this.sap.reportProgress(0);
              WBLGPanel.this.sap.reportStatus("Saving to file...");
              WBLGPanel.this.resultTable.walkSelectedEntries((IRD_Consumer)saveFileConsumer);
              saveFileConsumer.finish();
              WBLGPanel.this.sap.reportStatus("done");
            }
          });
      thread.start();
    } 
  }
  
  private JFileChooser openChooser(String paramString, FileFilter paramFileFilter) {
    JFileChooser jFileChooser = new JFileChooser(paramString);
    if (paramFileFilter != null)
      jFileChooser.addChoosableFileFilter(paramFileFilter); 
    return jFileChooser;
  }
  
  private void showAbout() {
    String str = "Version: " + ProjectStats.version + "\n" + ProjectStats.linesOfCode + " lines of code in " + ProjectStats.numFiles + " files";
    JOptionPane.showMessageDialog(this, str, "About this program", 1);
  }
  
  private void showHelp() {
    int i = this.tabbed.indexOfTab("help");
    if (i != -1) {
      this.tabbed.setSelectedIndex(i);
      return;
    } 
    try {
      URL uRL = getClass().getResource("/wblg_help.html");
      JPanel jPanel = new JPanel();
      jPanel.setLayout(new GridLayout(1, 1));
      this.jep = new JEditorPane(uRL);
      this.jep.addHyperlinkListener((HyperlinkListener)new HtmlHelpListener());
      this.jep.setEditable(false);
      JScrollPane jScrollPane = new JScrollPane(this.jep);
      jPanel.add(jScrollPane);
      this.tabbed.addTab("help", jPanel);
      this.tabbed.setSelectedIndex(this.tabbed.indexOfTab("help"));
    } catch (Exception exception) {
      this.sap.reportException(exception);
    } 
  }
  
  public JMenuBar createMenuBar() {
    JMenuBar jMenuBar = new JMenuBar();
    JMenu jMenu1 = new JMenu("File");
    jMenu1.add(createMenuItem("Convert DB->WBL", "convert_db"));
    jMenu1.add(new JSeparator());
    jMenu1.add(new JMenuItem(this.loadResults));
    jMenu1.add(new JMenuItem(this.storeResults));
    jMenu1.add(new JSeparator());
    jMenu1.add(createMenuItem("Exit", "exit"));
    jMenuBar.add(jMenu1);
    JMenu jMenu2 = new JMenu("Tools");
    jMenuBar.add(jMenu2);
    this.generateDepthMItem = createMenuItem("generate 'depth' search", "gen_depth_search");
    this.generateIPListMItem = createMenuItem("generate IP list", "gen_iplist");
    jMenu2.add(this.generateDepthMItem);
    jMenu2.add(this.generateIPListMItem);
    jMenu2.add(createMenuItem("generate IP list & check hostname", "gen_iplist_res"));
    JMenu jMenu3 = new JMenu("View");
    JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("toolbar", this.config.getPropBool("toolbar", "true"));
    jCheckBoxMenuItem.setActionCommand("toggle_toolbar");
    jCheckBoxMenuItem.addActionListener(this.listener);
    jMenu3.add(jCheckBoxMenuItem);
    jCheckBoxMenuItem = new JCheckBoxMenuItem("list check", this.config.getPropBool("list_check", "false"));
    jCheckBoxMenuItem.setActionCommand("toggle_list_check");
    jCheckBoxMenuItem.addActionListener(this.listener);
    jMenu3.add(jCheckBoxMenuItem);
    jMenuBar.add(jMenu3);
    jMenuBar.add(new JSeparator());
    JMenu jMenu4 = new JMenu("Help");
    jMenu4.add(createMenuItem("About", "show_about"));
    jMenu4.add(createMenuItem("Documentation", "show_help"));
    jMenuBar.add(jMenu4);
    return jMenuBar;
  }
  
  private JMenuItem createMenuItem(String paramString1, String paramString2) {
    JMenuItem jMenuItem = new JMenuItem(paramString1);
    jMenuItem.setActionCommand(paramString2);
    jMenuItem.addActionListener(this.listener);
    return jMenuItem;
  }
  
  public JPopupMenu createResultsPopup() {
    JPopupMenu jPopupMenu = new JPopupMenu();
    try {
      JMenu jMenu1 = new JMenu("copy");
      jMenu1.add(createMenuItem("description", "cp_descr"));
      jMenu1.add(createMenuItem("entry", "cp_entry"));
      jMenu1.add(createMenuItem("netname", "cp_netname"));
      jMenu1.add(createMenuItem("range", "cp_range"));
      jMenu1.add(createMenuItem("start IP", "cp_ip"));
      jMenu1.add(createMenuItem("end IP", "cp_eip"));
      jPopupMenu.add(jMenu1);
      jPopupMenu.add(createMenuItem("delete", "rm_selected"));
      JMenu jMenu2 = new JMenu("mark");
      jMenu2.add(createMenuItem("set", "mark_selected"));
      jMenu2.add(createMenuItem("unset", "unmark_selected"));
      jMenu2.add(createMenuItem("toggle", "togglemark_selected"));
      jPopupMenu.add(jMenu2);
      jPopupMenu.add(createMenuItem("reset description", "reset_descr"));
      jPopupMenu.add(createMenuItem("reset highlight", "reset_high"));
      JMenu jMenu3 = new JMenu("save");
      jMenu3.add(createMenuItem("description", "save_descr"));
      jMenu3.add(createMenuItem("entry", "save_entry"));
      jMenu3.add(createMenuItem("netname", "save_netname"));
      jMenu3.add(createMenuItem("range", "save_range"));
      jMenu3.add(createMenuItem("start IP", "save_ip"));
      jMenu3.add(createMenuItem("end IP", "save_eip"));
      jPopupMenu.add(jMenu3);
      JMenu jMenu4 = new JMenu("select");
      jMenu4.add(createMenuItem("all", "res_select_all"));
      jMenu4.add(createMenuItem("blocked", "res_select_blocked"));
      jMenu4.add(createMenuItem("not blocked", "res_select_nblocked"));
      jMenu4.add(createMenuItem("highlighted", "res_select_highlighted"));
      jMenu4.add(createMenuItem("marked", "res_select_marked"));
      jPopupMenu.add(jMenu4);
      jPopupMenu.add(createMenuItem("set new description", "set_descr"));
      jPopupMenu.add(new JPopupMenu.Separator());
      XMLMenu xMLMenu = new XMLMenu(jPopupMenu);
    } catch (Exception exception) {
      this.sap.reportException(exception);
    } 
    return jPopupMenu;
  }
  
  private JPopupMenu createEditPopup(JTextArea paramJTextArea) {
    JPopupMenu jPopupMenu = new JPopupMenu();
    ActionMap actionMap = paramJTextArea.getActionMap();
    jPopupMenu.add(new JMenuItem(actionMap.get("Undo")));
    jPopupMenu.add(new JMenuItem(actionMap.get("Redo")));
    jPopupMenu.add(new JPopupMenu.Separator());
    jPopupMenu.add(new JMenuItem(actionMap.get("Cut")));
    jPopupMenu.add(new JMenuItem(actionMap.get("Copy")));
    jPopupMenu.add(new JMenuItem(actionMap.get("Paste")));
    jPopupMenu.add(new JMenuItem(actionMap.get("Delete")));
    jPopupMenu.add(new JPopupMenu.Separator());
    jPopupMenu.add(new JMenuItem(actionMap.get("Select all")));
    return jPopupMenu;
  }
  
  private JPanel createToolBar() {
    JPanel jPanel1 = new JPanel(new BorderLayout());
    JPanel jPanel2 = new JPanel();
    jPanel1.add(jPanel2, "West");
    jPanel2.add(makeToolButton("/reload.png", "update_blocked", "Check results against active blocklists", 85));
    jPanel2.add(makeToolButton("/editcut.png", "cp_marked", "Copy marked entries to clipboard", 67));
    JButton jButton1 = makeToolButton("/find.png", "sub_search", "<html>left-click: Search in results<br>right-click: search next</html>", 70);
    jButton1.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent param1MouseEvent) {
            if (SwingUtilities.isRightMouseButton(param1MouseEvent)) {
              JButton jButton = (JButton)param1MouseEvent.getSource();
              jButton.setActionCommand("subsub_search");
              jButton.doClick();
              jButton.setActionCommand("sub_search");
            } 
          }
        });
    jPanel2.add(jButton1);
    JButton jButton2 = makeToolButton("/clear.png", "rm_blocked", "<html>left-click: Remove blocklisted entries <font size=\"1\"> ALT-T</font><br>right-click: Remove highlighted entries</html>", 84);
    jButton2.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent param1MouseEvent) {
            if (SwingUtilities.isRightMouseButton(param1MouseEvent)) {
              JButton jButton = (JButton)param1MouseEvent.getSource();
              jButton.setActionCommand("rm_highlighted");
              jButton.doClick();
              jButton.setActionCommand("rm_blocked");
            } 
          }
        });
    jPanel2.add(jButton2);
    jPanel2.add(makeToolButton("/convert.png", "convert_db", "Convert db -> wbl", 73));
    jPanel2.add(makeToolButton("/stop.png", "abort_search", "Abort running search", 90));
    return jPanel1;
  }
  
  private JButton makeToolButton(String paramString1, String paramString2, String paramString3, int paramInt) {
    JButton jButton = new JButton();
    jButton.setActionCommand(paramString2);
    jButton.setToolTipText(paramString3);
    URL uRL = WBLGPanel.class.getResource(paramString1);
    jButton.setIcon(new ImageIcon(uRL));
    jButton.addActionListener(this.listener);
    Dimension dimension = new Dimension(40, 32);
    jButton.setPreferredSize(dimension);
    jButton.setSize(dimension);
    jButton.setMnemonic(paramInt);
    return jButton;
  }
  
  private void toggleToolbar() {
    boolean bool = this.config.getPropBool("toolbar", "true");
    if (bool) {
      bool = false;
    } else {
      bool = true;
    } 
    this.config.setProperty("toolbar", Boolean.toString(bool));
    if (bool) {
      this.toolPanel = createToolBar();
      this.resultPanel.add(this.toolPanel, "First");
    } else {
      this.resultPanel.remove(this.toolPanel);
    } 
  }
  
  private void addUndoSupport(JTextArea paramJTextArea) {
    Document document = paramJTextArea.getDocument();
    document.addUndoableEditListener(new UndoableEditListener() {
          public void undoableEditHappened(UndoableEditEvent param1UndoableEditEvent) {
            WBLGPanel.this.undoer.addEdit(param1UndoableEditEvent.getEdit());
          }
        });
    document.addUndoableEditListener(new UndoableEditListener() {
          public void undoableEditHappened(UndoableEditEvent param1UndoableEditEvent) {
            WBLGPanel.this.undoer.addEdit(param1UndoableEditEvent.getEdit());
          }
        });
    paramJTextArea.getActionMap().put("Undo", new AbstractAction("Undo") {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            try {
              if (WBLGPanel.this.undoer.canUndo())
                WBLGPanel.this.undoer.undo(); 
            } catch (CannotUndoException cannotUndoException) {
              WBLGPanel.this.sap.reportException(cannotUndoException);
            } 
          }
        });
    paramJTextArea.getActionMap().put("Redo", new AbstractAction("Redo") {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            try {
              if (WBLGPanel.this.undoer.canRedo())
                WBLGPanel.this.undoer.redo(); 
            } catch (CannotRedoException cannotRedoException) {
              WBLGPanel.this.sap.reportException(cannotRedoException);
            } 
          }
        });
  }
  
  private void createListCheck() {
    this.listCheckPanel = new ListCheckPanel(this.listener);
    this.tabbed.insertTab("list check", null, this.listCheckPanel, null, 4);
    final JTextArea lca = this.listCheckPanel.getArea();
    addUndoSupport(jTextArea);
    jTextArea.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent param1MouseEvent) {
            int i = param1MouseEvent.getModifiers();
            if ((i & 0x4) != 0) {
              JPopupMenu jPopupMenu = WBLGPanel.this.createEditPopup(lca);
              jPopupMenu.show(lca, param1MouseEvent.getX(), param1MouseEvent.getY());
            } 
          }
        });
    delegateAction(jTextArea, "selectAll", "Select all", (Icon)null);
    delegateAction(jTextArea, "cut", "Cut", (Icon)null);
    delegateAction(jTextArea, "copy", "Copy", (Icon)null);
    delegateAction(jTextArea, "paste", "Paste", (Icon)null);
  }
  
  private void toggleListCheck() {
    boolean bool = this.config.getPropBool("list_check", "false");
    if (bool) {
      bool = false;
    } else {
      bool = true;
    } 
    this.config.setProperty("list_check", Boolean.toString(bool));
    if (bool) {
      createListCheck();
      this.tabbed.setSelectedIndex(this.tabbed.indexOfTab("list check"));
    } else {
      int i = this.tabbed.indexOfTab("help");
      if (i > -1)
        this.tabbed.setSelectedIndex(0); 
      int j = this.tabbed.indexOfTab("list check");
      if (j > -1)
        this.tabbed.removeTabAt(j); 
      if (i > -1)
        showHelp(); 
      this.listCheckPanel = null;
    } 
  }
  
  public void saveConfig() {
    JFrame jFrame = (JFrame)SwingUtilities.windowForComponent(this);
    Dimension dimension = jFrame.getSize();
    Integer integer1 = Integer.valueOf((int)dimension.getWidth());
    Integer integer2 = Integer.valueOf((int)dimension.getHeight());
    String str = integer1.toString() + "x" + integer2.toString() + ":" + Integer.toString(jFrame.getExtendedState());
    this.config.setProperty("screen", str);
    FilesTableModel filesTableModel = (FilesTableModel)this.dbFilesPanel.getDataTable().getModel();
    this.config.setProperty("db_files", filesTableModel.toString());
    filesTableModel = (FilesTableModel)this.blockFilesPanel.getDataTable().getModel();
    this.config.setProperty("blocklist_files", filesTableModel.toString());
    filesTableModel = this.onlineList.getModel();
    this.config.setProperty("online_search", filesTableModel.toString());
    this.config.saveToFile();
  }
  
  public void screenSetup() {
    String str = this.config.getProp("screen", "");
    JFrame jFrame = (JFrame)SwingUtilities.windowForComponent(this);
    int i = str.indexOf('x');
    int j = str.indexOf(':');
    if (i > -1 && j > -1) {
      Dimension dimension = new Dimension(Integer.parseInt(str.substring(0, i)), Integer.parseInt(str.substring(i + 1, j)));
      jFrame.setSize(dimension);
      int k = Integer.parseInt(str.substring(j + 1));
      jFrame.setExtendedState(k);
    } else {
      jFrame.pack();
    } 
  }
  
  class ListTableModelListener implements TableModelListener {
    public void tableChanged(TableModelEvent param1TableModelEvent) {
      WBLGPanel.this.dynamicDbButtons();
    }
  }
  
  public class Listener implements ActionListener {
    public void actionPerformed(ActionEvent param1ActionEvent) {
      String str = param1ActionEvent.getActionCommand();
      if (str.equals("search")) {
        WBLGPanel.this.runSearch(false);
      } else if (str.equals("check_list")) {
        WBLGPanel.this.runListCheck();
      } else if (str.equals("search_range")) {
        WBLGPanel.this.runSearch(true);
      } else if (str.equals("load_search")) {
        WBLGPanel.this.searchChooserLoading(true);
      } else if (str.equals("save_search")) {
        WBLGPanel.this.searchChooserLoading(false);
      } else if (str.equals("clear_search")) {
        WBLGPanel.this.searchArea.setText("");
      } else if (str.equals("load_listcheck")) {
        WBLGPanel.this.listCheckChooserLoading(true);
      } else if (str.equals("save_listcheck")) {
        WBLGPanel.this.listCheckChooserLoading(false);
      } else if (str.equals("abort_search")) {
        WBLGPanel.this.wbl.abort();
      } else if (str.equals("sub_search")) {
        new SubSearchDialog((JFrame)SwingUtilities.windowForComponent(WBLGPanel.this), false);
      } else if (str.equals("subsub_search")) {
        new SubSearchDialog((JFrame)SwingUtilities.windowForComponent(WBLGPanel.this), true);
      } else if (str.equals("clear_highlight")) {
        WBLGPanel.this.resultTable.resetSubSearch((byte)0);
      } else if (str.equals("scroll_top")) {
        WBLGPanel.this.resultTable.scrollToResult(0);
      } else if (str.equals("scroll_bottom")) {
        WBLGPanel.this.resultTable.scrollToResult(WBLGPanel.this.resultTable.getRowCount() - 1);
      } else if (str.equals("convert_db")) {
        WBLGPanel.this.convertFiles();
      } else if (str.equals("show_help")) {
        WBLGPanel.this.showHelp();
      } else if (str.equals("show_about")) {
        WBLGPanel.this.showAbout();
      } else if (str.equals("rm_blocked")) {
        WBLGPanel.this.resultTable.deleteEntries((IRDFilter)new BlockedResultFilter(1));
      } else if (str.equals("rm_highlighted")) {
        WBLGPanel.this.resultTable.deleteEntries((IRDFilter)new HighlightedResultFilter());
      } else if (str.equals("rm_selected")) {
        WBLGPanel.this.resultTable.deleteSelectedEntries();
      } else if (str.equals("mark_selected")) {
        WBLGPanel.this.resultTable.walkSelectedEntries((IRD_Consumer)new MarkEntryConsumer((byte)0));
      } else if (str.equals("unmark_selected")) {
        WBLGPanel.this.resultTable.walkSelectedEntries((IRD_Consumer)new MarkEntryConsumer((byte)1));
      } else if (str.equals("togglemark_selected")) {
        WBLGPanel.this.resultTable.walkSelectedEntries((IRD_Consumer)new MarkEntryConsumer((byte)2));
      } else if (str.equals("reset_descr")) {
        WBLGPanel.this.resultTable.walkSelectedEntries((IRD_Consumer)new DescrResetConsumer());
      } else if (str.equals("reset_high")) {
        WBLGPanel.this.resultTable.visitSelectedEntries((IRD_Visitor)new IRD_HighlightRemover());
      } else if (str.equals("cp_marked")) {
        ClipHelper.Consumer consumer = new ClipHelper.Consumer("toBlocklistEntry");
        WBLGPanel.this.resultTable.walkEntries((IRDFilter)new MarkedResultFilter(), (IRD_Consumer)consumer);
        consumer.finish();
      } else if (str.equals("set_descr")) {
        WBLGPanel.this.walkAndUpdateDescription();
      } else if (str.equals("cp_descr")) {
        WBLGPanel.this.walkAndClip("getBestMatch");
      } else if (str.equals("cp_ip")) {
        WBLGPanel.this.walkAndClip("getStartString");
      } else if (str.equals("cp_eip")) {
        WBLGPanel.this.walkAndClip("getEndString");
      } else if (str.equals("cp_range")) {
        WBLGPanel.this.walkAndClip("getRangeString");
      } else if (str.equals("cp_netname")) {
        WBLGPanel.this.walkAndClip("getNetName");
      } else if (str.equals("cp_entry")) {
        WBLGPanel.this.walkAndClip("toBlocklistEntry");
      } else if (str.equals("save_descr")) {
        WBLGPanel.this.walkAndSave("getBestMatch");
      } else if (str.equals("save_ip")) {
        WBLGPanel.this.walkAndSave("getStartString");
      } else if (str.equals("save_eip")) {
        WBLGPanel.this.walkAndSave("getEndString");
      } else if (str.equals("save_range")) {
        WBLGPanel.this.walkAndSave("getRangeString");
      } else if (str.equals("save_netname")) {
        WBLGPanel.this.walkAndSave("getNetName");
      } else if (str.equals("save_entry")) {
        WBLGPanel.this.walkAndSave("toBlocklistEntry");
      } else if (str.equals("update_blocked")) {
        int i = Configuration.getInstance().getPropInt("thread_priority", Integer.toString(5));
        Thread thread = new Thread((Runnable)new BlockCheckThread());
        thread.setPriority(i);
        thread.start();
      } else if (str.equals("toggle_toolbar")) {
        WBLGPanel.this.toggleToolbar();
      } else if (str.equals("toggle_list_check")) {
        WBLGPanel.this.toggleListCheck();
      } else if (str.equals("res_select_all")) {
        WBLGPanel.this.resultTable.selectAll();
      } else if (str.equals("res_select_blocked")) {
        WBLGPanel.this.resultTable.selectEntries((IRDFilter)new BlockedResultFilter(1));
      } else if (str.equals("res_select_nblocked")) {
        WBLGPanel.this.resultTable.selectEntries((IRDFilter)new BlockedResultFilter(1, true));
      } else if (str.equals("res_select_highlighted")) {
        WBLGPanel.this.resultTable.selectEntries((IRDFilter)new HighlightedResultFilter());
      } else if (str.equals("res_select_marked")) {
        WBLGPanel.this.resultTable.selectEntries((IRDFilter)new MarkedResultFilter());
      } else if (str.equals("cidr_asn_d")) {
        WBLGPanel.this.runCidrAsnSearch(true);
      } else if (str.equals("cidr_asn_n")) {
        WBLGPanel.this.runCidrAsnSearch(false);
      } else if (str.equals("gen_depth_search")) {
        WBLGPanel.this.depthSearchDialog();
      } else if (str.equals("gen_iplist")) {
        WBLGPanel.this.iplistDialog(true);
      } else if (str.equals("gen_iplist_res")) {
        WBLGPanel.this.iplistDialog(false);
      } else if (str.equals("iplist_rdns")) {
        WBLGPanel.this.iplistLookup();
      } else if (str.equals("exit")) {
        WBLGPanel.this.saveConfig();
        System.exit(0);
      } else {
        WBLGPanel.this.sap.reportError("Unhandled command: " + str);
      } 
    }
  }
  
  class ShowTabHelper implements ActionListener {
    public void actionPerformed(ActionEvent param1ActionEvent) {
      String str = param1ActionEvent.getActionCommand();
      int i = WBLGPanel.this.tabbed.indexOfTab(str);
      if (i != -1)
        WBLGPanel.this.tabbed.setSelectedIndex(i); 
    }
  }
  
  class HelpVisibleHandler implements ChangeListener {
    public void stateChanged(ChangeEvent param1ChangeEvent) {
      int i = WBLGPanel.this.tabbed.indexOfTab("help");
      if (i != -1 && WBLGPanel.this.tabbed.getSelectedIndex() != i)
        WBLGPanel.this.tabbed.removeTabAt(i); 
      i = WBLGPanel.this.tabbed.getSelectedIndex();
      if (i == 1) {
        WBLGPanel.this.generateIPListMItem.setEnabled(true);
        WBLGPanel.this.generateDepthMItem.setEnabled(true);
      } else {
        WBLGPanel.this.generateIPListMItem.setEnabled(false);
        WBLGPanel.this.generateDepthMItem.setEnabled(false);
      } 
    }
  }
  
  public class RIPostCall {
    private final JDialog jp;
    
    private boolean toText;
    
    public RIPostCall(JDialog param1JDialog, boolean param1Boolean) {
      this.jp = param1JDialog;
      this.toText = param1Boolean;
    }
    
    public void close() {
      this.jp.setVisible(false);
    }
    
    public void make(long param1Long1, long param1Long2, int param1Int) {
      close();
      if (this.toText) {
        makeToText(param1Long1, param1Long2, param1Int);
      } else {
        makeToResults(param1Long1, param1Long2, param1Int);
      } 
    }
    
    private void makeToText(long param1Long1, long param1Long2, int param1Int) {
      long l = param1Long1;
      String str = System.getProperty("line.separator");
      while (l <= param1Long2) {
        long l1 = l & 0xFFL;
        if (l1 > 0L && l1 < 255L)
          WBLGPanel.this.searchArea.append(ipv4.fromLong(l) + str); 
        l += param1Int;
      } 
    }
    
    private void makeToResults(long param1Long1, long param1Long2, int param1Int) {
      long l = param1Long1;
      BLTableModel bLTableModel = (BLTableModel)WBLGPanel.this.resultTable.getModel();
      bLTableModel.clear();
      Vector<IpRangeDescr> vector = new Vector();
      while (l <= param1Long2) {
        long l1 = l & 0xFFL;
        if (l1 > 0L && l1 < 255L)
          vector.add(new IpRangeDescr(l, l, "", "")); 
        l += param1Int;
      } 
      WBLGPanel.this.sap.reportStatus("looking up hostnames");
      try {
        Thread thread = new Thread((Runnable)new RDnsThread(vector));
        thread.start();
      } catch (Exception exception) {
        WBLGPanel.this.sap.reportException(exception);
      } 
    }
  }
  
  public class GDPostCall {
    private final JDialog jp;
    
    public GDPostCall(JDialog param1JDialog) {
      this.jp = param1JDialog;
    }
    
    public void close() {
      this.jp.setVisible(false);
    }
    
    public void make(int param1Int, String param1String1, String param1String2) {
      close();
      String str1 = System.getProperty("line.separator");
      String str2 = param1String1;
      for (byte b = 0; b < param1Int; b++) {
        WBLGPanel.this.searchArea.append(str2 + param1String2 + str1);
        str2 = str2 + param1String1;
      } 
    }
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\gui\WBLGPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */