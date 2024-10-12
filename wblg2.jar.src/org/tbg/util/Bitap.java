package org.tbg.util;

import java.util.HashMap;
import java.util.Map;

public class Bitap {
  private static final int MAXBITS = 32;
  
  private static final float BALANCE = 0.5F;
  
  private static float balance = 0.5F;
  
  private static final float THRESHOLD = 0.5F;
  
  private static float threshold = 0.5F;
  
  private static final int MINLENGTH = 100;
  
  private static int minLength = 100;
  
  private static final int MAXLENGTH = 1000;
  
  private static int maxLength = 1000;
  
  private String text;
  
  private String pattern;
  
  private int loc;
  
  private int scoreTextLength;
  
  private Map alphabet;
  
  public Bitap(String paramString1, String paramString2, int paramInt) {
    this.text = paramString1.toLowerCase();
    this.pattern = paramString2.toLowerCase();
    this.loc = paramInt;
    this.alphabet = new HashMap<>();
  }
  
  public Bitap(String paramString1, String paramString2) {
    this.text = paramString1;
    this.pattern = paramString2;
    this.loc = 0;
    this.alphabet = new HashMap<>();
    alphabet();
  }
  
  public int maxPatternLength() {
    return 32;
  }
  
  public int locate(int paramInt) {
    this.loc = paramInt;
    if (this.text.length() == 0)
      return -1; 
    this.scoreTextLength = Math.max(this.text.length(), minLength);
    this.scoreTextLength = Math.min(this.scoreTextLength, maxLength);
    double d = threshold;
    int i = this.text.indexOf(this.pattern, paramInt);
    if (i != -1) {
      d = Math.min(bitapScore(0, i), d);
      return i;
    } 
    i = this.text.lastIndexOf(this.pattern, paramInt + this.pattern.length());
    if (i != -1)
      d = Math.min(bitapScore(0, i), d); 
    int j = (int)Math.pow(2.0D, (this.pattern.length() - 1));
    i = -1;
    int k = Math.max(paramInt + paramInt, this.text.length());
    int[] arrayOfInt = new int[0];
    for (byte b = 0; b < this.pattern.length(); b++) {
      int[] arrayOfInt1 = new int[this.text.length()];
      int m = paramInt;
      int n;
      for (n = k; m < n; n = (k - m) / 2 + m) {
        if (bitapScore(b, n) < d) {
          m = n;
        } else {
          k = n;
        } 
      } 
      k = n;
      int i1 = Math.max(0, paramInt - n - paramInt - 1);
      int i2 = Math.min(this.text.length() - 1, this.pattern.length() + n);
      if (this.text.charAt(i2) == this.pattern.charAt(this.pattern.length() - 1)) {
        arrayOfInt1[i2] = (int)Math.pow(2.0D, (b + 1)) - 1;
      } else {
        arrayOfInt1[i2] = (int)Math.pow(2.0D, b) - 1;
      } 
      for (int i3 = i2 - 1; i3 >= i1; i3--) {
        Character character = new Character(this.text.charAt(i3));
        boolean bool = this.alphabet.containsKey(character) ? ((Integer)this.alphabet.get(character)).intValue() : false;
        if (b == 0) {
          arrayOfInt1[i3] = (arrayOfInt1[i3 + 1] << 1 | 0x1) & bool;
        } else {
          arrayOfInt1[i3] = (arrayOfInt1[i3 + 1] << 1 | 0x1) & bool | arrayOfInt[i3 + 1] << 1 | 0x1 | arrayOfInt[i3] << 1 | 0x1 | arrayOfInt[i3 + 1];
        } 
        if ((arrayOfInt1[i3] & j) != 0) {
          double d1 = bitapScore(b, i3);
          if (d1 <= d) {
            d = d1;
            i = i3;
            if (i3 > paramInt) {
              i1 = Math.max(0, paramInt - i3 - paramInt);
            } else {
              break;
            } 
          } 
        } 
      } 
      if (bitapScore(b + 1, paramInt) > d)
        break; 
      arrayOfInt = arrayOfInt1;
    } 
    return i;
  }
  
  protected Map getAlphabet() {
    return this.alphabet;
  }
  
  private double bitapScore(int paramInt1, int paramInt2) {
    int i = Math.abs(this.loc - paramInt2);
    return (paramInt1 / this.pattern.length() / balance) + (i / this.scoreTextLength) / (1.0D - balance);
  }
  
  protected void alphabet() {
    int i = this.pattern.length();
    assert i <= 32 : "Pattern too long for this application.";
    for (byte b = 0; b < i; b++) {
      Character character = new Character(this.pattern.charAt(b));
      Integer integer = (Integer)this.alphabet.get(character);
      int j = (integer == null) ? 0 : integer.intValue();
      j |= (int)Math.pow(2.0D, (i - b - 1));
      this.alphabet.put(character, new Integer(j));
    } 
  }
  
  public static void setBalance(float paramFloat) {
    balance = paramFloat;
  }
  
  public static void setThreshold(float paramFloat) {
    threshold = paramFloat;
  }
  
  public static void setMinLength(int paramInt) {
    minLength = paramInt;
  }
  
  public static void setMaxLength(int paramInt) {
    maxLength = paramInt;
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tb\\util\Bitap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */