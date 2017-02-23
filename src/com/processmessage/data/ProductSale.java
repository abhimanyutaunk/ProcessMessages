/**
*Author: Divya Srivastava
* Creation Date: 24-02-2017
* Copyright: Divya Srivastava
* Description: ProductSale
* 
*     ----------------------------------------------------------------------------------------------------------------
* Revision:  Version Last Revision Date   Name 					 Description 
*     ----------------------------------------------------------------------------------------------------------------
* @        1.0          24/02/2017       Divya Srivastava         ProductSale will keep track of Product's total sale count and value
*
*/
package com.processmessage.data;

public class ProductSale {

	private int totCount;
	private Float totValue;
	
	public ProductSale(){
		this.totCount = 0;
		this.totValue= 0f;
	}
	
	public ProductSale(int count, Float Value){
		this.totCount =count;
		this.totValue= Value;
	}
	
	/**
	 * @return the totCount
	 */
	public int getTotCount() {
		return totCount;
	}
	/**
	 * @param totCount the totCount to set
	 */
	public void setTotCount(int totCount) {
		this.totCount = totCount;
	}
	/**
	 * @return the tolValue
	 */
	public Float getTotValue() {
		return totValue;
	}
	/**
	 * @param tolValue the tolValue to set
	 */
	public void setTotValue(Float totValue) {
		this.totValue = totValue;
	}
	
	
}
