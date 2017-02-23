/**
*Author: Divya Srivastava
* Creation Date: 24-02-2017
* Copyright: Divya Srivastava
* Description: InvalidMessageException
* 
*     ----------------------------------------------------------------------------------------------------------------
* Revision:  Version Last Revision Date   Name 					 Description 
*     ----------------------------------------------------------------------------------------------------------------
* @        1.0          24/02/2017       Divya Srivastava         InvalidMessageException is custom exception thrown for invalid inputs
*
*/
package com.processmessage.exceptions;

public class InvalidMessageException extends RuntimeException {

	
	/**
	 * 
	 */
	public InvalidMessageException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 */
	public InvalidMessageException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public InvalidMessageException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public InvalidMessageException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}



	@Override
    public String toString() {
          return "Exception Occured :: " + super.toString() + "::"
		+ getMessage();

    }

    @Override
    public String getMessage() {
        return "Exception Occured :: " +super.getMessage();
    }
}
