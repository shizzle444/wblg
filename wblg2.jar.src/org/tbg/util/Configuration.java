package org.tbg.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.tbg.exceptions.AlreadyRunningException;

public class Configuration extends Properties {
  private String file;
  
  private LockFile lock;
  
  private static Configuration instance = null;
  
  public Configuration(String paramString) throws IOException, AlreadyRunningException {
    loadFromFile(paramString);
    this.file = new String(paramString);
    boolean bool = Boolean.parseBoolean(getProp("single_instance", "true"));
    if (bool) {
      this.lock = new LockFile("wblg.lock");
    } else {
      this.lock = null;
    } 
    instance = this;
  }
  
  public static Configuration getInstance() {
    return instance;
  }
  
  void loadFromFile(String paramString) {
    try {
      load(new FileInputStream(paramString));
    } catch (Exception exception) {}
  }
  
  public String getProp(String paramString1, String paramString2) {
    String str = getProperty(paramString1, paramString2);
    setProperty(paramString1, str);
    return str;
  }
  
  public boolean getPropBool(String paramString1, String paramString2) {
    String str = getProp(paramString1, paramString2);
    return Boolean.parseBoolean(str);
  }
  
  public int getPropInt(String paramString1, String paramString2) {
    String str = getProp(paramString1, paramString2);
    return Integer.parseInt(str);
  }
  
  public void saveToFile() {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(this.file);
      store(fileOutputStream, "WhoisBLGui configuration");
      fileOutputStream.close();
      if (this.lock != null)
        this.lock.remove(); 
    } catch (Exception exception) {}
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\Configuration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */