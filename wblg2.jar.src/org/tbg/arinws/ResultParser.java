package org.tbg.arinws;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilderFactory;
import org.tbg.ip.IpRangeDescr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ResultParser {
  private static final String rootNets = "nets";
  
  private static final String rootOrgs = "orgs";
  
  private static final String rootCustomers = "customers";
  
  private boolean limitExceeded = true;
  
  private Vector<IpRangeDescr> result = new Vector<>();
  
  private Map<String, String> orgHandle2Name = new HashMap<>();
  
  private Map<String, String> customerHandle2Name = new HashMap<>();
  
  public ResultParser(InputStream paramInputStream) throws Exception {
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(paramInputStream);
    Element element = document.getDocumentElement();
    String str = element.getTagName();
    checkLimitExceeded(element);
    if (str.equals("nets")) {
      parseNetEntries(element);
    } else if (str.equals("orgs")) {
      parseOrgEntries(element);
    } else if (str.equals("customers")) {
      parseCustomerEntries(element);
    } 
    paramInputStream.close();
  }
  
  private void checkLimitExceeded(Element paramElement) {
    NodeList nodeList = paramElement.getElementsByTagName("limitExceeded");
    if (nodeList.getLength() == 1) {
      String str = nodeList.item(0).getTextContent();
      if (str.equals("false"))
        this.limitExceeded = false; 
    } 
  }
  
  public boolean isLimitExceeded() {
    return this.limitExceeded;
  }
  
  public Vector<IpRangeDescr> getIRDResults() {
    return this.result;
  }
  
  private void parseOrgEntries(Element paramElement) {
    Node node = paramElement.getFirstChild();
    while (node != null) {
      if (node.getNodeName().equals("orgRef")) {
        Element element = (Element)node;
        this.orgHandle2Name.put(element.getAttribute("handle"), element.getAttribute("name"));
      } 
    } 
  }
  
  private void parseCustomerEntries(Element paramElement) {
    Node node = paramElement.getFirstChild();
    while (node != null) {
      if (node.getNodeName().equals("customerRef")) {
        Element element = (Element)node;
        this.customerHandle2Name.put(element.getAttribute("handle"), element.getAttribute("name"));
      } 
    } 
  }
  
  private void parseNetEntries(Element paramElement) {
    for (Node node = paramElement.getFirstChild(); node != null; node = node.getNextSibling()) {
      if (node.getNodeName().equals("netRef")) {
        Element element = (Element)node;
        this.result.add(new IpRangeDescr(element.getAttribute("startAddress"), element.getAttribute("endAddress"), element.getAttribute("name"), element.getAttribute("handle")));
      } 
    } 
  }
  
  public static void main(String[] paramArrayOfString) throws Exception {
    String str = "/tmp/sony-nets.xml";
    FileInputStream fileInputStream = new FileInputStream(str);
    ResultParser resultParser = new ResultParser(fileInputStream);
    Vector<IpRangeDescr> vector = resultParser.getIRDResults();
    for (IpRangeDescr ipRangeDescr : vector)
      System.out.println(ipRangeDescr); 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\arinws\ResultParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */