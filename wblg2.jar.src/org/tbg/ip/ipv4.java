package org.tbg.ip;

import java.util.StringTokenizer;

public class ipv4 {
  public static long fromString(String paramString) {
    StringTokenizer stringTokenizer = new StringTokenizer(paramString, ".");
    long l1 = Short.parseShort(stringTokenizer.nextToken());
    long l2 = Short.parseShort(stringTokenizer.nextToken());
    long l3 = Short.parseShort(stringTokenizer.nextToken());
    long l4 = Short.parseShort(stringTokenizer.nextToken());
    return l1 * 256L * 256L * 256L + l2 * 256L * 256L + l3 * 256L + l4;
  }
  
  public static String fromLong(long paramLong) {
    long l1 = paramLong % 256L;
    paramLong -= l1;
    paramLong /= 256L;
    long l2 = paramLong % 256L;
    paramLong -= l2;
    paramLong /= 256L;
    long l3 = paramLong % 256L;
    paramLong -= l3;
    paramLong /= 256L;
    long l4 = paramLong % 256L;
    return Long.toString(l4).concat(".").concat(Long.toString(l3).concat(".")).concat(Long.toString(l2).concat(".")).concat(Long.toString(l1));
  }
  
  public static long numHosts(int paramInt) {
    return (long)Math.pow(2.0D, (32 - paramInt)) - 1L;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\ip\ipv4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */