/**
*Author: Divya Srivastava
* Creation Date: 24-02-2017
* Copyright: Divya Srivastava
* Description: Message
* 
*     ----------------------------------------------------------------------------------------------------------------
* Revision:  Version Last Revision Date   Name 					 Description 
*     ----------------------------------------------------------------------------------------------------------------
* @        1.0          24/02/2017       Divya Srivastava         Message comprises of Sale details to be added or perform an operation on existing sales. 
*
*/
package com.processmessage.data;

public class Message {
	private String type;
	private Sale sale;
	private String salesOperation;
	
	public Message(){
		type=null;
		sale=null;
		salesOperation=null;
	}
	
	public Message(String type, Sale s, String operation) {
		this.type=type;
		this.sale=s;
		this.salesOperation=operation;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the sale
	 */
	public Sale getSale() {
		return sale;
	}

	/**
	 * @param sale the sale to set
	 */
	public void setSale(Sale sale) {
		this.sale = sale;
	}

	/**
	 * @return the salesOperation
	 */
	public String getSalesOperation() {
		return salesOperation;
	}

	/**
	 * @param salesOperation the salesOperation to set
	 */
	public void setSalesOperation(String salesOperation) {
		this.salesOperation = salesOperation;
	}

	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Message Type:" + getType());
        sb.append(", ");
        sb.append("Product type:" + getSale().getProductType());
        sb.append(", ");
        sb.append("Sale Value:" + getSale().getSaleValue());
        sb.append(", ");
        sb.append("Sales Count:" + getSale().getSalesCount());
        if(salesOperation!=null){
        sb.append(", ");
        sb.append("Operation:" + getSalesOperation());
        }
        return sb.toString();
 }



	
	
}
