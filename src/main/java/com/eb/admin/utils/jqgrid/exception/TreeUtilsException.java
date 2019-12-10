/**  
* @Title: TreeUtilsException.java
* @Package cn.wywk.mypay.comm.util.exception
* @Description: 
* @author kevin
* @date 2014-4-9 下午4:26:02
* @version V1.0  
*/
package com.eb.admin.utils.jqgrid.exception;

/**
 * @ClassName: TreeUtilsException
 * @Description: TreeUtilsException
 * @author kevin
 * @date 2014-4-9 下午4:26:02
 *
 */
public class TreeUtilsException extends Exception {

	private static final long serialVersionUID = 1787789132268338188L;

	public TreeUtilsException() {
        super();
    }
    
    public TreeUtilsException(String msg) {
        super(msg);
    }
    
    public TreeUtilsException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public TreeUtilsException(Throwable cause) {
        super(cause);
    }
}
