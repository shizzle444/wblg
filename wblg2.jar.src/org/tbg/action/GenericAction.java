package org.tbg.action;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.tbg.gui.WBLGPanel;
import org.tbg.util.SAPReport;

public class GenericAction extends AbstractAction {
  protected Method methodToCall;
  
  protected static final Class[] signature = new Class[] { ActionEvent.class };
  
  protected static final Class[] noType = new Class[0];
  
  protected Object target;
  
  protected Object[] params;
  
  public GenericAction(Object paramObject, String paramString1, String paramString2, Icon paramIcon) {
    this(paramObject, paramString1, paramString2, paramIcon, null);
  }
  
  public GenericAction(Object paramObject, String paramString1, String paramString2, Icon paramIcon, String paramString3) {
    super(paramString2, paramIcon);
    try {
      this.methodToCall = paramObject.getClass().getMethod(paramString1, noType);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    this.target = paramObject;
    if (paramString3 != null)
      putValue("ShortDescription", paramString3); 
  }
  
  private SAPReport fetchReporter() {
    return (SAPReport)(WBLGPanel.getInstance()).sap;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    try {
      this.methodToCall.invoke(this.target, new Object[0]);
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
      fetchReporter().reportException(illegalAccessException);
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
      Throwable throwable = invocationTargetException.getCause();
      if (throwable != null) {
        fetchReporter().reportException(throwable);
      } else {
        fetchReporter().reportException(invocationTargetException);
      } 
    } catch (OutOfMemoryError outOfMemoryError) {
      fetchReporter().reportException(outOfMemoryError);
    } catch (Exception exception) {
      fetchReporter().reportException(exception);
    } 
  }
}


/* Location:              C:\Users\shado\Desktop\WBLG chatgpt\wblg2.jar!\org\tbg\action\GenericAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */