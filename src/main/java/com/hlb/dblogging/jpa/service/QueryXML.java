package com.hlb.dblogging.jpa.service;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hlb.dblogging.jpa.model.AuditDetail;
import com.hlb.dblogging.jpa.model.AuditMaster;
import com.hlb.dblogging.log.utility.ApplLogger;

public class QueryXML {
	
	public List mappingXMLToPojo(InputStream xmlData) {

		AuditMaster aMaster = new AuditMaster();
		AuditDetail aDetail = new AuditDetail();
		List<Object> auditComplete = new ArrayList<Object>();
		try {
			// standard for reading an XML file
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder;
			Document doc = null;
			// XPathExpression expr = null;
			builder = factory.newDocumentBuilder();
			// FileInputStream in=null;

			doc = builder.parse(xmlData);
			ApplLogger.getLogger().info("Parsing the formatted XML and map with POJOs....");
			XPathExpression xPathExpression = XPathFactory.newInstance().newXPath().compile("//XML");
			NodeList nodes = (NodeList) xPathExpression.evaluate(doc,XPathConstants.NODESET);

			// cast the result to a DOM NodeList
			NodeList nodes1 = (NodeList) nodes;
			// System.out.println("Test1 :"+nodes1.getLength());
			for (int i = 0; i < nodes1.getLength(); ++i) {
				final NodeList childList = ((Element) nodes1.item(i)).getChildNodes();
				for (int j = 0; j < childList.getLength(); ++j) {
					final Node node = childList.item(j);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String nodename = node.getNodeName();
						Object nodevalue = node.getTextContent();
						targetAuditMasterMapper(nodename, aMaster, nodevalue);
						targetAuditDetailMapper(nodename, aDetail, nodevalue);

					}
				}
			}
		}

		catch (Exception e) {
			ApplLogger.getLogger().error("Error while mapping the xml data : ",e);
		}
		auditComplete.add(aMaster);
		auditComplete.add(aDetail);
		ApplLogger.getLogger().info("Successfully mapped xml to pojo and returning the list of size : "+auditComplete.size());
		return auditComplete;
  }
  
  private void targetAuditDetailMapper(String nodename, AuditDetail aDetail,Object value) {
	  
	  switch(nodename){
	  
	  case "UniqueProcessID":aDetail.setUniqueProcessID(value.toString());
	  break;
	  case "MessageID":aDetail.setMessageID(value.toString());
	  break;
	  case "Content":aDetail.setContent(value.toString());
	  break;
	  case "MessageType":aDetail.setMessageType(value.toString());
	  break;
	  case "CreatedDateTime":{ DateFormat df = new SimpleDateFormat("yyMMdd");
							   try {
								Date createdDate = df.parse((String) value);
								aDetail.setCreatedDateTime(createdDate);
							   }catch(ParseException e) {
								ApplLogger.getLogger().error("Error while converting string date to util date",e);
							   }
							}
	  break;
	  case "UpdatedDateTime":{ DateFormat df = new SimpleDateFormat("yyMMdd");
							   try {
								Date updatedDate = df.parse((String) value);
								aDetail.setUpdatedDateTime(updatedDate);
							   }catch(ParseException e) {
								ApplLogger.getLogger().error("Error while converting string date to util date",e);
							   }
							}
	  break;
	  
	  }
	  
	
}

private void targetAuditMasterMapper(String nodeName, AuditMaster aMaster,Object value) {
  	
	switch(nodeName){  
  	case "LogInterface":aMaster.setLogInterface(value.toString());
	break;
  	case "MessageFormat":aMaster.setMessageFormat(value.toString());
	break;
  	case "Host":aMaster.setHost(value.toString());
	break;
  	case "TransType":aMaster.setTransType(value.toString());
	break;
  	case "MessageID":aMaster.setMessageID(value.toString());
	break;
  	case "ServiceID":aMaster.setServiceID(value.toString());
	break;
  	case "ApplName":aMaster.setApplName(value.toString());
	break;
  	case "ApplID":aMaster.setAppID(value.toString());
	break;
  	case "UniqueProcessID":aMaster.setUniqueProcessID(value.toString());
	break;
  	case "JournalSeq":aMaster.setJournalSeq(Integer.valueOf(value.toString()));
	break;
  	case "LogLevel":aMaster.setLogLevel(value.toString());
	break;
  	case "StatusCode":aMaster.setStatusCode(value.toString());
	break;
  	case "ErrorCode":aMaster.setErrorCode(value.toString());
	break;
  	case "ErrorMessage":aMaster.setErrorMessage(value.toString());
	break;
  	case "ReasonCode":aMaster.setReasonCode(value.toString());
	break;
  	case "TransDateTime":{ DateFormat df = new SimpleDateFormat("yyMMdd");
  						   try {
							Date startDate = df.parse((String) value);
							aMaster.setTransDateTime(startDate);
  						   }catch (ParseException e) {
							ApplLogger.getLogger().error("Error while converting string date to util date",e);
  						   }
  						}
	break;
  	case "UpdatedDateTime":{ DateFormat df = new SimpleDateFormat("yyMMdd");
						   try {
							Date updatedDate = df.parse((String) value);
							aMaster.setUpdatedDateTime(updatedDate);
						   }catch(ParseException e) {
							ApplLogger.getLogger().error("Error while converting string date to util date",e);
						   }
						}
	break;
  	case "CreatedDateTime":{ DateFormat df = new SimpleDateFormat("yyMMdd");
						   try {
							Date createdDate = df.parse((String) value);
							aMaster.setCreatedDateTime(createdDate);
						   }catch(ParseException e) {
							ApplLogger.getLogger().error("Error while converting string date to util date",e);
						   }
						}
	break;
  	case "CreatedBy":aMaster.setCreatedBy(value.toString());
	break;
  	case "UpdatedBy":aMaster.setUpdatedBy(value.toString());
	break;
  	case "CustomString1":aMaster.setCustomString1(value.toString());
	break;
  	case "CustomString2":aMaster.setCustomString2(value.toString());
	break;
  	case "CustomString3":aMaster.setCustomString3(value.toString());
	break;
  	case "CustomInt1":aMaster.setCustomInt1(Integer.valueOf(value.toString()));
	break;
	case "CustomInt2":aMaster.setCustomInt2(Integer.valueOf(value.toString()));
	break;
	case "CustomInt3":aMaster.setCustomInt3(Integer.valueOf(value.toString()));
	break;
	case "CustomDate1":aMaster.setCustomDate1((Date)value);
	break;
	case "CustomDate2":aMaster.setCustomDate1((Date)value);
	break;
	
	default:
		break;
	
	}
	
}
@SuppressWarnings("unused")
private java.sql.Clob stringToClob(String source)
{
    try
    {
        return new javax.sql.rowset.serial.SerialClob(source.toCharArray());
    }
    catch (Exception e)
    {
        System.out.println("Could not convert string to a CLOB :"+e );
        return null;
    }
}


public boolean checkWhetherContentIsXml(ByteArrayInputStream inputStream){
	try {
		// standard for reading an XML file
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		// XPathExpression expr = null;
		builder = factory.newDocumentBuilder();
		// FileInputStream in=null;

		doc = builder.parse(inputStream);
		ApplLogger.getLogger().info("Parsing the formatted XML and map with POJOs....");
		XPathExpression xPathExpression = XPathFactory.newInstance().newXPath().compile("//MessageFormat");
		NodeList nodes = (NodeList) xPathExpression.evaluate(doc,XPathConstants.NODESET);
		Node  node =	nodes.item(0);
		if(node!=null && "XML".equalsIgnoreCase(node.getTextContent().trim()) ){
			ApplLogger.getLogger().info("Message Format is : "+node.getTextContent());
			return true;
		}
		
	}

	catch (Exception e) {
		ApplLogger.getLogger().error("Error while mapping the xml data : ",e);
	}
	return false;
}


public String getLogLevelMessage(InputStream inputStream){
	try {
		// standard for reading an XML file
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		// XPathExpression expr = null;
		builder = factory.newDocumentBuilder();
		// FileInputStream in=null;

		doc = builder.parse(inputStream);
		ApplLogger.getLogger().info("Parsing the formatted XML and finding whether the LogLevel property matches or not");
		XPathExpression xPathExpression = XPathFactory.newInstance().newXPath().compile("//LogLevel");
		NodeList nodes = (NodeList) xPathExpression.evaluate(doc,XPathConstants.NODESET);
		Node  node =	nodes.item(0);
		if(node!=null && node.getTextContent()!=null && !node.getTextContent().isEmpty() ){
			ApplLogger.getLogger().info("LogLevel property value from the Quere message is  : "+node.getTextContent());
			return node.getTextContent().trim();
		}
	}catch (Exception e) {
		ApplLogger.getLogger().error("Error while mapping the xml data to find LogLevel of message: ",e);
	}
	return null;
}


public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, FileNotFoundException {
	QueryXML process = new QueryXML();
	File   file = new File("C:\\Users\\Lakshminarayana\\Desktop\\sample.xml");
	FileInputStream in =new FileInputStream(file);
	/*@SuppressWarnings("rawtypes")
	List a1= process.mappingXMLToPojo(in);
	System.out.println(a1.get(0));
	System.out.println(a1.get(1));*/
	
	System.out.println("=======================");
	process.getLogLevelMessage(in);
}
}