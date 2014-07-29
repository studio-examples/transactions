package org.exceptions;

public class MyException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1123544523432L;
	String mistake;
	
	public MyException() {
		super();
		mistake = "test";
	}
	
	public MyException(String err) {
		super(err);
		mistake = err;
	}
	
	 public String getError()
	  {
	    return mistake;
	  }

}