/**
*Author: Divya Srivastava
* Creation Date: 24-02-2017
* Copyright: Divya Srivastava
* Description: ProcessMessage
* 
*     ----------------------------------------------------------------------------------------------------------------
* Revision:  Version Last Revision Date   Name 					 Description 
*     ----------------------------------------------------------------------------------------------------------------
* @        1.0          24/02/2017       Divya Srivastava         MEssageProcessor will Process messages and display Sales for Producttypes
*
*/
package com.processmessage.util;
import com.processmessage.data.Message;
import com.processmessage.data.ProductSale;
import com.processmessage.data.Sale;
import com.processmessage.data.Adjustment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.processmessage.exceptions.InvalidMessageException;
public class MessageProcessor {

	
	List<Message> myMsgList;
	Document dom;
	List<Adjustment> adjustmentList;
	private Map<String,ProductSale> productRecords = new HashMap<String,ProductSale>();

	public MessageProcessor(){
		//create a list to hold the Message objects
		myMsgList = new ArrayList<Message>();
		adjustmentList = new ArrayList<Adjustment>();
	}

	public void runParser(String path) {
		
		parseXmlFile(path);
		parseMessages();
			
		
	}
	
	
	private void parseXmlFile(String path){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//parse using builder to get DOM representation of the XML file
			dom = db.parse(path);
			

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	
	private void parseMessages(){
		//get the root elememt
		Element docEle = dom.getDocumentElement();
		
		//get a nodelist of <Message> elements
		NodeList nl = docEle.getElementsByTagName("Message");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				
			 try{
					//get the Message element
					Element el = (Element)nl.item(i);
					
					//get the Message object
					Message m = getMessage(el);
					
					//add it to list
					myMsgList.add(m);
					
					//update productRecord List to keep track of product sales
					updateProductRecords(m);
					
					//After every 10th message read, log Total sales and Total Value 
					//corresponding to the Product
					if((i+1)%10==0) {
						logProductSalesAndValue();
					}
					
					//After reading 50 messages, report the Adjustments made
					if((i+1)%50 == 0) {
						System.out.println(" Message Processor is now pausing and stop accepting new messages. \n "
								+ "Report of adjustments that have been made to each sale while running are below: \n");
						logAdjustments();
						
						//exit MessageProcessor to stop reading rest of the Messages in XML file
						System.exit(0);
					}
			 }catch(InvalidMessageException e) {
				 System.out.println(e.getMessage());
			 }catch(NumberFormatException e) {
				 System.out.println(e.getMessage());
			 }catch(Exception e) {
				 System.out.println(e.getMessage());
			 }
			 
				
			}
		}
	}


	/**
	 * Take a Message element and read the values in, create
	 * an Message object and return it
	 * @param empEl
	 * @return
	 */
	private Message getMessage(Element msgElement) throws InvalidMessageException,NumberFormatException,Exception {
		
		String operation,type,productType;
		Message m = null;
		Float saleValue;
		int salesCount;
		
		type = msgElement.getAttribute("type");
		productType = getTextValue(msgElement,"productType").toUpperCase();
		saleValue = getFloatValue(msgElement,"saleValue");
		
		validateString(productType);
		validateMsgType(type);
		validateFloat(saleValue);
		
		
		Sale s  = new Sale();
		s.setProductType(productType);
		s.setSaleValue(saleValue);
		
		//populate Message on the basis of type 
		if (type.toUpperCase().equals("TYPE3")){
			operation = getTextValue(msgElement,"salesOperation");
			validateOperation(operation);
			m = new Message(type,s,operation);
		}
		else{
			salesCount = getIntValue(msgElement,"salesCount");
			validateInt(salesCount);
			s.setSalesCount(salesCount);
			m = new Message(type,s,null);
						
		}
		
		return m;
	}



	
/**
 * This will log total sales and total value for products after reading 10 messages
 */
private void logProductSalesAndValue(){
	for (Map.Entry<String,ProductSale> entry : productRecords.entrySet()) {
	    System.out.println("Product: " + entry.getKey() + ", Total Sales: " + entry.getValue().getTotCount()+" , Total Value: " + entry.getValue().getTotValue());
	}
	System.out.println("\n");
}

/**update productRecord List to keep track of product sales
 *  or perform operation if message type is Type3
 * @param msg
 */
private void updateProductRecords(Message msg){
	Sale sale = msg.getSale();
	String productType=sale.getProductType();
	int salesCnt = sale.getSalesCount();
	Float saleVal = sale.getSaleValue();
	String type = msg.getType().toUpperCase();
	
	if(type.equals("TYPE3"))
		performOperation(msg);
	else if( productRecords.containsKey(productType.toUpperCase())) {
		ProductSale ps = productRecords.get(productType);
		int totCount = ps.getTotCount();
		Float totValue= ps.getTotValue();
		ps.setTotValue(totValue + (saleVal*salesCnt) );
		ps.setTotCount(totCount + salesCnt);
		/*System.out.println("Existing Product: "+productType+"  ExistingtotCount:"
				+ ""+totCount+" Existingtotvalue:"+totValue+" Current salesCnt:"+salesCnt+" "
						+ "Current saleVal"+saleVal+" New totcount:(totCount + salesCnt):"+(totCount + salesCnt)+" "
								+ "and totvalue(totValue + (saleVal*salesCnt)):"+(totValue + (saleVal*salesCnt)));*/
		productRecords.put(productType,ps);
		
	} else{
		/*System.out.println("New Product"+productType+"  salesCnt:"+salesCnt+" and totvalue(saleVal*salesCnt):"+(saleVal*salesCnt));*/
		productRecords.put(productType,new ProductSale(salesCnt,(saleVal*salesCnt)));
		
	}
}
	
	/**
	 * This will perform operation-add,subtract,multiply on all the registered 
	 * sales for a producttype.Also will track totalsalesvalue for a product
	 * before and after applying the operation
	 * @param msg
	 */
	private void performOperation(Message msg) {
		ProductSale ps =  null;
		int totCount=0;
		Float totValue=0f,updatedValue=0f;
		
		Sale sale = msg.getSale();
		String productType=sale.getProductType();
		Float saleVal = sale.getSaleValue();
		String operation = msg.getSalesOperation().toUpperCase();
		if( productRecords.containsKey(productType.toUpperCase())) {
			ps = productRecords.get(productType);
			if(operation.equals("ADD")) {
				totValue= ps.getTotValue();
				totCount = ps.getTotCount();
				updatedValue = totValue+(totCount*saleVal);
				ps.setTotValue(updatedValue);
				/*System.out.println("Product: "+productType+" operation:"+operation+" totCount:"
						+ ""+totCount+" totvalue:"+totValue+" saleVal"+saleVal+" "
								+ "UpdatedValue(totValue+(totCount*saleVal))"+updatedValue);*/
				productRecords.put(productType,ps);
			}else if(operation.equals("SUBTRACT")) {
				totValue= ps.getTotValue();
				totCount = ps.getTotCount();
				updatedValue = totValue-(totCount*saleVal);
				ps.setTotValue(updatedValue);
				/*System.out.println("Product: "+productType+" operation:"+operation+" totCount:"
						+ ""+totCount+" totvalue:"+totValue+" saleVal"+saleVal+" "
								+ "UpdatedValue(totValue-(totCount*saleVal))"+updatedValue);*/
				productRecords.put(productType,ps);
			}else if(operation.equals("MULTIPLY")) {
				totValue= ps.getTotValue();
				totCount = ps.getTotCount();
				updatedValue = totValue*saleVal;
				ps.setTotValue(updatedValue);
				/*System.out.println("Product: "+productType+" operation:"+operation+" totCount:"
						+ ""+totCount+" totvalue:"+totValue+" saleVal"+saleVal+" "
								+ "UpdatedValue(totValue*saleVal)"+updatedValue);*/
				productRecords.put(productType,ps);
					
				
			}
			adjustmentList.add(new Adjustment(productType,operation,totValue,updatedValue));
		}
	}
	
	
	/**
	 * Log and report all the adjustments made to sales for a product type
	 * will display operation,producttype,sales before applying operation and 
	 * sales after applying operation
	 */
	private void logAdjustments() {

		Iterator itr = adjustmentList.iterator();
		
		while (itr.hasNext()) {
			Adjustment adj =(Adjustment) itr.next();
			System.out.println("Adjustment Operation: "+adj.getOperationApplied()+" ProductType: "+adj.getProductType()+""
					+ " Sales Before Adjustment: "+adj.getSalesBeforeOperation()+" Sales after adjustment: "+adj.getSalesAfterOperation());
		}
	}
	

	/**
	 * Take a xml element and the tag name, look for the tag and get
	 * the text content 
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName){
		String textVal = null;
		
			NodeList nl = ele.getElementsByTagName(tagName);
		
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		
		return textVal;
	}

	private void validateString(String str) throws InvalidMessageException {
		if(str == null)
			throw new InvalidMessageException("Value entered is null");
		else if(!(str.matches("[a-zA-Z]+"))){
			throw new InvalidMessageException("Value entered "+str+" is not a valid Productype");
		}
			
	}
	
	
	private void validateMsgType(String type) throws InvalidMessageException {
		if(type == null)
			throw new InvalidMessageException("Message Type is null");
		else if (!(type.equalsIgnoreCase("TYPE1")) && !(type.equalsIgnoreCase("TYPE2")) && !(type.equalsIgnoreCase("TYPE3")))
			throw new InvalidMessageException("Message Type is invalid");
	}
	
	private void validateOperation(String op) throws InvalidMessageException {
		if(op == null)
			throw new InvalidMessageException("Operation entered is null");
		else if (!(op.equalsIgnoreCase("ADD")) && !(op.equalsIgnoreCase("SUBTRACT")) && !(op.equalsIgnoreCase("MULTIPLY")))
			throw new InvalidMessageException("Operation entered is invalid");
	}
		
	private void validateFloat(Float var) throws InvalidMessageException {
		String var1 = new Float(var).toString();
		
		if(var == null)
			throw new InvalidMessageException("Value entered is null");
		else if(!(var1.matches("[+-]?([0-9]*[.])?[0-9]+"))){
			throw new InvalidMessageException("Value entered "+var1+" is not a valid Float");
		}
	}
	
	private void validateInt(int var) throws InvalidMessageException {
		String var1 = new Integer(var).toString();
		if(!(var1.matches("[0-9]+"))){
			throw new InvalidMessageException("Value entered "+var1+" is not a valid integer");
		}
	}
	
	
	
	/**
	 * Calls getIntValue and returns a int value
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private int getIntValue(Element ele, String tagName) throws NumberFormatException{
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}
	
	/**
	 * Calls getFloatValue and returns a Float value
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private Float getFloatValue(Element ele, String tagName) {
		return Float.valueOf(getTextValue(ele,tagName));
	}
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args){
		
		MessageProcessor mp = new MessageProcessor();
	String path ;
	//positive scenario
	path = "src/com/processmessage/resources/Message.xml";
	
	//negative scenario 1
	//path = "src/com/processmessage/resources/MessageNeg1.xml";
	
	//negative scenario 2
	//	path = "src/com/processmessage/resources/MessageNeg2.xml";
	
	//negative scenario 3
	//	path = "src/com/processmessage/resources/MessageNeg3.xml";
	
	//negative scenario 4
	//	path = "src/com/processmessage/resources/MessageNeg4.xml";
	
	//negative scenario 5
	//	path = "src/com/processmessage/resources/MessageNeg5.xml";
	
		mp.runParser(path);
	}

}