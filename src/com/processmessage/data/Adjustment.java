/**
*Author: Divya Srivastava
* Creation Date: 24-02-2017
* Copyright: Divya Srivastava
* Description: Adjustment
* 
*     ----------------------------------------------------------------------------------------------------------------
* Revision:  Version Last Revision Date   Name 					 Description 
*     ----------------------------------------------------------------------------------------------------------------
* @        1.0          24/02/2017       Divya Srivastava         Adjustment keeps track of adjustments on Sales for a product done while performing operations
*
*/
package com.processmessage.data;

public class Adjustment {

	private String productType;
	private String operationApplied;
	private Float salesBeforeOperation;
	private Float salesAfterOperation;
	public Adjustment(String productType, String operationApplied,
			Float salesBeforeOperation, Float salesAfterOperation) {
		super();
		this.productType = productType;
		this.operationApplied = operationApplied;
		this.salesBeforeOperation = salesBeforeOperation;
		this.salesAfterOperation = salesAfterOperation;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * @return the operationApplied
	 */
	public String getOperationApplied() {
		return operationApplied;
	}
	/**
	 * @param operationApplied the operationApplied to set
	 */
	public void setOperationApplied(String operationApplied) {
		this.operationApplied = operationApplied;
	}
	/**
	 * @return the salesBeforeOperation
	 */
	public Float getSalesBeforeOperation() {
		return salesBeforeOperation;
	}
	/**
	 * @param salesBeforeOperation the salesBeforeOperation to set
	 */
	public void setSalesBeforeOperation(Float salesBeforeOperation) {
		this.salesBeforeOperation = salesBeforeOperation;
	}
	/**
	 * @return the salesAfterOperation
	 */
	public Float getSalesAfterOperation() {
		return salesAfterOperation;
	}
	/**
	 * @param salesAfterOperation the salesAfterOperation to set
	 */
	public void setSalesAfterOperation(Float salesAfterOperation) {
		this.salesAfterOperation = salesAfterOperation;
	}
	
	
	
}
