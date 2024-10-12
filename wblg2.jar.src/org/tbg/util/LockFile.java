package org.tbg.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import org.tbg.exceptions.AlreadyRunningException;

public class LockFile {
  private FileOutputStream fos;
  
  private File f;
  
  private FileLock lock;
  
  public LockFile(String paramString) throws IOException, AlreadyRunningException {
    this.f = new File(paramString);
    this.fos = new FileOutputStream(this.f);
    this.lock = this.fos.getChannel().tryLock();
    if (this.lock == null)
      throw new AlreadyRunningException(); 
    this.f.deleteOnExit();
  }
  
  public void remove() {
    try {
      if (this.lock.isValid())
        this.lock.release(); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\LockFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */