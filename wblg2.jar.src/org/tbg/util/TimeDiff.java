package org.tbg.util;

import java.util.Date;

public class TimeDiff {
  private Date start = new Date();
  
  public long getDeltaMilliSeconds() {
    Date date = new Date();
    return date.getTime() - this.start.getTime();
  }
  
  public static String formatDelta(long paramLong) {
    String str;
    long l1 = paramLong / 1000L;
    long l2 = l1 / 60L;
    long l3 = l2 / 60L;
    int i = (int)l3 % 24;
    int j = (int)l2 % 24;
    int k = (int)l1 % 60;
    if (i > 0) {
      str = Integer.toString(i) + " h, " + Integer.toString(j) + " m, " + Integer.toString(k) + " s";
    } else if (j > 0) {
      str = Integer.toString(j) + " m, " + Integer.toString(k) + " s";
    } else {
      str = Integer.toString(k) + " s";
    } 
    return str;
  }
  
  public String getDeltaTime() {
    return formatDelta(getDeltaMilliSeconds());
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\TimeDiff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */